package com.siga.comun.vos;

import java.util.Map;
import java.util.TreeMap;

import com.siga.Utilidades.SortCriteriaComparator;

public class SortedVo extends Vo{
	private Map<String, SortDirection> criteria=new TreeMap<String, SortDirection>(new SortCriteriaComparator());
	
	public void setCriteria(String column){
		setCriteria(column, true);
	}

	public void setCriteria(String column, Boolean direction){
		setCriteria(column, direction.booleanValue());
	}

	public void setCriteria(String column, boolean direction){
		criteria.put(column, SortDirection.valueOf(direction));
	}
	
	@SuppressWarnings("unchecked")
	public Map.Entry<String,SortDirection>[] getCriteria(){
		return (Map.Entry<String,SortDirection>[])criteria.entrySet().toArray(new Map.Entry[criteria.size()]);
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
	}

}
