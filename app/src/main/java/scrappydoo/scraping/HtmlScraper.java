package scrappydoo.scraping;

import org.jsoup.nodes.Document;
import scrappydoo.exception.ScrapingException;

import java.util.List;
import java.util.function.Function;

public class HtmlScraper<T> {
	private final Function<Document, List<T>> strategy;

	private HtmlScraper(Function<Document, List<T>> strategy) {
		this.strategy = strategy;
	}

	public static <E> HtmlScraper<E> of(Class<E> scrapedClass) {
		Function<Document, List<E>> strategy = ScrapeStrategyFactory.getInstance().ofClass(scrapedClass);
		return new HtmlScraper<>(strategy);
	}

	public List<T> scrape(Document document) throws ScrapingException {
		List<T> scraped = strategy.apply(document);
		if (scraped == null) {
			throw new ScrapingException();
		}
		return scraped;
	}
}
