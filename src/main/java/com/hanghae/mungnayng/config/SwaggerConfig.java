package com.hanghae.mungnayng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean   /* Docket : Swagger 설정을 할 수 있게 도와주는 클래스 */
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .useDefaultResponseMessages(false)  /* Swagger에서 제공해주는 응답코드에 대한 기본 메시지 적용 or 제거(false) */
                .select()   /* ApiSelectorBuilder를 생성하여 apis()와 paths() 사용할 수 있게해줌 */
                .apis(RequestHandlerSelectors.basePackage("com.hanghae.mungnayng.controller"))  /* API가 작성된 패키지 지정 */
                .paths(PathSelectors.any()) /* apis()로 설정된 패키지 중 문서화할 path를 지정, any() -> 모든 API 문서화 */
                .build();
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