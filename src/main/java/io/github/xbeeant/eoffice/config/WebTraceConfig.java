package io.github.xbeeant.eoffice.config;

import io.github.xbeeant.mdc.trace.RequestMdcInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * TRACE 配置
 *
 * @author xiaobiao
 * @version 2021/10/8
 */
@Configuration
public class WebTraceConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestMdcInterceptor()).addPathPatterns("/**");
    }
}
