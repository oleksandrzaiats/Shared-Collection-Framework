package com.scf.server.application.model.dao;

import com.scf.server.application.model.entity.CommonEntity;
import com.scf.server.application.utils.Filtering;
import com.scf.server.configuration.SpringRootConfig;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional(readOnly = false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRootConfig.class)
@WebAppConfiguration
@Ignore
public abstract class BaseDaoTest<E extends CommonEntity>  extends Assert {

	@SuppressWarnings(value = "SpringJavaAutowiringInspection")
	@Autowired
	public AbstractDAO<E> dao;

	public List<E> baseGetList(List<Filtering> filteringList) {
		List<E> list = dao.getList(filteringList);
		Assert.assertNotNull(list);

		return list;
	}

	public void baseMergeList(Collection<E> list) {
		dao.mergeList(list);
	}

	public void baseSaveList(Collection<E> list) {
		dao.saveList(list);
	}

	public void baseCreate(E entity) {
		dao.create(entity);
		assertNotNull(entity);
	}

	public void baseUpdate(E entity) {
		dao.update(entity);
		assertNotNull(entity);
	}

	public void baseDelete(E entity) {
		dao.delete(entity);
	}

	public E baseGet(Long id) {
		return dao.get(id);
	}
}
