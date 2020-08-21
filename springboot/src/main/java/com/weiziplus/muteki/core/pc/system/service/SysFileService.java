package com.weiziplus.muteki.core.pc.system.service;

import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.HttpRequestUtils;
import com.weiziplus.muteki.common.util.ToolUtils;
import com.weiziplus.muteki.core.pc.system.vo.LogFileVo;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/08/18 17/20
 */
@Slf4j
@Service
public class SysFileService extends BaseService {

    /**
     * 日志文件存放路径
     */
    @Value("${global.log-path}")
    private String logPath = "";

    /**
     * 获取日志文件
     *
     * @return
     */
    public ResultBean<List<LogFileVo>> getLogFile() {
        //日志文件夹
        File baseFile = new File(logPath);
        //获取文件夹下所有文件
        File[] fileList = baseFile.listFiles();
        if (null == fileList) {
            return ResultBean.error("请检查日志文件路径设置");
        }
        List<LogFileVo> dirList = new ArrayList<>(ToolUtils.initialCapacity(fileList.length));
        for (File dir : fileList) {
            File[] files = dir.listFiles();
            //不是目录
            if (null == files) {
                continue;
            }
            List<LogFileVo> childrenList = new ArrayList<>(ToolUtils.initialCapacity(files.length));
            for (File file : files) {
                String fileName = file.getName();
                LogFileVo logFileVo = new LogFileVo()
                        .setName(fileName)
                        .setLength(file.length());
                childrenList.add(logFileVo);
            }
            LogFileVo fileVo = new LogFileVo()
                    .setName(dir.getName())
                    .setFileNum(childrenList.size())
                    .setChildren(childrenList);
            dirList.add(fileVo);
        }
        return ResultBean.success(dirList);
    }

    /**
     * 下载日志文件
     *
     * @param dir
     * @param name
     */
    public void downLogFile(String dir, String name) {
        if (ToolUtils.isBlank(dir)) {
            HttpRequestUtils.handleErrorResponse(ResultBean.error("目录错误"));
            return;
        }
        if (ToolUtils.isBlank(name)) {
            HttpRequestUtils.handleErrorResponse(ResultBean.error("文件名错误"));
            return;
        }
        //获取当前目录
        File dirFile = new File(logPath + File.separator + dir);
        if (!dirFile.exists()) {
            HttpRequestUtils.handleErrorResponse(ResultBean.error("目录错误"));
            return;
        }
        File[] files = dirFile.listFiles();
        if (null == files || 0 >= files.length) {
            HttpRequestUtils.handleErrorResponse(ResultBean.error("文件名错误"));
            return;
        }
        //是否有当前日志文件
        boolean haveFileFlag = false;
        for (File file : files) {
            if (name.equals(file.getName())) {
                haveFileFlag = true;
                break;
            }
        }
        if (!haveFileFlag) {
            HttpRequestUtils.handleErrorResponse(ResultBean.error("文件名错误"));
            return;
        }
        //当前文件
        File file = new File(logPath + File.separator + dir + File.separator + name);
        HttpServletResponse response = getResponse();
        try {
            @Cleanup InputStream inputStream = new FileInputStream(file);
            @Cleanup ServletOutputStream servletOutputStream = response.getOutputStream();
            IOUtils.copy(inputStream, servletOutputStream);
            response.flushBuffer();
        } catch (Exception e) {
            log.warn("下载日志文件出错，详情:" + e);
            HttpRequestUtils.handleErrorResponse(ResultBean.error("系统错误，请重试。" + e));
        }
    }

}
