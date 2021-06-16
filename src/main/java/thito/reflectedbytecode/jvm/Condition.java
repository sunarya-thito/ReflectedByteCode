package thito.reflectedbytecode.jvm;

import org.objectweb.asm.*;
import thito.reflectedbytecode.*;

import java.sql.*;

public class Condition {

    private static int getJumpCode(String name, int opcode) {
        return opcode;
    }
    private Runnable trueBody, falseBody;
    private int opcode;
    private Object reference;
    private When when;
    private Label markIfFalse;

    public Condition(When when, int opcode, Object reference) {
        this.when = when;
        this.opcode = opcode;
        this.reference = reference;
    }

    private void compile() {
        MethodVisitor visitor = Code.getCode().getCodeVisitor();
        int opcode = this.opcode;
        if (opcode == Opcodes.IFEQ || opcode == Opcodes.IFNE) {
            Reference.handleWrite(boolean.class, when.reference);
        } else if (opcode == Opcodes.IFNULL || opcode == Opcodes.IFNONNULL) {
            Reference.handleWrite(Object.class, when.reference);
        } else {
            boolean convert = false;
            boolean compareLess = opcode == Opcodes.IF_ICMPEQ || opcode == Opcodes.IF_ICMPNE || opcode == Opcodes.IF_ICMPLT || opcode == Opcodes.IF_ICMPLE;
            if (when.hostType != null) {
                if ("double".equals(when.hostType.getTypeName()) || "java.lang.Double".equals(when.hostType.getTypeName())) {
                    Reference.handleWrite(double.class, when.reference);
                    Reference.handleWrite(double.class, reference);
                    if (compareLess) {
                        visitor.visitInsn(Opcodes.DCMPL);
                    } else if (opcode == Opcodes.IF_ICMPGE || opcode == Opcodes.IF_ICMPGT) {
                        visitor.visitInsn(Opcodes.DCMPG);
                    }
                    convert = true;
                } else if ("long".equals(when.hostType.getTypeName()) || "java.lang.Long".equals(when.hostType.getTypeName())) {
                    Reference.handleWrite(long.class, when.reference);
                    Reference.handleWrite(long.class, reference);
                    visitor.visitInsn(Opcodes.LCMP);
                    convert = true;
                } else if ("float".equals(when.hostType.getTypeName()) || "java.lang.Float".equals(when.hostType.getTypeName())) {
                    Reference.handleWrite(float.class, when.reference);
                    Reference.handleWrite(float.class, reference);
                    if (compareLess) {
                        visitor.visitInsn(Opcodes.FCMPL);
                    } else if (opcode == Opcodes.IF_ICMPGE || opcode == Opcodes.IF_ICMPGT) {
                        visitor.visitInsn(Opcodes.FCMPG);
                    }
                    convert = true;
                } else if ("int,java.lang.Integer,byte,java.lang.Byte,short,java.lang.Short".contains(when.hostType.getTypeName())) {
                    Reference.handleWrite(int.class, when.reference);
                    Reference.handleWrite(int.class, reference);
                } else {
                    Reference.handleWrite(Object.class, when.reference);
                    Reference.handleWrite(Object.class, reference);
                    if (opcode == Opcodes.IF_ICMPNE) {
                        opcode = Opcodes.IF_ACMPNE;
                    } else if (opcode == Opcodes.IF_ICMPEQ) {
                        opcode = Opcodes.IF_ACMPEQ;
                    } else throw new UnsupportedOperationException(when.hostType.getTypeName());
                }
            } else {
                Reference.handleWrite(Object.class, when.reference);
                Reference.handleWrite(Object.class, reference);
                if (opcode == Opcodes.IF_ICMPNE) {
                    opcode = Opcodes.IF_ACMPNE;
                } else if (opcode == Opcodes.IF_ICMPEQ) {
                    opcode = Opcodes.IF_ACMPEQ;
                } else throw new UnsupportedOperationException(when.hostType.getTypeName());
            }
            if (convert) {
                if (opcode == Opcodes.IF_ICMPEQ) {
                    opcode = Opcodes.IFEQ;
                } else if (opcode == Opcodes.IF_ICMPNE) {
                    opcode = Opcodes.IFNE;
                } else if (opcode == Opcodes.IF_ICMPGE) {
                    opcode = Opcodes.IFGE;
                } else if (opcode == Opcodes.IF_ICMPGT) {
                    opcode = Opcodes.IFGT;
                } else if (opcode == Opcodes.IF_ICMPLE) {
                    opcode = Opcodes.IFLE;
                } else if (opcode == Opcodes.IF_ICMPLT) {
                    opcode = Opcodes.IFLT;
                }
            }
        }
        markIfFalse = new Label();
        // this is a reversed logic WTF
        visitor.visitJumpInsn(opcode, markIfFalse);
    }

    public void EndIf() {
        MethodVisitor visitor = Code.getCode().getCodeVisitor();
        compile();
        if (trueBody != null) {
            trueBody.run();
        }
        // it will pass to the next stack only if its true
        // and will go to the specified Label only if its false
        if (falseBody != null) {
            Label endPoint = new Label();
            visitor.visitJumpInsn(Opcodes.GOTO, endPoint);
            visitor.visitLabel(markIfFalse);
            falseBody.run();
            visitor.visitLabel(endPoint);
        } else {
            visitor.visitLabel(markIfFalse);
        }
    }

    public Condition Then(Runnable body) {
        this.trueBody = body;
        return this;
    }

    public Condition Else(Runnable body) {
        this.falseBody = body;
        return this;
    }

}
