package thito.reflectedbytecode;


import org.objectweb.asm.*;
import thito.reflectedbytecode.ext.*;

import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.*;

public class ASMHelper {
    static final Class<?>[] INTEGER_PRIORITIES = {
        long.class,
        int.class,
    };
    static final Class<?>[] PRIORITIES = {
            long.class,
            double.class,
            int.class,
            float.class,
            short.class,
            char.class,
            byte.class,
            boolean.class,
    };
    public static Type GetArrayType(Object object) {
        Reference reference = (Reference) object;
        Class<?> type = FindRealType(reference.getType());
        if (type != null && type.isArray()) {
            return type.getComponentType();
        }
        return null;
    }
    public static Class<?> ConvertToArrayType(Class<?> type, int dimensions) {
        char[] chars = new char[dimensions];
        Arrays.fill(chars, '[');
        try {
            return Class.forName(new String(chars)+type.getName()+";", false, type.getClassLoader());
        } catch (Throwable t) {
        }
        return null;
    }
    public static Class<?> FindRealType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof KClass) {
            return ((KClass) type).getDelegated();
        }
        if (type instanceof ParameterizedType) {
            return FindRealType(((ParameterizedType) type).getRawType());
        }
        return null;
    }
    public static int getCastedOpcodes(Class<?> type, int opcode) {
        if (opcode == Opcodes.I2S || opcode == Opcodes.I2B || opcode == Opcodes.I2C) {
            if (type == int.class) {
                return opcode;
            } else if (type == double.class) {
                return Opcodes.D2I;
            } else if (type == long.class) {
                return Opcodes.L2I;
            } else if (type == float.class) {
                return Opcodes.F2I;
            }
        } else if (opcode == Opcodes.I2D) {
            if (type == int.class) {
                return opcode;
            } else if (type == long.class) {
                return Opcodes.L2D;
            } else if (type == float.class) {
                return Opcodes.F2D;
            }
        } else if (opcode == Opcodes.I2L) {
            if (type == int.class) {
                return opcode;
            } else if (type == double.class) {
                return Opcodes.D2L;
            } else if (type == float.class) {
                return Opcodes.F2L;
            }
        } else if (opcode == Opcodes.I2F) {
            if (type == int.class) {
                return opcode;
            } else if (type == long.class) {
                return Opcodes.L2D;
            } else if (type == double.class) {
                return Opcodes.F2D;
            }
        } else if (opcode == Opcodes.D2I) {
            if (type == double.class) {
                return opcode;
            } else if (type == long.class) {
                return Opcodes.L2I;
            } else if (type == float.class) {
                return Opcodes.F2I;
            }
        }
        return -1; // No casting required
    }
    public static org.objectweb.asm.Type ToASMType(Type type) {
        Class<?> clazz = FindRealType(type);
        if (clazz != null) {
            return org.objectweb.asm.Type.getType(clazz);
        }
        return org.objectweb.asm.Type.getType('L'+type.getTypeName().replace('.','/')+';');
    }
    public static Class<?> GetHigherType(Class<?> a, Class<?> b) {
        a = WrapperToPrimitive(a);
        b = WrapperToPrimitive(b);
        int indexA = search(PRIORITIES, a);
        int indexB = search(PRIORITIES, b);
        if (indexA == -1) return b;
        if (indexB == -1) return a;
        if (indexA < indexB) {
            return a;
        }
        return b;
    }
    public static int GetHigherIndex(Class<?> a, Class<?> b) {
        a = WrapperToPrimitive(a);
        b = WrapperToPrimitive(b);
        int indexA = search(PRIORITIES, a);
        int indexB = search(PRIORITIES, b);
        if (indexA < indexB) {
            return -1;
        }
        return 1;
    }
    private static int search(Object[] object, Object o) {
        for (int i = 0; i < object.length; i++) {
            if (object[i] == o) {
                return i;
            }
        }
        return -1;
    }
    public static Class<?> GetIntegerHigherType(Class<?> a, Class<?> b) {
        a = WrapperToPrimitive(a);
        b = WrapperToPrimitive(b);
        int indexA = search(INTEGER_PRIORITIES, a);
        int indexB = search(INTEGER_PRIORITIES, b);
        if (indexA < indexB) {
            return a;
        }
        return b;
    }
    public static Type GetType(Object reference) {
        if (reference instanceof Reference) {
            Type type = ((Reference) reference).getType();
            if (type == null) type = Object.class;
            return FindRealType(type);
        }
        return reference.getClass();
    }
    static boolean IsNumberPrimitive(Class<?> clazz) {
        return int.class == clazz || double.class == clazz || short.class == clazz || long.class == clazz
                || float.class == clazz || byte.class == clazz;
    }
    public static Class<?> PrimitiveToWrapper(Class<?> clazz) {
        if (clazz == int.class) return Integer.class;
        if (clazz == double.class) return Double.class;
        if (clazz == char.class) return Character.class;
        if (clazz == float.class) return Float.class;
        if (clazz == long.class) return Long.class;
        if (clazz == boolean.class) return Boolean.class;
        if (clazz == byte.class) return Byte.class;
        if (clazz == short.class) return Short.class;
        return clazz;
    }
    public static Class<?> WrapperToPrimitive(Class<?> clazz) {
        if (clazz == Integer.class) return int.class;
        if (clazz == Double.class) return double.class;
        if (clazz == Character.class) return char.class;
        if (clazz == Float.class) return float.class;
        if (clazz == Long.class) return long.class;
        if (clazz == Boolean.class) return boolean.class;
        if (clazz == Byte.class) return byte.class;
        if (clazz == Short.class) return short.class;
        return clazz;
    }
    static void CheckCastHelper(Class<?> source, Class<?> target) {
        if (target != null && target.isPrimitive() && source == null) {
            MethodVisitor visitor = Code.getCode().getCodeVisitor();
            visitor.visitInsn(Opcodes.POP);
            if (target == int.class || target == short.class || target == byte.class ||
                    target == boolean.class || target == char.class) {
                visitor.visitInsn(Opcodes.ICONST_0);
                return;
            }
            if (target == double.class) {
                visitor.visitInsn(Opcodes.DCONST_0);
                return;
            }
            if (target == float.class) {
                visitor.visitInsn(Opcodes.FCONST_0);
                return;
            }
            if (target == long.class) {
                visitor.visitInsn(Opcodes.LCONST_0);
                return;
            }
        }
        // on stack: [sourceObject]
        // target stack: [targetObject]
        Reference result = ObjectTransformHandler.handleCasting(source, target);
        if (result != null) {
            result.write(target);
        }
    }
}
