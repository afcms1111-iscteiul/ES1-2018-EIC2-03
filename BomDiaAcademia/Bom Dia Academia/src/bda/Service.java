package bda;

public enum Service {
	EMAIL, FACEBOOK, TWITTER;
	
	public String stringFormat() {
		return name().charAt(0) + name().substring(1).toLowerCase();
	}
}
