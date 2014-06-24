/*
 * VERSIONES:
 * 
 * carlos.vidal - 09-03-2005 - Creacion
 *	
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla CenHistorico de la BBDD
* 
*/
public class FacFacturaIncluidaEnDisqueteAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public FacFacturaIncluidaEnDisqueteAdm(UsrBean usu) {
		super(FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION,              
				FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS,           
				FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE,
				FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA,                  
				FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA,                   
				FacFacturaIncluidaEnDisqueteBean.C_CONTABILIZADA,              
				FacFacturaIncluidaEnDisqueteBean.C_FECHAMODIFICACION,          
				FacFacturaIncluidaEnDisqueteBean.C_USUMODIFICACION,            
				FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO,                   
				FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION,            
				FacFacturaIncluidaEnDisqueteBean.C_FECHADEVOLUCION,            
				FacFacturaIncluidaEnDisqueteBean.C_IDPERSONA,                  
				FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA,                   
				FacFacturaIncluidaEnDisqueteBean.C_IMPORTE};                   

		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION,
							FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS,
							FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}	

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacFacturaIncluidaEnDisqueteBean bean = null;
		
		try {
			bean = new FacFacturaIncluidaEnDisqueteBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,				FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION));
			bean.setIdDisqueteCargos(UtilidadesHash.getInteger(hash,           	FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS));
			bean.setIdFacturaIncluidaEnDisquete(UtilidadesHash.getInteger(hash,	FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE));
			bean.setIdFactura(UtilidadesHash.getString(hash,                  	FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA));
			bean.setDevuelta(UtilidadesHash.getString(hash,                   	FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA));
			bean.setContabilizada(UtilidadesHash.getString(hash,              	FacFacturaIncluidaEnDisqueteBean.C_CONTABILIZADA));
			bean.setIdRecibo(UtilidadesHash.getString(hash,                   	FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO));       
			bean.setIdRenegociacion(UtilidadesHash.getInteger(hash,           	FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION));
			bean.setFechaDevolucion(UtilidadesHash.getString(hash,            	FacFacturaIncluidaEnDisqueteBean.C_FECHADEVOLUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash,                  	FacFacturaIncluidaEnDisqueteBean.C_IDPERSONA));      
			bean.setIdCuenta(UtilidadesHash.getInteger(hash,                   	FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA));       
			bean.setImporte(UtilidadesHash.getDouble(hash,                    	FacFacturaIncluidaEnDisqueteBean.C_IMPORTE));        
			bean.setFechaMod(UtilidadesHash.getString(hash,						FacFacturaIncluidaEnDisqueteBean.C_FECHAMODIFICACION));  
			bean.setUsuMod(UtilidadesHash.getInteger(hash,						FacFacturaIncluidaEnDisqueteBean.C_USUMODIFICACION));		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/** 
	 * Obtiene la tabla hash a partir del bean introducido
	 * @param  bean - bean con los valores de la tabla 
	 * @return  Hashtable - Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FacFacturaIncluidaEnDisqueteBean b = (FacFacturaIncluidaEnDisqueteBean) bean;
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION,              b.getIdInstitucion());              
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS,           b.getIdDisqueteCargos());           
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE,b.getIdFacturaIncluidaEnDisquete());
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA,                  b.getIdFactura());                 
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA,                   b.getDevuelta());                  
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_CONTABILIZADA,              b.getContabilizada());             
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_FECHAMODIFICACION,          b.getIdRecibo());                  
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_USUMODIFICACION,            b.getIdRenegociacion());           
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO,                   b.getFechaDevolucion());           
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION,            b.getIdPersona());                 
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_FECHADEVOLUCION,            b.getIdCuenta());                  
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDPERSONA,                  b.getImporte());                   
			UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA,                   b.getFechaMod());                  
      UtilidadesHash.set(htData,FacFacturaIncluidaEnDisqueteBean.C_IMPORTE,                    		 b.getUsuMod());                    
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/**
	 * Funcion que actualiza el idRegnegociacion de todos los registros de la tabla 
	 * que correspondan a la factura, institucion y el idRenegociacion sea null 
	 * @param idInstitucion institucion a mofidicar
	 * @param idFactura factura a modificar
	 * @param idRenegociacion nuevo ID
	 * @return true si ok, false en caso contrario
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public boolean updateRenegociacion (Integer idInstitucion, String idFactura, Integer idRenegociacion) throws ClsExceptions, SIGAException {

		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			FacFacturaIncluidaEnDisqueteAdm adm = new FacFacturaIncluidaEnDisqueteAdm(this.usrbean);
			rc = new RowsContainer(); 
			String sql = " SELECT " + 
			 " " + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + 
			 ", " + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + 
			 ", " + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE + 
			 " FROM " + this.nombreTabla + 
			 " WHERE " + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + idInstitucion +
			 " AND " + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = '" + idFactura + "'" +
			 " AND " + FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION + " IS NULL ";

			if (rc.findForUpdate(sql)) {

				// TENEMOS LOS UPDATES.
				if (rc!=null) {
		       		for (int i = 0; i < rc.size(); i++)	{
						Row filaClientes = (Row) rc.get(i);
						Hashtable fila = filaClientes.getRow();
						/*
						FacFacturaIncluidaEnDisqueteBean bean = new FacFacturaIncluidaEnDisqueteBean();
						bean.setIdInstitucion(new Integer((String)fila.get(FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION)));
						bean.setIdDisqueteCargos((Integer)fila.get(FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS));
						bean.setIdFacturaIncluidaEnDisquete(new Integer((String)fila.get(FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE)));
						bean.setIdRenegociacion(idRenegociacion);
						*/
						
						fila.put(FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION,idRenegociacion);
						
						String claves[] = {FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION,FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS,FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE};
						String campos[] = {FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION};
						
						if (!adm.updateDirect(fila,claves,campos)) {
							this.setError(adm.getError());
							return false;
						}
		       		}
				}
				return true;
				
			} else  {
				return false;
			}

/*
			String sql = " UPDATE " + this.nombreTabla + 
						 " SET " + FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION + " = " + idRenegociacion +
						 " WHERE " + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + idInstitucion +
						 " AND " + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = '" + idFactura + "'" +
						 " AND " + FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION + " IS NULL ";
			
			if (rc.findForUpdate(sql)) {
				return true;
			}
			else {
				return false;
			}
*/
			
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'updateRenegociacion' en B.D."); 
		}
	}

	/**
	 * Realiza la busqueda de recibos para devoluciones manuales mediante criterios de busqueda.
	 * @param idInstitucion
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param numRecibo
	 * @param titular
	 * @param numRemesa
	 * @return Vector de resultados
	 * @throws ClsExceptions
	 */
	public PaginadorCaseSensitive getRecibosParaDevolucion(String idInstitucion,String fechaDesde,String fechaHasta,String numRecibo,String titular, String numRemesa, String numFactura, String destinatario) throws ClsExceptions {
		Vector v = new Vector();
		RowsContainer rc = null;
		try{
			String sql = " select " + 
				" " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + ", " +
				" " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE + ", " +
				" " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + ", " +
				" " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO + ", " +
				" " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDPERSONA + ", " +
				" " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IMPORTE + ", " +
				" " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_FECHACREACION + ", " +
				" " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ", " +
				" " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + ", " +
				" " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ", " +
				" " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + " " +
				" from " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + ", " + CenPersonaBean.T_NOMBRETABLA + ", " + FacFacturaBean.T_NOMBRETABLA + " " + ", " + FacDisqueteCargosBean.T_NOMBRETABLA + " " +
				" where " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDPERSONA + " = " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " " +
				" and  " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " " +
				" and  " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " " +
				" and  " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDDISQUETECARGOS + " " +
				" and  " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDINSTITUCION + " " +
				" and " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + idInstitucion +
				" and " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA + " = 'N'"+
			
			" and " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_ESTADO + " = "+FacEstadoFacturaBean.ID_ESTADO_PAGADA;

			
			
				if ((fechaDesde != null && !fechaDesde.trim().equals("")) || (fechaHasta != null && !fechaHasta.trim().equals(""))) {
					if (!fechaDesde.equals(""))
						fechaDesde = GstDate.getApplicationFormatDate("", fechaDesde); 
					if (!fechaHasta.equals(""))
						fechaHasta = GstDate.getApplicationFormatDate("", fechaHasta);
					sql+= " AND " + GstDate.dateBetweenDesdeAndHasta(FacDisqueteCargosBean.T_NOMBRETABLA+"."+FacDisqueteCargosBean.C_FECHACREACION, fechaDesde, fechaHasta);
				}
				
			if (!numRecibo.equals("")) {
				
			
				sql += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(numRecibo.trim(),FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO )+") ";
			}
			
			if (!numRemesa.equals("")) {
				
			
				sql += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(numRemesa.trim(),FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS)+") ";
			}
			
			if (!numFactura.equals("")) {				
			
				sql += " AND ("+ComodinBusquedas.prepararSentenciaCompleta(numFactura.trim(),FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA)+") ";
			}
			
			if (!titular.equals("")) {
				sql += " AND " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDPERSONA + " = " + titular + " ";
				
			}
			
			if (!destinatario.equals("")) {
				sql += " AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONADEUDOR + " = " + destinatario + " ";
				
			}
			
			sql += " ORDER BY " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO + " ";
			
				
				
           /* rc = this.find(sql);
 			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}*/
			PaginadorCaseSensitive paginador = new PaginadorCaseSensitive(sql);				
				int totalRegistros = paginador.getNumeroTotalRegistros();
				
				if (totalRegistros==0){					
					paginador =null;
				}
	 
		       
				
				return paginador;

		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error en getRecibosParaDevolucion");
		}
	//	return v;	
	}
}