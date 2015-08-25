package com.dm.platform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_dict")
public class Dict implements java.io.Serializable {

	private static final long serialVersionUID = -151817970652584863L;
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "DICT_ID",length = 32)
	private String dictId;

	@Column(name = "DICT_NAME", length = 50)
	private String dictName;
	
	@Column(name = "DICT_CODE", length = 50)
	private String dictCode;

	@Column(name = "DICT_DESC", length = 100)
	private String dictDesc;

	@Column(name = "DICT_STATUS")
	private Long dictStatus;
	
	@Column(name = "DICT_SEQ")
	private Long dictSeq;


	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
	
	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getDictDesc() {
		return dictDesc;
	}

	public void setDictDesc(String dictDesc) {
		this.dictDesc = dictDesc;
	}


	public Long getDictStatus() {
		return dictStatus;
	}

	public void setDictStatus(Long dictStatus) {
		this.dictStatus = dictStatus;
	}

	public Long getDictSeq() {
		return dictSeq;
	}

	public void setDictSeq(Long dictSeq) {
		this.dictSeq = dictSeq;
	}

}