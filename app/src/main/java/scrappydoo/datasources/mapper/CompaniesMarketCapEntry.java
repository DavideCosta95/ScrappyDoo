package scrappydoo.datasources.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CompaniesMarketCapEntry {
	@JsonProperty("name")
	@NonNull
	private String name;

	@JsonProperty("market_capitalization_USD")
	@NonNull
	private String marketCapitalizationUSD;

	@JsonProperty("price")
	@NonNull
	private String price;

	@JsonProperty("country")
	@NonNull
	private String country;
}
