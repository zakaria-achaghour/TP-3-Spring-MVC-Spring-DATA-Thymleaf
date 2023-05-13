package com.zakaria.hospital.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {
    @GetMapping("notAuthorized")
    public String notAuthorize() {
        return "notAuthorized";
    }
    @GetMapping("login")
    public String login() {
        return "login";
    }
}
