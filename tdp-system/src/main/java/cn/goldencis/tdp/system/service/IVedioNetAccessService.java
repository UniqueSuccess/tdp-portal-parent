package cn.goldencis.tdp.system.service;

import cn.goldencis.tdp.common.service.BaseService;
import cn.goldencis.tdp.system.entity.*;

import java.util.List;

/**
 * Created by limingchao on 2018/1/9.
 */
public interface IVedioNetAccessService extends BaseService<VedioNetAccessDO, VedioNetAccessDOCriteria> {

    /**
     * 查询视频业务网络访问管控的总记录数
     * @return
     */
    int countVedioNetAccess();

    /**
     * 从全局配置的XML文件中，查询视频业务网络访问管控列表
     * @return
     */
    List<String> getVedioNetAccessListFromCfgXml();

    /**
     * 添加视频业务网络访问
     * @param vedioNetAccess
     */
    void addVedioNetAccessIntoXml(VedioNetAccessDO vedioNetAccess);

    /**
     * 删除视频业务网络访问
     * @param vedioNetAccess
     */
    boolean deleteVedioNetAccessFromCfgXml(VedioNetAccessDO vedioNetAccess);
}
