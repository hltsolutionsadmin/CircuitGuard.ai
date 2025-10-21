package com.skillrat.commonservice.enums;

public enum Category {
    ELECTRICIAN(1, "ELECTRICIAN"),
    PLUMBING(2, "PLUMBING"),
    REPAIRS(3, "CARPENTER"),
    CLEANING(4, "CLEANING"),
    PAINTING(5, "PAINTING"),
    LIST_SERVICE(6, "LIFT SERVICE");

    public final int categoryId;
    public final String label;

    Category(int categoryId, String label) {
        this.categoryId = categoryId;
        this.label = label;
    }

    public static Category fromId(int id) {
        for (Category category : values()) {
            if (category.categoryId == id) {
                return category;
            }
        }
        return null; // or throw an exception
    }
}
