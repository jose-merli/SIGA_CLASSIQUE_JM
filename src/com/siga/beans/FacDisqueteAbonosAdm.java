/*
 * VERSIONES:
 * 
 * nuria.rgonzalez - 29-03-2004 - Creaci�n
 * jose.barrientos - 28-02-2009 - A�adidos los campos fcs y nlineas
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
import com.siga.facturacion.form.FicheroBancarioAbonosForm;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla FAC_DISQUETEABONOS de la BBDD
* 
*/
public class FacDisqueteAbonosAdm  extends MasterBeanAdministrador {
	
	/**	
	 * @param usuario
	 */
	public FacDisqueteAbonosAdm (UsrBean usu) {
		super (FacDisqueteAbonosBean.T_NOMBRETABLA, usu);
	}
	
	/** 
	 *  Funcion que devuelve los campos de la tabla.
	 * @return  String[] Los campos de la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {FacDisqueteAbonosBean.C_IDINSTITUCION, 	
				FacDisqueteAbonosBean.C_IDDISQUETEABONO,
				FacDisqueteAbonosBean.C_BANCOS_CODIGO,
				FacDisqueteAbonosBean.C_FECHA,
				FacDisqueteAbonosBean.C_NOMBREFICHERO,
				FacDisqueteAbonosBean.C_FCS,
				FacDisqueteAbonosBean.C_FECHAMODIFICACION,
				FacDisqueteAbonosBean.C_USUMODIFICACION,
				FacDisqueteAbonosBean.C_NUMEROLINEAS};
		return campos;
	}

	/** 
	 *  Funcion que devuelve las claves de la tabla.
	 * @return  String[] Los campos de la tabla   
	 */
	protected String[] getClavesBean() {
		String [] claves = {FacDisqueteAbonosBean.C_IDINSTITUCION, FacDisqueteAbonosBean.C_IDDISQUETEABONO};
		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  FacLineaFacturaBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacDisqueteAbonosBean bean = null;
		
		try {
			bean = new FacDisqueteAbonosBean();
			bean.setIdInstitucion		(UtilidadesHash.getInteger(hash, FacDisqueteAbonosBean.C_IDINSTITUCION));
			bean.setIdDisqueteAbono		(UtilidadesHash.getLong(hash, FacDisqueteAbonosBean.C_IDDISQUETEABONO));
			bean.setBancosCodigo		(UtilidadesHash.getString(hash, FacDisqueteAbonosBean.C_BANCOS_CODIGO));
			bean.setFecha				(UtilidadesHash.getString(hash, FacDisqueteAbonosBean.C_FECHA));
			bean.setNombreFichero		(UtilidadesHash.getString(hash, FacDisqueteAbonosBean.C_NOMBREFICHERO));
			bean.setNombreFichero		(UtilidadesHash.getString(hash, FacDisqueteAbonosBean.C_FCS));
			bean.setFechaMod			(UtilidadesHash.getString(hash, FacDisqueteAbonosBean.C_FECHAMODIFICACION));
			bean.setUsuMod				(UtilidadesHash.getInteger(hash, FacDisqueteAbonosBean.C_USUMODIFICACION));
			bean.setNumeroLineas		(UtilidadesHash.getInteger(hash, FacDisqueteAbonosBean.C_NUMEROLINEAS));
		}
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
			FacDisqueteAbonosBean b = (FacDisqueteAbonosBean) bean;
			UtilidadesHash.set(htData, FacDisqueteAbonosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, FacDisqueteAbonosBean.C_IDDISQUETEABONO, b.getIdDisqueteAbono());
			UtilidadesHash.set(htData, FacDisqueteAbonosBean.C_BANCOS_CODIGO, b.getBancosCodigo());
			UtilidadesHash.set(htData, FacDisqueteAbonosBean.C_FECHA, b.getFecha());
			UtilidadesHash.set(htData, FacDisqueteAbonosBean.C_NOMBREFICHERO, b.getNombreFichero());
			UtilidadesHash.set(htData, FacDisqueteAbonosBean.C_FCS, b.getFCS());
			UtilidadesHash.set(htData, FacDisqueteAbonosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, FacDisqueteAbonosBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData, FacDisqueteAbonosBean.C_NUMEROLINEAS, b.getNumeroLineas());
			
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	/**
	 * Obtiene un vector con los datos del fichero
	 * @param idInstitucion String - identificador de la instituci�n
	 * @param abonosSJCS boolean abono
	 * @return PaginadorCaseSensitive 
	 * @throws ClsExceptions
	 */
	public PaginadorCaseSensitive getDatosFichero(FicheroBancarioAbonosForm form, boolean abonosSJCS) throws ClsExceptions {				
		try{
			String sql = " SELECT " + FacDisqueteAbonosBean.C_FECHA + ", " +
							FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_COD_BANCO + " || ' - ' || " + CenBancosBean.T_NOMBRETABLA + "." + CenBancosBean.C_NOMBRE + " AS BANCO, " + 
							FacDisqueteAbonosBean.T_NOMBRETABLA + "." + FacDisqueteAbonosBean.C_IDDISQUETEABONO + ", " +
							FacDisqueteAbonosBean.T_NOMBRETABLA + "." + FacDisqueteAbonosBean.C_NOMBREFICHERO + ", " +
							" ( " +
								" SELECT COUNT (1) " +
								" FROM " + FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA + 
								" WHERE " + FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA + "." + FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION + " = " + FacDisqueteAbonosBean.T_NOMBRETABLA + "." + FacDisqueteAbonosBean.C_IDINSTITUCION + 
									" AND " + FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA + "." + FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO + " = " + FacDisqueteAbonosBean.T_NOMBRETABLA + "." + FacDisqueteAbonosBean.C_IDDISQUETEABONO + 
							" ) AS NUMRECIBOS, " +
							" ( " +
								" SELECT SUM(IMPORTEABONADO) " +
								" FROM " + FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA + 
								" WHERE " + FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA + "." + FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION + " = " + FacDisqueteAbonosBean.T_NOMBRETABLA + "." + FacDisqueteAbonosBean.C_IDINSTITUCION + 
									" AND " + FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA + "." + FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO + " = " + FacDisqueteAbonosBean.T_NOMBRETABLA + "." + FacDisqueteAbonosBean.C_IDDISQUETEABONO + 
							" ) AS IMPORTE " +
						" FROM " + FacDisqueteAbonosBean.T_NOMBRETABLA + ", " + 
							CenBancosBean.T_NOMBRETABLA + ", " + 
							FacBancoInstitucionBean.T_NOMBRETABLA +
						" WHERE " + FacDisqueteAbonosBean.T_NOMBRETABLA + "." + FacDisqueteAbonosBean.C_IDINSTITUCION + " = " + form.getIdInstitucion() + 
							" AND " + FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_COD_BANCO + " = " + CenBancosBean.T_NOMBRETABLA + "." + CenBancosBean.C_CODIGO +
							" AND " + FacDisqueteAbonosBean.T_NOMBRETABLA + "." + FacDisqueteAbonosBean.C_BANCOS_CODIGO + " = " + FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_BANCOS_CODIGO +
							" AND " + FacDisqueteAbonosBean.T_NOMBRETABLA + "." + FacDisqueteAbonosBean.C_IDINSTITUCION + " = " + FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_IDINSTITUCION +
							" AND " + FacDisqueteAbonosBean.T_NOMBRETABLA + "." + FacDisqueteAbonosBean.C_IDINSTITUCION + " = " + FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_IDINSTITUCION +
							" AND " + FacDisqueteAbonosBean.T_NOMBRETABLA + "." + FacDisqueteAbonosBean.C_FCS + " = " + (abonosSJCS ? "'1'" : "'0'") ;
			
							//FILTRO BANCO
							if(form.getCodigoBanco() != null && !form.getCodigoBanco().equals("")){
								sql += " AND " + FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_COD_BANCO + " = " +form.getCodigoBanco();
							}
							
							//FILTRO FECHAS
							String dFechaDesde = null, dFechaHasta = null;
							if (form.getFechaDesde()!=null && !form.getFechaDesde().trim().equals(""))
							    dFechaDesde = GstDate.getApplicationFormatDate("",form.getFechaDesde());
							if (form.getFechaHasta()!=null && !form.getFechaHasta().trim().equals(""))
								dFechaHasta = GstDate.getApplicationFormatDate("",form.getFechaHasta());
							if (dFechaDesde!=null || dFechaHasta!=null){
							    sql += " AND " + GstDate.dateBetweenDesdeAndHasta(FacDisqueteAbonosBean.C_FECHA,dFechaDesde,dFechaHasta);
							}
							
							
					//SQL POR ENCIMA PARA PODER FILTRAR POR COMISION Y NUMERO DE FACTURAS				
					String sqlGrande = " SELECT FICHEROSBANCARIOS.FECHA, " +
									   "   FICHEROSBANCARIOS.BANCO, " +
									   "   FICHEROSBANCARIOS.IDDISQUETEABONO, " +
						               "   FICHEROSBANCARIOS.NOMBREFICHERO, " +
						               "   FICHEROSBANCARIOS.NUMRECIBOS, " +
						               "   FICHEROSBANCARIOS.IMPORTE " +
						               " FROM  ( "+ sql + ") FICHEROSBANCARIOS WHERE 1=1";
					
					//FILTRO ABONOS
					if (form.getAbonosDesde()!=null && !form.getAbonosDesde().trim().equals("")){
						sqlGrande += " AND FICHEROSBANCARIOS.NUMRECIBOS >= " +form.getAbonosDesde();
					}
					if (form.getAbonosHasta()!=null && !form.getAbonosHasta().trim().equals("")){
						sqlGrande += " AND FICHEROSBANCARIOS.NUMRECIBOS <= " +form.getAbonosHasta();
					}					
					
					//FILTRO IMPORTES
					if (form.getImportesDesde()!=null && !form.getImportesDesde().trim().equals("")){
						sqlGrande += " AND FICHEROSBANCARIOS.IMPORTE >= " + form.getImportesDesde().replace(",", ".");
					}
					if (form.getImportesHasta()!=null && !form.getImportesHasta().trim().equals("")){
						sqlGrande += " AND FICHEROSBANCARIOS.IMPORTE <= " + form.getImportesHasta().replace(",", ".");
					}			
			
					sqlGrande +=" ORDER BY " + FacDisqueteAbonosBean.C_FECHA + " DESC"; 
			
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

	/** 
	 * Obtiene el valor IDDISQUETEABONO, <br/>
	 * @param  institucion - identificador de la institucion 
	 * @return  Long - Identificador del disquete  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Long getNuevoID (String institucion) throws ClsExceptions, SIGAException 
	{

		Long resultado=null;
		
		try { 
            RowsContainer rc = new RowsContainer();

			String sql;
			sql ="SELECT (MAX(IDDISQUETEABONO) + 1) AS IDDISQUETEABONO FROM " + nombreTabla +
				" WHERE IDINSTITUCION =" + institucion;
		
			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDDISQUETEABONO").equals("")) {
					resultado=new Long(1);
				}
				else 
					resultado=UtilidadesHash.getLong(prueba, "IDDISQUETEABONO");;								
			}
		}	
//		catch (SIGAException e) {
//			throw e;
//		}

		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el getNuevoId en B.D.");		
		}
		return resultado;
	}
	
	
	/**
	 * @param idInstitucion
	 * @param idDisqueteAbono
	 * @return
	 */
	public Hashtable getInformeRemesa (String idInstitucion, String idDisqueteAbono) 
	{
		try {
			String sql = 
				
				" SELECT  FECHA FECHACREACIONFICHERO, " +
				"         FECHA FECHAEMISIONORDENES, " +
				"         NUMEROLINEAS NUMREGISTROS, " +

				"(SELECT COUNT(1)"+
				"   FROM FAC_ABONOINCLUIDOENDISQUETE "+
				"  WHERE FAC_ABONOINCLUIDOENDISQUETE.IDINSTITUCION = FAC_DISQUETEABONOS.IDINSTITUCION "+
				"    AND FAC_ABONOINCLUIDOENDISQUETE.IDDISQUETEABONO = FAC_DISQUETEABONOS.IDDISQUETEABONO) AS NUMORDENES, "+
				 
				"(SELECT SUM(IMPORTEABONADO) "+
				"  FROM Fac_Abonoincluidoendisquete "+
				" WHERE Fac_Abonoincluidoendisquete.IDINSTITUCION = FAC_DISQUETEabonos.IDINSTITUCION "+
				"   AND Fac_Abonoincluidoendisquete.IDDISQUETEabono = FAC_DISQUETEabonos.IDDISQUETEabono) AS IMPORTETOTAL, "+

				" (SELECT nombre "  +
				"  FROM cen_institucion " +
				" WHERE cen_institucion.IDINSTITUCION = FAC_DISQUETEabonos.IDINSTITUCION) AS NOMBREINSTITUCION , " +

				" (SELECT cen_persona.nifcif " +
				"  FROM cen_institucion, cen_persona " +
				" WHERE cen_institucion.IDINSTITUCION = FAC_DISQUETEabonos.IDINSTITUCION " +
				"  AND cen_persona.idpersona = cen_institucion.idpersona) AS CODIGOORDENANTE , " +

				" (SELECT cod_banco||'-'|| cod_sucursal ||'-'|| digitocontrol || numerocuenta " + 
				"  FROM fac_bancoinstitucion " +
				" WHERE fac_bancoinstitucion.IDINSTITUCION = FAC_DISQUETEabonos.IDINSTITUCION " +
				"  AND fac_bancoinstitucion.bancos_codigo = FAC_DISQUETEabonos.Bancos_Codigo) AS CUENTAABONO " + 

				"  FROM FAC_DISQUETEabonos " +
				" WHERE FAC_DISQUETEabonos.IDINSTITUCION = " + idInstitucion +
				"   AND FAC_DISQUETEabonos.Iddisqueteabono = " + idDisqueteAbono; 
			
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
