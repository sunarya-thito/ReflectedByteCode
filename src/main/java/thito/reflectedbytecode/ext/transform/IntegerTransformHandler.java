package thito.reflectedbytecode.ext.transform;

import thito.reflectedbytecode.*;
import thito.reflectedbytecode.jvm.*;

public class IntegerTransformHandler extends AbstractNumberTransformHandler {

    public IntegerTransformHandler() {
        super(Integer.class, int.class);
    }

    @Override
    protected Reference cast(Object other) {
        return Java.Math.toInt(other);
    }

    @Override
    protected Number getDefaultValue() {
        return Integer.valueOf(0);
    }
}
