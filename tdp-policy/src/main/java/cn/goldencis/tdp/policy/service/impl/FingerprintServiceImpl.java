package cn.goldencis.tdp.policy.service.impl;

import cn.goldencis.tdp.common.dao.BaseDao;
import cn.goldencis.tdp.common.service.impl.AbstractBaseServiceImpl;
import cn.goldencis.tdp.policy.dao.FingerprintDOMapper;
import cn.goldencis.tdp.policy.entity.FingerprintDO;
import cn.goldencis.tdp.policy.entity.FingerprintDOCriteria;
import cn.goldencis.tdp.policy.service.IFingerprintService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import zk.jni.JavaToBiokey;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by limingchao on 2018/5/18.
 */
@Service
public class FingerprintServiceImpl extends AbstractBaseServiceImpl<FingerprintDO, FingerprintDOCriteria> implements IFingerprintService {

    @Autowired
    private FingerprintDOMapper mapper;

    @Override
    protected BaseDao<FingerprintDO, FingerprintDOCriteria> getDao() {
        return mapper;
    }

    private List<FingerprintDO> regTemplateList;

    @PostConstruct
    public void init() {
        refreshFingerprintRegCache();
    }

    @Override
    public List<FingerprintDO> parseFingerprintParam(String fingerprintParam) {
        List<FingerprintDO> fingerList = null;
        //将指纹参数先转化为json数组
        JSONArray jsonArray = JSONArray.parseArray(fingerprintParam);

        if (jsonArray != null && jsonArray.size() > 0) {
            fingerList = new ArrayList<>();
            //遍历json数组，依次将内容转化为指纹对象，并添加到集合中
            for (int i = 0; i < jsonArray.size(); i++) {
                FingerprintDO fingerprint = jsonArray.getObject(i, FingerprintDO.class);
                fingerList.add(fingerprint);
            }
        }
        return fingerList;
    }

    @Transactional
    @Override
    public boolean checkFingerprintMaxCount(String guid, List<FingerprintDO> fingerprintList) {
        //查询该账户已有指纹数量
        FingerprintDOCriteria example = new FingerprintDOCriteria();
        example.createCriteria().andClientUserGuidEqualTo(guid);
        int num = (int) mapper.countByExample(example);

        List<FingerprintDO> add = new ArrayList<>();
        List<Integer> delete = new ArrayList<>();

        this.splitFingerprintInGroup(guid, fingerprintList, add, delete);

        //增加新添加指纹数量
        if (add != null && add.size() > 0) {
            num += add.size();
        }

        //减掉需要删除的指纹数量
        if (delete != null && delete.size() > 0) {
            num -= delete.size();
        }

        //已有指纹数加新添加数量，小于等于3个为符合返回true，否则。
        return (num <= 3) ? true : false;
    }

    @Transactional
    @Override
    public void addFingerprintListForClientUser(String guid, List<FingerprintDO> fingerprintList) {
        for (FingerprintDO fingerprint : fingerprintList) {
            fingerprint.setClientUserGuid(guid);
            fingerprint.setCreateTime(new Date());
            mapper.insert(fingerprint);
        }
    }

    @Transactional
    @Override
    public void updateFingerprintListForClientUser(List<FingerprintDO> fingerprintList) {
        for (FingerprintDO fingerprint : fingerprintList) {
            mapper.updateByPrimaryKey(fingerprint);
        }
    }

    @Transactional
    @Override
    public void modifyFingerprintListForClientUser(String guid, List<FingerprintDO> fingerprintList) {
        List<FingerprintDO> add = new ArrayList<>();
        List<Integer> delete = new ArrayList<>();

        this.splitFingerprintInGroup(guid, fingerprintList, add, delete);

        //新添加指纹
        if (add != null && add.size() > 0) {
            this.addFingerprintListForClientUser(guid, add);
        }

        //如果存在需要删除的指纹，进行删除
        if (delete != null && delete.size() > 0) {
            this.deleteFingerprintByIdList(delete);
        }

    }

    @Override
    @Transactional
    public void deleteFingerprintByClientUserGuid(String guid) {
        FingerprintDOCriteria example = new FingerprintDOCriteria();
        example.createCriteria().andClientUserGuidEqualTo(guid);
        mapper.deleteByExample(example);
    }

    @Transactional
    @Override
    public void deleteFingerprintByIdList(List<Integer> delete) {
        FingerprintDOCriteria example = new FingerprintDOCriteria();
        example.createCriteria().andIdIn(delete);
        mapper.deleteByExample(example);
    }

    @Override
    public String verifyFingerprint(String verTemplate) {

        for (FingerprintDO regTemplate : regTemplateList) {
            if (JavaToBiokey.NativeToProcess(regTemplate.getFingerprintPwd(), verTemplate)) {
                return regTemplate.getClientUserGuid();
            }
        }
        return null;
    }

    @Override
    public List<FingerprintDO> getFingerprintListByCilentUserGuid(String guid) {
        FingerprintDOCriteria example = new FingerprintDOCriteria();
        example.createCriteria().andClientUserGuidEqualTo(guid);
        //未查询指纹密码信息
        return mapper.selectByExample(example);
    }

    @Override
    public boolean refreshFingerprintRegCache() {
        try {
            FingerprintDOCriteria example = new FingerprintDOCriteria();
            regTemplateList = mapper.selectByExampleWithBLOBs(example);
            return true;
        } catch (Exception e) {
        }
        return false;
    }


    public void splitFingerprintInGroup(String guid, List<FingerprintDO> fingerprintList, List<FingerprintDO> add, List<Integer> delete) {
        //查询数据库中该用户已经存在的指纹
        FingerprintDOCriteria example = new FingerprintDOCriteria();
        example.createCriteria().andClientUserGuidEqualTo(guid);
        //未查询指纹密码信息
        List<FingerprintDO> inDB = mapper.selectByExample(example);
        //默认将这些指纹的id先添加待删除集合中
        for (FingerprintDO fingerprint : inDB) {
            delete.add(fingerprint.getId());
        }

        for (FingerprintDO fingerprint : fingerprintList) {
            //没有id的，认为是新添加指纹
            if (StringUtils.isEmpty(fingerprint.getId())) {
                add.add(fingerprint);
            } else {
                //有id，则为已经存在的指纹，将其从待删除集合中剔除。
                if (delete.contains(fingerprint.getId())) {
                    delete.remove(fingerprint.getId());
                }
            }
        }
    }
}
