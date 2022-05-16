package io.jutil.jdo.internal.core.parser.model;

import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.internal.core.util.AssertUtil;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-05-12
 */
public class DefaultSqlItem implements SqlItem {
    private String sql;
    private List<String> parameterNameList;

    public DefaultSqlItem(String sql, List<String> parameterNameList) {
        this.sql = sql;
        this.setParameterNameList(parameterNameList);
    }

    @Override
    public String toString() {
        return String.format("SQL: %s, params: %s", sql, parameterNameList);
    }

    public void check() {
        AssertUtil.notEmpty(sql, "SQL");
        AssertUtil.notNull(parameterNameList, "参数名称列表");
    }

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public List<String> getParameterNameList() {
        return parameterNameList;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setParameterNameList(List<String> paramNameList) {
        if (paramNameList == null || paramNameList.isEmpty()) {
            this.parameterNameList = List.of();
        } else {
            this.parameterNameList = List.copyOf(paramNameList);
        }
    }
}
