/*
 * Created on 17/09/2008
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.transaction.UserTransaction;

import org.redabogacia.sigaservices.app.AppConstants.ESTADOS_EJG;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.helper.StringHelper;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;
import com.siga.ws.PCAJGConstantes;

/**
 * @author angel corral
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CajgEJGRemesaAdm extends MasterBeanAdministrador {
	
	public CajgEJGRemesaAdm (UsrBean usu) {
		super (CajgEJGRemesaBean.T_NOMBRETABLA, usu);
	}	


	protected String[] getCamposBean() {
		String [] campos = {CajgEJGRemesaBean.C_IDEJGREMESA, CajgEJGRemesaBean.C_IDINSTITUCION, 		CajgEJGRemesaBean.C_ANIO,
							CajgEJGRemesaBean.C_NUMERO, 			CajgEJGRemesaBean.C_IDTIPOEJG,
							CajgEJGRemesaBean.C_IDINSTITUCIONREMESA,CajgEJGRemesaBean.C_IDREMESA,
							CajgEJGRemesaBean.C_NUMEROINTERCAMBIO, CajgEJGRemesaBean.C_RECIBIDA,
							CajgEJGRemesaBean.C_FECHAMODIFICACION,	CajgEJGRemesaBean.C_USUMODIFICACION};
		return campos;
	}


	protected String[] getClavesBean() {
		String[] campos = { CajgEJGRemesaBean.C_IDEJGREMESA};
		return campos;
	}


	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CajgEJGRemesaBean bean = null;
		try{
			bean = new CajgEJGRemesaBean();
			bean.setIdEjgRemesa(UtilidadesHash.getInteger(hash,CajgEJGRemesaBean.C_IDEJGREMESA));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CajgEJGRemesaBean.C_IDINSTITUCION));
			bean.setAnio(UtilidadesHash.getInteger(hash,CajgEJGRemesaBean.C_ANIO));
			bean.setNumero(UtilidadesHash.getInteger(hash,CajgEJGRemesaBean.C_NUMERO));
			bean.setIdTipoEJG(UtilidadesHash.getInteger(hash,CajgEJGRemesaBean.C_IDTIPOEJG));
			bean.setIdInstitucionRemesa(UtilidadesHash.getInteger(hash,CajgEJGRemesaBean.C_IDINSTITUCIONREMESA));
			bean.setIdRemesa(UtilidadesHash.getInteger(hash, CajgEJGRemesaBean.C_IDREMESA));
			bean.setNumeroIntercambio(UtilidadesHash.getInteger(hash, CajgEJGRemesaBean.C_NUMEROINTERCAMBIO));
			bean.setRecibida(UtilidadesHash.getInteger(hash, CajgEJGRemesaBean.C_RECIBIDA));
			
			bean.setFechaMod(UtilidadesHash.getString (hash,CajgRemesaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CajgRemesaBean.C_USUMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CajgEJGRemesaBean b = (CajgEJGRemesaBean) bean;
			
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_IDEJGREMESA, b.getIdEjgRemesa());
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_ANIO, b.getAnio());
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_NUMERO, b.getNumero());
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_IDTIPOEJG, b.getIdTipoEJG());
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_IDINSTITUCIONREMESA, b.getIdInstitucionRemesa());
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_IDREMESA, b.getIdRemesa());
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_NUMEROINTERCAMBIO, b.getNumeroIntercambio());
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_RECIBIDA, b.getRecibida());
			
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, CajgEJGRemesaBean.C_USUMODIFICACION, b.getUsuMod());	
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	public Vector busquedaEJGRemesa(String idInstitucion, String idRemesa) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		String consulta="";
		try {
			consulta = "select ejg." + ScsEJGBean.C_ANIO + ", ejg." + ScsEJGBean.C_IDINSTITUCION + ", ejg." + ScsEJGBean.C_IDTIPOEJG + ",ejg." + ScsEJGBean.C_NUMERO + "" +
					   " from " + ScsEJGBean.T_NOMBRETABLA + " ejg, CAJG_EJGREMESA ejgremesa" ;
			/* realizamos la join con de las tablas que necesitamos */
			consulta += " where ejg.idinstitucion=ejgremesa.idinstitucion"+
					    " and ejg.anio=ejgremesa.anio"+
					    " and ejg.numero=ejgremesa.numero"+
					    " and ejg.idtipoejg=ejgremesa.idtipoejg"+
					    " and ejgremesa.idremesa="+idRemesa+""+
					    " and ejgremesa.idinstitucion="+idInstitucion+"";	
			
			
			//Consulta:
			datos = this.selectGenerico(consulta);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	
	
	
	
	
	public Vector busquedaEJGRemesaParaRemitidoComision(String idInstitucion, String idRemesa) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		String consulta="";
		try {
			consulta = "select ejgremesa.ANIO, ejgremesa.IDINSTITUCION, ejgremesa.IDTIPOEJG, ejgremesa.NUMERO" +
					" from CAJG_EJGREMESA ejgremesa, scs_estadoejg estado" +
					" where ejgremesa.idinstitucion = estado.idinstitucion" +
					" and ejgremesa.anio = estado.anio" +
					" and ejgremesa.numero = estado.numero" +
					" and ejgremesa.idtipoejg = estado.idtipoejg" +
					" and ejgremesa.idremesa = " + idRemesa +
					" and ejgremesa.idinstitucion = " + idInstitucion +
					" and estado.idestadoporejg = (select max(estadoEJG.idestadoporejg)" +
					" FROM scs_estadoejg estadoEJG where estado.idinstitucion = estadoEJG.idinstitucion" +
					" and estado.anio = estadoEJG.anio" +
					" and estadoEJG.fechabaja is null " +
					" and estado.numero = estadoEJG.numero" +
					" and estado.idtipoejg = estadoEJG.idtipoejg)" +
					" and estado.idestadoporejg <> " + ESTADOS_EJG.REMITIDO_COMISION.getCodigo();
			
			
			//Consulta:
			datos = this.selectGenerico(consulta);			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}


	/**
	 * Obtiene los datos de los EJGs de la remesa
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */	
	public Vector getDatosEJGs(int idInstitucion, int idRemesa, String idLenguaje) throws ClsExceptions, SIGAException {				
		String consulta = "SELECT *" +
					" FROM V_PCAJG_EJG" +
					" WHERE " + PCAJGConstantes.IDREMESA + "  = " + idRemesa +
					" AND " + PCAJGConstantes.IDINSTITUCION + " = " + idInstitucion +
					" AND " + PCAJGConstantes.IDLENGUAJE + " = " + idLenguaje +
					" ORDER BY " + PCAJGConstantes.TIPOINTERCAMBIO;				
			
		return getDatos(consulta);
	}
	
	
	/**
	 * Obtiene las marcas expediente de los EJGs de la remesa
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getDatosMarcasExpediente(int idInstitucion, int idRemesa) throws ClsExceptions, SIGAException {
				
		String consulta = "SELECT *" +
				" FROM V_PCAJG_MARCASEXPEDIENTES" +
			" WHERE IDINSTITUCION = " + idInstitucion +
			" AND IDREMESA = " + idRemesa;
		
		return getDatos(consulta);
	}


	/**
	 * Obtiene los datos de los familiares de los EJGs de la remesa
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getFamiliares(int idInstitucion, int idRemesa, String idLenguaje) throws ClsExceptions, SIGAException {				
		String sql = "SELECT *" +
				" FROM V_PCAJG_FAMILIARES" +
				" WHERE " + PCAJGConstantes.IDREMESA + "  = " + idRemesa +
				" AND " + PCAJGConstantes.IDINSTITUCION + " = " + idInstitucion +
				" AND " + PCAJGConstantes.IDLENGUAJE + " = " + idLenguaje;

		return getDatos(sql);
	}


	/**
	 * Obtiene los datos de los contrarios de los EJGs de la remesa
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getContrarios(int idInstitucion, int idRemesa, String idLenguaje) throws ClsExceptions, SIGAException {
		String sql = "SELECT *" +
				" FROM V_PCAJG_CONTRARIOS" +
				" WHERE " + PCAJGConstantes.IDREMESA + "  = " + idRemesa +
				" AND " + PCAJGConstantes.IDINSTITUCION + " = " + idInstitucion +
				" AND " + PCAJGConstantes.IDLENGUAJE + " = " + idLenguaje;
		
		return getDatos(sql);
	}
	
	/**
	 * Obtiene los abogados designados de los EJGs de la remesa
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getAbogadosDesignados(int idInstitucion, int idRemesa) throws ClsExceptions, SIGAException {
		String sql = "SELECT *" +
			" FROM V_PCAJG_ABOGADOSDESIGNADOS" +
			" WHERE IDINSTITUCION = " + idInstitucion +
			" AND IDREMESA = " + idRemesa;
		
		return getDatos(sql);
	}

	/**
	 * Obtiene los datos de documentación de los expedientes
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getDocumentacionExpedienteDS(int idInstitucion, int idRemesa, String idLenguaje) throws ClsExceptions, SIGAException {
		String sql = "SELECT *" +
				" FROM V_PCAJG_M_DOCUMENTACIONEXP" +
				" WHERE " + PCAJGConstantes.IDREMESA + "  = " + idRemesa +
				" AND " + PCAJGConstantes.IDINSTITUCION + " = " + idInstitucion +
				" AND " + PCAJGConstantes.IDLENGUAJE + " = " + idLenguaje;
		return getDatos(sql);
	}
	
	/**
	 * Obtiene los datos de documentación de los expedientes
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getDocumentacionExpedienteF(int idInstitucion, int idRemesa) throws ClsExceptions, SIGAException {
		String sql = "SELECT *" +
				" FROM V_PCAJG_DOCUMENTACIONEXP_F" +
				" WHERE IDINSTITUCION = " + idInstitucion +
				" AND IDREMESA = " + idRemesa;
		return getDatos(sql);
	}
	
	public Vector getDelitos(int idInstitucion, int idRemesa, String idLenguaje) throws ClsExceptions, SIGAException {
		String sql = "SELECT *" +
				" FROM V_PCAJG_DELITOS" +
				" WHERE " + PCAJGConstantes.IDREMESA + "  = " + idRemesa +
				" AND " + PCAJGConstantes.IDINSTITUCION + " = " + idInstitucion +
				" AND " + PCAJGConstantes.IDLENGUAJE + " = " + idLenguaje;
		return getDatos(sql);
	}
	
	public Vector getDatosPrestacionesResolucion(int idInstitucion, int idRemesa, String idTipoEJG, String anio, String numero) {
		Vector datos = new Vector();		
		return datos;
	}
	
	
	/**
	 * Metodo que comprueba si los cambos de la query estan recogidos en java, así
	 * si cambia una columna lanzará un error. Este método es solo de debug y luego puede
	 * comentarse su contenido si la versión está estable
	 * @param vector
	 * @throws SIGAException
	 */
	private void compruebaCampos(Vector vector) throws SIGAException {
		if (vector != null && vector.size() > 0) {
			if (vector.get(0) instanceof Hashtable) {
				 
				Hashtable ht = (Hashtable)vector.get(0);
				Enumeration enumeration = ht.keys();
				while (enumeration.hasMoreElements()) {
					String key = (String) enumeration.nextElement();
					try {
						PCAJGConstantes.class.getField(key);
					} catch (Exception e) {
						throw new SIGAException("No se encuentra la columna " + key);
					}					
				}
			}
		}
	}
	
	/**
	 * Obtiene un vector de hastables con los datos de la query que se pase por parametro
	 * @param sql
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private Vector getDatos(String sql) throws ClsExceptions, SIGAException {
		Vector datos = this.selectGenerico(sql);
		//llamada al metodo compruebaCampos es solo para debug.
//		compruebaCampos(datos);
		return datos;
	}

	
	/**
	 * @throws ClsExceptions 
	 * @throws  
	 * Selecciona el siguiente número de intercambio de la institucion que se le pasa por parámetro
	 * @param idInstitucion
	 * @return
	 */
	public int getNextNumeroIntercambio(Integer idInstitucion) throws ClsExceptions {
		int numeroIntercambio = -1;
		
		String sql = "SELECT NVL(MAX(" + CajgEJGRemesaBean.C_NUMEROINTERCAMBIO + "), 0) AS " + CajgEJGRemesaBean.C_NUMEROINTERCAMBIO +
				" FROM " + CajgEJGRemesaBean.T_NOMBRETABLA +
				" WHERE " + CajgEJGRemesaBean.C_IDINSTITUCION + " = " + idInstitucion;
		
		RowsContainer rc = new RowsContainer();

		if (rc.find(sql)) {
			Row r = (Row) rc.get(0);
			numeroIntercambio = Integer.parseInt(r.getString(CajgEJGRemesaBean.C_NUMEROINTERCAMBIO));
			numeroIntercambio++;
		}
		return numeroIntercambio;
	}
	
	/**
	 * 
	 * @return
	 * @throws ClsExceptions
	 */
	public int getNextVal() throws ClsExceptions {
		int nextVal = -1;
		
		String sql = "SELECT NVL(MAX(" + CajgEJGRemesaBean.C_IDEJGREMESA + "), 0) AS " + CajgEJGRemesaBean.C_IDEJGREMESA +
				" FROM " + CajgEJGRemesaBean.T_NOMBRETABLA;
		
		RowsContainer rc = new RowsContainer();

		if (rc.find(sql)) {
			Row r = (Row) rc.get(0);
			nextVal = Integer.parseInt(r.getString(CajgEJGRemesaBean.C_IDEJGREMESA));
			nextVal++;
		}
		return nextVal;
	}
	
	/**
	 * 
	 * @param usr
	 * @param idInstitucion
	 * @param idRemesa
	 * @param idEstado
	 * @throws ClsExceptions
	 */
	public void nuevoEstadoEJGRemitidoComision(UsrBean usr, String idInstitucion, String idRemesa, ESTADOS_EJG estadoEJG) throws ClsExceptions {
		
		String sqlMaxIdEstadoPorEJG = "SELECT NVL(MAX(IDESTADOPOREJG), 0) + 1" +
				" FROM SCS_ESTADOEJG E" +
				" WHERE E.IDINSTITUCION = ER.IDINSTITUCION" +
				" AND E.ANIO = ER.ANIO" +
				" AND E.NUMERO = ER.NUMERO" +
				" AND E.IDTIPOEJG = ER.IDTIPOEJG";
		
		String sqlInsertEstadoEJG = "insert into scs_estadoejg (idinstitucion, idtipoejg, anio, numero, idestadoejg" +
				", fechainicio, fechamodificacion, usumodificacion, observaciones, idestadoporejg, automatico)" +
				" SELECT ER.IDINSTITUCION, ER.IDTIPOEJG, ER.ANIO, ER.NUMERO, '" + estadoEJG.getCodigo() + "'" +
				", TRUNC(SYSDATE), SYSDATE, " + usr.getUserName() + ", NULL, (" + sqlMaxIdEstadoPorEJG + "), 1" +
				" FROM CAJG_EJGREMESA ER" +
				" WHERE ER.IDEJGREMESA NOT IN (SELECT RER.IDEJGREMESA FROM CAJG_RESPUESTA_EJGREMESA RER)" +
				" AND ER.IDINSTITUCION = " + idInstitucion +
				" AND ER.IDREMESA = " + idRemesa;
		
		ScsEstadoEJGAdm beanEstado = new ScsEstadoEJGAdm(usr);
		beanEstado.insertSQL(sqlInsertEstadoEJG);

	}
	
	private void actualizaCabeceraInsercionRemesa(Vector<List<String>> paresVector,List<String> queryList ,List<Hashtable<Integer, String>> codigosList ){
		Hashtable<Integer,String> codigosHashtable = new Hashtable<Integer, String>();
		int contador = 0;
		
		
		List<String> camposList = paresVector.get(0);
		List<String> valuesList = paresVector.get(1);
		StringBuilder camposBuilder = new StringBuilder();
		StringBuilder valuesBuilder = new StringBuilder();
		camposBuilder.append("CAB_INTERCAMBIO_ID");
		camposBuilder.append(",CAB_FECHAENVIO");
		
		
		
		camposBuilder.append(",CAB_FECHARESPUESTA");
		
		
		
		camposBuilder.append(",CAB_EST_ID");
		contador++;
		valuesBuilder.append(",:");
		valuesBuilder.append(contador);
		codigosHashtable.put(new Integer(contador),"1");
		
		
		
		
		String numeroIntercambio = "";
		
		for (int i = 0; i < camposList.size(); i++) {
			camposBuilder.append(",");
			camposBuilder.append(camposList.get(i));
			contador++;
			valuesBuilder.append(",:");
			valuesBuilder.append(contador);
			codigosHashtable.put(new Integer(contador),valuesList.get(i));
			if(camposList.get(i).equals("CAB2_NUMERO_INTERCAMBIO")){
				numeroIntercambio = valuesList.get(i);
			}
			
			
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO ");
		builder.append(this.tablaCabecera);
		builder.append(" ( ");
		builder.append(camposBuilder);
		builder.append(" ) VALUES ");
		builder.append(" ( SEQ_PCAJG_ALC_INT_CAB.NEXTVAL,SYSDATE,SYSDATE");
		builder.append(valuesBuilder);
		builder.append(" ) ");
		
		queryList.add(builder.toString()); 
		codigosList.add(codigosHashtable);
		
		//Actualizamos el campo receibida de la remes a a 1. Recibida respuetsa correcta
		builder = new StringBuilder();
		codigosHashtable = new Hashtable<Integer, String>();
		contador =0;
		
		builder.append(" UPDATE CAJG_EJGREMESA SET RECIBIDA = ");
		contador++;
		builder.append(":");
		builder.append(contador);
		codigosHashtable.put(new Integer(contador),"1");
		builder.append(" ,FECHAMODIFICACION = SYSDATE ");
		
		
		builder.append(" WHERE NUMEROINTERCAMBIO =");
		contador++;
		builder.append(":");
		builder.append(contador);
		codigosHashtable.put(new Integer(contador),Integer.valueOf(numeroIntercambio).toString());
		builder.append(" AND IDINSTITUCION = ");
		contador++;
		builder.append(":");
		builder.append(contador);
		codigosHashtable.put(new Integer(contador),"2003");
		
		queryList.add(builder.toString()); 
		codigosList.add(codigosHashtable);
		
		
	}
	
	final String tablaPrefijo = "PCAJG_ALC_INT_";
	final String tablaCabecera = "PCAJG_ALC_INT_CAB";
	
	private  void actualizaInsercionRemesa(String tipoTabla,Vector<List<String>> paresVector, List<String> queryList ,List<Hashtable<Integer, String>> codigosList ){
		Hashtable<Integer,String> codigosHashtable = new Hashtable<Integer, String>();
		int contador = 0;
	
		
		List<String> camposList = paresVector.get(0);
		List<String> valuesList = paresVector.get(1);
		StringBuilder camposBuilder = new StringBuilder();
		StringBuilder valuesBuilder = new StringBuilder();
		//Buscamos el suijo que nos determionara la tabla
		String tabla =  this.tablaPrefijo+tipoTabla;
		camposBuilder.append(tipoTabla);
		camposBuilder.append("_INTERCAMBIO_ID");
		

		
		for (int i = 0; i < camposList.size(); i++) {
			camposBuilder.append(",");
			camposBuilder.append(camposList.get(i));
			contador++;
			valuesBuilder.append(",:");
			valuesBuilder.append(contador);
			codigosHashtable.put(new Integer(contador),valuesList.get(i).toString());
			
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO ");
		builder.append(tabla);
		builder.append(" ( ");
		builder.append(camposBuilder);
		builder.append(" ) VALUES ");
		builder.append(" ( SEQ_PCAJG_ALC_INT_CAB.CURRVAL");
		builder.append(valuesBuilder);
		builder.append(" ) ");
		
		queryList.add(builder.toString());
		codigosList.add(codigosHashtable);
		
	}
	/**
	 * No dice si se ha guardado en historico un expediente enviado en una remesa de envio
	 * @param anio
	 * @param numExpediente
	 * @param idInstitucion
	 * @return
	 * @throws BusinessException
	 */
	public boolean isEJGHistorico(String anio, String numExpediente)throws BusinessException  {
		StringBuilder builder = new StringBuilder();
		builder.append(" "); 
		builder.append("SELECT 1 ");
		builder.append("FROM PCAJG_ALC_INT_CAB CAB, PCAJG_ALC_INT_EXP EXP ");
		builder.append("WHERE CAB.CAB_INTERCAMBIO_ID = EXP.EXP_INTERCAMBIO_ID ");
		builder.append("AND EXP.EXP1_NUM_EXPEDIENTE = '");
		builder.append(numExpediente);
		builder.append("' ");
		builder.append("AND EXP.EXP2_ANIO_EXPEDIENTE = '");
		builder.append(anio);
		builder.append("' ");
		builder.append("AND CAB.CAB_FECHARESPUESTA IS NOT NULL ");
		builder.append("AND CAB.CAB_EST_ID = 1 ");
		Vector vHistoricoEJG = null;
		try {
			vHistoricoEJG = selectSQL(builder.toString());
		} catch (ClsExceptions e) {
			throw new BusinessException("Error en SQL"+e.toString());
		}
		
		return vHistoricoEJG!=null && vHistoricoEJG.size() > 0;
			
		
		
	}
	
	public Map<String,List<String>> getLineasExpedientesFicheroActualizacion(Map<String, Map<String, String>> expActualizarMapDatosNew) throws BusinessException {
		
		Map<String,List<String>> expInluidosFicheroActualizacion = new HashMap<String, List<String>>();
		
		
		Map<String, Map<String, String>> expActualizarMapDatosOld = new TreeMap<String, Map<String, String>>();
		Map<String, Set<String>> expActualizarMap = new TreeMap<String, Set<String>>();
		

		Set<String> segmentosActualizarSet = null;
		Iterator<String> iteradorExp = expActualizarMapDatosNew.keySet().iterator();
		while (iteradorExp.hasNext()) {
			String numIntercambioKey = (String) iteradorExp.next();
			Hashtable<String, String> expNewHashtable = (Hashtable<String, String>) expActualizarMapDatosNew.get(numIntercambioKey);
			String expOldNumero = expNewHashtable.get("EXP1_NUM_EXPEDIENTE");
			String expOldAnio = expNewHashtable.get("EXP2_ANIO_EXPEDIENTE");
			Hashtable<String, String> expOldHashtable = getUltimoEJGEnviado(expNewHashtable.get("EXP2_ANIO_EXPEDIENTE"), expNewHashtable.get("EXP1_NUM_EXPEDIENTE"));
			expActualizarMapDatosOld.put(numIntercambioKey, expOldHashtable);

			Iterator<String> ejgNewIterator = expNewHashtable.keySet().iterator();
			String numeroIntercambioEJG = expNewHashtable.get("CAB2_NUMERO_INTERCAMBIO");
			while (ejgNewIterator.hasNext()) {
				String key = (String) ejgNewIterator.next();

				if (!key.startsWith("SALTO_LINEA_") && !key.startsWith("TIPO_REGISTRO_") && !key.startsWith("GRUPO_") && key.indexOf("_NUMERO_INTERCAMBIO") == -1) {
					String newValue = expNewHashtable.get(key) == null ? "" : expNewHashtable.get(key);
					String oldValue = expOldHashtable.get(key) == null ? "" : expOldHashtable.get(key);
					if (!newValue.trim().equalsIgnoreCase(oldValue.trim())) {
						if (!expActualizarMap.containsKey(numeroIntercambioEJG)) {
							segmentosActualizarSet = new HashSet<String>();

						}
						segmentosActualizarSet.add(key.substring(0, 3));
						expActualizarMap.put(numeroIntercambioEJG, segmentosActualizarSet);


						
					}
				}
			}
		}
		List<String> lineasFicheroAct = null;
		Iterator<String> ejgActualizar = expActualizarMap.keySet().iterator();
		int numActualizacion = 1;
		while (ejgActualizar.hasNext()) {
			lineasFicheroAct = new ArrayList<String>();
			String keyNumIntercambioAct = ejgActualizar.next();
			Map<String, String> expActualizarMapDatosOriginalMap = expActualizarMapDatosOld.get(keyNumIntercambioAct);
			Map<String, String> expActualizarMapDatosNewMap = expActualizarMapDatosNew.get(keyNumIntercambioAct);
			lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
			numActualizacion++;
			
			Set<String> segmentosActualizarSetValue = expActualizarMap.get(keyNumIntercambioAct);
			boolean isAñadidaSegmentosTipoMod = false;
			for (String segmentoActualizar : segmentosActualizarSetValue) {
				// Añadimos e
				if (segmentoActualizar.equals("EXP")) {
					if(isAñadidaSegmentosTipoMod){
						lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
						numActualizacion++;
					}
					
//					001	Actualización datos expediente
					
					// hay que modificar EXP por AXP y añadirle el tipo de actualizacion ("001" Actualización datos expediente)
					lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"001"));
					isAñadidaSegmentosTipoMod = true;
					
				}else if (segmentoActualizar.equals("PRD")) {
//					002	Turnado profesional
//					003	Cambio de profesional
					
					
					
//					En todos los caso hay que añadir la linea del expediente si no estuviera añadimos
					// Vamos a ver si el expediente que se envio no tgenia abogado designado.
					// IF OLD.PRD1_NCOLEGIADOABOGADO NULL && NEW.PRD1_NCOLEGIADOABOGADO NOT NULL
					
					
					String colegiadoOld = expActualizarMapDatosOriginalMap.get("PRD1_NCOLEGIADOABOGADO") != null ? expActualizarMapDatosOriginalMap.get("PRD1_NCOLEGIADOABOGADO").trim() : "";
					String colegiadoNew = expActualizarMapDatosNewMap.get("PRD1_NCOLEGIADOABOGADO") != null ? expActualizarMapDatosNewMap.get("PRD1_NCOLEGIADOABOGADO").trim() : "";
					
					String procuradorOld = expActualizarMapDatosOriginalMap.get("PRD5_NCOLEGIADO_PROCURADOR") != null ? expActualizarMapDatosOriginalMap.get("PRD5_NCOLEGIADO_PROCURADOR").trim() : "";
					String procuradorNew = expActualizarMapDatosNewMap.get("PRD5_NCOLEGIADO_PROCURADOR") != null ? expActualizarMapDatosNewMap.get("PRD5_NCOLEGIADO_PROCURADOR").trim() : "";
					
					String numDesignaOld = expActualizarMapDatosOriginalMap.get("PRD3_NUMERO_DESIGNA") != null ? expActualizarMapDatosOriginalMap.get("PRD3_NUMERO_DESIGNA").trim() : "";
					String numDesignaNew = expActualizarMapDatosNewMap.get("PRD3_NUMERO_DESIGNA") != null ? expActualizarMapDatosNewMap.get("PRD3_NUMERO_DESIGNA").trim() : "";
					
					String anioDesignaOld = expActualizarMapDatosOriginalMap.get("PRD4_ANIO_DESIGNA") != null ? expActualizarMapDatosOriginalMap.get("PRD4_ANIO_DESIGNA").trim() : "";
					String anioDesignaNew = expActualizarMapDatosNewMap.get("PRD4_ANIO_DESIGNA") != null ? expActualizarMapDatosNewMap.get("PRD4_ANIO_DESIGNA").trim() : "";
					
					
					String numDesignaProcuradorOld = expActualizarMapDatosOriginalMap.get("PRD6_NUMERO_DESIGNA_PROCURADOR") != null ? expActualizarMapDatosOriginalMap.get("PRD6_NUMERO_DESIGNA_PROCURADOR").trim() : "";
					String numDesignaProcuradorNew = expActualizarMapDatosNewMap.get("PRD6_NUMERO_DESIGNA_PROCURADOR") != null ? expActualizarMapDatosNewMap.get("PRD6_NUMERO_DESIGNA_PROCURADOR").trim() : "";
					
					String anioDesignaProcuradorOld = expActualizarMapDatosOriginalMap.get("PRD8_ANIO_DESIGNA_PROCURADOR") != null ? expActualizarMapDatosOriginalMap.get("PRD8_ANIO_DESIGNA_PROCURADOR").trim() : "";
					String anioDesignaProcuradorNew = expActualizarMapDatosNewMap.get("PRD8_ANIO_DESIGNA_PROCURADOR") != null ? expActualizarMapDatosNewMap.get("PRD8_ANIO_DESIGNA_PROCURADOR").trim() : "";
					
					
					
					
					String organoJudicialOld = expActualizarMapDatosOriginalMap.get("PRJ3_ORGANO_JUDICIAL") != null ? expActualizarMapDatosOriginalMap.get("PRJ3_ORGANO_JUDICIAL").trim() : "";
					String organoJudicialNew = expActualizarMapDatosNewMap.get("PRJ3_ORGANO_JUDICIAL") != null ? expActualizarMapDatosNewMap.get("PRJ3_ORGANO_JUDICIAL").trim() : "";

					String tipoProcedimientoOld = expActualizarMapDatosOriginalMap.get("PRJ4_TIPO_PROCED_JUDICIAL") != null ? expActualizarMapDatosOriginalMap.get("PRJ4_TIPO_PROCED_JUDICIAL").trim() : "";
					String tipoProcedimientoNew = expActualizarMapDatosNewMap.get("PRJ4_TIPO_PROCED_JUDICIAL") != null ? expActualizarMapDatosNewMap.get("PRJ4_TIPO_PROCED_JUDICIAL").trim() : "";

					String numProcedimientoOld = expActualizarMapDatosOriginalMap.get("PRJ5_NUM_PROCEDIMIENTO") != null ? expActualizarMapDatosOriginalMap.get("PRJ5_NUM_PROCEDIMIENTO").trim() : "";
					String numProcedimientoNew = expActualizarMapDatosNewMap.get("PRJ5_NUM_PROCEDIMIENTO") != null ? expActualizarMapDatosNewMap.get("PRJ5_NUM_PROCEDIMIENTO").trim() : "";
					
					
					
					if (colegiadoOld.trim().equals("") && !colegiadoNew.trim().equals("")) {
						// Vamos a ver los datos del procedimiento judicial (tribunal, procedimiento y número de Procedimiento)
						// IF OLD.PRJ3_ORGANO_JUDICIAL == NEW. PRJ3_ORGANO_JUDICIAL && OLD.PRJ4_TIPO_PROCED_JUDICIAL == NEW.PRJ4_TIPO_PROCED_JUDICIAL && OLD.PRJ5_NUM_PROCEDIMIENTO == NEW.PRJ5_NUM_PROCEDIMIENTO
						// En tal caso se hara un turnado de profesional y si no se hara una nueva designacion de abogado
						if (organoJudicialOld.equals(organoJudicialNew) && tipoProcedimientoOld.equals(tipoProcedimientoNew) && numProcedimientoOld.equals(numProcedimientoNew)) {
							if(isAñadidaSegmentosTipoMod){
								lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
								numActualizacion++;
							}
//							hay que modificar EXP por AXP y añadirle el tipo de actualizacion ("002" Turnado profesional)
							lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"002"));
							lineasFicheroAct.add(getSegmento("PFA",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,true));
							isAñadidaSegmentosTipoMod = true;
							
						} else {
							if(isAñadidaSegmentosTipoMod){
								lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
								numActualizacion++;
							}
//							hay que modificar EXP por AXP y añadirle el tipo de actualizacion ("010"	Nueva designacion)
							lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"010"));
							lineasFicheroAct.add(getSegmento("PRN",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,true));
							lineasFicheroAct.add(getSegmento("PFA",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,true));
							isAñadidaSegmentosTipoMod = true;

						}

					}else if (!colegiadoOld.trim().equals(colegiadoNew.trim())) {
						
//						si es el mismo procedimiento es un cambio de profesional designado. Si es distinto es una nueva designacion
						if (organoJudicialOld.equals(organoJudicialNew) && tipoProcedimientoOld.equals(tipoProcedimientoNew) && numProcedimientoOld.equals(numProcedimientoNew)) {
							if(isAñadidaSegmentosTipoMod){
								lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
								numActualizacion++;
							}
//							hay que modificar EXP por AXP y añadirle el tipo de actualizacion ("002" Turnado profesional)
							lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"002"));
							lineasFicheroAct.add(getSegmento("PFA",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,true));
							//La fecha de baja del colegiado sustituido es la fecha de designacion del nuevo
//							expActualizarMapDatosOriginalMap.put("PRD2_FECHA_DESIGNA", expActualizarMapDatosNewMap.get("PRD2_FECHA_DESIGNA"));
							lineasFicheroAct.add(getSegmento("PRS",expActualizarMapDatosOriginalMap,expActualizarMapDatosOriginalMap,true));
							isAñadidaSegmentosTipoMod = true;
						}else{
							if(isAñadidaSegmentosTipoMod){
								lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
								numActualizacion++;
							}
							lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"010"));
							lineasFicheroAct.add(getSegmento("PRN",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,true));
							lineasFicheroAct.add(getSegmento("PFA",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,true));
							isAñadidaSegmentosTipoMod = true;
							
						}
						
					}else if (colegiadoOld.trim().equals(colegiadoNew.trim()) && !colegiadoOld.trim().equals("")) {
						
						//Si hay una nueva designacion para elmismo abogado
						if(!numDesignaOld.equals(numDesignaNew) || !anioDesignaOld.equals(anioDesignaNew)){
							if(isAñadidaSegmentosTipoMod){
								lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
								numActualizacion++;
							}
							lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"010"));
							lineasFicheroAct.add(getSegmento("PRN",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,true));
							lineasFicheroAct.add(getSegmento("PFA",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,true));
							isAñadidaSegmentosTipoMod = true;
						}else if (organoJudicialOld.equals(organoJudicialNew) && tipoProcedimientoOld.equals(tipoProcedimientoNew) && numProcedimientoOld.equals(numProcedimientoNew)) {
							//Mismo colegiado y mismo procedimineto no se hace nada
						}else{
							if(isAñadidaSegmentosTipoMod){
								lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
								numActualizacion++;
							}
							//Nueva designacion
							lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"010"));
							lineasFicheroAct.add(getSegmento("PRN",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,true));
							lineasFicheroAct.add(getSegmento("PFA",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,true));
							isAñadidaSegmentosTipoMod = true;
						}
					}
					if (procuradorOld.trim().equals("") && !procuradorNew.trim().equals("")) {
						if(isAñadidaSegmentosTipoMod){
							lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
							numActualizacion++;
						}
						// Vamos a ver los datos del procedimiento judicial (tribunal, procedimiento y número de Procedimiento)
						// IF OLD.PRJ3_ORGANO_JUDICIAL == NEW. PRJ3_ORGANO_JUDICIAL && OLD.PRJ4_TIPO_PROCED_JUDICIAL == NEW.PRJ4_TIPO_PROCED_JUDICIAL && OLD.PRJ5_NUM_PROCEDIMIENTO == NEW.PRJ5_NUM_PROCEDIMIENTO
						// En tal caso se hara un turnado de profesional y si no se hara una nueva designacion de abogado
						if (organoJudicialOld.equals(organoJudicialNew) && tipoProcedimientoOld.equals(tipoProcedimientoNew) && numProcedimientoOld.equals(numProcedimientoNew)) {
//							hay que modificar EXP por AXP y añadirle el tipo de actualizacion ("002" Turnado profesional)
							lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"002"));
							lineasFicheroAct.add(getSegmento("PFA",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
							isAñadidaSegmentosTipoMod = true;
						} else {
//							hay que modificar EXP por AXP y añadirle el tipo de actualizacion ("010"	Nueva designacion)
							lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"010"));
							lineasFicheroAct.add(getSegmento("PRN",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
							lineasFicheroAct.add(getSegmento("PFA",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
							isAñadidaSegmentosTipoMod = true;

						}

					}else if (!procuradorOld.trim().equals(procuradorNew.trim())) {
						
//						si es el mismo procedimiento es un cambio de profesional designado. Si es distinto es una nueva designacion
						if (organoJudicialOld.equals(organoJudicialNew) && tipoProcedimientoOld.equals(tipoProcedimientoNew) && numProcedimientoOld.equals(numProcedimientoNew)) {
							if(isAñadidaSegmentosTipoMod){
								lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
								numActualizacion++;
							}
//							hay que modificar EXP por AXP y añadirle el tipo de actualizacion ("002" Turnado profesional)
							lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"002"));
							lineasFicheroAct.add(getSegmento("PFA",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
							//La fecha de baja del colegiado sustituido es la fecha de designacion del nuevo
							expActualizarMapDatosOriginalMap.put("PRD2_FECHA_DESIGNA", expActualizarMapDatosNewMap.get("PRD2_FECHA_DESIGNA"));
							lineasFicheroAct.add(getSegmento("PRS",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
							isAñadidaSegmentosTipoMod = true;
						}else{
							if(isAñadidaSegmentosTipoMod){
								lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
								numActualizacion++;
							}
							lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"010"));
							lineasFicheroAct.add(getSegmento("PRN",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
							lineasFicheroAct.add(getSegmento("PFA",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
							isAñadidaSegmentosTipoMod = true;
							
						}
						
					}else if (procuradorOld.trim().equals(procuradorNew.trim()) && !procuradorOld.trim().equals("")) {
						
						if(!numDesignaProcuradorOld.equals(numDesignaProcuradorNew) || !anioDesignaProcuradorOld.equals(anioDesignaProcuradorNew)){
							if(isAñadidaSegmentosTipoMod){
								lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
								numActualizacion++;
							}
							lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"010"));
							lineasFicheroAct.add(getSegmento("PRN",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
							lineasFicheroAct.add(getSegmento("PFA",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
							isAñadidaSegmentosTipoMod = true;
						}else	if (organoJudicialOld.equals(organoJudicialNew) && tipoProcedimientoOld.equals(tipoProcedimientoNew) && numProcedimientoOld.equals(numProcedimientoNew)) {
							//Mismo colegiado y mismo procedimineto no se hace nada
						}else{
							//Nueva designacion
							if(isAñadidaSegmentosTipoMod){
								lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
								numActualizacion++;
							}
							lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"010"));
							lineasFicheroAct.add(getSegmento("PRN",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
							lineasFicheroAct.add(getSegmento("PFA",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
							isAñadidaSegmentosTipoMod = true;
						}
					}
				}else if (segmentoActualizar.equals("PRJ") && !segmentosActualizarSetValue.contains("PRD") ) {
					if(isAñadidaSegmentosTipoMod){
						lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
						numActualizacion++;
					}
//					011	Actualización de Órgano y procedimiento judicial
					lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"011"));
					lineasFicheroAct.add(getSegmento("PRO",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
					lineasFicheroAct.add(getSegmento("PRA",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
					isAñadidaSegmentosTipoMod = true;
					
					
				}else if (segmentoActualizar.equals("SOL")) {
//					006	Actualización datos solicitante
					if(isAñadidaSegmentosTipoMod){
						lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
						numActualizacion++;
					}
					lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"006"));
					lineasFicheroAct.add(getSegmento("SOA",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
					isAñadidaSegmentosTipoMod = true;
					
				}else if (segmentoActualizar.equals("DOM")) {
//					007	Actualización domicilio solicitante. Hay que enviar primero el SOA
					if(isAñadidaSegmentosTipoMod){
						lineasFicheroAct.add(getSegmentoCAB("CAB",expActualizarMapDatosNewMap,numActualizacion));
						numActualizacion++;
					}
					lineasFicheroAct.add(getSegmentoAxp(expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,"007"));
					lineasFicheroAct.add(getSegmento("SOA",null,expActualizarMapDatosNewMap,false));
					lineasFicheroAct.add(getSegmento("DOM",expActualizarMapDatosNewMap,expActualizarMapDatosOriginalMap,false));
					isAñadidaSegmentosTipoMod = true;
					
				}
			}
			   
//			String keyEJG = expActualizarMapDatosNewMap.get("EXP2_ANIO_EXPEDIENTE")+"/"+expActualizarMapDatosNewMap.get("EXP1_NUM_EXPEDIENTE");
			String keyExpIncluidosFicheroAct = expActualizarMapDatosNewMap.get("CAB2_NUMERO_INTERCAMBIO");
			
			expInluidosFicheroActualizacion.put(keyExpIncluidosFicheroAct, lineasFicheroAct);
		}
		
		Iterator<String> pruebaFinal = expInluidosFicheroActualizacion.keySet().iterator();
		while (pruebaFinal.hasNext()) {
			String key = (String) pruebaFinal.next();
			List<String> arrayList = expInluidosFicheroActualizacion.get(key);
			for (int i = 0; i < arrayList.size(); i++) {
				String linea = arrayList.get(i);
				System.out.println(linea);
				
			}
			
			
		}
		
		
		
		return expInluidosFicheroActualizacion;
	}
	
	private String getSegmentoAxp(Map<String, String> expHashtableNew, Map<String, String> expHashtableOriginal,String tipoActualizacion){
		StringBuilder linea = new StringBuilder("AXP");
		linea.append(tipoActualizacion);
		
//		EXP1_NUM_EXPEDIENTE           VARCHAR2(8),
//		  EXP2_ANIO_EXPEDIENTE          VARCHAR2(4),
//		  EXP3_TIPO_EXPEDIENTE          CHAR(3),
//		  EXP4_TIPO_EXPEDIENTE_EXTRAORD VARCHAR2(27),
//		  EXP5_FECHA_SOLICITUD          VARCHAR2(8),
//		  EXP6_COLEGIO_ABOGADOS         VARCHAR2(5),
//		  EXP7_CALIFICACION             VARCHAR2(3),
//		  EXP8_FECHA_DICTAMEN           VARCHAR2(8),
//		  EXP9_FUNDAMENTO_CALIFICACION  VARCHAR2(3),
//		  EXP10_PRECISA_ABOGADO         CHAR(1),
//		  EXP11_PRECISA_PROCURADOR      CHAR(1),
//		  EXP12_PROCEDENCIA             VARCHAR2(100),
//		  EXP13_OBSERVACIONES           VARCHAR2(1000),
//		  EXP14_EEJG    
		
//				AXP1	Tipo de actualización	VARCHAR2(3)	AJGR_C_TIPOACTUALIZACIÓN(  1	Actualización datos expediente
//				AXP2	Nº  de expediente del Colegio de Abogados (original)	VARCHAR2(8)	
//				AXP3	Año de expediente del Colegio de Abogados (original)	NUMBER(4)	
//				AXP4	Nº  de expediente del Servicio (original)	NUMBER(7)	
//				AXP5	Año de expediente del Servicio (original)	NUMBER(4)	
		
//				AXP6	Calificación provisional del Colegio de Abogados	VARCHAR2(3)	AJGR_C_TIPOCALIFCOLEGIO
//				AXP7	Fecha de la calificación provisional realizada	DATE
//				(yyyymmdd)	
//				AXP8	Fundamento calificación	VARCHAR2(3)	AJGR_C_TIPOFUNDAMENTOS
//				AXP9	Precisa abogado	VARCHAR2(1)	
//				AXP10	Precisa procurador	VARCHAR2(1)	
//				AXP11		Procedencia	VARCHAR2(100)	
//				AXP12	Fecha Anulación	NUMBER(8)	Se lee y recupera del fichero pero no se guarda en AJGR
//				AXP13	Mot.anulacion	NUMBER(4)	Se lee y recupera del fichero pero no se guarda en AJGR
//				AXP14	Nº de documentos electrónicos	VARCHAR2(2)	Puede tener valor vacío o introducir un número de hasta 2 dígitos.
		
		linea.append(StringHelper.rellena(expHashtableOriginal.get("EXP1_NUM_EXPEDIENTE"), '0', 8, StringHelper.IZQUIERDA));
		linea.append(StringHelper.rellena(expHashtableOriginal.get("EXP2_ANIO_EXPEDIENTE"), '0', 4, StringHelper.IZQUIERDA));
		//Tenemos que buscar el expediente CAJG ya qu eno se envia en los expedientes
		linea.append(StringHelper.rellena("", ' ', 7, StringHelper.DERECHA));
		linea.append(StringHelper.rellena("", ' ', 4, StringHelper.DERECHA));
		linea.append(StringHelper.rellena(expHashtableNew.get("EXP7_CALIFICACION"), ' ', 3, StringHelper.DERECHA));
		linea.append(StringHelper.rellena(expHashtableNew.get("EXP8_FECHA_DICTAMEN"), ' ', 8, StringHelper.DERECHA));
		linea.append(StringHelper.rellena(expHashtableNew.get("EXP9_FUNDAMENTO_CALIFICACION"), ' ', 3, StringHelper.DERECHA));
		linea.append(StringHelper.rellena(expHashtableNew.get("EXP10_PRECISA_ABOGADO"), ' ', 1, StringHelper.IZQUIERDA));
		linea.append(StringHelper.rellena(expHashtableNew.get("EXP11_PRECISA_PROCURADOR"), ' ', 1, StringHelper.IZQUIERDA));
		linea.append(StringHelper.rellena(expHashtableNew.get("EXP12_PROCEDENCIA"), ' ', 100, StringHelper.DERECHA));
		//Fecha anulacion
		linea.append(StringHelper.rellena("", '0', 8, StringHelper.IZQUIERDA));
		//Motivo anulacion
		linea.append(StringHelper.rellena("", '0', 4, StringHelper.IZQUIERDA));
		//nº docuemntos el enviados
		linea.append(StringHelper.rellena("", ' ', 2, StringHelper.DERECHA));
		
		return linea.toString();
		
		
	}
	private String getSegmentoCAB(String segmento, Map<String, String> expHashtableNew, int numActualizacion) {

		StringBuilder linea = new StringBuilder();

		linea.append(segmento);
		// pONEMOS EL TIPO DE FICHERO QUE ES 002 Actualización
		linea.append("002");
		if(numActualizacion>1){
			long numIntercambio = Long.parseLong(expHashtableNew.get("CAB2_NUMERO_INTERCAMBIO"))+1;
			linea.append(StringHelper.rellena(""+numIntercambio, '0', 7, StringHelper.IZQUIERDA));
		}
		else
			linea.append(StringHelper.rellena(expHashtableNew.get("CAB2_NUMERO_INTERCAMBIO"), '0', 7, StringHelper.IZQUIERDA));
		linea.append(StringHelper.rellena(expHashtableNew.get("CAB3_ANIO_INTERCAMBIO"), '0', 4, StringHelper.IZQUIERDA));
		return linea.toString();

	}

	private String getSegmento(String segmento, Map<String, String> expHashtableNew, Map<String, String> expHashtableOriginal, boolean isAbogado) {

		StringBuilder linea = new StringBuilder();

		if (segmento.equals("PFA")) {
			// PFA1 Abogado/Procurador NO VARCHAR2(1) A = Abogado P = Procurador
			// PFA2 Colegio Profesional NO VARCHAR2(5) INTS_C_COLEGIOPROFESIONAL
			// PFA3 Número de colegiado SÍ VARCHAR2(6) INTS_PROFESIONAL
			// PFA4 Fecha de designación NO DATE(yyyymmdd)
			// PFA5 Número de designación SÍ VARCHAR2(6)
			// PFA6 Año de designación NO NUMBER(4)

			// No tenemos el codigo de colegio de procurador por lo que no ponemos nada

			linea.append(segmento);
			if (isAbogado) {
				linea.append("A");
				// Codigo ejis del colegio de alcala de henares
				linea.append("28005");
				linea.append(StringHelper.rellena(expHashtableNew.get("PRD1_NCOLEGIADOABOGADO"), '0', 6, StringHelper.IZQUIERDA));
				linea.append(expHashtableNew.get("PRD2_FECHA_DESIGNA"));
				linea.append(StringHelper.rellena(expHashtableNew.get("PRD3_NUMERO_DESIGNA"), '0', 6, StringHelper.IZQUIERDA));
				linea.append(StringHelper.rellena(expHashtableNew.get("PRD4_ANIO_DESIGNA"), '0', 4, StringHelper.IZQUIERDA));

			} else {
				linea.append("P");
				// Codigo ejis del colegio de procuradores de madrid
				linea.append("28079");
				linea.append(StringHelper.rellena(expHashtableNew.get("PRD5_NCOLEGIADO_PROCURADOR"), '0', 6, StringHelper.IZQUIERDA));
				linea.append(expHashtableNew.get("PRD7_FECHA_DESIGNA_PROCURADOR"));
				linea.append(StringHelper.rellena(expHashtableNew.get("PRD8_NUMERO_DESIGNA_PROCURADOR"), '0', 6, StringHelper.IZQUIERDA));
				linea.append(StringHelper.rellena(expHashtableNew.get("PRD6_ANIO_DESIGNA_PROCURADOR"), '0', 4, StringHelper.IZQUIERDA));
			}

		} else if (segmento.equals("PRN")) {
			linea.append(segmento);

			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ1_ORDEN_JURISDICCIONAL"), ' ', 1, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ3_ORGANO_JUDICIAL"), ' ', 10, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ4_TIPO_PROCED_JUDICIAL"), ' ', 3, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ5_NUM_PROCEDIMIENTO"), '0', ' ', 7, StringHelper.IZQUIERDA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ6_ANIO_PROCEDIMIENTO"), '0', ' ', 4, StringHelper.IZQUIERDA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ2_CLASE_ASUNTO_TIPO_ORDEN"), ' ', 3, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ7_NATURALEZA_PROCEDIMIENTO"), ' ', 3, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ8_ESTADO_PROCEDIMIENTO"), ' ', 3, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ9_DESCRIPCION_PROCEDIMIEN"), ' ', 50, StringHelper.DERECHA));
			linea.append("1");

			// PRN1 Orden Jurisdiccional VARCHAR2(1) INTS_C_AMBITOJURIS
			// PRN2 Órgano Judicial VARCHAR2(10) INTS_C_UFORGJUDICIAL
			// PRN3 Tipo procedimiento (*) VARCHAR2(3) INTS_C_TIPOPRORGJUD
			// PRN4 Número del procedimiento NUMBER(7)
			// PRN5 Año del procedimiento NUMBER(4)
			// PRN6 Clase Asunto VARCHAR2(3) AJGR_C_CLASEASUNTO
			// PRN7 Naturaleza del Procedimiento (violencia doméstica…) VARCHAR2(3) AJGR_C_TIPONATURALEZA
			// PRN8 Estado del Procedimiento VARCHAR2(3) AJGR_C_TIPOSITUACION
			// PRN9 Descripción del Procedimiento VARCHAR2(50)
			// PRN10 Motivo del nuevo procedimiento VARCHAR2(1) 0 = Procedimiento incoado
			// 1 = Nueva instancia

			// PRJ1 Orden Jurisdiccional SI SI VARCHAR2(1) INTS_C_AMBITOJURIS
			// PRJ3 Órgano Judicial SÍ Código Juzgado 13 SI VARCHAR2(10) INTS_C_UFORGJUDICIAL
			// PRJ4 Tipo procedimiento judicial (***) SÍ Código Procedimiento 4 (4) VARCHAR2(3) INTS_C_TIPOPRORGJUD
			// PRJ5 Número del procedimiento SÍ Número Procedimiento 10 (4) NUMBER(7)
			// PRJ6 Año del procedimiento NO (4) NUMBER(4)
			// PRJ2 Clase Asunto (por tipo de orden) (**) SÍ SÍ VARCHAR2(3) AJGR_C_CLASEASUNTO
			// PRJ7 Naturaleza del Procedimiento (violencia doméstica…) SÍ SI VARCHAR2(3) AJGR_C_TIPONATURALEZA
			// PRJ8 Estado del Procedimiento NO (4) VARCHAR2(3) AJGR_C_TIPOSITUACION
			// PRJ9 Descripción del Procedimiento SÍ Descripción del proc. NO VARCHAR2(50)

		} else if (segmento.equals("PRS")) {

			linea.append(segmento);
			if (isAbogado) {
				linea.append("A");
				// Codigo ejis del colegio de alcala de henares
				linea.append("28005");
				linea.append(StringHelper.rellena(expHashtableOriginal.get("PRD1_NCOLEGIADOABOGADO"), '0', 6, StringHelper.IZQUIERDA));
				// La fecha de baja del colegiado es la fecha de designacion del nuevo
				linea.append(expHashtableNew.get("PRD2_FECHA_DESIGNA"));
				linea.append(StringHelper.rellena(expHashtableOriginal.get("PRD3_NUMERO_DESIGNA"), '0', 6, StringHelper.IZQUIERDA));
				linea.append(StringHelper.rellena(expHashtableOriginal.get("PRD4_ANIO_DESIGNA"), '0', 4, StringHelper.IZQUIERDA));

			} else {
				linea.append("P");
				// Codigo ejis del colegio de procuradores de madrid
				linea.append("28079");
				linea.append(StringHelper.rellena(expHashtableOriginal.get("PRD5_NCOLEGIADO_PROCURADOR"), '0', 6, StringHelper.IZQUIERDA));
				// La fecha de baja del colegiado es la fecha de designacion del nuevo
				linea.append(expHashtableNew.get("PRD7_FECHA_DESIGNA_PROCURADOR"));
				linea.append(StringHelper.rellena(expHashtableOriginal.get("PRD8_NUMERO_DESIGNA_PROCURADOR"), '0', 6, StringHelper.IZQUIERDA));
				linea.append(StringHelper.rellena(expHashtableOriginal.get("PRD6_ANIO_DESIGNA_PROCURADOR"), '0', 4, StringHelper.IZQUIERDA));
			}
			// Estos tres caracteres es para el motivo del cambio, que si es obligatorio habra que sacarlo
			linea.append("   ");

			// PRS1 Abogado/Procurador NO VARCHAR2(1) A = Abogado P = Procurador
			// PRS2 Colegio Profesional NO VARCHAR2(5) INTS_COLEGIOPROFESIONAL
			// PRS3 Número de colegiado SÍ VARCHAR2(6) INTS_PROFESIONAL
			// PRS4 Fecha de baja NO DATE(yyyymmdd)
			// PRS5 Número de designación NO VARCHAR2(6)
			// PRS6 Año designación NO NUMBER(4)
			// PRS7 Motivo del cambio de profesional NO VARCHAR2(3) AJGR_C_TIPOMOTCAMBIO

			// PRD1 Número de colegiado del abogado designado SÍ Cronológico Letrado 6 (2) VARCHAR2(6) INTS_PROFESIONAL
			// PRD2 Fecha de la designación del abogado SÍ Fecha designación 10 (2) DATE
			// (yyyymmdd)
			// PRD3 Número de designación del abogado SÍ Nº Designación 5 (2) VARCHAR2(6))
			// PRD4 Año de designación del abogado NO (2) NUMBER(4)
			// PRD5 Número de colegiado del procurador designado SÍ (3) VARCHAR2(6) INTS_PROFESIONAL
			// PRD6 Año de designación del procurador NO (3) NUMBER(4)
			// PRD7 Fecha de la designación del proc. NO (3) DATE
			// (yyyymmdd)
			// PRD8 Número de designación del proc. SÍ Nº Designación 6 (3) VARCHAR2(6)
		} else if (segmento.equals("PRO")) {
			// PRO1 Fecha de designación del abogado NO 6 (20) DATE(yyyymmdd)
			// PRO2 Número de designación SÍ Nº Designación 5 (20) VARCHAR2(6)
			// PRO3 Año de designación NO (20) NUMBER(4)
			linea.append(segmento);

			linea.append(StringHelper.rellena(expHashtableNew.get("PRD2_FECHA_DESIGNA"), '0', 8, StringHelper.IZQUIERDA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRD3_NUMERO_DESIGNA"), '0', 6, StringHelper.IZQUIERDA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRD4_ANIO_DESIGNA"), '0', 4, StringHelper.IZQUIERDA));

		} else if (segmento.equals("PRA")) {
			linea.append(segmento);
			// Si han modificado el organo tipo proced y nuemro proced ha entrado por nueva designacion

			linea.append(StringHelper.rellena(expHashtableOriginal.get("PRJ1_ORDEN_JURISDICCIONAL"), ' ', 1, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableOriginal.get("PRJ3_ORGANO_JUDICIAL"), ' ', 10, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableOriginal.get("PRJ4_TIPO_PROCED_JUDICIAL"), ' ', 3, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableOriginal.get("PRJ5_NUM_PROCEDIMIENTO"), '0', ' ', 7, StringHelper.IZQUIERDA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ6_ANIO_PROCEDIMIENTO"), '0', ' ', 4, StringHelper.IZQUIERDA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ2_CLASE_ASUNTO_TIPO_ORDEN"), ' ', 3, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ7_NATURALEZA_PROCEDIMIENTO"), ' ', 3, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ8_ESTADO_PROCEDIMIENTO"), ' ', 3, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ9_DESCRIPCION_PROCEDIMIEN"), ' ', 50, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ10_ACUSACION_PARTICULAR"), ' ', 1, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ11_PETICION_PARTICULAR"), ' ', 1, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("PRJ12_PARTE_CONTRARIA"), ' ', 100, StringHelper.DERECHA));
			linea.append(StringHelper.rellena("", ' ', 8, StringHelper.DERECHA));
			linea.append("5");

			// PRA1 Orden Jurisdiccional (original) VARCHAR2(1) INTS_C_AMBITOJURIS
			// PRA2 Órgano Judicial (original) Código Juzgado VARCHAR2(10) INTS_C_UFORGJUDICIAL
			// PRA3 Tipo procedimiento (original) (*) Código Procedimiento VARCHAR2(3) INTS_C_TIPOPRORGJUD
			// PRA4 Número del procedimiento (original) Número Procedimiento NUMBER(7)
			// PRA5 Año del procedimiento NUMBER(4)
			// PRA6 Clase Asunto (por tipo de orden) VARCHAR2(3) AJGR_C_CLASEASUNTO
			// PRA7 Naturaleza del Procedimiento (violencia doméstica…) VARCHAR2(3) AJGR_C_TIPONATURALEZA
			// PRA8 Estado del Procedimiento VARCHAR2(3) AJGR_C_TIPOSITUACION
			// PRA9 Descripción del Procedimiento Descripción del proc. VARCHAR2(50)
			// PRA10 Acusación particular VARCHAR2(1)
			// PRA11 Petición particular VARCHAR2(1)
			// PRA12 Parte contraria VARCHAR2(100)
			// PRA13 Fecha de la modificación DATE(yyyymmdd)
			// PRA14 Motivo de la modificación VARCHAR2(1)
			// 0 = Procedimiento incoado
			// 4 = Competencia otro tribunal
			// 5 = Otros

			// PRJ_INTERCAMBIO_ID NUMBER(10) not null,
			// PRJ1_ORDEN_JURISDICCIONAL CHAR(1),
			// PRJ2_CLASE_ASUNTO_TIPO_ORDEN VARCHAR2(3),
			// PRJ3_ORGANO_JUDICIAL VARCHAR2(10),
			// PRJ4_TIPO_PROCED_JUDICIAL VARCHAR2(3),
			// PRJ5_NUM_PROCEDIMIENTO VARCHAR2(7),
			// PRJ6_ANIO_PROCEDIMIENTO VARCHAR2(4),
			// PRJ7_NATURALEZA_PROCEDIMIENTO VARCHAR2(3),
			// PRJ8_ESTADO_PROCEDIMIENTO VARCHAR2(20),
			// PRJ9_DESCRIPCION_PROCEDIMIEN VARCHAR2(50),
			// PRJ10_ACUSACION_PARTICULAR VARCHAR2(1),
			// PRJ11_PETICION_PARTICULAR VARCHAR2(1),
			// PRJ12_PARTE_CONTRARIA VARCHAR2(100)
		} else if (segmento.equals("SOA")) {

			linea.append(segmento);

			linea.append(StringHelper.rellena(expHashtableOriginal.get("SOL1_TIPO_IDENTIFICACION"), '0', 1, StringHelper.IZQUIERDA));
			linea.append(StringHelper.rellena(expHashtableOriginal.get("SOL2_IDENTIFICACION_SOLICIT"), ' ', 11, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableOriginal.get("SOL3_TIPOPERSONA"), '0', 3, StringHelper.IZQUIERDA));
			linea.append(StringHelper.rellena(expHashtableOriginal.get("SOL4_NOMBRE_SOLICITANTE"), ' ', 50, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableOriginal.get("SOL5_APELLIDO1_SOLICITANTE"), ' ', 50, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableOriginal.get("SOL6_APELLIDO2_SOLICITANTE"), ' ', 50, StringHelper.DERECHA));
			if(expHashtableNew!=null){
			
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL7_SEXO_SOLICITANTE"), ' ', 1, StringHelper.IZQUIERDA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL13_NOMBRE_ENTIDAD_JURIDICA"), ' ', 100, StringHelper.DERECHA));
	
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL1_TIPO_IDENTIFICACION"), '0', ' ', 1, StringHelper.IZQUIERDA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL2_IDENTIFICACION_SOLICIT"), ' ', ' ', 11, StringHelper.DERECHA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL3_TIPOPERSONA"), '0', ' ', 3, StringHelper.IZQUIERDA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL4_NOMBRE_SOLICITANTE"), ' ', 50, StringHelper.DERECHA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL5_APELLIDO1_SOLICITANTE"), ' ', 50, StringHelper.DERECHA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL6_APELLIDO2_SOLICITANTE"), ' ', 50, StringHelper.DERECHA));
	
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL8_NACIONALIDAD"), ' ', 3, StringHelper.DERECHA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL9_SITUACION_PERSONAL"), ' ', 1, StringHelper.DERECHA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL10_FECHA_NACIMIENTO"), ' ', 8, StringHelper.DERECHA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL11_PROFESION"), ' ', 50, StringHelper.DERECHA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL12_REGIMEN_ECONOMICO"), ' ', 1, StringHelper.DERECHA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL13_NOMBRE_ENTIDAD_JURIDICA"), ' ', 100, StringHelper.DERECHA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL14_USO_DOMICILIO"), ' ', 3, StringHelper.DERECHA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL15_TIPO_TELEFONO"), ' ', 1, StringHelper.DERECHA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL16_NUMERO_TELEFONO"), ' ', 15, StringHelper.DERECHA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL17_PRESO"), ' ', 1, StringHelper.DERECHA));
				linea.append(StringHelper.rellena(expHashtableNew.get("SOL18_CENTRO_PENITENCIARIO"), ' ', 10, StringHelper.DERECHA));
				// Esta linea vacia para para SOA26_Autorización
				linea.append(" ");
				// Esta linea vacia para para SOA27_Denunciado
				linea.append(" ");
			}else{
				linea.append(StringHelper.rellena("", ' ', 1, StringHelper.IZQUIERDA));
				linea.append(StringHelper.rellena("", ' ', 100, StringHelper.DERECHA));
				linea.append(StringHelper.rellena("", ' ', ' ', 1, StringHelper.IZQUIERDA));
				linea.append(StringHelper.rellena("", ' ', ' ', 11, StringHelper.DERECHA));
				linea.append(StringHelper.rellena("", ' ', ' ', 3, StringHelper.IZQUIERDA));
				linea.append(StringHelper.rellena("", ' ', 50, StringHelper.DERECHA));
				linea.append(StringHelper.rellena("", ' ', 50, StringHelper.DERECHA));
				linea.append(StringHelper.rellena("", ' ', 50, StringHelper.DERECHA));
	
				linea.append(StringHelper.rellena("", ' ', 3, StringHelper.DERECHA));
				linea.append(StringHelper.rellena("", ' ', 1, StringHelper.DERECHA));
				linea.append(StringHelper.rellena("", ' ', 8, StringHelper.DERECHA));
				linea.append(StringHelper.rellena("", ' ', 50, StringHelper.DERECHA));
				linea.append(StringHelper.rellena("", ' ', 1, StringHelper.DERECHA));
				linea.append(StringHelper.rellena("", ' ', 100, StringHelper.DERECHA));
				linea.append(StringHelper.rellena("", ' ', 3, StringHelper.DERECHA));
				linea.append(StringHelper.rellena("", ' ', 1, StringHelper.DERECHA));
				linea.append(StringHelper.rellena("", ' ', 15, StringHelper.DERECHA));
				linea.append(StringHelper.rellena("", ' ', 1, StringHelper.DERECHA));
				linea.append(StringHelper.rellena("", ' ', 10, StringHelper.DERECHA));
				// Esta linea vacia para para SOA26_Autorización
				linea.append(" ");
				// Esta linea vacia para para SOA27_Denunciado
				linea.append(" ");
			}
			// SOA1 Tipo Identificación (original) VARCHAR2(1) INTS_C_TIPOIDENTIFICACION
			// SOA2 Identificación del sol. (original) DNI Litigante VARCHAR2(11)
			// SOA3 Tipo persona solicitante (original) VARCHAR2(3) INTS_C_TIPOPERSONA
			// SOA4 Nombre completo sol. (original) Nombre Litigante VARCHAR2(50)
			// SOA5 Primer apellido sol. (original) Primer Apellido Litigante VARCHAR2(50)
			// SOA6 Segundo apellido sol. (original) Segundo Apellido Litigante VARCHAR2(50)
			// SOA7 Sexo del solicitante VARCHAR2(1) 0 = Hombre
			// 1 = Mujer
			// SOA8 Nombre entidad VARCHAR2(100)
			// SOA9 Tipo Identificación actualizada VARCHAR2(1) INTS_C_TIPOIDENTIFICACION
			// SOA10 Identificación del solicitante actualizado DNI Litigante VARCHAR2(11)
			// SOA11 Tipo persona solicitante actualizada VARCHAR2(3) INTS_C_TIPOPERSONA
			// SOA12 Nombre completo del solicitante actualizado Nombre Litigante VARCHAR2(50)
			// SOA13 Primer apellido del solicitante actualizado Primer Apellido Litigante VARCHAR2(50)
			// SOA14 Segundo apellido del solicitante actualizado Segundo Apellido Litigante VARCHAR2(50)
			// SOA15 País nacionalidad solicitante VARCHAR2(3) INTS_C_NACIONALIDAD
			// SOA16 Situación personal VARCHAR2(1) Soltero = S
			// SOA17 Fecha Nacimiento DATE(yyyymmdd)
			// SOA18 Profesión VARCHAR2(50)
			// SOA19 Régimen económico VARCHAR2(1) 0 = Separación de bienes
			// SOA20 Nombre entidad VARCHAR2(100)
			// SOA21 Uso domicilio VARCHAR2(3) AJGR_C_TIPOREGIMENDOM
			// SOA22 Tipo de teléfono VARCHAR2(1) INTS_C_TIPOTELEFONO
			// SOA23 Número de teléfono VARCHAR2(15)
			// SOA24 Preso VARCHAR2(1) Se lee y recupera del fichero pero no se guarda en AJGR
			// SOA25 Centro Penitenciario VARCHAR2(10) INTS_C_CPENITENCIARIO
			// ESTOS CAMPOS SON NUEVOS Y NO ESTAN EN BBDD
			// SOA26 Autorización de avisos telemáticos VARCHAR2(1) S = Autoriza
			// N = No Autoriza
			// SOA27 Denunciado VARCHAR2(1) S = Es denunciado
			// N = No es denunciado

			// Se lee y recupera del fichero pero no se guarda en AJGR

			// SOL1_TIPO_IDENTIFICACION VARCHAR2(1),
			// SOL2_IDENTIFICACION_SOLICIT VARCHAR2(11),
			// SOL3_TIPOPERSONA VARCHAR2(3),
			// SOL4_NOMBRE_SOLICITANTE VARCHAR2(50),
			// SOL5_APELLIDO1_SOLICITANTE VARCHAR2(50),
			// SOL6_APELLIDO2_SOLICITANTE VARCHAR2(50),

			// SOL7_SEXO_SOLICITANTE CHAR(1),
			// SOL8_NACIONALIDAD VARCHAR2(3),
			// SOL9_SITUACION_PERSONAL CHAR(1),
			// SOL10_FECHA_NACIMIENTO VARCHAR2(8),
			// SOL11_PROFESION VARCHAR2(50),
			// SOL12_REGIMEN_ECONOMICO VARCHAR2(1),
			// SOL13_NOMBRE_ENTIDAD_JURIDICA VARCHAR2(100),
			// SOL14_USO_DOMICILIO VARCHAR2(3),
			// SOL15_TIPO_TELEFONO VARCHAR2(1),
			// SOL16_NUMERO_TELEFONO VARCHAR2(15),
			// SOL17_PRESO VARCHAR2(1),
			// SOL18_CENTRO_PENITENCIARIO

		} else if (segmento.equals("DOM")) {

			linea.append(segmento);
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM1_TIPO_DOMICILIO"), ' ', 1, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM2_PAIS_DOMICILIO"), ' ', 3, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM3_PROVINCIA_DOMICILIO"), ' ', 2, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM4_DESCRIP_PROVIN_DOMICI_EXT"), ' ', 50, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM5_MUNICIPIO_DOMICILIO_NORM"), ' ', 3, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM6_DESC_MUNIC_DOM_SINNORM"), ' ', 50, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM7_TIPO_NUMERACION"), ' ', 1, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM8_PISO"), ' ', 50, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM9_TIPO_VIA"), ' ', 5, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM10_DESCRIPCION_TIPO_VIA"), ' ', 15, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM11_NUMERO_PORTAL"), ' ', 5, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM12_DESCRIPCION_VIA_EXTRANJ"), ' ', 10, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM13_DESCRIPCION_VIA"), ' ', 125, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM14_DESCRIPCION_VIA_COMPLETA"), ' ', 100, StringHelper.DERECHA));
			linea.append(StringHelper.rellena(expHashtableNew.get("DOM15_CODIGO_POSTAL"), '0', ' ', 10, StringHelper.IZQUIERDA));

			// DOM1_TIPO_DOMICILIO VARCHAR2(1),
			// DOM2_PAIS_DOMICILIO VARCHAR2(3),
			// DOM3_PROVINCIA_DOMICILIO VARCHAR2(2),
			// DOM4_DESCRIP_PROVIN_DOMICI_EXT VARCHAR2(50),
			// DOM5_MUNICIPIO_DOMICILIO_NORM VARCHAR2(3),
			// DOM6_DESC_MUNIC_DOM_SINNORM VARCHAR2(50),
			// DOM7_TIPO_NUMERACION VARCHAR2(1),
			// DOM8_PISO VARCHAR2(50),
			// DOM9_TIPO_VIA VARCHAR2(5),
			// DOM10_DESCRIPCION_TIPO_VIA VARCHAR2(15),
			// DOM11_NUMERO_PORTAL VARCHAR2(5),
			// DOM12_DESCRIPCION_VIA_EXTRANJ VARCHAR2(10),
			// DOM13_DESCRIPCION_VIA VARCHAR2(125),
			// DOM14_DESCRIPCION_VIA_COMPLETA VARCHAR2(100),
			// DOM15_CODIGO_POSTAL

			// DOM1 Tipo de domicilio (**) NO (15) VARCHAR2(1) 0 = Aeropuerto de Madrid-Barajas,
			// 1 = Centro Penitenciario
			// 2 = Otro domicilio
			// 3 = Desconocido
			//
			// DOM2 País del domicilio normalizado NO (16) VARCHAR2(3) SUCA_PAIS
			// DOM3 Provincia del domicilio normalizada NO (17)
			// (18) VARCHAR2(2) SUCA_PROVINCIA
			// DOM4 Descripción de la provincia o estado extranjero SÍ Provincia Litigante 20 (19) VARCHAR2(50)
			// DOM5 Municipio del domicilio normalizado NO (17)
			// (18) VARCHAR2(3) SUCA_MUNICIPIO
			// DOM6 Descripción de la ciudad o municipio sin normalizar. SÍ Localidad Litigante 30 (19) VARCHAR2(50)
			// DOM7 Tipo de numeración (Punto Kilométrico, nº de portal…) NO (17) VARCHAR2(1) SUCA_TIPO_NUMERACION
			// DOM8 Escalera, piso, puerta NO NO VARCHAR2(50)
			// DOM9 Tipo de vía normalizada NO (17) VARCHAR2(5) SUCA_TIPO_VIAL
			// DOM10 Descripción del tipo de vía sin normalizar SI Tipo de Vía 3 (18) VARCHAR2(15)
			// DOM11 Número del portal sin normalizar NO (17)
			// (18) VARCHAR2(5)
			// DOM12 Descripción de la vía, número, escalera sin normalizar NO NO VARCHAR2(10)
			// DOM13 Descripción de la vía sin normalizar SÍ Dirección Litigante 60 (17) (18) VARCHAR2(125)
			// DOM14 Descripción de la vía, número, escalera sin normalizar. SÍ Dirección Litigante 60 (19) VARCHAR2(100)
			// DOM15 Código postal sin normalizar SI Código Postal Litigante 5 (16) VARCHAR2(10)

		}

		return linea.toString();
	}

	private Hashtable<String, String> getUltimoEJGEnviado(String anio, String numExpediente)throws BusinessException  {
		
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT CAB.CAB1_TIPO_INTERCAMBIO,CAB.CAB2_NUMERO_INTERCAMBIO,CAB.CAB3_ANIO_INTERCAMBIO,EXP.*,PRD.*,PRJ.*,SOL.*,DOM.*,ECO.*,DOC.* ");
		builder.append("FROM PCAJG_ALC_INT_CAB CAB, ");
		builder.append("PCAJG_ALC_INT_EXP EXP, ");
		builder.append("PCAJG_ALC_INT_PRD PRD, ");
		builder.append("PCAJG_ALC_INT_PRJ PRJ, ");
		builder.append("PCAJG_ALC_INT_SOL SOL, ");
		builder.append("PCAJG_ALC_INT_DOM DOM, ");
		builder.append("PCAJG_ALC_INT_ECO ECO, ");
		builder.append("PCAJG_ALC_INT_DOC DOC ");
		builder.append("WHERE CAB.CAB_INTERCAMBIO_ID = EXP.EXP_INTERCAMBIO_ID(+) ");
		builder.append("AND CAB.CAB_INTERCAMBIO_ID = PRD.PRD_INTERCAMBIO_ID(+) ");
		builder.append("AND CAB.CAB_INTERCAMBIO_ID = PRJ.PRJ_INTERCAMBIO_ID(+) ");
		builder.append("AND CAB.CAB_INTERCAMBIO_ID = SOL.SOL_INTERCAMBIO_ID(+) ");
		builder.append("AND CAB.CAB_INTERCAMBIO_ID = DOM.DOM_INTERCAMBIO_ID(+) ");
		builder.append("AND CAB.CAB_INTERCAMBIO_ID = ECO.ECO_INTERCAMBIO_ID(+) ");
		builder.append("AND CAB.CAB_INTERCAMBIO_ID = DOC.DOC_INTERCAMBIO_ID(+) ");
		builder.append("AND EXP.EXP1_NUM_EXPEDIENTE = '");
		builder.append(numExpediente);
		builder.append("' ");
		builder.append("AND EXP.EXP2_ANIO_EXPEDIENTE = '");
		builder.append(anio);
		builder.append("' ");
		builder.append("AND CAB.CAB_FECHARESPUESTA IS NOT NULL ");
		builder.append("AND CAB.CAB_EST_ID = 1 ");
		builder.append("ORDER BY CAB.CAB_FECHAENVIO DESC");
		
		Hashtable ejgHashtable = null;
		try {
			Vector vHistoricoEJG = getHashSQL(builder.toString());
			ejgHashtable = (Hashtable) vHistoricoEJG.get(0);
			
		} catch (ClsExceptions e) {
			throw new BusinessException("Error en SQL"+e.toString());
		}
		return ejgHashtable;
		
		
	}
	
	public void insertaHistoricoRemesa(List<Map<String,Vector<List<String>>>>  expedientesList)throws BusinessException  {
		List<String> queryList = new ArrayList<String>();
		List<Hashtable<Integer, String>> codigosList = new ArrayList<Hashtable<Integer,String>>();
		for (Map<String, Vector<List<String>>> map : expedientesList) {
			Iterator<String> tablasHistoricoIterator = map.keySet().iterator();
			while (tablasHistoricoIterator.hasNext()) {
				String tablaKey = (String) tablasHistoricoIterator.next();
				if(tablaKey.equals("CAB")){
					actualizaCabeceraInsercionRemesa(map.get(tablaKey),  queryList, codigosList);
				}else {
					if(map.get(tablaKey)!=null && map.get(tablaKey).get(0).size()>0)
						actualizaInsercionRemesa(tablaKey,map.get(tablaKey), queryList, codigosList);
				}
			}
		}
		UserTransaction tx 	= this.usrbean.getTransactionPesada(); 			

		// Comienzo la transaccion
				
		try {
			tx.begin();
			for (int i = 0; i < queryList.size(); i++) {
				insertSQLBind(queryList.get(i),codigosList.get(i));
			}	
			tx.commit();
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception e1) {
//				e1.printStackTrace();
			}
			throw new BusinessException("Error al insertar historico de remesa enviada."+e.toString());
		}
		
		
	}
	
	
	
	public void insertaErroresHistoricoRemesa(String idInstitucion,String idRemesa, List<String> expedientesSinModificacion) throws BusinessException {
		CajgRespuestaEJGRemesaAdm respuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(this.usrbean);
		UserTransaction tx = this.usrbean.getTransaction();
		try {
			tx.begin();
			for (String numIntercambio : expedientesSinModificacion) {
				respuestaEJGRemesaAdm.insertaErrorEJGnoEnviados(idInstitucion,idRemesa,numIntercambio);
			}
			tx.commit();
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception e1) {
				// e1.printStackTrace();
			}
			throw new BusinessException("Error al insertar insertaErroresHistoricoRemesa." + e.toString());
		}
	}
	
	public void borraHistoricoRemesa(Long idRemesa,Short idInstitucion)throws BusinessException  {
		
		UserTransaction tx 	= this.usrbean.getTransactionPesada(); 			

		// Comienzo la transaccion
				
		try {
			tx.begin();
			deleteSQL(getQueryDeleteHistoricoRemesa("SOL", idRemesa, idInstitucion).toString());
			deleteSQL(getQueryDeleteHistoricoRemesa("PRD", idRemesa, idInstitucion).toString());
			deleteSQL(getQueryDeleteHistoricoRemesa("PRJ", idRemesa, idInstitucion).toString());
			deleteSQL(getQueryDeleteHistoricoRemesa("ECO", idRemesa, idInstitucion).toString());
			deleteSQL(getQueryDeleteHistoricoRemesa("DOC", idRemesa, idInstitucion).toString());
			deleteSQL(getQueryDeleteHistoricoRemesa("DOM", idRemesa, idInstitucion).toString());
			deleteSQL(getQueryDeleteHistoricoRemesa("EXP", idRemesa, idInstitucion).toString());
			deleteSQL(getQueryDeleteHistoricoRemesa("CAB", idRemesa, idInstitucion).toString());
			tx.commit();
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception e1) {
//				e1.printStackTrace();
			}
			throw new BusinessException("Error al insertar historico de remesa enviada."+e.toString());
		}
		
		
		
	}
	private StringBuilder getQueryDeleteHistoricoRemesa(String identTabla, Long idRemesa, short idInstitucion){
		StringBuilder builder = new StringBuilder();
		builder.append("DELETE FROM PCAJG_ALC_INT_");
		builder.append(identTabla);
		builder.append(" WHERE ");
		builder.append(identTabla);
		builder.append("_INTERCAMBIO_ID IN ");
		builder.append("(");
		builder.append(getQueryIdCabRemesa(idRemesa, idInstitucion));
		builder.append(")");
		return builder;
		
		
	}
	private StringBuilder getQueryIdCabRemesa(Long idRemesa, short idInstitucion){
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT CAB.CAB_INTERCAMBIO_ID ");
		builder.append("FROM PCAJG_ALC_INT_CAB CAB ");
		builder.append("WHERE TO_NUMBER(CAB.CAB2_NUMERO_INTERCAMBIO) IN ");
		builder.append("(SELECT EJGREM.NUMEROINTERCAMBIO ");
		builder.append("FROM CAJG_EJGREMESA EJGREM ");
		builder.append("WHERE EJGREM.IDREMESA = ");
		builder.append(idRemesa);
		builder.append(" AND EJGREM.IDINSTITUCION = ");
		builder.append(idInstitucion);
		builder.append(") ");
		return builder;
		
	}
	
	
	
}