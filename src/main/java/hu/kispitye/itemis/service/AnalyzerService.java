package hu.kispitye.itemis.service;

import java.util.List;

public interface AnalyzerService {
	Result analyze(String q);
	
	public interface Result {
		List<String> getResult();
		List<Error> getErrors();
		public record Error(String sender, String key, Object... params) {};
	}

}
