package scrappydoo.scraping;

import scrappydoo.exception.ScrapingException;

import java.util.List;
import java.util.function.Function;

public class HtmlScraper<T> {
	private final Function<String, List<T>> strategy;

	private HtmlScraper(Function<String, List<T>> strategy) {
		this.strategy = strategy;
	}

	public static <E> HtmlScraper<E> of(Class<E> scrapedClass) {
		Function<String, List<E>> strategy = ScrapeStrategyFactory.getInstance().ofClass(scrapedClass);
		return new HtmlScraper<>(strategy);
	}

	public List<T> scrape(String url) throws ScrapingException {
		List<T> scraped = strategy.apply(url);
		if (scraped == null) {
			throw new ScrapingException("Failed to scrape content from " + url);
		}
		return scraped;
	}
}
