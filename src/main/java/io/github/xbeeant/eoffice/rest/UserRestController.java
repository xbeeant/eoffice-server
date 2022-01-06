package io.github.xbeeant.eoffice.rest;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.ErrorCodeConstant;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.service.IUserService;
import io.github.xbeeant.http.Requests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiaobiao
 * @version 2022/1/6
 */
@RestController
@RequestMapping("api/user")
public class UserRestController {

    @Autowired
    private IUserService userService;

    @PostMapping("register")
    public ApiResponse<String> register(User user, HttpServletRequest request) {
        ApiResponse<String> result = new ApiResponse<>();
        // 用户名校验
        User example = new User();
        example.setUsername(user.getUsername());
        ApiResponse<User> exist = userService.selectOneByExample(example);
        if (exist.getSuccess()) {
            result.setResult(ErrorCodeConstant.CONFLICT, "用户名已存在");
            return result;
        }
        // 邮箱校验
        example = new User();
        example.setEmail(user.getEmail());
        exist = userService.selectOneByExample(example);
        if (exist.getSuccess()) {
            result.setResult(ErrorCodeConstant.CONFLICT, "邮箱已注册");
            return result;
        }
        // 手机号
        example = new User();
        example.setPhone(user.getPhone());
        exist = userService.selectOneByExample(example);
        if (exist.getSuccess()) {
            result.setResult(ErrorCodeConstant.CONFLICT, "手机号已注册");
            return result;
        }

        user.emptySensitiveInfo();
        user.setStatus(true);
        user.setRegip(Requests.getIp(request));
        userService.insertSelective(user);

        return result;
    }
}
