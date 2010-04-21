package com.siga.censo.form;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.siga.censo.vos.NoColegiadoVO;
import com.siga.comun.form.BaseForm;
import com.siga.comun.vos.Vo;


/**
 */
public class BusquedaNoColegiadoForm extends BaseForm {

	private static final long serialVersionUID = 695063387318018368L;

	private List<NoColegiadoVO> listaNoColegiados = null;

	public BusquedaNoColegiadoForm(){

	}


	public List<NoColegiadoVO> getlistaNoColegiados(){
		return listaNoColegiados;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setlistaNoColegiados(List<NoColegiadoVO> newVal){
		listaNoColegiados = newVal;
	}


	@Override
	public void fromVO(Vo vo) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Vo toVO() {
		// TODO Auto-generated method stub
		return null;
	}

}
