package com.whty.base.ssi.dialect;

import com.whty.base.ssi.dao.IDialect;

/**
 * 
 * MYSQL的方言类
 * @author  t2w
 * @version  [V100R001, 2012-09-04]
 * @see  #getLimitString(String, int, int)
 * @see MySQLDialect#getLimitString(String, boolean)
 * @since  [SDP.BASE.SSI-V100R001]
 */
public class MySQLDialect implements IDialect
{
    
    protected static final String SQL_END_DELIMITER = ";";
    
    /**
     * 
     * @param sql 查询的SQL
     * @param hasOffset 是否有分页
     * @return String 分页后的SQL语句
     */
    public String getLimitString(String sql, boolean hasOffset)
    {
        return new StringBuilder(sql.length() + 20).append(trim(sql))
                .append(hasOffset ? " limit ?,?" : " limit ?")
                .append(SQL_END_DELIMITER)
                .toString();
    }
    
    /**
     * 
     * @param sql 查询参数
     * @param offset 分页起始点
     * @param limit  分页终止点
     * @return String 组装后的SQL
     */
    public String getLimitString(String sql, int offset, int limit)
    {
        sql = trim(sql);
        StringBuilder sb = new StringBuilder(sql.length() + 20);
        sb.append(sql);
        if (offset > 0)
        {
            sb.append(" limit ")
                    .append(offset)
                    .append(',')
                    .append(limit)
                    .append(SQL_END_DELIMITER);
        }
        else
        {
            sb.append(" limit ").append(limit).append(SQL_END_DELIMITER);
        }
        return sb.toString();
    }
    
    /**
     * @return boolean 是否支持分页
     */
    public boolean isLimit()
    {
        return true;
    }
    /**
     * 去掉SQL语句中的分号(;)
     * @param sql SQL语句
     * @return String [去掉分号后的SQL语句]
     */
    private String trim(String sql)
    {
        sql = sql.trim();
        if (sql.endsWith(SQL_END_DELIMITER))
        {
            sql = sql.substring(0, sql.length() - 1
                    - SQL_END_DELIMITER.length());
        }
        return sql;
    }
    
}