package com.springframework.batch.sample.biz.entity;

public class User {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User [id=" + id + "]";
    }

}
