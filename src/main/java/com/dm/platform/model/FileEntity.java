package com.dm.platform.model;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "T_FILETABLE")
public class FileEntity implements Serializable{

	/**
	 * CHENGJ
	 */
	private static final long serialVersionUID = 113336492162196181L;
	
	private String id;
	
	private String name;
	
	private String type;
	
	private String url;
	
	private String cDate;
	
	private String cUser;
	
	private String userObject;
	
	private String objField;
	
	private String urlField;
	
	private String filesize;
	
	private String realPath;
	
	private String saveFlag;
	
	private String objType;
	
	private String objId;

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name="NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name="URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name="CDATE")
	public String getcDate() {
		return cDate;
	}

	public void setcDate(String cDate) {
		this.cDate = cDate;
	}
	@Column(name="FILESIZE")
	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	
	@Column(name="CREATEUSER")
	public String getcUser() {
		return cUser;
	}

	public void setcUser(String cUser) {
		this.cUser = cUser;
	}
	@Column(name="REALPATH")
	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}
	@Column(name="SAVEFLAG")
	public String getSaveFlag() {
		return saveFlag;
	}

	public void setSaveFlag(String saveFlag) {
		this.saveFlag = saveFlag;
	}
	@Column(name="USEROBJECT")
	public String getUserObject() {
		return userObject;
	}

	public void setUserObject(String userObject) {
		this.userObject = userObject;
	}
	@Column(name="OBJFIELD")
	public String getObjField() {
		return objField;
	}

	public void setObjField(String objField) {
		this.objField = objField;
	}
	@Column(name="URLFIELD")
	public String getUrlField() {
		return urlField;
	}

	public void setUrlField(String urlField) {
		this.urlField = urlField;
	}

	@SuppressWarnings("unused")
	private String filesize2;
	@Transient
	public String getFilesize2() {
		Long i = Long.valueOf(getFilesize());
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (i < 1024) {
		fileSizeString = df.format((double) i) + "B";
		} else if (i < 1048576) {
		fileSizeString = df.format((double) i / 1024) + "K";
		} else if (i < 1073741824) {
		fileSizeString = df.format((double) i / 1048576) + "M";
		} else {
		fileSizeString = df.format((double) i / 1073741824) + "G";
		}
		return fileSizeString;
	}
	public void setFilesize2(String filesize2) {
		this.filesize2 = filesize2;
	}
	
	
	@Column(name="OBJ_ID",length=32)
	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}
	@Column(name="OBJ_TYPE",length=2)
	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

}
