package cn.goldencis.tdp.common.filter;

import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.common.wrapper.XssHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author mll
 * 2017年6月30日20:33:26
 */
public class XssFilter implements Filter {

    FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        chain.doFilter(new XssHttpServletRequestWrapper(request), servletResponse);
    }
}
