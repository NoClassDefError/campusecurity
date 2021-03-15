package cn.macswelle.campusecurity.gateway.service;

import cn.macswelle.campusecurity.common.dto.responseDto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {


    @Override
    public String generateSalt(String username) {
        return null;
    }

    @Override
    public String generateToken(UserDto user) {
        return null;
    }

    @Override
    public boolean checkTokenValid(String username, String token) {

        return false;
    }

    @Override
    public boolean cancelToken(String username) {
        return false;
    }
}
