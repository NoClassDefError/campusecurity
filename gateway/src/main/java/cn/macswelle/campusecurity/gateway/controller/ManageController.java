package cn.macswelle.campusecurity.gateway.controller;

import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.feignapi.userservice.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

@Controller
public class ManageController {
    @Autowired
    private UserApi userApi;

    @Autowired
    private HttpSession session;

    @RequestMapping("/changeDescription")
    public ModelAndView changeDescription(String d) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("devicemanage");
        LoginDto2 loginDto2 = userApi.changeDescription(d);
        modelAndView.addObject("user", loginDto2);
        session.setAttribute("user", loginDto2);
//        modelAndView.addObject("locations", servletContext.getAttribute("locations"));
        return modelAndView;
    }

    @RequestMapping("/changePassword")
    public ModelAndView changePassword(String origin, String newP) {
        ModelAndView modelAndView = new ModelAndView();
        HttpResult result = userApi.changePassword(origin, newP);
        if (result.result.get("status").equals("success")) {
            modelAndView.setViewName("index");
            session.removeAttribute("user");
            modelAndView.addObject("status", "密码修改成功，请重新登录");
        } else {
            modelAndView.setViewName("devicemanage");
            modelAndView.addObject("user", session.getAttribute("user"));
            modelAndView.addObject("status", "密码修改失败");
//            modelAndView.addObject("locations", servletContext.getAttribute("locations"));
        }
        return modelAndView;
    }

}
