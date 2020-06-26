package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.dao.RoleDao;
import web.model.Role;

@Service
public class RoleServiceImp implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public Role getRoleByName(String name) {
        return roleDao.findByName(name);
    }
}
