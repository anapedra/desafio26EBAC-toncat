package org.anasantana.daos.generics;

import org.anasantana.model.Category;

import java.util.List;

public interface ICategoryDAO extends IGenericDAO<Category, Long> {

    List<Category> findByDescription(String description);
}
