package io.jutil.jdo.core.engine;

import io.jutil.jdo.core.annotation.LockModeType;
import io.jutil.jdo.core.parser.EntityConfig;

import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-24
 */
public interface JdoTemplate {
	/**
	 * 保存对象
	 *
	 * @param object 对象
	 * @return 影响记录数
	 */
	default int save(Object object) {
		return this.save(object, true);
	}

	/**
	 * 保存对象
	 *
	 * @param object  对象
	 * @param dynamic 空字段是否也写进SQL语句中
	 * @return 影响记录数
	 */
	int save(Object object, boolean dynamic);

	/**
	 * 保存对象
	 *
	 * @param clazz 对象类型
	 * @param param <字段名, 字段值>
	 * @return 影响记录数
	 */
	int saveObject(Class<?> clazz, Map<String, ?> param);

	/**
	 * 保存对象列表，默认不产生主键
	 *
	 * @param objectList 对象列表
	 * @return 影响记录数
	 */
	int[] saveList(List<?> objectList);

	/**
	 * 更新对象
	 *
	 * @param object
	 * @return 影响记录数
	 */
	default int update(Object object) {
		return this.update(object, true);
	}

	/**
	 * 更新对象
	 *
	 * @param object
	 * @param dynamic 空字段是否也写进SQL语句中
	 * @return 影响记录数
	 */
	int update(Object object, boolean dynamic);

	/**
	 * 更新对象
	 *
	 * @param clazz 对象类型
	 * @param id    对象主键值
	 * @param param <字段名, 字段值>
	 * @return 影响记录数
	 */
	int updateObject(Class<?> clazz, Object id, Map<String, ?> param);

	/**
	 * 更新对象列表
	 *
	 * @param objectList 对象列表
	 * @return 影响记录数
	 */
	int[] updateList(List<?> objectList);

	/**
	 * 对象自增长
	 *
	 * @param clazz 对象类型
	 * @param id    对象主键
	 * @param param <字段名, 增长值>
	 * @return 影响记录数
	 */
	int inc(Class<?> clazz, Object id, Map<String, ? extends Number> param);

	/**
	 * 根据主键删除对象
	 *
	 * @param clazz 对象类型
	 * @param id    主键
	 * @return 影响数据库记录数
	 */
	int deleteId(Class<?> clazz, Object id);

	/**
	 * 根据主键列表删除对象
	 *
	 * @param clazz  对象类型
	 * @param idList 主键列表
	 * @return 影响数据库记录数
	 */
	<K, T> int deleteIdList(Class<T> clazz, List<K> idList);

	/**
	 * 根据字段值删除
	 *
	 * @param clazz 对象类型
	 * @param param <字段名, 字段值>
	 * @return 影响记录数
	 */
	<T> int deleteBy(Class<T> clazz, Map<String, ?> param);

	/**
	 * 根据主键取得一个对象
	 *
	 * @param id    单主键
	 * @param clazz 对象类型
	 * @return 对象，不存在返回 null
	 */
	default <T> T get(Class<T> clazz, Object id) {
		return this.get(clazz, id, LockModeType.NONE);
	}

	/**
	 * 根据主键取得一个对象
	 *
	 * @param id    单主键
	 * @param clazz 对象类型
	 * @param type  锁类型
	 * @return 对象，不存在返回 null
	 */
	<T> T get(Class<T> clazz, Object id, LockModeType type);

	/**
	 * 根据主键取得对象Map
	 *
	 * @param clazz  对象类型
	 * @param idList 主键列表
	 * @return 对象Map
	 */
	<K, T> Map<K, T> getList(Class<T> clazz, List<K> idList);

	/**
	 * 查询一个字段
	 *
	 * @param clazz  对象类型
	 * @param target 字段类型
	 * @param field  字段名称
	 * @param param  <字段名, 字段值>
	 * @return 字段值
	 */
	<T> T getField(Class<?> clazz, Class<T> target, String field, Map<String, ?> param);

	/**
	 * 查询一个对象
	 *
	 * @param clazz 对象类型
	 * @param param <字段名, 字段值>
	 * @return 对象
	 */
	<T> T getObject(Class<T> clazz, Map<String, ?> param);

	/**
	 * 判断对象某些字段是否存在
	 *
	 * @param object 对象
	 * @param names  字段列表
	 * @return true表示存在，false表示不存在
	 */
	boolean exist(Object object, String... names);

	/**
	 * 查询记录数
	 *
	 * @param clazz 类型
	 * @param param <字段名, 字段值>
	 * @return 记录数
	 */
	int count(Class<?> clazz, Map<String, ?> param);

	/**
	 * 根据 SQL 语句查询一个对象
	 *
	 * @param clazz     对象类型
	 * @param sql       SQL 语句
	 * @param <T>       对象参数
	 * @return 单个对象，不存在返回 null
	 */
	default <T> T getObject(Class<T> clazz, String sql) {
		return this.getObject(clazz, sql, null);
	}

	/**
	 * 根据 SQL 语句查询一个对象
	 *
	 * @param clazz     对象类型
	 * @param sql       SQL 语句
	 * @param paramList 参数列表
	 * @param <T>       对象参数
	 * @return 单个对象，不存在返回 null
	 */
	<T> T getObject(Class<T> clazz, String sql, List<?> paramList);

	/**
	 * 根据SQL语句查询列表，以对象列表返回
	 *
	 * @param clazz 对象类型
	 * @param sql   SQL语句
	 * @return 对象列表
	 */
	default <T> List<T> list(Class<T> clazz, String sql) {
		return this.list(clazz, sql, null);
	}

	/**
	 * 根据SQL语句查询列表，以对象列表返回
	 *
	 * @param clazz 对象类型
	 * @param sql   SQL语句
	 * @param start 起始行号
	 * @param size  最大记录数
	 * @return 对象列表
	 */
	<T> List<T> list(Class<T> clazz, String sql, int start, int size);

	/**
	 * 根据SQL语句查询列表，以对象列表返回
	 *
	 * @param clazz     对象类型
	 * @param sql       SQL语句
	 * @param paramList 查询参数
	 * @param start     起始行号
	 * @param size      最大记录数
	 * @return 对象列表
	 */
	<T> List<T> list(Class<T> clazz, String sql, List<?> paramList, int start, int size);

	/**
	 * 根据SQL语句查询列表，以对象列表返回
	 *
	 * @param clazz     对象类型
	 * @param sql       SQL语句
	 * @param paramList 查询参数
	 * @return 对象列表
	 */
	<T> List<T> list(Class<T> clazz, String sql, List<?> paramList);

	/**
	 * 查询字段列表
	 *
	 * @param clazz  对象类型
	 * @param target 字段类型
	 * @param field  字段名称
	 * @param param  <字段名, 字段值>
	 * @return 字段列表
	 */
	<T> List<T> listField(Class<?> clazz, Class<T> target, String field, Map<String, ?> param);

	/**
	 * 查询对象列表
	 *
	 * @param clazz 对象类型
	 * @param param <字段名, 字段值>
	 * @return 对象列表
	 */
	<T> List<T> listObject(Class<T> clazz, Map<String, ?> param);

	/**
	 * 执行SQL
	 *
	 * @param sql SQL
	 * @return 影响数据库记录数
	 */
	int execute(String sql);

	/**
	 * 检查配置
	 * @param clazz 对象类型
	 * @return
	 */
	EntityConfig checkEntityConfig(Class<?> clazz);

}
