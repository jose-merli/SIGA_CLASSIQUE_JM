/*
 * Created on Mar 9, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.gratuita.util.calendarioSJCS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.GenClientesTemporalAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsCabeceraGuardiasAdm;
import com.siga.beans.ScsCabeceraGuardiasBean;
import com.siga.beans.ScsCalendarioGuardiasAdm;
import com.siga.beans.ScsCalendarioGuardiasBean;
import com.siga.beans.ScsCalendarioLaboralAdm;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.beans.ScsGuardiasColegiadoBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsHitoFacturableGuardiaAdm;
import com.siga.beans.ScsHitoFacturableGuardiaBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsSaltosCompensacionesBean;
import com.siga.general.SIGAException;
import com.siga.gratuita.util.calendarioSJCS.Entity.PeriodoEfectivo;

/**
 * @author A203486
 *
 * Esta Clase representa un calendario automatico con todos los datos propios del calendario.
 * Desde esta clase se puede llamar al metodo "calcularMatrizPeriodosDiasGuardia()" para calcular el periodo <br>
 * de guardias.
 * 
 * En su constructor inicializaremos todos los datos de la guardia y el calendario, incluyendo un vector de festivos <br>
 * para esta guardia.
 * 
 * Mantenemos dos arrays donde almacenaremos en su momento:
 * - arrayPeriodosDiasGuardiaSJCS: ArrayList de periodos de dias de guardias (cada periodo es una lista de fechas <br>
 * de guardias posibles en formato corto).
 * 
 * - arrayPeriodosLetradosSJCS: ArrayList con los periodos, cada uno con sus letrados para hacer las guardias SJCS.	
 * 
 * Tenemos dos metodos principales:
 * - calcularMatrizPeriodosDiasGuardia(): inserta en el arraylist arrayPeriodosDiasGuardiaSJCS los periodos de dias de guardia calculados por el CGAE
 * 
 * - calcularMatrizLetradosGuardia(): rellena el arraylist de la clase "arrayLetradosGuardiaSJCS" con los periodos rellenados cada uno con su letrado y el arralist de fechas que comprende el periodo.
 * 	 Nota: previamente se ha calculado mediante el metodo "calcularMatrizPeriodosDiasGuardia()" el arraylist de periodos de dias factibles con las caracteristicas de la guardia.
 * 	 Devuelve un int con el nivel de error:
 * 	 	- 0: Todo Correcto
 * 	 	- 1: NO Hay Suficientes Letrados
 * 	 	- 2: Hay Incompatibilidad de Guardias	
 * 	 	- 3: NO hay Separacion suficiente
 * 		- 4: Error al provocarse una excepcion en el desarrollo del metodo.
 * 
 * El resto de metodos son get/set de atributos y funcionalidad varia para el desarrollo del algoritmo de generacion del calendario.
 */
public class CalendarioSJCS {

	
	//Claves para identificar la guardia de un calendario:
	private Integer idInstitucion;
	private Integer idTurno;
	private Integer idGuardia;
	private Integer idCalendarioGuardias;
	
	//UserBean
	private UsrBean usrBean;

	//Guardia del Turno:
	private ScsGuardiasTurnoBean beanGuardiasTurno = null;
	
    //Datos para usar posteriormente propios de SJCS:
	//ArrayList de periodos con las fechas en formato corto como String:
	private ArrayList arrayPeriodosDiasGuardiaSJCS;	
	//ArrayList con los periodos, cada uno con sus letrados para hacer las guardias SJCS.	
	private ArrayList arrayPeriodosLetradosSJCS;
 
	//Datos a calcular previamente:
	private Vector vDiasFestivos;
	private String fechaInicio = null;
	private String fechaFin = null;	


	public CalendarioSJCS(Integer idInstitucion, Integer idTurno, Integer idGuardia, Integer idCalendarioGuardias, UsrBean usr){
		//Claves:
		this.idInstitucion = idInstitucion;
		this.idTurno = idTurno;
		this.idGuardia = idGuardia;
		this.idCalendarioGuardias = idCalendarioGuardias;

		//UsrBean:
		this.usrBean = usr;

		//Obtengo el resto de datos:
		//1. GUARDIATURNO: calculo todos los datos de la guardia.
		ScsGuardiasTurnoAdm admGuardiasTurno = new ScsGuardiasTurnoAdm(this.usrBean);
		String where = " WHERE "+ScsGuardiasTurnoBean.C_IDINSTITUCION+"="+idInstitucion+
					   " AND "+ScsGuardiasTurnoBean.C_IDTURNO+"="+idTurno+
					   " AND "+ScsGuardiasTurnoBean.C_IDGUARDIA+"="+idGuardia;
		Vector vGuardiasTurno;
		try {
			vGuardiasTurno = admGuardiasTurno.select(where);
			if (!vGuardiasTurno.isEmpty())
				this.beanGuardiasTurno = (ScsGuardiasTurnoBean)vGuardiasTurno.firstElement();
		} catch (Exception e){
			this.beanGuardiasTurno = null;
		}

		//2. CALENDARIO: calculo la fecha de inicio y fin.
		ScsCalendarioGuardiasAdm admCalendarioGuardias = new ScsCalendarioGuardiasAdm(this.usrBean);
		where = " WHERE "+ScsCalendarioGuardiasBean.C_IDINSTITUCION+"="+idInstitucion+
		 	    " AND "+ScsCalendarioGuardiasBean.C_IDTURNO+"="+idTurno+
				" AND "+ScsCalendarioGuardiasBean.C_IDGUARDIA+"="+idGuardia+
				" AND "+ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+idCalendarioGuardias;
		Vector vCalendarioGuardias;
		try {
			vCalendarioGuardias = admCalendarioGuardias.select(where);
			if (!vCalendarioGuardias.isEmpty()) {
				ScsCalendarioGuardiasBean beanCalendarioGuardias = (ScsCalendarioGuardiasBean)vCalendarioGuardias.firstElement();
				this.fechaInicio = beanCalendarioGuardias.getFechaInicio();
				this.fechaFin = beanCalendarioGuardias.getFechaFin();
			}
		} catch (Exception e){
			this.fechaInicio = "";
			this.fechaFin = "";
		}

		//3. CALENDARIO FESTIVOS:
		ScsCalendarioLaboralAdm admCalendarioLaboral = new ScsCalendarioLaboralAdm(this.usrBean);
		this.vDiasFestivos = (Vector)admCalendarioLaboral.obtenerFestivosTurno(idInstitucion, idTurno, this.fechaInicio, this.fechaFin).clone();
		
		//4. INICIALIZO LOS 2 ARRAYS:
		this.arrayPeriodosDiasGuardiaSJCS = new ArrayList();
		this.arrayPeriodosLetradosSJCS = new ArrayList();
	}
	
	
	

	




	/**
	 * @return Returns the arrayPeriodosDiasGuardiaSJCS.
	 */
	public ArrayList getArrayPeriodosDiasGuardiaSJCS() {
		return arrayPeriodosDiasGuardiaSJCS;
	}
	/**
	 * @param arrayPeriodosDiasGuardiaSJCS The arrayPeriodosDiasGuardiaSJCS to set.
	 */
	public void setArrayPeriodosDiasGuardiaSJCS(
			ArrayList arrayPeriodosDiasGuardiaSJCS) {
		this.arrayPeriodosDiasGuardiaSJCS = arrayPeriodosDiasGuardiaSJCS;
	}
	/**
	 * @return Returns the arrayPeriodosLetradosSJCS.
	 */
	public ArrayList getArrayPeriodosLetradosSJCS() {
		return arrayPeriodosLetradosSJCS;
	}
	/**
	 * @param arrayPeriodosLetradosSJCS The arrayPeriodosLetradosSJCS to set.
	 */
	public void setArrayPeriodosLetradosSJCS(ArrayList arrayPeriodosLetradosSJCS) {
		this.arrayPeriodosLetradosSJCS = arrayPeriodosLetradosSJCS;
	}
	/**
	 * @return Returns the beanGuardiasTurno.
	 */
	public ScsGuardiasTurnoBean getBeanGuardiasTurno() {
		return beanGuardiasTurno;
	}
	/**
	 * @param beanGuardiasTurno The beanGuardiasTurno to set.
	 */
	public void setBeanGuardiasTurno(ScsGuardiasTurnoBean beanGuardiasTurno) {
		this.beanGuardiasTurno = beanGuardiasTurno;
	}
	/**
	 * @return Returns the fechaFin.
	 */
	public String getFechaFin() {
		return fechaFin;
	}
	/**
	 * @param fechaFin The fechaFin to set.
	 */
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	/**
	 * @return Returns the fechaInicio.
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}
	/**
	 * @param fechaInicio The fechaInicio to set.
	 */
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	/**
	 * @return Returns the idCalendarioGuardias.
	 */
	public Integer getIdCalendarioGuardias() {
		return idCalendarioGuardias;
	}
	/**
	 * @param idCalendarioGuardias The idCalendarioGuardias to set.
	 */
	public void setIdCalendarioGuardias(Integer idCalendarioGuardias) {
		this.idCalendarioGuardias = idCalendarioGuardias;
	}
	/**
	 * @return Returns the idGuardia.
	 */
	public Integer getIdGuardia() {
		return idGuardia;
	}
	/**
	 * @param idGuardia The idGuardia to set.
	 */
	public void setIdGuardia(Integer idGuardia) {
		this.idGuardia = idGuardia;
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the idTurno.
	 */
	public Integer getIdTurno() {
		return idTurno;
	}
	/**
	 * @param idTurno The idTurno to set.
	 */
	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
	/**
	 * @return Returns the usrBean.
	 */
	public UsrBean getUsrBean() {
		return usrBean;
	}
	/**
	 * @param usrBean The usrBean to set.
	 */
	public void setUsrBean(UsrBean usrBean) {
		this.usrBean = usrBean;
	}
	/**
	 * @return Returns the vDiasFestivos.
	 */
	public Vector getVDiasFestivos() {
		return vDiasFestivos;
	}
	/**
	 * @param diasFestivos The vDiasFestivos to set.
	 */
	public void setVDiasFestivos(Vector diasFestivos) {
		vDiasFestivos = diasFestivos;
	}

	
	
	//Pinta las fechas de los periodos guardados en el arraylist SJCS.
	public void pintarCalendarioSJCS() throws ClsExceptions {
	
		try {
			ClsLogging.writeFileLog("*** INICIO CALENDARIO: ---------------------------------",7);
			ClsLogging.writeFileLog("--> IDINSTITUCION="+this.idInstitucion,7);
			ClsLogging.writeFileLog("--> IDGUARDIA="+this.idGuardia,7);
			ClsLogging.writeFileLog("--> IDCALENDARIO="+this.idCalendarioGuardias,7);
			
			//Recorro el arraylist de los periodos efectivos
			Iterator it = this.arrayPeriodosDiasGuardiaSJCS.iterator();
			int i=1;
			while (it.hasNext()) {
				ClsLogging.writeFileLog("> Periodo "+i+":",7);				
				ArrayList arrayDias = new ArrayList();
				arrayDias = (ArrayList)it.next();
				Iterator it2 = arrayDias.iterator();
				int j=1;
				while (it2.hasNext()) {
					ClsLogging.writeFileLog("--> Dia de Guardia "+j+" (Periodo "+i+") / Fecha=<"+it2.next()+">",7);
					j++;
				}
				i++;
			}
			ClsLogging.writeFileLog("*** FIN CALENDARIO: --------------------------",7);
		} catch (Exception e){
			throw new ClsExceptions(e, "ERROR al pintar los datos del Calendario SJCS");			
		}		
	}
	
	//Chequea que el letrado obtenido del PL de BBDD tenga un Salto
	private boolean esSalto (LetradoGuardia letradoSeleccionado) throws ClsExceptions {
	    RowsContainer rc = new RowsContainer();
	    
		boolean haySalto = false;
//		 RGG 05/09/2008 	
//		if (letradoSeleccionado!=null && letradoSeleccionado.getSaltoCompensacion()!=null && letradoSeleccionado.getSaltoCompensacion().equalsIgnoreCase("S"))
//			haySalto = true;
		// voy a comprobar si existe un salto en base de datos
		try {

		    String sql = " select * from scs_saltoscompensaciones s "+
						 " where s.idinstitucion="+letradoSeleccionado.getIdInstitucion()+
						 " and   s.idpersona="+letradoSeleccionado.getIdPersona()+
						 " and   s.idturno="+letradoSeleccionado.getIdTurno()+
						 " and   s.idguardia="+letradoSeleccionado.getIdGuardia()+
						 " and   s.saltoocompensacion='S' "+
						 " and   s.fechacumplimiento is null ";
	
			ScsSaltosCompensacionesAdm adm = new ScsSaltosCompensacionesAdm(this.usrBean);
			rc = adm.find(sql);
			if (rc!=null && rc.size()>0){
				haySalto=true;
			}
		} catch (Exception e) {
		    throw new ClsExceptions(e,"Error al comporbar si hay salto en BD.");
		}
		
		return haySalto;
	}
	
	
	
	private LetradoGuardia dameCompensacion () throws ClsExceptions {
	    RowsContainer rc = new RowsContainer();
	    LetradoGuardia letradoSeleccionado = null;
	    
		try {
			//obtiene las compesaciones de letrados que no estén de baja en la guardia
		    String sql = " select * from scs_saltoscompensaciones s, scs_inscripcionguardia g "+
						 " where s.idinstitucion="+this.getIdInstitucion()+
						 " and   s.idturno="+this.getIdTurno()+
						 " and   s.idguardia="+this.getIdGuardia()+
						 " and   s.saltoocompensacion='C' "+
						 " and   s.fechacumplimiento is null "+
						 " and s.idinstitucion = g.idinstitucion "+
					     " and s.idturno = g.idturno "+
					     " and s.idguardia = g.idguardia "+
					     " and s.idpersona = g.idpersona "+
					     " and g.fechasuscripcion = "+
					     "     (select max(i.fechasuscripcion) "+
					     "        from scs_inscripcionguardia i "+
					     "       where i.idinstitucion = g.idinstitucion "+
					     "         and i.idturno = g.idturno "+
					     "         and i.idguardia = g.idguardia "+
					     "         and i.idpersona = g.idpersona "+
					     "      ) "+
					     " and g.fechabaja is null";
	
			ScsSaltosCompensacionesAdm adm = new ScsSaltosCompensacionesAdm(this.usrBean);
			rc = adm.find(sql);
			if (rc!=null && rc.size()>0){
			    letradoSeleccionado = new LetradoGuardia();
			    letradoSeleccionado.setIdInstitucion(this.getIdInstitucion());
			    letradoSeleccionado.setIdTurno(this.getIdTurno());
			    letradoSeleccionado.setIdGuardia(this.getIdGuardia());
			    letradoSeleccionado.setSaltoCompensacion("C");
			    
			    Row fila = (Row) rc.get(0);
				Hashtable hFila = fila.getRow();
				letradoSeleccionado.setIdPersona(new Long((String)hFila.get(ScsSaltosCompensacionesBean.C_IDPERSONA)));

			    //letradoSeleccionado
			}
		} catch (Exception e) {
		    throw new ClsExceptions(e,"Error al obtener compensación en BD.");
		}
		
		return letradoSeleccionado;
	}

	//Chequea que el letrado obtenido del PL de BBDD tenga una Compensacion
	private boolean esCompensacion (LetradoGuardia letradoSeleccionado) {
		boolean haySalto = false;
		
		if (letradoSeleccionado!=null && letradoSeleccionado.getSaltoCompensacion()!=null && letradoSeleccionado.getSaltoCompensacion().equalsIgnoreCase("C"))
			haySalto = true;
		return haySalto;
	}
	
	//Modifica el letrado almacenado en una hash en una determinada posicion de un vector de letrados obtenidos de un PL de BBDD:
	//Nota: primero consulta su indice, modifica el letrado, lo borra del vector e inserta el letrado modificado en la posicion anterior.
	private void marcarNoSalto(LetradoGuardia letradoSeleccionado, ArrayList arrayListaLetrados) throws ClsExceptions {
		try {
			if (letradoSeleccionado!=null && !arrayListaLetrados.isEmpty()) {
				int indice = arrayListaLetrados.indexOf(letradoSeleccionado);
				letradoSeleccionado.setSaltoCompensacion("N");
				arrayListaLetrados.remove(indice);
				arrayListaLetrados.add(indice,letradoSeleccionado);
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Excepcion al intentar marcar a un letrado como Salto en el array de letrados.");
		}
	}
	
	// Actualiza en BBDD el Salto o la Compensacion para este letrado.
	private void marcarSaltoCompensacionBBDD(LetradoGuardia letradoSeleccionado, String saltoCompensacion) throws ClsExceptions {
		try {
			if (saltoCompensacion.equalsIgnoreCase("S") || saltoCompensacion.equalsIgnoreCase("C")) {
				String sql = " UPDATE "+ScsSaltosCompensacionesBean.T_NOMBRETABLA+
							 " SET "+ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+" = SYSDATE,"+
							 		 ScsSaltosCompensacionesBean.C_USUMODIFICACION+" = "+this.usrBean.getUserName()+","+
							 		 ScsSaltosCompensacionesBean.C_FECHAMODIFICACION+" = SYSDATE "+","+
							 		 ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIAS+" = "+this.idCalendarioGuardias+
							 " WHERE "+ScsSaltosCompensacionesBean.C_IDINSTITUCION+" = "+letradoSeleccionado.getIdInstitucion()+
							 " AND "+ScsSaltosCompensacionesBean.C_IDTURNO+" = "+letradoSeleccionado.getIdTurno()+
							 " AND "+ScsSaltosCompensacionesBean.C_IDGUARDIA+" = "+letradoSeleccionado.getIdGuardia()+
							 " AND "+ScsSaltosCompensacionesBean.C_IDPERSONA+" = "+letradoSeleccionado.getIdPersona()+
							 " AND "+ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION+" = '"+saltoCompensacion+"'"+
							 " AND "+ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+" IS NULL"+
							 " AND ROWNUM < 2";
							
				ClsMngBBDD.executeUpdate(sql);
			} else 
				throw new ClsExceptions(null, "Excepcion en marcarSaltoCompensacionBBDD.");	
		} catch (Exception e) {
			throw new ClsExceptions(e, "Excepcion en marcarSaltoCompensacionBBDD.");
		}
	}
	
	//Devuelve true si NO hay incompatibilidad de guardias
	private boolean hayIncompatibilidadGuardias(LetradoGuardia letrado, ArrayList periodoDiasGuardia) throws ClsExceptions {
		boolean salida = false;
		
		try {
			ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.usrBean);
			
			String idInstitucion = letrado.getIdInstitucion().toString();
			String idGuardia = letrado.getIdGuardia().toString();
			String idTurno = letrado.getIdTurno().toString();
			String idPersona = letrado.getIdPersona().toString();
			
			salida = !admGuardiasColegiado.validarIncompatibilidadGuardia(idInstitucion, idTurno, idGuardia, periodoDiasGuardia, idPersona);
		} catch (Exception e) {
			throw new ClsExceptions(e, "Excepcion en noHayIncompatibilidadGuardias.");
		}
		return salida;
	}

	//Devuelve true si hay suficiente separacion de dias de guardia
	private boolean haySeparacionDias(LetradoGuardia letrado, ArrayList periodoDiasGuardia) throws ClsExceptions {
		boolean salida = false;
		
		try {
			ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.usrBean);
			
			Hashtable miHash = new Hashtable();
			miHash.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION, letrado.getIdInstitucion().toString());
			miHash.put(ScsGuardiasColegiadoBean.C_IDGUARDIA, letrado.getIdGuardia().toString());
			miHash.put(ScsGuardiasColegiadoBean.C_IDTURNO, letrado.getIdTurno().toString());
			miHash.put(ScsGuardiasColegiadoBean.C_FECHAINICIO, (String)periodoDiasGuardia.get(0));
			miHash.put(ScsGuardiasColegiadoBean.C_FECHAFIN, (String)periodoDiasGuardia.get(periodoDiasGuardia.size()-1));
			miHash.put(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS, this.idCalendarioGuardias.toString());
			miHash.put(ScsGuardiasColegiadoBean.C_IDPERSONA, letrado.getIdPersona().toString());
			
			salida = admGuardiasColegiado.validarSeparacionGuardias(miHash);
		} catch (Exception e) {
			throw new ClsExceptions(e, "Excepcion en haySeparacionDias.");
		}
		return salida;
	}
	
	//Actualizo el ultimo de la guardia por el puntero de letrados:
	private void actualizarUltimoLetradoGuardiaBBDD(LetradoGuardia letradoSeleccionado) throws ClsExceptions {
		try {
			ScsGuardiasTurnoAdm admGuardiasTurno = new ScsGuardiasTurnoAdm(this.usrBean);
			
			String[] claves = {ScsGuardiasTurnoBean.C_IDINSTITUCION, ScsGuardiasTurnoBean.C_IDTURNO, 
							   ScsGuardiasTurnoBean.C_IDGUARDIA};
			String[] campos = {ScsGuardiasTurnoBean.C_IDPERSONA_ULTIMO, ScsGuardiasTurnoBean.C_USUMODIFICACION,
							   ScsGuardiasTurnoBean.C_FECHAMODIFICACION};
			
			Hashtable hash = new Hashtable();
			hash.put(ScsGuardiasTurnoBean.C_IDPERSONA_ULTIMO, letradoSeleccionado.getIdPersona());
			hash.put(ScsGuardiasTurnoBean.C_USUMODIFICACION, this.usrBean.getUserName());
			hash.put(ScsGuardiasTurnoBean.C_FECHAMODIFICACION, "SYSDATE");
			hash.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, letradoSeleccionado.getIdInstitucion());
			hash.put(ScsGuardiasTurnoBean.C_IDTURNO, letradoSeleccionado.getIdTurno());
			hash.put(ScsGuardiasTurnoBean.C_IDGUARDIA, letradoSeleccionado.getIdGuardia());
			
			admGuardiasTurno.updateDirect(hash, claves, campos);
		} catch (Exception e) {
			throw new ClsExceptions(e, "Excepcion en actualizarUltimoLetradoGuardiaBBDD.");
		}
	}

	//Almacena para un letrado la guardia del mismo con los registros correspondientes a sus dias de guardia.
	public void almacenarAsignacionGuardiaLetrado(LetradoGuardia letrado, ArrayList periodoDiasGuardia, List lDiasASeparar) throws ClsExceptions {
		ArrayList arrayLetrados = new ArrayList();
		//Anhado el letrado
		arrayLetrados.add(letrado);
		almacenarAsignacionGuardia(arrayLetrados, periodoDiasGuardia, lDiasASeparar,UtilidadesString.getMensajeIdioma(this.getUsrBean(),"gratuita.literal.letrado.refuerzo"));
	}
	
	//Almacena para una lista de letrados las guardias con los registros correspondientes a sus dias de guardia.
	private void almacenarAsignacionGuardia(ArrayList arrayLetrados, ArrayList periodoDiasGuardia,List lDiasASeparar, String mensaje) throws ClsExceptions {
		Iterator iter;
		Iterator iterLetrados;
		String fechaInicioPeriodo=null, fechaFinPeriodo=null, fechaPeriodo=null;
		ScsCabeceraGuardiasBean beanCabeceraGuardias;
		ScsGuardiasColegiadoBean beanGuardiasColegiado;
		LetradoGuardia letrado;

		try {

			List alPeriodosSinAgrupar = getPeriodos(periodoDiasGuardia,lDiasASeparar);
			for (int j = 0; j < alPeriodosSinAgrupar.size(); j++) {
				ArrayList alDiasPeriodo = (ArrayList) alPeriodosSinAgrupar.get(j);

				ScsCabeceraGuardiasAdm admCabeceraGuardias = new ScsCabeceraGuardiasAdm(this.usrBean);
				ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.usrBean);

				fechaInicioPeriodo = (String)alDiasPeriodo.get(0);
				fechaFinPeriodo = (String)alDiasPeriodo.get(alDiasPeriodo.size()-1);

				iterLetrados = arrayLetrados.iterator();
				while (iterLetrados.hasNext()) {
					letrado = (LetradoGuardia)iterLetrados.next();

					//Paso1: inserto un registro cada guardia:
					beanCabeceraGuardias = new ScsCabeceraGuardiasBean();
					beanCabeceraGuardias.setIdInstitucion(letrado.getIdInstitucion());
					beanCabeceraGuardias.setIdTurno(letrado.getIdTurno());
					beanCabeceraGuardias.setIdGuardia(letrado.getIdGuardia());
					beanCabeceraGuardias.setIdCalendario(this.idCalendarioGuardias);
					beanCabeceraGuardias.setIdPersona(letrado.getIdPersona());
					beanCabeceraGuardias.setFechaInicio(GstDate.getApplicationFormatDate(this.usrBean.getLanguage(),fechaInicioPeriodo));
					beanCabeceraGuardias.setFechaFin(GstDate.getApplicationFormatDate(this.usrBean.getLanguage(),fechaFinPeriodo));
					beanCabeceraGuardias.setFechaMod("SYSDATE");
					beanCabeceraGuardias.setComenSustitucion(mensaje);
					beanCabeceraGuardias.setSustituto("0");
					beanCabeceraGuardias.setUsuMod(new Integer(usrBean.getUserName()));
					beanCabeceraGuardias.setFacturado("");
					beanCabeceraGuardias.setPagado("");							


					// RGG cambio para cabeceras de guardia validadas
					GenParametrosAdm admPar = new GenParametrosAdm(this.usrBean);
					String valorValidar = admPar.getValor(this.usrBean.getLocation(),"SCS","VALIDAR_VOLANTE","N"); 
					if (valorValidar.equals("N")) {
						// directamente quedan validados
						beanCabeceraGuardias.setValidado(ClsConstants.DB_TRUE);
					} else {
						// quedan pendientes de validacion
						beanCabeceraGuardias.setValidado(ClsConstants.DB_FALSE);
					}


					admCabeceraGuardias.insert(beanCabeceraGuardias);


					//Paso2: inserto un registro por dia de guardia en cada guardia:
					iter = alDiasPeriodo.iterator();
					while (iter.hasNext()) {
						fechaPeriodo = (String)iter.next();

						beanGuardiasColegiado = new ScsGuardiasColegiadoBean();
						beanGuardiasColegiado.setIdInstitucion(letrado.getIdInstitucion());
						beanGuardiasColegiado.setIdTurno(letrado.getIdTurno());
						beanGuardiasColegiado.setIdGuardia(letrado.getIdGuardia());
						beanGuardiasColegiado.setIdCalendarioGuardias(this.idCalendarioGuardias);
						beanGuardiasColegiado.setIdPersona(letrado.getIdPersona());
						beanGuardiasColegiado.setFechaInicio(GstDate.getApplicationFormatDate(this.usrBean.getLanguage(),fechaInicioPeriodo));
						beanGuardiasColegiado.setFechaFin(GstDate.getApplicationFormatDate(this.usrBean.getLanguage(),fechaPeriodo));
						beanGuardiasColegiado.setDiasGuardia(new Long(this.beanGuardiasTurno.getDiasGuardia().longValue()));
						beanGuardiasColegiado.setDiasACobrar(new Long(this.beanGuardiasTurno.getDiasPagados().longValue()));
						beanGuardiasColegiado.setFechaMod("SYSDATE");			 		
						beanGuardiasColegiado.setUsuMod(new Integer(usrBean.getUserName()));
						beanGuardiasColegiado.setReserva("N");
						beanGuardiasColegiado.setObservaciones("-");
						beanGuardiasColegiado.setFacturado("N");
						beanGuardiasColegiado.setPagado("N");
						beanGuardiasColegiado.setIdFacturacion(null);					

						admGuardiasColegiado.insert(beanGuardiasColegiado);
					}
				}
			}
		} catch (Exception e) {		
			e.printStackTrace();
			throw new ClsExceptions(e, "Excepcion en almacenarAsignacionGuardia.");
		}
	}
	
	//Inserta compensaciones haciendo un insert por cada letrado de la lista de pendientes con compensacion:
	//Nota: los saltos ya los he ido actualizando a lo largo del algoritmo.
	private void insertarCompensacionesBBDD(ArrayList arrayListaLetradosPendientes) throws ClsExceptions {
		Iterator iter;
		String sql = "";

		try {
			iter = arrayListaLetradosPendientes.iterator();

			while (iter.hasNext()) {
				LetradoGuardia letrado = (LetradoGuardia)iter.next();
				//Insert para cada letrado:
				sql = " INSERT INTO "+ScsSaltosCompensacionesBean.T_NOMBRETABLA+
						   " ( "+ScsSaltosCompensacionesBean.C_IDINSTITUCION+" , "+
						   		 ScsSaltosCompensacionesBean.C_IDTURNO+" , "+
						   		 ScsSaltosCompensacionesBean.C_IDSALTOSTURNO+" , "+
						   		 ScsSaltosCompensacionesBean.C_IDPERSONA+" , "+
						   		 ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION+" , "+
						   		 ScsSaltosCompensacionesBean.C_FECHA+" , "+
						   		 ScsSaltosCompensacionesBean.C_FECHAMODIFICACION+" , "+
						   		 ScsSaltosCompensacionesBean.C_USUMODIFICACION+" , "+
						   		 ScsSaltosCompensacionesBean.C_IDGUARDIA+" , "+
						   		 ScsSaltosCompensacionesBean.C_MOTIVOS+" , "+
						   		 ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO+" , "+
						   		 ScsSaltosCompensacionesBean.C_IDCALENDARIOGUARDIASCREACION+
						 " ) VALUES ("+
						 		letrado.getIdInstitucion()+" , "+
						 		letrado.getIdTurno()+" , "+
						 		" (SELECT NVL(MAX("+ScsSaltosCompensacionesBean.C_IDSALTOSTURNO+") + 1, 1) FROM "+ScsSaltosCompensacionesBean.T_NOMBRETABLA+
								" WHERE "+ScsSaltosCompensacionesBean.C_IDINSTITUCION+" = "+letrado.getIdInstitucion()+
								" AND "+ScsSaltosCompensacionesBean.C_IDTURNO+" = "+letrado.getIdTurno()+")"+
								" ,"+
						 		letrado.getIdPersona()+" , "+
						 		" 'C' , "+
						 		" SYSDATE , "+
						 		" SYSDATE , "+
						 		usrBean.getUserName()+" , "+
						 		letrado.getIdGuardia()+" , "+
						 		" 'No poder asignar automáticamente en la generación de calendario' , "+
								" NULL , "+
								this.idCalendarioGuardias+
						" ) ";
				
				ClsMngBBDD.executeUpdate(sql);
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Excepcion en insertarCompensacionesBBDD.");
		}
	}
	
	
	
	
	
	/**
	 * Si el letrado esta de baja seteamos la baja temporarl en el objeto LetradoGuardia, para luego insertar un salto
	 * @param periodoDiasGuardia
	 * @param letradoGuardia
	 * @return
	 */
	
	private boolean isLetradoBajaTemporal(ArrayList diasGuardia,LetradoGuardia letradoGuardia){
		boolean isLetradoBaja = false;
		CenBajasTemporalesBean bajaTemporal= null;
		for (int j = 0; j < diasGuardia.size(); j++) {
			String fechaPeriodo = (String)diasGuardia.get(j);
		    if( letradoGuardia.getBajasTemporales().containsKey(fechaPeriodo)){
		    	bajaTemporal = letradoGuardia.getBajasTemporales().get(fechaPeriodo);
		    	letradoGuardia.setBajaTemporal(bajaTemporal);
		    	isLetradoBaja= true;
		    	break;
		    }
		}
		return isLetradoBaja;
	}
	
	private LetradoGuardia getSiguienteLetradoCompensado(List<LetradoGuardia> alCompensaciones,ArrayList diasGuardia,HashMap<Long, List<LetradoGuardia>> hmPersonasConSaltos) throws ClsExceptions{
		LetradoGuardia letradoGuardia = null;
		//Vamos a recorrer las compensaciones
		if(alCompensaciones!=null && alCompensaciones.size()>0){
			for(LetradoGuardia auxLetradoSeleccionado:alCompensaciones){
				if(auxLetradoSeleccionado.getBajasTemporales()==null){
					//si esta de vacaciones meto salto y que continue
					CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(this.usrBean);
					Map<String,CenBajasTemporalesBean> mBajasTemporales =  bajasTemporalescioneAdm.getDiasBajaTemporal(auxLetradoSeleccionado.getIdPersona(), auxLetradoSeleccionado.getIdInstitucion());
					auxLetradoSeleccionado.setBajasTemporales(mBajasTemporales);
				}
				
				//vale
				if(isLetradoValido(auxLetradoSeleccionado, diasGuardia, hmPersonasConSaltos, true)){
//					Se quita ya que se quito la compensacion en isLetradoValido
					alCompensaciones.remove(auxLetradoSeleccionado);
					letradoGuardia = auxLetradoSeleccionado;
					break;
				}
			}
		}
		return letradoGuardia;
	}
	private boolean isIncompatible(LetradoGuardia letradoGuardia,ArrayList diasGuardia) throws ClsExceptions{
		
		
		if (hayIncompatibilidadGuardias(letradoGuardia, diasGuardia))
			return true;
		if (!haySeparacionDias(letradoGuardia, diasGuardia))
			return  true;
		
		return false;
		
	}
	private boolean isLetradoValido(LetradoGuardia letradoGuardia,ArrayList diasGuardia,HashMap<Long, List<LetradoGuardia>> hmPersonasConSaltos, boolean isCompensacion) throws ClsExceptions{
		if(isLetradoBajaTemporal(diasGuardia, letradoGuardia)){
			ScsSaltosCompensacionesBean salto = new ScsSaltosCompensacionesBean(
					new Integer(idInstitucion),new Integer(idTurno),letradoGuardia.getIdPersona(),ClsConstants.SALTOS,"sysdate");
			ScsSaltosCompensacionesAdm scsSaltosCompensacionesAdm = new ScsSaltosCompensacionesAdm(this.usrBean);
			salto.setIdGuardia(idGuardia);
			salto.setIdCalendarioGuardiasCreacion(idCalendarioGuardias);
			//bajaTemporal.setDescripcion(bajaTemporal.getDescripcion()+" al crear designa para el "+fecha+" ");
			scsSaltosCompensacionesAdm.insertarSaltoPorBajaTemporal(letradoGuardia.getBajaTemporal(),salto);
			return false;
			
		}

		if(isIncompatible(letradoGuardia, diasGuardia)){
//			TODO aaaaaaaaa ATENCION!!! HAY QUWE INSERTAR COMPENSACIONES
			return false;
			
		}
		if(isSaltoLetrado(letradoGuardia, hmPersonasConSaltos)){
			List<LetradoGuardia> alSaltos = hmPersonasConSaltos.get(letradoGuardia.getIdPersona());
			if(alSaltos!=null && alSaltos.size()>0){
				this.marcarSaltoCompensacionBBDD(letradoGuardia, "S");
				alSaltos.remove(0);
				if(alSaltos.size()==0)
					hmPersonasConSaltos.remove(letradoGuardia.getIdPersona());
		
			}
			return false;
		}
		if(isCompensacion){
			this.marcarSaltoCompensacionBBDD(letradoGuardia, "C");
		}
		
		
			
		return true;

	}
	private boolean isSaltoLetrado(LetradoGuardia letradoSeleccionado, HashMap<Long, List<LetradoGuardia>> hmPersonasConSaltos) throws ClsExceptions {
		return hmPersonasConSaltos.containsKey(letradoSeleccionado.getIdPersona());
	}
	
	private LetradoGuardia getSiguienteLetradoOrdenado(List alLetradosOrdenados,Puntero punteroLetrado,ArrayList diasGuardia,HashMap<Long, List<LetradoGuardia>> hmPersonasConSaltos) throws ClsExceptions{
		LetradoGuardia letradoGuardia = null;
		//Vamos a recorrer las compensaciones
		if(alLetradosOrdenados!=null && alLetradosOrdenados.size()>0){
			
			boolean findIt = false;
			int fin = punteroLetrado.getValor();
			do{
				LetradoGuardia auxLetradoSeleccionado = (LetradoGuardia)alLetradosOrdenados.get(punteroLetrado.getValor());
				if(auxLetradoSeleccionado.getBajasTemporales()==null){
					//si esta de vacaciones meto salto y que continue
					CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(this.usrBean);
					Map<String,CenBajasTemporalesBean> mBajasTemporales =  bajasTemporalescioneAdm.getDiasBajaTemporal(auxLetradoSeleccionado.getIdPersona(), auxLetradoSeleccionado.getIdInstitucion());
					auxLetradoSeleccionado.setBajasTemporales(mBajasTemporales);
				}
				
				//vale
				if(isLetradoValido(auxLetradoSeleccionado, diasGuardia, hmPersonasConSaltos, false)){
					letradoGuardia = auxLetradoSeleccionado;
					findIt= true;
				}
				if (punteroLetrado.getValor()<alLetradosOrdenados.size()-1)
					punteroLetrado.setValor(punteroLetrado.getValor()+1);
				else
					punteroLetrado.setValor(0);

				
			}while(!findIt && fin!=punteroLetrado.getValor());
			
		}
		return letradoGuardia;
	}
	
	private LetradoGuardia getSiguienteLetrado(List<LetradoGuardia> alCompensaciones,List alLetradosOrdenados,Puntero punteroLetrado,ArrayList diasGuardia,HashMap<Long, List<LetradoGuardia>> hmPersonasConSaltos) throws SIGAException, ClsExceptions{
		LetradoGuardia letradoGuardia = getSiguienteLetradoCompensado(alCompensaciones,diasGuardia,hmPersonasConSaltos);
		if(letradoGuardia==null){
			letradoGuardia = getSiguienteLetradoOrdenado(alLetradosOrdenados, punteroLetrado,diasGuardia,hmPersonasConSaltos);
		}
		if(letradoGuardia==null)
			throw new SIGAException("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorLetradosSuficientes");
		return letradoGuardia;
		
	
	}

	
	
	public List getDiasASeparar(Integer idInstitucion, Integer idTurno, Integer idGuardia,UsrBean usrBean) throws ClsExceptions{
		ScsHitoFacturableGuardiaAdm admScsHitoFacturableGuardia = new ScsHitoFacturableGuardiaAdm(usrBean);
		List alDiasASeparar = admScsHitoFacturableGuardia.getDiasASeparar(idInstitucion, idTurno, idGuardia);
		List alDiasASepararEnUnidadesDiarias = null;
		if(alDiasASeparar!=null && alDiasASeparar.size()>0){
			alDiasASepararEnUnidadesDiarias = new ArrayList();
			for (int i=0; i<alDiasASeparar.size(); i++){
				Hashtable htRegistro = (Hashtable)alDiasASeparar.get(i);
				String diasSemana = (String)htRegistro.get(ScsHitoFacturableGuardiaBean.C_DIASAPLICABLES);
				for (int j = 0; j < diasSemana.length(); j++) {
					char diaSemana = diasSemana.charAt(j);
					
					alDiasASepararEnUnidadesDiarias.add(new Integer 
							(CalendarioAutomatico.convertirUnidadesDiasSemana (diaSemana)));
						
				}
				
			}
		}
		return alDiasASepararEnUnidadesDiarias;
		
	}
	

	
	
	//Inserta en el arraylist arrayPeriodosDiasGuardiaSJCS los periodos de dias de guardia calculados por el CGAE
	//Nota: cada periodo es un arraylist de fechas (String en formato de fecha corto DD/MM/YYYY).
	public void calcularMatrizPeriodosDiasGuardia() throws ClsExceptions {
		try {
			CalendarioAutomatico calendarioAutomatico = new CalendarioAutomatico(this);
			ArrayList arrayDiasGuardiaCGAE = calendarioAutomatico.obtenerMatrizDiasGuardia();
	
			//Borro el array de dias de guardia:
			this.arrayPeriodosDiasGuardiaSJCS.clear();
			
			//Recorro el array de periodos y por cada periodo recorro el mismo para anhadir las fechas de dias de guardia en formato corto:
			for (int i=0; i<arrayDiasGuardiaCGAE.size(); i++) {
				PeriodoEfectivo periodoEfectivo = (PeriodoEfectivo)arrayDiasGuardiaCGAE.get(i);				
				Iterator it = periodoEfectivo.iterator();
					
				//Recorro por cada periodo efectivo los dias (en un Iterator de un TreeSet)
				ArrayList arrayDias = new ArrayList();								
				while (it.hasNext()) {
					Date fecha = (Date)it.next();
					SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");
			        String sFecha = datef.format(fecha);
					arrayDias.add(sFecha);				
				}
				
				//Inserto en el array para este periodo el array de los dias disponibles:
				//Nota: no inserto los arrays de periodos vacios que si guarda el CGAE.
				if (!arrayDias.isEmpty())
					this.arrayPeriodosDiasGuardiaSJCS.add(arrayDias);
			}
		} catch (Exception e){
			throw new ClsExceptions(e, "Excepcion al calcular la matriz de periodos de dias de guardias.");
		}
	}

	public void pintarMatrizPeriodosLetrados() {
		if (this.arrayPeriodosLetradosSJCS!=null && !this.arrayPeriodosLetradosSJCS.isEmpty()) {
			Iterator iter = this.arrayPeriodosLetradosSJCS.iterator();
			int i=1;
			while(iter.hasNext()) {
				ArrayList letrados = (ArrayList)iter.next();				
				ClsLogging.writeFileLog(">PERIODO "+i,7);
				Iterator iter2 = letrados.iterator();
				int j=1;
				while (iter2.hasNext()){
					LetradoGuardia letrado = (LetradoGuardia)iter2.next();
					ClsLogging.writeFileLog("->Dias Periodo "+i+":"+letrado.getPeriodoGuardias().toString(),7);
					ClsLogging.writeFileLog("->Periodo: "+i+" / Letrado: "+j+" es: "+letrado.getIdPersona(),7);
					j++;
				}
				i++;
			}
		} else
			ClsLogging.writeFileLog("->NO HAY PERIODOS...",7);
	}
	
	//Pinta el resultado del metodo "calcularMatrizLetradosGuardia".
	public void pintarSalidaCalcularMatrizLetradosGuardia (int salidaError) {		
		switch (salidaError) {
			case 0: ClsLogging.writeFileLog("> SALIDA=OK",7); break;
			case 1: ClsLogging.writeFileLog("> SALIDA=ERROR, NO HAY SUFICIENTES LETRADOS",7); break;
			case 2: ClsLogging.writeFileLog("> SALIDA=ERROR, HAY INCOMPATIBILIDAD DE GUARDIAS",7); break;
			case 3: ClsLogging.writeFileLog("> SALIDA=ERROR, NO HAY SEPARACION SUFIENTE EN LOS DIAS DE GUARDIAS",7); break;
			case 4: ClsLogging.writeFileLog("> SALIDA=ERROR, EXCEPCION",7); break;
		}
	}

	//Rellena el arraylist de la clase "arrayPeriodosLetradosSJCS" con los periodos rellenados cada uno con su letrado y el arralist de fechas que comprende el periodo.
	//Nota: previamente se ha calculado mediante el metodo "calcularMatrizPeriodosDiasGuardia()" el arraylist de periodos de dias factibles con las caracteristicas de la guardia.
	//
	//Devuelve un int con el nivel de error:
//case 1: forward = exito("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorLetradosSuficientes",request); break;
////- 2: Hay Incompatibilidad de Guardias
//case 2: forward = exito("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorIncompatibilidad",request); break;
////- 3: NO hay Separacion suficiente
//case 3: forward = exito("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorSeparacion",request); break;
////- 4: Error al provocarse una excepcion en el desarrollo del metodo.				
//case 4: forward = exito("messages.inserted.error",request); break;

	public void calcularMatrizLetradosGuardia(List lDiasASeparar) throws SIGAException ,ClsExceptions {		
		//En este objeto guardo los datos de los parametros modificados por otros metodos y usados en este:
		
		ScsCabeceraGuardiasAdm admCabecera=new ScsCabeceraGuardiasAdm(this.usrBean); 
		GenClientesTemporalAdm admClientesTmp = new GenClientesTemporalAdm(this.usrBean);
		CenBajasTemporalesAdm bajasTemporalescionesAdm = new CenBajasTemporalesAdm(this.usrBean);
		ArrayList alLetradosOrdenados;
		ArrayList diasGuardia; //Periodo obtenido de la lista de periodos del algoritmo del CGAE 
		ArrayList alLetradosInsertar; //lista de letrados para cada periodo
		Puntero punteroListaLetrados = new Puntero();

		try {

			alLetradosOrdenados = admClientesTmp.obtenerLetradosPosiblesPL(this.idInstitucion, this.idTurno, this.idGuardia);
			punteroListaLetrados.setValor(0);

			if (alLetradosOrdenados == null ||alLetradosOrdenados.size()==0)
				throw new SIGAException("No existe cola de letrados de guardia");//return (salidaCalcularMatrizLetradosGuardia = 1);
			
			
			//Bucle que recorre los periodos para este calendario e intenta asignarles letrados.
			//Nota: en caso de no poder seguir termina la ejecucion.

			//Obtengo las compensaciones:
			List<LetradoGuardia> alCompensaciones = getCompensaciones(this.idInstitucion, this.idTurno, this.idGuardia);
			//Obtengo los saltos
			HashMap<Long, List<LetradoGuardia>> hmPersonasConSaltos = getSaltos(this.idInstitucion, this.idTurno, this.idGuardia);
			
			for (int i=0;   i<this.arrayPeriodosDiasGuardiaSJCS.size(); i++) {
				//Obtengo un periodo del array de periodos inicial:
				//Nota: cada periodo es un arraylist de fechas (String en formato de fecha corto DD/MM/YYYY).
				diasGuardia = (ArrayList)this.arrayPeriodosDiasGuardiaSJCS.get(i);

				//INICIO: Bucle por el numero total de letrados por periodo de guardia
				//////////////////////////////////////////////////////////////////////
				int letradosInsertados = 0;
				alLetradosInsertar = new ArrayList();//ArrayList con los letrados para este periodo.
				for (int k=0; (k<this.beanGuardiasTurno.getNumeroLetradosGuardia().intValue()) ; k++) {								
					//TODO AAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAA AQUI
					LetradoGuardia letradoGuardia =  getSiguienteLetrado(alCompensaciones ,alLetradosOrdenados,punteroListaLetrados,diasGuardia, hmPersonasConSaltos);
					
					//Almaceno la asignacion de guardia si todo ha ido bien:
					if (letradoGuardia!=null) {
//						//Relleno el periodo para este letrado:
						letradoGuardia.setPeriodoGuardias(diasGuardia);
						//Anhado el letrado a la lista de letrados
						alLetradosInsertar.add(letradoGuardia.clone());
						letradosInsertados++;
					}else{
						throw new SIGAException("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorLetradosSuficientes");
					}					
				}

				//Almaceno la asignacion de guardia en BBDD si todo ha ido bien:
				this.almacenarAsignacionGuardia(alLetradosInsertar, diasGuardia,lDiasASeparar, UtilidadesString.getMensajeIdioma(this.usrBean,"gratuita.literal.comentario.sustitucion"));
					
				this.arrayPeriodosLetradosSJCS.add(alLetradosInsertar.clone());
				//Controlo que he insertado tantos letrados como numero de Guardias y todo ha ido bien:
				if ((letradosInsertados != this.beanGuardiasTurno.getNumeroLetradosGuardia().intValue()) )
					throw new SIGAException("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorLetradosSuficientes"); //No hay suficientes letrados				
			
			}//FIN del bucle que busca un rellenar los periodos con letrados
			
			//Si todo ha ido bien actualizo el ultimo letrado para la llamada al PL y realizo las compensaciones oportunas:
			
				//Actualizo el ultimo de la guardia por el puntero de letrados:
				int punteroUltimo = 0;
				if (punteroListaLetrados.getValor() == 0)
					punteroUltimo = alLetradosOrdenados.size()-1;
				else
					punteroUltimo = punteroListaLetrados.getValor()-1;
				this.actualizarUltimoLetradoGuardiaBBDD((LetradoGuardia)alLetradosOrdenados.get(punteroUltimo));

			 
		} catch (Exception e){
			e.printStackTrace();
			this.arrayPeriodosLetradosSJCS = null;
			throw new ClsExceptions("");
		}
		//Devuelvo el nivel del error:

	}
	
	//Mira si un letrado pertenece a un array de letrados por el idpersona
	private boolean hayLetradoRepetido (ArrayList arrayLetradosInsertados, LetradoGuardia letrado) {
		boolean hay = false;
		
		Iterator iter = arrayLetradosInsertados.iterator();
		while(iter.hasNext()) {
			LetradoGuardia letradoTmp = (LetradoGuardia)iter.next();
			if (letradoTmp.getIdPersona().equals(letrado.getIdPersona()))
				return true;
		}
		
		return hay;
	}
	
	//Compruebo que hay sufientes letrados en el array de letrados inicial:
	private boolean hayLetradosSuficientes (ArrayList arrayLetrados, boolean esPrimeraVuelta, int punteroArrayLetrados) {
		//AA> He realizado una mejor reestructuración de las condiciones,
		// además de añadir otra necesaria para resolver una incidencia
		//if (esPrimeraVuelta)
		//	return true;
		//else//NO es primera vuelta
		//	if (arrayLetrados.isEmpty())
		//		return false;
		//	else//El array NO esta vacio
		//		//Miro si estoy en el ultimo de la segunda vuelta para que no avance mas:
		//		if (punteroArrayLetrados == (arrayLetrados.size()-1))
		//			return false;
		//		else 
		//			return true;
		
		if (esPrimeraVuelta)
			return true;
		//NO es primera vuelta
		
		if (arrayLetrados.isEmpty())
			return false;
		//El array NO esta vacio
		
		if (arrayLetrados.size() == 1)
			return true;
		
		//Miro si estoy en el ultimo de la segunda vuelta para que no avance mas:
		if (punteroArrayLetrados == (arrayLetrados.size()-1))
			return false;
		
		return true;
	}
	
	//Devuelve el total de letrados del array que no tiene compensacion (tiene salto o normal)
	private int contarLetradosSinCompensaciones (ArrayList letrados){
		int total = 0;
		Iterator iter = letrados.iterator();
		
		while (iter.hasNext()){
			LetradoGuardia letrado = (LetradoGuardia)iter.next();
			if (!letrado.getSaltoCompensacion().equals("C"))
				total++;
		}
		return total;
	}

	/**
	 * Metodo que genera una array de periodos de de fecha a partir de otro. La restriccion que tenemos para 
	 * partir el array inicial es que contenga dias que se agrupan(que vienen en alDiasAgrupar)
	 * @param alPeriodo
	 * @param alDiasAgrupar
	 * @return
	 * @throws Exception
	 */
	private List getPeriodos(ArrayList alPeriodo, List alDiasAgrupar) throws Exception{
		
        
		List lPeriodosSinAgrupar = new ArrayList();
		if(alDiasAgrupar==null || alDiasAgrupar.size()<1){
			lPeriodosSinAgrupar.add(alPeriodo);
			return lPeriodosSinAgrupar;
		}
        List alDias = new ArrayList();
		for (int i = 0; i < alPeriodo.size(); i++) {
			String date = (String)alPeriodo.get(i);
			Calendar c = Calendar.getInstance ();
		    c.setTime (GstDate.convertirFecha(date,"dd/MM/yyyy"));
		    if(alDiasAgrupar.contains(new Integer (c.get (Calendar.DAY_OF_WEEK)))){
		    	//añadimos el acuemulado anterios si no esta vacio
		    	if(alDias.size()>0){
		    		lPeriodosSinAgrupar.add(alDias);
		    		
		    	}
		    	//Creamos un nuevo almacen para el dia
		    	alDias = new ArrayList();
		    	alDias.add(date);
		    	//lo añadimos al que devolveremos
		    	lPeriodosSinAgrupar.add(alDias);
		    	//inicializamos de nuevo el acumulador de dias
		    	alDias = new ArrayList();
		    	
		    	
		    }else{
		    	alDias.add(date);
		    	
		    }
			
		}
		//añadimos el último
		if(alDias.size()>0)
			lPeriodosSinAgrupar.add(alDias);
			
		return lPeriodosSinAgrupar; 
		
		
		
	}
	private HashMap<Long, List<LetradoGuardia>> getSaltos (Integer idInstitucion,Integer idTurno,Integer idGuardia) throws ClsExceptions {
	    RowsContainer rc = new RowsContainer();
	    LetradoGuardia letradoSeleccionado = null;
	    HashMap<Long, List<LetradoGuardia>> hmPersonasConSaltos = null;
		// voy a comprobar si existe un salto en base de datos
		try {

		    String sql = " select * from scs_saltoscompensaciones s "+
						 " where s.idinstitucion="+idInstitucion+
						// " and   s.idpersona="+letradoSeleccionado.getIdPersona()+
						 " and   s.idturno="+idTurno+
						 " and   s.idguardia="+idGuardia+
						 " and   s.saltoocompensacion='S' "+
						 " and   s.fechacumplimiento is null ";
	
			ScsSaltosCompensacionesAdm adm = new ScsSaltosCompensacionesAdm(this.usrBean);
			rc = adm.find(sql);
			hmPersonasConSaltos = new HashMap<Long, List<LetradoGuardia>>();
			List<LetradoGuardia> alLetradosSaltados = null; 
			for (int i = 0; i < rc.size(); i++){
			    letradoSeleccionado = new LetradoGuardia();
			    letradoSeleccionado.setIdInstitucion(this.getIdInstitucion());
			    letradoSeleccionado.setIdTurno(this.getIdTurno());
			    letradoSeleccionado.setIdGuardia(this.getIdGuardia());
			    letradoSeleccionado.setSaltoCompensacion("C");
			    
			    Row fila = (Row) rc.get(i);
				Hashtable hFila = fila.getRow();
				Long idPersona = new Long((String)hFila.get(ScsSaltosCompensacionesBean.C_IDPERSONA));
				letradoSeleccionado.setIdPersona(idPersona);
				if(hmPersonasConSaltos.containsKey(idPersona)){
					alLetradosSaltados = (List)hmPersonasConSaltos.get(idPersona);
					
				}else{
					alLetradosSaltados = new ArrayList<LetradoGuardia>();
					
				}
				
				alLetradosSaltados.add(letradoSeleccionado);
				hmPersonasConSaltos.put(idPersona, alLetradosSaltados);
			    //letradoSeleccionado
			}
		} catch (Exception e) {
		    throw new ClsExceptions(e,"Error al comporbar si hay salto en BD.");
		}
		
		return hmPersonasConSaltos;
	}
	
	private List<LetradoGuardia> getCompensaciones (Integer idInstitucion,Integer idTurno,Integer idGuardia) throws ClsExceptions {
	    RowsContainer rc = new RowsContainer();
	    LetradoGuardia letradoSeleccionado = null;
	    List<LetradoGuardia> alLetradosCompensados = null;
		try {
			//obtiene las compesaciones de letrados que no estén de baja en la guardia
		    String sql = " select * from scs_saltoscompensaciones s, scs_inscripcionguardia g "+
						 " where s.idinstitucion="+idInstitucion+
						 " and   s.idturno="+idTurno+
						 " and   s.idguardia="+idGuardia+
						 " and   s.saltoocompensacion='C' "+
						 " and   s.fechacumplimiento is null "+
						 " and s.idinstitucion = g.idinstitucion "+
					     " and s.idturno = g.idturno "+
					     " and s.idguardia = g.idguardia "+
					     " and s.idpersona = g.idpersona "+
					     " and g.fechasuscripcion = "+
					     "     (select max(i.fechasuscripcion) "+
					     "        from scs_inscripcionguardia i "+
					     "       where i.idinstitucion = g.idinstitucion "+
					     "         and i.idturno = g.idturno "+
					     "         and i.idguardia = g.idguardia "+
					     "         and i.idpersona = g.idpersona "+
					     "      ) "+
					     " and g.fechabaja is null";
	
			ScsSaltosCompensacionesAdm adm = new ScsSaltosCompensacionesAdm(this.usrBean);
			rc = adm.find(sql);
			alLetradosCompensados = new ArrayList<LetradoGuardia>();
			
			
    			
			
			
			for (int i = 0; i < rc.size(); i++){
			    letradoSeleccionado = new LetradoGuardia();
			    letradoSeleccionado.setIdInstitucion(this.getIdInstitucion());
			    letradoSeleccionado.setIdTurno(this.getIdTurno());
			    letradoSeleccionado.setIdGuardia(this.getIdGuardia());
			    letradoSeleccionado.setSaltoCompensacion("C");
			    
			    Row fila = (Row) rc.get(i);
				Hashtable hFila = fila.getRow();
				letradoSeleccionado.setIdPersona(new Long((String)hFila.get(ScsSaltosCompensacionesBean.C_IDPERSONA)));
				alLetradosCompensados.add(letradoSeleccionado);
			    //letradoSeleccionado
			}
		} catch (Exception e) {
		    throw new ClsExceptions(e,"Error al obtener letrados compensados  en BD.");
		}
		
		return alLetradosCompensados;
	}
	public class Puntero {
		int valor;
		int getValor () {
			return valor;
		}
		void setValor (int valor) {
			this.valor = valor;
		}
	}
	
}


