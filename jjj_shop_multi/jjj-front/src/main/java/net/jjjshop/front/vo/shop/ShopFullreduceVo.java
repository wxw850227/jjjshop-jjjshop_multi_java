package net.jjjshop.front.vo.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.shop.ShopFullreduce;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("满减VO")
public class ShopFullreduceVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer fullreduceId;

    @ApiModelProperty("活动名称")
    private String activeName;

    @ApiModelProperty("减金额")
    private BigDecimal reducedPrice;
}
