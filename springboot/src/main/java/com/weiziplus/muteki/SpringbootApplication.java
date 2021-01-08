package com.weiziplus.muteki;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

/**
 * springboot启动类
 *
 * @author wanglongwei
 * @date 2019/5/6 15:50
 */
@Slf4j
@ComponentScan(
        value = {"com.weiziplus.muteki"},
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@SpringBootApplication
public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
        log.info("springboot启动成功。接口文档:http://localhost:8080/doc.html");
    }

}
