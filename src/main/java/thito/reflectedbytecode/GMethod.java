package thito.reflectedbytecode;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

public class GMethod extends AbstractMethod implements GMember {

    LField createLocalField() {
        LField field = new LField();
        localFields.add(field);
        return field;
    }

    PField getArgument(int index) {
        return parameters[index];
    }

    private String name;
    private int modifiers = Modifier.PUBLIC;
    private GClass declaringClass;
    private IClass returnType;
    private PField[] parameters;
    private List<LField> localFields = new ArrayList<>();
    private Body<GMethodAccessor> body;
    private Type[] Throws;
    private Map<Type, GAnnotation<GMethod>> annotationMap = new HashMap<>();

    GMethod(GClass declaringClass, String name) {
        this.name = name;
        this.declaringClass = declaringClass;
    }

    public GAnnotation<GMethod> annotate(Type type) {
        return annotationMap.computeIfAbsent(type, x -> new GAnnotation<>(this));
    }

    protected Map<Type, GAnnotation<GMethod>> getAnnotationMap() {
        return annotationMap;
    }

    public GMethod exceptionThrows(Type...exceptions) {
        Throws = exceptions;
        return this;
    }

    public GMethod body(Body<GMethodAccessor> body) {
        this.body = body;
        return this;
    }

    Body<GMethodAccessor> getBody() {
        return body;
    }

    public GMethod returnType(IClass returnType) {
        this.returnType = returnType;
        return this;
    }

    public GMethod parameters(Type... types) {
        return _parameters(Arrays.stream(types).map(x -> new PField(IClass.fromClass(x))).toArray(PField[]::new));
    }

    private GMethod _parameters(PField... parameters) {
        this.parameters = parameters;
        for (int i = 0; i < parameters.length; i++) {
            parameters[i].localIndex = i + 0;
        }
        return this;
    }

    @Override
    public GModifierMethod<GMethod> modifier() {
        return new GModifierMethod<>(this);
    }

    @Override
    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public GClass getDeclaringClass() {
        return declaringClass;
    }

    @Override
    public IClass[] getParameterTypes() {
        return parameters == null ? new IClass[0] : Arrays.stream(parameters).map(PField::getType).toArray(IClass[]::new);
    }

    @Override
    public IClass getReturnType() {
        return returnType;
    }

    @Override
    public int getParameterCount() {
        return parameters == null ? 0 : parameters.length;
    }

    @Override
    public Type[] getThrows() {
        return Throws == null ? new Type[0] : Throws;
    }
}
