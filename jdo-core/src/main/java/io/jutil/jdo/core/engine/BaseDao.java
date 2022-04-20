package io.jutil.jdo.core.engine;

import io.jutil.jdo.core.annotation.LockModeType;
import io.jutil.jdo.internal.core.util.IdUtil;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-28
 */
@NoArgsConstructor
public class BaseDao<T> extends QueryDao<T, T> {

	/**
	 * 保存对象，动态生成SQL
	 *
	 * @param object 对象
	 * @return 影响记录数
	 */
	public int save(T object) {
		return jdoTemplate.save(object);
	}

	/**
	 * 保存对象
	 *
	 * @param object  对象
	 * @param dynamic 是否动态生成SQL
	 * @return 影响记录数
	 */
	public int save(T object, boolean dynamic) {
		return jdoTemplate.save(object, dynamic);
	}

	/**
	 * 保存对象
	 *
	 * @param param <字段名, 字段值>
	 * @return 影响记录数
	 */
	public int saveObject(Map<String, Object> param) {
		return jdoTemplate.saveObject(returnClazz, param);
	}

	/**
	 * 保存对象列表，不动态生成SQL
	 *
	 * @param objectList 对象列表
	 * @return 影响记录数
	 */
	public int[] saveList(List<T> objectList) {
		return jdoTemplate.saveList(objectList);
	}

	/**
	 * 根据主键删除对象
	 *
	 * @param id 主键
	 * @return 影响数据库记录数
	 */
	public int deleteId(Object id) {
		return jdoTemplate.deleteId(returnClazz, id);
	}

	/**
	 * 根据ID列表删除对象
	 *
	 * @param idList ID列表
	 * @return 影响记录数
	 */
	public <K> int deleteIdList(List<K> idList) {
		return jdoTemplate.deleteIdList(returnClazz, idList);
	}

	/**
	 * 根据字段值删除
	 *
	 * @param param <字段名, 字段值>
	 * @return 影响记录数
	 */
	public int deleteBy(Map<String, Object> param) {
		return jdoTemplate.deleteBy(returnClazz, param);
	}

	/**
	 * 更新对象，动态生成SQL
	 *
	 * @param object 对象
	 * @return 影响记录数
	 */
	public int update(T object) {
		return jdoTemplate.update(object);
	}

	/**
	 * 更新对象
	 *
	 * @param object  对象
	 * @param dynamic 是否动态生成SQL
	 * @return 影响记录数
	 */
	public int update(T object, boolean dynamic) {
		return jdoTemplate.update(object, dynamic);
	}

	/**
	 * 更新对象
	 *
	 * @param id    对象主键
	 * @param param <字段名, 字段值>
	 * @return 影响记录数
	 */
	public int updateObject(Object id, Map<String, Object> param) {
		return jdoTemplate.updateObject(returnClazz, id, param);
	}

	/**
	 * 更新对象列表，不动态生成SQL
	 *
	 * @param objectList 对象列表
	 * @return 影响记录数
	 */
	public int[] updateList(List<T> objectList) {
		return jdoTemplate.updateList(objectList);
	}

	/**
	 * 对象属性自增长
	 *
	 * @param id    对象主键
	 * @param param <字段名(字符串), 增长值(数字)>
	 * @return 影响记录数
	 */
	public int inc(Object id, Map<String, ? extends Number> param) {
		return jdoTemplate.inc(returnClazz, id, param);
	}

	/**
	 * 根据主键取得对象
	 *
	 * @param id 主键
	 * @return 对象
	 */
	public T get(Object id) {
		return jdoTemplate.get(returnClazz, id);
	}

	/**
	 * 根据主键取得对象
	 *
	 * @param id   主键
	 * @param type 锁类型
	 * @return 对象
	 */
	public T get(Object id, LockModeType type) {
		return jdoTemplate.get(returnClazz, id, type);
	}

	/**
	 * 根据主键取得对象
	 *
	 * @param id 主键
	 * @return 对象
	 */
	public T getSelect(Object id) {
		var config = jdoTemplate.checkEntityConfig(returnClazz);
		var idConfig = IdUtil.checkSingleId(config);

		List<Object> paramList = new ArrayList<>();
		paramList.add(id);
		StringBuilder sql = this.select();
		sql.append(" where a.").append(idConfig.getEscapeColumnName()).append("=?");
		return jdoTemplate.getObject(returnClazz, sql.toString(), paramList);
	}

	/**
	 * 查询一个字段
	 *
	 * @param target 字段类型
	 * @param field  字段名称
	 * @param param  <字段名, 字段值>
	 * @return 字段值
	 */
	public <K> K getField(Class<K> target, String field, Map<String, Object> param) {
		return jdoTemplate.getField(returnClazz, target, field, param);
	}

	/**
	 * 查询一个对象
	 *
	 * @param param <字段名, 字段值>
	 * @return 对象
	 */
	public T getObject(Map<String, Object> param) {
		return jdoTemplate.getObject(returnClazz, param);
	}

	/**
	 * 根据主键列表取得对象Map
	 *
	 * @param idList 主键列表
	 * @return 对象Map
	 */
	public <K> Map<K, T> getList(List<K> idList) {
		return jdoTemplate.getList(returnClazz, idList);
	}

	/**
	 * 判断对象某些字段是否存在
	 *
	 * @param object 对象
	 * @param names  字段列表
	 * @return true表示存在，false表示不存在
	 */
	public boolean exist(T object, String... names) {
		return jdoTemplate.exist(object, names);
	}

	/**
	 * 查询字段列表
	 *
	 * @param target 字段类型
	 * @param field  字段名称
	 * @param param  <字段名, 字段值>
	 * @return 字段列表
	 */
	public <K> List<K> listField(Class<K> target, String field, Map<String, Object> param) {
		return jdoTemplate.listField(returnClazz, target, field, param);
	}


	/**
	 * 查询对象列表
	 *
	 * @param param <字段名, 字段值>
	 * @return 对象列表
	 */
	public List<T> listObject(Map<String, Object> param) {
		return jdoTemplate.listObject(returnClazz, param);
	}

	/**
	 * 查询记录数
	 *
	 * @param param <字段名, 字段值>
	 * @return 记录数
	 */
	public int count(Map<String, Object> param) {
		return jdoTemplate.count(returnClazz, param);
	}
}
