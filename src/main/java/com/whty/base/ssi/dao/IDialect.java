package com.whty.base.ssi.dao;

import java.io.Serializable;

/**
 * 
 * 数据库方言接口类
 * 用于根据各种数据库的差异生成不同的SQL语句
 * 
 * @author  t2w
 * @version  [100R001, 2012-09-04]
 * @since  [SDP.BASE.SSI-V100R001]
 */
public interface IDialect
{
    /**
     * 返回数据库是否支持分页
     * @return boolean [true--支持分页,false--不支持分页]
     */
    public boolean isLimit();
    
    /**
     * 得到分页语句
     * 根据不同数据库返回不同的分页代码
     * @param sql sql标准语句
     * @param hasOffset 是否有终止参数true-有,false-没有
     * @return String [返回组装好的分页SQL语句]
     */
    public String getLimitString(String sql, boolean hasOffset);
    
    /**
     * 得到具体的分页语句
     * 根据传入的参数不同拼装不同的数据库分页语句
     * @param sql [类型:String]数据库的原始SQL语句
     * @param skipResults [类型:int]起始的记录数
     * @param maxResults [类型:int]结束的记录数
     * @return String [返回组装好的分页SQL语句]
     */
    public String getLimitString(String sql, int skipResults, int maxResults);
}