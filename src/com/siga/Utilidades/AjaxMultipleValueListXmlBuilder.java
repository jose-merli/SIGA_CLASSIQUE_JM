package com.siga.Utilidades;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.ajaxtags.helpers.ValueItem;
import net.sourceforge.ajaxtags.xml.BaseXmlBuilder;

public abstract class AjaxMultipleValueListXmlBuilder extends BaseXmlBuilder<ValueItem>{
	private Collection<Collection<ValueItem>> valuesList;
	
	public void addValuesList(Collection<ValueItem> valueItem){
		if(valuesList==null)
			valuesList = new ArrayList<Collection<ValueItem>>();
		valuesList.add(valueItem);	
	}

    private String valueToString(ValueItem item) {
    	StringBuilder stringbuilder = new StringBuilder("<item>");

    	stringbuilder.append("<name>");
        if(item.isAsCData())
            stringbuilder.append("<![CDATA[");
        stringbuilder.append(item.getName());
        if(item.isAsCData())
            stringbuilder.append("]]>");
        stringbuilder.append("</name>");

        for (Object valor:item.getValue()){
            stringbuilder.append("<value>");
            if(item.isAsCData())
                stringbuilder.append("<![CDATA[");
            stringbuilder.append(valor);
            if(item.isAsCData())
                stringbuilder.append("]]>");
            stringbuilder.append("</value>");
        }

    	stringbuilder.append("</item>");

        return stringbuilder.toString();
    }

    protected String getXMLString() {
        StringBuilder stringbuilder = new StringBuilder("<ajax-response>");

        for (Collection<ValueItem> lista:getValuesList()){
        	stringbuilder.append("<response>");
        	for(ValueItem elemento:lista){
	            stringbuilder.append(valueToString(elemento));
            }
        	stringbuilder.append("</response>");
        }

        stringbuilder.append("</ajax-response>");
        return stringbuilder.toString();
    }

	public Collection<Collection<ValueItem>> getValuesList() {
		return valuesList;
	}

	public void setValuesList(Collection<Collection<ValueItem>> valuesList) {
		this.valuesList = valuesList;
	}
}
