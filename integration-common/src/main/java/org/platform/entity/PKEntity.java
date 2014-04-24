package org.platform.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public abstract class PKEntity <PK extends Serializable> implements Serializable{

	private static final long serialVersionUID = 1L;

	public abstract PK getPK();
	
	public abstract void setPK(PK pk);
	
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
        PKEntity<?> that = (PKEntity<?>) obj;
        return null == this.getPK() ? false : this.getPK().equals(that.getPK());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getPK() ? 0 : getPK().hashCode() * 31;
        return hashCode;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
