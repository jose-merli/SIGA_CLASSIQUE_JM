package com.siga.Utilidades;

import java.lang.reflect.Method;
import java.util.Collection;

import net.sourceforge.ajaxtags.xml.AjaxValueListXmlBuilder;

public class AjaxCollectionXmlBuilder<E> extends AjaxValueListXmlBuilder {

	public AjaxCollectionXmlBuilder<E> addItems(Collection<E> collection){
		String name=null;
		String value=null;
		boolean isCData=false;
		
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
				addItem(name, isCData, value);
			}
		}
		
		return this;
	}
}

