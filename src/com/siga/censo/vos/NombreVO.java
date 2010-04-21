package com.siga.censo.vos;



/**
 */
public class NombreVO {

	private String nombre;
	private String apellido1;
	private String apellido2;

	public NombreVO(){

	}

	public void finalize() throws Throwable {

	}

	public String getNombre(){
		return nombre;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setNombre(String newVal){
		nombre = newVal;
	}

	public String getApellido1(){
		return apellido1;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setApellido1(String newVal){
		apellido1 = newVal;
	}

	public String getapellido2(){
		return apellido2;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setapellido2(String newVal){
		apellido2 = newVal;
	}

}