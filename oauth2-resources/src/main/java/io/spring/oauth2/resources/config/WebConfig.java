package io.spring.oauth2.resources.config;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlets.MetricsServlet;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.*;
import java.util.EnumSet;

import static org.slf4j.LoggerFactory.getLogger;

@EnableWebMvc
@Configuration
public class WebConfig
        extends WebMvcConfigurerAdapter
        implements ServletContextInitializer, EmbeddedServletContainerCustomizer {

    private final Logger LOG = getLogger(WebConfig.class);

    @Autowired(required = false)
    private MetricRegistry metricRegistry;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
        initMetrics(servletContext, disps);
        LOG.info("OAuth2 Server is now fully configured");
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {

    }

    /**
     * Initializes Metrics.
     */
    private void initMetrics(ServletContext servletContext, EnumSet<DispatcherType> disps) {
        LOG.debug("Initializing Metrics registries");
        servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE, metricRegistry);
        servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY, metricRegistry);

        LOG.debug("Registering Metrics Filter");
        FilterRegistration.Dynamic metricsFilter =
                servletContext.addFilter("webappMetricsFilter", new InstrumentedFilter());

        metricsFilter.addMappingForUrlPatterns(disps, true, "/*");
        metricsFilter.setAsyncSupported(true);

        LOG.debug("Registering Metrics Servlet");
        ServletRegistration.Dynamic metricsAdminServlet =
                servletContext.addServlet("metricsServlet", new MetricsServlet());

        metricsAdminServlet.addMapping("/metrics/metrics/*");
        metricsAdminServlet.setAsyncSupported(true);
        metricsAdminServlet.setLoadOnStartup(2);
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(newArrayList(ALL));
//        config.setAllowedMethods(newArrayList("POST", "PUT", "GET", "DELETE"));
//        config.setAllowedHeaders(newArrayList("content-type", "access-control-request-headers",
//                "access-control-request-method", "accept", "origin", "authorization", "x-requested-with"));
//        config.setMaxAge(3600L);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/*", config);
//        return new CorsFilter(source);
//    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/login").setViewName("login");
    }
}