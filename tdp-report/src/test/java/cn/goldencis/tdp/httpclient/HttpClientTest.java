package cn.goldencis.tdp.httpclient;

import cn.goldencis.tdp.common.utils.HttpClientUtil;
import cn.goldencis.tdp.core.constants.ConstantsDto;
import cn.goldencis.tdp.core.entity.ChildNodeDO;
import cn.goldencis.tdp.core.entity.ResultMsg;
import cn.goldencis.tdp.report.entity.VideoTransferLogDO;
import cn.goldencis.tdp.report.service.impl.VideoTransferLogServiceImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by limingchao on 2018/7/18.
 */
public class HttpClientTest {

    @Test
    public void testPost() {
        String url = "http://10.10.16.143/tdp/report/uploadVideoTransferLogsWithAttachment";
        Map<String, String> param = new HashMap<>();
        param.put("start", "2018-07-26 15:55:22");
        param.put("end", "2018-08-26 15:55:22");

        String res = HttpClientUtil.doPost(url, param);
        System.out.println(res);
    }

    @Test
    public void testPostJson() {
        String url = "http://10.10.16.144/tdp/clientUser/clientUserLogin";

        String json = "{\"logontype\":103,\"pcguid\":\"9057546B-6963-4436-B840-9C7F51821E45\",\"computername\":\"WIN-K05MI0RRTHN\",\"mac\":\"00-0C-29-9D-BE-58\"}";
        String res = HttpClientUtil.doPostJson(url, json);

        System.out.println(res);
    }

    @Test
    public void testPostParam() {
        Map<String, String> param = new HashMap<>();
        param.put("start", "2018-07-01 09:03:26");
        param.put("end", "2018-07-26 15:55:22");
        String res = HttpClientUtil.doPost("http://10.10.16.140/tdp/report/uploadVideoTransferLogsWithAttachment", param);

        System.out.println(res);
    }

    @Test
    public void testPojo() {
        ChildNodeDO node = new ChildNodeDO();
        node.setId(2);
        node.setNodeIp("http://10.10.16.140");
//        node.setNodeName("服务器");
//        node.setCreateTime(null);
        ResultMsg resultMsg = ResultMsg.ok(node);


        String jsonString = JSONObject.toJSONString(resultMsg);
        System.out.println(jsonString);

        ResultMsg jsonres = JSON.parseObject(jsonString, ResultMsg.class);

        System.out.println(jsonres.toString());
    }

    @Test
    public void testResultFormat() {
        String resStr = "{\"resultCode\":1,\"resultMsg\":\"上传视频流转日志成功！\",\"data\":[{\"id\":1,\"truename\":\"lilili\",\"username\":\"li\",\"userguid\":\"6dc237b2-7c90-4729-b274-893cce3ba28c\",\"departmentId\":1,\"departmentName\":\"顶级部门\",\"devunique\":\"90AAEA56-2C29-4020-BE59-B98EE4F163BD\",\"extradata\":\"{\\\"computername\\\":\\\"PC-20170419HXZR\\\",\\\"dataType\\\":\\\"tGopLog\\\",\\\"depid\\\":\\\"1\\\",\\\"depname\\\":\\\"顶级部门\\\",\\\"devunique\\\":\\\"90AAEA56-2C29-4020-BE59-B98EE4F163BD\\\",\\\"extradata\\\":{\\\"nIsShow\\\":0,\\\"reason\\\":\\\"无需审批\\\",\\\"recv\\\":\\\"li\\\",\\\"scrnWaterMark\\\":1,\\\"scrnWaterMarkContent\\\":\\\"\\\",\\\"watermarkid\\\":\\\"\\\"},\\\"fftype\\\":1,\\\"filepath\\\":\\\"1061桂花路佳地园小区口_0411120751.bmp\\\",\\\"time\\\":\\\"2018-07-25 15:42:48\\\",\\\"tranunique\\\":\\\"\\\",\\\"truename\\\":\\\"lilili\\\",\\\"username\\\":\\\"li\\\",\\\"usrunique\\\":\\\"6dc237b2-7c90-4729-b274-893cce3ba28c\\\"}\",\"filePath\":\"/resource/videotransferlog/201807/1AAF767A-CBEC-4A2E-913C-1B89B4DABC21.jpg\",\"fileName\":\"1061桂花路佳地园小区口_0411120751.bmp\",\"receiver\":\"li\",\"transferTime\":\"2018-07-25 15:42:48\",\"tranunique\":\"\",\"fftype\":1,\"primaryKey\":\"1\"},{\"id\":2,\"truename\":\"lilili\",\"username\":\"li\",\"userguid\":\"6dc237b2-7c90-4729-b274-893cce3ba28c\",\"departmentId\":1,\"departmentName\":\"顶级部门\",\"devunique\":\"90AAEA56-2C29-4020-BE59-B98EE4F163BD\",\"extradata\":\"{\\\"computername\\\":\\\"PC-20170419HXZR\\\",\\\"dataType\\\":\\\"tGopLog\\\",\\\"depid\\\":\\\"1\\\",\\\"depname\\\":\\\"顶级部门\\\",\\\"devunique\\\":\\\"90AAEA56-2C29-4020-BE59-B98EE4F163BD\\\",\\\"extradata\\\":{\\\"nIsShow\\\":0,\\\"reason\\\":\\\"无需审批\\\",\\\"recv\\\":\\\"li\\\",\\\"scrnWaterMark\\\":1,\\\"scrnWaterMarkContent\\\":\\\"\\\",\\\"watermarkid\\\":\\\"\\\"},\\\"fftype\\\":1,\\\"filepath\\\":\\\"1061桂花路佳地园小区口_0411120751.bmp\\\",\\\"time\\\":\\\"2018-07-25 15:59:23\\\",\\\"tranunique\\\":\\\"\\\",\\\"truename\\\":\\\"lilili\\\",\\\"username\\\":\\\"li\\\",\\\"usrunique\\\":\\\"6dc237b2-7c90-4729-b274-893cce3ba28c\\\"}\",\"filePath\":\"/resource/videotransferlog/201807/52A1519C-7A73-4564-93DC-E7400587BD5A.jpg\",\"fileName\":\"1061桂花路佳地园小区口_0411120751.bmp\",\"receiver\":\"li\",\"transferTime\":\"2018-07-25 15:59:23\",\"tranunique\":\"\",\"fftype\":1,\"primaryKey\":\"2\"},{\"id\":3,\"truename\":\"lilili\",\"username\":\"li\",\"userguid\":\"6dc237b2-7c90-4729-b274-893cce3ba28c\",\"departmentId\":1,\"departmentName\":\"顶级部门\",\"devunique\":\"90AAEA56-2C29-4020-BE59-B98EE4F163BD\",\"extradata\":\"{\\\"computername\\\":\\\"PC-20170419HXZR\\\",\\\"dataType\\\":\\\"tGopLog\\\",\\\"depid\\\":\\\"1\\\",\\\"depname\\\":\\\"顶级部门\\\",\\\"devunique\\\":\\\"90AAEA56-2C29-4020-BE59-B98EE4F163BD\\\",\\\"extradata\\\":{\\\"nIsShow\\\":0,\\\"reason\\\":\\\"无需审批\\\",\\\"recv\\\":\\\"三生三世\\\",\\\"scrnWaterMark\\\":1,\\\"scrnWaterMarkContent\\\":\\\"\\\",\\\"watermarkid\\\":\\\"\\\"},\\\"fftype\\\":1,\\\"filepath\\\":\\\"1061桂花路佳地园小区口_0411120751.bmp\\\",\\\"time\\\":\\\"2018-07-25 16:01:05\\\",\\\"tranunique\\\":\\\"\\\",\\\"truename\\\":\\\"lilili\\\",\\\"username\\\":\\\"li\\\",\\\"usrunique\\\":\\\"6dc237b2-7c90-4729-b274-893cce3ba28c\\\"}\",\"filePath\":\"/resource/videotransferlog/201807/840F1DB9-B183-428A-8D26-62A5EF5520A9.jpg\",\"fileName\":\"1061桂花路佳地园小区口_0411120751.bmp\",\"receiver\":\"三生三世\",\"transferTime\":\"2018-07-25 16:01:05\",\"tranunique\":\"\",\"fftype\":1,\"primaryKey\":\"3\"},{\"id\":4,\"truename\":\"lilili\",\"username\":\"li\",\"userguid\":\"6dc237b2-7c90-4729-b274-893cce3ba28c\",\"departmentId\":1,\"departmentName\":\"顶级部门\",\"devunique\":\"90AAEA56-2C29-4020-BE59-B98EE4F163BD\",\"extradata\":\"{\\\"computername\\\":\\\"PC-20170419HXZR\\\",\\\"dataType\\\":\\\"tGopLog\\\",\\\"depid\\\":\\\"1\\\",\\\"depname\\\":\\\"顶级部门\\\",\\\"devunique\\\":\\\"90AAEA56-2C29-4020-BE59-B98EE4F163BD\\\",\\\"extradata\\\":{\\\"alpha\\\":125,\\\"autoDelete\\\":1,\\\"bScroll\\\":1,\\\"beginTime\\\":\\\"2018-07-25\\\",\\\"color\\\":16777215,\\\"direction\\\":0,\\\"editable\\\":0,\\\"endTime\\\":\\\"2018-07-31\\\",\\\"fileCount\\\":1,\\\"forbidCopy\\\":1,\\\"forbidDrag\\\":0,\\\"forbidPrint\\\":0,\\\"forbidScreenShot\\\":1,\\\"location\\\":0,\\\"machineCode\\\":\\\"\\\",\\\"nIsShow\\\":1,\\\"openCount\\\":0,\\\"password\\\":\\\"\\\",\\\"prntWaterMark\\\":0,\\\"prntWaterMarkContent\\\":\\\"\\\",\\\"reason\\\":\\\"\\\",\\\"recv\\\":\\\"顶顶顶顶\\\",\\\"scrnWaterMark\\\":1,\\\"scrnWaterMarkContent\\\":\\\"顶级部门 lilili PC-20170419HXZR 127.0.0.1 00-E0-4C-32-A4-B7 2018-07-25  顶顶顶顶\\\",\\\"useNetTime\\\":0,\\\"watermarkid\\\":\\\"s000000001000010000100100\\\"},\\\"fftype\\\":10,\\\"filepath\\\":\\\"1061桂花路佳地园小区口_0411120751.bmp\\\",\\\"time\\\":\\\"2018-07-25 16:01:22\\\",\\\"tranunique\\\":\\\"\\\",\\\"truename\\\":\\\"lilili\\\",\\\"username\\\":\\\"li\\\",\\\"usrunique\\\":\\\"6dc237b2-7c90-4729-b274-893cce3ba28c\\\"}\",\"filePath\":\"/resource/videotransferlog/201807/8fcf1117.jpg\",\"fileName\":\"1061桂花路佳地园小区口_0411120751.bmp\",\"receiver\":\"顶顶顶顶\",\"transferTime\":\"2018-07-25 16:01:22\",\"tranunique\":\"\",\"fftype\":10,\"primaryKey\":\"4\"},{\"id\":5,\"truename\":\"lilili\",\"username\":\"li\",\"userguid\":\"6dc237b2-7c90-4729-b274-893cce3ba28c\",\"departmentId\":1,\"departmentName\":\"顶级部门\",\"devunique\":\"90AAEA56-2C29-4020-BE59-B98EE4F163BD\",\"extradata\":\"{\\\"computername\\\":\\\"PC-20170419HXZR\\\",\\\"dataType\\\":\\\"tGopLog\\\",\\\"depid\\\":\\\"1\\\",\\\"depname\\\":\\\"顶级部门\\\",\\\"devunique\\\":\\\"90AAEA56-2C29-4020-BE59-B98EE4F163BD\\\",\\\"extradata\\\":{\\\"alpha\\\":125,\\\"autoDelete\\\":1,\\\"bScroll\\\":1,\\\"beginTime\\\":\\\"2018-07-25\\\",\\\"color\\\":16777215,\\\"direction\\\":0,\\\"editable\\\":0,\\\"endTime\\\":\\\"2018-07-31\\\",\\\"fileCount\\\":1,\\\"forbidCopy\\\":1,\\\"forbidDrag\\\":0,\\\"forbidPrint\\\":0,\\\"forbidScreenShot\\\":1,\\\"location\\\":0,\\\"machineCode\\\":\\\"\\\",\\\"nIsShow\\\":1,\\\"openCount\\\":0,\\\"password\\\":\\\"\\\",\\\"prntWaterMark\\\":0,\\\"prntWaterMarkContent\\\":\\\"\\\",\\\"reason\\\":\\\"\\\",\\\"recv\\\":\\\"何润东\\\",\\\"scrnWaterMark\\\":1,\\\"scrnWaterMarkContent\\\":\\\"顶级部门 lilili PC-20170419HXZR 127.0.0.1 00-E0-4C-32-A4-B7 2018-07-25  何润东\\\",\\\"useNetTime\\\":0,\\\"watermarkid\\\":\\\"s000000001000010000100111\\\"},\\\"fftype\\\":10,\\\"filepath\\\":\\\"1061桂花路佳地园小区口_0411120751.bmp\\\",\\\"time\\\":\\\"2018-07-25 16:01:38\\\",\\\"tranunique\\\":\\\"\\\",\\\"truename\\\":\\\"lilili\\\",\\\"username\\\":\\\"li\\\",\\\"usrunique\\\":\\\"6dc237b2-7c90-4729-b274-893cce3ba28c\\\"}\",\"filePath\":\"/resource/videotransferlog/201807/206124ca.jpg\",\"fileName\":\"1061桂花路佳地园小区口_0411120751.bmp\",\"receiver\":\"何润东\",\"transferTime\":\"2018-07-25 16:01:38\",\"tranunique\":\"\",\"fftype\":10,\"primaryKey\":\"5\"},{\"id\":6,\"truename\":\"lilili\",\"username\":\"li\",\"userguid\":\"6dc237b2-7c90-4729-b274-893cce3ba28c\",\"departmentId\":1,\"departmentName\":\"顶级部门\",\"devunique\":\"90AAEA56-2C29-4020-BE59-B98EE4F163BD\",\"extradata\":\"{\\\"computername\\\":\\\"PC-20170419HXZR\\\",\\\"dataType\\\":\\\"tGopLog\\\",\\\"depid\\\":\\\"1\\\",\\\"depname\\\":\\\"顶级部门\\\",\\\"devunique\\\":\\\"90AAEA56-2C29-4020-BE59-B98EE4F163BD\\\",\\\"extradata\\\":{\\\"alpha\\\":125,\\\"autoDelete\\\":1,\\\"bScroll\\\":1,\\\"beginTime\\\":\\\"2018-07-25\\\",\\\"color\\\":16777215,\\\"direction\\\":0,\\\"editable\\\":0,\\\"endTime\\\":\\\"2018-07-31\\\",\\\"fileCount\\\":1,\\\"forbidCopy\\\":1,\\\"forbidDrag\\\":0,\\\"forbidPrint\\\":0,\\\"forbidScreenShot\\\":1,\\\"location\\\":0,\\\"machineCode\\\":\\\"\\\",\\\"nIsShow\\\":1,\\\"openCount\\\":0,\\\"password\\\":\\\"\\\",\\\"prntWaterMark\\\":0,\\\"prntWaterMarkContent\\\":\\\"\\\",\\\"reason\\\":\\\"\\\",\\\"recv\\\":\\\"客户端\\\",\\\"scrnWaterMark\\\":1,\\\"scrnWaterMarkContent\\\":\\\"顶级部门 lilili PC-20170419HXZR 127.0.0.1 00-E0-4C-32-A4-B7 2018-07-25  客户端\\\",\\\"useNetTime\\\":0,\\\"watermarkid\\\":\\\"s000000001000010000101000\\\"},\\\"fftype\\\":10,\\\"filepath\\\":\\\"1061桂花路佳地园小区口_0411120751.bmp\\\",\\\"time\\\":\\\"2018-07-25 16:01:54\\\",\\\"tranunique\\\":\\\"\\\",\\\"truename\\\":\\\"lilili\\\",\\\"username\\\":\\\"li\\\",\\\"usrunique\\\":\\\"6dc237b2-7c90-4729-b274-893cce3ba28c\\\"}\",\"filePath\":\"/resource/videotransferlog/201807/76cd8b71.jpg\",\"fileName\":\"1061桂花路佳地园小区口_0411120751.bmp\",\"receiver\":\"客户端\",\"transferTime\":\"2018-07-25 16:01:54\",\"tranunique\":\"\",\"fftype\":10,\"primaryKey\":\"6\"}]}";

        ResultMsg resultMsg = ResultMsg.formatToList(resStr, VideoTransferLogDO.class);


        if (ConstantsDto.RESULT_CODE_TRUE == resultMsg.getResultCode().intValue()) {
            Object data = resultMsg.getData();

            if (data instanceof List) {
                List dataList = (List) data;
                for (int i = 0; i < dataList.size(); i++) {
                    Class<?> aClass = dataList.get(i).getClass();
                    System.out.println(aClass);
                }



            }



            /*List<VideoTransferLogDO> videoTransferLogList = (List<VideoTransferLogDO>) data;
            for (VideoTransferLogDO videoTransferLog : videoTransferLogList) {
                System.out.println(videoTransferLog.getFileName());
            }*/


        }
    }

    @Test
    public void testResultPojo() {
        String resStr = "{\"resultCode\":1,\"resultMsg\":\"上传视频流转日志成功！\",\"data\":{}}";
        ResultMsg resultMsg = ResultMsg.formatToPojo(resStr, VideoTransferLogDO.class);

        System.out.println(resultMsg);

        VideoTransferLogDO data = JSONObject.parseObject(resStr).getObject("data", VideoTransferLogDO.class);
        System.out.println(data);
    }

    @Test
    public void testResultWithoutPojo() {
        String resStr = "{\"resultCode\":1,\"resultMsg\":\"上传视频流转日志成功！\"}";
        ResultMsg resultMsg = ResultMsg.format(resStr);

        System.out.println(resultMsg);

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        String methodName = stackTrace[1].getMethodName();
        System.out.println(methodName);
//        for (int i = 0; i < 5; i++) {
//        }
    }

    @Test
    public void testFileTrans() throws IOException {
        FileUtils.copyURLToFile(new URL("http://10.10.16.140/tdp/resource/videotransferlog/currentNode/201807/300603a3.jpg"), new File("C:\\Users\\Administrator\\Desktop\\300603a3.jpg"), 5000, 20000);
    }

    @Test
    public void testReflect() throws NoSuchMethodException {
        Method method = VideoTransferLogServiceImpl.class.getMethod("jobService", String.class);
        System.out.println(method);
    }

    @Test
    public void test() {
        long time = new Date(0, 1, 1).getTime();
        System.out.println(time);
        Date date = new Date(time);
        System.out.println(date);

        System.out.println(new Date().getTime());
        System.out.println(new Date(0));
    }
}