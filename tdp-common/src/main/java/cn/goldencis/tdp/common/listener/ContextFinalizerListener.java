package cn.goldencis.tdp.common.listener;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Created by limingchao on 2018/8/8.
 */
@WebListener
public class ContextFinalizerListener implements ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver d = null;
        try {
            while (drivers.hasMoreElements()) {
                d = drivers.nextElement();
                DriverManager.deregisterDriver(d);
                logger.info(" 消除数据库连接驱动 --> : Driver {} deregistered", d);
            }
        } catch (SQLException ex) {
            logger.error("Error: deregistering driver {} exceptionName:{} detail:{}", d, ex.getClass().getName(), ex.getMessage());
        }finally {
            try {
                AbandonedConnectionCleanupThread.shutdown();
            } catch (InterruptedException e) {
                logger.error("Error: SEVERE problem cleaning up: " + e.getMessage());
            }
        }


    }
}
