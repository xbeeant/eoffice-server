package io.github.xbeeant.eoffice.rest;

import com.alibaba.fastjson.annotation.JSONField;
import io.github.xbeeant.antdesign.TableResponse;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.ErrorCodeConstant;
import io.github.xbeeant.eoffice.model.Share;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.rest.vo.ShareResourceVo;
import io.github.xbeeant.eoffice.service.IShareService;
import io.github.xbeeant.eoffice.util.AntDesignUtil;
import io.github.xbeeant.spring.mybatis.antdesign.PageRequest;
import io.github.xbeeant.spring.mybatis.antdesign.PageResponse;
import io.github.xbeeant.spring.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author xiaobiao
 * @date 2022/1/12
 */
@RequestMapping("api/share")
@RestController
public class ShareRestController {

    @Autowired
    private IShareService shareService;

    @GetMapping("")
    public ApiResponse<TableResponse<ShareResourceVo>> list(Authentication authentication, PageRequest pageRequest) {
        SecurityUser<User> userSecurityUser = (SecurityUser<User>) authentication.getPrincipal();

        ApiResponse<TableResponse<ShareResourceVo>> apiResponse = new ApiResponse<>();

        String sorter = AntDesignUtil.translateOrder(pageRequest.getSorter());
        pageRequest.setSorter(sorter);

        ApiResponse<PageResponse<ShareResourceVo>> list = shareService.list(userSecurityUser.getUserId(), pageRequest.getPageBounds());

        if (!list.getSuccess()) {
            apiResponse.setResult(ErrorCodeConstant.NO_MATCH, ErrorCodeConstant.NO_MATCH_MSG);
            return apiResponse;
        }

        TableResponse<ShareResourceVo> pageResponse = new TableResponse<>();
        pageResponse.setList(list.getData());
        pageResponse.setPagination(list.getData().getPagination());
        apiResponse.setData(pageResponse);

        return apiResponse;
    }

    @PostMapping("")
    public ApiResponse<Share> share(
            @RequestParam(value = "users") List<Long> users,
            @RequestParam(value = "perm") List<String> perm,
            Long rid,
            Date endtime) {
        return shareService.share(users, perm, rid, endtime);
    }
}
