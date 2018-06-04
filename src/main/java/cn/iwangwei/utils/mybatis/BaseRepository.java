package cn.iwangwei.utils.mybatis;

public interface BaseRepository<T, ID> {

    T getById(ID id);
}
