package cn.goldencis.tdp.core.service.impl;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.core.annotation.LogType;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.dao.OperationLogDOMapper;
import cn.goldencis.tdp.core.entity.OperationLogDO;
import cn.goldencis.tdp.core.entity.OperationLogDOCriteria;
import cn.goldencis.tdp.core.service.IOperationLogService;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/1/25.
 */
@Service
public class OperationLogServiceImpl extends AbstractBaseServiceImpl<OperationLogDO, OperationLogDOCriteria> implements IOperationLogService {

    @Autowired
    private OperationLogDOMapper mapper;

    @Override
    protected BaseDao<OperationLogDO, OperationLogDOCriteria> getDao() {
        return mapper;
    }

    /**
     * 多条件分页查询登录日志
     * @param start
     * @param length
     * @param logType
     * @param order    @return
     */
    @Override
    public List<OperationLogDO> getSystemLogListInPageByParams(Integer start, Integer length, String logType, Map<String, Date> timeMap, String search, String orderType) {
        RowBounds row = new RowBounds(start, length);

        //生成日志查询参数
        OperationLogDOCriteria example = this.generatorSystemLogExample(logType, timeMap, search);
        example.setOrderByClause("time " + orderType);

        List<OperationLogDO> operationLogList = mapper.selectByExampleWithBLOBsWithRowbounds(example, row);
        return operationLogList;
    }

    /**
     * 查询登录日志总数
     * @param logType
     * @param order
     * @return
     */
    @Override
    public int countSystemLogListByParams(String logType, Map<String, Date> timeMap, String order) {
        //生成日志查询参数
        OperationLogDOCriteria example = this.generatorSystemLogExample(logType, timeMap, order);
        long count = mapper.countByExample(example);
        return (int) count;
    }

    /**
     * 按条件获取日志列表，不分页
     * @param logType
     * @param timeMap
     * @param order
     */
    @Override
    public List<OperationLogDO> getSystemLogListByParams(String logType, Map<String, Date> timeMap, String order) {
        //生成日志查询参数
        OperationLogDOCriteria example = this.generatorSystemLogExample(logType, timeMap, order);
        return mapper.selectByExampleWithBLOBs(example);
    }

    /**
     * 清理系统日志
     * @param clearDate 需要清理日志的日期
     * @param logType
     */
    @Override
    public void deleteOperationLogsByClearDate(Date clearDate, String logType) {
        OperationLogDOCriteria example = new OperationLogDOCriteria();
        if (ConstantsDto.LOGON_LOG_TYPE.equals(logType)) {
            example.createCriteria().andTimeLessThan(clearDate).andLogTypeEqualTo(0);
        } else if (ConstantsDto.CLIENT_LOGON_LOG_TYPE.equals(logType)) {
            example.createCriteria().andTimeLessThan(clearDate).andLogTypeEqualTo(8);
        }else {
            example.createCriteria().andTimeLessThan(clearDate).andLogTypeNotEqualTo(0).andLogTypeNotEqualTo(8);
        }
        mapper.deleteByExample(example);
    }

    /**
     * 生成日志查询参数
     * @param logType
     * @param timeMap
     * @param order
     * @return
     */
    private OperationLogDOCriteria generatorSystemLogExample(String logType, Map<String, Date> timeMap, String order) {
        OperationLogDOCriteria example = new OperationLogDOCriteria();
        OperationLogDOCriteria.Criteria criteria = example.createCriteria();
        //添加类型判断
        if (ConstantsDto.LOGON_LOG_TYPE.equals(logType)) {
            criteria.andLogTypeEqualTo(LogType.LOGIN.value());
        } else if (ConstantsDto.CLIENT_LOGON_LOG_TYPE.equals(logType)) {
            criteria.andLogTypeEqualTo(LogType.CLIENT_LOGIN.value());
        } else {
            criteria.andLogTypeNotEqualTo(LogType.LOGIN.value()).andLogTypeNotEqualTo(LogType.CLIENT_LOGIN.value());
        }
        //添加时间范围
        if (timeMap != null && timeMap.size() > 1) {
            Date startDateTime = timeMap.get("startDateTime");
            Date endDateTime = timeMap.get("endDateTime");

            if (startDateTime != null && endDateTime != null) {
                criteria.andTimeBetween(startDateTime, endDateTime);
            }
        }
        //添加查询条件
        if (!StringUtil.isEmpty(order)) {
            criteria.andSearchLike(" (instr(ip, '" + order + "') or " + " instr(log_operate_param, '" + order + "') or " + " instr(user_name, '" + order + "')) ");
        }
        return example;
    }

}
