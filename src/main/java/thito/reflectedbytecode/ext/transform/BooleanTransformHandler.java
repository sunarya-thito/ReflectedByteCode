package thito.reflectedbytecode.ext.transform;

import thito.reflectedbytecode.ext.*;
import thito.reflectedbytecode.jvm.*;

public class BooleanTransformHandler extends ObjectTransformHandler {
    @Override
    protected boolean canHandle() {
        Class<?> source = getSourceType();
        return source == Boolean.class || source == boolean.class || source == String.class;
    }

    @Override
    protected void handleTransformation() {
        Class<?> source = getSourceType();
        Class<?> target = getTargetType();
        if (target == Boolean.class) {
            if (source == boolean.class) {
                setValue(Java.Class(Boolean.class).getMethod("valueOf", boolean.class).get()
                .invoke(null, getValue()));
            } else if (source == String.class) {
                setValue(Java.Class(Boolean.class).getMethod("valueOf", String.class).get()
                        .invoke(null, getValue()));
            }
        } else if (target == boolean.class) {
            if (source == Boolean.class) {
                setValue(Java.Class(Boolean.class).getMethod("booleanValue").get()
                .invoke(getValue()));
            } else if (source == String.class) {
                setValue(Java.Class(Boolean.class).getMethod("parseBoolean", String.class).get()
                .invoke(null, getValue()));
            }
        }
    }
}
