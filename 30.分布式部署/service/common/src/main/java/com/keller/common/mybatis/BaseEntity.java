package com.keller.common.mybatis;

/**
 * @author yangkaile
 * @date 2019-07-17 16:59:52
 * BaseEntity，使用复杂查询（带条件的增删改查和分页查询）时需要继承的父类
 * 该类提供了可供选择的多条件查询方式、排序方式、分页查询相关参数等
 * 数据实体类继承该类即可使用
 *
 */
public class BaseEntity {
    /**
     * 是否查询明细字段，默认查询明细字段
     */
    private boolean baseKyleDetailed = true;
    /**
     * 多个查询条件是否用And连接
     */
    private Boolean baseKyleUseAnd = true;
    /**
     * 是否按排序关键字升序排列，默认不指定排序方式
     */
    private Boolean baseKyleUseASC = null;
    /**
     * 页面大小
     */
    private int baseKylePageSize = 10;
    /**
     * 要查询的页码
     */
    private int baseKyleCurrentPage = 1;
    /**
     * 根据页面大小和要查询的页码计算出的起始行号
     */
    private int baseKyleStartRows ;

    /**
     * 自定义查询条件,指定自定义查询条件后，执行查询语句时，只会选择自定义的查询条件执行，忽略其他条件
     */
    private String baseKyleCustomCondition = null;

    public Boolean getBaseKyleUseAnd() {
        return baseKyleUseAnd;
    }

    public void setBaseKyleUseAnd(Boolean baseKyleUseAnd) {
        this.baseKyleUseAnd = baseKyleUseAnd;
    }

    public Boolean getBaseKyleUseASC() {
        return baseKyleUseASC;
    }

    public void setBaseKyleUseASC(Boolean baseKyleUseASC) {
        this.baseKyleUseASC = baseKyleUseASC;
    }

    public void setBaseKylePageSize(int baseKylePageSize) {
        this.baseKylePageSize = baseKylePageSize;
        this.baseKyleStartRows = this.baseKylePageSize * (this.baseKyleCurrentPage - 1);
    }

    public int getBaseKylePageSize() {
        return baseKylePageSize;
    }

    public int getBaseKyleStartRows() {
        return baseKyleStartRows;
    }

    public void setBaseKyleStartRows(int baseKyleStartRows) {
        this.baseKyleStartRows = baseKyleStartRows;
    }

    public int getBaseKyleCurrentPage() {
        return baseKyleCurrentPage;
    }

    public void setBaseKyleCurrentPage(int baseKyleCurrentPage) {
        this.baseKyleStartRows = this.baseKylePageSize * (this.baseKyleCurrentPage - 1);
        this.baseKyleCurrentPage = baseKyleCurrentPage;
    }

    public boolean isBaseKyleDetailed() {
        return baseKyleDetailed;
    }

    public void setBaseKyleDetailed(boolean baseKyleDetailed) {
        this.baseKyleDetailed = baseKyleDetailed;
    }

    public String getBaseKyleCustomCondition() {
        return baseKyleCustomCondition;
    }

    public void setBaseKyleCustomCondition(String baseKyleCustomCondition) {
        this.baseKyleCustomCondition = baseKyleCustomCondition;
    }
}
