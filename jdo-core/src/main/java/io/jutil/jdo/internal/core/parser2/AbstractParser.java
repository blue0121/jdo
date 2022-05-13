package io.jutil.jdo.internal.core.parser2;

import io.jutil.jdo.core.annotation.Column;
import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.reflect2.ClassFieldOperation;
import io.jutil.jdo.internal.core.dialect.Dialect;
import io.jutil.jdo.internal.core.parser2.model.DefaultFieldMetadata;

import java.lang.reflect.Modifier;

/**
 * @author Jin Zheng
 * @since 2022-05-12
 */
public abstract class AbstractParser implements Parser {
    protected final Dialect dialect;
    protected final boolean escape;

    public AbstractParser(Dialect dialect, boolean escape) {
        this.dialect = dialect;
        this.escape = escape;
    }

    protected String getColumnName(ClassFieldOperation field) {
        if (!Modifier.isPublic(field.getModifiers())) {
            if (field.getGetterMethod() == null) {
                throw new JdbcException("找不到Getter方法: " + field.getFieldName());
            }
            if (field.getSetterMethod() == null) {
                throw new JdbcException("找不到Setter方法: " + field.getFieldName());
            }
        }

        Column annotation = field.getAnnotation(Column.class);
        if (annotation != null) {
            return annotation.name();
        }
        return this.toColumnName(field.getFieldName());
    }

    /**
     * 字段 => 列名，类名 => 表名
     *
     * @return
     */
    protected String toColumnName(String field) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < field.length(); i++) {
            char c = field.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    sb.append('_');
                }

                sb.append(Character.toLowerCase(c));
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    protected void setFieldMetadata(ClassFieldOperation field, DefaultFieldMetadata metadata) {
        String columnName = this.getColumnName(field);
        String escapeColumnName = escape ? dialect.escape(columnName) : columnName;
        metadata.setColumnName(columnName);
        metadata.setEscapeColumnName(escapeColumnName);
        metadata.setBeanField(field);
    }

}
