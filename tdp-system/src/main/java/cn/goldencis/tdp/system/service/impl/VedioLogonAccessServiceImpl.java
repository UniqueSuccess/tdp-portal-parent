package cn.goldencis.tdp.system.service.impl;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.common.utils.XmlUtil;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.goldencis.tdp.system.dao.VedioLogonAccessDOMapper;
import cn.goldencis.tdp.system.entity.VedioLogonAccessDO;
import cn.goldencis.tdp.system.entity.VedioLogonAccessDOCriteria;
import cn.goldencis.tdp.system.service.IVedioLogonAccessService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by limingchao on 2018/1/9.
 */
@Service
public class VedioLogonAccessServiceImpl extends AbstractBaseServiceImpl<VedioLogonAccessDO, VedioLogonAccessDOCriteria> implements IVedioLogonAccessService, ServletContextAware {

    @Autowired
    private VedioLogonAccessDOMapper mapper;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    protected BaseDao<VedioLogonAccessDO, VedioLogonAccessDOCriteria> getDao() {
        return mapper;
    }

    /**
     *查询视频业务登录方式管控的总记录数
     * @return
     */
    @Override
    public int countVedioLogonAccess() {
        VedioLogonAccessDOCriteria example = new VedioLogonAccessDOCriteria();
        int count = (int)mapper.countByExample(example);
        return count;
    }

    /**
     * 分页查询视频业务登录方式管控列表
     * @return
     */
    @Override
    public List<VedioLogonAccessDO> getVedioLogonAccessListFromCfgXml() {
        List<VedioLogonAccessDO> vedioLogonAccessList = null;

        //查询xml中的标签
        Document document = XmlUtil.getDocument(servletContext.getRealPath(PathConfig.GOLBALCFG_PATH));
        List<Element> elementList = XmlUtil.getElementListByTree("/cfg/servip/item", document);

        //遍历标签解析为对象
        if (elementList != null && elementList.size() > 0) {
            vedioLogonAccessList = new ArrayList<>();
            for (Element element : elementList) {
                VedioLogonAccessDO vedioLogonAccess = new VedioLogonAccessDO();
                vedioLogonAccess.setId(0);
                vedioLogonAccess.setName(element.attribute("name").getValue());
                vedioLogonAccess.setStartPath(element.attribute("url").getValue());
                vedioLogonAccessList.add(vedioLogonAccess);
            }
        }

        return vedioLogonAccessList;
    }

    /**
     * 添加视频业务登录方式
     * @param vedioLogonAccess
     */
    @Override
    @Transactional
    public void addVedioLogonAccessIntoXml(VedioLogonAccessDO vedioLogonAccess) {
        //对前台传的参数不进行转义操作,原样保存到xml中
        vedioLogonAccess.setStartPath(StringEscapeUtils.unescapeHtml4(vedioLogonAccess.getStartPath()));

        String xmlPath = servletContext.getRealPath(PathConfig.GOLBALCFG_PATH);
        Document document = XmlUtil.getDocument(xmlPath);
        List<Element> elementList = XmlUtil.getElementListByTree("/cfg/servip", document);
        if (elementList != null && elementList.size() == 1) {
            Element itemEle = elementList.get(0).addElement("item");
            itemEle.addAttribute("name", vedioLogonAccess.getName());
            itemEle.addAttribute("url", vedioLogonAccess.getStartPath());
        }
        XmlUtil.writeDocumentIntoFile(document, xmlPath);
    }

    /**
     * 删除视频业务登录方式
     * @param vedioLogonAccess
     */
    @Override
    @Transactional
    public boolean deleteVedioLogonAccessFromCfgXml(VedioLogonAccessDO vedioLogonAccess) {
        //对前台传的参数不进行转义操作,重新赋值到对象中
        vedioLogonAccess.setStartPath(StringEscapeUtils.unescapeHtml4(vedioLogonAccess.getStartPath()));
        String xmlPath = servletContext.getRealPath(PathConfig.GOLBALCFG_PATH);
        Document document = XmlUtil.getDocument(xmlPath);

        List<Element> servipList = XmlUtil.getElementListByTree("/cfg/servip", document);
        if (servipList != null && servipList.size() == 1) {
            Element servipElement = servipList.get(0);
            List<Element> elementList = servipElement.elements();

            String name;
            String startPath;
            for (Element element : elementList) {
                name = element.attributeValue("name");
                startPath = element.attributeValue("url");

                if (name.equals(vedioLogonAccess.getName()) && startPath.equals(vedioLogonAccess.getStartPath())) {
                    servipElement.remove(element);
                    XmlUtil.writeDocumentIntoFile(document, xmlPath);
                    return true;
                }
            }
        }

        return false;
    }


}
