package dao;

/**
 * Generic Data Access Object (DAO) interface for managing entities in a database.
 * Provides basic CRUD (Create, Read, Update, Delete) operations.
 *
 * @param <T> the type of entity to be managed by the DAO
 */
public interface IDAO<T> {

    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity to retrieve
     * @return the entity with the specified ID, or null if not found
     */
    T get(int id);

    /**
     * Adds a new entity to the database.
     *
     * @param t the entity to add
     */
    void add(T t);

    /**
     * Updates an existing entity in the database.
     *
     * @param t the entity with updated details
     */
    void update(T t);

    /**
     * Deletes an entity from the database by its ID.
     *
     * @param id the ID of the entity to delete
     */
    void delete(int id);
}