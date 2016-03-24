package com.scf.server.application.model.dao;

import com.scf.server.application.InitialValue;
import com.scf.server.application.model.entity.ArtifactEntity;
import com.scf.server.application.model.entity.UserEntity;
import com.scf.server.application.utils.Filtering;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        assertNotNull(userEntity.getId());

        entity = InitialValue.getArtifactEntity(userEntity);
    }

    @Test
    public void testAdd() {
        this.baseCreate(entity);

        assertNotNull(this.baseGet(entity.getId()));
    }

    @Test
    public void testGetList() {
        this.baseCreate(entity);

        Filtering filtering = new Filtering("name", "=", "\'test\'");
        List<Filtering> filteringList = new ArrayList<>();
        filteringList.add(filtering);

        List<ArtifactEntity> list = this.baseGetList(filteringList);

        assertFalse(list.isEmpty());
        assertNotNull(list.get(0));
        assertTrue(list.get(0).getName().equals("test"));
    }

    @Test
    public void testMergeList() {
        this.baseCreate(entity);

        List<ArtifactEntity> listOld = this.baseGetList(new ArrayList<>());
        assertFalse(listOld.isEmpty());

        entity.setName("new_test");
        List<ArtifactEntity> listAdd = new ArrayList<>();
        listAdd.add(entity);
        this.baseMergeList(listAdd);

        List<ArtifactEntity> listNew = this.baseGetList(new ArrayList<>());

        assertFalse(listNew.isEmpty());
        assertTrue(listNew.size() == listOld.size());
        assertEquals(this.baseGet(entity.getId()).getName(), "new_test");
    }

    @Test
    public void testSaveList() {
        this.baseCreate(entity);

        List<ArtifactEntity> listOld = this.baseGetList(new ArrayList<>());
        assertFalse(listOld.isEmpty());

        ArtifactEntity newEntity = InitialValue.getArtifactEntity(userEntity);
        List<ArtifactEntity> listAdd = new ArrayList<>();
        listAdd.add(newEntity);
        this.baseSaveList(listAdd);

        List<ArtifactEntity> listNew = this.baseGetList(new ArrayList<>());

        assertFalse(listNew.isEmpty());
        assertTrue(listNew.size() > listOld.size());
        assertTrue(listNew.size() - listOld.size() == 1);
    }

    @Test
    public void testCreate() {
        this.baseCreate(entity);
        assertNotNull(entity.getId());
    }

    @Test
    public void testUpdate() {
        this.baseCreate(entity);

        entity.setName("new_test");
        this.baseUpdate(entity);

        assertNotNull(entity);
        assertEquals(this.baseGet(entity.getId()).getName(), "new_test");
    }

    @Test
    public void testDelete() {
        this.baseCreate(entity);
        this.baseDelete(entity);

        assertNull(this.baseGet(entity.getId()));
    }

    @Test
    public void testGet() {
        this.baseCreate(entity);

        this.baseGet(entity.getId());
    }
}
