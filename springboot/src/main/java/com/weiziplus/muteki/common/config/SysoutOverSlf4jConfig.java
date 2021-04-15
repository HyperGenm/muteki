package com.weiziplus.muteki.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

/**
 * @author wanglongwei
 * @date 2021/04/08 11/08
 */
@Component
public class SysoutOverSlf4jConfig implements CommandLineRunner {

    /**
     * 当前项目运行环境
     */
    @Value("${spring.profiles:}")
    private String profiles;

    /**
     * 本地环境
     */
    private final static String DEV_PROFILES = "dev";

    @Override
    public void run(String... args) {
        //将System.out.print()输出保存到日志文件
        //非本地环境，System.out.print()使用slf4j打印，便于本地调试
        if (!DEV_PROFILES.equals(profiles)) {
            SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
        }

    }

}
