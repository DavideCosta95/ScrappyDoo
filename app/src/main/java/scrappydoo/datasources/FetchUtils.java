package scrappydoo.datasources;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import scrappydoo.exception.PageFetchException;

public final class FetchUtils {
	private FetchUtils() {}

	public static Document fetchUrl(String url) throws PageFetchException {
		try {
			Connection connection = Jsoup.connect(url);
			return connection.get();
		} catch (Exception e) {
			throw new PageFetchException("Failed to fetch web page from URL=" + url, e);
		}
	}
}
