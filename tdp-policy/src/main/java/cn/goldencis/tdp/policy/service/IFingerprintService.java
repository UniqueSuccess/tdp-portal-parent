package cn.goldencis.tdp.policy.service;

import cn.goldencis.tdp.common.service.BaseService;
import cn.goldencis.tdp.policy.entity.FingerprintDO;
import cn.goldencis.tdp.policy.entity.FingerprintDOCriteria;

import java.util.List;

/**
 * Created by limingchao on 2018/5/18.
 */
public interface IFingerprintService extends BaseService<FingerprintDO, FingerprintDOCriteria> {

    /**
     * 将指纹参数解析为指纹实体集合
     * @param fingerprintParam 指纹参数
     * @return
     */
    List<FingerprintDO> parseFingerprintParam(String fingerprintParam);

    /**
     * 检查指纹录入是否达到最大
     * @param guid 用户guid
     * @param fingerprintList 指纹对象集合
     */
    boolean checkFingerprintMaxCount(String guid, List<FingerprintDO> fingerprintList);

    /**
     * 为用户添加关联指纹
     * @param guid 用户guid
     * @param fingerprintList 指纹对象集合
     */
    void addFingerprintListForClientUser(String guid, List<FingerprintDO> fingerprintList);

    /**
     * 更新用户指纹信息
     * @param fingerprintList 指纹对象集合
     */
    void updateFingerprintListForClientUser(List<FingerprintDO> fingerprintList);

    /**
     * 修改用户的指纹信息
     * @param guid 用户guid
     * @param fingerprintList 指纹对象集合
     */
    void modifyFingerprintListForClientUser(String guid, List<FingerprintDO> fingerprintList);

    /**
     * 根据用户guid的删除对应的指纹信息
     * @param guid 用户guid
     */
    void deleteFingerprintByClientUserGuid(String guid);

    /**
     * 根据指纹id集合,删除对应的指纹信息
     * @param delete 指纹记录的id集合
     */
    void deleteFingerprintByIdList(List<Integer> delete);

    /**
     * 指纹比对接口，传入指纹信息，跟库中的所有指纹进行比对
     * @param verTemplate 需要比对的指纹信息
     * @return 比对成功，返回匹配用户的guid，失败返回null
     */
    String verifyFingerprint(String verTemplate);

    /**
     * 根据用户guid，查找对应指纹信息
     * @param guid 用户guid
     * @return 返回指纹集合
     */
    List<FingerprintDO> getFingerprintListByCilentUserGuid(String guid);

    /**
     * 刷新指纹比对模板集合
     * @return 刷新成功返回true，失败返回false
     */
    boolean refreshFingerprintRegCache();
}
