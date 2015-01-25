package com.whty.base.ssi.dao;

import java.io.Serializable;
import java.util.List;

import com.whty.base.ssi.exception.SSIException;

/**
 * 
 * ibatis的DAO方案
 * 针对ibatis的特殊性定义的接口，继承自IBaseDAO
 * 
 * @author  t2w
 * @version  [V100R001, 2012-09-04]
 * @see  IBaseDAO
 * @since  [SDP.BASE.SSI-V100R001]
 */
public abstract interface IIbatisDAO<T extends Serializable, PK extends Serializable>
        extends IBaseDAO<T, PK>, Serializable
{
    /**
     * IBATIS配置文件中定义文件中对应的sqlid，插入操作
     */
    public static final String SQLID_INSERT = "insert";
    
    /**
     * IBATIS配置文件中定义文件中对应的sqlid，按主键删除实体操作
     */
    public static final String SQLID_DELETE = "deleteByPrimaryKey";
    
    /**
     * IBATIS配置文件中定义文件中对应的sqlid，按主键的更新操作
     */
    public static final String SQLID_UPDATE = "updateById";
    
    /**
     * IBATIS配置文件中定义文件中对应的sqlid，查找所有实体操作
     */
    public static final String SQLID_FINDALL = "findAll";
    
    /**
     * IBATIS配置文件中定义文件中对应的sqlid，按主键ID查找实体对象
     */
    public static final String SQLID_FINDBYID = "findById";
    
    /**
     * IBATIS配置文件中定义文件中对应的sqlid，查询记录集数
     */
    public static final String SQLID_ROWCOUNT = "rowCount";
    
    /**
     * 批量插入实体
     * 数据库的insert批量操作，接口方法，由具体实现类实现
     * @param list 参数类型:List<T>列表中存放泛型，任何实现序列化的实体类
     * @exception throws [portalMSException] [当批量插入失败时抛出此异常]
     */
    public abstract void batchInsert(final List<T> list) throws SSIException;
    
    /**
     * 批量修改实体
     * 数据库的update批量操作，接口方法，由具体实现类实现
     * @param list 参数类型:[List<T>]列表中存放泛型，任何实现序列化的实体类
     * @exception throws [portalMSException] [当批量更新失败时抛出此异常]
     */
    public abstract void batchUpdate(final List<T> list) throws SSIException;
    
    /**
     * 批量删除实体
     * 数据库的delete批量操作，接口方法，由具体实现类实现
     * @param list  [参数类型:List<T>]列表中存放泛型，任何实现序列化的实体类
     * @exception throws [portalMSException] [当批量删除失败时抛出此异常]
     */
    public abstract void batchDelete(final List<PK> list) throws SSIException;
    /**
     * 批量删除实体
     * 数据库的delete批量操作，接口方法，由具体实现类实现
     * @param args  [参数类型:PK[]列表中存放泛型，任何实现序列化的实体类
     * @exception throws [portalMSException] [当批量删除失败时抛出此异常]
     */
    
    public abstract void batchDelete(final PK[] args) throws SSIException;
    
    /**
     * 带分页条件查找记录
     * 使用当前页和每页大小获得实体的列表
     * @param map 查询的参数列表
     * @param currPage 当前页码，整型
     * @param pageSize 每页记录数，整型
     * @return List<T> [返回实体的列表，实体类型为泛型，任何实现序列化的实体类]
     * @exception throws [portalMSException] [当操作中发生数据库访问异常抛出]
     */
    public List<T> findByCriteria(Object map, int currPage, int pageSize)
            throws SSIException;
    
    /**
     * 得到满足条件的记录数
     * 根据查询条件得到满足条件的记录数
     * @param map 查询参数
     * @return Integer [整型包装类]
     * @exception throws [DataAccessException] [当操作中发生数据库访问异常抛出]
     */
    public Integer getRowCount(Object map) throws SSIException;
    
    /**
     * 根据查询条件得到数据库中所有满足条件的记录实体
     * 此功能不带分页功能
     * @param map 查询条件的参数
     * @return List<T> [泛型的列表，列表中存放任何实现序列化的实体类]
     * @exception throws [portalMSException] [当ibatis访问数据库出现异常时抛出]
     */
    public List<T> findByCriteria(Object map) throws SSIException;
    
}
