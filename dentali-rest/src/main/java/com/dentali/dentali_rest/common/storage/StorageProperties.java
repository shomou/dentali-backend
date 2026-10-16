package com.dentali.dentali_rest.common.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties (prefix = "dentali.storage")
public record StorageProperties(String rutaBase) {

}
