package application;

public class User {
	int id;
	String firstName;
	String lastName;
	String address;
	int zip;
	String state;
	String username;
	String password;
	String email;
	int ssn;
	String securityQuestion;
	String securityAnswer;
	
	public User(
			int id, 
			String firstName, 
			String lastName, 
			String address, 
			int zip, 
			String state,
			String username,
			String password,
			String email,
			int ssn,
			String securityQuestion,
			String securityAnswer
	) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.zip = zip;
		this.state = state;
		this.username = username;
		this.password = password;
		this.email = email;
		this.ssn = ssn;
		this.securityQuestion = securityQuestion;
		this.securityAnswer = securityAnswer;
	}
	
	public String toString() {
		return this.firstName + " " + this.lastName;
	}
	
	public void changePassword(String newPassword) {
		//connect to db
		//find current row
		//edit row with new password
		//set this.password = newPassword
	}
	
}
