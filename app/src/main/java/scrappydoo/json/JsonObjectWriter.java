package scrappydoo.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j
public class JsonObjectWriter {
	private final String filePath;

	public JsonObjectWriter(String filePath) {
		this.filePath = filePath;
	}

	public void writeObjectList(List<?> objects) {
		File file = new File(filePath);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(file, objects);
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
