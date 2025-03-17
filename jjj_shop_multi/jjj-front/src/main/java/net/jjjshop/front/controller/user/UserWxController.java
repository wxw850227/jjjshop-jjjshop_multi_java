

package net.jjjshop.front.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.shiro.util.JwtTokenUtil;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.AppWxParam;
import net.jjjshop.front.service.user.SmsService;
import net.jjjshop.front.service.user.UserService;
import net.jjjshop.front.vo.user.LoginUserTokenVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "user", tags = {"用户微信"})
@RestController
@RequestMapping("/front/user/userWx")
public class UserWxController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @OperationLog(name = "login")
    @ApiOperation(value = "login", response = String.class)
    public ApiResult<LoginUserTokenVo> login(@RequestBody AppWxParam appWxParam){
        log.debug("login...");
        LoginUserTokenVo user = userService.loginWx(appWxParam);
        // 设置token响应头
        response.setHeader(JwtTokenUtil.getTokenName(""), user.getToken());
        return ApiResult.ok(user, "登录成功");
    }

    @RequestMapping(value = "/getSession", method = RequestMethod.POST)
    @OperationLog(name = "getSession")
    @ApiOperation(value = "getSession", response = String.class)
    public ApiResult<String> getSessionKey(String code){
        String sessionKey = userService.getSessionKey(code);
        return ApiResult.ok(sessionKey, "");
    }

    //发送验证码
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    @OperationLog(name = "sendCode")
    @ApiOperation(value = "sendCode", response = String.class)
    public ApiResult<String> sendCode(String mobile, String type) {
        if (smsService.send(mobile, type)) {
            return ApiResult.ok(null, "发送成功");
        } else {
            return ApiResult.fail("发送失败");
        }
    }


    @RequestMapping(value = "/bindMobile", method = RequestMethod.POST)
    @OperationLog(name = "bindMobile")
    @ApiOperation(value = "bindMobile", response = String.class)
    public ApiResult<String> bindMobile(String sessionKey, String encryptedData, String iv){
        User user = this.getUser(true);
        String mobile = userService.bindMobileByWx(user.getUserId(), sessionKey, encryptedData, iv);
        if(!"".equals(mobile)){
            return ApiResult.ok(mobile, "");
        }
        return ApiResult.fail("绑定失败，请重试");
    }
}
