package com.artist.sysadmin.activiti.identity;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by asiam on 2018/2/8 0008.
 */
@Service
public class CustomGroupEntityManager extends GroupEntityManager {

    @Override
    public List<Group> findGroupsByUser(String userId) {
        return super.findGroupsByUser(userId);
    }

}