package com.leothenardo.ecommerce.services;


import com.leothenardo.ecommerce.dtos.OrderDTO;
import com.leothenardo.ecommerce.dtos.PaymentUrlRequestDTO;
import com.leothenardo.ecommerce.dtos.webhooks.AsaasPayWebhook;
import com.leothenardo.ecommerce.gateways.EmailProvider;
import com.leothenardo.ecommerce.gateways.PaymentGatewayProvider;
import com.leothenardo.ecommerce.models.*;
import com.leothenardo.ecommerce.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PaymentService {
	private static final Logger log = Logger.getLogger(PaymentService.class.getName());
	private final UserService userService;
	private final OrderService orderService;
	private final PaymentGatewayProvider paymentGateway;
	private final OrderRepository orderRepository;
	private final EmailProvider emailProvider;
	private final NotificationService notificationService;

	public PaymentService(UserService userService, OrderService orderService, PaymentGatewayProvider paymentGateway, OrderRepository orderRepository, EmailProvider emailProvider, NotificationService notificationService) {
		this.userService = userService;
		this.orderService = orderService;
		this.paymentGateway = paymentGateway;
		this.orderRepository = orderRepository;
		this.emailProvider = emailProvider;
		this.notificationService = notificationService;
	}


	@Transactional
	public String generateUrl(PaymentUrlRequestDTO input) {
		User me = userService.authenticated();
		Optional<String> gatewayCustomerId = getGatewayCustomerId(me);

		if (gatewayCustomerId.isEmpty()) {
			String createdGatewayCustomerId = paymentGateway.createCustomer(me);
			gatewayCustomerId = Optional.of(createdGatewayCustomerId);
			log.info("Created Gateway Customer ID: " + createdGatewayCustomerId);
		}
		OrderDTO order = orderService.internalGet(input.orderId());

		return paymentGateway.generatePaymentUrl(
						gatewayCustomerId.get(),
						order.total(),
						order.id()
		);
	}


	@Transactional
	public void handleAsaasWebhook(AsaasPayWebhook webhook) {
		//map as router handling
		switch (webhook.event()) {
			case PAYMENT_CONFIRMED, PAYMENT_RECEIVED, PAYMENT_APPROVED_BY_RISK_ANALYSIS:
				handlePaymentConfirmed(webhook);
				break;
			case PAYMENT_AWAITING_RISK_ANALYSIS:
				handlePaymentAwaitingRiskAnalysis(webhook);
				break;
			case PAYMENT_REPROVED_BY_RISK_ANALYSIS:
				handlePaymentReprovedByRiskAnalysis(webhook);
				break;
			default:
				log.info("Ignoring unknown event: " + webhook.event());
		}
	}

	private void handlePaymentReprovedByRiskAnalysis(AsaasPayWebhook webhook) {
		log.info("Handling PAYMENT_REPROVED_BY_RISK_ANALYSIS event");
		AsaasPayWebhook.Payment theEvent = webhook.payment();
		Long orderId = theEvent.externalReference();
		Order order = orderRepository.findById(orderId).orElseThrow();
		order.setStatus(OrderStatus.REPROVED);
		orderRepository.save(order);
		emailProvider.send(
						order.getClient().getEmail(),
						"Payment Service",
						"Payment Reproved",
						buildPaymentReprovedEmail(order)
		);
	}

	private String buildPaymentReprovedEmail(Order order) {
		//TODO: integrate with thymeleaf
		return "payment reproved email";
	}

	private void handlePaymentAwaitingRiskAnalysis(AsaasPayWebhook webhook) {
		log.info("Handling PAYMENT_AWAITING_RISK_ANALYSIS event");
		AsaasPayWebhook.Payment theEvent = webhook.payment();
		Long orderId = theEvent.externalReference();
		Order order = orderRepository.findById(orderId).orElseThrow();
		order.setStatus(OrderStatus.PROCESSING);
		orderRepository.save(order);
		emailProvider.send(
						order.getClient().getEmail(),
						"Payment Service",
						"We are processing your payment",
						buildProcessingPaymentEmail(order)
		);
	}

	private String buildProcessingPaymentEmail(Order order) {
		//TODO: integrate with thymeleaf
		return "processing payment email";
	}

	private Optional<String> getGatewayCustomerId(User user) {
		Optional<String> gatewayCustomerId = paymentGateway.getGatewayCustomerIdByDb(user.getId());

		if (gatewayCustomerId.isEmpty()) {
			log.info("Searching customer ID by HTTP API");
			gatewayCustomerId = paymentGateway.getGatewayCustomerIdByHttp(user);

			if (gatewayCustomerId.isPresent()) {
				log.info("Persisting found customer ID by HTTP API: " + gatewayCustomerId.get());
				paymentGateway.persistCustomer(gatewayCustomerId.get(), user);
			}
		} else {
			log.info("Customer ID found in DB");
		}

		return gatewayCustomerId;
	}

	@Transactional
	protected void handlePaymentConfirmed(AsaasPayWebhook webhook) {
		String eventName = webhook.event().toString();
		AsaasPayWebhook.Payment theEvent = webhook.payment();
		if (webhook.event() == AsaasPayWebhook.EventType.PAYMENT_RECEIVED
						&& BillingType.valueOf(theEvent.billingType()) == BillingType.CREDIT_CARD) {
			log.info("Ignoring PAYMENT_RECEIVED event for CREDIT_CARD due to handled by PAYMENT_CONFIRMED event");
			return;
		}

		log.info("Handling " + eventName + " asaas webhook event");
		Long orderId = theEvent.externalReference();
		Order order = orderRepository.findById(orderId).orElseThrow();
		order.setStatus(OrderStatus.PAID);
		Payment payment = new Payment(
						order,
						BillingType.valueOf(theEvent.billingType()),
						theEvent.value(),
						theEvent.netValue()
		);
		order.setPayment(payment);
		order.getItems().forEach(item -> item.getProduct().setStock(item.getProduct().getStock() - item.getQuantity()));
		orderRepository.save(order);
		emailProvider.send(
						order.getClient().getEmail(),
						"Payment Service",
						"Payment Confirmed",
						buildPayConfirmationEmail(order)
		);
		notificationService.notifyListeners(webhook);

	}


	private String buildPayConfirmationEmail(Order order) {
		//TODO: unhardcode it and integrate with thymeleaf
		return """
						      						<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
						      						<html xmlns="http://www.w3.org/1999/xhtml">
						      						<head>
						      						    <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
						      						    <title>Email Template for Order Confirmation Email</title>
						      						     \s
						      						    <!-- Start Common CSS -->
						      						    <style type="text/css">
						      						        #outlook a {
						      						            padding: 0;
						      						        }
						      						     \s
						      						        body {
						      						            width: 100% !important;
						      						            -webkit-text-size-adjust: 100%;
						      						            -ms-text-size-adjust: 100%;
						      						            margin: 0;
						      						            padding: 0;
						      						            font-family: Helvetica, arial, sans-serif;
						      						        }
						      						     \s
						      						        .ExternalClass {
						      						            width: 100%;
						      						        }
						      						     \s
						      						        .ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div {
						      						            line-height: 100%;
						      						        }
						      						     \s
						      						        .backgroundTable {
						      						            margin: 0;
						      						            padding: 0;
						      						            width: 100% !important;
						      						            line-height: 100% !important;
						      						        }
						      						     \s
						      						        .main-temp table {
						      						            border-collapse: collapse;
						      						            mso-table-lspace: 0pt;
						      						            mso-table-rspace: 0pt;
						      						            font-family: Helvetica, arial, sans-serif;
						      						        }
						      						     \s
						      						        .main-temp table td {
						      						            border-collapse: collapse;
						      						        }
						      						    </style>
						      						    <!-- End Common CSS -->
						      						</head>
						      						<body>
						      						<table border="0" cellpadding="0" cellspacing="0" class="backgroundTable main-temp" style="background-color: #d5d5d5;"
						      						       width="100%">
						      						    <tbody>
						      						    <tr>
						      						        <td>
						      						            <table align="center" border="0" cellpadding="15" cellspacing="0" class="devicewidth"
						      						                   style="background-color: #ffffff;"
						      						                   width="600">
						      						                <tbody>
						      						                <!-- Start header Section -->
						      						                <tr>
						      						                    <td style="padding-top: 30px;">
						      						                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="devicewidthinner"
						      						                               style="border-bottom: 1px solid #eeeeee; text-align: center;" width="560">
						      						                            <tbody>
						      						                            <tr>
						      						                                <td style="padding-bottom: 10px;">
						      						                                    <a href="https://htmlcodex.com"><img alt="PapaChina" src="images/logo.png"/></a>
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #666666;">
						      						                                    3828 Mall Road
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #666666;">
						      						                                    Los Angeles, California, 90017
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #666666;">
						      						                                    Phone: 310-807-6672 | Email: info@example.com
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #666666; padding-bottom: 25px;">
						      						                                    <strong>Order Number:</strong> 001 | <strong>Order Date:</strong> 21-Nov-19
						      						                                </td>
						      						                            </tr>
						      						                            </tbody>
						      						                        </table>
						      						                    </td>
						      						                </tr>
						      						                <!-- End header Section -->
						      						     \s
						      						                <!-- Start address Section -->
						      						                <tr>
						      						                    <td style="padding-top: 0;">
						      						                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="devicewidthinner"
						      						                               style="border-bottom: 1px solid #bbbbbb;" width="560">
						      						                            <tbody>
						      						                            <tr>
						      						                                <td style="width: 55%; font-size: 16px; font-weight: bold; color: #666666; padding-bottom: 5px;">
						      						                                    Delivery Adderss
						      						                                </td>
						      						                                <td style="width: 45%; font-size: 16px; font-weight: bold; color: #666666; padding-bottom: 5px;">
						      						                                    Billing Address
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="width: 55%; font-size: 14px; line-height: 18px; color: #666666;">
						      						                                    James C Painter
						      						                                </td>
						      						                                <td style="width: 45%; font-size: 14px; line-height: 18px; color: #666666;">
						      						                                    James C Painter
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="width: 55%; font-size: 14px; line-height: 18px; color: #666666;">
						      						                                    3939 Charles Street, Farmington Hills
						      						                                </td>
						      						                                <td style="width: 45%; font-size: 14px; line-height: 18px; color: #666666;">
						      						                                    3939 Charles Street, Farmington Hills
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="width: 55%; font-size: 14px; line-height: 18px; color: #666666; padding-bottom: 10px;">
						      						                                    Michigan, 48335
						      						                                </td>
						      						                                <td style="width: 45%; font-size: 14px; line-height: 18px; color: #666666; padding-bottom: 10px;">
						      						                                    Michigan, 48335
						      						                                </td>
						      						                            </tr>
						      						                            </tbody>
						      						                        </table>
						      						                    </td>
						      						                </tr>
						      						                <!-- End address Section -->
						      						     \s
						      						                <!-- Start product Section -->
						      						                <tr>
						      						                    <td style="padding-top: 0;">
						      						                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="devicewidthinner"
						      						                               style="border-bottom: 1px solid #eeeeee;" width="560">
						      						                            <tbody>
						      						                            <tr>
						      						                                <td rowspan="4" style="padding-right: 10px; padding-bottom: 10px;">
						      						                                    <img alt="Product Image" src="images/product-1.jpg" style="height: 80px;"/>
						      						                                </td>
						      						                                <td colspan="2"
						      						                                    style="font-size: 14px; font-weight: bold; color: #666666; padding-bottom: 5px;">
						      						                                    Lorem ipsum dolor sit amet
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575; width: 440px;">
						      						                                    Quantity: 100
						      						                                </td>
						      						                                <td style="width: 130px;"></td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575;">
						      						                                    Color: White & Blue
						      						                                </td>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575; text-align: right;">
						      						                                    $1.23 Per Unit
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575; padding-bottom: 10px;">
						      						                                    Size: XL
						      						                                </td>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575; text-align: right; padding-bottom: 10px;">
						      						                                    <b style="color: #666666;">$1,234.50</b> Total
						      						                                </td>
						      						                            </tr>
						      						                            </tbody>
						      						                        </table>
						      						                    </td>
						      						                </tr>
						      						                <tr>
						      						                    <td style="padding-top: 0;">
						      						                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="devicewidthinner"
						      						                               style="border-bottom: 1px solid #eeeeee;" width="560">
						      						                            <tbody>
						      						                            <tr>
						      						                                <td rowspan="4" style="padding-right: 10px; padding-bottom: 10px;">
						      						                                    <img alt="Product Image" src="images/product-2.jpg" style="height: 80px;"/>
						      						                                </td>
						      						                                <td colspan="2"
						      						                                    style="font-size: 14px; font-weight: bold; color: #666666; padding-bottom: 5px;">
						      						                                    Aliquam posuere ultrices mi ut rutrum
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575; width: 440px;">
						      						                                    Quantity: 100
						      						                                </td>
						      						                                <td style="width: 130px;"></td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575;">
						      						                                    Color: White & Blue
						      						                                </td>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575; text-align: right;">
						      						                                    $1.23 Per Unit
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575; padding-bottom: 10px;">
						      						                                    Size: XL
						      						                                </td>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575; text-align: right; padding-bottom: 10px;">
						      						                                    <b style="color: #666666;">$1,234.50</b> Total
						      						                                </td>
						      						                            </tr>
						      						                            </tbody>
						      						                        </table>
						      						                    </td>
						      						                </tr>
						      						                <tr>
						      						                    <td style="padding-top: 0;">
						      						                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="devicewidthinner"
						      						                               style="border-bottom: 1px solid #eeeeee;" width="560">
						      						                            <tbody>
						      						                            <tr>
						      						                                <td rowspan="4" style="padding-right: 10px; padding-bottom: 10px;">
						      						                                    <img alt="Product Image" src="images/product-3.jpg" style="height: 80px;"/>
						      						                                </td>
						      						                                <td colspan="2"
						      						                                    style="font-size: 14px; font-weight: bold; color: #666666; padding-bottom: 5px;">
						      						                                    Phasellus vitae pharetra arcu
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575; width: 440px;">
						      						                                    Quantity: 100
						      						                                </td>
						      						                                <td style="width: 130px;"></td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575;">
						      						                                    Color: White & Blue
						      						                                </td>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575; text-align: right;">
						      						                                    $1.23 Per Unit
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575; padding-bottom: 10px;">
						      						                                    Size: XL
						      						                                </td>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #757575; text-align: right; padding-bottom: 10px;">
						      						                                    <b style="color: #666666;">$1,234.50</b> Total
						      						                                </td>
						      						                            </tr>
						      						                            </tbody>
						      						                        </table>
						      						                    </td>
						      						                </tr>
						      						                <!-- End product Section -->
						      						     \s
						      						                <!-- Start calculation Section -->
						      						                <tr>
						      						                    <td style="padding-top: 0;">
						      						                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="devicewidthinner"
						      						                               style="border-bottom: 1px solid #bbbbbb; margin-top: -5px;" width="560">
						      						                            <tbody>
						      						                            <tr>
						      						                                <td rowspan="5" style="width: 55%;"></td>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #666666;">
						      						                                    Sub-Total:
						      						                                </td>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #666666; width: 130px; text-align: right;">
						      						                                    $1,234.50
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #666666; padding-bottom: 10px; border-bottom: 1px solid #eeeeee;">
						      						                                    Shipping Fee:
						      						                                </td>
						      						                                <td style="font-size: 14px; line-height: 18px; color: #666666; padding-bottom: 10px; border-bottom: 1px solid #eeeeee; text-align: right;">
						      						                                    $10.20
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; font-weight: bold; line-height: 18px; color: #666666; padding-top: 10px;">
						      						                                    Order Total
						      						                                </td>
						      						                                <td style="font-size: 14px; font-weight: bold; line-height: 18px; color: #666666; padding-top: 10px; text-align: right;">
						      						                                    $1,234.50
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; font-weight: bold; line-height: 18px; color: #666666;">
						      						                                    Payment Term:
						      						                                </td>
						      						                                <td style="font-size: 14px; font-weight: bold; line-height: 18px; color: #666666; text-align: right;">
						      						                                    100%
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="font-size: 14px; font-weight: bold; line-height: 18px; color: #666666; padding-bottom: 10px;">
						      						                                    Deposit Amount
						      						                                </td>
						      						                                <td style="font-size: 14px; font-weight: bold; line-height: 18px; color: #666666; text-align: right; padding-bottom: 10px;">
						      						                                    $1,234.50
						      						                                </td>
						      						                            </tr>
						      						                            </tbody>
						      						                        </table>
						      						                    </td>
						      						                </tr>
						      						                <!-- End calculation Section -->
						      						     \s
						      						                <!-- Start payment method Section -->
						      						                <tr>
						      						                    <td style="padding: 0 10px;">
						      						                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="devicewidthinner"
						      						                               width="560">
						      						                            <tbody>
						      						                            <tr>
						      						                                <td colspan="2"
						      						                                    style="font-size: 16px; font-weight: bold; color: #666666; padding-bottom: 5px;">
						      						                                    Payment Method (Bank Transfer)
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="width: 55%; font-size: 14px; line-height: 18px; color: #666666;">
						      						                                    Bank Name:
						      						                                </td>
						      						                                <td style="width: 45%; font-size: 14px; line-height: 18px; color: #666666;">
						      						                                    Account Name:
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="width: 55%; font-size: 14px; line-height: 18px; color: #666666;">
						      						                                    Bank Address:
						      						                                </td>
						      						                                <td style="width: 45%; font-size: 14px; line-height: 18px; color: #666666;">
						      						                                    Account Number:
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td style="width: 55%; font-size: 14px; line-height: 18px; color: #666666; padding-bottom: 10px;">
						      						                                    Bank Code:
						      						                                </td>
						      						                                <td style="width: 45%; font-size: 14px; line-height: 18px; color: #666666; padding-bottom: 10px;">
						      						                                    SWIFT Code:
						      						                                </td>
						      						                            </tr>
						      						                            <tr>
						      						                                <td colspan="2"
						      						                                    style="width: 100%; text-align: center; font-style: italic; font-size: 13px; font-weight: 600; color: #666666; padding: 15px 0; border-top: 1px solid #eeeeee;">
						      						                                    <b style="font-size: 14px;">Note:</b> Lorem ipsum dolor sit amet, consectetur
						      						                                    adipiscing elit
						      						                                </td>
						      						                            </tr>
						      						                            </tbody>
						      						                        </table>
						      						                    </td>
						      						                </tr>
						      						                <!-- End payment method Section -->
						      						                </tbody>
						      						            </table>
						      						        </td>
						      						    </tr>
						      						    </tbody>
						      						</table>
						      						</body>
						      						</html>
						""";
	}
}
