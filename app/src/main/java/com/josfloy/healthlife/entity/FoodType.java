package com.josfloy.healthlife.entity;

import java.util.List;

/**
 * Created by Jos on 2019/7/15 0015.
 */
public class FoodType {
    private String food_type;
    private List<FoodMessage> food_list;

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public List<FoodMessage> getFood_list() {
        return food_list;
    }

    public void setFood_list(List<FoodMessage> food_list) {
        this.food_list = food_list;
    }
}
