package cn.macswelle.campusecurity.nvrlistener.controller;

import cn.macswelle.campusecurity.nvrlistener.DeviceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
  @Autowired
  private DeviceInfo info;

  @RequestMapping("/")
  public ModelAndView index() {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("index");
    modelAndView.addObject("info", info);
    return modelAndView;
  }
}
