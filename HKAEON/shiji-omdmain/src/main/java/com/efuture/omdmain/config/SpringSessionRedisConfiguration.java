package com.efuture.omdmain.config;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import com.efuture.redis.component.RedisClient;

import redis.clients.jedis.JedisPoolConfig;

/***
 * @author huyh 初始化jedis模板 1）开启jedis模板的事务 2）屏蔽spring data redis对事务结果的转换，spring
 *         data redis 目前有1个缺陷，事务结果没有hash操作的反馈。
 *
 */

@Configuration
public class SpringSessionRedisConfiguration {

	@Resource
	ConfigurableEnvironment environment;

	/** 单节点连接池连接工厂 **/
	@Bean("jedisConnectionFactory")
	@ConditionalOnProperty(prefix = "redis", name = "connection.mode", havingValue = "single")
	public JedisConnectionFactory jedisConnectionFactory() {
		return getSingleConnectionFactory();
	}

	/** sentinel连接池连接工厂 **/
	@Bean("jedisConnectionFactory")
	@ConditionalOnProperty(prefix = "redis", name = "connection.mode", havingValue = "sentinel")
	public JedisConnectionFactory sentineljedisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(sentinelConfiguration(),
				poolConfiguration());
		jedisConnectionFactory.setConvertPipelineAndTxResults(false); // 屏蔽spring data redis的事务结果转换
		String password = (String) environment.getProperty("spring.redis.password");
		if (!StringUtils.isEmpty(password))
			jedisConnectionFactory.setPassword(password.trim());
		// jedisConnectionFactory.afterPropertiesSet();
		String index = environment.getProperty("spring.redis.database");
		int dbIndex = 0;
		if (!StringUtils.isEmpty(index)) {
			dbIndex = Integer.valueOf(index);
		}
		jedisConnectionFactory.setDatabase(dbIndex);
		return jedisConnectionFactory;
	}

	/** sentinel连接配置 **/
	@Bean
	@ConditionalOnProperty(prefix = "redis", name = "connection.mode", havingValue = "sentinel")
	public RedisSentinelConfiguration sentinelConfiguration() {
		Properties properties = new Properties();
		properties.put("spring.redis.sentinel.master", environment.getProperty("spring.redis.sentinel.master"));
		properties.put("spring.redis.sentinel.nodes", environment.getProperty("spring.redis.sentinel.nodes"));
		PropertiesPropertySource redisPropertySource = new PropertiesPropertySource("redis", properties);
		return new RedisSentinelConfiguration(redisPropertySource);
	}

	/** 公共连接池配置 **/
	@Bean
	@ConditionalOnProperty(prefix = "redis", name = "connection.mode", matchIfMissing = false)
	public JedisPoolConfig poolConfiguration() {
		JedisPoolConfig config = new JedisPoolConfig();
		String maxActive = (String) environment.getProperty("spring.redis.pool.max-active");
		String maxWait = (String) environment.getProperty("spring.redis.pool.max-wait");
		String maxIdle = (String) environment.getProperty("spring.redis.pool.max-idle");
		String minIdle = (String) environment.getProperty("spring.redis.pool.min-idle");
		config.setMaxTotal(Integer.parseInt(maxActive));
		config.setMaxWaitMillis(Integer.parseInt(maxWait));
		config.setMaxIdle(Integer.parseInt(maxIdle));
		config.setMinIdle(Integer.parseInt(minIdle));
		// add your customized configuration if needed
		return config;
	}

	/** sentinel连接池对应的 redisTemplate模版 **/
	@Bean("redisTemplate")
	@ConditionalOnProperty(prefix = "redis", name = "connection.mode", havingValue = "sentinel")
	RedisTemplate<Object, Object> sentinelRedisTemplate() {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
		redisTemplate.setConnectionFactory(sentineljedisConnectionFactory());
		redisTemplate.setEnableTransactionSupport(true);

		return redisTemplate;
	}

	/** sentinel连接池对应的 StringRedisTemplate模版 **/
	@Bean("stringRedisTemplate")
	@Qualifier("stringRedisTemplate")
	@ConditionalOnProperty(prefix = "redis", name = "connection.mode", havingValue = "sentinel")
	StringRedisTemplate sentinelStringRedisTemplate() {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(sentineljedisConnectionFactory());
		stringRedisTemplate.setEnableTransactionSupport(true);
		return stringRedisTemplate;
	}

	/** 单节点连接池对应的redisTemplate模版 **/
	@Bean("redisTemplate")
	@ConditionalOnProperty(prefix = "redis", name = "connection.mode", havingValue = "single")
	RedisTemplate<Object, Object> redisTemplate() {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setEnableTransactionSupport(true);

		return redisTemplate;
	}

	/** 单节点连接池对应的stringRedisTemplate模版 **/
	@Bean("stringRedisTemplate")
	@Qualifier("stringRedisTemplate")
	@ConditionalOnProperty(prefix = "redis", name = "connection.mode", havingValue = "single")
	StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(jedisConnectionFactory());
		stringRedisTemplate.setEnableTransactionSupport(true);
		return stringRedisTemplate;
	}

	/** redis接口封装类 **/
	@Bean("RedisClient")
	@ConditionalOnProperty(prefix = "redis", name = "connection.mode", matchIfMissing = false)
	RedisClient redisClient() {
		return new RedisClient();
	}

	/** 单节点连接池对应的cacheManager **/
	@Bean("cacheManager")
	@ConditionalOnProperty(prefix = "redis", name = "connection.mode", havingValue = "single")
	RedisCacheManager cacheManager() {
		return new RedisCacheManager(redisTemplate());
	}

	/** 哨兵连接池对应的cacheManager **/
	@Bean("cacheManager")
	@ConditionalOnProperty(prefix = "redis", name = "connection.mode", havingValue = "sentinel")
	RedisCacheManager sentinelCacheManager() {
		return new RedisCacheManager(sentinelRedisTemplate());
	}

	/** 单节点连接工厂构造 **/
	@ConditionalOnProperty(prefix = "redis", name = "connection.mode", havingValue = "single")
	private JedisConnectionFactory getSingleConnectionFactory() {
		String host = (String) environment.getProperty("spring.redis.host");
		String port = (String) environment.getProperty("spring.redis.port");
		String password = (String) environment.getProperty("spring.redis.password");
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setHostName(host.trim());
		jedisConnectionFactory.setPort(Integer.parseInt(port.trim()));
		jedisConnectionFactory.setPoolConfig(poolConfiguration());
		jedisConnectionFactory.setConvertPipelineAndTxResults(false); // 屏蔽spring data redis的事务结果转换
		if (!StringUtils.isEmpty(password))
			jedisConnectionFactory.setPassword(password.trim());
		// jedisConnectionFactory.afterPropertiesSet();
		String index = environment.getProperty("spring.redis.database");
		int dbIndex = 0;
		if (!StringUtils.isEmpty(index)) {
			dbIndex = Integer.valueOf(index);
		}
		jedisConnectionFactory.setDatabase(dbIndex);
		return jedisConnectionFactory;
	}

}
