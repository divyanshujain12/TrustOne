package com.example.deii.Models;

/**
 * Created by divyanshu on 6/12/2016.
 */
public class MyOrderModel {
    String order_id;
    String invoice;

    String transaction_message;
    String category_name;
    String category_id;
    String email;
    String auto_subscribe;
    String card;
    String exp;
    String amount;
    String cardholder;
    String street;
    String zip;
    String description;
    String cvv2String;
    String valid_upto;
    String status;
    String edate;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getTransaction_message() {
        return transaction_message;
    }

    public void setTransaction_message(String transaction_message) {
        this.transaction_message = transaction_message;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuto_subscribe() {
        return auto_subscribe;
    }

    public void setAuto_subscribe(String auto_subscribe) {
        this.auto_subscribe = auto_subscribe;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCvv2String() {
        return cvv2String;
    }

    public void setCvv2String(String cvv2String) {
        this.cvv2String = cvv2String;
    }

    public String getValid_upto() {
        return valid_upto;
    }

    public void setValid_upto(String valid_upto) {
        this.valid_upto = valid_upto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }
}
