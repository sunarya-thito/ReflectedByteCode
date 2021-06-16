import org.objectweb.asm.*;

import java.lang.reflect.*;
import java.util.*;

public class ClassGenerator {
    static boolean allow(Class<?> type) {
        return !type.isArray() && !type.isPrimitive();
    }
    public static void main(String[] args) {
        Class<?> target = MethodVisitor.class;
        String name = "MethodVisitorDebug";
        Set<String> imports = new HashSet<>();
        imports.add("import "+target.getName()+";");
        StringBuilder builder = new StringBuilder("public class "+name+" extends "+target.getSimpleName()+" {");
        for (Method method : target.getDeclaredMethods()) {
            if (!Modifier.isFinal(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())) {
                builder.append("\n\t");
                builder.append(Modifier.toString(method.getModifiers())+" "+method.getReturnType().getSimpleName()+" "+method.getName()+"(");
                int index = 0;
                for (Parameter parameter : method.getParameters()) {
                    if (index > 0) builder.append(", ");
                    builder.append(parameter.getType().getSimpleName()+" "+parameter.getName());
                    if (allow(parameter.getType())) {
                        imports.add("import "+parameter.getType().getName()+";");
                    }
                    index++;
                }
                builder.append(") {\n\t\tSystem.out.println(\""+method.getName()+"(");
                index = 0;
                for (Parameter parameter : method.getParameters()) {
                    if (index > 0) builder.append(", ");
                    builder.append("\"+"+parameter.getName()+"+\"");
                    index++;
                }
                builder.append(")\");\n");
                if (method.getReturnType() != void.class) {
                    imports.add("import "+method.getReturnType().getName()+";");
                    builder.append("\t\treturn ");
                } else {
                    builder.append("\t\t");
                }
                builder.append("super."+method.getName()+"(");
                index = 0;
                for (Parameter parameter : method.getParameters()) {
                    if (index > 0) builder.append(", ");
                    builder.append(parameter.getName());
                    index++;
                }
                builder.append(");\n");
                builder.append("\t}");
            }
        }
        builder.append("\n}");
        builder.insert(0, String.join("\n", imports)+"\n");
        System.out.println(builder);
    }
}
