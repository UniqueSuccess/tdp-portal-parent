package cn.goldencis.tdp.policy.dao;

import cn.goldencis.tdp.common.annotation.Mybatis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by limingchao on 2018/6/4.
 */
@Mybatis
public interface CUsbKeyDOMapper {

    /**
     * 根据usbkey的id集合，解除usbkey的绑定
     * @param idList usbkey的id集合
     */
    void batchUnbindedByUsbkeyIdList(@Param(value = "idList") List<Integer> idList);

    /**
     * 根据用户guid，解除usbkey的绑定
     * @param userguid 用户guid
     */
    void unbindedByClientUserGuid(@Param(value = "userguid") String userguid);

    /**
     * 根据用户guid的集合，解除usbkey的绑定
     * @param guidList 用户guid的集合
     */
    void unbindedByClientUserGuidList(@Param(value = "guidList") List<String> guidList);
}
