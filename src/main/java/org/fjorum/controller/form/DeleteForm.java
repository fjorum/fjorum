package org.fjorum.controller.form;

import javax.validation.constraints.NotNull;

/**
 * As a delete request requires only the entity id, one form for all entities is sufficient.
 * However, the model attribute should always get an expressive name like "userDeleteForm".
 */
public class DeleteForm {

    @NotNull
    private Long entityId = 0L;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
