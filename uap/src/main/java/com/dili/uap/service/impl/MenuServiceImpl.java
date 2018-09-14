package com.dili.uap.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.MenuMapper;
import com.dili.uap.dao.ResourceMapper;
import com.dili.uap.dao.RoleMenuMapper;
import com.dili.uap.domain.Menu;
import com.dili.uap.domain.Resource;
import com.dili.uap.domain.RoleMenu;
import com.dili.uap.domain.dto.MenuCondition;
import com.dili.uap.service.MenuService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 16:08:04.
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu, Long> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    public MenuMapper getActualDao() {
        return (MenuMapper)getDao();
    }

    @Override
    public List<Menu> listDirAndLinksByUserIdAndSystemCode(String jsonParam){
        return this.menuMapper.listDirAndLinksByUserId(JSONObject.parseObject(jsonParam));
    }

    @Override
    public List<Map> listDirAndLinks(Long userId, String systemCode){
        Map param = Maps.newHashMap();
        param.put("userId", userId);
        param.put("systemCode", systemCode);
        List<Menu> menus =  this.menuMapper.listDirAndLinksByUserId(param);
        return parentToChildren(menus);
    }

    @Override
    public List<Menu> getParentMenus(String id) {
        String parentIds = getActualDao().getParentMenus(id);
        if(StringUtils.isBlank(parentIds)){
            return null;
        }
        String[] parentIdArr = parentIds.split(",");
        MenuCondition menuCondition = DTOUtils.newDTO(MenuCondition.class);
        //递归查出来的父id需要反转
        List ids = Arrays.asList(parentIdArr);
        Collections.reverse(ids);
        menuCondition.setIds(ids);
        //然而in查询无法按in的顺序获得结果，还是要根据ids的顺序重排
        List<Menu> menus = listByExample(menuCondition);
        List<Menu> sortedMenus = new ArrayList<>(menus.size());
        for(int i=0; i<ids.size(); i++){
            for(Menu menu : menus){
                if(ids.get(i).equals(menu.getId().toString())){
                    sortedMenus.add(menu);
                    break;
                }
            }
        }
        return sortedMenus;
    }

    @Override
    public List<Menu> getParentMenusByUrl(String url){
        Menu menu = DTOUtils.newDTO(Menu.class);
        menu.setUrl(url);
        List<Menu> menus = getActualDao().select(menu);
        if(menus == null || menus.isEmpty()){
            return null;
        }
        return getParentMenus(menus.get(0).getId().toString());
    }

    @Override
    public Map<String, Object> getMenuDetailByUrl(String url) {
        return getActualDao().getMenuDetailByUrl(url);
    }

    @Override
    public List<Map> listSystemMenu(){
        List<Map> menuTrees = getActualDao().listSystemMenu();
        menuTrees.forEach( menuTree -> {
            Map<String, String> attr = new HashMap<>(1);
            attr.put("type", menuTree.get("type").toString());
            attr.put("systemId", menuTree.get("system_id").toString());
            menuTree.put("attributes", attr);
        });
        return menuTrees;
    }

    @Override
    public String deleteMenu(Long id) {
        Menu menu = DTOUtils.newDTO(Menu.class);
        menu.setParentId(id);
        List children = getActualDao().select(menu);
        if(!children.isEmpty()){
            return "菜单下有子菜单，无法删除";
        }
        Resource resource = DTOUtils.newDTO(Resource.class);
        resource.setMenuId(id);
        List resources = resourceMapper.select(resource);
        if(!resources.isEmpty()){
            return "菜单下有资源，无法删除";
        }

        //删除菜单角色关系
        RoleMenu roleMenu = DTOUtils.newDTO(RoleMenu.class);
        roleMenu.setMenuId(id);
        roleMenuMapper.delete(roleMenu);
        //删除菜单
        delete(id);
        return null;
    }

    /**
     * id, parentId树型结构转children[]树型结构
     * @param list
     * @return
     */
    private List<Map> parentToChildren(List<Menu> list){
//        final String ID_KEY = "id";
//        final String PARENT_ID_KEY = "parentId";
        final String CHILDREN_ID_KEY = "children";
        List<Map> tree = Lists.newArrayList();
        //key为parentId, value为tree node map
        Map<Long, List<Map>> parentId2Children = Maps.newHashMap();
        for(Menu item : list){
            if(parentId2Children.containsKey(item.getParentId())){
                parentId2Children.get(item.getParentId()).add(DTOUtils.go(item));
            }else{
                List<Map> children = Lists.newArrayList();
                children.add(DTOUtils.go(item));
                parentId2Children.put(item.getParentId(), children);
            }
        }
        for(Menu item : list){
            Map treeItem = DTOUtils.go(item);
            //如果当前项有子节点，则加入到CHILDREN_ID_KEY中
            if(parentId2Children.containsKey(item.getId())){
                treeItem.put(CHILDREN_ID_KEY, parentId2Children.get(item.getId()));
            }
            //将顶层节点加入到tree列表中
            if(item.getParentId() == null){
                tree.add(treeItem);
            }
        }
        return tree;
    }
}