package net.jjjshop.supplier.controller.store;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.SupplierUser;
import net.jjjshop.common.service.supplier.SupplierUserService;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.framework.util.SupplierLoginUtil;
import net.jjjshop.supplier.param.store.StoreOrderPageParam;
import net.jjjshop.supplier.service.store.StoreOrderService;
import net.jjjshop.supplier.service.store.StoreService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/supplier/store/order")
public class StoreOrderController {
    @Autowired
    private StoreOrderService storeOrderService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private SupplierUserService supplierUserService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/store/order/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@Validated @RequestBody StoreOrderPageParam storeOrderPageParam) throws Exception {
        Map<String, Object> map = new HashMap<>();
        SupplierUser user = supplierUserService.getById(SupplierLoginUtil.getUserId());
        storeOrderPageParam.setShopSupplierId(user.getShopSupplierId());
        map.put("storeList", storeService.getAll(user.getShopSupplierId()));
        map.put("list", storeOrderService.getList(storeOrderPageParam));
        return ApiResult.ok(map);
    }
}
