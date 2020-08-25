package com.weiziplus.muteki.ali.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.weiziplus.muteki.ali.AliConfig;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.result.ResultEnum;
import com.weiziplus.muteki.common.util.ToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云短信接口
 *
 * @author wanglongwei
 * @date 2020/08/21 11/08
 */
@Slf4j
@Component
public class AliSmsApi {

    /**
     * 请求成功的code
     */
    private static final String SUCCESS_CODE = "OK";

    /**
     * 签名
     */
    private static String SIGN_NAME;

    /**
     * 签名赋值
     */
    @Value("${global.ali.sms.sign-name:}")
    private void setSignName(String signName) {
        AliSmsApi.SIGN_NAME = signName;
    }

    /**
     * 模板code
     */
    private static String TEMPLATE_CODE;

    /**
     * 模板code赋值
     *
     * @param templateCode
     */
    @Value("${global.ali.sms.template-code:}")
    private void setTemplateCode(String templateCode) {
        AliSmsApi.TEMPLATE_CODE = templateCode;
    }

    /**
     * 发送短信
     *
     * @param signName
     * @param templateCode
     * @param phoneNumbers
     * @param templateParam
     */
    private static ResultBean<AliSmsResult> baseSend(String signName, String templateCode, String phoneNumbers, String templateParam) {
        DefaultProfile profile = DefaultProfile.getProfile(
                AliConfig.getRegionId(), AliConfig.getAccessKeyId(), AliConfig.getAccessSecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", AliConfig.getRegionId());
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        request.putQueryParameter("TemplateParam", templateParam);
        try {
            CommonResponse response = client.getCommonResponse(request);
            //如果请求出错
            if (HttpServletResponse.SC_OK != response.getHttpStatus()) {
                log.warn("阿里云发送短信出错,状态码:{},详情:{}", response.getHttpStatus(), response.getData());
                return ResultBean.error(ResultEnum.ERROR_ALI, response.getData());
            }
            AliSmsResult aliSmsResult = JSONObject.parseObject(response.getData(), AliSmsResult.class);
            if (!SUCCESS_CODE.equals(aliSmsResult.getCode())) {
                log.warn("阿里云发送短信出错,详情:{}", JSON.toJSONString(aliSmsResult));
                return ResultBean.error(ResultEnum.ERROR_ALI, JSON.toJSONString(aliSmsResult));
            }
            return ResultBean.success(aliSmsResult);
        } catch (ClientException e) {
            log.warn("阿里云发送短信出错,错误码:{},错误信息:{},错误类型:{},错误描述:{},详情:{}",
                    e.getErrCode(), e.getErrMsg(), e.getErrorType(), e.getErrorDescription(), e.getMessage());
            return ResultBean.error(ResultEnum.ERROR_ALI, e.getErrMsg(), e);
        }
    }

    /**
     * 发送验证码
     * 您正在申请手机注册，验证码为：${code}，5分钟内有效！
     *
     * @param phoneNumbers
     * @param code
     * @return
     */
    public static ResultBean<AliSmsResult> sendCode(String phoneNumbers, String code) {
        Map<String, Object> map = new HashMap<>(ToolUtils.initialCapacity(1));
        map.put("code", code);
        return baseSend(SIGN_NAME, TEMPLATE_CODE, phoneNumbers, JSON.toJSONString(map));
    }

}
