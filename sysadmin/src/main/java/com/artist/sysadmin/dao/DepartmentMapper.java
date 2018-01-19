package com.artist.sysadmin.dao;

import java.util.List;

import com.artist.sysadmin.domain.Department;
import com.dili.ss.base.MyMapper;

public interface DepartmentMapper extends MyMapper<Department> {

	List<Department> findByUserId(Long id);
}