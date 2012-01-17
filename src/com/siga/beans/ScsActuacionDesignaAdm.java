
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.AcreditacionForm;
import com.siga.gratuita.form.ActuacionDesignaForm;
import com.siga.gratuita.form.DesignaForm;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_DESIGNA
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 * @version 27/01/2006 (david.sacnhezp) modificaciones para los campos nuevos de combos.
 */
public class ScsActuacionDesignaAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsActuacionDesignaAdm (UsrBean usuario) {
		super( ScsActuacionDesignaBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	public String[] getCamposBean() {
		String[] campos = {	ScsActuacionDesignaBean.C_IDINSTITUCION,				ScsActuacionDesignaBean.C_IDTURNO,
							ScsActuacionDesignaBean.C_ANIO,							ScsActuacionDesignaBean.C_NUMERO,
							ScsActuacionDesignaBean.C_FECHAMODIFICACION,			ScsActuacionDesignaBean.C_USUMODIFICACION,
							ScsActuacionDesignaBean.C_FECHA,						ScsActuacionDesignaBean.C_NUMEROASUNTO,
							ScsActuacionDesignaBean.C_ACUERDOEXTRAJUDICIAL,			ScsActuacionDesignaBean.C_ANULACION,
							ScsActuacionDesignaBean.C_IDPROCEDIMIENTO,				ScsActuacionDesignaBean.C_LUGAR,
							ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION,	ScsActuacionDesignaBean.C_OBSERVACIONES,			
							ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,			ScsActuacionDesignaBean.C_FACTURADO,
							ScsActuacionDesignaBean.C_PAGADO,
							ScsActuacionDesignaBean.C_IDFACTURACION,				ScsActuacionDesignaBean.C_VALIDADA,
							ScsActuacionDesignaBean.C_IDJUZGADO,				    ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO,
							ScsActuacionDesignaBean.C_IDCOMISARIA,				    ScsActuacionDesignaBean.C_IDINSTITUCIONCOMISARIA,
							ScsActuacionDesignaBean.C_IDPRISION,				    ScsActuacionDesignaBean.C_IDINSTITUCIONPRISION,
							ScsActuacionDesignaBean.C_IDACREDITACION,				ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO,
							ScsActuacionDesignaBean.C_IDPERSONACOLEGIADO,			ScsActuacionDesignaBean.C_IDPRETENSION,
		    				ScsActuacionDesignaBean.C_TALONARIO,					ScsActuacionDesignaBean.C_TALON,
		    				ScsActuacionDesignaBean.C_NUMEROPROCEDIMIENTO,			ScsActuacionDesignaBean.C_NIG};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	public String[] getClavesBean() {
		String[] campos = {	ScsActuacionDesignaBean.C_IDINSTITUCION,		ScsActuacionDesignaBean.C_IDTURNO,
							ScsActuacionDesignaBean.C_ANIO,					ScsActuacionDesignaBean.C_NUMERO,
							ScsActuacionAsistenciaBean.C_NUMEROASUNTO};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsActuacionDesignaBean bean = null;
		try{
			bean = new ScsActuacionDesignaBean();
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsActuacionDesignaBean.C_ANIO));
			bean.setAcuerdoExtraJudicial(UtilidadesHash.getInteger(hash,ScsActuacionDesignaBean.C_ACUERDOEXTRAJUDICIAL));
			bean.setAnulacion(UtilidadesHash.getInteger(hash,ScsActuacionDesignaBean.C_ANULACION));
			bean.setFecha(UtilidadesHash.getString(hash,ScsActuacionDesignaBean.C_FECHA));
			bean.setFechaJustificacion(UtilidadesHash.getString(hash,ScsActuacionDesignaBean.C_FECHAJUSTIFICACION));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsActuacionDesignaBean.C_FECHAMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ScsActuacionDesignaBean.C_IDINSTITUCION));
			bean.setIdProcedimiento(UtilidadesHash.getString(hash, ScsActuacionDesignaBean.C_IDPROCEDIMIENTO));
			bean.setIdTurno(UtilidadesHash.getInteger(hash, ScsActuacionDesignaBean.C_IDTURNO));
			bean.setLugar(UtilidadesHash.getString(hash, ScsActuacionDesignaBean.C_LUGAR));
			bean.setNumero(UtilidadesHash.getLong(hash, ScsActuacionDesignaBean.C_NUMERO));
			bean.setNumeroAsunto(UtilidadesHash.getLong(hash, ScsActuacionDesignaBean.C_NUMEROASUNTO));
			bean.setObservaciones(UtilidadesHash.getString(hash, ScsActuacionDesignaBean.C_OBSERVACIONES));
			bean.setObservacionesJustificacion(UtilidadesHash.getString(hash, ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsActuacionDesignaBean.C_USUMODIFICACION));			
			bean.setIdJuzgado(UtilidadesHash.getLong(hash, ScsActuacionDesignaBean.C_IDJUZGADO));
			bean.setIdInstitucionJuzgado(UtilidadesHash.getInteger(hash, ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO));
			bean.setIdComisaria(UtilidadesHash.getLong(hash, ScsActuacionDesignaBean.C_IDCOMISARIA));
			bean.setIdInstitucionComisaria(UtilidadesHash.getInteger(hash, ScsActuacionDesignaBean.C_IDINSTITUCIONCOMISARIA));
			bean.setIdPrision(UtilidadesHash.getLong(hash, ScsActuacionDesignaBean.C_IDPRISION));
			bean.setIdInstitucionPrision(UtilidadesHash.getInteger(hash, ScsActuacionDesignaBean.C_IDINSTITUCIONPRISION));
			bean.setIdInstitucionProcedimiento(UtilidadesHash.getInteger(hash, ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO));
			bean.setIdAcreditacion(UtilidadesHash.getInteger(hash, ScsActuacionDesignaBean.C_IDACREDITACION));
			bean.setIdPersonaColegiado(UtilidadesHash.getLong(hash, ScsActuacionDesignaBean.C_IDPERSONACOLEGIADO));
			bean.setValidada(UtilidadesHash.getString(hash, ScsActuacionDesignaBean.C_VALIDADA));
			bean.setFacturado(UtilidadesHash.getString(hash, ScsActuacionDesignaBean.C_FACTURADO));
			bean.setIdPretension(UtilidadesHash.getInteger(hash, ScsActuacionDesignaBean.C_IDPRETENSION));
		    bean.setTalonario(UtilidadesHash.getString(hash, ScsActuacionDesignaBean.C_TALONARIO));
		    bean.setTalon(UtilidadesHash.getString(hash, ScsActuacionDesignaBean.C_TALON));
		    bean.setNumeroProcedimiento(UtilidadesHash.getString(hash, ScsActuacionDesignaBean.C_NUMEROPROCEDIMIENTO));
		    bean.setNig(UtilidadesHash.getString(hash, ScsActuacionDesignaBean.C_NIG));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsActuacionDesignaBean b = (ScsActuacionDesignaBean) bean;
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_ANIO, String.valueOf(b.getAnio()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_IDTURNO, String.valueOf(b.getIdTurno()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_NUMERO, String.valueOf(b.getNumero()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_OBSERVACIONES,b.getObservaciones());
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_ACUERDOEXTRAJUDICIAL, String.valueOf(b.getAcuerdoExtraJudicial()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_ANULACION, String.valueOf(b.getAnulacion()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_FECHA , b.getFecha());
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_FECHAJUSTIFICACION , b.getFechaJustificacion());
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_IDPROCEDIMIENTO , b.getIdProcedimiento());
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_LUGAR , b.getLugar());
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_NUMEROASUNTO , String.valueOf(b.getNumeroAsunto()));
			if(b.getObservacionesJustificacion() != null){
				UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_OBSERVACIONESJUSTIFICACION , b.getObservacionesJustificacion());
			}else{
				UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_OBSERVACIONES,b.getObservaciones());
			}
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_IDJUZGADO, String.valueOf(b.getIdJuzgado()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO, String.valueOf(b.getIdInstitucionJuzgado()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_IDCOMISARIA, String.valueOf(b.getIdComisaria()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_IDINSTITUCIONCOMISARIA, String.valueOf(b.getIdInstitucionComisaria()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_IDPRISION, String.valueOf(b.getIdPrision()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_IDINSTITUCIONPRISION, String.valueOf(b.getIdInstitucionPrision()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO, String.valueOf(b.getIdInstitucionProcedimiento()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_IDACREDITACION, String.valueOf(b.getIdAcreditacion()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_IDPERSONACOLEGIADO, String.valueOf(b.getIdPersonaColegiado()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_VALIDADA, b.getValidada());
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_IDPRETENSION, String.valueOf(b.getIdPretension()));
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_TALONARIO,b.getTalonario());
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_TALON,b.getTalon());
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_NUMEROPROCEDIMIENTO,b.getNumeroProcedimiento());
			UtilidadesHash.set(hash, ScsActuacionDesignaBean.C_NIG,b.getNig());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		return null;
	}
	
	
	/** Funcion ejecutaSelect(String select)
	 *	@param select sentencia "select" sql valida, sin terminar en ";"
	 *  @return Vector todos los registros que se seleccionen 
	 *  en BBDD debido a la ejecucion de la sentencia select
	 *
	 */
	public Vector ejecutaSelect(String select) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
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
	}
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador del turno que se va a insertar. 
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		String values;	
		RowsContainer rc = null;
		int contador = 0;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT (MAX("+ScsActuacionAsistenciaBean.C_NUMEROASUNTO+") + 1) AS NUMEROASUNTO FROM " + nombreTabla + 
					" WHERE IDINSTITUCION =" + (String)entrada.get("IDINSTITUCION") +
					" AND ANIO =" + (String)entrada.get("ANIO")+
					" AND IDTURNO =" + (String)entrada.get("IDTURNO")+
					" AND NUMERO =" +(String)entrada.get("NUMERO");	
		try {		
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("NUMEROASUNTO").equals("")) {
					entrada.put(ScsActuacionDesignaBean.C_NUMEROASUNTO,"1");
				}
				else entrada.put(ScsActuacionDesignaBean.C_NUMEROASUNTO,(String)prueba.get("NUMEROASUNTO"));								
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert' en B.D.");		
		}
		return entrada;
	}
	public ArrayList existeActuacionesSinJustificar(Hashtable entrada)throws ClsExceptions 
	{
			
		RowsContainer rc = null;
		ArrayList alHash = new ArrayList();
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT  act.NUMEROASUNTO NUMEROASUNTO,act.FECHAJUSTIFICACION FECHAJUSTIFICACION"
			+",act.FECHA FECHA,act.IDACREDITACION IDACREDITACION,act.VALIDADA VALIDADA," +
					"acr.IDTIPOACREDITACION IDTIPOACREDITACION FROM " + nombreTabla + 
					" act,scs_acreditacion acr WHERE IDINSTITUCION =" + (String)entrada.get("IDINSTITUCION") +
					" AND act.ANIO =" + (String)entrada.get("ANIO")+
					" AND act.IDTURNO =" + (String)entrada.get("IDTURNO")+
					" AND act.NUMERO = "+(String)entrada.get("NUMERO")+
					" and acr.idacreditacion = act.idacreditacion";
					sql +=" AND (FECHAJUSTIFICACION is null or act.validada='0')";
		try {		
			if (rc.query(sql)){ 
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable row = fila.getRow();
					Hashtable clonEntrada = (Hashtable)entrada.clone();
					String idAcreditacion  =  (String)row.get(ScsActuacionDesignaBean.C_IDACREDITACION);
					if(idAcreditacion!=null && !idAcreditacion.equalsIgnoreCase(""))
						clonEntrada.put(ScsActuacionDesignaBean.C_IDACREDITACION,idAcreditacion);
					String fechaJustificacion  =  (String)row.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION);
					if(fechaJustificacion!=null && !fechaJustificacion.equalsIgnoreCase(""))
						clonEntrada.put(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,fechaJustificacion);
					String fecha  =  (String)row.get(""+ScsActuacionDesignaBean.C_FECHA);
					if(fecha!=null && !fecha.equalsIgnoreCase(""))
						clonEntrada.put(ScsActuacionDesignaBean.C_FECHA,fecha);
					String tipoAcreditacion = (String)row.get(ScsAcreditacionBean.C_IDTIPOACREDITACION);
					if(tipoAcreditacion!=null && !tipoAcreditacion.equalsIgnoreCase(""))
						clonEntrada.put(ScsAcreditacionBean.C_IDTIPOACREDITACION,tipoAcreditacion);
					String numeroAsunto = (String)row.get(ScsActuacionDesignaBean.C_NUMEROASUNTO);
					if(numeroAsunto!=null && !numeroAsunto.equalsIgnoreCase(""))
						clonEntrada.put(ScsActuacionDesignaBean.C_NUMEROASUNTO,numeroAsunto);
					alHash.add(clonEntrada);
				} 
				
			}
			

							
			
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'existeActuacionSinJustificar' en B.D.");		
		}
		return alHash;
		
	}
	
	public Hashtable existeActuacionSinJustificar(Hashtable entrada)throws ClsExceptions 
	{
			
		RowsContainer rc = null;

		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT  NUMEROASUNTO,FECHAJUSTIFICACION,FECHA,IDACREDITACION FROM " + nombreTabla + 
					" WHERE IDINSTITUCION =" + (String)entrada.get("IDINSTITUCION") +
					" AND ANIO =" + (String)entrada.get("ANIO")+
					" AND IDTURNO =" + (String)entrada.get("IDTURNO")+
					" AND NUMERO = "+(String)entrada.get("NUMERO");
					//if(isInicioYFinal){
						//sql +=" AND IDACREDITACION = "+(String)entrada.get("IDACREDITACION");
					//}else{
						//sql +=" AND IDACREDITACION = "+(String)entrada.get("IDACREDITACION");
						
					//}
						sql +=" AND FECHAJUSTIFICACION is null ";
		try {		
			if (rc.query(sql)){ 
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				String idAcreditacion  =  (String)prueba.get(ScsActuacionDesignaBean.C_IDACREDITACION);
				if(idAcreditacion!=null && !idAcreditacion.equalsIgnoreCase(""))
					entrada.put(ScsActuacionDesignaBean.C_IDACREDITACION,idAcreditacion);
				String fechaJustificacion  =  (String)prueba.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION);
				if(fechaJustificacion!=null && !fechaJustificacion.equalsIgnoreCase(""))
					entrada.put(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,fechaJustificacion);
				String fecha  =  (String)prueba.get(ScsActuacionDesignaBean.C_FECHA);
				if(fecha!=null && !fecha.equalsIgnoreCase(""))
					entrada.put(ScsActuacionDesignaBean.C_FECHA,fecha);
				entrada.put(ScsActuacionDesignaBean.C_NUMEROASUNTO,(String)prueba.get(ScsActuacionDesignaBean.C_NUMEROASUNTO));
			}
			

							
			
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'existeActuacionSinJustificar' en B.D.");		
		}
		return entrada;
		
	}
	
	
	public Hashtable prepararUpdate(Hashtable entrada)throws ClsExceptions 
	{
			
		RowsContainer rc = null;

		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT NUMEROASUNTO,FECHAJUSTIFICACION,FECHA FROM " + nombreTabla + 
					" WHERE IDINSTITUCION =" + (String)entrada.get("IDINSTITUCION") +
					" AND ANIO =" + (String)entrada.get("ANIO")+
					" AND IDTURNO =" + (String)entrada.get("IDTURNO")+
					" AND NUMERO = "+(String)entrada.get("NUMERO");
					//if(isInicioYFinal){
						//sql +=" AND IDACREDITACION = "+(String)entrada.get("IDACREDITACION");
					//}else{
						sql +=" AND IDACREDITACION = "+(String)entrada.get("IDACREDITACION");
						
					//}
					//" AND FECHAJUSTIFICACION is null ";
		try {		
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				String fechaJustificacion  =  (String)prueba.get(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION);
				if(fechaJustificacion!=null && !fechaJustificacion.equalsIgnoreCase(""))
					entrada.put(ScsActuacionDesignaBean.C_FECHAJUSTIFICACION,fechaJustificacion);
				String fecha  =  (String)prueba.get(ScsActuacionDesignaBean.C_FECHA);
				if(fecha!=null && !fecha.equalsIgnoreCase(""))
					entrada.put(ScsActuacionDesignaBean.C_FECHA,fecha);
				/*String idAcreditacion  =  (String)prueba.get(ScsActuacionDesignaBean.C_IDACREDITACION);
				if(idAcreditacion!=null && !idAcreditacion.equalsIgnoreCase(""))
					entrada.put(ScsActuacionDesignaBean.C_IDACREDITACION,idAcreditacion);*/
				entrada.put(ScsActuacionDesignaBean.C_NUMEROASUNTO,(String)prueba.get(ScsActuacionDesignaBean.C_NUMEROASUNTO));

							
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararUpdate' en B.D.");		
		}
		return entrada;
	}
	
	
		
	/** 
	 * Recoge informacion relativa a las actuaciones necesaria para rellenar la carta a los interesados de oficio<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  turno - identificador de turno
	 * @param  anio - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getListaActuaciones (Integer institucion, Integer anio, Integer numero, Integer turno) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
		   try {
			   RowsContainer rc = new RowsContainer();
			   
			   String sql =
				   "select "+
				   "to_char(ad."+ScsActuacionDesignaBean.C_FECHA+",'DD/MM/YYYY') "+ScsActuacionDesignaBean.C_FECHA+","+
				   "ad."+ScsActuacionDesignaBean.C_ANIO+","+
				   "ad."+ScsActuacionDesignaBean.C_NUMERO+","+
				   "decode(ad."+ScsActuacionDesignaBean.C_IDPRISION+",null,"+
				   " (select j."+ScsJuzgadoBean.C_NOMBRE+" from "+ScsJuzgadoBean.T_NOMBRETABLA+" j where j."+ScsJuzgadoBean.C_IDINSTITUCION+"=ad."+ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO+" and j."+ScsJuzgadoBean.C_IDJUZGADO+"=ad."+ScsActuacionDesignaBean.C_IDJUZGADO+"),"+
				   " (select p."+ScsPrisionBean.C_NOMBRE+" from "+ScsPrisionBean.T_NOMBRETABLA+" p where p."+ScsPrisionBean.C_IDINSTITUCION+"=ad."+ScsActuacionDesignaBean.C_IDINSTITUCIONPRISION+" and p."+ScsPrisionBean.C_IDPRISION+"=ad."+ScsActuacionDesignaBean.C_IDPRISION+" )"+
				   ") LUGAR,"+
				   "decode(pr.nombre,null,'',pr.nombre||'('||ap."+ScsAcreditacionProcedimientoBean.C_PORCENTAJE+"||'%)') PROCEDIMIENTO,"+
				   "ad."+ScsActuacionDesignaBean.C_OBSERVACIONES+
				   " from "+
				   ScsActuacionDesignaBean.T_NOMBRETABLA+" ad,"+
				   ScsProcedimientosBean.T_NOMBRETABLA+" pr,"+
				   ScsAcreditacionProcedimientoBean.T_NOMBRETABLA+" ap "+
				   "where ad."+ScsActuacionDesignaBean.C_IDINSTITUCION+"=pr."+ScsProcedimientosBean.C_IDINSTITUCION+"(+)"+
				   "  and ad."+ScsActuacionDesignaBean.C_IDPROCEDIMIENTO+"=pr."+ScsProcedimientosBean.C_IDPROCEDIMIENTO+"(+)"+
				   "  and ad."+ScsActuacionDesignaBean.C_IDINSTITUCION+"=ap."+ScsAcreditacionProcedimientoBean.C_IDINSTITUCION+"(+)"+
				   "  and ad."+ScsActuacionDesignaBean.C_IDPROCEDIMIENTO+"=ap."+ScsAcreditacionProcedimientoBean.C_IDPROCEDIMIENTO+"(+)"+
				   "  and ad."+ScsActuacionDesignaBean.C_IDACREDITACION+"=ap."+ScsAcreditacionProcedimientoBean.C_IDACREDITACION+"(+)"+
				   "  and ad."+ScsActuacionDesignaBean.C_IDINSTITUCION+"="+institucion+
				   "  and ad."+ScsActuacionDesignaBean.C_ANIO+"="+anio+
				   "  and ad."+ScsActuacionDesignaBean.C_IDTURNO+"="+turno+
				   "  and ad."+ScsActuacionDesignaBean.C_NUMERO+"="+numero+
				   "  and ad."+ScsActuacionDesignaBean.C_ANULACION+"<>"+ClsConstants.DB_TRUE+
				   " order by ad."+ScsActuacionDesignaBean.C_FECHA;
			   
			   
			   
			   if (rc.find(sql)) {
				   for (int i = 0; i < rc.size(); i++){
					   Row fila = (Row) rc.get(i);
					   Hashtable resultado=fila.getRow();	                  
					   datos.add(resultado);
				   }
			   } 
		   }
		   catch (Exception e) {
			   throw new ClsExceptions (e, "Error al obtener la informacion sobre una actuacion designa.");
		   }
		   return datos;                        
	}
	
	public int getNumeroActuacionesDeTipo (int tipoAcreditacion, String designaNumero, String designaInstitucion, String  designaTurno, String  designaAnio, 
	        						String idProcedimiento, String  procedimientoInstitucion) {
		   try {
			   RowsContainer rc = new RowsContainer();
			   String sql =  " select count(*) CANTIDAD" +
			    			 " from "  + ScsActuacionDesignaBean.T_NOMBRETABLA + " a, " + 
						    			 ScsAcreditacionBean.T_NOMBRETABLA + " ac " +
			    		     " where " +
				    		     " a." + ScsActuacionDesignaBean.C_IDINSTITUCION + " = " + designaInstitucion + " and " +
					    		 " a." + ScsActuacionDesignaBean.C_IDTURNO + " = " + designaTurno + " and " +
					    		 " a." + ScsActuacionDesignaBean.C_ANIO + " = " + designaAnio + " and " +
					    		 " a." + ScsActuacionDesignaBean.C_NUMERO + " = " + designaNumero + " and " +
					    		 " a." + ScsActuacionDesignaBean.C_IDPROCEDIMIENTO + " = " + idProcedimiento + " and " +
					    		 " a." + ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO + " = " + procedimientoInstitucion + " and " +
					    		 " a." + ScsActuacionDesignaBean.C_IDACREDITACION + " = " + " ac." + ScsAcreditacionBean.C_IDACREDITACION + " and " +
					    		 " ac." + ScsAcreditacionBean.C_IDTIPOACREDITACION + " = " + tipoAcreditacion;
			   if (rc.find(sql)) {
				   Row fila = (Row) rc.get(0);
				   return (UtilidadesHash.getInteger(fila.getRow(), "CANTIDAD")).intValue();
			   } 
			   return 0;
		   }
		   catch (Exception e) {
		       return 0;
		   }
	   }

	public Vector getConsultaDesigna (Hashtable entrada, HttpServletRequest request)throws ClsExceptions 
	  {
		Vector salida=null;				
		String consultaDesigna = " SELECT des.anio anio, des.numero numero, des.idturno idturno, des.idinstitucion idinstitucion,"+
									" per.nombre nombre, per.apellidos1 apellido1, per.apellidos2 apellido2,"+
									" col.ncolegiado ncolegiado, tur.nombre turno, des.fechaentrada fecha, des.FECHAANULACION, "+
									" tur." + ScsTurnoBean.C_VALIDARJUSTIFICACIONES + " validarJustificaciones, " +
									" des.codigo codigo, des.idjuzgado juzgado, des.idinstitucion_juzg institucionjuzgado, des.idprocedimiento procedimiento, "+
									" (select nombre "+
									" from scs_juzgado juz"+
									" where juz.idinstitucion=des.idinstitucion_juzg "+
									"  and  juz.idjuzgado=des.idjuzgado) nombrejuzgado, "+
									" (select nombre "+
									" from scs_procedimientos proc"+
									" where proc.idinstitucion="+entrada.get("IDINSTITUCION")+
									"  and  proc.idprocedimiento=des.idprocedimiento) nombreprocedimiento , "+
								    " ejgdesigna.anioejg anioejg, "+
								    " ejgdesigna.idtipoejg idtipoejg, "+
							        " ejgdesigna.numeroejg numeroejg"+
									" FROM scs_designa des, cen_colegiado col, cen_persona per,scs_turno tur, scs_ejgdesigna ejgdesigna "+
									" WHERE  per.idpersona = F_SIGA_GETIDLETRADO_DESIGNA("+entrada.get("IDINSTITUCION")+","+ entrada.get("IDTURNO")+","+ entrada.get("ANIO")+","+entrada.get("NUMERO")+")"+
									" and col.idpersona = per.idpersona"+
									" and col.idinstitucion = des.idinstitucion"+
									" and tur.idinstitucion = des.idinstitucion"+									
									" and tur.idturno = des.idturno"+
									" and ejgdesigna.idinstitucion(+)=des.idinstitucion"+
									" and ejgdesigna.idturno(+)=des.idturno"+
									" and ejgdesigna.aniodesigna(+)=des.anio"+
									" and ejgdesigna.numerodesigna(+)=des.numero"+
									" and des."+ ScsDesignaBean.C_IDINSTITUCION +"="+  entrada.get("IDINSTITUCION")+
									" and des."+ ScsDesignaBean.C_ANIO +"="+  entrada.get("ANIO")+
									" and des."+ ScsDesignaBean.C_NUMERO +"="+ entrada.get("NUMERO")+
									" and des."+ ScsDesignaBean.C_IDTURNO +"="+ entrada.get("IDTURNO")+" ";
	
		try {	
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsActuacionDesignaAdm designaAdm = new ScsActuacionDesignaAdm (usr);		
			salida=(Vector)designaAdm.ejecutaSelect(consultaDesigna);					
		}catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getConsultaDesigna' en B.D.");		
		}
		return salida;
	}
	
	  
		public Vector getConsultaActuacion (Hashtable entrada, HttpServletRequest request)throws ClsExceptions {
			Vector salida=null;				
		String consultaActuacion = "  SELECT act.IDINSTITUCION idinstitucion, act.IDTURNO idturno, act.ANIO anio, act.NUMERO numero, act.NUMEROASUNTO numeroasunto, act.FECHA fechaactuacion"+
									",act.FECHAJUSTIFICACION fechajustificacion, act.ACUERDOEXTRAJUDICIAL acuerdoextrajudicial"+ 
									",tur.nombre turno, act.numeroasunto numeroasunto, act.observaciones observaciones,  act.observacionesjustificacion observacionesjustificacion,"+
									" act.fechajustificacion fechajustificacion,act.acuerdoextrajudicial acuerdoextrajudicial, act.lugar lugar, act.anulacion anulacion, act.numeroasunto numeroasunto"+
									",act."+ScsActuacionDesignaBean.C_IDCOMISARIA+
									",act."+ScsActuacionDesignaBean.C_IDINSTITUCIONCOMISARIA+
									",act."+ScsActuacionDesignaBean.C_IDJUZGADO+
									",act."+ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO+
									",act."+ScsActuacionDesignaBean.C_IDPRISION+
									",act."+ScsActuacionDesignaBean.C_IDINSTITUCIONPRISION+
									",act."+ScsActuacionDesignaBean.C_IDPROCEDIMIENTO+
									",act."+ScsActuacionDesignaBean.C_IDINSTITUCIONPROCEDIMIENTO+
									",act."+ScsActuacionDesignaBean.C_IDACREDITACION+
									",pro.nombre nombreprocedimiento, pro.idprocedimiento idprocedimiento"+
									",acred."+ScsAcreditacionBean.C_DESCRIPCION+" AS NOMBREACREDITACION "+
									",act." + ScsActuacionDesignaBean.C_VALIDADA + " actuacionValidada " +
									",juzgado." + ScsJuzgadoBean.C_NOMBRE+ " NOMBREJUZGADO " +
									",act."+ScsActuacionDesignaBean.C_IDPRETENSION+
									" ,act.facturado FACTURADO "+
									" ,act.idpersonacolegiado IDPERSONACOLEGIADO, "+
									" per.nombre nombre, per.apellidos1 apellido1, per.apellidos2 apellido2,"+
									" col.ncolegiado ncolegiado, "+
									" act."+ScsActuacionDesignaBean.C_TALONARIO+
									" ,act."+ScsActuacionDesignaBean.C_TALON+		
									" ,act."+ScsActuacionDesignaBean.C_NUMEROPROCEDIMIENTO+
									" ,act."+ScsActuacionDesignaBean.C_NIG+
									",(select "+FcsFacturacionJGBean.C_NOMBRE+"||' ('||TO_CHAR("+FcsFacturacionJGBean.C_FECHADESDE+",'DD/MM/YYYY')||'-'||TO_CHAR("+FcsFacturacionJGBean.C_FECHAHASTA+",'DD/MM/YYYY')||')'"+
									" from "+FcsFacturacionJGBean.T_NOMBRETABLA+
								    " where "+FcsFacturacionJGBean.C_IDINSTITUCION+" = "+entrada.get("IDINSTITUCION")+
								    "   and "+FcsFacturacionJGBean.T_NOMBRETABLA+"."+FcsFacturacionJGBean.C_IDFACTURACION+" = act."+ScsActuacionDesignaBean.C_IDFACTURACION+") nombrefacturacion"+
									" FROM SCS_ACTUACIONDESIGNA act, scs_procedimientos pro , scs_turno tur, scs_acreditacion acred, scs_juzgado juzgado, cen_colegiado col, cen_persona per"+
									" WHERE act.IDINSTITUCION =  "+  entrada.get("IDINSTITUCION")+
									" and act.IDTURNO = "+ entrada.get("IDTURNO")+
									" and act.ANIO = "+entrada.get("ANIO")+
									" and act.NUMERO =  "+ entrada.get("NUMERO")+
									" and pro.idinstitucion = act.idinstitucion_proc"+ 
									" and pro.idprocedimiento = act.IDPROCEDIMIENTO"+  
									" and tur.idinstitucion = act.idinstitucion"+
									" and tur.idturno = act.idturno"+
									" and acred.idacreditacion = act.idacreditacion"+
									" and act.numeroasunto = "+entrada.get("VISIBLE")+
									" and act."+ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO+"=juzgado."+ScsJuzgadoBean.C_IDINSTITUCION+
									" and act."+ScsActuacionDesignaBean.C_IDJUZGADO+"=juzgado."+ScsJuzgadoBean.C_IDJUZGADO +
									" and act.idpersonacolegiado = per.idpersona" +    
									" and col.idpersona = per.idpersona" +
									" and col.idinstitucion = act.idinstitucion";
	
		try {	
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsActuacionDesignaAdm designaAdm = new ScsActuacionDesignaAdm (usr);		
			salida=(Vector)designaAdm.ejecutaSelect(consultaActuacion);					
		}catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getConsultaActuacion' en B.D.");		
		}
		return salida;
		}
		
		public Vector getDesignaActuaciones(Hashtable entrada, HttpServletRequest request)throws ClsExceptions {
		
		
			Vector salida=new Vector();
			String consulta =	"Select Idinstitucion,Idturno,Anio,Numero,Fechamodificacion,Usumodificacion,Fecha, "+
							    " Numeroasunto,Acuerdoextrajudicial,Anulacion,Idprocedimiento,Lugar,Observacionesjustificacion, "+
							    " Observaciones,Fechajustificacion,Facturado,Pagado,Idfacturacion,Validada,Idjuzgado,Idinstitucion_Juzg, "+
							    " Idcomisaria,Idinstitucion_Comis,Idprision,Idinstitucion_Pris,Idacreditacion,Idinstitucion_Proc, "+
							    " Idpersonacolegiado,Idpretension,Talonario,Talon,NUMEROPROCEDIMIENTO "+
								" From Scs_Actuaciondesigna " +
								" WHERE IDINSTITUCION =  "+ entrada.get("IDINSTITUCION")+
								" and IDTURNO = "+entrada.get("IDTURNO")+
								" and ANIO = "+entrada.get("ANIO")+
								" and NUMERO =  "+ entrada.get("NUMERO")+
								" and numeroasunto = "+entrada.get("VISIBLE")+
								" Order By Idinstitucion, Idturno, Anio, Numeroasunto ";
			
		try {	
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsActuacionDesignaAdm designaAdm = new ScsActuacionDesignaAdm (usr);		
			salida=(Vector)designaAdm.ejecutaSelect(consulta);		
			
		}catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getDesignaActuaciones' en B.D.");		
		}
		return salida;
		
		}

		public void setActuacionesDesignas(DesignaForm designa, boolean isMostrarJustificacionesPtes)  throws ClsExceptions, SIGAException 
	{
	    Hashtable<Integer,String> codigos = new Hashtable<Integer,String>();
	    int contador=0;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT AC.IDACREDITACION,AC.DESCRIPCION ACREDITACION,AC.IDTIPOACREDITACION,ACP.PORCENTAJE, TAC.DESCRIPCION TIPO, ");
		sql.append(" PRO.NOMBRE PROCEDIMIENTO,PRO.CODIGO CATEGORIA, PRO.IDJURISDICCION,PRO.COMPLEMENTO,PRO.PERMITIRANIADIRLETRADO,ACT.NUMEROASUNTO,ACT.IDPROCEDIMIENTO,ACT.IDJUZGADO,");
		sql.append(" TO_CHAR(ACT.FECHAJUSTIFICACION,'dd/mm/yyyy') FECHAJUSTIFICACION,ACT.VALIDADA,ACT.IDFACTURACION,ACT.NUMEROPROCEDIMIENTO ");
		sql.append(" ,(SELECT NOMBRE || ' (' || FECHADESDE || '-' || FECHAHASTA || ')' ");
		sql.append(" FROM FCS_FACTURACIONJG FJG ");
		sql.append(" WHERE FJG.IDINSTITUCION = ACT.IDINSTITUCION ");
		sql.append(" AND FJG.IDFACTURACION = ACT.IDFACTURACION) AS ");
		sql.append(" DESCRIPCIONFACTURACION ");
		sql.append(" FROM SCS_ACTUACIONDESIGNA          ACT, ");
		sql.append(" SCS_PROCEDIMIENTOS            PRO, ");
		sql.append(" SCS_ACREDITACIONPROCEDIMIENTO ACP, ");
		sql.append(" SCS_ACREDITACION              AC, ");
		sql.append(" SCS_TIPOACREDITACION          TAC ");
		sql.append(" WHERE AC.IDTIPOACREDITACION = TAC.IDTIPOACREDITACION ");
		sql.append(" AND ACT.IDACREDITACION = AC.IDACREDITACION ");
		      
		sql.append(" AND ACT.IDACREDITACION = ACP.IDACREDITACION ");
		sql.append(" AND ACT.IDINSTITUCION_PROC = ACP.IDINSTITUCION ");
		sql.append(" AND ACT.IDPROCEDIMIENTO = ACP.IDPROCEDIMIENTO ");
		      
		sql.append(" AND ACT.IDINSTITUCION_PROC = PRO.IDINSTITUCION ");
		sql.append(" AND ACT.IDPROCEDIMIENTO = PRO.IDPROCEDIMIENTO ");
		sql.append(" AND ACT.IDINSTITUCION = :");
		contador++;
		codigos.put(new Integer(contador),designa.getIdInstitucion());
		sql.append(contador);
		sql.append(" AND ACT.IDTURNO = :");
		contador++;
		codigos.put(new Integer(contador),designa.getIdTurno());
		sql.append(contador);
		sql.append(" AND ACT.ANIO = :");
		contador++;
		codigos.put(new Integer(contador),designa.getAnio());
		sql.append(contador);
		sql.append(" AND ACT.NUMERO = :");
		contador++;
		codigos.put(new Integer(contador),designa.getNumero());
		sql.append(contador);
		sql.append(" ORDER BY ACT.FECHAJUSTIFICACION,ACT.NUMEROASUNTO");
		Vector actuacionesVector = this.selectGenericoBind(sql.toString(), codigos);
		
		
		TreeMap<String, List<ActuacionDesignaForm>> tmActuaciones = new TreeMap<String, List<ActuacionDesignaForm>>();
		List<ActuacionDesignaForm> actuacionesList = null;
		ActuacionDesignaForm actuacionDesigna = null;
		AcreditacionForm acreditacion = null;
		StringBuffer asuntoDesigna = null; 
		if(actuacionesVector!=null && actuacionesVector.size()>0){
			asuntoDesigna = new StringBuffer(designa.getAsunto()==null?"":designa.getAsunto());
			asuntoDesigna.append(" ");
			for (int j = 0; j < actuacionesVector.size(); j++) {

				Hashtable registro = (Hashtable) actuacionesVector.get(j);
				String idProcedimiento  = (String)registro.get("IDPROCEDIMIENTO");
				if(tmActuaciones.containsKey(idProcedimiento)){
					actuacionesList = (List<ActuacionDesignaForm>)tmActuaciones.get(idProcedimiento);
				}else{
					actuacionesList = new ArrayList<ActuacionDesignaForm>();
				}
				actuacionDesigna = new ActuacionDesignaForm();
				actuacionDesigna.setCategoria((String)registro.get("CATEGORIA"));
				actuacionDesigna.setFechaJustificacion((String)registro.get("FECHAJUSTIFICACION"));
				actuacionDesigna.setValidada((String)registro.get("VALIDADA"));
				actuacionDesigna.setNumero((String)registro.get("NUMEROASUNTO"));
				actuacionDesigna.setIdJuzgado((String)registro.get("IDJUZGADO"));
				actuacionDesigna.setIdProcedimiento(idProcedimiento);
				actuacionDesigna.setDescripcionProcedimiento((String)registro.get("PROCEDIMIENTO"));
				actuacionDesigna.setMultiplesComplementos((String)registro.get("COMPLEMENTO"));
				actuacionDesigna.setIdJurisdiccion((String)registro.get("IDJURISDICCION"));
				actuacionDesigna.setIdFacturacion((String)registro.get("IDFACTURACION"));
				actuacionDesigna.setDescripcionFacturacion((String)registro.get("DESCRIPCIONFACTURACION"));
				actuacionDesigna.setPermitirEditarActuacion((String)registro.get("PERMITIRANIADIRLETRADO"));
				actuacionDesigna.setNumeroProcedimiento((String)registro.get("NUMEROPROCEDIMIENTO"));
				if(actuacionDesigna.getNumeroProcedimiento()!=null && !actuacionDesigna.getNumeroProcedimiento().equals("")){
					asuntoDesigna.append(actuacionDesigna.getNumeroProcedimiento());
					asuntoDesigna.append(" ");
				}
				
				acreditacion = new AcreditacionForm();
				actuacionDesigna.setAcreditacion(acreditacion);
				acreditacion.setId((String)registro.get("IDACREDITACION"));
				acreditacion.setDescripcion((String)registro.get("ACREDITACION"));
				acreditacion.setPorcentaje((String)registro.get("PORCENTAJE"));
				acreditacion.setIdTipo((String)registro.get("IDTIPOACREDITACION"));
				acreditacion.setIdProcedimiento(idProcedimiento);
				
				actuacionesList.add(actuacionDesigna);
				tmActuaciones.put(idProcedimiento, actuacionesList);
				
			}
		}
		if(asuntoDesigna!=null)
			designa.setAsunto(asuntoDesigna.toString().trim());
		designa.setActuaciones(tmActuaciones);
		TreeMap<String, List<AcreditacionForm>> tmAcreditaciones = new TreeMap<String, List<AcreditacionForm>>();
		designa.setAcreditaciones(tmAcreditaciones);
		if(tmActuaciones.size()>0){
			Iterator<String> itActuaciones = tmActuaciones.keySet().iterator();
			boolean isAgunaActuacionPte = false;
			while (itActuaciones.hasNext()) {
				String idProcedimiento = (String) itActuaciones.next();
				List<ActuacionDesignaForm> actuacionesProcedimientoList = (List<ActuacionDesignaForm>) tmActuaciones.get(idProcedimiento);
				
				
				List<AcreditacionForm> acreditacionesPendientesList = null;
				if(designa.getEstado().equals("V")||designa.getEstado().equals(""))
					acreditacionesPendientesList = getAcreditacionesPendientes(designa.getIdInstitucion(), idProcedimiento,null,(designa.getActuacionRestriccionesActiva()!=null && designa.getActuacionRestriccionesActiva().equals(ClsConstants.DB_TRUE)),actuacionesProcedimientoList);
				else
					acreditacionesPendientesList = new ArrayList<AcreditacionForm>();
				
				//Hacemos esto para el rowspan de la jsp
				
				if(acreditacionesPendientesList==null || acreditacionesPendientesList.size()==0){
					//si no hay mas actuaciones no aparece en la vista
					if(false){
//					if(tmActuaciones.size()==1){
						boolean isPteValidar = false;
						for(ActuacionDesignaForm actuacion:actuacionesProcedimientoList ){
							isPteValidar = actuacion.getValidada().equals(ClsConstants.DB_FALSE);
							if(isPteValidar)
								break;
						}
						if(!isPteValidar&&isMostrarJustificacionesPtes)
							itActuaciones.remove();
					}else{
						boolean isPteValidar = false;
						for(ActuacionDesignaForm actuacion:actuacionesProcedimientoList ){
							isPteValidar = actuacion.getValidada().equals(ClsConstants.DB_FALSE);
							if(isPteValidar&&!isAgunaActuacionPte)
								isAgunaActuacionPte = true;
								
						}
						
						
					}
				}else{
					isAgunaActuacionPte = true;
					AcreditacionForm primeraAcreditacion = acreditacionesPendientesList.get(0);
					primeraAcreditacion.setRowSpan(acreditacionesPendientesList.size());
					tmAcreditaciones.put(idProcedimiento, acreditacionesPendientesList);
				}
				
			}
			if(!isAgunaActuacionPte&&isMostrarJustificacionesPtes){
				designa.setActuaciones(null);
			}
		}else{
			if(designa.getIdProcedimiento()!=null && !designa.getIdProcedimiento().equals("") && designa.getIdJuzgado()!=null && !designa.getIdJuzgado().equals("") ){
				
				List<AcreditacionForm> acreditacionesPendientesList = null;
				if(designa.getEstado().equals("V")||designa.getEstado().equals(""))
					acreditacionesPendientesList = getAcreditacionesPendientes(designa.getIdInstitucion(), designa.getIdProcedimiento(),designa.getIdJuzgado(),(designa.getActuacionRestriccionesActiva()!=null && designa.getActuacionRestriccionesActiva().equals(ClsConstants.DB_TRUE)), null);
				else
					acreditacionesPendientesList = new ArrayList<AcreditacionForm>();
				
				//Hacemos esto para el rowspan de la jsp
				if(acreditacionesPendientesList!=null && acreditacionesPendientesList.size()>0){
					AcreditacionForm primeraAcreditacion = acreditacionesPendientesList.get(0);
					primeraAcreditacion.setRowSpan(acreditacionesPendientesList.size());
				}
				tmAcreditaciones.put(designa.getIdProcedimiento(), acreditacionesPendientesList);
			}
		}		
	}

	
	public List<AcreditacionForm> getAcreditacionesPendientes(String idInstitucion,String idProcedimiento,String idJuzgado,boolean restriccionesActivas, List<ActuacionDesignaForm> actuacionesList) throws ClsExceptions, SIGAException 
	{
	    
		
		Hashtable<Integer,String> codigos = new Hashtable<Integer,String>();
	    int contador=0;
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT AC.IDACREDITACION,AC.DESCRIPCION,TAC.IDTIPOACREDITACION,ACPRO.PORCENTAJE,PRO.IDJURISDICCION ");
		sql.append(" FROM SCS_ACREDITACIONPROCEDIMIENTO ACPRO, ");
		sql.append(" SCS_PROCEDIMIENTOS            PRO, ");
		sql.append(" SCS_ACREDITACION              AC, ");
		sql.append(" SCS_TIPOACREDITACION          TAC ");
		sql.append(" WHERE  ");
		sql.append(" AC.IDTIPOACREDITACION = TAC.IDTIPOACREDITACION ");
		sql.append(" AND ACPRO.IDACREDITACION = AC.IDACREDITACION ");
		sql.append(" AND PRO.IDINSTITUCION = ACPRO.IDINSTITUCION ");
		sql.append(" AND PRO.IDPROCEDIMIENTO = ACPRO.IDPROCEDIMIENTO ");

		
		
		sql.append(" AND ACPRO.IDINSTITUCION = :");
		contador++;
		codigos.put(new Integer(contador),idInstitucion);
		sql.append(contador);
		sql.append(" AND ACPRO.IDPROCEDIMIENTO = :");
		contador++;
		codigos.put(new Integer(contador),idProcedimiento);
		sql.append(contador);
		sql.append(" AND TAC.IDTIPOACREDITACION NOT IN (4) ");
		sql.append(" AND AC.IDACREDITACION NOT IN (10,11)");
		
		
		
//		ArrayList
		if(restriccionesActivas && actuacionesList!=null && actuacionesList.size()>0){
			boolean isInicio = false;
			boolean isFin = false;
			boolean isCompleta = false;
			for (int i = 0; i < actuacionesList.size(); i++) {
				ActuacionDesignaForm actuacion = (ActuacionDesignaForm) actuacionesList.get(i);
				String multiplesComplementos = actuacion.getMultiplesComplementos();
				if(multiplesComplementos!=null && multiplesComplementos.equals(ClsConstants.DB_TRUE))
					return new ArrayList<AcreditacionForm>();
//					break;
				
//				if(multiplesComplementos!=null && multiplesComplementos.equals(ClsConstants.DB_FALSE))
//					return new ArrayList<AcreditacionForm>();
//				
				if(i==0){
					sql.append(" AND AC.IDACREDITACION NOT IN (");
				
					idJuzgado = actuacion.getIdJuzgado();
					contador++;
					codigos.put(new Integer(contador),actuacion.getAcreditacion().getId());
					sql.append(":");
					sql.append(contador);
					sql.append(")");
				}
				
				
				if(!isInicio){
					isInicio = actuacion.getAcreditacion().getIdTipo().equals("1");
					if(isInicio){
						sql.append(" AND TAC.IDTIPOACREDITACION NOT IN (:");
						contador++;
						codigos.put(new Integer(contador),"3");
						sql.append(contador);
						sql.append(")");
						
					}
				}
				if(!isFin){
					isFin = actuacion.getAcreditacion().getIdTipo().equals("2");
					if(isFin){
						sql.append(" AND TAC.IDTIPOACREDITACION NOT IN (:");
						contador++;
						codigos.put(new Integer(contador),"3");
						sql.append(contador);
						sql.append(")");
						
					}
				}
				if(!isCompleta){
					isCompleta = actuacion.getAcreditacion().getIdTipo().equals("3");
					if(isCompleta){
						return new ArrayList<AcreditacionForm>();
					}
				}
				
				
				
//				if(i!=actuacionesList.size()-1)
//					sql.append(",");
//				else
//					sql.append(")");
				
			}
			if(isInicio && isFin)
				return new ArrayList<AcreditacionForm>();
			
			
		}
		else{
			if(actuacionesList!=null && actuacionesList.size()>0){
				for (int x = 0; x < actuacionesList.size(); x++) {
					ActuacionDesignaForm actuacion = (ActuacionDesignaForm) actuacionesList.get(x);
					if(x==0){
						idJuzgado = actuacion.getIdJuzgado();
						break;
					}
					
				}
			}
		}
		sql.append(" ORDER BY AC.IDACREDITACION ");

		Vector acreditacionesPtesVector = this.selectGenericoBind(sql.toString(), codigos);
		List<AcreditacionForm> acreditacionesPtesList = new ArrayList<AcreditacionForm>();
		AcreditacionForm acreditacionForm = null;
		for (int j = 0; j < acreditacionesPtesVector.size(); j++) {
			Hashtable registro = (Hashtable) acreditacionesPtesVector.get(j);
			acreditacionForm = new AcreditacionForm();
			acreditacionForm.setDescripcion((String)registro.get("DESCRIPCION"));
			acreditacionForm.setIdTipo((String)registro.get("IDTIPOACREDITACION"));
			acreditacionForm.setPorcentaje((String)registro.get("PORCENTAJE"));
			acreditacionForm.setId((String)registro.get("IDACREDITACION"));
			acreditacionForm.setIdJurisdiccion((String)registro.get("IDJURISDICCION"));
			acreditacionForm.setIdJuzgado(idJuzgado);
			acreditacionForm.setIdProcedimiento(idProcedimiento);
			acreditacionesPtesList.add(acreditacionForm);
			
		}
		return acreditacionesPtesList;

	}
	
	/**
	 * Recupera la fecha de la primera actuacion de una designa
	 * @param sdb
	 * @param fecha
	 * @return
	 * @throws ClsExceptions 
	 */
	public String getFechaPrimeraActuacion(ScsDesignaBean sdb) throws ClsExceptions {
		try {
			String sql = "select min(fecha) as FECHAACTUACION " +
						 "  from scs_actuaciondesigna " +
						 " where idinstitucion = "+sdb.getIdInstitucion()+" " +
						 "   and idturno = "+sdb.getIdTurno()+" " +
						 "   and anio = "+sdb.getAnio()+" " +
						 "   and numero = "+sdb.getNumero();

			Vector vector = selectGenerico(sql);
		    if ((vector != null) && (vector.size() == 1)) {
		    	return (String)((Hashtable)vector.get(0)).get("FECHAACTUACION");
		    }

			return null;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre obtenerColegiadoDesignadoEnFecha()");
		}

	}
}