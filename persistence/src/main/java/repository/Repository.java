package repository;

import entity.Entity;
import entity.tiny.EntityId;

import java.util.Collection;
import java.util.Optional;

interface Repository<ObjectType extends Entity<ObjectId>, ObjectId extends EntityId> {

    ObjectId add(ObjectType object);

    Optional<ObjectType> findOne(ObjectId id);

    Collection<ObjectType> findAll();

    void delete(ObjectId id);
}
