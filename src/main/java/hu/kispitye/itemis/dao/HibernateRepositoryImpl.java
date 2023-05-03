package hu.kispitye.itemis.dao;


import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class HibernateRepositoryImpl<T> implements HibernateRepository<T> {

	@PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<T> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> S save(S entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> S persist(S entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public <S extends T> S merge(S entity) {
        return entityManager.merge(entity);
    }

    @Override
    public <S extends T> S update(S entity) {
    	return entityManager.merge(entity);
    }

}