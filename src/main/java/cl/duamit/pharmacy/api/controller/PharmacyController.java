package cl.duamit.pharmacy.api.controller;

import cl.duamit.pharmacy.model.Coordinates;
import cl.duamit.pharmacy.model.Pharmacy;
import cl.duamit.pharmacy.service.Geolocation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Luis Riveros
 * @version 1.0.0 - 04-08-2020
 * @since 1.0.0 - 04-08-2020
 */
@RestController
@RequestMapping("/v1/turn")
public class PharmacyController {

	private static final String BY_GEOLOCATION = "/by-geolocation";
	private Geolocation geolocation;

	@Operation(summary = "Get nearby pharmacys by geolocation coordinates")
	@ApiResponse(responseCode = "200", description = "Found a list of pharmacies",
	content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Pharmacy.class))})
	@GetMapping(value = BY_GEOLOCATION, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> getNearbyByCoordinates(@RequestParam(value = "lat") Double lat,
														  @RequestParam(value = "lng") Double lng,
														  @RequestParam(value = "radiusKm", defaultValue = "10") Double maxKmRadius,
														  HttpServletRequest request) throws Exception {

		Coordinates coordinates = Coordinates.builder()
			.latitude(lat)
			.longitude(lng)
			.build();

		return ok(geolocation.getByGeolocation(coordinates, maxKmRadius * 1000));
	}


	@Autowired
	public void setGeolocation(Geolocation geolocation) {
		this.geolocation = geolocation;
	}
}
