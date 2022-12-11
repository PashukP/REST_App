package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;

    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Autowired
    public UserServiceImpl(RoleDao roleDao, UserDao userDao) {
        this.roleDao = roleDao;
        this.userDao = userDao;
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return user;
    }

    @Override
    public boolean addRole(Role role) {
        Role userPrimary = roleDao.findByName(role.getRole());
        if(userPrimary != null) {return false;}
        roleDao.add(role);
        return true;
    }

    @Override
    public Role findByNameRole(String name) {
        return roleDao.findByName(name);
    }

    @Override
    public LinkedHashSet<Role> listRoles() {
        return new LinkedHashSet<>(roleDao.listRoles());
    }

    @Override
    public Role findByIdRole(int id) {
        return roleDao.findByIdRole(id);
    }

    @Override
    public Set<Role> listByRole(List<String> name) {
        Set<Role> roles = new LinkedHashSet<>(roleDao.listByName(name));
        return roles;
    }

    @Override
    public boolean add(User user) {
        User userPrimary = userDao.findByName(user.getUsername());
        if(userPrimary != null) {return false;}
        user.setPassword(bCryptPasswordEncoder().encode(user.getPassword()));
        List<String> listS = user.getRoles().stream().map(r -> r.getRole()).collect(Collectors.toList());
        Set<Role> listR = listByRole(listS);
        user.setRoles(listR);
        userDao.add(user);
        return true;
    }

    @Override
    public Set<User> listUsers() {
        return userDao.listUsers();
    }

    @Override
    public void delete(int id) {
        userDao.delete(id);
    }

    @Override
    public void update(User user) {
        User userPrimary = findById(user.getId());
        if(!userPrimary.getPassword().equals(user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder().encode(user.getPassword()));
        }
        List<String> listS = user.getRoles().stream().map(r -> r.getRole()).collect(Collectors.toList());
        Set<Role> listR = listByRole(listS);
        user.setRoles(listR);
        userDao.update(user);
    }
    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }
    @Override
    public User findByUsername(String userName) {
        return userDao.findByName(userName);
    }
}

