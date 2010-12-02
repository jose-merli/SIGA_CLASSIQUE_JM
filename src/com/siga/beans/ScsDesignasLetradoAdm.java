
package com.siga.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DesignaForm;
import com.siga.gratuita.form.InformeJustificacionMasivaForm;



/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_TIPODESIGNASCOLEGIADO
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 */
public class ScsDesignasLetradoAdm extends MasterBeanAdministrador {
	
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
				ScsDesignasLetradoBean.C_IDTIPOMOTIVO,		ScsDesignasLetradoBean.C_OBSERVACIONES
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
							ScsDesignasLetradoBean.C_IDPERSONA,			ScsDesignasLetradoBean.C_FECHADESIGNA};
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
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsDesignasLetradoBean.C_ANIO));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsDesignasLetradoBean.C_NUMERO));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsDesignasLetradoBean.C_IDTURNO));
			bean.setIdPersona(UtilidadesHash.getInteger(hash,ScsDesignasLetradoBean.C_IDPERSONA));
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
	
	private PaginadorBind getDesignasPendientesJustificacionPaginador(InformeJustificacionMasivaForm formulario,boolean isInforme)throws ClsExceptions,SIGAException {
		PaginadorBind paginador=null;
		try {
			Acumulador acumula = new Acumulador();
			List<DesignaForm> designasList = getDesignasJustificacion(formulario,acumula,isInforme);
			
			if(designasList!=null && designasList.size()>0){
				Hashtable codigosHashtable = new Hashtable();
				String sqlDesignas = getQueryDesignasPendientesJustificacion(designasList,formulario,codigosHashtable,isInforme);
				paginador = new PaginadorBind(sqlDesignas.toString(),codigosHashtable);
			 }else{
				 paginador = null;
			 } 
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta getDesignasPendientesJustificacionPaginador.");
		}
		return paginador;                        
	}
	private String getQueryDesignasPendientesJustificacion(List<DesignaForm> designasList,InformeJustificacionMasivaForm formulario,Hashtable codigosHashtable,boolean isInforme) throws ClsExceptions{
		
		int contador = 0;
		ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		StringBuffer sqlDesignas = new StringBuffer();
		sqlDesignas.append(" SELECT D.ANIO || '/' || D.CODIGO CODIGODESIGNA, ");
		sqlDesignas.append(" F_SIGA_GETEJG_DESIGNA(:");
		contador++;
		codigosHashtable.put(new Integer(contador),formulario.getIdInstitucion());
		sqlDesignas.append(contador);
		sqlDesignas.append(",d.idturno,d.anio,d.numero) AS EXPEDIENTES, ");
		sqlDesignas.append(" TO_CHAR(D.FECHAENTRADA,'dd/mm/yyyy') FECHADESIGNA, ");
		sqlDesignas.append(" TO_CHAR(D.FECHAENTRADA,'yyyy_mm_dd') FECHAORDEN, ");
		sqlDesignas.append(" D.NUMPROCEDIMIENTO ASUNTO, ");
		sqlDesignas.append(" f_siga_getdefendidosdesigna(:");
		contador++;
		codigosHashtable.put(new Integer(contador),formulario.getIdInstitucion());
		sqlDesignas.append(contador);
		sqlDesignas.append(",d.anio,d.idturno,d.numero,0) AS CLIENTE, ");
		sqlDesignas.append(" D.IDINSTITUCION IDINSTITUCION, ");
		sqlDesignas.append(" D.IDTURNO IDTURNO, ");
		sqlDesignas.append(" D.ANIO ANIO, ");
		sqlDesignas.append(" D.NUMERO NUMERO, ");
		sqlDesignas.append(" D.CODIGO CODIGO, ");
		sqlDesignas.append(" D.IDJUZGADO IDJUZGADO, ");
		sqlDesignas.append(" D.IDINSTITUCION_JUZG IDINSTITUCION_JUZG, ");
		sqlDesignas.append(" D.ESTADO ESTADO, ");
		sqlDesignas.append(" D.RESUMENASUNTO RESUMENASUNTO, ");
		sqlDesignas.append(" DL.IDPERSONA, ");
		sqlDesignas.append(" DL.FECHARENUNCIA, ");
		sqlDesignas.append(" D.IDPROCEDIMIENTO IDPROCEDIMIENTO ");
		sqlDesignas.append(" , (SELECT COUNT(*) FROM SCS_DESIGNASLETRADO SDL ");
		sqlDesignas.append(" WHERE D.IDINSTITUCION = SDL.IDINSTITUCION ");
		sqlDesignas.append(" AND D.ANIO = SDL.ANIO ");
		sqlDesignas.append(" AND D.NUMERO = SDL.NUMERO ");
		sqlDesignas.append(" AND D.IDTURNO = SDL.IDTURNO) CAMBIOLETRADO ");
		final String TIPO_RESOLUCION_RECONOCIDO100 = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.reconocido100");
		final String TIPO_RESOLUCION_RECONOCIDO80 = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.reconocido80");
		//que tenga al menos uno con resolucon favorable
		sqlDesignas.append(", (select count(*) ");
		sqlDesignas.append(" from SCS_EJG EJG, SCS_EJGDESIGNA EJGDES ");
		sqlDesignas.append(" where d.idinstitucion = ejgdes.idinstitucion ");
		sqlDesignas.append(" and d.idturno = ejgdes.idturno ");
		sqlDesignas.append(" and d.anio = ejgdes.aniodesigna ");
		sqlDesignas.append(" and d.numero = ejgdes.numerodesigna ");
		sqlDesignas.append(" and ejgdes.idinstitucion = ejg.idinstitucion ");
		sqlDesignas.append(" and ejgdes.idtipoejg = ejg.idtipoejg ");
		sqlDesignas.append(" and ejgdes.anioejg = ejg.anio ");
		sqlDesignas.append(" and (ejg.idtiporatificacionejg in (");
		sqlDesignas.append(TIPO_RESOLUCION_RECONOCIDO100);
		sqlDesignas.append(",");
		sqlDesignas.append(TIPO_RESOLUCION_RECONOCIDO80);
		sqlDesignas.append(" ) or ejg.idtiporatificacionejg is null)");
		sqlDesignas.append(" and ejgdes.numeroejg = ejg.numero) NUMEJGRESUELTOSFAVORABLES ");
		
		sqlDesignas.append(" FROM SCS_DESIGNA D, SCS_DESIGNASLETRADO DL ");
		sqlDesignas.append(" WHERE D.IDINSTITUCION = DL.IDINSTITUCION ");
		sqlDesignas.append(" AND D.ANIO = DL.ANIO ");
		sqlDesignas.append(" AND D.NUMERO = DL.NUMERO ");
		sqlDesignas.append(" AND D.IDTURNO = DL.IDTURNO ");
		sqlDesignas.append(" AND DL.IDINSTITUCION = :");
		
		contador++;
		codigosHashtable.put(new Integer(contador),formulario.getIdInstitucion());
		sqlDesignas.append(contador);
		
		
		if(formulario.getIdPersona()!=null && !formulario.getIdPersona().equalsIgnoreCase("")){
			sqlDesignas.append(" and DL.IDPERSONA = :");
			contador++;
			codigosHashtable.put(new Integer(contador),formulario.getIdPersona());
			sqlDesignas.append(contador);
		}
		
		if(designasList!=null && designasList.size()>0){
            sqlDesignas.append(" AND (D.IDINSTITUCION,D.ANIO,D.IDTURNO,D.NUMERO) IN ( ");
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
			sqlDesignas.append(" ORDER BY D.FECHAENTRADA,D.IDINSTITUCION, D.ANIO, D.CODIGO, D.SUFIJO");
			
		}
		return sqlDesignas.toString();
		
	}
	
	private PaginadorBind getTodasDesignasJustificacionPaginador(InformeJustificacionMasivaForm formulario,boolean isInforme)throws ClsExceptions,SIGAException {
		PaginadorBind paginador=null;
		try {
			Hashtable codigosHashtable = new Hashtable();
			String queryDesignas = getQueryDesignas(formulario,isInforme, codigosHashtable);
			paginador = new PaginadorBind(queryDesignas,codigosHashtable);
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta getTodasDesignasJustificacionPaginador.");
		}
		return paginador;                        
	}
	public PaginadorBind getDesignasJustificacionPaginador(InformeJustificacionMasivaForm formulario,boolean isInforme) throws ClsExceptions, SIGAException{
		PaginadorBind paginador=null;
		boolean isMostrarJustificacionesPtes = formulario.getMostrarTodas()!=null && formulario.getMostrarTodas().equals("true");
		if(isMostrarJustificacionesPtes){
			paginador = getDesignasPendientesJustificacionPaginador(formulario, isInforme);
		}else{
			paginador = getTodasDesignasJustificacionPaginador(formulario, isInforme);
		}
		return paginador;                        
	}	
	
	private String getQueryDesignasLetradoSinBroza(Integer idInstitucion, String idPersona, String fechaDesde, 
			String fechaHasta, String mostrarTodas, String interesadoNombre,
			String interesadoApellidos, String anio, boolean isInforme,Hashtable codigos)throws ClsExceptions{
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (");
		sql.append(getQueryDesignasLetrado(idInstitucion, idPersona, fechaDesde, fechaHasta, mostrarTodas, 
								interesadoNombre, interesadoApellidos, anio, isInforme,codigos));
		sql.append(" ) where fechaRenuncia is null  or fechaActuacionIni is not null");
		return sql.toString();
		
	}

	private String getQueryDesignasLetrado(Integer idInstitucion, String idPersona, String fechaDesde, 
			String fechaHasta, String mostrarTodas, String interesadoNombre,
			String interesadoApellidos, String anio, boolean isInforme,Hashtable codigos)throws ClsExceptions{
		StringBuffer extra = new StringBuffer("");
		
		
		boolean isMostrarTodas = mostrarTodas!=null && mostrarTodas.equals("on");
		
		
	    int contador=0;

		StringBuffer sql = new StringBuffer("");
		
	    ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		//ReadProperties rp3 = new ReadProperties("SIGA.properties");
		
		GenParametrosAdm paramAdm = new GenParametrosAdm (usrbean);
		//Haria falta meter los parametros en con ClsConstants
        String codExcluirEjgDenegados = paramAdm.getValor (usrbean.getLocation (), "SCS", ClsConstants.GEN_PARAM_EXCLUIR_EJG_DENEGADOS_JUSTIF_LETRADO, "");
		
		
		final String INI_ANTES_2005 = rp3.returnProperty("codigo.general.scsacreditacion.inicio.antes2005");
		final String FIN_ANTES_2005 = rp3.returnProperty("codigo.general.scsacreditacion.fin.antes2005");
		final String INI_DESPUES_2005 = rp3.returnProperty("codigo.general.scsacreditacion.inicio.despues2005");
		final String FIN_DESPUES_2005 = rp3.returnProperty("codigo.general.scsacreditacion.fin.despues2005");
		final String INI_FIN = rp3.returnProperty("codigo.general.scsacreditacion.iniciofin");
		final String INCOMPARECENCIA = rp3.returnProperty("codigo.general.scsacreditacion.iniciofin.incomparecencia");
		final String TRANS_EXTRAJUDICIAL = rp3.returnProperty("codigo.general.scsacreditacion.iniciofin.transextrajudicial");
		
		final String TIPO_ACREDIT_INI = rp3.returnProperty("codigo.general.scstipoacreditacion.inicio");
		final String TIPO_ACREDIT_FIN= rp3.returnProperty("codigo.general.scstipoacreditacion.fin");
		final String TIPO_ACREDIT_INIFIN = rp3.returnProperty("codigo.general.scstipoacreditacion.iniciofin");
		
		final String TIPO_RESOLUCION_DENEGADO = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.denegado");
		
		
		//codigo.general.scsacreditacion.inicio.antes2005=2
		//codigo.general.scsacreditacion.fin.antes2005=3
		//codigo.general.scsacreditacion.inicio.despues2005=10	
		//codigo.general.scsacreditacion.fin.despues2005=11
		sql.append(" select  ");
		sql.append(" d.idinstitucion idinstitucion,");
		sql.append(" d.idturno idturno, ");
		sql.append(" d.anio anio, ");
		sql.append(" d.numero numero, ");
		sql.append(" d.codigo codigo, ");
		sql.append(" d.idjuzgado idjuzgado, ");
		sql.append(" trunc(d.fechaentrada) fechaOrden, ");
		if(isInforme)
			sql.append(" nvl(to_char(d.fechaentrada, 'DD/mm/YYYY'),' ') fechadesigna, ");
		else
			sql.append(" trunc(d.fechaentrada) fechadesigna, ");
		sql.append(" d.estado estado, ");
		//sql.append(" d.resumenasunto asunto, ");//Asi es lo que queria malaga. se cambia porque lo pide gijon
		sql.append(" d.numprocedimiento asunto, ");
		sql.append(" d.RESUMENASUNTO RESUMENASUNTO, ");//ahora lo aniadimos porque lo pide Baleares
		//
		sql.append(" dl.idpersona, ");
		sql.append(" dl.fecharenuncia, ");
		//sql.append(" F_SIGA_GETEJG_DESIGNA(d.idinstitucion, d.idturno, d.anio, d.numero) as expedientes, ");
		//sql.append(" f_siga_getdefendidosdesigna(d.idinstitucion, d.anio, d.idturno, d.numero,1) as cliente, ");
		sql.append(" d.idprocedimiento idprocedimiento, ");
		
		
		sql.append(" d.idinstitucion_juzg idinstitucion_juzg, ");
		sql.append("d.anio");
		sql.append("||'/'");
		sql.append("||");
		sql.append("d.codigo");
		sql.append(" designacion,");
		
		
		//Obtiene el numero de actuaciones del letrado en la designa
		//junto con la fechaRenuncia sirve para habilitar o deshabilitar campos
		sql.append(" (select count(1) ");
		sql.append(" from scs_actuaciondesigna actini, scs_acreditacionprocedimiento acp, scs_acreditacion ac ");       
		sql.append(" where actini.idinstitucion = ");
		
		contador++;
		codigos.put(new Integer(contador),idInstitucion.toString());
		sql.append(":"+contador);
		
		sql.append(" and   actini.idpersonacolegiado = dl.idpersona ");
		sql.append(" and   actini.idturno = d.idturno ");
		sql.append(" and   actini.anio = d.anio ");
		sql.append(" and   actini.numero = d.numero ");
		
		sql.append(" and   acp.idinstitucion = ");
		
		//ultima modificacion
		sql.append(" actini.idinstitucion_proc");
		//sql.append(idInstitucion);
		
		sql.append(" and   acp.idprocedimiento = actini.idprocedimiento ");
		sql.append(" and   acp.idacreditacion = actini.idacreditacion ");
		sql.append(" and   acp.idacreditacion = ac.idacreditacion ");
		sql.append(" and   actini.validada=1 ");
		sql.append(" and   (ac.idtipoacreditacion=");
		
		contador++;
		codigos.put(new Integer(contador),TIPO_ACREDIT_INI);
		sql.append(":"+contador);
		
		sql.append(" or   ac.idtipoacreditacion=");

		contador++;
		codigos.put(new Integer(contador),TIPO_ACREDIT_INIFIN);
		sql.append(":"+contador);
		
		sql.append(")");
		//acreditacion de inicio normal ");        
		//para penal la acreditacion es 2 cuando la mayor entre fechadesigna y fechaactuacion es > 2005, si no es 10 ");
		sql.append(" and   ac.idacreditacion  ");
		
		//lo de arriba lo sustituyo por estp
		sql.append(" in (");

		contador++;
		codigos.put(new Integer(contador),INI_DESPUES_2005);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),INI_ANTES_2005);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),INI_FIN);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),INCOMPARECENCIA);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),TRANS_EXTRAJUDICIAL);
		sql.append(":"+contador);
		
		sql.append(")");
		//Me quedo con la primera, se presupone que solo hay una ");
		sql.append(") as NActuaciones, ");

		
		
		if(isInforme){
			sql.append(" (select nvl(to_char(actini.fechajustificacion, 'DD/mm/YYYY'),' ')");
			sql.append("||'||'||acp.porcentaje");
		}
		else	
			sql.append(" (select trunc(actini.fechajustificacion) ");
		sql.append(" from scs_actuaciondesigna actini, scs_acreditacionprocedimiento acp, scs_acreditacion ac ");       
		sql.append(" where actini.idinstitucion = ");
		
		contador++;
		codigos.put(new Integer(contador),idInstitucion.toString());
		sql.append(":"+contador);
		
		sql.append(" and   actini.idturno = d.idturno ");
		sql.append(" and   actini.anio = d.anio ");
		sql.append(" and   actini.numero = d.numero ");
		
		sql.append(" and   acp.idinstitucion = ");
		
		//ultima modificacion
		sql.append(" actini.idinstitucion_proc");
		//sql.append(idInstitucion);
		
		sql.append(" and   acp.idprocedimiento = actini.idprocedimiento ");
		sql.append(" and   acp.idacreditacion = actini.idacreditacion ");
		sql.append(" and   acp.idacreditacion = ac.idacreditacion ");
		sql.append(" and   actini.validada=1 ");
		sql.append(" and   (ac.idtipoacreditacion=");
		
		contador++;
		codigos.put(new Integer(contador),TIPO_ACREDIT_INI);
		sql.append(":"+contador);
		
		sql.append(" or   ac.idtipoacreditacion=");

		contador++;
		codigos.put(new Integer(contador),TIPO_ACREDIT_INIFIN);
		sql.append(":"+contador);
		
		sql.append(")");
		//acreditacion de inicio normal ");        
		//para penal la acreditacion es 2 cuando la mayor entre fechadesigna y fechaactuacion es > 2005, si no es 10 ");
		sql.append(" and   ac.idacreditacion  ");
		
		//lo de arriba lo sustituyo por estp
		sql.append(" in (");

		contador++;
		codigos.put(new Integer(contador),INI_DESPUES_2005);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),INI_ANTES_2005);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),INI_FIN);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),INCOMPARECENCIA);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),TRANS_EXTRAJUDICIAL);
		sql.append(":"+contador);
		
		sql.append(")");
		//Me quedo con la primera, se presupone que solo hay una ");
		sql.append(" and rownum<2) as acreditacion_ini, ");
		
		
		if(isInforme){
			sql.append(" (select nvl(to_char(actfin.fechajustificacion, 'DD/mm/YYYY'),' ') ");
			sql.append("||'||'||acpfin.porcentaje");
		}
		else
			sql.append(" (select trunc(actfin.fechajustificacion) ");
		sql.append(" from scs_actuaciondesigna actfin, scs_acreditacionprocedimiento acpfin, scs_acreditacion acfin ");       
		sql.append(" where actfin.idinstitucion = ");

		contador++;
		codigos.put(new Integer(contador),idInstitucion.toString());
		sql.append(":"+contador);
		
		sql.append(" and   actfin.idturno = d.idturno ");
		sql.append(" and   actfin.anio = d.anio ");
		sql.append(" and   actfin.numero = d.numero ");
		sql.append(" and   acpfin.idinstitucion =  ");

		contador++;
		codigos.put(new Integer(contador),idInstitucion.toString());
		sql.append(":"+contador);
		
		sql.append(" and   acpfin.idprocedimiento = actfin.idprocedimiento ");
		sql.append(" and   acpfin.idacreditacion = actfin.idacreditacion ");
		sql.append(" and   acpfin.idacreditacion = acfin.idacreditacion ");
		sql.append(" and   actfin.validada=1 ");
		sql.append(" and  ( acfin.idtipoacreditacion=");

		contador++;
		codigos.put(new Integer(contador),TIPO_ACREDIT_FIN);//2 ; // acreditacion de fin normal ");
		sql.append(":"+contador);
		
		sql.append(" or   acfin.idtipoacreditacion=");

		contador++;
		codigos.put(new Integer(contador),TIPO_ACREDIT_INIFIN);//3) ");
		sql.append(":"+contador);
		
		sql.append(")");
		// para penal la acreditacion es 3 cuando la mayor entre fechadesigna y fechaactuacion es > 2005, si no es 11 ");
		sql.append(" and   acfin.idacreditacion "); 
		
		sql.append(" in (");

		contador++;
		codigos.put(new Integer(contador),FIN_DESPUES_2005);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),FIN_ANTES_2005);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),INI_FIN);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),INCOMPARECENCIA);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),TRANS_EXTRAJUDICIAL);
		sql.append(":"+contador);
		
		sql.append(")");
		
		// Me quedo con la primera, se presupone que solo hay una ");
		sql.append(" and rownum<2) as acreditacion_fin, ");   
		
		
		sql.append(" (select trunc(actini.fecha) ");
		sql.append(" from scs_actuaciondesigna actini, scs_acreditacionprocedimiento acp, scs_acreditacion ac ");       
		sql.append(" where actini.idinstitucion = ");
		
		contador++;
		codigos.put(new Integer(contador),idInstitucion.toString());
		sql.append(":"+contador);
		
		sql.append(" and   actini.idturno = d.idturno ");
		sql.append(" and   actini.anio = d.anio ");
		sql.append(" and   actini.numero = d.numero ");
		
		sql.append(" and   acp.idinstitucion = ");
		
		//ultima modificacion
		sql.append(" actini.idinstitucion_proc");
		//sql.append(idInstitucion);
		
		sql.append(" and   acp.idprocedimiento = actini.idprocedimiento ");
		sql.append(" and   acp.idacreditacion = actini.idacreditacion ");
		sql.append(" and   acp.idacreditacion = ac.idacreditacion ");
		sql.append(" and   (ac.idtipoacreditacion=");
		
		contador++;
		codigos.put(new Integer(contador),TIPO_ACREDIT_INI);
		sql.append(":"+contador);
		
		sql.append(" or   ac.idtipoacreditacion=");

		contador++;
		codigos.put(new Integer(contador),TIPO_ACREDIT_INIFIN);
		sql.append(":"+contador);
		
		sql.append(")");
		//acreditacion de inicio normal ");        
		//para penal la acreditacion es 2 cuando la mayor entre fechadesigna y fechaactuacion es > 2005, si no es 10 ");
		sql.append(" and   ac.idacreditacion  ");
		
		//lo de arriba lo sustituyo por estp
		sql.append(" in (");

		contador++;
		codigos.put(new Integer(contador),INI_DESPUES_2005);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),INI_ANTES_2005);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),INI_FIN);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),INCOMPARECENCIA);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),TRANS_EXTRAJUDICIAL);
		sql.append(":"+contador);
		
		sql.append(")");
		//Me quedo con la primera, se presupone que solo hay una ");
		sql.append(" and rownum<2) as fechaActuacionIni, ");
		
		
		sql.append(" (select trunc(actfin.fecha) ");
		sql.append(" from scs_actuaciondesigna actfin, scs_acreditacionprocedimiento acpfin, scs_acreditacion acfin ");       
		sql.append(" where actfin.idinstitucion = ");

		contador++;
		codigos.put(new Integer(contador),idInstitucion.toString());
		sql.append(":"+contador);
		
		sql.append(" and   actfin.idturno = d.idturno ");
		sql.append(" and   actfin.anio = d.anio ");
		sql.append(" and   actfin.numero = d.numero ");
		sql.append(" and   acpfin.idinstitucion =  ");

		contador++;
		codigos.put(new Integer(contador),idInstitucion.toString());
		sql.append(":"+contador);
		
		sql.append(" and   acpfin.idprocedimiento = actfin.idprocedimiento ");
		sql.append(" and   acpfin.idacreditacion = actfin.idacreditacion ");
		sql.append(" and   acpfin.idacreditacion = acfin.idacreditacion ");
		sql.append(" and  ( acfin.idtipoacreditacion=");

		contador++;
		codigos.put(new Integer(contador),TIPO_ACREDIT_FIN);//2 ; // acreditacion de fin normal ");
		sql.append(":"+contador);
		
		sql.append(" or   acfin.idtipoacreditacion=");

		contador++;
		codigos.put(new Integer(contador),TIPO_ACREDIT_INIFIN);//3) ");
		sql.append(":"+contador);
		
		sql.append(")");
		// para penal la acreditacion es 3 cuando la mayor entre fechadesigna y fechaactuacion es > 2005, si no es 11 ");
		sql.append(" and   acfin.idacreditacion "); 
		
		sql.append(" in (");

		contador++;
		codigos.put(new Integer(contador),FIN_DESPUES_2005);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),FIN_ANTES_2005);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),INI_FIN);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),INCOMPARECENCIA);
		sql.append(":"+contador);
		
		sql.append(",");

		contador++;
		codigos.put(new Integer(contador),TRANS_EXTRAJUDICIAL);
		sql.append(":"+contador);
		
		sql.append(")");
		
		// Me quedo con la primera, se presupone que solo hay una ");
		sql.append(" and rownum<2) as fechaActuacionFin ");  
		
		
		
		//Vamos a meter este datos para ver las concurrencias
		if(!isInforme){
			sql.append(",( select count(*)  from scs_actuaciondesigna act2");
			sql.append(" where act2.idinstitucion =  "); 
			
			contador++;
			codigos.put(new Integer(contador),idInstitucion.toString());
			sql.append(":"+contador);
			
			sql.append(" and act2.idturno = d.idturno ");
			sql.append(" and act2.anio = d.anio ");
			sql.append(" and act2.numero = d.numero ");
			sql.append(" and (act2.fechajustificacion is null or act2.validada='0')) AS NUMJUSTIFICACIONES ");
			
		}
		sql.append(", F_SIGA_GETEJG_DESIGNA(:");
		contador++;
		codigos.put(new Integer(contador),idInstitucion.toString());
		sql.append(contador);
		sql.append(",d.idturno,d.anio,d.numero) AS EXPEDIENTES ");
		
		sql.append(" ,f_siga_getdefendidosdesigna(:");
		contador++;
		codigos.put(new Integer(contador),idInstitucion.toString());
		sql.append(contador);
		sql.append(",d.anio,d.idturno,d.numero,1) AS CLIENTE ");
		
		sql.append(" from scs_designa d, scs_designasletrado dl");
		
		sql.append(" where d.idinstitucion = dl.idinstitucion ");
		sql.append(" and d.anio = dl.anio ");
		
		if (anio != null && !anio.equals("")) {
			sql.append(" and d.anio = :");	
			contador++;
			codigos.put(new Integer(contador),anio);
			sql.append(contador);
		}
		
		sql.append(" and d.numero = dl.numero ");
		sql.append(" and d.idturno = dl.idturno ");
		sql.append(" and dl.idinstitucion = :");
		
		contador++;
		codigos.put(new Integer(contador),idInstitucion.toString());
		sql.append(contador);
		
		
		
		if(idPersona!=null && !idPersona.equalsIgnoreCase("")){
			sql.append(" and dl.idpersona = :");
			contador++;
			codigos.put(new Integer(contador),idPersona);
			sql.append(contador);
		}
		
		
		sql.append(" and (dl.fechadesigna  is null or dl.fechadesigna = (select max(aux.fechadesigna) ");
		sql.append(" from scs_designasletrado aux ");
		sql.append(" where aux.idinstitucion = dl.idinstitucion ");
		sql.append(" AND dl.idinstitucion = :");
		contador++;
		codigos.put(new Integer(contador),idInstitucion.toString());
		sql.append(contador);
		

		sql.append(" and aux.idturno = dl.idturno ");
		sql.append(" and aux.anio = dl.anio ");
		sql.append(" and aux.numero = dl.numero ");
		if(idPersona!=null && !idPersona.equalsIgnoreCase("")){
			sql.append(" and aux.idpersona = dl.idpersona ");
		}		
		sql.append(" and trunc(aux.fechadesigna) <= trunc(sysdate))) ");
		if (fechaDesde != null && !fechaDesde.equals("")) {
			extra.append(" and trunc(d.fechaentrada) >= :");
			
			contador++;
			codigos.put(new Integer(contador),fechaDesde);
			extra.append(contador);
			 
		}
		
		if (fechaHasta != null && !fechaHasta.equals("")) {
			extra.append(" and trunc(d.fechaentrada) <=:");
			
			contador++;
			codigos.put(new Integer(contador),fechaHasta);
			extra.append(contador);
			 
		}
		sql.append(extra);

		
		sql.append(" and d.estado <> 'A' ");
		if((interesadoApellidos != null && !interesadoApellidos.equalsIgnoreCase("")) 
				&& (interesadoNombre != null && !interesadoNombre.equalsIgnoreCase(""))){
			sql.append(" and UPPER(f_siga_getdefendidosdesigna(d.idinstitucion, d.anio, d.idturno, d.numero,0)) like ");
			contador++;
			StringBuffer aux = new StringBuffer();
			aux.append("%");
			aux.append(interesadoNombre.toUpperCase());
			aux.append(" ");
			aux.append(interesadoApellidos.toUpperCase());
			aux.append("%");
			codigos.put(new Integer(contador),aux.toString());
			
			sql.append(":"+contador);
		}else if (interesadoApellidos != null && !interesadoApellidos.equalsIgnoreCase("") && (interesadoNombre==null||interesadoNombre.equalsIgnoreCase("")) ){
		    sql.append(" and UPPER(f_siga_getdefendidosdesigna(d.idinstitucion,  d.anio,d.idturno, d.numero,0)) like ");
			contador++;
			StringBuffer aux = new StringBuffer();
		    aux.append("%");
		    aux.append(interesadoApellidos.toUpperCase());
		    aux.append("%");
			codigos.put(new Integer(contador),aux.toString());
		    
			sql.append(":"+contador);
		}else if ((interesadoApellidos == null||interesadoApellidos.equalsIgnoreCase("")) && interesadoNombre!=null && !interesadoNombre.equalsIgnoreCase("")){
		    sql.append(" and UPPER(f_siga_getdefendidosdesigna(d.idinstitucion, d.anio, d.idturno, d.numero,0)) like ");
		    contador++;
			StringBuffer aux = new StringBuffer();
		    aux.append("%");
		    aux.append(interesadoNombre.toUpperCase());
		    aux.append("%");
		    codigos.put(new Integer(contador),aux.toString());
		    
			sql.append(":"+contador);
		}
		//no salen los que ya están finalizados
		if(!isMostrarTodas){
			sql.append(" and d.estado <> 'F' ");
		    // no salen los que ya están justificados totalmente ");
		    
		}
		//tODOS MENOS DENEGADO
		if(codExcluirEjgDenegados!=null && codExcluirEjgDenegados.equals(ClsConstants.DB_TRUE)){
			sql.append(" and not exists (select 1 ");
			sql.append(" from SCS_EJG EJG,SCS_EJGDESIGNA EJGDES ");
	
			sql.append(" where d.idinstitucion=ejgdes.idinstitucion ");
	
			sql.append(" and d.idturno=ejgdes.idturno ");
			sql.append(" and d.anio=ejgdes.aniodesigna ");
			sql.append(" and d.numero=ejgdes.numerodesigna ");
			sql.append(" and ejgdes.idinstitucion=ejg.idinstitucion ");
			sql.append(" and ejgdes.idtipoejg=ejg.idtipoejg ");
			sql.append(" and ejgdes.anioejg=ejg.anio ");
			sql.append(" and ejgdes.numeroejg=ejg.numero ");
			sql.append(" and (ejg.idtiporatificacionejg =  ");
			   //
			sql.append(TIPO_RESOLUCION_DENEGADO);
	
			sql.append(" or ejg.idtiporatificacionejg is null)) ");
		}

		
		
		return sql.toString();
		
	}
	
	
	
	private Vector getDesignasLetrado (Integer idInstitucion, String idPersona, String fechaDesde, 
			String fechaHasta, String mostrarTodas, String interesadoNombre,
			String interesadoApellidos,boolean isInforme)  throws ClsExceptions, SIGAException 
	{
	    Hashtable codigos = new Hashtable();
		String sql = getQueryDesignasLetrado( idInstitucion,  idPersona,  fechaDesde, 
			 fechaHasta,  mostrarTodas,  interesadoNombre,	 interesadoApellidos, null, isInforme, codigos);
	
		return this.selectGenericoBind(sql, codigos);
	}
	private Vector getDesignasLetradoJustificacion (InformeJustificacionMasivaForm formulario,Acumulador	acumula,boolean isInforme)  throws ClsExceptions, SIGAException 
	{
		boolean isMostrarJustificacionesPtes = formulario.getMostrarTodas()!=null && formulario.getMostrarTodas().equals("true");
		
		if(isMostrarJustificacionesPtes){
			
			
			List<DesignaForm> designasList = getDesignasJustificacion(formulario,acumula,isInforme);
			
			if(designasList!=null && designasList.size()>0){
				Hashtable codigosHashtable = new Hashtable();
				String sqlDesignas = getQueryDesignasPendientesJustificacion(designasList,formulario,codigosHashtable,isInforme);
				return this.selectGenericoBind(sqlDesignas, codigosHashtable);
			}else 
				return new Vector();
		}else{
			Hashtable codigosHashtable = new Hashtable();
			String sqlDesignas = getQueryDesignas(formulario,isInforme,codigosHashtable);
			return this.selectGenericoBind(sqlDesignas, codigosHashtable);
			
		}
		
	
		
	}
	
	public Hashtable getPersonasSalidaJustificacion(Integer idInstitucion, String idPersona, String fechaDesde, 
			String fechaHasta, String mostrarTodas, String interesadoNombre,
			String interesadoApellidos,boolean isInforme) throws ClsExceptions  
	{	 
		
		Hashtable htPersona = new Hashtable();
		//Vector vSalida = null;
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		Hashtable htAcumuladorJuzgados = new Hashtable();
		Hashtable htAcumuladorProcedimientos = new Hashtable();
		Hashtable htAcumuladorTurnos = new Hashtable();
		TreeMap tmDesignas = null;
		
		try {
			//vSalida = new Vector();	
			ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: INICIO Consulta Justificacion",10);
			Vector vDesigna = getDesignasLetrado(idInstitucion, idPersona, fechaDesde, 
					fechaHasta, mostrarTodas, interesadoNombre,	interesadoApellidos,isInforme); 
			ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: FIN Consulta Justificacion",10);
			for (int j = 0; j < vDesigna.size(); j++) {
				
				Hashtable registro = (Hashtable) vDesigna.get(j);
				idPersona = (String)registro.get("IDPERSONA");
				String numeroDesigna = (String)registro.get("NUMERO");
				String codigoDesigna = (String)registro.get("CODIGO");
				String anioDesigna = (String)registro.get("ANIO");
				String idTurno  = (String)registro.get("IDTURNO");
				String idProcedimiento  = (String)registro.get("IDPROCEDIMIENTO");
				String fechaOrden  = (String)registro.get("FECHAORDEN");
				
				//helperInformes.completarHashSalida(registro,getNumeroJustificacionesDesignaSalida(idInstitucion.toString(), 
					//	idTurno,anioDesigna,numeroDesigna));
				
				Vector vTurno = geTurno(idInstitucion.toString(), 
						idTurno,htAcumuladorTurnos,helperInformes);
				helperInformes.completarHashSalida(registro,vTurno);
				
				
				if(idProcedimiento!=null && !idProcedimiento.equalsIgnoreCase("")){
					Vector vProcedimiento = getProcedimiento(idInstitucion.toString(), 
							idProcedimiento,htAcumuladorProcedimientos,helperInformes);
					helperInformes.completarHashSalida(registro,vProcedimiento);
				}else{
					registro.put("PROCEDIMIENTO","");
					registro.put("CATEGORIA","");
					registro.put("IDJURISDICCION","");
					
				}
				
				
				String idJuzgado = (String)registro.get(ScsDesignaBean.C_IDJUZGADO);
				String idInstitucionJuzgado  = (String)registro.get("IDINSTITUCION_JUZG");
				if(idJuzgado!=null && !idJuzgado.equalsIgnoreCase("")){
					Vector vJuzgado = getJuzgado(idInstitucionJuzgado.toString(),idJuzgado,htAcumuladorJuzgados, helperInformes);
					helperInformes.completarHashSalida(registro,vJuzgado);
					String descJuzgado = (String)registro.get("JUZGADO"); 
					registro.put("DESC_JUZGADO",descJuzgado);
					if(isInforme)
						registro.put("IDJUZGADO",descJuzgado);
				}else{
					registro.put("DESC_JUZGADO","");
					registro.put("IDJUZGADO","");
					
				}
	
				Hashtable htFuncion = new Hashtable();
				htFuncion.put(new Integer(1), idInstitucion.toString());
				htFuncion.put(new Integer(2), idTurno);
				htFuncion.put(new Integer(3), anioDesigna);
				htFuncion.put(new Integer(4), numeroDesigna);
				//helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
					//	htFuncion, "F_SIGA_GETEJG_DESIGNA", "EXPEDIENTES"));
				
				htFuncion.put(new Integer(5), "1");
				//helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
					//	htFuncion, "f_siga_getdefendidosdesigna", "CLIENTE"));
				
//				if(isInforme)
//					formatearHashInforme(registro,idInstitucion);
//				
				
				
				String keyTreeMap = fechaOrden+anioDesigna+codigoDesigna+idTurno+numeroDesigna;
				if(!htPersona.containsKey(idPersona)){
					/*Vector vPersona = admPersona.getDatosPersonaTag(idInstitucion.toString(),idPersona);
					Hashtable htDatosPersona = (Hashtable) vPersona.get(0);
					String nombre = (String)htDatosPersona.get(CenPersonaBean.C_NOMBRE);
					String nColegiado = (String)htDatosPersona.get(CenColegiadoBean.C_NCOLEGIADO);
					registro.put(CenPersonaBean.C_NOMBRE,nombre);
					registro.put(CenColegiadoBean.C_NCOLEGIADO,nColegiado);
					
					
					helperInformes.completarHashSalida(registro, getDireccionLetradoSalida(idPersona, idInstitucion.toString()));*/
					if(isInforme)
						helperInformes.completarHashSalida(registro, getLetradoSalida(idPersona, idInstitucion.toString()));
					
					tmDesignas = new TreeMap();
					//vRows = new Vector();
				}else{
					//vRows = (Vector) htPersona.get(idPersona);
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
				
				//vRows.add(registro);
				tmDesignas.put(keyTreeMap, registro);
				//htPersona.put(idPersona,vRows);
				htPersona.put(idPersona,tmDesignas);
				
				
				
			}
			

		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getPersonasSalidaJustificacion");
		}
		return htPersona;
		
		
		
	}
	
	
	
	public Hashtable getPersonasSalidaInformeJustificacion(InformeJustificacionMasivaForm formulario,boolean isInforme) throws ClsExceptions  
	{	 
		
		Hashtable htPersona = new Hashtable();
		//Vector vSalida = null;
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		Hashtable htAcumuladorJuzgados = new Hashtable();
		Hashtable htAcumuladorProcedimientos = new Hashtable();
		Hashtable htAcumuladorTurnos = new Hashtable();
		Hashtable htAcumuladorActuaciones = new Hashtable();
		TreeMap tmDesignas = null;
		
		try {
			//vSalida = new Vector();	
			ClsLogging.writeFileLog(Calendar.getInstance().getTimeInMillis() + ",==> SIGA: INICIO Consultas Justificacion",10);
			Acumulador acumula = new Acumulador();
			Vector vDesigna = getDesignasLetradoJustificacion(formulario,acumula,isInforme);
			
			for (int j = 0; j < vDesigna.size(); j++) {
				
				Hashtable registro = (Hashtable) vDesigna.get(j);
				String idPersona = (String)registro.get("IDPERSONA");
				Integer idInstitucion = Integer.parseInt((String)registro.get("IDINSTITUCION"));
				String numeroDesigna = (String)registro.get("NUMERO");
				String codigoDesigna = (String)registro.get("CODIGO");
				String anioDesigna = (String)registro.get("ANIO");
				String idTurno  = (String)registro.get("IDTURNO");
				String idProcedimiento  = (String)registro.get("IDPROCEDIMIENTO");
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
				List<DesignaForm> designaList = getDesignaList(formulario, registro, acumula, isInforme);
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
	
	/*
	
	private Hashtable formatearHashInforme(Hashtable htRows,Integer idInstitucion)throws ClsExceptions{
		
		//Vemos si esta finalizado y le metemos el campo baja=X
	
	
		String estado = (String)htRows.get(ScsDesignaBean.C_ESTADO);
		if (estado != null && estado.equals("F"))
			htRows.put("BAJA","X");
		else
			htRows.put("BAJA"," ");
		//Sacamos la descripcion de los juzgado. Habria que preguntar si esto 
		//es mejor que meter la join en la query. Respuesta: Seria mejor que trabaje oracle. 
		//Lo voy a dejar asi por si hay cambios
		
		//Las fecha de acreditacion vienen concatenadas dd/mm/aaa|porcentaje
		boolean isPendiente = false;
		String fechaAcreditacioIni = (String)htRows.get("ACREDITACION_INI");
		int porc = 0;
		if(fechaAcreditacioIni!=null && !fechaAcreditacioIni.equalsIgnoreCase("")){
			int index = fechaAcreditacioIni.indexOf("||");
			String fechaRealAcreditacioIni = fechaAcreditacioIni.substring(0,index);
			htRows.put("ACREDITACION_INI",fechaRealAcreditacioIni);
			String porcentaje = fechaAcreditacioIni.substring(index+2);
			porc += Integer.parseInt(porcentaje);
			
			isPendiente = true;
		}
		String fechaAcreditacioFin = (String)htRows.get("ACREDITACION_FIN");
		if(fechaAcreditacioFin!=null && !fechaAcreditacioFin.equalsIgnoreCase("")){
			int index = fechaAcreditacioFin.indexOf("||");
			String fechaRealAcreditacioFin = fechaAcreditacioFin.substring(0,index);
			htRows.put("ACREDITACION_FIN",fechaRealAcreditacioFin);
			String porcentaje = null;
			//pongo esto por si no viene con fecha porcentaje. Seria un error!
			if(fechaAcreditacioFin.length()>index+2){
				porcentaje = fechaAcreditacioFin.substring(index+2);
				porc += Integer.parseInt(porcentaje);
			}
			
			
		}
		//Si es de inicio y fin trae el porcentaje(100) en cada justificacion por lo que sera 200.pongo <100 y evito problemas
		if(porc>=100)
			htRows.put("PENDIENTE","");
		// parche 
		// if(100==porc)
		//	htRows.put("PENDIENTE","");
		else
			htRows.put("PENDIENTE",String.valueOf(100-porc)+"% PEND");
		if(!isPendiente)
			htRows.put("PENDIENTE","100% PEND");
	
	return htRows;
	
}*/
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
	public Integer obtenerColegiadoDesignadoEnFecha(ScsDesignaBean sdb, String fecha) throws ClsExceptions {
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
		    	return new Integer((String)((Hashtable)vector.get(0)).get("IDPERSONA"));
		    }

			return null;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre obtenerColegiadoDesignadoEnFecha()");
		}

	}
	
	
	public List<DesignaForm> getDesignasJustificacion(InformeJustificacionMasivaForm formulario,Acumulador acumula,boolean isInforme) throws ClsExceptions  
	{	 
		
		Hashtable htPersona = new Hashtable();
		boolean isMostrarJustificacionesPtes = formulario.getMostrarTodas()!=null && formulario.getMostrarTodas().equals("true");
		
		
		
		//Vector vSalida = null;
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		Hashtable htAcumuladorJuzgados = acumula.getAcumuladorJuzgadosHashtable();
		Hashtable htAcumuladorProcedimientos = acumula.getAcumuladorProcedimientosHashtable();
		Hashtable htAcumuladorTurnos = acumula.getAcumuladorTurnosHashtable();
		TreeMap tmDesignas = null;
		List<Hashtable> designaList = null;
		List<DesignaForm> designaFormList = null;
		ScsActuacionDesignaAdm admActuacionDesignaAdm = new ScsActuacionDesignaAdm(usrbean);
		String idPersona = formulario.getIdPersona();
		
		try {
			//vSalida = new Vector();	
			designaList = getDesignas(formulario,isInforme); 
			DesignaForm designaForm = null;
			designaFormList = new ArrayList<DesignaForm>();
			Iterator iteDesignas = designaList.iterator();
			
			while (iteDesignas.hasNext()) {
				Hashtable registro =  (Hashtable) iteDesignas.next();
				designaForm = new DesignaForm();
				String numEjgResultosFavolables  = (String)registro.get("NUMEJGRESUELTOSFAVORABLES");
				designaForm.setNumEjgResueltosFavorables(Integer.parseInt(numEjgResultosFavolables));
				
				idPersona = (String)registro.get("IDPERSONA");
				String idProcedimiento  = (String)registro.get("IDPROCEDIMIENTO");
				designaForm.setIdPersona(idPersona);
				designaForm.setNumero((String)registro.get("NUMERO"));
				designaForm.setAnio((String)registro.get("ANIO"));
				designaForm.setIdInstitucion(formulario.getIdInstitucion());
				designaForm.setIdTurno((String)registro.get("IDTURNO"));

				designaForm.setCodigoDesigna((String)registro.get("CODIGODESIGNA"));
				designaForm.setEjgs((String)registro.get("EXPEDIENTES"));
				designaForm.setFecha((String)registro.get("FECHADESIGNA"));
				designaForm.setAsunto((String)registro.get("ASUNTO"));
				designaForm.setClientes((String)registro.get("CLIENTE"));

				String estado = (String)registro.get("ESTADO");
				designaForm.setEstado(estado);
				designaForm.setBaja(estado!=null&&(estado.equals("F")||estado.equals("A"))?"1":"0");
				
				
				Vector vTurno = geTurno(designaForm.getIdInstitucion().toString(), 
						designaForm.getIdTurno(),htAcumuladorTurnos,helperInformes);
				helperInformes.completarHashSalida(registro,vTurno);
				
				//El turno es quiene tiene si se validan las justificaciones
				String validarJustificaciones = (String)registro.get("VALIDARJUSTIFICACIONES");
				designaForm.setActuacionValidarJustificaciones(validarJustificaciones!=null?validarJustificaciones:"S");
				designaForm.setActuacionRestriccionesActiva((String)registro.get("ACTIVARRETRICCIONACREDIT"));
				designaForm.setActuacionPermitidaLetrado((String)registro.get("LETRADOACTUACIONES"));
				String cambioLetrado = (String)registro.get("CAMBIOLETRADO");
				designaForm.setCambioLetrado(cambioLetrado!=null&&Integer.parseInt(cambioLetrado)>1?"S":"N");
				
				
				
				designaForm.setIdProcedimiento(idProcedimiento);
				
				if(idProcedimiento!=null && !idProcedimiento.equalsIgnoreCase("")){
					Vector vProcedimiento = getProcedimiento(formulario.getIdInstitucion(), 
							idProcedimiento,htAcumuladorProcedimientos,helperInformes);
					helperInformes.completarHashSalida(registro,vProcedimiento);
				}else{
					registro.put("PROCEDIMIENTO","");
					registro.put("CATEGORIA","");
					registro.put("IDJURISDICCION","");
					
				}
				designaForm.setDescripcionProcedimiento((String)registro.get("PROCEDIMIENTO"));
				designaForm.setCategoria((String)registro.get("CATEGORIA"));
				designaForm.setIdJurisdiccion((String)registro.get("IDJURISDICCION"));
				
				String idJuzgado = (String)registro.get(ScsDesignaBean.C_IDJUZGADO);
				String idInstitucionJuzgado  = (String)registro.get("IDINSTITUCION_JUZG");
				if(idJuzgado!=null && !idJuzgado.equalsIgnoreCase("")){
					designaForm.setIdJuzgado(idJuzgado);
					Vector vJuzgado = getJuzgado(idInstitucionJuzgado.toString(),idJuzgado,htAcumuladorJuzgados, helperInformes);
					helperInformes.completarHashSalida(registro,vJuzgado);
					String descJuzgado = (String)registro.get("JUZGADO"); 
					registro.put("DESC_JUZGADO",descJuzgado);
					if(isInforme)
						registro.put("IDJUZGADO",descJuzgado);
				}else{
					registro.put("DESC_JUZGADO","");
					registro.put("IDJUZGADO","");
					
				}
				designaForm.setJuzgado((String)registro.get("DESC_JUZGADO"));
				
				//seteamos las actuaciones de las designas
				designaForm.setMultiplesComplementos((String)registro.get("COMPLEMENTO"));
				if(designaForm.getNumEjgResueltosFavorables()>0 ||formulario.isPermitirSinResolucionJustifLetrado()){
					admActuacionDesignaAdm.setActuacionesDesignas(designaForm,isMostrarJustificacionesPtes);
					designaForm.setRowSpan();
					
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
	
	public List<DesignaForm> getDesignaList(InformeJustificacionMasivaForm formulario,Hashtable designaHashtable,Acumulador acumula,boolean isInforme) throws ClsExceptions  
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
				
				String numEjgResultosFavolables  = (String)designaHashtable.get("NUMEJGRESUELTOSFAVORABLES");
				designaForm.setNumEjgResueltosFavorables(Integer.parseInt(numEjgResultosFavolables));
				//Hashtable registro = designaList.get(j);
				idPersona = (String)designaHashtable.get("IDPERSONA");
				String idProcedimiento  = (String)designaHashtable.get("IDPROCEDIMIENTO");
				designaForm.setIdPersona(idPersona);
				designaForm.setNumero((String)designaHashtable.get("NUMERO"));
				designaForm.setAnio((String)designaHashtable.get("ANIO"));
				designaForm.setIdInstitucion(formulario.getIdInstitucion());
				designaForm.setIdTurno((String)designaHashtable.get("IDTURNO"));

				designaForm.setCodigoDesigna((String)designaHashtable.get("CODIGODESIGNA"));
				designaForm.setEjgs((String)designaHashtable.get("EXPEDIENTES"));
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
				String cambioLetrado = (String)designaHashtable.get("CAMBIOLETRADO");
				designaForm.setCambioLetrado(cambioLetrado!=null&&Integer.parseInt(cambioLetrado)>1?"S":"N");
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
				designaForm.setMultiplesComplementos((String)designaHashtable.get("COMPLEMENTO"));
				if(designaForm.getNumEjgResueltosFavorables()>0||formulario.isPermitirSinResolucionJustifLetrado()){
					admActuacionDesignaAdm.setActuacionesDesignas(designaForm,isMostrarJustificacionesPtes);
					designaForm.setRowSpan();
					
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
					designaForm.setRowSpan();
					designaFormList.add(designaForm);
				}
				
				
	
				
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getDesignasJustificacion");
		}
		return designaFormList;
	}
	
	
	
	
	
	private List<Hashtable> getDesignas(InformeJustificacionMasivaForm formulario,boolean isInforme)  throws ClsExceptions, SIGAException 
	{
	    Hashtable codigos = new Hashtable();
		String sql = getQueryDesignas(formulario,isInforme, codigos);
	
		return (Vector<Hashtable>)this.selectGenericoBind(sql, codigos);
	}
	
	private String getQueryDesignas(InformeJustificacionMasivaForm formulario, boolean isInforme,Hashtable codigos)throws ClsExceptions{
		StringBuffer extra = new StringBuffer("");
		
		boolean isMostrarJustificacionesPtes = formulario.getMostrarTodas()!=null && formulario.getMostrarTodas().equals("true");
		
		
	    int contador=0;

		StringBuffer sql = new StringBuffer("");
		
	    ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		//ReadProperties rp3 = new ReadProperties("SIGA.properties");
		
		GenParametrosAdm paramAdm = new GenParametrosAdm (usrbean);
		
		
		
		
		
		sql.append(" SELECT * from (");
		sql.append(" SELECT TO_CHAR(D.FECHAENTRADA,'dd/mm/yyyy') FECHADESIGNA, ");
		sql.append(" TO_CHAR(D.FECHAENTRADA,'yyyy_mm_dd') FECHAORDEN, ");
		
		if(!isMostrarJustificacionesPtes){
			sql.append("D.ANIO || '/' || D.CODIGO CODIGODESIGNA,");
			sql.append(" F_SIGA_GETEJG_DESIGNA(:");
			contador++;
			codigos.put(new Integer(contador),formulario.getIdInstitucion());
			sql.append(contador);
			sql.append(",d.idturno,d.anio,d.numero) AS EXPEDIENTES, ");
			sql.append(" D.NUMPROCEDIMIENTO ASUNTO, ");
			sql.append(" f_siga_getdefendidosdesigna(:");
			contador++;
			codigos.put(new Integer(contador),formulario.getIdInstitucion());
			sql.append(contador);
			sql.append(",d.anio,d.idturno,d.numero,0) AS CLIENTE, ");
			
			sql.append(" D.RESUMENASUNTO RESUMENASUNTO, ");
			sql.append(" DL.FECHARENUNCIA, ");
		}//else{
			
		final String TIPO_RESOLUCION_RECONOCIDO100 = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.reconocido100");
		final String TIPO_RESOLUCION_RECONOCIDO80 = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.reconocido80");
		//que tenga al menos uno con resolucon favorable
		sql.append(" (select count(*) ");
    	sql.append(" from SCS_EJG EJG, SCS_EJGDESIGNA EJGDES ");
    	sql.append(" where d.idinstitucion = ejgdes.idinstitucion ");
    	sql.append(" and d.idturno = ejgdes.idturno ");
    	sql.append(" and d.anio = ejgdes.aniodesigna ");
    	sql.append(" and d.numero = ejgdes.numerodesigna ");
    	sql.append(" and ejgdes.idinstitucion = ejg.idinstitucion ");
    	sql.append(" and ejgdes.idtipoejg = ejg.idtipoejg ");
    	sql.append(" and ejgdes.anioejg = ejg.anio ");
    	sql.append(" and (ejg.idtiporatificacionejg in (");
    	sql.append(TIPO_RESOLUCION_RECONOCIDO100);
    	sql.append(",");
    	sql.append(TIPO_RESOLUCION_RECONOCIDO80);
    	sql.append(" ) or ejg.idtiporatificacionejg is null)");
    	sql.append(" and ejgdes.numeroejg = ejg.numero) NUMEJGRESUELTOSFAVORABLES, ");
			
		//}
		sql.append(" D.IDINSTITUCION IDINSTITUCION, ");
		sql.append(" D.IDTURNO IDTURNO, ");
		sql.append(" D.ANIO ANIO, ");
		sql.append(" D.NUMERO NUMERO, ");
		
		sql.append(" D.IDJUZGADO IDJUZGADO, ");
		sql.append(" D.IDINSTITUCION_JUZG IDINSTITUCION_JUZG, ");
		sql.append(" D.ESTADO ESTADO, ");
		sql.append(" D.IDPROCEDIMIENTO IDPROCEDIMIENTO, ");
		sql.append(" D.FECHAENTRADA, D.CODIGO, D.SUFIJO, ");
		sql.append(" DL.IDPERSONA ");
		
		
		sql.append(" , (SELECT COUNT(*) FROM SCS_DESIGNASLETRADO SDL ");
		sql.append(" WHERE D.IDINSTITUCION = SDL.IDINSTITUCION ");
		sql.append(" AND D.ANIO = SDL.ANIO ");
		sql.append(" AND D.NUMERO = SDL.NUMERO ");
		sql.append(" AND D.IDTURNO = SDL.IDTURNO) CAMBIOLETRADO ");

		
		
		sql.append(" FROM SCS_DESIGNA D, SCS_DESIGNASLETRADO DL ");
		sql.append(" WHERE D.IDINSTITUCION = DL.IDINSTITUCION ");
		sql.append(" AND D.ANIO = DL.ANIO ");
		sql.append(" AND D.NUMERO = DL.NUMERO ");
		sql.append(" AND D.IDTURNO = DL.IDTURNO ");
		sql.append(" AND DL.IDINSTITUCION = :");
		
		contador++;
		codigos.put(new Integer(contador),formulario.getIdInstitucion());
		sql.append(contador);
		//sql.append("    and d.codigo in ('00164') ");
		
		//sql.append("    and d.codigo in ('00157','00158','00211','00212') ");
		
		if(formulario.getIdPersona()!=null && !formulario.getIdPersona().equalsIgnoreCase("")){
			sql.append(" and DL.IDPERSONA = :");
			contador++;
			codigos.put(new Integer(contador),formulario.getIdPersona());
			sql.append(contador);
		}
		if (formulario.getAnio() != null && !formulario.getAnio().equals("")) {
			sql.append(" and D.ANIO = :");	
			contador++;
			codigos.put(new Integer(contador),formulario.getAnio());
			sql.append(contador);
		}
		if(isMostrarJustificacionesPtes){
			sql.append(" AND D.ESTADO NOT IN ('A','F')  ");
			sql.append(" AND (NOT EXISTS (SELECT * ");
			sql.append(" FROM SCS_ACTUACIONDESIGNA ACT ");
			sql.append(" WHERE ACT.IDINSTITUCION = D.IDINSTITUCION ");
			sql.append(" AND ACT.IDTURNO = D.IDTURNO ");
			sql.append(" AND ACT.ANIO = D.ANIO ");
			sql.append(" AND ACT.NUMERO = D.NUMERO) ");
			sql.append(" OR F_SIGA_GETACREDITACIONESPTES(D.IDINSTITUCION, ");
			sql.append(" D.IDTURNO, ");
			sql.append(" D.ANIO, ");
			sql.append(" D.NUMERO) > 0 ");
			sql.append(" ) ");








		}else{
			//Haria falta meter los parametros en con ClsConstants

			/*and not ((select count(*)
            from SCS_EJG EJG, SCS_EJGDESIGNA EJGDES
           where d.idinstitucion = ejgdes.idinstitucion
             and d.idturno = ejgdes.idturno
             and d.anio = ejgdes.aniodesigna
             and d.numero = ejgdes.numerodesigna
             and ejgdes.idinstitucion = ejg.idinstitucion
             and ejgdes.idtipoejg = ejg.idtipoejg
             and ejgdes.anioejg = ejg.anio
             and ejgdes.numeroejg = ejg.numero) > 0 and

     --... el numero de ejgs con ratificacion 3...
             (select count(*)
            from SCS_EJG EJG, SCS_EJGDESIGNA EJGDES
           where d.idinstitucion = ejgdes.idinstitucion
             and d.idturno = ejgdes.idturno
             and d.anio = ejgdes.aniodesigna
             and d.numero = ejgdes.numerodesigna
             and ejgdes.idinstitucion = ejg.idinstitucion
             and ejgdes.idtipoejg = ejg.idtipoejg
             and ejgdes.anioejg = ejg.anio
             and ejgdes.numeroejg = ejg.numero
             and ejg.idtiporatificacionejg = 3) = 
    --... es igual al numero de ejgs relacionados
    (select count(*)
            from SCS_EJG EJG, SCS_EJGDESIGNA EJGDES
           where d.idinstitucion = ejgdes.idinstitucion
             and d.idturno = ejgdes.idturno
             and d.anio = ejgdes.aniodesigna
             and d.numero = ejgdes.numerodesigna
             and ejgdes.idinstitucion = ejg.idinstitucion
             and ejgdes.idtipoejg = ejg.idtipoejg
             and ejgdes.anioejg = ejg.anio
             and ejgdes.numeroejg = ejg.numero))

             )*/

			String codExcluirEjgDenegados = paramAdm.getValor (usrbean.getLocation (), "SCS", ClsConstants.GEN_PARAM_EXCLUIR_EJG_DENEGADOS_JUSTIF_LETRADO, "");
			String codExcluirEjgArchivo = paramAdm.getValor (usrbean.getLocation (), "SCS", ClsConstants.GEN_PARAM_EXCLUIR_EJG_ARCHIVO_JUSTIF_LETRADO, "");
			String codExcluirEjgPtesCAJG = paramAdm.getValor (usrbean.getLocation (), "SCS", ClsConstants.GEN_PARAM_EXCLUIR_EJG_PENDIENTES_CAJG_JUSTIF_LETRADO, "");
			String codExcluirEjgSinResolucion = paramAdm.getValor (usrbean.getLocation (), "SCS", ClsConstants.GEN_PARAM_EXCLUIR_EJG_SIN_RESOLUCION_JUSTIF_LETRADO, "");
			String codExcluirSinEjg = paramAdm.getValor (usrbean.getLocation (), "SCS", ClsConstants.GEN_PARAM_EXCLUIR_SIN_EJG_JUSTIF_LETRADO, "");
			StringBuffer excluirEjs = new StringBuffer("");
			if(codExcluirEjgDenegados!=null && codExcluirEjgDenegados.equals(ClsConstants.DB_TRUE)){
				final String TIPO_RESOLUCION_DENEGADO = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.denegado");
				excluirEjs.append(TIPO_RESOLUCION_DENEGADO);
				excluirEjs.append(",");
			}
			if(codExcluirEjgArchivo!=null && codExcluirEjgArchivo.equals(ClsConstants.DB_TRUE)){
				final String TIPO_RESOLUCION_ARCHIVO = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.archivo");
				excluirEjs.append(TIPO_RESOLUCION_ARCHIVO);
				excluirEjs.append(",");
			}
			if(codExcluirEjgPtesCAJG!=null && codExcluirEjgPtesCAJG.equals(ClsConstants.DB_TRUE)){
				final String TIPO_RESOLUCION_PTE_CAJG = rp3.returnProperty("codigo.general.scstiporesolucion.idtiporesolucion.pendientecajg");
				excluirEjs.append(TIPO_RESOLUCION_PTE_CAJG);
				excluirEjs.append(",");
			}
			if(!excluirEjs.toString().equals("")){
				//Quitamos la ultima ,
				excluirEjs = new StringBuffer(excluirEjs.substring(0,excluirEjs.length()-1));

				//excluidos los que, teniendo algun ejg...
				sql.append(" and not  ");
				if(codExcluirSinEjg!=null && codExcluirSinEjg.equals(ClsConstants.DB_TRUE)){



					sql.append(" ( ");


				}else{
					sql.append(" ((select count(*) from SCS_EJG EJG, SCS_EJGDESIGNA EJGDES ");
					sql.append(" where d.idinstitucion = ejgdes.idinstitucion ");
					sql.append(" and d.idturno = ejgdes.idturno ");
					sql.append(" and d.anio = ejgdes.aniodesigna ");
					sql.append(" and d.numero = ejgdes.numerodesigna ");
					sql.append(" and ejgdes.idinstitucion = ejg.idinstitucion ");
					sql.append(" and ejgdes.idtipoejg = ejg.idtipoejg ");
					sql.append(" and ejgdes.anioejg = ejg.anio ");
					sql.append(" and ejgdes.numeroejg = ejg.numero) > 0 and ");

				}



				//... el numero de ejgs con ratificacion 3...
				sql.append(" (select count(*) ");
				sql.append(" from SCS_EJG EJG, SCS_EJGDESIGNA EJGDES ");
				sql.append(" where d.idinstitucion = ejgdes.idinstitucion ");
				sql.append(" and d.idturno = ejgdes.idturno ");
				sql.append(" and d.anio = ejgdes.aniodesigna ");
				sql.append(" and d.numero = ejgdes.numerodesigna ");
				sql.append(" and ejgdes.idinstitucion = ejg.idinstitucion ");
				sql.append(" and ejgdes.idtipoejg = ejg.idtipoejg ");
				sql.append(" and ejgdes.anioejg = ejg.anio ");
				sql.append(" and ejgdes.numeroejg = ejg.numero ");
				sql.append(" and (ejg.idtiporatificacionejg in ( "); 
				sql.append(excluirEjs);
				//... es igual al numero de ejgs relacionados
				sql.append(" ) ");
				if(codExcluirEjgSinResolucion!=null && codExcluirEjgSinResolucion.equals(ClsConstants.DB_TRUE)){
					sql.append(" OR ejg.idtiporatificacionejg is null)) ");

				}else{
					sql.append("))");

				}

				sql.append("  = (select count(*) ");
				sql.append(" from SCS_EJG EJG, SCS_EJGDESIGNA EJGDES ");
				sql.append(" where d.idinstitucion = ejgdes.idinstitucion ");
				sql.append(" and d.idturno = ejgdes.idturno ");
				sql.append(" and d.anio = ejgdes.aniodesigna ");
				sql.append(" and d.numero = ejgdes.numerodesigna ");
				sql.append(" and ejgdes.idinstitucion = ejg.idinstitucion ");
				sql.append(" and ejgdes.idtipoejg = ejg.idtipoejg ");
				sql.append(" and ejgdes.anioejg = ejg.anio ");
				sql.append(" and ejgdes.numeroejg = ejg.numero) ");

				sql.append(")");

			}else{
				String and = "and";
				if(codExcluirSinEjg!=null && codExcluirSinEjg.equals(ClsConstants.DB_TRUE)){
					sql.append(" and (select count(*) from SCS_EJG EJG, SCS_EJGDESIGNA EJGDES ");
					sql.append(" where d.idinstitucion = ejgdes.idinstitucion ");
					sql.append(" and d.idturno = ejgdes.idturno ");
					sql.append(" and d.anio = ejgdes.aniodesigna ");
					sql.append(" and d.numero = ejgdes.numerodesigna ");
					sql.append(" and ejgdes.idinstitucion = ejg.idinstitucion ");
					sql.append(" and ejgdes.idtipoejg = ejg.idtipoejg ");
					sql.append(" and ejgdes.anioejg = ejg.anio ");
					sql.append(" and ejgdes.numeroejg = ejg.numero) > 0  ");
				}
				
					if(codExcluirEjgSinResolucion!=null && codExcluirEjgSinResolucion.equals(ClsConstants.DB_TRUE)){
						sql.append(" and not ((select count(*) from SCS_EJG EJG, SCS_EJGDESIGNA EJGDES ");
						sql.append(" where d.idinstitucion = ejgdes.idinstitucion ");
						sql.append(" and d.idturno = ejgdes.idturno ");
						sql.append(" and d.anio = ejgdes.aniodesigna ");
						sql.append(" and d.numero = ejgdes.numerodesigna ");
						sql.append(" and ejgdes.idinstitucion = ejg.idinstitucion ");
						sql.append(" and ejgdes.idtipoejg = ejg.idtipoejg ");
						sql.append(" and ejgdes.anioejg = ejg.anio ");
						sql.append(" and ejgdes.numeroejg = ejg.numero) > 0  ");
						
						sql.append(" and  ((select count(*) ");
						sql.append(" from SCS_EJG EJG, SCS_EJGDESIGNA EJGDES ");
						sql.append(" where d.idinstitucion = ejgdes.idinstitucion ");
						sql.append(" and d.idturno = ejgdes.idturno ");
						sql.append(" and d.anio = ejgdes.aniodesigna ");
						sql.append(" and d.numero = ejgdes.numerodesigna ");
						sql.append(" and ejgdes.idinstitucion = ejg.idinstitucion ");
						sql.append(" and ejgdes.idtipoejg = ejg.idtipoejg ");
						sql.append(" and ejgdes.anioejg = ejg.anio ");
						sql.append(" and ejgdes.numeroejg = ejg.numero ");
						sql.append(" and ejg.idtiporatificacionejg is  null) ");

						sql.append("  = (select count(*) ");
						sql.append(" from SCS_EJG EJG, SCS_EJGDESIGNA EJGDES ");
						sql.append(" where d.idinstitucion = ejgdes.idinstitucion ");
						sql.append(" and d.idturno = ejgdes.idturno ");
						sql.append(" and d.anio = ejgdes.aniodesigna ");
						sql.append(" and d.numero = ejgdes.numerodesigna ");
						sql.append(" and ejgdes.idinstitucion = ejg.idinstitucion ");
						sql.append(" and ejgdes.idtipoejg = ejg.idtipoejg ");
						sql.append(" and ejgdes.anioejg = ejg.anio ");
						sql.append(" and ejgdes.numeroejg = ejg.numero))) ");
					
				}

			}


		}
		if(formulario.getEstado()!=null && !formulario.getEstado().equals("")){
			sql.append(" AND D.ESTADO =:");
			contador++;
			codigos.put(new Integer(contador),formulario.getEstado());
			sql.append(contador);
			
		}
		if(formulario.getActuacionesPendientes()!=null && !formulario.getActuacionesPendientes().equals("")){
			if (formulario.getActuacionesPendientes()!= null && !formulario.getActuacionesPendientes().equalsIgnoreCase("")) {
				if(formulario.getActuacionesPendientes().equalsIgnoreCase("SINACTUACIONES")){
					sql.append(" and F_SIGA_ACTUACIONESDESIG(D.idinstitucion,D.idturno,D.anio,D.numero) is null");
				}else{
					contador++;
				    codigos.put(new Integer(contador),formulario.getActuacionesPendientes().toUpperCase());
				    sql.append(" and upper(F_SIGA_ACTUACIONESDESIG(D.idinstitucion,D.idturno,D.anio,D.numero))=upper(:");
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
			sql.append(" and trunc(d.fechaentrada) >= :");
			
			contador++;
			codigos.put(new Integer(contador),formulario.getFechaDesde());
			sql.append(contador);
			 
		}
		
		if (formulario.getFechaHasta() != null && !formulario.getFechaHasta().equals("")) {
			sql.append(" and trunc(d.fechaentrada) <=:");
			
			contador++;
			codigos.put(new Integer(contador),formulario.getFechaHasta());
			sql.append(contador);
			 
		}
		
		

		//tODOS MENOS DENEGADO
		
		if((formulario.getInteresadoApellidos() != null && !formulario.getInteresadoApellidos().equalsIgnoreCase("")) 
				&& (formulario.getInteresadoNombre() != null && !formulario.getInteresadoNombre().equalsIgnoreCase(""))){
			
			
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
			aux.append(formulario.getInteresadoNombre().toUpperCase());
			aux.append(" ");
			aux.append(formulario.getInteresadoApellidos().toUpperCase());
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
		    aux.append(formulario.getInteresadoApellidos().toUpperCase());
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
		    aux.append(formulario.getInteresadoNombre().toUpperCase());
		    aux.append("%");
		    codigos.put(new Integer(contador),aux.toString());
		    
			sql.append(":"+contador);
		}
		if(isMostrarJustificacionesPtes){
			sql.append(") WHERE NUMEJGRESUELTOSFAVORABLES>0 ");
		}else{
			sql.append(") ORDER BY FECHAENTRADA,IDINSTITUCION, ANIO, CODIGO, SUFIJO ");
			
			
		}
		return sql.toString();
		
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