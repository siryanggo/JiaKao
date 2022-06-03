package com.mj.jk.common.shiro;

import com.mj.jk.common.cache.Caches;
import com.mj.jk.common.util.JsonVos;
import com.mj.jk.pojo.result.CodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 作用：验证用户的合法性、是否有相关权限
 */
@Slf4j
public class TokenFilter extends AccessControlFilter {
    public static final String HEADER_TOKEN = "Token";

    /**
     * 当请求被TokenFilter拦截时，就会调用这个方法
     * 可以在这个方法中做初步判断
     *
     * 如果返回true：允许访问。可以进入下一个链条调用（比如Filter、拦截器、控制器等）
     * 如果返回false：不允许访问。会进入onAccessDenied方法，不会进入下一个链条调用（比如Filter、拦截器、控制器等）
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        // 放行所有的OPTIONS请求
//        return "OPTIONS".equals(request.getMethod());

        log.debug("TokenFilter - isAccessAllowed - " + request.getRequestURI());
        return false;
    }

    /**
     * 当isAccessAllowed返回false时，就会调用这个方法
     * 在这个方法中进行token的校验
     *
     * 如果返回true：允许访问。可以进入下一个链条调用（比如Filter、拦截器、控制器等）
     * 如果返回false：不允许访问。不会进入下一个链条调用（比如Filter、拦截器、控制器等）
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.debug("TokenFilter - onAccessDenied - " + request.getRequestURI());

        // 取出Token
        String token = request.getHeader(HEADER_TOKEN);

        // 如果没有Token
        if (token == null) {
            return JsonVos.raise(CodeMsg.NO_TOKEN);
        }

        // 如果Token过期了
        if (Caches.getToken(token) == null) {
            return JsonVos.raise(CodeMsg.TOKEN_EXPIRED);
        }

        log.debug("TokenFilter - onAccessDenied - " + token);

        // 鉴权（进入Realm）
        // 这里调用login，并不是“登录”的意思，是为了触发Realm的相应方法去加载用户的角色、权限信息，以便鉴权
        SecurityUtils.getSubject().login(new Token(token));
        return true;
    }
}
