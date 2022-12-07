package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public void add(Role user) {
        entityManager.persist(user);
    }

    @Override
    public Role findByIdRole(int id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public List<Role> listRoles() {
        return entityManager.createQuery("select l from Role l", Role.class).getResultList();
    }

    @Override
    public Role findByName(String name) {
        return entityManager.createQuery("select u from Role u where u.role = :id", Role.class)
                .setParameter("id", name)
                .getResultList().stream().findAny().orElse(null);
    }

    @Override
    public List<Role> listByName(List<String> name) {
        return  entityManager.createQuery("select u from Role u where u.role in (:id)", Role.class)
                .setParameter("id", name)
                .getResultList();
    }
}
