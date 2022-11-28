package com.tryton.tut.tut_spring_kafka_streams_travis_credits.config;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.StandardServletEnvironment;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CommonsLog
@Component
public class PropertiesLoaderConfigurer extends PropertySourcesPlaceholderConfigurer {

    private static final String ENVIRONMENT_PROPERTIES = "environmentProperties";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        super.postProcessBeanFactory(beanFactory);
        PropertySource<?> propertySource = super.getAppliedPropertySources().get(ENVIRONMENT_PROPERTIES);
        if (propertySource == null) {
            log.info("No properties loaded, very unlikely...");
            return;
        }

        StandardServletEnvironment propertySources = (StandardServletEnvironment) propertySource.getSource();

        log.info("Loaded properties start");
        propertySources.getPropertySources().forEach(property -> {
            if (property.getSource() instanceof Map) {
                // it will print systemProperties, systemEnvironment, application.properties and other overrides of
                // application.properties
                log.info("#######" + property.getName() + "#######");
                @SuppressWarnings("unchecked")
                Map<String, String> properties = mapValueAsString((Map<String, Object>) property.getSource());
                log.info(properties);
            }
        });
        log.info("Loaded properties end");
    }

    private static Map<String, String> mapValueAsString(Map<String, Object> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> toString(entry.getValue())));
    }

    private static String toString(Object object) {
        return Optional.ofNullable(object)
                .map(Object::toString)
                .orElse(null);
    }
}
