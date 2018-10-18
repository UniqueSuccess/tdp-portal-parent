package cn.goldencis.tdp.report.service.impl;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.common.utils.StringUtil;
import cn.goldencis.tdp.common.utils.SysContext;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.DepartmentDO;
import cn.goldencis.tdp.core.utils.LegalTimeUtil;
import cn.goldencis.tdp.core.utils.NetworkUtil;
import cn.goldencis.tdp.core.utils.PathConfig;
import cn.goldencis.tdp.core.websocket.IVdpWebSocketOnMessageService;
import cn.goldencis.tdp.core.websocket.VdpWebSocketHandler;
import cn.goldencis.tdp.report.dao.IllegalOperationAlarmDOMapper;
import cn.goldencis.tdp.report.entity.IllegalOperationAlarmDO;
import cn.goldencis.tdp.report.entity.IllegalOperationAlarmDOCriteria;
import cn.goldencis.tdp.report.service.IIllegalOperationAlarmService;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.socket.TextMessage;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.util.*;

/**
 * Created by limingchao on 2018/5/23.
 */
@Service
public class IllegalOperationAlarmServiceImpl extends AbstractBaseServiceImpl<IllegalOperationAlarmDO, IllegalOperationAlarmDOCriteria> implements IIllegalOperationAlarmService, ServletContextAware, IVdpWebSocketOnMessageService {

    @Autowired
    private IllegalOperationAlarmDOMapper mapper;

    @Autowired
    private VdpWebSocketHandler webSocketHandler;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    protected BaseDao<IllegalOperationAlarmDO, IllegalOperationAlarmDOCriteria> getDao() {
        return mapper;
    }

    @PostConstruct
    public void init() {
        webSocketHandler.getOnMessageServiceMap().put("illegalOperationAlarm", this);
        LegalTimeUtil.setCfgPath(servletContext.getRealPath(PathConfig.GOLBALCFG_PATH));
        LegalTimeUtil.refreshClientLegalTime();
    }

    @Override
    public void encapsulationBean(IllegalOperationAlarmDO alarm, JSONObject argvJson) {
        //设置基本参数
        alarm.setTruename(argvJson.getString("truename"));
        alarm.setUsername(argvJson.getString("username"));
        alarm.setUserguid(argvJson.getString("usrunique"));
        alarm.setDepartmentId(Integer.parseInt((String) argvJson.get("depid")));
        alarm.setDepartmentName(argvJson.getString("depname"));
        alarm.setDevunique(argvJson.getString("devunique"));
        alarm.setExtradata(argvJson.toString());

        //设置ip地址
        String ip = NetworkUtil.getIpAddress(SysContext.getRequest());
        alarm.setIp(ip);

        //设置未读状态
        alarm.setHasRead(ConstantsDto.ALARM_NOT_READ);
        //设置时间
        alarm.setWarningTime(new Date());

    }

    @Override
    public void addIllegalOperationAlarm(IllegalOperationAlarmDO alarm) {
        mapper.insertSelective(alarm);
    }

    @Override
    public void warningVideoTransfer(IllegalOperationAlarmDO alarm, JSONObject argvJson) {
        //解析参数，转化为非法登录告警对象
        this.encapsulationBean(alarm, argvJson);

        //设置告警类型
        if (ConstantsDto.SELF_EXPORT.intValue() == argvJson.getInt("fftype") || ConstantsDto.APPROVE_EXPORT.intValue() == argvJson.getInt("fftype")) {
            alarm.setWarningType(ConstantsDto.ALARM_OPT);
        } else if (ConstantsDto.SELF_OUT.intValue() == argvJson.getInt("fftype") || ConstantsDto.APPROVE_OUT.intValue() == argvJson.getInt("fftype")) {
            alarm.setWarningType(ConstantsDto.ALARM_OUTCFG);
        }

        //设置接收人
        alarm.setReceiver(argvJson.getJSONObject("extradata").getString("recv"));

        //设置流转文件
        alarm.setFileName(argvJson.getString("filepath"));
    }

    @Override
    public int IllegalOperationAlarm(List<DepartmentDO> departmentList, String logType, String submitDate, String startDate, String endDate, String order) {
        IllegalOperationAlarmDOCriteria example = generatorAlarmExample(departmentList, logType, submitDate, startDate, endDate, order);
        long count = mapper.countByExample(example);
        return (int) count;
    }

    @Override
    public List<IllegalOperationAlarmDO> getIllegalOperationAlarmInPage(Integer start, Integer length, List<DepartmentDO> departmentList, String logType, String submitDate, String startDate, String endDate, String order) {
        RowBounds rowBounds = new RowBounds(start, length);
        IllegalOperationAlarmDOCriteria example = generatorAlarmExample(departmentList, logType, submitDate, startDate, endDate, order);

        return mapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    @Override
    public void readIllegalOperationAlarmByIdList(List<Integer> idList) {
        IllegalOperationAlarmDOCriteria example = new IllegalOperationAlarmDOCriteria();
        example.createCriteria().andIdIn(idList);

        IllegalOperationAlarmDO alarm = new IllegalOperationAlarmDO();
        alarm.setHasRead(ConstantsDto.ALARM_READ);

        mapper.updateByExampleSelective(alarm, example);
    }

    public IllegalOperationAlarmDOCriteria generatorAlarmExample(List<DepartmentDO> departmentList, String logType, String submitDate, String startDate, String endDate, String order) {
        IllegalOperationAlarmDOCriteria example = new IllegalOperationAlarmDOCriteria();

        List<IllegalOperationAlarmDOCriteria.Criteria> criteriaList = new ArrayList();

        if (!StringUtil.isEmpty(order)) {
            criteriaList.add(example.or().andUsernameLike("%" + order + "%"));
            criteriaList.add(example.or().andTruenameLike("%" + order + "%"));
            criteriaList.add(example.or().andFileNameLike("%" + order + "%"));
            criteriaList.add(example.or().andReceiverLikeInsensitive("%" + order + "%"));
        } else {
            criteriaList.add(example.createCriteria());
        }

        //设置部门查询条件
        if (departmentList != null && departmentList.size() > 0) {
            List<Integer> idList = new ArrayList<>();
            for (DepartmentDO department : departmentList) {
                idList.add(department.getId());
            }
            for (IllegalOperationAlarmDOCriteria.Criteria criteria : criteriaList) {
                criteria.andDepartmentIdIn(idList);
            }
        }

        //添加流转类型查询
        if (!StringUtils.isEmpty(logType)) {
            for (IllegalOperationAlarmDOCriteria.Criteria criteria : criteriaList) {
                criteria.andWarningTypeEqualTo(Integer.parseInt(logType));
            }
        }

        //解析查询的开始时间和结束时间
        Map<String, Date> timeMap = DateUtil.analyzeQueryTime(submitDate, startDate, endDate);
        if (timeMap != null && timeMap.size() > 1) {
            //使用通用的方法解析查询的开始时间和结束时间
            Date startDateTime = timeMap.get("startDateTime");
            Date endDateTime = timeMap.get("endDateTime");

            if (startDateTime != null && endDateTime != null) {
                for (IllegalOperationAlarmDOCriteria.Criteria criteria : criteriaList) {
                    criteria.andWarningTimeBetween(startDateTime, endDateTime);
                }
            }
        }

        example.setOrderByClause("has_read, warning_time DESC");

        return example;
    }

    @Override
    public int countNotReadAlarm() {
        IllegalOperationAlarmDOCriteria example = new IllegalOperationAlarmDOCriteria();
        example.createCriteria().andHasReadEqualTo(ConstantsDto.ALARM_NOT_READ);
        long count = mapper.countByExample(example);
        return (int)count;
    }

    @Override
    public void noticeIllegalOperationAlarm() {
        //统计当前未读告警的数量
        int count = this.countNotReadAlarm();
        JSONObject res = new JSONObject();
        res.put("count", count);
        //查询告警声音选项
        boolean sound = LegalTimeUtil.isSound();
        res.put("sound", sound);
        //设置socket消息类型
        res.put("messageType", "alarmCount");
        TextMessage message = new TextMessage(res.toString());
        webSocketHandler.sendMessageToUsers(message);
    }

    @Override
    public List<IllegalOperationAlarmDO> getIllegalOperationAlarm(List<DepartmentDO> departmentList, String logType, String submitDate, String startDate, String endDate, String order) {
        IllegalOperationAlarmDOCriteria example = generatorAlarmExample(departmentList, logType, submitDate, startDate, endDate, order);
        return mapper.selectByExample(example);
    }

    @Override
    public Object[] convertParams(Object invoke, Object params) {
        Object[] objs = null;


        return objs;
    }


}
