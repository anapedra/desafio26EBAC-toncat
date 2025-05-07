package org.anasantana.daos.generics.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import org.anasantana.daos.generics.IUserDAO;
import org.anasantana.model.User;

import java.util.Optional;

public class UserDAOImpl extends GenericDAOImpl<User, Long> implements IUserDAO {

    public UserDAOImpl(EntityManager em) {
        super(User.class, em);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            User user = em.createQuery(
                    "SELECT u FROM User u WHERE LOWER(u.registrationEmail) = LOWER(:email)", User.class)
                .setParameter("email", email)
                .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
