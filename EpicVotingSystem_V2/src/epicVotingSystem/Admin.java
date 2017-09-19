package epicVotingSystem;

/**
* This class is used to store and retrieve data located in admin.txt
* @author  John R. McLaren
* @version 1.0
* @since   01/09/2016
*/

public class Admin {
	private int id;
	private String name, userName, password;
	
	public Admin(int id, String name, String userName, String password){
		this.id = id;
		this.name = name;
		this.userName = userName;
		this.password = password;
	}
	public void setID(int ID){
		this.id = ID;
	}
	public void setName(String Name){
		this.name = Name;
	}
	public void setUsername(String Username){
		this.userName = Username;
	}
	public void setPassword(String Password){
		this.password = Password;
	}
	public int getID(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getUsername(){
		return userName;
	}
	public String getPassword(){
		return password;
	}
	
}
