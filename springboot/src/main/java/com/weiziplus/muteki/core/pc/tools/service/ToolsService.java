package com.weiziplus.muteki.core.pc.tools.service;

import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.result.ResultEnum;
import com.weiziplus.muteki.common.util.FileUtils;
import com.weiziplus.muteki.common.util.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author wanglongwei
 * @date 2019/8/24 16:27
 */
@Slf4j
@Service
public class ToolsService extends BaseService {

    /**
     * 文件上传
     *
     * @return
     */
    public ResultBean upload(MultipartFile file) {
        ResultBean<String> stringResultUtils = FileUtils.upImage(file);
        if (!ResultEnum.SUCCESS.getValue().equals(stringResultUtils.getCode())) {
            return ResultBean.error(stringResultUtils.getMsg());
        }
        return ResultBean.success(new HashMap<String, String>(1) {{
            put("path", stringResultUtils.getData());
        }});
    }

    /**
     * 文件下载
     */
    public void downTemp() {
        String path = "/logo.png";
        HttpServletResponse response = getResponse();
        try {
            FileUtils.downFile(response, path);
        } catch (IOException e) {
            log.warn("文件下载失败,详情:" + e);
            HttpRequestUtils.handleErrorResponse(ResultBean.error("文件下载失败，详情:" + e));
        }
    }
}
