package cl.duamit.pharmacy.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Pharmacy {
	private String id;
	private String name;
	private String addresse;
	private String phone;
	private Coordinates coordinates;
	private LocalDateTime openTime;
	private LocalDateTime closeTime;
}
