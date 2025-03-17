package net.jjjshop.common.service.supplier.impl;

import net.jjjshop.common.entity.supplier.SupplierService;
import net.jjjshop.common.mapper.supplier.SupplierServiceMapper;
import net.jjjshop.common.service.supplier.SupplierServiceService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 供应商客服表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-18
 */
@Slf4j
@Service
public class SupplierServiceServiceImpl extends BaseServiceImpl<SupplierServiceMapper, SupplierService> implements SupplierServiceService {

    @Autowired
    private SupplierServiceMapper supplierServiceMapper;

}
