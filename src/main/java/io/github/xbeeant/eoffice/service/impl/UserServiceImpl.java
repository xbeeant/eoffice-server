package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.UserMapper;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.service.IUserService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import io.github.xbeeant.spring.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * eoffice_user
 */
@Service
public class UserServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<User, Long> implements IUserService {
    /**
     * user mapper
     *
     */
    @Autowired
    private UserMapper userMapper;

    /**
     * password encoder
     *
     */
    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public IMybatisPageHelperDao<User, Long> getRepositoryDao() {
        return this.userMapper;
    }

    @Override
    public void setDefaults(User record) {
        if (record.getUid() == null) {
            record.setUid(IdWorker.getId());
        }
    }

    @Override
    public SecurityUser<User> loadUserByUsername(String username) {
        User example = new User();
        example.setUsername(username);
        example.setStatus(true);
        ApiResponse<User> userMsg = selectOneByExample(example);
        if (Boolean.FALSE.equals(userMsg.getSuccess())) {
            return null;
        }
        User user = userMsg.getData();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        return new SecurityUser<>(String.valueOf(user.getUid()), user.getNickname(), user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}