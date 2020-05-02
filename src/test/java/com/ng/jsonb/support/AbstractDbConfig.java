package com.ng.jsonb.support;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:/test-app.properties")
@EntityScan(
    basePackages = {
        "com.ng.jsonb.model"
    })
@EnableJpaRepositories(
    basePackages = {
        "com.ng.jsonb.rest"
    })
@Import(HsqlDataSourceConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractDbConfig {

}
