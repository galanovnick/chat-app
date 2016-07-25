package persistence.repository;

import java.util.Collection;

public interface Repository<ObjectType, ObjectId> {

    ObjectId insert(ObjectType object);

    ObjectType findOne(ObjectId id);

    Collection<ObjectType> findAll();
}
