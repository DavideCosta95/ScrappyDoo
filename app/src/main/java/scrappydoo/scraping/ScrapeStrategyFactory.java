package scrappydoo.scraping;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scrappydoo.datasources.mapper.FinancialTimesEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ScrapeStrategyFactory {
	private static ScrapeStrategyFactory instance;
	private final Map<Class<?>, Function<Document, ?>> strategies;

	private ScrapeStrategyFactory() {
		this.strategies = new HashMap<>();
		this.strategies.put(FinancialTimesEntry.class, a -> {
			List<FinancialTimesEntry> scraped = new ArrayList<>(1000);
			Elements rows = a.select(".o-table > tbody > tr");
			for (Element element : rows) {
				Elements columns = element.select("td");
				String name = columns.get(1).text();
				String country = columns.get(4).text();
				String sector = columns.get(5).text();
				String revenue2020 = columns.get(8).text().replace(",", "");
				String revenue2017 = columns.get(9).text().replace(",", "");
				String employeesNumber2020 = columns.get(10).text().replace(",", "");
				String employeesNumber2017 = columns.get(11).text().replace(",", "");
				String foundingYear = columns.get(12).text();
				scraped.add(new FinancialTimesEntry(name, country, sector, revenue2020, revenue2017, employeesNumber2020, employeesNumber2017, foundingYear));
			}
			return scraped;
		});

	}

	public static ScrapeStrategyFactory getInstance() {
		if (instance == null) {
			instance = new ScrapeStrategyFactory();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T> Function<Document, List<T>> ofClass(Class<T> clazz) {
		return (Function<Document, List<T>>) strategies.get(clazz);
	}
}
