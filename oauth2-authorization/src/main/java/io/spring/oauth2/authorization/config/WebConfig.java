package io.spring.oauth2.authorization.config;

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
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {

    }

    /**
     * Initializes Metrics.
     */
    @SuppressWarnings("Duplicates")
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

    /**
     * Configure simple automated controllers pre-configured with the response status code and/or a view to render
     * the response body. This is useful in cases where there is no need for custom controller logic (e.g. render a
     * home page), perform simple site URL redirects, return a 404 status with HTML content, a 204 with no content, etc.
     *
     * @param registry helper class to assist with the registration of simple automated controllers pre-configured
     *                 with status code and/or a view.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
}