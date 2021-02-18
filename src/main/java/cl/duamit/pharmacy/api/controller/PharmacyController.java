package cl.duamit.pharmacy.api.controller;

import cl.duamit.pharmacy.model.Coordinates;
import cl.duamit.pharmacy.service.Geolocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Luis Riveros
 * @version 1.0.0 - 04-08-2020
 * @since 1.0.0 - 04-08-2020
 */
@RestController
@RequestMapping("/pharmacy")
public class PharmacyController {

	private Geolocation geolocation;

	private static final String BY_GEOLOCATION = "/turn/geolocation";

	@GetMapping(value = BY_GEOLOCATION, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> getValidUser(@RequestParam(value = "lat") Double lat,
											   @RequestParam(value = "lng") Double lng,
											   @RequestParam(value = "radius", defaultValue = "10") Integer maxKmRadius,
											   HttpServletRequest request) throws Exception {

		Coordinates coordinates = Coordinates.builder()
			.latitude(lat)
			.longitude(lng)
			.build();

		return ResponseEntity.ok(geolocation.getByGeolocation(coordinates, maxKmRadius));
	}


	@Autowired
	public void setGeolocation(Geolocation geolocation) {
		this.geolocation = geolocation;
	}
}
