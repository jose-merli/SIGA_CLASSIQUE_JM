package com.siga.Utilidades;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.ajaxtags.helpers.ValueItem;

public class AjaxMultipleCollectionXmlBuilder<E> extends AjaxMultipleValueListXmlBuilder {

	public AjaxMultipleCollectionXmlBuilder<E> addItems(Collection<Collection<E>> colecciones){
		for (Collection<E> items:colecciones){
			 List<ValueItem> valueItem = addItem(items);
			 addValuesList(valueItem);
		}
		
		return this;
	}
	
	private List<ValueItem> addItem(Collection<E> collection){
		String name=null;
		String value=null;
		boolean isCData=false;
		List<ValueItem> items = new ArrayList<ValueItem>();
		
		for (E objeto:collection){
			if (objeto.getClass().getAnnotation(AjaxXMLBuilderAnnotation.class)!=null){
				for (Method m : objeto.getClass().getMethods()) {
					if (m.getParameterTypes()!=null && m.getParameterTypes().length>0)
						continue;
					if (m.isAnnotationPresent(AjaxXMLBuilderNameAnnotation.class)) {
						try {
							name=m.invoke(objeto).toString();
						} catch (Throwable ex) {
						}
					} else if (m.isAnnotationPresent(AjaxXMLBuilderValueAnnotation.class)) {
						isCData=((AjaxXMLBuilderValueAnnotation)m.getAnnotation(AjaxXMLBuilderValueAnnotation.class)).isCData();
						try {
							value=m.invoke(objeto).toString();
						} catch (Throwable ex) {
						}
					}
				}
				items.add(new ValueItem(name, isCData, value));
			}
		}
		
		return items;
	}
}

