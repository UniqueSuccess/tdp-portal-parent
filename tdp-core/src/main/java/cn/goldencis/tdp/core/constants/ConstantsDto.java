package cn.goldencis.tdp.core.constants;

public class ConstantsDto {

    public static final Integer CONST_TRUE = 1;
    public static final Integer CONST_FALSE = 0;
    public static final Integer CONST_ERROR = -1;
    public static final Integer CONST_ERROR_USER_PASSWORD = 2;
    public static final int RESULT_CODE_FALSE = 1;
    public static final int RESULT_CODE_TRUE = 0;
    public static final int RESULT_CODE_ERROR = -1;

    //审批流程的执行状态，-1为审批被驳回，0为审批进行中，1为审批通过',
    public static final Integer APPROVE_FLOW_STATUS_ADOPT = 1;
    public static final Integer APPROVE_FLOW_STATUS_APPROVING = 0;
    public static final Integer APPROVE_FLOW_STATUS_REFUSE = -1;

    //审批结果，-1为审批被驳回，0为审批进行中，1为审批通过，若null则未开始。
    public static final Integer APPROVE_DETAIL_RESULT_ADOPT = 1;
    public static final Integer APPROVE_DETAIL_RESULT_APPROVING = 0;
    public static final Integer APPROVE_DETAIL_RESULT_REFUSE = -1;
    public static final Integer APPROVE_DETAIL_RESULT_INQUEEN = null;

    //是否标准环节,1为标准环节，0为严格环节
    public static final Integer APPROVE_MODEL_STANDARD = 1;
    public static final Integer APPROVE_MODEL_STRICT = 0;

    public static final Integer APPROVE_NEEDAPPROVE = 1;

    public static final String APPROVE_SUBMIT_SUCCESS = "success";
    public static final String APPROVE_SUBMIT_FAILED = "failed";

    //导出
    public static final String TRANSFER_TYPE_EXPORT = "OPT";
    public static final Integer SELF_EXPORT = 1;
    public static final Integer APPROVE_EXPORT = 3;
    //外发
    public static final String TRANSFER_TYPE_OUT = "OUTCFG";
    //自主外发
    public static final Integer SELF_OUT = 10;
    public static final Integer APPROVE_OUT = 11;

    //策略选项是否应用
    public static final Integer POLICYOPTIONENABLE = 1;
    public static final Integer POLICYOPTIONUNENABLE = 0;

    //策略潜在风险类型:1为屏幕无水印，2为文件外发无水印，3为文件导出无水印，4为无审批
    public static final Integer RISK_OF_SCRNWATERMARK = 1;
    public static final Integer RISK_OF_FILEOUTCFG = 2;
    public static final Integer RISK_OF_FILEOPT = 3;
    public static final Integer RISK_OF_APPRO = 4;

    //非法告警状态：1为已读，0为未读
    public static final Integer ALARM_NOT_READ = 0;
    public static final Integer ALARM_READ = 1;

    //非法告警类型：1为非法登录，2为非法导出，3为非法外发
    public static final Integer ALARM_LOGIN = 1;
    public static final Integer ALARM_OPT = 2;
    public static final Integer ALARM_OUTCFG = 3;

    //默认节点层级
    public static final Integer CURRENT_NODE_LEVEL = 0;
    //默认节点名称
    public static final String CURRENT_NODE = "currentNode";
    public static final String CHILD_NODE = "childNode";

    //日志类型
    public static final String LOGON_LOG_TYPE = "logonLog";
    public static final String CLIENT_LOGON_LOG_TYPE = "clientLogonLog";
    public static final String OPERATION_LOG_TYPE = "operationLog";

    //导入用户信息模板文件名称
    public static final String TEMPLET_FILENAME_CLIENTUSER = "clientUserTemplet.xls";

    public static final String ADMIN_NAME = "system";
    public static final Integer ADMIN_ID = 1;

    public static final Integer DEFAULT_POLICY_ID = 1;

    //预置部门id，顶级部门为1，未分组为2
    public static final Integer SUPER_DEPARTMENT_ID = 1;
    public static final Integer DEPARTMENT_UNKOWN_GROUP = 2;

    public static final Integer CLIENT_LOGON_TYPE_PWD = 1;
    public static final Integer CLIENT_LOGON_TYPE_USBKEY = 2;
    public static final Integer CLIENT_LOGON_TYPE_FINGERPRINT = 101;

    //通过Excel导入用户时，默认的初始化密码
    public static final String DEFAULT_CLIENTUSER_STR = "F1FEA6DCDBE21260484F946A1BDFCB4D46C8253B320910E8AC2206EA302A7751";

    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    public static final String EMPTY_STR = "";

    public static final String DEFAULT_PERMISSION = "1";

    public static final String AUTH_FILE_NAME = "tdpauthorized.auth";
    public static final String EXPORT_AUTH_FILE_NAME = "goldencis.tdp";
    public static final Long LIMIT_TIME = 2 * 1000 * 6l;
    public static final Integer VALIDATE_FLAG = 0;

    /**
     * 以后改
     */
    public static final String PROCJECT_IDENTIFICATION = "vdp";

    public static final String READ_VDP_AUTH_FILE_NAME = "readauth.out";

    public static final String EXECUTE_CLIENT_UPDATE_FILE_NAME = "clientupdate.sh";

    public static final String LONG_TIME_LIMIT = "永久";

    /*public static final String SEC_CODE = "SEC_CODE";
    public static final String SEC_CODE_FLAG = "SEC_CODE_FLAG";
    public static final String SEC_CODE_PARAMETER = "sec_code_parameter";*/
}
