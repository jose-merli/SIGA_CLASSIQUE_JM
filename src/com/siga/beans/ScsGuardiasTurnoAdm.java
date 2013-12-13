
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;
import com.siga.gratuita.vos.VolantesExpressVo;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_GUARDIASTURNO
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 */
public class ScsGuardiasTurnoAdm extends MasterBeanAdministrador
{
	public static final int CAMPOS_LISTAINSCRIPCIONES = 2;
	
	/**
	 * Constructor de la clase. 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsGuardiasTurnoAdm (UsrBean usuario) {
		super (ScsGuardiasTurnoBean.T_NOMBRETABLA, usuario);
	}
	
	/** 
	 * Funcion getCamposBean ()
	 * @return conjunto de datos con los nombres de todos los campos del bean
	 */
	protected String[] getCamposBean ()
	{
		String[] campos = {
				ScsGuardiasTurnoBean.C_IDINSTITUCION,
				ScsGuardiasTurnoBean.C_IDTURNO,
				ScsGuardiasTurnoBean.C_IDGUARDIA,
				ScsGuardiasTurnoBean.C_NOMBRE,
				ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA,
				ScsGuardiasTurnoBean.C_NUMEROSUSTITUTOSGUARDIA,
				ScsGuardiasTurnoBean.C_DIASGUARDIA,
				ScsGuardiasTurnoBean.C_DIASPAGADOS,
				ScsGuardiasTurnoBean.C_VALIDARJUSTIFICACIONES,
				ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS,
				ScsGuardiasTurnoBean.C_NUMEROASISTENCIAS,
				ScsGuardiasTurnoBean.C_NUMEROACTUACIONES,
				ScsGuardiasTurnoBean.C_DESCRIPCION,
				ScsGuardiasTurnoBean.C_DESCRIPCIONFACTURACION,
				ScsGuardiasTurnoBean.C_DESCRIPCIONPAGO,
				ScsGuardiasTurnoBean.C_IDORDENACIONCOLAS,
				ScsGuardiasTurnoBean.C_IDPARTIDAPRESUPUESTARIA,
				ScsGuardiasTurnoBean.C_IDPERSONA_ULTIMO,
				ScsGuardiasTurnoBean.C_IDGRUPOGUARDIACOLEGIADO_ULTIMO,
				ScsGuardiasTurnoBean.C_FECHASUSCRIPCION_ULTIMO,
				ScsGuardiasTurnoBean.C_DIASPERIODO,
				ScsGuardiasTurnoBean.C_TIPODIASPERIODO,
				ScsGuardiasTurnoBean.C_FECHAMODIFICACION,
				ScsGuardiasTurnoBean.C_USUMODIFICACION,
				ScsGuardiasTurnoBean.C_SELECCIONLABORABLES,
				ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS,
				ScsGuardiasTurnoBean.C_IDGUARDIASUSTITUCION,				
				ScsGuardiasTurnoBean.C_IDTURNOSUSTITUCION,
				ScsGuardiasTurnoBean.C_PORGRUPOS,
				ScsGuardiasTurnoBean.C_ROTARCOMPONENTES,
				ScsGuardiasTurnoBean.C_IDINSTITUCIONPRINCIPAL,
				ScsGuardiasTurnoBean.C_IDTURNOPRINCIPAL,
				ScsGuardiasTurnoBean.C_IDGUARDIAPRINCIPAL,
				ScsGuardiasTurnoBean.C_TIPODIASGUARDIA,
				ScsGuardiasTurnoBean.C_IDTIPOGUARDIA
		};
		return campos;
	} //getCamposBean ()
	
	/** 
	 * Funcion getClavesBean ()
	 * @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 */
	protected String[] getClavesBean ()
	{
		String[] campos = {
				ScsGuardiasTurnoBean.C_IDINSTITUCION,
				ScsGuardiasTurnoBean.C_IDTURNO,
				ScsGuardiasTurnoBean.C_IDGUARDIA
		};
		return campos;
	} //getClavesBean ()
	
	/** 
	 * Funcion hashTableToBean (Hashtable hash)
	 * @param hash Hashtable para crear el bean
	 * @return bean con la información de la hashtable
	 */
	public MasterBean hashTableToBean (Hashtable hash)
			throws ClsExceptions
	{
		ScsGuardiasTurnoBean bean = null;
		try
		{
			bean = new ScsGuardiasTurnoBean ();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_IDINSTITUCION));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_IDTURNO));
			bean.setIdGuardia(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_IDGUARDIA));
			bean.setNombre(UtilidadesHash.getString(hash,ScsGuardiasTurnoBean.C_NOMBRE));
			bean.setNumeroLetradosGuardia(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA));
			bean.setNumeroSustitutosGuardia(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA));
			bean.setDiasGuardia(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_DIASGUARDIA));
			bean.setDiasPagados(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_DIASPAGADOS));
			bean.setValidarJustificaciones(UtilidadesHash.getString(hash,ScsGuardiasTurnoBean.C_VALIDARJUSTIFICACIONES));
			bean.setDiasSeparacionGuardia(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS));
			bean.setNumeroAsistencias(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_NUMEROASISTENCIAS));
			bean.setNumeroActuaciones(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_NUMEROACTUACIONES));
			bean.setDescripcion(UtilidadesHash.getString(hash,ScsGuardiasTurnoBean.C_DESCRIPCION));
			bean.setDescripcionFacturacion(UtilidadesHash.getString(hash,ScsGuardiasTurnoBean.C_DESCRIPCIONFACTURACION));
			bean.setDescripcionPago(UtilidadesHash.getString(hash,ScsGuardiasTurnoBean.C_DESCRIPCIONPAGO));
			bean.setIdOrdenacionColas(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_IDORDENACIONCOLAS));
			bean.setIdPartidaPresupuestaria(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_IDPARTIDAPRESUPUESTARIA));
			bean.setIdPersona_Ultimo(UtilidadesHash.getLong(hash, ScsGuardiasTurnoBean.C_IDPERSONA_ULTIMO));
			bean.setIdGrupoGuardiaColegiado_Ultimo(UtilidadesHash.getLong(hash, ScsGuardiasTurnoBean.C_IDGRUPOGUARDIACOLEGIADO_ULTIMO));
			bean.setFechaSuscripcion_Ultimo(UtilidadesHash.getString(hash, ScsGuardiasTurnoBean.C_FECHASUSCRIPCION_ULTIMO));
			bean.setTipodiasGuardia(UtilidadesHash.getString(hash,ScsGuardiasTurnoBean.C_TIPODIASGUARDIA));
			bean.setDiasPeriodo(UtilidadesHash.getInteger(hash,ScsGuardiasTurnoBean.C_DIASPERIODO));
			bean.setTipoDiasPeriodo(UtilidadesHash.getString(hash,ScsGuardiasTurnoBean.C_TIPODIASPERIODO));
			bean.setSeleccionLaborables(UtilidadesHash.getString(hash, ScsGuardiasTurnoBean.C_SELECCIONLABORABLES));			
			bean.setSeleccionFestivos(UtilidadesHash.getString(hash, ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS));
			bean.setIdGuardiaSustitucion(UtilidadesHash.getInteger(hash, ScsGuardiasTurnoBean.C_IDGUARDIASUSTITUCION));
			bean.setIdTurnoSustitucion(UtilidadesHash.getInteger(hash, ScsGuardiasTurnoBean.C_IDTURNOSUSTITUCION));
			bean.setPorGrupos(UtilidadesHash.getString(hash, ScsGuardiasTurnoBean.C_PORGRUPOS));
			bean.setRotarComponentes(UtilidadesHash.getString(hash, ScsGuardiasTurnoBean.C_ROTARCOMPONENTES));
			bean.setIdInstitucionPrincipal(UtilidadesHash.getInteger(hash, ScsGuardiasTurnoBean.C_IDINSTITUCIONPRINCIPAL));
			bean.setIdTurnoPrincipal(UtilidadesHash.getInteger(hash, ScsGuardiasTurnoBean.C_IDTURNOPRINCIPAL));
			bean.setIdGuardiaPrincipal(UtilidadesHash.getInteger(hash, ScsGuardiasTurnoBean.C_IDGUARDIAPRINCIPAL));
			
			bean.setIdTipoGuardiaSeleccionado(UtilidadesHash.getInteger(hash, ScsGuardiasTurnoBean.C_IDTIPOGUARDIA));
			
			
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;	
	} //hashTableToBean ()
	
	/** 
	 * Funcion beanToHashTable (MasterBean bean)
	 * @param bean para crear el hashtable asociado
	 * @return hashtable con la información del bean
	 */
	public Hashtable beanToHashTable (MasterBean bean)
			throws ClsExceptions
	{
		Hashtable hash = null;
		try
		{
			hash = new Hashtable();
			ScsGuardiasTurnoBean b = (ScsGuardiasTurnoBean) bean;
			
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDTURNO, b.getIdTurno());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDGUARDIA, b.getIdGuardia());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA, b.getNumeroLetradosGuardia());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_NUMEROSUSTITUTOSGUARDIA, b.getNumeroSustitutosGuardia());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_DIASGUARDIA, b.getDiasGuardia());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_DIASPAGADOS, b.getDiasPagados());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_VALIDARJUSTIFICACIONES, b.getValidarJustificaciones());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS, b.getDiasSeparacionGuardia());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_NUMEROASISTENCIAS, b.getNumeroAsistencias());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_NUMEROACTUACIONES, b.getNumeroActuaciones());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_DESCRIPCIONFACTURACION, b.getDescripcionFacturacion());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_DESCRIPCIONPAGO, b.getDescripcionPago());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDORDENACIONCOLAS, b.getIdOrdenacionColas());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDPARTIDAPRESUPUESTARIA, b.getIdPartidaPresupuestaria());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDPERSONA_ULTIMO, b.getIdPersona_Ultimo());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDGRUPOGUARDIACOLEGIADO_ULTIMO, b.getIdGrupoGuardiaColegiado_Ultimo());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_FECHASUSCRIPCION_ULTIMO, b.getFechaSuscripcion_Ultimo());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_TIPODIASGUARDIA, b.getTipodiasGuardia());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_DIASPERIODO, b.getDiasPeriodo());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_TIPODIASPERIODO, b.getTipoDiasPeriodo());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_SELECCIONLABORABLES, b.getSeleccionLaborables());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS, b.getSeleccionFestivos());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDGUARDIASUSTITUCION, b.getIdGuardiaSustitucion());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDTURNOSUSTITUCION, b.getIdTurnoSustitucion());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_PORGRUPOS, b.getPorGrupos());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_ROTARCOMPONENTES, b.getRotarComponentes());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDINSTITUCIONPRINCIPAL, b.getIdInstitucionPrincipal());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDTURNOPRINCIPAL, b.getIdTurnoPrincipal());
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDGUARDIAPRINCIPAL, b.getIdGuardiaPrincipal());			
			UtilidadesHash.set(hash, ScsGuardiasTurnoBean.C_IDTIPOGUARDIA, b.getIdTipoGuardiaSeleccionado());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	} //beanToHashTable ()
		
	/**
	 * Funcion getOrdenCampos ()
	 * @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 * que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos ()
	{
		return null;
	}
	
	/** 
	 * Funcion ejecutaSelect(String select)
	 * @param select sentencia "select" sql valida, sin terminar en ";"
	 * @return Vector todos los registros que se seleccionen 
	 * en BBDD debido a la ejecucion de la sentencia select
	 */
	public Vector ejecutaSelect (String select)
			throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try
		{ 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	} //ejecutaSelect ()
	
	/**
	 * Devuelve los posibles campos que queremos recuperar desde el select
	 * Los posibles valores son los 3 posibles select que se le pedirán a esta clase,
	 * para rellenar las tablas de las páginas "listarGuardias.jsp" y "listarGuardiasTurnos.jsp",
	 * 
	 * Modificado por david.sanchezp para incluir la opcion 4
	 * Modificado por adrian.ayala para incluir nueva configuracion de tipos de dias
	 * 
	 * @param valor int selector de los campos a mostrar
	 * @return String campos que recupera desde la select
	 */
	public String getCamposTabla (int valor)
	{
		String campos = "";
		switch (valor) {
			case 1:
				campos =
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NOMBRE+" GUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDGUARDIA+" IDGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTURNO+" IDTURNO,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DESCRIPCION+" DESCRIPCION,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDGUARDIA+" IDGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" IDINSTITUCION,"+
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_GUARDIAS+" OBLIGATORIEDAD,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASGUARDIA+" TIPODIASGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DIASGUARDIA+" DURACION,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA+" NUMEROLETRADOS,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NUMEROSUSTITUTOSGUARDIA+" NUMEROSUSTITUTOS,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DIASPAGADOS+" DIASPAGADOS,"+
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_VALIDARJUSTIFICACIONES+" VALIDARJUSTIFICACIONES,"+
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_VALIDARINSCRIPCIONES+" VALIDARINSCRIPCIONES,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASPERIODO+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONLABORABLES+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTIPOGUARDIA+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_PORGRUPOS+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS;
				break;
				
			case CAMPOS_LISTAINSCRIPCIONES:
				campos =
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_NOMBRE+" TURNO,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDGUARDIA+" IDGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTURNO+" IDTURNO,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NOMBRE+" GUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDINSTITUCION+" IDINSTITUCION,"+
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_GUARDIAS+" OBLIGATORIEDAD,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASGUARDIA+" TIPODIASGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DIASGUARDIA+" DURACION,"+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHASUSCRIPCION+" FECHAINSCRIPCION,"+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHABAJA+" FECHABAJA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASPERIODO+","+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_IDPERSONA+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONLABORABLES+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTIPOGUARDIA+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_PORGRUPOS+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS+","+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHASOLICITUDBAJA+","+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHAVALIDACION+","+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_OBSERVACIONESVALIDACION+","+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_OBSERVACIONESSUSCRIPCION+","+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_OBSERVACIONESBAJA+","+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_OBSERVACIONESDENEGACION+" OBSERVACIONESDENEGACION,"+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHADENEGACION+" FECHADENEGACION,"+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_OBSERVACIONESVALBAJA +"," +
					"NVL("+ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHADENEGACION+","+ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHAVALIDACION+") FECHAVALOR,"+
					"TO_CHAR(NVL("+ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHADENEGACION+","+ScsInscripcionGuardiaBean.T_NOMBRETABLA+"."+ScsInscripcionGuardiaBean.C_FECHABAJA+"),'dd/mm/yyyy') FECHAVALORBAJA,"+
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_VALIDARINSCRIPCIONES+" VALIDARINSCRIPCIONES";
				
				break;
				
			case 3:
				campos =
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NOMBRE+" GUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDGUARDIA+" IDGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTURNO+" IDTURNO,"+
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_GUARDIAS+" OBLIGATORIEDAD,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASGUARDIA+" TIPODIASGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DIASGUARDIA+" DURACION,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA+" NUMEROLETRADOS,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NUMEROSUSTITUTOSGUARDIA+" NUMEROSUSTITUTOS,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASPERIODO+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONLABORABLES+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTIPOGUARDIA+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_PORGRUPOS+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS+","+
					ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_VALIDARINSCRIPCIONES+" VALIDARINSCRIPCIONES";
				break;
				
			case 4:
				campos =
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DIASGUARDIA+" DIASGUARDIA,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DIASPAGADOS+" DIASACOBRAR,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NUMEROLETRADOSGUARDIA+" NUMEROLETRADOS,"+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_NUMEROSUSTITUTOSGUARDIA+" NUMEROSUSTITUTOS, "+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_DIASSEPARACIONGUARDIAS+" DIASSEPARACIONGUARDIAS, "+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_TIPODIASGUARDIA+" TIPODIASGUARDIA"+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONLABORABLES+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTIPOGUARDIA+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_PORGRUPOS+","+
					ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS+"";
				break;
				
			default:
				break;
		}
		 
		return campos;
	} //getCamposTabla ()
	
	
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador de la guardia que se va a insertar. 
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	public Hashtable prepararInsert (Hashtable entrada)
			throws ClsExceptions 
	{
		RowsContainer rc = null;
		try					{ rc = new RowsContainer(); }
		catch (Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT (MAX(IDGUARDIA) + 1) AS IDGUARDIA FROM " + nombreTabla;	
		try {		
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDGUARDIA").equals(""))
					entrada.put(ScsGuardiasTurnoBean.C_IDGUARDIA,
							"1");
				else
					entrada.put(ScsGuardiasTurnoBean.C_IDGUARDIA,
							(String)prueba.get("IDGUARDIA"));								
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert' en B.D.");		
		}
		return entrada;
	} //prepararInsert ()
	
	/**
	 * Hay que cambiar los valores de los campos recogidos en el formulario jsp.
	 * 
	 * @param hash con los datos que vamos a querer meter en la BBDD
	 * @return hash con los datos como deben ir en BBDD
	 * @throws ClsExceptions
	 */
	public Hashtable prepararHash (Hashtable hash)
			throws ClsExceptions
	{
		try{
			String aux = UtilidadesHash.getString(hash,"VALIDARJUSTIFICACIONES");
			if (aux!=null)UtilidadesHash.set(hash,"VALIDARJUSTIFICACIONES","S");
			else UtilidadesHash.set(hash,"VALIDARJUSTIFICACIONES","N");
		}catch(Exception e){
			UtilidadesHash.set(hash,"VALIDARJUSTIFICACIONES","N");
		}
		return hash;
	} //prepararHash ()
	
	/**
	 * Funcion insert (MasterBean bean)
	 * @param hash con los datos a insertar
	 * @return true si todo va bien OK, false si hay algun error 
	 */
	public boolean insert (Hashtable hash)
			throws ClsExceptions
	{
		try {
			return super.insert(this.prepararInsert(this.prepararHash(hash)));
		}
		catch (Exception e)	{
			throw new ClsExceptions (e, "Error al ejecutar el 'iNsert' en B.D.");
		}
	} //insert ()
	
	/**
	 * Devuelve la descripcion en texto de la seleccion de tipo de dia
	 * en el idioma del usuario<BR>
	 * La diferencia con el metodo etiquetaTipoDia (), 
	 * es que este último no es estatico
	 * 
	 * @param laborables
	 * @param festivos
	 * @param usr
	 * @return String
	 */
	public static String obtenerTipoDia (String laborables, String festivos, UsrBean usr)
	{
		String seleccionLaborables	= UtilidadesString.getMensajeIdioma(usr, "gratuita.guardiasTurno.literal.seleccionLaborables");
		String seleccionFestivos	= UtilidadesString.getMensajeIdioma(usr, "gratuita.guardiasTurno.literal.seleccionFestivos");
		String L = UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Lunes");
		String M = UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Martes");
		String X = UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Miercoles");
		String J = UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Jueves");
		String V = UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Viernes");
		String S = UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Sabado");
		String D = UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Domingo");
		
		String seleccion = "";
		
		//sustituyendo por las letras de cada idioma
		laborables.replaceAll("L", L);
		laborables.replaceAll("M", M);
		laborables.replaceAll("X", X);
		laborables.replaceAll("J", J);
		laborables.replaceAll("V", V);
		laborables.replaceAll("S", S);
		festivos.replaceAll("L", L);
		festivos.replaceAll("M", M);
		festivos.replaceAll("X", X);
		festivos.replaceAll("J", J);
		festivos.replaceAll("V", V);
		festivos.replaceAll("S", S);
		festivos.replaceAll("D", D);
		
		//escribiendo los laborables
		String selTemp = laborables;
		
		if (selTemp.equals(L+M+X+J+V))			seleccion += seleccionLaborables + " " + L + "-" + V;
		else if (selTemp.equals(L+M+X+J+V+S))	seleccion += seleccionLaborables + " " + L + "-" + S;
		else if (! (selTemp.length() == 0))		seleccion += seleccionLaborables + " " + selTemp;
		
		//escribiendo los festivos
		selTemp = festivos;
		
		if (seleccion.length() > 0 && selTemp.length() > 0) seleccion += ", ";
		
		if (selTemp.equals(L+M+X+J+V))			seleccion += seleccionFestivos + " " + L + "-" + V;
		else if (selTemp.equals(L+M+X+J+V+S))	seleccion += seleccionFestivos + " " + L + "-" + S;
		else if (selTemp.equals(L+M+X+J+V+S+D))	seleccion += seleccionFestivos + " " + L + "-" + D;
		else if (! (selTemp.length() == 0))		seleccion += seleccionFestivos + " " + selTemp;
		
		//mostrando en pantalla
		return seleccion;
	} //obtenerTipoDia ()
	
	/**
	 * Devuelve un String con los campos por los que debe ser ordenado el listado de los letrados inscritos a la guardia
	 * 
	 * Depende del valor del idOrdenacionColas 
	 * @param int idOrdenacion: int don el identificador de la ordenación de cola para esa guardia 
	 */
	public String getOrdenacionLetradosInscritos (int idOrdenacion)
			throws ClsExceptions
	{
		String resultado="";
		ScsOrdenacionColasAdm colaAdm = new ScsOrdenacionColasAdm(this.usrbean);
		Hashtable hash = new Hashtable();
		hash.put(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS,String.valueOf(idOrdenacion));
		try{
			ScsOrdenacionColasBean cola = (ScsOrdenacionColasBean)((Vector)colaAdm.select(hash)).get(0);
			int maximo=0;
			int elegido=0;
			for (int cont=1; cont<=4; cont++){
				maximo = 0;
				elegido = 0;
				if (maximo<Math.abs(cola.getAlfabeticoApellidos().intValue())){
					elegido=1;
					maximo = Math.abs(cola.getAlfabeticoApellidos().intValue());
				}
				if (maximo < Math.abs(cola.getFechaNacimiento().intValue())){
					elegido=2;
					maximo = Math.abs(cola.getFechaNacimiento().intValue());
				}
				if (maximo < Math.abs(cola.getAntiguedadCola().intValue())){
					elegido =3;
					maximo = Math.abs(cola.getAntiguedadCola().intValue());
				}
				if (maximo < Math.abs(cola.getNumeroColegiado().intValue())){
					elegido = 4;
					maximo = Math.abs(cola.getNumeroColegiado().intValue());
				}
				switch (elegido){
					case 1: if (cont>1)resultado+=",B.ALFABETICOAPELLIDOS";
							else resultado+="B.ALFABETICOAPELLIDOS";
							if (maximo<0)resultado+=" DESC";			//SI ES NEGATIVO SE ORDENA DESCENDIENTEMENTE
							break;
					case 2: if (cont>1)resultado+=",B.FECHANACIMIENTO";
							else resultado+="B.FECHANACIMIENTO";
							if (maximo<0)resultado+=" DESC";			//SI ES NEGATIVO SE ORDENA DESCENDIENTEMENTE
							break;
					case 3: if (cont>1)resultado+=",B.ANTIGUEDADCOLA";
							else resultado+="B.ANTIGUEDADCOLA";
							if (maximo<0)resultado+=" DESC";			//SI ES NEGATIVO SE ORDENA DESCENDIENTEMENTE
							break;
					case 4: if (cont>1)resultado+=",B.NUMEROCOLEGIADO";
							else resultado+="B.NUMEROCOLEGIADO";
							if (maximo<0)resultado+=" DESC";			//SI ES NEGATIVO SE ORDENA DESCENDIENTEMENTE
							break;
					default:break; 
				}
			}
			
		}catch(Exception e){
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
		return resultado;
	} //getOrdenacionLetradosInscritos ()

	/** 
	 * Devuelve un String con la consulta SQL que devuelve 1 registro que contiene los datos: Tipo Dias, Nº Letrados de<br>
	 * Guardia, Nº Letrados de Reserva y los Días de Guardia.  
	 * el IDCALENDARIOGUARDIAS.
	 * 
	 * @param String idinstitucion_pestanha de la pestanha
	 * @param String idturno_pestanha de la pestanha
	 * @param String idguardia_pestanha de la pestanha
	 * @return String con la consulta SQL.
	 * @throws ClsExceptions
	 */	
	public String getDatosCalendario (String idinstitucion_pestanha,
									  String idturno_pestanha,
									  String idguardia_pestanha)
			throws ClsExceptions
	{
		String consulta = "";
		
		try {
			consulta = "SELECT "+this.getCamposTabla(4);
			consulta += " FROM "+ScsGuardiasTurnoBean.T_NOMBRETABLA;
			consulta += " WHERE ";
			consulta += ScsGuardiasTurnoBean.C_IDINSTITUCION+"="+idinstitucion_pestanha;
			consulta += " AND "+ScsGuardiasTurnoBean.C_IDTURNO+"="+idturno_pestanha;
			consulta += " AND "+ScsGuardiasTurnoBean.C_IDGUARDIA+"="+idguardia_pestanha;
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en " +
					"ScsGuardiasTurnoAdm.getDatosCalendario(). Consulta SQL:"+consulta);
		}
		
		return consulta;
	} //getDatosCalendario ()

	/** 
	 * Recoge la informacion necesaria para formalizar un informe sobre listas de guardias <br/> 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  turno - identificador del turno	 	  
	 * @param  guardia - identificador de la guardia
	 * @param  fechaInicio - fecha de inicio del periodo
	 * @param  fechaFin - fecha de finalizacion del periodo
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosListaGuardias(String institucion,
			String idlista,
			Vector guardias,
			String fechaInicio,
			String fechaFin,
			String idPersona) throws ClsExceptions, SIGAException {
		Vector datos = new Vector();
		String idcalendarioguardias = "", idturno = "", idguardia = "", idpersona = "";
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		TreeMap tmListaGuardias = new TreeMap();
		try {
			RowsContainer rc = new RowsContainer();

			/* String sql =" SELECT GUARDIA, LETRADO, MAX(OFICINA1), MAX(OFICINA2), MAX(MOVIL), MAX(FAX1), FECHA_FIN" + */
			/**
			 * Cambio realizado por PDM, se cambia el campo de salida FECHA_FIN por FECHA_INICIO para que asi se ordene
			 * por el y muestre la misma informacion que cuando se accede a las guardias a traves del calendario
			 * INC-02348
			 */

			Hashtable codigos = new Hashtable();
			int contador = 0;
			//aalg: INC_09675. Se filtra por las fechas de baja y validación de la suscripción
			String sql = " SELECT "
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + " AS IDGUARDIA, "
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_DESCRIPCION + " AS DESCRIPCIONGUARDIA, "
					+ "(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + " || " 
						+ "NVL2(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ", ' ' || " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ", '') || ', ' || "
						+ CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ") AS LETRADO,"
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_NOMBRE + " AS GUARDIA,"
					+ " guardias2.idpersona IDPERSONA,  guardias2.idcalendarioguardias IDCALENDARIOGUARDIAS,  "
					+ ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_NOMBRE + "	TURNO, "
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDTURNO + " IDTURNO, "
					+ " decode(cen_colegiado.ncolegiado,null,cen_colegiado.ncomunitario,cen_colegiado.ncolegiado) NCOLEGIADO, ";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += "f_siga_getdireccioncliente(:" + contador + ", guardias2.idpersona,6,11) AS OFICINA1, ";

			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " f_siga_getdireccioncliente(:" + contador + ", guardias2.idpersona, 6,12) AS OFICINA2, ";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " f_siga_getdireccioncliente(:" + contador + ", guardias2.idpersona, 1,11) AS RESIDENCIA, ";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " f_siga_getdireccioncliente(:" + contador + ", guardias2.idpersona,6,13) AS MOVIL, ";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " f_siga_getdireccioncliente(:" + contador + ", guardias2.idpersona,6,14) AS FAX1, ";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " f_siga_getdireccioncliente(:" + contador + ", guardias2.idpersona, 6,15) AS FAX2, ";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += "decode(F_SIGA_FECHAINISOLICITANTE(:" + contador + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDTURNO + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += " '', decode(F_SIGA_FECHAINICONFIRMADOR(:" + contador + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "." + ScsGuardiasTurnoBean.C_IDTURNO + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += " '', guardias2.fechainicio, F_SIGA_FECHAINICONFIRMADOR(:" + contador + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)),";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += " F_SIGA_FECHAINISOLICITANTE(:" + contador + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDTURNO + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)) AS FECHA_INICIO, ";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " to_char(decode(F_SIGA_FECHAINISOLICITANTE(:" + contador + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "." + ScsGuardiasTurnoBean.C_IDTURNO + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " '', decode(F_SIGA_FECHAINICONFIRMADOR(:" + contador + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "." + ScsGuardiasTurnoBean.C_IDTURNO + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " '', guardias2.fechainicio, F_SIGA_FECHAINICONFIRMADOR(:" + contador + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)),";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " F_SIGA_FECHAINISOLICITANTE(:"
					+ contador
					+ ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "."
					+ ScsGuardiasTurnoBean.C_IDTURNO
					+ ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA
					+ ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)), 'Day') AS DIA_FECHA_INICIO, ";

			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += "decode(F_SIGA_FECHAFINSOLICITANTE(:" + contador + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDTURNO + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += " '', decode(F_SIGA_FECHAFINCONFIRMADOR(:" + contador + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "." + ScsGuardiasTurnoBean.C_IDTURNO + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += " '', guardias2.fecha_fin, F_SIGA_FECHAFINCONFIRMADOR(:" + contador + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)),";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += " F_SIGA_FECHAFINSOLICITANTE(:"
					+ contador
					+ ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "."
					+ ScsGuardiasTurnoBean.C_IDTURNO
					+ ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA
					+ ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)) AS FECHA_FIN,  SCS_INCLUSIONGUARDIASENLISTAS.ORDEN, guardias2.posicion, "
					//+ " Grg.NUMEROGRUPO As GRUPO ";
					+ " guardias2.NUMEROGRUPO As GRUPO, ";
			
			sql +=" F_SIGA_GETCOMPAS("
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDINSTITUCION + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, 0) AS NUM_COLEGIADO_COMPA, ";
			
			sql +=" F_SIGA_GETCOMPAS("
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDINSTITUCION + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, 1) AS NOMBRE_COMPA ";
			
			sql += " FROM " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "," + CenPersonaBean.T_NOMBRETABLA + ","
					+ ScsCabeceraGuardiasBean.T_NOMBRETABLA + " guardias2, " + ScsTurnoBean.T_NOMBRETABLA + ", "
					+ CenColegiadoBean.T_NOMBRETABLA + "," + ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA 
					+" WHERE " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDINSTITUCION
					+ "=guardias2." + ScsCabeceraGuardiasBean.C_IDINSTITUCION + " AND "
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + "=guardias2."
					+ ScsCabeceraGuardiasBean.C_IDTURNO + " AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + "=guardias2." + ScsCabeceraGuardiasBean.C_IDGUARDIA + " AND "
					+ " guardias2." + ScsCabeceraGuardiasBean.C_IDINSTITUCION + "=" + ScsTurnoBean.T_NOMBRETABLA + "."
					+ ScsTurnoBean.C_IDINSTITUCION + " AND " + " guardias2." + ScsCabeceraGuardiasBean.C_IDTURNO + "="
					+ ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDTURNO +

					" AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA + "="
					+ CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " AND "
					+ CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION + "=guardias2."
					+ ScsCabeceraGuardiasBean.C_IDINSTITUCION + " AND " + CenColegiadoBean.T_NOMBRETABLA + "."
					+ CenColegiadoBean.C_IDPERSONA + "=guardias2." + ScsCabeceraGuardiasBean.C_IDPERSONA + " AND " ;
					 
			contador++;
			codigos.put(new Integer(contador), fechaInicio);

			sql += "((guardias2." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " >= TO_DATE(:" + contador
					+ ",'DD/MM/YYYY')" + " AND ";
			contador++;
			codigos.put(new Integer(contador), fechaFin);

			sql += "guardias2." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " < TO_DATE(:" + contador
					+ ",'DD/MM/YYYY')+1)" + " OR ";
			contador++;
			codigos.put(new Integer(contador), fechaInicio);

			sql += "(guardias2." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " < TO_DATE(:" + contador
					+ ",'DD/MM/YYYY')+1" + " AND ";
			contador++;
			codigos.put(new Integer(contador), fechaInicio);

			sql += "guardias2." + ScsCabeceraGuardiasBean.C_FECHA_FIN + " >= TO_DATE(:" + contador + ",'DD/MM/YYYY')))"
					+ " AND ";
			
			contador++;
			codigos.put(new Integer(contador), institucion);
			
			sql += ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "=:" + contador;

			if (guardias != null && guardias.size() > 0) {
				sql += " AND " + "(" + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + ","
						+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + ") in (";
				String aux = "";
				for (int i = 0; i < guardias.size(); i++) {
					Vector v = (Vector) guardias.get(i);
					contador++;
					codigos.put(new Integer(contador), (String) v.get(0));
					aux += "(:" + contador;
					contador++;
					codigos.put(new Integer(contador), (String) v.get(1));
					aux += ",:" + contador + "),";
				}
				if (aux.length() > 0) {
					aux = aux.substring(0, aux.length() - 1);
				}
				sql += aux + ")";
			}
			sql += " AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "="
					+ ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "."
					+ ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION + " AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "." + ScsGuardiasTurnoBean.C_IDTURNO + "=" + ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "."
					+ ScsInclusionGuardiasEnListasBean.C_IDTURNO + " AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + "=" + ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "."
					+ ScsInclusionGuardiasEnListasBean.C_IDGUARDIA + " AND SCS_INCLUSIONGUARDIASENLISTAS.IDLISTA ="
					+ idlista;
			
			if (idPersona!=null&&!idPersona.trim().equalsIgnoreCase("")) {
				sql += " AND guardias2.idpersona="+ idPersona;
			}
			
			sql += " ORDER BY FECHA_INICIO,FECHA_FIN, SCS_INCLUSIONGUARDIASENLISTAS.ORDEN, GUARDIA, POSICION, LETRADO";
			// jbd // inc7654
			// Para evitar lo ocurrido en Murcia se cambia la forma de crear la lista de letrados de
			// guardia. Se devuelve ya en el orden de salida, asi no hace falta hacer la ordenacion 
			// a posteriori y evitamos usar un TreeMap intermedio que provocaba el error de eliminar
			//un letrado si tenian el mismo nombre y guardia el mismo dia
			if (rc.findBind(sql, codigos)) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable resultado = fila.getRow();
					datos.add(resultado);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener la informacion sobre listas de guardias.");
		}
		return datos;
	} // getDatosListaGuardias ()	

	/** 
	 * JPT: Recoge la informacion necesaria para realizar el informe de definicion de listas de guardias
	 *  
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  turno - identificador del turno	 	  
	 * @param  guardia - identificador de la guardia
	 * @param  fechaInicio - fecha de inicio del periodo
	 * @param  fechaFin - fecha de finalizacion del periodo
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosDefinirListaGuardias(
			String institucion,
			String idlista,
			Vector guardias,
			String fechaInicio,
			String fechaFin,
			String idPersona
		) throws ClsExceptions, SIGAException {
		
		Vector datos = new Vector();
		String idcalendarioguardias = "", idturno = "", idguardia = "", idpersona = "";
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		TreeMap tmListaGuardias = new TreeMap();
		
		try {
			RowsContainer rc = new RowsContainer();

			Hashtable codigos = new Hashtable();
			int contador = 0;
			String sql = " SELECT (" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + " || " + 
								"NVL2(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ", ' ' || " + 
									CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ", '') || ', ' || "
								+ CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ") AS LETRADO,"
							+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_NOMBRE + " AS GUARDIA,"
							+ ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_NOMBRE + " AS TURNO, "
							+ " NVL(" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOLEGIADO + "," + 
								CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOMUNITARIO + ") AS NCOLEGIADO, ";
			
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " F_SIGA_GETDIRECCIONCLIENTE(:" + contador + ", " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDPERSONA + ", 6, 11) AS OFICINA1, ";

			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " F_SIGA_GETDIRECCIONCLIENTE(:" + contador + ", " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDPERSONA + ", 6, 12) AS OFICINA2, ";
			
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " F_SIGA_GETDIRECCIONCLIENTE(:" + contador + ", " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDPERSONA + ", 1, 11) AS RESIDENCIA, ";
			
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " F_SIGA_GETDIRECCIONCLIENTE(:" + contador + "," + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDPERSONA + ", 6, 13) AS MOVIL, ";
			
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " F_SIGA_GETDIRECCIONCLIENTE(:" + contador + ", " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDPERSONA + ", 6, 14) AS FAX1, ";
			
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " F_SIGA_GETDIRECCIONCLIENTE(:" + contador + ", " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDPERSONA + ", 6, 15) AS FAX2, ";
			
			sql += ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " AS FECHA_INICIO, " +
					" TO_CHAR(" + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + ", 'Day') AS DIA_FECHA_INICIO, " +
					ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_FECHA_FIN + " AS FECHA_FIN, " +
					ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "." + ScsInclusionGuardiasEnListasBean.C_ORDEN + " AS ORDEN, " +
					ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_FECHA_FIN + " AS FECHA_FIN, " +
					ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_POSICION + " AS POSICION ";
			
			sql += " FROM " + ScsGuardiasTurnoBean.T_NOMBRETABLA + ", " + 
						CenPersonaBean.T_NOMBRETABLA + ", " + 
						ScsCabeceraGuardiasBean.T_NOMBRETABLA + ", " + 
						ScsTurnoBean.T_NOMBRETABLA + ", " + 
						CenColegiadoBean.T_NOMBRETABLA + ", " + 
						ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + ", " + 
						ScsInscripcionGuardiaBean.T_NOMBRETABLA;
			
			sql += " WHERE " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDINSTITUCION + " = " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDINSTITUCION + 
						" AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + " = " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDTURNO + 
						" AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + " = " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDGUARDIA + 
						" AND " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDINSTITUCION + " = " + ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDINSTITUCION + 
						" AND " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDTURNO + " = " + ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDTURNO +
						" AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA + " = " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + 
						" AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION + " = " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDINSTITUCION + 
						" AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA + " = " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDPERSONA + 
						" AND " + ScsInscripcionGuardiaBean.T_NOMBRETABLA + "." + ScsInscripcionGuardiaBean.C_IDINSTITUCION + " = " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDINSTITUCION + 
						" AND " + ScsInscripcionGuardiaBean.T_NOMBRETABLA + "." + ScsInscripcionGuardiaBean.C_IDPERSONA + " = " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDPERSONA + 
						" AND " + ScsInscripcionGuardiaBean.T_NOMBRETABLA + "." + ScsInscripcionGuardiaBean.C_IDTURNO + " = " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDTURNO + 
						" AND " + ScsInscripcionGuardiaBean.T_NOMBRETABLA + "." + ScsInscripcionGuardiaBean.C_IDGUARDIA + " = " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDGUARDIA;
			
			/*
			 * JPT y Adri: Mejorados los intervalos de fechas
			 * 1. La fecha inicial de las cabeceras de guardia debe ser menor o igual que la fecha final indicada
			 * 2. La fecha final de las cabeceras de guardia debe ser mayor o igual que la fecha inicial indicada 
			 */
			contador++;
			codigos.put(new Integer(contador), fechaFin);
			sql += " AND " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " <= TO_DATE(:" + contador + ",'DD/MM/YYYY') ";
			
			contador++;
			codigos.put(new Integer(contador), fechaInicio);
			sql += " AND " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_FECHA_FIN + " >= TO_DATE(:" + contador + ",'DD/MM/YYYY') ";
			
			/*
			 * JPT y Adri: Mejorados los intervalos de fechas (la inscripcion de guardia debe estar incluida en el intervalo de fechas de la cabecera de guardia) 
			 * 1. La fecha de validacion debe ser menor o igual que la fecha final de la cabecera de guardia
			 * 2. La fecha de baja, en caso de tener, debe ser mayor o igual que la fecha inicial de la cabecera de guardia  
			 */
			sql += " AND " + ScsInscripcionGuardiaBean.T_NOMBRETABLA + "." + ScsInscripcionGuardiaBean.C_FECHAVALIDACION + " <= " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_FECHA_FIN +		
					" AND (" + ScsInscripcionGuardiaBean.T_NOMBRETABLA + "." + ScsInscripcionGuardiaBean.C_FECHABAJA + " IS NULL " +
						" OR " + ScsInscripcionGuardiaBean.T_NOMBRETABLA + "." + ScsInscripcionGuardiaBean.C_FECHABAJA + " >= " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + ") ";

			contador++;
			codigos.put(new Integer(contador), institucion);			
			sql += " AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "=:" + contador;

			if (guardias != null && guardias.size() > 0) {
				sql += " AND " + "(" + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + "," + 
							ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + ") IN (";
				String aux = "";
				for (int i = 0; i < guardias.size(); i++) {
					Vector v = (Vector) guardias.get(i);
					
					contador++;
					codigos.put(new Integer(contador), (String) v.get(0));
					aux += "(:" + contador;
					
					contador++;
					codigos.put(new Integer(contador), (String) v.get(1));
					aux += ",:" + contador + "),";
				}
				
				if (aux.length() > 0) {
					aux = aux.substring(0, aux.length() - 1);
				}
				sql += aux + ") ";
			}
			
			sql += " AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDINSTITUCION + " = " + ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "." + ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION + 
					" AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + " = " + ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "." + ScsInclusionGuardiasEnListasBean.C_IDTURNO + 
					" AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + " = " + ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "." + ScsInclusionGuardiasEnListasBean.C_IDGUARDIA + 
					" AND " + ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "." + ScsInclusionGuardiasEnListasBean.C_IDLISTA + " = " + idlista;
			
			if (idPersona!=null && !idPersona.trim().equalsIgnoreCase("")) {
				sql += " AND " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_IDPERSONA + " = " + idPersona;
			}
			
			sql += " ORDER BY " + ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + ", " + 
						ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_FECHA_FIN + ", " +
						ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "." + ScsInclusionGuardiasEnListasBean.C_ORDEN + ", " +
						ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_NOMBRE + ", " +
						ScsCabeceraGuardiasBean.T_NOMBRETABLA + "." + ScsCabeceraGuardiasBean.C_POSICION + ", " +
						" LETRADO";
			
			// jbd // inc7654
			// Para evitar lo ocurrido en Murcia se cambia la forma de crear la lista de letrados de
			// guardia. Se devuelve ya en el orden de salida, asi no hace falta hacer la ordenacion 
			// a posteriori y evitamos usar un TreeMap intermedio que provocaba el error de eliminar
			//un letrado si tenian el mismo nombre y guardia el mismo dia
			if (rc.findBind(sql, codigos)) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable resultado = fila.getRow();
					datos.add(resultado);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener la informacion sobre listas de guardias.");
		}
		return datos;
	} // getDatosListaGuardias ()

	
	public Vector getDatosPersonasGuardias(String institucion,
			String idlista,
			Vector guardias,
			String fechaInicio,
			String fechaFin) throws ClsExceptions, SIGAException
	{
		Vector datos = new Vector();
		String idcalendarioguardias = "", idturno = "", idguardia = "", idpersona = "";
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		TreeMap tmListaGuardias = new TreeMap();
		try {
			RowsContainer rc = new RowsContainer();

			/* String sql =" SELECT GUARDIA, LETRADO, MAX(OFICINA1), MAX(OFICINA2), MAX(MOVIL), MAX(FAX1), FECHA_FIN" + */
			/**
			 * Cambio realizado por PDM, se cambia el campo de salida FECHA_FIN por FECHA_INICIO para que asi se ordene
			 * por el y muestre la misma informacion que cuando se accede a las guardias a traves del calendario
			 * INC-02348
			 */

			Hashtable codigos = new Hashtable();
			int contador = 0;

			String sql = " SELECT distinct"
					+ " guardias2.idpersona IDPERSONA";

			sql += " FROM " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "," + CenPersonaBean.T_NOMBRETABLA + ","
					+ ScsCabeceraGuardiasBean.T_NOMBRETABLA + " guardias2, " + ScsTurnoBean.T_NOMBRETABLA + ", "
					+ CenColegiadoBean.T_NOMBRETABLA + "," + ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA +

					" WHERE " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDINSTITUCION
					+ "=guardias2." + ScsCabeceraGuardiasBean.C_IDINSTITUCION + " AND "
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + "=guardias2."
					+ ScsCabeceraGuardiasBean.C_IDTURNO + " AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + "=guardias2." + ScsCabeceraGuardiasBean.C_IDGUARDIA + " AND "
					+ " guardias2." + ScsCabeceraGuardiasBean.C_IDINSTITUCION + "=" + ScsTurnoBean.T_NOMBRETABLA + "."
					+ ScsTurnoBean.C_IDINSTITUCION + " AND " + " guardias2." + ScsCabeceraGuardiasBean.C_IDTURNO + "="
					+ ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDTURNO +

					" AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA + "="
					+ CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " AND "
					+ CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION + "=guardias2."
					+ ScsCabeceraGuardiasBean.C_IDINSTITUCION + " AND " + CenColegiadoBean.T_NOMBRETABLA + "."
					+ CenColegiadoBean.C_IDPERSONA + "=guardias2." + ScsCabeceraGuardiasBean.C_IDPERSONA + " AND ";
			contador++;
			codigos.put(new Integer(contador), fechaInicio);

			sql += "((guardias2." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " >= TO_DATE(:" + contador
					+ ",'DD/MM/YYYY')" + " AND ";
			contador++;
			codigos.put(new Integer(contador), fechaFin);

			sql += "guardias2." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " < TO_DATE(:" + contador
					+ ",'DD/MM/YYYY')+1)" + " OR ";
			contador++;
			codigos.put(new Integer(contador), fechaInicio);

			sql += "(guardias2." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " < TO_DATE(:" + contador
					+ ",'DD/MM/YYYY')+1" + " AND ";
			contador++;
			codigos.put(new Integer(contador), fechaInicio);

			sql += "guardias2." + ScsCabeceraGuardiasBean.C_FECHA_FIN + " >= TO_DATE(:" + contador + ",'DD/MM/YYYY')))"
					+ " AND ";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "=:" + contador;

			if (guardias != null && guardias.size() > 0) {
				sql += " AND " + "(" + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + ","
						+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + ") in (";
				String aux = "";
				for (int i = 0; i < guardias.size(); i++) {
					Vector v = (Vector) guardias.get(i);
					contador++;
					codigos.put(new Integer(contador), (String) v.get(0));
					aux += "(:" + contador;
					contador++;
					codigos.put(new Integer(contador), (String) v.get(1));
					aux += ",:" + contador + "),";
				}
				if (aux.length() > 0) {
					aux = aux.substring(0, aux.length() - 1);
				}
				sql += aux + ")";
			}
			sql += " AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "="
					+ ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "."
					+ ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION + " AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "." + ScsGuardiasTurnoBean.C_IDTURNO + "=" + ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "."
					+ ScsInclusionGuardiasEnListasBean.C_IDTURNO + " AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + "=" + ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "."
					+ ScsInclusionGuardiasEnListasBean.C_IDGUARDIA + " AND SCS_INCLUSIONGUARDIASENLISTAS.IDLISTA ="
					+ idlista;
			
			// jbd // inc7654
			// Para evitar lo ocurrido en Murcia se cambia la forma de crear la lista de letrados de
			// guardia. Se devuelve ya en el orden de salida, asi no hace falta hacer la ordenacion 
			// a posteriori y evitamos usar un TreeMap intermedio que provocaba el error de eliminar
			//un letrado si tenian el mismo nombre y guardia el mismo dia
			if (rc.findBind(sql, codigos)) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable resultado = fila.getRow();
					datos.add(resultado);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener la informacion sobre las persona que tienen listas de guardias.");
		}
		return datos;
	} // getDatosListaGuardias ()

	public Vector getDatosListaGuardiasPorPersona(String institucion,
			String idlista,
			Vector guardias,
			String fechaInicio,
			String fechaFin, Vector idPersonas) throws ClsExceptions, SIGAException
	{
		Vector datos = new Vector();
		String idcalendarioguardias = "", idturno = "", idguardia = "", idpersona = "";
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		TreeMap tmListaGuardias = new TreeMap();
		try {
			RowsContainer rc = new RowsContainer();

			/* String sql =" SELECT GUARDIA, LETRADO, MAX(OFICINA1), MAX(OFICINA2), MAX(MOVIL), MAX(FAX1), FECHA_FIN" + */
			/**
			 * Cambio realizado por PDM, se cambia el campo de salida FECHA_FIN por FECHA_INICIO para que asi se ordene
			 * por el y muestre la misma informacion que cuando se accede a las guardias a traves del calendario
			 * INC-02348
			 */

			Hashtable codigos = new Hashtable();
			int contador = 0;

			String sql = " SELECT "
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA
					+ " AS IDGUARDIA, "
					+ "("
					+ CenPersonaBean.T_NOMBRETABLA
					+ "."
					+ CenPersonaBean.C_APELLIDOS1
					+ " || ' ' || "
					+ CenPersonaBean.T_NOMBRETABLA
					+ "."
					+ CenPersonaBean.C_APELLIDOS2
					+ " || ', ' || "
					+ CenPersonaBean.T_NOMBRETABLA
					+ "."
					+ CenPersonaBean.C_NOMBRE
					+ ") AS LETRADO,"
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "."
					+ ScsGuardiasTurnoBean.C_NOMBRE
					+ " AS GUARDIA,"
					+ " guardias2.idpersona IDPERSONA,  guardias2.idcalendarioguardias IDCALENDARIOGUARDIAS,  "
					+ ScsTurnoBean.T_NOMBRETABLA
					+ "."
					+ ScsTurnoBean.C_NOMBRE
					+ "	 TURNO, "
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "."
					+ ScsTurnoBean.C_IDTURNO
					+ " IDTURNO, "
					+ " decode(cen_colegiado.ncolegiado,null,cen_colegiado.ncomunitario,cen_colegiado.ncolegiado) NCOLEGIADO, ";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += "f_siga_getdireccioncliente(:" + contador + ", guardias2.idpersona,6,11) AS OFICINA1, ";

			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " f_siga_getdireccioncliente(:" + contador + ", guardias2.idpersona, 6,12) AS OFICINA2, ";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " f_siga_getdireccioncliente(:" + contador + ", guardias2.idpersona, 1,11) AS RESIDENCIA, ";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " f_siga_getdireccioncliente(:" + contador + ", guardias2.idpersona,6,13) AS MOVIL, ";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " f_siga_getdireccioncliente(:" + contador + ", guardias2.idpersona,6,14) AS FAX1, ";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " f_siga_getdireccioncliente(:" + contador + ", guardias2.idpersona, 6,15) AS FAX2, ";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += "decode(F_SIGA_FECHAINISOLICITANTE(:" + contador + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDTURNO + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += " '', decode(F_SIGA_FECHAINICONFIRMADOR(:" + contador + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "." + ScsGuardiasTurnoBean.C_IDTURNO + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += " '', guardias2.fechainicio, F_SIGA_FECHAINICONFIRMADOR(:" + contador + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)),";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += " F_SIGA_FECHAINISOLICITANTE(:" + contador + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDTURNO + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)) AS FECHA_INICIO, ";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " to_char(decode(F_SIGA_FECHAINISOLICITANTE(:" + contador + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "." + ScsGuardiasTurnoBean.C_IDTURNO + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " '', decode(F_SIGA_FECHAINICONFIRMADOR(:" + contador + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "." + ScsGuardiasTurnoBean.C_IDTURNO + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " '', guardias2.fechainicio, F_SIGA_FECHAINICONFIRMADOR(:" + contador + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)),";
			contador++;
			codigos.put(new Integer(contador), institucion);
			sql += " F_SIGA_FECHAINISOLICITANTE(:"
					+ contador
					+ ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "."
					+ ScsGuardiasTurnoBean.C_IDTURNO
					+ ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA
					+ ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)), 'Day') AS DIA_FECHA_INICIO, ";
			sql += ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_NOMBRE + "	 TURNO, ";

			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += "decode(F_SIGA_FECHAFINSOLICITANTE(:" + contador + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDTURNO + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += " '', decode(F_SIGA_FECHAFINCONFIRMADOR(:" + contador + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "." + ScsGuardiasTurnoBean.C_IDTURNO + "," + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias),";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += " '', guardias2.fecha_fin, F_SIGA_FECHAFINCONFIRMADOR(:" + contador + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)),";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += " F_SIGA_FECHAFINSOLICITANTE(:"
					+ contador
					+ ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "."
					+ ScsGuardiasTurnoBean.C_IDTURNO
					+ ","
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA
					+ ","
					+ "guardias2.idpersona, guardias2.fechainicio, guardias2.idcalendarioguardias)) AS FECHA_FIN,  SCS_INCLUSIONGUARDIASENLISTAS.ORDEN, guardias2.posicion ";

			sql += " FROM " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "," + CenPersonaBean.T_NOMBRETABLA + ","
					+ ScsCabeceraGuardiasBean.T_NOMBRETABLA + " guardias2, " + ScsTurnoBean.T_NOMBRETABLA + ", "
					+ CenColegiadoBean.T_NOMBRETABLA + "," + ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA +

					" WHERE " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDINSTITUCION
					+ "=guardias2." + ScsCabeceraGuardiasBean.C_IDINSTITUCION + " AND "
					+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + "=guardias2."
					+ ScsCabeceraGuardiasBean.C_IDTURNO + " AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + "=guardias2." + ScsCabeceraGuardiasBean.C_IDGUARDIA + " AND "
					+ " guardias2." + ScsCabeceraGuardiasBean.C_IDINSTITUCION + "=" + ScsTurnoBean.T_NOMBRETABLA + "."
					+ ScsTurnoBean.C_IDINSTITUCION + " AND " + " guardias2." + ScsCabeceraGuardiasBean.C_IDTURNO + "="
					+ ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_IDTURNO +

					" AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA + "="
					+ CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " AND "
					+ CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION + "=guardias2."
					+ ScsCabeceraGuardiasBean.C_IDINSTITUCION + " AND " + CenColegiadoBean.T_NOMBRETABLA + "."
					+ CenColegiadoBean.C_IDPERSONA + "=guardias2." + ScsCabeceraGuardiasBean.C_IDPERSONA + " AND ";
			contador++;
			codigos.put(new Integer(contador), fechaInicio);

			sql += "((guardias2." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " >= TO_DATE(:" + contador
					+ ",'DD/MM/YYYY')" + " AND ";
			contador++;
			codigos.put(new Integer(contador), fechaFin);

			sql += "guardias2." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " < TO_DATE(:" + contador
					+ ",'DD/MM/YYYY')+1)" + " OR ";
			contador++;
			codigos.put(new Integer(contador), fechaInicio);

			sql += "(guardias2." + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " < TO_DATE(:" + contador
					+ ",'DD/MM/YYYY')+1" + " AND ";
			contador++;
			codigos.put(new Integer(contador), fechaInicio);

			sql += "guardias2." + ScsCabeceraGuardiasBean.C_FECHA_FIN + " >= TO_DATE(:" + contador + ",'DD/MM/YYYY')))"
					+ " AND ";
			contador++;
			codigos.put(new Integer(contador), institucion);

			sql += ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "=:" + contador;

			if (guardias != null && guardias.size() > 0) {
				sql += " AND " + "(" + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDTURNO + ","
						+ ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDGUARDIA + ") in (";
				String aux = "";
				for (int i = 0; i < guardias.size(); i++) {
					Vector v = (Vector) guardias.get(i);
					contador++;
					codigos.put(new Integer(contador), (String) v.get(0));
					aux += "(:" + contador;
					contador++;
					codigos.put(new Integer(contador), (String) v.get(1));
					aux += ",:" + contador + "),";
				}
				if (aux.length() > 0) {
					aux = aux.substring(0, aux.length() - 1);
				}
				sql += aux + ")";
			}
			sql += " AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "="
					+ ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "."
					+ ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION + " AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA
					+ "." + ScsGuardiasTurnoBean.C_IDTURNO + "=" + ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "."
					+ ScsInclusionGuardiasEnListasBean.C_IDTURNO + " AND " + ScsGuardiasTurnoBean.T_NOMBRETABLA + "."
					+ ScsGuardiasTurnoBean.C_IDGUARDIA + "=" + ScsInclusionGuardiasEnListasBean.T_NOMBRETABLA + "."
					+ ScsInclusionGuardiasEnListasBean.C_IDGUARDIA + " AND SCS_INCLUSIONGUARDIASENLISTAS.IDLISTA ="
					+ idlista;
			sql +="and guardias2.idpersona in (";
			//idPersonas.
			sql += ")  ORDER BY LETRADO, FECHA_INICIO";
			// jbd // inc7654
			// Para evitar lo ocurrido en Murcia se cambia la forma de crear la lista de letrados de
			// guardia. Se devuelve ya en el orden de salida, asi no hace falta hacer la ordenacion 
			// a posteriori y evitamos usar un TreeMap intermedio que provocaba el error de eliminar
			//un letrado si tenian el mismo nombre y guardia el mismo dia
			if (rc.findBind(sql, codigos)) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable resultado = fila.getRow();
					datos.add(resultado);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener la informacion sobre listas de guardias.");
		}
		return datos;
	} // getDatosListaGuardias ()

	/*
	public Vector getDatosPlantillas(String idinstitucion,
			String anio,
			String idturno,
			String numero,
			String lenguaje,
			boolean isASolicitantes,
			String idPersonaJG,
			String codigo) throws ClsExceptions, SIGAException
	{
		Vector datos = new Vector();
		try {
			RowsContainer rc = new RowsContainer();

			Hashtable codigos = new Hashtable();
			int contador = 0;

			String query = "SELECT  DISTINCT COL.NCOLEGIADO AS NCOLEGIADO_LETRADO,"
					+ "         COL.NOMBRE || ' ' || COL.APELLIDOS1 || ' ' || COL.APELLIDOS2 AS NOMBRE_LETRADO,"
					+ "         DECODE(COL.SEXO,null,null,'M',";
			contador++;
			codigos.put(new Integer(contador), lenguaje);

			query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.mujer',:" + contador + "),";
			contador++;
			codigos.put(new Integer(contador), lenguaje);

			query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.hombre',:" + contador
					+ ")) AS SEXO_LETRADO,"
					+ "		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,2,1)  AS DOMICILIO_LETRADO,"
					+ "	      f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,2,2)  AS CP_LETRADO,"
					+ "		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,2,3)  AS POBLACION_LETRADO,"
					+ "		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,2,4)  AS PROVINCIA_LETRADO,"
					+ "		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,2,11)  AS TELEFONODESPACHO_LET,"
					+ "		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,2,16)  AS EMAIL_LETRADO,"
					+ "		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,2,14)  AS FAX_LETRADO,"
					+ "		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,6,11)  AS TELEFONO1_LETRADO,"
					+ "		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,6,12)  AS TELEFONO2_LETRADO,"
					+ "		  f_siga_getdireccioncliente(col.IDINSTITUCION,col.IDPERSONA,6,13)  AS MOVIL_LETRADO,"
					+ "         DES.OBSERVACIONES AS OBSERVACIONES," + "         DES.NUMPROCEDIMIENTO AS AUTOS,"
					+ "         TO_CHAR(DES.FECHAJUICIO, 'dd/MM/yyyy') AS FECHA_JUICIO,"
					+ "         TO_CHAR(DES.FECHAJUICIO, 'HH24:MM') AS HORA_JUICIO,"
					+ "         JUZ.NOMBRE AS JUZGADO," + "         JUZ.DOMICILIO AS DIR_JUZGADO,"
					+ "         JUZ.CODIGOPOSTAL AS CP_JUZGADO,"
					+ "         JUZ.POBLACION_NOMBRE AS POBLACION_JUZGADO,"
					+ "         F_SIGA_GETCONTRARIOS_DESIGNA(DES.IDINSTITUCION,"
					+ "                                      DES.IDTURNO,"
					+ "                                      DES.ANIO,"
					+ "                                      DES.NUMERO) AS CONTRARIOS,"
					+ "         F_SIGA_GETPROCURADORCONT_DESIG(DES.IDINSTITUCION,"
					+ "                                        DES.IDTURNO,"
					+ "                                        DES.ANIO,"
					+ "                                        DES.NUMERO) AS PROCURADOR_CONTRARIOS,"
					+ "         DES.ANIO AS ANIO_DESIGNA," + "         DES.CODIGO AS CODIGO,"
					+ "         DES.RESUMENASUNTO AS ASUNTO," + "         DES.ANIO || '/' || DES.CODIGO AS NOFICIO,"
					+ "         PROC.NOMBRE || ' ' || PROC.APELLIDOS1 || ' ' || PROC.APELLIDOS2 AS PROCURADOR,"
					+ "         (select POB.NOMBRE " + "	       from cen_poblaciones pob "
					+ "	       where pob.idpoblacion=PROC.IDPOBLACION) AS POBLACION_PROCURADOR, "
					+ "	       (select prov.NOMBRE " + "	       from cen_provincias  prov "
					+ "	       where prov.idprovincia=PROC.IDPROVINCIA) AS PROVINCIA_PROCURADOR, "
					+ "	      PROC.DOMICILIO AS DOMICILIO_PROCURADOR, " + "	      PROC.CODIGOPOSTAL AS CP_PROCURADOR, "
					+ "	      PROC.TELEFONO1 AS TELEFONO1_PROCURADOR, ";
			contador++;
			codigos.put(new Integer(contador), lenguaje);

			query += "         F_SIGA_GETDELITOS_DESIGNA(DES.IDINSTITUCION, DES.NUMERO, DES.IDTURNO, DES.ANIO,:"
					+ contador + ") AS DELITOS," + "         TO_CHAR(DES.FECHAENTRADA, 'dd-mm-yyyy') AS FECHA_DESIGNA,"
					+ "         DES.TURNO_DESCRIPCION AS DESCRIPCION_TURNO," + "		  DES.ABREV_TURNO AS ABREV_TURNO, "
					+ "         F_SIGA_NOMBRE_PARTIDO(DES.IDTURNO,DES.IDINSTITUCION) as NOMBRE_PARTIDO," +

					" (select (DECODE(VACT1.ANIO, NULL, null, " + " VACT1.ANIO || '/' || VACT1.NUMERO || '/' || "
					+ " VACT1.NUMEROASUNTO)) " + " from  V_SIGA_ACTUACION_DESIGNA VACT1 "
					+ "  where DES.IDINSTITUCION = VACT1.IDINSTITUCION " + " AND DES.IDTURNO = VACT1.IDTURNO "
					+ " AND DES.ANIO = VACT1.ANIO " + "     AND DES.NUMERO = VACT1.NUMERO "
					+ "        AND ROWNUM < 2) AS NACTUACION, " + " nvl((select VACT2.NOMBRE_PROCEDIMIENTO " +

					"      from V_SIGA_ACTUACION_DESIGNA VACT2 "
					+ "       where DES.IDINSTITUCION = VACT2.IDINSTITUCION "
					+ "            AND DES.IDTURNO = VACT2.IDTURNO " + "            AND DES.ANIO = VACT2.ANIO "
					+ "           AND DES.NUMERO = VACT2.NUMERO "
					+ "            AND ROWNUM < 2),DES.NOMBRE_PROCEDIMIENTO) AS PROCEDIMIENTO, "
					+ "        (select TO_CHAR(VACT3.FECHA, 'dd-mm-yyyy') "
					+ "            from V_SIGA_ACTUACION_DESIGNA VACT3 "
					+ "            where DES.IDINSTITUCION = VACT3.IDINSTITUCION "
					+ "              AND DES.IDTURNO = VACT3.IDTURNO " + "              AND DES.ANIO = VACT3.ANIO "
					+ "              AND DES.NUMERO = VACT3.NUMERO "
					+ "                AND ROWNUM <2 ) AS FECHA_ACTUACION, "
					+ "          (select DECODE(vact3.IDPRISION, " + "                        NULL, "
					+ "                        (SELECT J.NOMBRE " + "                            FROM SCS_JUZGADO J "
					+ "                           WHERE J.IDINSTITUCION = DES.IDINSTITUCION_JUZG "
					+ "                            AND J.IDJUZGADO = DES.IDJUZGADO), "
					+ "                           (SELECT P.NOMBRE " + "                           FROM SCS_PRISION P "
					+ "                       WHERE P.IDINSTITUCION = vact3.IDINSTITUCION_PRIS "
					+ "                           AND P.IDPRISION = vact3.IDPRISION)) "
					+ "             from V_SIGA_ACTUACION_DESIGNA vact3 "
					+ "           where DES.IDINSTITUCION = vact3.IDINSTITUCION "
					+ "       AND DES.IDTURNO = vact3.IDTURNO " + "         AND DES.ANIO = vact3.ANIO "
					+ "           AND DES.NUMERO = vact3.NUMERO " + "           AND ROWNUM < 2) AS LUGAR,";

			query += " DECODE( "
					+ "       (select count(EJGDES1.idinstitucion) from SCS_EJGDESIGNA EJGDES1 where EJGDES1.IDINSTITUCION = DES.IDINSTITUCION "
					+ "             and EJGDES1.ANIODESIGNA = DES.ANIO "
					+ "            and EJGDES1.IDTURNO = DES.IDTURNO "
					+ "             and EJGDES1.NUMERODESIGNA = DES.NUMERO), " + "       '1', "
					+ "        (select EJGDES.ANIOEJG || '/' || ejg.NUMEJG from scs_ejg ejg,scs_ejgdesigna ejgdes "
					+ "               where ejg.anio =ejgdes.anioejg "
					+ "            and ejg.numero = ejgdes.numeroejg "
					+ "            and ejg.idinstitucion = ejgdes.idinstitucion "
					+ "             and ejg.idtipoejg = ejgdes.idtipoejg "
					+ "             AND ejgdes.IDINSTITUCION = DES.IDINSTITUCION "
					+ "             and ejgdes.ANIODESIGNA = DES.ANIO "
					+ "            and ejgdes.IDTURNO = DES.IDTURNO "
					+ "             and ejgdes.NUMERODESIGNA = DES.NUMERO), " +

					"    DECODE(INTERESADO.ANIOEJG,NULL,'0',INTERESADO.ANIOEJG || '/' || INTERESADO.NUMEJG)) "
					+ "     AS NUMERO_EJG, " +

					"         INTERESADO.NOMBRE || ' ' || INTERESADO.APELLIDO1 || ' ' || INTERESADO.APELLIDO2 AS NOMBRE_DEFENDIDO, "
					+ "         INTERESADO.DIRECCION AS DOMICILIO_DEFENDIDO,"
					+ "         INTERESADO.CODIGOPOSTAL AS CP_DEFENDIDO,"
					+ "         INTERESADO.NOMBRE_POB AS POBLACION_DEFENDIDO, "
					+ "         INTERESADO.NOMBRE_PROV AS PROVINCIA_DEFENDIDO,"
					+ "         INTERESADO.TELEFONO AS TELEFONO1_DEFENDIDO,"
					+ "         DECODE(INTERESADO.SEXO,null,null,'M',";
			contador++;
			codigos.put(new Integer(contador), lenguaje);

			query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.mujer',:" + contador + "),";
			contador++;
			codigos.put(new Integer(contador), lenguaje);

			query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaEJG.sexo.hombre',:" + contador
					+ ")) AS SEXO_DEFENDIDO," +

					"         INTERESADO.IDLENGUAJE AS IDLENGUAJE_DEFENDIDO,"
					+ "         DECODE(INTERESADO.CALIDAD,null,'','D',";
			contador++;
			codigos.put(new Integer(contador), lenguaje);

			query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaJG.calidad.literal.demandante',:"
					+ contador + "),";
			contador++;
			codigos.put(new Integer(contador), lenguaje);

			query += "                F_SIGA_GETRECURSO_ETIQUETA('gratuita.personaJG.calidad.literal.demandado',:"
					+ contador + ")) AS CALIDAD_DEFENDIDO," + "         INTERESADO.OBSERVACIONES AS OBS_INTERESADO, "
					+ "		  F_SIGA_GETCODIDIOMA(INTERESADO.IDLENGUAJE) AS CODIGOLENGUAJE, ";

			query += "F_SIGA_GETINTERESADOSDESIGNA(DES.IDINSTITUCION,DES.ANIO,DES.IDTURNO,DES.NUMERO,0) LISTA_INTERESADOS_DESIGNA,";
			query += "F_SIGA_GETACTUACIONESDESIGNA(DES.IDINSTITUCION,DES.ANIO,DES.IDTURNO,DES.NUMERO) LISTA_ACTUACIONES_DESIGNA,";
			query += " TO_CHAR(SYSDATE, 'dd') AS DIA_ACTUAL,"
					+ "         TO_CHAR(SYSDATE, 'MONTH', 'NLS_DATE_LANGUAGE = SPANISH') AS MES_ACTUAL,"
					+ "         TO_CHAR(SYSDATE, 'yyyy') AS ANIO_ACTUAL,"
					+

					"         EJGD.FECHARESOLUCIONCAJG AS FECHARESOLUCIONCAJG "
					+ ",   F_SIGA_GETFIRSTASISDESIGNA(DES.IDINSTITUCION,DES.ANIO, DES.IDTURNO, DES.NUMERO) AS FECHA_ASISTENCIA "
					+

					"   FROM V_SIGA_DESIGNACIONES DES," + "        V_SIGA_EJG_DESIGNA EJGD,"
					+ "        V_SIGA_DATOSLETRADO_COLEGIADO COL," + "        V_SIGA_JUZGADOS JUZ,"
					+ "        V_SIGA_INTERESADOS_DESIGNA INTERESADO," + "        V_SIGA_PROCURADOR_DESIGNA PROC"
					+ "  WHERE DES.IDINSTITUCION = COL.IDINSTITUCION(+)"
					+ "    AND DES.IDPERSONA_DESIGNADA = COL.IDPERSONA(+)"
					+ "    AND DES.IDINSTITUCION = EJGD.IDINSTITUCION (+)"
					+ "    AND DES.IDTURNO = EJGD.IDTURNO_DES (+)" + "    AND DES.ANIO = EJGD.ANIO_DES (+)"
					+ "    AND DES.NUMERO = EJGD.NUMERO_DES (+)"
					+ "    AND DES.IDINSTITUCION_JUZG = JUZ.IDINSTITUCION (+)"
					+ "    AND DES.IDJUZGADO = JUZ.IDJUZGADO (+)";
			query += "    AND DES.IDINSTITUCION = INTERESADO.IDINSTITUCION (+)"
					+ "    AND DES.IDTURNO = INTERESADO.IDTURNO (+)" + "    AND DES.ANIO = INTERESADO.ANIO (+)"
					+ "    AND DES.NUMERO = INTERESADO.NUMERO (+)";
			query += "    AND DES.IDINSTITUCION = PROC.IDINSTITUCION (+)" + "    AND DES.IDTURNO = PROC.IDTURNO (+)"
					+ "    AND DES.ANIO = PROC.ANIO (+)" + "    AND DES.NUMERO = PROC.NUMERO (+)";
			contador++;
			codigos.put(new Integer(contador), idinstitucion);

			query += "   and des.IDINSTITUCION =:" + contador + "";

			contador++;
			codigos.put(new Integer(contador), codigo);

			query += "   and des.CODIGO =:" + contador + "";

			if (idPersonaJG != null && !idPersonaJG.equalsIgnoreCase("")) {

				contador++;
				codigos.put(new Integer(contador), idPersonaJG);
				query += "   and INTERESADO.IDPERSONAJG =:" + contador + "";

			}

			if (rc.findBind(query, codigos)) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable resultado = fila.getRow();
					datos.add(resultado);
					if (!isASolicitantes) {
						break;
					}

				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, " Error al obtener la informacion sobre listas de guardias.");
		}
		return datos;
	} // getDatosPlantillas ()
*/
	public static String getNombreGuardiaJSP(String institucion, String idturno, String idguardia) throws ClsExceptions, SIGAException
	{
		String datos = "";
		try {
			RowsContainer rc = new RowsContainer();

			if (institucion != null && !institucion.equals("") && idturno != null && !idturno.equals("")
					&& idguardia != null && !idguardia.equals("")) {

				Hashtable codigos = new Hashtable();
				int contador = 0;

				String sql = "select NOMBRE " + " from SCS_GUARDIASTURNO ";

				contador++;
				codigos.put(new Integer(contador), institucion);
				sql += " WHERE IDINSTITUCION =:" + contador;

				contador++;
				codigos.put(new Integer(contador), idturno);
				sql += " AND IDTURNO =:" + contador;

				contador++;
				codigos.put(new Integer(contador), idguardia);
				sql += " AND IDGUARDIA =:" + contador;

				if (rc.findBind(sql, codigos)) {
					for (int i = 0; i < rc.size(); i++) {
						Row fila = (Row) rc.get(i);
						Hashtable resultado = fila.getRow();
						datos = (String) resultado.get("NOMBRE");
					}
				}
			}
		} catch (Exception e) {

			throw new ClsExceptions(e, "Error al ejecutar consulta.");
		}
		return datos;
	}	
	
	public List<ScsGuardiasTurnoBean> getGuardiasTurnos(VolantesExpressVo volanteExpres) throws ClsExceptions
	{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();

		if (volanteExpres.getFechaGuardia() != null || volanteExpres.getIdColegiado() != null) {
			// if(volanteExpres.getIdColegiado()!=null){
			if (false) {
				sql.append(" SELECT GUA.IDGUARDIA, GUA.NOMBRE, GUA.IDTURNO, GUA.IDINSTITUCION ");
				sql.append(" FROM SCS_GUARDIASTURNO GUA,SCS_CALENDARIOGUARDIAS GC, SCS_CABECERAGUARDIAS CG ");
				sql.append(" WHERE GUA.IDINSTITUCION = :");
				contador++;
				sql.append(contador);
				htCodigos.put(new Integer(contador), volanteExpres.getIdInstitucion());
				sql.append(" AND GUA.IDTURNO = :");
				contador++;
				sql.append(contador);
				htCodigos.put(new Integer(contador), volanteExpres.getIdTurno());
				sql.append(" AND  GC.IDINSTITUCION = GUA.IDINSTITUCION ");
				sql.append(" AND GC.IDTURNO = GUA.IDTURNO ");
				sql.append(" AND GC.IDGUARDIA = GUA.IDGUARDIA ");
				sql.append(" AND GC.IDINSTITUCION = CG.IDINSTITUCION ");
				sql.append(" AND GC.IDTURNO = CG.IDTURNO ");
				sql.append(" AND GC.IDGUARDIA = CG.IDGUARDIA ");
				sql.append(" AND GC.IDCALENDARIOGUARDIAS = CG.IDCALENDARIOGUARDIAS ");
				sql.append(" AND CG.IDPERSONA = :");
				contador++;
				sql.append(contador);
				htCodigos.put(new Integer(contador), volanteExpres.getIdColegiado());

				sql.append(" AND :");
				contador++;
				String truncFechaGuardia = GstDate.getFormatedDateShort("", volanteExpres.getFechaGuardia());
				htCodigos.put(new Integer(contador), truncFechaGuardia);
				sql.append(contador);
				sql.append(" BETWEEN TRUNC(CG.FECHAINICIO) AND ");
				sql.append(" TRUNC(CG.FECHA_FIN) ");
				sql.append(" ORDER BY GUA.NOMBRE ");

			} else {
				sql.append(" SELECT GUA.IDGUARDIA, GUA.NOMBRE, GUA.IDTURNO, GUA.IDINSTITUCION ");
				sql.append(" FROM SCS_GUARDIASTURNO GUA,SCS_CALENDARIOGUARDIAS GC ");
				sql.append(" WHERE GUA.IDINSTITUCION = :");
				contador++;
				sql.append(contador);
				htCodigos.put(new Integer(contador), volanteExpres.getIdInstitucion());
				sql.append(" AND GUA.IDTURNO = :");
				contador++;
				sql.append(contador);
				htCodigos.put(new Integer(contador), volanteExpres.getIdTurno());
				sql.append(" AND  GC.IDINSTITUCION = GUA.IDINSTITUCION ");
				sql.append(" AND GC.IDTURNO = GUA.IDTURNO ");
				sql.append(" AND GC.IDGUARDIA = GUA.IDGUARDIA ");
				sql.append(" AND :");
				contador++;
				String truncFechaGuardia = GstDate.getFormatedDateShort("", volanteExpres.getFechaGuardia());
				htCodigos.put(new Integer(contador), truncFechaGuardia);
				sql.append(contador);
				sql.append(" BETWEEN TRUNC(GC.FECHAINICIO) AND ");
				sql.append(" TRUNC(GC.FECHAFIN) ");
				sql.append(" ORDER BY GUA.NOMBRE ");
			}

		} else {
			sql.append(" SELECT IDGUARDIA, NOMBRE,IDTURNO,IDINSTITUCION FROM SCS_GUARDIASTURNO ");
			sql.append(" WHERE IDINSTITUCION =:");
			contador++;
			sql.append(contador);
			htCodigos.put(new Integer(contador), volanteExpres.getIdInstitucion());
			sql.append(" AND IDTURNO = :");
			contador++;
			sql.append(contador);
			htCodigos.put(new Integer(contador), volanteExpres.getIdTurno());
			sql.append(" ORDER BY NOMBRE ");

		}
		List<ScsGuardiasTurnoBean> alGuardias = null;
		try {
			RowsContainer rc = new RowsContainer();

			if (rc.findBind(sql.toString(), htCodigos)) {
				alGuardias = new ArrayList<ScsGuardiasTurnoBean>();
				ScsGuardiasTurnoBean guardiaBean = null;
				if (rc.size() > 1) {
					guardiaBean = new ScsGuardiasTurnoBean();
					guardiaBean.setIdGuardia(new Integer(-1));
					guardiaBean.setNombre(UtilidadesString.getMensajeIdioma(volanteExpres.getUsrBean(),
							"general.combo.seleccionar"));
					alGuardias.add(guardiaBean);
				}
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();

					guardiaBean = new ScsGuardiasTurnoBean();
					guardiaBean.setIdInstitucion(UtilidadesHash
							.getInteger(htFila, ScsGuardiasTurnoBean.C_IDINSTITUCION));
					guardiaBean.setIdTurno(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_IDTURNO));
					guardiaBean.setNombre(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_NOMBRE));
					guardiaBean.setIdGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_IDGUARDIA));
					alGuardias.add(guardiaBean);
				}
			}
		} catch (Exception e) {
			ClsLogging.writeFileLog("VOLANTES EXPRESS:Error Select Guardias" + e.toString(), 10);
			throw new ClsExceptions(e, "Error al ejecutar consulta.");
		}

		return alGuardias;
	}

	/**
	 * Obtiene las guardias de un turno
	 * 
	 * @param idTurno
	 * @param idInstitucion
	 * @param isCombo
	 * @return
	 * @throws ClsExceptions
	 */
	public List<ScsGuardiasTurnoBean> getGuardiasTurnos(Integer idTurno, Integer idInstitucion, boolean isCombo) throws ClsExceptions {
		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT IDGUARDIA, "); 
		sql.append(" NOMBRE, ");
		sql.append(" IDTURNO, ");
		sql.append(" IDINSTITUCION "); 
		sql.append(" FROM SCS_GUARDIASTURNO ");
		
		sql.append(" WHERE IDINSTITUCION =:");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idInstitucion);
		
		sql.append(" AND IDTURNO = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idTurno);
		
		sql.append(" ORDER BY NOMBRE ");

		List<ScsGuardiasTurnoBean> alGuardias = null;
		try {
			RowsContainer rc = new RowsContainer();
			if (rc.findBind(sql.toString(), htCodigos)) {
				
				alGuardias = new ArrayList<ScsGuardiasTurnoBean>();
				ScsGuardiasTurnoBean guardiaBean = null;
				if (isCombo) {
					if (rc.size() > 1) {
						guardiaBean = new ScsGuardiasTurnoBean();
						guardiaBean.setIdGuardia(new Integer(-1));
						guardiaBean.setNombre(UtilidadesString.getMensajeIdioma(this.usrbean, "general.combo.seleccionar"));
						alGuardias.add(guardiaBean);
					}
				}
				
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();

					guardiaBean = new ScsGuardiasTurnoBean();
					guardiaBean.setIdInstitucion(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_IDINSTITUCION));
					guardiaBean.setIdTurno(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_IDTURNO));
					guardiaBean.setNombre(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_NOMBRE));
					guardiaBean.setIdGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_IDGUARDIA));
					
					alGuardias.add(guardiaBean);
				}
			}
			
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar consulta.");
		}
		
		return alGuardias;
	}	
	
	public List<ScsGuardiasTurnoBean> getGuardiasTurnosVinculadas(Integer idTurno,Integer idGuardia, Integer idInstitucion) throws ClsExceptions
	{
		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT GT.*,");
		sql.append("T.ABREVIATURA ABREVIATURATURNO ,T.NOMBRE NOMBRETURNO FROM SCS_GUARDIASTURNO GT,SCS_TURNO T ");
		sql.append(" WHERE GT.IDTURNO= T.IDTURNO AND GT.IDINSTITUCION = T.IDINSTITUCION ");
		
		sql.append(" AND GT.IDINSTITUCIONPRINCIPAL =:");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idInstitucion);
		sql.append(" AND GT.IDTURNOPRINCIPAL = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idTurno);
		sql.append(" AND GT.IDGUARDIAPRINCIPAL = :");
		contador++;
		sql.append(contador);
		htCodigos.put(new Integer(contador), idGuardia);
		sql.append(" ORDER BY GT.NOMBRE ");

		List<ScsGuardiasTurnoBean> alGuardias = null;
		try {
			RowsContainer rc = new RowsContainer();

			if (rc.findBind(sql.toString(), htCodigos)) {
				alGuardias = new ArrayList<ScsGuardiasTurnoBean>();
				ScsGuardiasTurnoBean guardiaBean = null;
				ScsTurnoBean turnoBean = null;
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable<String, Object> htFila = fila.getRow();
					turnoBean = new ScsTurnoBean();
					guardiaBean = new ScsGuardiasTurnoBean();
					guardiaBean.setTurno(turnoBean);
					guardiaBean.setIdInstitucion(UtilidadesHash
							.getInteger(htFila, ScsGuardiasTurnoBean.C_IDINSTITUCION));
					guardiaBean.setIdTurno(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_IDTURNO));
					guardiaBean.setNombre(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_NOMBRE));
					guardiaBean.setDescripcion(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_DESCRIPCION));
					guardiaBean.setDescripcionFacturacion(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_DESCRIPCIONFACTURACION));
					guardiaBean.setDescripcionPago(UtilidadesHash.getString(htFila, ScsGuardiasTurnoBean.C_DESCRIPCIONPAGO));
					guardiaBean.setIdGuardia(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_IDGUARDIA));
					turnoBean.setIdTurno(UtilidadesHash.getInteger(htFila, ScsGuardiasTurnoBean.C_IDTURNO));
					turnoBean.setAbreviatura(UtilidadesHash.getString(htFila, "ABREVIATURATURNO"));
					turnoBean.setNombre(UtilidadesHash.getString(htFila, "NOMBRETURNO"));
					alGuardias.add(guardiaBean);
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar consulta.");
		}
		return alGuardias;

	}	

	/**
	 * Cambia el ultimo letrado de la cola de la guardia indicada por el nuevo que se ha solicitado
	 */
	public void cambiarUltimoCola (Integer idInstitucion,
								   Integer idTurno,
								   Integer idGuardia,
								   Long idPersona_Ultimo,
								   String fechaSuscripcion_Ultimo,
								   Long idGrupoGuardiaColegiado_Ultimo)
		throws ClsExceptions
	{
		String sIdinstitucion = idInstitucion.toString();
		String sIdTurno = idTurno.toString();
		String sIdGuardia = idGuardia.toString();
		String sIdpersona = (idPersona_Ultimo == null) ? "null" : idPersona_Ultimo.toString();
		String sFechaSusc = (fechaSuscripcion_Ultimo == null || fechaSuscripcion_Ultimo.equals("")) ?
				"null" : fechaSuscripcion_Ultimo.toString();
		String sIdGrupoGuardiaColegiado_Ultimo = (idGrupoGuardiaColegiado_Ultimo == null) ? "null" : idGrupoGuardiaColegiado_Ultimo.toString();
		
		String[] campos = 
		{
				ScsGuardiasTurnoBean.C_IDPERSONA_ULTIMO,
				ScsGuardiasTurnoBean.C_IDGRUPOGUARDIACOLEGIADO_ULTIMO,
				ScsGuardiasTurnoBean.C_FECHASUSCRIPCION_ULTIMO,
				ScsGuardiasTurnoBean.C_USUMODIFICACION,
				ScsGuardiasTurnoBean.C_FECHAMODIFICACION
		};
		
		Hashtable hash = new Hashtable();
		hash.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, sIdinstitucion);
		hash.put(ScsGuardiasTurnoBean.C_IDTURNO, sIdTurno);
		hash.put(ScsGuardiasTurnoBean.C_IDGUARDIA, sIdGuardia);
		hash.put(ScsGuardiasTurnoBean.C_IDPERSONA_ULTIMO, sIdpersona);
		hash.put(ScsGuardiasTurnoBean.C_IDGRUPOGUARDIACOLEGIADO_ULTIMO, sIdGrupoGuardiaColegiado_Ultimo);
		hash.put(ScsGuardiasTurnoBean.C_FECHASUSCRIPCION_ULTIMO, sFechaSusc);
		hash.put(ScsGuardiasTurnoBean.C_USUMODIFICACION, this.usrbean.getUserName());
		hash.put(ScsGuardiasTurnoBean.C_FECHAMODIFICACION, "SYSDATE");
		
		this.updateDirect(hash, this.getClavesBean(), campos);
	} // cambiarUltimoCola()
	public ScsGuardiasTurnoBean getGuardiaTurno(String idInstitucion, String idTurno, String idGuardia) throws ClsExceptions{
		StringBuffer sql = new StringBuffer();
		sql.append("WHERE " + ScsGuardiasTurnoBean.C_IDINSTITUCION + "=" + idInstitucion);
		sql.append(" AND " + ScsGuardiasTurnoBean.C_IDTURNO + "=" + idTurno);
		sql.append(" AND " + ScsGuardiasTurnoBean.C_IDGUARDIA + "=" + idGuardia);
		
		ScsGuardiasTurnoBean guardiaBean = null;
		try {
			Vector v = this.select(sql.toString());
			if (v == null || v.isEmpty())
				throw new Exception();
			guardiaBean = (ScsGuardiasTurnoBean) v.get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ClsExceptions("Error al buscar la guardia del calendario");
		}
		
			
		
		
		return guardiaBean;
	}
	

	public Vector<ScsGuardiasTurnoBean> getGuardiasVinculadas(
			Integer idInstitucion, Integer idTurno, Integer idGuardia) throws ClsExceptions {
		StringBuffer where = new StringBuffer();
		where.append("WHERE idinstitucionprincipal = ");
		where.append(idInstitucion);
		where.append(" AND idturnoprincipal =");
		where.append( idTurno);
		where.append(" AND idguardiaprincipal =");
		where.append( idGuardia);
		
		Vector<ScsGuardiasTurnoBean> guardiasVinculadas = null;
		try {
			guardiasVinculadas = this.select(where.toString());
			if (guardiasVinculadas == null)
				guardiasVinculadas = new Vector<ScsGuardiasTurnoBean>();
		} catch (Exception e) {
			throw new ClsExceptions("Error al buscar la guardia del calendario");
		}
		return guardiasVinculadas;
	}
	
	
}