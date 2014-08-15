package parserBro;

public class NotFoundException extends Exception {
	private String showName;
	private Exception e;

	public NotFoundException() {
	}

	public NotFoundException(Exception e) {
		this.e = e;
	}

	public NotFoundException(String showName) {
		this.showName = showName;
	}
}
