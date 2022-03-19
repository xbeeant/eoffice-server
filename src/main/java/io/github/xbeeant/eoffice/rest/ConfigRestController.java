package io.github.xbeeant.eoffice.rest;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.Config;
import io.github.xbeeant.eoffice.service.IConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobiao
 * @version 2021/11/22
 */
@Api(tags = "配置模块")
@RestController
@RequestMapping("api/config")
public class ConfigRestController {
    @Autowired
    private IConfigService configService;

    /**
     * 列表
     *
     * @param module 模块
     * @return {@link ApiResponse}
     * @see ApiResponse
     * @see List
     */
    @GetMapping("{module}")
    @ApiOperation(value = "获取配置")
    public ApiResponse<Map<String, String>> list(@PathVariable String module) {
        ApiResponse<Map<String, String>> result = new ApiResponse<>();
        Config example = new Config();
        example.setModule(module);
        ApiResponse<List<Config>> listApiResponse = configService.selectAllByExample(example);
        if (listApiResponse.getSuccess()) {
            Map<String, String> maps = new HashMap<>(listApiResponse.getData().size());
            for (Config config : listApiResponse.getData()) {
                maps.put(config.getCkey(), config.getCvalue());
            }
            result.setData(maps);
        }
        return result;
    }
}
