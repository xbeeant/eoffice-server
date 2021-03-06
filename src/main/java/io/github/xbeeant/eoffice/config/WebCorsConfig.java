package io.github.xbeeant.eoffice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 *
 * @author xiaobiao
 * @version 2021/12/28
 */
@Configuration
public class WebCorsConfig implements WebMvcConfigurer {
    static final String[] ORIGINS = new String[]{"GET", "POST", "PUT", "DELETE"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedOrigins("*").allowedMethods(ORIGINS);
    }
}
