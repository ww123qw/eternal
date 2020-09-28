package com.xionglin.common.aspectj;


import com.alibaba.fastjson.JSON;
import com.xionglin.common.annotation.Log;
import com.xionglin.common.domain.SysOperLog;
import com.xionglin.common.service.LogService;
import com.xionglin.common.utils.IpUtils;
import net.sf.json.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 操作日志记录处理
 * 
 * @author xionglin
 */
@Aspect
@Component
public class LogAspect
{
    @Autowired
    private LogService logService ;
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    // 配置织入点
    @Pointcut("@annotation(com.xionglin.common.annotation.Log)")
    public void logPointCut()
    {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult)
    {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     * 
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e)
    {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult)
    {
        try
        {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null)
            {
                return;
            }
            SysOperLog operLog = new SysOperLog();

            //获取http请求
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
             HttpServletRequest request =attributes.getRequest();

            // 获取当前的用户
           /* if(null !=SessionManager.getUserSession().getLoginInfo()){
                LoginInfoVo loginingo = SessionManager.getUserSession().getLoginInfo();
                operLog.setOperUserId(loginingo.getAppletUserId());
            }
            if(null !=SessionManager.getUserSession().getAdminLoginVo()){
                AdminLoginVo loginingo = SessionManager.getUserSession().getAdminLoginVo();
                operLog.setOperUserId(loginingo.getAdmin_id());
            }*/


            //获取ip
            String ip = IpUtils.getIpAddr(request);
            operLog.setOperIp(ip);

            //返回状态
//           String status = JSONObject.fromObject(jsonResult).getString("status");
//            operLog.setStatus(Integer.valueOf(status));

            // 返回参数
            operLog.setJsonResult(JSON.toJSONString(jsonResult));

            // 设置请求方式
            operLog.setRequestMethod(request.getMethod());

            //访问路径
            operLog.setOperUrl(request.getRequestURI());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operLog);

            //id
            operLog.setOperId(UUID.randomUUID().toString());
           // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operLog);
            logService.logSave(operLog);

        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * 
     * @param log 日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperLog operLog) throws Exception
    {

        operLog.setTitle(log.title());

    }

    /**
     * 获取请求的参数，放到log中
     * 
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, SysOperLog operLog) throws Exception
    {
        String requestMethod = operLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))
        {
            String params = argsArrayToString(joinPoint.getArgs());
            operLog.setOperParam(params);
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray)
    {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0)
        {
            for (int i = 0; i < paramsArray.length; i++)
            {
                if (!isFilterObject(paramsArray[i]))
                {
                    JSONObject jsonObj = JSONObject.fromObject(paramsArray[i]);
                    params += jsonObj.toString() + " ";
                }
                if(paramsArray[i] instanceof String){
                    params +=paramsArray[i]+" ";
                }

            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     * 
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o)
    {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse || o instanceof BindingResult ||o instanceof String;
    }
}
