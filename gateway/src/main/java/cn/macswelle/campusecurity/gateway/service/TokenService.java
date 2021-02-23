package cn.macswelle.campusecurity.gateway.service;

import cn.macswelle.campusecurity.common.entities.User;

public interface TokenService {
    /**
     * 生成用户的随机 salt，5分钟失效
     */
    String generateSalt(String username);

    /**
     * 生成用户的Token令牌，5小时失效
     */
    String generateToken(User user);

    /**
     * 校验用户的Token令牌是否有效
     */
    boolean checkTokenValid(String username, String token);

    /**
     * 注销用户的Token令牌
     */
    boolean cancelToken(String username);
}
