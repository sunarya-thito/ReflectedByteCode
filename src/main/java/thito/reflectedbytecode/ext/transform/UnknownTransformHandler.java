package thito.reflectedbytecode.ext.transform;

import thito.reflectedbytecode.ext.*;
import thito.reflectedbytecode.jvm.*;

public class UnknownTransformHandler extends ObjectTransformHandler {
    @Override
    protected boolean canHandle() {
        return getSourceType().isPrimitive();
    }

    @Override
    protected void handleTransformation() {
        Class<?> source = getSourceType();
        if (source == int.class) {
            setValue(Java.Class(Integer.class).getMethod("valueOf", int.class).get()
            .invoke(null, getValue()));
        } else if (source == double.class) {
            setValue(Java.Class(Double.class).getMethod("valueOf", double.class).get()
                    .invoke(null, getValue()));
        } else if (source == long.class) {
            setValue(Java.Class(Long.class).getMethod("valueOf", long.class).get()
                    .invoke(null, getValue()));
        } else if (source == float.class) {
            setValue(Java.Class(Float.class).getMethod("valueOf", float.class).get()
                    .invoke(null, getValue()));
        } else if (source == short.class) {
            setValue(Java.Class(Short.class).getMethod("valueOf", short.class).get()
                    .invoke(null, getValue()));
        } else if (source == byte.class) {
            setValue(Java.Class(Byte.class).getMethod("valueOf", byte.class).get()
                    .invoke(null, getValue()));
        } else if (source == boolean.class) {
            setValue(Java.Class(Boolean.class).getMethod("valueOf", boolean.class).get()
                    .invoke(null, getValue()));
        } else if (source == char.class) {
            setValue(Java.Class(Character.class).getMethod("valueOf", char.class).get()
                    .invoke(null, getValue()));
        }
    }
}
