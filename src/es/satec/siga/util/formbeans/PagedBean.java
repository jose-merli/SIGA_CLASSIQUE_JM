package es.satec.siga.util.formbeans;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import es.satec.siga.util.vos.PagedVo;

public abstract class PagedBean extends SortedBean {
	private static final long serialVersionUID = -5708125575603975421L;

	private static final Integer DEFAULT_PAGE_SIZE = new Integer(7);
	private static final Integer DEFAULT_PAGE=new Integer(1);

	private Integer pageSize=DEFAULT_PAGE_SIZE;
	private Integer page=DEFAULT_PAGE;
	private Integer totalTableSize=null;
	private List table=null;

	public PagedBean(){
		super();
	}
	
	public void setPage(String page){
		if (StringUtils.isEmpty(page))
			this.page=DEFAULT_PAGE;
		else
			this.page=new Integer(page);
	}
	
	public Integer getPage(){
		return page;
	}
	
	public void setTotalTableSize(int totalTableSize){
		this.totalTableSize=new Integer(totalTableSize);
	}
	
	public void setTotalTableSize(Integer totalTableSize){
		this.totalTableSize=totalTableSize;
	}
	
	public void setTotalTableSize(String totalTableSize){
		if (!StringUtils.isEmpty(totalTableSize))
			this.totalTableSize=new Integer(totalTableSize);
	}
	
	public Integer getTotalTableSize(){
		if (totalTableSize==null)
			return new Integer(0);
		return totalTableSize;
	}

	public void setTable(List table){
		this.table=table;
	}
	
	public List getTable(){
		return this.table;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize==null)
			this.pageSize=PagedBean.DEFAULT_PAGE;
		else
			this.pageSize = pageSize;
	}

	public void setRequest(HttpServletRequest request) {
		super.setRequest(request);
		
		ParamEncoder encoder=new ParamEncoder(getTableName());
		setPage(request.getParameter(encoder.encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
	}
	
	public PagedVo getPagedVo(){
		PagedVo paginador=new PagedVo();
		
		paginador.setTotalListSize(getTotalTableSize());
		paginador.setPageSize(getPageSize().intValue());
		
		return paginador;
	}
}
