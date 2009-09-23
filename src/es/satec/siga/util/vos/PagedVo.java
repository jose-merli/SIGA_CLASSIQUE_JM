package es.satec.siga.util.vos;

public class PagedVo {
	private int pageSize=Integer.MAX_VALUE;
	private int page=1;
	private Integer totalListSize=null;

	public Integer getPageSize() {
		return new Integer(pageSize);
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return new Integer(page);
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	public int getFirstRecord(int page){
		return ((page-1)*pageSize)+1;
	}

	public int getLastRecord(int page){
		int max = page*pageSize;

		return max>totalListSize.intValue()?totalListSize.intValue():max;
	}

	public int getFirstRecord(){
		return getFirstRecord(page);
	}

	public int getLastRecord(){
		return getLastRecord(page);
	}

	public Integer getTotalListSize() {
		return totalListSize;
	}

	public void setTotalListSize(Integer totalListSize) {
		this.totalListSize = totalListSize;
	}

	public void getNextPage() {
		this.page+=1;
	}

	public void getPreviousPage() {
		this.page-=1;
	}
}
