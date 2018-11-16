package cn.goldencis.tdp.approve.service;

import cn.goldencis.tdp.approve.entity.ScrnwatermarkLog;
import cn.goldencis.tdp.approve.entity.ScrnwatermarkLogCriteria;
import cn.goldencis.tdp.common.service.BaseService;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.policy.entity.ClientUserDO;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by limingchao on 2018/1/20.
 */
public interface IScrnwatermarkLogService extends BaseService<ScrnwatermarkLog, ScrnwatermarkLogCriteria> {

    /**
     * 添加水印日志
     *
     * @param watermark
     */
    void addScrnwatermarkLog(ScrnwatermarkLog watermark);

    /**
     * 根据水印日志id，更新水印日志
     *
     * @param watermark
     */
    void updateScrnwatermarkLogById(ScrnwatermarkLog watermark);

    /**
     * 根据水印日志的id获取水印日志信息
     *
     * @param logId 水印日志id
     * @return
     */
    ScrnwatermarkLog getScrnwatermarkLogByLogId(String logId);

    /**
     * 扫描图片识别logId
     *
     * @param cmdStr 需要执行的命令
     * @return
     */
    String scanPicture(String cmdStr);

    /**
     * 使用Python来识别图片的方法
     *
     * @param imgPath
     * @return
     */
    String[] scanPictureByPython(String imgPath);

    /**
     * 根据水印日志的id获取水印日志信息
     *
     * @param logId 水印日志id
     * @return
     */
    ScrnwatermarkLog getScrnwatermarkLogByLast32LogId(String logId);

    /**
     * 将用户信息封装到info对象中
     *
     * @param info       info对象
     * @param clientUser 用户信息
     * @param department
     */
    void setClientUserIntoInfo(JSONObject info, ClientUserDO clientUser, DepartmentDO department);

    /**
     * 判断数据库中是否存在该用户对应的屏幕隐式水印记录
     *
     * @param guid 用户的guid
     * @return 该用户对应的屏幕隐式水印记录是否存在
     */
    boolean isExistsClientUserScrnwatermark(String guid);
}
