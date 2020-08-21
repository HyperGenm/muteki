package com.weiziplus.muteki.common.async;

import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.models.SysUserLog;
import com.weiziplus.muteki.common.models.SysUserLogin;
import com.weiziplus.muteki.common.models.UserLog;
import com.weiziplus.muteki.common.models.UserLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步保存日志
 *
 * @author wanglongwei
 * @date 2020/08/13 17/57
 */
@Slf4j
@EnableAsync
@Configuration
public class LogAsync extends BaseService {


    /**
     * 存放日志
     *
     * @return
     */
    @Bean("log")
    public Executor sysError() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //线程池创建时候初始化的线程数
        executor.setCorePoolSize(2);
        //线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(5);
        //用来缓冲执行任务的队列
        executor.setQueueCapacity(50);
        //允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        //线程池名的前缀
        executor.setThreadNamePrefix("async-log-");
        //在调用者线程中运行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 将操作日志异步存储到数据库
     *
     * @param sysUserLog
     */
    @Async("log")
    public void saveLog(SysUserLog sysUserLog) {
        if (null == sysUserLog) {
            return;
        }
        try {
            baseInsert(sysUserLog);
        } catch (Exception e) {
            log.warn("将系统用户操作日志异步存储到数据库出错，详情:" + e);
        }
    }

    /**
     * 将操作日志异步存储到数据库
     *
     * @param userLog
     */
    @Async("log")
    public void saveLog(UserLog userLog) {
        if (null == userLog) {
            return;
        }
        try {
            baseInsert(userLog);
        } catch (Exception e) {
            log.warn("将用户操作日志异步存储到数据库出错，详情:" + e);
        }
    }

    /**
     * 将登录日志异步存储到数据库
     *
     * @param sysUserLogin
     */
    @Async("log")
    public void saveLoginLog(SysUserLogin sysUserLogin) {
        if (null == sysUserLogin) {
            return;
        }
        try {
            baseInsert(sysUserLogin);
        } catch (Exception e) {
            log.warn("将系统用户登录日志异步存储到数据库出错，详情:" + e);
        }
    }

    /**
     * 将登录日志异步存储到数据库
     *
     * @param userLogin
     */
    @Async("log")
    public void saveLoginLog(UserLogin userLogin) {
        if (null == userLogin) {
            return;
        }
        try {
            baseInsert(userLogin);
        } catch (Exception e) {
            log.warn("将用户登录日志异步存储到数据库出错，详情:" + e);
        }
    }

}
