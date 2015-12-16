package com.example.deii.Models;

import java.util.ArrayList;

/**
 * Created by deii on 12/5/2015.
 */
public class TopicModel {
    String content;
    String name;
    String subcategory_id;
    String category_id;
    String topic_id;
    ArrayList<ProductTypeModel> product_array;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(String subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public ArrayList<ProductTypeModel> getProduct_array() {
        return product_array;
    }
}
