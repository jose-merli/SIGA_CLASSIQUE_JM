package com.siga.beans;

import java.io.File;
import java.util.List;

import com.atos.utils.UsrBean;
import com.siga.administracion.SIGAConstants;
import com.siga.tlds.FilaExtElement;

public class FileInforme {
private File file;
private String nombre;
private String permisos;
private String fecha;
private List<FileInforme> files;
UsrBean usrBean;
boolean permitidoBorrar;
public boolean isPermitidoBorrar() {
	return permitidoBorrar;
}
public void setPermitidoBorrar(boolean permitidoBorrar) {
	this.permitidoBorrar = permitidoBorrar;
}
public FileInforme(String nombre, String permisos, String fecha, File file,boolean permitidoBorrar) {
	super();
	this.nombre = nombre;
	this.permisos = permisos;
	this.fecha = fecha;
	this.file = file;
	this.permitidoBorrar = permitidoBorrar;
}
public FileInforme() {
	super();
}
private String botones;
private FilaExtElement[] elementosFila;
public String getBotones() {
	if(isPermitidoBorrar())
		botones = "B";
	else
		botones = "";
	
	this.setBotones(botones);
	
	
	return botones;
}
public FilaExtElement[] getElementosFila() {
	FilaExtElement[] elementosFila = null;
	
	elementosFila = new FilaExtElement[2];
	elementosFila[1] = new FilaExtElement("download", "download","general.boton.download",	SIGAConstants.ACCESS_READ);
	this.setElementosFila(elementosFila);
	return elementosFila;
}
public void setElementosFila(FilaExtElement[] elementosFila) {
	this.elementosFila = elementosFila;
}

public void setBotones(String botones) {
	this.botones = botones;
}

public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
public String getPermisos() {
	return permisos;
}
public void setPermisos(String permisos) {
	this.permisos = permisos;
}
public String getFecha() {
	return fecha;
}
public void setFecha(String fecha) {
	this.fecha = fecha;
}
public List<FileInforme> getFiles() {
	return files;
}
public void setFiles(List<FileInforme> files) {
	this.files = files;
}
public File getFile() {
	return file;
}
public void setFile(File file) {
	this.file = file;
}

}
