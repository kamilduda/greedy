package org.kduda.greedy.unit.algorithm.m;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.Test;
import org.kduda.greedy.algorithm.DecisionTableFactory;
import org.kduda.greedy.algorithm.HeuristicsM;
import org.kduda.greedy.spark.reader.csv.SparkCsvReader;
import org.kduda.greedy.unit.SpringUnitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import scala.Option;
import scala.Some;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HeuristicsMSpecialCasesTest extends SpringUnitTest {

	@Autowired private SparkCsvReader sparkCsvReader;

	@Test
	public void shouldNotGenerateFromDegeneratedDecisionTable() throws IOException {
		scala.collection.immutable.Map<String, Dataset<Row>> dtsMapped = readTestFile("paper-sample.csv");

		scala.collection.immutable.Map<String, Option<Dataset<Row>>> result = HeuristicsM.calculateDecisionRules(dtsMapped);

		assertThat(result).isNotNull();
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get("f3")).isEqualTo(Some.apply(Option.empty()));
	}

	@Test
	public void shouldNotGenerateFromEmptyDecisionTable() throws IOException {
		scala.collection.immutable.Map<String, Dataset<Row>> dtsMapped = readTestFile("degenerated.csv");

		scala.collection.immutable.Map<String, Option<Dataset<Row>>> result = HeuristicsM.calculateDecisionRules(dtsMapped);

		assertThat(result).isNotNull();
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get("f3")).isEqualTo(Some.apply(Option.empty()));
	}

	private scala.collection.immutable.Map<String, Dataset<Row>> readTestFile(String filename) throws IOException {
		ClassPathResource resource = new ClassPathResource("/files/" + filename, getClass());
		File file = new File(resource.getURL().getPath());

		Map<String, String> options = new HashMap<>();
		options.put("header", "true");

		Dataset<Row> dts = sparkCsvReader.read(file, options);
		//noinspection unchecked
		return DecisionTableFactory.createMapOf(new Dataset[]{dts});
	}

}
