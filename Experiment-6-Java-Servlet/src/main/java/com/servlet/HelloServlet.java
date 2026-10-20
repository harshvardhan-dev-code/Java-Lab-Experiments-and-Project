package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    // Handles GET requests: /hello  or  /hello?name=Harsh
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        if (name == null || name.isEmpty()) {
            out.println("<h1>Hello Students!</h1>");
            out.println("<h2>Welcome to Servlet Programming</h2>");
        } else {
            out.println("<h1>Welcome " + name + "</h1>");
        }
    }

    // Handles POST requests from the form in hello.html
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        String name = request.getParameter("name");
        PrintWriter out = response.getWriter();
        out.println("<h1>Welcome " + name + "</h1>");
    }
}
