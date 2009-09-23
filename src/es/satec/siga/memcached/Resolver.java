package es.satec.siga.memcached;

import java.lang.reflect.InvocationTargetException;

public interface Resolver {
	public Class getType(String property);
	public Class getType(String base, String property)throws SecurityException, NoSuchMethodException;
	public Object getValue(String property);
	public Object getValue(String base, String property)throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException ;
	public Object setValue(String property, Object value);
	public Object setValue(String base, String property, Object value)throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException ;
}
