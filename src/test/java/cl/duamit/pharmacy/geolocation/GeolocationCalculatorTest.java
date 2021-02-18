package cl.duamit.pharmacy.geolocation;

import cl.duamit.pharmacy.model.Coordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeolocationCalculatorTest {

	@Test
	public void Should_Return_Distance_Between_Points() {
		Coordinates point1 = Coordinates.builder()
			.latitude(-33.36686)
			.longitude(-70.47649)
			.build();

		Coordinates point2 = Coordinates.builder()
			.latitude(-33.41452)
			.longitude(-70.45336)
			.build();

		assertEquals(5718.093280682832, GeolocationCalculator.distanceBetwenPoints(point1, point2));
	}
}
