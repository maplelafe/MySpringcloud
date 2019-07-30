package com.ffb.eureka_service.VO;

import java.util.Date;

public class CategoryInfo {
    /**
     * 主键ID
     */
    private Long categoryId;

    /**
     * 学生姓名
     */
    private String categoryName;

    /**
     * 创建时间
     */
    private Date createTime;

    private String categoryType;

    private Date updateTime;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}