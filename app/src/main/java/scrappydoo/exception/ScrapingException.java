package scrappydoo.exception;

public class ScrapingException extends Exception {
	public ScrapingException() {
	}

	public ScrapingException(Throwable cause) {
		super(cause);
	}

	public ScrapingException(String message, Throwable cause) {
		super(message, cause);
	}
}
