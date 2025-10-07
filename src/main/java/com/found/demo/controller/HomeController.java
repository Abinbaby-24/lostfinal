package com.found.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String login() {
        return "login";
    }
    
    @GetMapping("/home")
    public String home() {
        return "index";
    }
    
    @GetMapping("/report-lost")
    public String reportLost() {
        return "report-lost";
    }
    
    @GetMapping("/report-found")
    public String reportFound() {
        return "report-found";
    }
    
    @GetMapping("/view-items")
    public String viewItems() {
        return "view-items";
    }
}