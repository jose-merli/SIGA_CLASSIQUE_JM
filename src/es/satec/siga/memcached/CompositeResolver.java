package es.satec.siga.memcached;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CompositeResolver implements Resolver {
	private List resolverList=new ArrayList();
	
	public void addResolver(Resolver resolver) {
		resolverList.add(resolver);
	}

	public Class getType(String property) {
		// TODO Auto-generated method stub
		return null;
	}

	public Class getType(String base, String property)
			throws SecurityException, NoSuchMethodException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(String property) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(String base, String property)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object setValue(String property, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object setValue(String base, String property, Object value)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}
}
