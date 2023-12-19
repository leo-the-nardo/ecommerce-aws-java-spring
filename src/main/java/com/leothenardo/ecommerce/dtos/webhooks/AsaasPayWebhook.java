package com.leothenardo.ecommerce.dtos.webhooks;


import java.util.List;

public record AsaasPayWebhook(
				EventType event,
				Payment payment
) {
	public enum EventType {
		PAYMENT_CREATED("PAYMENT_CREATED"),
		PAYMENT_AWAITING_RISK_ANALYSIS("PAYMENT_AWAITING_RISK_ANALYSIS"),
		PAYMENT_APPROVED_BY_RISK_ANALYSIS("PAYMENT_APPROVED_BY_RISK_ANALYSIS"),
		PAYMENT_REPROVED_BY_RISK_ANALYSIS("PAYMENT_REPROVED_BY_RISK_ANALYSIS"),
		PAYMENT_UPDATED("PAYMENT_UPDATED"),
		PAYMENT_CONFIRMED("PAYMENT_CONFIRMED"),
		PAYMENT_RECEIVED("PAYMENT_RECEIVED"),
		PAYMENT_CREDIT_CARD_CAPTURE_REFUSED("PAYMENT_CREDIT_CARD_CAPTURE_REFUSED"),
		PAYMENT_ANTICIPATED("PAYMENT_ANTICIPATED"),
		PAYMENT_OVERDUE("PAYMENT_OVERDUE"),
		PAYMENT_DELETED("PAYMENT_DELETED"),
		PAYMENT_RESTORED("PAYMENT_RESTORED"),
		PAYMENT_REFUNDED("PAYMENT_REFUNDED"),
		PAYMENT_REFUND_IN_PROGRESS("PAYMENT_REFUND_IN_PROGRESS"),
		PAYMENT_RECEIVED_IN_CASH_UNDONE("PAYMENT_RECEIVED_IN_CASH_UNDONE"),
		PAYMENT_CHARGEBACK_REQUESTED("PAYMENT_CHARGEBACK_REQUESTED"),
		PAYMENT_CHARGEBACK_DISPUTE("PAYMENT_CHARGEBACK_DISPUTE"),
		PAYMENT_AWAITING_CHARGEBACK_REVERSAL("PAYMENT_AWAITING_CHARGEBACK_REVERSAL"),
		PAYMENT_DUNNING_RECEIVED("PAYMENT_DUNNING_RECEIVED"),
		PAYMENT_DUNNING_REQUESTED("PAYMENT_DUNNING_REQUESTED"),
		PAYMENT_BANK_SLIP_VIEWED("PAYMENT_BANK_SLIP_VIEWED"),
		PAYMENT_CHECKOUT_VIEWED("PAYMENT_CHECKOUT_VIEWED");

		private final String value;

		EventType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public record Payment(
					String object,
					String id,
					String dateCreated,
					String customer,
					String subscription,
					String installment,
					String paymentLink,
					String dueDate,
					String originalDueDate,
					Double value,
					Double netValue,
					Double originalValue,
					Double interestValue,
					String nossoNumero,
					String description,
					Long externalReference,
					String billingType,
					String status,
					String pixTransaction,
					String confirmedDate,
					String paymentDate,
					String clientPaymentDate,
					Integer installmentNumber,
					String creditDate,
					String custody,
					String estimatedCreditDate,
					String invoiceUrl,
					String bankSlipUrl,
					String transactionReceiptUrl,
					String invoiceNumber,
					Boolean deleted,
					Boolean anticipated,
					Boolean anticipable,
					String lastInvoiceViewedDate,
					String lastBankSlipViewedDate,
					Boolean postalService,
					CreditCard creditCard,
					Discount discount,
					Fine fine,
					Interest interest,
					List<Split> split,
					Chargeback chargeback,
					String refunds
	) {

		public record CreditCard(
						String creditCardNumber,
						String creditCardBrand,
						String creditCardToken
		) {
		}

		public record Discount(
						Double value,
						Integer dueDateLimitDays,
						String limitedDate,
						String type
		) {
		}

		public record Fine(
						Double value,
						String type
		) {
		}

		public record Interest(
						Double value,
						String type
		) {
		}

		public record Split(
						String walletId,
						Double fixedValue,
						Double percentualValue,
						String status,
						String refusalReason
		) {
		}

		public record Chargeback(
						String status,
						String reason
		) {
		}
	}
}