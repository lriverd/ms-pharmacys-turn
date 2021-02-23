package cl.duamit.pharmacy.service;

import cl.duamit.pharmacy.geolocation.GeolocationCalculator;
import cl.duamit.pharmacy.model.Coordinates;
import cl.duamit.pharmacy.model.Pharmacy;
import cl.duamit.pharmacy.ws.PharmacyRest;
import cl.duamit.pharmacy.ws.PharmacyRestService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class Geolocation {

	private PharmacyRestService pharmacyRestService;

	@SneakyThrows
	public List<Pharmacy> getByGeolocation(Coordinates coordinates, Double maxKmRatio) {
		List<Pharmacy> pharmacyList = new ArrayList<>();
		List<PharmacyRest> pharmacyRestList = pharmacyRestService.getPharmacys();

		pharmacyRestList.forEach(pr -> {
			try {
				if (!Objects.toString(pr.getLocalLat(), "").isEmpty()) {
					double distance = GeolocationCalculator.distanceBetwenPoints(coordinates,
						Coordinates.builder()
							.latitude(Double.parseDouble(pr.getLocalLat().replaceAll("(^\\h*)|(\\h*$)","")))
							.longitude(Double.parseDouble(pr.getLocalLng().replaceAll("(^\\h*)|(\\h*$)",""))).build());
					if (distance <= maxKmRatio) {
						pharmacyList.add(parsePharmacy(pr, distance));
					}
				}
			}catch (Exception e){
				log.error("Error evaluating pharmacy", e);
			}
		});

		pharmacyList.sort(Comparator.comparing(Pharmacy::getDistanceKmFromOrigin));

		return pharmacyList;
	}

	private Pharmacy parsePharmacy(PharmacyRest pharmacyRest, Double distance) {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Pharmacy response = new Pharmacy();
		response.setId(pharmacyRest.getLocalId());
		response.setName(pharmacyRest.getLocalNombre());
		response.setAddresse(pharmacyRest.getLocalDireccion());
		Coordinates c = Coordinates.builder()
			.latitude(Double.parseDouble(pharmacyRest.getLocalLat()))
			.longitude(Double.parseDouble(pharmacyRest.getLocalLng()))
			.build();
		response.setCoordinates(c);

		LocalDate date = LocalDate.parse(pharmacyRest.getFecha(), dateFormatter);

		LocalTime timeFrom = LocalTime.parse(pharmacyRest.getFuncionamientoHoraApertura(), timeFormatter);
		LocalTime timeTo = LocalTime.parse(pharmacyRest.getFuncionamientoHoraCierre(), timeFormatter);

		LocalDateTime from = LocalDateTime.of(date, timeFrom);
		LocalDateTime to = LocalDateTime.of(date, timeTo);

		if (to.isBefore(from)) {
			to = to.plusDays(1);
		}

		response.setOpenAt(from);
		response.setCloseAt(to);

		response.setPhone(pharmacyRest.getLocalTelefono());

		response.setOpenNow(LocalDateTime.now().isAfter(from) && LocalDateTime.now().isBefore(to));

		response.setDistanceKmFromOrigin((double) (distance.intValue()) / 1000d);

		return response;
	}

	@Autowired
	public void setPharmacyRestService(PharmacyRestService pharmacyRestService) {
		this.pharmacyRestService = pharmacyRestService;
	}
}
