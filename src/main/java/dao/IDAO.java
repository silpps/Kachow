package dao;
import java.time.LocalDate;
import java.util.List;

public interface IDAO<T> {
    public T get(String id);
    public void add(T t);
    public void update(T t);
    public void delete(String id);
}
