package com.bocchi.controller;

import com.bocchi.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${fileSavePath.path}")
    private String fileSavePath;

    /**
     * 文件上传
     * MultiPartFile为专门接收file类型input框的数据
     *
     * @param file 参数名必须和请求体中的name名一致
     * @return
     */
    @PostMapping("upload")
    public Result<String> fileUpload(MultipartFile file) throws IOException {
        log.info("{}", file);
        log.info(fileSavePath);

        String originalFilename = file.getOriginalFilename();
        String[] split = originalFilename.split("\\.");
        String suffix = split[split.length - 1];
        String fileName = UUID.randomUUID().toString();

        File filePath = new File(fileSavePath);

        if (!filePath.exists()) {
            filePath.mkdirs();//mkdirs也可创建多级目录
        }

        String fullPath = fileSavePath + fileName + "." + suffix;
        log.info(fullPath);

        file.transferTo(new File(fullPath));

        return Result.success(fileName + "." + suffix);
    }


    /**
     * 文件下载
     */
    @GetMapping("/download")
    public void download(@RequestParam("name") String fileName, HttpServletResponse response) {
        FileInputStream fis = null;
        ServletOutputStream sos = null;
        try {
            fis = new FileInputStream(new File(fileSavePath + fileName));
            sos = response.getOutputStream();

            int len = 0;
            byte[] bytes = new byte[1024*1024*4];
            while ((len = fis.read(bytes)) != -1){
                sos.write(bytes,0,len);
            }
            sos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fis != null){
                try  {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sos != null){
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
