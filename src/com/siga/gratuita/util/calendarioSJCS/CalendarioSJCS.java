/*
 * Created on Mar 9, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.gratuita.util.calendarioSJCS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
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
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsSaltosCompensacionesBean;
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
	public void almacenarAsignacionGuardiaLetrado(LetradoGuardia letrado, ArrayList periodoDiasGuardia) throws ClsExceptions {
		ArrayList arrayLetrados = new ArrayList();
		//Anhado el letrado
		arrayLetrados.add(letrado);
		almacenarAsignacionGuardia(arrayLetrados, periodoDiasGuardia, UtilidadesString.getMensajeIdioma(this.getUsrBean(),"gratuita.literal.letrado.refuerzo"));
	}
	
	//Almacena para una lista de letrados las guardias con los registros correspondientes a sus dias de guardia.
	private void almacenarAsignacionGuardia(ArrayList arrayLetrados, ArrayList periodoDiasGuardia, String mensaje) throws ClsExceptions {
		Iterator iter;
		Iterator iterLetrados;
		String fechaInicioPeriodo=null, fechaFinPeriodo=null, fechaPeriodo=null;
		ScsCabeceraGuardiasBean beanCabeceraGuardias;
		ScsGuardiasColegiadoBean beanGuardiasColegiado;
		LetradoGuardia letrado;
			
		try {
			ScsCabeceraGuardiasAdm admCabeceraGuardias = new ScsCabeceraGuardiasAdm(this.usrBean);
			ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.usrBean);

			fechaInicioPeriodo = (String)periodoDiasGuardia.get(0);
			fechaFinPeriodo = (String)periodoDiasGuardia.get(periodoDiasGuardia.size()-1);
			
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
				iter = periodoDiasGuardia.iterator();
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

	//Recorro el array de letrados mientras haya saltos y los marco como "N" en la lista y en BBDD.
	//Devuelvo un int con el puntero de letrados actualizado.
	private DatosIteracionCalendarioSJCS recorrerSaltos(int punteroListaLetrados, ArrayList arrayListaLetrados, LetradoGuardia letradoSeleccionado) throws ClsExceptions {
		DatosIteracionCalendarioSJCS salida = new DatosIteracionCalendarioSJCS();
		
		try {
			//INICIO Bucle mientras haya Saltos en la lista de letrados
			boolean haySaltos = true;			
			
//			Lo que debo hacer es:
//		    - Buscar un letrado con compensacion y sin saltos para devolverlo (letradoSeleccionado = dameCompensacion() a medio hacer)
//		    - Marcar la compensacion como cumplida --> this.marcarSaltoCompensacionBBDD(letradoSeleccionado, "C");
//			- Si no es el ultimo decremento el contador del puntero. (NO ESTOY SEGURO, revisar bien que hacer)
//			//				if (indiceActual != (arrayListaLetrados.size()-1))  
//			//				punteroListaLetrados--;
//			- No continuar con el while de los saltos
//			- devolver el personaje

			// RGG 05/09/2008
			letradoSeleccionado = dameCompensacion();
			if (letradoSeleccionado!=null) {
			    this.marcarSaltoCompensacionBBDD(letradoSeleccionado, "C");
			    //if (indiceActual != (arrayListaLetrados.size()-1))  
			    //    punteroListaLetrados--;
			    
			} else {
			
				while (haySaltos && !arrayListaLetrados.isEmpty()) {				
					//Selecciono letrados de la lista de letrados en funcion del puntero
					//Nota: debo controlar que no se quede en un bucle infinito.
					letradoSeleccionado = (LetradoGuardia)arrayListaLetrados.get(punteroListaLetrados);
					
					//Incremento el puntero:		
					punteroListaLetrados = (punteroListaLetrados + 1) % arrayListaLetrados.size(); 
												
					//SI es SALTO
					haySaltos = this.esSalto(letradoSeleccionado);
					if (haySaltos) {
						//Marco como NO SALTO en la lista de letrados:
						
					    // RGG 05/09/2008 this.marcarNoSalto(letradoSeleccionado, arrayListaLetrados);
						
						//Compenso el SALTO en BBDD:
						this.marcarSaltoCompensacionBBDD(letradoSeleccionado, "S");								
					}
				}//FIN bucle que avanza el puntero mientras haya saltos en la lista de letrados.
				
			} // fin else
			
			//SI es COMPENSACION
//			if (this.esCompensacion(letradoSeleccionado)) {
//				//Actualizo el puntero de letrados:
//				int indiceActual = arrayListaLetrados.indexOf(letradoSeleccionado);
//				//Si no es el ultimo decremento el contador del puntero.
//				if (indiceActual != (arrayListaLetrados.size()-1))  
//					punteroListaLetrados--;
//				//Lo elimino de la lista de letrados:									
//				arrayListaLetrados.remove(indiceActual);
//				
//				//Marco la compensacion como cumplida en BBDD:
//				this.marcarSaltoCompensacionBBDD(letradoSeleccionado, "C");
//			}		
			
			//Actualizo la salida:
			salida.setLetradoSeleccionado(letradoSeleccionado);
			salida.setArrayListaLetrados(arrayListaLetrados);
			salida.setPunteroListaLetrados(punteroListaLetrados);			
		} catch (Exception e) {
			throw new ClsExceptions(e, "Excepcion al realizar el bucle que recorre los letrados con Salto en el metodo recorrerSatos.");
		}
		return salida;
	}

	//Obtiene el letrado a partir de la lista de letrados, la lista de letrados pendiente y el periodo.
	//Devuelve un objeto con los datos de los parametros de entrada modificados:
	//-error: el nivel de erro es el mismo que en el metodo padre:
	//		- 0: Todo Correcto
	//		- 1: NO Hay Suficientes Letrados
	//		- 2: Hay Incompatibilidad de Guardias	
	//		- 3: NO hay Separacion suficiente
	//		- 4: Error al provocarse una excepcion en el desarrollo del metodo.
	//-punteroListaLetrados
	//-Letrado
	//-ArrayList letrados
	//-ArrayList letrados pendientes	
	private DatosIteracionCalendarioSJCS obtenerLetrado(ArrayList arrayListaLetrados, ArrayList arrayListaLetradosPendientes, int punteroListaLetrados, ArrayList periodoDiasGuardia, ArrayList letradosInsertadosEnPeriodo) {
		LetradoGuardia letradoSeleccionado = new LetradoGuardia();
		//En estos objetos guardo los datos de los parametros modificados por otros metodos y usados en este:
		DatosIteracionCalendarioSJCS salida = new DatosIteracionCalendarioSJCS();
		DatosIteracionCalendarioSJCS salidaRecorrerSaltos = new DatosIteracionCalendarioSJCS();
		
		int error = 4;				
		boolean hayIncompatibilidad = true;
		boolean haySuficienteSeparacion = true;				
		boolean haySuficientesLetrados = true;
		boolean esPrimeraIteracionBucle = true;
		boolean encontrado = false;
		boolean hayLetradoRepetido = false;
		
		
		
		try {
			
			//Inicializo la lista de letrados pendientes Temporal (contiene aquellos letrados que por la causa que sea para este periodo no pueden hacer la guardia):
			ArrayList arrayListaLetradosPendientesTemporal = new ArrayList();
					
			//Bucle para recorrerlo mientras:
			//1.-Haya suficientes letrados en la lista de letrados o en la lista de letrados temporal
			//2.-No haya incompatibilidad con otras guardias
			//3.-Haya suficiente separacion de dias
			
			while (!encontrado && (haySuficientesLetrados || esPrimeraIteracionBucle)) {
				//SI HAY LETRADOS EN LISTA PENDIENTES
				if (!arrayListaLetradosPendientes.isEmpty()) {
					//Selecciono el primero de la lista de pendientes:
					letradoSeleccionado = (LetradoGuardia)((LetradoGuardia)arrayListaLetradosPendientes.get(0)).clone();
					
					//Lo elimino de la lista de pendientes:
					arrayListaLetradosPendientes.remove(0);					
				//NO HAY LETRADOS EN LISTA PENDIENTES
				} else {
					//Recorro el array de letrados mientras haya saltos y los marco como "N" en la lista y en BBDD.
					salidaRecorrerSaltos = this.recorrerSaltos(punteroListaLetrados, arrayListaLetrados, letradoSeleccionado);
					//Actualizo los valores devueltos:
					punteroListaLetrados = salidaRecorrerSaltos.getPunteroListaLetrados();
					arrayListaLetrados = salidaRecorrerSaltos.getArrayListaLetrados();
					letradoSeleccionado = salidaRecorrerSaltos.getLetradoSeleccionado();

					//Si estoy en la primera vuelta y el puntero vuelve al inicio ya he dado una vuelta entera:
					if (esPrimeraIteracionBucle && punteroListaLetrados==0)
						esPrimeraIteracionBucle = false;				
				}
				
				//Parte comun con el letrado obtenido: "letradoSeleccionado"
				//COMPRUEBO QUE PARA ESTE PERIODO NO TENGO REPETIDO EL LETRADO:
				hayLetradoRepetido = hayLetradoRepetido(letradosInsertadosEnPeriodo,letradoSeleccionado);
				if (hayLetradoRepetido) {
				    //SI esta repetido
				    //Anhado el letrado a la lista de letrados temporal:
					arrayListaLetradosPendientesTemporal.add(letradoSeleccionado);
				} else { 
				    //NO esta repetido
					//Compruebo la incompatibilidad de guardias:
					hayIncompatibilidad = this.hayIncompatibilidadGuardias(letradoSeleccionado, periodoDiasGuardia);
					if (hayIncompatibilidad) {
						//SI HAY INCOMPATIBILIDAD
						//Anhado el letrado a la lista de letrados temporal:
						arrayListaLetradosPendientesTemporal.add(letradoSeleccionado);
					} else {
						//NO HAY INCOMPATIBILIDAD
						//Compruebo la separacion de dias:
						haySuficienteSeparacion = this.haySeparacionDias(letradoSeleccionado, periodoDiasGuardia);
						if (!haySuficienteSeparacion) {
							//NO HAY SEPARACION SUFICIENTE
							//Anhado el letrado a la lista temporal:
							arrayListaLetradosPendientesTemporal.add(letradoSeleccionado);
						
						} else {
							//SI HAY SEPARACION SUFICIENTE
							//Tengo el letrado:
							encontrado = true;
							//Vuelco lista de pendientes temporal a lista pendientes y termino el proceso de obtener el letrado:
							Iterator itCopia = arrayListaLetradosPendientesTemporal.iterator();
							while (itCopia.hasNext())
								arrayListaLetradosPendientes.add(itCopia.next());
						}
					}
				}
				
				//Actualizo si hay suficientes letrados:
				// Se ha modificado la busqueda de letradosSuficientes separando el caso de un solo letrado en la lista o mas de un letrado en la lista
				// porque cuando sólo habia un letrado no se cumplían nunca las condiciones de salida del bucle 
				if (arrayListaLetrados.size()>1){//Si tenemos mas de un letrado en la lista 
				  haySuficientesLetrados = hayLetradosSuficientes(arrayListaLetrados, esPrimeraIteracionBucle, punteroListaLetrados);
				}else{// si solo hay un letrado en la lista
					if (!haySuficientesLetrados)
						error = 1; 
					else if (hayIncompatibilidad)
						error = 2;
					else if (!haySuficienteSeparacion)
						error = 3;
					else
						error = 0;	
					
					if (error!=0 && error!=4){
					    haySuficientesLetrados=false;
					}else{
						haySuficientesLetrados = hayLetradosSuficientes(arrayListaLetrados, esPrimeraIteracionBucle, punteroListaLetrados);
					}
				}
				//La siguiente condicion da un mal funcionamiento si solo hay 1 letrado
				//|| !arrayListaLetradosPendientes.isEmpty();
				
//				
			}//FIN bucle de obtener letrado
			
			//- 0: Todo Correcto
			//- 1: NO Hay Suficientes Letrados
			//- 2: Hay Incompatibilidad de Guardias	
			//- 3: NO hay Separacion suficiente
			//- 4: Error al provocarse una excepcion en el desarrollo del metodo.
			if (error==4){
				if (!haySuficientesLetrados)
					error = 1; 
				else if (hayIncompatibilidad)
					error = 2;
				else if (!haySuficienteSeparacion)
					error = 3;
				else
					error = 0;	
			}
			
			//Actualizo la salida:
			salida.setLetradoSeleccionado(letradoSeleccionado);
			salida.setArrayListaLetrados(arrayListaLetrados);
			salida.setArrayListaLetradosPendientes(arrayListaLetradosPendientes);			
			salida.setPunteroListaLetrados(punteroListaLetrados);
			salida.setError(error);
			
		} catch (Exception e){
		    e.printStackTrace();
			salida.setError(4);
		}
		return salida;
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
	//- 0: Todo Correcto
	//- 1: NO Hay Suficientes Letrados
	//- 2: Hay Incompatibilidad de Guardias	
	//- 3: NO hay Separacion suficiente
	//- 4: Error al provocarse una excepcion en el desarrollo del metodo.
	public int calcularMatrizLetradosGuardia() throws ClsExceptions {		
		//En este objeto guardo los datos de los parametros modificados por otros metodos y usados en este:
		DatosIteracionCalendarioSJCS salidaObtenerLetrado = new DatosIteracionCalendarioSJCS();
		
		ScsCabeceraGuardiasAdm admCabecera=new ScsCabeceraGuardiasAdm(this.usrBean); 
		GenClientesTemporalAdm admClientesTmp = new GenClientesTemporalAdm(this.usrBean); 		
		ArrayList arrayListaLetrados;
		ArrayList arrayListaLetradosPendientes;
		ArrayList periodoDiasGuardia; //Periodo obtenido de la lista de periodos del algoritmo del CGAE 
		ArrayList arrayLetrados; //lista de letrados para cada periodo
		LetradoGuardia letradoSeleccionado = new LetradoGuardia();
		
		LetradoGuardia letrado;
		Iterator iterLetrados;
		int punteroListaLetrados;
		int salidaCalcularMatrizLetradosGuardia = 0; //Salida para este metodo
		int totalLetradosSinCompensaciones = 0;

		try {
			//Ejecuto el metodo que llama al PL que me inserta en una tabla temporal los letrados para hacer las guardias con un indice posicion + idinstitucion
			//Nota: creo un Vector con objetos LetradoGuardia en el que guardo ordenadamente el idpersona y el salto.
			arrayListaLetrados = admClientesTmp.obtenerLetradosPosiblesPL(this.idInstitucion, this.idTurno, this.idGuardia);
			//Puntero de la lista de letrados al principio:
			punteroListaLetrados = 0;

			//Inicializo la lista de letrados pendientes:
			arrayListaLetradosPendientes = new ArrayList();			
									
			//Hay suficientes letrados mientras haya letrados en la lista de letrados y en la temporal:
			//Nota: inicialmente solo hay letrados en la lista de letrados.
			//AA> Es de mala programacion comparar una variable con null y
			// a continuacion acceder a su contenido en otra condicion.
			// Por eso, se ha modificado la estructura del if siguiente
/*			if ((arrayListaLetrados!=null) && !arrayListaLetrados.isEmpty())
				salidaCalcularMatrizLetradosGuardia = 0;
			else
				salidaCalcularMatrizLetradosGuardia = 1;
*/
			if (arrayListaLetrados == null)
				return (salidaCalcularMatrizLetradosGuardia = 1);
			if (arrayListaLetrados.isEmpty())
				return (salidaCalcularMatrizLetradosGuardia = 1);
			
			//
			//Bucle que recorre los periodos para este calendario e intenta asignarles letrados.
			//Nota: en caso de no poder seguir termina la ejecucion.
			//
			for (int i=0;  (salidaCalcularMatrizLetradosGuardia==0) && (i<this.arrayPeriodosDiasGuardiaSJCS.size()); i++) {
				//Obtengo un periodo del array de periodos inicial:
				//Nota: cada periodo es un arraylist de fechas (String en formato de fecha corto DD/MM/YYYY).
				periodoDiasGuardia = (ArrayList)this.arrayPeriodosDiasGuardiaSJCS.get(i);

				//Controlo inicialmente que  hay sufientes letrados sin compensaciones para hacer el periodo:
				totalLetradosSinCompensaciones = contarLetradosSinCompensaciones(arrayListaLetrados);
				if (totalLetradosSinCompensaciones < this.beanGuardiasTurno.getNumeroLetradosGuardia().intValue())
					salidaCalcularMatrizLetradosGuardia = 1; //ERROR: No hay letrados suficientes
				
				
				//////////////////////////////////////////////////////////////////////
				//INICIO: Bucle por el numero total de letrados por periodo de guardia
				//////////////////////////////////////////////////////////////////////
				int letradosInsertados = 0;
				arrayLetrados = new ArrayList();//ArrayList con los letrados para este periodo.
				for (int k=0; (k<this.beanGuardiasTurno.getNumeroLetradosGuardia().intValue()) && (salidaCalcularMatrizLetradosGuardia==0); k++) {								
					//INICIO OBTENER LETRADO
					//Devuelve un objeto con los siguientes datos:
					//-punteroListaLetrados
					//-arrayListaLetrados
					//-arrayListaLetradosPendientes
					//-error
					salidaObtenerLetrado = obtenerLetrado(arrayListaLetrados, arrayListaLetradosPendientes, punteroListaLetrados, periodoDiasGuardia, arrayLetrados);
					
					//Actualizo los valores devueltos:
					punteroListaLetrados = salidaObtenerLetrado.getPunteroListaLetrados();
					arrayListaLetrados = salidaObtenerLetrado.getArrayListaLetrados();
					arrayListaLetradosPendientes = salidaObtenerLetrado.getArrayListaLetradosPendientes();
					letradoSeleccionado = salidaObtenerLetrado.getLetradoSeleccionado();
					salidaCalcularMatrizLetradosGuardia = salidaObtenerLetrado.getError();
					//FIN OBTENER LETRADO
					
					//Almaceno la asignacion de guardia si todo ha ido bien:
					if (salidaCalcularMatrizLetradosGuardia == 0) {
						//Relleno el periodo para este letrado:
						letradoSeleccionado.setPeriodoGuardias(periodoDiasGuardia);
						//Anhado el letrado a la lista de letrados
						arrayLetrados.add(letradoSeleccionado.clone());
						letradosInsertados++;
					}					
				}

				//Almaceno la asignacion de guardia en BBDD si todo ha ido bien:
				if (salidaCalcularMatrizLetradosGuardia == 0)
					this.almacenarAsignacionGuardia(arrayLetrados, periodoDiasGuardia, UtilidadesString.getMensajeIdioma(this.usrBean,"gratuita.literal.comentario.sustitucion"));
					
				//////////////////////////////////////////////////////////////////////
				//FIN: Bucle por el numero total de letrados por periodo de guardia
				//////////////////////////////////////////////////////////////////////
								
				//Anhado los letrados a el arraylist de periodos de letrados si todo fue bien:
				if (salidaCalcularMatrizLetradosGuardia==0)
					this.arrayPeriodosLetradosSJCS.add(arrayLetrados.clone());
				
				//Controlo que he insertado tantos letrados como numero de Guardias y todo ha ido bien:
				if ((letradosInsertados != this.beanGuardiasTurno.getNumeroLetradosGuardia().intValue()) && (salidaCalcularMatrizLetradosGuardia == 0))
					salidaCalcularMatrizLetradosGuardia = 1; //No hay suficientes letrados				
			
			}//FIN del bucle que busca un rellenar los periodos con letrados
			
			//Si todo ha ido bien actualizo el ultimo letrado para la llamada al PL y realizo las compensaciones oportunas:
			if (salidaCalcularMatrizLetradosGuardia == 0) {
				//Actualizo el ultimo de la guardia por el puntero de letrados:
				int punteroUltimo = 0;
				if (punteroListaLetrados == 0)
					punteroUltimo = arrayListaLetrados.size()-1;
				else
					punteroUltimo = punteroListaLetrados-1;
				this.actualizarUltimoLetradoGuardiaBBDD((LetradoGuardia)arrayListaLetrados.get(punteroUltimo));
				
				//Actualizo compensaciones haciendo un insert por cada letrado de la lista de pendientes con compensacion:
				//Nota: los saltos ya los he ido actualizando a lo largo del algoritmo.
				this.insertarCompensacionesBBDD(arrayListaLetradosPendientes);
			} 
		} catch (Exception e){
			e.printStackTrace();
			this.arrayPeriodosLetradosSJCS = null;
			salidaCalcularMatrizLetradosGuardia = 4; //Error de Excepcion
		}
		//Devuelvo el nivel del error:
		return salidaCalcularMatrizLetradosGuardia;
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
}