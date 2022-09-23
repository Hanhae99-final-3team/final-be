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
        return new JPAQueryFactory(entityManager);
    }
}
