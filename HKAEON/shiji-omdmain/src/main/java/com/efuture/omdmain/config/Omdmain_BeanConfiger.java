package com.efuture.omdmain.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import com.product.component.EasyDataSource;
import com.product.storage.template.FMybatisTemplate;

@Configuration
//@Order(3)
public class Omdmain_BeanConfiger {
	private static final Logger logger = LoggerFactory.getLogger(Omdmain_BeanConfiger.class);

	@Autowired
    JdbcTemplate jdbcTemplate;

//	@Autowired
//	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
//	@Autowired
//	protected ResourceLoader resourceLoader = new DefaultResourceLoader();
	PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
	
//    @Bean(name = "omdmain_datasource")
//	@Qualifier("omdmain_datasource")
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource onDataSource_omdmain() {
//
//    	    EasyDataSource service = new EasyDataSource();
//		return service;
//    }
 
    
    @Autowired
	@Bean(name = "omdmain_sqlSessionFactory")
    @Qualifier("omdmain_sqlSessionFactory")
	public SqlSessionFactoryBean onSqlSessionFactoryBeane_omdmain(@Qualifier("datasource") DataSource omdmain_datasource) throws IOException {
		SqlSessionFactoryBean service = new SqlSessionFactoryBean();

		Resource[] resources = resourceLoader.getResources("classpath*:beanmapper/omdmain/mybatis-config.xml");
		//Resource resource = resourceLoader.getResource("classpath:mybatis-config.xml");
		service.setConfigLocation(resources[0]);
		service.setDataSource(omdmain_datasource);
		return service;
	}

	/*
	 * 关系数据库数据访问对象
	 */
	@Bean(name = "omdmain_StorageOperation")
	@Qualifier("omdmain_StorageOperation")
	public FMybatisTemplate onFMybatisTemplate_omdmain(@Qualifier("omdmain_sqlSessionFactory") SqlSessionFactory omdmain_sqlSessionFactory) {
		return new FMybatisTemplate(omdmain_sqlSessionFactory, ExecutorType.BATCH);
	}
    
  
}
