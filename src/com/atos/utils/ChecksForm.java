/*
 * Created on 23-nov-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.atos.utils;

import java.util.Vector;
import java.lang.IndexOutOfBoundsException;

import org.apache.struts.action.ActionForm;

/**
 * @author Jose.Zulueta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ChecksForm extends ActionForm{
private Vector lista=new Vector();
private String prueba="hola";

/**
 * @return Returns the lista.
 */
public Vector getTodos() {
	return lista;
}
/**
 * @param lista The lista to set.
 */
public void setTodos(Vector list) {
	this.lista = list;
}
public Lista getLista(int index){
	Lista aux = null;
	try {
		aux = (Lista)lista.get(index);
		if (aux==null) lista.set(index,new Lista());
	} catch (IndexOutOfBoundsException e) {
		lista.setSize(index+1);
		lista.set(index,new Lista());
	}
	return (Lista)lista.get(index);
};
public void setLista(int index,Lista l){
	lista.set(index,l);
}
/**
 * @return Returns the prueba.
 */
public String getPrueba() {
	return prueba;
}
/**
 * @param prueba The prueba to set.
 */
public void setPrueba(String prueba) {
	this.prueba = prueba;
}
}
