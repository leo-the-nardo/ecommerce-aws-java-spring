package com.leothenardo.ecommerce.gateways.models.asaas;

public record PostCustomerResponse(
				String objectType,
				String id,
				String dateCreated,
				String name,
				String email,
				String phone,
				String mobilePhone,
				String address,
				String addressNumber,
				String complement,
				String province,
				int city,
				String state,
				String country,
				String postalCode,
				String cpfCnpj,
				String personType,
				boolean deleted,
				String additionalEmails,
				String externalReference,
				boolean notificationDisabled,
				String observations
) {
}