package com.dm.platform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_dict_item")
public class DictItem implements java.io.Serializable{
	private static final long serialVersionUID = 123L;


	private String itemId;

	
	private String itemCode;

	
	private String itemName;
	
	
	private String dictId;
	
	
	private String itemPid;

	
	private Long itemSeq;

	
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ITEM_ID",length = 32)
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	@Column(name = "ITEM_CODE")
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "ITEM_NAME")
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Column(name = "DICT_ID")
	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	@Column(name = "ITEM_PID")
	public String getItemPid() {
		return itemPid;
	}

	public void setItemPid(String itemPid) {
		this.itemPid = itemPid;
	}

	
	@Column(name = "ITEM_SEQ")
	public Long getItemSeq() {
		return itemSeq;
	}

	public void setItemSeq(Long itemSeq) {
		this.itemSeq = itemSeq;
	}

}