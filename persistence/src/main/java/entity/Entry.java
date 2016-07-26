package entity;

public interface Entry<ObjectId extends Long> {

    void setId(ObjectId id);

    ObjectId getId();
}
