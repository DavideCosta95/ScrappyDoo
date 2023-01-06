package scrappydoo.datasources.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ValueTodayEntry {
	@JsonProperty("name")
	@NonNull
	private String name;

	@JsonProperty("annual_revenue_USD")
	@NonNull
	private String annualRevenueUSD;

	@JsonProperty("annual_net_income_USD")
	@NonNull
	private String annualNetIncomeUSD;

	@JsonProperty("market_capitalization_2022")
	@NonNull
	private String marketCapitalization2022;

	@JsonProperty("employees_number")
	@NonNull
	private String employeesNumber;

	@JsonProperty("CEO")
	@NonNull
	private String ceo;

	@JsonProperty("headquarters_country")
	@NonNull
	private String headquartersCountry;

	@JsonProperty("wikipedia_page_url")
	@NonNull
	private String wikipediaPageUrl;

	@JsonProperty("twitter_page_url")
	@NonNull
	private String twitterPageUrl;

	@JsonProperty("facebook_page_url")
	@NonNull
	private String facebookPageUrl;
}
