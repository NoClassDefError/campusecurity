package cn.macswelle.campusecurity.feignapi.userservice;

import cn.macswelle.campusecurity.common.dto.requestDto.LoginDto;
import cn.macswelle.campusecurity.common.dto.requestDto.SignUpDto;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.common.dto.responseDto.LogoutDto;
import cn.macswelle.campusecurity.common.dto.responseDto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    HttpResult changePassword(@RequestParam("original") String original,
                              @RequestParam("newPassword") String newPassword);

    @RequestMapping(value = "/admin/signUp", method = RequestMethod.POST)
    HttpResult signUp(@RequestParam("signUpDto") SignUpDto signUpDto);

    @RequestMapping(value = "/admin/remove", method = RequestMethod.POST)
    HttpResult remove(@RequestParam("id") String id);

    @RequestMapping(value = "/admin/getUsers", method = RequestMethod.POST)
    List<UserDto> getUsers();

}
