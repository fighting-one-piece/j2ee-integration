/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package org.platform.entity;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

/**
 * <p>查询操作符</p>
 */
public enum QueryOperator {
    eq(0, "等于", "="), ne(1, "不等于", "!="),
    gt(2, "大于", ">"), gte(3, "大于等于", ">="), lt(4, "小于", "<"), lte(5, "小于等于", "<="),
    prefixLike(6, "前缀模糊匹配", "like"), prefixNotLike(7, "前缀模糊不匹配", "not like"),
    suffixLike(8, "后缀模糊匹配", "like"), suffixNotLike(9, "后缀模糊不匹配", "not like"),
    like(10, "模糊匹配", "like"), notLike(11, "不匹配", "not like"),
    isNull(12, "空", "is null"), isNotNull(13, "非空", "is not null"),
    in(14, "包含", "in"), notIn(15, "不包含", "not in"), custom(16, "自定义默认的", null);

    private final int id;
    private final String info;
    private final String symbol;

    QueryOperator(final int id, final String info, String symbol) {
        this.id = id;
    	this.info = info;
        this.symbol = symbol;
    }
    
    public int getId() {
    	return id;
    }

    public String getInfo() {
        return info;
    }

    public String getSymbol() {
        return symbol;
    }

    public static String toStringAllOperator() {
        return Arrays.toString(QueryOperator.values());
    }

    /**
     * 操作符是否允许为空
     * @param operator
     * @return
     */
    public static boolean isAllowBlankValue(final QueryOperator operator) {
        return operator == QueryOperator.isNotNull || operator == QueryOperator.isNull;
    }


    public static QueryOperator valueBySymbol(String symbol) {
        symbol = formatSymbol(symbol);
        for (QueryOperator operator : values()) {
            if (operator.getSymbol().equals(symbol)) {
                return operator;
            }
        }
        return null;
    }

    private static String formatSymbol(String symbol) {
        if (StringUtils.isBlank(symbol)) {
            return symbol;
        }
        return symbol.trim().toLowerCase().replace("  ", " ");
    }
}
