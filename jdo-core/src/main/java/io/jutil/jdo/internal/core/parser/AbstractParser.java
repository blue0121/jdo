package io.jutil.jdo.internal.core.parser;

import io.jutil.jdo.core.annotation.Column;
import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.reflect.BeanField;
import io.jutil.jdo.core.reflect.JavaBean;
import io.jutil.jdo.internal.core.dialect.Dialect;
import io.jutil.jdo.internal.core.parser.model.DefaultFieldConfig;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public abstract class AbstractParser implements Parser {

    protected final Dialect dialect;
    protected final boolean escape;
    protected final Set<Class<?>> parserClassSet = new HashSet<>();
    protected final ConfigCache configCache;

    protected AbstractParser(Dialect dialect, boolean escape, ConfigCache cache) {
        this.dialect = dialect;
        this.escape = escape;
        this.configCache = cache;
    }

    @Override
    public final void parse(JavaBean bean) {
        if (parserClassSet.contains(bean.getTargetClass())) {
            return;
        }
        this.parseInternal(bean);
        parserClassSet.add(bean.getTargetClass());
    }

    protected abstract void parseInternal(JavaBean bean);


    protected String getColumnName(BeanField field) {
        if (!Modifier.isPublic(field.getField().getModifiers())) {
            if (field.getGetterMethod() == null) {
                throw new JdbcException("找不到Getter方法: " + field.getField().getName());
            }
            if (field.getSetterMethod() == null) {
                throw new JdbcException("找不到Setter方法: " + field.getField().getName());
            }
        }

        Column annotation = field.getDeclaredAnnotation(Column.class);
        if (annotation != null) {
            return annotation.name();
        }
        return field.getColumnName();
    }

    protected void setFieldConfig(BeanField field, DefaultFieldConfig config) {
        String columnName = this.getColumnName(field);
        String escapeColumnName = escape ? dialect.escape(columnName) : columnName;
        config.setColumnName(columnName);
        config.setEscapeColumnName(escapeColumnName);
        config.setBeanField(field);
    }

}
