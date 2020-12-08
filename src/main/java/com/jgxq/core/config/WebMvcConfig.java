package com.jgxq.core.config;

import com.jgxq.core.intercepter.JwtLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * @author LuCong
 * @since 2020-12-08
 **/
@Configuration
public class WebMvcConfig  implements WebMvcConfigurer {

    @Autowired
    private JwtLoginInterceptor jwtLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 排除swagger 拦截路径-mfc
//        String[] excludePatterns = new String[]{"/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**",
//                "/api", "/api-docs", "/api-docs/**"};
//        List<String> swaggerExclude = Arrays.asList(excludePatterns);
//        registry.addInterceptor(new JwtAuthInterceptor()).addPathPatterns("/**").excludePathPatterns(swaggerExclude);

        registry.addInterceptor(jwtLoginInterceptor).addPathPatterns("/**");
    }
}
