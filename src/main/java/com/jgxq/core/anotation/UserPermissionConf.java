package com.jgxq.core.anotation;

import com.jgxq.core.enums.UserPermissionType;

import java.lang.annotation.*;

/**
 * @author LuCong
 * @since 2020-12-08
 **/
@Documented//@Documented注解标记的元素，Javadoc工具会将此注解标记元素的注解信息包含在javadoc中。
@Target({ElementType.TYPE})//类注解
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPermissionConf {
    UserPermissionType Type() default UserPermissionType.FORBIDDEN;
}
