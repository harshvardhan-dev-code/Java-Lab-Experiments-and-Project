package com.servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

// Notified when the web application starts and stops
@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("Application Started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Application Stopped");
    }
}
