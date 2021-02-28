package cn.macswelle.campusecurity.gateway.controller;

import cn.macswelle.campusecurity.common.dto.LocationDto2;
import cn.macswelle.campusecurity.common.dto.requestDto.LoginDto;
import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.feignapi.userservice.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserApi userApi;

    @Autowired
    private HttpSession session;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public ModelAndView login(LoginDto loginDto) {
        ModelAndView modelAndView = new ModelAndView();
        LoginDto2 loginDto2 = userApi.login(loginDto);
        if (loginDto2.getStatus().equals("success")) {
            modelAndView.setViewName("devicemanage");
            modelAndView.addObject("user", loginDto2);
//            modelAndView.addObject("locations", servletContext.getAttribute("locations"));
            session.setAttribute("user", loginDto2);
        } else {
            modelAndView.setViewName("index");
            modelAndView.addObject("status", "failed");
        }
        return modelAndView;
    }
}
