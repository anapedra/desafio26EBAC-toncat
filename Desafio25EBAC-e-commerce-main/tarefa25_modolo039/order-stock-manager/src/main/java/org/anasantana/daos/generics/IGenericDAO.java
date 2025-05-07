package org.anasantana.daos.generics;



import org.anasantana.daos.pagination.PageResult;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IGenericDAO<T, ID extends Serializable> {

    T save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    T getOne(ID id);

    void deleteById(ID id);

    PageResult<T> findAllPaged(int page, int size);
}
