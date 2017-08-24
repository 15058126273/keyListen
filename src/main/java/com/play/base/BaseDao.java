package com.play.base;

import java.util.List;

/**
 * Dao层基类
 * Created by yjy on 2017/8/22.
 */
public interface BaseDao<T> {

    // 保存实体
    boolean add(T t);

    // 根据id删除
    boolean remove(int id);

    // 更新实体数据
    boolean update(T t);

    // 用id获取一个实体
    T getObjectById(int id);

    // 自定义获取一个实体
    T getObject(T t);

    // 自定义获取一个集合
    List<T> getObjects(T t);

    // 获取所有实体
    List<T> getObjects();

}