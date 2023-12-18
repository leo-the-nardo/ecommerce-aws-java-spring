package com.leothenardo.ecommerce.gateways.models.asaas;

public class PostPaymentResponse {
	private String object;
	private String id;
	private String dateCreated;
	private String customer;
	private String paymentLink;
	private double value;
	private double netValue;
	private String originalValue;
	private String interestValue;
	private String description;
	private String billingType;
	private String pixTransaction;
	private String status;
	private String dueDate;
	private String originalDueDate;
	private String paymentDate;
	private String clientPaymentDate;
	private String installmentNumber;
	private String invoiceUrl;
	private String invoiceNumber;
	private String externalReference;
	private boolean deleted;
	private boolean anticipated;
	private boolean anticipable;
	private String creditDate;
	private String estimatedCreditDate;
	private String transactionReceiptUrl;
	private String nossoNumero;
	private String bankSlipUrl;
	private String lastInvoiceViewedDate;
	private String lastBankSlipViewedDate;
	private Discount discount;
	private Fine fine;
	private Interest interest;
	private boolean postalService;
	private String custody;
	private String refunds;

	public PostPaymentResponse() {
	}

	@Override
	public String toString() {
		return "AsaasApiPostPaymentResponse{" +
						"object='" + object + '\'' +
						", id='" + id + '\'' +
						", dateCreated='" + dateCreated + '\'' +
						", customer='" + customer + '\'' +
						", paymentLink='" + paymentLink + '\'' +
						", value=" + value +
						", netValue=" + netValue +
						", originalValue='" + originalValue + '\'' +
						", interestValue='" + interestValue + '\'' +
						", description='" + description + '\'' +
						", billingType='" + billingType + '\'' +
						", pixTransaction='" + pixTransaction + '\'' +
						", status='" + status + '\'' +
						", dueDate='" + dueDate + '\'' +
						", originalDueDate='" + originalDueDate + '\'' +
						", paymentDate='" + paymentDate + '\'' +
						", clientPaymentDate='" + clientPaymentDate + '\'' +
						", installmentNumber='" + installmentNumber + '\'' +
						", invoiceUrl='" + invoiceUrl + '\'' +
						", invoiceNumber='" + invoiceNumber + '\'' +
						", externalReference='" + externalReference + '\'' +
						", deleted=" + deleted +
						", anticipated=" + anticipated +
						", anticipable=" + anticipable +
						", creditDate='" + creditDate + '\'' +
						", estimatedCreditDate='" + estimatedCreditDate + '\'' +
						", transactionReceiptUrl='" + transactionReceiptUrl + '\'' +
						", nossoNumero='" + nossoNumero + '\'' +
						", bankSlipUrl='" + bankSlipUrl + '\'' +
						", lastInvoiceViewedDate='" + lastInvoiceViewedDate + '\'' +
						", lastBankSlipViewedDate='" + lastBankSlipViewedDate + '\'' +
						", discount=" + discount +
						", fine=" + fine +
						", interest=" + interest +
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

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
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

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getNetValue() {
		return netValue;
	}

	public void setNetValue(double netValue) {
		this.netValue = netValue;
	}

	public String getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}

	public String getInterestValue() {
		return interestValue;
	}

	public void setInterestValue(String interestValue) {
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

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getOriginalDueDate() {
		return originalDueDate;
	}

	public void setOriginalDueDate(String originalDueDate) {
		this.originalDueDate = originalDueDate;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getClientPaymentDate() {
		return clientPaymentDate;
	}

	public void setClientPaymentDate(String clientPaymentDate) {
		this.clientPaymentDate = clientPaymentDate;
	}

	public String getInstallmentNumber() {
		return installmentNumber;
	}

	public void setInstallmentNumber(String installmentNumber) {
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

	public String getCreditDate() {
		return creditDate;
	}

	public void setCreditDate(String creditDate) {
		this.creditDate = creditDate;
	}

	public String getEstimatedCreditDate() {
		return estimatedCreditDate;
	}

	public void setEstimatedCreditDate(String estimatedCreditDate) {
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

	public String getLastInvoiceViewedDate() {
		return lastInvoiceViewedDate;
	}

	public void setLastInvoiceViewedDate(String lastInvoiceViewedDate) {
		this.lastInvoiceViewedDate = lastInvoiceViewedDate;
	}

	public String getLastBankSlipViewedDate() {
		return lastBankSlipViewedDate;
	}

	public void setLastBankSlipViewedDate(String lastBankSlipViewedDate) {
		this.lastBankSlipViewedDate = lastBankSlipViewedDate;
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

}

