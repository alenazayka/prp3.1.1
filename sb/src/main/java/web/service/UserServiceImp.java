package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDao;
import web.model.User;

import java.util.List;

@Service
public class UserServiceImp implements UserService {

   @Autowired
   private UserDao userDao;

   @Autowired
   private BCryptPasswordEncoder bCryptPasswordEncoder;

   @Transactional
   @Override
   public boolean addUser(User user) {
      user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
      userDao.addUser(user);
      return true;
   }

   @Transactional(readOnly = true)
   @Override
   public List<User> getAllUsers() {
      return userDao.getAllUsers();
   }

   @Transactional
   @Override
   public boolean deleteUser(User user) {
      userDao.deleteUser(user);
      return true;
   }

   @Transactional(readOnly = true)
   @Override
   public User getUserById(int id) {
      return userDao.getUserById(id);
   }

   @Transactional
   @Override
   public boolean updateUser(User user) {
      userDao.updateUser(user);
      return true;
   }

   @Transactional(readOnly = true)
   @Override
   public User getUserByLogin(String login) {
      return userDao.getUserByLogin(login);
   }

   @Transactional(readOnly = true)
   @Override
   public boolean isExistLogin(String login) {
      return userDao.isExistLogin(login);
   }

   @Override
   public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
      User user = userDao.getUserByLogin(s);
      if (user == null) {
         throw new UsernameNotFoundException("User not found");
      }
      return user;
   }
}
