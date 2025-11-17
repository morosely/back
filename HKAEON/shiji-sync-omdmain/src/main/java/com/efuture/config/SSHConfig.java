package com.efuture.config;

import com.efuture.utils.SSHEntity;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SSHConfig {

    @Bean(name = "ssh")
    @ConfigurationProperties(prefix = "ssh")
    public SSHEntity sshEntity() {
        return new SSHEntity();
    }
}
