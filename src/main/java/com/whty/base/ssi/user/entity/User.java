package com.whty.base.ssi.user.entity;

import java.util.List;

/**
 * 
 * 用户实体
 * 
 * @author 904201
 * @version [V200R001, 2009-11-09]
 * @since [DHM.Core.portalMS-V200R001]
 */
public class User implements java.io.Serializable {

	private static final long serialVersionUID = -4674395738786625526L;

	/**
	 * 用户id
	 */
	private Integer id;

	/**
	 * 用户名
	 */
	private String loginName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 用户名称
	 */
	private String username;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 电话号码
	 */
	private String phone;

	/**
	 * Email邮箱
	 */
	private String email;

	/**
	 * 确认密码
	 */
	private String rePassword;

	/**
	 * 旧密码
	 */
	private String oldPassword;

	/**
	 * 自动生成密码
	 */
	private String autoPassword;

	/**
	 * 验证码
	 */
	private String rand;

	/**
	 * 用户角色
	 */
	private String role;

	private String roleListStr;

	/**
	 * 操作码
	 */
	private String operateCode;

	/**
	 * @return 返回 operateCode
	 */
	public String getOperateCode() {
		return operateCode;
	}

	/**
	 * @param operateCode
	 */
	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}

	/**
	 * @return 返回 roleListStr
	 */
	public String getRoleListStr() {
		return roleListStr;
	}

	/**
	 * @param roleListStr
	 */
	public void setRoleListStr(String roleListStr) {
		this.roleListStr = roleListStr;
	}

	/**
	 * @return 返回 role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return 返回 rand
	 */
	public String getRand() {
		return rand;
	}

	/**
	 * @param rand
	 */
	public void setRand(String rand) {
		this.rand = rand;
	}

	/**
	 * @return 返回 oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}

	/**
	 * @param oldPassword
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * @return 返回 rePassword
	 */
	public String getRePassword() {
		return rePassword;
	}

	/**
	 * @param rePassword
	 */
	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	/**
	 * @return 返回 autoPassword
	 */
	public String getAutoPassword() {
		return autoPassword;
	}

	/**
	 * @param autoPassword
	 */
	public void setAutoPassword(String autoPassword) {
		this.autoPassword = autoPassword;
	}

	public User() {
	}

	public User(Integer id) {
		this.id = id;
	}

	/**
	 * @return 返回 id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return 返回 loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @param loginName
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * @return 返回 password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return 返回 username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return 返回 status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return 返回 phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return 返回 email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return String
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Admin [id=");
		builder.append(id);
		builder.append(", loginName=");
		builder.append(loginName);
		builder.append(", username=");
		builder.append(username);
		builder.append("]");
		return builder.toString();
	}

}
