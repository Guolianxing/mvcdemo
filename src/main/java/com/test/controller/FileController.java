package com.test.controller;

import com.test.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
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
    // 如果不用表单的name属性，则需要@RequestParam(value = "xxx")注解
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
}
