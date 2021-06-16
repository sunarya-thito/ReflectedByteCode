package thito.reflectedbytecode.ext.transform;

import thito.reflectedbytecode.ext.*;
import thito.reflectedbytecode.jvm.*;

public class CharacterTransformHandler extends ObjectTransformHandler {
    @Override
    protected boolean canHandle() {
        Class<?> source = getSourceType();
        return source == Character.class || source == char.class;
    }

    @Override
    protected void handleTransformation() {
        Class<?> source = getSourceType();
        Class<?> target = getTargetType();
        if (target == char.class) {
            if (source == Character.class) {
                setValue(Java.Class(Character.class).getMethod("charValue").get()
                .invoke(getValue()));
            }
        } else if (target == Character.class) {
            if (source == char.class) {
                setValue(Java.Class(Character.class).getMethod("valueOf", char.class).get()
                .invoke(null, getValue()));
            }
        }
    }
}
