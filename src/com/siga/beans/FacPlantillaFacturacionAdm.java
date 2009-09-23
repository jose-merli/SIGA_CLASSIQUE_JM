/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


public class FacPlantillaFacturacionAdm extends MasterBeanAdministrador {
	
	public FacPlantillaFacturacionAdm (UsrBean usu) {
		super (FacPlantillaFacturacionBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacPlantillaFacturacionBean.C_IDINSTITUCION, 		
							FacPlantillaFacturacionBean.C_IDPLANTILLA,
							FacPlantillaFacturacionBean.C_DESCRIPCION, 
							FacPlantillaFacturacionBean.C_PLANTILLAPDF, 
							FacPlantillaFacturacionBean.C_FECHAMODIFICACION,
							FacPlantillaFacturacionBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacPlantillaFacturacionBean.C_IDINSTITUCION, FacPlantillaFacturacionBean.C_IDPLANTILLA};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacPlantillaFacturacionBean bean = null;
		
		try {
			bean = new FacPlantillaFacturacionBean();
			bean.setIdInstitucion	(UtilidadesHash.getInteger(hash, FacPlantillaFacturacionBean.C_IDINSTITUCION));
			bean.setIdPlantilla		(UtilidadesHash.getInteger(hash, FacPlantillaFacturacionBean.C_IDPLANTILLA));
			bean.setDescripcion		(UtilidadesHash.getString(hash, FacPlantillaFacturacionBean.C_DESCRIPCION));
			bean.setPlantillaPDF	(UtilidadesHash.getString(hash, FacPlantillaFacturacionBean.C_PLANTILLAPDF));
			bean.setFechaMod		(UtilidadesHash.getString(hash, FacPlantillaFacturacionBean.C_FECHAMODIFICACION));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash, FacPlantillaFacturacionBean.C_USUMODIFICACION));
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
			FacPlantillaFacturacionBean b = (FacPlantillaFacturacionBean) bean;
			UtilidadesHash.set(htData, FacPlantillaFacturacionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacPlantillaFacturacionBean.C_IDPLANTILLA, b.getIdPlantilla());
			UtilidadesHash.set(htData, FacPlantillaFacturacionBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, FacPlantillaFacturacionBean.C_PLANTILLAPDF, b.getPlantillaPDF());
			UtilidadesHash.set(htData, FacPlantillaFacturacionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacPlantillaFacturacionBean.C_USUMODIFICACION, b.getUsuMod());
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
	 * Obtiene el valor IDPLANTILLA <br/>
	 * @param  institucion - identificador de la institucion 
	 * @return  Integer - Siguiente identificador de plantilla  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Integer getNuevoID (String institucion) throws ClsExceptions, SIGAException 
	{

		Integer resultado=null;
		
		try { 
            RowsContainer rc = new RowsContainer();

			String sql;
			sql ="SELECT (MAX(IDPLANTILLA) + 1) AS IDPLANTILLA FROM " + nombreTabla +
				" WHERE IDINSTITUCION =" + institucion;
		
			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDPLANTILLA").equals("")) {
					resultado=new Integer(1);
				}
				else 
					resultado=UtilidadesHash.getInteger(prueba, "IDPLANTILLA");;								
			}
		}	
//		catch (SIGAException e) {
//			throw e;
//		}

		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoId' en B.D.");		
		}
		return resultado;
	}
	
	
	/** 
	 * Recoge la plantilla relacionada con la serie de facturacion pasada como parametro
	 * @param  institucion - identificador de la institucion
	 * @param  seriefacturacion - identificador de la serie de facturacion
	 * @return  Vector - plantillas asociadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector getPlantillaSerieFacturacion(String institucion, String serieFacturacion) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            Hashtable codigos = new Hashtable();
	            codigos.put(new Integer(1), serieFacturacion);
	            codigos.put(new Integer(2), institucion);
	            String sql ="SELECT " +
    						FacPlantillaFacturacionBean.T_NOMBRETABLA + "." + FacPlantillaFacturacionBean.C_IDINSTITUCION + "," +
    						FacPlantillaFacturacionBean.T_NOMBRETABLA + "." + FacPlantillaFacturacionBean.C_IDPLANTILLA + "," +
    						FacPlantillaFacturacionBean.T_NOMBRETABLA + "." + FacPlantillaFacturacionBean.C_PLANTILLAPDF +
							" FROM " + FacPlantillaFacturacionBean.T_NOMBRETABLA +
								" INNER JOIN " + FacSerieFacturacionBean.T_NOMBRETABLA +
									" ON " + 
										FacPlantillaFacturacionBean.T_NOMBRETABLA + "." + FacPlantillaFacturacionBean.C_IDINSTITUCION + "=" +
										FacSerieFacturacionBean.T_NOMBRETABLA +"."+ FacSerieFacturacionBean.C_IDINSTITUCION +
										" AND " +
										FacPlantillaFacturacionBean.T_NOMBRETABLA + "." + FacPlantillaFacturacionBean.C_IDPLANTILLA + "=" +
										FacSerieFacturacionBean.T_NOMBRETABLA +"."+ FacSerieFacturacionBean.C_IDPLANTILLA +
										" AND "+FacSerieFacturacionBean.T_NOMBRETABLA +"."+ FacSerieFacturacionBean.C_IDSERIEFACTURACION + "= :1" ;
	            	
	            						sql+=" AND " + FacSerieFacturacionBean.T_NOMBRETABLA +"."+ FacSerieFacturacionBean.C_IDINSTITUCION + "=:2 " ;
										
	            if (rc.findBind(sql,codigos)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add((String)fila.getRow().get(FacPlantillaFacturacionBean.C_PLANTILLAPDF));
	               }
	            }
	       }
//	       catch (SIGAException e) {
//	       	throw e;
//	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener las facturas pendientes");
	       }
	       return datos;                        
	    }
	
}
