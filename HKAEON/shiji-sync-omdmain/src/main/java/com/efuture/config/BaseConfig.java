package com.efuture.config;

import com.product.component.EasyDataSource;
import com.product.controller.ProductReflect;
import com.product.util.UniqueID;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
public class BaseConfig {

    @Bean(name = "UniqueID")
    UniqueID onUniqueIDTemplate() throws Exception {
        UniqueID instance = new UniqueID();
        instance.setHostId("120");

        return instance;
    }

    @Bean(name = "ServiceMethodReflect")
    public ProductReflect onProductReflect() {
        return new ProductReflect();
    }


    @Autowired
    protected ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Bean(name = "omdmainDataSource")
    @Primary
    @Qualifier
    @ConfigurationProperties(prefix = "spring.datasource")
    public EasyDataSource omdmainbDataSource() {
        return new EasyDataSource();
    }
    @Autowired
    @Primary
    @Bean(name = "sqlSessionFactory")
    @Qualifier("sqlSessionFactory")
    public SqlSessionFactoryBean omdmainSqlSessionFactory(@Qualifier("omdmainDataSource") DataSource datasource) {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        Resource resource = resourceLoader.getResource("classpath:mybatis-config.xml");
        factoryBean.setConfigLocation(resource);
        factoryBean.setDataSource(datasource);
        return factoryBean;
    }
    @Primary
    @Bean(name = "sqlSessionTemplate")
    @Qualifier("sqlSessionTemplate")
    public SqlSessionTemplate omdmainSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    @Primary
    @Bean(name = "omdmainTransactionManager")
    public DataSourceTransactionManager omdmainTransactionManager(@Qualifier("omdmainDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name = "stockDataSource")
    @Qualifier
    @ConfigurationProperties(prefix = "spring.stock.datasource")
    public EasyDataSource posdbDataSource() {
        EasyDataSource service = new EasyDataSource();
        return service;
    }
    @Autowired
    @Bean(name = "stockSqlSessionFactory")
    @Qualifier("stockSqlSessionFactory")
    public SqlSessionFactoryBean stockSqlSessionFactory(@Qualifier("stockDataSource") DataSource datasource) {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        Resource resource = resourceLoader.getResource("classpath:mybatis-config.xml");
        factoryBean.setConfigLocation(resource);
        factoryBean.setDataSource(datasource);
        return factoryBean;
    }
    @Bean(name = "stockSqlSessionTemplate")
    @Qualifier("stockSqlSessionTemplate")
    public SqlSessionTemplate posdbSqlSessionTemplate(@Qualifier("stockSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    @Bean(name = "stockTransactionManager")
    public DataSourceTransactionManager posdbTransactionManager(@Qualifier("stockDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    //---------- 离线会员 数据源 ---------
    @Bean(name = "crmadapterDataSource")
    @Qualifier
    @ConfigurationProperties(prefix = "spring.crmadapter.datasource")
    public EasyDataSource crmadapterDataSource() {
        EasyDataSource service = new EasyDataSource();
        return service;
    }
    @Autowired
    @Bean(name = "crmadapterSqlSessionFactory")
    @Qualifier("crmadapterSqlSessionFactory")
    public SqlSessionFactoryBean crmadapterSqlSessionFactory(@Qualifier("crmadapterDataSource") DataSource datasource) {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        Resource resource = resourceLoader.getResource("classpath:mybatis-config.xml");
        factoryBean.setConfigLocation(resource);
        factoryBean.setDataSource(datasource);
        return factoryBean;
    }
    @Bean(name = "crmadapterSqlSessionTemplate")
    @Qualifier("crmadapterSqlSessionTemplate")
    public SqlSessionTemplate crmadapterSqlSessionTemplate(@Qualifier("crmadapterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    @Bean(name = "crmadapterTransactionManager")
    public DataSourceTransactionManager crmadapterTransactionManager(@Qualifier("crmadapterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
