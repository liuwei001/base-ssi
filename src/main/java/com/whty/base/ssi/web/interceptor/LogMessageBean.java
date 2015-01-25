package com.whty.base.ssi.web.interceptor;

import java.io.Serializable;

/**
 * 
 * 读取LogDefinitions.properties中定义的操作码信息，如从该资源文件读取的值为：
 * “2, 权限管理--> 用户管理, 增加用户”，那么解析器会将这一条信息封装成
 * LogMessageBean对象。
 * 
 * @author  t2w
 * @version  [V100R001, 2009-11-23]
 * @see  LogMessageParser#convert2LogMessageBean
 * @since  [SDP.BASE.SSI-V100R001]
 */
public class LogMessageBean implements Serializable {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = -2246191922354111643L;
	
	/**操作码*/
	private int messageId;
	/**操作类型：新增、编辑、删除这三种类型*/
	private String operateType;
	/**操作模块，如： 站点管理--> 编辑节点信息*/
	private String operateModule;
	/**操作对象，如：编辑节点信息*/
	private String operateObject;
	
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getOperateModule() {
		return operateModule;
	}
	public void setOperateModule(String operateModule) {
		this.operateModule = operateModule;
	}
	public String getOperateObject() {
		return operateObject;
	}
	public void setOperateObject(String operateObject) {
		this.operateObject = operateObject;
	}
}
