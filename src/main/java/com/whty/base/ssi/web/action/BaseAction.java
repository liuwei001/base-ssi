package com.whty.base.ssi.web.action;

import java.io.Serializable;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;
import com.whty.base.ssi.common.Constants;
import com.whty.base.ssi.common.util.PageList;

/**
 * 
 * 所有Action的基类
 * 
 * @author  t2w
 * @version  [V100R001, 2012-09-04]
 * @see  ActionSupport
 * @since  [SDP.BASE.SSI-V100R001]
 */
public class BaseAction extends  ActionSupport{

	private static final long serialVersionUID = 5411812877029151131L;


	/**分页程序中当前页，默认显示第一页*/
	protected Integer currentPage = 1;
	/**分页程序每页显示的条数，默认情况一页显示10条记录*/
	protected Integer pageSize = 10;
	/**分页记录*/
	protected PageList<Serializable> pager;
	/**操作描述*/
	protected String operateDesc = "";
	/**操作码*/
	protected String operateCode;
	/**动作名称*/
	protected String action;
	/**在不发生异常的情况，记录日志操作结果的状态，默认操作结果为：“成功”。*/
	protected boolean operateResult = true;
	
	/**弹出成功提示的对话框，2表示不弹出任何提示框，1表示弹出成功提示框，0表示弹出失败提示框*/
	protected String showSuccessDialog = Constants.NO_DIALOG.getStringValue();
	
	/**获取操作代码的资源文件*/
//	protected TextProvider operateCodeProvider = new TextProviderFactory().createInstance(
//			ResourceBundle.getBundle( "LogDefinitions", getLocale()), this
//	);
	
	/**从Session中获取登录用户的ID*/
//	protected Long getOperatorId() {
//		Map<String, Object> session = ActionContext.getContext().getSession();
//		Admin user = (Admin) session.get( "loginUser" );
//		
//		if ( user == null ) {
//			return -1l;
//		}
//		
//		return (long)user.getId();
//	}
	
	/**从Session中获取登录用户*/
//	protected String getOperator() {
//		Map<String, Object> session = ActionContext.getContext().getSession();
//		Admin user = (Admin) session.get( "loginUser" );		
//		if ( user == null ) {
//			return "unknow";
//		}
//		
//		return user.getUsername();
//	}
	
	/**
	 * 用户日志记录的操作描述
	 * @return String 操作描述
	 */
	public String getOperateDesc() {
		return operateDesc;
	}
	
	/**
	 * 清除session中保存的登录者信息、权限信息
	 */
	@SuppressWarnings("unchecked")
	protected void sessionClear() {
		Map<String, Object> map = ActionContext.getContext().getSession();
		map.remove("loginUser");
		map.remove("assignedPrivileges");
		map.remove("privielgeTree");
		map.remove("allPrivileges");
		map.remove("welcomeURL");

		Map<String, Set<String>> privilegeURLCache = (Map<String, Set<String>>) map
				.get("privilegeURLCache");
		if (privilegeURLCache != null) {
			privilegeURLCache.clear();
		}

		getHttpServletRequest().getSession().invalidate();
	}
	
	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
	}

	public PageList<Serializable> getPager() {
		return pager;
	}

	public void setPager(PageList<Serializable> pager) {
		this.pager = pager;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	protected HttpServletRequest getHttpServletRequest() {
		return ServletActionContext.getRequest();
	}
	
	protected HttpServletResponse getHttpServletResponse() {
		return ServletActionContext.getResponse();
	}
	
	protected HttpSession getHttpSession() {
		return getHttpServletRequest().getSession();
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}

	public String getOperateCode() {
		return operateCode;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}

	public boolean isOperateResult() {
		return operateResult;
	}

	public void setOperateResult(boolean operateResult) {
		this.operateResult = operateResult;
	}

	public String getShowSuccessDialog() {
		return showSuccessDialog;
	}

	public void setShowSuccessDialog(String showSuccessDialog) {
		this.showSuccessDialog = showSuccessDialog;
	}

}
