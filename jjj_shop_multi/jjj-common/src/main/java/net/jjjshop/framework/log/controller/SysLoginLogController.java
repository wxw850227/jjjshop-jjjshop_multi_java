

package net.jjjshop.framework.log.controller;

import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.common.controller.BaseController;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.Module;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.log.entity.SysLoginLog;
import net.jjjshop.framework.log.enums.OperationLogType;
import net.jjjshop.framework.log.param.SysLoginLogPageParam;
import net.jjjshop.framework.log.service.SysLoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统登录日志 控制器
 */
@Slf4j
@RestController
@RequestMapping("/sysLoginLog")
@Module("log")
@Api(value = "系统登录日志API", tags = {"系统登录日志"})
public class SysLoginLogController extends BaseController {

    @Autowired
    private SysLoginLogService sysLoginLogService;

    /**
     * 系统登录日志分页列表
     */
    @PostMapping("/getPageList")
    @RequiresPermissions("sys:login:log:page")
    @OperationLog(name = "系统登录日志分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "系统登录日志分页列表", response = SysLoginLog.class)
    public ApiResult<Paging<SysLoginLog>> getSysLoginLogPageList(@Validated @RequestBody SysLoginLogPageParam sysLoginLogPageParam) throws Exception {
        Paging<SysLoginLog> paging = sysLoginLogService.getSysLoginLogPageList(sysLoginLogPageParam);
        return ApiResult.ok(paging);
    }

}

