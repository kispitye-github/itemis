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
public abstract class NamedEntity<T extends NamedEntity<T>> {

	public static final String NAME_ATTRIBUTE = "name";
	public static final String ID_ATTRIBUTE = "id";
	
	@Id
	@GeneratedValue
	@Column(name = ID_ATTRIBUTE)
	protected Long id;

	@Column(name = NAME_ATTRIBUTE, nullable = false, unique = true, length = 42)
	@NaturalId(mutable = true)
	protected String name;    	

	protected Object getKey() {
		String name = getName();
		return name == null?null : name.toLowerCase();
	}	
	
	public String getName() {
		return name;
	}
	
	public NamedEntity<T> setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public String toString() {
		return "["+getName()+"]";
	}
	
	public Long getId() {
		return id;
	}
	
	public NamedEntity<T> setId(Long id) {
		this.id = id;
		return this;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof NamedEntity<?> entity)) return false;
		if (entity==this) return true;
		if (getId() != null && entity.getId() != null)  {
			if (!getId().equals(entity.getId())) return false;
			PersistenceUtil pu = Persistence.getPersistenceUtil();
			if (!pu.isLoaded(this, NAME_ATTRIBUTE) || !pu.isLoaded(entity, NAME_ATTRIBUTE)) return true;  
		}
		return Objects.equals(getKey(), entity.getKey());
	}
	
	@Override
	public int hashCode() {
		Object key = getKey();
		return key == null?0 : key.hashCode();
	}

}
