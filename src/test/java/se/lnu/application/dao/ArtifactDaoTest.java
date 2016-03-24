package se.lnu.application.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import se.lnu.application.entity.UserEntity;
import se.lnu.application.entity.ArtifactEntity;
import se.lnu.application.utils.Filtering;

import java.util.ArrayList;
import java.util.List;


public class ArtifactDaoTest extends BaseDaoTest<ArtifactEntity> {

    @Autowired
    private UserDAO userDAO;
    private UserEntity userEntity;

    private ArtifactEntity entity;

    @Before
    public void setUp() {
        userEntity = InitialValue.getUserEntity();
        userDAO.create(userEntity);
        Assert.assertNotNull(userEntity.getId());

        entity = InitialValue.getArtifactEntity(userEntity);
    }

    @Test
    public void testAdd() {
        this.baseCreate(entity);

        Assert.assertNotNull(this.baseGet(entity.getId()));
    }

    @Test
    public void testGetList() {
        this.baseCreate(entity);

        Filtering filtering = new Filtering("name", "=", "\'test\'");
        List<Filtering> filteringList = new ArrayList<>();
        filteringList.add(filtering);

        List<ArtifactEntity> list = this.baseGetList(filteringList);

        Assert.assertFalse(list.isEmpty());
        Assert.assertNotNull(list.get(0));
        Assert.assertTrue(list.get(0).getName().equals("test"));
    }

    @Test
    public void testMergeList() {
        this.baseCreate(entity);

        List<ArtifactEntity> listOld = this.baseGetList(new ArrayList<>());
        Assert.assertFalse(listOld.isEmpty());

        entity.setName("new_test");
        List<ArtifactEntity> listAdd = new ArrayList<>();
        listAdd.add(entity);
        this.baseMergeList(listAdd);

        List<ArtifactEntity> listNew = this.baseGetList(new ArrayList<>());

        Assert.assertFalse(listNew.isEmpty());
        Assert.assertTrue(listNew.size() == listOld.size());
        Assert.assertEquals(this.baseGet(entity.getId()).getName(), "new_test");
    }

    @Test
    public void testSaveList() {
        this.baseCreate(entity);

        List<ArtifactEntity> listOld = this.baseGetList(new ArrayList<>());
        Assert.assertFalse(listOld.isEmpty());

        ArtifactEntity newEntity = InitialValue.getArtifactEntity(userEntity);
        List<ArtifactEntity> listAdd = new ArrayList<>();
        listAdd.add(newEntity);
        this.baseSaveList(listAdd);

        List<ArtifactEntity> listNew = this.baseGetList(new ArrayList<>());

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

        entity.setName("new_test");
        this.baseUpdate(entity);

        Assert.assertNotNull(entity);
        Assert.assertEquals(this.baseGet(entity.getId()).getName(), "new_test");
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
