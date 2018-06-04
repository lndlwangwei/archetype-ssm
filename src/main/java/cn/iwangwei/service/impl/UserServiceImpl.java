package cn.iwangwei.service.impl;

import cn.iwangwei.dao.UserDao;
import cn.iwangwei.domain.User;
import cn.iwangwei.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by wangwei on 17-3-14.
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    public List<User> getUsers() {
        return userDao.getUsers();
    }
}
