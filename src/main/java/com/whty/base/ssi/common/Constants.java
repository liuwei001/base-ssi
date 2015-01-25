package com.whty.base.ssi.common;

/**
 * 
 * 系统常量的定义
 * 
 * @author t2w
 * @version [V100R001, 2012-09-04]
 * @since [SDP.BASE.SSI-V100R001]
 */
public enum Constants {
	
    /**-1表示查询所有的操作结果*/
    OPERATE_RESULT_ALL("-1"),
    /**表示执行成功，弹出成功的提示框*/
    SUCCEED("1"),
    /**表示执行失败，弹出失败的提示框*/
    FAILURED("0"),
	
    /********************************* 权限过滤器的常量定义 **************************************/
    /**表示无权操作的提示页面 */
    NO_PRIVILEGE_URL("/common/noprivilege.jsp"),
    /**表示无权操作的提示页面，页面内容为纯文本不含HTML标签，主要是用于弹出框的无权提示 */
    NO_PRIVILEGE_URL_ONLY_TEXT("/common/noprivilege_text.jsp"),
    /**系统登录首页*/
    FIRST_LOGIN_URL("/"),
    /**系统注销页面*/
    LOGOUT_URL("/common/logout.jsp"), LOGIN_ACTION("/login.action"),

    /********************************* 拦截器的常量定义 **************************************/
    /**IEPG后台日志错误代码前缀*/
    ERROR_CODE_PREFIX("system.error."),
    /**操作代码前缀*/
    OPERATE_CODE_PREFIX("system.operate.code."),
    /**错误内容*/
    ERROR_MESSAGE("errorMessage"),
    /**数据库连接超时*/
    ERROR_CONNECT_DB("portalMS-13003003：数据库连接超时异常"),
    /**数据库访问异常*/
    ERROR_FETCH_DB("portalMS-13003002：数据库访问异常"),
    
    /********************************* 数据库错误的常量定义 **************************************/
	/**
	 * 13002001 程序错误,类型转换异常
	 */
	ERROR_CODE_CLASS_CAST(13002001L),
	/**
	 * 13002002 程序错误,空指针异常
	 */
	ERROR_CODE_NULL_POINT(13002002L),
	/**
	 * 13003001 数据库错误,数据操作异常
	 */
	ERROR_CODE_OPERATOR_DATABASE(13003001L),
	/**
	 * 13003002 数据库错误,数据库访问异常
	 */
	ERROR_CODE_ACCESS_DATABASE(13003002L),
	/**
	 * 13003003 数据库错误,数据库连接超时异常
	 */
	ERROR_CODE_CONNECT_DATABASE(13003003L),

	/**表示全局错误，并由struts2来跳转指定页面*/
    GLOBAL_ERROR("global.error"),
    /**表示全局错误，该全局错误是新打开一个浏览器窗口，然后由struts2来跳转到该错误页面*/
    GLOBAL_ERROR_WINDOW("global.error.window"),
    /**表示浏览文件夹节点信息，并由struts2来跳转指定页面*/
    FOLDER_SITE("foler.site"),
    /**表示浏览文件节点信息，并由struts2来跳转指定页面*/
    PAGE_SITE("page.site"),

    /********************************* 日志部分操作类型的定义 **********************************/
    /**操作类型*/
    ALL_OPERATE_TYPE("0"),
    /**1 表示“新增”操作类型*/
    ADD_OPERATE_TYPE("1"),
    /**2 表示“编辑”操作类型*/
    UPDATE_OPERATE_TYPE("2"),
    /**3 表示“删除”操作类型*/
    DELETE_OPERATE_TYPE("3"),
    /**4 表示“其他”操作类型*/
    OTHER_OPERATE_TYPE("4"),

    /**用于页面显示的操作类型：“新增”*/
    VIEW_ADD_OPERATE_TYPE("新增"),
    /**用于页面显示的操作类型：“修改”*/
    VIEW_UPDATE_OPERATE_TYPE("修改"),
    /**用于页面显示的操作类型：“删除”*/
    VIEW_DELETE_OPERATE_TYPE("删除"),
    /**用于页面显示的操作类型：“其他”*/
    VIEW_OTHER_OPERATE_TYPE("其他"),

    /**用于页面显示的操作结果：“成功”*/
    VIEW_OPERATE_RESULT_SUCCESS("成功"),
    /**用于页面显示的操作结果：“失败”*/
    VIEW_OPERATE_RESULT_FAILURE("失败"),
    
    /**默认情况不弹出提示框*/
    NO_DIALOG("2");
	
	private byte byteValue;

	public byte getByteValue() {
		return this.byteValue;
	}

	Constants(byte byteValue) {
		this.byteValue = byteValue;
	}

	private int intValue;

	public int getIntValue() {
		return this.intValue;
	}

	/**
	 * @param intValue
	 */
	Constants(int intValue) {
		this.intValue = intValue;
	}

	private String strValue;

	private long longValue;

	public String getStringValue() {
		return this.strValue;
	}

	public long getLongValue() {
		return this.longValue;
	}

	Constants(String value) {
		this.strValue = value;
	}

	Constants(long errorCode) {
		this.longValue = errorCode;
	}

	public String toString() {
		return this.strValue;
	}
}
