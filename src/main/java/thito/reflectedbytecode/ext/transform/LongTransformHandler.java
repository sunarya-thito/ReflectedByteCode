package thito.reflectedbytecode.ext.transform;

import thito.reflectedbytecode.*;
import thito.reflectedbytecode.jvm.*;

public class LongTransformHandler extends AbstractNumberTransformHandler {

    public LongTransformHandler() {
        super(Long.class, long.class);
    }

    @Override
    protected Reference cast(Object other) {
        return Java.Math.toLong(other);
    }

    @Override
    protected Number getDefaultValue() {
        return Long.valueOf(0);
    }
}
