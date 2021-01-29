package com.ustglobal.rapido.mapping.infrastructure.web;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CorrelationIdFilter implements Filter {

  public static final String CORRELATION_ID = "rapidoCorrelationId";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

    String correlationId = httpRequest.getHeader("CORRELATION_ID");

    // Note that the filter does not aggressively look for a correlation id, rather if there
    // is such a header field it adds it to MDC and response header.
    if(!StringUtils.isEmpty(correlationId)) {

      // Add correlation id to MDC for logging
      MDC.put(CORRELATION_ID, correlationId);

      // Set the correlation id back on response header.
      HttpServletResponse response = (HttpServletResponse) servletResponse;
      response.setHeader(CORRELATION_ID, correlationId);
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy() {

  }
}
