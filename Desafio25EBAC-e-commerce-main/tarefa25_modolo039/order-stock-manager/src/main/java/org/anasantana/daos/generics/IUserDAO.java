package org.anasantana.daos.generics;

import org.anasantana.model.User;

import java.util.Optional;

public interface IUserDAO extends  IGenericDAO<User, Long>{
    Optional<User> findByEmail(String email);
}
