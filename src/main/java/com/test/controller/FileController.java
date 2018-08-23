package com.test.controller;

import com.test.dto.UserDto;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Create by Guolianxing on 2018/8/22.
 */
@Controller
@RequestMapping(value = "/file")
public class FileController {


    @RequestMapping(value = "/upload")
    @ResponseBody
    // 上传文件的入参需要是表单中的name，而不是文件名
    // 如果不用表单的name属性，则需要@RequestParam(value = "name")注解
    // 有除文件以外的表单参数，只需name对应即可
    public String uploadFile(MultipartFile file) {
        String path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("upload");
        try {
            String fileName = file.getOriginalFilename();
            System.out.println(fileName);
            File parent = new File(path);
            if (!parent.exists()) {
                parent.mkdir();
            }
            file.transferTo(new File(parent, fileName));
            return "success";
        } catch (IOException e) {
            e.getStackTrace();
        }
        return "fail";
    }

    @RequestMapping(value = "/uploadForm")
    @ResponseBody
    public String uploadForm(MultipartFile userImg, UserDto userDto) {
        System.out.println(userDto);
        String path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("upload");
        try {
            String fileName = userImg.getOriginalFilename();
            System.out.println(fileName);
            File parent = new File(path);
            if (!parent.exists()) {
                parent.mkdir();
            }
            userImg.transferTo(new File(parent, fileName));
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @RequestMapping(value = "/uploadMFile")
    @ResponseBody
    public String uploadMFile(MultipartFile[] files) {
        String path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("upload");
        try {
            File parent = new File(path);
            if (!parent.exists()) {
                parent.mkdir();
            }
            for (MultipartFile file : files) {
                System.out.println(file.getOriginalFilename());
                file.transferTo(new File(parent, file.getOriginalFilename()));
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @RequestMapping(value = "/uploadInputs")
    @ResponseBody
    public String uploadInput(MultipartFile[] files1, MultipartFile[] files2) {
        String path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("upload");
        try {
            File parent = new File(path);
            if (!parent.exists()) {
                parent.mkdir();
            }
            for (MultipartFile file : files1) {
                System.out.println(file.getOriginalFilename());
                file.transferTo(new File(parent, file.getOriginalFilename()));
            }
            for (MultipartFile file : files2) {
                System.out.println(file.getOriginalFilename());
                file.transferTo(new File(parent, file.getOriginalFilename()));
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @ExceptionHandler
    @ResponseBody
    // 捕获文件大小异常
    public String doException(Exception e) {
        if (e instanceof MaxUploadSizeExceededException) {
            System.out.println("文件大小超过限制" + ((MaxUploadSizeExceededException) e).getMaxUploadSize());
            return "size exceeded";
        }
        return "no error";
    }

    @RequestMapping(value = "download")
    public ResponseEntity<byte[]> download() throws Exception {
        String path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("imgs/timg.jpg");
        File file = new File(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", file.getName());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }
}
