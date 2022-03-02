package io.jutil.jdo.core.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库分页对象
 *
 * @author Jin Zheng
 * @since 2022-02-28
 */
public class Page {

	/**
	 * 总记录数
	 */
	private int totalResult = 0;

	/**
	 * 每页记录数
	 */
	private int pageSize = 20;

	/**
	 * 总页数
	 */
	private int totalPage = 0;

	/**
	 * 当前页，从1开始
	 */
	private int pageIndex = 1;

	/**
	 * 当前记录位置，从0开始
	 */
	private int rowIndex = 0;

	/**
	 * 从数据库里查到的对象列表
	 */
	private List<?> results = new ArrayList<>();

	public Page() {
	}

	/**
	 * 创建分页对象
	 * @param pageSize 每页记录数
	 * @param pageIndex 当前页数，从1开始
	 */
	public Page(int pageSize, int pageIndex) {
		this.setPageSize(pageSize);
		this.setPageIndex(pageIndex);
	}

	public void setTotalResult(int totalResult) throws IllegalArgumentException {
		if (totalResult < 0) {
			throw new IllegalArgumentException("总记录数不能小于 0");
		}

		this.totalResult = totalResult;
		this.totalPage = (totalResult + pageSize - 1) / pageSize;
	}

	public void setPageIndex(int pageIndex) throws IllegalArgumentException {
		if (pageIndex < 1) {
			throw new IllegalArgumentException("当前页面不能小于 1");
		}

		this.pageIndex = pageIndex;
		this.rowIndex = pageSize * (pageIndex - 1);
	}

	public int getTotalResult() {
		return totalResult;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getResults() {
		List<T> list = new ArrayList<>();
		for (Object object : results) {
			list.add((T) object);
		}
		return list;
	}

	public boolean hasNextPage() {
		return pageIndex < totalPage;
	}

	public boolean hasPreviewPage() {
		return pageIndex > 1;
	}

	public void setResults(List<?> objectList) {
		this.results = objectList;
	}

	public void setPageSize(int pageSize) {
		if (pageSize < 1) {
			throw new IllegalArgumentException("每页记录数不能小于 1");
		}

		this.pageSize = pageSize;
		this.rowIndex = pageSize * (pageIndex - 1);
	}

}
