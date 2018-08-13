package com.test.controller;

import com.test.dto.UserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Create by Guolianxing on 2018/7/4.
 */

/**
 * 将请求body中的json或xml转换成一个对象，需要在入参使用@RequestBody注解，或使用HttpEntity<T>对象作为入参，
 * 如使用后者不仅可以访问body中的信息，还可以访问请求头中的信息。
 * 将一个对象以json的形式直接写入响应body，可以使用@ResponseBody注解，或使用ResponseEntity<T>对象返回。
 */
@Controller
@RequestMapping(value = "/data")
public class ConvertController {


//    POST /data/handle1 HTTP/1.1
//    Host: localhost:8080
//    Content-Type: application/json
//    Cache-Control: no-cache
//    Postman-Token: d0851550-9b98-4bf3-bd54-c647dadcac7a
//
//    {"userName":"xiaoming","password":"123456"}

    @RequestMapping(value = "/handle1")
    @ResponseBody
    public UserDto handle1(@RequestBody UserDto userDto) {
        return userDto;
    }

    @RequestMapping(value = "/handle2")
    public ResponseEntity<UserDto> handle2(HttpEntity<UserDto> httpEntity) {
        MediaType contentType = httpEntity.getHeaders().getContentType();
        System.out.println(contentType);
        UserDto userDto = httpEntity.getBody();
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(userDto, HttpStatus.OK);
        return responseEntity;
    }

}
