package com.ykfjava.week04.model;

public class Entity {

    private Integer age;

    private String name;


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Entity() {

    }

    public Entity(Integer age, String name) {
        this.age = age;
        this.name = name;
    }

    public void addAge () {
        this.age++;
    }
}
