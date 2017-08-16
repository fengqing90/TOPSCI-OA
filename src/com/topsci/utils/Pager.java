package com.topsci.utils;

import java.util.List;

public class Pager {

	// 排序方式
	public enum OrderType {
		asc, desc
	}

	// 当前页码
	private Integer pageNumber = 0;
	// 每页记录数
	private Integer pageSize = 20;
	// 总记录数
	private Integer totalCount = 0;
	// 总页数
	private Integer pageCount = 0;
	// 模糊查询对象
	private Object likeObject;
	// 排序字段
	private String orderBy;
	// 排序方式
	private OrderType orderType = OrderType.desc;
	// 数据List
	private List<?> list;

	/** 逻辑分页 **/
	private Boolean logicalPaging = false;

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageCount() {
		pageCount = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			pageCount++;
		}
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Object getLikeObject() {
		return likeObject;
	}

	public void setLikeObject(Object likeObject) {
		this.likeObject = likeObject;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public Boolean getLogicalPaging() {
		return logicalPaging;
	}

	public void setLogicalPaging(Boolean logicalPaging) {
		this.logicalPaging = logicalPaging;
	}

	@Override
	public String toString() {
		return "Pager [keyword="
				// + keyword +
				+ ", list=" + list + ", orderBy=" + orderBy + ", orderType=" + orderType + ", pageCount=" + pageCount
				+ ", pageNumber=" + pageNumber + ", pageSize=" + pageSize + ", property="
				// + property +
				+ ", totalCount=" + totalCount + "]";
	}

}
