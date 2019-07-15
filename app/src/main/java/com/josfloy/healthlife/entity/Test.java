package com.josfloy.healthlife.entity;

/**
 * Created by Jos on 2019/7/15 0015.
 */
public class Test {
    private String address;
    private String age;
    private String name;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {

        return "【address】" + address + "\t\t【age】" + age + "\t\t【name】" + name;
    }
}

