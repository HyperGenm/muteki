package com.weiziplus.muteki.core.pc.tools.controller;

import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.core.pc.common.interceptor.PcAuthToken;
import com.weiziplus.muteki.core.pc.tools.service.ToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author wanglongwei
 * @date 2019/8/24 16:46
 */
@RestController
@ApiIgnore
@PcAuthToken
@RequestMapping("/pc/tools")
public class ToolsController {

    @Autowired
    ToolsService service;

    /**
     * 文件上传
     *
     * @return
     */
    @PostMapping("/upload")
    public ResultBean upload(MultipartFile file) {
        return service.upload(file);
    }

    /**
     * 文件下载
     */
    @PostMapping("/downTemp")
    public void downTemp() {
        service.downTemp();
    }
}
