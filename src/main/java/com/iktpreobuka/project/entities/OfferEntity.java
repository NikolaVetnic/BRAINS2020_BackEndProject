package com.iktpreobuka.project.entities;

import java.util.Date;

public class OfferEntity {

	
	protected Integer id;
	protected String name;
	protected String desc;
	protected Date created;
	protected Date expires;
	protected Double regPrice;
	protected Double actPrice;
	protected String imgPath;
	protected Integer numAvailable;
	protected Integer numBought;
	protected OfferType type;
	
	
	public OfferEntity() { }


	public OfferEntity(int id, String name, String desc, Date created, Date expires, double regPrice, double actPrice, String imgPath,
			int numAvailable, int numBought, OfferType type) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.created = created;
		this.expires = expires;
		this.regPrice = regPrice;
		this.actPrice = actPrice;
		this.imgPath = imgPath;
		this.numAvailable = numAvailable;
		this.numBought = numBought;
		this.type = type;
	}


	public Integer getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public String getDesc() {
		return desc;
	}


	public Date getCreated() {
		return created;
	}


	public Date getExpires() {
		return expires;
	}


	public Double getRegPrice() {
		return regPrice;
	}


	public Double getActPrice() {
		return actPrice;
	}


	public String getImgPath() {
		return imgPath;
	}


	public Integer getNumAvailable() {
		return numAvailable;
	}


	public Integer getNumBought() {
		return numBought;
	}


	public OfferType getType() {
		return type;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public void setCreated(Date created) {
		this.created = created;
	}


	public void setExpires(Date expires) {
		this.expires = expires;
	}


	public void setRegPrice(Double regPrice) {
		this.regPrice = regPrice;
	}


	public void setActPrice(Double actPrice) {
		this.actPrice = actPrice;
	}


	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}


	public void setNumAvailable(Integer numAvailable) {
		this.numAvailable = numAvailable;
	}


	public void setNumBought(Integer numBought) {
		this.numBought = numBought;
	}


	public void setType(OfferType type) {
		this.type = type;
	}
	
	
	
}
