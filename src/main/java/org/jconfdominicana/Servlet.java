package org.jconfdominicana;

import com.vaadin.quarkus.QuarkusVaadinServlet;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;

//@WebServlet(urlPatterns = "/*", name = "VaadinServlet", asyncSupported = true, initParams = {
//        // TODO: Enable dev mode gizmo when issues with Atmosphere and Quarkus are resolved
//        // https://github.com/vaadin/quarkus/issues/5
//        @WebInitParam(name = "devmode.gizmo.enabled", value = "false")})
//public class Servlet extends QuarkusVaadinServlet {
//}