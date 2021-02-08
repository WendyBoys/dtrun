package com.xuan.dtrun.config;

import com.xuan.dtrun.interceptor.TokenInterceptor;
import com.xuan.dtrun.utils.ClientIpResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.List;


@Configuration
public class CorsConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor())
                .excludePathPatterns("/user/login", "/user/register", "/", "/login", "/icon/**")
                .addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String iconpath = new File("file:/" + System.getProperty("user.dir")).getParent() + "/icon/";
        registry.addResourceHandler("/icon/**").addResourceLocations(iconpath);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(clientIpResolver());
    }

    @Bean
    public ClientIpResolver clientIpResolver() {
        return new ClientIpResolver();
    }

}
