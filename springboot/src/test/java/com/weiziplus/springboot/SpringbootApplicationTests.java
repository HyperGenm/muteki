package com.weiziplus.springboot;

import com.alibaba.fastjson.JSON;
import com.weiziplus.muteki.SpringbootApplication;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.base.BaseWhere;
import com.weiziplus.muteki.common.base.BaseWhereModel;
import com.weiziplus.muteki.common.models.SysUser;
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
     * 阿里云发送短信
     */
    @Test
    public void aliSms() {
        BaseWhere<SysUser> baseWhere = new BaseWhere<>(SysUser.class);
        BaseWhere<SysUser> admin = baseWhere.where(
                BaseWhereModel.eq(SysUser.COLUMN_STATUS, 1),
                BaseWhereModel.eq(SysUser.COLUMN_ICON, "/icon"),
                BaseWhereModel.like(SysUser.COLUMN_USERNAME, "admin")
        );
        System.out.println(JSON.toJSONString(baseFindList(admin)));

    }


}
