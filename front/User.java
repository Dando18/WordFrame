package front;

import java.util.List;

public class User {
	
	public String username;
	public String password;
	public String email;
	public List<Project> projects;
	public long id;
	

	public User(String username, String password, String email, List<Project> projects, long id) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.projects = projects;
		this.id = id;
	}

}
