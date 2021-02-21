package cl.duamit.pharmacy.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Luis Riveros
 * @version 1.0.0 - 01-02-2021
 * @since 1.0.0 - 01-02-2021
 */
//@Repository
@Slf4j
public class DataBaseUpdater {

	public static final String VERBOSE = "verbose";
	private static final String CHANGELOG_LOCATION = "classpath:/db/changelog/changelog-master.xml";

	@Autowired
	private ResourceLoader resourceLoader;

	/**
	 * Execute the update of liquibase to generate the database
	 *
	 * @return spring implementation of liquibase
	 */
	@Bean
	public SpringLiquibase liquibase() {

		Resource resource = resourceLoader.getResource(CHANGELOG_LOCATION);
		Assert.state(resource.exists(), "Unable to find file: " + CHANGELOG_LOCATION);

		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog(CHANGELOG_LOCATION);

		Map<String, String> params = new HashMap<>();
		params.put(VERBOSE, Boolean.TRUE.toString());
		liquibase.setChangeLogParameters(params);

		return liquibase;
	}

}
