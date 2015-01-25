package com.whty.base.ssi.common.businesslog.entity;

import java.io.Serializable;
import java.util.Date;

import com.whty.base.ssi.common.Constants;


/**
 * 
 * 日志实体
 * 
 * @author  t2w
 * @version  [V100R001, 2009-11-09]
 * @since  [SDP.BASE.SSI-V100R001]
 */
public class BusinessLog implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 4460525401864778549L;
	
	/**日志编号*/
	private long logId;
	/**操作者姓名*/
	private String operator;
	/**操作者ID*/
	private int userId;
	/**操作模块名*/
	private String operatModuleName;
	/**操作对象*/
	private String operateObject;
	/**操作者IP地址*/
	private String ipAddr;
	/**操作时间*/
	private Date operateTime;
	/**操作描述*/
	private String description;
	/**操作类型 */
	private String operateType;
	/**操作结果*/
	private String result;
	
	/**操作类型在页面上显示*/
	private String viewOperateType;
	/**操作结果在页面上显示*/
	private String viewOperateResult;
	
	/**页面上显示的操作时间字符串，格式为：yyyy-MM-dd hh:mm:ss*/
	private String viewOperateTime;
	
	/**页面：起始时间*/
	private String startTime;
	/**页面：结束时间*/
	private String endTime;
	
	public String getViewOperateTime() {
		return viewOperateTime;
	}

	public void setViewOperateTime(String viewOperateTime) {
		this.viewOperateTime = viewOperateTime;
	}

	public String getViewOperateResult() {
		if ( Constants.SUCCEED.getStringValue().equals(this.result)) {
			this.viewOperateResult = Constants.VIEW_OPERATE_RESULT_SUCCESS.getStringValue();
		} else {
			this.viewOperateResult = Constants.VIEW_OPERATE_RESULT_FAILURE.getStringValue();
		} 
		
		return viewOperateResult;
	}
	
	public void setViewOperateResult(String viewOperateResult) {
		this.viewOperateResult = viewOperateResult;
	}
	
	public String getViewOperateType() {
		if( Constants.ADD_OPERATE_TYPE.getStringValue().equals( this.operateType ) ) {
			this.viewOperateType = Constants.VIEW_ADD_OPERATE_TYPE.getStringValue();
		} else if ( Constants.UPDATE_OPERATE_TYPE.getStringValue().equals( this.operateType ) ) {
			this.viewOperateType = Constants.VIEW_UPDATE_OPERATE_TYPE.getStringValue();
		} else if ( Constants.DELETE_OPERATE_TYPE.getStringValue().equals( this.operateType ) ) {
			this.viewOperateType = Constants.VIEW_DELETE_OPERATE_TYPE.getStringValue();
		} else {
			this.viewOperateType = Constants.VIEW_OTHER_OPERATE_TYPE.getStringValue();
		}
		
		return viewOperateType;
	}
	public void setViewOperateType(String viewOperateType) {
		this.viewOperateType = viewOperateType;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public long getLogId() {
		return logId;
	}
	public void setLogId(long logId) {
		this.logId = logId;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperatModuleName() {
		return operatModuleName;
	}
	public void setOperatModuleName(String operatModuleName) {
		this.operatModuleName = operatModuleName;
	}
	public String getOperateObject() {
		return operateObject;
	}
	public void setOperateObject(String operateObject) {
		this.operateObject = operateObject;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append( "BusinessLog [");
		buffer.append( "userId=" );
		buffer.append( this.userId );
		buffer.append( "," );
		buffer.append( "operatModuleName=");
		buffer.append( this.operatModuleName );
		buffer.append( ",");
		buffer.append( "operateObject=");
		buffer.append( this.operateObject );
		buffer.append( ",");
		buffer.append( "operateType=");
		buffer.append( this.operateType );
		buffer.append( ",");
		buffer.append( "result=" );
		buffer.append( this.result );
		buffer.append( ", ");
		buffer.append( "ipAddr=");
		buffer.append( this.ipAddr );
		buffer.append( "]");
		
		return buffer.toString();
	}
}

