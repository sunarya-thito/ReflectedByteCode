package thito.reflectedbytecode.ext.transform;

import thito.reflectedbytecode.*;
import thito.reflectedbytecode.jvm.*;

public class ByteTransformHandler extends AbstractNumberTransformHandler {

    public ByteTransformHandler() {
        super(Byte.class, byte.class);
    }

    @Override
    protected Reference cast(Object other) {
        return Java.Math.toByte(other);
    }

    @Override
    protected Number getDefaultValue() {
        return Byte.valueOf((byte) 0);
    }
}
