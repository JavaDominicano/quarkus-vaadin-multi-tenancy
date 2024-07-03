package org.jconfdominicana.security.filters;

import com.vaadin.flow.server.VaadinSession;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

//@WebFilter("/*")
public class CustomWebFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        System.out.println(request.getRequestURI());

        request.getSession()
                .removeAttribute("User");

        chain.doFilter(request, response);
    }


}
