package live;

public interface Callback<T> {
    void execute(T value);
}
