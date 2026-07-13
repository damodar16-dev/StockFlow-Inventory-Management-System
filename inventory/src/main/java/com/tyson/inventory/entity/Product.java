package com.tyson.inventory.entity;

import jakarta.persistence.*;

    @Entity
    @Table(name="products")
    public class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String productName;

        private String category;

        private double price;

        private int quantity;
        private String imageName;


        public Long getId() {
            return id;
        }


        public void setId(Long id) {
            this.id = id;
        }


        public String getProductName() {
            return productName;
        }


        public void setProductName(String productName) {
            this.productName = productName;
        }


        public String getCategory() {
            return category;
        }


        public void setCategory(String category) {
            this.category = category;
        }


        public double getPrice() {
            return price;
        }


        public void setPrice(double price) {
            this.price = price;
        }


        public int getQuantity() {
            return quantity;
        }


        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
