package io.jutil.jdo.core.engine;

import io.jutil.jdo.core.collection.Page;
import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.EntityConfig;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-28
 */
public abstract class QueryDao<P, R> {
	protected static final String COUNT_TPL = "select count(*) from %s a";
	protected static final String SELECT_TPL = "select a.* from %s a";

	protected JdoTemplate jdoTemplate;

	protected final Class<P> paramClazz;
	protected final Class<R> returnClazz;

	@SuppressWarnings("unchecked")
	protected QueryDao() {
		Class<?> clazz = this.getClass();
		if (clazz.getName().contains("$$")) {
			clazz = clazz.getSuperclass();
		}
		Type[] types = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
		if (types.length == 2) {
			this.paramClazz = (Class<P>) types[0];
			this.returnClazz = (Class<R>) types[1];
		} else if (types.length == 1) {
			this.paramClazz = (Class<P>) types[0];
			this.returnClazz = (Class<R>) types[0];
		} else {
			throw new JdbcException("ParameterizedType Error");
		}
	}

	/**
	 * 列出在数据库中的所有对象，不分页
	 *
	 * @param object 查询参数
	 * @return 对象列表
	 */
	public List<R> list(P object) {
		StringBuilder sql = this.select();
		var paramList = this.where(sql, object);
		this.orderBy(sql, object);
		List<R> list = jdoTemplate.list(returnClazz, sql.toString(), paramList);
		return list;
	}

	/**
	 * 列出在数据库中的对象，必须分页
	 *
	 * @param object 查询参数，若为 null，则查询所有记录
	 * @param start  起始行号，0开始
	 * @param size   最多记录数，不能小于1
	 * @return
	 */
	public List<R> listPage(P object, int start, int size) {
		StringBuilder sql = this.select();
		var paramList = this.where(sql, object);
		this.orderBy(sql, object);
		List<R> list = jdoTemplate.list(returnClazz, sql.toString(), paramList, start, size);
		return list;
	}

	/**
	 * 列出在数据库中的对象，必须分页
	 *
	 * @param object 查询参数
	 * @param page   数据库分页对象
	 * @return 数据库分页对象，不创建先的分页对象
	 */
	public Page listPage(P object, Page page) {
		if (page == null) {
			page = new Page();
		}

		int rows = this.getTotalResult(object);
		page.setTotalResult(rows);

		List<R> list = this.listPage(object, page.getRowIndex(), page.getPageSize());
		page.setResults(list);

		return page;
	}

	/**
	 * 查询对象的记录数
	 *
	 * @param object 查询参数
	 * @return 记录数
	 */
	public int getTotalResult(P object) {
		StringBuilder sql = this.selectCount();
		var paramList = this.where(sql, object);
		return jdoTemplate.getObject(Integer.class, sql.toString(), paramList);
	}

	private List<Object> where(StringBuilder sql, P object) {
		List<Object> paramList = new ArrayList<>();
		Expression exp = Expression.and();
		this.query(exp, object, paramList);
		String strExp = exp.toString();
		if (!strExp.isEmpty()) {
			sql.append(" where ").append(strExp);
		}
		return paramList;
	}

	private void orderBy(StringBuilder sql, P object) {
		OrderBy order = Expression.orderBy();
		this.orderBy(order, object);
		String strOrder = order.toString();
		if (!strOrder.isEmpty()) {
			sql.append(" order by ").append(strOrder);
		}
	}

	/**
	 * <p>创建 select count(*) 查询语句，<b>子类通过覆盖该方法来自定义查询</b></p>
	 *
	 * @return select 查询语句
	 */
	protected StringBuilder selectCount() {
		EntityConfig entityConfig = jdoTemplate.checkEntityConfig(returnClazz);
		StringBuilder sql = new StringBuilder(String.format(COUNT_TPL, entityConfig.getEscapeTableName()));
		return sql;
	}

	/**
	 * <p>创建 select 查询语句，<b>子类通过覆盖该方法来自定义投影</b></p>
	 *
	 * @return select 查询语句
	 */
	protected StringBuilder select() {
		EntityConfig entityConfig = jdoTemplate.checkEntityConfig(returnClazz);
		StringBuilder sql = new StringBuilder(String.format(SELECT_TPL, entityConfig.getEscapeTableName()));
		return sql;
	}

	/**
	 * <p>创建查询 WHERE 子句，<b>子类通过覆盖该方法来自定义查询</b></p>
	 * <b>注：父类该方法并没作任何查询
	 *
	 * @param exp       拼接SQL语句表达式
	 * @param object    对象查询参数
	 * @param paramList SQL占位符参数
	 */
	protected void query(Expression exp, P object, List<Object> paramList) {
	}

	protected void orderBy(OrderBy order, P object) {
		if (paramClazz != returnClazz) {
			return;
		}

		EntityConfig entityConfig = jdoTemplate.checkEntityConfig(returnClazz);
		var id = entityConfig.getIdConfig();
		if (id == null) {
			return;
		}

		order.add("a." + id.getEscapeColumnName() + " desc");
	}

	public void setJdoTemplate(JdoTemplate jdoTemplate) {
		this.jdoTemplate = jdoTemplate;
	}
}
