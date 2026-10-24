package com.laxmifinance.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Keeps the legacy ".action" URLs (used by the old JavaScript and bookmarks)
 * working by exposing the clean Spring MVC path to the dispatcher.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class ActionSuffixFilter extends OncePerRequestFilter {

    private static final String SUFFIX = ".action";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri != null && uri.endsWith(SUFFIX)) {
            String cleanPath = uri.substring(0, uri.length() - SUFFIX.length());
            filterChain.doFilter(new CleanPathRequest(request, cleanPath), response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private static final class CleanPathRequest extends HttpServletRequestWrapper {

        private final String cleanPath;

        private CleanPathRequest(HttpServletRequest request, String cleanPath) {
            super(request);
            this.cleanPath = cleanPath;
        }

        @Override
        public String getRequestURI() {
            return cleanPath;
        }

        @Override
        public String getServletPath() {
            String servletPath = super.getServletPath();
            if (servletPath != null && servletPath.endsWith(SUFFIX)) {
                return servletPath.substring(0, servletPath.length() - SUFFIX.length());
            }
            return cleanPath;
        }

        @Override
        public StringBuffer getRequestURL() {
            StringBuffer url = new StringBuffer();
            url.append(getScheme()).append("://").append(getServerName());
            int port = getServerPort();
            if (("http".equals(getScheme()) && port != 80) || ("https".equals(getScheme()) && port != 443)) {
                url.append(':').append(port);
            }
            url.append(cleanPath);
            return url;
        }
    }
}
