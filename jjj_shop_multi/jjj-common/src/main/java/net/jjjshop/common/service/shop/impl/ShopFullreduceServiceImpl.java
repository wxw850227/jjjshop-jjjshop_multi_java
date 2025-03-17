package net.jjjshop.common.service.shop.impl;

import net.jjjshop.common.entity.shop.ShopFullreduce;
import net.jjjshop.common.mapper.shop.ShopFullreduceMapper;
import net.jjjshop.common.service.shop.ShopFullreduceService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 满减设置表 服务实现类
 *
 * @author jjjshop
 * @since 2022-08-22
 */
@Slf4j
@Service
public class ShopFullreduceServiceImpl extends BaseServiceImpl<ShopFullreduceMapper, ShopFullreduce> implements ShopFullreduceService {

    @Autowired
    private ShopFullreduceMapper shopFullreduceMapper;

}
