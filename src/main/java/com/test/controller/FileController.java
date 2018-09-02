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
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

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

    /**
     * @Description: 下载文件
     * @Author: Guolianxing
     * @Date: 2018/9/2 10:49
     */
    @RequestMapping(value = "download")
    public ResponseEntity<byte[]> download() throws Exception {
        String path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("imgs/timg.jpg");
        File file = new File(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", file.getName());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    /**
     * @Description: 接受单个分片文件，放在以整个文件的md5命名的文件夹内
     * @Author: Guolianxing
     * @Date: 2018/9/2 10:01
     */
    @RequestMapping(value = "/bigFile")
    @ResponseBody
    public String bigFile(MultipartFile file, String md5, Integer index) throws Exception {
        String path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("upload");
        String tempPath = path + File.separator + "chunks" + File.separator + md5;
        File tempPathFile = new File(tempPath);
        if (!tempPathFile.exists()) {
            tempPathFile.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        String pre = fileName.substring(0, fileName.lastIndexOf("."));
        String suf = fileName.substring(fileName.lastIndexOf("."));
        String tempFileName = pre + index + suf;
        File tempFile = new File(tempPath, tempFileName);
        file.transferTo(tempFile);
        return "success";
    }

    /**
     * @Description: 合并分片文件成一个文件，并删除分片文件
     * @Author: Guolianxing
     * @Date: 2018/9/2 9:59
     */
    @RequestMapping(value = "/merge")
    @ResponseBody
    public String merge(String md5, String fileName) throws Exception {
        String path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("upload");
        String tempPath = path + File.separator + "chunks" + File.separator + md5;
        File tempPathFile = new File(tempPath);
        File[] sources = tempPathFile.listFiles();
        String pre = fileName.substring(0, fileName.lastIndexOf("."));

        Arrays.sort(sources, (f1, f2) -> {
            String f1Name = f1.getName();
            String f2Name = f2.getName();
            Integer order1 = Integer.parseInt(f1Name.substring(pre.length(), f1Name.lastIndexOf(".")));
            Integer order2 = Integer.parseInt(f2Name.substring(pre.length(), f2Name.lastIndexOf(".")));
            return order1.compareTo(order2);
        });
        File target = new File(path, fileName);
        target.createNewFile();
        RandomAccessFile out = new RandomAccessFile(target, "rw");
        out.setLength(0);
        out.seek(0);
        byte[] bytes = new byte[1024];
        for (File source : sources) {
            try (RandomAccessFile in = new RandomAccessFile(source, "rw")) {
                int len = -1;
                while ((len = in.read(bytes)) != -1) {
                    out.write(bytes, 0, len);
                }
            } catch (Exception e) {
                throw new RuntimeException("error", e);
            }
            source.delete();
        }
        out.close();
        tempPathFile.delete();
        return "success";
    }

    @ExceptionHandler
    @ResponseBody
    // 捕获文件大小异常
    public String doException(Exception e) {
        e.printStackTrace();
        if (e instanceof MaxUploadSizeExceededException) {
            System.out.println("文件大小超过限制" + ((MaxUploadSizeExceededException) e).getMaxUploadSize());
            return "size exceeded";
        }
        return "fail";
    }


}
