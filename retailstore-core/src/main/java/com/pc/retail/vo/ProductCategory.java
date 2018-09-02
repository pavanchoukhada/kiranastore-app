package com.pc.retail.vo;

public class ProductCategory {

	private String categoryName;
	private boolean foodGrade;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isFoodGrade() {
        return foodGrade;
    }

    public void setFoodGrade(boolean foodGrade) {
        this.foodGrade = foodGrade;
    }
}
