package thito.reflectedbytecode;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GField extends AbstractField implements GMember {
    private IClass type;
    private String name;
    private GClass declaringClass;
    private int modifiers = Modifier.PRIVATE;
    private Object constantValue;
    private Map<Type, GAnnotation<GField>> annotationMap = new HashMap<>();

    GField(GClass declaringClass, String name, IClass type) {
        this.declaringClass = declaringClass;
        this.name = name;
        this.type = type;
    }

    public GAnnotation<GField> annotate(Type type) {
        return annotationMap.computeIfAbsent(type, x -> new GAnnotation<>(this));
    }

    protected Map<Type, GAnnotation<GField>> getAnnotationMap() {
        return annotationMap;
    }

    @Override
    public IClass getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
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
    public GModifierField<GField> modifier() {
        return new GModifierField<>(this);
    }

    @Override
    public void setModifiers(int modifiers) {
        if (!Modifier.isStatic(modifiers) && constantValue != null) throw new UnsupportedOperationException("cannot set modifier to non-static because there is a constant value already");
        this.modifiers = modifiers;
    }

    public GField initialValue(Object value) {
        if (!Modifier.isStatic(modifiers) && value != null) throw new UnsupportedOperationException("constant value only for static methods");
        this.constantValue = value;
        return this;
    }

    Object getConstantValue() {
        return constantValue;
    }
}

