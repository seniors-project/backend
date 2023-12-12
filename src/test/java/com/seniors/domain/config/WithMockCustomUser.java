package com.seniors.domain.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    long id() default 123456789L;
    String snsId() default "snsId";
    String email() default "test@test.com";
    String nickName() default "TEST";
    String gender() default "male";
    String ageRange() default "20~29";
    String birthday() default "12-31";
    String profileImageUrl() default "profileImageUrl";
    String role() default "USER";

}
