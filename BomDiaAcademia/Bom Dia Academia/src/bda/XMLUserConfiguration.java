package bda;

import java.io.IOException;

public class XMLUserConfiguration {
	
	private boolean saveInformation = false;
	private int typeOfService; // 0 = email, 1 = facebook, 2 = twitter;
	private String username = "";
	private String password = "";
	
	public XMLUserConfiguration(boolean saveInformation, int typeOfService, String username, String password) throws IOException {
		if(typeOfService < 0 || typeOfService > 2) {
			throw new IOException("There are only type = 0 for email, type = 1 for facebook, type = 2 for twitter!");
		}
		this.saveInformation = saveInformation;
		this.typeOfService = typeOfService;
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
	
	public int getTypeOfService() {
		return typeOfService;
	}
	
	public String getTypeOfServiceInString() {
		String typeOfServiceInString;
		switch (typeOfService) {
		case 0:
			typeOfServiceInString = "Email";
			break;
			
		case 1:
			typeOfServiceInString = "Facebook";
			break;
			
		case 2:
			typeOfServiceInString = "Tweeter";
			break;

		default:
			typeOfServiceInString = "Other";
			break;
		}
		return typeOfServiceInString;
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
		+ "Type of Service: " + getTypeOfServiceInString() + System.getProperty("line.separator")
		+ "Username: " + getUsername() + System.getProperty("line.separator")
		+ "Password: " + getPassword() + System.getProperty("line.separator");
	}

}
