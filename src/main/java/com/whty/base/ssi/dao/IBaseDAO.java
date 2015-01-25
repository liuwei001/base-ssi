package com.whty.base.ssi.dao;

import java.io.Serializable;
import java.util.List;

import com.whty.base.ssi.exception.SSIException;

/**
 * 
 * 泛形DAO顶层接口，Hibernate,Ibatis公共接口
 * 抽象出了Hibernate和Ibatis公共的方法
 * 
 * @author  t2w
 * @version  [100R001, 2012-09-04]
 * @see  Serializable
 * @since  [SDP.BASE.SSI-V100R001]
 */

public interface IBaseDAO<T extends Serializable, PK extends Serializable>
{
    
    /**
     * 通过主键ID查找对应的实体
     * 接口方法，子类必须实现该方法
     * @param id [泛型，主键ID编号]
     * @return T [泛型，返回一个具体的实体]
     * @exception throws [portalMSException] [当数据库操作异常时将抛出该异常]
     */
    public abstract T findById(PK id) throws SSIException;
    
    /**
     * 查找所有实体
     * 接口方法，用于查找所有实体，子类必须实现该方法
     * @return List<T> [返回所有符合条件的实体]
     * @exception throws [portalMSException] [当数据库操作异常时将抛出该异常]
     */
    public abstract List<T> findAll() throws SSIException;
    
    /**
     * 得到总行数
     * 接口方法
     * @return Integer [返回记录的总行数]
     * @exception throws [portalMSException] [当数据库访问失败时抛出的自定义异常]
     */
    public abstract Integer getRowCount() throws SSIException;
    
    /**
     * 更新单个实体数据库update操作
     * 根据实体的参数更新数据库中对应的记录
     * @param entity 实体对象，泛型，任何实现序列化的类
     * @exception throws [portalMSException] [当数据库操作发生失败时抛出该异常]
     */
    public abstract void updateEntity(T entity) throws SSIException;
    
    /**
     * 保存单个实体数据库insert操作
     * 根据实体内容插入到数据库
     * @param entity 实体对象，泛型，任何实现序列化的类
     * @exception throws [portalMSException] [当数据库操作发生失败时抛出该异常]
     */
    public abstract void saveEntity(T entity) throws SSIException;
    
    /**
     * 按主键编号删除数据库对应单个实体
     * 对应数据库delete操作
     * @param id 主键ID
     * @exception throws [portalMSException] [当数据库操作失败时抛出该异常]
     */
    public abstract void deleteByKey(PK id) throws SSIException;
    
}
