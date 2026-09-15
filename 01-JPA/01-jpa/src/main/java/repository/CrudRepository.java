package repository;

import java.util.List;

public interface CrudRepository<Entity> {

    Entity findById(Long id);
    List<Entity> findAll();
    void create(Entity entity);
    void update(Entity entity);
    void delete(Entity entity);
}