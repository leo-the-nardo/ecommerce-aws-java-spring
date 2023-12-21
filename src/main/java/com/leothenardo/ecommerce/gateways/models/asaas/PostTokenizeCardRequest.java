package com.leothenardo.ecommerce.gateways.models.asaas;

public class PostTokenizeCardRequest {
	private String customer;
	private CreditCard creditCard;
	private CreditCardHolderInfo creditCardHolderInfo;
	private String remoteIp;

	public PostTokenizeCardRequest() {
	}

	private PostTokenizeCardRequest(Builder builder) {
		this.customer = builder.customer;
		this.creditCard = builder.creditCard;
		this.creditCardHolderInfo = builder.creditCardHolderInfo;
		this.remoteIp = builder.remoteIp;
	}

	@Override
	public String toString() {
		return "";
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public CreditCardHolderInfo getCreditCardHolderInfo() {
		return creditCardHolderInfo;
	}

	public void setCreditCardHolderInfo(CreditCardHolderInfo creditCardHolderInfo) {
		this.creditCardHolderInfo = creditCardHolderInfo;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public static class Builder {
		private String customer;
		private CreditCard creditCard;
		private CreditCardHolderInfo creditCardHolderInfo;
		private String remoteIp;

		public Builder() {
		}

		public Builder withCustomer(String customer) {
			this.customer = customer;
			return this;
		}

		public Builder withCreditCard(CreditCard creditCard) {
			this.creditCard = creditCard;
			return this;
		}

		public Builder withCreditCardHolderInfo(CreditCardHolderInfo creditCardHolderInfo) {
			this.creditCardHolderInfo = creditCardHolderInfo;
			return this;
		}

		public Builder withRemoteIp(String remoteIp) {
			this.remoteIp = remoteIp;
			return this;
		}

		public PostTokenizeCardRequest build() {
			return new PostTokenizeCardRequest(this);
		}
	}

	public static class CreditCard {
		private String holderName;
		private String number;
		private String expiryMonth;
		private String expiryYear;
		private String ccv;

		public CreditCard() {
		}

		private CreditCard(Builder builder) {
			this.holderName = builder.holderName;
			this.number = builder.number;
			this.expiryMonth = builder.expiryMonth;
			this.expiryYear = builder.expiryYear;
			this.ccv = builder.ccv;
		}

		public String getHolderName() {
			return holderName;
		}

		public void setHolderName(String holderName) {
			this.holderName = holderName;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getExpiryMonth() {
			return expiryMonth;
		}

		public void setExpiryMonth(String expiryMonth) {
			this.expiryMonth = expiryMonth;
		}

		public String getExpiryYear() {
			return expiryYear;
		}

		public void setExpiryYear(String expiryYear) {
			this.expiryYear = expiryYear;
		}

		public String getCcv() {
			return ccv;
		}

		public void setCcv(String ccv) {
			this.ccv = ccv;
		}

		@Override
		public String toString() {
			return "";
		}

		public static class Builder {
			private String holderName;
			private String number;
			private String expiryMonth;
			private String expiryYear;
			private String ccv;

			public Builder() {
			}

			public Builder withHolderName(String holderName) {
				this.holderName = holderName;
				return this;
			}

			public Builder withNumber(String number) {
				this.number = number;
				return this;
			}

			public Builder withExpiryMonth(String expiryMonth) {
				this.expiryMonth = expiryMonth;
				return this;
			}

			public Builder withExpiryYear(String expiryYear) {
				this.expiryYear = expiryYear;
				return this;
			}

			public Builder withCcv(String ccv) {
				this.ccv = ccv;
				return this;
			}

			public CreditCard build() {
				return new CreditCard(this);
			}
		}
	}

	public static class CreditCardHolderInfo {
		private String name;
		private String email;
		private String cpfCnpj;
		private String postalCode;
		private String addressNumber;
		private String addressComplement;
		private String phone;
		private String mobilePhone;

		public CreditCardHolderInfo() {
		}

		private CreditCardHolderInfo(Builder builder) {
			this.name = builder.name;
			this.email = builder.email;
			this.cpfCnpj = builder.cpfCnpj;
			this.postalCode = builder.postalCode;
			this.addressNumber = builder.addressNumber;
			this.addressComplement = builder.addressComplement;
			this.phone = builder.phone;
			this.mobilePhone = builder.mobilePhone;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getCpfCnpj() {
			return cpfCnpj;
		}

		public void setCpfCnpj(String cpfCnpj) {
			this.cpfCnpj = cpfCnpj;
		}

		public String getPostalCode() {
			return postalCode;
		}

		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}

		public String getAddressNumber() {
			return addressNumber;
		}

		public void setAddressNumber(String addressNumber) {
			this.addressNumber = addressNumber;
		}

		public String getAddressComplement() {
			return addressComplement;
		}

		public void setAddressComplement(String addressComplement) {
			this.addressComplement = addressComplement;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getMobilePhone() {
			return mobilePhone;
		}

		public void setMobilePhone(String mobilePhone) {
			this.mobilePhone = mobilePhone;
		}

		public static class Builder {
			private String name;
			private String email;
			private String cpfCnpj;
			private String postalCode;
			private String addressNumber;
			private String addressComplement;
			private String phone;
			private String mobilePhone;

			public Builder() {
			}

			public Builder withName(String name) {
				this.name = name;
				return this;
			}

			public Builder withEmail(String email) {
				this.email = email;
				return this;
			}

			public Builder withCpfCnpj(String cpfCnpj) {
				this.cpfCnpj = cpfCnpj;
				return this;
			}

			public Builder withPostalCode(String postalCode) {
				this.postalCode = postalCode;
				return this;
			}

			public Builder withAddressNumber(String addressNumber) {
				this.addressNumber = addressNumber;
				return this;
			}

			public Builder withAddressComplement(String addressComplement) {
				this.addressComplement = addressComplement;
				return this;
			}

			public Builder withPhone(String phone) {
				this.phone = phone;
				return this;
			}

			public Builder withMobilePhone(String mobilePhone) {
				this.mobilePhone = mobilePhone;
				return this;
			}

			public CreditCardHolderInfo build() {
				return new CreditCardHolderInfo(this);
			}
		}
	}
}