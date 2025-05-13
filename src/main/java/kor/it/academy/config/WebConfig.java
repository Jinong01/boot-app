package kor.it.academy.config;

import jakarta.servlet.Filter;
import jakarta.servlet.MultipartConfigElement;
import kor.it.academy.config.interceptor.LoginInterceptor;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/board/**")
                .addPathPatterns("/user/**")
                .excludePathPatterns("/board/detail/view")
                .excludePathPatterns("/board/list")
                .excludePathPatterns("/user/join");
    }

    @Bean
    public Filter characterEncodingFilter(){
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    //업로드 설정
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.of(30, DataUnit.MEGABYTES));
        factory.setMaxRequestSize(DataSize.of(30, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }

    //업로드 처리를 위한 resolver 등록
    @Bean
    public MultipartResolver multipartResolver(){
        return new StandardServletMultipartResolver();
    }
}
