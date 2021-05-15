package cn.macswelle.campusecurity.sdk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public abstract class IndexController {
  @RequestMapping("/")
  public abstract ModelAndView index();
}
