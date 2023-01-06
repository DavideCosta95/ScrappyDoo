package scrappydoo.datasources.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class DisfoldEntry {
	@JsonProperty("name")
	@NonNull
	private String name;

	@JsonProperty("market_capitalization_USD")
	@NonNull
	private String marketCapitalizationUSD;

	@JsonProperty("stock")
	@NonNull
	private String stock;

	@JsonProperty("country")
	@NonNull
	private String country;

	@JsonProperty("sector")
	@NonNull
	private String sector;

	@JsonProperty("industry")
	@NonNull
	private String industry;
}
