package com.artist.sysadmin.dao;

import java.util.List;

import com.artist.sysadmin.domain.Resource;
import com.dili.ss.base.MyMapper;

public interface ResourceMapper extends MyMapper<Resource> {

	List<Resource> findByRole(Long id);

	boolean checkResourceUrlUnique(String url, Long id);

	boolean checkResourceNameUnique(String resourceName, Long id);

	List<Resource> listAllResourceJson(Resource resource);

	List<Resource> findByUserId(Long userId);
}