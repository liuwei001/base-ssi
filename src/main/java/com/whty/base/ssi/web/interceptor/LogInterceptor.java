package com.whty.base.ssi.web.interceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;


import com.whty.base.ssi.common.businesslog.entity.BusinessLog;
import com.whty.base.ssi.common.util.RequestUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.whty.base.ssi.common.Constants;
import com.whty.base.ssi.dao.IBaseDAO;
import com.whty.base.ssi.exception.SSIException;
import com.whty.base.ssi.service.IService;
import com.whty.base.ssi.user.entity.User;
import com.whty.base.ssi.web.action.BaseAction;

/**
 * 
 * 日志拦截器，可以拦截系统的所有struts的Action动作。该拦截器具备以下功能： 1、打印Action执行前、后的日志。
 * 2、针对一些必须记录操作的Action，如（新增操作），根据操作码将操作信息写入数据库日志。如果 操作出现异常，那么将操作信息写入数据库日志和系统日志。
 * 
 * @author t2w
 * @version [V100R001, 2009-11-23]
 * @since [SDP.BASE.SSI-V100R001]
 */
public class LogInterceptor extends AbstractInterceptor implements
        LocaleProvider
{
    
    private static final long serialVersionUID = 8317600420657773448L;
    
    /** IEPG系统日志 */
    private static final Logger sysLogger = Logger.getLogger(LogInterceptor.class);
    
    /** IEPG数据库日志 */
    private static final Logger dbLogger = Logger.getLogger(LogInterceptor.class);
    
    /** 操作码 */
    private String logMessageId;
    
    /** 错误码 */
    private Long errorCode;
    
    /** 操作描述 */
    private String operateDesc = "";
    
    /** 操作结果 */
    private boolean operateResult = true;
    
    /** 获取错误代码的资源文件 */
    TextProvider errorCodeProvider = null;
    
    /** 获取操作码的资源文件 */
    TextProvider operateCodePrivider = null;
    
    /** IEPG后台Action基类 */
    private BaseAction baseAction = null;
    
    /** 传入拦截器的参数 */
    private String includeParams = null;
    
    /** 封装查询动作数组 */
    private String[] filterActions = null;
    
    /**
     * 应用启动的时候，初始化拦截器
     */
    public void init()
    {
        errorCodeProvider = new TextProviderFactory().createInstance(ResourceBundle.getBundle("ErrorCodeResources",
                getLocale()),
                this);
        
        operateCodePrivider = new TextProviderFactory().createInstance(ResourceBundle.getBundle("LogDefinitions",
                getLocale()),
                this);
        
        if (this.includeParams != null && this.includeParams.length() != 0)
        {
            String[] temp = this.includeParams.split(",");
            if (temp != null && temp.length != 0)
            {
                this.filterActions = temp;
            }
        }
    }
    
    /**
     * 拦截器对Action进行拦截。
     * 
     * 1、从Session中获取用户的登录对象，如果登录对象为空，直接执行拦截器后，返回登录页面。
     * 2、从登录对象中获取用户ID，以及获取HTTP请求的IP地址，在执行拦截器。 3、获取操作描述、操作码。
     * 4、如果Action执行异常，那么在异常处理中将执行信息写入系统日志、数据库日志。 5、如果Action执行成功，那么将执行信息写入数据库日志。
     * 
     * @param invoke
     *            Action执行的状态
     * @return 返回 String代表Action执行的执行结果
     * @throws Exception
     *             抛出拦截器执行的异常对象，由容器来捕获。
     * 
     * @return String 返回 String代表Action执行的执行结果
     * @exception throws Exception 抛出拦截器执行的异常对象
     */
    @Override
    public String intercept(ActionInvocation invoke) throws Exception
    {
        
        HttpServletRequest request = ServletActionContext.getRequest();
        Map<String, Object> session = ServletActionContext.getContext()
                .getSession();
        Object obj = session.get("loginUser");
        User userInfo = null;
        String result = null;
        String ipAddr = RequestUtil.getInstance().getIPAddress(request);
        if (obj == null)
        {
            try
            {
                return invoke.invoke();
            }
            catch (Exception e)
            {
                String message = handleException(0, ipAddr, invoke, e);
                request.setAttribute(Constants.ERROR_MESSAGE.getStringValue(),
                        message);
                return Action.INPUT;
            }
        }
        else
        {
            userInfo = (User) obj;
        }
        // 获取登录用户的编号
        int userId = -1;
        if (userInfo != null)
        {
            userId = userInfo.getId();
        }
        
        try
        {
            String className = invoke.getAction().getClass().getName();
            ActionProxy proxy = invoke.getProxy();
            String actionMethod = proxy.getMethod();
            baseAction = (BaseAction) invoke.getAction();
            sysLogger.log(Level.DEBUG, "method starts..." + className + "."
                    + actionMethod);
            result = invoke.invoke();
            if (sysLogger.isDebugEnabled())
            {
                sysLogger.log(Level.DEBUG, printActionExceuteResult(proxy,
                        invoke));
                printValueStackAfterActionExceuted(proxy, invoke);
            }
            // 获取描述
            operateDesc = baseAction.getOperateDesc();
            // 获取操作码
            logMessageId = baseAction.getOperateCode();
            // 获取不发生异常情况的操作结果
            operateResult = baseAction.isOperateResult();
        }
        catch (Exception e)
        {
            String message = handleException(userId, ipAddr, invoke, e);
            request.setAttribute(Constants.ERROR_MESSAGE.getStringValue(),
                    message);
            String errorPageWindow = request.getParameter("errorPageWindow");
            
            // 是否采用打开新窗口来显示错误页面
            if (errorPageWindow != null)
            {
                return Constants.GLOBAL_ERROR_WINDOW.getStringValue();
            }
            else
            {
                return Constants.GLOBAL_ERROR.getStringValue();
            }
        }
        
        // 不发生异常情况的处理
        return handleActionResult(userId, ipAddr, invoke, result);
    }
    
    /**
     * 向日志信息中写入Action执行结果的详细信息。
     * 
     * @param proxy
     *            Action代理对象
     * @param invoke
     *            Action执行对象
     * @return [返回Action执行结果]
     * 
     * @return String [返回Action执行结果]
     */
    protected String printActionExceuteResult(ActionProxy proxy,
            ActionInvocation invoke)
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("method ends...");
        buffer.append(invoke.getAction().getClass().getName());
        buffer.append(".");
        buffer.append(proxy.getMethod());
        buffer.append(", and the execute result is ")
                .append(invoke.getResultCode());
        
        return buffer.toString();
    }
    
    /**
     * 打印Action执行后，ValueStack栈中的数据。
     * 
     * @param proxy
     *            Action执行代理对象
     * @param invoke
     *            Action执行对象
     */
    protected void printValueStackAfterActionExceuted(ActionProxy proxy,
            ActionInvocation invoke)
    {
        
        String actionName = invoke.getAction().getClass().getName();
        String actionMethod = proxy.getMethod();
        
        StringBuilder buffer = new StringBuilder();
        Object action = invoke.getAction();
        Method[] methods = action.getClass().getMethods();
        
        for (Method method : methods)
        {
            if (method.getParameterTypes() != null
                    && method.getParameterTypes().length > 0)
            {
                continue;
            }
            printMethodInAction(buffer, method, action);
        }
        
        String result = buffer.toString();
        int index = result.lastIndexOf(",");
        if (index != -1)
        {
            result = result.substring(0, index);
        }
        
        buffer = new StringBuilder();
        buffer.append("invoke '");
        buffer.append(actionName);
        buffer.append(".");
        buffer.append(actionMethod);
        buffer.append("', and it's variables are: ");
        buffer.append(result);
        
        sysLogger.debug(buffer.toString());
    }
    
    protected boolean printMethodInAction(StringBuilder buffer, Method method,
            Object action)
    {
        boolean doProcess = true;
        String methodName = method.getName();
        if (methodName != null && methodName.startsWith("get"))
        {
            try
            {
                Object obj = method.invoke(action);
                if (!isFilter(obj))
                {
                    String fieldName = getFieldName(methodName);
                    boolean doOutput = cannotOutput(fieldName);
                    if (!doOutput)
                    {
                        String className = obj.getClass()
                                .getName()
                                .toLowerCase();
                        if (className.contains("service")
                                || className.contains("dao"))
                        {
                            doProcess = false;
                        }
                        else
                        {
                            append(buffer, fieldName, obj);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                sysLogger.error(methodName, e);
            }
        }
        
        return doProcess;
    }
    
    @SuppressWarnings("unchecked")
    public boolean isFilter(Object obj)
    {
        if (obj == null)
        {
            return true;
        }
        else if (obj instanceof IService)
        {
            return true;
        }
        else if (obj instanceof IBaseDAO)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    protected void append(StringBuilder buffer, String fieldName, Object object)
    {
        buffer.append(fieldName);
        buffer.append("=");
        buffer.append(Arrays.toString(new Object[] { object }));
        buffer.append(",");
    }
    
    protected boolean cannotOutput(String fieldName)
    {
        if ("locale".equalsIgnoreCase(fieldName))
        {
            return true;
        }
        else if ("actionErrors".equalsIgnoreCase(fieldName))
        {
            return true;
        }
        else if ("actionMessages".equalsIgnoreCase(fieldName))
        {
            return true;
        }
        else if ("errorMessages".equalsIgnoreCase(fieldName))
        {
            return true;
        }
        else if ("class".equalsIgnoreCase(fieldName))
        {
            return true;
        }
        else if ("fieldErrors".equalsIgnoreCase(fieldName))
        {
            return true;
        }
        else if ("errors".equalsIgnoreCase(fieldName))
        {
            return true;
        }
        else if ("error".equalsIgnoreCase(fieldName))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    protected String getFieldName(String methodName)
    {
        String firstChar = methodName.substring(3, 4);
        String out = firstChar.toLowerCase() + methodName.substring(4);
        return out;
    }
    
    /**
     * 执行Action后没有发生异常的处理。 1、先根据操作码从操作码定义的资源文件获取内容，解析成Log实体。
     * 2、再根据action执行的操作结果，设置该Log实体的操作结果属性。 3、插入一条数据库日志记录。
     * 
     * 如果不存在操作，则不做任何处理，即：不会往数据库写入日志记录。
     * 
     * @param userId
     *            登录用户的编号
     * @param ipAddr
     *            登录用户的IP地址
     * @param invoke
     *            拦截器调用对象
     * @param result
     *            拦截器执行Action的返回结果
     * @return 拦截器执行Action的返回结果
     * 
     * @return String 拦截器执行Action的返回结果
     */
    protected String handleActionResult(int userId, String ipAddr,
            ActionInvocation invoke, String result)
    {
        // 存在操作码，写入操作日志
        if (logMessageId != null)
        {
            if (!"browse".equalsIgnoreCase(baseAction.getAction()))
            {
                
                // 如果返回的是“input”字符串，表明是校验没有通过，返回输入页面，因此不需要记录数据库日志。
                String resultCode = invoke.getResultCode();
                if ("input".equalsIgnoreCase(resultCode))
                {
                    return result;
                }
                else if (resultCode.toLowerCase().contains("input"))
                {
                    return result;
                }
                
                // 成功写入数据库日志
                BusinessLog log = createLog(userId,
                        ipAddr);
                if (log != null)
                {
                    if (this.operateResult)
                    {
                        log.setResult(Constants.SUCCEED.getStringValue()); // 表示成功
                    }
                    else
                    {
                        log.setResult(Constants.FAILURED.getStringValue());// 不发生异常情况下，Action返回失败的状态码，并设置日志操作结果为失败。
                    }
                    
                    //TODO:记录到数据库
                    //.....
                }
            }
        }
        
        return result;
    }
    
    /**
     * 异常处理 首先判断异常类型，如果是SSIExceptin或系统异常xxxSystemException，那么这样的处理：
     * 1、从异常对象中获取错误代码，然后根据错误代码从资源文件中，读取该错误代码所对应的内容， 然后将内容和异常堆栈信息，写入系统日志。
     * 2、从操作的Action中获取操作码和操作描述，根据操作码从资源文件中读取具体什么操作（操作名称、操作对象），写入数据库日志
     * 如果不是xxxSystemException，那么将错误信息和异常堆栈写入系统日志。
     * 
     * @param userId
     *            登录用户的编号
     * @param ipAddr
     *            登录用户的IP地址
     * @param invoke
     *            拦截器调用对象
     * @param e
     *            异常对象
     * @return 返回错误信息
     * 
     * @return String 返回错误信息
     */
    protected String handleException(int userId, String ipAddr,
            ActionInvocation invoke, Exception e)
    {
        baseAction = (BaseAction) invoke.getAction();
        // 获取描述
        operateDesc = baseAction.getOperateDesc();
        // 获取操作码
        logMessageId = baseAction.getOperateCode();
        // 错误信息
        String message = null;
        if (e instanceof SSIException)
        {
        	SSIException ssiExce = (SSIException) e;
            // 从异常中获取错误代码
        	Long errorCodeTmp = (Long) ssiExce.getCode();
            // 从拦截器中获取错误代码
            if (errorCodeTmp != 0)
            {
                errorCode = errorCodeTmp;
            }
            
            if (errorCode != 0)
            {
                // 发生异常写入系统日志
                message = errorCodeProvider.getText(Constants.ERROR_CODE_PREFIX.getStringValue()
                        + String.valueOf(errorCodeTmp),
                        new String[] { String.valueOf(errorCodeTmp) });
                // 从错误定义的资源文件中没有获取相应的错误信息，则从异常堆栈中获取错误信息。
                if ((Constants.ERROR_CODE_PREFIX.getStringValue() + errorCodeTmp).equalsIgnoreCase(message))
                {
                    message = ssiExce.getMessage();
                }
                // 判断是否是增，删和改操作
                boolean isManipulate = isDataBaseManipulateException(message);
                // 发生异常写入操作描述
                StringBuilder buffer = new StringBuilder();
                buffer.append(message);
                LogMessageBean logMessageBean = parseOperateContentAsLogMessageBean();
                if (logMessageBean != null)
                {
                    buffer.append("Operate：[OperateDescription=");
                    buffer.append(logMessageBean.getOperateModule());
                    buffer.append("]");
                }
                sysLogger.error(buffer.toString(), e);
                
                // 无法与数据库建立连接
                if (!isValidConnection(e))
                {
                    message = getConnectDatabaseErrorMessage();
                    sysLogger.error("can't connect to database, please check the connection is valid!!");
                }
                else
                {
                    // 获取异常信息，并处理异常信息
                    message = getExceptionMessageDisplayInErrorPage(message);
                    // 能够建立与数据库的连接
                    if (isManipulate)
                    {
                        // 发生异常写入数据库日志
                        BusinessLog log = createLog(userId,
                                ipAddr);
                        if (log != null)
                        {
                            String exceptionMessage = pareseExceptionMessage(ssiExce);
                            if (exceptionMessage != null)
                            {
                                String operateDesc = log.getDescription()
                                        + "\n<br/>Exception ["
                                        + exceptionMessage + "]";
                                log.setDescription(operateDesc);
                            }
                            
                            log.setResult(Constants.FAILURED.getStringValue()); // 表示失败
                            //TODO:向DB记录日志
                        }
                    }
                }
            }
            else
            {
                // 从portalMSException中获取获取信息。
                message = e.getMessage();
            }
        }
        else
        {
            ActionProxy proxy = invoke.getProxy();
            
            StringBuilder buffer = new StringBuilder();
            buffer.append(invoke.getAction().getClass().getName());
            buffer.append(".");
            buffer.append(proxy.getMethod());
            if (e.getMessage() == null)
            {
                return null;
            }
            // 发生异常写入系统日志
            sysLogger.error(buffer.toString(), e);
            // 将异常信息显示在错误页面
            message = getExceptionMessageDisplayInErrorPage(e.getMessage());
        }
        
        return message;
    }
    
    protected String getExceptionMessageDisplayInErrorPage(String errorMessage)
    {
        String message = errorMessage;
        int index = message.indexOf("portalMS-");
        if (index == -1)
        {
            message = this.errorCodeProvider.getText(Constants.ERROR_CODE_PREFIX.getStringValue()
                    + String.valueOf(Constants.ERROR_CODE_ACCESS_DATABASE.getLongValue()),
                    new String[] { String.valueOf(Constants.ERROR_CODE_ACCESS_DATABASE.getLongValue()) });
        }
        if (message.equals(String.valueOf(Constants.ERROR_CODE_ACCESS_DATABASE.getLongValue())))
        {
            message = Constants.ERROR_CONNECT_DB.getStringValue();
        }
        
        return message;
    }
    
    protected boolean isDataBaseManipulateException(String exceptionMessage)
    {
        boolean isManipulate = true;
        if (this.filterActions != null && this.filterActions.length != 0)
        {
            for (String action : this.filterActions)
            {
                if (exceptionMessage.contains(action.trim()))
                {
                    isManipulate = false;
                    break;
                }
            }
        }
        
        return isManipulate;
    }
    
    protected String pareseExceptionMessage(Exception e)
    {
        
        StringBuilder buffer = new StringBuilder();
        String message = e.toString();
        
        int start = message.indexOf("java.sql.SQLException");
        int end = message.indexOf("at", 5000);
        if (start != -1 && end != -1)
        {
            buffer.append(message.substring(start, end));
        }
        else if (start != -1 && end == -1)
        {
            buffer.append(message.substring(start));
        }
        else
        {
            buffer.append(e.getMessage());
        }
        
        StackTraceElement[] elements = e.getStackTrace();
        for (int i = 0, size = elements.length; i < 5 && i < size; i++)
        {
            StackTraceElement element = elements[i];
            buffer.append("\n<br/>");
            buffer.append("at&nbsp;&nbsp;&nbsp;&nbsp;");
            buffer.append(element);
        }
        
        return buffer.toString();
    }
    
    protected String getConnectDatabaseErrorMessage()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("DHM.Studio-");
        buffer.append(Constants.ERROR_CODE_CONNECT_DATABASE.getLongValue());
        buffer.append("：");
        buffer.append(this.errorCodeProvider.getText("cant.connected.db.message"));
        
        return buffer.toString();
    }
    
    /**
     * 根据异常对象，来判断该异常是否是由于无法连接到数据库产生的。
     * 
     * @param e
     *            异常对象
     * @return 返回true表示能够正常连接到数据库，返回false无法连接到数据库
     * 
     * @return boolean 返回true表示能够正常连接到数据库，返回false无法连接到数据库
     */
    protected boolean isValidConnection(Exception e)
    {
        if (e == null)
        {
            return true;
        }
        boolean isValid = true;
        String message = e.toString();
        String cantConnectedMessage = this.errorCodeProvider.getText("cant.connected.db.message");
        if (message != null && message.contains(cantConnectedMessage))
        {
            isValid = false;
        }
        
        return isValid;
    }
    
    /**
     * 根据操作码解析出操作内容，并将操作内容解析成LogMessageBean对象
     * 
     * @return 返回解析的LogMessageBean对象
     * 
     * @return LogMessageBean 返回解析的LogMessageBean对象
     */
    protected LogMessageBean parseOperateContentAsLogMessageBean()
    {
        if (logMessageId == null)
        {
            return null;
        }
        
        String messageContent = this.operateCodePrivider.getText(Constants.OPERATE_CODE_PREFIX.getStringValue()
                + String.valueOf(logMessageId));
        return LogMessageParser.convert2LogMessageBean(messageContent);
    }
    
    /**
     * 创建日志实体，日志实体的信息来源与日志配置文件。
     * 根据操作码，从LogDefinitions.properties读取操作信息，并将信息封装成LogMessageBean对象。
     * 
     * @param userId
     *            登录用户的ID号
     * @param ipAddr
     *            客户端请求的IP地址
     * @return LogMessageBean对象，封装具体的操作信息。
     * 
     * @return com.coship.dhm.portalMS.log.entity.BusinessLog
     *         LogMessageBean对象，封装具体的操作信息
     */
    protected BusinessLog createLog(int userId,String ipAddr)
    {
        if (logMessageId == null && userId != 0)
        {
            return null;
        }
        
        String messageContent = this.operateCodePrivider.getText(Constants.OPERATE_CODE_PREFIX.getStringValue()
                + String.valueOf(logMessageId));
        LogMessageBean messageBean = LogMessageParser.convert2LogMessageBean(messageContent);
        if (messageBean != null)
        {
            BusinessLog log = LogMessageParser.createLog(userId,
                    ipAddr,
                    logMessageId,
                    messageContent,
                    this.operateDesc);
            
            return log;
        }
        else
        {
            sysLogger.warn("there is no '" + logMessageId
                    + "' define in LogDefinitions.xml");
            return null;
        }
    }
    
    /**
     * 获取语言
     */
    public Locale getLocale()
    {
        ActionContext ctx = ActionContext.getContext();
        if (ctx != null)
        {
            return ctx.getLocale();
        }
        else
        {
            sysLogger.debug("Action context not initialized");
            return null;
        }
    }
    
    public String getLogMessageId()
    {
        return logMessageId;
    }
    
    public void setLogMessageId(String logMessageId)
    {
        this.logMessageId = logMessageId;
    }
    
    
    public String getIncludeParams()
    {
        return includeParams;
    }
    
    public void setIncludeParams(String includeParams)
    {
        this.includeParams = includeParams;
    }
    
    public String[] getFilterActions()
    {
        return filterActions;
    }
    
    public void setFilterActions(String[] filterActions)
    {
        this.filterActions = filterActions;
    }
}
