/*
 * Created on Dec 30, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.ArrayList;
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
import com.siga.Utilidades.UtilidadesString;
import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.SIGAException;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenComponentesAdm extends MasterBeanAdministrador {

	public CenComponentesAdm (UsrBean usu) {
		super (CenComponentesBean.T_NOMBRETABLA, usu);
	}	

	/**
	 * Devuelve un array con el nombre de los campos de la tabla CEN_COMPONENTES 
	 * @author nuria.rgonzalez 30-12-04	 
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenComponentesBean.C_IDPERSONA, 
				CenComponentesBean.C_IDINSTITUCION,		CenComponentesBean.C_IDCOMPONENTE,
				CenComponentesBean.C_CARGO,				CenComponentesBean.C_FECHACARGO,
				CenComponentesBean.C_CEN_CLIENTE_IDPERSONA,
				CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION,
				CenComponentesBean.C_SOCIEDAD, 			CenComponentesBean.C_IDCUENTA, 
				CenComponentesBean.C_IDTIPOCOLEGIO,		CenComponentesBean.C_NUMCOLEGIADO,
				CenComponentesBean.C_CAPITALSOCIAL,		CenComponentesBean.C_IDCARGO,
				CenComponentesBean.C_IDPROVINCIA,     	CenComponentesBean.C_FECHABAJA,
				CenComponentesBean.C_FECHAMODIFICACION,	CenComponentesBean.C_USUMODIFICACION};
		return campos;
	}

	/**
	 * Devuelve un array con el nombre de los campos clave de la tabla CEN_COMPONENTES 
	 * @author nuria.rgonzalez 30-12-04	  
	 */
	protected String[] getClavesBean() {
		String[] campos = { CenComponentesBean.C_IDINSTITUCION, 
							CenComponentesBean.C_IDPERSONA,			
							CenComponentesBean.C_IDCOMPONENTE};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	/**
	 * Devuelve un CenComponentesBean con los campos de la tabla CEN_COMPONENTES 
	 * @author nuria.rgonzalez 30-12-04	 
	 * @param Hashtable 
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenComponentesBean bean = null;
		try{
			bean = new CenComponentesBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenComponentesBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash,CenComponentesBean.C_IDPERSONA));
			bean.setIdComponente(UtilidadesHash.getInteger(hash,CenComponentesBean.C_IDCOMPONENTE));
			bean.setCargo(UtilidadesHash.getString(hash,CenComponentesBean.C_CARGO));
			bean.setFechaCargo(UtilidadesHash.getString(hash,CenComponentesBean.C_FECHACARGO));
			bean.setCen_Cliente_IdPersona(UtilidadesHash.getString(hash,CenComponentesBean.C_CEN_CLIENTE_IDPERSONA));
			bean.setCen_Cliente_IdInstitucion(UtilidadesHash.getInteger(hash,CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION));
			bean.setSociedad(UtilidadesHash.getString(hash, CenComponentesBean.C_SOCIEDAD));
			bean.setIdCuenta(UtilidadesHash.getInteger(hash, CenComponentesBean.C_IDCUENTA));
			bean.setIdTipoColegio(UtilidadesHash.getString(hash, CenComponentesBean.C_IDTIPOCOLEGIO));
			bean.setNumColegiado(UtilidadesHash.getString(hash, CenComponentesBean.C_NUMCOLEGIADO));
			bean.setCapitalSocial(UtilidadesHash.getFloat(hash, CenComponentesBean.C_CAPITALSOCIAL));
			bean.setIdCargo(UtilidadesHash.getString(hash, CenComponentesBean.C_IDCARGO));
			bean.setIdProvincia(UtilidadesHash.getString(hash, CenComponentesBean.C_IDPROVINCIA));
			bean.setFechaBaja(UtilidadesHash.getString(hash,CenComponentesBean.C_FECHABAJA));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenComponentesBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenComponentesBean.C_USUMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/**
	 * Devuelve un Hashtable con los campos de la tabla CEN_COMPONENTES 
	 * @author nuria.rgonzalez 12-01-05
	 * @version 2	 
	 * @param CenComponentesBean 
	 */

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CenComponentesBean b = (CenComponentesBean) bean;
			UtilidadesHash.set(hash, CenComponentesBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CenComponentesBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(hash, CenComponentesBean.C_IDCOMPONENTE, b.getIdComponente());			
			UtilidadesHash.set(hash, CenComponentesBean.C_CARGO, b.getCargo());
			UtilidadesHash.set(hash, CenComponentesBean.C_FECHACARGO, b.getFechaCargo());
			UtilidadesHash.set(hash, CenComponentesBean.C_CEN_CLIENTE_IDPERSONA, b.getCen_Cliente_IdPersona());
			UtilidadesHash.set(hash, CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION, b.getCen_Cliente_IdInstitucion());	
			UtilidadesHash.set(hash, CenComponentesBean.C_SOCIEDAD, b.getSociedad());	
			UtilidadesHash.set(hash, CenComponentesBean.C_IDCUENTA, b.getIdCuenta());
			UtilidadesHash.set(hash, CenComponentesBean.C_IDTIPOCOLEGIO, b.getIdTipoColegio());
			UtilidadesHash.set(hash, CenComponentesBean.C_NUMCOLEGIADO, b.getNumColegiado());
	
			if (b.getCapitalSocial()!=null && b.getCapitalSocial().doubleValue()!=0.0){
				UtilidadesHash.set(hash, CenComponentesBean.C_CAPITALSOCIAL, b.getCapitalSocial());
			}else{
				UtilidadesHash.set(hash, CenComponentesBean.C_CAPITALSOCIAL, "");
			}
			
			UtilidadesHash.set(hash, CenComponentesBean.C_IDCARGO, b.getIdCargo());
			UtilidadesHash.set(hash, CenComponentesBean.C_IDPROVINCIA, b.getIdProvincia());
			UtilidadesHash.set(hash, CenComponentesBean.C_FECHABAJA, b.getFechaBaja());	
			UtilidadesHash.set(hash, CenComponentesBean.C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, CenComponentesBean.C_USUMODIFICACION, b.getUsuMod());	
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	/**
	 * Devuelve un array con el nombre de los campos de la tablas CEN_COMPONENTES, CEN_PERSONA, CEN_CUENTASBANCARIAS para construir la Query.
	 * @author nuria.rgonzalez 30-12-04	 
	 */
	protected String[] getCamposComponentes() {
		String[] campos = {	CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_IDINSTITUCION,	
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_IDPERSONA,	
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_IDCOMPONENTE,				
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_CARGO,	
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_FECHACARGO,	
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_FECHABAJA,	
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_CEN_CLIENTE_IDPERSONA,		
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_CEN_CLIENTE_IDINSTITUCION,
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_SOCIEDAD,
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_IDCUENTA,
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_IDTIPOCOLEGIO,
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_NUMCOLEGIADO,
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_CAPITALSOCIAL,
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_IDCARGO,
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_IDPROVINCIA,
							CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_CAPITALSOCIAL,
							
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF,	
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE,	
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1,	
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2,	
							
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO,						
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CODIGOSUCURSAL,		
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_DIGITOCONTROL,	
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_NUMEROCUENTA,
							"TO_CHAR(CEN_COMPONENTES.FECHACARGO, 'dd/mm/yyyy') AS FECHACARGOINFORME",
							"DECODE(f_siga_gettipocliente(CEN_COMPONENTES.CEN_CLIENTE_IDPERSONA,CEN_COMPONENTES.CEN_CLIENTE_IDINSTITUCION,SYSDATE),20,1,0) AS EJERCIENTE"};
		return campos;
	}
	
	/**
	 * Devuelve un array con el nombre de las tablas tablas CEN_COMPONENTES, CEN_PERSONA, CEN_CUENTASBANCARIAS y las relaciones entre ellas para construir la Query.
	 * @author nuria.rgonzalez 30-12-04	 
	 */
	protected String getTablasComponentes(){

		String campos = CenComponentesBean.T_NOMBRETABLA + 
			" INNER JOIN " + CenPersonaBean.T_NOMBRETABLA +
		 	" ON " + CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_CEN_CLIENTE_IDPERSONA + " = " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + 
			" LEFT JOIN " + CenCuentasBancariasBean.T_NOMBRETABLA + 
			" ON " + CenComponentesBean.T_NOMBRETABLA +"."+ CenComponentesBean.C_IDINSTITUCION + " = " + CenCuentasBancariasBean.T_NOMBRETABLA +"."+ CenCuentasBancariasBean.C_IDINSTITUCION + 
				" AND " + CenComponentesBean.T_NOMBRETABLA +"."+ CenComponentesBean.C_IDPERSONA + " = " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + 
				" AND " + CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_IDCUENTA + " = " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA; 	 		

/* RGG 17-02-2005 cambio los LEFT JOIN por COMAS para 
 *  el tratamiento de visibilidad de campos (NO APLICADO)
 * 		

		String campos = CenComponentesBean.T_NOMBRETABLA + 
		" , "+ // INNER JOIN 
		CenPersonaBean.T_NOMBRETABLA +
		"(+) , "+ // LEFT JOIN (preguntar a ana)
		CenCuentasBancariasBean.T_NOMBRETABLA +
		" WHERE "+
		// cruce del inner join
		CenComponentesBean.T_NOMBRETABLA +"."+ CenComponentesBean.C_CEN_CLIENTE_IDPERSONA + "=" +
		CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA  +
		" AND "+ 
		// cruce del left join
		CenComponentesBean.T_NOMBRETABLA +"."+ CenComponentesBean.C_IDINSTITUCION + "=" +
		CenCuentasBancariasBean.T_NOMBRETABLA +"."+ CenCuentasBancariasBean.C_IDINSTITUCION+ 
		" AND " +
		CenComponentesBean.T_NOMBRETABLA +"."+ CenComponentesBean.C_IDPERSONA + "=" +
		CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA; 	 		
*/
		return campos;
	}
	
	/**
	 * Devuelve un array con el nombre de los campos por los cuales se ordenarán los resultados de la Query.
	 * @author nuria.rgonzalez 30-12-04	 
	 */
	protected String[] getOrdenComponentes(){
		String[] campos = {CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF };
		return campos;
	}
	
	/**
	 * Devuelve un vector con los datos de los compoentes del cliente pasado como parámetro
	 * @author nuria.rgonzalez 18-01-05	
	 * @version 2 
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de la que vamos a obtener los datos.
	 * @param incluirHistorico, true si queremos los componentes dados de baja, false si solo queremos los activos
	 */	
	public Vector selectComponentes(Long idPersona, Integer idInstitucion, boolean incluirHistorico)  throws ClsExceptions, SIGAException{
		Vector v = new Vector();
		RowsContainer rc = null;
		String where = " WHERE " + CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_IDPERSONA + " = " + idPersona +
					   " AND "   + CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_IDINSTITUCION + " = " + idInstitucion;
		if(!incluirHistorico){
			where += " AND (" + CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_FECHABAJA + " IS NULL or "
					 + CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_FECHABAJA + " > SYSDATE)";
		}
					   
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasComponentes(), this.getCamposComponentes());
			
			sql += where;
			sql += UtilidadesBDAdm.sqlOrderBy(this.getOrdenComponentes());
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			} 
		}
		catch(ClsExceptions e){
			throw new ClsExceptions (e, "Error en selectComponentes");
		}
		return v;
	}

	public List<ValueKeyVO> getCuentas(String idPersona, String idInstitucion)  throws ClsExceptions, SIGAException{
		Vector v = null;
		RowsContainer rc = null;
		CenComponentesBean cuentas= new CenComponentesBean();
		List<ValueKeyVO> valueKeyVOs = null;
		try{
			rc = new RowsContainer(); 
			String cuentasSJCSSociedad = "select C.IDCUENTA || '*' || C.IDPERSONA AS ID, substr(B.NOMBRE, 0, 25) || ' ' || 'nº ' || C.cbo_codigo || '-' " +
			"||  C.codigosucursal || '-' || C.digitocontrol || '-' ||  LPAD(SUBSTR(C.numerocuenta, 7), 10, '*') as DESCRIPCION   " +
			"from cen_cuentasbancarias C, cen_bancos B, CEN_COMPONENTES A  where C.cbo_codigo = B.codigo AND C.IDPERSONA = A.Idpersona " +
			"AND A.IDPERSONA = "+idPersona+"  AND C.IDINSTITUCION = "+idInstitucion+"  AND (C.ABONOCARGO = 'A' OR C.ABONOCARGO = 'T')  " +
			"AND C.ABONOSJCS = 1   and c.fechabaja IS NULL    ORDER BY SOCIEDAD desc, A.IDCUENTA desc, DESCRIPCION";

			if (rc.query(cuentasSJCSSociedad)) {

				for (int i = 0; i < rc.size(); i++)	{
					valueKeyVOs = new ArrayList<ValueKeyVO>();
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro == null && i==0) {
						ValueKeyVO vk = new ValueKeyVO();
						vk.setKey("");
						vk.setValue("");
						valueKeyVOs.add(vk);
					}
					if (registro != null) {
						ValueKeyVO vk = new ValueKeyVO();
						vk.setValue((String)registro.get("ID"));
						vk.setKey((String)registro.get("DESCRIPCION"));
						valueKeyVOs.add(vk);
					}
				}
			}
		}
		catch(ClsExceptions e){
			throw new ClsExceptions (e, "Error en selectComponentes");
		}
		return valueKeyVOs;
	}
	public String getPersonaSociedad (String idPersona, String idInstitucion)throws ClsExceptions, SIGAException 
	{
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sJCSSociedades = "select a.IDPERSONA  AS ID,  P.NOMBRE || ' ' || P.APELLIDOS1 as DESCRIPCION  " +
			"from cen_cuentasbancarias C,  cen_bancos B, cen_persona p, CEN_COMPONENTES A   where  p.IDPERSONA = A.Idpersona  " +
			"AND A.CEN_CLIENTE_IDPERSONA = "+idPersona+" AND A.IDINSTITUCION = "+idInstitucion+"    AND C.cbo_codigo = B.codigo    " +
			"AND C.IDPERSONA = A.Idpersona    AND (C.ABONOCARGO = 'A' OR C.ABONOCARGO = 'T')     AND C.ABONOSJCS = 1     " +
			"and c.fechabaja IS NULL ORDER BY DESCRIPCION";
			
			if (rc.query(sJCSSociedades)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				String idPerSociedad = UtilidadesHash.getString(prueba, "ID");
	
				 return idPerSociedad;								
			}
		}	
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getPersonaSociedad' en B.D.");		
		}
		return null;
	}	



	/**
	 * Devuelve un Hastable con los datos de la direccion del cliente pasado como parámetro.
	 * @author nuria.rgonzalez 18-01-05 
	 * @version 2
	 * @param idPersona, es el identificador de la persona de al que vamos a obtener los datos. 
	 * @param idInstitucion, es el identificador de la institucion de la persona de la que vamos a obtener los datos.
	 * @param idComponente, es el identificador del componente del quequeremos obtener los datos.  
	 */
	public Hashtable selectComponentes(Long idPersona, Integer idInstitucion, Integer idComponente) throws ClsExceptions, SIGAException{
		Hashtable registro = null;
		RowsContainer rc = null;
		String where = " WHERE " + CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_IDPERSONA + " = " + idPersona +
		  " AND " + CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_IDINSTITUCION + " = " + idInstitucion +
		  " AND " + CenComponentesBean.T_NOMBRETABLA + "." + CenComponentesBean.C_IDCOMPONENTE + " = " + idComponente;
		try{
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.getTablasComponentes(), this.getCamposComponentes());
			
			sql += where;
			sql += UtilidadesBDAdm.sqlOrderBy(this.getOrdenComponentes());
			if (rc.query(sql)) {
				if(rc.size()>0){
					Row fila = (Row) rc.get(0);
					registro = (Hashtable)fila.getRow(); 					
				}
			}
		}
		catch(ClsExceptions e){
			throw new ClsExceptions (e, "Error en selectComponentes");
		}
		return registro;
	}

	/**
	 * Inserta los datos de un componente y rellena la tabla de historicos (CEN_HISTORICO)
	 * @author daniel.campos 25-01-05
	 * @version 1	 
	 * @param beanComponentes datos del componente
	 * @param BeanNoColegiado datos del no colegiado, si es necesario insertarlo. Puede ser 'null'
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico
	 */
	public boolean insertarConHistorico (CenComponentesBean beanComponentes, CenNoColegiadoBean beanNoColegiado, CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			
			// Insertamos el No colegiado si no es null
			if (beanNoColegiado != null) {
				// Insertamos el no Colegiado
				CenNoColegiadoAdm admNoCol = new CenNoColegiadoAdm (this.usrbean);
				if (!(admNoCol.insert(beanNoColegiado))) {
					return false;
				}
			}
			
			// Insertamos la direccion
			beanComponentes.setIdComponente(this.getNuevoID(beanComponentes));
			if (insert(beanComponentes)) {
				// Insertamos el historico
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
				if (admHis.insertCompleto(beanHis, beanComponentes, CenHistoricoAdm.ACCION_INSERT, idioma)) {
					return true;
				}
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

	/**
	 * Obtiene un nuevo ID del curriculum de una persona e institucion determinada
	 * @author daniel.campos 26-01-05
	 * @version 1	 
	 * @param Bean con los datos del componente.
	 * @return nuevo ID.
	 */
	public Integer getNuevoID (CenComponentesBean bean)throws ClsExceptions, SIGAException 
	{
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " SELECT (MAX(" + CenComponentesBean.C_IDCOMPONENTE + ") + 1) AS " + CenComponentesBean.C_IDCOMPONENTE + 
			  			 " FROM " + this.nombreTabla +
						 " WHERE IDPERSONA = " + bean.getIdPersona() +
						 " AND IDINSTITUCION = " + bean.getIdInstitucion();

			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				Integer idComponente = UtilidadesHash.getInteger(prueba, CenComponentesBean.C_IDCOMPONENTE);
				if (idComponente == null) {
					return new Integer(1);
				}
				else return idComponente;								
			}
		}	
		catch (Exception e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoID' en B.D.");		
		}
		return null;
	}	

	/**
	 * Borrar los datos de un componente y rellena la tabla de historicos (CEN_HISTORICO)
	 * @author daniel.campos 26-01-05
	 * @version 1	 
	 * @param Hash con los datos del componente.
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico.
	 */
	public boolean deleteConHistorico (Hashtable clavesComponente, CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			CenComponentesBean beanComponente = (CenComponentesBean) this.selectByPK(clavesComponente).get(0);
			// Cambiamos el borrado fisico por una baja logica añadiendo la fechaBaja
			beanComponente.setFechaBaja(UtilidadesString.getTimeStamp(ClsConstants.DATE_FORMAT_JAVA));
			if(update(beanComponente)){
			// if (delete(clavesComponente)) {
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
				if (admHis.insertCompleto(beanHis, beanComponente, CenHistoricoAdm.ACCION_DELETE, idioma)) {
					return true;
				}
			}
			return false;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al borrar datos en B.D.");
		}
	}
	
	/**
	 * Actualiza los datos de un componente y rellena la tabla de historicos (CEN_HISTORICO)
	 * @author daniel.campos 21-01-05
	 * @version 1	 
	 * @param BeanComponente datos del componente
	 * @param BeanHis con el motivo y el tipo, para almacenar en el Historico
	 */
	public boolean updateConHistorico (CenComponentesBean beanComponente, CenHistoricoBean beanHis, String idioma) throws ClsExceptions, SIGAException 
	{
		try {
			if (update(beanToHashTable(beanComponente), beanComponente.getOriginalHash())) {
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
				if (admHis.insertCompleto(beanHis, beanComponente, CenHistoricoAdm.ACCION_UPDATE, idioma)) {
					return true;
				}
			}
			return false;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al actualizar los datos en B.D.");
		}
	}
}