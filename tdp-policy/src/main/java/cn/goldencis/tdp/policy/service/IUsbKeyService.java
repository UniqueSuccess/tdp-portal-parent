package cn.goldencis.tdp.policy.service;

import cn.goldencis.tdp.common.service.BaseService;
import cn.goldencis.tdp.policy.entity.ClientUserDO;
import cn.goldencis.tdp.policy.entity.UsbKeyDO;
import cn.goldencis.tdp.policy.entity.UsbKeyDOCriteria;

import java.util.List;

/**
 * Created by limingchao on 2018/1/4.
 */
public interface IUsbKeyService extends BaseService<UsbKeyDO, UsbKeyDOCriteria> {

    /**
     * 通过用户解除绑定
     * @param clientUser 用户对象
     */
    void unbindUsbKeyByClientUser(ClientUserDO clientUser);

    /**
     * 通过用户集合解除绑定
     * @param clientUserList 用户集合
     * @param idList 用户id集合
     */
    void unbindUsbKeyByClientUser(List<ClientUserDO> clientUserList, List<Integer> idList);

    /**
     * 通过usbkey的id集合解除绑定
     * @param idList usbkey的id集合
     */
    void unbindUsbKeyByUsbKeyIdList(List<Integer> idList);

    /**
     * 获取全部未绑定的UsbKey列表
     * @return UsbKey列表
     */
    List<UsbKeyDO> getAllUnbindUsbKey();

    /**
     * 查询UsbKey的总数量
     * @param order 查询条件
     * @return UsbKey的总数量
     */
    int countAllUsbKey(String order);

    /**
     * 获取UsbKey列表，分页查询
     * @param start 开始
     * @param length 每页条数
     * @param order 查询条件
     * @return UsbKey列表
     */
    List<UsbKeyDO> getUsbKeyInPage(Integer start, Integer length, String order);

    /**
     * 新增UsbKey对象
     * @param usbKey 新建的UsbKey对象
     */
    void addUsbKey(UsbKeyDO usbKey);

    /**
     * 编辑UsbKey对象
     * @param usbKey 编辑的UsbKey对象
     */
    void editUsbKey(UsbKeyDO usbKey);

    /**
     * 删除UsbKey对象
     * @param idList 要删除的usbkey的id的数组
     */
    void deleteUsbKey(List<Integer> idList);

    /**
     * 判断UsbKey名称是否重复
     * @param usbKey usbkey对象
     * @return 名称是否重复
     */
    boolean checkUsbKeyNameDuplicate(UsbKeyDO usbKey);

    /**
     * 判断UsbKey是否重复录入
     * @param usbKey usbkey对象
     * @return 是否重复录入
     */
    boolean checkUsbKeyNumDuplicate(UsbKeyDO usbKey);

    /**
     * 判断UsbKey是否绑定用户
     * @param idList 要删除的usbkey的id的数组
     * @return 是否绑定用户
     */
    boolean checkUsbKeyIsBinded(List<Integer> idList);

    /**
     * 根据usbkey的名称查询usbkey对象
     * @param name usbkey的名称
     * @return usbkey对象
     */
    UsbKeyDO getUsbKeyByName(String name);

    /**
     * 根据usbkey的唯一标示查询usbkey对象
     * @param keynum usbkey的唯一标示
     * @return usbkey对象
     */
    UsbKeyDO getUsbKeyByNum(String keynum);

    /**
     * 获取usbkey登录时，是否需要密码
     * @return 是否需要密码
     */
    Boolean getPwdNeeded();

    /**
     * 刷新usbkey登录时，是否需要密码的状态
     */
    boolean refreshPwdNeededState();

    /**
     * 更新usbkey双因子认证的开启状态
     * @param isNeeded 更新的状态
     */
    void updateUsbkeyPwdState(boolean isNeeded);
}
