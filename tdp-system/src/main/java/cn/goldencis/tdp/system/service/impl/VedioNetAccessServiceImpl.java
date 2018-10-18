package cn.goldencis.tdp.system.service.impl;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.common.utils.XmlUtil;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.goldencis.tdp.system.dao.VedioNetAccessDOMapper;
import cn.goldencis.tdp.system.entity.VedioNetAccessDO;
import cn.goldencis.tdp.system.entity.VedioNetAccessDOCriteria;
import cn.goldencis.tdp.system.service.IVedioNetAccessService;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * Created by limingchao on 2018/1/9.
 */
@Service
public class VedioNetAccessServiceImpl extends AbstractBaseServiceImpl<VedioNetAccessDO, VedioNetAccessDOCriteria> implements IVedioNetAccessService, ServletContextAware {

    @Autowired
    private VedioNetAccessDOMapper mapper;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    protected BaseDao<VedioNetAccessDO, VedioNetAccessDOCriteria> getDao() {
        return mapper;
    }

    /**
     * 查询视频业务网络访问管控的总记录数
     * @return
     */
    @Override
    public int countVedioNetAccess() {
        VedioNetAccessDOCriteria example = new VedioNetAccessDOCriteria();
        int count = (int)mapper.countByExample(example);
        return count;
    }

    /**
     * 从全局配置的XML文件中，查询视频业务网络访问管控列表
     * @return
     */
    @Override
    public List<String> getVedioNetAccessListFromCfgXml() {

        Document document = XmlUtil.getDocument(servletContext.getRealPath(PathConfig.GOLBALCFG_PATH));
        List<String> ipList = XmlUtil.getAttributeValueListByTree("/cfg/ipmgr/item/@ip", document);

        return ipList;
    }

    /**
     * 添加视频业务网络访问
     * @param vedioNetAccess
     */
    @Override
    public void addVedioNetAccessIntoXml(VedioNetAccessDO vedioNetAccess) {
        String xmlPath = servletContext.getRealPath(PathConfig.GOLBALCFG_PATH);
        Document document = XmlUtil.getDocument(xmlPath);
        List<Element> ipmgrList = XmlUtil.getElementListByTree("/cfg/ipmgr", document);
        if (ipmgrList != null && ipmgrList.size() == 1) {
            Element itemEle = ipmgrList.get(0).addElement("item");
            itemEle.addAttribute("ip", vedioNetAccess.getIp());
        }
        XmlUtil.writeDocumentIntoFile(document, xmlPath);
    }

    /**
     * 删除视频业务网络访问
     * @param vedioNetAccess
     */
    @Override
    @Transactional
    public boolean deleteVedioNetAccessFromCfgXml(VedioNetAccessDO vedioNetAccess) {
        String xmlPath = servletContext.getRealPath(PathConfig.GOLBALCFG_PATH);
        Document document = XmlUtil.getDocument(xmlPath);

        List<Element> ipmgrList = XmlUtil.getElementListByTree("/cfg/ipmgr", document);
        if (ipmgrList != null && ipmgrList.size() == 1) {
            Element ipmgrElement = ipmgrList.get(0);
            List<Element> elementList = ipmgrElement.elements();

            String ip;
            for (Element element : elementList) {
                ip = element.attributeValue("ip");
                if (ip.equals(vedioNetAccess.getIp())) {
                    ipmgrElement.remove(element);
                    XmlUtil.writeDocumentIntoFile(document, xmlPath);
                    return true;
                }
            }
        }

        return false;
    }
}
