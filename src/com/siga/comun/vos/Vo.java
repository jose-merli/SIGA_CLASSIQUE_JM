package com.siga.comun.vos;

public abstract class Vo {
	
	public static String PK_SEPARATOR = "@@@";

	public abstract String getId();
	public abstract void setId(String id);
	
	protected String getPk(String ... values){
		StringBuffer buf = new StringBuffer();
		for(String value: values){
			buf.append(value);
			buf.append(PK_SEPARATOR);
		}
		buf.delete(buf.lastIndexOf(PK_SEPARATOR), buf.length());
		return buf.toString();
	}
	
}
