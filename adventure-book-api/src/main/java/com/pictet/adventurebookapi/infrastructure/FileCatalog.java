package com.pictet.adventurebookapi.infrastructure;

import org.springframework.core.io.Resource;

import java.util.Collection;

public record FileCatalog(Collection<Resource> resources) {
}
