package thito.reflectedbytecode;

import java.lang.reflect.Modifier;

public class GConstructorAccessor extends BodyAccessor {
    private GConstructor constructor;

    public GConstructorAccessor(GConstructor constructor) {
        this.constructor = constructor;
    }

    public IClass getSuperClass() {
        return constructor.getDeclaringClass().getSuperClass();
    }

    public LField createVariable() {
        return constructor.createLocalField();
    }

    public PField getParameter(int index) {
        return constructor.getArgument(index);
    }

    public <T extends ArrayReference> T getArgument(int index) {
        return getParameter(index).get();
    }

    public void constructDefault() {
        IClass clazz = getSuperClass();
        clazz.getConstructor().ifPresent(constructor -> {
            ((AbstractConstructor) constructor).invoke();
        });
    }

    @Override
    protected boolean hasInstance() {
        return !Modifier.isStatic(constructor.getModifiers());
    }
}
