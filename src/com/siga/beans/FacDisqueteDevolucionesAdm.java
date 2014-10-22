/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creación
 */

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.facturacion.form.DevolucionesForm;
import com.siga.general.SIGAException;


public class FacDisqueteDevolucionesAdm extends MasterBeanAdministrador {
	
	public FacDisqueteDevolucionesAdm (UsrBean usu) {
		super (FacDisqueteDevolucionesBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacDisqueteDevolucionesBean.C_IDINSTITUCION, 		
						    FacDisqueteDevolucionesBean.C_BANCOS_CODIGO,
							FacDisqueteDevolucionesBean.C_FECHAGENERACION,							
							FacDisqueteDevolucionesBean.C_NOMBREFICHERO,
							FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES,							
							FacDisqueteDevolucionesBean.C_FECHAMODIFICACION,
							FacDisqueteDevolucionesBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacDisqueteDevolucionesBean.C_IDINSTITUCION, FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacDisqueteDevolucionesBean bean = null;
		
		try {
			bean = new FacDisqueteDevolucionesBean();
			bean.setIdInstitucion				(UtilidadesHash.getInteger(hash, FacDisqueteDevolucionesBean.C_IDINSTITUCION));
			bean.setBancosCodigo				(UtilidadesHash.getString(hash, FacDisqueteDevolucionesBean.C_BANCOS_CODIGO));
			bean.setFechaGeneracion				(UtilidadesHash.getString(hash, FacDisqueteDevolucionesBean.C_FECHAGENERACION));
			bean.setNombreFichero				(UtilidadesHash.getString(hash, FacDisqueteDevolucionesBean.C_NOMBREFICHERO));
			bean.setIdDisqueteDevoluciones		(UtilidadesHash.getLong(hash, FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES));					
			bean.setFechaMod					(UtilidadesHash.getString(hash, FacDisqueteDevolucionesBean.C_FECHAMODIFICACION));
			bean.setUsuMod						(UtilidadesHash.getInteger(hash, FacDisqueteDevolucionesBean.C_USUMODIFICACION));
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
			FacDisqueteDevolucionesBean b = (FacDisqueteDevolucionesBean) bean;
			UtilidadesHash.set(htData, FacDisqueteDevolucionesBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacDisqueteDevolucionesBean.C_BANCOS_CODIGO, b.getBancosCodigo());
			UtilidadesHash.set(htData, FacDisqueteDevolucionesBean.C_FECHAGENERACION, b.getFechaGeneracion());
			UtilidadesHash.set(htData, FacDisqueteDevolucionesBean.C_NOMBREFICHERO, b.getNombreFichero());
			UtilidadesHash.set(htData, FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES, b.getIdDisqueteDevoluciones());
			UtilidadesHash.set(htData, FacDisqueteDevolucionesBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacDisqueteDevolucionesBean.C_USUMODIFICACION, b.getUsuMod());
			
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
	 * Recoge informacion sobre las devoluciones asociadas a una determinada institucion<br/> 
	 * @param  institucion - identificador de la institucion	 	  
	 * @return  PaginadorCaseSensitiveBind
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public PaginadorCaseSensitive getDevoluciones (DevolucionesForm form) throws ClsExceptions {
		try {
			RowsContainer rc = new RowsContainer();
			String sql = " SELECT DIS." + FacDisqueteDevolucionesBean.C_IDINSTITUCION + ", " +
							" DIS." + FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES + ", " +
							" BAN." + CenBancosBean.C_CODIGO + ", " +
							" DIS." + FacDisqueteDevolucionesBean.C_FECHAGENERACION + ", " +
							" DIS." + FacDisqueteDevolucionesBean.C_NOMBREFICHERO + ", " +
							" ( " +
								" SELECT COUNT (*) " +
								" FROM " + FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + " LIN " +
								" WHERE LIN." + FacLineaDevoluDisqBancoBean.C_IDINSTITUCION + " = DIS." + FacDisqueteDevolucionesBean.C_IDINSTITUCION +
									" AND LIN." + FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES + " = DIS." + FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES +
							" ) AS FACTURAS, " +
							" ( " +
								" SELECT COUNT (DISTINCT LIN." + FacLineaDevoluDisqBancoBean.C_CARGARCLIENTE + ") + 1 " +
								" FROM " + FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + " LIN " +
								" WHERE LIN." + FacLineaDevoluDisqBancoBean.C_IDINSTITUCION + " = DIS." + FacDisqueteDevolucionesBean.C_IDINSTITUCION +
									" AND LIN." + FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES + " = DIS."+FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES +
									" AND LIN." + FacLineaDevoluDisqBancoBean.C_CARGARCLIENTE + " = 'S' " +
							" ) AS COMISION, " +
							" BAN."+CenBancosBean.C_NOMBRE +
						" FROM " + FacDisqueteDevolucionesBean.T_NOMBRETABLA + " DIS, " +
							FacBancoInstitucionBean.T_NOMBRETABLA + " BANINS, " +
							CenBancosBean.T_NOMBRETABLA + " BAN " +
						" WHERE DIS." + FacDisqueteDevolucionesBean.C_IDINSTITUCION + " = BANINS." + FacBancoInstitucionBean.C_IDINSTITUCION +
							" AND DIS." + FacDisqueteDevolucionesBean.C_BANCOS_CODIGO + " = BANINS." + FacBancoInstitucionBean.C_BANCOS_CODIGO +
							" AND BANINS." + FacBancoInstitucionBean.C_COD_BANCO + " = BAN." + CenBancosBean.C_CODIGO +
							" AND DIS." + FacDisqueteDevolucionesBean.C_IDINSTITUCION + " = " + form.getIdInstitucion();
			
				//FILTRO TIPO DEVOLUCION
				if(form.getTipoDevolucion() != null && !form.getTipoDevolucion().equals("")) {					
					if(form.getTipoDevolucion().equals("0")) {
						sql += " AND SUBSTR(DIS." + FacDisqueteDevolucionesBean.C_NOMBREFICHERO + ",1,6) <> 'Manual' "; 
					} else if(form.getTipoDevolucion().equals("1")) {
						sql += " AND (DIS." + FacDisqueteDevolucionesBean.C_NOMBREFICHERO + " IS NULL OR SUBSTR(DIS." + FacDisqueteDevolucionesBean.C_NOMBREFICHERO + ",1,6) = 'Manual') ";
					}
				} 
				
				//FILTRO BANCO
				if(form.getCodigoBanco() != null && !form.getCodigoBanco().equals("")){
					sql += " AND BANINS.COD_BANCO = " +form.getCodigoBanco();
				}
				
				//FILTRO FECHAS
				String dFechaDesde = null, dFechaHasta = null;
				if (form.getFechaDesde()!=null && !form.getFechaDesde().trim().equals(""))
				    dFechaDesde = GstDate.getApplicationFormatDate("",form.getFechaDesde());
				if (form.getFechaHasta()!=null && !form.getFechaHasta().trim().equals(""))
					dFechaHasta = GstDate.getApplicationFormatDate("",form.getFechaHasta());
				if (dFechaDesde!=null || dFechaHasta!=null){
				    sql += " AND " + GstDate.dateBetweenDesdeAndHasta(FacDisqueteDevolucionesBean.C_FECHAGENERACION,dFechaDesde,dFechaHasta);
				    
				}
				
				//SQL POR ENCIMA PARA PODER FILTRAR POR COMISION Y NUMERO DE FACTURAS				
				String sqlGrande = " SELECT DEVOLUCIONES.COMISION, " +
								   "   DEVOLUCIONES.FACTURAS, " +
								   "   DEVOLUCIONES.IDINSTITUCION, " +
					               "   DEVOLUCIONES.IDDISQUETEDEVOLUCIONES, " +
					               "   DEVOLUCIONES.CODIGO, " +
					               "   DEVOLUCIONES.FECHAGENERACION, " +
					               "   DEVOLUCIONES.NOMBREFICHERO, " +
					               "   DEVOLUCIONES.NOMBRE " +
					               " FROM  ( "+ sql + ") DEVOLUCIONES WHERE 1=1";
				
				//FILTRO COMISONES
				if(form.getComision() != null && !form.getComision().equals("")){
					sqlGrande += " AND DEVOLUCIONES.COMISION = " +form.getComision();
				}
				
				//FILTRO Nº FACTURAS
				if (form.getFacturasDesde()!=null && !form.getFacturasDesde().trim().equals("")){
					sqlGrande += " AND DEVOLUCIONES.FACTURAS >= " +form.getFacturasDesde();
				}
				
				if (form.getFacturasHasta()!=null && !form.getFacturasHasta().trim().equals("")){
					sqlGrande += " AND DEVOLUCIONES.FACTURAS <= " +form.getFacturasHasta();
				}
								
				sqlGrande +=" ORDER BY DEVOLUCIONES." + FacDisqueteDevolucionesBean.C_FECHAGENERACION + " DESC, " +
								" DEVOLUCIONES." + FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES + " DESC";
					
			PaginadorCaseSensitive paginador = new PaginadorCaseSensitive(sqlGrande);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador = null;
			}
			
			return paginador;
		
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de abonos.");
		}                    
	} //getDevoluciones()
	
	/** 
	 * Obtiene el valor IDDISQUETEDEVOLUCIONES <br/>
	 * @param  institucion - identificador de la institucion 
	 * @return  Long - Siguiente identificador de IDDISQUETEDEVOLUCIONES 
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Long getNuevoID (String institucion) throws ClsExceptions, SIGAException 
	{

		Long resultado=null;
		
		try { 
            RowsContainer rc = new RowsContainer();

			String sql;
			sql ="SELECT (MAX(IDDISQUETEDEVOLUCIONES) + 1) AS IDDISQUETEDEVOLUCIONES FROM " + nombreTabla +
				" WHERE IDINSTITUCION =" + institucion;
		
			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba==null || prueba.get("IDDISQUETEDEVOLUCIONES")==null || prueba.get("IDDISQUETEDEVOLUCIONES").equals("")) {
					resultado=new Long(1);
				}
				else 
					resultado=UtilidadesHash.getLong(prueba, "IDDISQUETEDEVOLUCIONES");;								
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
	 * Obtiene las facturas devueltas en disquete
	 * @param idInstitucion
	 * @param idDisquete
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getFacturasDevueltasEnDisquete (String idInstitucion, String idDisquete) throws ClsExceptions,SIGAException {
		Vector factDevueltasVector = new Vector();
	
		try {
			FacFacturaAdm admFacFactura = new FacFacturaAdm(this.usrbean);
			String[] camposFactura = admFacFactura.getCamposBean();
			
			String sql = "SELECT ";
			
			for (int i=0; i<camposFactura.length; i++) {
				sql += " FAC." + camposFactura[i] + ", ";
			}			
			
			sql += " LD.IDRECIBO " +
					" FROM FAC_DISQUETEDEVOLUCIONES DD, " + 
						" FAC_LINEADEVOLUDISQBANCO LD, " +
						" FAC_FACTURAINCLUIDAENDISQUETE FID, " + 
						" FAC_FACTURA FAC " + 
					" WHERE FAC.IDINSTITUCION = FID.IDINSTITUCION " +
						" AND FAC.IDFACTURA = FID.IDFACTURA " +
						" AND FID.IDINSTITUCION = LD.IDINSTITUCION " + 
						" AND FID.IDFACTURAINCLUIDAENDISQUETE = LD.IDFACTURAINCLUIDAENDISQUETE " + 
						" AND FID.IDDISQUETECARGOS = LD.IDDISQUETECARGOS " +
						" AND LD.IDINSTITUCION = DD.IDINSTITUCION " +
						" AND LD.IDDISQUETEDEVOLUCIONES = DD.IDDISQUETEDEVOLUCIONES " +
						" AND DD.IDINSTITUCION = " + idInstitucion +
						" AND DD.IDDISQUETEDEVOLUCIONES = " + idDisquete;
			
			RowsContainer rc = new RowsContainer();
			if (rc.find(sql)) {
				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					factDevueltasVector.add(fila);
				}
			}
	
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre del disquete de devoluciones.");
		}
	
		return factDevueltasVector;                        
	} //getFacturasDevueltasEnDisquete()
}