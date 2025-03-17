

package net.jjjshop.framework.core.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.core.bean.RequestDetail;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import net.jjjshop.framework.shiro.vo.LoginShopUserRedisVo;
import net.jjjshop.framework.shiro.vo.LoginSupplierUserRedisVo;
import net.jjjshop.framework.util.IpUtil;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.framework.util.SupplierLoginUtil;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * 请求详情信息Filter
 **/
@Slf4j
public class RequestDetailFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("RequestDetailFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 设置请求详情信息
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // 请求IP
        String ip = IpUtil.getRequestIp(httpServletRequest);
        // 请求路径
        String path = httpServletRequest.getRequestURI();
        // 设置appId
        Long appId = 0L;
        String userName = "";
        if(path.startsWith("/api/shop/")){
            appId = getShopAppId();
            userName = getShopUserName();
        }else if(path.startsWith("/api/front/")){
            appId = getFrontAppId(httpServletRequest);
        }else if(path.startsWith("/api/supplier/")){
            appId = getSupplierAppId();
            userName = getSupplierUserName();
        }
        RequestDetail requestDetail = new RequestDetail()
                .setIp(ip)
                .setPath(path).setAppId(appId).setUserName(userName);
        // shopSupplierId
        if(path.startsWith("/api/supplier/")){
            requestDetail.setShopSupplierId(SupplierLoginUtil.getShopSupplierId());
        }
        // 设置请求详情信息
        RequestDetailThreadLocal.setRequestDetail(requestDetail);
        boolean isBody = false;
        if (request instanceof HttpServletRequest && path.startsWith("/api/front/") && appId == 0L) {
            String contentType = request.getContentType();
            // 如果处理上传文件数据，下面方法执行到chain.doFilter()时会出线异常，所以此处只处理@RequestBody数据
            if (!StringUtils.isEmpty(contentType) && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                // body中获取appid参数
                ServletRequest jsonRequest = new CustomizeHttpServletRequestWrapper((HttpServletRequest) request);
                BufferedReader reader = jsonRequest.getReader();
                String bodyParams = reader.lines().collect(Collectors.joining());
                JSONObject params = JSONObject.parseObject(bodyParams);
                appId = params.getLongValue("appId");
                requestDetail.setAppId(appId);
                RequestDetailThreadLocal.setRequestDetail(requestDetail);
                chain.doFilter(jsonRequest, response);
                isBody = true;
            }
        }
        if(!isBody){
            chain.doFilter(request, response);
        }
        // 释放
        RequestDetailThreadLocal.remove();
    }

    private Long getShopAppId(){
        LoginShopUserRedisVo vo = ShopLoginUtil.getLoginShopUserRedisVo();
        if(vo == null){
            return 0L;
        }
        if(vo.getAppId() == null){
            return 0L;
        }
        return Long.valueOf(vo.getAppId());
    }

    private String getShopUserName(){
        LoginShopUserRedisVo vo = ShopLoginUtil.getLoginShopUserRedisVo();
        if(vo == null){
            return "";
        }
        return vo.getUserName();
    }

    private Long getSupplierAppId(){
        LoginSupplierUserRedisVo vo = SupplierLoginUtil.getLoginSupplierUserRedisVo();
        if(vo == null){
            return 0L;
        }
        if(vo.getAppId() == null){
            return 0L;
        }
        return Long.valueOf(vo.getAppId());
    }

    private String getSupplierUserName(){
        LoginSupplierUserRedisVo vo = SupplierLoginUtil.getLoginSupplierUserRedisVo();
        if(vo == null){
            return "";
        }
        return vo.getUserName();
    }

    private Long getFrontAppId(HttpServletRequest request){
        boolean isBody = false;
        String contentType = request.getContentType();
        Long appId = 0L;
        // 如果处理上传文件数据，下面方法执行到chain.doFilter()时会出线异常，所以此处只处理@RequestBody数据
        if (!StringUtils.isEmpty(contentType)
                && contentType.contains(MediaType.APPLICATION_JSON_VALUE)
                && request.getMethod().equals(RequestMethod.POST.name())) {
            isBody = true;
        }
        if(!isBody){
            appId = Long.parseLong(request.getParameter("appId"));
        }
        return appId;
    }

    @Override
    public void destroy() {
        log.info("RequestDetailFilter destroy");
    }
}
