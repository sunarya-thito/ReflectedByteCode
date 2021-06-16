package thito.reflectedbytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

public class GConstructor extends AbstractConstructor implements GMember, BodyOwner {

    protected PField getArgument(int index) {
        return parameters[index];
    }

    private int modifiers = Modifier.PUBLIC;
    private GClass declaringClass;
    private PField[] parameters;
    private Body<GConstructorAccessor> body;
    private Type[] Throws;
    private Map<Type, GAnnotation<GConstructor>> annotationMap = new HashMap<>();
    private List<Runnable> compileTask = new ArrayList<>();

    GConstructor(GClass declaringClass) {
        this.declaringClass = declaringClass;
    }

    public GAnnotation<GConstructor> annotate(Type type) {
        return annotationMap.computeIfAbsent(type, x -> new GAnnotation<>(this));
    }

    public void putPostCompileTask(Runnable postTask) {
        compileTask.add(postTask);
    }

    @Override
    public void putPreCompileTask(Runnable task) {
        compileTask.add(0, task);
    }

    @Override
    public void executeCompileTask() {
        compileTask.forEach(Runnable::run);
        compileTask.clear();
    }

    protected Map<Type, GAnnotation<GConstructor>> getAnnotationMap() {
        return annotationMap;
    }

    public GConstructor body(Body<GConstructorAccessor> body) {
        this.body = body;
        return this;
    }

    Body<GConstructorAccessor> getBody() {
        return body;
    }
    
    public GConstructor parameters(Type... types) {
        return _parameters(Arrays.stream(types).map(x -> new PField(IClass.fromClass(x), this)).toArray(PField[]::new));
    }

    private GConstructor _parameters(PField... parameters) {
        this.parameters = parameters;
        for (int i = 0; i < parameters.length; i++) {
            parameters[i].localIndex = i;
        }
        return this;
    }

    public GConstructor exceptionThrows(Type...exceptions) {
        Throws = exceptions;
        return this;
    }

    void writeBody() {
        if (body != null) {
            body.declareBody(new GConstructorAccessor(this));
        }
    }

    @Override
    public GModifierConstructor<GConstructor> modifier() {
        return new GModifierConstructor<>(this);
    }

    @Override
    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public GClass getDeclaringClass() {
        return declaringClass;
    }

    @Override
    public IClass[] getParameterTypes() {
        return parameters == null ? new IClass[0] : Arrays.stream(parameters).map(PField::getType).toArray(IClass[]::new);
    }

    @Override
    public int getParameterCount() {
        return parameters == null ? 0 : parameters.length;
    }

    void invoke(Object... args) {
        Code code = Code.getCode();
        MethodVisitor visitor = code.getCodeVisitor();
        visitor.visitVarInsn(Opcodes.ALOAD, 0);
        for (int i = 0; i < args.length; i++) {
            Reference.handleWrite(parameters[i].getType(), args[i]);
        }
        visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, getDeclaringClass().getRawName(), "<init>", "()V", false);
    }

    @Override
    public Type[] getThrows() {
        return Throws == null ? new Type[0] : Throws;
    }
}
