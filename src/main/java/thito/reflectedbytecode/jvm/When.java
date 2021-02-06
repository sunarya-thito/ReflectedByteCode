package thito.reflectedbytecode.jvm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import thito.reflectedbytecode.Code;
import thito.reflectedbytecode.Reference;

public class When {
    Condition jump(int opcode) {
        Code code = Code.getCode();
        Label markIfFalse = new Label();
        MethodVisitor visitor = code.getCodeVisitor();
        // this is a reversed logic WTF
        visitor.visitJumpInsn(opcode, markIfFalse);
        // it will pass to the next stack only if its true
        // and will go to the specified Label only if its false
        return new Condition(markIfFalse);
    }
    Condition jump(int opcode, Object reference) {
        Reference.handleWrite(reference);
        Code code = Code.getCode();
        Label markIfFalse = new Label();
        MethodVisitor visitor = code.getCodeVisitor();
        // this is a reversed logic WTF
        visitor.visitJumpInsn(opcode, markIfFalse);
        // it will pass to the next stack only if its true
        // and will go to the specified Label only if its false
        return new Condition(markIfFalse);
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
        return jump(Opcodes.IF_ICMPLT, comparable);
    }
    public Condition isLessThan(Object comparable) {
        return jump(Opcodes.IF_ICMPGT, comparable);
    }
    public Condition isEqualsTo(Object comparable) {
        return jump(Opcodes.IF_ICMPNE, comparable);
    }
    public Condition isNotEqualsTo(Object comparable) {
        return jump(Opcodes.IF_ICMPEQ, comparable);
    }
    public Condition isGreaterOrEqualsTo(Object comparable) {
        return jump(Opcodes.IF_ICMPLE, comparable);
    }
    public Condition isLessOrEqualsTo(Object comparable) {
        return jump(Opcodes.IF_ICMPGE, comparable);
    }
}
