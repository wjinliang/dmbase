package com.dm.platform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_USER_ATTR")
public class UserAttrEntity implements Serializable{

	/**
	 * @author CHENGJ
	 */
	private static final long serialVersionUID = -3118582904885915548L;
	
	private String userId;
	
	private String gender;//性别
	
	private String birthDate;//出生日期
	
	private String Introduce;//个人简介
	
	@Id
	@Column(name = "USER_ID", nullable = false, length = 32)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name = "GENDER", length = 2)
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	@Column(name = "BIRTHDATE", length = 20)
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	@Column(name = "INTRODUCE", length = 140)
	public String getIntroduce() {
		return Introduce;
	}
	public void setIntroduce(String introduce) {
		Introduce = introduce;
	}
	
}
