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
public class IdMetadata extends ColumnMetadata {
    protected boolean inc = false;
}
