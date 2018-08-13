package com.test.controller;

import com.test.dto.UserDto;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.ServletContextResource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;

/**
 * Create by Guolianxing on 2018/7/4.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {


    @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    public String handle1(@PathVariable("userId") Integer userId) {
        return userId.toString();
    }

    @RequestMapping(value = "/handle2")
    public UserDto handle2(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        return new UserDto(userName, password);
    }



    @RequestMapping(value = "/getCookie")
    public void getCookei(HttpServletResponse response) {
        Cookie cookie = new Cookie("time", System.currentTimeMillis() + "");
        response.addCookie(cookie);
    }

    @RequestMapping(value = "handle3")
    public String handle3(@CookieValue("time") String time, @RequestHeader("Accept") String Accept) {
        String res = "{\"time\":\"" + time + "\",\"Accept\":\"" + Accept + "\"}";
        return res;
    }

    /**
     * @Description: http://localhost:8080/user/userName/xiaoming;a=11/password/123456;b=5
     * @Author: Guolianxing
     * @Date: 2018/7/4 15:05
     */
    @RequestMapping(value = "/userName/{userName}/password/{password}")
    public void handle4(@PathVariable("userName") String userName, @PathVariable("password") String password,
                           @MatrixVariable(pathVar = "userName", value = "a", defaultValue = "2") Integer a,
                           @MatrixVariable(pathVar = "password", value = "b", defaultValue = "3") String b) {
        System.out.println("username: " + userName);
        System.out.println("password: " + password);
        System.out.println("a: " + a);
        System.out.println("b: " + b);
    }

    /**
     * @Description: http://localhost:8080/user/handle5?userName=xiaoming&password=123456
     * @Author: Guolianxing
     * @Date: 2018/7/4 15:09
     */
    @RequestMapping(value = "/handle5")
    public UserDto handle5(UserDto userDto) {
        return userDto;
    }


    /**
     * @Description: http://localhost:8080/user/getSession?userName=xiaoming&password=123456
     * @Author: Guolianxing
     * @Date: 2018/7/4 15:16
     */
    @RequestMapping(value = "/getSession")
    public void getSession(HttpServletRequest request, HttpServletResponse response, UserDto userDto) {
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", userDto);
    }

    /**
     * @Description: http://localhost:8080/user/handle6?userName=xiaoming&password=123456
     * @Author: Guolianxing
     * @Date: 2018/7/4 15:17
     */
    @RequestMapping(value = "handle6")
    public UserDto handle6(HttpServletRequest request, HttpServletResponse response, HttpSession session, UserDto userDto) {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        UserDto inSessionUser = (UserDto) session.getAttribute("userInfo");

        System.out.println("userName: " + userName);
        System.out.println("password: " + password);
        System.out.println(inSessionUser.equals(userDto));

        return userDto;
    }


    /**
     * @Description: http://localhost:8080/user/handle7?userName=xiaoming&password=123456
     * @Author: Guolianxing
     * @Date: 2018/7/4 15:23
     * @param request spring 提供的代理Servlet原生api的接口
     */
    @RequestMapping(value = "/handle7")
    public UserDto handle7(WebRequest request) {
        return new UserDto(request.getParameter("userName"), request.getParameter("password"));
    }


    /**
     * @Description: 可以以ServletRequest 的InputStream/Reader或ServletResponse的OutputStream/Writer对象作为控制器方法入参
     * @Author: Guolianxing
     * @Date: 2018/7/4 15:38
     */
    @RequestMapping(value = "/handle8")
    public void handle8(OutputStream outputStream) {
        // 可以从RequestContestHolder中直接获取当前request，response等对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        /**
         * 这两个方法在没有使用JSF的项目中没有区别
         */
//        RequestContextHolder.getRequestAttributes()
//        RequestContextHolder.currentRequestAttributes()
//        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse()
        Resource resource = new ServletContextResource(request.getServletContext(), "imgs/timg.jpg");
        try {
            FileCopyUtils.copy(resource.getInputStream(), outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
