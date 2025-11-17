package com.efuture.omdmain;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.mongodb.MongoClient;
import com.product.util.SpringContext;



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
//@EnableEurekaClient
@SpringBootApplication(exclude = {MongoAutoConfiguration.class,MongoDataAutoConfiguration.class})
@EnableDiscoveryClient(autoRegister = false)
public class BootWarApplication extends SpringBootServletInitializer implements ApplicationContextAware{
	
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	    SpringContext.setInstance((ConfigurableApplicationContext)applicationContext);
  }

	@Bean
	public RestTemplate restTempldate(){
		return new RestTemplate();
	}
//	
	@Override
	protected SpringApplicationBuilder configure( SpringApplicationBuilder builder) {
		builder.sources(this.getClass());
//		SpringContext.run(this.getClass());
        return super.configure(builder);
    }

	
	public static void main(String[] args) {
		SpringContext.run(BootWarApplication.class, args);
    }
	
//	public void onDiscovery() {
//		// Register with Eureka  
//		DiscoveryManager.getInstance().initComponent(new MyDataCenterInstanceConfig(),new DefaultEurekaClientConfig());  
//		ApplicationInfoManager.getInstance().setInstanceStatus(InstanceStatus.UP);  
//		String vipAddress = configInstance.getStringProperty("eureka.vipAddress", "sampleservice.mydomain.net").get();  
//		InstanceInfo nextServerInfo = DiscoveryManager.getInstance().getDiscoveryClient().getNextServerFromEureka(vipAddress, false);
//	}
}
