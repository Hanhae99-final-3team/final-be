package com.hanghae.mungnayng.util;

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class MysqlDialect extends MySQL8Dialect {     /* 기존 MySqlDialect 클래스를 상속받아 Custom 하는 클래스 */
    public MysqlDialect() {                           /* MATCH 함수 사용 위함 */
        super();    /* 권한 부여 */
        registerFunction(   /* registerFunction - MySQL에 등록된 메서드를 사용할 수 있게끔 해주는 메서드 */
                "match",    /* match 함수 return type -> double, match(4개의 args) against(input 값 in boolean mode)로 사용핟도록 세팅 */
                new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1, ?2, ?3, ?4) against (?5 in boolean mode)")
        );
    }
}