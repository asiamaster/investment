package com.artist.sysadmin.dao;

import com.artist.sysadmin.domain.Menu;
import com.dili.ss.base.MyMapper;
import com.artist.sysadmin.domain.dto.MenuDto;
import com.artist.sysadmin.domain.dto.MenuJsonDto;

import java.util.List;

public interface MenuMapper extends MyMapper<Menu> {

	List<Menu> findByUserId(Long id);

	List<Menu> findByRoleId(Long id);

	List<MenuJsonDto> findAllMenuJson();

	List<MenuDto> selectMenuDto();

	List<MenuDto> selectRoleMenuDto(Long roleId);

	String getParentMenus(String id);
}