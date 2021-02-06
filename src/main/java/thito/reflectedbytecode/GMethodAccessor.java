package thito.reflectedbytecode;

import java.lang.reflect.Modifier;

public class GMethodAccessor extends BodyAccessor {
    private GMethod method;

    public GMethodAccessor(GMethod method) {
        this.method = method;
    }

    public IClass getSuperClass() {
        return method.getDeclaringClass().getSuperClass();
    }

    public LField createVariable() {
        return method.createLocalField();
    }

    public PField getParameter(int index) {
        return method.getArgument(index);
    }

    public <T extends Reference> T getArgument(int index) {
        return getParameter(index).get();
    }

    @Override
    protected boolean hasInstance() {
        return !Modifier.isStatic(method.getModifiers());
    }
}
