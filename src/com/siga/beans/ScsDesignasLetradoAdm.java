
package com.siga.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.services.scs.EjgService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirEJGForm;
import com.siga.gratuita.form.DesignaForm;
import com.siga.gratuita.form.InformeJustificacionMasivaForm;

import es.satec.businessManager.BusinessManager;



/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_TIPODESIGNASCOLEGIADO
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 */
public class ScsDesignasLetradoAdm extends MasterBeanAdministrador {
	
	public final String resolucionDesignaFavorable ="FAVORABLE";
	public final String resolucionDesignaNoFavorable ="NO_FAVORABLE";
	public final String resolucionDesignaPteCAJG ="PTE_CAJG";
	public final String resolucionDesignaSinResolucion ="SIN_RESOLUCION";
	public final String resolucionDesignaSinEjg ="SIN_EJG";
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsDesignasLetradoAdm (UsrBean usuario) {
		super( ScsDesignasLetradoBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsDesignasLetradoBean.C_FECHAMODIFICACION,	ScsDesignasLetradoBean.C_USUMODIFICACION,
				ScsDesignasLetradoBean.C_IDINSTITUCION,		ScsDesignasLetradoBean.C_ANIO,			
				ScsDesignasLetradoBean.C_IDTURNO,			ScsDesignasLetradoBean.C_NUMERO,
				ScsDesignasLetradoBean.C_IDPERSONA,			ScsDesignasLetradoBean.C_FECHADESIGNA,
				ScsDesignasLetradoBean.C_FECHARENUNCIA,		ScsDesignasLetradoBean.C_FECHARENUNCIASOLICITA,
				ScsDesignasLetradoBean.C_LETRADODELTURNO,	ScsDesignasLetradoBean.C_MANUAL,
				ScsDesignasLetradoBean.C_IDTIPOMOTIVO,		ScsDesignasLetradoBean.C_OBSERVACIONES,
				ScsDesignasLetradoBean.C_IDINSTITUCIONORIGEN
		};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	public String[] getClavesBean() {
		String[] campos = {	ScsDesignasLetradoBean.C_IDINSTITUCION,		ScsDesignasLetradoBean.C_IDTURNO,
							ScsDesignasLetradoBean.C_ANIO,				ScsDesignasLetradoBean.C_NUMERO,
							ScsDesignasLetradoBean.C_IDPERSONA,			ScsDesignasLetradoBean.C_FECHADESIGNA,
							ScsDesignasLetradoBean.C_IDINSTITUCIONORIGEN};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsDesignasLetradoBean bean = null;
		try{
			bean = new ScsDesignasLetradoBean();
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsDesignasLetradoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsDesignasLetradoBean.C_USUMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsDesignasLetradoBean.C_IDINSTITUCION));
			bean.setIdInstitucionOrigen(UtilidadesHash.getInteger(hash,ScsDesignasLetradoBean.C_IDINSTITUCIONORIGEN));			
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsDesignasLetradoBean.C_ANIO));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsDesignasLetradoBean.C_NUMERO));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsDesignasLetradoBean.C_IDTURNO));
			bean.setIdPersona(UtilidadesHash.getLong(hash,ScsDesignasLetradoBean.C_IDPERSONA));
			bean.setFechaDesigna(UtilidadesHash.getString(hash,ScsDesignasLetradoBean.C_FECHADESIGNA));
			bean.setFechaRenuncia(UtilidadesHash.getString(hash,ScsDesignasLetradoBean.C_FECHARENUNCIA));
			bean.setFechaRenunciaSolicita(UtilidadesHash.getString(hash,ScsDesignasLetradoBean.C_FECHARENUNCIASOLICITA));
			bean.setLetradoDelTurno(UtilidadesHash.getString(hash,ScsDesignasLetradoBean.C_LETRADODELTURNO));
			bean.setManual(UtilidadesHash.getInteger(hash,ScsDesignasLetradoBean.C_MANUAL));
			bean.setIdTipoMotivo(UtilidadesHash.getInteger(hash,ScsDesignasLetradoBean.C_IDTIPOMOTIVO));
			bean.setObservaciones(UtilidadesHash.getString(hash,ScsDesignasLetradoBean.C_OBSERVACIONES));
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
			ScsDesignasLetradoBean b = (ScsDesignasLetradoBean) bean;
			UtilidadesHash.set(hash, ScsDesignasLetradoBean.C_USUMODIFICACION,String.valueOf(b.getUsuMod()));
			UtilidadesHash.set(hash, ScsDesignasLetradoBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(hash, ScsDesignasLetradoBean.C_IDINSTITUCION,String.valueOf(b.getIdInstitucion()));
			UtilidadesHash.set(hash, ScsDesignasLetradoBean.C_IDINSTITUCIONORIGEN,String.valueOf(b.getIdInstitucionOrigen()));			
			UtilidadesHash.set(hash, ScsDesignasLetradoBean.C_NUMERO,String.valueOf(b.getNumero()));
			UtilidadesHash.set(hash, ScsDesignasLetradoBean.C_ANIO,String.valueOf(b.getAnio()));
			UtilidadesHash.set(hash, ScsDesignasLetradoBean.C_IDTURNO,String.valueOf(b.getIdTurno()));
			UtilidadesHash.set(hash, ScsDesignasLetradoBean.C_IDPERSONA,String.valueOf(b.getIdPersona()));
			UtilidadesHash.set(hash, ScsDesignasLetradoBean.C_FECHADESIGNA,b.getFechaDesigna());
			UtilidadesHash.set(hash, ScsDesignasLetradoBean.C_FECHARENUNCIA,b.getFechaRenuncia());
			UtilidadesHash.set(hash, ScsDesignasLetradoBean.C_FECHARENUNCIASOLICITA,b.getFechaRenunciaSolicita());
			UtilidadesHash.set(hash, ScsDesignasLetradoBean.C_LETRADODELTURNO,b.getLetradoDelTurno());
			UtilidadesHash.set(hash, ScsDesignasLetradoBean.C_MANUAL,String.valueOf(b.getManual()));
			UtilidadesHash.set(hash, ScsDesignasLetradoBean.C_IDTIPOMOTIVO,b.getIdTipoMotivo());
		}catch (Exception e){
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
	public Vector selectGenerico(String select) throws ClsExceptions 
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
	
	
	private PaginadorBind getDesignasPendientesJustificacionPaginador(InformeJustificacionMasivaForm formulario,String longitudNumEjg,boolean isInforme,boolean isPermitidoEditarActFicha, boolean isEjisActivo)throws ClsExceptions,SIGAException {
		PaginadorBind paginador=null;
		try {
			Acumulador acumula = new Acumulador();
			List<DesignaForm> designasList = getDesignasJustificacion(formulario,acumula,longitudNumEjg,isInforme,isPermitidoEditarActFicha,isEjisActivo);
			
			if(designasList!=null && designasList.size()>0){
				Hashtable codigosHashtable = new Hashtable();
				String sqlDesignas = getQueryDesignasPendientesJustificacion(designasList,formulario,codigosHashtable,longitudNumEjg,isInforme);
				paginador = new PaginadorBind(sqlDesignas.toString(),codigosHashtable);
			 }else{
				 paginador = null;
			 } 
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta getDesignasPendientesJustificacionPaginador.");
		}
		return paginador;                        
	}
	
	private String getQueryDesignasPendientesJustificacion(List<DesignaForm> designasList,InformeJustificacionMasivaForm formulario,Hashtable codigosHashtable,String longitudNumEjg,boolean isInforme) throws ClsExceptions{
		StringBuffer sqlDesignas = new StringBuffer();
		
		sqlDesignas.append(getQueryBaseJustificacion( formulario, isInforme, codigosHashtable,longitudNumEjg));
		
		if(designasList!=null && designasList.size()>0){
            sqlDesignas.append(" WHERE (ALLDESIGNAS.IDINSTITUCION,ALLDESIGNAS.ANIO,ALLDESIGNAS.IDTURNO,ALLDESIGNAS.NUMERO) IN ( ");
			for(DesignaForm designa:designasList){
				
				sqlDesignas.append("(");
				sqlDesignas.append(designa.getIdInstitucion());
				sqlDesignas.append(",");
				sqlDesignas.append(designa.getAnio());
				sqlDesignas.append(",");
				sqlDesignas.append(designa.getIdTurno());
				sqlDesignas.append(",");
				sqlDesignas.append(designa.getNumero());
				sqlDesignas.append(")");
				sqlDesignas.append(",");
				
				
			}
			//quitamos la coma
			sqlDesignas.deleteCharAt(sqlDesignas.length()-1);
			sqlDesignas.append(")");
			if(!isInforme)
				sqlDesignas.append(" ORDER BY ALLDESIGNAS.FECHAENTRADA DESC,ALLDESIGNAS.IDINSTITUCION, ALLDESIGNAS.ANIO, ALLDESIGNAS.CODIGO DESC, ALLDESIGNAS.SUFIJO");
			
		}
		return sqlDesignas.toString();
		
	}
	
	
	
	
	
	private PaginadorBind getTodasDesignasJustificacionPaginador(InformeJustificacionMasivaForm formulario,String longitudNumEjg,boolean isInforme)throws ClsExceptions,SIGAException {
		PaginadorBind paginador=null;
		try {
			Hashtable codigosHashtable = new Hashtable();
			String queryDesignas = getQueryJustificacion(formulario,isInforme, codigosHashtable, longitudNumEjg);
			paginador = new PaginadorBind(queryDesignas,codigosHashtable);
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta getTodasDesignasJustificacionPaginador.");
		}
		return paginador;                        
	}
	
	public PaginadorBind getDesignasJustificacionPaginador(InformeJustificacionMasivaForm formulario,String longitudNumEjg,boolean isInforme,boolean isPermitidoEditarActFicha, boolean isEjisActivo) throws ClsExceptions, SIGAException{
		PaginadorBind paginador=null;
		boolean isMostrarJustificacionesPtes = formulario.getMostrarTodas()!=null && formulario.getMostrarTodas().equals("true");
		if(isMostrarJustificacionesPtes){
			paginador = getDesignasPendientesJustificacionPaginador(formulario,longitudNumEjg, isInforme,isPermitidoEditarActFicha,isEjisActivo);
		}else{
			paginador = getTodasDesignasJustificacionPaginador(formulario, longitudNumEjg, isInforme);
		}
		return paginador;                        
	}	
	
	
	private Vector getDesignasLetradoJustificacion (InformeJustificacionMasivaForm formulario,Acumulador	acumula,String longitudNumEjg,boolean isInforme,boolean isPermitidoEditarActFicha, boolean isEjisActivo)  throws ClsExceptions, SIGAException 
	{
		boolean isMostrarJustificacionesPtes = formulario.getMostrarTodas()!=null && formulario.getMostrarTodas().equals("true");
		
		if(isMostrarJustificacionesPtes){
			
			
			List<DesignaForm> designasList = getDesignasJustificacion(formulario,acumula,longitudNumEjg,isInforme,isPermitidoEditarActFicha,isEjisActivo);
			
			if(designasList!=null && designasList.size()>0){
				Hashtable codigosHashtable = new Hashtable();
				String sqlDesignas = getQueryDesignasPendientesJustificacion(designasList,formulario,codigosHashtable,longitudNumEjg,isInforme);
				return this.selectGenericoBind(sqlDesignas, codigosHashtable);
			}else 
				return new Vector();
		}else{
			Hashtable codigosHashtable = new Hashtable();
			String sqlDesignas = getQueryJustificacion(formulario,isInforme,codigosHashtable,longitudNumEjg);
			return this.selectGenericoBind(sqlDesignas, codigosHashtable);
			
		}
		
	
		
	}
	
	
	
	public Hashtable getPersonasSalidaInformeJustificacion(InformeJustificacionMasivaForm formulario,String longitudNumEjg,boolean isInforme,boolean isPermitidoEditarActFicha, boolean isEjisActivo) throws ClsExceptions  
	{	 
		
		Hashtable htPersona = new Hashtable();
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		TreeMap tmDesignas = null;
		
		try {
			ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: INICIO Consultas Justificacion",10);
			Acumulador acumula = new Acumulador();
			Vector vDesigna = getDesignasLetradoJustificacion(formulario,acumula,longitudNumEjg,isInforme,isPermitidoEditarActFicha,isEjisActivo);
			
			for (int j = 0; j < vDesigna.size(); j++) {
				
				Hashtable registro = (Hashtable) vDesigna.get(j);
				String idPersona = (String)registro.get("IDPERSONA");
				Integer idInstitucion = Integer.parseInt((String)registro.get("IDINSTITUCION"));
				String numeroDesigna = (String)registro.get("NUMERO");
				String codigoDesigna = (String)registro.get("CODIGO");
				String anioDesigna = (String)registro.get("ANIO");
				String idTurno  = (String)registro.get("IDTURNO");
				//String idProcedimiento  = (String)registro.get("IDPROCEDIMIENTO");
				String fechaOrden  = (String)registro.get("FECHAORDEN");
				String keyTreeMap = fechaOrden+anioDesigna+codigoDesigna+idTurno+numeroDesigna;
				if(!htPersona.containsKey(idPersona)){
					if(isInforme)
						helperInformes.completarHashSalida(registro, getLetradoSalida(idPersona, idInstitucion.toString()));
					
					tmDesignas = new TreeMap();
				}else{
					tmDesignas = (TreeMap) htPersona.get(idPersona);
					if(isInforme){
						Iterator iteDesignasPersona = tmDesignas.keySet().iterator();
						String keyPrimerRegistroPersona = (String)iteDesignasPersona.next();
						Hashtable primerRegistroPersona =   (Hashtable)tmDesignas.get(keyPrimerRegistroPersona);
						registro.put(CenPersonaBean.C_NOMBRE,(String)primerRegistroPersona.get(CenPersonaBean.C_NOMBRE));
						registro.put(CenColegiadoBean.C_NCOLEGIADO,(String)primerRegistroPersona.get(CenColegiadoBean.C_NCOLEGIADO));
						registro.put("DOMICILIO_LETRADO",(String)primerRegistroPersona.get("DOMICILIO_LETRADO"));
						registro.put("CP_LETRADO",(String)primerRegistroPersona.get("CP_LETRADO"));
						registro.put("POBLACION_LETRADO",(String)primerRegistroPersona.get("POBLACION_LETRADO"));
						registro.put("PROVINCIA_LETRADO",(String)primerRegistroPersona.get("PROVINCIA_LETRADO"));
					}
				}
				List<DesignaForm> designaList = getDesignaList(formulario, registro, acumula, isInforme,isPermitidoEditarActFicha,isEjisActivo);
				registro.put("designaList", designaList);
				tmDesignas.put(keyTreeMap, registro);
				htPersona.put(idPersona,tmDesignas);
			}
			 ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: FIN Consulta Justificacion",10);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getPersonasSalidaJustificacion");
		}
		return htPersona;
	}
	
	/**
	 * Creamos un acumulado de juzgados para evitar conexiones innecesarias a base de datos
	 * 
	 * @param idInstitucion
	 * @param idJuzgado
	 * @param acumuladorJuzgados
	 * @param usr
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector getJuzgado(String idInstitucion, String idJuzgado, Hashtable acumuladorJuzgados,HelperInformesAdm helperInformes)throws ClsExceptions{
		Vector v = null;
		
		String keyAcumulador = idInstitucion+"||"+idJuzgado;
		if(acumuladorJuzgados.containsKey(keyAcumulador))
			v = (Vector) acumuladorJuzgados.get(keyAcumulador);
		else{
			
			v = helperInformes.getJuzgadoSalida(idInstitucion,idJuzgado,"");
			acumuladorJuzgados.put(keyAcumulador,v);
		}
			
		
			return v;
		 
			
		
	}
	

	public Vector getProcedimiento(String idInstitucion, String idProcedimiento, 
			Hashtable acumuladorProcedimientos,HelperInformesAdm helperInformes)throws ClsExceptions{
		Vector v = null;
		
		String keyAcumulador = idInstitucion+"||"+idProcedimiento;
		if(acumuladorProcedimientos.containsKey(keyAcumulador))
			v = (Vector) acumuladorProcedimientos.get(keyAcumulador);
		else{
			
			v = (Vector) helperInformes.getProcedimientoSalida(idInstitucion.toString(), 
					idProcedimiento,"");
			acumuladorProcedimientos.put(keyAcumulador,v);
		}
			
		
			return v;
		 
			
		
	}
	public Vector geTurno(String idInstitucion, String idTurno, 
			Hashtable acumuladorTurnos,HelperInformesAdm helperInformes)throws ClsExceptions{
		Vector v = null;
		
		String keyAcumulador = idInstitucion+"||"+idTurno;
		if(acumuladorTurnos.containsKey(keyAcumulador))
			v = (Vector) acumuladorTurnos.get(keyAcumulador);
		else{
			
			v =  helperInformes.getTurnoSalida(idInstitucion.toString(), 
					idTurno);
			acumuladorTurnos.put(keyAcumulador,v);
		}
			
		
			return v;
		 
			
		
	}
	
	public Vector getDireccionLetradoSalida(String idPersona,String idInstitucion) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idPersona);
			
			String sql = "SELECT DIR.Domicilio DOMICILIO_LETRADO," +
					" dir.codigopostal CP_LETRADO," +
					" nvl(dir.poblacionextranjera, pob.nombre) POBLACION_LETRADO" +
					" from CEN_DIRECCIONES DIR, CEN_DIRECCION_TIPODIRECCION TIP,CEN_POBLACIONES pob" +
					" where dir.idinstitucion = tip.idinstitucion " +
					" and dir.idpersona = tip.idpersona  " +
					" and dir.iddireccion = tip.iddireccion " +
					" and dir.idpoblacion = pob.IDPOBLACION(+)" +
					" and tip.idtipodireccion = 2 " +
					" and dir.fechabaja is null "+
					" and dir.idinstitucion = :1 "+
					" and dir.idpersona = :2 "+
					" and rownum = 1 ";

			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getDireccionLetrado");
		}
	}
	public Vector getLetradoSalida(String idPersona,String idInstitucion) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idPersona);
			
			String sql = "SELECT  "+
				" p.apellidos1 || ' ' || p.apellidos2 || ', ' || p.nombre as NOMBRE, "+ 
				" f_siga_calculoncolegiado(c.idinstitucion, c.idpersona) as NCOLEGIADO, "+
				" DIR.Domicilio DOMICILIO_LETRADO, "+
				" dir.codigopostal CP_LETRADO, "+
				" nvl(dir.poblacionextranjera, pob.nombre) POBLACION_LETRADO, "+
				" pROV.nombre PROVINCIA_LETRADO "+
				" from cen_cliente c,cen_persona p, "+
  
				" CEN_DIRECCIONES             DIR, "+
				" CEN_DIRECCION_TIPODIRECCION TIP, "+
				" CEN_POBLACIONES             pob, "+
				" CEN_PROVINCIAS              prov"+
				" where  "+
				" p.idpersona = c.idpersona "+
				" and dir.idpersona = c.idpersona "+
				" and dir.idinstitucion =  c.idinstitucion "+
				" and  dir.idinstitucion = tip.idinstitucion "+
				" and dir.idpersona = tip.idpersona "+
				" and dir.iddireccion = tip.iddireccion "+
				" and dir.idpoblacion = pob.IDPOBLACION(+) "+
				" and dir.idprovincia = prov.IDPROVINCIA(+) "+
				" and tip.idtipodireccion = 2 "+
  
			" and dir.fechabaja is null "+
			" and c.idinstitucion = :1 "+
			" and c.idpersona = :2 "+
			" and rownum = 1 ";

			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getDireccionLetrado");
		}
	}

	
	/**
	 * Recupera el colegiado designado en una fecha
	 * @param sdb
	 * @param fecha
	 * @return
	 * @throws ClsExceptions 
	 */
	public Long obtenerColegiadoDesignadoEnFecha(ScsDesignaBean sdb, String fecha) throws ClsExceptions {
		try {
			String sql = "select * " +
						 "  from scs_designasletrado " +
						 " where idinstitucion = "+sdb.getIdInstitucion()+" " +
						 "   and idturno = "+sdb.getIdTurno()+" " +
						 "   and anio = "+sdb.getAnio()+" " +
						 "   and numero = "+sdb.getNumero()+" " +
						 "   and fechadesigna <= to_date('"+fecha+"', 'DD/MM/YYYY') " +
						 "   and (fecharenuncia > to_date('"+fecha+"', 'DD/MM/YYYY') " +
			 			 "    or fecharenuncia is null)";

			Vector vector = selectGenerico(sql);
		    if ((vector != null) && (vector.size() == 1)) {
		    	return new Long((String)((Hashtable)vector.get(0)).get("IDPERSONA"));
		    }

			return null;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre obtenerColegiadoDesignadoEnFecha()");
		}

	}
	
	public List<DesignaForm> getDesignasJustificacion(InformeJustificacionMasivaForm formulario,Acumulador acumula,String longitudNumEjg,boolean isInforme,boolean isPermitidoEditarActFicha, boolean isEjisActivo) throws ClsExceptions  
	{	 
		boolean isMostrarJustificacionesPtes = formulario.getMostrarTodas()!=null && formulario.getMostrarTodas().equals("true");
		
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		Hashtable htAcumuladorJuzgados = acumula.getAcumuladorJuzgadosHashtable();
		Hashtable htAcumuladorProcedimientos = acumula.getAcumuladorProcedimientosHashtable();
		Hashtable htAcumuladorTurnos = acumula.getAcumuladorTurnosHashtable();
		List<Hashtable> designaList = null;
		List<DesignaForm> designaFormList = null;
		ScsActuacionDesignaAdm admActuacionDesignaAdm = new ScsActuacionDesignaAdm(usrbean);
		String idPersona = formulario.getIdPersona();
		
		
		
		try {
			//Sacamos todas las designa con los filtros.  Mas adelante miraremos que, dependiendo de las actuaciones que tenga y de la peticion, se necesitan sacar las que esten pendientes de justificar o todas.
			designaList = getDesignas(formulario,longitudNumEjg,isInforme); 
			DesignaForm designaForm = null;
			designaFormList = new ArrayList<DesignaForm>();
			Iterator iteDesignas = designaList.iterator();
			
			while (iteDesignas.hasNext()) {
				Hashtable designaHashtable =  (Hashtable) iteDesignas.next();
				designaForm = new DesignaForm();
				String tipoResolucionDesigna  =UtilidadesHash.getString(designaHashtable, "TIPO_RESOLUCION_DESIGNA") ;
				designaForm.setTipoResolucionDesigna(tipoResolucionDesigna);
				
				idPersona = (String)designaHashtable.get("IDPERSONA");
				String idProcedimiento  = (String)designaHashtable.get("IDPROCEDIMIENTO");
				designaForm.setIdPersona(idPersona);
				designaForm.setNumero((String)designaHashtable.get("NUMERO"));
				designaForm.setAnio((String)designaHashtable.get("ANIO"));
				designaForm.setIdInstitucion(formulario.getIdInstitucion());
				designaForm.setIdTurno((String)designaHashtable.get("IDTURNO"));

				designaForm.setCodigoDesigna((String)designaHashtable.get("CODIGODESIGNA"));
//				List ejgList = getExpedientes((String)registro.get("EXPEDIENTES"));
//				designaForm.setExpedientes(ejgList);
//				designaForm.setEjgs((String)registro.get("EXPEDIENTES"));
				designaForm.setFecha((String)designaHashtable.get("FECHADESIGNA"));
				designaForm.setAsunto((String)designaHashtable.get("ASUNTO"));
				designaForm.setClientes((String)designaHashtable.get("CLIENTE"));
				

				String estado = (String)designaHashtable.get("ESTADO");
				designaForm.setEstado(estado);
				designaForm.setBaja(estado!=null&&(estado.equals("F")||estado.equals("A"))?"1":"0");
				
				
				Vector vTurno = geTurno(designaForm.getIdInstitucion().toString(), 
						designaForm.getIdTurno(),htAcumuladorTurnos,helperInformes);
				helperInformes.completarHashSalida(designaHashtable,vTurno);
				
				//El turno es quiene tiene si se validan las justificaciones
				String validarJustificaciones = (String)designaHashtable.get("VALIDARJUSTIFICACIONES");
				designaForm.setActuacionValidarJustificaciones(validarJustificaciones!=null?validarJustificaciones:"S");
				designaForm.setActuacionRestriccionesActiva((String)designaHashtable.get("ACTIVARRETRICCIONACREDIT"));
				designaForm.setActuacionPermitidaLetrado((String)designaHashtable.get("LETRADOACTUACIONES"));
				String cambioLetrado = (String)designaHashtable.get("CAMBIOLETRADO");
				designaForm.setCambioLetrado(cambioLetrado!=null&&Integer.parseInt(cambioLetrado)>1?"S":"N");
				
				designaForm.setArticulo27(designaHashtable.get("ART27")!=null && ((String)designaHashtable.get("ART27")).equals("1")?"S":"N");
				
				
				designaForm.setIdProcedimiento(idProcedimiento);
				
				if(idProcedimiento!=null && !idProcedimiento.equalsIgnoreCase("")){
					Vector vProcedimiento = getProcedimiento(formulario.getIdInstitucion(), 
							idProcedimiento,htAcumuladorProcedimientos,helperInformes);
					helperInformes.completarHashSalida(designaHashtable,vProcedimiento);
				}else{
					designaHashtable.put("PROCEDIMIENTO","");
					designaHashtable.put("CATEGORIA","");
					designaHashtable.put("IDJURISDICCION","");
					
				}
				designaForm.setDescripcionProcedimiento((String)designaHashtable.get("PROCEDIMIENTO"));
				designaForm.setCategoria((String)designaHashtable.get("CATEGORIA"));
				designaForm.setIdJurisdiccion((String)designaHashtable.get("IDJURISDICCION"));
				
				String idJuzgado = (String)designaHashtable.get(ScsDesignaBean.C_IDJUZGADO);
				String idInstitucionJuzgado  = (String)designaHashtable.get("IDINSTITUCION_JUZG");
				if(idJuzgado!=null && !idJuzgado.equalsIgnoreCase("")){
					designaForm.setIdJuzgado(idJuzgado);
					Vector vJuzgado = getJuzgado(idInstitucionJuzgado.toString(),idJuzgado,htAcumuladorJuzgados, helperInformes);
					helperInformes.completarHashSalida(designaHashtable,vJuzgado);
					String descJuzgado = (String)designaHashtable.get("JUZGADO"); 
					designaHashtable.put("DESC_JUZGADO",descJuzgado);
					if(isInforme)
						designaHashtable.put("IDJUZGADO",descJuzgado);
				}else{
					designaHashtable.put("DESC_JUZGADO","");
					designaHashtable.put("IDJUZGADO","");
					
				}
				designaForm.setJuzgado((String)designaHashtable.get("DESC_JUZGADO"));
				
				//seteamos las actuaciones de las designas
				String numProcedimiento = (String)designaHashtable.get(ScsDesignaBean.C_NUMPROCEDIMIENTO);
				designaForm.setNumProcedimiento(numProcedimiento!=null?numProcedimiento:"");
				String anioProcedimiento = (String)designaHashtable.get(ScsDesignaBean.C_ANIOPROCEDIMIENTO);
				designaForm.setAnioProcedimiento(anioProcedimiento!=null?anioProcedimiento:"");
				String nig = (String)designaHashtable.get(ScsDesignaBean.C_NIG);
				designaForm.setNig(nig!=null?nig:"");
				
				//si no estan activos los parametros se pone siempre permitido justificar
				
				if((!formulario.getFichaColegial()) || (tipoResolucionDesigna.equals(this.resolucionDesignaFavorable)||
						(tipoResolucionDesigna.equals(this.resolucionDesignaPteCAJG)&&formulario.getIncluirEjgPteCAJG()!=null&&formulario.getIncluirEjgPteCAJG().equals("2"))
						||
						(tipoResolucionDesigna.equals(this.resolucionDesignaNoFavorable)&&formulario.getIncluirEjgNoFavorable()!=null&&formulario.getIncluirEjgNoFavorable().equals("2"))
						||
						(tipoResolucionDesigna.equals(this.resolucionDesignaSinEjg)&&formulario.getIncluirSinEJG()!=null&&formulario.getIncluirSinEJG().equals("2"))
						||
						(tipoResolucionDesigna.equals(this.resolucionDesignaSinResolucion)&&formulario.getIncluirEjgSinResolucion()!=null&&formulario.getIncluirEjgSinResolucion().equals("2"))
				)){
					
					designaForm.setPermitidoJustificar(true);
					
					if(isEjisActivo){
						designaForm.setAcreditacionCompleta(designaHashtable.get(ScsDesignaBean.C_NIG)!=null && !((String)designaHashtable.get(ScsDesignaBean.C_NIG)).equals("") && designaHashtable.get(ScsDesignaBean.C_NUMPROCEDIMIENTO)!=null && !((String)designaHashtable.get(ScsDesignaBean.C_NUMPROCEDIMIENTO)).equals("")&& designaHashtable.get(ScsDesignaBean.C_ANIOPROCEDIMIENTO)!=null && !((String)designaHashtable.get(ScsDesignaBean.C_ANIOPROCEDIMIENTO)).equals(""));
						
					}else{
						designaForm.setAcreditacionCompleta(designaHashtable.get(ScsDesignaBean.C_NIG)!=null && !((String)designaHashtable.get(ScsDesignaBean.C_NIG)).equals("") && designaHashtable.get(ScsDesignaBean.C_NUMPROCEDIMIENTO)!=null && !((String)designaHashtable.get(ScsDesignaBean.C_NUMPROCEDIMIENTO)).equals(""));
					}
					
					admActuacionDesignaAdm.setActuacionesDesignas(designaForm,isMostrarJustificacionesPtes,false,isPermitidoEditarActFicha);
					designaForm.setRowSpan();
					
					 if(designaForm.getIdJuzgado()==null || designaForm.getIdJuzgado().equals("")){
						 designaForm.setPermitidoJustificar(false);
						designaFormList.add(designaForm);
						if(designaForm.getActuaciones()!=null && designaForm.getActuaciones().size()>0&& designaForm.getCambioLetrado()!=null && designaForm.getCambioLetrado().equalsIgnoreCase("N"))
							designaForm.addRowSpan();
//						if(designaForm.getActuacionPermitidaLetrado().equals(AppConstants.DB_TRUE) && designaForm.getCambioLetrado()!=null && designaForm.getCambioLetrado().equalsIgnoreCase("N") && designaForm.getEstado()!=null && designaForm.getEstado().equalsIgnoreCase("V"))
//							designaForm.addRowSpan();
					}else if(designaForm.getIdProcedimiento()==null || designaForm.getIdProcedimiento().equals("")){
						designaForm.setPermitidoJustificar(false);
						designaFormList.add(designaForm);
						if(designaForm.getActuaciones()!=null && designaForm.getActuaciones().size()>0 && designaForm.getCambioLetrado()!=null && designaForm.getCambioLetrado().equalsIgnoreCase("N"))
							designaForm.addRowSpan();
//						if(designaForm.getActuacionPermitidaLetrado().equals(AppConstants.DB_TRUE) && designaForm.getCambioLetrado()!=null && designaForm.getCambioLetrado().equalsIgnoreCase("N") && designaForm.getEstado()!=null && designaForm.getEstado().equalsIgnoreCase("V"))
//							designaForm.addRowSpan();
					}
					else if(designaForm.getActuaciones()!=null && designaForm.getActuaciones().size()>0) {
						designaFormList.add(designaForm);
						if(designaForm.getEstado()!=null && designaForm.getEstado().equalsIgnoreCase("V") && designaForm.getCambioLetrado()!=null && designaForm.getCambioLetrado().equalsIgnoreCase("N")&& ((formulario.getFichaColegial() && designaForm.getActuacionPermitidaLetrado().equals(AppConstants.DB_TRUE) ) || !formulario.getFichaColegial()))
							designaForm.addRowSpan();
					}else if(designaForm.getAcreditaciones()!=null && designaForm.getAcreditaciones().size()>0){
						designaFormList.add(designaForm);
						
					}
				}else if((!formulario.getFichaColegial()) || (tipoResolucionDesigna.equals(this.resolucionDesignaFavorable)||
						(tipoResolucionDesigna.equals(this.resolucionDesignaPteCAJG)&&formulario.getIncluirEjgPteCAJG()!=null&&formulario.getIncluirEjgPteCAJG().equals("1"))
						||
						(tipoResolucionDesigna.equals(this.resolucionDesignaNoFavorable)&&formulario.getIncluirEjgNoFavorable()!=null&&formulario.getIncluirEjgNoFavorable().equals("1"))
						||
						(tipoResolucionDesigna.equals(this.resolucionDesignaSinEjg)&&formulario.getIncluirSinEJG()!=null&&formulario.getIncluirSinEJG().equals("1"))
						||
						(tipoResolucionDesigna.equals(this.resolucionDesignaSinResolucion)&&formulario.getIncluirEjgSinResolucion()!=null&&formulario.getIncluirEjgSinResolucion().equals("1"))
				)){
					
					designaForm.setPermitidoJustificar(false);
					admActuacionDesignaAdm.setActuacionesDesignas(designaForm,isMostrarJustificacionesPtes,true,isPermitidoEditarActFicha);
					designaForm.setRowSpan();
					if(designaForm.getActuaciones()!=null && !designaForm.getActuaciones().isEmpty() && designaForm.getCambioLetrado()!=null && designaForm.getCambioLetrado().equals("N"))
						designaForm.addRowSpan();
					if(!isMostrarJustificacionesPtes){
						designaFormList.add(designaForm);
					}else if(designaForm.getIdJuzgado()==null || designaForm.getIdJuzgado().equals("")){
						designaFormList.add(designaForm);
					}else if(designaForm.getIdProcedimiento()==null || designaForm.getIdProcedimiento().equals("")){
						designaFormList.add(designaForm);
					}
					else if((designaForm.getActuaciones()!=null && !designaForm.getActuaciones().isEmpty()) ||
							(designaForm.getAcreditaciones()!=null && designaForm.getAcreditaciones().size()>0)){
						designaFormList.add(designaForm);
					}
				}else{
					designaForm.setPermitidoJustificar(false);
					designaForm.setRowSpan();
					designaFormList.add(designaForm);
					
				}
				
				
				
	
			
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getDesignasJustificacion");
		}
		return designaFormList;
	}
	
	
	public List<DesignaForm> getDesignaList(InformeJustificacionMasivaForm formulario,Hashtable designaHashtable,Acumulador acumula,boolean isInforme,boolean isPermitidoEditarActFicha, boolean isEjisActivo) throws ClsExceptions  
	{	 
		
		Hashtable htPersona = new Hashtable();
		
		boolean isMostrarJustificacionesPtes = formulario.getMostrarTodas()!=null && formulario.getMostrarTodas().equals("true");

		
		//Vector vSalida = null;
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		if(acumula==null)
			acumula= new Acumulador();
		Hashtable htAcumuladorJuzgados = acumula.getAcumuladorJuzgadosHashtable();
		Hashtable htAcumuladorProcedimientos = acumula.getAcumuladorProcedimientosHashtable();
		Hashtable htAcumuladorTurnos = acumula.getAcumuladorTurnosHashtable();
		//Hashtable htAcumuladorPersona = new Hashtable();
		TreeMap tmDesignas = null;
//		List<Hashtable> designaList = null;
		List<DesignaForm> designaFormList = null;
		ScsActuacionDesignaAdm admActuacionDesignaAdm = new ScsActuacionDesignaAdm(usrbean);
		String idPersona = formulario.getIdPersona();
		try {
			//vSalida = new Vector();	
//			ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: INICIO Consulta Justificacion",10);
//			designaList = getDesignas(formulario,isInforme); 
//			ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: FIN Consulta Justificacion",10);
			DesignaForm designaForm = null;
			designaFormList = new ArrayList<DesignaForm>();
//			Iterator iteDesignas = designaList.iterator();
			
			
				
			
			
			//for (int j = 0; j < designaList.size(); j++) {
				designaForm = new DesignaForm();
				
				String tipoResolucionDesigna  =UtilidadesHash.getString(designaHashtable, "TIPO_RESOLUCION_DESIGNA") ;
				designaForm.setTipoResolucionDesigna(tipoResolucionDesigna);
				//Hashtable registro = designaList.get(j);
				idPersona = (String)designaHashtable.get("IDPERSONA");
				String idProcedimiento  = (String)designaHashtable.get("IDPROCEDIMIENTO");
				designaForm.setIdPersona(idPersona);
				designaForm.setNumero((String)designaHashtable.get("NUMERO"));
				designaForm.setAnio((String)designaHashtable.get("ANIO"));
				designaForm.setIdInstitucion(formulario.getIdInstitucion());
				designaForm.setIdTurno((String)designaHashtable.get("IDTURNO"));

				designaForm.setCodigoDesigna((String)designaHashtable.get("CODIGODESIGNA"));
				List ejgList = getExpedientes((String)designaHashtable.get("EXPEDIENTES"));
				designaForm.setExpedientes(ejgList);
//				designaForm.setEjgs((String)designaHashtable.get("EXPEDIENTES"));
				designaForm.setFecha((String)designaHashtable.get("FECHADESIGNA"));
				designaForm.setAsunto((String)designaHashtable.get("ASUNTO"));
				designaForm.setClientes((String)designaHashtable.get("CLIENTE"));

				//Si el estado es finalizado ponemos baja = 1 si no baja =0
				String estado = (String)designaHashtable.get("ESTADO");
				designaForm.setEstado(estado);
				designaForm.setBaja(estado!=null&&(estado.equals("F")||estado.equals("A"))?"1":"0");
				
				
				Vector vTurno = geTurno(designaForm.getIdInstitucion().toString(), 
						designaForm.getIdTurno(),htAcumuladorTurnos,helperInformes);
				helperInformes.completarHashSalida(designaHashtable,vTurno);
				
				//El turno es quiene tiene si se validan las justificaciones
				String validarJustificaciones = (String)designaHashtable.get("VALIDARJUSTIFICACIONES");
				String actuacionPermitidaLetrado = (String)designaHashtable.get("LETRADOACTUACIONES");
				designaForm.setActuacionValidarJustificaciones(validarJustificaciones!=null?validarJustificaciones:"S");
				designaForm.setActuacionRestriccionesActiva((String)designaHashtable.get("ACTIVARRETRICCIONACREDIT"));
				designaForm.setActuacionPermitidaLetrado(actuacionPermitidaLetrado!=null?actuacionPermitidaLetrado:"0");
				designaForm.setArticulo27(designaHashtable.get("ART27")!=null && ((String)designaHashtable.get("ART27")).equals("1")?"S":"N");
				String cambioLetrado = (String)designaHashtable.get("CAMBIOLETRADO");
				
				designaForm.setCambioLetrado(cambioLetrado!=null&&Integer.parseInt(cambioLetrado)>1?"S":"N");
				designaForm.setIdProcedimiento(idProcedimiento);
				
				if(idProcedimiento!=null && !idProcedimiento.equalsIgnoreCase("")){
					Vector vProcedimiento = getProcedimiento(formulario.getIdInstitucion(), 
							idProcedimiento,htAcumuladorProcedimientos,helperInformes);
					helperInformes.completarHashSalida(designaHashtable,vProcedimiento);
				}else{
					designaHashtable.put("PERMITIRANIADIRLETRADO","");
					designaHashtable.put("PROCEDIMIENTO","");
					designaHashtable.put("CATEGORIA","");
					designaHashtable.put("IDJURISDICCION","");
					
				}
				designaForm.setDescripcionProcedimiento((String)designaHashtable.get("PROCEDIMIENTO"));
				designaForm.setCategoria((String)designaHashtable.get("CATEGORIA"));
				designaForm.setIdJurisdiccion((String)designaHashtable.get("IDJURISDICCION"));
				
				String idJuzgado = (String)designaHashtable.get(ScsDesignaBean.C_IDJUZGADO);
				String idInstitucionJuzgado  = (String)designaHashtable.get("IDINSTITUCION_JUZG");
				if(idJuzgado!=null && !idJuzgado.equalsIgnoreCase("")){
					designaForm.setIdJuzgado(idJuzgado);
					Vector vJuzgado = getJuzgado(idInstitucionJuzgado.toString(),idJuzgado,htAcumuladorJuzgados, helperInformes);
					helperInformes.completarHashSalida(designaHashtable,vJuzgado);
					String descJuzgado = (String)designaHashtable.get("JUZGADO"); 
					designaHashtable.put("DESC_JUZGADO",descJuzgado);
					if(isInforme)
						designaHashtable.put("IDJUZGADO",descJuzgado);
				}else{
					designaHashtable.put("DESC_JUZGADO","");
					designaHashtable.put("IDJUZGADO","");
					
				}
				designaForm.setJuzgado((String)designaHashtable.get("DESC_JUZGADO"));
				
				//seteamos las actuaciones de las designas
				designaForm.setMultiplesComplementos((String)designaHashtable.get("COMPLEMENTO"));

				String numProcedimiento = (String)designaHashtable.get(ScsDesignaBean.C_NUMPROCEDIMIENTO);
				designaForm.setNumProcedimiento(numProcedimiento!=null?numProcedimiento:"");
				String anioProcedimiento = (String)designaHashtable.get(ScsDesignaBean.C_ANIOPROCEDIMIENTO);
				designaForm.setAnioProcedimiento(anioProcedimiento!=null?anioProcedimiento:"");
				String nig = (String)designaHashtable.get(ScsDesignaBean.C_NIG);
				designaForm.setNig(nig!=null?nig:"");
				
				
				
				

				
				if((!formulario.getFichaColegial()) ||( tipoResolucionDesigna.equals(this.resolucionDesignaFavorable)||
						(tipoResolucionDesigna.equals(this.resolucionDesignaPteCAJG)&&formulario.getIncluirEjgPteCAJG()!=null&&formulario.getIncluirEjgPteCAJG().equals("2"))
						||
						(tipoResolucionDesigna.equals(this.resolucionDesignaNoFavorable)&&formulario.getIncluirEjgNoFavorable()!=null&&formulario.getIncluirEjgNoFavorable().equals("2"))
						||
						(tipoResolucionDesigna.equals(this.resolucionDesignaSinEjg)&&formulario.getIncluirSinEJG()!=null&&formulario.getIncluirSinEJG().equals("2"))
						||
						(tipoResolucionDesigna.equals(this.resolucionDesignaSinResolucion)&&formulario.getIncluirEjgSinResolucion()!=null&&formulario.getIncluirEjgSinResolucion().equals("2"))
				)){
					
					designaForm.setPermitidoJustificar(true);
					if(isEjisActivo){
						designaForm.setAcreditacionCompleta(designaHashtable.get(ScsDesignaBean.C_NIG)!=null && !((String)designaHashtable.get(ScsDesignaBean.C_NIG)).equals("") && designaHashtable.get(ScsDesignaBean.C_NUMPROCEDIMIENTO)!=null && !((String)designaHashtable.get(ScsDesignaBean.C_NUMPROCEDIMIENTO)).equals("")&& designaHashtable.get(ScsDesignaBean.C_ANIOPROCEDIMIENTO)!=null && !((String)designaHashtable.get(ScsDesignaBean.C_ANIOPROCEDIMIENTO)).equals(""));
						
					}else{
						designaForm.setAcreditacionCompleta(designaHashtable.get(ScsDesignaBean.C_NIG)!=null && !((String)designaHashtable.get(ScsDesignaBean.C_NIG)).equals("") && designaHashtable.get(ScsDesignaBean.C_NUMPROCEDIMIENTO)!=null && !((String)designaHashtable.get(ScsDesignaBean.C_NUMPROCEDIMIENTO)).equals(""));
					}
					
					admActuacionDesignaAdm.setActuacionesDesignas(designaForm,isMostrarJustificacionesPtes,false,isPermitidoEditarActFicha);
					designaForm.setRowSpan();
					
					if(designaForm.getIdJuzgado()==null || designaForm.getIdJuzgado().equals("")){
						designaForm.setPermitidoJustificar(false);
						designaFormList.add(designaForm);
						if(designaForm.getActuaciones()!=null && designaForm.getActuaciones().size()>0&& designaForm.getCambioLetrado()!=null && designaForm.getCambioLetrado().equalsIgnoreCase("N"))
							designaForm.addRowSpan();
//						if(designaForm.getActuacionPermitidaLetrado().equals(AppConstants.DB_TRUE) && designaForm.getCambioLetrado()!=null && designaForm.getCambioLetrado().equalsIgnoreCase("N") && designaForm.getEstado()!=null && designaForm.getEstado().equalsIgnoreCase("V"))
//							designaForm.addRowSpan();
					}else if(designaForm.getIdProcedimiento()==null || designaForm.getIdProcedimiento().equals("")){
						designaForm.setPermitidoJustificar(false);
						designaFormList.add(designaForm);
						if(designaForm.getActuaciones()!=null && designaForm.getActuaciones().size()>0&& designaForm.getCambioLetrado()!=null && designaForm.getCambioLetrado().equalsIgnoreCase("N"))
							designaForm.addRowSpan();
//						if(designaForm.getActuacionPermitidaLetrado().equals(AppConstants.DB_TRUE) && designaForm.getCambioLetrado()!=null && designaForm.getCambioLetrado().equalsIgnoreCase("N") && designaForm.getEstado()!=null && designaForm.getEstado().equalsIgnoreCase("V"))
//							designaForm.addRowSpan();
					}
					else if(designaForm.getActuaciones()!=null && designaForm.getActuaciones().size()>0) {
						designaFormList.add(designaForm);
						if(designaForm.getEstado()!=null && designaForm.getEstado().equalsIgnoreCase("V") && designaForm.getCambioLetrado()!=null && designaForm.getCambioLetrado().equalsIgnoreCase("N")&& ((formulario.getFichaColegial() && designaForm.getActuacionPermitidaLetrado().equals(AppConstants.DB_TRUE) ) || !formulario.getFichaColegial()))
							designaForm.addRowSpan();
					}else if(designaForm.getAcreditaciones()!=null && designaForm.getAcreditaciones().size()>0){
						designaFormList.add(designaForm);
						
					}
					
						
				}else if((!formulario.getFichaColegial()) ||( tipoResolucionDesigna.equals(this.resolucionDesignaFavorable)||
						(tipoResolucionDesigna.equals(this.resolucionDesignaPteCAJG)&&formulario.getIncluirEjgPteCAJG()!=null&&formulario.getIncluirEjgPteCAJG().equals("1"))
						||
						(tipoResolucionDesigna.equals(this.resolucionDesignaNoFavorable)&&formulario.getIncluirEjgNoFavorable()!=null&&formulario.getIncluirEjgNoFavorable().equals("1"))
						||
						(tipoResolucionDesigna.equals(this.resolucionDesignaSinEjg)&&formulario.getIncluirSinEJG()!=null&&formulario.getIncluirSinEJG().equals("1"))
						||
						(tipoResolucionDesigna.equals(this.resolucionDesignaSinResolucion)&&formulario.getIncluirEjgSinResolucion()!=null&&formulario.getIncluirEjgSinResolucion().equals("1"))
				)){
					
					designaForm.setPermitidoJustificar(false);
					admActuacionDesignaAdm.setActuacionesDesignas(designaForm,isMostrarJustificacionesPtes,true,isPermitidoEditarActFicha);
					designaForm.setRowSpan();
					if(designaForm.getActuaciones()!=null && !designaForm.getActuaciones().isEmpty()&& designaForm.getCambioLetrado()!=null && designaForm.getCambioLetrado().equals("N"))
						designaForm.addRowSpan();
					
					if(!isMostrarJustificacionesPtes){
						designaFormList.add(designaForm);
					}else if(designaForm.getIdJuzgado()==null || designaForm.getIdJuzgado().equals("")){
						designaFormList.add(designaForm);
					}else if(designaForm.getIdProcedimiento()==null || designaForm.getIdProcedimiento().equals("")){
						designaFormList.add(designaForm);
					}
					else if((designaForm.getActuaciones()!=null && designaForm.getActuaciones().size()>0) ||
							(designaForm.getAcreditaciones()!=null && designaForm.getAcreditaciones().size()>0)){
						designaFormList.add(designaForm);
					}
				}else{
					designaForm.setPermitidoJustificar(false);
					designaForm.setRowSpan();
					designaFormList.add(designaForm);
				}
				
				
	
				
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getDesignasJustificacion");
		}
		return designaFormList;
	}
	
	
	private List<Hashtable> getDesignas(InformeJustificacionMasivaForm formulario,String longitudNumEjg,boolean isInforme)  throws ClsExceptions, SIGAException 
	{
	    Hashtable codigos = new Hashtable();
		String sql = this.getQueryJustificacion(formulario, isInforme, codigos, longitudNumEjg);
	
		return (Vector<Hashtable>)this.selectGenericoBind(sql, codigos);
	}
	
	/** 
	 * Obtiene la subquery para obtener los EJGs
	 * 
	 * @param tipoResolucionDesigna
	 * @return
	 */
	private String getQueryEjgs (String tipoResolucionDesigna)
	{
		// comprobando entrada
		if (! (tipoResolucionDesigna.equals(this.resolucionDesignaFavorable) || 
				tipoResolucionDesigna.equals(this.resolucionDesignaPteCAJG) || 
				tipoResolucionDesigna.equals(this.resolucionDesignaNoFavorable)) ) {
			return "";
		}
		
		// obteniendo las propiedades que nos indican los tipos de resoluciones
		ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		final String TIPO_RESOLUCION_DENEGADO = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.denegado");
		final String TIPO_RESOLUCION_ARCHIVO = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.archivo");
		final String TIPO_RESOLUCION_DEVUELTO_COLEGIO = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.devuelto");
		final String TIPO_RESOLUCION_MODIFICADO_DENEGADO = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.modificadoydenegado");
		final String LISTA_RESOLUCIONES_NEGATIVAS = TIPO_RESOLUCION_DENEGADO +","+ TIPO_RESOLUCION_ARCHIVO +","+ 
													TIPO_RESOLUCION_DEVUELTO_COLEGIO +","+ TIPO_RESOLUCION_MODIFICADO_DENEGADO;

		final String TIPO_RESOLUCION_RECONOCIDO100 = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.reconocido100");
		final String TIPO_RESOLUCION_RECONOCIDO80 = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.reconocido80");
		final String TIPO_RESOLUCION_MOD_RECONOCIDO100_CON_NOMBRAMIENTO = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.modreconocido100_connombramiento");
		final String TIPO_RESOLUCION_MOD_RECONOCIDO100_SIN_NOMBRAMIENTO = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.modreconocido100_sinnombramiento");
		final String TIPO_RESOLUCION_MOD_RECONOCIDO80_CON_NOMBRAMIENTO = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.modreconocido80_connombramiento");
		final String TIPO_RESOLUCION_MOD_RECONOCIDO80_SIN_NOMBRAMIENTO = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.modreconocido80_sinnombramiento");
		final String LISTA_RESOLUCIONES_POSITIVAS = TIPO_RESOLUCION_RECONOCIDO100 +","+ TIPO_RESOLUCION_RECONOCIDO80 +","+ 
													TIPO_RESOLUCION_MOD_RECONOCIDO100_CON_NOMBRAMIENTO +","+ TIPO_RESOLUCION_MOD_RECONOCIDO100_SIN_NOMBRAMIENTO +","+ 
													TIPO_RESOLUCION_MOD_RECONOCIDO80_CON_NOMBRAMIENTO +","+ TIPO_RESOLUCION_MOD_RECONOCIDO80_SIN_NOMBRAMIENTO;
		
		final String TIPO_RESOLUCION_PTE_CAJG = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.pendientecajg");

		// iniciando la consulta de EJGs resueltos
		StringBuffer sql = new StringBuffer();
		sql.append(", (SELECT EJGDES.IDINSTITUCION, EJGDES.IDTURNO, EJGDES.ANIODESIGNA, EJGDES.NUMERODESIGNA ");
		sql.append("     FROM SCS_EJG EJG, SCS_EJGDESIGNA EJGDES ");
		sql.append("    WHERE EJGDES.IDINSTITUCION = EJG.IDINSTITUCION ");
		sql.append("      AND EJGDES.IDTIPOEJG = EJG.IDTIPOEJG ");
		sql.append("      AND EJGDES.ANIOEJG = EJG.ANIO ");
		sql.append("      AND EJGDES.NUMEROEJG = EJG.NUMERO ");
		sql.append("      AND EJG.Fecharesolucioncajg is not null ");
		
		// dependiendo del tipo de resolucion
		if(tipoResolucionDesigna.equals(this.resolucionDesignaFavorable))
		{
			sql.append(" AND ( (  EJG.IDTIPORATIFICACIONEJG IN ( ");
			sql.append(LISTA_RESOLUCIONES_NEGATIVAS);	
			sql.append(" ) ");
			sql.append(" AND EJG.IDTIPORESOLAUTO IS NOT NULL ");
			sql.append(" AND EJG.IDTIPORESOLAUTO IN (");
			sql.append(ClsConstants.IDTIPO_RESOLUCIONAUTO_MODYCONCEDER);
			sql.append(	"))"); 
			sql.append(" OR (EJG.IDTIPORATIFICACIONEJG IN (");
			sql.append(LISTA_RESOLUCIONES_POSITIVAS);			
			sql.append(" ) ");
			sql.append(" AND (EJG.IDTIPORESOLAUTO IS NULL OR ");
			sql.append(" EJG.IDTIPORESOLAUTO NOT IN (");
			sql.append(ClsConstants.IDTIPO_RESOLUCIONAUTO_MODYDENEGAR);
			sql.append(" ))))");
	
		} else if(tipoResolucionDesigna.equals(this.resolucionDesignaPteCAJG))
		{
			sql.append(" AND EJG.IDTIPORATIFICACIONEJG IN (");
			sql.append(TIPO_RESOLUCION_PTE_CAJG);
			sql.append(" ) ");
			
		} else if(tipoResolucionDesigna.equals(this.resolucionDesignaNoFavorable))
		{
			sql.append(" AND ((EJG.IDTIPORATIFICACIONEJG IN (");
			sql.append(LISTA_RESOLUCIONES_POSITIVAS);				
			sql.append(",");
			sql.append(TIPO_RESOLUCION_PTE_CAJG);
			sql.append(")");
			sql.append(" AND EJG.IDTIPORESOLAUTO IS NOT NULL ");
			sql.append(" AND EJG.IDTIPORESOLAUTO IN (");
			sql.append(ClsConstants.IDTIPO_RESOLUCIONAUTO_MODYDENEGAR);
			sql.append("))");
			sql.append(" OR  (EJG.IDTIPORATIFICACIONEJG IN (");
			sql.append(LISTA_RESOLUCIONES_NEGATIVAS);	
			sql.append(")");
			sql.append(" AND (EJG.IDTIPORESOLAUTO IS NULL OR ");
			sql.append(" EJG.IDTIPORESOLAUTO NOT IN (");
			sql.append(ClsConstants.IDTIPO_RESOLUCIONAUTO_MODYCONCEDER);
			sql.append("))))");
			
			
			// y que no existe otro ejg
			sql.append(" AND NOT EXISTS ");
			sql.append("     (SELECT 1 ");
			sql.append("        FROM SCS_EJG EJGDISTINTO, SCS_EJGDESIGNA EJGDESDISTINTO ");
			sql.append("       WHERE EJGDESDISTINTO.IDINSTITUCION = EJGDISTINTO.IDINSTITUCION ");
			sql.append("         AND EJGDESDISTINTO.IDTIPOEJG = EJGDISTINTO.IDTIPOEJG ");
			sql.append("         AND EJGDESDISTINTO.ANIOEJG = EJGDISTINTO.ANIO ");
			sql.append("         AND EJGDESDISTINTO.NUMEROEJG = EJGDISTINTO.NUMERO ");
			
			// [otro ejg...] distinto
			sql.append("         AND EJG.IDINSTITUCION = EJGDISTINTO.IDINSTITUCION ");
			sql.append("         AND (EJG.IDTIPOEJG <> EJGDISTINTO.IDTIPOEJG ");
			sql.append("           OR EJG.ANIO <> EJGDISTINTO.ANIO ");
			sql.append("           OR EJG.NUMERO <> EJGDISTINTO.NUMERO ) ");
	                 
			// [otro ejg...] relacionado con la misma designacion [que el ejg que estamos buscando inicialmente]
			sql.append("         AND EJGDES.IDINSTITUCION = EJGDESDISTINTO.IDINSTITUCION ");
			sql.append("         AND EJGDES.IDTURNO = EJGDESDISTINTO.IDTURNO ");
			sql.append("         AND EJGDES.ANIODESIGNA = EJGDESDISTINTO.ANIODESIGNA ");
			sql.append("         AND EJGDES.NUMERODESIGNA = EJGDESDISTINTO.NUMERODESIGNA ");
			
			// [otro ejg...] que sea favorable (bueno, con todas las opciones de favorabilidad)
			sql.append("         AND EJGDISTINTO.Fecharesolucioncajg is not null ");
			sql.append("         AND ( (  EJGDISTINTO.IDTIPORATIFICACIONEJG IN ( ");
			sql.append(                                                         LISTA_RESOLUCIONES_NEGATIVAS);	
			sql.append("                                                        ) ");
			sql.append("                  AND EJGDISTINTO.IDTIPORESOLAUTO IS NOT NULL ");
			sql.append("                  AND EJGDISTINTO.IDTIPORESOLAUTO = ");
			sql.append(                                                   ClsConstants.IDTIPO_RESOLUCIONAUTO_MODYCONCEDER);
			sql.append("               )"); 
			sql.append("           OR (EJGDISTINTO.IDTIPORATIFICACIONEJG IN (");
			sql.append(                                                      LISTA_RESOLUCIONES_POSITIVAS);			
			sql.append("                                                     ) ");
			sql.append("                AND (EJGDISTINTO.IDTIPORESOLAUTO IS NULL OR ");
			sql.append("                     EJGDISTINTO.IDTIPORESOLAUTO <> ");
			sql.append(                                                     ClsConstants.IDTIPO_RESOLUCIONAUTO_MODYDENEGAR);
			sql.append("                     ) ");
			sql.append("               ) ");
			sql.append("            ) ");
			sql.append("      )"); // fin de "y que no existe otro ejg"
			
		}
		
		// finalizando la consulta de EJGs resueltos
		sql.append(") EJGS ");
		
		return sql.toString();
	} // getQueryEjgs()
	
	private String getQueryWhereResolucion(String tipoResolucionDesigna){
		 ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		GenParametrosAdm paramAdm = new GenParametrosAdm (usrbean);
		StringBuffer sql = new StringBuffer();
		
		if(tipoResolucionDesigna.equals(this.resolucionDesignaSinResolucion)){
			//Todo sus ejgs estan sin resolucion
			sql.append(" AND (SELECT COUNT(*) ");
			sql.append(" FROM SCS_EJG EJG, SCS_EJGDESIGNA EJGDES ");
			sql.append(" WHERE D.IDINSTITUCION = EJGDES.IDINSTITUCION ");
			sql.append(" AND D.IDTURNO = EJGDES.IDTURNO ");
			sql.append(" AND D.ANIO = EJGDES.ANIODESIGNA ");
			sql.append(" AND D.NUMERO = EJGDES.NUMERODESIGNA ");
			sql.append(" AND EJGDES.IDINSTITUCION = EJG.IDINSTITUCION ");
			sql.append(" AND EJGDES.IDTIPOEJG = EJG.IDTIPOEJG ");
			
			sql.append(" AND EJGDES.ANIOEJG = EJG.ANIO ");
			sql.append(" AND EJGDES.NUMEROEJG = EJG.NUMERO)=(SELECT COUNT(*) ");
			sql.append(" FROM SCS_EJG EJG, SCS_EJGDESIGNA EJGDES ");
			sql.append(" WHERE D.IDINSTITUCION = EJGDES.IDINSTITUCION ");
			sql.append(" AND D.IDTURNO = EJGDES.IDTURNO ");
			sql.append(" AND D.ANIO = EJGDES.ANIODESIGNA ");
			sql.append(" AND D.NUMERO = EJGDES.NUMERODESIGNA ");
			sql.append(" AND EJGDES.IDINSTITUCION = EJG.IDINSTITUCION ");
			sql.append(" AND EJGDES.IDTIPOEJG = EJG.IDTIPOEJG ");
			sql.append(" AND EJGDES.ANIOEJG = EJG.ANIO ");
			sql.append(" AND EJGDES.NUMEROEJG = EJG.NUMERO ");
			sql.append(" AND (EJG.IDTIPORATIFICACIONEJG IS NULL ");
			sql.append(" OR EJG.FECHARESOLUCIONCAJG IS NULL) ");
			sql.append(" ) ");
			sql.append(" AND  ");
			sql.append(" (SELECT COUNT(*) ");
			sql.append(" FROM SCS_EJG EJG, SCS_EJGDESIGNA EJGDES ");
			sql.append(" WHERE D.IDINSTITUCION = EJGDES.IDINSTITUCION ");
			sql.append(" AND D.IDTURNO = EJGDES.IDTURNO ");
			sql.append(" AND D.ANIO = EJGDES.ANIODESIGNA ");
			sql.append(" AND D.NUMERO = EJGDES.NUMERODESIGNA ");
			sql.append(" AND EJGDES.IDINSTITUCION = EJG.IDINSTITUCION ");
			sql.append(" AND EJGDES.IDTIPOEJG = EJG.IDTIPOEJG ");
			sql.append(" AND EJGDES.ANIOEJG = EJG.ANIO ");
			sql.append(" AND EJGDES.NUMEROEJG = EJG.NUMERO)>0 ");
//			sql.append(" ) ");

			
		}else if(tipoResolucionDesigna.equals(this.resolucionDesignaSinEjg)){
			sql.append(" AND NOT EXISTS (SELECT * ");
			sql.append(" FROM SCS_EJG EJG, SCS_EJGDESIGNA EJGDES ");
			sql.append(" WHERE D.IDINSTITUCION = EJGDES.IDINSTITUCION ");
			sql.append(" AND D.IDTURNO = EJGDES.IDTURNO ");
			sql.append(" AND D.ANIO = EJGDES.ANIODESIGNA ");
			sql.append(" AND D.NUMERO = EJGDES.NUMERODESIGNA ");
			sql.append(" AND EJGDES.IDINSTITUCION = EJG.IDINSTITUCION ");
			sql.append(" AND EJGDES.IDTIPOEJG = EJG.IDTIPOEJG ");
			sql.append(" AND EJGDES.ANIOEJG = EJG.ANIO ");
			sql.append(" AND EJGDES.NUMEROEJG = EJG.NUMERO) ");

		}
		return sql.toString();
		
	}
	
	
	private String getQueryBaseJustificacion(InformeJustificacionMasivaForm formulario, boolean isInforme,Hashtable codigos,String longitudNumEjg){
		StringBuffer extra = new StringBuffer("");
		
		boolean isMostrarJustificacionesPtes = formulario.getMostrarTodas()!=null && formulario.getMostrarTodas().equals("true");
	    int contador=codigos.size();
		StringBuilder sql = new StringBuilder();
		sql.append(" ");
		sql.append(" SELECT DECODE(ALLDESIGNAS.NUM_TIPO_RESOLUCION_DESIGNA,1,'FAVORABLE', 2,'NO_FAVORABLE', 3,'PTE_CAJG', 4, 'SIN_RESOLUCION','SIN_EJG') AS TIPO_RESOLUCION_DESIGNA, ");
		sql.append(" ALLDESIGNAS.* ");
		sql.append(" FROM ( ");
		
//		Estos campos solo los muestra cuanto no hay que mostrar justificaciones pendientes
//		sql.append(" ,SELECT F_SIGA_GETEJG_DESIGNA(D.IDINSTITUCION, D.IDTURNO, D.ANIO, D.NUMERO,5) AS EXPEDIENTES ");
//		sql.append(" ,D.ANIO || '/' || D.CODIGO AS CODIGODESIGNA , D.RESUMENASUNTO,   DL.FECHARENUNCIA ");
//		sql.append(" ,D.NUMPROCEDIMIENTO AS ASUNTO ");
//		sql.append(" ,F_SIGA_GETDEFENDIDOSDESIGNA(D.IDINSTITUCION, D.ANIO, D.IDTURNO, D.NUMERO, 0) AS CLIENTE ");
		
		sql.append(" SELECT TO_CHAR(D.FECHAENTRADA, 'dd/mm/yyyy') AS FECHADESIGNA, ");
		sql.append(" TO_CHAR(D.FECHAENTRADA, 'yyyy_mm_dd') AS FECHAORDEN, ");
		sql.append(" D.ART27 AS ART27, ");
		sql.append(" D.ANIO || '/' || D.CODIGO AS CODIGODESIGNA,");
		
		sql.append(" F_SIGA_GETEJG_DESIGNA(:");
		contador++;
		codigos.put(new Integer(contador),formulario.getIdInstitucion());
		sql.append(contador);
		sql.append(",d.idturno,d.anio,d.numero,");
		sql.append(Integer.parseInt(longitudNumEjg));
		sql.append(") AS EXPEDIENTES, ");
		
		sql.append(" DECODE(D.ANIOPROCEDIMIENTO,NULL,D.NUMPROCEDIMIENTO,D.NUMPROCEDIMIENTO||'/'||D.ANIOPROCEDIMIENTO) AS ASUNTO, ");
		
		sql.append(" f_siga_getdefendidosdesigna(:");
		contador++;
		codigos.put(new Integer(contador),formulario.getIdInstitucion());
		sql.append(contador);
		sql.append(",d.anio,d.idturno,d.numero,0) AS CLIENTE, ");
		
		sql.append(" D.RESUMENASUNTO AS RESUMENASUNTO, ");
		sql.append(" DL.FECHARENUNCIA, ");
		sql.append(" D.IDINSTITUCION, ");
		sql.append(" D.IDTURNO, ");
		sql.append(" D.ANIO, ");
		sql.append(" D.NUMERO, ");
		sql.append(" D.CODIGO, ");
		sql.append(" D.IDJUZGADO, ");
		sql.append(" D.IDINSTITUCION_JUZG, ");
		sql.append(" D.ESTADO, ");
		sql.append(" D.SUFIJO, ");
		sql.append(" D.FECHAENTRADA, ");
		sql.append(" DL.IDPERSONA, ");
		sql.append(" (SELECT PROC.IDPROCEDIMIENTO FROM SCS_PROCEDIMIENTOS PROC WHERE "); 
		sql.append(" D.IDINSTITUCION = PROC.IDINSTITUCION AND ");
		sql.append(" D.IDPROCEDIMIENTO =PROC.IDPROCEDIMIENTO  ");
		sql.append(" AND (PROC.FECHABAJA IS NULL OR PROC.FECHABAJA>SYSDATE) ");
		sql.append(" ) IDPROCEDIMIENTO ");
		sql.append(" , ");
//		sql.append(" D.IDPROCEDIMIENTO, ");
		sql.append(" D.NUMPROCEDIMIENTO, ");
		sql.append(" D.ANIOPROCEDIMIENTO, ");
		sql.append(" D.NIG, ");
		sql.append(" (SELECT COUNT(*) FROM SCS_DESIGNASLETRADO SDL WHERE D.IDINSTITUCION = SDL.IDINSTITUCION AND D.ANIO = SDL.ANIO AND D.NUMERO = SDL.NUMERO AND D.IDTURNO = SDL.IDTURNO) AS CAMBIOLETRADO, ");

		BusinessManager businessManager =  BusinessManager.getInstance();	
		EjgService ejgService = (EjgService)businessManager.getService(EjgService.class);
		boolean isColegioZonaMinisterio = ejgService.isColegioZonaComun(Short.valueOf(formulario.getIdInstitucion()));
		if(isColegioZonaMinisterio)
	    	sql.append("DECODE(");
		sql.append(" (SELECT MIN(CASE WHEN (EJG.FECHARESOLUCIONCAJG IS NOT NULL ");
		sql.append(" AND ((EJG.IDTIPORATIFICACIONEJG IN (3,5,6,7) ");
		sql.append(" AND EJG.IDTIPORESOLAUTO IS NOT NULL ");
		sql.append(" AND EJG.IDTIPORESOLAUTO IN (1)) ");
		sql.append(" OR (EJG.IDTIPORATIFICACIONEJG IN (1,2,8,9,10,11) ");
		sql.append(" AND (EJG.IDTIPORESOLAUTO IS NULL ");
		sql.append(" OR EJG.IDTIPORESOLAUTO NOT IN (3))))) THEN 1 "); 
	                           
		sql.append(" WHEN (EJG.FECHARESOLUCIONCAJG IS NOT NULL ");
		sql.append(" AND ((EJG.IDTIPORATIFICACIONEJG IN (1,2,8,9,10,11,0) ");
		sql.append(" AND EJG.IDTIPORESOLAUTO IS NOT NULL ");
		sql.append(" AND EJG.IDTIPORESOLAUTO IN (3)) ");
		sql.append(" OR (EJG.IDTIPORATIFICACIONEJG IN (3,5,6,7) ");
		sql.append(" AND (EJG.IDTIPORESOLAUTO IS NULL ");
		sql.append(" OR EJG.IDTIPORESOLAUTO NOT IN (1))))) THEN 2 "); 
	                           
		sql.append(" WHEN (EJG.FECHARESOLUCIONCAJG IS NOT NULL ");
		sql.append(" AND EJG.IDTIPORATIFICACIONEJG IN (4)) THEN  3 "); 
	                           
		sql.append(" ELSE 4 END) ");
	                
		sql.append(" FROM SCS_EJG EJG, ");
		sql.append(" SCS_EJGDESIGNA EJGDES ");
		
		sql.append(" WHERE EJGDES.IDINSTITUCION = EJG.IDINSTITUCION ");
		sql.append(" AND EJGDES.IDTIPOEJG = EJG.IDTIPOEJG ");
		sql.append(" AND EJGDES.ANIOEJG = EJG.ANIO ");
		sql.append(" AND EJGDES.NUMEROEJG = EJG.NUMERO ");
		sql.append(" AND D.IDINSTITUCION = EJGDES.IDINSTITUCION ");
		sql.append(" AND D.IDTURNO = EJGDES.IDTURNO ");
		sql.append(" AND D.ANIO = EJGDES.ANIODESIGNA ");
		sql.append(" AND D.NUMERO = EJGDES.NUMERODESIGNA) ");
		
		
		if(isColegioZonaMinisterio){
			sql.append(" ,1,1,2,2,3,NVL((SELECT DISTINCT 1 ");
			sql.append(" FROM SCS_PROCEDIMIENTOS P, SCS_JURISDICCION J ");
			sql.append(" WHERE P.IDJURISDICCION = J.IDJURISDICCION ");
			sql.append(" AND NVL(J.JUSTIFICAR_SIN_RESOL, 0) = 1 ");
			sql.append(" AND P.IDINSTITUCION = D.IDINSTITUCION ");
			sql.append(" AND P.IDPROCEDIMIENTO = D.IDPROCEDIMIENTO),3),4,NVL((SELECT DISTINCT 1 ");
			sql.append(" FROM SCS_PROCEDIMIENTOS P, SCS_JURISDICCION J ");
			sql.append(" WHERE P.IDJURISDICCION = J.IDJURISDICCION ");
			sql.append(" AND NVL(J.JUSTIFICAR_SIN_RESOL, 0) = 1 ");
			sql.append(" AND P.IDINSTITUCION = D.IDINSTITUCION ");
			sql.append(" AND P.IDPROCEDIMIENTO = D.IDPROCEDIMIENTO),4),NVL((SELECT DISTINCT 1 ");
			sql.append(" FROM SCS_PROCEDIMIENTOS P, SCS_JURISDICCION J ");
			sql.append(" WHERE P.IDJURISDICCION = J.IDJURISDICCION ");
			sql.append(" AND NVL(J.JUSTIFICAR_SIN_RESOL, 0) = 1 ");
			sql.append(" AND P.IDINSTITUCION = D.IDINSTITUCION ");
			sql.append(" AND P.IDPROCEDIMIENTO = D.IDPROCEDIMIENTO),NULL)) ");
		}
		sql.append(" AS NUM_TIPO_RESOLUCION_DESIGNA ");
	        
		sql.append(" FROM SCS_DESIGNA D, ");
		sql.append(" SCS_DESIGNASLETRADO DL ");
	        
		sql.append(" WHERE D.IDINSTITUCION = DL.IDINSTITUCION ");
		sql.append(" AND D.ANIO = DL.ANIO ");
		sql.append(" AND D.NUMERO = DL.NUMERO ");
		sql.append(" AND D.IDTURNO = DL.IDTURNO ");
		
		sql.append(" AND D.IDINSTITUCION = :");
		contador++;
		codigos.put(new Integer(contador),formulario.getIdInstitucion());
		sql.append(contador);
		
		String sAnioDesigna = formulario.getAnioDesgina();
		if (sAnioDesigna!=null && !sAnioDesigna.trim().equals("")) {
			sql.append(" AND D.ANIO = :");
			contador++;
			codigos.put(new Integer(contador), formulario.getAnioDesgina().trim());
			sql.append(contador);
		}
		
		String sCodigoDesigna = formulario.getCodigoDesigna();
		if (sCodigoDesigna!=null) {
			sCodigoDesigna = sCodigoDesigna.trim();
			if (!sCodigoDesigna.equals("")) {
				sql.append(" AND ");
				contador++;
				if (ComodinBusquedas.hasComodin(sCodigoDesigna)) {
					sql.append(ComodinBusquedas.prepararSentenciaCompletaBind(sCodigoDesigna, "D.CODIGO", contador, codigos));
				
				} else if (ComodinBusquedas.hasComa(sCodigoDesigna) || ComodinBusquedas.hasGuion(sCodigoDesigna)) {
					ComodinBusquedas comodinBusquedas = new ComodinBusquedas();
					sql.append(comodinBusquedas.prepararSentenciaCompletaEJGBind(sCodigoDesigna, "D.CODIGO", contador, codigos));
					contador =  codigos.size();
					
				} else {
					codigos.put(new Integer(contador), sCodigoDesigna);
					sql.append(" LTRIM(D.CODIGO, '0')  = LTRIM(:");
					sql.append(contador);
					sql.append(", '0') ");
				}
			}
		}	
		
		String sAnioEJG = formulario.getAnioEJG();
		String sCodigoEJG = formulario.getCodigoEJG();
		if ((sAnioEJG!=null && !sAnioEJG.trim().equals("")) || (sCodigoEJG!=null && !sCodigoEJG.trim().equals(""))) {
			sql.append(" AND EXISTS ( ");
			sql.append(" SELECT 1 ");
			sql.append(" FROM SCS_EJG EJG, ");
			sql.append(" SCS_EJGDESIGNA EJGDES ");
			sql.append(" WHERE EJGDES.IDINSTITUCION = EJG.IDINSTITUCION ");
			sql.append(" AND EJGDES.IDTIPOEJG = EJG.IDTIPOEJG ");
			sql.append(" AND EJGDES.ANIOEJG = EJG.ANIO ");
			sql.append(" AND EJGDES.NUMEROEJG = EJG.NUMERO ");
			sql.append(" AND D.IDINSTITUCION = EJGDES.IDINSTITUCION ");
			sql.append(" AND D.IDTURNO = EJGDES.IDTURNO ");
			sql.append(" AND D.ANIO = EJGDES.ANIODESIGNA ");
			sql.append(" AND D.NUMERO = EJGDES.NUMERODESIGNA ");
			
			if (sAnioEJG!=null && !sAnioEJG.trim().equals("")) {
				sql.append(" AND EJG.ANIO = :");
				contador++;
				codigos.put(new Integer(contador), sAnioEJG.trim());
				sql.append(contador);
			}
			
			if (sCodigoEJG!=null) {
				sCodigoEJG = sCodigoEJG.trim();
				if (!sCodigoEJG.equals("")) {
					sql.append(" AND ");
					contador++;
					if (ComodinBusquedas.hasComodin(sCodigoEJG)) {
						sql.append(ComodinBusquedas.prepararSentenciaCompletaBind(sCodigoEJG, "EJG.NUMEJG", contador, codigos));
					
					} else if (ComodinBusquedas.hasComa(sCodigoEJG) || ComodinBusquedas.hasGuion(sCodigoEJG)) {
						ComodinBusquedas comodinBusquedas = new ComodinBusquedas();
						sql.append(comodinBusquedas.prepararSentenciaCompletaEJGBind(sCodigoEJG, "EJG.NUMEJG", contador, codigos));
						contador =  codigos.size();
						
					} else {
						codigos.put(new Integer(contador), sCodigoEJG);
						sql.append(" LTRIM(EJG.NUMEJG, '0')  = LTRIM(:");
						sql.append(contador);
						sql.append(", '0') ");
					}
				}
			}			
			sql.append(" ) ");
		}

		if(formulario.getActuacionesPendientes()!=null && !formulario.getActuacionesPendientes().equals("")){
			if (formulario.getActuacionesPendientes()!= null && !formulario.getActuacionesPendientes().equalsIgnoreCase("")) {
				if(formulario.getActuacionesPendientes().equalsIgnoreCase("SINACTUACIONES")){
					sql.append(" AND F_SIGA_ACTUACIONESDESIG(D.IDINSTITUCION,D.IDTURNO,D.ANIO,D.NUMERO) IS NULL ");
				}else{
					contador++;
				    codigos.put(new Integer(contador),formulario.getActuacionesPendientes().toUpperCase());
				    sql.append(" AND UPPER(F_SIGA_ACTUACIONESDESIG(D.IDINSTITUCION,D.IDTURNO,D.ANIO,D.NUMERO))=UPPER(:");
				    sql.append(contador);
				    sql.append(")");
				}
			}
		}
		
		if ((formulario.getFechaJustificacionDesde() != null && !formulario.getFechaJustificacionDesde().equals(""))||(formulario.getFechaJustificacionHasta() != null && !formulario.getFechaJustificacionHasta().equals(""))) {
			
			sql.append(" AND (SELECT COUNT(*) FROM SCS_ACTUACIONDESIGNA ACT");
			sql.append(" WHERE ACT.IDINSTITUCION = D.IDINSTITUCION AND ACT.ANIO = D.ANIO ");
			sql.append(" AND ACT.IDTURNO = D.IDTURNO AND ACT.NUMERO = D.NUMERO" );
			
			if (formulario.getFechaJustificacionDesde() != null && !formulario.getFechaJustificacionDesde().equals("")) {
				sql.append(" AND ACT.FECHAJUSTIFICACION >= :");
				
				contador++;
				codigos.put(new Integer(contador),formulario.getFechaJustificacionDesde());
				sql.append(contador);
				 
			}
			if (formulario.getFechaJustificacionHasta() != null && !formulario.getFechaJustificacionHasta().equals("")) {
				sql.append(" AND ACT.FECHAJUSTIFICACION<= :");
				contador++;
				codigos.put(new Integer(contador),formulario.getFechaJustificacionHasta());
				sql.append(contador);
				 
			}
			sql.append(" )>0");
			 
		}
		
		if (formulario.getFechaDesde() != null && !formulario.getFechaDesde().equals("")) {
			sql.append(" AND TRUNC(D.FECHAENTRADA) >= :");
			
			contador++;
			codigos.put(new Integer(contador),formulario.getFechaDesde());
			sql.append(contador);
			 
		}else{
			sql.append(" AND TRUNC(D.FECHAENTRADA) > :");
			
			contador++;
			codigos.put(new Integer(contador),"01/01/1950");
			sql.append(contador);
			
		}
		
		if (formulario.getFechaHasta() != null && !formulario.getFechaHasta().equals("")) {
			sql.append(" AND TRUNC(D.FECHAENTRADA) <=:");
			
			contador++;
			codigos.put(new Integer(contador),formulario.getFechaHasta());
			sql.append(contador);
			 
		}
		
		

		
		if((formulario.getInteresadoApellidos() != null && !formulario.getInteresadoApellidos().equalsIgnoreCase("")) 
				&& (formulario.getInteresadoNombre() != null && !formulario.getInteresadoNombre().equalsIgnoreCase(""))){
			
			
			sql.append(" AND UPPER(");
			sql.append("f_siga_getdefendidosdesigna(:");
			contador++;
			codigos.put(new Integer(contador),formulario.getIdInstitucion());
			sql.append(contador);
			sql.append(",D.anio,D.idturno,D.numero,1) ");
			sql.append(") like ");
			contador++;
			StringBuffer aux = new StringBuffer();
			aux.append("%");
			aux.append(formulario.getInteresadoNombre().trim().toUpperCase());
			aux.append("%");
			aux.append(formulario.getInteresadoApellidos().trim().toUpperCase());
			aux.append("%");
			codigos.put(new Integer(contador),aux.toString());
			
			sql.append(":"+contador);
		}else if (formulario.getInteresadoApellidos() != null && !formulario.getInteresadoApellidos().equalsIgnoreCase("") && (formulario.getInteresadoNombre()==null||formulario.getInteresadoNombre().equalsIgnoreCase("")) ){
			sql.append(" and UPPER(");
			sql.append("f_siga_getdefendidosdesigna(:");
			contador++;
			codigos.put(new Integer(contador),formulario.getIdInstitucion());
			sql.append(contador);
			sql.append(",d.anio,d.idturno,d.numero,1) ");
			sql.append(") like ");
			contador++;
			StringBuffer aux = new StringBuffer();
		    aux.append("%");
		    aux.append(formulario.getInteresadoApellidos().trim().toUpperCase());
		    aux.append("%");
			codigos.put(new Integer(contador),aux.toString());
		    
			sql.append(":"+contador);
		}else if ((formulario.getInteresadoApellidos() == null||formulario.getInteresadoApellidos().equalsIgnoreCase("")) && formulario.getInteresadoNombre()!=null && !formulario.getInteresadoNombre().equalsIgnoreCase("")){
			sql.append(" and UPPER(");
			sql.append("f_siga_getdefendidosdesigna(:");
			contador++;
			codigos.put(new Integer(contador),formulario.getIdInstitucion());
			sql.append(contador);
			sql.append(",d.anio,d.idturno,d.numero,1) ");
			sql.append(") like ");
		    contador++;
			StringBuffer aux = new StringBuffer();
		    aux.append("%");
		    aux.append(formulario.getInteresadoNombre().trim().toUpperCase());
		    aux.append("%");
		    codigos.put(new Integer(contador),aux.toString());
		    
			sql.append(":"+contador);
		}
		
		
		if(formulario.getIdPersona()!=null && !formulario.getIdPersona().equalsIgnoreCase("")){
			sql.append(" AND DL.IDPERSONA = :");
			contador++;
			codigos.put(new Integer(contador),formulario.getIdPersona());
			sql.append(contador);
		}
		if (formulario.getAnio() != null && !formulario.getAnio().equals("")) {
			sql.append(" AND D.ANIO = :");	
			contador++;
			codigos.put(new Integer(contador),formulario.getAnio());
			sql.append(contador);
		}
		if(isMostrarJustificacionesPtes)
			sql.append(" AND D.ESTADO NOT IN ('A','F') ");
		
		if(formulario.getEstado()!=null && !formulario.getEstado().equals("")){
			sql.append(" AND D.ESTADO =:");
			contador++;
			codigos.put(new Integer(contador),formulario.getEstado());
			sql.append(contador);
			
		}
		
		sql.append(" ) ALLDESIGNAS ");

		return sql.toString();
		
	}
	
	private String getQueryJustificacion(InformeJustificacionMasivaForm formulario, boolean isInforme,Hashtable codigos,String longitudNumEjg)throws ClsExceptions{
		StringBuffer extra = new StringBuffer("");
		boolean isMostrarJustificacionesPtes = formulario.getMostrarTodas()!=null && formulario.getMostrarTodas().equals("true");
	    int contador=0;
	    StringBuffer sql = new StringBuffer("");
		
	    ReadProperties rp3 = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		GenParametrosAdm paramAdm = new GenParametrosAdm (usrbean);
		sql.append(this.getQueryBaseJustificacion(formulario, isInforme, codigos, longitudNumEjg));
		String codIncluirEjgNoFavorable = formulario.getIncluirEjgNoFavorable();
		String codIncluirEjgSinResolucion = formulario.getIncluirEjgSinResolucion();
		String codIncluirSinEJG = formulario.getIncluirSinEJG();
		String codIncluirEjgPteCAJG = formulario.getIncluirEjgPteCAJG();
		//cunado no estan activas las restricciones sacamos todo. Entonces la union se pone siempre que no estesn activas las restricciones
		// o cuando la restriccion nos lo diga
		
		//WHERE DATOS.NUM_TIPO_RESOLUCION_DESIGNA IN (1,2,3,4) or DATOS.NUM_TIPO_RESOLUCION_DESIGNA is null
		StringBuilder tiposResolucionBuilder = new StringBuilder();
		tiposResolucionBuilder.append(" WHERE (ALLDESIGNAS.NUM_TIPO_RESOLUCION_DESIGNA IN (1");
		if (!formulario.isActivarRestriccionesFicha() || (codIncluirEjgPteCAJG!=null && !codIncluirEjgPteCAJG.equals(ClsConstants.DB_FALSE))) {
			tiposResolucionBuilder.append(",3");
		}		
		if (!formulario.isActivarRestriccionesFicha() || (codIncluirEjgNoFavorable!=null && !codIncluirEjgNoFavorable.equals(ClsConstants.DB_FALSE))) {
			tiposResolucionBuilder.append(",2");
		}
		if (!formulario.isActivarRestriccionesFicha() || (codIncluirEjgSinResolucion!=null && !codIncluirEjgSinResolucion.equals(ClsConstants.DB_FALSE))){
			tiposResolucionBuilder.append(",4");
		}
		tiposResolucionBuilder.append(")");
		
		if (!formulario.isActivarRestriccionesFicha() ||(codIncluirSinEJG!=null && !codIncluirSinEJG.equals(ClsConstants.DB_FALSE))){
			tiposResolucionBuilder.append(" OR ALLDESIGNAS.NUM_TIPO_RESOLUCION_DESIGNA is null ");
		}
		
		tiposResolucionBuilder.append(")");
		sql.append(tiposResolucionBuilder);
		if (isMostrarJustificacionesPtes) {
			sql.append(" AND (NOT EXISTS ");
			sql.append(" (SELECT * ");
			sql.append(" FROM SCS_ACTUACIONDESIGNA ACT ");
			sql.append(" WHERE ACT.IDINSTITUCION = ALLDESIGNAS.IDINSTITUCION ");
			sql.append(" AND ACT.IDTURNO = ALLDESIGNAS.IDTURNO ");
			sql.append(" AND ACT.ANIO = ALLDESIGNAS.ANIO ");
			sql.append(" AND ACT.NUMERO = ALLDESIGNAS.NUMERO) OR ");
			sql.append(" (SELECT COUNT(*) ");
//	  --NUMERO DE ACREDITACIONES 
			sql.append(" FROM SCS_ACREDITACIONPROCEDIMIENTO ACP ");
//	  --DE LOS PROCEDIMIENTOS DE TODAS LAS ACTUACIONES DE LA DESIGNACION
			sql.append(" WHERE EXISTS (SELECT * ");
			sql.append(" FROM SCS_ACTUACIONDESIGNA ACT ");
			sql.append(" WHERE ACT.IDINSTITUCION = ALLDESIGNAS.IDINSTITUCION ");
			sql.append(" AND ACT.IDTURNO = ALLDESIGNAS.IDTURNO ");
			sql.append(" AND ACT.ANIO = ALLDESIGNAS.ANIO ");
			sql.append(" AND ACT.NUMERO = ALLDESIGNAS.NUMERO ");
			sql.append(" AND ACT.IDINSTITUCION_PROC = ACP.IDINSTITUCION ");
			sql.append(" AND ACT.IDPROCEDIMIENTO = ACP.IDPROCEDIMIENTO) ");
//	        --Y QUE NO ESTAN YA VALIDADAS 
			sql.append(" AND NOT EXISTS (SELECT * ");
			sql.append(" FROM SCS_ACTUACIONDESIGNA ACT ");
			sql.append(" WHERE ACT.IDINSTITUCION = ALLDESIGNAS.IDINSTITUCION ");
			sql.append(" AND ACT.IDTURNO = ALLDESIGNAS.IDTURNO ");
			sql.append(" AND ACT.ANIO = ALLDESIGNAS.ANIO ");
			sql.append(" AND ACT.NUMERO = ALLDESIGNAS.NUMERO ");
			sql.append(" AND ACT.IDINSTITUCION_PROC = ACP.IDINSTITUCION ");
			sql.append(" AND ACT.IDPROCEDIMIENTO = ACP.IDPROCEDIMIENTO ");
			sql.append(" AND ACT.IDACREDITACION = ACP.IDACREDITACION ");
			sql.append(" AND ACT.VALIDADA = '1'))>0 ");
			sql.append(" ) ");
		}
		
		if (!isMostrarJustificacionesPtes && !isInforme)
			sql.append(" ORDER BY ALLDESIGNAS.FECHAENTRADA DESC,        ALLDESIGNAS.IDINSTITUCION,      ALLDESIGNAS.ANIO,          ALLDESIGNAS.CODIGO DESC,          ALLDESIGNAS.SUFIJO ");
		
		return sql.toString();
	}
	
	/**
	 * Recupera la fecha de renuncia del primer letrado de la designa
	 * @param sdb
	 * @param fecha
	 * @return
	 * @throws ClsExceptions 
	 */
	public Hashtable getUltimoLetrado(Hashtable<String, Object> dato) throws ClsExceptions {
		try {
			String sql = 
				" select dp.* "+
				" from "+ 
				ScsDesignasLetradoBean.T_NOMBRETABLA+" dp"+
				" where dp."+ScsDesignasLetradoBean.C_IDINSTITUCION+"=" +(String)dato.get("IDINSTITUCION")+
				" and dp."+ScsDesignasLetradoBean.C_ANIO+"="+(String)dato.get("ANIO")+
				" and dp."+ScsDesignasLetradoBean.C_NUMERO+"=" +(String)dato.get("NUMERO")+
				" and dp."+ScsDesignasLetradoBean.C_IDTURNO+"=" +(String)dato.get("IDTURNO")+
				" order by dp."+ScsDesignasLetradoBean.C_FECHADESIGNA+" DESC";

			Vector vector = selectGenerico(sql);
		    if ((vector != null) && (vector.size() > 0)) {
		    	return (Hashtable)vector.get(0);
		    }

			return null;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getUltimoLetrado()");
		}

	}	
	
	
	/**
	 * Recupera la fecha de renuncia del primer letrado de la designa
	 * @param sdb
	 * @param fecha
	 * @return
	 * @throws ClsExceptions 
	 */
	public Hashtable getPrimerLetrado(ScsDesignaBean sdb) throws ClsExceptions {
		try {
			String sql = "select * "+
						 "  from scs_designasletrado " +
						 " where idinstitucion = "+sdb.getIdInstitucion()+" " +
						 "   and idturno = "+sdb.getIdTurno()+" " +
						 "   and anio = "+sdb.getAnio()+" " +
						 "   and numero = "+sdb.getNumero()+" " +
						 " order by fechaRenuncia asc";

			Vector vector = selectGenerico(sql);
		    if ((vector != null) && (vector.size() > 0)) {
		    	return (Hashtable)vector.get(0);
		    }

			return null;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre obtenerColegiadoDesignadoEnFecha()");
		}

	}

private List<DefinirEJGForm> getExpedientes(String expedientes)throws ClsExceptions{
		//Vemos si esta finalizado y le metemos el campo baja=X
	List<DefinirEJGForm> ejgList = new ArrayList<DefinirEJGForm>();
	DefinirEJGForm ejgForm =  null;
	if (expedientes != null && !expedientes.equals("")) {
		
		String[] ejgs = expedientes.split(",");
		for (String ejg:ejgs) {
			if(ejg.indexOf("##") > -1){
				String[] ejgDoc = ejg.split("##");
				ejgForm = new DefinirEJGForm();
				ejgForm.setNombre(ejgDoc[0].trim());
				ejgForm.setDocResolucion(ejgDoc[1].trim());
				ejgForm.setIdInstitucion(ejgDoc[2]);
				ejgForm.setAnio(ejgDoc[3]);
				ejgForm.setIdTipoEJG(ejgDoc[4]);
				ejgForm.setNumero(ejgDoc[5]);
				ejgForm.setIdTipoRatificacionEJG(ejgDoc[6].trim());
				ejgForm.setFechaResolucionCAJG(ejgDoc[7].trim());
				ejgForm.setIdTipoDictamenEJG(ejgDoc[8].trim());
				
			}else{
				ejgForm = new DefinirEJGForm();
				ejgForm.setNombre(ejg.trim());
			}
			ejgList.add(ejgForm);
		}

		
	}
	return ejgList;
	
}
	
	public boolean updateFechaDesigna(Hashtable hash, String fechaDesigna) throws ClsExceptions{

		try {
			Row row = new Row();	
			StringBuffer sql = new StringBuffer(); 
			
			sql.append(" update scs_designasletrado ");
			sql.append("    set fechadesigna = '"+UtilidadesString.formatoFecha(fechaDesigna, ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH)+"', ");
			sql.append("        fechamodificacion = sysdate ,");
			sql.append("        usumodificacion =" +this.usuModificacion );
			sql.append("  where idinstitucion = " + hash.get(ScsDesignasLetradoBean.C_IDINSTITUCION));
			sql.append("    and idturno = "+ hash.get(ScsDesignasLetradoBean.C_IDTURNO));
			sql.append("    and anio = " + hash.get(ScsDesignasLetradoBean.C_ANIO));
			sql.append("    and numero = " + hash.get(ScsDesignasLetradoBean.C_NUMERO));
			sql.append("    and idpersona = " + hash.get(ScsDesignasLetradoBean.C_IDPERSONA));
			sql.append("    and fechadesigna = '"+UtilidadesString.formatoFecha((String)hash.get(ScsDesignasLetradoBean.C_FECHADESIGNA), ClsConstants.DATE_FORMAT_JAVA,ClsConstants.DATE_FORMAT_SHORT_SPANISH)+"' ");
			
			this.updateSQL(sql.toString());
		}
		catch (Exception e) {
			throw new ClsExceptions(e,  e.getMessage());
		}
		return true;
	}
	
	
		
}
class Acumulador{
	
	Hashtable acumuladorJuzgadosHashtable;
	Hashtable acumuladorProcedimientosHashtable;
	Hashtable acumuladorTurnosHashtable;
	public Acumulador() {
		acumuladorJuzgadosHashtable = new Hashtable();
		acumuladorProcedimientosHashtable = new Hashtable();
		acumuladorTurnosHashtable = new Hashtable();
	}
	public Hashtable getAcumuladorJuzgadosHashtable() {
		return acumuladorJuzgadosHashtable;
	}
	public void setAcumuladorJuzgadosHashtable(Hashtable acumuladorJuzgadosHashtable) {
		this.acumuladorJuzgadosHashtable = acumuladorJuzgadosHashtable;
	}
	public Hashtable getAcumuladorProcedimientosHashtable() {
		return acumuladorProcedimientosHashtable;
	}
	public void setAcumuladorProcedimientosHashtable(
			Hashtable acumuladorProcedimientosHashtable) {
		this.acumuladorProcedimientosHashtable = acumuladorProcedimientosHashtable;
	}
	public Hashtable getAcumuladorTurnosHashtable() {
		return acumuladorTurnosHashtable;
	}
	public void setAcumuladorTurnosHashtable(Hashtable acumuladorTurnosHashtable) {
		this.acumuladorTurnosHashtable = acumuladorTurnosHashtable;
	}
	public void reset(){
		acumuladorJuzgadosHashtable = new Hashtable();
		acumuladorProcedimientosHashtable = new Hashtable();
		acumuladorTurnosHashtable = new Hashtable();
		
	}

	
}