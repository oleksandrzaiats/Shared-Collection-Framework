package se.lnu.application.model.dao;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import se.lnu.application.model.entity.CommonEntity;
import se.lnu.application.utils.Filtering;

import javax.persistence.Table;
import java.util.Collection;
import java.util.List;

/**
 * Abstract class for working with database
 *
 * @param <E> Entity class
 */
public abstract class AbstractDAO<E extends CommonEntity> extends HibernateDaoSupport {

    private Class<E> aClass;

    public AbstractDAO(Class<E> aClass) {
        this.aClass = aClass;
    }

    @Transactional
    public List<E> getList(List<Filtering> filteringList) {
        StringBuilder stringBuilder = getWhereFiltering(filteringList);
        List<E> list = (List<E>) getHibernateTemplate().find("FROM " + getTableName() + stringBuilder.toString());
        return list;
    }

    @Transactional
    public void mergeList(Collection<E> list) {
        for (E e : list) {
            getHibernateTemplate().merge(e);
        }
    }

    @Transactional
    public void saveList(Collection<E> list) {
        for (E e : list) {
            getHibernateTemplate().persist(e);
        }
    }

    @Transactional
    public E create(E entity) {
        getHibernateTemplate().persist(entity);
        return entity;
    }

    @Transactional
    public E update(E entity) {
        getHibernateTemplate().update(entity);
        return entity;
    }

    @Transactional
    public void delete(E entity) {
        getHibernateTemplate().delete(entity);
    }

    @Transactional
    public E get(Long id) {
        List<?> list = getHibernateTemplate().find("from " + getTableName() + " where id=?", id);
        if (list.size() > 0) {
            return (E) list.get(0);
        }
        return null;
    }

    protected String getTableName() {
        Table annotation = aClass.getAnnotation(Table.class);
        if (annotation != null) {
            return annotation.name();
        }
        throw new IllegalArgumentException("Entity class should have Table annotation with name attribute. Class name: " + aClass.getSimpleName());
    }

    protected StringBuilder getWhereFiltering(List<Filtering> filteringList) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (filteringList != null && !filteringList.isEmpty()) {
            stringBuilder.append(" WHERE ");
            for (Filtering filtering : filteringList) {
                String relation = "";
                if (filtering.getRelationWithPrevious() != null) {
                    relation = filtering.getRelationWithPrevious();
                }
                stringBuilder.append(" ").append(relation).append(" ")
                        .append(filtering.getColumnName()).append(" ")
                        .append(filtering.getOperator()).append(" ")
                        .append(filtering.getValue()).append(" ");
            }
        }
        return stringBuilder;
    }
}
