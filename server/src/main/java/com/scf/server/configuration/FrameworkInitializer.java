package com.scf.server.configuration;

import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import com.scf.server.configuration.filter.SimpleCORSFilter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class FrameworkInitializer extends
        AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { SpringRootConfig.class, SpringSecurityConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { SpringWebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/api/v1.0/*" };
	}

	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		servletContext
				.addFilter("springSecurityFilterChain",
						new DelegatingFilterProxy("springSecurityFilterChain"))
				.addMappingForUrlPatterns(null, false, "/*");
		servletContext.addFilter("simpleCORSFilter", new SimpleCORSFilter()).addMappingForUrlPatterns(null, false, "/*");
		super.onStartup(servletContext);
	}

}