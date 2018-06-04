package cn.iwangwei.controller;

import cn.iwangwei.domain.User;
import cn.iwangwei.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by wangwei on 17-3-14.
 */
@Controller
public class TestController {

    @Resource
    private UserService userService;

    @RequestMapping("/hello")
    @ResponseBody
    public List<User> test() {
        return userService.getUsers();
    }
}
