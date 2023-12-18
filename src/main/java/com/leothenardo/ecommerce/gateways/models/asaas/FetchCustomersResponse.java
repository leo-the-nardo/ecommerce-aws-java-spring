package com.leothenardo.ecommerce.gateways.models.asaas;

public record FetchCustomersResponse(
				String object,
				Boolean hasMore,
				Integer totalCount,
				Integer limit,
				Integer offset,
				CustomerDTO[] data
) {
}
