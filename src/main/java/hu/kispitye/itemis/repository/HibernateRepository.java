package hu.kispitye.itemis.repository;

import java.util.List;

public interface HibernateRepository<T> {

    //The findAll method will trigger an UnsupportedOperationException

    @Deprecated
    List<T> findAll();

    //Save methods will trigger an UnsupportedOperationException
    
    @Deprecated
    <S extends T> S save(S entity);

    @Deprecated
    <S extends T> List<S> saveAll(Iterable<S> entities);

    @Deprecated
    <S extends T> S saveAndFlush(S entity);

    @Deprecated
    <S extends T> List<S> saveAllAndFlush(Iterable<S> entities);

    //Persist methods are meant to save newly created entities

    <S extends T> S persist(S entity);

    //Merge methods are meant to propagate detached entity state changes
    //if they are really needed
    
    <S extends T> S merge(S entity);

    //Update methods are meant to force the detached entity state changes

    <S extends T> S update(S entity);

}