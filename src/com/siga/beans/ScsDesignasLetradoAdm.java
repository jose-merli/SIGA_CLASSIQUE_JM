
package com.siga.beans;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;



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
	public PaginadorBind getDesignasLetradoPaginador(Integer idInstitucion, String idPersona, String fechaDesde, 
			String fechaHasta, String mostrarTodas, String interesadoNombre,
			String interesadoApellidos, String anio, boolean isInforme)throws ClsExceptions,SIGAException {
		PaginadorBind paginador=null;
		try {
			Hashtable codigos = new Hashtable();
			StringBuffer sql = new StringBuffer(getQueryDesignasLetrado(idInstitucion, idPersona, fechaDesde, 
					fechaHasta, mostrarTodas, interesadoNombre,
					interesadoApellidos, anio, isInforme,codigos));
			
			sql.append(" ORDER BY fechaOrden desc,anio,codigo desc,idturno,numero ");
			paginador = new PaginadorBind(sql.toString(),codigos);	


		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta getDesignasLetradoPaginador.");
		}
		return paginador;                        
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
		//sql.append(" F_SIGA_GETEJG_DESIGNA(d.idinstitucion, d.idturno, d.anio, d.numero) as expedientes, ");
		//sql.append(" f_siga_getdefendidosdesigna(d.idinstitucion, d.anio, d.idturno, d.numero,1) as cliente, ");
		sql.append(" d.idprocedimiento idprocedimiento, ");
		
		
		sql.append(" d.idinstitucion_juzg idinstitucion_juzg, ");
		sql.append("d.anio");
		sql.append("||'/'");
		sql.append("||");
		sql.append("d.codigo");
		sql.append(" designacion,");
		
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
				
				if(isInforme)
					formatearHashInforme(registro,idInstitucion);
				
				
				
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

	
	
	
	
		
}