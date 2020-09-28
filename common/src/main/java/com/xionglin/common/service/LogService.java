package com.xionglin.common.service;

import com.xionglin.common.domain.SysOperLog;
import com.xionglin.common.mapper.LogUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LogService {
    @Autowired
    private LogUserMapper logMapper ;

    public void logSave(SysOperLog operLog){
        logMapper.saveLog(operLog);

    }
}
