package thito.reflectedbytecode.jvm;

import org.objectweb.asm.*;
import thito.reflectedbytecode.*;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.*;

public interface Java {

    static IClass Class(Type clazz) {
        return IClass.fromClass(clazz);
    }

    static IClass Class(String name) {
        return IClass.findClass(name);
    }

    static Reference Cast(Reference object, Type type) {
        return new Reference(type) {
            @Override
            protected void write() {
                object.write(Object.class);
                Code.getCode().getCodeVisitor().visitTypeInsn(Opcodes.CHECKCAST, ASMHelper.ToASMType(type).getInternalName());
            }
        };
    }

    static Reference Null() {
        return new Reference(null) {
            @Override
            public void write() {
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.ACONST_NULL);
            }

        };
    }

    static Object Equals(Object a, Object b) {
        return IClass.fromClass(Objects.class).getDeclaredMethod("equals", Object.class, Object.class).get().invoke(null, a, b);
    };

    static UClass UClass(String name) {
        return UClass.Class(name);
    }

    // INNER CLASSES
    interface Enum {
        Type getDeclaringClass();
        Constant valueOf(String name);
        abstract class Constant extends Reference {
            public Constant(Type type) {
                super(type);
            }

            public abstract String name();
            public abstract Type getType();
        }
    }

    interface Logic {
        static Reference True() {
            return new Reference(boolean.class) {
                @Override
                protected void write() {
                    Code.getCode().getCodeVisitor().visitInsn(Opcodes.ICONST_1);
                }
            };
        }
        static Reference False() {
            return new Reference(boolean.class) {
                @Override
                protected void write() {
                    Code.getCode().getCodeVisitor().visitInsn(Opcodes.ICONST_0);
                }
            };
        }
        static Reference And(Object condition1, Object condition2) {
            Class<?> type = ASMHelper.GetIntegerHigherType(ASMHelper.FindRealType(ASMHelper.GetType(condition1)),
                    ASMHelper.FindRealType(ASMHelper.GetType(condition2)));
            return new Reference(type) {
                @Override
                public void write() {
                    Reference.handleWrite(type, condition1);
                    Reference.handleWrite(type, condition2);
                    Code.getCode().getCodeVisitor().visitInsn(ASMHelper.ToASMType(type).getOpcode(Opcodes.IAND));
                }
            };
        }
        static Reference Or(Object condition1, Object condition2) {
            Class<?> type = ASMHelper.GetIntegerHigherType(ASMHelper.FindRealType(ASMHelper.GetType(condition1)),
                    ASMHelper.FindRealType(ASMHelper.GetType(condition2)));
            return new Reference(type) {
                @Override
                public void write() {
                    Reference.handleWrite(type, condition1);
                    Reference.handleWrite(type, condition2);
                    Code.getCode().getCodeVisitor().visitInsn(ASMHelper.ToASMType(type).getOpcode(Opcodes.IOR));
                }
            };
        }
        static Reference XOr(Object condition1, Object condition2) {
            Class<?> type = ASMHelper.GetIntegerHigherType(ASMHelper.FindRealType(ASMHelper.GetType(condition1)),
                    ASMHelper.FindRealType(ASMHelper.GetType(condition2)));
            return new Reference(type) {
                @Override
                public void write() {
                    Reference.handleWrite(type, condition1);
                    Reference.handleWrite(type, condition2);
                    Code.getCode().getCodeVisitor().visitInsn(ASMHelper.ToASMType(type).getOpcode(Opcodes.IXOR));
                }
            };
        }
        static Reference Negate(Object condition) {
            Type type = ASMHelper.GetType(condition);
            return new Reference(type) {
                @Override
                public void write() {
                    Reference.handleWrite(type, condition);
                    Code.getCode().getCodeVisitor().visitInsn(ASMHelper.ToASMType(type).getOpcode(Opcodes.INEG));
                }
            };
        }
    }

    static Reference NewArray(Class<?> type, Object... dimensions) {
        IClass iClass = IClass.fromClass(type);
        char[] chars = new char[dimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            chars[i] = '[';
        }
        return new Reference(IClass.findClass(new String(chars)+iClass.getDescriptor())) {
            @Override
            public void write() {
                for (int i = 0; i < dimensions.length; i++) {
                    Reference.handleWrite(int.class, dimensions[i]);
                }
                MethodVisitor visitor = Code.getCode().getCodeVisitor();
                visitor.visitMultiANewArrayInsn(new String(chars)+iClass.getDescriptor(), dimensions.length);
            }
        };
    }

    static Reference WrapArray(Reference array, Object index, Object value) {
        return new Reference(array.getType()) {
            @Override
            protected void write() {
                array.write(array.getType());
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.DUP);
                Reference.handleWrite(int.class, index);
                Reference.handleWrite(ASMHelper.GetArrayType(array), value);
                MethodVisitor visitor = Code.getCode().getCodeVisitor();
                visitor.visitInsn(Opcodes.AASTORE);
            }
        };
    }

    interface Math {
        static Reference toInt(Object a) {
            Type type = ASMHelper.GetType(a);
            return new Reference(int.class) {
                @Override
                public void write() {
                    Reference.handleWrite(type, a);
                    int opcode = ASMHelper.getCastedOpcodes(ASMHelper.FindRealType(type), Opcodes.D2I);
                    if (opcode != -1) {
                        Code.getCode().getCodeVisitor().visitInsn(opcode);
                    }
                }
            };
        }
        static Reference toDouble(Object a) {
            Type type = ASMHelper.GetType(a);
            return new Reference(double.class) {
                @Override
                public void write() {
                    Reference.handleWrite(type, a);
                    int opcode = ASMHelper.getCastedOpcodes(ASMHelper.FindRealType(type), Opcodes.I2D);
                    if (opcode != -1) {
                        Code.getCode().getCodeVisitor().visitInsn(opcode);
                    }
                }
            };
        }
        static Reference toFloat(Object a) {
            Type type = ASMHelper.GetType(a);
            return new Reference(float.class) {
                @Override
                public void write() {
                    Reference.handleWrite(type, a);
                    int opcode = ASMHelper.getCastedOpcodes(ASMHelper.FindRealType(type), Opcodes.I2F);
                    if (opcode != -1) {
                        Code.getCode().getCodeVisitor().visitInsn(opcode);
                    }
                }
            };
        }
        static Reference toByte(Object a) {
            Type type = ASMHelper.GetType(a);
            return new Reference(byte.class) {
                @Override
                public void write() {
                    Reference.handleWrite(type, a);
                    int opcode = ASMHelper.getCastedOpcodes(ASMHelper.FindRealType(type), Opcodes.I2B);
                    if (opcode != -1) {
                        Code.getCode().getCodeVisitor().visitInsn(opcode);
                    }
                }
            };
        }
        static Reference toLong(Object a) {
            Type type = ASMHelper.GetType(a);
            return new Reference(long.class) {
                @Override
                public void write() {
                    Reference.handleWrite(type, a);
                    int opcode = ASMHelper.getCastedOpcodes(ASMHelper.FindRealType(type), Opcodes.I2L);
                    if (opcode != -1) {
                        Code.getCode().getCodeVisitor().visitInsn(opcode);
                    }
                }
            };
        }
        static Reference toShort(Object a) {
            Type type = ASMHelper.GetType(a);
            return new Reference(short.class) {
                @Override
                public void write() {
                    Reference.handleWrite(type, a);
                    int opcode = ASMHelper.getCastedOpcodes(ASMHelper.FindRealType(type), Opcodes.I2S);
                    if (opcode != -1) {
                        Code.getCode().getCodeVisitor().visitInsn(opcode);
                    }
                }
            };
        }
        static Reference toChar(Object a) {
            Type type = ASMHelper.GetType(a);
            return new Reference(char.class) {
                @Override
                public void write() {
                    Reference.handleWrite(type, a);
                    int opcode = ASMHelper.getCastedOpcodes(ASMHelper.FindRealType(type), Opcodes.I2C);
                    if (opcode != -1) {
                        Code.getCode().getCodeVisitor().visitInsn(opcode);
                    }
                }
            };
        }
        static Reference add(Object a, Object b) {
            Class<?> highest = ASMHelper.GetHigherType(
                    (Class<?>) ASMHelper.GetType(a),
                    (Class<?>) ASMHelper.GetType(b));
            return new Reference(highest) {
                @Override
                public void write() {
                    Reference.handleWrite(highest, a);
                    Reference.handleWrite(highest, b);
                    Code.getCode().getCodeVisitor().visitInsn(org.objectweb.asm.Type.getType(highest).getOpcode(Opcodes.IADD));
                }
            };
        }
        static Reference multiply(Object a, Object b) {
            Class<?> highest = ASMHelper.GetHigherType(
                    (Class<?>) ASMHelper.GetType(a),
                    (Class<?>) ASMHelper.GetType(b));
            return new Reference(highest) {
                @Override
                public void write() {
                    Reference.handleWrite(highest, a);
                    Reference.handleWrite(highest, b);
                    Code.getCode().getCodeVisitor().visitInsn(org.objectweb.asm.Type.getType(highest).getOpcode(Opcodes.IMUL));
                }
            };
        }
        static Reference divide(Object a, Object b) {
            Class<?> highest = ASMHelper.GetHigherType(
                    (Class<?>) ASMHelper.GetType(a),
                    (Class<?>) ASMHelper.GetType(b));
            return new Reference(highest) {
                @Override
                public void write() {
                    Reference.handleWrite(highest, a);
                    Reference.handleWrite(highest, b);
                    Code.getCode().getCodeVisitor().visitInsn(org.objectweb.asm.Type.getType(highest).getOpcode(Opcodes.IDIV));
                }
            };
        }
        static Reference subtract(Object a, Object b) {
            Class<?> highest = ASMHelper.GetHigherType(
                    (Class<?>) ASMHelper.GetType(a),
                    (Class<?>) ASMHelper.GetType(b));
            return new Reference(highest) {
                @Override
                public void write() {
                    Reference.handleWrite(highest, a);
                    Reference.handleWrite(highest, b);
                    Code.getCode().getCodeVisitor().visitInsn(org.objectweb.asm.Type.getType(highest).getOpcode(Opcodes.ISUB));
                }
            };
        }
        static Reference mod(Object a, Object b) {
            Class<?> highest = ASMHelper.GetHigherType(
                    (Class<?>) ASMHelper.GetType(a),
                    (Class<?>) ASMHelper.GetType(b));
            return new Reference(highest) {
                @Override
                public void write() {
                    Reference.handleWrite(highest, a);
                    Reference.handleWrite(highest, b);
                    Code.getCode().getCodeVisitor().visitInsn(org.objectweb.asm.Type.getType(highest).getOpcode(Opcodes.IREM));
                }
            };
        }
        static Reference shl(Object a, Object b) {
            Class<?> highest = ASMHelper.GetIntegerHigherType(
                    (Class<?>) ASMHelper.GetType(a),
                    (Class<?>) ASMHelper.GetType(b));
            return new Reference(highest) {
                @Override
                public void write() {
                    Reference.handleWrite(highest, a);
                    Reference.handleWrite(highest, b);
                    Code.getCode().getCodeVisitor().visitInsn(org.objectweb.asm.Type.getType(highest).getOpcode(Opcodes.ISHL));
                }
            };
        }
        static Reference shr(Object a, Object b) {
            Class<?> highest = ASMHelper.GetIntegerHigherType(
                    (Class<?>) ASMHelper.GetType(a),
                    (Class<?>) ASMHelper.GetType(b));
            return new Reference(highest) {
                @Override
                public void write() {
                    Reference.handleWrite(highest, a);
                    Reference.handleWrite(highest, b);
                    Code.getCode().getCodeVisitor().visitInsn(org.objectweb.asm.Type.getType(highest).getOpcode(Opcodes.ISHR));
                }
            };
        }
        static Reference uShr(Object a, Object b) {
            Class<?> highest = ASMHelper.GetIntegerHigherType(
                    (Class<?>) ASMHelper.GetType(a),
                    (Class<?>) ASMHelper.GetType(b));
            return new Reference(highest) {
                @Override
                public void write() {
                    Reference.handleWrite(highest, a);
                    Reference.handleWrite(highest, b);
                    Code.getCode().getCodeVisitor().visitInsn(org.objectweb.asm.Type.getType(highest).getOpcode(Opcodes.IUSHR));
                }
            };
        }
    }

    // METHODS
    static Enum Enum(Type type) {
        return new Enum() {
            @Override
            public Type getDeclaringClass() {
                return type;
            }

            @Override
            public Constant valueOf(String name) {
                return new Enum.Constant(type) {

                    @Override
                    protected void write() {
                        Code.getCode().getCodeVisitor().visitFieldInsn(Opcodes.GETSTATIC, type.getTypeName().replace('.', '/'), name, "L"+type.getTypeName().replace('.', '/')+";");
                    }

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
        };
    }

    static void SetArrayElement(Object array, Object index, Object value) {
        Reference.handleWrite(null, array);
        Reference.handleWrite(int.class, index);
        Reference.handleWrite(ASMHelper.GetArrayType(array), value);
        MethodVisitor visitor = Code.getCode().getCodeVisitor();
        visitor.visitInsn(Opcodes.AASTORE);
    }
    static Reference GetArrayElement(Object array, Object index) {
        return new Reference(ASMHelper.GetArrayType(array)) {
            @Override
            public void write() {
                Reference.handleWrite(null, array);
                Reference.handleWrite(int.class, index);
                MethodVisitor visitor = Code.getCode().getCodeVisitor();
                visitor.visitInsn(Opcodes.AALOAD);
            }
        };
    }
    static Reference Type(Type type) {
        return new Reference(Class.class) {
            @Override
            public void write() {
                Code.getCode().getCodeVisitor().visitLdcInsn(org.objectweb.asm.Type.getType(ASMHelper.FindRealType(type)));
            }
        };
    }
    static Try Try(Runnable tryBody) {
        return new Try(tryBody);
    }
    static When If(Object object) {
        return new When(object);
    }
    static Reference ArrayLength(Object array) {
        return new Reference(int.class) {
            @Override
            public void write() {
                Reference.handleWrite(null, array);
                Code.getCode().getCodeVisitor().visitInsn(Opcodes.ARRAYLENGTH);
            }
        };
    }
    static Reference InstanceOf(Object value, Type type) {
        return new Reference(boolean.class) {
            @Override
            public void write() {
                Reference.handleWrite(Object.class, value);
                Code.getCode().getCodeVisitor().visitTypeInsn(Opcodes.INSTANCEOF, type.getTypeName().replace('.', '/'));
            }
        };
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
    static void Throw(Object reference) {
        MethodVisitor visitor = Code.getCode().getCodeVisitor();
        Reference.handleWrite(Throwable.class, reference);
        visitor.visitInsn(Opcodes.ATHROW);
    }
}
