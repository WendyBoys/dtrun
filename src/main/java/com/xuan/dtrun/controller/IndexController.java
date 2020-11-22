package com.xuan.dtrun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {


    @RequestMapping(value = {"","/login"})
    public String index() {
        return "redirect:login.html";
    }
}
