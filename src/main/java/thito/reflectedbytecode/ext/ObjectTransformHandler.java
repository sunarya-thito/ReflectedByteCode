package thito.reflectedbytecode.ext;

import org.objectweb.asm.*;
import thito.reflectedbytecode.*;
import thito.reflectedbytecode.ext.transform.*;
import thito.reflectedbytecode.jvm.*;

import java.util.*;

public abstract class ObjectTransformHandler {
    private static Map<Class<?>, ObjectTransformHandler> handlers = new HashMap<>();
    private static UnknownTransformHandler unknownTransformHandler = new UnknownTransformHandler();
    private static ForceCastTransformHandler forceCastTransformHandler = new ForceCastTransformHandler();

    static {
        registerHandler(String.class, new StringTransformHandler());
        registerHandler(Integer.class, new IntegerTransformHandler());
        registerHandler(Double.class, new DoubleTransformHandler());
        registerHandler(Long.class, new LongTransformHandler());
        registerHandler(Float.class, new FloatTransformHandler());
        registerHandler(Short.class, new ShortTransformHandler());
        registerHandler(Byte.class, new ByteTransformHandler());
        registerHandler(Character.class, new CharacterTransformHandler());
        registerHandler(Boolean.class, new BooleanTransformHandler());
        registerHandler(int.class, new IntegerTransformHandler());
        registerHandler(double.class, new DoubleTransformHandler());
        registerHandler(long.class, new LongTransformHandler());
        registerHandler(float.class, new FloatTransformHandler());
        registerHandler(short.class, new ShortTransformHandler());
        registerHandler(byte.class, new ByteTransformHandler());
        registerHandler(char.class, new CharacterTransformHandler());
        registerHandler(boolean.class, new BooleanTransformHandler());
    }

    public static void registerHandler(Class<?> target, ObjectTransformHandler handler) {
        handlers.put(target, handler);
    }

    public static void unregisterHandler(Class<?> target, ObjectTransformHandler handler) {
        handlers.remove(target, handler);
    }

    public static Reference handleCasting(Class<?> from, Class<?> to) {
        if (from == null || to == null || from == to || to.isAssignableFrom(from)) return null;
        ObjectTransformHandler handler = handlers.getOrDefault(to, unknownTransformHandler);
        handler.store(from, to);
        if (!handler.canHandle()) {
            ((ObjectTransformHandler) forceCastTransformHandler).store(from, to);
            ((ObjectTransformHandler) forceCastTransformHandler).beginTransformation();
            return ((ObjectTransformHandler) forceCastTransformHandler).result.get().get();
        }
        handler.beginTransformation();
        return handler.result.get().get();
    }

    private ThreadLocal<Class<?>> sourceType = new ThreadLocal<>();
    private ThreadLocal<Class<?>> targetType = new ThreadLocal<>();
    private ThreadLocal<ILocalField> local = new ThreadLocal<>();
    private ThreadLocal<ILocalField> result = new ThreadLocal<>();

    protected void setValue(Object newValue) {
        result.get().set(newValue);
    }

    protected Reference getValue() {
        return local.get().get();
    }

    protected Class<?> getSourceType() {
        return sourceType.get();
    }

    protected Class<?> getTargetType() {
        return targetType.get();
    }

    private void store(Class<?> from, Class<?> to) {
        sourceType.set(from);
        targetType.set(to);
    }

    private void beginTransformation() {
        local.set(Code.getCode().getLocalFieldMap().createField(Java.Class(sourceType.get())));
        result.set(Code.getCode().getLocalFieldMap().createField(Java.Class(targetType.get())));
        Code.getCode().getCodeVisitor().visitVarInsn(ASMHelper.ToASMType(sourceType.get()).getOpcode(Opcodes.ISTORE), ((LField) local.get()).getLocalIndex());
        handleTransformation();
    }

    protected abstract void handleTransformation();

    protected abstract boolean canHandle();
}
