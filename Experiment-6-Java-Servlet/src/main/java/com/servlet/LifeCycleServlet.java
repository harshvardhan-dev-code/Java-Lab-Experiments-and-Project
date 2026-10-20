package com.servlet;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Demonstrates the Servlet life cycle: init() -> service()/doGet() -> destroy()
@WebServlet("/lifecycle")
public class LifeCycleServlet extends HttpServlet {

    @Override
    public void init() {
        System.out.println("Servlet Initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        response.getWriter().println("Request Processed");
    }

    @Override
    public void destroy() {
        System.out.println("Servlet Destroyed");
    }
}
