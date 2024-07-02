package org.jconfdominicana.security.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter("/")
public class CustomWebFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println(((HttpServletRequest) servletRequest).getSession());
        ((HttpServletRequest) servletRequest).getSession()
                        .setAttribute("User", "Probando");

        filterChain.doFilter(servletRequest, servletResponse);
    }


}
