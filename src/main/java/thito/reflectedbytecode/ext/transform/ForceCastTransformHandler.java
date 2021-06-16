package thito.reflectedbytecode.ext.transform;

import thito.reflectedbytecode.ext.*;
import thito.reflectedbytecode.jvm.*;

public class ForceCastTransformHandler extends ObjectTransformHandler {
    @Override
    protected void handleTransformation() {
        setValue(Java.Cast(getValue(), getTargetType()));
    }

    @Override
    protected boolean canHandle() {
        return true;
    }
}
