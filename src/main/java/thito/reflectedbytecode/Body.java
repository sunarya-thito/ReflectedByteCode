package thito.reflectedbytecode;

public interface Body<T extends BodyAccessor> {
    static <T extends BodyAccessor> Body<T> empty() {
        return target -> {};
    }
    void declareBody(T target);
}
