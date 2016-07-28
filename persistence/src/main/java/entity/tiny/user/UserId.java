package entity.tiny.user;

import entity.tiny.EntityId;

public class UserId implements EntityId<Long> {

    private final Long id;

    public UserId(Long id) {
        this.id = id;
    }

    public Long value() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserId userId = (UserId) o;

        return id != null ? id.equals(userId.id) : userId.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
