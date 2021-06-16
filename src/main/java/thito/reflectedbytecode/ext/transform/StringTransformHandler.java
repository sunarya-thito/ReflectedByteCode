package thito.reflectedbytecode.ext.transform;

import thito.reflectedbytecode.ext.*;
import thito.reflectedbytecode.jvm.*;

public class StringTransformHandler extends ObjectTransformHandler {

    @Override
    protected boolean canHandle() {
        return getSourceType() != String.class && getTargetType() == String.class;
    }

    @Override
    protected void handleTransformation() {
        setValue(Java.Class(String.class).getMethod("valueOf", Object.class).get()
                .invoke(null, getValue()));
    }
}
