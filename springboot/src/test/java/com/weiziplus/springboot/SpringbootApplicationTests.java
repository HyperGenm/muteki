package com.weiziplus.springboot;

import com.alibaba.fastjson.JSON;
import com.weiziplus.muteki.SpringbootApplication;
import com.weiziplus.muteki.baidu.map.BaiduMapApi;
import com.weiziplus.muteki.baidu.map.iplocation.IpLocation;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.result.ResultBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootApplication.class)
public class SpringbootApplicationTests extends BaseService {

    @Autowired
    BaiduMapApi mapApi;

    @Test
    public void test() {
        ResultBean<IpLocation> location = mapApi.getLocation("222.134.55.245");
        System.out.println(JSON.toJSONString(location));


    }

}
