package com.artist.sysadmin.activiti.identity;

import com.artist.sysadmin.dao.UserMapper;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author asiam
 * @date 2018/2/8 0008
 */
@Service
public class CustomUserEntityManager extends UserEntityManager {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserById(String userId) {
        return super.findUserById(userId);
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        return super.findGroupsByUser(userId);
    }

    @Override
    public User createNewUser(String userId) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public void insertUser(User user) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public void updateUser(User updatedUser) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public void deleteUser(String userId) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId,
                                                         String key) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<String> findUserInfoKeysByUserIdAndType(String userId,
                                                        String type) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query) {
        throw new RuntimeException("not implement method.");
    }
}