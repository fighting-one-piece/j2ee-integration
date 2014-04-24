package org.platform.modules.abstr.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.data.domain.Persistable;

public abstract class IdAbstrEntity <ID extends Serializable> implements Persistable<ID> {

	private static final long serialVersionUID = 1L;

	public abstract ID getId();
	
	public abstract void setId(ID id);
	
	public boolean isNew() {
        return null == getId();
    }
	
	@Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        IdAbstrEntity<?> that = (IdAbstrEntity<?>) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode() * 31;
        return hashCode;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
    
}
