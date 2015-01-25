package com.whty.base.ssi.dao.ibatis;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.whty.base.ssi.common.Constants;
import com.whty.base.ssi.common.util.PageList;
import com.whty.base.ssi.common.util.ReflectUtil;
import com.whty.base.ssi.dao.IIbatisDAO;
import com.whty.base.ssi.exception.SSIException;
import com.whty.base.ssi.executor.LimitSqlExecutor;

import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;

/**
 * 
 * IIbatisDAO 类的抽象实现，
 * 部分实现公共的增删改查功能，子类可以继续扩展该类的方法
 * 
 * @author  t2w
 * @version  [V100R001, 2012-09-04]
 * @see  SqlMapClientDaoSupport
 * @since  [SDP.BASE.SSI-V100R001]
 */
public abstract class IbatisDAO<T extends Serializable, PK extends Serializable>
        extends SqlMapClientDaoSupport implements IIbatisDAO<T, PK>
{
    /**
     * 序列化的ID
     */
    private static final long serialVersionUID = 7489415169618169154L;
    
    /**
     * ibatis中sqlmap命名空间
     */
    private String nameSpace = "";
    
    /**
     * 实体类
     */
    private Class<T> entityClass;
    
    /**
     * SQL语句的操作对象
     */
    protected SqlExecutor sqlExecutor;
    
    /**
     * 对命名空间进行了赋值，可以通过命名空间执行具体的sqlMap对应的语句
     * <默认构造函数>
     */
    @SuppressWarnings("unchecked")
    public IbatisDAO()
    {
        this.entityClass = null;
        Class c = getClass();
        //得到具体的子类类型
        Type t = c.getGenericSuperclass();
        if (t instanceof ParameterizedType)
        {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            this.entityClass = (Class<T>) p[0];
            //得到命名空间，用于向sqlMap传入
            nameSpace = entityClass.getSimpleName() + ".";
        }
    }
    
    /**
     * 对SQL语句操作对象进行赋值
     * @param sqlExecutor 
     */
    public void setSqlExecutor(SqlExecutor sqlExecutor)
    {
        this.sqlExecutor = sqlExecutor;
    }
    
    /**
     * 对是否支持物理分页进行赋值
     * spring注入方法，注入具体的sqlExecutor是否支持物理分页
     * @param enableLimit [是否支持物理分页，true-支持,false-不支持]
     * @see LimitSqlExecutor#setEnableLimit(boolean)
     */
    public void setEnableLimit(boolean enableLimit)
    {
        if (sqlExecutor instanceof LimitSqlExecutor)
        {
            ((LimitSqlExecutor) sqlExecutor).setEnableLimit(enableLimit);
        }
    }
    
    /**
     * 初始化sqlExcutor类，在spring初始化时会加载该方法
     * 因为ibatis本身不支持物理分页，用自己定义的sqlExecutor来替代默认的sqlExecutor
     * @exception throws [Exception] [向上层直接抛出]
     * @see SqlMapExecutorDelegate
     */
    public void initialize() throws Exception
    {
        if (sqlExecutor != null)
        {
            SqlMapClientImpl client = (SqlMapClientImpl) getSqlMapClient();
            SqlMapExecutorDelegate delgate = client.getDelegate();
            //由于SqlMapExecutorDelegate没有setSqlExecutor方法，利用反射强行对sqlExecutor赋值
            ReflectUtil.setFieldValue(delgate,
                    "sqlExecutor",
                    SqlExecutor.class,
                    sqlExecutor);
        }
    }
    
    /**
     * 按主键删除实体对象
     * @param id 泛型主键ID
     * @throws PortalMSException
     */
    public void deleteByKey(PK id) throws SSIException
    {
        try
        {
            getSqlMapClientTemplate().delete(nameSpace + SQLID_DELETE, id);
        }
        catch (Exception ex)
        {
            throw new SSIException(
                    Constants.ERROR_CODE_OPERATOR_DATABASE.getLongValue(), ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    /**
     * 查找符合条件的所有对象
     * @return List,List中存放泛型的实体对象
     */
	public List<T> findAll() throws SSIException {
		try {
			return getSqlMapClientTemplate().queryForList(nameSpace + SQLID_FINDALL);
		} catch (Exception ex) {
			throw new SSIException(Constants.ERROR_CODE_OPERATOR_DATABASE.getLongValue(), ex);
		}
	}

    @SuppressWarnings("unchecked")
    /**
     * 按主键查找实体对象
     * @param id 泛型主键
     * @return 泛型实体，实现序列化接口的任何类型
     * @throws portalMSException
     */
    public T findById(PK id) throws SSIException
    {
        T result = null;
        try
        {
            result = (T) this.getSqlMapClient().queryForObject(nameSpace
                    + SQLID_FINDBYID,
                    id);
        }
        catch (Exception e)
        {
            throw new SSIException(
                    Constants.ERROR_CODE_ACCESS_DATABASE.getLongValue(), e);
        }
        return result;
        
    }
    
    /**
     * 保存实体对象,数据库的insert操作
     * @param entity 参数类型:泛型，任何实现序列化的类
     * @throws PortalMSException iepg管理系统统一抛出的异常
     */
    public void saveEntity(T entity) throws SSIException
    {
        try
        {
            this.getSqlMapClient().insert(nameSpace + SQLID_INSERT, entity);
        }
        catch (Exception e)
        {
            throw new SSIException(
                    Constants.ERROR_CODE_OPERATOR_DATABASE.getLongValue(), e);
        }
    }
    
    /**
     * 更新实体对象，数据库的update操作
     * @param entity 参数类型:泛型，任何实现序列化的类
     * @throws PortalMSException iepg管理系统统一抛出的异常
     */
    public void updateEntity(T entity) throws SSIException
    {
        try
        {
            this.getSqlMapClient().update(nameSpace + SQLID_UPDATE, entity);
        }
        catch (Exception e)
        {
            throw new SSIException(
                    Constants.ERROR_CODE_OPERATOR_DATABASE.getLongValue(), e);
        }
    }
    
    /**
     * 按实体对象的主键ID批量删除实体
     * 空方法,子类需要时自己去实现
     * @param list 主键的ID列表，列表内存放泛型，任何实现序列化接口的类
     */
    public void batchDelete(final List<PK> list) throws SSIException
    {
        
    }
    
    /**
     * 按实体对象的主键ID批量删除实体
     * 空方法,子类需要时自己去实现
     * @param args 主键的ID列表，列表内存放泛型，任何实现序列化接口的类
     */
    public void batchDelete(final PK[] args) throws SSIException
    {
        
    }
    
    /**
     * 批量插入实体,数据库insert操作
     * 空方法，子类需要使用时自己去实现
     * @param list 实体对象列表，列表内存放泛型，任何实现序列化接口的类
     */
    public void batchInsert(final List<T> list) throws SSIException
    {
        
    }
    
    /**
     * 批量更新实体,数据库update操作
     * 空方法，子类需要使用时自己去实现
     * @param list 实体对象列表，列表内存放泛型，任何实现序列化接口的类
     */
    public void batchUpdate(final List<T> list) throws SSIException
    {
        
    }
    
    @SuppressWarnings("unchecked")
    /**
     * 按查询条件分页查询记录数
     * @param map 查询的参数，封装为map或一个实体对象
     * @param currPage 当前的页码
     * @param pageSize 每页显示的记录数
     * @return List 实体对象的列表
     * @throws IEMPMException
     */
    public List<T> findByCriteria(Object map, int currPage, int pageSize)
            throws SSIException
    {
        //举例：当前页为第2页，每页显示10条，则开始记录数为11，结束记录数为20
        //得到记录的开始数
        int skipResults = (currPage - 1) * pageSize + 1;
        //得到记录的结束数
        int maxResults = currPage * pageSize;
        Map<String, Object> obj = null;
        int totalRows = 0;
        // 判断是否map类型，还是普通的JavaBean。普通的JavaBean则转换成Map类型
        if (map instanceof Map)
        {
            obj = (Map) map;
        }
        else
        {
            obj = ReflectUtil.getObjectAsMap(map);
        }
        //obj.put("siteID", SessionUtil.getLocalSiteID());
        try
        {
            totalRows = this.getRowCount(obj);
        }
        catch (Exception e)
        {
            throw new SSIException(
                    Constants.ERROR_CODE_OPERATOR_DATABASE.getLongValue(), e);
        }
        // 带分页参数的列表
        PageList<T> pageList = new PageList<T>(currPage, pageSize, totalRows);
        // 支持物理分页页码从1开始
        try
        {
            List queryResult = this.getSqlMapClientTemplate()
                    .queryForList(nameSpace + SQLID_FINDALL,
                            obj,
                            skipResults,
                            maxResults);
            if (queryResult != null)
            {
                pageList.addAll(queryResult);
            }
        }
        catch (Exception e)
        {
            throw new SSIException(
                    Constants.ERROR_CODE_ACCESS_DATABASE.getLongValue(), e);
        }
        return pageList;
        
    }
    
    @SuppressWarnings("unchecked")
    /**
     * 按查询条件返回所有记录，不带分页
     * @param map 查询的参数，封装为map或一个实体对象
     * @return List 实体对象的列表
     */
    public List<T> findByCriteria(Object map) throws SSIException
    {
        Map<String, Object> obj = null;
        // 判断是否map类型，还是普通的JavaBean。普通的JavaBean则转换成Map类型
        if (map instanceof Map)
        {
            obj = (Map) map;
        }
        else
        {
            obj = ReflectUtil.getObjectAsMap(map);
        }
        //obj.put("siteID", SessionUtil.getLocalSiteID());
        List<T> resultList = null;
        try
        {
            resultList = getSqlMapClientTemplate().queryForList(nameSpace
                    + SQLID_FINDALL,
                    obj);
        }
        catch (Exception e)
        {
            throw new SSIException(
                    Constants.ERROR_CODE_ACCESS_DATABASE.getLongValue(), e);
        }
        return resultList;
    }
    
    /**
     * 按查询条件得到该SQL语句的总数量
     * @param map 查询条件
     * @return  Integer 记录总数
     * @throws DataAccessException 数据访问异常
     */
    @SuppressWarnings("unchecked")
	public Integer getRowCount(Object map) throws SSIException
    {
        try
        {
        	 Map<String, Object> obj = null;
             // 判断是否map类型，还是普通的JavaBean。普通的JavaBean则转换成Map类型
             if (map instanceof Map)
             {
                 obj = (Map) map;
             }
             else
             {
                 obj = ReflectUtil.getObjectAsMap(map);
             }
            //obj.put("siteID", SessionUtil.getLocalSiteID());
            return (Integer) this.getSqlMapClientTemplate()
                    .queryForObject(nameSpace + SQLID_ROWCOUNT, obj);
        }
        catch (Exception ex)
        {
            throw new SSIException(
                    Constants.ERROR_CODE_ACCESS_DATABASE.getLongValue(), ex);
        }
        
    }
    
    /**
     * 得到查询结果集的行数，此参数用于分页
     * @return Integer记录集的行数
     * @throws PortalMSException
     */
    public Integer getRowCount() throws SSIException
    {
        try
        {
            return (Integer) this.getSqlMapClientTemplate()
                    .queryForObject(nameSpace + SQLID_ROWCOUNT);
        }
        catch (Exception ex)
        {
            throw new SSIException(
                    Constants.ERROR_CODE_ACCESS_DATABASE.getLongValue(), ex);
        }
        
    }
    
}
