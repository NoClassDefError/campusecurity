package cn.macswelle.campusecurity.nvrlistener.controller;

import cn.macswelle.campusecurity.nvrlistener.DeviceInfo;
import org.bytedeco.javacpp.videoInputLib;
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
    int a = videoInputLib.videoInput.listDevices();
    modelAndView.addObject("numr", a);
    modelAndView.addObject("num", Math.min(a, 4));
    modelAndView.addObject("info", info);
    return modelAndView;
  }
}
