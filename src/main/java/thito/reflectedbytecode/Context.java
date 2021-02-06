package thito.reflectedbytecode;

import jdk.internal.org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.*;
import thito.reflectedbytecode.jvm.Java;
import thito.reflectedbytecode.jvm.JavaValidatorHelper;

import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class Context implements AutoCloseable {

    public static Context open() {
        if (context.get() != null) throw new IllegalStateException("already open");
        Context ctx = new Context();
        context.set(ctx);
        return ctx;
    }

    private static final ThreadLocal<Context> context = new ThreadLocal<>();

    public static Context getContext() {
        Context ctx = context.get();
        if (ctx == null) throw new IllegalStateException("no context");
        return ctx;
    }

    private Set<GClass> classes = new HashSet<>();
    Context() {
    }

    Set<GClass> getClasses() {
        return classes;
    }

    public GClass getClass(String name) {
        for (GClass gClass : classes) {
            if (gClass.getName().equals(name)) {
                return gClass;
            }
        }
        return null;
    }

    public GClass createClass(String name) {
        if (getClass(name) != null) throw new IllegalStateException("class "+name+" already exists");
        GClass clazz = new GClass(this, null, name);
        classes.add(clazz);
        return clazz;
    }

    public <T> T createProxy(Class<T> clazz, Object instance) {
        if (!clazz.isInterface()) throw new IllegalArgumentException(clazz.getName()+" is not an interface");
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] {clazz}, new ContextProxy(this, instance));
    }

    public byte[] writeClass(GClass clazz) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        writer.visit(Opcodes.V1_8, clazz.getModifiers(), clazz.getRawName(), null, clazz.getSuperClass() == null ? "java/lang/Object" : clazz.getSuperClass().getRawName(),
                Arrays.stream(clazz.getInterfaces()).map(IClass::getRawName).toArray(String[]::new));

        BiConsumer<java.lang.reflect.Type, GAnnotation<?>> annotationWriter = (type, annotation) -> {
            AnnotationVisitor visitor = writer.visitAnnotation(TypeHelper.getTypeDescriptor(type),
                    GAnnotation.isAnnotationVisible(type, true));
            annotation.map.forEach((key, value) -> {
                if (value instanceof Java.Enum.Constant) {
                    visitor.visitEnum(key, TypeHelper.getTypeDescriptor(((Java.Enum.Constant) value).getType()), ((Java.Enum.Constant) value).name());
                } else {
                    visitor.visit(key, value);
                }
            });
        };

        clazz.getAnnotationMap().forEach(annotationWriter);

        for (IField field : clazz.getDeclaredFields()) {
            writer.visitField(field.getModifiers(), field.getName(), field.getType().getDescriptor(), null, ((GField) field).getConstantValue());
            ((GField) field).getAnnotationMap().forEach(annotationWriter);
        }

        // WRITE CONSTRUCTORS
        IConstructor[] constructors = clazz.getDeclaredConstructors();
        for (IConstructor constructor : constructors) {
            MethodVisitor constructorVisitor = wrap(
                    writer.visitMethod(constructor.getModifiers(), "<init>", ((GConstructor) constructor).getMethodDescriptor(), null,
                            Arrays.stream(constructor.getThrows()).map(TypeHelper::getInternalName).toArray(String[]::new))
            );
            ((GConstructor) constructor).getAnnotationMap().forEach(annotationWriter);
            try (Code code = Code.pushCode(constructor.getModifiers(), constructorVisitor)) {
                code.skipLocalIndex(constructor.getParameterCount());
                ((GConstructor) constructor).writeBody();
                if (!code.isMarkedReturn()) {
                    // constructors are void-type, always RETURN
                    constructorVisitor.visitInsn(Opcodes.RETURN);
                }
            }
        }

        // WRITE DEFAULT CONSTRUCTORS IF DOESN'T HAVE ANY CONSTRUCTOR
        if (constructors.length == 0) {
            if (clazz.getSuperClass() != null) {
                IConstructor constructor = clazz.getSuperClass().getConstructor().get();
                MethodVisitor defaultConstructorVisitor = wrap(writer.visitMethod(Modifier.PUBLIC, "<init>", "()V", null,
                        Arrays.stream(constructor.getThrows()).map(TypeHelper::getInternalName).toArray(String[]::new)));
                try (Code code = Code.pushCode(Modifier.PUBLIC, defaultConstructorVisitor)) {
                    code.skipLocalIndex(0); // necessary? i don't think so
                    ((AbstractConstructor) constructor).invoke();
                    defaultConstructorVisitor.visitInsn(Opcodes.RETURN);
                }
            } else {
                MethodVisitor defaultConstructorVisitor = wrap(writer.visitMethod(Modifier.PUBLIC, "<init>", "()V", null, null));
                defaultConstructorVisitor.visitVarInsn(Opcodes.ALOAD, 0);
                defaultConstructorVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(Object.class), "<init>", "()V", false);
                defaultConstructorVisitor.visitInsn(Opcodes.RETURN);
                defaultConstructorVisitor.visitMaxs(-1, -1);
            }
        }

        // WRITE METHODS
        for (IMethod method : clazz.getDeclaredMethods()) {
            MethodVisitor methodVisitor = wrap(
                    writer.visitMethod(method.getModifiers(), method.getName(), ((GMethod) method).getMethodDescriptor(), null,
                            Arrays.stream(method.getThrows()).map(TypeHelper::getInternalName).toArray(String[]::new))
            );
            ((GMethod) method).getAnnotationMap().forEach(annotationWriter);
            try (Code code = Code.pushCode(method.getModifiers(), methodVisitor)) {
                Body<GMethodAccessor> accessorBody = ((GMethod) method).getBody();
                if (accessorBody != null) {
                    code.skipLocalIndex(method.getParameterCount());
                    accessorBody.declareBody(new GMethodAccessor((GMethod) method));
                    if (!code.isMarkedReturn()) {
                        if (method.getReturnType() == null || method.getReturnType().getDescriptor().equals("V")) {
                            methodVisitor.visitInsn(Opcodes.RETURN);
                        } else {
                            throw new IllegalStateException("unreturned non-void method");
                        }
                    }
                }
            }
        }
        CheckClassAdapter.verify(new jdk.internal.org.objectweb.asm.ClassReader(writer.toByteArray()), true, new PrintWriter(System.out));

        return writer.toByteArray();
    }

    public ClassLoader loadIntoMemory() {
        return loadIntoMemory(null);
    }

    public ClassLoader loadIntoMemory(ClassLoader parent) {
        return new ContextClassLoader(parent, this);
    }

    public void close() {
        Context ctx = context.get();
        if (ctx != this) throw new IllegalStateException("already closed");
        context.set(null);
    }

    private JavaValidatorHelper wrap(MethodVisitor visitor) {
        return new JavaValidatorHelper(Opcodes.ASM9, visitor);
    }
}
