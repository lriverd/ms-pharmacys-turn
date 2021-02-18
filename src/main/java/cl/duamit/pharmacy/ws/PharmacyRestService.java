package cl.duamit.pharmacy.ws;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Luis Riveros
 * @version 1.0.0 - 28-11-2020
 * @since 1.0.0 - 28-11-2020
 */

@Service
@ConfigurationProperties(prefix = "pharmacy.turns")
public class PharmacyRestService extends RestService<List, Object> {
	@Override
	protected Class<List> type() {
		return List.class;
	}

	public List<PharmacyRest> getPharmacys(){
		//this.buildUrl();
		ResponseEntity<List<PharmacyRest>> rateResponse =
			getRestTemplate().exchange("https://farmanet.minsal.cl/index.php/ws/getLocalesTurnos",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<PharmacyRest>>() {
				});
		List<PharmacyRest> rates = rateResponse.getBody();
		return rates;
	}
}
