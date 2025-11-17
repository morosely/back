package com.efuture;

import com.alibaba.fastjson.JSONObject;
import com.efuture.mapper.OrderCallNoMapper;
import com.efuture.utils.SSHEntity;
import com.product.util.SpringContext;
import groovy.util.logging.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;


/**
 * 微服务客户端工程
 *    1、@EnableEurekaClient 标注表示需要在注册中心发现
 *    2、pom.xml配置相关
 *      =======================================================================
 *    	<parent>
 *    		<groupId>org.springframework.cloud</groupId>
 *    		<artifactId>spring-cloud-starter-parent</artifactId>
 *    		<version>Brixton.SR4</version>
 *    		<relativePath />
 *    	</parent>
 *      -----------------------------------------------------------------------
 *      <dependencies>
 *      	<dependency>
 *      		<groupId>org.springframework.cloud</groupId>
 *      		<artifactId>spring-cloud-starter-eureka</artifactId>
 *      	</dependency>
 *     		<dependency>
 *     			<groupId>org.springframework.boot</groupId>
 *     			<artifactId>spring-boot-starter-test</artifactId>
 *     			<scope>test</scope>
 *     		</dependency>
 *      -----------------------------------------------------------------------
 *      	<dependency> 
 *      		<groupId>org.springframework.boot</groupId>
 *      		<artifactId>spring-boot-starter-data-mongodb</artifactId>
 *      	</dependency> 
 *      </dependencies>
 *      =======================================================================
 *    3、application.properties需要配置注册中心地址
 *      eureka.client.serviceUrl.defaultZone=http://120.55.42.236:8010/eureka/
 *      
 * @author qianhb
 * @date   2017-07-26
 */
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableDiscoveryClient(autoRegister = true)
@SpringBootApplication
@Slf4j
@MapperScan({"com.efuture.mapper"})
public class BootApplication {
	@Bean
	@LoadBalanced
	public RestTemplate restTempldate(){
		return new RestTemplate();
	}

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext context = SpringContext.run(BootApplication.class, args);
		//默认application配置文件，表有数据将其配置覆盖，表中数据优先
		if(beanExists(context,OrderCallNoMapper.class) && beanExists(context,SSHEntity.class)){
			OrderCallNoMapper orderCallNoMapper = context.getBean(OrderCallNoMapper.class);
			List<JSONObject> data =  orderCallNoMapper.selectListSql("select * from ordercallnosshconfig");
			if(data!=null && !data.isEmpty()){
				JSONObject ssh = data.get(0);
				SSHEntity sshEntity = context.getBean(SSHEntity.class);
				sshEntity.setHost(ssh.getString("host"));
				sshEntity.setPort(ssh.getInteger("port"));
				sshEntity.setUser(ssh.getString("user"));
				sshEntity.setPassword(ssh.getString("password"));
			}
		};
	}

	public static boolean beanExists(ApplicationContext context, String beanName) {
		return context.containsBean(beanName);
	}

	public static <T> boolean beanExists(ApplicationContext context,Class<T> beanType) {
		try {
			context.getBean(beanType);
			return true;
		} catch (NoSuchBeanDefinitionException e) {
			return false;
		}
	}
}
