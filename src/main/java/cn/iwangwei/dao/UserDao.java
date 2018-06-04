package cn.iwangwei.dao;

import cn.iwangwei.domain.User;
import cn.iwangwei.utils.mybatis.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by wangwei on 17-3-14.
 */
@Repository
public interface UserDao extends BaseRepository<User, Long> {

    List<User> getUsers();
}
