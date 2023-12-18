package com.leothenardo.ecommerce.gateways.models.asaas;

public class PostPaymentRequest {
	private String customer;
	private String billingType;
	private String dueDate;
	private double value;
	private String description;
	private String externalReference;
	private Discount discount;
	private Fine fine;
	private Interest interest;
	private boolean postalService;

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public Discount getDiscount() {
		return discount;
	}

	public void setDiscount(Discount discount) {
		this.discount = discount;
	}

	public Fine getFine() {
		return fine;
	}

	public void setFine(Fine fine) {
		this.fine = fine;
	}

	public Interest getInterest() {
		return interest;
	}

	public void setInterest(Interest interest) {
		this.interest = interest;
	}

	public boolean isPostalService() {
		return postalService;
	}

	public void setPostalService(boolean postalService) {
		this.postalService = postalService;
	}

	public static class Discount {
		private double value;
		private int dueDateLimitDays;

		public Discount(double value, int dueDateLimitDays) {
			this.value = value;
			this.dueDateLimitDays = dueDateLimitDays;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}

		public int getDueDateLimitDays() {
			return dueDateLimitDays;
		}

		public void setDueDateLimitDays(int dueDateLimitDays) {
			this.dueDateLimitDays = dueDateLimitDays;
		}
	}

	public static class Fine {
		private double value;

		public Fine(double value) {
			this.value = value;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}
	}

	public static class Interest {
		private double value;

		public Interest(double value) {
			this.value = value;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}
	}

	public static class Builder {
		private String customer;
		private String billingType;
		private String dueDate;
		private double value;
		private String description;
		private String externalReference;
		private Discount discount = new Discount(0, 0);
		private Fine fine = new Fine(0);
		private Interest interest = new Interest(0);
		private boolean postalService;

		public Builder withCustomer(String customer) {
			this.customer = customer;
			return this;
		}

		public Builder withBillingType(String billingType) {
			this.billingType = billingType;
			return this;
		}

		public Builder withDueDate(String dueDate) {
			this.dueDate = dueDate;
			return this;
		}

		public Builder withValue(double value) {
			this.value = value;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder withExternalReference(String externalReference) {
			this.externalReference = externalReference;
			return this;
		}

		public Builder withDiscount(double value, int dueDateLimitDays) {
			this.discount.setValue(value);
			this.discount.setDueDateLimitDays(dueDateLimitDays);
			return this;
		}

		public Builder withFine(double value) {
			this.fine.setValue(value);
			return this;
		}

		public Builder withInterest(double value) {
			this.interest.setValue(value);
			return this;
		}

		public Builder withPostalService(boolean postalService) {
			this.postalService = postalService;
			return this;
		}

		public PostPaymentRequest build() {
			PostPaymentRequest paymentRequest = new PostPaymentRequest();
			paymentRequest.customer = this.customer;
			paymentRequest.billingType = this.billingType;
			paymentRequest.dueDate = this.dueDate;
			paymentRequest.value = this.value;
			paymentRequest.description = this.description;
			paymentRequest.externalReference = this.externalReference;
			paymentRequest.discount = this.discount;
			paymentRequest.fine = this.fine;
			paymentRequest.interest = this.interest;
			paymentRequest.postalService = this.postalService;
			return paymentRequest;
		}
	}
}