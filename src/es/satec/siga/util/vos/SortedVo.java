package es.satec.siga.util.vos;

import java.util.HashMap;
import java.util.Map;

public class SortedVo {
	private Map criteria=new HashMap();
	
	public void setCriteria(String column){
		setCriteria(column, true);
	}

	public void setCriteria(String column, Boolean direction){
		setCriteria(column, direction.booleanValue());
	}

	public void setCriteria(String column, boolean direction){
		criteria.put(column, SortDirection.valueOf(direction));
	}
	
	public Map.Entry[] getCriteria(){
		return (Map.Entry[])criteria.entrySet().toArray(new Map.Entry[criteria.size()]);
	}
}
