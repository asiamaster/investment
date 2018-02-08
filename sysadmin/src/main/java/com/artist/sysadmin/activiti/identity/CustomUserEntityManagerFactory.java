package com.artist.sysadmin.activiti.identity;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by asiam on 2018/2/8 0008.
 */
public class CustomUserEntityManagerFactory implements SessionFactory {

    @Autowired
    private UserEntityManager userEntityManager;

    @Override
    public Class<?> getSessionType() {
        return UserEntityManager.class;
    }

    @Override
    public Session openSession() {
        return userEntityManager;
    }
}