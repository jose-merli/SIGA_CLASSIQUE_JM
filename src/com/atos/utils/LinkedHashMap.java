/**
 * <p>Title: LinkedHashMap </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.io.Serializable;


public class LinkedHashMap extends Hashtable implements Serializable {

	private LinkedList orderedKeys;

    public LinkedHashMap() {
		super();
		orderedKeys = new LinkedList();
    }

	public void foo() {
//		this.
	}

	public Object put(Object key, Object value) {
		Object o = super.put(key, value);
		orderedKeys.add(key);
		return o;
	}

	public Object remove(Object key) {
		Object o = super.remove(key);
		orderedKeys.remove(key);
		return o;
	}

	public Iterator orderedKeys() {
		return orderedKeys.iterator();
	}

	public Enumeration keys() {
		Vector v = new Vector(orderedKeys);
		return v.elements();
	}

	public Object clone() {
		LinkedHashMap m2 = new LinkedHashMap();
		m2.putAll(this);
		return m2;
	}

	public void putAll(Map m) {
		Iterator it = null;
		if (m instanceof LinkedHashMap) {
			it = ((LinkedHashMap) m).orderedKeys();
		} else {
			it = m.keySet().iterator();
		}
		while (it.hasNext()) {
			Object key = it.next();
			put(key, m.get(key));
		}
	}

	public void changeKey(String oldKey, String newKey) {
		if (!containsKey(oldKey)) return;

		int idx = orderedKeys.indexOf(oldKey);
		put(newKey, remove(oldKey));
		orderedKeys.remove(newKey);
		orderedKeys.add(idx, newKey);
	}

}