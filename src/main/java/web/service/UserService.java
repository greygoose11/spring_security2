package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.model.Role;
import web.model.User;
import web.repository.RoleRepository;
import web.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
    @Transactional
    public void save(User user){
        Set<Role>roles = user.getRoles();
        if(roles==null){
            roles=new HashSet<>();
            roles.add(roleRepository.findById(1L).get());
            user.setRoles(roles);
        }
        userRepository.save(user);
    }

    @Transactional
    public List<User> listAll() {
        return (List<User>) userRepository.findAll();
    }
    @Transactional
    public User get(long id) {
        return userRepository.findById(id).get();
    }
    @Transactional
    public void delete(long id) {
        System.out.println("удаление юзера "+id);
        userRepository.deleteById(id);
    }
}
