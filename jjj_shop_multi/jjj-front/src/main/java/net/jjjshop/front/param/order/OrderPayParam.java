

package net.jjjshop.front.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 微信小程序登录参数
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "订单参数对象", description = "订单参数对象")
public class OrderPayParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("orderId")
    private String orderId;

    @ApiModelProperty("支付来源")
    private String paySource;

    @ApiModelProperty("支付方式")
    private Integer payType;

    @ApiModelProperty("是否使用余额")
    private Boolean useBalance;
}
