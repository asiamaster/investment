package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.dao.*;
import com.dili.uap.domain.*;
import com.dili.uap.domain.dto.PaymentRecord;
import com.dili.uap.domain.dto.UserDataDto;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.glossary.UserState;
import com.dili.uap.manager.UserManager;
import com.dili.uap.rpc.PaymentRecordRpc;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.exception.NotLoginException;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.UserService;
import com.dili.uap.utils.MD5Util;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-18 10:46:46.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

    @Autowired
    private MD5Util md5Util;

    @Autowired
    private UserManager userManager;

    @Autowired
    private PaymentRecordRpc paymentRecordRpc;

    public UserMapper getActualDao() {
        return (UserMapper) getDao();
    }

    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    FirmMapper firmMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    UserDepartmentMapper userDepartmentMapper;
    @Autowired
    UserDataAuthMapper userDataAuthMapper;

    @Override
    public void logout(String sessionId) {
        this.userManager.clearSession(sessionId);
    }

    @Override
    public List<User> findUserByRole(Long roleId) {
        return getActualDao().findUserByRole(roleId);
    }

    @Transactional
    @Override
    public BaseOutput<Object> changePwd(Long userId, UserDto user) {
        if (userId == null) {
            return BaseOutput.failure("您还未登录");
        }
        if (StringUtils.isBlank(user.getOldPassword())) {
            return BaseOutput.failure("当前密码不能为空");
        }
        if (StringUtils.isBlank(user.getNewPassword())) {
            return BaseOutput.failure("新密码不能为空");
        }
        if (user.getNewPassword().trim().length() < 6) {
            return BaseOutput.failure("新密码长度限定为6-20");
        }
        if (user.getNewPassword().trim().length() > 20) {
            return BaseOutput.failure("新密码长度限定为6-20");
        }

        if (user.getNewPassword().equals(user.getOldPassword())) {
            return BaseOutput.failure("当前密码与新密码不能相同");
        }
        if (!user.getNewPassword().equals(user.getConfirmPassword())) {
            return BaseOutput.failure("两次密码输入不一致,请重新输入");
        }

        User userInDB = this.get(userId);
        if (userInDB == null) {
            return BaseOutput.failure("修改密码失败");
        }
        // 进行原密码比较
        if (!StringUtils.equalsIgnoreCase(userInDB.getPassword(), this.encryptPwd(user.getOldPassword()))) {
            return BaseOutput.failure("当前密码有误,请重新输入");
        }

        userInDB.setModified(new Date());
        //激活用户
        userInDB.setState(UserState.NORMAL.getCode());
        // 加密并更新密码
        userInDB.setPassword(this.encryptPwd(user.getNewPassword()));
        this.updateExactSimple(userInDB);
        return BaseOutput.success("修改密码成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput save(User user) {
        if (StringUtils.isNotBlank(user.getUserName())) {
            user.setUserName(user.getUserName().toLowerCase());
        }
        //验证邮箱是否重复
        User query = DTOUtils.newDTO(User.class);
        query.setEmail(user.getEmail());
        List<User> userList = getActualDao().select(query);
        //用户新增
        if (null == user.getId()) {
            if (CollectionUtils.isNotEmpty(userList)) {
                return BaseOutput.failure("邮箱已存在");
            }
            query.setEmail(null);
            query.setCellphone(user.getCellphone());
            userList = getActualDao().select(query);
            if (CollectionUtils.isNotEmpty(userList)) {
                return BaseOutput.failure("手机号码已存在");
            }
            query.setCellphone(null);
            query.setUserName(user.getUserName());
            userList = getActualDao().select(query);
            if (CollectionUtils.isNotEmpty(userList)) {
                return BaseOutput.failure("用户账号已存在");
            } else {
                user.setState(UserState.INACTIVE.getCode());
            }
            user.setPassword(encryptPwd(UapConstants.DEFAULT_PASS));
            this.insertExactSimple(user);
        } else {
            if (CollectionUtils.isNotEmpty(userList)) {
                //匹配是否有用户ID不等当前修改记录的用户
                boolean result = userList.stream().anyMatch(u -> !u.getId().equals(user.getId()));
                if (result) {
                    return BaseOutput.failure("邮箱已存在");
                }
            }
            query.setEmail(null);
            query.setCellphone(user.getCellphone());
            userList = getActualDao().select(query);
            if (CollectionUtils.isNotEmpty(userList)) {
                //匹配是否有用户ID不等当前修改记录的用户
                boolean result = userList.stream().anyMatch(u -> !u.getId().equals(user.getId()));
                if (result) {
                    return BaseOutput.failure("手机号码已存在");
                }
            }
            User update = DTOUtils.as(user, User.class);
            DTO go = DTOUtils.go(update);
            go.remove("userName");
            go.remove("password");
            go.remove("firmCode");
            go.remove("created");
            go.remove("modified");
            this.updateExactSimple(update);
        }
        return BaseOutput.success("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput resetPass(Long userId) {
        User user = DTOUtils.newDTO(User.class);
        user.setId(userId);
        user.setPassword(encryptPwd(UapConstants.DEFAULT_PASS));
        user.setState(UserState.INACTIVE.getCode());
        this.updateSelective(user);
        return BaseOutput.success("重置成功");
    }

    @Override
    public BaseOutput upateEnable(Long userId, Boolean enable) {
        User user = DTOUtils.newDTO(User.class);
        user.setId(userId);
        if (enable) {
            user.setState(UserState.NORMAL.getCode());
        } else {
            user.setState(UserState.DISABLED.getCode());
        }
        this.updateSelective(user);
        return BaseOutput.success("操作成功");
    }

    @Override
    public List<UserDataDto> getUserRolesForTree(Long userId) {
        //获取需要分配角色的用户信息
        User user = this.get(userId);
        //预分配角色的用户，是否属于集团用户
        Boolean isGroup = false;
        if (UapConstants.GROUP_CODE.equalsIgnoreCase(user.getFirmCode())) {
            isGroup = true;
        }
        //用户所拥有的市场信息
        List<Firm> firmList = null;
        //用户所拥有的角色信息
        List<Role> roleList = null;
        if (isGroup) {
            roleList = roleMapper.selectAll();
            firmList = firmMapper.selectAll();
        } else {
            //根据用户查询该用户对应的市场的角色信息
            Role roleQuery = DTOUtils.newDTO(Role.class);
            roleQuery.setFirmCode(user.getFirmCode());
            roleList = roleMapper.select(roleQuery);
            //查询对应的市场信息
            Firm firmQuery = DTOUtils.newDTO(Firm.class);
            firmQuery.setCode(user.getFirmCode());
            firmList = firmMapper.select(firmQuery);
        }
        //用户对应的市场，存在角色信息，则需进一步检索用户已有的角色
        if (CollectionUtils.isNotEmpty(roleList)) {
            Set<Long> userRoleIds = userRoleMapper.getRoleIdsByUserId(user.getId());
            List<UserDataDto> userRoleDtos = Lists.newArrayList();
            /**
             * 遍历角色信息，设置是否选中
             * 末级节点，设置为open状态
             */
            roleList.stream().forEach(role -> {
                UserDataDto dto = DTOUtils.newDTO(UserDataDto.class);
                dto.setName(role.getRoleName());
                dto.setParentId(UapConstants.FIRM_PREFIX + role.getFirmCode());
                dto.setTreeId(String.valueOf(role.getId()));
                if (userRoleIds.contains(role.getId())) {
                    dto.setChecked(true);
                } else {
                    dto.setChecked(false);
                }
                userRoleDtos.add(dto);
            });
            firmList.stream().forEach(firm -> {
                UserDataDto dto = DTOUtils.newDTO(UserDataDto.class);
                dto.setName(firm.getName());
                dto.setTreeId(UapConstants.FIRM_PREFIX + firm.getCode());
                dto.setParentId("");
                userRoleDtos.add(dto);
            });
            return userRoleDtos;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput saveUserRoles(Long userId, String[] roleIds) {
        if (null == userId) {
            return BaseOutput.failure("用户数据丢失");
        }
        UserRole userRole = DTOUtils.newDTO(UserRole.class);
        userRole.setUserId(userId);
        userRoleMapper.delete(userRole);
        //需要保存的用户角色信息
        if (null != roleIds && roleIds.length > 0) {
            List<UserRole> saveDatas = Lists.newArrayList();
            for (String id : roleIds) {
                if (!id.startsWith(UapConstants.FIRM_PREFIX)) {
                    UserRole ur = DTOUtils.newDTO(UserRole.class);
                    ur.setUserId(userId);
                    ur.setRoleId(Long.valueOf(id));
                    saveDatas.add(ur);
                }
            }
            //如果存在需要保存的用户角色数据，则保存数据
            if (CollectionUtils.isNotEmpty(saveDatas)) {
                userRoleMapper.insertList(saveDatas);
            }
        }
        return BaseOutput.success("操作成功");
    }

    @Override
    public EasyuiPageOutput listEasyuiPage(User domain, boolean useProvider) throws Exception {
        if (domain.getRows() != null && domain.getRows() >= 1) {
            PageHelper.startPage(domain.getPage(), domain.getRows());
        }
        List<UserDto> users = getActualDao().selectForPage(domain);
        long total = users instanceof Page ? ((Page) users).getTotal() : (long) users.size();
        List results = useProvider ? ValueProviderUtils.buildDataByProvider(domain, users) : users;
        return new EasyuiPageOutput((int) total, results);
    }

    @Override
    public BaseOutput<Object> fetchLoginUserInfo(Long userId) {
        User user = this.get(userId);
        if (user == null) {
            return BaseOutput.success("操作失败");
        }
        user.setPassword("");
        Map<String, Object> map = DTOUtils.go(user);
        Department department = departmentMapper.selectByPrimaryKey(user.getDepartmentId());
        if (department != null) {
            map.put("departmentId", department.getName());
        }

        if (StringUtils.isNotBlank(user.getFirmCode())) {
            Firm firmConditon = DTOUtils.newDTO(Firm.class);
            firmConditon.setCode(user.getFirmCode());
            Firm firm = this.firmMapper.selectOne(firmConditon);
            if (firm != null) {
                map.put("firmCode", firm.getName());
            }
        }
        return BaseOutput.success("操作成功").setData(map);
    }

    @Override
    public List<UserDataDto> getUserDataAuthForTree(Long userId) {
        //获取需要分配数据权限的用户信息
        User user = this.get(userId);
        if (null == user) {
            return null;
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", user.getId());
        //预分配角色的用户，是否属于集团用户
        if (!UapConstants.GROUP_CODE.equalsIgnoreCase(user.getFirmCode())) {
            //不为集团用户的情况下，则需要根据集团过滤
            params.put("firmCode", user.getFirmCode());
        }
        return getActualDao().selectUserDatas(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput saveUserDatas(Long userId, String[] dataIds, Long dataRange) {
        if (null == userId || null == dataRange) {
            return BaseOutput.failure("用户数据丢失");
        }
        //先根据用户删除现有的部门权限信息
        UserDepartment delete = DTOUtils.newDTO(UserDepartment.class);
        delete.setUserId(userId);
        userDepartmentMapper.delete(delete);
        //需要保存的用户角色信息
        if (null != dataIds && dataIds.length > 0) {
            List<UserDepartment> saveDatas = Lists.newArrayList();
            for (String id : dataIds) {
                if (!id.startsWith(UapConstants.FIRM_PREFIX)) {
                    UserDepartment ud = DTOUtils.newDTO(UserDepartment.class);
                    ud.setUserId(userId);
                    ud.setDepartmentId(Long.valueOf(id));
                    saveDatas.add(ud);
                }
            }
            //如果存在需要保存的用户角色数据，则保存数据
            if (CollectionUtils.isNotEmpty(saveDatas)) {
                userDepartmentMapper.insertList(saveDatas);
            }
        }
        //查询当前用户所属的权限范围
        UserDataAuth userDataAuth = DTOUtils.newDTO(UserDataAuth.class);
        userDataAuth.setUserId(userId);
        List<UserDataAuth> userDataAuths = userDataAuthMapper.select(userDataAuth);
        /**
         * 判断是否存在权限信息
         * 正常情况下，目前要么没有，要么应该只有一种type范围对应的一条，所以，前期不需要关联查询，否则需要关联dataAuth进行type区分
         */
        if (CollectionUtils.isNotEmpty(userDataAuths)) {
            //如果存在多条，则先直接根据用户删除所有，再重新新增
            if (userDataAuths.size() > 1) {
                userDataAuthMapper.delete(userDataAuth);
                userDataAuth.setDataAuthId(dataRange);
                userDataAuthMapper.insert(userDataAuth);
            } else {
                //否则，比较是否相同，不同则执行更改
                UserDataAuth temp = userDataAuths.get(0);
                if (!temp.getDataAuthId().equals(dataRange)) {
                    temp.setDataAuthId(dataRange);
                    userDataAuthMapper.updateByPrimaryKeySelective(temp);
                }
            }
        } else {
            //数据库里不存的话，则直接新增数据
            userDataAuth.setDataAuthId(dataRange);
            userDataAuthMapper.insert(userDataAuth);
        }
        return BaseOutput.success("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput unlock(Long userId) {
        //解锁-设置状态为已启用
        User user = DTOUtils.newDTO(User.class);
        user.setId(userId);
        user.setState(UserState.NORMAL.getCode());
        this.updateSelective(user);
        return BaseOutput.success("操作成功");
    }

    @Override
    public EasyuiPageOutput listOnlinePage(UserDto user) throws Exception {
        List<String> onlineUserIds = userManager.getOnlineUserIds();
        if (CollectionUtils.isEmpty(onlineUserIds)) {
            return new EasyuiPageOutput(0, Lists.newArrayList());
        }
        List queryList = Lists.newArrayList();
        user.setIds(onlineUserIds);
        return super.listEasyuiPageByExample(user, true);
    }

    @Override
    public BaseOutput forcedOffline(Long userId) {
        this.userManager.clearUserSession(userId);
        return BaseOutput.success("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        //删除用户数据权限关系
        UserDataAuth userDataAuth = DTOUtils.newDTO(UserDataAuth.class);
        userDataAuth.setUserId(id);
        userDataAuthMapper.delete(userDataAuth);
        //删除用户-部门数据权限
        UserDepartment ud = DTOUtils.newDTO(UserDepartment.class);
        ud.setUserId(id);
        userDepartmentMapper.delete(ud);
        //删除用户-角色关系
        UserRole userRole = DTOUtils.newDTO(UserRole.class);
        userRole.setUserId(id);
        userRoleMapper.delete(userRole);
        //删除用户本身
        return super.delete(id);
    }

    @Override
    @Transactional
    public BaseOutput<Long> adjustBalance(Long id, Long amount) {
        User user = get(id);
        if(user.getBalance()<-amount){
            return BaseOutput.failure("余额不足");
        }
        user.setBalance(user.getBalance()+amount);
        updateSelective(user);
        return BaseOutput.success().setData(user.getBalance());
    }

    @Override
    @Transactional
    public BaseOutput<String> adjustBalance(Long id, String amount, String notes) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            throw new NotLoginException();
        }
        User user = get(id);
        //记录初始余额
        Long oriBalance = user.getBalance();
        //计算更新后的余额
        Long newBalance = new BigDecimal(amount).setScale(2,BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).add(BigDecimal.valueOf(user.getBalance())).longValue();
        user.setBalance(newBalance);
        //更新用户余额
        updateSelective(user);
        PaymentRecord paymentRecord = DTOUtils.newDTO(PaymentRecord.class);
        paymentRecord.setCreatedName(userTicket.getRealName());
        //设置当前余额
        paymentRecord.setBalance(newBalance);
        //查询原始余额
        paymentRecord.setInitialAmount(oriBalance);
        paymentRecord.setTargetAmount(newBalance);
        paymentRecord.setName("调整余额");
        //调整余额为正数，则是收入1,否则为支出2
        if(Float.parseFloat(amount) > 0) {
            paymentRecord.setType(1);
        }else{
            paymentRecord.setType(2);
        }
        paymentRecord.setUserName(user.getRealName());
        //资金去向
        paymentRecord.setPlatformName("余额调整:"+amount+"元");
        paymentRecord.setNotes(notes);
        BaseOutput<String> output = paymentRecordRpc.insert(paymentRecord);
        if (!output.isSuccess()){
            throw new AppException("远程调用异常");
        }
        return BaseOutput.success("调整余额成功");
    }

    private String encryptPwd(String passwd) {
        return md5Util.getMD5ofStr(passwd).substring(6, 24);
    }

}