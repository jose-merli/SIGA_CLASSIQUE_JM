package com.siga.comun.form;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import com.siga.comun.vos.PagedVo;
import com.siga.comun.vos.Vo;


public abstract class PagedForm extends BaseForm{
	private static final long serialVersionUID = -5708125575603975421L;

	private static final String DEFAULT_TABLE_NAME="tabla";
	private static final Integer DEFAULT_PAGE_SIZE = new Integer(20);
	private static final Integer DEFAULT_PAGE=new Integer(1);
	private String tableName=DEFAULT_TABLE_NAME;
	private Integer pageSize=DEFAULT_PAGE_SIZE;
	private Integer page=DEFAULT_PAGE;
	private Integer totalTableSize=null;
	private List<Vo> table=null;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
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

	public void setTable(List<Vo> table){
		this.table=table;
	}
	
	public List<Vo> getTable(){
		return this.table;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize==null)
			this.pageSize=DEFAULT_PAGE;
		else
			this.pageSize = pageSize;
	}

	public void setRequest(HttpServletRequest request) {
		ParamEncoder encoder=new ParamEncoder(getTableName());
		setPage(request.getParameter(encoder.encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
	}
	
	public PagedVo getPagedVo(){
		PagedVo paginador=new PagedVo();
		paginador.setPage(getPage());
		paginador.setTotalListSize(getTotalTableSize());
		paginador.setPageSize(getPageSize().intValue());
		
		return paginador;
	}

}