package com.scf.server.application.model.dao;

import com.scf.server.application.model.entity.ArtifactEntity;
import com.scf.server.application.model.entity.CollectionEntity;
import com.scf.server.application.model.entity.UserEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CollectionDaoTest extends BaseDaoTest<CollectionEntity> {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ArtifactDAO artifactDAO;

    private UserEntity userEntity;
    private ArtifactEntity artifactEntity;

    private CollectionEntity entity;

    @Before
    public void setUp() {
        userEntity = InitialValue.getUserEntity();
        userDAO.create(userEntity);

        artifactEntity = InitialValue.getArtifactEntity(userEntity);
        artifactDAO.create(artifactEntity);

        List<ArtifactEntity> artifactEntities = new ArrayList<>();
        artifactEntities.add(artifactEntity);

        Assert.assertNotNull(userEntity.getId());
        Assert.assertNotNull(artifactEntity.getId());

        entity = InitialValue.getCollectionEntity(userEntity, artifactEntities, null);
    }

    @Test
    public void testAdd() {
        this.baseCreate(entity);

        Assert.assertNotNull(this.baseGet(entity.getId()));
    }

    @Test
    public void testGetList() {
        this.baseCreate(entity);

        List<CollectionEntity> list = this.baseGetList(new ArrayList<>());

        Assert.assertFalse(list.isEmpty());
        Assert.assertNotNull(list.get(0));
        Assert.assertTrue(list.get(0).getName().equals("test"));
    }

    @Test
    public void testMergeList() {
        this.baseCreate(entity);

        List<CollectionEntity> listOld = this.baseGetList(new ArrayList<>());
        Assert.assertFalse(listOld.isEmpty());

        entity.setName("new_test");
        List<CollectionEntity> listAdd = new ArrayList<>();
        listAdd.add(entity);
        this.baseMergeList(listAdd);

        List<CollectionEntity> listNew = this.baseGetList(new ArrayList<>());

        Assert.assertFalse(listNew.isEmpty());
        Assert.assertTrue(listNew.size() == listOld.size());
        Assert.assertEquals(this.baseGet(entity.getId()).getName(), "new_test");
    }

    @Test
    public void testSaveList() {
        this.baseCreate(entity);

        List<CollectionEntity> listOld = this.baseGetList(new ArrayList<>());
        Assert.assertFalse(listOld.isEmpty());

        List<ArtifactEntity> artifactEntities = new ArrayList<>();
        artifactEntities.add(artifactEntity);

        CollectionEntity newEntity = InitialValue.getCollectionEntity(userEntity, artifactEntities, null);
        List<CollectionEntity> listAdd = new ArrayList<>();
        listAdd.add(newEntity);
        this.baseSaveList(listAdd);

        List<CollectionEntity> listNew = this.baseGetList(new ArrayList<>());

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

        entity.setName("new_test_value");
        this.baseUpdate(entity);

        Assert.assertNotNull(entity);
        Assert.assertEquals(this.baseGet(entity.getId()).getName(), "new_test_value");
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
