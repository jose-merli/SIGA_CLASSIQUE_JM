/*
 * Created on 22-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenTipoIdentificacionAdm extends MasterBeanAdministrador{

	public CenTipoIdentificacionAdm(UsrBean usu) {
		super(CenTipoIdentificacionBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenTipoIdentificacionBean.C_DESCRIPCION, 			CenTipoIdentificacionBean.C_FECHAMODIFICACION,	CenTipoIdentificacionBean.C_CODIGOEXT,
							CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION, 	CenTipoIdentificacionBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenTipoIdentificacionBean bean = null;
		
		try {
			bean = new CenTipoIdentificacionBean();
			bean.setDescripcion((String)hash.get(CenTipoIdentificacionBean.C_DESCRIPCION));
			bean.setFechaMod((String)hash.get(CenTipoIdentificacionBean.C_FECHAMODIFICACION));
			bean.setCodigoExt((String)hash.get(CenTipoIdentificacionBean.C_CODIGOEXT));
			bean.setIdTipoIdentificacion(UtilidadesHash.getInteger(hash, CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenTipoIdentificacionBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenTipoIdentificacionBean b = (CenTipoIdentificacionBean) bean;
			htData.put(CenTipoIdentificacionBean.C_DESCRIPCION, b.getDescripcion());
			htData.put(CenTipoIdentificacionBean.C_FECHAMODIFICACION, b.getFechaMod());
			htData.put(CenTipoIdentificacionBean.C_CODIGOEXT, b.getCodigoExt());
			htData.put(CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION, String.valueOf(b.getIdTipoIdentificacion()));
			htData.put(CenTipoIdentificacionBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " SELECT " + CenTipoIdentificacionBean.C_CODIGOEXT + ", " +
									  CenTipoIdentificacionBean.C_DESCRIPCION + ", " +
									  CenTipoIdentificacionBean.C_FECHAMODIFICACION + ", " +
									  CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION + ", " +
									  CenTipoIdentificacionBean.C_USUMODIFICACION + 
						   " FROM " + CenTipoIdentificacionBean.T_NOMBRETABLA;
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}

	public Vector selectGenericaBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " SELECT " + CenTipoIdentificacionBean.C_CODIGOEXT + ", " +
			  CenTipoIdentificacionBean.C_DESCRIPCION + ", " +
			  CenTipoIdentificacionBean.C_FECHAMODIFICACION + ", " +
			  CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION + ", " +
			  CenTipoIdentificacionBean.C_USUMODIFICACION + 
			  " FROM " + CenTipoIdentificacionBean.T_NOMBRETABLA;
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryBind(sql,data)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}	
	/**
	 * A falta de bean le metemos este 
	 * 
	 */
	public List<CenTipoIdentificacionBean>  getTipoPersona(String idlenguaje)throws ClsExceptions{

		
		StringBuffer sql = new StringBuffer();
		
		sql.append("Select Decode(Idrecurso,  'gratuita.personaJG.literal.tipoJuridica', 'J',");
		sql.append(" 'gratuita.personaJG.literal.tipoFisica','F','gratuita.personaJG.literal.otra','O') As IDTIPO, Descripcion ");
		sql.append("  From (Select Idrecurso, Descripcion From Gen_Recursos ");
		sql.append(" Where (Idrecurso = 'gratuita.personaJG.literal.tipoFisica' Or Idrecurso = 'gratuita.personaJG.literal.tipoJuridica' Or Idrecurso = 'gratuita.personaJG.literal.otra')");
        sql.append(" And Idlenguaje = "+idlenguaje+") Order By IDTIPO");
		
		List<CenTipoIdentificacionBean> alTipos = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.find(sql.toString())) {
            	alTipos = new ArrayList<CenTipoIdentificacionBean>();
            	CenTipoIdentificacionBean tipoBean = null;            	
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();     		
            		StringBuffer peticiontipos = new StringBuffer();
            		tipoBean = new CenTipoIdentificacionBean();            	
            		tipoBean.setIdTipo(UtilidadesHash.getString(htFila,"IDTIPO"));            		
            		tipoBean.setDescripcion(UtilidadesHash.getString(htFila,CenTipoIdentificacionBean.C_DESCRIPCION));            		
            		alTipos.add(tipoBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alTipos;
		
		
		
	}
	
	public List<CenTipoIdentificacionBean>  getTiposIdentificacion(String idlenguaje)throws ClsExceptions{

		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT IDTIPOIDENTIFICACION, f_siga_getrecurso (DESCRIPCION,"+idlenguaje+") as DESCRIPCION ");
		sql.append(" FROM CEN_TIPOIDENTIFICACION where IDTIPOIDENTIFICACION<>20  order by IDTIPOIDENTIFICACION ");		
      
        List<CenTipoIdentificacionBean> alTipos = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.find(sql.toString())) {
            	alTipos = new ArrayList<CenTipoIdentificacionBean>();
            	CenTipoIdentificacionBean tipoBean = null;
            	if(rc.size()>1){
            		tipoBean = new CenTipoIdentificacionBean();
            		tipoBean.setIdTipoIdentificacion(new Integer("1"));            		
	    			tipoBean.setDescripcion(UtilidadesString.getMensajeIdioma(this.usrbean, "general.combo.seleccionar"));	    			
	            	alTipos.add(tipoBean);
            		
            		
            	}
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();     		
            		StringBuffer peticiontipos = new StringBuffer();
            		tipoBean = new CenTipoIdentificacionBean();            	
            		tipoBean.setIdTipoIdentificacion(UtilidadesHash.getInteger(htFila,CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION));            		
            		tipoBean.setDescripcion(UtilidadesHash.getString(htFila,CenTipoIdentificacionBean.C_DESCRIPCION));            		
            		alTipos.add(tipoBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alTipos;
		
		
		
	}
	
	
	public List<CenTipoIdentificacionBean>  getTiposIdentificaciones(String idlenguaje,String idtipoidentificacion)throws ClsExceptions{

		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT IDTIPOIDENTIFICACION, f_siga_getrecurso (DESCRIPCION,"+idlenguaje+") as DESCRIPCION ");
		if (idtipoidentificacion.equals("F")){			
			sql.append(" FROM CEN_TIPOIDENTIFICACION where IDTIPOIDENTIFICACION<>20  order by IDTIPOIDENTIFICACION ");		
		}else{			
			sql.append(" From Cen_Tipoidentificacion where IDTIPOIDENTIFICACION=20 or  IDTIPOIDENTIFICACION=50 Order By Idtipoidentificacion ");		
			
		}
		
        List<CenTipoIdentificacionBean> alTipos = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.find(sql.toString())) {
            	alTipos = new ArrayList<CenTipoIdentificacionBean>();
            	CenTipoIdentificacionBean tipoBean = null;
            	if(rc.size()>1){
            		tipoBean = new CenTipoIdentificacionBean();
            		tipoBean.setIdTipoIdentificacion(new Integer("1"));            		
	    			tipoBean.setDescripcion(UtilidadesString.getMensajeIdioma(this.usrbean, "general.combo.seleccionar"));	    			
	            	alTipos.add(tipoBean);
            		
            		
            	}
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();     		
            		StringBuffer peticiontipos = new StringBuffer();
            		tipoBean = new CenTipoIdentificacionBean();            	
            		tipoBean.setIdTipoIdentificacion(UtilidadesHash.getInteger(htFila,CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION));            		
            		tipoBean.setDescripcion(UtilidadesHash.getString(htFila,CenTipoIdentificacionBean.C_DESCRIPCION));            		
            		alTipos.add(tipoBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return alTipos;
}
	
}
