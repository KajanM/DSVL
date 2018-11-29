package com.dsvl.flood.service;

import com.dsvl.flood.model.Log;
import org.springframework.data.repository.CrudRepository;

/**
 * Implementations for all the methods inside {@link CrudRepository}
 * will be implemented by {@code SpringBoot} on the fly.
 */
public interface LogRepository extends CrudRepository<Log, Long> {
}
