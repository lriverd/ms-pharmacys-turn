package cl.duamit.pharmacy.ws;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
		setConverter();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<List<PharmacyRest>> rateResponse =
			getRestTemplate().exchange("https://farmanet.minsal.cl/index.php/ws/getLocalesTurnos",
				HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<PharmacyRest>>() {
				});
		List<PharmacyRest> rates = rateResponse.getBody();
		return rates;
	}

	public void setConverter() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		messageConverters.add(converter);
		getRestTemplate().setMessageConverters(messageConverters);
	}
}
