package cn.macswelle.campusecurity.gateway.controller;

import cn.macswelle.campusecurity.common.dto.LocationDto2;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.feignapi.deviceManager.ManagerApi;
import cn.macswelle.campusecurity.feignapi.userservice.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

@Controller
public class ManageController {
    @Autowired
    private UserApi userApi;
    @Autowired
    private ServletContext context;
    @Autowired
    private ManagerApi managerApi;
    @Autowired
    private HttpSession session;

    @RequestMapping(value = "/changeDescription", method = RequestMethod.POST)
    public ModelAndView changeDescription(String d) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("devicemanage");
        LoginDto2 loginDto2 = userApi.changeDescription(d);
        modelAndView.addObject("user", loginDto2);
        session.setAttribute("user", loginDto2);
//        modelAndView.addObject("locations", servletContext.getAttribute("locations"));
        return modelAndView;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
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

    @RequestMapping(value = "/changeLocation", method = RequestMethod.POST)
    public ModelAndView changeLocation(String id, String name, String des) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("devicemanage");
        LocationDto2 locationDto2 = new LocationDto2();
        locationDto2.setId(id);
        locationDto2.setName(name);
        locationDto2.setDescription(des);
        managerApi.refractorOrAddLocation(locationDto2);
        context.setAttribute("locations", managerApi.getLocations());
        return modelAndView;
    }

    @RequestMapping(value = "/refreshLocation", method = RequestMethod.POST)
    public ModelAndView refreshLocation() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("devicemanage");
        context.setAttribute("locations", managerApi.getLocations());
        return modelAndView;
    }

    @RequestMapping(value = "/getLocation", method = RequestMethod.POST)
    public ModelAndView getLocation(String id, String name, String description) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("location");
        LocationDto2 locationDto2 = new LocationDto2();
        locationDto2.setId(id);
        locationDto2.setName(name);
        locationDto2.setDescription(description);
        modelAndView.addObject("location", locationDto2);
        return modelAndView;
    }
}
