package cl.duamit.pharmacy.ws;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract class from restservices
 *
 * @author Luis Riveros
 * @version 1.0.0 - 01-02-2021
 * @since 1.0.0 - 01-02-2021
 */
@Slf4j
public abstract class RestService<Q, S> {

	private Map<String, String> pathParams = new HashMap<>();
	private Map<String, String> queryParams = new HashMap<>();
	private HttpHeaders httpHeaders;

	private String url;
	private String tmplUrl;
	private RestTemplate restTemplate;

	protected abstract Class<Q> type();

	public void pathParams(Map<String, String> pathParams) {
		this.pathParams = pathParams;
	}

	public void queryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}

	public void httpHeaders(HttpHeaders httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	/**
	 * @return
	 */
	public Q get() throws Exception {
		return exchange(null, HttpMethod.GET);
	}

	/**
	 * @param rq
	 * @return
	 */
	public Q get(S rq) throws Exception {
		return exchange(rq, HttpMethod.GET);
	}

	/**
	 * @param rq
	 * @return
	 */
	public Q post(S rq) throws Exception {
		return exchange(rq, HttpMethod.POST);
	}

	/**
	 * Method to exchange rest services
	 *
	 * @param rq
	 * @param httpMethod
	 * @return Q Class defined on class
	 */
	private Q exchange(S rq, HttpMethod httpMethod) throws Exception {
		this.buildUrl();

		if (Objects.isNull(this.httpHeaders) && Objects.nonNull(rq)) {
			this.httpHeaders = new HttpHeaders();
			this.httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		}

		HttpEntity<S> entity = new HttpEntity<>(rq, this.httpHeaders);

		try {
			ResponseEntity<Q> httpResponse = getRestTemplate().exchange(getUrl(), httpMethod, entity, type());
			return httpResponse.getBody();
		} catch (Exception e) {
			log.error("Error", e);
			throw e;
		}
	}

	/**
	 * Method that build the url based on tmplUrl and getting
	 * query and path params
	 */
	protected void buildUrl() {
		UriComponentsBuilder ucb = UriComponentsBuilder.fromUriString(this.tmplUrl);

		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			ucb.queryParam(entry.getKey(), entry.getValue());
		}

		this.url = ucb.buildAndExpand(pathParams).toString();
	}

	protected RestTemplate getRestTemplate() {
		if (this.restTemplate == null) {
			this.restTemplate = new RestTemplate();
		}
		this.configureRestTemplate();

		return this.restTemplate;
	}

	private void configureRestTemplate() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(10000);
		factory.setReadTimeout(10000);
		this.restTemplate.setRequestFactory(factory);
	}


	protected String getUrl() {
		return url;
	}

}
