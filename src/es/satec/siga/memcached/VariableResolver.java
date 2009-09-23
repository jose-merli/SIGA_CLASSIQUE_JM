package es.satec.siga.memcached;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class VariableResolver implements Resolver {
	private Map asociation = new HashMap();
	
	public Object getValue(String property){
		return asociation.get(property);
	}

	public Class getType(String property) {
		return asociation.get(property).getClass();
	}

	public Class getType(String base, String property) throws SecurityException, NoSuchMethodException {
		Class clase=asociation.get(base).getClass();
		Method metodo= clase.getDeclaredMethod(property, (Class[])null);
		
		if (metodo!=null){
			return metodo.getReturnType();
		} else {
			return null;
		}
	}

	public Object getValue(String base, String property) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object obj=asociation.get(base);
		Method metodo= obj.getClass().getDeclaredMethod(property, (Class[])null);
		
		if (metodo!=null){
			return metodo.invoke(obj, (Object[])null);
		} else {
			return null;
		}
	}

	public Object setValue(String property, Object value) {
		asociation.put(property, value);
		return value;
	}

	public Object setValue(String base, String property, Object value) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object obj=asociation.get(base);
		Method metodo=null;
//		Method metodo= obj.getClass().getDeclaredMethod(property, (Class[])(new Class[]){value.getClass()});
		
		if (metodo!=null){
			return metodo.invoke(obj, (Object[])null);
		} else {
			return null;
		}
	}
}
