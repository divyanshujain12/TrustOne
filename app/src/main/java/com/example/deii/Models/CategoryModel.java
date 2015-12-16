package com.example.deii.Models;

import java.util.ArrayList;

/**
 * Created by Lenovo on 04-11-2015.
 */
public class CategoryModel {

    String category_id;
    String type;
    String name;
    String subscription_amount;
    String edate;
    ArrayList<SubCategoryModel> subcategories;


    public ArrayList<SubCategoryModel> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<SubCategoryModel> subcategories) {
        this.subcategories = subcategories;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubscription_amount() {
        return subscription_amount;
    }

    public void setSubscription_amount(String subscription_amount) {
        this.subscription_amount = subscription_amount;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }
}
