package cn.goldencis.tdp.system.service;

import cn.goldencis.tdp.common.service.BaseService;
import cn.goldencis.tdp.system.entity.VedioLogonAccessDO;
import cn.goldencis.tdp.system.entity.VedioLogonAccessDOCriteria;

import java.util.List;

/**
 * Created by limingchao on 2018/1/9.
 */
public interface IVedioLogonAccessService extends BaseService<VedioLogonAccessDO, VedioLogonAccessDOCriteria> {

    /**
     *查询视频业务登录方式管控的总记录数
     * @return
     */
    int countVedioLogonAccess();

    /**
     * 从全局配置的XML文件中，查询视频业务登录方式管控列表
     * @return
     */
    List<VedioLogonAccessDO> getVedioLogonAccessListFromCfgXml();

    /**
     * 添加视频业务登录方式
     * @param vedioLogonAccess
     */
    void addVedioLogonAccessIntoXml(VedioLogonAccessDO vedioLogonAccess);


    /**
     * 删除视频业务登录方式
     * @param vedioLogonAccessId
     */
    boolean deleteVedioLogonAccessFromCfgXml(VedioLogonAccessDO vedioLogonAccessId);
}
