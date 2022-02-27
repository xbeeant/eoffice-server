package io.github.xbeeant.eoffice.rest;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.rest.vo.QiankunSlave;
import io.github.xbeeant.eoffice.model.Slave;
import io.github.xbeeant.eoffice.service.ISlaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaobiao
 * @version 2022/2/21
 */
@RestController
@RequestMapping("api/slaves")
public class SlaveRestController {

    @Autowired
    private ISlaveService slaveService;

    @GetMapping("")
    public List<QiankunSlave> slaves() {
        ApiResponse<List<Slave>> listApiResponse = slaveService.selectAllByExample(new Slave());
        List<QiankunSlave> slaves = new ArrayList<>();
        if (listApiResponse.getSuccess()) {
            for (Slave slave : listApiResponse.getData()) {
                slaves.add(new QiankunSlave(slave));
            }
        }
        return slaves;
    }
}
