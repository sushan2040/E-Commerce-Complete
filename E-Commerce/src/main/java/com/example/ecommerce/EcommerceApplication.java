package com.example.ecommerce;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication(scanBasePackages = "com.example")
@EnableAutoConfiguration
@ComponentScan(basePackages = {
		"com.example" }, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE)

)
@EnableTransactionManagement
@EnableWebMvc
@EnableAsync
@EnableJpaAuditing
@EnableCaching
public class EcommerceApplication extends SpringBootServletInitializer implements WebMvcConfigurer{

	@Autowired
	private Environment environment;

	// @Autowired
	// private DataSource dataSource;


public static final org.slf4j.Logger logger = LoggerFactory.getLogger(EcommerceApplication.class);


	
	public static void main(String[] args) {
		new SpringApplicationBuilder(EcommerceApplication.class).sources(EcommerceApplication.class).run(args);


		logger.info(":: Ecommerce ::        (v1.1.RELEASE)");
		logger.info(":: JAVA version       ::        (" + System.getProperty("java.version") + ")");		
		System.out.println(" ****************************************************  ");
		logger.info(" **********************ApplicationConfiguration Init...  ");
		logger.info(" ********************** Ecommerce- API   ");
		logger.info(" ****************************************************  ");
				}

		
		@Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }	
	
		 
	    
		
	
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//
//		// registry.addMapping("/api/**,/*")
//		registry.addMapping("/**").allowedOriginPatterns("http://localhost:3000").allowedOrigins("http://localhost:3000").allowedMethods("POST", "GET", "PUT", "DELETE")
//				// .allowedHeaders("header1", "header2", "header3")
//				// .exposedHeaders("header1", "header2")
//				.allowCredentials(false).maxAge(3600);
//		System.out.println(" ************** CorsRegistry done....   ");
//		// Add more mappings...
//	}
		
		@Bean
		@Order(value = Ordered.HIGHEST_PRECEDENCE)
		public CorsFilter corsFilter() {
		    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		    source.registerCorsConfiguration("/**", buildCorsConfig());
		    logger.info("********************* CorsFilter Initialized *******************************");
		    return new CorsFilter(source);
		}
//
		@Bean
		public CorsConfiguration buildCorsConfig() {
		    CorsConfiguration corsConfiguration = new CorsConfiguration();

		    // Use setAllowedOrigins instead of setAllowedOriginPatterns if not using wildcards
		    List<String> allowedOrigins = List.of(
		            "http://localhost:3000",
					"http://31.97.110.118",
					"http://31.97.110.118:80",
					"http://localhost:80", 
		            "https://ecommerce.codebysushant.com",
		            "http://ecommerce.codebysushant.com",
		            "http://ecommerce-nlb-frontend-803c4743c5de5a4b.elb.eu-north-1.amazonaws.com"
		    );
		    corsConfiguration.setAllowedOrigins(allowedOrigins);

		    // Allow all headers
		    corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

		    // Allow all HTTP methods
		    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		    // Allow credentials (important if using cookies/session-based authentication)
		    corsConfiguration.setAllowCredentials(true);

		    // Set the max age for preflight requests (in seconds)
		    corsConfiguration.setMaxAge(3600L);

		    return corsConfiguration;
		}

		
//		@Bean
//		CorsConfigurationSource corsConfigurationSource() {
//		    CorsConfiguration configuration = new CorsConfiguration();
//		    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//		    configuration.setAllowedMethods(Arrays.asList("GET","POST","OPTION","OPTIONS","DELETE"));
//		    configuration.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
//		    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		    source.registerCorsConfiguration("/**", configuration);
//		    return source;
//		}
	
	/*
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		 
		servletContext.getSessionCookieConfig().setHttpOnly(true);        
		servletContext.getSessionCookieConfig().setSecure(true);  
		System.out.println(" ************** ServletContext done....   ");
	}
	*/

	@Bean
	// public LocalSessionFactoryBean sessionFactory(/*DataSource ds*/) /*throws
	// ClassNotFoundException*/ {
	@Primary
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
		logger.info(" **********************setDataSource Init...  ");
		localSessionFactoryBean.setDataSource(dataSource());
		localSessionFactoryBean.setPackagesToScan("com.example");

		localSessionFactoryBean.setHibernateProperties(hibernateProperties());
		try {
			localSessionFactoryBean.afterPropertiesSet();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info(" **********************setDataSource Init...  "+localSessionFactoryBean.getObject().isOpen());
		return localSessionFactoryBean;
	}
	@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example"); // Adjust package name for your entities

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
		dataSource.setUrl(environment.getRequiredProperty("spring.datasource.url"));
		dataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
		dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));

		logger.info(" ************ Database :  " + dataSource.getUrl());
		return dataSource;
	}


//	@Bean
//	public HibernateTransactionManager transactionManager(SessionFactory sf) {
//		return new HibernateTransactionManager(sf);
//	}

	@Bean
	public Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
		properties.put("hibernate.proc.param_null_passing",
				environment.getRequiredProperty("hibernate.proc.param_null_passing"));
		properties.put("hibernate.format_sql", environment.getProperty("hibernate.format_sql"));

		return properties;
	}
	/*
	 * @Autowired Environment environment;
	 * 
	 * @Bean(name = "viewResolver") public InternalResourceViewResolver
	 * getViewResolver() { InternalResourceViewResolver viewResolver = new
	 * InternalResourceViewResolver(); viewResolver.setPrefix("/WEB-INF/");
	 * viewResolver.setSuffix(".jsp"); return viewResolver; }
	 * 
	 * @Bean public BasicDataSource getDataSource() { BasicDataSource dataSource =
	 * new BasicDataSource();
	 * dataSource.setDriverClassName(environment.getProperty("db.driverClassName"));
	 * dataSource.setUrl(environment.getProperty("db.databaseUrl"));
	 * dataSource.setUsername(environment.getProperty("db.username"));
	 * dataSource.setPassword(environment.getProperty("db.password")); return
	 * dataSource; }
	 * 
	 * @Bean public LocalSessionFactoryBean getSessionFactory() {
	 * LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
	 * factoryBean.setDataSource(getDataSource());
	 * 
	 * Properties props = new Properties(); props.put("hbm.dialect",
	 * environment.getProperty("hbm.dialect")); props.put("hibernate.show_sql",
	 * environment.getProperty("hbm.show_sql")); props.put("hibernate.hbm2ddl.auto",
	 * environment.getProperty("hbm.hbm2ddl.auto"));
	 * props.put("spring.jpa.database",
	 * environment.getProperty("spring.jpa.database"));
	 * 
	 * factoryBean.setHibernateProperties(props);
	 * factoryBean.setPackagesToScan("com.hms.*"); return factoryBean; }
	 * 
	 * @Bean public HibernateTransactionManager getTransactionManager() {
	 * HibernateTransactionManager transactionManager = new
	 * HibernateTransactionManager();
	 * transactionManager.setSessionFactory(getSessionFactory().getObject()); return
	 * transactionManager; }
	 */

//	@Bean
//	public ViewResolver viewResolver() {
//		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//		viewResolver.setViewClass(JstlView.class);
//		viewResolver.setPrefix("/");
//		// viewResolver.setPrefix("/"); 
//		viewResolver.setSuffix(".jsp");
//
//		return viewResolver;
//	}

	
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		return messageSource;
	}

//	@Bean
//	public ModelMapper modelMapper() {
//		return new ModelMapper();
//	}

	@Autowired
	private DataSourceProperties dataSourceProperties;

}
