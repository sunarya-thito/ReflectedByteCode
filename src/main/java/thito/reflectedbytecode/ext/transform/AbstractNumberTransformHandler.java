package thito.reflectedbytecode.ext.transform;

import thito.reflectedbytecode.*;
import thito.reflectedbytecode.ext.*;
import thito.reflectedbytecode.jvm.*;

public abstract class AbstractNumberTransformHandler extends ObjectTransformHandler {
    private Class<?> type;
    private Class<?> primitiveType;
    private String primitiveMethodName;

    public AbstractNumberTransformHandler(Class<?> type, Class<?> primitiveType) {
        this.type = type;
        this.primitiveType = primitiveType;
        this.primitiveMethodName = primitiveType.getName()+"Value";
    }

    protected abstract Number getDefaultValue();

    protected abstract Reference cast(Object other);

    protected boolean acceptPrimitive(Class<?> source) {
        return source == primitiveType;
    }

    protected boolean accept(Class<?> source) {
        return source == type;
    }

    @Override
    protected boolean canHandle() {
        Class<?> source = getSourceType();
        return acceptPrimitive(source) ||
                accept(source) ||
                source == String.class ||
                source == Character.class ||
                source == char.class ||
                source.isPrimitive() ||
                Number.class.isAssignableFrom(source);
    }

    @Override
    protected void handleTransformation() {
        Class<?> source = getSourceType();
        Class<?> target = getTargetType();
        boolean isPrimitiveNumber = source.isPrimitive();
        if (accept(target)) {
            if (acceptPrimitive(source)) {
                setValue(Java.Class(type).getMethod("valueOf", primitiveType).get()
                        .invoke(null, getValue()));
            } else if (source == Character.class) {
                    setValue(Java.Class(type).getMethod("valueOf", primitiveType).get()
                            .invoke(null, cast(Java.Class(Character.class).getMethod("charValue").get()
                                    .invoke(getValue()))));
            } else if (source == char.class) {
                setValue(Java.Class(type).getMethod("valueOf", primitiveType).get()
                        .invoke(null, cast(getValue())));
            } else if (isPrimitiveNumber) {
                setValue(Java.Class(type).getMethod("valueOf", primitiveType).get()
                        .invoke(null, cast(getValue())));
            } else if (Number.class.isAssignableFrom(source)) {
                setValue(Java.Class(type).getMethod("valueOf", primitiveType).get()
                        .invoke(null, Java.Class(Number.class).getMethod(primitiveMethodName).get()
                                .invoke(getValue())));
            }
        } else if (acceptPrimitive(target)) {
            if (accept(source)) {
                setValue(Java.Class(Number.class).getMethod(primitiveMethodName).get()
                        .invoke(getValue()));
            }  else if (source == Character.class) {
                setValue(cast(Java.Class(Character.class).getMethod("charValue").get()
                        .invoke(getValue())));
            } else if (source == char.class) {
                setValue(cast(getValue()));
            } else if (isPrimitiveNumber) {
                setValue(cast(getValue()));
            } else if (Number.class.isAssignableFrom(source)) {
                System.out.println("casting source");
                setValue(Java.Class(Number.class).getMethod(primitiveMethodName).get()
                        .invoke(getValue()));
            } else {
                System.out.println("from: "+source+" to "+target);
            }
        }
    }
}
