/**
 * <p>Title: PairsKeys </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

public class PairsKeys
    {
    String id=""; String value="";
    Object idObj=null;
    Object valueObj=null;

        public PairsKeys(){}
        public String getId(){
                return id;
        }
        public String getValue()	{
                return value;
        }
        public void setId(String ide){
                id=ide;
        }
        public void setValue(String val){
                value=val;
        }

        public Object getIdObj(){
        return idObj;
        }
        public Object getValueObj()	{
        return valueObj;
        }
        public void setIdObj(Object ide){
        idObj=ide;
        }
        public void setValueObj(Object val){
        valueObj=val;
        }
    }