package thito.reflectedbytecode;

import org.objectweb.asm.Opcodes;
import thito.reflectedbytecode.jvm.Java;

public interface Reference {
    static void handleWrite(Object object) {
        if (object instanceof Reference) {
            ((Reference) object).write();
            return;
        }
        if (object != null) {
            Code.getCode().getCodeVisitor().visitLdcInsn(object);
        } else {
            Code.getCode().getCodeVisitor().visitInsn(Opcodes.ACONST_NULL);
        }
    }
    void write();
    default Reference ToString() {
        return () -> {
            IClass.fromClass(String.class).getMethod("toString").ifPresent(toString -> {
                toString.invoke(this);
            });
        };
    }
    default ArrayReference asArray() {
        if (this instanceof ArrayReference) return (ArrayReference) this;
        throw new UnsupportedOperationException("not an array reference");
    }
}
