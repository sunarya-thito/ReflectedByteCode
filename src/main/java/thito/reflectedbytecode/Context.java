package thito.reflectedbytecode;

import org.objectweb.asm.Type;
import org.objectweb.asm.*;
import org.objectweb.asm.util.*;
import thito.reflectedbytecode.jvm.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

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

    public Set<GClass> getClasses() {
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

    public GClass createClass(String name, GClass declaring) {
        if (getClass(name) != null) throw new IllegalStateException("class "+name+" already exists");
        GClass clazz = new GClass(this, declaring, name);
        classes.add(clazz);
        return clazz;
    }

    public <T> T createProxy(Class<T> clazz, Object instance) {
        if (!clazz.isInterface()) throw new IllegalArgumentException(clazz.getName()+" is not an interface");
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] {clazz}, new ContextProxy(this, instance));
    }

    public byte[] writeClass(GClass clazz) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor classVisitor = classWriter;
        classVisitor.visit(Opcodes.V1_8, clazz.getModifiers() & 456241 | Opcodes.ACC_SUPER, clazz.getRawName(), null, clazz.getSuperClass() == null ? "java/lang/Object" : clazz.getSuperClass().getRawName(),
                Arrays.stream(clazz.getInterfaces()).map(IClass::getRawName).toArray(String[]::new));

        clazz.getAnnotationMap().forEach((type, annotation) -> {
            AnnotationVisitor visitor = classVisitor.visitAnnotation(TypeHelper.getTypeDescriptor(type),
                    GAnnotation.isAnnotationVisible(type, true));
            annotation.map.forEach((key, value) -> {
                if (value instanceof Java.Enum.Constant) {
                    visitor.visitEnum(key, TypeHelper.getTypeDescriptor(((Java.Enum.Constant) value).getType()), ((Java.Enum.Constant) value).name());
                } else {
                    visitor.visit(key, value);
                }
            });
        });

        // WRITE CONSTRUCTORS
        IConstructor[] constructors = clazz.getDeclaredConstructors();
        for (IConstructor constructor : constructors) {
            MethodVisitor constructorVisitor = wrap(constructor.getModifiers(), ((GConstructor) constructor).getMethodDescriptor(),
                    classVisitor.visitMethod(constructor.getModifiers(), "<init>", ((GConstructor) constructor).getMethodDescriptor(), null,
                            Arrays.stream(constructor.getThrows()).map(TypeHelper::getInternalName).toArray(String[]::new)),
                    (GConstructor) constructor
            );
            ((GConstructor) constructor).getAnnotationMap().forEach(
                    (type, annotation) -> {
                        AnnotationVisitor visitor = constructorVisitor.visitAnnotation(TypeHelper.getTypeDescriptor(type),
                                GAnnotation.isAnnotationVisible(type, true));
                        annotation.map.forEach((key, value) -> {
                            if (value instanceof Java.Enum.Constant) {
                                visitor.visitEnum(key, TypeHelper.getTypeDescriptor(((Java.Enum.Constant) value).getType()), ((Java.Enum.Constant) value).name());
                            } else {
                                visitor.visit(key, value);
                            }
                        });
                        visitor.visitEnd();
                    }
            );
            try (Code code = Code.pushCode(constructor.getModifiers(), constructorVisitor, (GConstructor) constructor)) {
                code.getCodeVisitor().visitCode();
                ((GConstructor) constructor).writeBody();
                if (!code.isMarkedReturn()) {
                    // constructors are void-type, always RETURN
                    code.getCodeVisitor().visitInsn(Opcodes.RETURN);
                }
            }
        }

        // WRITE DEFAULT CONSTRUCTORS IF DOESN'T HAVE ANY CONSTRUCTOR
        if (constructors.length == 0) {
            if (clazz.getSuperClass() != null) {
                IConstructor constructor = clazz.getSuperClass().getDeclaredConstructor().get();
                JavaValidatorHelper defaultConstructorVisitor = wrap(Modifier.PUBLIC, "()V",classVisitor.visitMethod(Modifier.PUBLIC, "<init>", "()V", null,
                        Arrays.stream(constructor.getThrows()).map(TypeHelper::getInternalName).toArray(String[]::new)), null);
                defaultConstructorVisitor.visitCode();
                defaultConstructorVisitor.visitVarInsn(Opcodes.ALOAD, 0);
                defaultConstructorVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
                        constructor.getDeclaringClass().getRawName(), "<init>", "()V", false);
                defaultConstructorVisitor.visitInsn(Opcodes.RETURN);
                defaultConstructorVisitor.compile();
                defaultConstructorVisitor.visitMaxs(1, 1);
                defaultConstructorVisitor.visitEnd();
            } else {
                JavaValidatorHelper defaultConstructorVisitor = wrap(Modifier.PUBLIC, "()V", classVisitor.visitMethod(Modifier.PUBLIC, "<init>", "()V", null, null), null);
                defaultConstructorVisitor.visitCode();
                defaultConstructorVisitor.visitVarInsn(Opcodes.ALOAD, 0);
                defaultConstructorVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(Object.class), "<init>", "()V", false);
                defaultConstructorVisitor.visitInsn(Opcodes.RETURN);
                defaultConstructorVisitor.compile();
                defaultConstructorVisitor.visitMaxs(1, 1);
                defaultConstructorVisitor.visitEnd();
            }
        }

        // WRITE METHODS
        for (IMethod method : clazz.getDeclaredMethods()) {
            JavaValidatorHelper methodVisitor = wrap(method.getModifiers(), ((GMethod) method).getMethodDescriptor(),
                    classVisitor.visitMethod(method.getModifiers(), method.getName(), ((GMethod) method).getMethodDescriptor(), null,
                            Arrays.stream(method.getThrows()).map(TypeHelper::getInternalName).toArray(String[]::new)),
                    (GMethod) method
            );
            ((GMethod) method).getAnnotationMap().forEach(
                    (type, annotation) -> {
                        AnnotationVisitor visitor = methodVisitor.visitAnnotation(TypeHelper.getTypeDescriptor(type),
                                GAnnotation.isAnnotationVisible(type, true));
                        annotation.map.forEach((key, value) -> {
                            if (value instanceof Java.Enum.Constant) {
                                visitor.visitEnum(key, TypeHelper.getTypeDescriptor(((Java.Enum.Constant) value).getType()), ((Java.Enum.Constant) value).name());
                            } else {
                                visitor.visit(key, value);
                            }
                        });
                        visitor.visitEnd();
                    }
            );
            try (Code code = Code.pushCode(method.getModifiers(), methodVisitor, (GMethod) method)) {
                Body<GMethodAccessor> accessorBody = ((GMethod) method).getBody();
                if (accessorBody != null) {
                    code.getCodeVisitor().visitCode();
                    accessorBody.declareBody(new GMethodAccessor((GMethod) method));
                    if (!code.isMarkedReturn()) {
                        if (method.getReturnType() == null || method.getReturnType().getDescriptor().equals("V")) {
                            code.getCodeVisitor().visitInsn(Opcodes.RETURN);
                        } else {
                            throw new IllegalStateException("unreturned non-void method");
                        }
                    }
                }
            }
        }

        for (IField field : clazz.getDeclaredFields()) {
            FieldVisitor fieldVisitor = classVisitor.visitField(field.getModifiers(), field.getName(), field.getType().getDescriptor(), null, ((GField) field).getConstantValue());
            ((GField) field).getAnnotationMap().forEach((type, annotation) -> {
                AnnotationVisitor visitor = fieldVisitor.visitAnnotation(TypeHelper.getTypeDescriptor(type),
                        GAnnotation.isAnnotationVisible(type, true));
                annotation.map.forEach((key, value) -> {
                    if (value instanceof Java.Enum.Constant) {
                        visitor.visitEnum(key, TypeHelper.getTypeDescriptor(((Java.Enum.Constant) value).getType()), ((Java.Enum.Constant) value).name());
                    } else {
                        visitor.visit(key, value);
                    }
                });
            });
        }

        if (clazz.getDeclaringClass() != null) {
            if (clazz.unknown) {
                classVisitor.visitOuterClass(clazz.getDeclaringClass().getRawName(), null, null);
                classVisitor.visitInnerClass(clazz.getRawName(), null, null, clazz.getModifiers() & ~Opcodes.ACC_SUPER);
            } else {
                classVisitor.visitInnerClass(clazz.getRawName(), clazz.getDeclaringClass().getSimpleName(), clazz.getSimpleName(), clazz.getModifiers() & ~Opcodes.ACC_SUPER);
            }
        }
        for (IClass declared : clazz.getDeclaredClasses()) {
            if (clazz.unknown) {
                classVisitor.visitInnerClass(declared.getRawName(), null, null, declared.getModifiers() & ~Opcodes.ACC_SUPER);
            } else {
                classVisitor.visitInnerClass(declared.getRawName(), clazz.getSimpleName(), declared.getSimpleName(), declared.getModifiers() & ~Opcodes.ACC_SUPER);
            }
        }
        try {
            CheckClassAdapter.verify(new ClassReader(classWriter.toByteArray()), false, new PrintWriter(System.out));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return classWriter.toByteArray();
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

    private JavaValidatorHelper wrap(int access, String descriptor, MethodVisitor visitor, BodyOwner method) {
        return new JavaValidatorHelper(Opcodes.ASM9, access, descriptor, visitor, method);
    }
}
