package cl.duamit.pharmacy.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Coordinates {
	private double latitude;
	private double longitude;
	private double altitude;
}
