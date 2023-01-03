package scrappydoo;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import scrappydoo.datasources.FetchUtils;
import scrappydoo.datasources.mapper.FinancialTimesEntry;
import scrappydoo.exception.PageFetchException;
import scrappydoo.exception.ScrapingException;
import scrappydoo.json.JsonObjectWriter;
import scrappydoo.scraping.HtmlScraper;

import java.util.List;

@Slf4j
public class App {
    public static void main(String[] args) throws ScrapingException, PageFetchException {
        Document page = FetchUtils.fetchUrl("https://www.ft.com/ft1000-2022");
        HtmlScraper<FinancialTimesEntry> scraper = HtmlScraper.of(FinancialTimesEntry.class);
        List<FinancialTimesEntry> scraped = scraper.scrape(page);

        JsonObjectWriter objectWriter = new JsonObjectWriter("./dataset/financial_times.json");
        objectWriter.writeObjectList(scraped);
    }
}
