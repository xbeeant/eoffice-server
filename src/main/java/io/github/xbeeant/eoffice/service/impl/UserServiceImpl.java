package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.ErrorCodeConstant;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.UserMapper;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.service.IUserService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import io.github.xbeeant.spring.security.SecurityUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * eoffice_user
 */
@Service
public class UserServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<User, Long> implements IUserService {
    /**
     * password encoder
     */
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    /**
     * user mapper
     */
    @Autowired
    private UserMapper userMapper;

    @Override
    public IMybatisPageHelperDao<User, Long> getRepositoryDao() {
        return this.userMapper;
    }

    @Override
    public void setDefaults(User user) {
        if (user.getUid() == null) {
            user.setUid(IdWorker.getId());
        }
        // 密码加密存储
        if (!StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
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
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }

    @Override
    public ApiResponse<List<User>> search(String search) {
        ApiResponse<List<User>> result = new ApiResponse<>();
        List<User> users = userMapper.search(search);

        if (CollectionUtils.isEmpty(users)) {
            result.setResult(ErrorCodeConstant.NO_MATCH, ErrorCodeConstant.NO_MATCH_MSG);
            return result;
        }

        result.setData(users);
        return result;
    }
}