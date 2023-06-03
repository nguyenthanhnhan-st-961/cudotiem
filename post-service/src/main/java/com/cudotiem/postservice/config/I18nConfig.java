package com.cudotiem.postservice.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class I18nConfig {

	@Bean
	LocaleResolver localeResolver() {
		AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
		resolver.setDefaultLocale(Locale.US);
		return resolver;
	}

//	@Bean
//	LocaleChangeInterceptor localeInterceptor() {
//		LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
//		localeInterceptor.setParamName("lang");
//		return localeInterceptor;
//	}
//
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(localeInterceptor());
//	}
}
