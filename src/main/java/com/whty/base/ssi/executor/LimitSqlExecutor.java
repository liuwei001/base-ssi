package com.whty.base.ssi.executor;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback;
import com.ibatis.sqlmap.engine.scope.StatementScope;
import com.whty.base.ssi.dao.IDialect;

/**
 * 
 * 实现物理分页的SqlExecutor类
 * 继承并重写了executeQuery方法，通过改变SQL语句来实现物理分页功能
 * 
 * @author  t2w
 * @version  [V300R001, 2012-09-04]
 * @see  com.ibatis.sqlmap.engine.execution.SqlExecutor
 * @since  [SDP.BASE.SSI-V100R001]
 */
public class LimitSqlExecutor extends SqlExecutor
{
    /**
     * 类的日志对象
     */
    private static final Log logger = LogFactory.getLog(LimitSqlExecutor.class);
    
    /**
     * 方言对象
     */
    private IDialect dialect;
    
    /**
     * 是否支持分页true-支持,false-不支持
     */
    private boolean enableLimit = true;
    
    /**
     * 返回实现分页的数据库方言
     * @return IDialect [返回数据库的方言]
     */
    public IDialect getDialect()
    {
        return dialect;
    }
    
    /**
     * 设置实现分页的数据库方言，通过spring注入
     * @param dialect 使用的数据库方言
     */
    public void setDialect(IDialect dialect)
    {
        this.dialect = dialect;
    }
    
    /**
     * 数据库是否支持分页
     * @return boolean [返回数据库是否支持分页]
     */
    public boolean isEnableLimit()
    {
        return enableLimit;
    }
    
    /**
     * 设置数据库是否支持分页
     * @param enableLimit   true-支持分页，false-不支持分页
     */
    public void setEnableLimit(boolean enableLimit)
    {
        this.enableLimit = enableLimit;
    }
    
    /**
     * 实现物理分页，重写了ibatis父类的方法
     * 通过不同的方言获得不同的SQL语言，实现物理分页功能
     * @param statementScope 数据库statenent的存放范围
     * @param conn 数据库连接
     * @param sql 传入的SQL语句
     * @param parameters SQL语句中的其他参数
     * @param skipResults 起始记录数
     * @param maxResults 终止点记录数
     * @param callback 回调的接口 
     * @throws SQLException 数据库异常
     * @see com.coship.dhm.portalMS.base.dao.IBaseDAO#deleteByKey(java.io.Serializable)
     */
    @Override
    public void executeQuery(StatementScope statementScope, Connection conn,
            String sql, Object[] parameters, int skipResults, int maxResults,
            RowHandlerCallback callback) throws SQLException
    {
        if ((skipResults != NO_SKIPPED_RESULTS || maxResults != NO_MAXIMUM_RESULTS)
                && supportsLimit())
        {
            //取得具体的分页SQL
            sql = dialect.getLimitString(sql, skipResults, maxResults);
            if (logger.isDebugEnabled())
            {
                logger.debug(sql);
            }
            //设置为不分页
            skipResults = NO_SKIPPED_RESULTS;
            maxResults = NO_MAXIMUM_RESULTS;
        }
        //调用父类方法执行SQL语句
        super.executeQuery(statementScope,
                conn,
                sql,
                parameters,
                skipResults,
                maxResults,
                callback);
    }
    
    /**
     * 是否支持分页
     * @return boolean [true-支持分页，false-不支持分页]
     */
    public boolean supportsLimit()
    {
        //如果分页开关开启方言不为空，则返回方言是否支持分页
        if (enableLimit && dialect != null)
        {
            return dialect.isLimit();
        }
        return false;
    }
    
}