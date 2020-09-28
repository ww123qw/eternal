package com.xionglin.common.mapper;

import com.xionglin.common.domain.SysOperLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LogUserMapper {
    @Insert("<script>insert into log(id,createtime" +
            "<if test='operLog.requestMethod !=null'>,requestMethod</if>" +
            "<if test='operLog.operIp !=null'>,ip</if>" +
            "<if test='operLog.operUserId !=null'>,applet_user_id</if>" +
            "<if test='operLog.operName !=null'>,name</if>" +
            "<if test='operLog.operUrl !=null'>,pathurl</if>" +
            "<if test='operLog.jsonResult !=null'>,result</if>" +
            "<if test='operLog.status !=null'>,status</if>" +
            "<if test='operLog.operParam !=null'>,param</if>" +
            "<if test='operLog.title !=null'>,title</if>" +
            "<if test='operLog.businessType !=null'>,businessType</if>) " +
            "values (#{operLog.operId},now()" +
            "<if test='operLog.requestMethod !=null'>,#{operLog.requestMethod}</if>" +
            "<if test='operLog.operIp !=null'>,#{operLog.operIp}</if>" +
            "<if test='operLog.operUserId !=null'>,#{operLog.operUserId}</if>" +
            "<if test='operLog.operName !=null'>,#{operLog.operName}</if>" +
            "<if test='operLog.operUrl !=null'>,#{operLog.operUrl}</if>" +
            "<if test='operLog.jsonResult !=null'>,#{operLog.jsonResult}</if>" +
            "<if test='operLog.status !=null'>,#{operLog.status}</if>" +
            "<if test='operLog.operParam !=null'>,#{operLog.operParam}</if>" +
            "<if test='operLog.title !=null'>,#{operLog.title}</if>" +
            "<if test='operLog.businessType !=null'>,#{operLog.businessType}</if>)</script>")
    void saveLog(@Param("operLog") SysOperLog operLog);
}
