package repository;

import com.google.common.base.Optional;
import entity.Entry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class InMemoryRepository
        <ObjectType extends Entry<ObjectId>, ObjectId extends Long>
        implements Repository<ObjectType, ObjectId> {

    protected final Map<ObjectId, ObjectType> entries = new HashMap<>();

    @Override
    public ObjectId insert(ObjectType object) {
        checkNotNull(object, "Entry cannot be null.");

        object.setId(nextId());
        entries.put(object.getId(), object);

        return object.getId();
    }

    @Override
    public Optional<ObjectType> findOne(ObjectId objectId) {
        return Optional.fromNullable(entries.get(objectId));
    }

    @Override
    public Collection<ObjectType> findAll() {
        return entries.values();
    }

    protected abstract ObjectId nextId();
}
