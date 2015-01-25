package com.whty.base.ssi.service;

import java.io.Serializable;
import java.util.List;

import com.whty.base.ssi.common.util.PageList;
import com.whty.base.ssi.exception.SSIException;


/**
 * 
 * 所有service的顶层接口
 * 定义了一些通用的方法
 * @author  t2w
 * @version  [V100R001, 2009-11-23]
 * @see  Serializable
 * @since  [SDP.BASE.SSI-V100R001]
 */
public interface IService extends Serializable
{
    /**
     * 按参数条件删除一个实体对象
     * @param args 删除传递的参数列表
     * @return boolean [true-删除成功,false-]
     * @exception throws [portalMSException] [发生异常包装后抛出]
     * @see PortalMSException
     */
    public boolean deleteByParam(Serializable[] args) throws SSIException;
    
    /**
     * 保存一个实体对象
     * @param entity 实体对象
     * @exception throws [portalMSException] [发生异常包装后抛出]
     * @see PortalMSException
     */
    public void saveEntity(Serializable entity) throws SSIException;
    
    /**
     * 更新一个实体对象
     * @param entity 实体对象
     * @exception throws [portalMSException] [发生异常包装后抛出]
     * @see PortalMSException
     */
    public void updateEntity(Serializable entity) throws SSIException;
    
    /**
     * 按主键编号查找一个实体对象
     * @param id 主键编号
     * @return Object [实体对象]
     * @exception throws [portalMSException] [发生异常包装后抛出]
     * @see PortalMSException
     */
    public Object findById(Serializable id) throws SSIException;
    
    /**
     * 查找所有符合条件的实体对象
     * @return List<Serializable> [返回所有符合条件的实体对象]
     * @exception throws [portalMSException] [发生异常包装后抛出]
     * @see PortalMSException
     */
    public List<Serializable> findAll() throws SSIException;
    
    /**
     * 得到拟查询SQL的记录数
     * @return int [记录的行数]
     * @exception throws [portalMSException] [发生异常包装后抛出]
     * @see PortalMSException
     */
    public int getRowCount() throws SSIException;
    
    /**
     * 分页查询满足条件的实体对象
     * @param map 查询需要的参数,可以是MAP或实体,当为NULL时表示没有参数
     * @param currPage 当前页码
     * @param pageSize 每页显示的记录数
     * @return PageList<Serializable> [返回所有符合条件的实体对象]
     * @exception throws [portalMSException] [发生异常包装后抛出]
     * @see PortalMSException
     */
    public PageList<Serializable> findByCriteria(Object map, int currPage,
            int pageSize) throws SSIException;
    
    /**
     * 得到拟查询SQL的记录数
     * @param map 查询附带的参数
     * @return int [记录的行数]
     * @exception throws [portalMSException] [发生异常包装后抛出]
     * @see PortalMSException
     */
    public int getRowCount(Object map) throws SSIException;
}
