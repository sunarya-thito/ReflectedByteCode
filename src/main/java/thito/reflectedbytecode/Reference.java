package thito.reflectedbytecode;

import org.objectweb.asm.*;
import thito.reflectedbytecode.jvm.*;

import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.*;

public abstract class Reference {
    static int[] LCONST = {Opcodes.LCONST_0, Opcodes.LCONST_1};
    static int[] ICONST = {Opcodes.ICONST_M1, Opcodes.ICONST_0, Opcodes.ICONST_1, Opcodes.ICONST_2,
            Opcodes.ICONST_3, Opcodes.ICONST_4, Opcodes.ICONST_5
        };
    public static Reference getSelf(Type type) {
        return new Reference(type) {
            @Override
            protected void write() {
                Code.getCode().getCodeVisitor().visitVarInsn(Opcodes.ALOAD, 0);
            }
        };
    }
    public static Reference javaToReference(Object object) {
        if (object instanceof Reference) return (Reference) object;
        if (object instanceof Enum) return Java.Enum(((Enum<?>) object).getDeclaringClass()).valueOf(((Enum<?>) object).name());
        if (object instanceof Field) return Java.Enum(((Field) object).getType()).valueOf(((Field) object).getName());
        Object finalObject;
        if (object instanceof Character) {
            finalObject = (int) ((Character) object).charValue();
        } else {
            finalObject = object;
        }
        Class<?> type = null;
        if (object != null) {
            type = ASMHelper.WrapperToPrimitive(object.getClass());
        }
        Reference result = new Reference(type) {
            @Override
            protected void write() {
                MethodVisitor visitor = Code.getCode().getCodeVisitor();
                if (finalObject instanceof Long) {
                    long l = (long) finalObject;
                    if (l == 0L) {
                        visitor.visitInsn(Opcodes.LCONST_0);
                        return;
                    }
                    if (l == 1L) {
                        visitor.visitInsn(Opcodes.LCONST_1);
                        return;
                    }
                } else if (finalObject instanceof Double) {
                    double d = (double) finalObject;
                    if (d == 0) {
                        visitor.visitInsn(Opcodes.DCONST_0);
                        return;
                    }
                    if (d == 0) {
                        visitor.visitInsn(Opcodes.DCONST_1);
                        return;
                    }
                } else if (finalObject instanceof Float) {
                    float f = (float) finalObject;
                    if (f == 0) {
                        visitor.visitInsn(Opcodes.FCONST_0);
                        return;
                    }
                    if (f == 1) {
                        visitor.visitInsn(Opcodes.FCONST_1);
                        return;
                    }
                    if (f == 2) {
                        visitor.visitInsn(Opcodes.FCONST_2);
                        return;
                    }
                } else if (finalObject instanceof Number) {
                    int i = ((Number) finalObject).intValue();
                    if (i == -1) {
                        visitor.visitInsn(Opcodes.ICONST_M1);
                        return;
                    }
                    if (i == 0) {
                        visitor.visitInsn(Opcodes.ICONST_0);
                        return;
                    }
                    if (i == 1) {
                        visitor.visitInsn(Opcodes.ICONST_1);
                        return;
                    }
                    if (i == 2) {
                        visitor.visitInsn(Opcodes.ICONST_2);
                        return;
                    }
                    if (i == 3) {
                        visitor.visitInsn(Opcodes.ICONST_3);
                        return;
                    }
                    if (i == 4) {
                        visitor.visitInsn(Opcodes.ICONST_4);
                        return;
                    }
                    if (i == 5) {
                        visitor.visitInsn(Opcodes.ICONST_5);
                        return;
                    }
                } else if (finalObject instanceof Boolean) {
                    if ((Boolean) finalObject) {
                        visitor.visitInsn(Opcodes.ICONST_1);
                    } else {
                        visitor.visitInsn(Opcodes.ICONST_0);
                    }
                    return;
                }
                if (finalObject == null) {
                    visitor.visitInsn(Opcodes.ACONST_NULL);
                } else {
                    visitor.visitLdcInsn(finalObject);
                }
            }
        };
        return result;
    }
    public static boolean isValid(Object value) {
        if (value instanceof Integer || value instanceof Byte || value instanceof Character || value instanceof Short
                || value instanceof Boolean || value instanceof Float || value instanceof Long || value instanceof Double
                || value instanceof String || value instanceof org.objectweb.asm.Type || value instanceof Handle || value instanceof ConstantDynamic) {
            return true;
        }
        return false;
    }
    public static void handleWrite(Type expectedOutput, Object object) {
        if (!(object instanceof Reference)) {
            object = javaToReference(object);
        }
        ((Reference) object).write(expectedOutput);
    }
    private Type type;
    public Reference(Type type) {
        this.type = type;
    }
    protected abstract void write();
    public void write(Type expectedOutput) {
        write();
        ASMHelper.CheckCastHelper(ASMHelper.FindRealType(type), ASMHelper.FindRealType(expectedOutput));
    }
    public Type getType() {
        return type;
    }
    public Reference arrayGet(Object index) {
        return Java.GetArrayElement(this, index);
    }
    public void arraySet(Object index, Object value) {
        Java.SetArrayElement(this, index, value);
    }
    public Reference arrayInitialValues(Object... values) {
        Reference array = this;
        for (int i = 0; i < values.length; i++) {
            array = Java.WrapArray(array, i, values[i]);
        }
        return array;
    }
    public Reference arrayLength() {
        return Java.ArrayLength(this);
    }
    public Reference refToString() {
        return IClass.fromClass(type).getMethod("toString").get().invoke(this);
    }
    public Reference mathAdd(Object obj) {
        return Java.Math.add(this, obj);
    }
    public Reference mathSubtract(Object obj) {
        return Java.Math.subtract(this, obj);
    }
    public Reference mathMultiply(Object obj) {
        return Java.Math.multiply(this, obj);
    }
    public Reference mathDivide(Object obj) {
        return Java.Math.divide(this, obj);
    }
    public Reference mathModulo(Object obj) {
        return Java.Math.mod(this, obj);
    }
    public Reference bitShiftLeft(Object amount) {
        return Java.Math.shl(this, amount);
    }
    public Reference bitShiftRight(Object amount) {
        return Java.Math.shr(this, amount);
    }
    public Reference bitUnsignedShiftRight(Object amount) {
        return Java.Math.uShr(this, amount);
    }
    public Reference stringConcat(Object amount) {
        return new Reference(String.class) {
            @Override
            public void write() {
                Java.Class(String.class).getMethod("concat", CharSequence.class).get()
                        .invoke(Reference.this, amount == null ? "null" : null);
            }
        };
    }
    public Reference logicAnd(Object other) {
        return Java.Logic.And(this, other);
    }
    public Reference logicOr(Object other) {
        return Java.Logic.Or(this, other);
    }
    public Reference logicXOr(Object other) {
        return Java.Logic.XOr(this, other);
    }
    public Reference logicNegate() {
        return Java.Logic.Negate(this);
    }
    public MethodInvocation method(String name, Type... types) {
        IClass type = Java.Class(getType());
        Optional<? extends IMethod> optional = type.getMethod(name, types);
        if (!optional.isPresent()) return null;
        IMethod method = optional.get();
        return method == null ? null : new MethodInvocation() {
            @Override
            public Reference invoke(Object... args) {
                return method.invoke(Reference.this, args);
            }

            @Override
            public void invokeVoid(Object... args) {
                method.invokeVoid(Reference.this, args);
            }
        };
    }
    public Reference get(String name) {
        return field(name).get(this);
    }
    public Reference set(String name, Object value) {
        IField field = field(name);
        field.set(this, value);
        return field.get(this);
    }
    public IField field(String name) {
        IClass type = Java.Class(getType());
        return type.getField(name).get();
    }
    public interface MethodInvocation {
        Reference invoke(Object...args);
        void invokeVoid(Object...args);
    }
}
