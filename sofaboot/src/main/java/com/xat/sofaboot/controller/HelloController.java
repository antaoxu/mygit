package com.xat.sofaboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xuantao
 */
@Controller
public class HelloController {
    @RequestMapping("/hello")
    public void sayHello() {
        System.out.println("Hello World");
    }

}
