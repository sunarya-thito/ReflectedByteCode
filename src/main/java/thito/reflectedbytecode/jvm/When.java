package thito.reflectedbytecode.jvm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import thito.reflectedbytecode.*;

import java.lang.reflect.*;

public class When {
    Condition jump(int opcode) {
        return new Condition(this, opcode, null);
    }
    Condition jump(int opcode, Object reference) {
        return new Condition(this, opcode, reference);
    }

    Object reference;
    Type hostType;

    public When(Object reference) {
        this.reference = reference;
        this.hostType = ASMHelper.GetType(reference);
    }

    public Condition isTrue() {
        return jump(Opcodes.IFEQ);
    }
    public Condition isFalse() {
        return jump(Opcodes.IFNE);
    }
    public Condition isNull() {
        return jump(Opcodes.IFNULL);
    }
    public Condition isNotNull() {
        return jump(Opcodes.IFNONNULL);
    }
    // this is so fking weird
    // everything is reversed
    // ICMPLT which stands for "int compare less than" works
    // as "int compare greater than" weird huh?
    public Condition isGreaterThan(Object comparable) {
        hostType = ASMHelper.GetHigherType(ASMHelper.FindRealType(ASMHelper.GetType(comparable)), ASMHelper.FindRealType(hostType));
        return jump(Opcodes.IF_ICMPLE, comparable);
    }
    public Condition isLessThan(Object comparable) {
        hostType = ASMHelper.GetHigherType(ASMHelper.FindRealType(ASMHelper.GetType(comparable)), ASMHelper.FindRealType(hostType));
        return jump(Opcodes.IF_ICMPGE, comparable);
    }
    public Condition isEqualsTo(Object comparable) {
        hostType = ASMHelper.GetHigherType(ASMHelper.FindRealType(ASMHelper.GetType(comparable)), ASMHelper.FindRealType(hostType));
        return jump(Opcodes.IF_ICMPNE, comparable);
    }
    public Condition isNotEqualsTo(Object comparable) {
        hostType = ASMHelper.GetHigherType(ASMHelper.FindRealType(ASMHelper.GetType(comparable)), ASMHelper.FindRealType(hostType));
        return jump(Opcodes.IF_ICMPEQ, comparable);
    }
    public Condition isGreaterOrEqualsTo(Object comparable) {
        hostType = ASMHelper.GetHigherType(ASMHelper.FindRealType(ASMHelper.GetType(comparable)), ASMHelper.FindRealType(hostType));
        return jump(Opcodes.IF_ICMPLT, comparable);
    }
    public Condition isLessOrEqualsTo(Object comparable) {
        hostType = ASMHelper.GetHigherType(ASMHelper.FindRealType(ASMHelper.GetType(comparable)), ASMHelper.FindRealType(hostType));
        return jump(Opcodes.IF_ICMPGT, comparable);
    }
}
