package entity;

import entity.tiny.EntityId;

public interface Entity<ObjectId extends EntityId> {

    void setId(ObjectId id);

    ObjectId getId();
}
