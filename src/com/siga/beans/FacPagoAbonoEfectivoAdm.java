/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creación
 */

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


public class FacPagoAbonoEfectivoAdm extends MasterBeanAdministrador {
	
	public FacPagoAbonoEfectivoAdm (UsrBean usu) {
		super (FacPagoAbonoEfectivoBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacPagoAbonoEfectivoBean.C_IDINSTITUCION, 		
						    FacPagoAbonoEfectivoBean.C_CONTABILIZADO,
							FacPagoAbonoEfectivoBean.C_FECHA,							
							FacPagoAbonoEfectivoBean.C_IDABONO,
							FacPagoAbonoEfectivoBean.C_IDPAGOABONO,							
							FacPagoAbonoEfectivoBean.C_IMPORTE,
							FacPagoAbonoEfectivoBean.C_USUMODIFICACION,
							FacPagoAbonoEfectivoBean.C_FECHAMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacPagoAbonoEfectivoBean.C_IDINSTITUCION, FacPagoAbonoEfectivoBean.C_IDABONO, FacPagoAbonoEfectivoBean.C_IDPAGOABONO};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacPagoAbonoEfectivoBean bean = null;
		
		try {
			bean = new FacPagoAbonoEfectivoBean();
			bean.setIdInstitucion				(UtilidadesHash.getInteger(hash, FacPagoAbonoEfectivoBean.C_IDINSTITUCION));
			bean.setContabilizado				(UtilidadesHash.getString(hash, FacPagoAbonoEfectivoBean.C_CONTABILIZADO));
			bean.setFecha						(UtilidadesHash.getString(hash, FacPagoAbonoEfectivoBean.C_FECHA));
			bean.setIdPagoAbono					(UtilidadesHash.getLong(hash, FacPagoAbonoEfectivoBean.C_IDPAGOABONO));
			bean.setIdAbono						(UtilidadesHash.getLong(hash, FacPagoAbonoEfectivoBean.C_IDABONO));
			bean.setImporte						(UtilidadesHash.getDouble(hash, FacPagoAbonoEfectivoBean.C_IMPORTE));			
			bean.setFechaMod					(UtilidadesHash.getString(hash, FacPagoAbonoEfectivoBean.C_FECHAMODIFICACION));
			bean.setUsuMod						(UtilidadesHash.getInteger(hash, FacPagoAbonoEfectivoBean.C_USUMODIFICACION));
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
			FacPagoAbonoEfectivoBean b = (FacPagoAbonoEfectivoBean) bean;
			UtilidadesHash.set(htData, FacPagoAbonoEfectivoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacPagoAbonoEfectivoBean.C_CONTABILIZADO, b.getContabilizado());
			UtilidadesHash.set(htData, FacPagoAbonoEfectivoBean.C_FECHA, b.getFecha());
			UtilidadesHash.set(htData, FacPagoAbonoEfectivoBean.C_IDPAGOABONO, b.getIdPagoAbono());
			UtilidadesHash.set(htData, FacPagoAbonoEfectivoBean.C_IDABONO, b.getIdAbono());
			UtilidadesHash.set(htData, FacPagoAbonoEfectivoBean.C_IMPORTE, b.getImporte());
			UtilidadesHash.set(htData, FacPagoAbonoEfectivoBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacPagoAbonoEfectivoBean.C_USUMODIFICACION, b.getUsuMod());
			
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
	 * Obtiene el valor IDPAGOABONO, <br/>
	 * @param  institucion - identificador del abono
	 * @param  institucion - identificador de la institucion 
	 * @return  Long - Siguiente numero de linea  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Long getNuevoID (String institucion, String abono) throws ClsExceptions, SIGAException 
	{

		Long resultado=null;
		int contador = 0;	            
        Hashtable codigos = new Hashtable();
		
		try { 
            RowsContainer rc = new RowsContainer();

			String sql;
			sql ="SELECT (MAX(IDPAGOABONO) + 1) AS IDPAGOABONO FROM " + nombreTabla;
			contador++;
			codigos.put(new Integer(contador),institucion);
			sql +=" WHERE IDINSTITUCION =:" + contador;
			contador++;
			codigos.put(new Integer(contador),abono);
			sql	+=" AND IDABONO =:" + contador;
		
			if (rc.findBind(sql,codigos)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDPAGOABONO").equals("")) {
					resultado=new Long(1);
				}
				else 
					resultado=UtilidadesHash.getLong(prueba, "IDPAGOABONO");;								
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
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  abono - identificador del abono	 	  
	 * @param  idPago - identificador del pago-abono
	 * @return  Vector - Fila seleccionada (Row)  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getPagoAbonoEfectivo (String institucion, String abono, String idPago) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IDABONO + "," +
			    			FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IDINSTITUCION + "," +
			    			FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IDPAGOABONO + "," +
			    			FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_FECHA + "," +
			    			FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IMPORTE + "," +
			    			FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_CONTABILIZADO + 
							" FROM " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA + 
							" WHERE " +
							FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDABONO + "=" + abono +
							" AND " +
							FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDPAGOABONO + "=" + idPago;
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
//			catch (SIGAException e) {
//				throw e;
//			}
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de pagos abonos en efectivo.");
	       }
	       return datos;                        
	    }
	
}
