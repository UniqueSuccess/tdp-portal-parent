package cn.goldencis.tdp.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.goldencis.tdp.core.dao.SystemValidateMapper;
import cn.goldencis.tdp.core.entity.SystemValidate;
import cn.goldencis.tdp.core.service.ISystemValidateService;

@Service
public class SystemValidateServiceImpl implements ISystemValidateService {

    @Autowired
    private SystemValidateMapper systemValidateMapper;

    @Override
    public List<SystemValidate> selectSystemValidateList() {
        return systemValidateMapper.selectByExample(null);
    }
}
