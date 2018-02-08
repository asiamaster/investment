package com.artist.sysadmin.activiti.identity;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by asiam on 2018/2/8 0008.
 */
public class CustomGroupEntityManagerFactory implements SessionFactory {

    @Autowired
    private GroupEntityManager groupEntityManager;

    @Override
    public Class<?> getSessionType() {
        return GroupEntityManager.class;
    }

    @Override
    public Session openSession() {
        return groupEntityManager;
    }
}