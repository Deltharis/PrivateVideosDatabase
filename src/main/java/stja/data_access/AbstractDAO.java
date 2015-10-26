package stja.data_access;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Michal on 2015-10-17.
 */
public abstract class AbstractDAO<T> {

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    protected abstract Class<T> getEntityClass();

    public List<T> getAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
        Root<T> rootEntry = cq.from(getEntityClass());
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    public void remove(T obj) {
        em.remove(em.contains(obj) ? obj : em.merge(obj));;
    }

    public T merge(T obj) {
        return em.merge(obj);
    }

    public void persist(T obj) {
        em.persist(obj);
    }
}
