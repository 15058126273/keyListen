package com.play.mapper;

import java.io.Serializable;
import java.util.List;

/**
 * mapper父类
 * Created by yjy on 2017/4/28.
 */
public interface BaseMapper<T extends Serializable> {

    void save(T t);

    boolean update(T t);

    boolean delete(long id);

    T findById(long id);

    List<T> findByT(T t);

    List<T> findPage(int start, int pageSize, T t);

    int findCount(T t);

}
