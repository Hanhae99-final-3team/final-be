package com.hanghae.mungnayng.config;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebMvc
public class SwaggerConfig {



    @Bean   /* Docket : Swagger 설정을 할 수 있게 도와주는 클래스 */
    public Docket api() {
TypeResolver typeResolver = new TypeResolver();

        return new Docket(DocumentationType.OAS_30)    /* Swagger Ui Authorize 사용 위한 Swagger 3.0 ver 적용 */
                .alternateTypeRules(    /* Swagger Ui에서 Pageable 관련 Request Param key 변경 위한 설정(pageNumber -> page) */
                        AlternateTypeRules.newRule(typeResolver.resolve(Pageable.class),
                                typeResolver.resolve(Page.class)))
                .securityContexts(Arrays.asList(securityContext()))    /* Swagger Ui에서 JWT 토큰 값을 넣기 위한 설정 */
                .securitySchemes(Arrays.asList(apiKey()))   /* Swagger Ui에서 JWT 토큰 값을 넣기 위한 설정 */
                .apiInfo(this.apiInfo())
                .useDefaultResponseMessages(false)  /* Swagger에서 제공해주는 응답코드에 대한 기본 메시지 적용 or 제거(false) */
                .select()   /* ApiSelectorBuilder를 생성하여 apis()와 paths() 사용할 수 있게해줌 */
                .apis(RequestHandlerSelectors.basePackage("com.hanghae.mungnayng.controller"))  /* API가 작성된 패키지 지정 */
                .paths(PathSelectors.any()) /* apis()로 설정된 패키지 중 문서화할 path를 지정, any() -> 모든 API 문서화 */
                .build();
    }

    /* 인증 방식 설정 - 전역 AuthorizationScope를 사용하여 JWT SecurityContext를 구성.*/
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

    /* 버튼 클릭 시 입력값 설정 - JWT를 인증 헤더로 포함하도록 ApiKey를 정의*/
    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");    /* keyname 설정에 유의 - 토큰 헤더 명 기입 */
    }

    @Getter
    @Setter
    @ApiModel
    static class Page {
        @ApiModelProperty(value = "페이지 번호")
        private Integer page;

        @ApiModelProperty(value = "한 페이지 당 불러올 정보(게시글) 수", allowableValues="range[0, 100]")
        private Integer size;
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "멍냥", "반려동물 중고용품 거래 서비스 멍냥 API Docs",
                "ver 1.0",
                "https://github.com/Hanhae99-final-3team",
                new Contact("MungNayang", "https://github.com/Hanhae99-final-3team", "jy0511_@naver.com"),
                "Licenses",
                "https://github.com/Hanhae99-final-3team",

                new ArrayList<>());
    }
}
