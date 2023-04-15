package hu.kispitye.itemis.dao;

import java.util.Objects;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUtil;

@MappedSuperclass
public abstract class NamedEntity {
	@Id
	@GeneratedValue
	protected Long id;

	@Column(nullable = false, unique = true, length = 42)
	@NaturalId(mutable = true)
	protected String name;    	

	protected Object getKey() {
		String name = getName();
		return name == null?null : name.toLowerCase();
	}	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "["+getName()+"]";
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof NamedEntity)) return false;
		if (o==this) return true;
		NamedEntity entity = (NamedEntity)o;
		if (getId() != null && entity.getId() != null)  {
			if (!getId().equals(entity.getId())) return false;
			PersistenceUtil pu = Persistence.getPersistenceUtil();
			if (!pu.isLoaded(this, "name") || !pu.isLoaded(entity, "name")) return true;  
		}
		return Objects.equals(getKey(), entity.getKey());
	}
	
	@Override
	public int hashCode() {
		Object key = getKey();
		return key == null?0 : key.hashCode();
	}

}
