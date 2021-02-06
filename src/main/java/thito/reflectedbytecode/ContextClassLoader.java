package thito.reflectedbytecode;

public class ContextClassLoader extends ClassLoader {

    private Context context;

    protected ContextClassLoader(ClassLoader parent, Context context) {
        super(parent);
        this.context = context;
        loadAll();
    }

    private void loadAll() {
        for (GClass generatedClass : context.getClasses()) {
            byte[] written = context.writeClass(generatedClass);
            defineClass(generatedClass.getName(), written, 0, written.length);
        }
    }

    public Context getContext() {
        return context;
    }

}
