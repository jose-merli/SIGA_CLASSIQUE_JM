/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creaci�n
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


public class FacLineaDevoluDisqBancoAdm extends MasterBeanAdministrador {
	
	public FacLineaDevoluDisqBancoAdm (UsrBean usu) {
		super (FacLineaDevoluDisqBancoBean.T_NOMBRETABLA, usu);
	}
	
	public String[] getCamposBean() {
		String [] campos = {FacLineaDevoluDisqBancoBean.C_IDINSTITUCION,
							FacLineaDevoluDisqBancoBean.C_IDDISQUETECARGOS,
							FacLineaDevoluDisqBancoBean.C_IDRECIBO,
							FacLineaDevoluDisqBancoBean.C_DESCRIPCIONMOTIVOS,
							FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES,
							FacLineaDevoluDisqBancoBean.C_IDFACTURAINCLUIDAENDISQUETE,
						    FacLineaDevoluDisqBancoBean.C_CARGARCLIENTE,
							FacLineaDevoluDisqBancoBean.C_CONTABILIZADA,							
							FacLineaDevoluDisqBancoBean.C_GASTOSDEVOLUCION,
							FacLineaDevoluDisqBancoBean.C_FECHAMODIFICACION,
							FacLineaDevoluDisqBancoBean.C_USUMODIFICACION};
		return campos;	
	}

	protected String[] getClavesBean() {
		String [] claves = {FacLineaDevoluDisqBancoBean.C_IDINSTITUCION, FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES, FacLineaDevoluDisqBancoBean.C_IDRECIBO};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacLineaDevoluDisqBancoBean bean = null;
		
		try {
			bean = new FacLineaDevoluDisqBancoBean();
			bean.setIdInstitucion				(UtilidadesHash.getInteger(hash, FacLineaDevoluDisqBancoBean.C_IDINSTITUCION));
			bean.setCargarCliente				(UtilidadesHash.getString(hash, FacLineaDevoluDisqBancoBean.C_CARGARCLIENTE));
			bean.setContabilizada				(UtilidadesHash.getString(hash, FacLineaDevoluDisqBancoBean.C_CONTABILIZADA));
			bean.setIdDisqueteCargos			(UtilidadesHash.getLong(hash, FacLineaDevoluDisqBancoBean.C_IDDISQUETECARGOS));
			bean.setIdDisqueteDevoluciones		(UtilidadesHash.getLong(hash, FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES));
			bean.setIdFacturaIncluidaEnDisquete (UtilidadesHash.getInteger(hash, FacLineaDevoluDisqBancoBean.C_IDFACTURAINCLUIDAENDISQUETE));
			bean.setIdRecibo					(UtilidadesHash.getString(hash, FacLineaDevoluDisqBancoBean.C_IDRECIBO));
			bean.setDescripcionMotivos			(UtilidadesHash.getString(hash, FacLineaDevoluDisqBancoBean.C_DESCRIPCIONMOTIVOS));
			bean.setGastosDevolucion			(UtilidadesHash.getDouble(hash, FacLineaDevoluDisqBancoBean.C_GASTOSDEVOLUCION));
			bean.setFechaMod					(UtilidadesHash.getString(hash, FacLineaDevoluDisqBancoBean.C_FECHAMODIFICACION));
			bean.setUsuMod						(UtilidadesHash.getInteger(hash, FacLineaDevoluDisqBancoBean.C_USUMODIFICACION));
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
			FacLineaDevoluDisqBancoBean b = (FacLineaDevoluDisqBancoBean) bean;
			UtilidadesHash.set(htData, FacLineaDevoluDisqBancoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacLineaDevoluDisqBancoBean.C_CARGARCLIENTE, b.getCargarCliente());
			UtilidadesHash.set(htData, FacLineaDevoluDisqBancoBean.C_CONTABILIZADA, b.getContabilizada());
			UtilidadesHash.set(htData, FacLineaDevoluDisqBancoBean.C_IDDISQUETECARGOS, b.getIdDisqueteCargos());
			UtilidadesHash.set(htData, FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES, b.getIdDisqueteDevoluciones());
			UtilidadesHash.set(htData, FacLineaDevoluDisqBancoBean.C_IDFACTURAINCLUIDAENDISQUETE, b.getIdFacturaIncluidaEnDisquete());
			UtilidadesHash.set(htData, FacLineaDevoluDisqBancoBean.C_IDRECIBO, b.getIdRecibo());
			UtilidadesHash.set(htData, FacLineaDevoluDisqBancoBean.C_DESCRIPCIONMOTIVOS, b.getDescripcionMotivos());
			UtilidadesHash.set(htData, FacLineaDevoluDisqBancoBean.C_GASTOSDEVOLUCION, b.getGastosDevolucion());
			UtilidadesHash.set(htData, FacLineaDevoluDisqBancoBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacLineaDevoluDisqBancoBean.C_USUMODIFICACION, b.getUsuMod());
			
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
	 * Recoge informacion sobre una determinada devolucion definida por sus claves<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  identDisquete - identificador del disquete de devolucion
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDesgloseDevolucion (String institucion, String identDisquete) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_IDINSTITUCION + "," +
			    			FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES + "," +
			    			FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_IDRECIBO + "," +
			    			FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_IDDISQUETECARGOS + "," +
			    			FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_IDFACTURAINCLUIDAENDISQUETE + "," +
			    			FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_DESCRIPCIONMOTIVOS + "," +
			    			FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_GASTOSDEVOLUCION + "," +
			    			FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_CARGARCLIENTE + "," +
			    			FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IMPORTE + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + "," + 
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA +
							" FROM " + FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + 
								" INNER JOIN " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + 
									" ON " + 
									FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_IDINSTITUCION + "=" + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION +
									" AND " + 
									FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_IDDISQUETECARGOS + "=" + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS +
									" AND " + 
									FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_IDFACTURAINCLUIDAENDISQUETE + "=" + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE +
								" INNER JOIN " + FacFacturaBean.T_NOMBRETABLA +  
									" ON " + 
									FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + "=" + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION +
									" AND " + 
									FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + "=" + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA +
							" WHERE " +
							FacLineaDevoluDisqBancoBean.T_NOMBRETABLA +"."+ FacLineaDevoluDisqBancoBean.C_IDINSTITUCION + "=" + institucion +
							" AND " + 
							FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES + "=" + identDisquete +
							" ORDER BY " + 
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + " DESC ";
														
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
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre el desglose de una devolucion.");
	       }
	       return datos;                        
	    }

	/**
	 * Obtiene las devoluciones del fichero
	 * @param institucion
	 * @param idDisqueteDevoluciones
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector obtenerDevolucionesFichero (String institucion, String idDisqueteDevoluciones) throws ClsExceptions {
		String campos[] = this.getCamposBean();
		
	   Vector datos=new Vector();
       try {
            RowsContainer rc = new RowsContainer();
            String sql = "SELECT " + FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + campos[0];
            for (int i=1; i<campos.length; i++) {
            	sql += ", " + FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + campos [i];
            }
            sql += " FROM " + FacLineaDevoluDisqBancoBean.T_NOMBRETABLA +
            		" WHERE " + FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_IDINSTITUCION + " = " + institucion +
	            		" AND " + FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES + " >= " + idDisqueteDevoluciones;
													
            if (rc.find(sql)) {
            	for (int i = 0; i < rc.size(); i++){
                  Row fila = (Row) rc.get(i);
                  Hashtable<String, Object> htFila=fila.getRow();
                  FacLineaDevoluDisqBancoBean beanDevoluciones = (FacLineaDevoluDisqBancoBean) this.hashTableToBean(htFila);
                  datos.add(beanDevoluciones);
               }
            } 
       }

       catch (Exception e) {
       		throw new ClsExceptions (e, "Error al obtener las devoluciones del fichero.");
       }
       return datos;                        
    }	
	
	/**
	 * Obtiene la devolucion manual
	 * @param institucion
	 * @param idDisqueteDevoluciones
	 * @return
	 * @throws ClsExceptions
	 */
	public FacLineaDevoluDisqBancoBean obtenerDevolucionManual (String institucion, String idDisqueteDevoluciones) throws ClsExceptions {
		FacLineaDevoluDisqBancoBean beanDevoluciones = null;
		String campos[] = this.getCamposBean();
		
       try {
            RowsContainer rc = new RowsContainer();
            String sql = "SELECT " + FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + campos[0];
            for (int i=1; i<campos.length; i++) {
            	sql += ", " + FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + campos [i];
            }
            sql += " FROM " + FacLineaDevoluDisqBancoBean.T_NOMBRETABLA +
            		" WHERE " + FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_IDINSTITUCION + " = " + institucion +
	            		" AND " + FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + "." + FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES + " = " + idDisqueteDevoluciones;
													
            if (rc.find(sql) && rc.size()>0) {
            	Row fila = (Row) rc.get(0);
            	Hashtable<String, Object> htFila = fila.getRow();
            	beanDevoluciones =  (FacLineaDevoluDisqBancoBean) this.hashTableToBean(htFila);
            }
            
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al obtener las devoluciones del fichero.");
       }
       
       return beanDevoluciones;                        
    }		
}
