package com.jgxq.core.config;

import com.jgxq.core.intercepter.JwtLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * @author LuCong
 * @since 2020-12-08
 **/
@Configuration
public class WebMvcConfig  implements WebMvcConfigurer {

    @Value("${web.upload-path}")//将静态资源映射到本地
    private String mImagesPath;

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

    //配置静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("file:" + mImagesPath);
    }

    //跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowCredentials(true)
                .maxAge(3600)
                .allowCredentials(true);
    }
}
