package net.jjjshop.front.vo.order;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@Accessors(chain = true)
@ApiModel(value = "订单商品VO")
public class OrderProductVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer orderProductId;

    @ApiModelProperty("商品id")
    private Integer productId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品封面图id")
    private Integer imageId;

    @ApiModelProperty("库存计算方式(10下单减库存 20付款减库存)")
    private Integer deductStockType;

    @ApiModelProperty("规格类型(10单规格 20多规格)")
    private Integer specType;

    @ApiModelProperty("商品sku标识")
    private String specSkuId;

    @ApiModelProperty("商品规格id")
    private Integer productSkuId;

    @ApiModelProperty("商品规格信息")
    private String productAttr;

    @ApiModelProperty("商品编码")
    private String productNo;

    @ApiModelProperty("商品价格(单价)")
    private BigDecimal productPrice;

    @ApiModelProperty("商品划线价")
    private BigDecimal linePrice;

    @ApiModelProperty("商品重量(Kg)")
    private Double productWeight;

    @ApiModelProperty("是否存在会员等级折扣")
    private Integer isUserGrade;

    @ApiModelProperty("会员折扣比例(0-10)")
    private Integer gradeRatio;

    @ApiModelProperty("会员折扣的商品单价")
    private BigDecimal gradeProductPrice;

    @ApiModelProperty("会员折扣的总额差")
    private BigDecimal gradeTotalMoney;

    @ApiModelProperty("积分金额")
    private BigDecimal pointsMoney;

    @ApiModelProperty("积分抵扣数量")
    private Integer pointsNum;

    @ApiModelProperty("赠送的积分数量")
    private Integer pointsBonus;

    @ApiModelProperty("购买数量")
    private Integer totalNum;

    @ApiModelProperty("商品总价(数量×单价)")
    private BigDecimal totalPrice;

    @ApiModelProperty("实际付款价(折扣和优惠后)")
    private BigDecimal totalPayPrice;

    @ApiModelProperty("是否已评价(0否 1是)")
    private Integer isComment;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("虚拟物品内容")
    private String virtualContent;

    //附加字段
    @ApiModelProperty("图片")
    private String productImage;

    @ApiModelProperty("售后")
    private Boolean refund;

}
