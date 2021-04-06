package framework.vo;

import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Pagination 정보
 *
 * @author ByeongDon
 */
public class PageVO {

	/** 현재페이지, 0이면 전체 */
	protected int pageNo = 0;
	/** 페이지당 Row 수 */
	protected int rowsPerPage = 10;
	/** 전체 Rows */
	protected int totalRows = 0;

	/** 정렬조건 */
	protected List<SortVO> sort;

	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = Math.max(0, pageNo);
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}
	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = Math.min(Math.max(1, rowsPerPage), 500);
	}

	public int getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(int totalRows) {
		this.totalRows = Math.max(0, totalRows);
		this.pageNo = Math.min(pageNo, getTotalPages());
	}

	public List<SortVO> getSort() {
		return sort;
	}
	public void setSort(List<SortVO> sort) {
		this.sort = sort;
	}

	/**
	 * 전체 페이지 수
	 */
	public int getTotalPages() {
		return (pageNo == 0 || totalRows == 0)? 1 : (totalRows-1)/rowsPerPage + 1;
	}
	/**
	 * 쿼리파라미터 - 시작 RowNum
	 */
	@JsonIgnore
	public int getFirstRowNum() {
		return (pageNo == 0 || totalRows == 0)? 1 : (pageNo - 1) * rowsPerPage + 1;
	}
	/**
	 * 쿼리파라미터 - 시작 RowNum
	 */
	@JsonIgnore
	public int getLastRowNum() {
		return (pageNo == 0)? Integer.MAX_VALUE : (totalRows == 0)? 1 : pageNo * rowsPerPage;
	}

	public boolean getHasOrderBy() {
        return (this.sort != null && !this.sort.isEmpty());
    }

    public String getOrderBy() {
        if (!getHasOrderBy()) {
            return null;
        }

        return StringUtils.collectionToDelimitedString(this.sort, ",");
    }
}
