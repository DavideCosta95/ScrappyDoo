package scrappydoo.scraping;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scrappydoo.datasources.FetchUtils;
import scrappydoo.datasources.mapper.CompaniesMarketCapEntry;
import scrappydoo.datasources.mapper.DisfoldEntry;
import scrappydoo.datasources.mapper.FinancialTimesEntry;
import scrappydoo.datasources.mapper.ValueTodayEntry;
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
				log.error("Cannot fetch url={}", s, e);
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
				String url = s + "?page=" + i;
				try {
					page = FetchUtils.fetchUrl(url);
				} catch (PageFetchException e) {
					log.error("Cannot fetch url={}", url, e);
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

		this.strategies.put(CompaniesMarketCapEntry.class, s -> {
			Document page;
			List<CompaniesMarketCapEntry> scraped = new ArrayList<>(8000);
			for (int i = 1; i <= 72; i++) {
				String url = s + "page/" + i + "/";
				try {
					page = FetchUtils.fetchUrl(url);
				} catch (PageFetchException e) {
					log.error("Cannot fetch url={}", url, e);
					return null;
				}
				Elements rows = page.select(".marketcap-table > tbody > tr");
				for (Element element : rows) {
					Elements columns = element.select("td");
					if (columns.size() == 1) {
						continue;
					}
					String name = columns.get(1).select(".company-name").text();
					String marketCapitalizationUSD = sanitizeCurrency(columns.get(2).text());
					String price = sanitizeCurrency(columns.get(3).text());
					String country = columns.get(6).text();
					scraped.add(new CompaniesMarketCapEntry(name, marketCapitalizationUSD, price, country));
				}
			}
			return scraped;
		});

		this.strategies.put(ValueTodayEntry.class, s -> {
			Document page;
			List<ValueTodayEntry> scraped = new ArrayList<>(11000);
			for (int i = 0; i <= 1068; i++) {
				log.info("{}", i);
				String url = s + "&page=" + i;
				try {
					page = FetchUtils.fetchUrl(url);
				} catch (PageFetchException e) {
					log.error("Cannot fetch url={}", url, e);
					return null;
				}
				Elements rows = page.select(".item-list > ol > li");
				for (Element element : rows) {
					String name = element.select("h1").text();
					String annualRevenueUSD = sanitizeCurrency(element.select(".field--name-field-revenue-in-usd").select(".field--item").text());
					String annualNetIncomeUSD = sanitizeCurrency(element.select(".field--name-field-net-income-in-usd").select(".field--item").text());
					String marketCapitalization2022 = sanitizeCurrency(element.select(".field--name-field-market-value-jan072022").select(".field--item").text());
					String employeesNumber = element.select(".field--name-field-employee-count").select(".field--item").text().replace(",", "");
					String ceo = element.select(".field--name-field-ceo").select(".field--item").text();
					String headquartersCountry = element.select(".field--name-field-headquarters-of-company").select(".field--item").text();
					String wikipediaPageUrl = element.select(".field--name-field-wikipedia").select("a").attr("href");
					String twitterPageUrl = element.select(".field--name-field-twitter").select("a").attr("href");
					String facebookPageUrl = element.select(".field--name-field-facebook").select("a").attr("href");
					scraped.add(new ValueTodayEntry(name, annualRevenueUSD, annualNetIncomeUSD, marketCapitalization2022,
							employeesNumber, ceo, headquartersCountry, wikipediaPageUrl, twitterPageUrl, facebookPageUrl));
				}
			}
			log.info("{}", scraped.size());
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
				.replace("USD", "")
				.replace(" ", "")
				.replace(",", "");
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
		try {
			double value = Double.parseDouble(s);
			return String.valueOf(Math.round(value * Math.pow(10, exponent)));
		} catch (NumberFormatException | NullPointerException e) {
			return "";
		}
	}
}
