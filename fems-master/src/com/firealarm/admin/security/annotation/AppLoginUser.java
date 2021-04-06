package com.firealarm.admin.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 * Annotation that is used to resolve {@link Authentication#getPrincipal()} to a method
 * argument.
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 * @author Rob Winch
 * @since 4.0
 *
 * @see org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver
 */
@Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface AppLoginUser {}
