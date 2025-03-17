package net.jjjshop.front.controller.product;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.util.ProductUtils;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.common.util.poster.ProductPosterUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.product.ProductParam;
import net.jjjshop.front.service.product.ProductCommentService;
import net.jjjshop.front.service.product.ProductService;
import net.jjjshop.front.service.user.UserCartService;
import net.jjjshop.front.service.user.UserFavoriteService;
import net.jjjshop.front.vo.product.ProductListVo;
import net.jjjshop.front.vo.product.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"商品管理"})
@RestController
@RequestMapping("/front/product/product")
public class ProductController extends BaseController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductUtils productUtils;
    @Autowired
    private ProductPosterUtils productPosterUtils;
    @Autowired
    private UserFavoriteService userFavoriteService;
    @Autowired
    private ProductCommentService productCommentService;
    @Autowired
    private UserCartService userCartService;
    @Autowired
    private SettingUtils settingUtils;

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    @OperationLog(name = "lists")
    @ApiOperation(value = "lists", response = String.class)
    public ApiResult<Paging<ProductListVo>> recommendProduct(@Validated @RequestBody ProductParam productParam) throws Exception{
        productParam.setType("sell");
        return ApiResult.ok(productService.getList(productParam, this.getUser(false)));
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<Map<String, Object>> detail(Integer productId, String url) throws Exception{
        User user = this.getUser(false);
        Map<String, Object> result = new HashMap<>();
        Product product = productService.getById(productId);
        ProductVo detail = productService.getDetail(product, user);
        result.put("detail", detail);
        // 商品sku数据
        result.put("specData", productUtils.getSpecData(product, detail.getSkuList()));
        // 是否收藏
        if(user == null){
            result.put("isFav", false);
        }else {
            result.put("isFav", userFavoriteService.isFav(productId, user.getUserId()));
        }
        // 评论
        result.put("commentDataCount", productCommentService.getCommentCount(productId));
        result.put("commentData", productCommentService.getListForDetail(productId));
        // 购物车数量
        result.put("cartTotalNum", userCartService.getTotalCartNum(user));
        JSONObject setting = settingUtils.getSetting(SettingEnum.STORE.getKey(), null);
        StoreVo storeVo = setting.toJavaObject(StoreVo.class);
        result.put("storeOpen", storeVo.getStoreOpen());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/recommendProduct", method = RequestMethod.POST)
    @OperationLog(name = "recommendProduct")
    @ApiOperation(value = "recommendProduct", response = String.class)
    public ApiResult<Map<String, Object>> recommendProduct(Integer location) throws Exception{
        return ApiResult.ok(productService.getRecommend(location, this.getUser(false)));
    }

    @RequestMapping(value = "/poster", method = RequestMethod.GET)
    @OperationLog(name = "poster")
    @ApiOperation(value = "生成海报", response = String.class)
    public ApiResult<String> poster(Integer productId, String source) throws Exception{
        return ApiResult.ok(productPosterUtils.genePoster(productId, source, this.getUser(false)));
    }
}
