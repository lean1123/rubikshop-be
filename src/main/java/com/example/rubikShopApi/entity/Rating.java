package com.example.rubikShopApi.entity;

import lombok.Builder;


public enum Rating {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);
    private final Integer value;

    public int getValue() {
        return value;
    }

    private Rating(Integer name) {
        // TODO Auto-generated constructor stub
        this.value = name;
    }

    public static Rating fromValue(int value) {
        for (Rating rating : Rating.values()) {
            if (rating.getValue() == value) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Invalid rating value: " + value);
    }
}
