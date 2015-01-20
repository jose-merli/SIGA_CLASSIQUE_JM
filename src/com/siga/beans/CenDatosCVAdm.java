/*
 * Created on Dec 22, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.censo.form.BusquedaComisionesForm;
import com.siga.general.*;

/**
 * @author nuria.rgonzalez
 *
 */
// RGG cambio visibilidad public class CenDatosCVAdm extends MasterBeanAdministrador{
public class CenDatosCVAdm extends MasterBeanAdmVisible{

	/**
	 * @param tabla
	 * @param usuario
	 */
	public CenDatosCVAdm(UsrBean usuario) {
		super( CenDatosCVBean.T_NOMBRETABLA, usuario);
	}		
	

	/**
	 * @param tabla
	 * @param usuario
	 * @param userbean
	 * @param idInsitucionClientes
	 * @param idPersonaCliente
	 */
	public CenDatosCVAdm(Integer usuario, UsrBean usrbean,int idInstitucionCliente, long idPersonaCliente) {
		super( CenDatosCVBean.T_NOMBRETABLA, usuario, usrbean, idInstitucionCliente,idPersonaCliente);
	}	

	/**
	 * Devuelve un array con el nombre de los campos de la tabla CEN_DATOSCV 
	 * @author nuria.rgonzalez 22-12-04	 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	CenDatosCVBean.C_IDINSTITUCION,	 CenDatosCVBean.C_IDINSTITUCIONCARGO,
				CenDatosCVBean.C_IDPERSONA,			 CenDatosCVBean.C_IDCV,
				CenDatosCVBean.C_FECHAINICIO,		 CenDatosCVBean.C_FECHAFIN,	
				CenDatosCVBean.C_DESCRIPCION,		 CenDatosCVBean.C_CERTIFICADO,	
				CenDatosCVBean.C_CREDITOS,			 CenDatosCVBean.C_IDTIPOCV,		
				CenDatosCVBean.C_FECHAMOVIMIENTO,	 CenDatosCVBean.C_FECHAMODIFICACION,
				CenDatosCVBean.C_FECHABAJA,			 CenDatosCVBean.C_USUMODIFICACION,
				CenDatosCVBean.C_IDTIPOCVSUBTIPO1,   CenDatosCVBean.C_IDTIPOCVSUBTIPO2,
				CenDatosCVBean.C_IDINSTITUCION_SUBT1, CenDatosCVBean.C_IDINSTITUCION_SUBT2};
		return campos;
	}

	/**
	 * Devuelve un array con el nombre de los campos clave de la tabla CEN_DATOSCV 
	 * @author nuria.rgonzalez  22-12-04	  
	 */
	protected String[] getClavesBean() {
		String[] campos = {	CenDatosCVBean.C_IDINSTITUCION,CenDatosCVBean.C_IDINSTITUCIONCARGO,
				CenDatosCVBean.C_IDPERSONA,			CenDatosCVBean.C_IDCV};
		return campos;
	}	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	/**
	 * Devuelve un CenDatosCVBean con los campos de la tabla CEN_DATOSCV 
	 * @author nuria.rgonzalez 22-12-04	 
	 * @param Hashtable 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenDatosCVBean bean = null;
		try{
			bean = new CenDatosCVBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenDatosCVBean.C_IDINSTITUCION));
			bean.setIdInstitucionCargo(UtilidadesHash.getInteger(hash,CenDatosCVBean.C_IDINSTITUCIONCARGO));			
			bean.setIdPersona(UtilidadesHash.getLong(hash,CenDatosCVBean.C_IDPERSONA));
			bean.setIdCV(UtilidadesHash.getInteger(hash,CenDatosCVBean.C_IDCV));
			bean.setFechaInicio(UtilidadesHash.getString(hash,CenDatosCVBean.C_FECHAINICIO));
			bean.setFechaFin(UtilidadesHash.getString(hash,CenDatosCVBean.C_FECHAFIN));
			bean.setDescripcion(UtilidadesHash.getString(hash,CenDatosCVBean.C_DESCRIPCION));
			bean.setCertificado(UtilidadesHash.getString(hash,CenDatosCVBean.C_CERTIFICADO));
			bean.setCreditos(UtilidadesHash.getLong(hash,CenDatosCVBean.C_CREDITOS));
			bean.setIdTipoCV(UtilidadesHash.getInteger(hash,CenDatosCVBean.C_IDTIPOCV));
			bean.setIdTipoCVSubtipo1(UtilidadesHash.getString(hash,CenDatosCVBean.C_IDTIPOCVSUBTIPO1));
			bean.setIdTipoCVSubtipo2(UtilidadesHash.getString(hash,CenDatosCVBean.C_IDTIPOCVSUBTIPO2));
			bean.setIdInstitucion_subt1(UtilidadesHash.getInteger(hash,CenDatosCVBean.C_IDINSTITUCION_SUBT1));
			bean.setIdInstitucion_subt2(UtilidadesHash.getInteger(hash,CenDatosCVBean.C_IDINSTITUCION_SUBT2));
			bean.setFechaMovimiento(UtilidadesHash.getString(hash,CenDatosCVBean.C_FECHAMOVIMIENTO));
			bean.setFechaBaja(UtilidadesHash.getString(hash,CenDatosCVBean.C_FECHABAJA));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenDatosCVBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenDatosCVBean.C_USUMODIFICACION));
			bean.setNombre(UtilidadesHash.getString(hash,"NOMBRE"));
			bean.setNumcolegiado(UtilidadesHash.getString(hash,"NCOLEGIADO"));
			bean.setCargo(UtilidadesHash.getString(hash,"CARGO"));
			bean.setApellidos(UtilidadesHash.getString(hash,"APELLIDOS"));
			
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/**
	 * Devuelve un Hashtable con los campos de la tabla CEN_DATOSCV 
	 * @author nuria.rgonzalez 22-12-04	 
	 * @param CenDatosCVBean 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CenDatosCVBean b = (CenDatosCVBean) bean;
			UtilidadesHash.set(hash, CenDatosCVBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			UtilidadesHash.set(hash, CenDatosCVBean.C_IDINSTITUCIONCARGO, String.valueOf(b.getIdInstitucionCargo()));			
			UtilidadesHash.set(hash, CenDatosCVBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
			UtilidadesHash.set(hash, CenDatosCVBean.C_IDCV, String.valueOf(b.getIdCV()));			
			UtilidadesHash.set(hash, CenDatosCVBean.C_FECHAINICIO, b.getFechaInicio());
			UtilidadesHash.set(hash, CenDatosCVBean.C_FECHAFIN, b.getFechaFin());
			UtilidadesHash.set(hash, CenDatosCVBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, CenDatosCVBean.C_CERTIFICADO, b.getCertificado());
			UtilidadesHash.set(hash, CenDatosCVBean.C_CREDITOS, String.valueOf(b.getCreditos()));
			UtilidadesHash.set(hash, CenDatosCVBean.C_IDTIPOCV, b.getIdTipoCV());
			UtilidadesHash.set(hash, CenDatosCVBean.C_IDTIPOCVSUBTIPO1, String.valueOf(b.getIdTipoCVSubtipo1()));
			UtilidadesHash.set(hash, CenDatosCVBean.C_IDTIPOCVSUBTIPO2,String.valueOf(b.getIdTipoCVSubtipo2()));
			UtilidadesHash.set(hash, CenDatosCVBean.C_IDINSTITUCION_SUBT1, String.valueOf(b.getIdInstitucion_subt1()));
			UtilidadesHash.set(hash, CenDatosCVBean.C_IDINSTITUCION_SUBT2, String.valueOf(b.getIdInstitucion_subt2()));
			UtilidadesHash.set(hash, CenDatosCVBean.C_FECHAMOVIMIENTO, b.getFechaMovimiento());	
			UtilidadesHash.set(hash, CenDatosCVBean.C_FECHABAJA, b.getFechaBaja());	
			UtilidadesHash.set(hash, CenDatosCVBean.C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, CenDatosCVBean.C_USUMODIFICACION, b.getUsuMod());	
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/**
	 * Devuelve un array con el nombre de los campos de la tablas CEN_DATOSCV, CEN_TIPOSCV, para construir la Query.
	 * @author nuria.rgonzalez 22-12-04	 
	 */
	protected String[] getCamposDatosCV() {
		String[] campos = {	
				UtilidadesMultidioma.getCampoMultidiomaSimple(CenTiposCVBean.T_NOMBRETABLA + "." + CenTiposCVBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " AS TIPOAPUNTE",	
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDCV,
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION,
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDPERSONA,
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_FECHAINICIO,	
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_FECHAFIN,
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_DESCRIPCION,
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_CERTIFICADO,	
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_CREDITOS,
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDTIPOCV,
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDTIPOCVSUBTIPO1,
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDTIPOCVSUBTIPO2,
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION_SUBT1,
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION_SUBT2,
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_FECHAMOVIMIENTO,
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_FECHABAJA,
				CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCIONCARGO,
				"(select " + UtilidadesMultidioma.getCampoMultidioma(CenTiposCVSubtipo1Bean.C_DESCRIPCION, this.usrbean.getLanguage())+
				" from "+CenTiposCVSubtipo1Bean.T_NOMBRETABLA+
				" where "+CenTiposCVSubtipo1Bean.C_IDTIPOCV+"="+CenDatosCVBean.T_NOMBRETABLA + "." +CenDatosCVBean.C_IDTIPOCV+
				"  and  "+CenTiposCVSubtipo1Bean.C_IDTIPOCVSUBTIPO1+"="+CenDatosCVBean.T_NOMBRETABLA + "."+CenDatosCVBean.C_IDTIPOCVSUBTIPO1+
				"  and  "+CenTiposCVSubtipo1Bean.C_IDINSTITUCION+"="+CenDatosCVBean.T_NOMBRETABLA + "."+CenDatosCVBean.C_IDINSTITUCION_SUBT1+") DESCSUBTIPO1,"+
				         
				"(select " + UtilidadesMultidioma.getCampoMultidioma(CenTiposCVSubtipo2Bean.C_DESCRIPCION, this.usrbean.getLanguage())+
				" from "+CenTiposCVSubtipo2Bean.T_NOMBRETABLA+
				" where "+CenTiposCVSubtipo2Bean.C_IDTIPOCV+"="+CenDatosCVBean.T_NOMBRETABLA + "."+CenDatosCVBean.C_IDTIPOCV+
				"  and  "+CenTiposCVSubtipo2Bean.C_IDTIPOCVSUBTIPO2+"="+CenDatosCVBean.T_NOMBRETABLA + "."+CenDatosCVBean.C_IDTIPOCVSUBTIPO2+
				"  and  "+CenTiposCVSubtipo2Bean.C_IDINSTITUCION+"="+CenDatosCVBean.T_NOMBRETABLA + "."+CenDatosCVBean.C_IDINSTITUCION_SUBT2+") DESCSUBTIPO2"};
		return campos;
	}
	
	/**
	 * Devuelve un array con el nombre de las tablas CEN_DATOSCV, CEN_TIPOSCV y las relaciones entre ellas para construir la Query.
	 * @author nuria.rgonzalez 22-12-04	 
	 */
	protected String getTablasDatosCV(){
		
		String campos = CenDatosCVBean.T_NOMBRETABLA 
			+ " INNER JOIN "+ 
			 CenTiposCVBean.T_NOMBRETABLA +
			 " ON "+
			 	CenDatosCVBean.T_NOMBRETABLA +"."+ CenDatosCVBean.C_IDTIPOCV + "=" +
			 	CenTiposCVBean.T_NOMBRETABLA + "." + CenTiposCVBean.C_IDTIPOCV;
		 		
		return campos;
	}
	
	/**
	 * Devuelve un array con el nombre de los campos por los cuales se ordenar�n los resultados de la Query.
	 * @author nuria.rgonzalez 22-12-04	 
	 */
	protected String[] getOrdenDatosCV(){
		String[] campos = { CenDatosCVBean.T_NOMBRETABLA +"."+ CenDatosCVBean.C_IDTIPOCV,
							CenDatosCVBean.T_NOMBRETABLA +"."+ CenDatosCVBean.C_IDTIPOCVSUBTIPO1,
							CenDatosCVBean.T_NOMBRETABLA +"."+ CenDatosCVBean.C_IDTIPOCVSUBTIPO2,
							CenDatosCVBean.T_NOMBRETABLA +"."+ CenDatosCVBean.C_FECHAINICIO};
		return campos;
	}
	
	/**
	 * Devuelve un vector con los datos de los CV del cliente pasado como par�metro 
	 * @author nuria.rgonzalez 22-12-04
	 * @version 1	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de la que vamos a obtener los datos. 
	 */	
	public Vector selectDatosCV(Long idPersona, Integer idInstitucion, boolean bIncluirRegistrosConBajaLogica)throws ClsExceptions, SIGAException
	{
		Vector v = null;
		try {
			RowsContainer rc = null;
			String where = " WHERE " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDPERSONA + " = " + idPersona +
			  				 " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION + " = " + idInstitucion;
			
			if(!bIncluirRegistrosConBajaLogica) {
				where += " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_FECHABAJA + " IS NULL";
			}
			
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasDatosCV(), this.getCamposDatosCV());
			
			sql += where;
			sql += " ORDER BY IDTIPOCV, DESCSUBTIPO1, DESCSUBTIPO2, FECHAINICIO"; 

            // RGG cambio visibilidad
            rc = this.find(sql);
            if (rc!=null) {
 				v = new Vector();
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en selectDatosCV");
		}
		return v;
	}
	
		/**
	 * Devuelve un vector con los datos de los CV del cliente pasado como par�metro 
	 * @author nuria.rgonzalez 22-12-04
	 * @version 1	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de la que vamos a obtener los datos. 
	 */	
	public Vector selectDatosCVInforme(Long idPersona, Integer idInstitucion, boolean bIncluirRegistrosConBajaLogica)throws ClsExceptions, SIGAException
	{
		Vector v = null;
		try {
			RowsContainer rc = null;
			rc = new RowsContainer(); 
			String sql = "Select IDTIPOCV,IDTIPOCVSUBTIPO1,IDTIPOCVSUBTIPO2,TIPOAPUNTE,IDCV,IDINSTITUCION,IDPERSONA, "+
						 " to_char(FECHAINICIO, 'dd/mm/yyyy') FECHAINICIO, " +
						 " to_char(FECHAFIN, 'dd/mm/yyyy') FECHAFIN, "+
						 " f_siga_getrecurso_etiqueta(decode(CERTIFICADO,1, 'messages.si','messages.no'),"+usrbean.getLanguage()+") AS VERIFICADO, "+
						 " DESCRIPCION,CREDITOS,IDINSTITUCION_SUBT1,IDINSTITUCION_SUBT2, "+
						 " to_char(FECHAMOVIMIENTO, 'dd/mm/yyyy') AS FECHAVERIFICACION, "+
						 " to_char(FECHABAJA, 'dd/mm/yyyy')FECHABAJA,"+
						 " DESCSUBTIPO1, DESCSUBTIPO2 FROM ( " +UtilidadesBDAdm.sqlSelect(this.getTablasDatosCV(), this.getCamposDatosCV());

			String where = " WHERE " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDPERSONA + " = " + idPersona +
			       		   " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION + " = " + idInstitucion;
			
			if(!bIncluirRegistrosConBajaLogica) {
				where += " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_FECHABAJA + " IS NULL";
			}
			
			sql += where;
			sql += UtilidadesBDAdm.sqlOrderBy(this.getOrdenDatosCV()) +")";

            // RGG cambio visibilidad
            rc = this.find(sql);
            if (rc!=null) {
 				v = new Vector();
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en selectDatosCV");
		}
		return v;
	}

	/**
	 * Devuelve un Hastable con los datos del CV del cliente pasado como par�metro.
	 * @author nuria.rgonzalez 22-12-04
	 * @version 1	 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de la que vamos a obtener los datos.
	 * @param idCV, es el identificador del CV del que queremos obtener los datos.  
	 */
	public Hashtable selectDatosCV(Long idPersona, Integer idInstitucion, Integer idCV) throws ClsExceptions, SIGAException{
		//Hashtable registro = new Hashtable();
		Hashtable registro = null;
		RowsContainer rc = null;
		String where =" WHERE " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDPERSONA + " = " + idPersona +
		  " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION + " = " + idInstitucion +
		  " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDCV + " = " + idCV;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasDatosCV(), this.getCamposDatosCV());
			sql += where;
			sql += UtilidadesBDAdm.sqlOrderBy(this.getOrdenDatosCV());
            // RGG cambio visibilidad
            rc = this.find(sql);
            if (rc!=null) {
				if(rc.size()>0){
					Row fila = (Row) rc.get(0);
					registro = (Hashtable)fila.getRow(); 					
				}
			}
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en selectDatosCV");
		}
		return registro;
	}
	public Hashtable selectDatosCVJunta(Long idPersona, Integer idInstitucion,Integer idInstitucionCargo, Integer idCV) throws ClsExceptions, SIGAException{
		//Hashtable registro = new Hashtable();
		Hashtable registro = null;
		RowsContainer rc = null;
		String where =" WHERE " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDPERSONA + " = " + idPersona +
		  " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION + " = " + idInstitucion +
		  " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCIONCARGO + " = " + idInstitucionCargo +
		  " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDCV + " = " + idCV;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasDatosCV(), this.getCamposDatosCV());
			sql += where;
			sql += UtilidadesBDAdm.sqlOrderBy(this.getOrdenDatosCV());
            // RGG cambio visibilidad
            rc = this.find(sql);
            if (rc!=null) {
				if(rc.size()>0){
					Row fila = (Row) rc.get(0);
					registro = (Hashtable)fila.getRow(); 					
				}
			}
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en selectDatosCV");
		}
		return registro;
	}


	/**
	 * Inserta los datos de un C.V. y rellena la tabla de historicos (CEN_HISTORICO)
	 * @author daniel.campos 10-01-05
	 * @version 1	 
	 * @param BeanCV datos del CV.
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico
	 */
	public boolean insertarConHistorico (CenDatosCVBean beanCV, CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			
			if (insert(beanCV)) {
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
				if (admHis.insertCompleto(beanHis, beanCV, CenHistoricoAdm.ACCION_INSERT, idioma)) {
					return true;
				}
			}
			return false;
		
		}catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
	}
	public boolean insertar (CenDatosCVBean beanCV, CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			beanCV.setIdCV(this.getNuevoID(beanCV));
			if (insert(beanCV)) {
				return true;
			}
			return false;
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
	}
	public boolean updatefecha (CenDatosCVBean beanCV) throws ClsExceptions, SIGAException 
	{
		try {
			String sql ="UPDATE CEN_DATOSCV SET  FECHAFIN =  TO_DATE('" + beanCV.getFechaFin() + "', '"	+ ClsConstants.DATE_FORMAT_SQL + "') WHERE IDINSTITUCION = "+beanCV.getIdInstitucion()+"  AND IDINSTITUCIONCARGO = "+beanCV.getIdInstitucionCargo()+"  AND IDPERSONA = "+beanCV.getIdPersona()+"  AND IDCV = "+beanCV.getIdCV();  
			updateDirectSQL(sql);
			return true;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
	}

	/**
	 * Actualiza los datos de un C.V. y rellena la tabla de historicos (CEN_HISTORICO)
	 * @author daniel.campos 10-01-05
	 * @version 1	 
	 * @param BeanCV datos del CV.
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico
	 */
	public boolean updateConHistorico (CenDatosCVBean beanCV, CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			if (update(beanToHashTable(beanCV), beanCV.getOriginalHash())) {
				CenHistoricoAdm admHis = new CenHistoricoAdm(this.usrbean);
				if (admHis.insertCompleto(beanHis, beanCV, CenHistoricoAdm.ACCION_UPDATE, idioma)) {
					return true;
				}
			}
			return false;
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
	}

	/**
	 * Borrar los datos de un C.V. y rellena la tabla de historicos (CEN_HISTORICO)
	 * @author daniel.campos 10-01-05
	 * @version 1	 
	 * @param Hash con las claves del CV a borrar
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico.
	 */
	public boolean deleteConHistorico (Hashtable clavesCV, CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			CenDatosCVBean beanCV = (CenDatosCVBean) this.selectByPK(clavesCV).get(0);
			if (delete(clavesCV)) {
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
				if (admHis.insertCompleto(beanHis, beanCV, CenHistoricoAdm.ACCION_DELETE, idioma)) {
					return true;
				}
			}
			return false;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
	}
	
	/**
	 * Obtiene un nuevo ID del curriculum de una persona e institucion determinada
	 * @author daniel.campos 12-01-05
	 * @version 1	 
	 * @param BeanCV datos del CV.
	 * @return nuevo ID.
	 */
	public Integer getNuevoID (CenDatosCVBean bean)throws SIGAException,ClsExceptions 
	{
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " SELECT (MAX(" + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDCV + ") + 1) AS " + CenDatosCVBean.C_IDCV + 
			  			 " FROM " + CenDatosCVBean.T_NOMBRETABLA +  
						 " WHERE " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDPERSONA+ " = " + bean.getIdPersona() +
						 " AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION + " = " + bean.getIdInstitucion();
						 //" AND " + CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCIONCARGO + " = " + bean.getIdInstitucionCargo();

            // RGG cambio visibilidad
            rc = this.findForUpdate(sql);
            if (rc!=null) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				Integer idCV = UtilidadesHash.getInteger(prueba, CenDatosCVBean.C_IDCV);
				if (idCV == null) {
					return new Integer(1);
				}
				else return idCV;								
			}
		}	
//		catch (SIGAException e) {
//			throw e;
//		}
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getSecuenciaIDCV' en B.D.");		
		}
		return null;
	}	
	
	/** 
	 * Recoge la informacion del registro seleccionado a partir de sus claves para <br/>
	 * transacciones (for update)
	 * @param  idPersona - identificador de persona 
	 * @param  idInstitucion - identificador de la institucion
	 * @param  idCV - identificador del curriculum
	 * @return  hashtable - Entrada seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Hashtable getEntradaCV (String idPersona, String idInstitucion, String idCV) throws ClsExceptions, SIGAException {
		   Hashtable datos=new Hashtable();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDPERSONA + "," +
							CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCION + "," +
							CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDINSTITUCIONCARGO + "," +
							CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDCV + "," +
							CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_FECHAINICIO + "," +
							CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_FECHAFIN + "," +
							CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_DESCRIPCION + "," +
							CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_CERTIFICADO + "," +
							CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_CREDITOS + "," +
							CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_IDTIPOCV + "," +
							CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_FECHAMOVIMIENTO + "," +
							CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_FECHAMODIFICACION + "," +
	            			CenDatosCVBean.T_NOMBRETABLA + "." + CenDatosCVBean.C_USUMODIFICACION +
							" FROM " + CenDatosCVBean.T_NOMBRETABLA + 
							" WHERE " +
							CenDatosCVBean.T_NOMBRETABLA +"."+ CenDatosCVBean.C_IDPERSONA + "=" + idPersona +
							" AND " +
							CenDatosCVBean.T_NOMBRETABLA +"."+ CenDatosCVBean.C_IDINSTITUCION + "=" + idInstitucion +
							" AND " +
							CenDatosCVBean.T_NOMBRETABLA +"."+ CenDatosCVBean.C_IDCV + "=" + idCV;							
														
	            // RGG cambio visibilidad
	            rc = this.findForUpdate(sql);
	            if (rc!=null) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos=fila.getRow();
	               }
	            } 
	       }
//			catch (SIGAException e) {
//				throw e;
//			}
			catch (Exception e) {
				throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla DatosCV.");
			}
			return datos;                        
    }
	
	/* DELETE LOGICO: no se borra fisicamente */
	public boolean delete(Hashtable hash) throws ClsExceptions 
	{
		try {
			Hashtable h = new Hashtable();
			String [] claves = this.getClavesBean();
			for (int i = 0; i < claves.length; i++) {
				h.put (claves[i], hash.get(claves[i]));
			}

			String [] campos = {CenDatosCVBean.C_FECHABAJA};
			UtilidadesHash.set(h, CenDatosCVBean.C_FECHABAJA, "SYSDATE");

			if (this.updateDirect(h, claves, campos)) {
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ClsExceptions(e, "Error al realizar el \"delete\" en B.D.");
		}
		return false;
	}
	
	public Vector buscarComisiones (BusquedaComisionesForm formulario/* , String language*/) throws ClsExceptions , SIGAException 
	{
			
		Vector registros = null;
		
		String select="";
		Integer idTipoCVSubtipo1 = null;
		Integer idTipoCVSubtipo2 = null;
		Integer idInstitucionSubtipo1=null;
		Integer idInstitucionSubtipo2=null;
		try 
		{
			    select=" select (select per.nombre"+
			           "          from "+CenPersonaBean.T_NOMBRETABLA+" per"+
				       "          where  per."+CenPersonaBean.C_IDPERSONA+" = t."+CenDatosCVBean.C_IDPERSONA+
				       "         ) NOMBRE,"+
				       "         (select per."+CenPersonaBean.C_APELLIDOS1+"||' '||"+CenPersonaBean.C_APELLIDOS2+
				       "          from "+CenPersonaBean.T_NOMBRETABLA+" per"+
				       "          where  per."+CenPersonaBean.C_IDPERSONA+" = t."+CenDatosCVBean.C_IDPERSONA+
				       "          ) APELLIDOS,";
			    
			    if((formulario.getIdInstitucionCargo()!=null)&&(!formulario.getIdInstitucionCargo().trim().equals(""))){
				    select+=" f_siga_calculoncolegiado(t."+CenDatosCVBean.C_IDINSTITUCIONCARGO+",t."+CenDatosCVBean.C_IDPERSONA+") NCOLEGIADO,";
			    }else  {
				    select+=" f_siga_calculoncolegiado(t."+CenDatosCVBean.C_IDINSTITUCION+",t."+CenDatosCVBean.C_IDPERSONA+") NCOLEGIADO,";
			    }    
			    
			    select+=" (select " + UtilidadesMultidioma.getCampoMultidioma("p."+CenTiposCVSubtipo1Bean.C_DESCRIPCION, this.usrbean.getLanguage()) + " " + 
				       "  from "+CenTiposCVSubtipo1Bean.T_NOMBRETABLA+" p"+
				       "  where p."+CenTiposCVSubtipo1Bean.C_IDTIPOCV+"=t."+CenDatosCVBean.C_IDTIPOCV+
				       "    and p."+CenTiposCVSubtipo1Bean.C_IDINSTITUCION+"=t."+CenDatosCVBean.C_IDINSTITUCION_SUBT1+
				       "    and p."+CenTiposCVSubtipo1Bean.C_IDTIPOCVSUBTIPO1+"=t."+CenDatosCVBean.C_IDTIPOCVSUBTIPO1+") COMISION,"+
				       
				       
				       
				       " (select CODIGOEXT " + 
				       "  from "+CenTiposCVSubtipo1Bean.T_NOMBRETABLA+" p"+
				       "  where p."+CenTiposCVSubtipo1Bean.C_IDTIPOCV+"=t."+CenDatosCVBean.C_IDTIPOCV+
				       "    and p."+CenTiposCVSubtipo1Bean.C_IDINSTITUCION+"=t."+CenDatosCVBean.C_IDINSTITUCION_SUBT1+
				       "    and p."+CenTiposCVSubtipo1Bean.C_IDTIPOCVSUBTIPO1+"=t."+CenDatosCVBean.C_IDTIPOCVSUBTIPO1+") CODIGOEXT,"+
				       
				       
				           
				       "  (select " + UtilidadesMultidioma.getCampoMultidioma("p."+CenTiposCVSubtipo2Bean.C_DESCRIPCION, this.usrbean.getLanguage()) + " " +
				       "   from "+CenTiposCVSubtipo2Bean.T_NOMBRETABLA+" p"+
				       "   where p."+CenTiposCVSubtipo2Bean.C_IDTIPOCV+"=t."+CenDatosCVBean.C_IDTIPOCV+
				       "     and p."+CenTiposCVSubtipo2Bean.C_IDINSTITUCION+"=t."+CenDatosCVBean.C_IDINSTITUCION_SUBT2+
				       "     and p."+CenTiposCVSubtipo2Bean.C_IDTIPOCVSUBTIPO2+"=t."+CenDatosCVBean.C_IDTIPOCVSUBTIPO2+") CARGO,"+
				       "    t."+CenDatosCVBean.C_IDTIPOCVSUBTIPO1+","+
				       "    t."+CenDatosCVBean.C_IDTIPOCVSUBTIPO2+","+
				       "    t."+CenDatosCVBean.C_IDCV+","+
				       "    t."+CenDatosCVBean.C_IDPERSONA+","+
				       "    t."+CenDatosCVBean.C_IDINSTITUCION+","+
				       "    t."+CenDatosCVBean.C_IDINSTITUCIONCARGO+","+
				       "    t."+CenDatosCVBean.C_IDTIPOCV+","+
				       "    t."+CenDatosCVBean.C_FECHAINICIO+","+
				       "    t."+CenDatosCVBean.C_FECHAFIN+
				       "  from "+CenDatosCVBean.T_NOMBRETABLA+" t "+
			           " where T."+CenDatosCVBean.C_IDTIPOCV+"="+ClsConstants.TIPOCV_COMISIONES+ "" +
			           "   AND T."+CenDatosCVBean.C_FECHABAJA+" IS NULL "; 
			           // Aqui metemos el if institucion no vacio
			           if((formulario.getIdInstitucion()!=null)&&(!formulario.getIdInstitucion().trim().equals(""))){
			        	    select+= "  and T."+CenDatosCVBean.C_IDINSTITUCION+"="+formulario.getIdInstitucion();
			           }  
			           if((formulario.getIdInstitucionCargo()!=null)&&(!formulario.getIdInstitucionCargo().trim().equals(""))){
			        	    select+= "  and T."+CenDatosCVBean.C_IDINSTITUCIONCARGO+"="+formulario.getIdInstitucionCargo();
			           } 			           
			           if((formulario.getIdPersona()!=null)&&(!formulario.getIdPersona().trim().equals(""))){
			        	    select+= "  and T."+CenDatosCVBean.C_IDPERSONA+"="+formulario.getIdPersona();
			           } 
			 
			    if (formulario.getComision()!=null && !formulario.getComision().equals("")){
			    	String[] datosCVSubtipo1;
					  datosCVSubtipo1=formulario.getComision().toString().split("@");
					  idTipoCVSubtipo1=new Integer(datosCVSubtipo1[0]);
					  idInstitucionSubtipo1=new Integer(datosCVSubtipo1[1]);
					 
					
			    	
			    	select+=" AND T."+CenDatosCVBean.C_IDTIPOCVSUBTIPO1+" = "+idTipoCVSubtipo1+
					        " AND T."+CenDatosCVBean.C_IDINSTITUCION_SUBT1+" = "+idInstitucionSubtipo1;
			    	
			    }
			    if (formulario.getCargos()!=null && !formulario.getCargos().equals("")){
			    		 String[] datosCVSubtipo2;
						 datosCVSubtipo2=formulario.getCargos().toString().split("@");
						 idTipoCVSubtipo2=new Integer(datosCVSubtipo2[0]);
						 idInstitucionSubtipo2=new Integer(datosCVSubtipo2[1]);
						
			    	select+=" AND T."+CenDatosCVBean.C_IDTIPOCVSUBTIPO2+" = "+idTipoCVSubtipo2+
			    	        " AND T."+CenDatosCVBean.C_IDINSTITUCION_SUBT1+" = "+idInstitucionSubtipo2;
			    }
			    if (formulario.getFechaCargo()!=null && !formulario.getFechaCargo().equals("")){
			    	String fechaCargo="to_date('"+formulario.getFechaCargo()+"','DD/MM/YYYY')";
			    	select+=" AND (("+fechaCargo+" between TO_DATE(TO_CHAR(T."+CenDatosCVBean.C_FECHAINICIO+",'DD/MM/YYYY'),'DD/MM/YYYY') and TO_DATE(TO_CHAR(T."+CenDatosCVBean.C_FECHAFIN+",'DD/MM/YYYY'),'DD/MM/YYYY')) OR (T."+CenDatosCVBean.C_FECHAFIN+" IS NULL AND TO_DATE(TO_CHAR(T."+CenDatosCVBean.C_FECHAINICIO+",'DD/MM/YYYY'),'DD/MM/YYYY') <= "+fechaCargo+"))";
			    }
			    
			    select+= " ORDER BY CODIGOEXT, FECHAINICIO";
				CenTiposCVSubtipo1Adm admCV = new CenTiposCVSubtipo1Adm(this.usrbean);
				Hashtable datosCV= new Hashtable();
			
				registros = admCV.selectGenerico(select);
						
	    } catch (Exception e) {
	    	throw new ClsExceptions(e,"Error obteniendo la comisiones y los cargos."); 
   	    }
		return registros;
	}

	public List<CenDatosCVBean> buscarComisionesJunta (BusquedaComisionesForm formulario/* , String language*/) throws ClsExceptions , SIGAException 
	{
			
				
		List<CenDatosCVBean> registros = null;
		
		String select="";
		Integer idTipoCVSubtipo1 = null;
		Integer idTipoCVSubtipo2 = null;
		Integer idInstitucionSubtipo1=null;
		Integer idInstitucionSubtipo2=null;
		GenParametrosAdm paramAdm = new GenParametrosAdm (this.usrbean);
		final String IDTIPOCV_JUNTASGOBIERNO = paramAdm.getValor (this.usrbean.getLocation (), "CEN", ClsConstants.GEN_PARAM_IDTIPOCV_JUNTASGOBIERNO, "");
		try 
		{
			    select=" select (select per.nombre"+
			           "          from "+CenPersonaBean.T_NOMBRETABLA+" per"+
				       "          where  per."+CenPersonaBean.C_IDPERSONA+" = t."+CenDatosCVBean.C_IDPERSONA+
				       "         ) NOMBRE,"+
				       "         (select per."+CenPersonaBean.C_APELLIDOS1+"||' '||"+CenPersonaBean.C_APELLIDOS2+
				       "          from "+CenPersonaBean.T_NOMBRETABLA+" per"+
				       "          where  per."+CenPersonaBean.C_IDPERSONA+" = t."+CenDatosCVBean.C_IDPERSONA+
				       "          ) APELLIDOS,";
					    if((formulario.getIdInstitucionCargo()!=null)&&(!formulario.getIdInstitucionCargo().trim().equals(""))){
						    select+=" f_siga_calculoncolegiado(t."+CenDatosCVBean.C_IDINSTITUCIONCARGO+",t."+CenDatosCVBean.C_IDPERSONA+") NCOLEGIADO,";
					    }else  {
						    select+=" f_siga_calculoncolegiado(t."+CenDatosCVBean.C_IDINSTITUCION+",t."+CenDatosCVBean.C_IDPERSONA+") NCOLEGIADO,";
					    }   

			  select+= " (select " + UtilidadesMultidioma.getCampoMultidioma("p."+CenTiposCVSubtipo1Bean.C_DESCRIPCION, this.usrbean.getLanguage()) + " " + 
				       "  from "+CenTiposCVSubtipo1Bean.T_NOMBRETABLA+" p"+
				       "  where p."+CenTiposCVSubtipo1Bean.C_IDTIPOCV+"=t."+CenDatosCVBean.C_IDTIPOCV+
				       "    and p."+CenTiposCVSubtipo1Bean.C_IDINSTITUCION+"=t."+CenDatosCVBean.C_IDINSTITUCION_SUBT1+
				       "    and p."+CenTiposCVSubtipo1Bean.C_IDTIPOCVSUBTIPO1+"=t."+CenDatosCVBean.C_IDTIPOCVSUBTIPO1+") CARGO,"+
				       "    t."+CenDatosCVBean.C_IDTIPOCVSUBTIPO1+" AS IDTIPOCVSUBTIPO1,"+
				       "    t."+CenDatosCVBean.C_IDTIPOCVSUBTIPO2+"  AS IDTIPOCVSUBTIPO2,"+
				       "    t."+CenDatosCVBean.C_IDCV+" AS IDCV,"+
				       "    t."+CenDatosCVBean.C_IDPERSONA+" AS IDPERSONA,"+
				       "    t."+CenDatosCVBean.C_IDINSTITUCION+","+
				       "    t."+CenDatosCVBean.C_IDINSTITUCIONCARGO+","+
				       "    t."+CenDatosCVBean.C_IDTIPOCV+","+
				       "    TO_CHAR(T."+CenDatosCVBean.C_FECHAINICIO+",'DD/MM/YYYY') AS FECHAINICIO, "+
				       "    TO_CHAR(T."+CenDatosCVBean.C_FECHAFIN+",'DD/MM/YYYY')  AS FECHAFIN "+
				       "  from "+CenDatosCVBean.T_NOMBRETABLA+" t, "+
				       		CenTiposCVSubtipo1Bean.T_NOMBRETABLA + " TIPO1 "+
			           " where T."+CenDatosCVBean.C_IDTIPOCV+"="+IDTIPOCV_JUNTASGOBIERNO+" "+
			           "   AND T."+CenDatosCVBean.C_FECHABAJA+" IS NULL ";
			          // "   AND T."+CenDatosCVBean.C_FECHAFIN+" IS NULL ";//+
			    	   //"   AND T."+CenDatosCVBean.C_FECHAINICIO+" = "+" TO_DATE('" + formulario.getFechaCargo() + "', '"
					//	+ ClsConstants.DATE_FORMAT_SQL + "') "; 
			           // Aqui metemos el if institucion no vacio
			           if((formulario.getIdInstitucion()!=null)&&(!formulario.getIdInstitucion().trim().equals(""))){
			        	    select+= "  and T."+CenDatosCVBean.C_IDINSTITUCION+"="+formulario.getIdInstitucion();
			           }   
			           if((formulario.getIdInstitucionCargo()!=null)&&(!formulario.getIdInstitucionCargo().trim().equals(""))){
			        	    select+= "  and T."+CenDatosCVBean.C_IDINSTITUCIONCARGO+"="+formulario.getIdInstitucionCargo();
			           }
					   if (formulario.getFechaCargo()!=null && !formulario.getFechaCargo().equals("")){
					    	String fechaCargo="to_date('"+formulario.getFechaCargo()+"','DD/MM/YYYY')";
					    	select+=" AND (T."+CenDatosCVBean.C_FECHAFIN+" IS NULL OR TO_DATE(TO_CHAR(T."+CenDatosCVBean.C_FECHAFIN+",'DD/MM/YYYY'),'DD/MM/YYYY') >= "+fechaCargo+")";
					   }
					   if (formulario.getIdPersona()!=null && !formulario.getIdPersona().equals("")){
						   select+= "  and T."+CenDatosCVBean.C_IDPERSONA+"="+formulario.getIdPersona();
					   }
			 
			    if (formulario.getCargos()!=null && !formulario.getCargos().equals("")){
			    	String[] datosCVSubtipo1;
					  datosCVSubtipo1=formulario.getCargos().toString().split("@");
					  idTipoCVSubtipo1=new Integer(datosCVSubtipo1[0]);
					  idInstitucionSubtipo1=new Integer(datosCVSubtipo1[1]);

					  select+=" AND T."+CenDatosCVBean.C_IDTIPOCVSUBTIPO1+" = "+idTipoCVSubtipo1+
					        " AND T."+CenDatosCVBean.C_IDINSTITUCION_SUBT1+" = "+idInstitucionSubtipo1;
			    	
			    }
			    
			   //if (formulario.getIdInstitucionCargo()!=null && !formulario.getIdInstitucionCargo().equals("")){
			   // 	 select+= "  and T."+CenDatosCVBean.C_IDINSTITUCIONCARGO+"="+formulario.getIdInstitucionCargo();
			   //}
			    
			   select +=" AND TIPO1."+CenTiposCVSubtipo1Bean.C_IDTIPOCV+"(+)= T."+CenDatosCVBean.C_IDTIPOCV+
				   " AND TIPO1."+CenTiposCVSubtipo1Bean.C_IDTIPOCVSUBTIPO1+"(+)= T."+CenDatosCVBean.C_IDTIPOCVSUBTIPO1+
				   " AND TIPO1."+CenTiposCVSubtipo1Bean.C_IDINSTITUCION+"(+)= T."+CenDatosCVBean.C_IDINSTITUCION_SUBT1+
				   " ORDER BY TIPO1."+CenTiposCVSubtipo1Bean.C_CODIGOEXT+" ASC, T."+CenDatosCVBean.C_FECHAINICIO+" DESC";	
			
				registros = selectGenericoList(select);
			
	    } catch (Exception e) {
	    	throw new ClsExceptions(e,"Error obteniendo la comisiones y los cargos."); 
   	    }
		return registros;
	}
	public boolean eliminarCargo(Hashtable hash)throws ClsExceptions, SIGAException {
		
	
		boolean eliminada;
		try{ 
			
			Hashtable h = new Hashtable();
			String [] claves = this.getClavesBean();
			for (int i = 0; i < claves.length; i++) {
				h.put (claves[i], hash.get(claves[i]));
			}
			// Procedo a borrar en la tabla -> conlleva borrado en cascada				
			eliminada=this.delete(h);
		
			
		}catch (Exception e){
			
			throw new ClsExceptions(e,"Error al borrar el cargo."); 				
		}
		return eliminada;
	}
	


	public List<CenDatosCVBean> selectGenericoList(String sql) throws ClsExceptions, SIGAException 
	{
		Vector datos = new Vector();
		List<CenDatosCVBean> lisComisiones = new ArrayList<CenDatosCVBean>();
		RowsContainer rc = new RowsContainer();
		try { 
			if (rc.findForUpdate(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
	           		Hashtable<String, Object> htFila=fila.getRow();
	           		
					CenDatosCVBean comisionesBean= new CenDatosCVBean();
					
					comisionesBean = (CenDatosCVBean)this.hashTableToBean(htFila);
					lisComisiones.add(comisionesBean);

				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e,  e.getMessage() + "Consulta SQL:"+sql);
		}
		return lisComisiones;	
	}

	public void updateInformeDatosCV(UsrBean usrBean, int idInstitucion, Long idPersona, Hashtable htDatosInforme) throws ClsExceptions {

		CenClienteAdm clienteAdm = new CenClienteAdm(new Integer(usrBean.getUserName()), usrBean, idInstitucion, idPersona);
		CenTiposCVAdm tipoCVAdm  = new CenTiposCVAdm(usrBean);
		Vector datosCV = new Vector();

		try {

			//Se rellenan todos los datos que restan
			datosCV = new Vector();
			datosCV = tipoCVAdm.getRestoDatosCV(idInstitucion);
			if (datosCV != null) {
				this.rellenarDatosCVInforme(datosCV, htDatosInforme, false);
			}
			
			//Se rellenan los datos del colegiado
			datosCV = clienteAdm.getDatosCVInforme(idPersona, idInstitucion, true);
			if (datosCV != null) {
				this.rellenarDatosCVInforme(datosCV, htDatosInforme, true);
			}

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error updateInformeDatosCV");
		}

	}
	
	public void rellenarDatosCVInforme(Vector datosCV, Hashtable htDatosInforme, boolean conDatos) {
		Vector tipo = null;
		Vector subtipo1 = null;
		Vector subtipo2 = null;
		for (int i = 0; i < datosCV.size(); i++) {
			Hashtable reg = ((Hashtable) datosCV.get(i));
			if (reg.get("IDTIPOCVSUBTIPO1") != null && !reg.get("IDTIPOCVSUBTIPO1").equals("")) {
				if (reg.get("IDTIPOCVSUBTIPO2") != null && !reg.get("IDTIPOCVSUBTIPO2").equals("")) {
					if(conDatos){
						if (subtipo2 != null && ((Hashtable) subtipo2.get(0)).get("IDTIPOCVSUBTIPO2").equals(reg.get("IDTIPOCVSUBTIPO2"))) {
							subtipo2.add(reg);
						} else {
							subtipo2 = new Vector();
							subtipo2.add(reg);
						}
						htDatosInforme.put(reg.get("IDTIPOCV") + "-" + reg.get("IDTIPOCVSUBTIPO1") + "-" + reg.get("IDTIPOCVSUBTIPO2"), subtipo2);
					}else{
						subtipo2 = new Vector();
						subtipo2.add(reg);
						htDatosInforme.put(reg.get("IDTIPOCV") + "-" + reg.get("IDTIPOCVSUBTIPO1") + "-" + reg.get("IDTIPOCVSUBTIPO2"), subtipo2);
					}

				} else {
					if(conDatos){
						if (subtipo1 != null && ((Hashtable) subtipo1.get(0)).get("IDTIPOCVSUBTIPO1").equals(reg.get("IDTIPOCVSUBTIPO1"))) {
							subtipo1.add(reg);
						} else {
							subtipo1 = new Vector();
							subtipo1.add(reg);
						}
						htDatosInforme.put(reg.get("IDTIPOCV") + "-" + reg.get("IDTIPOCVSUBTIPO1"), subtipo1);
					}else{
						subtipo1 = new Vector();
						subtipo1.add(reg);
						htDatosInforme.put(reg.get("IDTIPOCV") + "-" + reg.get("IDTIPOCVSUBTIPO1"), subtipo1);
					}
				}

			} else {
				if (tipo != null && ((Hashtable) tipo.get(0)).get("IDTIPOCV").equals(reg.get("IDTIPOCV"))) {
					tipo.add(reg);
				} else {
					tipo = new Vector();
					tipo.add(reg);
				}
				htDatosInforme.put(reg.get("IDTIPOCV"), tipo);
			}
		}
	}
}