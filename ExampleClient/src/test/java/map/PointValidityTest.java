package map;

import java.text.MessageFormat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PointValidityTest {

	@ParameterizedTest
	@CsvSource({ "0, 1, true", "0, -1, false", "1000, 1, false" })
	void generatePoint_isValid_ShouldBeEqualToValidityBoolean(int x, int y, Boolean valid) {
		Point p = new Point(x, y);
		Assertions.assertEquals(valid, p.isValid(p),
				MessageFormat.format("Expected {0} when when checking if Point ({1},{2}) is valid.", valid, x, y));
	}

}
