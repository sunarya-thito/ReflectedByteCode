package thito.reflectedbytecode;

import org.objectweb.asm.*;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

public class GClass extends AbstractClass implements GMember {

    private static <T> T[] combine(T[] a, T[] b) {
        Object newArray = Array.newInstance(a.getClass().getComponentType(), a.length + b.length);
        System.arraycopy(a, 0, newArray, 0, a.length);
        System.arraycopy(b, 0, newArray, a.length, b.length);
        return (T[]) newArray;
    }

    private Context context;
    private int modifiers = Modifier.PUBLIC;
    private String name, simpleName;
    private List<GMethod> declaredMethods = new ArrayList<>();
    private List<GField> declaredFields = new ArrayList<>();
    private List<GConstructor> declaredConstructors = new ArrayList<>();
    private List<GClass> declaredClasses = new ArrayList<>();
    private List<GClass> classes = new ArrayList<>();
    private IClass superclass, declaringClass;
    private IClass[] interfaces = new IClass[0];
    private Map<Type, GAnnotation<GClass>> annotationMap = new HashMap<>();
    private Body<BodyAccessor> staticInitializer;
    private Package pack;
    private List<GGeneric> generics = new ArrayList<>();
    boolean unknown;
    GClass(Context context, IClass declaring, String name) {
        this.declaringClass = declaring;
        this.context = context;
        this.name = name;
        int index = name.lastIndexOf('.');
        if (index >= 0) {
            simpleName = name.substring(index + 1);
            pack = new Package(name.substring(0, index),
                    null, null, null, null, null, null, null);
        } else {
            simpleName = name;
            pack = new Package(name,
                    null, null, null, null, null, null, null);
        }
    }

    public List<GGeneric> getGenerics() {
        return generics;
    }

    public GGeneric addGeneric() {
        GGeneric generic = new GGeneric(this);
        generics.add(generic);
        return generic;
    }

    public GAnnotation<GClass> annotate(Type type) {
        return annotationMap.computeIfAbsent(type, x -> new GAnnotation<>(this));
    }

    protected Map<Type, GAnnotation<GClass>> getAnnotationMap() {
        return annotationMap;
    }

    public GMethod declareMethod(String name) {
        GMethod method = new GMethod(this, name);
        declaredMethods.add(method);
        return method;
    }

    public GField declareField(String name, Type type) {
        GField field = new GField(this, name, IClass.fromClass(type));
        declaredFields.add(field);
        return field;
    }

    public GConstructor declareConstructor() {
        GConstructor constructor = new GConstructor(this);
        declaredConstructors.add(constructor);
        return constructor;
    }

    public GClass declareInnerClass() {
        GClass clazz = declareInnerClass(getName()+"$"+getInnerClassesCount());
        clazz.unknown = true;
        return clazz;
    }

    public GClass declareInnerClass(String name) {
        GClass clazz = context.createClass(name, this);
        declaredClasses.add(clazz);
        return clazz;
    }

    private int getInnerClassesCount() {
        int i = 1;
        while (!(IClass.findClass(getName()+"$"+i) instanceof UClass)) {
            i++;
        }
        return i;
    }

    public GClass thatImplements(Type... interfaces) {
        this.interfaces = Arrays.stream(interfaces).map(IClass::fromClass).toArray(IClass[]::new);
        return this;
    }

    public GClass thatExtends(Type superclass) {
        this.superclass = IClass.fromClass(superclass);
        return this;
    }

    @Override
    public GModifierClass<GClass> modifier() {
        return new GModifierClass<>(this);
    }

    @Override
    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public String getName() {
        return name;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public IConstructor[] getDeclaredConstructors() {
        return declaredConstructors.toArray(new IConstructor[0]);
    }

    @Override
    public IConstructor[] getConstructors() {
        return Arrays.stream(getDeclaredConstructors()).filter(constructor -> Modifier.isPublic(constructor.getModifiers())).toArray(IConstructor[]::new);
    }

    @Override
    public IMethod[] getDeclaredMethods() {
        return declaredMethods.toArray(new IMethod[0]);
    }

    @Override
    public IMethod[] getMethods() {
        if (superclass != null) {
            return combine(superclass.getMethods(), getDeclaredMethods());
        }
        return getDeclaredMethods();
    }

    @Override
    public IField[] getDeclaredFields() {
        return declaredFields.toArray(new IField[0]);
    }

    @Override
    public IField[] getFields() {
        if (superclass != null) {
            return combine(superclass.getFields(), getDeclaredFields());
        }
        return getDeclaredFields();
    }

    @Override
    public IClass[] getClasses() {
        return classes.toArray(new IClass[0]);
    }

    @Override
    public IClass[] getDeclaredClasses() {
        return declaredClasses.toArray(new IClass[0]);
    }

    @Override
    public IClass getSuperClass() {
        return superclass;
    }

    @Override
    public IClass[] getInterfaces() {
        return interfaces;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public String getDescriptor() {
        return "L"+name.replace('.', '/')+";";
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public IClass getDeclaringClass() {
        return declaringClass;
    }

    @Override
    public String getRawName() {
        return name.replace('.', '/');
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }

    @Override
    public Package getPackage() {
        return pack;
    }
}
