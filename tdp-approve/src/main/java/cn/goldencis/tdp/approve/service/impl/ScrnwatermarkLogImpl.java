package cn.goldencis.tdp.approve.service.impl;

import cn.goldencis.tdp.approve.dao.ScrnwatermarkLogMapper;
import cn.goldencis.tdp.approve.entity.ScrnwatermarkLog;
import cn.goldencis.tdp.approve.entity.ScrnwatermarkLogCriteria;
import cn.goldencis.tdp.approve.service.IScrnwatermarkLogService;
import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.policy.entity.ClientUserDO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by limingchao on 2018/1/20.
 */
@Service
public class ScrnwatermarkLogImpl extends AbstractBaseServiceImpl<ScrnwatermarkLog, ScrnwatermarkLogCriteria> implements IScrnwatermarkLogService {

    @Autowired
    private ScrnwatermarkLogMapper mapper;

    @Override
    protected BaseDao<ScrnwatermarkLog, ScrnwatermarkLogCriteria> getDao() {
        return mapper;
    }

    /**
     * 添加水印日志
     *
     * @param watermark
     */
    @Override
    public void addScrnwatermarkLog(ScrnwatermarkLog watermark) {
        //不设置随机id，使用自增id
        mapper.insert(watermark);
    }

    /**
     * 根据水印日志id，更新水印日志
     *
     * @param watermark
     */
    @Override
    public void updateScrnwatermarkLogById(ScrnwatermarkLog watermark) {
        ScrnwatermarkLogCriteria example = new ScrnwatermarkLogCriteria();
        example.createCriteria().andIdEqualTo(watermark.getId());
        mapper.updateByExampleSelective(watermark, example);
    }

    /**
     * 根据水印日志的id获取水印日志信息
     *
     * @param logId 水印日志id
     * @return
     */
    @Override
    public ScrnwatermarkLog getScrnwatermarkLogByLogId(String logId) {
        ScrnwatermarkLogCriteria example = new ScrnwatermarkLogCriteria();
        example.createCriteria().andScrnwatermarkIdEqualTo(logId);
        List<ScrnwatermarkLog> scrnwatermarkLogList = mapper.selectByExample(example);
        if (scrnwatermarkLogList.size() > 0) {
            ScrnwatermarkLog scrnwatermarkLog = scrnwatermarkLogList.get(0);

            return scrnwatermarkLog;
        }
        return null;
    }

    /**
     * 扫描图片识别logId
     *
     * @param cmdStr 需要执行的命令
     * @return
     */
    @Override
    public String scanPicture(String cmdStr) {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader brStat = null;
        String str = null;
        try {
            String[] cmd = new String[]{"sh", "-c", cmdStr};
            Process process = Runtime.getRuntime().exec(cmd);
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            brStat = new BufferedReader(isr);
            str = brStat.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (brStat != null) {
                    brStat.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    @Override
    public String[] scanPictureByPython(String imgPath) {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader brStat = null;
        String resStr;
        String[] logIdArr = null;
        try {
            String[] cmd = new String[]{"python3", "/gdsoft/soft/stp.py", imgPath};
            Process process = Runtime.getRuntime().exec(cmd);
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            brStat = new BufferedReader(isr);
            resStr = brStat.readLine();
            //处理识别返回信息
            if (resStr != null && resStr.length() > 2) {
                String arrStr = resStr.substring(1, resStr.length() - 1);
                arrStr = arrStr.replace("'", "");
                logIdArr = arrStr.split(",");
                for (int i = 0; i < logIdArr.length; i++) {
                    logIdArr[i] = "s" + logIdArr[i].trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }

                if (isr != null) {
                    isr.close();
                }
                if (brStat != null) {
                    brStat.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return logIdArr;
    }

    @Override
    public ScrnwatermarkLog getScrnwatermarkLogByLast32LogId(String logId) {
        if (logId.length() > 32) {
            logId = "%" + logId.substring(logId.length() - 32);
            ScrnwatermarkLogCriteria example = new ScrnwatermarkLogCriteria();
            example.createCriteria().andScrnwatermarkIdLike(logId);
            List<ScrnwatermarkLog> scrnwatermarkLogList = mapper.selectByExample(example);
            if (scrnwatermarkLogList.size() > 0) {
                return scrnwatermarkLogList.get(0);
            }
        }
        return null;
    }

    @Override
    public void setClientUserIntoInfo(JSONObject info, ClientUserDO clientUser, DepartmentDO department) {
        info.put("truename", clientUser.getTruename());
        info.put("department", department.getName());
        info.put("ip", clientUser.getIp());
        info.put("mac", clientUser.getMac());
        info.put("computername", clientUser.getComputername());
    }

    @Override
    public boolean isExistsClientUserScrnwatermark(String guid) {
        ScrnwatermarkLogCriteria example = new ScrnwatermarkLogCriteria();
        example.createCriteria().andApplicantIdEqualTo(guid).andApplyInfoEqualTo("{\"type\":\"用户屏幕水印\"}");
        long count = mapper.countByExample(example);
        return count > 0 ? true : false;
    }
}
