package scrappydoo.exception;

public class PageFetchException extends Exception {
	public PageFetchException(Throwable cause) {
		super(cause);
	}

	public PageFetchException(String message, Throwable cause) {
		super(message, cause);
	}
}
