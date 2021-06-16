package thito.reflectedbytecode.ext.transform;

import thito.reflectedbytecode.*;
import thito.reflectedbytecode.jvm.*;

public class ShortTransformHandler extends AbstractNumberTransformHandler {

    public ShortTransformHandler() {
        super(Short.class, short.class);
    }

    @Override
    protected Reference cast(Object other) {
        return Java.Math.toShort(other);
    }

    @Override
    protected Number getDefaultValue() {
        return Short.valueOf((short) 0);
    }
}
