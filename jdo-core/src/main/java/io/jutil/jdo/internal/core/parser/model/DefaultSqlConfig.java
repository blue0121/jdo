package io.jutil.jdo.internal.core.parser.model;


import io.jutil.jdo.core.parser.SqlConfig;
import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.internal.core.util.AssertUtil;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
@Setter
@NoArgsConstructor
public class DefaultSqlConfig implements SqlConfig {
	private SqlItem selectById;
	private SqlItem selectByIdList;
	private SqlItem insert;
	private SqlItem updateById;
	private SqlItem updateByIdAndVersion;
	private SqlItem deleteById;
	private SqlItem deleteByIdList;


	@Override
	public void check() {
		AssertUtil.notNull(selectById, "SelectById SQL");
		selectById.check();
		AssertUtil.notNull(selectByIdList, "SelectByIdList SQL");
		selectByIdList.check();
		AssertUtil.notNull(insert, "Insert SQL");
		insert.check();
		AssertUtil.notNull(updateById, "UpdateById SQL");
		updateById.check();
		AssertUtil.notNull(deleteById, "DeleteById SQL");
		deleteById.check();
		AssertUtil.notNull(deleteByIdList, "DeleteByIdList SQL");
		deleteByIdList.check();
	}

	@Override
	public SqlItem getSelectById() {
		return selectById;
	}

	@Override
	public SqlItem getSelectByIdList() {
		return selectByIdList;
	}

	@Override
	public SqlItem getInsert() {
		return insert;
	}

	@Override
	public SqlItem getUpdateById() {
		return updateById;
	}

	@Override
	public SqlItem getUpdateByIdAndVersion() {
		return updateByIdAndVersion;
	}

	@Override
	public SqlItem getDeleteById() {
		return deleteById;
	}

	@Override
	public SqlItem getDeleteByIdList() {
		return deleteByIdList;
	}

}
