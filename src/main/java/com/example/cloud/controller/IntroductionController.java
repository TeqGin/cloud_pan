package com.example.cloud.controller;


import com.example.cloud.dao.DocumentRepository;
import com.example.cloud.dao.TypeRepository;
import com.example.cloud.dao.UserRepository;
import com.example.cloud.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/info")
@Controller
public class IntroductionController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/about")
    public String aboutIndex(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            user = new User();
            user.setName("游客访问");
        }
        model.addAttribute("user", user);
        return "/introduction/about";
    }
}
