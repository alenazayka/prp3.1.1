package web.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import web.model.Role;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class RoleDaoImp implements RoleDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Role findByName(String name) {
        TypedQuery<Role> query = entityManager
                .createQuery("select r from Role r WHERE r.name= :name", Role.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findAny().orElse(null);

    }
}
