package thito.reflectedbytecode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ContextProxy implements InvocationHandler {
    private Context context;
    private Object reference;

    public ContextProxy(Context context, Object reference) {
        this.context = context;
        this.reference = reference;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> clazz = method.getDeclaringClass();
        Alias alias = clazz.getAnnotation(Alias.class);
        if (alias != null) clazz = alias.value();
        IClass iClass = IClass.fromClass(clazz);
        FieldAlias fieldAlias = method.getAnnotation(FieldAlias.class);
        if (fieldAlias != null) {
            IField iField = iClass.getDeclaredField(fieldAlias.value()).get();
            return context.createProxy(method.getReturnType(), iField.get(reference));
        }
        IMethod iMethod = iClass.getDeclaredMethod(method.getName(), method.getParameterTypes()).get();
        return iMethod.invoke(reference, args);
    }
}
