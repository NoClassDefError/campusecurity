package cn.macswelle.campusecurity.feignapi.userservice;

import cn.macswelle.campusecurity.common.dto.requestDto.LoginDto;
import cn.macswelle.campusecurity.common.dto.requestDto.SignUpDto;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.common.dto.responseDto.LogoutDto;
import cn.macswelle.campusecurity.common.dto.responseDto.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@FeignClient(value = "userservice", path = "/user")
public interface UserApi {

    @RequestMapping(name = "/getAuth",method = RequestMethod.POST)
    Integer getAuth(String userId);

    @RequestMapping(name = "/login", method = RequestMethod.POST)
    LoginDto2 login(LoginDto loginDto, HttpServletResponse response);

    @RequestMapping(name = "/logout", method = RequestMethod.POST)
    LogoutDto logoutDto(HttpServletResponse response);

    @RequestMapping(name = "/changePassword", method = RequestMethod.POST)
    HttpResult changePassword(String original, String newPassword);

    @RequestMapping(name = "/admin/signUp", method = RequestMethod.POST)
    HttpResult signUp(SignUpDto signUpDto);

    @RequestMapping(name = "/admin/remove", method = RequestMethod.POST)
    HttpResult remove(String id);

    @RequestMapping(name = "/admin/getUsers", method = RequestMethod.POST)
    List<UserDto> getUsers();

}
