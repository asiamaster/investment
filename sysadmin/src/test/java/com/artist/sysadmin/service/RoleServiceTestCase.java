package com.artist.sysadmin.service;

import java.util.regex.Pattern;

import org.junit.Test;

public class RoleServiceTestCase {

	@Test
	public void test() {
		Pattern p = Pattern
				.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$");
		System.out.println(p.matcher("asdasdfasf").matches());
	}

}
