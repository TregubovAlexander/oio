package ru.elsu.oio.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import java.util.Locale;

@Configuration
public class InternationalizationConfig {

    private static final String MESSAGE_SOURCE = "/WEB-INF/i18n/messages";
    private static final String ERROR_SOURCE = "/WEB-INF/i18n/errors";

    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(MESSAGE_SOURCE, ERROR_SOURCE);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(5);
        return messageSource;
    }

    @Bean
    public SmartCookieLocaleResolver localeResolver(){
        SmartCookieLocaleResolver cookieLocaleResolver = new SmartCookieLocaleResolver();
        cookieLocaleResolver.setCookieName("locale");
        cookieLocaleResolver.setDefaultLocale(new Locale("ru"));
        cookieLocaleResolver.setCookieMaxAge(10000);
        return cookieLocaleResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor localeChangeInterceptor=new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }


}
