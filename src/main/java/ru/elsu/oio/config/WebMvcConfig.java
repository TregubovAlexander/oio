package ru.elsu.oio.config;

import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring.support.ThymeleafLayoutInterceptor;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import ru.elsu.oio.Application;

import java.util.List;


@Configuration
@ComponentScan(basePackageClasses = {Application.class})
class WebMvcConfig extends WebMvcConfigurationSupport {

    private static final String VIEWS = "/WEB-INF/views/";
    private static final String UTF8 = "UTF-8";

    @Autowired
    LocaleChangeInterceptor localeChangeInterceptor;

    @Autowired
    MessageSource messageSource;

    // THYMELEAF-SPECIFIC ARTIFACTS  ( TemplateResolver <- TemplateEngine <- ViewResolver )
    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix(VIEWS);
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding(UTF8);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false); // Template cache is true by default. Set to false if you want templates to be automatically updated when modified
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setEnableSpringELCompiler(true);
        templateEngine.addTemplateResolver(templateResolver());
        templateEngine.addDialect(new Java8TimeDialect());
        templateEngine.addDialect(new SpringSecurityDialect());
        templateEngine.addDialect(new DataAttributeDialect()); // https://github.com/mxab/thymeleaf-extras-data-attribute
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding(UTF8);
        return viewResolver;
    }
    // /THYMELEAF-SPECIFIC ARTIFACTS


    // Обрабатывает запросы на получение статических ресурсов
    // Handles HTTP GET requests for /resources/** by efficiently serving up static resources in the ${webappRoot}/resources directory
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/")
                //.setCachePeriod(3600)         // TODO: включить в продакшене
                .resourceChain(true);
    }

    // Поддержка загрузки файлов (фотографий) на сервер (не более 10Мб)
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        multipartResolver.setMaxUploadSize(10485760);
        return multipartResolver;
    }

    @Bean
    public ThymeleafLayoutInterceptor thymeleafLayoutInterceptor() {
        return new ThymeleafLayoutInterceptor();
    }

    // Регистрируем Interceptor (Перехват запросов в Spring MVC)
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(thymeleafLayoutInterceptor());
        registry.addInterceptor(localeChangeInterceptor);
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource);
        return factory;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    // Форматируем вывод JSON-ов
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        for ( HttpMessageConverter<?> converter : converters ) {
            if ( converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jacksonConverter = (MappingJackson2HttpMessageConverter) converter;
                jacksonConverter.setPrettyPrint( true );
            }
        }

    }


}
