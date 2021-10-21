package com.mrluo.cloud.config;


import com.mrluo.cloud.common.defs.NewsDefs;
import com.mrluo.cloud.security.NewsWebSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerAutoConfiguration {
    @Autowired
    private ServerProperties serverProperties;

    @Bean("swaggerDocket")
    @ConditionalOnMissingBean(name = "swaggerDocket")
    public Docket swaggerDocket() {
        return new Docket(DocumentationType.OAS_30).apiInfo(ApiInfo.DEFAULT)
                .globalRequestParameters(Collections.singletonList(new RequestParameterBuilder()
                        .name(NewsWebSessionService.TOKEN_HEADER_KEY)
                        .description("AccessToken").in(ParameterType.HEADER)
                        .required(false)
                        .build())).select()
                .paths(PathSelectors.ant(serverProperties.getServlet().getContextPath()
                        + NewsDefs.API_PREFIX_URI + "/**"))
                .build();
    }
}
