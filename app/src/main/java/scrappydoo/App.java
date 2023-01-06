package scrappydoo;

import lombok.extern.slf4j.Slf4j;
import scrappydoo.datasources.mapper.CompaniesMarketCapEntry;
import scrappydoo.datasources.mapper.DisfoldEntry;
import scrappydoo.datasources.mapper.FinancialTimesEntry;
import scrappydoo.datasources.mapper.ValueTodayEntry;
import scrappydoo.exception.ScrapingException;
import scrappydoo.json.JsonObjectWriter;
import scrappydoo.scraping.HtmlScraper;

import java.util.List;

@Slf4j
public class App {
    public static void main(String[] args) {
        makeDataset("https://www.ft.com/ft1000-2022", FinancialTimesEntry.class, "./dataset/financial_times.json");
        makeDataset("https://disfold.com/world/companies/", DisfoldEntry.class, "./dataset/disfold.json");
        makeDataset("https://companiesmarketcap.com/", CompaniesMarketCapEntry.class, "./dataset/companies_market_cap.json");
        makeDataset("https://www.value.today/?field_headquarters_of_company_target_id=All", ValueTodayEntry.class, "./dataset/value_today.json");
    }

    private static <T> void makeDataset(String datasourceUrl, Class<T> contentType, String filePath) {
        HtmlScraper<T> scraper = HtmlScraper.of(contentType);
        List<T> scraped;
        try {
            scraped = scraper.scrape(datasourceUrl);
        } catch (ScrapingException e) {
            log.error("", e);
            return;
        }

        JsonObjectWriter objectWriter = new JsonObjectWriter(filePath);
        objectWriter.writeObjectList(scraped);
    }
}
