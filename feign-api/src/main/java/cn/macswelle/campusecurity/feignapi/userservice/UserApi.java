package cn.macswelle.campusecurity.feignapi.userservice;

import cn.macswelle.campusecurity.common.dto.requestDto.LoginDto;
import cn.macswelle.campusecurity.common.dto.requestDto.SignUpDto;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.common.dto.responseDto.LogoutDto;
import cn.macswelle.campusecurity.common.dto.responseDto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "userservice", path = "/user")
public interface UserApi {

  @RequestMapping(value = "/getUser", method = RequestMethod.POST)
  UserDto getUser(String token);

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  LoginDto2 login(LoginDto loginDto);

  @RequestMapping(value = "/logout", method = RequestMethod.POST)
  LogoutDto logoutDto();

  @RequestMapping(value = "/changeDescription", method = RequestMethod.POST)
  HttpResult changeDescription(String d);

  //feign 可以不加 @ResponseBody注解 但@RequestParam 注解十分重要
  @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
  @ResponseBody
  HttpResult changePassword(@RequestParam("original") String original,
                            @RequestParam("newPassword") String newPassword);

  @RequestMapping(value = "/signUp", method = RequestMethod.POST)
  @ResponseBody
  HttpResult signUp(@RequestParam("signUpDto") SignUpDto signUpDto);

  @RequestMapping(value = "/send", method = RequestMethod.POST)
  @ResponseBody
  HttpResult send(@RequestParam("email") String email);

  @RequestMapping(value = "/admin/changeUser", method = RequestMethod.POST)
  @ResponseBody
  HttpResult changeUser(@RequestBody UserDto userDto);

  @RequestMapping(value = "/admin/remove", method = RequestMethod.POST)
  @ResponseBody
  HttpResult remove(@RequestParam("id") String id);

  @RequestMapping(value = "/admin/getUsers", method = RequestMethod.POST)
  @ResponseBody
  List<UserDto> getUsers();

}
