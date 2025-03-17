package net.jjjshop.supplier.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "OrderParam对象", description = "订单参数")
public class OrderParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单Id")
    private Integer orderId;

    @ApiModelProperty("物流公司Id")
    private Integer expressId;

    @ApiModelProperty("物流编号")
    private String expressNo;

    @ApiModelProperty("是否取消")
    private Boolean isCancel;
}
