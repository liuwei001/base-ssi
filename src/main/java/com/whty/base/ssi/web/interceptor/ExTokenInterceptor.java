package com.whty.base.ssi.web.interceptor;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.apache.struts2.util.TokenHelper;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

/**
 * 
 * 防止重复提交的拦截器，通过扩展了struts2的重复提交的拦截器。
 * 该拦截器的使用如下：
 * 	1、不进行重复提交校验，那么该拦截器将不做任何拦截操作。
 *  2、如果进行重复提交校验，需要做的操作如下：
 *     1)、在jsp页面中form中加入：<s:token />，那么就进行重复提交的校验操作。
 *     2)、校验的规则是这样的，首先会重http请求参数中获取该token的值，然后再从
 *     session中获取同名的token值，然后两个值进行比较。
 *     3)、如果两个一样，则校验通过，否则struts2将会将跳转到“input”配置的页面去。
 *     
 * 
 * @author  t2w
 * @version  [V100R001, 2009-12-1]
 * @since  [SDP.BASE.SSI-V100R001]
 */
public class ExTokenInterceptor extends TokenInterceptor {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = -6688464557536335754L;
	/**日志记录对象*/
	private static final Logger logger = Logger.getLogger( ExTokenInterceptor.class );
	
	@Override
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Intercepting invocation to check for valid transaction token.");
        }

        HttpSession session = ServletActionContext.getRequest().getSession(true);

        synchronized (session) {
        	String tokenName = getTokenName();
        	if( tokenName == null ) {
        		return handleValidToken(invocation);
        	} 
            
        	if (!validToken()) {
                return handleInvalidToken(invocation);
            } else {
            	return handleValidToken(invocation);
            }
        }
    }
    
    @Override
    protected String handleInvalidToken(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        String errorMessage = LocalizedTextUtil.findText(this.getClass(), "struts.messages.invalid.token",
                invocation.getInvocationContext().getLocale(),
                "The form has already been processed or no token was supplied, please try again.", new Object[0]);

        if (action instanceof ValidationAware) {
            ((ValidationAware) action).addActionError(errorMessage);
        } else {
        	logger.warn(errorMessage);
        }

        return Action.INPUT;
    }
    
    public static boolean validToken() {
        String tokenName = TokenHelper.getTokenName();
        if ( tokenName == null ) {
        	return true;
        }

        String token = TokenHelper.getToken(tokenName);

        if (token == null) {
            return false;
        }

        Map<String, Object> session = ActionContext.getContext().getSession();
        String sessionToken = (String) session.get(tokenName);

        if (!token.equals(sessionToken)) {
        	logger.warn(LocalizedTextUtil.findText(TokenHelper.class, "struts.internal.invalid.token", ActionContext.getContext().getLocale(), "Form token {0} does not match the session token {1}.", new Object[]{
                    token, sessionToken
            }));
        	
            return false;
        }
        
        // remove the token so it won't be used again
        session.remove(tokenName);
        return true;
    }
    
    public static String getTokenName() {
        Map<String, Object> params = ActionContext.getContext().getParameters();

        if (!params.containsKey( "struts.token.name" )) {
            return null;
        }

        String[] tokenNames = (String[]) params.get("struts.token.name");
        String tokenName;

        if ((tokenNames == null) || (tokenNames.length < 1)) {
            return null;
        }

        tokenName = tokenNames[0];

        return tokenName;
    }
}

