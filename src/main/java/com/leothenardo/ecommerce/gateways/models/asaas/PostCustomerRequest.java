package com.leothenardo.ecommerce.gateways.models.asaas;

public class PostCustomerRequest {
	private final String name;
	private final String cpfCnpj;
	private String email;
	private String phone;
	private String mobilePhone;
	private String address;
	private String addressNumber;
	private String complement;
	private String province;
	private String postalCode;
	private String externalReference;
	private boolean notificationDisabled;
	private String additionalEmails;
	private String municipalInscription;
	private String stateInscription;
	private String observations;
	private String groupName;
	private String company;

	private PostCustomerRequest(Builder builder) {
		this.name = builder.name;
		this.cpfCnpj = builder.cpfCnpj;
		this.email = builder.email;
		this.phone = builder.phone;
		this.mobilePhone = builder.mobilePhone;
		this.address = builder.address;
		this.addressNumber = builder.addressNumber;
		this.complement = builder.complement;
		this.province = builder.province;
		this.postalCode = builder.postalCode;
		this.externalReference = builder.externalReference;
		this.notificationDisabled = builder.notificationDisabled;
		this.additionalEmails = builder.additionalEmails;
		this.municipalInscription = builder.municipalInscription;
		this.stateInscription = builder.stateInscription;
		this.observations = builder.observations;
		this.groupName = builder.groupName;
		this.company = builder.company;
	}

	public String getName() {
		return name;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressNumber() {
		return addressNumber;
	}

	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public boolean isNotificationDisabled() {
		return notificationDisabled;
	}

	public void setNotificationDisabled(boolean notificationDisabled) {
		this.notificationDisabled = notificationDisabled;
	}

	public String getAdditionalEmails() {
		return additionalEmails;
	}

	public void setAdditionalEmails(String additionalEmails) {
		this.additionalEmails = additionalEmails;
	}

	public String getMunicipalInscription() {
		return municipalInscription;
	}

	public void setMunicipalInscription(String municipalInscription) {
		this.municipalInscription = municipalInscription;
	}

	public String getStateInscription() {
		return stateInscription;
	}

	public void setStateInscription(String stateInscription) {
		this.stateInscription = stateInscription;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	// Getters for all fields

	public static class Builder {
		private final String name;
		private final String cpfCnpj;
		private String email;
		private String phone;
		private String mobilePhone;
		private String address;
		private String addressNumber;
		private String complement;
		private String province;
		private String postalCode;
		private String externalReference;
		private boolean notificationDisabled;
		private String additionalEmails;
		private String municipalInscription;
		private String stateInscription;
		private String observations;
		private String groupName;
		private String company;

		public Builder(String name, String cpfCnpj) {
			this.name = name;
			this.cpfCnpj = cpfCnpj;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Builder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public Builder mobilePhone(String mobilePhone) {
			this.mobilePhone = mobilePhone;
			return this;
		}

		public Builder address(String address) {
			this.address = address;
			return this;
		}

		public Builder addressNumber(String addressNumber) {
			this.addressNumber = addressNumber;
			return this;
		}

		public Builder complement(String complement) {
			this.complement = complement;
			return this;
		}

		public Builder province(String province) {
			this.province = province;
			return this;
		}

		public Builder postalCode(String postalCode) {
			this.postalCode = postalCode;
			return this;
		}

		public Builder externalReference(String externalReference) {
			this.externalReference = externalReference;
			return this;
		}

		public Builder notificationDisabled(boolean notificationDisabled) {
			this.notificationDisabled = notificationDisabled;
			return this;
		}

		public Builder additionalEmails(String additionalEmails) {
			this.additionalEmails = additionalEmails;
			return this;
		}

		public Builder municipalInscription(String municipalInscription) {
			this.municipalInscription = municipalInscription;
			return this;
		}

		public Builder stateInscription(String stateInscription) {
			this.stateInscription = stateInscription;
			return this;
		}

		public Builder observations(String observations) {
			this.observations = observations;
			return this;
		}

		public Builder groupName(String groupName) {
			this.groupName = groupName;
			return this;
		}

		public Builder company(String company) {
			this.company = company;
			return this;
		}

		public PostCustomerRequest build() {
			return new PostCustomerRequest(this);
		}
	}
}