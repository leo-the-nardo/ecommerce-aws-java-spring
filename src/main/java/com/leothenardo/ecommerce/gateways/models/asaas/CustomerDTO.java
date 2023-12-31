package com.leothenardo.ecommerce.gateways.models.asaas;

public record CustomerDTO(
				String object,
				String id,
				String dateCreated,
				String name,
				String email,
				String company,
				String phone,
				String mobilePhone,
				String address,
				String addressNumber,
				String complement,
				String province,
				String postalCode,
				String cpfCnpj,
				String personType,
				Boolean deleted,
				String additionalEmails,
				String externalReference,
				Boolean notificationDisabled,
				String observations,
				String municipalInscription,
				String stateInscription,
				Boolean canDelete,
				String cannotBeDeletedReason,
				Boolean canEdit,
				String cannotEditReason,
				Boolean foreignCustomer,
				String city,
				String state,
				String country
) {
}
