package com.artist.sysadmin;

import com.dili.ss.retrofitful.annotation.RestfulScan;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.velocity.VelocityAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 由MyBatis Generator工具自动生成
 */
// =====================  Spring Cloud  =====================
//@EnableHystrixDashboard
//@EnableAspectJAutoProxy(proxyTargetClass = true)
//@EnableDiscoveryClient
//@EnableFeignClients
//@EnableHystrix

// =====================  Spring Boot  =====================
// @EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = { ThymeleafAutoConfiguration.class, VelocityAutoConfiguration.class })
// @ImportResource(locations = "classpath:applicationContext.xml")
@ComponentScan(basePackages = { "com.dili.ss", "com.artist.sysadmin" })
// @EnableEncryptableProperties
@EncryptablePropertySource(name = "EncryptedProperties", value = "classpath:conf/security.properties")
// @ServletComponentScan

// =====================  Other  =====================
@RestfulScan({"com.artist.sysadmin.rpc", "com.artist.sysadmin.sdk.rpc"})
@MapperScan(basePackages = {"com.artist.sysadmin.dao", "com.dili.ss.dao"})
// @EnableScheduling
/**
 * 除了内嵌容器的部署模式，Spring Boot也支持将应用部署至已有的Tomcat容器, 或JBoss, WebLogic等传统Java EE应用服务器。
 * 以Maven为例，首先需要将<packaging>从jar改成war，然后取消spring-boot-maven-plugin，然后修改Application.java
 * 继承SpringBootServletInitializer
 */
@RestController("")
public class SysadminApplication extends SpringBootServletInitializer {

	@Autowired
	private TaskService taskService;
	@Autowired
	private ProcessEngine processEngine;
	@RequestMapping("/test")
	public String index() {
		System.out.println("################################taskService" + taskService);
		System.out.println("################################processEngine" + processEngine);
		return "xxxxxxxxxxxxx";
	}

	public static void main(String[] args) {
		SpringApplication.run(SysadminApplication.class, args);
	}

}
