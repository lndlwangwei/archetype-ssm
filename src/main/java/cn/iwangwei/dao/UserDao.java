package cn.iwangwei.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by wangwei on 17-3-14.
 */
@Repository
public interface UserDao {

    List<Map<String, Object>> getUsers();
}
