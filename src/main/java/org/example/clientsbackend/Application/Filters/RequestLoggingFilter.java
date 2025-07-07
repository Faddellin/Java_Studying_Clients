package org.example.clientsbackend.Application.Filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RequestLoggingFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger("JSON_REQUESTS_LOGGER");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);

        chain.doFilter(requestWrapper, response);

        Map<String, Object> logData = new HashMap<>();
        logData.put("method", httpRequest.getMethod());
        logData.put("url", httpRequest.getRequestURI());

        if ("POST".equals(httpRequest.getMethod())) {
            logData.put("body", requestWrapper.getContentAsString());
        }

        logger.info("{}",logData);
    }


}
