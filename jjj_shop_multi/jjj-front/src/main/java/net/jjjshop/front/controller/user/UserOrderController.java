

package net.jjjshop.front.controller.user;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.OrderTypeEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.settings.vo.PointsVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.common.util.poster.ExtractUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.order.OrderPageParam;
import net.jjjshop.front.param.order.OrderPayParam;
import net.jjjshop.front.service.app.AppService;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.service.user.UserOrderService;
import net.jjjshop.front.vo.order.OrderDetailVo;
import net.jjjshop.front.vo.order.OrderListVo;
import net.jjjshop.front.vo.settings.ExpressDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "user", tags = {"用户订单管理"})
@RestController
@RequestMapping("/front/user/order")
public class UserOrderController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private AppService appService;
    @Autowired
    private UserOrderService userOrderService;
    @Autowired
    private ExtractUtils extractUtils;
    @Autowired
    private SettingUtils settingUtils;

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    @OperationLog(name = "lists")
    @ApiOperation(value = "lists", response = String.class)
    public ApiResult<Paging<OrderListVo>> lists(@RequestBody OrderPageParam orderPageParam){
        orderPageParam.setUserId(this.getUser(true).getUserId());
        return ApiResult.ok(orderService.getList(orderPageParam));
    }

    @RequestMapping(value = "/toPay", method = RequestMethod.GET)
    @OperationLog(name = "toPay")
    @ApiOperation(value = "toPay", response = String.class)
    public ApiResult<Map<String, Object>> toPay(String orderId, String paySource){
        User user = this.getUser(true);
        Map<String, Object> result = new HashMap<>();
        result.put("payPrice", orderService.getTotalPayMoney(orderId, user.getUserId()));
        result.put("balance", user.getBalance());
        // 开启的支付类型
        result.put("payTypes", appService.getPayType(paySource));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    @OperationLog(name = "pay")
    @ApiOperation(value = "pay", response = String.class)
    public ApiResult<Map<String, Object>> pay(@RequestBody OrderPayParam orderPayParam){
        User user = this.getUser(true);
        Map<String, Object> result = orderService.orderPay(orderPayParam, user);
        // 订单类型
        result.put("orderType", OrderTypeEnum.MASTER.getValue());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/paySuccessDetail", method = RequestMethod.POST)
    @OperationLog(name = "paySuccessDetail")
    @ApiOperation(value = "paySuccessDetail", response = String.class)
    public ApiResult<Map<String, Object>> paySuccessDetail(String orderId){
        Map<String, Object> result = new HashMap<>();
        User user = this.getUser(true);
        String[] orderIds = orderId.split(",");
        BigDecimal payPrice = BigDecimal.ZERO;
        int pointsBonus = 0;
        for(String id:orderIds){
            OrderDetailVo order = userOrderService.detail(user, Integer.parseInt(id));
            payPrice = payPrice.add(order.getPayPrice());
            pointsBonus += order.getPointsBonus();
        }
        // 支付金额
        result.put("payPrice", payPrice);
        // 赠送积分
        result.put("pointsBonus", pointsBonus);
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @OperationLog(name = "cancel")
    @ApiOperation(value = "cancel", response = String.class)
    public ApiResult<String> cancel(Integer orderId){
        User user = this.getUser(true);
        Order order = orderService.getUserOrderDetail(orderId, user.getUserId());
        if(orderService.cancelOrder(order)){
            return ApiResult.ok(null, "取消成功");
        }else{
            return ApiResult.fail("取消失败");
        }
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<Map<String, Object>> detail(Integer orderId){
        Map<String, Object> result = new HashMap<>();
        result.put("detail", userOrderService.detail(this.getUser(true),orderId));
        JSONObject points = settingUtils.getSetting(SettingEnum.POINTS.getKey(), null);
        PointsVo pointsVo = points.toJavaObject(PointsVo.class);
        result.put("pointsName", pointsVo.getPointsName());
        //todo 店铺客服信息
        result.put("mpService", "");
        //todo 是否开启客服
        result.put("serviceOpen", false);
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/express", method = RequestMethod.GET)
    @OperationLog(name = "express")
    @ApiOperation(value = "express", response = String.class)
    public ApiResult<ExpressDetailVo> express(Integer orderId)  throws Exception  {
        return ApiResult.ok(userOrderService.express(orderId));
    }

    @RequestMapping(value = "/receipt", method = RequestMethod.POST)
    @OperationLog(name = "receipt")
    @ApiOperation(value = "receipt", response = String.class)
    public ApiResult<String> receipt(Integer orderId){
        if(orderService.receipt(orderId)){
            return ApiResult.ok(null, "收货成功");
        }else{
            return ApiResult.fail("收货失败");
        }
    }

    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    @OperationLog(name = "qrcode")
    @ApiOperation(value = "qrcode", response = String.class)
    public ApiResult<String> qrcode(Integer orderId){
        User user = this.getUser(true);
        return ApiResult.ok(extractUtils.genePoster(orderId, user));
    }
}
