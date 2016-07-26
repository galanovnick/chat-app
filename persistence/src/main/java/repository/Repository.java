package repository;

import com.google.common.base.Optional;

import java.util.Collection;

public interface Repository<ObjectType, ObjectId extends Long> {

    ObjectId insert(ObjectType object);

    Optional<ObjectType> findOne(ObjectId id);

    Collection<ObjectType> findAll();

    void delete(ObjectId id);
}
