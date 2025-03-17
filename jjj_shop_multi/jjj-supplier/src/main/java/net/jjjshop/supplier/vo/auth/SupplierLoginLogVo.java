package net.jjjshop.supplier.vo.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商户登陆日志VO")
public class SupplierLoginLogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer loginLogId;

    @ApiModelProperty("供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("登录ip")
    private String ip;

    @ApiModelProperty("登录结果")
    private String result;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
