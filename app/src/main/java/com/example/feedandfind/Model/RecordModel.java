package com.example.feedandfind.Model;

public class RecordModel {

    String id, name, age, image;


    public RecordModel(String id, String name, String age, String image) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
