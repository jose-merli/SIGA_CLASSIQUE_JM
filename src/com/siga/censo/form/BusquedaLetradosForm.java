package com.siga.censo.form;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.displaytag.decorator.DisplaytagColumnDecorator;

import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesString;
import com.siga.censo.vos.LetradoVO;
import com.siga.comun.decorator.CheckboxDecorator;
import com.siga.comun.form.PagedSortedForm;
import com.siga.comun.vos.Vo;

public class BusquedaLetradosForm extends PagedSortedForm {

	private static final long serialVersionUID = -1399131790004490633L;

	/**
	 * Identificador de la tabla en la pagina html
	 */
	private static final String TABLE_NAME = "tablaLetrados";
	private static final Integer DEFAULT_PAGE_SIZE = 22;
	private static final String DEFAULT_SORT_COLUMN = "3";
	
	private String idInstitucion;
	private String residencia;
	private String apellidos1;
	private String apellidos2;
	private String nif;
	private String nombre;
	private String fechaNacimiento;
	private boolean busquedaExacta;

	public BusquedaLetradosForm(){
		setPageSize(DEFAULT_PAGE_SIZE);
		//mapeo de las columnas por las cuales se ordenara la lista de resultados
		//En este caso se anyade idInstitucion por un problema con el parametro 
		//NLS_COMP=linguistic;
		setColumnTranslation("","","","nifCif","apellidos1","nombre","fechaNacimiento","idInstitucion");
		//id de la tabla en la pagina html
		setTableName(TABLE_NAME);
	}

	public String getResidencia() {
		return residencia;
	}

	public void setResidencia(String residencia) {
		this.residencia = residencia;
	}

	public String getApellidos1() {
		return apellidos1;
	}

	public void setApellidos1(String apellidos1) {
		this.apellidos1 = apellidos1;
	}

	public String getApellidos2() {
		return apellidos2;
	}

	public void setApellidos2(String apellidos2) {
		this.apellidos2 = apellidos2;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Vo toVO(){
		LetradoVO vo = new LetradoVO();
		vo.setIdInstitucion(idInstitucion);
		vo.setNombre(nombre);
		vo.setApellidos1(apellidos1);
		vo.setApellidos2(apellidos2);
		try {
			vo.setFechaNacimiento(UtilidadesFecha.getDate(fechaNacimiento,UtilidadesFecha.FORMATO_FECHA_ES));
		} catch (ParseException e) {
			vo.setFechaNacimiento(null);
		}
		vo.setNif(nif);
		vo.setResidencia(residencia);
		vo.setBusquedaExacta(busquedaExacta);
		return  vo;
	}

	public void fromVO(Vo vo) {
		LetradoVO letrado = (LetradoVO) vo;
		idInstitucion = letrado.getIdInstitucion();
		nombre = letrado.getNombre();
		apellidos1 = letrado.getApellidos1();
		apellidos2 = letrado.getApellidos2();
		try {
			fechaNacimiento = UtilidadesString.formatoFecha(letrado.getFechaNacimiento(), UtilidadesFecha.FORMATO_FECHA_ES);
		} catch (ClsExceptions e) {
			fechaNacimiento = null;
		}
		nif = letrado.getNif();
		residencia = letrado.getResidencia();
	}

	public void setBusquedaExacta(boolean busquedaExacta) {
		this.busquedaExacta = busquedaExacta;
	}

	public boolean getBusquedaExacta() {
		return busquedaExacta;
	}

	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getIdInstitucion() {
		return idInstitucion;
	}

	@Override
	public String getDefaultSortColumn() {
		return DEFAULT_SORT_COLUMN;
	}


	public DisplaytagColumnDecorator getDecorator(String checkboxName){
		CheckboxDecorator decorator = (CheckboxDecorator) super.getDecorator(checkboxName);
		List<String> disabled = new ArrayList<String>();
		for (Vo vo: getTable()){
			LetradoVO letrado = (LetradoVO) vo;
			if (UtilidadesString.stringToBoolean(letrado.getNoAparecerRedAbogacia()))
				disabled.add(letrado.getId());
		}
		decorator.setDisabled(disabled);
		return decorator;
	}


}
