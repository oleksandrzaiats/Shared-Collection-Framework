package se.lnu.application.dao;

import org.springframework.transaction.annotation.Transactional;
import se.lnu.application.entity.CommonEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.Collection;
import java.util.List;

/**
 * Abstract class for working with database
 *
 * @param <E> Entity class
 */
public class AbstractDAO<E extends CommonEntity> {

    Class<E> aClass;
    @PersistenceContext
    private EntityManager entityManager;

    public AbstractDAO(Class<E> aClass) {
        this.aClass = aClass;
    }

    @Transactional
    public List<E> getList() {
        Query query = entityManager.createQuery("FROM " + getTableName());
        List<?> list = query.getResultList();
        return (List<E>) list;
    }

    @Transactional
    public void mergeList(Collection<E> list) {
        for (E e : list) {
            entityManager.merge(e);
        }
    }

    @Transactional
    public void saveList(Collection<E> list) {
        for (E e : list) {
            entityManager.persist(e);
        }
    }

    @Transactional
    public E create(E entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Transactional
    public E update(E entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Transactional
    public void delete(E entity) {
        entityManager.remove(entity);
    }

    @Transactional
    public E get(Long id) {
        return entityManager.<E>find(aClass, id);
    }

    private String getTableName() {
        Table annotation = aClass.getAnnotation(Table.class);
        if (annotation != null) {
            String name = annotation.name();
            return name;
        }
        throw new IllegalArgumentException("Entity class should have Table annotation with name attribute. Class name: " + aClass.getSimpleName());
    }
}
