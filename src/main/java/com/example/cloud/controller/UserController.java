package com.example.cloud.controller;

import com.alibaba.fastjson.JSON;
import com.example.cloud.dao.DocumentRepository;
import com.example.cloud.dao.TypeRepository;
import com.example.cloud.dao.UserRepository;
import com.example.cloud.domain.Document;
import com.example.cloud.domain.Type;
import com.example.cloud.domain.User;
import com.example.cloud.service.MailSendService;
import com.example.cloud.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author TeqGin
 */
@Controller
@RequestMapping("/user")
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private MailSendService mailSendService;

    @Value("${spring.mail.username}")
    private String from;

    @GetMapping("/login")
    public String login(){
        return "/user/login";
    }

    //主页
    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        List<Document> documentList = documentRepository.findAll();
        String allNums = "已全部加载，共" + documentList.size() + "个文件";
        String type = "全部文件";
        List<Type> typeList = typeRepository.findAll();
        model.addAttribute("typeList", typeList);
        model.addAttribute("type", type);
        model.addAttribute("all_nums", allNums);
        model.addAttribute("documentList", documentList);
        model.addAttribute("user", user);
        return "/user/index";
    }

    @GetMapping("/sign_up")
    public String signUp(){
        return "/user/sign_up";
    }


    @PostMapping("/verify")
    @ResponseBody
    public User verify(@RequestParam("account")String account,
                       @RequestParam("password")String password,
                       HttpServletResponse response,
                       HttpServletRequest request){
        if (userRepository.existsById(account)){
            //avoid id not found exception
            User user = userRepository.getOne(account);
            if (user.getAccount().equals(account)
                    && user.getPassword().equals(PasswordUtil.encodePassword(password))){
                //cookie已暂停使用
                Cookie cookie = new Cookie("account", account);
                //set access permission:everywhere
                cookie.setPath("/");
                response.addCookie(cookie);
                //使用session
                request.getSession().setAttribute("user", user);

                return user;
            }
        }
        return null;
    }

    @PostMapping("/veri_sign_up")
    @ResponseBody
    public User verifySignUp(@RequestParam("account")String account,
                             @RequestParam("password")String password,
                             @RequestParam("name")String name){
        //confirm
        if(userRepository.existsById(account)){
            return null;
        }else {
            User user = new User();
            user.setAccount(account);
            user.setPassword(PasswordUtil.encodePassword(password));
            user.setName(name);
            userRepository.save(user);
            return user;
        }
    }

    @GetMapping("/personal")
    public String personalIndex(Model model,HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        List<Type> types = typeRepository.findAll();
        List<Document> documents = documentRepository.findDocumentsByUserId(user.getAccount());
        HashMap<String, Integer> graphX = new HashMap<>();
        for (Type type: types){
            String typeName = type.getTypeName();
            int length = documentRepository.findDocumentsByTypeName(typeName).size();
            graphX.put(typeName, length);
        }
        model.addAttribute("graphX", JSON.toJSON(graphX));
        model.addAttribute("documents", documents);
        model.addAttribute("user", user);
        return "/user/personal";
    }

    @GetMapping("/change_password")
    public String changePassword(Model model, HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        return "/user/change_password";
    }
    @GetMapping("/change_account")
    public String changeEmail(Model model, HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        return "/user/change_email";
    }

    @GetMapping("/change_name")
    public String changeUsername(Model model, HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        return "/user/change_username";
    }



    @GetMapping("/forget")
    public String forgetIndex(){
        return "/user/forget";
    }

    @PostMapping("/verify_forget")
    @ResponseBody
    public User verifyForget(@RequestParam("account")String account,
                             @RequestParam("password")String password,
                             @RequestParam("verify_code")String verifyCode) throws Exception {
        if (userRepository.existsById(account)){
            User user = userRepository.getOne(account);
            if (user.getVerifyCode().equals(verifyCode)){
                user.setPassword(PasswordUtil.encodePassword(password));
                user.setVerifyCode("");
                userRepository.save(user);
                return user;
            }else {
                throw new Exception("验证码错误");
            }
        }
        return null;
    }

    @PostMapping("/send_verify_code")
    @ResponseBody
    public User SendCode(@RequestParam("account")String account) throws MessagingException {
        if (userRepository.existsById(account)){
            User user = userRepository.getOne(account);
            user.setVerifyCode(PasswordUtil.getVerifyCode());
            userRepository.save(user);
            String subject = "来自TeqGin的云的校验码";
            String content = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Title</title>\n" +
                    "    <STYLE>\n" +
                    "        p{\n" +
                    "            text-align: center\n" +
                    "        }\n" +
                    "        #code{\n" +
                    "            padding-top: 10px;\n" +
                    "            color: rgba(0, 0, 0, 0.49);\n" +
                    "        }\n" +
                    "        .code_bg{\n" +
                    "            width: 100%;\n" +
                    "            height: 50px;\n" +
                    "            background-color: #f4f4f4;\n" +
                    "        }\n" +
                    "    </STYLE>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<p >你好，我是<em>TeqGin的云</em>，你的验证码如下，请查收：</p>\n" +
                    "<p >Copy and paste this code to verify:</p>\n" +
                    "<div class=\"code_bg\">\n" +
                    "    <p id=\"code\">" +user.getVerifyCode() + "</p>\n" +
                    "</div>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";
            mailSendService.sendHtmlMail(account, subject, content);
            return user;
        }
        return null;
    }

    @GetMapping("/sign_out")
    public String signOut(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        request.getSession().invalidate();
        return "/user/login";
    }

    @PostMapping("/email_changed")
    @ResponseBody
    public User emailChanged(@RequestParam("account")String account,
                             @RequestParam("code") String code,
                             HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (!userRepository.existsById(account)){
            if (user.getVerifyCode().equals(code)){
                user.setAccount(account);
                user.setVerifyCode("");
                userRepository.save(user);
                return user;
            }else {
                throw new Exception("验证码错误");
            }
        }else {
            throw new Exception("账号已存在");
        }
    }

    @PostMapping("/username_changed")
    @ResponseBody
    public User usernameChanged(@RequestParam("name")String name,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        user.setName(name);
        userRepository.save(user);
       return user;
    }
    @PostMapping("/password_changed")
    @ResponseBody
    public User passwordChanged(@RequestParam("password")String password, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        user.setPassword(PasswordUtil.encodePassword(password));
        userRepository.save(user);
        return user;
    }
}
