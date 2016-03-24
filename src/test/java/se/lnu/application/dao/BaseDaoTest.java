package se.lnu.application.dao;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import se.lnu.application.entity.CommonEntity;
import se.lnu.application.utils.Filtering;
import se.lnu.configuration.SpringRootConfig;

import java.util.Collection;
import java.util.List;

@Transactional(readOnly = false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRootConfig.class)
@WebAppConfiguration
@Ignore
public class BaseDaoTest<E extends CommonEntity> {

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
		Assert.assertNotNull(entity);
	}

	public void baseUpdate(E entity) {
		dao.update(entity);
		Assert.assertNotNull(entity);
	}

	public void baseDelete(E entity) {
		dao.delete(entity);
	}

	public E baseGet(Long id) {
		return dao.get(id);
	}
}
