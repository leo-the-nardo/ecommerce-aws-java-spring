package com.leothenardo.ecommerce.dtos.mail;

import java.util.List;

public class ConfirmationOrderMailInput {
	private String to;
	private String name;
	private List<Item> items;
	private Double total;

	public ConfirmationOrderMailInput() {
	}

	public ConfirmationOrderMailInput(String to, String name, List<Item> items, Double total) {
		this.to = to;
		this.name = name;
		this.items = items;
		this.total = total;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public static class Item {
		private String name;
		private String quantity;
		private double price;
		private String thumbUrl;

		public Item(String name, String quantity, double price, String thumbUrl) {
			this.name = name;
			this.quantity = quantity;
			this.price = price;
			this.thumbUrl = thumbUrl;
		}

		public Item() {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getQuantity() {
			return quantity;
		}

		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public String getThumbUrl() {
			return thumbUrl;
		}

		public void setThumbUrl(String thumbUrl) {
			this.thumbUrl = thumbUrl;
		}
	}
}
