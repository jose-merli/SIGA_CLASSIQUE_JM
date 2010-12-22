/*
 * Created on Mar 9, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.gratuita.util.calendarioSJCS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.LogFileWriter;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsCabeceraGuardiasAdm;
import com.siga.beans.ScsCabeceraGuardiasBean;
import com.siga.beans.ScsCalendarioGuardiasAdm;
import com.siga.beans.ScsCalendarioGuardiasBean;
import com.siga.beans.ScsCalendarioLaboralAdm;
import com.siga.beans.ScsGrupoGuardiaColegiadoAdm;
import com.siga.beans.ScsGrupoGuardiaColegiadoBean;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.beans.ScsGuardiasColegiadoBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsHitoFacturableGuardiaAdm;
import com.siga.beans.ScsHitoFacturableGuardiaBean;
import com.siga.beans.ScsSaltoCompensacionGrupoAdm;
import com.siga.beans.ScsSaltoCompensacionGrupoBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsSaltosCompensacionesBean;
import com.siga.general.SIGAException;
import com.siga.gratuita.InscripcionGuardia;
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
public class CalendarioSJCS
{
	//Claves para identificar la guardia de un calendario
	private Integer idInstitucion;
	private Integer idTurno;
	private Integer idGuardia;
	private Integer idCalendarioGuardias;
	
	//UserBean
	private UsrBean usrBean;

	//Guardia del Turno
	private ScsGuardiasTurnoBean beanGuardiasTurno = null;
	
    //Datos para usar posteriormente propios de SJCS
	/**ArrayList de periodos con las fechas en formato corto como String*/
	private ArrayList<ArrayList<String>> arrayPeriodosDiasGuardiaSJCS;	
	/**ArrayList con los periodos, cada uno con sus letrados para hacer las guardias SJCS*/
	private ArrayList arrayPeriodosLetradosSJCS;
 
	//Datos a calcular previamente
	private Vector vDiasFestivos;
	private String fechaInicio;
	private String fechaFin;
	private Vector<ScsCalendarioGuardiasBean> calendariosVinculados;
	
	/**Buffer de lineas para log*/
	private LogFileWriter log;


	/**
	 * Constructor
	 */
	public CalendarioSJCS(Integer idInstitucion, Integer idTurno, Integer idGuardia, Integer idCalendarioGuardias,
			Vector<ScsCalendarioGuardiasBean> calendariosVinculados, UsrBean usr, LogFileWriter log)
	{
		// Controles
		ScsGuardiasTurnoAdm admGuardiasTurno = new ScsGuardiasTurnoAdm(this.usrBean);
		ScsCalendarioGuardiasAdm admCalendarioGuardias = new ScsCalendarioGuardiasAdm(this.usrBean);
		ScsCalendarioLaboralAdm admCalendarioLaboral = new ScsCalendarioLaboralAdm(this.usrBean);
		
		// Variables
		String where;
		Vector<ScsGuardiasTurnoBean> vGuardias;
		
		// Claves
		this.idInstitucion = idInstitucion;
		this.idTurno = idTurno;
		this.idGuardia = idGuardia;
		this.idCalendarioGuardias = idCalendarioGuardias;

		// UsrBean
		this.usrBean = usr;

		// obteniendo el resto de datos:
		
		// 1. GUARDIATURNO: Todos los datos de la guardia
		where = " WHERE " + ScsGuardiasTurnoBean.C_IDINSTITUCION + "=" + idInstitucion + " AND "
				+ ScsGuardiasTurnoBean.C_IDTURNO + "=" + idTurno + " AND " + ScsGuardiasTurnoBean.C_IDGUARDIA + "="
				+ idGuardia;
		try {
			vGuardias = admGuardiasTurno.select(where);
			if (!vGuardias.isEmpty())
				this.beanGuardiasTurno = (ScsGuardiasTurnoBean) vGuardias.firstElement();
		} catch (Exception e) {
			this.beanGuardiasTurno = null;
		}
		
		// Calendarios vinculados (guardias vinculadas)
		if (calendariosVinculados == null || calendariosVinculados.isEmpty())
			this.calendariosVinculados = null;
		else
			this.calendariosVinculados = calendariosVinculados;
		
		// 2. CALENDARIO: Fecha de inicio y fin
		where = " WHERE " + ScsCalendarioGuardiasBean.C_IDINSTITUCION + "=" + idInstitucion + " AND "
				+ ScsCalendarioGuardiasBean.C_IDTURNO + "=" + idTurno + " AND " + ScsCalendarioGuardiasBean.C_IDGUARDIA
				+ "=" + idGuardia + " AND " + ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS + "="
				+ idCalendarioGuardias;
		Vector vCalendarioGuardias;
		try {
			vCalendarioGuardias = admCalendarioGuardias.select(where);
			if (!vCalendarioGuardias.isEmpty()) {
				ScsCalendarioGuardiasBean beanCalendarioGuardias = (ScsCalendarioGuardiasBean) vCalendarioGuardias
						.firstElement();
				this.fechaInicio = beanCalendarioGuardias.getFechaInicio();
				this.fechaFin = beanCalendarioGuardias.getFechaFin();
			}
		} catch (Exception e) {
			this.fechaInicio = "";
			this.fechaFin = "";
		}

		// 3. CALENDARIO FESTIVOS:
		this.vDiasFestivos = (Vector) admCalendarioLaboral.obtenerFestivosTurno(idInstitucion, idTurno,
				this.fechaInicio, this.fechaFin).clone();

		// 4. INICIALIZACION DE LOS 2 ARRAYS:
		this.arrayPeriodosDiasGuardiaSJCS = new ArrayList();
		this.arrayPeriodosLetradosSJCS = new ArrayList();
		this.log = log;
	} // CalendarioSJCS()


	// SETTERS
	public void setIdInstitucion(Integer valor) {this.idInstitucion = valor;}
	public void setIdTurno(Integer valor) {this.idTurno = valor;}
	public void setIdGuardia(Integer valor) {this.idGuardia = valor;}
	public void setIdCalendarioGuardias(Integer valor) {this.idCalendarioGuardias = valor;}
	public void setFechaInicio(String valor) {this.fechaInicio = valor;}
	public void setFechaFin(String valor) {this.fechaFin = valor;}
	
	public void setArrayPeriodosDiasGuardiaSJCS(ArrayList valor) {this.arrayPeriodosDiasGuardiaSJCS = valor;}
	public void setArrayPeriodosLetradosSJCS(ArrayList valor) {this.arrayPeriodosLetradosSJCS = valor;}
	public void setBeanGuardiasTurno(ScsGuardiasTurnoBean valor) {this.beanGuardiasTurno = valor;}
	public void setUsrBean(UsrBean valor) {this.usrBean = valor;}
	public void setVDiasFestivos(Vector valor) {vDiasFestivos = valor;}
	public void setCalendariosVinculados(Vector<ScsCalendarioGuardiasBean> valor) {this.calendariosVinculados = valor;}



	// GETTERS
	public Integer getIdInstitucion() {return idInstitucion;}
	public Integer getIdTurno() {return idTurno;}
	public Integer getIdGuardia() {return idGuardia;}
	public Integer getIdCalendarioGuardias() {return idCalendarioGuardias;}
	public String getFechaInicio() {return fechaInicio;}
	public String getFechaFin() {return fechaFin;}
	
	public ArrayList getArrayPeriodosDiasGuardiaSJCS() {return arrayPeriodosDiasGuardiaSJCS;}
	public ArrayList getArrayPeriodosLetradosSJCS() {return arrayPeriodosLetradosSJCS;}
	public ScsGuardiasTurnoBean getBeanGuardiasTurno() {return beanGuardiasTurno;}
	public UsrBean getUsrBean() {return usrBean;}
	public Vector getVDiasFestivos() {return vDiasFestivos;}
	public Vector<ScsCalendarioGuardiasBean> getCalendariosVinculados() {return calendariosVinculados;}
	
	
	/**
	 * Pinta las fechas de los periodos guardados en el arraylist SJCS.
	 * @throws ClsExceptions
	 */
	public void pintarCalendarioSJCS() throws ClsExceptions
	{
		try {
			ClsLogging.writeFileLog("INICIO CALENDARIO (" + "IDINSTITUCION=" + this.idInstitucion + ", IDGUARDIA="
					+ this.idGuardia + ", IDCALENDARIO=" + this.idCalendarioGuardias + "):", 7);

			// Recorro el arraylist de los periodos efectivos
			Iterator it = this.arrayPeriodosDiasGuardiaSJCS.iterator();
			int i = 1;
			// TODO Adrian: Escribir esto en el LOG del calendario
			while (it.hasNext()) {
				ClsLogging.writeFileLog("> Periodo " + i + ":", 7);
				ArrayList arrayDias = new ArrayList();
				arrayDias = (ArrayList) it.next();
				Iterator it2 = arrayDias.iterator();
				int j = 1;
				while (it2.hasNext()) {
					ClsLogging.writeFileLog("--> Dia " + j + " (Periodo " + i + ") / Fecha=<" + it2.next() + ">", 7);
					j++;
				}
				i++;
			}

			ClsLogging.writeFileLog("FIN CALENDARIO (" + "IDINSTITUCION=" + this.idInstitucion + ", IDGUARDIA="
					+ this.idGuardia + ", IDCALENDARIO=" + this.idCalendarioGuardias + "):", 7);
		} catch (Exception e) {
			throw new ClsExceptions(e, "ERROR al pintar los datos del Calendario SJCS");
		}
	} // pintarCalendarioSJCS()
	
	/**
	 * @param letrado
	 * @param periodoDiasGuardia
	 * @return true si NO hay incompatibilidad de guardias
	 * @throws ClsExceptions
	 */
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

	/**
	 * @param letrado
	 * @param periodoDiasGuardia
	 * @return true si hay suficiente separacion de dias de guardia
	 * @throws ClsExceptions
	 */
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
	
	/**
	 * Almacena para un letrado la guardia del mismo con los registros correspondientes a sus dias de guardia.
	 * @param letrado
	 * @param periodoDiasGuardia
	 * @param lDiasASeparar
	 * @throws ClsExceptions
	 */
	public void almacenarAsignacionGuardiaLetrado(LetradoGuardia letrado,
			ArrayList periodoDiasGuardia,
			List lDiasASeparar) throws ClsExceptions
	{
		ArrayList arrayLetrados = new ArrayList();
		// Anhado el letrado
		arrayLetrados.add(letrado);
		almacenarAsignacionGuardia(this.idCalendarioGuardias, arrayLetrados, periodoDiasGuardia, lDiasASeparar, UtilidadesString.getMensajeIdioma(
				this.getUsrBean(), "gratuita.literal.letrado.refuerzo"));
	}
	
	/**
	 * Almacena para una lista de letrados las guardias con los registros correspondientes a sus dias de guardia.
	 * @param arrayLetrados
	 * @param periodoDiasGuardia
	 * @param lDiasASeparar
	 * @param mensaje
	 * @throws ClsExceptions
	 */
	private void almacenarAsignacionGuardia(Integer idCalendarioGuardias, ArrayList arrayLetrados, ArrayList periodoDiasGuardia,List lDiasASeparar, String mensaje) throws ClsExceptions {
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
					beanCabeceraGuardias.setIdCalendario(idCalendarioGuardias);
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

					beanCabeceraGuardias.setFechaAlta("SYSDATE");
					beanCabeceraGuardias.setPosicion(letrado.getPosicion());
					admCabeceraGuardias.insert(beanCabeceraGuardias);


					//Paso2: inserto un registro por dia de guardia en cada guardia:
					iter = alDiasPeriodo.iterator();
					while (iter.hasNext()) {
						fechaPeriodo = (String)iter.next();

						beanGuardiasColegiado = new ScsGuardiasColegiadoBean();
						beanGuardiasColegiado.setIdInstitucion(letrado.getIdInstitucion());
						beanGuardiasColegiado.setIdTurno(letrado.getIdTurno());
						beanGuardiasColegiado.setIdGuardia(letrado.getIdGuardia());
						beanGuardiasColegiado.setIdCalendarioGuardias(idCalendarioGuardias);
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
	
	private LetradoGuardia getSiguienteLetrado(List<LetradoGuardia> alCompensaciones,
			List alLetradosOrdenados,
			Puntero punteroLetrado,
			ArrayList diasGuardia,
			HashMap<Long, ArrayList<LetradoGuardia>> hmPersonasConSaltos) throws SIGAException, ClsExceptions
	{
		LetradoGuardia letradoGuardia, auxLetradoSeleccionado;

		letradoGuardia = null;

		// recorriendo compensaciones
		if (alCompensaciones != null && alCompensaciones.size() > 0) {

			Iterator<LetradoGuardia> iterador = alCompensaciones.iterator();
			while (iterador.hasNext()) {
				auxLetradoSeleccionado = (LetradoGuardia) iterador.next();
				log.addLog(new String[] {"Probando Letrado Compensado", auxLetradoSeleccionado.toString()});
				// vale
				if (comprobarRestriccionesLetradoCompensado(auxLetradoSeleccionado, diasGuardia, iterador, null)) {
					letradoGuardia = auxLetradoSeleccionado;
					break;
				}
				else
					log.addLog(new String[] {"Letrado Compensado no valido", auxLetradoSeleccionado.toString()});
			}
		}
		if (letradoGuardia != null)
			return letradoGuardia;

		// recorriendo la cola
		if (alLetradosOrdenados != null && alLetradosOrdenados.size() > 0) {

			int fin = punteroLetrado.getValor();
			do {
				auxLetradoSeleccionado = (LetradoGuardia) alLetradosOrdenados.get(punteroLetrado.getValor());
				log.addLog(new String[] {"Probando Letrado", auxLetradoSeleccionado.toString()});

				// vale
				if (comprobarRestriccionesLetradoCola(auxLetradoSeleccionado, diasGuardia, hmPersonasConSaltos))
					letradoGuardia = auxLetradoSeleccionado;
				else
					log.addLog(new String[] {"Letrado no valido", auxLetradoSeleccionado.toString()});

				// obteniendo siguiente en la cola
				if (punteroLetrado.getValor() < alLetradosOrdenados.size() - 1)
					punteroLetrado.setValor(punteroLetrado.getValor() + 1);
				else
					punteroLetrado.setValor(0); // como es una cola circular hay que volver al principio

			} while (letradoGuardia == null && fin != punteroLetrado.getValor());
		}

		if (letradoGuardia != null)
			return letradoGuardia;
		else
			return null;
	} // getSiguienteLetrado()

	/**
	 * Obtiene el siguiente grupo de letrados valido para asignar en una guardia
	 * 
	 * @param alCompensaciones
	 * @param alLetradosOrdenados
	 * @param punteroLetrado
	 * @param diasGuardia
	 * @param alSaltos
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException 
	 */
	private ArrayList<LetradoGuardia> getSiguienteGrupo(ArrayList<ScsSaltoCompensacionGrupoBean> alCompensaciones,
			ArrayList<LetradoGuardia> alLetradosOrdenados,
			Puntero punteroLetrado,
			ArrayList<String> diasGuardia,
			HashMap<Long, ArrayList<LetradoGuardia>> hmPersonasConSaltos) throws ClsExceptions, SIGAException
	{
		// Variables
		ScsSaltoCompensacionGrupoAdm saltosCompenGruposAdm = new ScsSaltoCompensacionGrupoAdm(this.usrBean);
		ArrayList<LetradoGuardia> grupoLetrados;
		ScsSaltoCompensacionGrupoBean compensacion = null;
		boolean grupoValido;
		int restricciones;

		// obteniendo grupo de entre los compensados
		grupoValido = false;
		Iterator<ScsSaltoCompensacionGrupoBean> iterador = alCompensaciones.iterator();
		while (iterador.hasNext() && !grupoValido) {
			compensacion = iterador.next();
			
			// comprobando cada letrado del grupo
			log.addLog(new String[] {"Probando Grupo Compensado", compensacion.getLetrados().toString()});
			grupoValido = true;
			for (LetradoGuardia lg : compensacion.getLetrados()) {
				if (!comprobarRestriccionesLetradoCompensado(lg, diasGuardia, null,
						compensacion.getIdSaltoCompensacionGrupo().toString()))
					grupoValido = false;
				if (!grupoValido)
					break; // salir de las comprobaciones por letrado si uno de ellos no es valido
			}
			if (!grupoValido)
				log.addLog(new String[] {"Grupo Compensado no valido", compensacion.getLetrados().toString()});
		}
		// si bien, cumplir la compensacion de grupo:
		if (grupoValido) {
			log.addLog(new String[] {"Compensacion de grupo cumplida"});
			String motivoCumplimiento = "Compensacion de grupo cumplida";
			saltosCompenGruposAdm.cumplirSaltoCompensacion(compensacion.getIdSaltoCompensacionGrupo().toString(),
					diasGuardia.get(0), motivoCumplimiento, idInstitucion.toString(), idTurno.toString(), 
					idGuardia.toString(), idCalendarioGuardias.toString());
			return compensacion.getLetrados();
		}
		
		// obteniendo grupo de la cola
		grupoLetrados = getGrupoLetrados(alLetradosOrdenados, punteroLetrado);
		while (grupoLetrados != null && !grupoValido) {
			
			// comprobando cada letrado del grupo
			log.addLog(new String[] {"Probando Grupo", grupoLetrados.toString()});
			grupoValido = true;
			for (LetradoGuardia lg : grupoLetrados) {
				if (!comprobarRestriccionesLetradoCola(lg, diasGuardia, hmPersonasConSaltos))
					grupoValido = false;
				if (!grupoValido)
					break; // salir de las comprobaciones por letrado si uno de ellos no es valido
			}
			if (!grupoValido) {
				log.addLog(new String[] {"Grupo no valido", grupoLetrados.toString()});
				grupoLetrados = getGrupoLetrados(alLetradosOrdenados, punteroLetrado);
			}
		}

		if (grupoValido)
			return grupoLetrados;
		else
			return null;
	} // getSiguienteGrupo()

	/**
	 * A partir de una lista de letrados, obtiene un grupo
	 * 
	 * @param alLetradosOrdenados
	 * @param punteroLetrado
	 * @return
	 * @throws ClsExceptions
	 */
	private ArrayList<LetradoGuardia> getGrupoLetrados(ArrayList<LetradoGuardia> alLetradosOrdenados,
			Puntero punteroLetrado) throws ClsExceptions
	{
		// Variables
		LetradoGuardia letrado;
		ArrayList<LetradoGuardia> grupoLetrados;
		int numeroGrupo;

		// controlando que la lista este rellena
		if (alLetradosOrdenados == null || alLetradosOrdenados.size() == 0)
			return null;

		// obteniendo primer componente del grupo
		letrado = alLetradosOrdenados.get(punteroLetrado.getValor());
		numeroGrupo = letrado.getGrupo();

		grupoLetrados = new ArrayList<LetradoGuardia>();
		do {
			// anyadiendo componente al grupo
			grupoLetrados.add(letrado);

			// obteniendo siguiente en la cola
			punteroLetrado.incValor();
			letrado = alLetradosOrdenados.get(punteroLetrado.getValor());
		} while (letrado != null && letrado.getGrupo() == numeroGrupo);

		if (grupoLetrados.size() == 0)
			return null;
		else
			return grupoLetrados;
	} // getGrupoLetrados()
	
	private boolean comprobarRestriccionesLetradoCompensado(LetradoGuardia letradoGuardia,
			ArrayList<String> diasGuardia,
			Iterator<LetradoGuardia> iteCompensaciones,
			String idSaltoCompensacionGrupo) throws ClsExceptions, SIGAException
	{
		// Controles
		ScsSaltoCompensacionGrupoAdm saltosCompenGruposAdm = new ScsSaltoCompensacionGrupoAdm(this.usrBean);

		// si esta de vacaciones, ...
		if (isLetradoBajaTemporal(diasGuardia, letradoGuardia)) {
			log.addLog(new String[] {"Encontrado Baja temporal", letradoGuardia.toString(), diasGuardia.toString()});
			if (letradoGuardia.getGrupo() == null || letradoGuardia.getGrupo().toString().equals(""))
				// ... crear un salto cumplido (como si fuera un log)
				insertarNuevoSaltoBT(letradoGuardia, diasGuardia, "Salto por BT");
			else
				// ... crear un salto cumplido (como si fuera un log)
				saltosCompenGruposAdm.crearSaltoBT(letradoGuardia.getGrupo().toString(), diasGuardia.get(0), "",
						idInstitucion.toString(), idTurno.toString(), idGuardia.toString(), 
						idCalendarioGuardias.toString(), letradoGuardia.getBajaTemporal());
			return false; // y no seleccionar
		}

		// si hay incompatibilidad
		if (isIncompatible(letradoGuardia, diasGuardia)) {
			log.addLog(new String[] {"Encontrado Incompatibilidad", letradoGuardia.toString(), diasGuardia.toString()});
			return false; // no seleccionar
		}

		// cumpliendo compensacion
		if (letradoGuardia.getGrupo() == null || letradoGuardia.getGrupo().toString().equals("")) {
			log.addLog(new String[] {"Compensacion cumplida", letradoGuardia.toString()});
			cumplirSaltoCompensacion(letradoGuardia, diasGuardia, ClsConstants.COMPENSACIONES, " - Compensacion cumplida");
			iteCompensaciones.remove();
		}
		else
			// nada, hay que cumplir la compensacion cuando todos los letrados esten comprobados

		// una vez comprobado todo, se selecciona a este letrado
		log.addLog(new String[] {"Letrado OK", letradoGuardia.toString()});
		return true;
	} // comprobarRestriccionesLetradoCompensado()
	
	private boolean comprobarRestriccionesLetradoCola(LetradoGuardia letradoGuardia,
			ArrayList<String> diasGuardia,
			HashMap<Long, ArrayList<LetradoGuardia>> hmPersonasConSaltos) throws ClsExceptions, SIGAException
	{
		// Controles
		ScsSaltoCompensacionGrupoAdm saltosCompenGruposAdm = new ScsSaltoCompensacionGrupoAdm(this.usrBean);
		ScsSaltosCompensacionesAdm scsSaltosCompensacionesAdm = new ScsSaltosCompensacionesAdm(this.usrBean);

		// si esta de vacaciones, ...
		if (isLetradoBajaTemporal(diasGuardia, letradoGuardia)) {
			log.addLog(new String[] {"Encontrado Baja temporal", letradoGuardia.toString(), diasGuardia.toString()});
			if (letradoGuardia.getGrupo() == null || letradoGuardia.getGrupo().toString().equals(""))
				// ... crear un salto cumplido (como si fuera un log)
				insertarNuevoSaltoBT(letradoGuardia, diasGuardia, "Salto por BT");
			else
				// ... crear un salto cumplido (como si fuera un log)
				saltosCompenGruposAdm.crearSaltoBT(letradoGuardia.getGrupo().toString(), diasGuardia.get(0), "",
						idInstitucion.toString(), idTurno.toString(), idGuardia.toString(), 
						idCalendarioGuardias.toString(), letradoGuardia.getBajaTemporal());

			return false; // y no seleccionar
		}

		// si tiene saltos, ...
		List<LetradoGuardia> alSaltos;
		if (letradoGuardia.getGrupo() == null || letradoGuardia.getGrupo().toString().equals("")) {
			if ((alSaltos = hmPersonasConSaltos.get(letradoGuardia.getIdPersona())) != null) {
				log.addLog(new String[] {"Encontrado Salto", letradoGuardia.toString()});
				
				// ... compensar uno
				cumplirSaltoCompensacion(letradoGuardia, diasGuardia, ClsConstants.SALTOS, " - Salto cumplido");
				alSaltos.remove(0);
				if (alSaltos.size() == 0)
					hmPersonasConSaltos.remove(letradoGuardia.getIdPersona());
				return false; // y no seleccionar
			}
		}
		else if ((alSaltos = hmPersonasConSaltos.get(letradoGuardia.getGrupo())) != null) {
			log.addLog(new String[] {"Encontrado Salto de grupo"});
			String motivoCumplimiento = "Salto de grupo cumplido";
			
			// ... compensar uno
			saltosCompenGruposAdm.cumplirSaltoCompensacion(alSaltos.get(0).getIdSaltoCompensacionGrupo(),
					diasGuardia.get(0), motivoCumplimiento, idInstitucion.toString(), idTurno.toString(),
					idGuardia.toString(), idCalendarioGuardias.toString());
			alSaltos.remove(0);
			if (alSaltos.size() == 0)
				hmPersonasConSaltos.remove(letradoGuardia.getGrupo());
			return false; // y no seleccionar
		}

		// si hay incompatibilidad, ...
		if (isIncompatible(letradoGuardia, diasGuardia)) {
			log.addLog(new String[] {"Encontrado Incompatibilidad", letradoGuardia.toString(), diasGuardia.toString()});
			if (letradoGuardia.getGrupo() == null || letradoGuardia.getGrupo().toString().equals("")) {
				// ... crear compensacion
				insertarNuevoSaltoCompensacion(letradoGuardia, diasGuardia, ClsConstants.COMPENSACIONES,
						"Compensacion por Incompatibilidad");
			}
			else {
				// ... crear compensacion
				saltosCompenGruposAdm.crearSaltoCompensacion(letradoGuardia.getGrupo().toString(), 
						diasGuardia.get(0), "", idInstitucion.toString(), idTurno.toString(), 
						idGuardia.toString(), idCalendarioGuardias.toString(), ClsConstants.COMPENSACIONES);
			}
			return false; // y no seleccionar
		}

		// una vez comprobado todo, se selecciona a este letrado
		log.addLog(new String[] {"Letrado OK", letradoGuardia.toString()});
		return true;
	} // comprobarRestriccionesLetradoCola()
	
	/**
	 * Si el letrado esta de baja seteamos la baja temporarl en el objeto LetradoGuardia, para luego insertar un salto
	 */
	private boolean isLetradoBajaTemporal(ArrayList diasGuardia, LetradoGuardia letradoGuardia) {
		boolean isLetradoBaja = false;
		CenBajasTemporalesBean bajaTemporal;
		
		for (int j = 0; j < diasGuardia.size(); j++) {
			String fechaPeriodo = (String) diasGuardia.get(j);
			if (letradoGuardia.getBajasTemporales().containsKey(fechaPeriodo)) {
				bajaTemporal = letradoGuardia.getBajasTemporales().get(fechaPeriodo);
				letradoGuardia.setBajaTemporal(bajaTemporal);
				isLetradoBaja = true;
				break;
			}
		}
		
		return isLetradoBaja;
	}
	private boolean isIncompatible(LetradoGuardia letradoGuardia, ArrayList diasGuardia)
			throws ClsExceptions
	{
		if (hayIncompatibilidadGuardias(letradoGuardia, diasGuardia))
			return true;
		if (!haySeparacionDias(letradoGuardia, diasGuardia))
			return true;

		return false;
	}
	private void insertarNuevoSaltoBT(LetradoGuardia letradoGuardia,
									  ArrayList diasGuardia,
									  String motivo) throws ClsExceptions
	{
		String saltoOCompensacion = ClsConstants.SALTOS;
		ScsSaltosCompensacionesBean saltoCompensacion = new ScsSaltosCompensacionesBean(
				idInstitucion, idTurno, idGuardia, idCalendarioGuardias,
				letradoGuardia.getIdPersona(), saltoOCompensacion, "sysdate");
		
		saltoCompensacion.setFechaCumplimiento(GstDate.getApplicationFormatDate("", (String) diasGuardia.get(0)) );
		saltoCompensacion.setIdCalendarioGuardias(idCalendarioGuardias);
		
		// obteniendo motivo
		StringBuffer descripcion = new StringBuffer();
		CenBajasTemporalesBean bajaTemporalBean = letradoGuardia.getBajaTemporal();
		if(bajaTemporalBean.getTipo().equals(CenBajasTemporalesBean.TIPO_COD_VACACION))
			descripcion.append(UtilidadesString.getMensajeIdioma(this.usrBean, CenBajasTemporalesBean.TIPO_DESC_VACACION));
		else if(bajaTemporalBean.getTipo().equals(CenBajasTemporalesBean.TIPO_COD_BAJA))
			descripcion.append(UtilidadesString.getMensajeIdioma(this.usrBean, CenBajasTemporalesBean.TIPO_DESC_BAJA));
		descripcion.append(" ");
		descripcion.append(bajaTemporalBean.getDescripcion());
		saltoCompensacion.setMotivos(motivo + ": " + descripcion);
		
		ScsSaltosCompensacionesAdm scsSaltosCompensacionesAdm = new ScsSaltosCompensacionesAdm(this.usrBean);
		scsSaltosCompensacionesAdm.insertarSaltoCompensacion(saltoCompensacion);
	}
	private void insertarNuevoSaltoCompensacion(LetradoGuardia letradoGuardia,
												ArrayList diasGuardia,
												String saltoOCompensacion,
												String motivo) throws ClsExceptions
	{
		ScsSaltosCompensacionesBean saltoCompensacion = new ScsSaltosCompensacionesBean(
				idInstitucion, idTurno, idGuardia, idCalendarioGuardias,
				letradoGuardia.getIdPersona(), saltoOCompensacion, "sysdate");

		saltoCompensacion.setMotivos(motivo);
		
		ScsSaltosCompensacionesAdm scsSaltosCompensacionesAdm = new ScsSaltosCompensacionesAdm(this.usrBean);
		scsSaltosCompensacionesAdm.insertarSaltoCompensacion(saltoCompensacion);
	}
	private void cumplirSaltoCompensacion(LetradoGuardia letradoGuardia,
			   							  ArrayList diasGuardia,
			   							  String saltoOCompensacion,
			   							  String motivo) throws ClsExceptions
	{
		ScsSaltosCompensacionesBean saltoCompensacion = new ScsSaltosCompensacionesBean(
				idInstitucion, idTurno, idGuardia,
				letradoGuardia.getIdPersona(), saltoOCompensacion);
		
		saltoCompensacion.setFechaCumplimiento((String) diasGuardia.get(0));
		//saltoCompensacion.setFechaCumplimiento(GstDate.getApplicationFormatDate("", (String) diasGuardia.get(0)) );
		saltoCompensacion.setIdCalendarioGuardias(idCalendarioGuardias);
		saltoCompensacion.setMotivos(motivo);
		
		ScsSaltosCompensacionesAdm scsSaltosCompensacionesAdm = new ScsSaltosCompensacionesAdm(this.usrBean);
		scsSaltosCompensacionesAdm.marcarSaltoCompensacionGuardia(saltoCompensacion);
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
	
	/**
	 * Inserta en el arraylist arrayPeriodosDiasGuardiaSJCS los periodos de dias de guardia calculados por el CGAE
	 * Nota: cada periodo es un arraylist de fechas (String en formato de fecha corto DD/MM/YYYY).
	 * @throws ClsExceptions
	 */
	public void calcularMatrizPeriodosDiasGuardia() throws ClsExceptions
	{
		this.arrayPeriodosDiasGuardiaSJCS.clear();
		ArrayList<ArrayList<String>> listaDiasPeriodos = this.obtenerPeriodosGuardia();
		if (listaDiasPeriodos != null && !listaDiasPeriodos.isEmpty())
			this.arrayPeriodosDiasGuardiaSJCS.addAll(listaDiasPeriodos);
	} // calcularMatrizPeriodosDiasGuardia()
	
	/**
	 * Inserta en el arraylist arrayPeriodosDiasGuardiaSJCS los periodos de dias de guardia calculados por el CGAE
	 * Nota: cada periodo es un arraylist de fechas (String en formato de fecha corto DD/MM/YYYY).
	 * @throws ClsExceptions
	 */
	public void calcularMatrizPeriodosDiasGuardiaAutomatico() throws ClsExceptions
	{
		// generando calendario normal
		this.arrayPeriodosDiasGuardiaSJCS = new ArrayList();
		ArrayList<ArrayList<String>> listaDiasPeriodos = this.obtenerPeriodosGuardia();
		if (listaDiasPeriodos != null && !listaDiasPeriodos.isEmpty())
			this.arrayPeriodosDiasGuardiaSJCS.addAll(listaDiasPeriodos);
		else
			return;
		
		// En el caso de que existan guardias vinculadas, se ha de generar un periodo mas:
		// Pero no sirve generar un calendario ampliado (que termine mas tarde), 
		// porque no hay forma de saber cual es el ultimo dia del calendario real
		// Asi que la unica forma es: 
		// generar el calendario real (antes) y (ahora) uno ampliado para obtener el periodo de mas
		if (this.calendariosVinculados != null) {
			// obteniendo una fechaFin suficientemente posterior
			try {
				SimpleDateFormat sdf = new SimpleDateFormat (ClsConstants.DATE_FORMAT_JAVA);
				SimpleDateFormat miFormatoFecha = new SimpleDateFormat (ClsConstants.DATE_FORMAT_SHORT_SPANISH);
				Date dFechaInicio = sdf.parse (this.fechaInicio);
				ArrayList<String> ultimoPeriodo = listaDiasPeriodos.get(listaDiasPeriodos.size()-1);
				Date dFechaFin = miFormatoFecha.parse (ultimoPeriodo.get(ultimoPeriodo.size()-1));
				
				Date nuevaFechaFin = new Date(dFechaFin.getTime() + dFechaFin.getTime() - dFechaInicio.getTime());
				Calendar cal = Calendar.getInstance();
				cal.setTime(nuevaFechaFin);
				cal.add(Calendar.DATE, 1);
				this.fechaFin = sdf.format(cal.getTime());
			} catch (ParseException e) {
				throw new ClsExceptions(e, "Error al parsear las fechas del calendario");
			}
			
			// generando el calendario ampliado
			ArrayList<ArrayList<String>> listaDiasPeriodosAmpliado = this.obtenerPeriodosGuardia();
			
			// anyadiendo el primer periodo del calendario ampliado
			this.arrayPeriodosDiasGuardiaSJCS.add(listaDiasPeriodosAmpliado.get(listaDiasPeriodos.size()));
		}
	} // calcularMatrizPeriodosDiasGuardiaAutomatico()
	
	private ArrayList<ArrayList<String>> obtenerPeriodosGuardia() throws ClsExceptions
	{
		// Variables
		ArrayList arrayDiasGuardiaCGAE;
		PeriodoEfectivo periodoEfectivo;
		ArrayList<String> arrayDias;
		Iterator iter;
		Date fecha;
		String sFecha;
		SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");
		
		ArrayList<ArrayList<String>> listaFinal = new ArrayList<ArrayList<String>>();

		try {
			// generando la matriz de periodos
			CalendarioAutomatico calendarioAutomatico = new CalendarioAutomatico(this);
			arrayDiasGuardiaCGAE = calendarioAutomatico.obtenerMatrizDiasGuardia();

			// recorriendo lista de periodos para anhadir en cada periodo
			// las fechas de dias de guardia en formato corto
			for (int i = 0; i < arrayDiasGuardiaCGAE.size(); i++) {
				periodoEfectivo = (PeriodoEfectivo) arrayDiasGuardiaCGAE.get(i);

				// obteniendo los dis por cada periodo
				arrayDias = new ArrayList<String>();
				iter = periodoEfectivo.iterator();
				while (iter.hasNext()) {
					fecha = (Date) iter.next();
					sFecha = datef.format(fecha);
					arrayDias.add(sFecha);
				}

				// insertando lista de dias (en formato corto) para el generador posterior
				// Nota: no se insertan los arrays de periodos vacios que si guarda el CGAE
				if (!arrayDias.isEmpty())
					listaFinal.add(arrayDias);
			}
			
			return listaFinal;
			
		} catch (Exception e) {
			throw new ClsExceptions(e, "Excepcion al calcular la matriz de periodos de dias de guardias.");
		}
	} // obtenerPeriodosGuardia()

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
	
	/**
	 * Pinta el resultado del metodo "calcularMatrizLetradosGuardia".
	 * @param salidaError
	 */
	public void pintarSalidaCalcularMatrizLetradosGuardia (int salidaError) {		
		switch (salidaError) {
			case 0: ClsLogging.writeFileLog("> SALIDA=OK",7); break;
			case 1: ClsLogging.writeFileLog("> SALIDA=ERROR, NO HAY SUFICIENTES LETRADOS",7); break;
			case 2: ClsLogging.writeFileLog("> SALIDA=ERROR, HAY INCOMPATIBILIDAD DE GUARDIAS",7); break;
			case 3: ClsLogging.writeFileLog("> SALIDA=ERROR, NO HAY SEPARACION SUFIENTE EN LOS DIAS DE GUARDIAS",7); break;
			case 4: ClsLogging.writeFileLog("> SALIDA=ERROR, EXCEPCION",7); break;
		}
	}

	/**
	 * Genera las cabeceras de guardias del calendario, a partir de la lista de dias/periodos
	 * Cuando la guardia no es por grupos
	 * Nota: previamente se ha calculado mediante el metodo "calcularMatrizPeriodosDiasGuardia()"
	 */
	public void calcularMatrizLetradosGuardia(List lDiasASeparar) throws SIGAException, ClsExceptions
	{
		final int INI_POSICION = 0;
		
		// Controles generales
		ScsSaltosCompensacionesAdm scAdm = new ScsSaltosCompensacionesAdm(this.usrBean);
		ScsGuardiasTurnoAdm guardiaAdm = new ScsGuardiasTurnoAdm(this.usrBean);

		// Variables generales
		ArrayList<String> diasGuardia, primerPeriodo, segundoPeriodo; // Periodo o dia de guardia para rellenar con letrado
		int numeroLetradosGuardia; // Numero de letrados necesarios para cada periodo
		HashMap<Long, ArrayList<LetradoGuardia>> hmPersonasConSaltos; // Lista de saltos
		List<LetradoGuardia> alCompensaciones; // Lista de compensaciones
		ArrayList<LetradoGuardia> alLetradosOrdenados; // Cola de letrados en la guardia
		Puntero punteroListaLetrados;
		int posicion;
		ArrayList<LetradoGuardia> alLetradosInsertar; // Lista de letrados obtenidos para cada periodo
		LetradoGuardia unLetrado;

		try {
			// obteniendo saltos
			hmPersonasConSaltos = scAdm.getSaltos(this.idInstitucion, this.idTurno, this.idGuardia);

			// obteniendo numero de letrados necesarios para cada periodo
			numeroLetradosGuardia = this.beanGuardiasTurno.getNumeroLetradosGuardia().intValue();
			log.addLog(new String[] {"Num. Letrados", Integer.toString(numeroLetradosGuardia)});

			// Si hay guardias vinculadas, hay que mirar dos periodos a la vez, 
			// por lo que se comienza con uno ya en memoria
			int inicial;
			primerPeriodo = (ArrayList<String>) this.arrayPeriodosDiasGuardiaSJCS.get(0);
			segundoPeriodo = null;
			if (this.calendariosVinculados == null)
				inicial = 0;
			else {
				log.addLog(new String[] {"Guardias vinculadas", this.calendariosVinculados.toString()});
				inicial = 1;
			}
			
			// Para cada dia o conjunto de dias:
			for (int i = inicial; i < this.arrayPeriodosDiasGuardiaSJCS.size(); i++) {
				posicion = INI_POSICION;
				// obteniendo conjunto de dias
				// Nota: cada periodo es un arraylist de fechas (String en formato de fecha corto DD/MM/YYYY)
				if (this.calendariosVinculados == null) {
					diasGuardia = (ArrayList<String>) this.arrayPeriodosDiasGuardiaSJCS.get(i);
				} // Si hay guardias vinculadas, hay que mirar dos periodos a la vez
				else {
					segundoPeriodo = (ArrayList<String>) this.arrayPeriodosDiasGuardiaSJCS.get(i);
					diasGuardia = new ArrayList<String>();
					diasGuardia.addAll(primerPeriodo);
					diasGuardia.addAll(segundoPeriodo);
					primerPeriodo = segundoPeriodo;
				}
				log.addLog(new String[] {"Dias", diasGuardia.toString()});

				// obteniendo cola de letrados
				punteroListaLetrados = new Puntero();
				alLetradosOrdenados = InscripcionGuardia.getColaGuardia(idInstitucion, idTurno, idGuardia,
						(String) diasGuardia.get(0), (String) diasGuardia.get(diasGuardia.size() - 1), usrBean);
				log.addLog(new String[] {"Cola", alLetradosOrdenados.toString()});

				if (alLetradosOrdenados == null || alLetradosOrdenados.size() == 0)
					throw new SIGAException("No existe cola de letrados de guardia");

				// obteniendo las compensaciones. Se obtienen dentro de este
				// bucle, ya que si hay incompatibilidades se añade una compensacion
				alCompensaciones = scAdm.getCompensaciones(this.idInstitucion, this.idTurno, this.idGuardia);
				log.addLog(new String[] {"Compensaciones", alCompensaciones.toString()});
				log.addLog(new String[] {"Saltos", hmPersonasConSaltos.toString()});

				// Para cada plaza que hay que ocupar en dia/conjunto de dias:
				int letradosInsertados = 0;
				for (int k = 0; k < numeroLetradosGuardia; k++) {
					// obteniendo el letrado a asignar.
					// ATENCION: este metodo es el nucleo del proceso
					LetradoGuardia letradoGuardia = getSiguienteLetrado(alCompensaciones, alLetradosOrdenados,
							punteroListaLetrados, diasGuardia, hmPersonasConSaltos);

					// guardando la asignacion de guardia si se encontro letrado
					// hay que hacerlo aqui para no repetir letrado el mismo dia
					if (letradoGuardia != null) {
						log.addLog(new String[] {"Letrado seleccionado", letradoGuardia.toString()});
						
						alLetradosInsertar = new ArrayList<LetradoGuardia>();
						letradoGuardia.setPeriodoGuardias(diasGuardia);
						LetradoGuardia letradoGuardiaClone = (LetradoGuardia) letradoGuardia.clone();
						letradoGuardiaClone.setPosicion(posicion);
						posicion++;
						alLetradosInsertar.add(letradoGuardiaClone);
						this.arrayPeriodosLetradosSJCS.add(alLetradosInsertar);
						
						// guardando las guardias en BD
						if (this.calendariosVinculados == null) {
							this.almacenarAsignacionGuardia(this.idCalendarioGuardias, alLetradosInsertar, diasGuardia, lDiasASeparar,
									UtilidadesString.getMensajeIdioma(this.usrBean,
											"gratuita.literal.comentario.sustitucion"));
						}
						else {
							// guardando la principal
							this.almacenarAsignacionGuardia(this.idCalendarioGuardias, alLetradosInsertar, primerPeriodo, lDiasASeparar,
									UtilidadesString.getMensajeIdioma(this.usrBean,
											"gratuita.literal.comentario.sustitucion"));

							// guardando para cada una de las vinculadas
							for (ScsCalendarioGuardiasBean calendario : this.calendariosVinculados) {
								// modificando la guardia y calendario en el que se insertaran las guardias
								for (LetradoGuardia lg : alLetradosInsertar) {
									lg.setIdInstitucion(calendario.getIdInstitucion());
									lg.setIdTurno(calendario.getIdTurno());
									lg.setIdGuardia(calendario.getIdGuardia());
								}
								
								// guardando en BD
								this.almacenarAsignacionGuardia(calendario.getIdCalendarioGuardias(), alLetradosInsertar, segundoPeriodo, lDiasASeparar,
										UtilidadesString.getMensajeIdioma(this.usrBean,
												"gratuita.literal.comentario.sustitucion"));
								log.addLog(new String[] {UtilidadesString.getMensajeIdioma(this.usrBean,
										"gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorLetradosSuficientes")});
							}
						}
						
						letradosInsertados++;
					} else {
						log.addLog(new String[] {UtilidadesString.getMensajeIdioma(this.usrBean,
								"gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorLetradosSuficientes")});
						throw new SIGAException(
								"gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorLetradosSuficientes");
					}
				} // FIN Para cada plaza que hay que ocupar en dia/conjunto de dias

				// controlando que se insertaron tantos letrados como hacian falta
				if (letradosInsertados != numeroLetradosGuardia) {
					log.addLog(new String[] {});
					throw new SIGAException(
							"gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorLetradosSuficientes");
				}

				// actualizando el ultimo letrado en la guardia solo si no es de la lista de compensaciones
				int punteroUltimo = 0;
				if (punteroListaLetrados.getValor() == 0)
					punteroUltimo = alLetradosOrdenados.size() - 1;
				else
					punteroUltimo = punteroListaLetrados.getValor() - 1;

				unLetrado = alLetradosOrdenados.get(punteroUltimo);
				if (unLetrado.getSaltoCompensacion() == null)
					guardiaAdm.cambiarUltimoCola(unLetrado.getIdInstitucion(), unLetrado.getIdTurno(), 
							unLetrado.getIdGuardia(), unLetrado.getIdPersona(), 
							unLetrado.getInscripcionGuardia().getFechaSuscripcion());
			} // FIN Para cada dia o conjunto de dias

		} catch (SIGAException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			this.arrayPeriodosLetradosSJCS = null;
			throw new ClsExceptions("");
		}
	} // calcularMatrizLetradosGuardia()

	/**
	 * Genera las cabeceras de guardias del calendario, a partir de la lista de dias/periodos
	 * Cuando la guardia es por grupos
	 * Nota: previamente se ha calculado mediante el metodo "calcularMatrizPeriodosDiasGuardia()"
	 */
	public void calcularMatrizLetradosGuardiaPorGrupos(List lDiasASeparar, boolean rotacion) throws SIGAException, ClsExceptions
	{
		final int INI_POSICION = 0;
		
		// Controles generales
		ScsSaltoCompensacionGrupoAdm salComAdm = new ScsSaltoCompensacionGrupoAdm(this.usrBean);
		ScsGrupoGuardiaColegiadoAdm gruGuaColAdm = new ScsGrupoGuardiaColegiadoAdm(this.usrBean);
		ScsGuardiasTurnoAdm guardiaAdm = new ScsGuardiasTurnoAdm(this.usrBean);

		// Variables
		ArrayList<String> diasGuardia, primerPeriodo, segundoPeriodo; // Periodo o dia de guardia para rellenar con letrado
		int numeroLetradosGuardia; // Numero de letrados necesarios para cada periodo
		
		ArrayList<ScsSaltoCompensacionGrupoBean> alSaltos; // Lista de saltos
		ArrayList<ScsSaltoCompensacionGrupoBean> alCompensaciones; // Lista de compensaciones
		
		ArrayList<LetradoGuardia> alLetradosOrdenados; // Cola de letrados en la guardia
		int posicion; // Orden de cada componente en la cola y en la lista de guardias generada
		ArrayList<LetradoGuardia> grupoLetrados; // Lista de letrados en el grupo
		ArrayList<LetradoGuardia> alLetradosInsertar; // Lista de letrados obtenidos para cada periodo
		Puntero punteroListaLetrados;
		LetradoGuardia unLetrado;
		
		ScsGrupoGuardiaColegiadoBean beanGrupoLetrado; // Bean para guardar cambios en los grupos
		Hashtable hashGrupoLetrado; // Hash para guardar cambios en los grupos
		
		ArrayList<String> lineaLog; // Linea para escribir en LOG

		
		try {
			// obteniendo saltos
			alSaltos = salComAdm.getSaltosCompensacionesPendientesGuardia(this.idInstitucion, this.idTurno, this.idGuardia, ClsConstants.SALTOS);
			HashMap<Long, ArrayList<LetradoGuardia>> hmGruposConSaltos = new HashMap<Long, ArrayList<LetradoGuardia>>();
			ArrayList<LetradoGuardia> grupoConSaltos;
			for (ScsSaltoCompensacionGrupoBean bean : alSaltos) {
				if ((grupoConSaltos = (ArrayList<LetradoGuardia>) hmGruposConSaltos.get(bean.getIdGrupoGuardia())) == null) {
					grupoConSaltos = new ArrayList<LetradoGuardia>();
					grupoConSaltos.add(bean.getLetrados().get(0)); // se inserta uno de los letrados, que lleva ya el idSaltoCompensacionGrupo
					hmGruposConSaltos.put(bean.getIdGrupoGuardia(), grupoConSaltos);
				}
				else {
					grupoConSaltos.add(bean.getLetrados().get(0)); // se inserta uno de los letrados, que lleva ya el idSaltoCompensacionGrupo
				}
			}

			// obteniendo numero de letrados necesarios para cada periodo
			numeroLetradosGuardia = this.beanGuardiasTurno.getNumeroLetradosGuardia().intValue();
			log.addLog(new String[] {"Num. Letrados", Integer.toString(numeroLetradosGuardia)});

			// Si hay guardias vinculadas, hay que mirar dos periodos a la vez, 
			// por lo que se comienza con uno ya en memoria
			int inicial;
			primerPeriodo = (ArrayList<String>) this.arrayPeriodosDiasGuardiaSJCS.get(0);
			segundoPeriodo = null;
			if (this.calendariosVinculados == null)
				inicial = 0;
			else {
				log.addLog(new String[] {"Guardias vinculadas", this.calendariosVinculados.toString()});
				inicial = 1;
			}
			
			// Para cada dia o conjunto de dias:
			for (int i = inicial; i < this.arrayPeriodosDiasGuardiaSJCS.size(); i++) {
				// obteniendo conjunto de dias
				// Nota: cada periodo es un arraylist de fechas (String en formato de fecha corto DD/MM/YYYY)
				if (this.calendariosVinculados == null) {
					diasGuardia = (ArrayList<String>) this.arrayPeriodosDiasGuardiaSJCS.get(i);
				} // Si hay guardias vinculadas, hay que mirar dos periodos a la vez
				else {
					segundoPeriodo = (ArrayList<String>) this.arrayPeriodosDiasGuardiaSJCS.get(i);
					diasGuardia = new ArrayList<String>();
					diasGuardia.addAll(primerPeriodo);
					diasGuardia.addAll(segundoPeriodo);
					primerPeriodo = segundoPeriodo;
				}
				log.addLog(new String[] {"Dias", diasGuardia.toString()});

				// obteniendo cola de letrados
				punteroListaLetrados = new Puntero();
				alLetradosOrdenados = InscripcionGuardia.getColaGuardia(idInstitucion, idTurno, idGuardia,
						(String) diasGuardia.get(0), (String) diasGuardia.get(diasGuardia.size() - 1), usrBean);
				log.addLog(new String[] {"Cola", alLetradosOrdenados.toString()});

				if (alLetradosOrdenados == null || alLetradosOrdenados.size() == 0)
					throw new SIGAException("No existe cola de letrados de guardia");

				// obteniendo las compensaciones. Se obtienen dentro de este
				// bucle, ya que si hay incompatibilidades se añade una compensacion
				alCompensaciones = salComAdm.getSaltosCompensacionesPendientesGuardia(this.idInstitucion, this.idTurno, this.idGuardia, ClsConstants.COMPENSACIONES);
				log.addLog(new String[] {"Compensaciones", alCompensaciones.toString()});
				log.addLog(new String[] {"Saltos", alSaltos.toString()});

				// buscando grupo que no tenga restricciones (incompatibilidades, bajas temporales, saltos)
				grupoLetrados = getSiguienteGrupo(alCompensaciones, alLetradosOrdenados, punteroListaLetrados, diasGuardia, hmGruposConSaltos);
				if (grupoLetrados == null)
					throw new SIGAException("Todos los grupos tienen restricciones que no permiten generar el calendario");
				log.addLog(new String[] {"Grupo seleccionado", grupoLetrados.toString()});
				
				// comprobando minimo de letrados en la guardia
				if (grupoLetrados.size() < numeroLetradosGuardia)
					log.addLog(new String[] {"Aviso", "El numero de letrados en el grupo es menor que el minimo configurado para la guardia: " + grupoLetrados.size() + " < " + numeroLetradosGuardia});
					
				// guardando ultimo de cola
				if (rotacion)
					unLetrado = grupoLetrados.get(0);
				else
					unLetrado = grupoLetrados.get(grupoLetrados.size()-1);
				if (unLetrado.getSaltoCompensacion() == null)
					guardiaAdm.cambiarUltimoCola(unLetrado.getIdInstitucion(), unLetrado.getIdTurno(), 
							unLetrado.getIdGuardia(), unLetrado.getIdPersona(), 
							unLetrado.getInscripcionGuardia().getFechaSuscripcion());
				
				// metiendo grupo en el periodo de guardia
				alLetradosInsertar = new ArrayList<LetradoGuardia>();
				posicion = INI_POSICION;
				for (LetradoGuardia letrado : grupoLetrados) {
					// insertando letrados
					letrado.setPosicion(posicion);
					letrado.setPeriodoGuardias(diasGuardia);
					alLetradosInsertar.add(letrado);
					
					// rotando grupo
					if (rotacion) {
						if (posicion == INI_POSICION)
							letrado.setOrdenGrupo(grupoLetrados.size());
						else
							letrado.setOrdenGrupo(posicion-1);
						
						hashGrupoLetrado = new Hashtable();
						hashGrupoLetrado.put(ScsGrupoGuardiaColegiadoBean.C_IDGRUPOGUARDIACOLEGIADO, letrado.getIdGrupoGuardiaColegiado());
						beanGrupoLetrado = new ScsGrupoGuardiaColegiadoBean();
						beanGrupoLetrado.setIdGrupoGuardiaColegiado(letrado.getIdGrupoGuardiaColegiado());
						beanGrupoLetrado = (ScsGrupoGuardiaColegiadoBean) gruGuaColAdm.selectByPKForUpdate(hashGrupoLetrado).get(0);
						beanGrupoLetrado.setOrden(letrado.getOrdenGrupo());
						beanGrupoLetrado.setFechaMod("sysdate");
						beanGrupoLetrado.setUsuMod(new Integer(usrBean.getUserName()));
						gruGuaColAdm.update(beanGrupoLetrado);
					}
					
					posicion++;
				}
				this.arrayPeriodosLetradosSJCS.add(alLetradosInsertar);
				
				// guardando las guardias en BD
				if (this.calendariosVinculados == null) {
					this.almacenarAsignacionGuardia(this.idCalendarioGuardias, alLetradosInsertar, diasGuardia, lDiasASeparar,
							UtilidadesString.getMensajeIdioma(this.usrBean,
									"gratuita.literal.comentario.sustitucion"));
				}
				else {
					// guardando la principal
					this.almacenarAsignacionGuardia(this.idCalendarioGuardias, alLetradosInsertar, primerPeriodo, lDiasASeparar,
							UtilidadesString.getMensajeIdioma(this.usrBean,
									"gratuita.literal.comentario.sustitucion"));

					// guardando para cada una de las vinculadas
					for (ScsCalendarioGuardiasBean calendario : this.calendariosVinculados) {
						// modificando la guardia y calendario en el que se insertaran las guardias
						for (LetradoGuardia lg : alLetradosInsertar) {
							lg.setIdInstitucion(calendario.getIdInstitucion());
							lg.setIdTurno(calendario.getIdTurno());
							lg.setIdGuardia(calendario.getIdGuardia());
						}
						
						// guardando en BD
						this.almacenarAsignacionGuardia(calendario.getIdCalendarioGuardias(), alLetradosInsertar, segundoPeriodo, lDiasASeparar,
								UtilidadesString.getMensajeIdioma(this.usrBean,
										"gratuita.literal.comentario.sustitucion"));
						log.addLog(new String[] {"Guardia vinculada", calendario.toString()});
					}
				}
			} // FIN Para cada dia o conjunto de dias

		} catch (SIGAException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			this.arrayPeriodosLetradosSJCS = null;
			throw new ClsExceptions("");
		}
	} // calcularMatrizLetradosGuardiaPorGrupos()
	
	/**
	 * Metodo que genera una array de periodos de de fecha a partir de otro. La restriccion que tenemos para 
	 * partir el array inicial es que contenga dias que se agrupan(que vienen en alDiasAgrupar)
	 * @param alPeriodo
	 * @param alDiasAgrupar
	 * @return
	 * @throws Exception
	 */
	private List getPeriodos(ArrayList alPeriodo, List alDiasAgrupar) throws Exception
	{
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
	
	public class Puntero
	{
		private int valor;
		public Puntero()				{this.valor = 0;}
		public void setValor(int valor)	{this.valor = valor;}
		public int getValor()			{return this.valor;}
		public void incValor()			{this.valor++;}
	}
	
}
