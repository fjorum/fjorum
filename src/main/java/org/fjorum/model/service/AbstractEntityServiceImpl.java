package org.fjorum.model.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractEntityServiceImpl<T> implements EntityService<T> {

    protected abstract JpaRepository<T, Long> getRepository();

    @Override
    public Optional<T> getById(Long id) {
        return Optional.ofNullable(getRepository().findOne(id));
    }

    @Override
    public List<T> getAll() {
        return getRepository().findAll();
    }

    @Override
    public List<T> getAll(Sort sort) {
        return getRepository().findAll(sort);
    }

    @Override
    public T save(T t) {
        return getRepository().save(t);
    }

    @Override
    public void delete(T t) {
        getRepository().delete(t);
    }
}
