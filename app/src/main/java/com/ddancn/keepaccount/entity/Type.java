package com.ddancn.keepaccount.entity;

import org.litepal.crud.LitePalSupport;

public class Type extends LitePalSupport {
	
	private int id;
	private String name;
	private int type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
