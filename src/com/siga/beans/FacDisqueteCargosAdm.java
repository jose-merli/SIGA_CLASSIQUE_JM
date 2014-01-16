/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creación
 */

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.facturacion.form.FicheroBancarioPagosForm;


public class FacDisqueteCargosAdm extends MasterBeanAdministrador {
	
	public FacDisqueteCargosAdm (UsrBean usu) {
		super (FacDisqueteCargosBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {FacDisqueteCargosBean.C_IDINSTITUCION, 		
						    FacDisqueteCargosBean.C_BANCOS_CODIGO,
							FacDisqueteCargosBean.C_FECHACREACION,
							FacDisqueteCargosBean.C_IDDISQUETECARGOS,
							FacDisqueteCargosBean.C_IDPROGRAMACION,
							FacDisqueteCargosBean.C_IDSERIEFACTURACION,
							FacDisqueteCargosBean.C_NOMBREFICHERO,
							FacDisqueteCargosBean.C_NUMEROLINEAS,
							FacDisqueteCargosBean.C_FECHACARGO,
							FacDisqueteCargosBean.C_FECHAMODIFICACION,
							FacDisqueteCargosBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {FacDisqueteCargosBean.C_IDINSTITUCION, FacDisqueteCargosBean.C_IDDISQUETECARGOS};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacDisqueteCargosBean bean = null;
		
		try {
			bean = new FacDisqueteCargosBean();
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash, FacDisqueteCargosBean.C_IDINSTITUCION));
			bean.setBancosCodigo		(UtilidadesHash.getString(hash, FacDisqueteCargosBean.C_BANCOS_CODIGO));
			bean.setFechaCreacion		(UtilidadesHash.getString(hash, FacDisqueteCargosBean.C_FECHACREACION));
			bean.setIdDisqueteCargos	(UtilidadesHash.getLong(hash, FacDisqueteCargosBean.C_IDDISQUETECARGOS));
			bean.setIdProgramacion		(UtilidadesHash.getLong(hash, FacDisqueteCargosBean.C_IDPROGRAMACION));
			bean.setIdSerieFacturacion	(UtilidadesHash.getLong(hash, FacDisqueteCargosBean.C_IDSERIEFACTURACION));
			bean.setNombreFichero		(UtilidadesHash.getString(hash, FacDisqueteCargosBean.C_NOMBREFICHERO));
			bean.setFechaCargo			(UtilidadesHash.getString(hash, FacDisqueteCargosBean.C_FECHACARGO));
			bean.setNumeroLineas		(UtilidadesHash.getLong(hash, FacDisqueteCargosBean.C_NUMEROLINEAS));
			bean.setFechaMod			(UtilidadesHash.getString(hash, FacDisqueteCargosBean.C_FECHAMODIFICACION));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash, FacDisqueteCargosBean.C_USUMODIFICACION));
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
			FacDisqueteCargosBean b = (FacDisqueteCargosBean) bean;
			UtilidadesHash.set(htData, FacDisqueteCargosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacDisqueteCargosBean.C_BANCOS_CODIGO, b.getBancosCodigo());
			UtilidadesHash.set(htData, FacDisqueteCargosBean.C_FECHACREACION, b.getFechaCreacion());
			UtilidadesHash.set(htData, FacDisqueteCargosBean.C_IDDISQUETECARGOS, b.getIdDisqueteCargos());
			UtilidadesHash.set(htData, FacDisqueteCargosBean.C_IDPROGRAMACION, b.getIdProgramacion());
			UtilidadesHash.set(htData, FacDisqueteCargosBean.C_IDSERIEFACTURACION, b.getIdSerieFacturacion());
			UtilidadesHash.set(htData, FacDisqueteCargosBean.C_NOMBREFICHERO, b.getNombreFichero());
			UtilidadesHash.set(htData, FacDisqueteCargosBean.C_NUMEROLINEAS, b.getNumeroLineas());
			UtilidadesHash.set(htData, FacDisqueteCargosBean.C_FECHACARGO, b.getFechaCargo());
			UtilidadesHash.set(htData, FacDisqueteCargosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacDisqueteCargosBean.C_USUMODIFICACION, b.getUsuMod());
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
	 * Obtiene un vector con los datos del fichero
	 * @param  String - identificador de la institución
	 * @return  PaginadorCaseSensitive 
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public PaginadorCaseSensitive getDatosFichero(FicheroBancarioPagosForm form) throws ClsExceptions {
		try {
			String sql = " SELECT " + FacDisqueteCargosBean.C_FECHACREACION + ", " +
						" ( " +
							" SELECT cen_bancos.codigo || '-' || substr(cen_bancos.NOMBRE, 1, 30) " +
							" FROM cen_bancos, " +
								" fac_bancoinstitucion"+
							" WHERE cen_bancos.codigo = fac_bancoinstitucion.cod_banco " +
							" AND fac_bancoinstitucion.bancos_codigo = fac_disquetecargos.bancos_codigo " +
							" AND fac_bancoinstitucion.idinstitucion = " + form.getIdInstitucion() + 
						") AS BANCO, "+
						FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDSERIEFACTURACION + ", " + 
						" ( " +
							" SELECT FAC_SERIEFACTURACION.NOMBREABREVIADO " +
							" FROM FAC_SERIEFACTURACION " +
							" WHERE fac_seriefacturacion.idinstitucion = FAC_DISQUETECARGOS.IDINSTITUCION " +
								" AND fac_seriefacturacion.idseriefacturacion = fac_disquetecargos.idseriefacturacion" +
						") NOMBREABREVIADO,"+
						FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDDISQUETECARGOS + ", " +
						FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_NOMBREFICHERO + ", " +								
						" ( " +
							" SELECT " + FacFacturacionProgramadaBean.C_DESCRIPCION + 
					        " FROM " + FacFacturacionProgramadaBean.T_NOMBRETABLA + 
					        " WHERE " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDINSTITUCION +
					        	" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDSERIEFACTURACION +
								" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDPROGRAMACION +
						" ) DESCRIPCION_PROGRAMACION, " + 
						" ( " +
							" SELECT SUM (" + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA +"."+ FacFacturaIncluidaEnDisqueteBean.C_IMPORTE +" ) " +
							" FROM " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + 
							" WHERE " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDINSTITUCION + 
								" AND " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDDISQUETECARGOS + 
						" ) AS TOTAL_REMESA, " + 
						" ( " +
							" SELECT COUNT (1) " +
							" FROM " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + 
							" WHERE " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDINSTITUCION + 
								" AND " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDDISQUETECARGOS + 
						") AS NUMRECIBOS " +
					" FROM " + FacDisqueteCargosBean.T_NOMBRETABLA + ", " 
							 + CenBancosBean.T_NOMBRETABLA + ", "  
							 + FacBancoInstitucionBean.T_NOMBRETABLA + 
					" WHERE " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDINSTITUCION + " = " + form.getIdInstitucion()  + 
			         	" AND " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDINSTITUCION + " = " + FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_IDINSTITUCION +
			         	" AND " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_BANCOS_CODIGO + " = " + FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_BANCOS_CODIGO +
			         	" AND " + FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_COD_BANCO+"="+CenBancosBean.T_NOMBRETABLA + "." + CenBancosBean.C_CODIGO;
			
							//FILTRO BANCO
							if(form.getCodigoBanco() != null && !form.getCodigoBanco().equals("")){
								sql += " AND " + FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_COD_BANCO + " = " +form.getCodigoBanco();
							}
							
							//FILTRO ORIGEN
							if(form.getOrigen() != null && form.getOrigen().equals("1")){
								//FILTRO SERIE
								if(form.getIdSerieFacturacion() != null && !form.getIdSerieFacturacion().equals("")){
									sql += " AND " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDSERIEFACTURACION + " = " +form.getIdSerieFacturacion();
								}else{
									sql += " AND " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDSERIEFACTURACION + " IS NOT NULL ";
								}
								
							} else if(form.getOrigen() != null && form.getOrigen().equals("2")){
								sql += " AND " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDSERIEFACTURACION + " IS NULL ";
							}
							
							//FILTRO FECHAS
							String dFechaDesde = null, dFechaHasta = null;
							if (form.getFechaDesde()!=null && !form.getFechaDesde().trim().equals(""))
							    dFechaDesde = GstDate.getApplicationFormatDate("",form.getFechaDesde());
							if (form.getFechaHasta()!=null && !form.getFechaHasta().trim().equals(""))
								dFechaHasta = GstDate.getApplicationFormatDate("",form.getFechaHasta());
							if (dFechaDesde!=null || dFechaHasta!=null){
							    sql += " AND " + GstDate.dateBetweenDesdeAndHasta(FacDisqueteCargosBean.C_FECHACREACION,dFechaDesde,dFechaHasta);
							}
			
			
			         					//SQL POR ENCIMA PARA PODER FILTRAR POR COMISION Y NUMERO DE FACTURAS				
					String sqlGrande = " SELECT CARGOS.FECHACREACION, " +
									   "   CARGOS.BANCO, " +
									   "   CARGOS.IDSERIEFACTURACION, " +
						               "   CARGOS.NOMBREABREVIADO, " +
						               "   CARGOS.DESCRIPCION_PROGRAMACION, " +
						               "   CARGOS.IDDISQUETECARGOS, " +									   
						               "   CARGOS.NOMBREFICHERO, " +
						               "   CARGOS.TOTAL_REMESA, " +
						               "   CARGOS.NUMRECIBOS " +
						               " FROM  ( "+ sql + ") CARGOS WHERE 1=1";
					
					//FILTRO DESCRIPCION
					if (form.getDescripcion()!=null && !form.getDescripcion().trim().equals("")){
						sqlGrande += " AND CARGOS.DESCRIPCION_PROGRAMACION LIKE '%"+UtilidadesBDAdm.validateChars(form.getDescripcion().trim())+"%'";
					}					
					
					//FILTRO NUMRECIBOS
					if (form.getRecibosDesde()!=null && !form.getRecibosDesde().trim().equals("")){
						sqlGrande += " AND CARGOS.NUMRECIBOS >= " +form.getRecibosDesde();
					}
					if (form.getRecibosHasta()!=null && !form.getRecibosHasta().trim().equals("")){
						sqlGrande += " AND CARGOS.NUMRECIBOS <= " +form.getRecibosHasta();
					}					
					
					//FILTRO TOTAL_REMESA
					if (form.getImportesDesde()!=null && !form.getImportesDesde().trim().equals("")){
						sqlGrande += " AND CARGOS.TOTAL_REMESA >= " + form.getImportesDesde().replace(",", ".");
					}
					if (form.getImportesHasta()!=null && !form.getImportesHasta().trim().equals("")){
						sqlGrande += " AND CARGOS.TOTAL_REMESA <= " + form.getImportesHasta().replace(",", ".");
					}			
			
					sqlGrande +=" ORDER BY " + FacDisqueteCargosBean.C_FECHACREACION + " DESC"; 	
			
			PaginadorCaseSensitive paginador = new PaginadorCaseSensitive(sqlGrande);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador = null;
			}
			return paginador;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener los datos de los ficheros.");
		}    			
	}
	
	public Hashtable getInformeRemesa (String idInstitucion, String idDisqueteCargo) 
	{
		try {
			String sql = "SELECT  NUMEROLINEAS NUMREGISTROS, " +
							    " FECHACREACION FECHACREACIONFICHERO, " +
							    " FECHACARGO FECHAEMISIONORDENES, " +
			
							   " (SELECT SUM(IMPORTE) " +
					              " FROM FAC_FACTURAINCLUIDAENDISQUETE " +
					             " WHERE FAC_FACTURAINCLUIDAENDISQUETE.IDINSTITUCION = FAC_DISQUETECARGOS.IDINSTITUCION " +
					               " AND FAC_FACTURAINCLUIDAENDISQUETE.IDDISQUETECARGOS = FAC_DISQUETECARGOS.IDDISQUETECARGOS) AS IMPORTETOTAL," +
								   
							   " (SELECT COUNT(1)" +
							      " FROM FAC_FACTURAINCLUIDAENDISQUETE " +
							     " WHERE FAC_FACTURAINCLUIDAENDISQUETE.IDINSTITUCION = FAC_DISQUETECARGOS.IDINSTITUCION " +
							       " AND FAC_FACTURAINCLUIDAENDISQUETE.IDDISQUETECARGOS = FAC_DISQUETECARGOS.IDDISQUETECARGOS) AS NUMORDENES , " +
								
						       " (SELECT nombre "  +
						         "  FROM cen_institucion " +
						         " WHERE cen_institucion.IDINSTITUCION = FAC_DISQUETECARGOS.IDINSTITUCION) AS NOMBREINSTITUCION , " +
	
						       " (SELECT cen_persona.nifcif " +
						         "  FROM cen_institucion, cen_persona " +
						         " WHERE cen_institucion.IDINSTITUCION = FAC_DISQUETECARGOS.IDINSTITUCION " +
						          "  AND cen_persona.idpersona = cen_institucion.idpersona) AS CODIGOORDENANTE , " +
	
						       " (SELECT IBAN " + 
						         "  FROM fac_bancoinstitucion " +
						         " WHERE fac_bancoinstitucion.IDINSTITUCION = FAC_DISQUETECARGOS.IDINSTITUCION " +
						          "  AND fac_bancoinstitucion.bancos_codigo = FAC_DISQUETECARGOS.Bancos_Codigo) AS CUENTAABONO " + 
	
						"  FROM FAC_DISQUETECARGOS " +
						" WHERE FAC_DISQUETECARGOS.IDINSTITUCION = " + idInstitucion +
						"   AND FAC_DISQUETECARGOS.Iddisquetecargos = " + idDisqueteCargo; 
			
			Vector v = this.selectGenerico(sql);
			if (v == null || v.size() != 1) 
				return null;
			
			return (Hashtable)v.get(0);
		}
		catch (Exception e) {
			return null;
		}
	}
}
