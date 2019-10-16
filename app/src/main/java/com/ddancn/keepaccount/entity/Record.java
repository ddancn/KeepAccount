package com.ddancn.keepaccount.entity;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * @author ddan.zhuang
 */
public class Record extends LitePalSupport implements Serializable{

	private int id;
	private String date;
	private double money;
	private String detail;
	private int type;
	private String typeName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	
}
