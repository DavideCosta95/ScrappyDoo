package scrappydoo.datasources.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class FinancialTimesEntry {
	@JsonProperty("name")
	@NonNull
	private String name;

	@JsonProperty("country")
	@NonNull
	private String country;

	@JsonProperty("sector")
	@NonNull
	private String sector;

	@JsonProperty("revenue_2020")
	@NonNull
	private String revenue2020;

	@JsonProperty("revenue_2017")
	@NonNull
	private String revenue2017;

	@JsonProperty("employees_number_2020")
	@NonNull
	private String employeesNumber2020;

	@JsonProperty("employees_number_2017")
	@NonNull
	private String employeesNumber2017;

	@JsonProperty("founding_year")
	@NonNull
	private String foundingYear;
}
