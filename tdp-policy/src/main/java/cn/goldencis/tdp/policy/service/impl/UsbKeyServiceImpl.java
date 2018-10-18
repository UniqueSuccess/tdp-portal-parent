package cn.goldencis.tdp.policy.service.impl;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.common.utils.XmlUtil;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.goldencis.tdp.policy.dao.CUsbKeyDOMapper;
import cn.goldencis.tdp.policy.dao.ClientUserDOMapper;
import cn.goldencis.tdp.policy.entity.ClientUserDO;
import cn.goldencis.tdp.policy.dao.UsbKeyDOMapper;
import cn.goldencis.tdp.policy.entity.ClientUserDOCriteria;
import cn.goldencis.tdp.policy.entity.UsbKeyDO;
import cn.goldencis.tdp.policy.entity.UsbKeyDOCriteria;
import cn.goldencis.tdp.policy.service.IUsbKeyService;
import org.apache.ibatis.session.RowBounds;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by limingchao on 2018/1/4.
 */
@Service
public class UsbKeyServiceImpl extends AbstractBaseServiceImpl<UsbKeyDO, UsbKeyDOCriteria> implements IUsbKeyService, ServletContextAware {

    @Autowired
    private UsbKeyDOMapper mapper;

    @Autowired
    private CUsbKeyDOMapper cmapper;

    @Autowired
    private ClientUserDOMapper clientUserDOMapper;

    private Boolean pwdNeeded = null;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    protected BaseDao<UsbKeyDO, UsbKeyDOCriteria> getDao() {
        return mapper;
    }

    /**
     * 根据用户id，将用户绑定状态置0，即未绑定。将UsbKey的用户id改成0
     * @param clientUser
     */
    @Override
    @Transactional
    public void unbindUsbKeyByClientUser(ClientUserDO clientUser) {
        //设置用户绑定UsbKey的状态为0，即未绑定
        //执行用户更新
        clientUserDOMapper.updateByPrimaryKeySelective(clientUser);

        //解除usbkey的绑定关联
        cmapper.unbindedByClientUserGuid(clientUser.getGuid());
    }

    @Override
    public void unbindUsbKeyByClientUser(List<ClientUserDO> clientUserList, List<Integer> idList) {
        //获取用户的guid集合
        List<String> guidList = new ArrayList<>();
        for (ClientUserDO clientUser : clientUserList) {
            guidList.add(clientUser.getGuid());
        }
        //解除usbkey的绑定关联
        cmapper.unbindedByClientUserGuidList(guidList);

        ClientUserDO clientUser = new ClientUserDO();
        //设置用户绑定UsbKey的状态为0，即未绑定
        //设置修改的用户id集合
        ClientUserDOCriteria example = new ClientUserDOCriteria();
        example.createCriteria().andIdIn(idList);
        //修改用户状态
        clientUserDOMapper.updateByExampleSelective(clientUser, example);
    }

    @Override
    @Transactional
    public void unbindUsbKeyByUsbKeyIdList(List<Integer> idList) {
        //查询要解绑的usbkey集合
        UsbKeyDOCriteria example = new UsbKeyDOCriteria();
        example.createCriteria().andIdIn(idList);
        List<UsbKeyDO> usbKeyList = mapper.selectByExample(example);

        //需要解绑的用户guid集合
        List<String> guidList = new ArrayList<>();
        for (UsbKeyDO usbKey : usbKeyList) {
            guidList.add(usbKey.getUserguid());
        }
        ClientUserDOCriteria clientUserExample = new ClientUserDOCriteria();
        clientUserExample.createCriteria().andGuidIn(guidList);

        //设置用户绑定UsbKey的状态为0，即未绑定
        ClientUserDO clientUser = new ClientUserDO();
        //更新用户信息
        clientUserDOMapper.updateByExampleSelective(clientUser, clientUserExample);

        //解除usbkey的绑定
        cmapper.batchUnbindedByUsbkeyIdList(idList);
    }

    /**
     * 获取全部未绑定的UsbKey列表
     * @return
     */
    @Override
    public List<UsbKeyDO> getAllUnbindUsbKey() {
        UsbKeyDOCriteria example = new UsbKeyDOCriteria();
        example.createCriteria().andUserguidIsNull();
        List<UsbKeyDO> usbKeyList = mapper.selectByExample(example);
        return usbKeyList;
    }

    @Override
    public int countAllUsbKey(String order) {

        UsbKeyDOCriteria example = new UsbKeyDOCriteria();
        if (StringUtils.isEmpty(order)) {
            //设置昵称的查询条件
            UsbKeyDOCriteria.Criteria criteria = example.createCriteria();
            criteria.andNameLike("%" + order +"%");

        }
        long count = mapper.countByExample(example);
        return (int)count;
    }

    @Override
    public List<UsbKeyDO> getUsbKeyInPage(Integer start, Integer length, String order) {
        RowBounds row = new RowBounds(start, length);
        UsbKeyDOCriteria example = new UsbKeyDOCriteria();
        if (!StringUtils.isEmpty(order)) {
            //设置昵称的查询条件
            UsbKeyDOCriteria.Criteria criteria = example.createCriteria();
            criteria.andNameLike("%" + order +"%");

        }
        List<UsbKeyDO> usbKeyList = mapper.selectByExampleWithRowbounds(example, row);

        if (usbKeyList != null && usbKeyList.size() > 0) {
            //填充用户名称
            this.setClientUserNameForUsbkey(usbKeyList);
        }

        return usbKeyList;
    }

    /**
     * 为usbkey集合中的对象，填充关联的用户名称
     * @param usbKeyList 需要填充的usbkey集合
     */
    private void setClientUserNameForUsbkey(List<UsbKeyDO> usbKeyList) {
        //转化guid的集合
        List<String> guidList = usbKeyList.stream().map(UsbKeyDO::getUserguid).collect(Collectors.toList());

        if (guidList != null && guidList.size() > 0) {
            //查询出对应的用户集合
            ClientUserDOCriteria example = new ClientUserDOCriteria();
            example.createCriteria().andGuidIn(guidList);
            List<ClientUserDO> clientUserList = clientUserDOMapper.selectByExample(example);
            Map<String, String> clientUserMap = clientUserList.stream().collect(Collectors.toMap(ClientUserDO::getGuid, ClientUserDO::getUsername));

            usbKeyList.stream().filter(usbKeyDO -> !StringUtils.isEmpty(usbKeyDO.getUserguid())).forEach(usbKeyDO -> usbKeyDO.setUserName(clientUserMap.get(usbKeyDO.getUserguid())));
        }



    }

    @Override
    public void addUsbKey(UsbKeyDO usbKey) {
        usbKey.setRegtime(new Date());
        mapper.insertSelective(usbKey);
    }

    @Override
    public void editUsbKey(UsbKeyDO usbKey) {
        UsbKeyDOCriteria example = new UsbKeyDOCriteria();
        example.createCriteria().andIdEqualTo(usbKey.getId());
        mapper.updateByExampleSelective(usbKey, example);
    }

    @Override
    public void deleteUsbKey(List<Integer> idList) {
        UsbKeyDOCriteria example = new UsbKeyDOCriteria();
        example.createCriteria().andIdIn(idList);
        mapper.deleteByExample(example);
    }

    @Override
    public boolean checkUsbKeyNameDuplicate(UsbKeyDO usbKey) {
        //通过usbkey名称获取数据库中usbkey记录
        UsbKeyDO preUsbKey = this.getUsbKeyByName(usbKey.getName());

        //判断数据库是否有该记录，不存在即可用，返回true，如果有继续判断
        if (preUsbKey != null) {
            //比较两个对象的id，若一致，是同一个对象没有改变名称的情况，返回可用true。
            if (preUsbKey.getId().equals(usbKey.getId())) {
                return true;
            }
            //若果不同，说明为两个usbkey，名称重复，不可用，返回false
            return false;
        }
        return true;
    }

    @Override
    public boolean checkUsbKeyNumDuplicate(UsbKeyDO usbKey) {
        //通过usbkey唯一标示获取数据库中usbkey记录
        UsbKeyDO preUsbKey = this.getUsbKeyByNum(usbKey.getKeynum());

        //判断数据库是否有该记录，不存在即可用，返回true，如果有继续判断
        if (preUsbKey != null) {
            //比较两个对象的id，若一致，是同一个对象没有改变名称的情况，返回可用true。
            if (preUsbKey.getId().equals(usbKey.getId())) {
                return true;
            }
            //若果不同，说明为两个usbkey，名称重复，不可用，返回false
            return false;
        }
        return true;
    }

    @Override
    public boolean checkUsbKeyIsBinded(List<Integer> idList) {
        UsbKeyDOCriteria example = new UsbKeyDOCriteria();
        example.createCriteria().andIdIn(idList);
        List<UsbKeyDO> usbKeyList = mapper.selectByExample(example);
        //遍历返回集合，任一对象中都没有绑定用户，才允许删除。
        for (UsbKeyDO usbKey : usbKeyList) {
            if (!StringUtils.isEmpty(usbKey.getUserguid())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public UsbKeyDO getUsbKeyByName(String name) {
        UsbKeyDOCriteria example = new UsbKeyDOCriteria();
        example.createCriteria().andNameEqualTo(name);
        List<UsbKeyDO> usbKeyList = mapper.selectByExample(example);
        if (usbKeyList != null && usbKeyList.size() > 0) {
            return usbKeyList.get(0);
        }
        return null;
    }

    @Override
    public UsbKeyDO getUsbKeyByNum(String keynum) {
        UsbKeyDOCriteria example = new UsbKeyDOCriteria();
        example.createCriteria().andKeynumEqualTo(keynum);
        List<UsbKeyDO> usbKeyList = mapper.selectByExample(example);
        if (usbKeyList != null && usbKeyList.size() > 0) {
            return usbKeyList.get(0);
        }
        return null;
    }

    @Override
    public Boolean getPwdNeeded() {
        if (pwdNeeded == null) {
            this.refreshPwdNeededState();
        }
        return pwdNeeded;
    }

    @Override
    public boolean refreshPwdNeededState() {
        String cfgPath = servletContext.getRealPath(PathConfig.GOLBALCFG_PATH);
        //查询xml中的标签
        Document document = XmlUtil.getDocument(cfgPath);

        //解析否需要密码的状态
        List<String> pwdneeded = XmlUtil.getAttributeValueListByTree("/cfg/usbkey/@pwdneeded", document);

        if (pwdneeded != null && pwdneeded.size() > 0) {
            pwdNeeded = Boolean.valueOf(pwdneeded.get(0));
            return true;
        }

        return false;
    }

    @Override
    public void updateUsbkeyPwdState(boolean isNeeded) {
        String cfgPath = servletContext.getRealPath(PathConfig.GOLBALCFG_PATH);
        //查询xml中的标签
        Document document = XmlUtil.getDocument(cfgPath);

        XmlUtil.updateAttributeByTree("/cfg/usbkey/@pwdneeded", String.valueOf(isNeeded), document);

        //将修改后的dom写回xml
        XmlUtil.writeDocumentIntoFile(document, cfgPath);

        this.refreshPwdNeededState();
    }
}
