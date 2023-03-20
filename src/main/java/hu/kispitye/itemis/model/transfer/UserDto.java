package hu.kispitye.itemis.model.transfer;

import hu.kispitye.itemis.model.User;

public class UserDto {
	public String name;
	public String pwd;
	public String pwd2;
	public boolean admin;
	
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public UserDto(User user) {
		setName(user.getUsername());
		setAdmin(user.isAdmin());
	}
	public UserDto() {
	}
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
	
}
