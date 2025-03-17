package net.jjjshop.shop.param.supplier;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SupplierApplyPageParam对象", description = "商户申请分页参数对象")
public class SupplierApplyPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("搜索栏内容")
    private String storeName;

    @ApiModelProperty("搜索栏审核状态")
    private Integer status;

}
