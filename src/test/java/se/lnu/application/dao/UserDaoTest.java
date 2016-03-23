package se.lnu.application.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import se.lnu.application.entity.UserEntity;
import se.lnu.application.utils.Filtering;

import java.util.ArrayList;
import java.util.List;

public class UserDaoTest extends BaseDaoTest<UserEntity> {

    private UserEntity entity;

    @Before
    public void setUp() {
        entity = InitialValue.getUserEntity();
    }

    @Test
    public void testAdd() {
        this.baseCreate(entity);

        Assert.assertNotNull(this.baseGet(entity.getId()));
    }

    @Test
    public void testGetList() {
        this.baseCreate(entity);

        List<UserEntity> list = this.baseGetList(new ArrayList<>());

        Assert.assertFalse(list.isEmpty());
        Assert.assertNotNull(list.get(0));
        Assert.assertTrue(list.get(0).getName().equals("test"));
    }

    @Test
    public void testMergeList() {
        this.baseCreate(entity);

        List<UserEntity> listOld = this.baseGetList(new ArrayList<>());
        Assert.assertFalse(listOld.isEmpty());

        entity.setName("new_test");
        List<UserEntity> listAdd = new ArrayList<>();
        listAdd.add(entity);
        this.baseMergeList(listAdd);

        List<UserEntity> listNew = this.baseGetList(new ArrayList<>());

        Assert.assertFalse(listNew.isEmpty());
        Assert.assertTrue(listNew.size() == listOld.size());
        Assert.assertEquals(this.baseGet(entity.getId()).getName(), "new_test");
    }

    @Test
    public void testSaveList() {
        this.baseCreate(entity);

        List<UserEntity> listOld = this.baseGetList(new ArrayList<>());
        Assert.assertFalse(listOld.isEmpty());

        UserEntity newEntity = InitialValue.getUserEntity();
        List<UserEntity> listAdd = new ArrayList<>();
        listAdd.add(newEntity);
        this.baseSaveList(listAdd);

        List<UserEntity> listNew = this.baseGetList(new ArrayList<>());

        Assert.assertFalse(listNew.isEmpty());
        Assert.assertTrue(listNew.size() > listOld.size());
        Assert.assertTrue(listNew.size() - listOld.size() == 1);
    }

    @Test
    public void testCreate() {
        this.baseCreate(entity);
        Assert.assertNotNull(entity.getId());
    }

    @Test
    public void testUpdate() {
        this.baseCreate(entity);

        entity.setName("new_test_v");
        this.baseUpdate(entity);

        Assert.assertNotNull(entity);
        Assert.assertEquals(this.baseGet(entity.getId()).getName(), "new_test_v");
    }

    @Test
    public void testDelete() {
        this.baseCreate(entity);
        this.baseDelete(entity);

        Assert.assertNull(this.baseGet(entity.getId()));
    }

    @Test
    public void testGet() {
        this.baseCreate(entity);

        this.baseGet(entity.getId());
    }
}
