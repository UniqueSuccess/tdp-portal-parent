package cn.goldencis.tdp.approve.service.impl;

import cn.goldencis.tdp.approve.dao.ApproveFlowInfoMapper;
import cn.goldencis.tdp.approve.entity.ApproveFlowInfo;
import cn.goldencis.tdp.approve.entity.ApproveFlowInfoCriteria;
import cn.goldencis.tdp.approve.service.IApproveFlowInfoService;
import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.common.utils.DateUtil;
import cn.goldencis.tdp.common.utils.FileUpload;
import cn.goldencis.tdp.common.utils.SysContext;
import cn.goldencis.tdp.core.utils.PathConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by limingchao on 2018/1/20.
 */
@Service
public class ApproveFlowInfoServiceImpl extends AbstractBaseServiceImpl<ApproveFlowInfo, ApproveFlowInfoCriteria> implements IApproveFlowInfoService {

    @Autowired
    private ApproveFlowInfoMapper mapper;

    @Override
    protected BaseDao<ApproveFlowInfo, ApproveFlowInfoCriteria> getDao() {
        return mapper;
    }

    /**
     * 插入审批流程详细信息
     *
     * @param flowInfo
     */
    @Override
    public void addapproveFlowInfo(ApproveFlowInfo flowInfo) {
        mapper.insertSelective(flowInfo);
    }

    /**
     * 根据流程id获取流程信息
     *
     * @param approveFlowInfoId
     * @return
     */
    @Override
    public ApproveFlowInfo getApproveFlowInfoByFlowId(Integer approveFlowInfoId) {
        ApproveFlowInfoCriteria example = new ApproveFlowInfoCriteria();
        example.createCriteria().andFlowIdEqualTo(approveFlowInfoId);
        List<ApproveFlowInfo> approveFlowInfoList = mapper.selectByExample(example);
        if (approveFlowInfoList != null && approveFlowInfoList.size() > 0) {
            return approveFlowInfoList.get(0);
        }
        return null;
    }

    /**
     * 保存文件，并在流程信息中添加路径
     *
     * @param flowInfo
     * @param approvfile
     */
    @Override
    public void uploadApproveFile(ApproveFlowInfo flowInfo, MultipartFile approvfile) {
        String dirPath = PathConfig.APPROVE_FILEPATH + "/" + DateUtil.getCurrentDate("yyyyMM");
        ServletContext servletContext = SysContext.getRequest().getServletContext();
        String realPath = servletContext.getRealPath(dirPath);

        String fileName = approvfile.getOriginalFilename();
        String filePath = realPath + "/" + fileName;

        File file = new File(filePath);
        while (file.exists()) {
            fileName = UUID.randomUUID().toString() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
            filePath = realPath + "/" + fileName;
            file = new File(filePath);
        }

        FileUpload fileUploader = new FileUpload();
        fileUploader.uploadFile(approvfile, realPath, fileName);
        flowInfo.setFilePath(dirPath + "/" + fileName);
    }
}
