package thito.reflectedbytecode.ext.transform;

import thito.reflectedbytecode.*;
import thito.reflectedbytecode.jvm.*;

public class FloatTransformHandler extends AbstractNumberTransformHandler {

    public FloatTransformHandler() {
        super(Float.class, float.class);
    }

    @Override
    protected Reference cast(Object other) {
        return Java.Math.toFloat(other);
    }

    @Override
    protected Number getDefaultValue() {
        return Float.valueOf(0);
    }
}
