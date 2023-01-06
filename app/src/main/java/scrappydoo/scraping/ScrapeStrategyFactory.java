package scrappydoo.scraping;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scrappydoo.datasources.FetchUtils;
import scrappydoo.datasources.mapper.DisfoldEntry;
import scrappydoo.datasources.mapper.FinancialTimesEntry;
import scrappydoo.exception.PageFetchException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class ScrapeStrategyFactory {
	private static ScrapeStrategyFactory instance;
	private final Map<Class<?>, Function<String, ?>> strategies;

	private ScrapeStrategyFactory() {
		this.strategies = new HashMap<>();
		this.strategies.put(FinancialTimesEntry.class, s -> {
			Document page;
			try {
				page = FetchUtils.fetchUrl(s);
			} catch (PageFetchException e) {
				return null;
			}

			List<FinancialTimesEntry> scraped = new ArrayList<>(1000);
			Elements rows = page.select(".o-table > tbody > tr");
			for (Element element : rows) {
				Elements columns = element.select("td");
				String name = columns.get(1).text();
				String country = columns.get(4).text();
				String sector = columns.get(5).text();
				String revenue2020EU = columns.get(8).text().replace(",", "");
				String revenue2017EU = columns.get(9).text().replace(",", "");
				String employeesNumber2020 = columns.get(10).text().replace(",", "");
				String employeesNumber2017 = columns.get(11).text().replace(",", "");
				String foundingYear = columns.get(12).text();
				scraped.add(new FinancialTimesEntry(name, country, sector, revenue2020EU, revenue2017EU, employeesNumber2020, employeesNumber2017, foundingYear));
			}
			return scraped;
		});
		this.strategies.put(DisfoldEntry.class, s -> {
			Document page;
			List<DisfoldEntry> scraped = new ArrayList<>(1000);
			for (int i = 1; i <= 20; i++) {
				log.info("{}", i);
				try {
					page = FetchUtils.fetchUrl(s + "?page=" + i);
				} catch (PageFetchException e) {
					return null;
				}
				Elements rows = page.select("table > tbody > tr");
				for (Element element : rows) {
					Elements columns = element.select("td");
					if (columns.size() == 1) {
						continue;
					}
					String name = columns.get(1).text();
					String marketCapitalizationUSD = sanitizeCurrency(columns.get(2).text());
					String stock = columns.get(3).text();
					String country = columns.get(4).text();
					String sector = columns.get(5).text();
					String industry = columns.get(6).text();
					scraped.add(new DisfoldEntry(name, marketCapitalizationUSD, stock, country, sector, industry));
				}
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
	public <T> Function<String, List<T>> ofClass(Class<T> clazz) {
		return (Function<String, List<T>>) strategies.get(clazz);
	}

	private String sanitizeCurrency(String s) {
		s = s.replace("$", "")
				.replace("€", "")
				.replace(" ", "");
		int exponent = 0;
		if (s.contains("T")) {
			exponent = 12;
			s = s.substring(0, s.indexOf("T"));
		} else if (s.contains("B")) {
			exponent = 9;
			s = s.substring(0, s.indexOf("B"));
		} else if (s.contains("M")) {
			exponent = 6;
			s = s.substring(0, s.indexOf("M"));
		}
		double value = Double.parseDouble(s);
		return String.valueOf(Math.round(value * Math.pow(10, exponent)));
	}
}
