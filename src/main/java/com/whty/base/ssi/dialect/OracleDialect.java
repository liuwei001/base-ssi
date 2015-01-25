package com.whty.base.ssi.dialect;

import com.whty.base.ssi.dao.IDialect;


/**
 * 
 * oracle实现的方言接口
 * 实现了IDialect，对oracle进行了特殊化处理
 * 
 * @author  t2w
 * @version  [V100R001, 2012-09-04]
 * @see  IDialect
 * @since  [SDP.BASE.SSI-V100R001]
 */
public class OracleDialect implements IDialect
{
    /**
     * SQL语句结束的符号
     */
    protected static final String SQL_END_DELIMITER = ";";
    
    /**
     * 是否支持分页true-是,false-不支持
     */
    private boolean limit;
    
    /**
     * 返回是否支持分页
     * @return true-支持，false-不支持
     */
    public boolean isLimit()
    {
        return limit;
    }
    
    /**
     * 设置是否支持分页
     * @param  limit true-支持,false-不支持
     */
    public void setLimit(boolean limit)
    {
        this.limit = limit;
    }
    
    /**
     * 得到ORACLE的分页SQL语句
     * @param sql 待分页的SQL语句
     * @param hasOffset 是否有最大页
     * @return [类型:String]返回带分页的SQL语句
     */
    public String getLimitString(String sql, boolean hasOffset)
    {
        StringBuffer bufsql = new StringBuffer(
                "SELECT * FROM (SELECT r.*, ROWNUM rn FROM (");
        bufsql.append(sql);
        bufsql.append(") r WHERE ROWNUM <= ? ");
        bufsql.append(")");
        if (hasOffset)
        {
            bufsql.append("where rn >=?");
            
        }
        return bufsql.toString();
    }
    
    /**
     * 根据起始记录数和终止记录数得到分页的SQL语句
     * @param sql [类型:String]待分页的SQL语句
     * @param skipResults [类型:int]分页的起点记录
     * @param maxResults [类型:int]分页的终止点记录
     * @return [类型:String]具体的分页SQL语句
     */
    
    public String getLimitString(String sql, int skipResults, int maxResults)
    {
        
        StringBuffer bufsql = new StringBuffer(
                "select * from (select r.*, rownum rn from (");
        bufsql.append(sql);
        bufsql.append(") r where rownum <= ");
        bufsql.append(maxResults);
        bufsql.append(") where rn >= ");
        bufsql.append(skipResults);
        return bufsql.toString();
    }
    
}
