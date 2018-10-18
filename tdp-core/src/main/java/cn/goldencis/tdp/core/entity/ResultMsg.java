package cn.goldencis.tdp.core.entity;

import cn.goldencis.tdp.core.constants.ConstantsDto;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by mengll on 2017/10/24 0024.
 */
public class ResultMsg {

    private Integer resultCode = ConstantsDto.RESULT_CODE_FALSE;
    private String resultMsg;
    private Object data;
    private Integer recordsTotal;
    private Integer exportlength;
    private Integer exportstart;
    private Integer recordsFiltered;

    private Integer total;
    private Object rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    public ResultMsg() {
    }

    public ResultMsg(Integer resultCode, String resultMsg, Object data) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.data = data;
    }

    public ResultMsg(Object data) {
        this.resultCode = ConstantsDto.RESULT_CODE_TRUE;
        this.resultMsg = "OK";
        this.data = data;
    }

    public static ResultMsg ok() {
        return new ResultMsg(null);
    }

    public static ResultMsg ok(Object data) {
        return new ResultMsg(data);
    }

    public static ResultMsg build(Integer resultCode, String resultMsg) {
        return new ResultMsg(resultCode, resultMsg, null);
    }

    public static ResultMsg build(Integer resultCode, String resultMsg, Object data) {
        return new ResultMsg(resultCode, resultMsg, data);
    }

    public Integer getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Integer recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public Integer getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Integer recordsTotal) {
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = this.recordsTotal;
    }

    public Integer getExportlength() {
        return exportlength;
    }

    public void setExportlength(Integer exportlength) {
        this.exportlength = exportlength;
    }

    public Integer getExportstart() {
        return exportstart;
    }

    public void setExportstart(Integer exportstart) {
        this.exportstart = exportstart;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object resultObj) {
        this.data = resultObj;
    }

    /**
     * 将json结果集转化为ResultMsg对象
     * jsonNode 是解析树形结构的  类似于解析DOM
     *  {"age" : 29,
     *   "messages" : [ "msg 1", "msg 2", "msg 3" ],
     *	 "name" : "mkyong"
     *	 }
     * @param jsonData json数据
     * @param clazz TaotaoResult中的object类型
     * @return
     */
    public static ResultMsg formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return JSON.parseObject(jsonData, ResultMsg.class);
            }
            JSONObject jsonResult = JSONObject.parseObject(jsonData);
            JSONObject data = jsonResult.getJSONObject("data");

            Object obj = null;
            if (clazz != null) {
                if (!data.isEmpty()) {
                    obj = JSON.parseObject(data.toJSONString(), clazz);

                }
            }
            return build(jsonResult.getInteger("resultCode"), jsonResult.getString("resultMsg"), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 没有object对象的转化
     *
     * @param json
     * @return
     */
    public static ResultMsg format(String json) {
        try {
            return JSON.parseObject(json, ResultMsg.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Object是集合转化
     *
     * @param jsonData json数据
     * @param clazz 集合中的类型
     * @return
     */
    public static ResultMsg formatToList(String jsonData, Class<?> clazz) {
        try {
            JSONObject jsonResult = JSONObject.parseObject(jsonData);
            JSONArray dataArr = jsonResult.getJSONArray("data");

            Object obj = null;
            if (!dataArr.isEmpty()) {
                obj = JSON.parseArray(dataArr.toJSONString(), clazz);
            }

            return build(jsonResult.getInteger("resultCode"), jsonResult.getString("resultMsg"), obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "ResultMsg{" + "resultCode=" + resultCode + ", resultMsg='" + resultMsg + '\'' + ", data=" + data + '}';
    }
}
