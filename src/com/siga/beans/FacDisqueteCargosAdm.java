/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;


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
	 * @param  Integer - identificador de la institución
	 * @return  Vector - Vector con los registros 
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector getDatosFichero(Integer idInstitucion) throws ClsExceptions {
		Vector v = null;
		String selectPrincipal;
		String selectRecibo;
		String selectOrigen;
		String sFrom;
		String sQuery;
		String sWhere;
		String sOrden;
		RowsContainer rc = null;
				
		try{
			selectRecibo = " (SELECT COUNT (1) FROM " +
			FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + " WHERE " + 
			FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION 
			+ " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDINSTITUCION + " AND " +
			FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS 
			+ " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDDISQUETECARGOS + ") AS NUMRECIBOS ";
			
			selectPrincipal = " SELECT " + FacDisqueteCargosBean.C_FECHACREACION + ", " +
								" (select cen_bancos.codigo||'-'||substr(cen_bancos.NOMBRE, 1, 30)"+
		                        " from cen_bancos, fac_bancoinstitucion"+
		                        " where cen_bancos.codigo=fac_bancoinstitucion.cod_banco"+
		                        " and fac_bancoinstitucion.bancos_codigo=fac_disquetecargos.bancos_codigo"+
		                        " and fac_bancoinstitucion.idinstitucion="+idInstitucion+")AS BANCO,"+
								FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDSERIEFACTURACION + ", " + 
								//FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_NOMBREABREVIADO + ", " +
								"(select FAC_SERIEFACTURACION.NOMBREABREVIADO "+
								" from FAC_SERIEFACTURACION "+
								" where fac_seriefacturacion.idinstitucion = FAC_DISQUETECARGOS.IDINSTITUCION "+
								"  and fac_seriefacturacion.idseriefacturacion =  fac_disquetecargos.idseriefacturacion) NOMBREABREVIADO,"+
								FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDDISQUETECARGOS + ", " +
								FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_NOMBREFICHERO + ", " +
								
							    " (SELECT " + FacFacturacionProgramadaBean.C_DESCRIPCION + 
					               " FROM " + FacFacturacionProgramadaBean.T_NOMBRETABLA + 
					              " WHERE " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDINSTITUCION +
								    " AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDSERIEFACTURACION +
								    " AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDPROGRAMACION +" ) DESCRIPCION_PROGRAMACION, " + 

								" (SELECT SUM (" + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA +"."+ FacFacturaIncluidaEnDisqueteBean.C_IMPORTE +") " +
								   " FROM " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + 
								  " WHERE " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDINSTITUCION + 
								    " AND " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + " = " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDDISQUETECARGOS + 
								" ) AS TOTAL_REMESA ";
			
			sFrom = " FROM " + FacDisqueteCargosBean.T_NOMBRETABLA + ", " 
							 + CenBancosBean.T_NOMBRETABLA + ", "  
							 +FacBancoInstitucionBean.T_NOMBRETABLA;
							 
			
//							
			
			sWhere = " WHERE " + FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDINSTITUCION + " = " + idInstitucion+ 
			         " and "+FacDisqueteCargosBean.T_NOMBRETABLA + "." +FacDisqueteCargosBean.C_IDINSTITUCION+"="+FacBancoInstitucionBean.T_NOMBRETABLA+"."+FacBancoInstitucionBean.C_IDINSTITUCION+
					 " and "+FacDisqueteCargosBean.T_NOMBRETABLA + "." +FacDisqueteCargosBean.C_BANCOS_CODIGO+"="+FacBancoInstitucionBean.T_NOMBRETABLA+"."+FacBancoInstitucionBean.C_BANCOS_CODIGO+
					 " and "+FacBancoInstitucionBean.T_NOMBRETABLA+"."+FacBancoInstitucionBean.C_COD_BANCO+"="+CenBancosBean.T_NOMBRETABLA+"."+CenBancosBean.C_CODIGO
					;

			
			sOrden = " ORDER BY " + FacDisqueteCargosBean.C_FECHACREACION + " DESC"; 
			sQuery = selectPrincipal + ", " + selectRecibo + sFrom + sWhere + sOrden;	

			
			
			
			rc = new RowsContainer(); 
			rc = this.find(sQuery);
	        if (rc!=null) {
	        	v = new Vector();
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
		
		}catch (Exception e) {				
			
			throw new ClsExceptions (e, "Error al obtener los datos de los ficheros.");
		}
		
		return v;
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
	
						       " (SELECT cod_banco||'-'|| cod_sucursal ||'-'|| digitocontrol || '-'|| numerocuenta " + 
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
