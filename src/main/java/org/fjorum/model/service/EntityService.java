package org.fjorum.model.service;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface EntityService<T> {

    Optional<T> getById(Long id);
    List<T> getAll();
    List<T> getAll(Sort sort);
    T save(T t);
    void delete(T t);
}
