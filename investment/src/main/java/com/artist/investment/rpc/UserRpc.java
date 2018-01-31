package com.artist.investment.rpc;

import com.artist.investment.domain.User;
import com.artist.investment.domain.Department;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.*;

import java.util.List;

/**
 * Created by asiamaster on 2017/10/19 0019.
 */
@Restful("${sysadmin.contextPath}")
public interface UserRpc {

	@POST("/userApi/get")
	BaseOutput<User> get(@VOSingleParameter Long id);

	@POST("/userApi/list")
	BaseOutput<List<User>> list(@VOBody User user);

	@POST("/userApi/listByExample")
	BaseOutput<List<User>> listByExample(@VOBody User user);

	@POST("/userApi/listUserDepartmentByUserId")
	BaseOutput<List<Department>> listUserDepartmentByUserId(@VOSingleParameter Long userId);

	@POST("/userApi/listUserByIds")
	BaseOutput<List<User>> listUserByIds(@VOSingleParameter List<String> ids);

	@POST("/userApi/adjustBalance")
	BaseOutput<Long> adjustBalance(@VOField("id") Long id, @VOField("amount") Long amount);

}
