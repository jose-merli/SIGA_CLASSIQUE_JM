package com.siga.censo.vos;


/**
 * @version 1.0
 * @created 09-Feb-2010 17:01:20
 */
public class ColegiadoVO extends ColegiadoAvanzadoVO {

	/**
	 * S�lo se emplea en las b�squedas de colegiados
	 */
	private String nColegiado;


	public ColegiadoVO(){

	}


	public void setnColegiado(String nColegiado) {
		this.nColegiado = nColegiado;
	}


	public String getnColegiado() {
		return nColegiado;
	}


}