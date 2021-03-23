package cn.macswelle.campusecurity.userservice.service;

import cn.macswelle.campusecurity.common.dto.requestDto.LoginDto;
import cn.macswelle.campusecurity.common.dto.requestDto.SignUpDto;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.common.dto.responseDto.LogoutDto;
import cn.macswelle.campusecurity.common.dto.responseDto.UserDto;
import cn.macswelle.campusecurity.common.utils.JwtUtil;
import cn.macswelle.campusecurity.database.entities.User;
import cn.macswelle.campusecurity.database.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements LoginService, SignUpService {

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected HttpServletRequest request;

  private final String secretKey = "gehanchen";

  @Override
  public LoginDto2 login(LoginDto dto) {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    //对象转换
    LoginDto2 loginDto2 = new LoginDto2();
    User result = userRepository.findByIdAndPassword(dto.getUserId(), dto.getPassword());
    if (result != null) {
      loginDto2.setStatus("success");
      switch (result.getAuth()) {
        case 0:
          loginDto2.setAuth("超级管理员");
          break;
        case 1:
          loginDto2.setAuth("管理员");
          break;
        case 2:
          loginDto2.setAuth("只读");
          break;
      }
      loginDto2.setDescription(result.getDescription());
      loginDto2.setId(result.getId());
      loginDto2.setName(result.getName());
      //记录登录状态，不能像单体应用那样，微服务架构存在session同步问题，要将session存在redis中
//            logger.info("access-token: " + token);
      String token = null;
      try {
        token = JwtUtil.generateToken(new ObjectMapper().writeValueAsString(loginDto2), secretKey);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      logger.info("登录: " + result.toString() + "token：" + token);
      loginDto2.setToken(token);
    } else {
      loginDto2.setStatus("failed");
      logger.info("登录失败" + dto.toString());
    }
    return loginDto2;
  }

  /**
   * token一次签发，永久有效，无法在后台使其失效，可以命令前端删除，所以后台不需要logout
   */
  @Deprecated
  @Override
  public LogoutDto logout() {
    LogoutDto result = new LogoutDto();
    LoginDto2 user = null;
    try {
      user = new ObjectMapper().readValue(request.getHeader("user"), LoginDto2.class);
    } catch (IOException e) {
      result.setStatus("already has done that");
    }
    if (user != null) {
      result.setId(user.getId());
      result.setName(user.getName());
      result.setStatus("success");
      //不如session, token一次签发，永久有效，无法在后台使其失效，可以命令前端删除
    } else result.setStatus("already has done that");
    return result;
  }

  @Override
  public List<UserDto> getUsers() {
    List<UserDto> result = new ArrayList<>();
    userRepository.findAll().forEach(user -> result.add(convertUser(user)));
    return result;
  }

  private UserDto convertUser(User user) {
    UserDto userDto = new UserDto();
    switch (user.getAuth()) {
      case 0:
        userDto.setAuth("超级管理员");
        break;
      case 1:
        userDto.setAuth("管理员");
        break;
      case 2:
        userDto.setAuth("只读");
        break;
    }
    userDto.setDescription(user.getDescription());
    userDto.setId(user.getId());
    userDto.setName(user.getName());
    return userDto;
  }

  @Override
  public UserDto getUser(String token) {
    String uerId = JwtUtil.getUserInfo(token);
    User user = userRepository.findById(uerId).orElse(null);
    return convertUser(user);
  }

  @Override
  public HttpResult signUp(SignUpDto signUpDto) {
    if (signUpDto.getId() == null) {
      User user = new User();
      user.setAuth(signUpDto.getAuth());
      user.setDescription(signUpDto.getDescription());
      user.setName(signUpDto.getName());
      String id = userRepository.save(user).getId();
      return new HttpResult("status:add", "id:" + id);
    } else {
      userRepository.updateUser(signUpDto.getId(), signUpDto.getName(),
        signUpDto.getDescription(), signUpDto.getAuth());
      return new HttpResult("status:modify", "id:" + signUpDto.getId());
    }
  }

  @Override
  public HttpResult remove(String id) {
    return null;
  }

  @Override
  public HttpResult changePassword(String original, String newPassword) {
    LoginDto2 user = getUserFromToken();
    if (user == null)
      return new HttpResult("status:error");
    User user1 = userRepository.findByIdAndPassword(user.getId(), original);
    if (user1 == null) return new HttpResult("status:error");
    else {
      userRepository.changePassword(user.getId(), newPassword);
      return new HttpResult("status:success");
    }
  }

  @Override
  public HttpResult changeDescription(String d) {
//    System.out.println(d);
    LoginDto2 user = getUserFromToken();
    if (user == null)
      return new HttpResult("status:error");
    userRepository.changeDescription(user.getId(), d);
    return new HttpResult("status:success");
  }

  private LoginDto2 getUserFromToken() {
    LoginDto2 user;
    try {
      user = new ObjectMapper().readValue(request.getHeader("user"), LoginDto2.class);
    } catch (IOException e) {
      return null;
    }
    return user;
  }
}
