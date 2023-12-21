package com.leothenardo.ecommerce.gateways.models.asaas;

import java.time.LocalDate;

public class PostCreditCardPaymentResponse {
	private String object;
	private String id;
	private LocalDate dateCreated;
	private String customer;
	private String paymentLink;
	private Double value;
	private Double netValue;
	private Double originalValue;
	private Double interestValue;
	private String description;
	private String billingType;
	private LocalDate confirmedDate;
	private CreditCard creditCard;
	private String pixTransaction;
	private String status;
	private LocalDate dueDate;
	private LocalDate originalDueDate;
	private LocalDate paymentDate;
	private LocalDate clientPaymentDate;
	private Integer installmentNumber;
	private String invoiceUrl;
	private String invoiceNumber;
	private String externalReference;
	private boolean deleted;
	private boolean anticipated;
	private boolean anticipable;
	private LocalDate creditDate;
	private LocalDate estimatedCreditDate;
	private String transactionReceiptUrl;
	private String nossoNumero;
	private String bankSlipUrl;
	private LocalDate lastInvoiceViewedDate;
	private LocalDate lastBankSlipViewedDate;
	private boolean postalService;
	private String custody;
	private String refunds;

	public PostCreditCardPaymentResponse() {
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getPaymentLink() {
		return paymentLink;
	}

	public void setPaymentLink(String paymentLink) {
		this.paymentLink = paymentLink;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getNetValue() {
		return netValue;
	}

	public void setNetValue(Double netValue) {
		this.netValue = netValue;
	}

	public Double getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(Double originalValue) {
		this.originalValue = originalValue;
	}

	public Double getInterestValue() {
		return interestValue;
	}

	public void setInterestValue(Double interestValue) {
		this.interestValue = interestValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public LocalDate getConfirmedDate() {
		return confirmedDate;
	}

	public void setConfirmedDate(LocalDate confirmedDate) {
		this.confirmedDate = confirmedDate;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public String getPixTransaction() {
		return pixTransaction;
	}

	public void setPixTransaction(String pixTransaction) {
		this.pixTransaction = pixTransaction;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getOriginalDueDate() {
		return originalDueDate;
	}

	public void setOriginalDueDate(LocalDate originalDueDate) {
		this.originalDueDate = originalDueDate;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public LocalDate getClientPaymentDate() {
		return clientPaymentDate;
	}

	public void setClientPaymentDate(LocalDate clientPaymentDate) {
		this.clientPaymentDate = clientPaymentDate;
	}

	public Integer getInstallmentNumber() {
		return installmentNumber;
	}

	public void setInstallmentNumber(Integer installmentNumber) {
		this.installmentNumber = installmentNumber;
	}

	public String getInvoiceUrl() {
		return invoiceUrl;
	}

	public void setInvoiceUrl(String invoiceUrl) {
		this.invoiceUrl = invoiceUrl;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isAnticipated() {
		return anticipated;
	}

	public void setAnticipated(boolean anticipated) {
		this.anticipated = anticipated;
	}

	public boolean isAnticipable() {
		return anticipable;
	}

	public void setAnticipable(boolean anticipable) {
		this.anticipable = anticipable;
	}

	public LocalDate getCreditDate() {
		return creditDate;
	}

	public void setCreditDate(LocalDate creditDate) {
		this.creditDate = creditDate;
	}

	public LocalDate getEstimatedCreditDate() {
		return estimatedCreditDate;
	}

	public void setEstimatedCreditDate(LocalDate estimatedCreditDate) {
		this.estimatedCreditDate = estimatedCreditDate;
	}

	public String getTransactionReceiptUrl() {
		return transactionReceiptUrl;
	}

	public void setTransactionReceiptUrl(String transactionReceiptUrl) {
		this.transactionReceiptUrl = transactionReceiptUrl;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getBankSlipUrl() {
		return bankSlipUrl;
	}

	public void setBankSlipUrl(String bankSlipUrl) {
		this.bankSlipUrl = bankSlipUrl;
	}

	public LocalDate getLastInvoiceViewedDate() {
		return lastInvoiceViewedDate;
	}

	public void setLastInvoiceViewedDate(LocalDate lastInvoiceViewedDate) {
		this.lastInvoiceViewedDate = lastInvoiceViewedDate;
	}

	public LocalDate getLastBankSlipViewedDate() {
		return lastBankSlipViewedDate;
	}

	public void setLastBankSlipViewedDate(LocalDate lastBankSlipViewedDate) {
		this.lastBankSlipViewedDate = lastBankSlipViewedDate;
	}

	public boolean isPostalService() {
		return postalService;
	}

	public void setPostalService(boolean postalService) {
		this.postalService = postalService;
	}

	public String getCustody() {
		return custody;
	}

	public void setCustody(String custody) {
		this.custody = custody;
	}

	public String getRefunds() {
		return refunds;
	}

	public void setRefunds(String refunds) {
		this.refunds = refunds;
	}

	@Override
	public String toString() {
		return "PostCreditCardPaymentResponse{" +
						"object='" + object + '\'' +
						", id='" + id + '\'' +
						", dateCreated=" + dateCreated +
						", customer='" + customer + '\'' +
						", paymentLink='" + paymentLink + '\'' +
						", value=" + value +
						", netValue=" + netValue +
						", originalValue=" + originalValue +
						", interestValue=" + interestValue +
						", description='" + description + '\'' +
						", billingType='" + billingType + '\'' +
						", confirmedDate=" + confirmedDate +
						", creditCard=" + creditCard +
						", pixTransaction='" + pixTransaction + '\'' +
						", status='" + status + '\'' +
						", dueDate=" + dueDate +
						", originalDueDate=" + originalDueDate +
						", paymentDate=" + paymentDate +
						", clientPaymentDate=" + clientPaymentDate +
						", installmentNumber=" + installmentNumber +
						", invoiceUrl='" + invoiceUrl + '\'' +
						", invoiceNumber='" + invoiceNumber + '\'' +
						", externalReference='" + externalReference + '\'' +
						", deleted=" + deleted +
						", anticipated=" + anticipated +
						", anticipable=" + anticipable +
						", creditDate=" + creditDate +
						", estimatedCreditDate=" + estimatedCreditDate +
						", transactionReceiptUrl='" + transactionReceiptUrl + '\'' +
						", nossoNumero='" + nossoNumero + '\'' +
						", bankSlipUrl='" + bankSlipUrl + '\'' +
						", lastInvoiceViewedDate=" + lastInvoiceViewedDate +
						", lastBankSlipViewedDate=" + lastBankSlipViewedDate +
						", postalService=" + postalService +
						", custody='" + custody + '\'' +
						", refunds='" + refunds + '\'' +
						'}';
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public static class CreditCard {
		private String creditCardNumber;
		private String creditCardBrand;
		private String creditCardToken;

		@Override
		public String toString() {
			return "CreditCard{" +
							"creditCardNumber='" + creditCardNumber + '\'' +
							", creditCardBrand='" + creditCardBrand + '\'' +
							", creditCardToken='" + creditCardToken + '\'' +
							'}';
		}

		public String getCreditCardNumber() {
			return creditCardNumber;
		}

		public void setCreditCardNumber(String creditCardNumber) {
			this.creditCardNumber = creditCardNumber;
		}

		public String getCreditCardBrand() {
			return creditCardBrand;
		}

		public void setCreditCardBrand(String creditCardBrand) {
			this.creditCardBrand = creditCardBrand;
		}

		public String getCreditCardToken() {
			return creditCardToken;
		}

		public void setCreditCardToken(String creditCardToken) {
			this.creditCardToken = creditCardToken;
		}
	}
}