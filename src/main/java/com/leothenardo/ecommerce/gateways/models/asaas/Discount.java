package com.leothenardo.ecommerce.gateways.models.asaas;

public class Discount {
	private double value;
	private String limitDate;
	private int dueDateLimitDays;
	private String type;

	public Discount() {
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getLimitDate() {
		return limitDate;
	}

	public void setLimitDate(String limitDate) {
		this.limitDate = limitDate;
	}

	public int getDueDateLimitDays() {
		return dueDateLimitDays;
	}

	public void setDueDateLimitDays(int dueDateLimitDays) {
		this.dueDateLimitDays = dueDateLimitDays;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
