package com.example.batchprocessing;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class MdcUriFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        MDC.put("uri", httpRequest.getRequestURI());
        
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("uri"); 
        }
    }
}
