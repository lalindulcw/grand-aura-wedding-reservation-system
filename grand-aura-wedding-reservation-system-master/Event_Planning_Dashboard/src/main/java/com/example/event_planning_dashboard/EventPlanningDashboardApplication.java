package com.example.event_planning_dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventPlanningDashboardApplication{

    public static void main(String[] args) {
        SpringApplication.run(EventPlanningDashboardApplication.class, args);

        System.out.println("\n" + "=".repeat(50));
        System.out.println("🎉 EVENT PLANNING DASHBOARD STARTED! 🎉");
        System.out.println("=".repeat(50));
        System.out.println("🌐 Dashboard: http://localhost:8080/event-dashboard.html");
        System.out.println("=".repeat(50) + "\n");
    }
}