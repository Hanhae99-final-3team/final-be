package com.hanghae.mungnayng.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QuerydslConfig {
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {      /* JPAQueryFactory를 사용하면 EntityManager를 통해 질의가 처리되고,JPQL을 생성하여 처리 */
        return new JPAQueryFactory(entityManager);  /* EntityManager는 영속성 컨텍스트에 접근하여 Entity에 대한 DB 작업을 제공하고 관리한다 */
    }                                               /* 영속성 컨텍스트란 Entity를 영구적으로 저장하는 환경을 뜻한다 */
}                                                   /* 어플리케이션과 DB사이의 객체를 저장하는 가상의 DB 역할 수행 */
