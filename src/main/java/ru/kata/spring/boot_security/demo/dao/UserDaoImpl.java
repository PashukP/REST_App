package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedHashSet;
import java.util.Set;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getByName(String username) {
        return entityManager.createQuery("select u from User u join fetch u.roles where u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList().stream().findAny().orElse(null);
    }

    @Override
    public  void delete(int id) {
        entityManager.remove(getById(id));
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    public boolean add(User user) {
        entityManager.persist(user);
        return true;
    }

    @Override
    public Set<User> getListUsers() {
        return new LinkedHashSet<>(entityManager.createQuery("select s from User s", User.class).getResultList());
    }

    @Override
    public User getById(int id) {
        return entityManager.find(User.class, id);
    }
}