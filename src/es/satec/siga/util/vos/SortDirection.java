package es.satec.siga.util.vos;

import java.io.Serializable;

public class SortDirection implements Serializable{
	private static final long serialVersionUID = -112005611910175451L;
	public static final SortDirection ASCENDING=new SortDirection(true);
	public static final SortDirection DESCENDING=new SortDirection(false);
	
	private boolean ascending=true;
	
	private SortDirection (boolean direction){
		ascending=direction;
	}
	
	public static SortDirection valueOf(Boolean direction){
		if (direction.equals(Boolean.TRUE))
			return ASCENDING;
		else
			return DESCENDING;
	}
	
	public static SortDirection valueOf(boolean direction){
		if (direction)
			return ASCENDING;
		else
			return DESCENDING;
	}
	
	public String getString(){
		return toString();
	}
	
	public String toString(){
		if (ascending) 
			return "asc";
		else
			return "desc";
	}
}
