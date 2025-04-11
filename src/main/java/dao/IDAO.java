package dao;

public interface IDAO<T> {
    public T get(int id);
    public void add(T t);
    public void update(T t);
    public void delete(int id);
}
