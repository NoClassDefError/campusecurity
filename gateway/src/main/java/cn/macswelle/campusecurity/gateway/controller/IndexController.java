package cn.macswelle.campusecurity.gateway.controller;

import cn.macswelle.campusecurity.common.dto.requestDto.LoginDto;
import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.feignapi.deviceManager.ManagerApi;
import cn.macswelle.campusecurity.feignapi.userservice.UserApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {
    @Autowired
    private UserApi userApi;

    @Autowired
    private ManagerApi managerApi;

    @Autowired
    private HttpSession session;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam("userId") String username,
                              @RequestParam("password") String password) {
        ModelAndView modelAndView = new ModelAndView();
        LoginDto loginDto = new LoginDto();
        loginDto.setUserId(username);
        loginDto.setPassword(password);
        LoginDto2 loginDto2 = userApi.login(loginDto);
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("登录：" + loginDto2);
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
