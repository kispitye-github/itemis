package hu.kispitye.itemis.model.transfer;

public class DbDto {
	public String getUrl() {
		return url;
	}
	public DbDto setUrl(String url) {
		this.url = url;
		return this;
	}
	public String getUser() {
		return user;
	}
	public DbDto setUser(String user) {
		this.user = user;
		return this;
	}
	public String getPwd() {
		return pwd;
	}
	public DbDto setPwd(String pwd) {
		this.pwd = pwd;
		return this;
	}
	public String url;
	public String user;
	public String pwd;
}
