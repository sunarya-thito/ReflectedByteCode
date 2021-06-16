package thito.reflectedbytecode;

import org.objectweb.asm.Opcodes;

import java.lang.reflect.*;

public abstract class BodyAccessor extends Reference {

    public BodyAccessor(Type type) {
        super(type);
    }

    BodyAccessor parent;

    protected BodyAccessor getParent() {
        return parent;
    }

    void beginScope() {

    }

    @Override
    public void write() {
        throw new UnsupportedOperationException();
    }

    void closeScope() {

    }

}
