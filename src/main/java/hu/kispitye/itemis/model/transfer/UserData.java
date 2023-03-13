package hu.kispitye.itemis.model.transfer;

import java.util.Locale;

import jakarta.persistence.AttributeConverter;

public class UserData {
	public String name;
	public String pwd;
	public String pwd2;
	public String locale;
	public Long id;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPwd2() {
		return pwd2;
	}
	public void setPwd2(String pwd2) {
		this.pwd2 = pwd2;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public static class LocaleConverter implements AttributeConverter<Locale, String> {

	    @Override
	    public String convertToDatabaseColumn(Locale locale) {
	        if (locale != null) {
	            return locale.toLanguageTag();
	        }
	        return null;
	    }

	    @Override
	    public Locale convertToEntityAttribute(String languageTag) {
	        if (languageTag != null && !languageTag.isEmpty()) {
	            return Locale.forLanguageTag(languageTag);
	        }
	        return null;
	    }
	}	
}
