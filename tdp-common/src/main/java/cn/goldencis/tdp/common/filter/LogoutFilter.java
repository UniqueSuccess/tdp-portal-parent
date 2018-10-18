package cn.goldencis.tdp.common.filter;

import cn.goldencis.tdp.common.utils.StringUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by limingchao on 2018/1/26.
 */
public class LogoutFilter implements Filter {

    FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String uri = httpServletRequest.getRequestURI();
        if (!StringUtil.isEmpty(uri)) {
        }

        chain.doFilter(httpServletRequest, response);
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }
}
