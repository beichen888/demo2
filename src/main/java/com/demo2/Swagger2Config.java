package com.demo2;

import com.demo2.common.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2 配置
 *
 * @author xxx
 */

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = {"com.demo2"})
public class Swagger2Config {
    @Autowired
    private Config config;

    @Bean
    public Docket addUserDocket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        ApiSelectorBuilder builder = docket.apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.demo2"));
        // 生产环境不生成swagger
        String profile = config.getEnv();
            if(profile.contains("prod")) {
                builder.paths(PathSelectors.none()).build();
                return docket;
        }
        builder.paths(PathSelectors.any()).build();
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("demo2 API接口说明")
                .termsOfServiceUrl("https://www.demo2.cn")
                .description("demo后端API接口说明")
                .license("License Version 2.0")
                .licenseUrl("https://www.demo2.cn")
                .version("1.0").build();
    }
}
