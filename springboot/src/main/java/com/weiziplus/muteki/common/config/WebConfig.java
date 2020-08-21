package com.weiziplus.muteki.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.weiziplus.muteki.core.api.common.interceptor.WebAuthorizationInterceptor;
import com.weiziplus.muteki.core.pc.common.interceptor.PcAuthorizationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 配置过滤器、拦截器等
 *
 * @author wanglongwei
 * @date 2019/5/7 8:42
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 拦截器配置---重写父类方法
     * <p>
     * addPathPatterns  拦截所有请求
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截/pc/请求
        registry.addInterceptor(pcAuthorizationInterceptor())
                // 放过swagger-ui
                .excludePathPatterns(
                        "/swagger-resources/**", "/webjars/**", "/swagger-ui.html/**", "/doc.html/**",
                        "/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/**/*.gif", "/**/*.bmp")
                .addPathPatterns("/pc/**");
        //拦截/api/请求
        registry.addInterceptor(webAuthorizationInterceptor())
                // 放过swagger-ui
                .excludePathPatterns(
                        "/swagger-resources/**", "/webjars/**", "/swagger-ui.html/**", "/doc.html/**",
                        "/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/**/*.gif", "/**/*.bmp")
                .addPathPatterns("/api/**");
    }

    /**
     * 自定义的拦截器interceptor
     *
     * @return
     */
    @Bean
    public PcAuthorizationInterceptor pcAuthorizationInterceptor() {
        return new PcAuthorizationInterceptor();
    }

    /**
     * 自定义的拦截器interceptor
     *
     * @return
     */
    @Bean
    public WebAuthorizationInterceptor webAuthorizationInterceptor() {
        return new WebAuthorizationInterceptor();
    }

    /**
     * 静态资源
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

    /**
     * 返回json时候将long类型转换为String类型
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
    }

}