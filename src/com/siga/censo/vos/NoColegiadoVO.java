package com.siga.censo.vos;

/**
 */
public class NoColegiadoVO extends ClienteVO {

	private int tipo;



	public NoColegiadoVO(){

	}

	public int getTipo(){
		return tipo;
	}

	/**
	 * 
	 * @param newVal    newVal
	 */
	public void setTipo(int newVal){
		tipo = newVal;
	}

}