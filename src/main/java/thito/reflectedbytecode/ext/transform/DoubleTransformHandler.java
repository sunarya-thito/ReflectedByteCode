package thito.reflectedbytecode.ext.transform;

import thito.reflectedbytecode.*;
import thito.reflectedbytecode.jvm.*;

public class DoubleTransformHandler extends AbstractNumberTransformHandler {

    public DoubleTransformHandler() {
        super(Double.class, double.class);
    }

    @Override
    protected Reference cast(Object other) {
        return Java.Math.toDouble(other);
    }

    @Override
    protected Number getDefaultValue() {
        return Double.valueOf(0);
    }
}
