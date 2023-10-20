package com.lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/18 下午 04:40
 */


@RestController
@RequestMapping("/hello")
public class UserVerifyController {

    @GetMapping("/demo")
    public String login() {
        return "auth check ok";
    }

}
