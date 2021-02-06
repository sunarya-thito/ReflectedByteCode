package thito.reflectedbytecode;

import java.lang.reflect.Field;

public class KField extends AbstractField implements IField {

    private Field field;
    private IClass type, declaringClass;

    public KField(Field field) {
        this.field = field;
    }

    @Override
    public IClass getType() {
        return type == null ? type = IClass.fromClass(field.getType()) : type;
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public int getModifiers() {
        return field.getModifiers();
    }

    @Override
    public IClass getDeclaringClass() {
        return declaringClass == null ? declaringClass = IClass.fromClass(field.getDeclaringClass()) : declaringClass;
    }

}
