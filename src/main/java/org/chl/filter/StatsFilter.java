package org.chl.filter;

import java.io.IOException;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.chl.model.ServiceResponseTime;
import org.chl.repository.ServiceResponseTimeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@WebFilter("/*")
public class StatsFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsFilter.class);
    @Autowired
    ServiceResponseTimeRepository serviceResponseTimeRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // empty
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        long time = System.currentTimeMillis();
        try {
            chain.doFilter(req, resp);
        } finally {
            time = System.currentTimeMillis() - time;
            LOGGER.trace("{}: {} ms ", ((HttpServletRequest) req).getRequestURI(),  time);
            System.out.println(String.format("%s: %s  ms ", ((HttpServletRequest) req).getRequestURI(),  time));
            ServiceResponseTime serviceResponseTime = new ServiceResponseTime();
            serviceResponseTime.setServiceName(((HttpServletRequest) req).getRequestURI().replace("/", ""));
            serviceResponseTime.setResponseTime(time);
            serviceResponseTime.setInsertDateTime(new Date());
            serviceResponseTimeRepository.save(serviceResponseTime);
        }
    }

    @Override
    public void destroy() {
        // empty
    }
}