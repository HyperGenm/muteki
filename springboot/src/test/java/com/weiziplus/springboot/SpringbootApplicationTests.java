package com.weiziplus.springboot;

import com.alibaba.fastjson.JSON;
import com.weiziplus.muteki.SpringbootApplication;
import com.weiziplus.muteki.ali.sms.AliSmsApi;
import com.weiziplus.muteki.ali.sms.AliSmsResult;
import com.weiziplus.muteki.baidu.map.BaiduMapApi;
import com.weiziplus.muteki.baidu.map.iplocation.IpLocation;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 单元测试
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootApplication.class)
public class SpringbootApplicationTests extends BaseService {

    /**
     * 百度根据ip获取地理信息
     */
    @Test
    public void baiduGetLocation() {
        ResultBean<IpLocation> location = BaiduMapApi.getLocation("39.96.52.201");
        //请求出错
        if (!ResultEnum.SUCCESS.getValue().equals(location.getCode())) {
            log.warn("调用阿里云发送短信验证码出错，详情:" + location.getMsg());
            return;
        }
        IpLocation data = location.getData();
        log.info("调用成功。" + JSON.toJSONString(data));
    }

    /**
     * 阿里云发送短信
     */
    @Test
    public void aliSms() {
        ResultBean<AliSmsResult> aliSmsResultResultBean = AliSmsApi.sendCode("18866839592", "1234");
        //请求出错
        if (!ResultEnum.SUCCESS.getValue().equals(aliSmsResultResultBean.getCode())) {
            log.warn("调用阿里云发送短信验证码出错，详情:" + aliSmsResultResultBean.getMsg());
            return;
        }
        AliSmsResult data = aliSmsResultResultBean.getData();
        log.info("调用成功。" + JSON.toJSONString(data));
    }

}
