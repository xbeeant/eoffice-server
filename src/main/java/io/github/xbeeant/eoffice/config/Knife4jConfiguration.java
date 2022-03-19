package io.github.xbeeant.eoffice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author xiaobiao
 * @version 2022/3/19
 */
@Configuration
@EnableSwagger2
public class Knife4jConfiguration {

    @Bean(value = "apidoc")
    public Docket apidoc() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("eoffice 接口文档")
                        .description("# eoffice RESTful APIs")
                        .contact(new Contact("黄小标", "xbeeant.cn", "huangxb0512@gmail.com"))
                        .version("1.0")
                        .license("MIT ")
                        .build())
                //分组名称
                .groupName("1.X版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("io.github.xbeeant.eoffice.rest"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}