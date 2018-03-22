package ru.elsu.oio.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.elsu.oio.Application;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@PropertySources(value = { @PropertySource("classpath:/application.properties") })
@ComponentScan(basePackageClasses = Application.class)
public class HibernateConfig {

    @Value("${jndi.name}")
    private String JNDI_NAME;

    @Value("${jndi.proxy_interface}")
    private String JNDI_PROXY_INTERFACE;

    @Value("${hibernate.dialect}")
    private String KEY_HIBERNATE_DIALECT;

    @Value("${hibernate.show_sql}")
    private String KEY_HBERNATE_SHOW_SQL;

    @Value("${hibernate.connection.useUnicode}")
    private String KEY_HIBERNATE_CONNECTION_USEUNICODE;

    @Value("${hibernate.connection.characterEncoding}")
    private String KEY_HIBERNATE_CONNECTION_CHARACTERENCODING;

    @Value("${hibernate.connection.charSet}")
    private String KEY_HIBERNATE_CONNECTION_CHARSET;

    @Value("${packagesToScan}")
    private String KEY_ENTITIES_PKG;


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    // Подключаем фабрику сеансов Hibernate
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource());
        factory.setPackagesToScan("ru.elsu.oio");
        factory.setHibernateProperties(hibernateProperties());
        return factory;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", KEY_HIBERNATE_DIALECT);
        properties.setProperty("hibernate.show_sql", KEY_HBERNATE_SHOW_SQL);
        properties.setProperty("hibernate.connection.useUnicode", KEY_HIBERNATE_CONNECTION_USEUNICODE);
        properties.setProperty("hibernate.connection.characterEncoding", KEY_HIBERNATE_CONNECTION_CHARACTERENCODING);
        properties.setProperty("hibernate.connection.charSet", KEY_HIBERNATE_CONNECTION_CHARSET);
        return properties;
    }

    // Подключаем менеджер транзакций
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    // Получаем источник данных из JNDI (из настроек GlassFish)
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        lookup.setResourceRef(true);
        lookup.setJndiEnvironment(jndiEnvironment());
        return lookup.getDataSource(JNDI_NAME);
    }

    private Properties jndiEnvironment() {
        Properties properties = new Properties();
        properties.setProperty("proxy-interface", JNDI_PROXY_INTERFACE);
        return properties;
    }


}
