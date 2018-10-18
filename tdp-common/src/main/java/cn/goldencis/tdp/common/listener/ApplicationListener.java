package cn.goldencis.tdp.common.listener;

import cn.goldencis.tdp.common.utils.ContextUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author mll
 * 2017年5月16日上午9:24:12
 */
public class ApplicationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        try {
            initContextUtil(context);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    private void initContextUtil(ServletContext context) throws Exception {
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        ContextUtil.setContext(ctx);
    }

}
