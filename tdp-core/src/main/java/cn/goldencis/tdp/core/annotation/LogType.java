package cn.goldencis.tdp.core.annotation;

/**
 * 日志类型
 * @author liuwp
 * @date 2017年11月14日 上午11:31:34
 * @version 1.0.0-SNAPSHOT
 */
public enum LogType {
    LOGIN(0), SELECT(1), INSERT(2), UPDATE(3), DELETE(4), CLIENT_LOGIN(8), OTHER(9);
    private int type;

    LogType(int type) {
        this.type = type;
    }

    public int value() {
        return type;
    }
}
