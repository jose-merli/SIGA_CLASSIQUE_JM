/*
 * Fecha creaci�n: 17/02/2005
 * Autor: julio.vicente
 */

package com.siga.gratuita.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.redabogacia.sigaservices.app.autogen.model.ScsEstadoejg;
import org.redabogacia.sigaservices.app.vo.scs.EstadoEjgVo;

import com.siga.beans.*;
import com.siga.general.MasterForm;

/**
* Maneja el formulario que mantiene la tabla SCS_EJGFORM
*/
 public class DefinirEstadosEJGForm extends MasterForm{

	/*
	 * Metodos SET
	 */
	 public String verHistorico = "0";
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8012668144229670475L;

	/**
	 * Almacena en la Hashtable el anho del EJG 
	 * 
	 * @param valor Anho de la EJG. De tipo "Integer". 
	 * @return void 
	 */
	public void setAnio 					    (String anio)					{ this.datos.put(ScsEstadoEJGBean.C_ANIO, anio);									}
	public void setAutomatico 					(String automatico)					{ this.datos.put(ScsEstadoEJGBean.C_AUTOMATICO, automatico);									}
	/**
	 * Almacena en la Hashtable el numero del EJG 
	 * 
	 * @param valor Numero de la EJG. De tipo "String". 
	 * @return void 
	 */
	public void setNumero 					(String numero)					{ this.datos.put(ScsEstadoEJGBean.C_NUMERO, numero);								}
	/**
	 * Almacena en la Hashtable la fecha de apretura del estado 
	 * 
	 * @param valor Fecha de inicio del estado. De tipo "String". 
	 * @return void 
	 */
	public void setFechaInicio			(String	 fechaInicio)				{ this.datos.put(ScsEstadoEJGBean.C_FECHAINICIO, fechaInicio);							}
	
	/**
	 * Almacena en la Hashtable el identificador de la institucion del dictamen del EJG 
	 * 
	 * @param valor Identificador de la institucion del dictamen del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setIdInstitucion			(String idInstitucion)			{ this.datos.put(ScsEstadoEJGBean.C_IDINSTITUCION, idInstitucion);		}
	
	/**
	 * Almacena en la Hashtable el identificador del estado por ejg 
	 * 
	 * @param valor Identificador estado. De tipo "String". 
	 * @return void 
	 */
	public void setIdEstadoPorEJG				(String idEstadoPorEJG)			{ this.datos.put(ScsEstadoEJGBean.C_IDESTADOPOREJG, idEstadoPorEJG);		}
	
	/**
	 * Almacena en la Hashtable las observaciones 
	 * 
	 * @param valor Identificador estado. De tipo "String". 
	 * @return void 
	 */
	public void setObservaciones				(String observaciones)			{ this.datos.put(ScsEstadoEJGBean.C_OBSERVACIONES, observaciones);		}
	
	/**
	 * Almacena en la Hashtable el identificador del estado 
	 * 
	 * @param valor Identificador estado. De tipo "String". 
	 * @return void 
	 */
	public void setIdEstadoEJG				(String idEstadoEJG)			{ this.datos.put(ScsEstadoEJGBean.C_IDESTADOEJG, idEstadoEJG);		}
	/**
	 * Almacena en la Hashtable el identificador del tipo de EJG del dictamen del EJG 
	 * 
	 * @param valor Identificador del tipo de EJG del dictamen del EJG. De tipo "String". 
	 * @return void 
	 */
	public void setIdTipoEJG				(String idTipoEJG)				{ this.datos.put(ScsEstadoEJGBean.C_IDTIPOEJG, idTipoEJG);				}
	/*
	 * Metodos GET*/
	
	/**
	 * Recupera de la Hashtable el anho de la EJG
	 * 
	 * @return Anho de la EJG
	 */
	public String getAnio 					()		{ return this.datos.get(ScsEstadoEJGBean.C_ANIO).toString();}
	public String getAutomatico 		    ()		{ return this.datos.get(ScsEstadoEJGBean.C_AUTOMATICO).toString();}
	/**
	 * Recupera de la Hashtable el numero de la EJG
	 * 
	 * @return Numero de la EJG
	 */
	public String getNumero 				()		{ return this.datos.get(ScsEstadoEJGBean.C_NUMERO).toString();}
	/**
	 * Recupera de la Hashtable la fecha de inicio del estado
	 * 
	 * @return Fecha de inicio del estado
	 */
	public String getFechaInicio			()		{ return this.datos.get(ScsEstadoEJGBean.C_FECHAINICIO).toString();}
		
	/**
	 * Recupera de la Hashtable el identificador de la institucion de la EJG
	 * 
	 * @return Identificador de la institucion  de la EJG
	 */
	public String getIdInstitucion			()		{ return this.datos.get(ScsEstadoEJGBean.C_IDINSTITUCION).toString();}
	/**
	 * Recupera de la Hashtable el identificador del tipo  de la EJG
	 * 
	 * @return Identificador del tipo de la EJG
	 */
	public String getIdTipoEJG				()		{ return this.datos.get(ScsEstadoEJGBean.C_IDTIPOEJG).toString();	}
	
	/**
	 * Almacena en la Hashtable el identificador del estado
 
	 * @return String 
	 */
	public String getIdEstadoEJG			()		{ return this.datos.get(ScsEstadoEJGBean.C_IDESTADOEJG).toString();	}
	
	/**
	 * Almacena en la Hashtable el identificador del estado
 
	 * @return String 
	 */
	public String getIdEstadoPorEJG			()		{ return this.datos.get(ScsEstadoEJGBean.C_IDESTADOPOREJG).toString();	}
	
	/**
	 * Almacena en la Hashtable el identificador del estado
 
	 * @return String 
	 */
	public String getObservaciones			()		{ return this.datos.get(ScsEstadoEJGBean.C_OBSERVACIONES).toString();	}
	public String getVerHistorico() {
		return verHistorico;
	}
	public void setVerHistorico(String verHistorico) {
		this.verHistorico = verHistorico;
	}
	public EstadoEjgVo getEstadoEjgVo(DefinirEstadosEJGForm estadosEJGForm) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		EstadoEjgVo estadoEjgVo = new EstadoEjgVo();
		estadoEjgVo.setIdinstitucion(estadosEJGForm.getIdInstitucion()!=null && !estadosEJGForm.getIdInstitucion().equalsIgnoreCase("")?Short.valueOf(estadosEJGForm.getIdInstitucion()):null);
		estadoEjgVo.setAnio(estadosEJGForm.getAnio()!=null&& !estadosEJGForm.getAnio().equalsIgnoreCase("")?Short.valueOf(estadosEJGForm.getAnio()):null);
		estadoEjgVo.setIdtipoejg(estadosEJGForm.getIdTipoEJG()!=null&& !estadosEJGForm.getIdTipoEJG().equalsIgnoreCase("")?Short.valueOf(estadosEJGForm.getIdTipoEJG()):null);
		estadoEjgVo.setNumero(estadosEJGForm.getNumero()!=null&& !estadosEJGForm.getNumero().equalsIgnoreCase("")?Long.valueOf(estadosEJGForm.getNumero()):null);
		estadoEjgVo.setIdestadoejg(estadosEJGForm.getIdEstadoEJG()!=null&& !estadosEJGForm.getIdEstadoEJG().equalsIgnoreCase("")?Short.valueOf(estadosEJGForm.getIdEstadoEJG()):null);
		estadoEjgVo.setIdestadoporejg(estadosEJGForm.getIdEstadoPorEJG()!=null&& !estadosEJGForm.getIdEstadoPorEJG().equalsIgnoreCase("")?Long.valueOf(estadosEJGForm.getIdEstadoPorEJG()):null);
		
		estadoEjgVo.setAutomatico(estadosEJGForm.getAutomatico());
		estadoEjgVo.setObservaciones(estadosEJGForm.getObservaciones());
		try {
			estadoEjgVo.setFechainicio(estadosEJGForm.getFechaInicio()!=null?simpleDateFormat.parse(estadosEJGForm.getFechaInicio()):null);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return estadoEjgVo;
		
	}
	
}