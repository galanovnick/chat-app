package repository;

import entity.Entity;
import entity.tiny.EntityId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/*package*/ abstract class InMemoryRepository
        <ObjectType extends Entity<ObjectId>, ObjectId extends EntityId>
        implements Repository<ObjectType, ObjectId> {

    final Map<ObjectId, ObjectType> entries = new HashMap<>();

    @Override
    public ObjectId add(ObjectType object) {
        checkNotNull(object, "Entity cannot be null.");

        object.setId(nextId());
        entries.put(object.getId(), object);

        return object.getId();
    }

    @Override
    public Optional<ObjectType> findOne(ObjectId objectId) {
        return Optional.ofNullable(entries.get(objectId));
    }

    @Override
    public Collection<ObjectType> findAll() {
        return entries.values();
    }

    @Override
    public void delete(ObjectId objectId) {
        entries.remove(objectId);
    }

    protected abstract ObjectId nextId();
}
