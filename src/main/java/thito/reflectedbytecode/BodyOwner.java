package thito.reflectedbytecode;

public interface BodyOwner {
    void putPreCompileTask(Runnable task);
    void putPostCompileTask(Runnable task);
    void executeCompileTask();
}
