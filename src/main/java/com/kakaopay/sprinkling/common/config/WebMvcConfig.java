package com.kakaopay.sprinkling.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final HandlerInterceptor sprinklingAuthInterceptor;

  public WebMvcConfig(HandlerInterceptor sprinklingAuthInterceptor) {
    this.sprinklingAuthInterceptor = sprinklingAuthInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(sprinklingAuthInterceptor)
            .addPathPatterns("/api/**");
  }

}
