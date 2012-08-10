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
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 * Modificado el 21-12-2004 por david.sanchezp
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenPoblacionesAdm extends MasterBeanAdministrador {

	public CenPoblacionesAdm(UsrBean usu) {
		super(CenPoblacionesBean.T_NOMBRETABLA, usu);
	}

	public String[] getCamposBean() {
		String [] campos = {CenPoblacionesBean.C_FECHAMODIFICACION, CenPoblacionesBean.C_IDPARTIDO,CenPoblacionesBean.C_CODIGOEXT,
							CenPoblacionesBean.C_IDPOBLACION, 		CenPoblacionesBean.C_IDPROVINCIA,
							CenPoblacionesBean.C_NOMBRE,			CenPoblacionesBean.C_USUMODIFICACION};
		return campos;
	}

	public String[] getClavesBean() {
		String [] claves = {CenPoblacionesBean.C_IDPOBLACION};
		return claves;
	}

	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenPoblacionesBean bean = null;
		
		try {
			bean = new CenPoblacionesBean();
			bean.setFechaMod(UtilidadesHash.getString(hash, CenPoblacionesBean.C_FECHAMODIFICACION));
			bean.setIdPartido(UtilidadesHash.getInteger(hash, CenPoblacionesBean.C_IDPARTIDO));
			bean.setIdPoblacion(UtilidadesHash.getString(hash, CenPoblacionesBean.C_IDPOBLACION));
			bean.setidProvincia(UtilidadesHash.getString(hash, CenPoblacionesBean.C_IDPROVINCIA));
			bean.setNombre(UtilidadesHash.getString(hash, CenPoblacionesBean.C_NOMBRE));
			bean.setIne(UtilidadesHash.getString(hash, CenPoblacionesBean.C_INE));
			bean.setIdPoblacionMunicipio(UtilidadesHash.getString(hash, CenPoblacionesBean.C_IDPOBLACIONMUNICIPIO));
			bean.setCodigoExt(UtilidadesHash.getString(hash, CenPoblacionesBean.C_CODIGOEXT));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenPoblacionesBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenPoblacionesBean b = (CenPoblacionesBean) bean;
			UtilidadesHash.set(htData, CenPoblacionesBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenPoblacionesBean.C_IDPARTIDO, String.valueOf(b.getIdPartido()));
			UtilidadesHash.set(htData, CenPoblacionesBean.C_IDPOBLACION, b.getIdPoblacion());
			UtilidadesHash.set(htData, CenPoblacionesBean.C_IDPROVINCIA, b.getIdProvincia());
			UtilidadesHash.set(htData, CenPoblacionesBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, CenPoblacionesBean.C_INE, b.getIne());
			UtilidadesHash.set(htData, CenPoblacionesBean.C_IDPOBLACIONMUNICIPIO, b.getIdPoblacionMunicipio());
			UtilidadesHash.set(htData, CenPoblacionesBean.C_CODIGOEXT, b.getCodigoExt());
			UtilidadesHash.set(htData, CenPoblacionesBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
	/**
	 * Devuelve la consulta SQL para obtener a partir de un id su fecha y usuario de modificacion. 
	 *
	 * @param String id: identidicador de la tabla.
	 * 
	 * @return String con la consulta SQL. 
	 */
	public String getFechaYUsu(String id){
		String sql = "select "+CenPoblacionesBean.C_FECHAMODIFICACION+","+CenPoblacionesBean.C_USUMODIFICACION+" FROM "+CenPoblacionesBean.T_NOMBRETABLA+" WHERE "+CenPoblacionesBean.C_IDPOBLACION+" = '"+id+"'";		
		return sql;
	}
	public Vector selectGenerico(String consulta) throws ClsExceptions,SIGAException {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try {
			RowsContainer rc = new RowsContainer(); 	
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					Hashtable registro2 = new Hashtable();
					if (registro != null) 
						datos.add(registro);
				}
			}			
		} 
		catch (ClsExceptions e) {
			throw e;		
		}	
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en CenPoblacionesAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}

	public String getDescripcion(String id) throws ClsExceptions,SIGAException {
		String salida = "";
		
		// Acceso a BBDD	
		try {
			Hashtable ht = new Hashtable();
			ht.put(CenPoblacionesBean.C_IDPOBLACION,id);
			Vector v = this.selectByPK(ht);
			if (v!=null && v.size()>0) {
				CenPoblacionesBean b = (CenPoblacionesBean) v.get(0);
				salida = b.getNombre();
			}			
		}	
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en getDescripcion()");
		}
		return salida;	
	}
	public List<CenPoblacionesBean> getPoblaciones(String idProvincia)throws ClsExceptions{

		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT IDPOBLACION, NOMBRE ");
		sql.append(" FROM CEN_POBLACIONES  ");
		sql.append(" WHERE IDPROVINCIA = :");
	    contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idProvincia);
		sql.append(" ORDER BY NOMBRE ");
		
		List<CenPoblacionesBean> alPoblaciones = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alPoblaciones = new ArrayList<CenPoblacionesBean>();
            	CenPoblacionesBean poblacionesBean = null;
            	
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		
            		
            		poblacionesBean = new CenPoblacionesBean();
            		poblacionesBean.setIdPoblacion(UtilidadesHash.getString(htFila,CenPoblacionesBean.C_IDPOBLACION));
            		poblacionesBean.setNombre(UtilidadesHash.getString(htFila,CenPoblacionesBean.C_NOMBRE));
            		alPoblaciones.add(poblacionesBean);
            	}
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta getPoblaciones.");
       }
       return alPoblaciones;
		
	}
	
 	public List<CenPoblacionesBean> getPoblacionesProvincia(UsrBean usuario, CenPoblacionesBean poblBean) throws ClsExceptions{
 		List<CenPoblacionesBean> listaPoblaciones = null; 		
		Hashtable codigosBind = new Hashtable();
		Boolean sinFiltro = false;
		
		try{
    		
		    String sql = " SELECT "+CenPoblacionesBean.T_NOMBRETABLA+"."+CenPoblacionesBean.C_IDPROVINCIA+", "+
		    		CenPoblacionesBean.T_NOMBRETABLA+"."+CenPoblacionesBean.C_IDPOBLACION+", "+
		    		CenPoblacionesBean.T_NOMBRETABLA+"."+CenPoblacionesBean.C_NOMBRE+", "+
		    		CenPoblacionesBean.T_NOMBRETABLA+"."+CenPoblacionesBean.C_PRIORIDAD;
		    
		    if (poblBean.getNombre()==null||poblBean.getNombre().trim().equalsIgnoreCase("")) {
		    	codigosBind.put(1,'*');
		    	sinFiltro=true;
		    }
		    	
		    else {		    	
		    	
		    	sql+=", LENGTH(FILTRO.VALOR)-LENGTH("+CenPoblacionesBean.T_NOMBRETABLA+"."+CenPoblacionesBean.C_NOMBRE+") AS "+CenPoblacionesBean.C_SELECCIONADO;
		    	codigosBind.put(1,poblBean.getNombre());
		    }		    
		    		
		    sql+=	" FROM " + CenPoblacionesBean.T_NOMBRETABLA+
		    		" , (SELECT :1 AS VALOR FROM DUAL) FILTRO "+
		    		" WHERE "+CenPoblacionesBean.T_NOMBRETABLA+"."+CenPoblacionesBean.C_IDPROVINCIA+"=:2 "+
		    		" AND REGEXP_LIKE("+CenPoblacionesBean.T_NOMBRETABLA+"."+CenPoblacionesBean.C_NOMBRE+", FILTRO.VALOR) "+
		    		" ORDER BY "+CenPoblacionesBean.T_NOMBRETABLA+"."+CenPoblacionesBean.C_PRIORIDAD+" ASC, "+
		    		CenPoblacionesBean.T_NOMBRETABLA+"."+CenPoblacionesBean.C_NOMBRE+" ASC ";	    

		    codigosBind.put(2,poblBean.getIdProvincia());
		    
		    RowsContainer rc = new RowsContainer();

            rc.findNLSBind(sql,codigosBind);
                                
 			if (rc!=null) { 				 				
 				listaPoblaciones = new ArrayList<CenPoblacionesBean>();
 				CenPoblacionesBean poblacionBean = new CenPoblacionesBean();
 				poblacionBean.setNombre(UtilidadesString.getMensajeIdioma(usuario,"general.combo.seleccionar"));
 				poblacionBean.setIdPoblacion("");
 				poblacionBean.setPriodidad(0);
 				listaPoblaciones.add(poblacionBean);
 				
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) { 
						poblacionBean = new CenPoblacionesBean();
						poblacionBean.setNombre(UtilidadesHash.getString(registro,poblacionBean.C_NOMBRE));
						poblacionBean.setIdPoblacion(UtilidadesHash.getString(registro,poblacionBean.C_IDPOBLACION));
						poblacionBean.setidProvincia(UtilidadesHash.getString(registro,poblacionBean.C_IDPROVINCIA));
						
						if (UtilidadesHash.getInteger(registro,poblacionBean.C_PRIORIDAD)!=null)
							poblacionBean.setPriodidad(UtilidadesHash.getInteger(registro,poblacionBean.C_PRIORIDAD));
						else
							poblacionBean.setPriodidad(-1);
						
						if (sinFiltro)
							poblacionBean.setSeleccionado(false);
						else
							poblacionBean.setSeleccionado(UtilidadesHash.getInteger(registro,poblacionBean.C_SELECCIONADO)==0);
						
						listaPoblaciones.add(poblacionBean);
					}
				}
			}
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getPoblacionesProvincia");
		}
		return listaPoblaciones;
	}    	
}
