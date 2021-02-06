package thito.reflectedbytecode.jvm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import thito.reflectedbytecode.ArrayReference;
import thito.reflectedbytecode.Code;
import thito.reflectedbytecode.Reference;
import thito.reflectedbytecode.TypeHelper;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public interface Java {

    // INNER CLASSES
    interface Enum {
        Constant valueOf(String name);
        interface Constant {
            String name();
            Type getType();
        }
    }

    interface Math {
        static Reference toInt(Object a) {
            return () -> {
                Reference.handleWrite(a);
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.D2I);
            };
        }
        static Reference toDouble(Object a) {
            return () -> {
                Reference.handleWrite(a);
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.I2D);
            };
        }
        static Reference toFloat(Object a) {
            return () -> {
                Reference.handleWrite(a);
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.I2F);
            };
        }
        static Reference toByte(Object a) {
            return () -> {
                Reference.handleWrite(a);
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.I2B);
            };
        }
        static Reference toLong(Object a) {
            return () -> {
                Reference.handleWrite(a);
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.I2L);
            };
        }
        static Reference toShort(Object a) {
            return () -> {
                Reference.handleWrite(a);
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.I2S);
            };
        }
        static Reference toChar(Object a) {
            return () -> {
                Reference.handleWrite(a);
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.I2C);
            };
        }
        static Reference add(Object a, Object b) {
            return () -> {
                Reference.handleWrite(a);
                Reference.handleWrite(b);
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.IADD);
            };
        }
        static Reference multiply(Object a, Object b) {
            return () -> {
                Reference.handleWrite(a);
                Reference.handleWrite(b);
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.IMUL);
            };
        }
        static Reference divide(Object a, Object b) {
            return () -> {
                Reference.handleWrite(a);
                Reference.handleWrite(b);
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.IDIV);
            };
        }
        static Reference subtract(Object a, Object b) {
            return () -> {
                Reference.handleWrite(a);
                Reference.handleWrite(b);
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.ISUB);
            };
        }
        static Reference mod(Object a, Object b) {
            return () -> {
                Reference.handleWrite(a);
                Reference.handleWrite(b);
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.IREM);
            };
        }
    }

    // METHODS
    static Enum Enum(Type type) {
        return name -> new Enum.Constant() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public Type getType() {
                return type;
            }
        };
    }

    static Reference Cast(Type type, Object object) {
        return () -> {
            Reference.handleWrite(object);
            Code.getCode().getCodeVisitor().visitTypeInsn(Opcodes.CHECKCAST, TypeHelper.getInternalName(type));
        };
    }

    static ArrayReference NewArray(Type component, int... length) {
        return () -> {
            Code code = Code.getCode();
            MethodVisitor visitor = code.getCodeVisitor();
            if (length.length == 0) {
                visitor.visitLdcInsn(0);
                visitor.visitTypeInsn(Opcodes.ANEWARRAY, component.getTypeName().replace('.', '/'));
            } else if (length.length == 1) {
                visitor.visitLdcInsn(length[0]);
                visitor.visitTypeInsn(Opcodes.ANEWARRAY, component.getTypeName().replace('.', '/'));
            } else {
                StringBuilder builder = new StringBuilder(component.getTypeName().length()+2+ length.length);
                for (int i = 0; i < length.length; i++) {
                    builder.append('[');
                    visitor.visitLdcInsn(length[i]);
                }
                builder.append('L');
                builder.append(component.getTypeName().replace('.', '/'));
                builder.append(';');
                visitor.visitMultiANewArrayInsn(builder.toString(), length.length);
            }
        };
    }
    static void SetArrayElement(Reference array, Object index, Object value) {
        Reference.handleWrite(array);
        Reference.handleWrite(index);
        MethodVisitor visitor = Code.getCode().getCodeVisitor();
        Reference.handleWrite(value);
        visitor.visitInsn(Opcodes.AASTORE);
    }
    static Reference GetArrayElement(Reference array, Object index) {
        return () -> {
            Reference.handleWrite(array);
            Reference.handleWrite(index);
            MethodVisitor visitor = Code.getCode().getCodeVisitor();
            visitor.visitInsn(Opcodes.AALOAD);
        };
    }
    static Try Try(Runnable tryBody) {
        return null;
    }
    static When If(Object object) {
        Reference.handleWrite(object);
        return new When();
    }
    static Reference ArrayLength(Reference array) {
        return () -> {
            Reference.handleWrite(array);
            Code.getCode().getCodeVisitor().visitInsn(Opcodes.ARRAYLENGTH);
        };
    }
    static Reference InstanceOf(Reference value) {
        return null;
    }
    static void Label(String label) {
        Code code = Code.getCode();
        code.getCodeVisitor().visitLabel(code.getLabel(label));
    }
    static void Goto(String label) {
        Code code = Code.getCode();
        code.getCodeVisitor().visitJumpInsn(Opcodes.GOTO, code.getLabel(label));
    }
    static void Loop(Consumer<While> body) {
        MethodVisitor visitor = Code.getCode().getCodeVisitor();
        Label loopPoint = new Label();
        Label endPoint = new Label();
        visitor.visitLabel(loopPoint);
        body.accept(new While(endPoint, loopPoint));
        visitor.visitJumpInsn(Opcodes.GOTO, loopPoint);
        visitor.visitLabel(endPoint);
    }
}
