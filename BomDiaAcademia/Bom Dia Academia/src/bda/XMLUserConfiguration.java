package bda;

public class XMLUserConfiguration {
	
	private boolean saveInformation = false;
	private String username = "";
	private String password = "";
	
	public XMLUserConfiguration(boolean saveInformation, String username, String password) {
		this.saveInformation = saveInformation;
		this.username = username;
		this.password = password;
	}
	
	public boolean isInformationSaved() {
		return saveInformation;
	}
	
	private int informationSavedAsInt() {
		if(saveInformation) {
			return 1; // true
		} else {
			return 0; // false
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	@Override
	public String toString() {
		return "Save Information: " + informationSavedAsInt() + System.getProperty("line.separator") 
		+ "Username: " + getUsername() + System.getProperty("line.separator")
		+ "Password: " + getPassword() + System.getProperty("line.separator");
	}

}
