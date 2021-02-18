package cl.duamit.pharmacy.service;

import cl.duamit.pharmacy.geolocation.GeolocationCalculator;
import cl.duamit.pharmacy.model.Coordinates;
import cl.duamit.pharmacy.model.Pharmacy;
import cl.duamit.pharmacy.ws.PharmacyRest;
import cl.duamit.pharmacy.ws.PharmacyRestService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Geolocation {

	private PharmacyRestService pharmacyRestService;

	@SneakyThrows
	public List<Pharmacy> getByGeolocation(Coordinates coordinates, Integer maxKmRatio) {
		List<Pharmacy> pharmacyList = new ArrayList<>();
		List<PharmacyRest> pharmacyRestList = pharmacyRestService.getPharmacys();

		List<PharmacyRest> filteredPharmacyRestList = pharmacyRestList.stream()
			.filter(pr -> GeolocationCalculator.distanceBetwenPoints(coordinates,
				Coordinates.builder()
					.latitude(Double.parseDouble(pr.getLocalLat()))
					.longitude(Double.parseDouble(pr.getLocalLng())).build()) <= maxKmRatio)
			.collect(Collectors.toList());

		filteredPharmacyRestList.forEach(f -> {
			pharmacyList.add(parsePharmacy(f));
		});

		return pharmacyList;
	}

	private Pharmacy parsePharmacy(PharmacyRest pharmacyRest) {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		Pharmacy response = new Pharmacy();
		response.setId(pharmacyRest.getLocalId());
		response.setName(pharmacyRest.getLocalNombre());
		response.setAddresse(pharmacyRest.getLocalDireccion());
		Coordinates c = Coordinates.builder()
			.latitude(Double.parseDouble(pharmacyRest.getLocalLat()))
			.longitude(Double.parseDouble(pharmacyRest.getLocalLng()))
			.build();
		response.setCoordinates(c);

		LocalDate date = LocalDate.now();

		LocalTime timeFrom = LocalTime.parse(pharmacyRest.getFuncionamientoHoraApertura(), timeFormatter);
		LocalTime timeTo = LocalTime.parse(pharmacyRest.getFuncionamientoHoraCierre(), timeFormatter);

		LocalDateTime from = LocalDateTime.of(date, timeFrom);
		LocalDateTime to = LocalDateTime.of(date, timeTo);

		if (to.isBefore(from)) {
			to = to.plusDays(1);
		}

		response.setOpenTime(from);
		response.setCloseTime(to);
		return response;
	}

	@Autowired
	public void setPharmacyRestService(PharmacyRestService pharmacyRestService) {
		this.pharmacyRestService = pharmacyRestService;
	}
}
