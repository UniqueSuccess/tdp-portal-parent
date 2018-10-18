package cn.goldencis.tdp.system.service.impl;

import cn.goldencis.tdp.common.utils.XmlUtil;
import cn.goldencis.tdp.core.utils.LegalTimeUtil;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.goldencis.tdp.system.service.IClientLegalTimeService;
import org.dom4j.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/5/14.
 */
@Service
public class ClientLegalTimeServiceImpl implements IClientLegalTimeService, ServletContextAware {

    private ServletContext servletContext;
    private String enable = null;
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 查询客户端合法登陆时间
     * @return
     */
    @Override
    public Map<String, String> getClientLegalTime() {
        Map<String, String> legalTime = new HashMap<>();
        //查询xml中的标签
        Document document = XmlUtil.getDocument(servletContext.getRealPath(PathConfig.GOLBALCFG_PATH));
        //依次查询legaltime合法时间标签下，周几、开始时间、结束时间、声音报警四个数据，放入Map中。
        List<String> week = XmlUtil.getAttributeValueListByTree("/cfg/legaltime/@week", document);
        if (week != null && week.size() > 0) {
            legalTime.put("week", week.get(0));
        } else {
            legalTime.put("week", "");
        }
        List<String> begin = XmlUtil.getAttributeValueListByTree("/cfg/legaltime/@begin", document);
        if (begin != null && begin.size() > 0) {
            legalTime.put("begin", begin.get(0));
        } else {
            legalTime.put("begin", "");
        }
        List<String> end = XmlUtil.getAttributeValueListByTree("/cfg/legaltime/@end", document);
        if (end != null && end.size() > 0) {
            legalTime.put("end", end.get(0));
        } else {
            legalTime.put("end", "");
        }
        List<String> sound = XmlUtil.getAttributeValueListByTree("/cfg/legaltime/@sound", document);
        if (sound != null && sound.size() > 0) {
            legalTime.put("sound", sound.get(0));
        } else {
            legalTime.put("sound", "");
        }
        return legalTime;
    }

    /**
     * 修改客户端合法登陆时间的接口
     * @param week 每周几为合法登陆时间
     * @param begin 每天的开始时间
     * @param end 每天的结束时间
     * @param sound
     */
    @Override
    public void updateClientLegalTime(String week, String begin, String end, boolean sound) {
        //读取xml文件
        String xmlPath = servletContext.getRealPath(PathConfig.GOLBALCFG_PATH);
        Document document = XmlUtil.getDocument(xmlPath);

        //修改属性值
        XmlUtil.updateAttributeByTree("/cfg/legaltime/@week", week, document);
        XmlUtil.updateAttributeByTree("/cfg/legaltime/@begin", begin, document);
        XmlUtil.updateAttributeByTree("/cfg/legaltime/@end", end, document);
        XmlUtil.updateAttributeByTree("/cfg/legaltime/@sound", String.valueOf(sound), document);

        //将修改后的dom写回xml
        XmlUtil.writeDocumentIntoFile(document, xmlPath);

        //刷新合法登陆时间的配置
        LegalTimeUtil.refreshClientLegalTime();
    }

    @Override
    public boolean refreshEnableState() {
        String cfgPath = servletContext.getRealPath(PathConfig.GOLBALCFG_PATH);
        //查询xml中的标签
        Document document = XmlUtil.getDocument(cfgPath);

        //解析否需要密码的状态
        List<String> enabled = XmlUtil.getAttributeValueListByTree("/cfg/screenmgr/@enable", document);

        if (enabled != null && enabled.size() > 0) {
            enable = enabled.get(0);
            return true;
        }

        return false;
    }

    @Override
    public void updateForbidScreenState(String isNeeded) {
        String cfgPath = servletContext.getRealPath(PathConfig.GOLBALCFG_PATH);
        //查询xml中的标签
        Document document = XmlUtil.getDocument(cfgPath);

        XmlUtil.updateAttributeByTree("/cfg/screenmgr/@enable", isNeeded, document);

        //将修改后的dom写回xml
        XmlUtil.writeDocumentIntoFile(document, cfgPath);

        this.refreshEnableState();
    }

    @Override
    public String getForbidScreen() {
        if (enable == null) {
            this.refreshEnableState();
        }
        return enable;
    }


}
