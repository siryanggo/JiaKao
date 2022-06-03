package com.mj.jk.common.cfg;

import com.mj.jk.common.shiro.TokenFilter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerCfg implements InitializingBean {
    @Autowired
    private Environment environment;
    private boolean enable;

    @Bean
    public Docket sysDocket() {
        return groupDocket(
                "01_系统",
                "/sys.*",
                "系统模块文档",
                "用户，角色，资源");
    }

    @Bean
    public Docket metadataDocket() {
        return groupDocket(
                "02_元数据",
                "/(dict.*|plate.*)",
                "元数据模块文档",
                "数据字典类型，数据字典条目，省份，城市");
    }

    @Bean
    public Docket examDocket() {
        return groupDocket(
                "03_考试",
                "/exam.*",
                "考试模块文档",
                "考场，科1科4，科2科3");
    }

    private Docket groupDocket(String group,
                               String regex,
                               String title,
                               String description) {
        return basicDocket()
                .groupName(group)
                .apiInfo(apiInfo(title, description))
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.regex(regex))
                .build();
    }

    private Docket basicDocket() {
        List<RequestParameter> list = new ArrayList<>();
        RequestParameter token = new RequestParameterBuilder()
                .name(TokenFilter.HEADER_TOKEN)
                .description("用户登录令牌")
                .in(ParameterType.HEADER)
                .build();
        list.add(token);
        return new Docket(DocumentationType.SWAGGER_2)
                .globalRequestParameters(list)
                .ignoredParameterTypes(
                        HttpSession.class,
                        HttpServletRequest.class,
                        HttpServletResponse.class)
                .enable(enable);
    }

    private ApiInfo apiInfo(String title, String description) {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version("1.0.0")
                .build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        enable = environment.acceptsProfiles(Profiles.of("dev", "test"));
    }

//    @Bean
//    public Docket docket() {
//        Docket docket = new Docket(DocumentationType.SWAGGER_2);
//        docket.apiInfo(apiInfo());
//        ApiSelectorBuilder builder = docket.select();
//        builder.paths(PathSelectors.ant("/dictItems/**"));
//        return docket;
//    }
//
//    private ApiInfo apiInfo() {
//        ApiInfoBuilder builder = new ApiInfoBuilder();
//        builder.title("小码哥驾考系统接口文档");
//        builder.description("这是一份比较详细的接口文档");
//        builder.version("1.0.0");
//        return builder.build();
//    }
}
