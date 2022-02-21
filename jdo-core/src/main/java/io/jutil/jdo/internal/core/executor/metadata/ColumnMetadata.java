package io.jutil.jdo.internal.core.executor.metadata;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
@Getter
@Setter
@NoArgsConstructor
public class ColumnMetadata {
    protected String columnName;
    protected int type;
    protected String typeName;
    protected boolean nullable = true;
    protected int size;
    protected String def;
    protected String comment;
}
