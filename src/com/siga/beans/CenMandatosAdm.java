
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.censo.form.MantenimientoMandatosForm;
import com.siga.general.SIGAException;


/**
 * Administrador de Cliente
 * 
 * @author daniel.campos
 * @since 22-11-2004
 * @version miguel.villegas - 10-01-2005 - incorporacion 
 *  getNumColegiosEsCliente () y eliminarNoColegiado ()
 * @version raul.ggonzalez - 11-01-2005 - incorporacion 
 *  getDatosPersonalesCliente () y getGrupos ()
 * @version luismiguel.sanchezp - 22-03-2005 - Se anhade el método 
 *  getDatosCertificado ()
 * @version david.sanchezp - 27-12-2005 - incluir el campo sexo
 * @version RGG - CAMBIO VISIBILIDAD 
 *  public class CenMandatoAdm extends MasterBeanAdministrador {
 * @version adrian.ayala - 05-06-2008 - Limpieza de altaColegial ()
 */
public class CenMandatosAdm extends MasterBeanAdmVisible
{
	public static final int TIPOCLIENTE_PROD_UNICO = 0;
	public static final int TIPOCLIENTE_PROD_NOC = 10; // No-colegiado
	public static final int TIPOCLIENTE_PROD_COL = 20; // Colegiado no residente
	public static final int TIPOCLIENTE_PROD_RES = 30; // Colegiado residente
	public static final int TIPOCLIENTE_PROD_NOCPRO = 40; // No-colegiado en colegio en produccion
	public static final int TIPOCLIENTE_PROD_COLPRO = 50; // Colegiado no residente en colegio en produccion
	public static final int TIPOCLIENTE_PROD_RESPRO = 60; // Colegiado residente en colegio en produccion

	//////////////////// CONSTRUCTORES ////////////////////
	public CenMandatosAdm (UsrBean usuario) {
		super (CenClienteBean.T_NOMBRETABLA, usuario);
	}
	
	public CenMandatosAdm (Integer usuario, UsrBean usrbean, 
			  int idInstitucionCliente, long idPersonaCliente)
	{
		super (CenClienteBean.T_NOMBRETABLA, usuario, usrbean, 
			idInstitucionCliente, idPersonaCliente);
	}
	
	
	
	//////////////////// METODOS DE ADMINISTRADOR ////////////////////
	protected String[] getCamposBean ()
	{
		String [] campos = 
		{
				CenClienteBean.C_ABONOSBANCO,
				CenClienteBean.C_ASIENTOCONTABLE,
				CenClienteBean.C_CARACTER,
				CenClienteBean.C_CARGOSBANCO,
				CenClienteBean.C_COMISIONES,
				CenClienteBean.C_FECHAALTA,
				CenClienteBean.C_FECHAMODIFICACION,
				CenClienteBean.C_FOTOGRAFIA,
				CenClienteBean.C_GUIAJUDICIAL,
				CenClienteBean.C_IDINSTITUCION,
				CenClienteBean.C_IDLENGUAJE,
				CenClienteBean.C_IDPERSONA,
				CenClienteBean.C_IDTRATAMIENTO,
				CenClienteBean.C_PUBLICIDAD,
				CenClienteBean.C_USUMODIFICACION,
				CenClienteBean.C_LETRADO,	
				CenClienteBean.C_FECHACARGA,
				CenClienteBean.C_NOENVIARREVISTA, 			
				CenClienteBean.C_NOAPARECERREDABOGACIA,
				CenClienteBean.C_EXPORTARFOTO
		};
		return campos;
	} //getCamposBean ()
	
	protected String[] getClavesBean ()
	{
		String [] claves = 
		{
				CenClienteBean.C_IDINSTITUCION,
				CenClienteBean.C_IDPERSONA
		};
		return claves;
	} //getClavesBean ()

	protected String[] getOrdenCampos () {
		return getClavesBean ();
	} //getOrdenCampos ()

	protected MasterBean hashTableToBean (Hashtable hash)
			throws ClsExceptions
	{
		CenClienteBean bean = null;
		
		try
		{
			bean = new CenClienteBean ();
			bean.setAbonosBanco (UtilidadesHash.getString (hash, CenClienteBean.C_ABONOSBANCO));
			bean.setAsientoContable (UtilidadesHash.getString (hash, CenClienteBean.C_ASIENTOCONTABLE));
			bean.setCaracter (UtilidadesHash.getString (hash, CenClienteBean.C_CARACTER));
			bean.setCargosBanco (UtilidadesHash.getString (hash, CenClienteBean.C_CARGOSBANCO));
			bean.setComisiones (UtilidadesHash.getString (hash, CenClienteBean.C_COMISIONES));
			bean.setFechaAlta (UtilidadesHash.getString (hash, CenClienteBean.C_FECHAALTA));
			bean.setFechaMod (UtilidadesHash.getString (hash, CenClienteBean.C_FECHAMODIFICACION));
			bean.setFotografia (UtilidadesHash.getString (hash, CenClienteBean.C_FOTOGRAFIA));
			bean.setGuiaJudicial (UtilidadesHash.getString (hash, CenClienteBean.C_GUIAJUDICIAL));
			bean.setIdInstitucion (UtilidadesHash.getInteger (hash, CenClienteBean.C_IDINSTITUCION));
			bean.setIdLenguaje (UtilidadesHash.getString (hash, CenClienteBean.C_IDLENGUAJE));
			bean.setIdPersona (UtilidadesHash.getLong (hash, CenClienteBean.C_IDPERSONA));
			bean.setIdTratamiento (UtilidadesHash.getInteger (hash, CenClienteBean.C_IDTRATAMIENTO));
			bean.setPublicidad (UtilidadesHash.getString (hash, CenClienteBean.C_PUBLICIDAD));
			bean.setUsuMod (UtilidadesHash.getInteger (hash, CenClienteBean.C_USUMODIFICACION));
			bean.setLetrado (UtilidadesHash.getString (hash, CenClienteBean.C_LETRADO));
			bean.setFechaCarga (UtilidadesHash.getString (hash, CenClienteBean.C_FECHACARGA));
			bean.setNoEnviarRevista (UtilidadesHash.getString (hash, CenClienteBean.C_NOENVIARREVISTA));
			bean.setNoAparacerRedAbogacia (UtilidadesHash.getString (hash, CenClienteBean.C_NOAPARECERREDABOGACIA));
			bean.setExportarFoto (UtilidadesHash.getString (hash, CenClienteBean.C_EXPORTARFOTO));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	} //hashTableToBean ()

	protected Hashtable beanToHashTable (MasterBean bean)
			throws ClsExceptions
	{
		Hashtable htData = null;
		
		try
		{
			htData = new Hashtable ();
			CenClienteBean b = (CenClienteBean) bean;
			UtilidadesHash.set (htData, CenClienteBean.C_ABONOSBANCO,			b.getAbonosBanco());
			UtilidadesHash.set (htData, CenClienteBean.C_ASIENTOCONTABLE,		b.getAsientoContable());
			UtilidadesHash.set (htData, CenClienteBean.C_CARACTER,				b.getCaracter());
			UtilidadesHash.set (htData, CenClienteBean.C_CARGOSBANCO,			b.getCargosBanco());
			UtilidadesHash.set (htData, CenClienteBean.C_COMISIONES,			b.getComisiones());
			UtilidadesHash.set (htData, CenClienteBean.C_FECHAALTA,				b.getFechaAlta());
			UtilidadesHash.set (htData, CenClienteBean.C_FECHAMODIFICACION,		b.getFechaMod());
			UtilidadesHash.set (htData, CenClienteBean.C_FOTOGRAFIA,			b.getFotografia());
			UtilidadesHash.set (htData, CenClienteBean.C_GUIAJUDICIAL,			b.getGuiaJudicial());
			UtilidadesHash.set (htData, CenClienteBean.C_IDINSTITUCION,			b.getIdInstitucion());
			UtilidadesHash.set (htData, CenClienteBean.C_IDLENGUAJE,			b.getIdLenguaje());
			UtilidadesHash.set (htData, CenClienteBean.C_IDPERSONA,				b.getIdPersona());
			UtilidadesHash.set (htData, CenClienteBean.C_IDTRATAMIENTO,			b.getIdTratamiento());
			UtilidadesHash.set (htData, CenClienteBean.C_PUBLICIDAD,			b.getPublicidad());
			UtilidadesHash.set (htData, CenClienteBean.C_LETRADO,				b.getLetrado());
			UtilidadesHash.set (htData, CenClienteBean.C_FECHACARGA,			b.getFechaCarga());
			UtilidadesHash.set (htData, CenClienteBean.C_USUMODIFICACION,		b.getUsuMod());
			UtilidadesHash.set (htData, CenClienteBean.C_NOENVIARREVISTA,		b.getNoEnviarRevista());
			UtilidadesHash.set (htData, CenClienteBean.C_NOAPARECERREDABOGACIA,	b.getNoAparacerRedAbogacia());
			UtilidadesHash.set (htData, CenClienteBean.C_EXPORTARFOTO,			b.getExportarFoto());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	} //beanToHashTable ()
	

	/**
	 * Obtiene los clientes colegiados para una institución y
	 * un formulario de datos (MantenimientoMandatosForm)
	 * 
	 * @param idInstitucion 
	 * @param formulario Formulario de busqueda de clientes con los datos de busqueda 
	 * @return java.util.Vector Vector de tablas hash  
	 */
	public Paginador getClientesMandatos(String idInstitucion, MantenimientoMandatosForm formulario, String idioma) throws ClsExceptions, SIGAException {
	  	// Acceso a BBDD
		try {
			String sql = "SELECT F_SIGA_GETNCOL_NCOM(" + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDINSTITUCION + ", " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + ") AS NCOLEGIADO," +
				CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF + "," +
				CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + "," +
				CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + "," +
				" DECODE(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + ", '#NA', '', " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + "|| ' ' || " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ") AS APELLIDOS," +
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN + "," +
				CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_TITULAR + "," +
				" DECODE(" + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_TIPOMANDATO + ", 0, 'SERVICIOS', 1, 'PRODUCTOS') AS TIPOMANDATO," +
				CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_REFMANDATOSEPA + " AS REFERENCIA," +
				CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_FIRMA_FECHA + " AS FECHAFIRMA," +
				CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_FIRMA_LUGAR + " AS LUGARFIRMA," +
				CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDCUENTA + "," +
				CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDMANDATO +			
			" FROM " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "," +
				CenPersonaBean.T_NOMBRETABLA + "," + 
				CenCuentasBancariasBean.T_NOMBRETABLA +			
			" WHERE " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " = " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDPERSONA +
				" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDINSTITUCION +
				" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDPERSONA +
				" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + " = " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDCUENTA +
				" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_FECHABAJA + " IS NULL" +
				" AND " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDINSTITUCION + " = " + usrbean.getLocation();
			
			if (formulario.getChkPendientesFirmar()!=null && formulario.getChkPendientesFirmar().equalsIgnoreCase("1")){
				sql += " AND " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_FIRMA_FECHA + " IS NULL";
			}
			// Aplicamos los filtros de busqueda

			if (formulario.getNombrePersona()!=null && !formulario.getNombrePersona().trim().equals("")) {
				sql += " AND UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + ") LIKE '%" + formulario.getNombrePersona().toUpperCase() + "%'";
			}
			
			if (formulario.getApellido1()!=null && !formulario.getApellido1().trim().equals("")) {
				sql += " AND UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + ") LIKE '%" + formulario.getApellido1().toUpperCase() + "%'";
			}
			
			if (formulario.getApellido2()!=null && !formulario.getApellido2().trim().equals("")) {
				sql += " AND UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ") LIKE '%" + formulario.getApellido2().toUpperCase() + "%'";
			}
			
			if (formulario.getNif()!=null && !formulario.getNif().trim().equals("")) {
				sql += " AND UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF + ") LIKE '%" + formulario.getNif().toUpperCase() + "%'";
			}
			
			
			/* Inicio - Control CEN_MANDATOS_CUENTASBANCARIAS.FIRMA_FECHA Inicio .. Fin */
			String dFechaDesde = null, dFechaHasta = null;
			if (formulario.getFechaFirmaInicio()!=null && !formulario.getFechaFirmaInicio().trim().equals(""))
			    dFechaDesde = GstDate.getApplicationFormatDate("",formulario.getFechaFirmaInicio());
			
			if (formulario.getFechaFirmaFin()!=null && !formulario.getFechaFirmaFin().trim().equals(""))
				dFechaHasta = GstDate.getApplicationFormatDate("",formulario.getFechaFirmaFin());
			
			if (dFechaDesde!=null || dFechaHasta!=null){
			    sql += " AND " + GstDate.dateBetweenDesdeAndHasta(CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_FIRMA_FECHA, dFechaDesde, dFechaHasta);
			}
			/* Fin - Control CEN_MANDATOS_CUENTASBANCARIAS.FIRMA_FECHA Inicio .. Fin */
			
			
			/* Inicio - Control CEN_CUENTASBANCARIAS.FECHAMODIFICACION Inicio .. Fin */
			dFechaDesde = null;
			dFechaHasta = null;
			if (formulario.getFechaModInicio()!=null && !formulario.getFechaModInicio().trim().equals(""))
			    dFechaDesde = GstDate.getApplicationFormatDate("",formulario.getFechaModInicio());
			
			if (formulario.getFechaModFin()!=null && !formulario.getFechaModFin().trim().equals(""))
				dFechaHasta = GstDate.getApplicationFormatDate("",formulario.getFechaModFin());
			
			if (dFechaDesde!=null || dFechaHasta!=null){
			    sql += " AND " + GstDate.dateBetweenDesdeAndHasta(CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_FECHAMODIFICACION, dFechaDesde, dFechaHasta);
			}			
			/* Fin - Control CEN_CUENTASBANCARIAS.FECHAMODIFICACION Inicio .. Fin */
			
			
			if (formulario.getTipoMandato()!=null && !formulario.getTipoMandato().trim().equals("")) {
				sql += " AND " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_TIPOMANDATO + " = '" + formulario.getTipoMandato() + "'";
			}
			
			if (formulario.getTipoCliente()!=null && !formulario.getTipoCliente().trim().equals("")) {
				
				if (formulario.getTipoCliente().equalsIgnoreCase("C")){
				
					sql += " AND EXISTS (" +
							" SELECT 1 " +
							" FROM " + CenColegiadoBean.T_NOMBRETABLA +
							" WHERE " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA + " = " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA +
								" AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION + " = " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDINSTITUCION;					
					if (formulario.getNumeroColegiado()!=null && !formulario.getNumeroColegiado().trim().equals("")) {
						sql += " AND DECODE(" + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_COMUNITARIO + ", 1, " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOMUNITARIO + ", " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOLEGIADO + ") = " + formulario.getNumeroColegiado();
					}
					sql += ")";
					
					if (formulario.getTipoColegiado()!=null && !formulario.getTipoColegiado().trim().equals("")) {
						sql += " AND EXISTS (" + 
	 							" SELECT 1" + 
	 							" FROM " + CenDatosColegialesEstadoBean.T_NOMBRETABLA +
	 							" WHERE " + CenDatosColegialesEstadoBean.T_NOMBRETABLA + "." + CenDatosColegialesEstadoBean.C_IDINSTITUCION + " = " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDINSTITUCION +
	 								" AND " + CenDatosColegialesEstadoBean.T_NOMBRETABLA + "." + CenDatosColegialesEstadoBean.C_IDPERSONA + " = " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDPERSONA +
	 								" AND " + CenDatosColegialesEstadoBean.T_NOMBRETABLA + "." + CenDatosColegialesEstadoBean.C_IDESTADO + 
	 									(
											formulario.getTipoColegiado().equals(String.valueOf(ClsConstants.ESTADO_COLEGIAL_ALTA)) 
	 											? " IN (" + ClsConstants.ESTADO_COLEGIAL_EJERCIENTE + ", " + ClsConstants.ESTADO_COLEGIAL_SINEJERCER + ")"
												: " = " + formulario.getTipoColegiado() 
										) +
	 								" AND " + CenDatosColegialesEstadoBean.T_NOMBRETABLA + "." + CenDatosColegialesEstadoBean.C_FECHAESTADO + " = (" + 
	 									" SELECT MAX(CDCE." + CenDatosColegialesEstadoBean.C_FECHAESTADO + ")" +
	 									" FROM " + CenDatosColegialesEstadoBean.T_NOMBRETABLA + " CDCE " + 
	 									" WHERE CDCE." + CenDatosColegialesEstadoBean.C_IDINSTITUCION + " = " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDINSTITUCION +
	 										" AND CDCE." + CenDatosColegialesEstadoBean.C_IDPERSONA + " = " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDPERSONA +
	 										" AND CDCE." + CenDatosColegialesEstadoBean.C_FECHAESTADO + " <= SYSDATE " +
	 								")" +
	 							")";
					 }
					 
				} else if (formulario.getTipoCliente().equalsIgnoreCase("N")) {
					sql += " AND NOT EXISTS (" +
								" SELECT 1 " +
								" FROM " + CenColegiadoBean.T_NOMBRETABLA +
								" WHERE " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA + " = " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA +
									" AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION + " = " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDINSTITUCION +
							" ) ";
					
					if (formulario.getTipoNoColegiado()!=null && !formulario.getTipoNoColegiado().trim().equals("")) {
						sql += " AND NOT EXISTS (" +
									" SELECT 1 " +
									" FROM " + CenNoColegiadoBean.T_NOMBRETABLA +
									" WHERE " + CenNoColegiadoBean.T_NOMBRETABLA + "." + CenNoColegiadoBean.C_IDPERSONA + " = " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA +
										" AND " + CenNoColegiadoBean.T_NOMBRETABLA + "." + CenNoColegiadoBean.C_IDINSTITUCION + " = " + CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_IDINSTITUCION +
										" AND " + CenNoColegiadoBean.T_NOMBRETABLA + "." + CenNoColegiadoBean.C_TIPO + " = '" + formulario.getTipoNoColegiado() + "'" + 
								" ) ";
					}
				}
			}

			sql += " ORDER BY APELLIDOS ASC, " + CenPersonaBean.C_NOMBRE + " ASC";			
			
			Paginador paginador = new Paginador(sql);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}
			
			return paginador;
		} 
		catch (Exception e) { 	
			throw new ClsExceptions(e,"Error obteniendo clientes colegiados"); 
		}
	}

	/**
	 * Firma un mandato
	 * @param ref referencia del mandato (es unica por colegio)
	 * @param fecha fecha de firma del mandato
	 * @param lugar lugar en que se ha firmado el mandato
	 * @return
	 */
	public boolean firmarReferencia(String ref, String fecha, String lugar){
		String sql = "update cen_mandatos_cuentasbancarias mcb ";
			sql+=" set firma_fecha='"+fecha+"'";
			sql+= ", firma_lugar='"+lugar.replaceAll("'", "''")+"'";
			sql+= ", mcb.fechamodificacion=sysdate";
			sql+= ", mcb.usumodificacion="+usrbean.getUserName();
			sql+=" where mcb.refmandatosepa='"+ref+"'";
			sql+=" and mcb.idInstitucion="+usrbean.getLocation();
			sql+=" and mcb.firma_fecha is null";
		try {
			if(this.updateOnlyOneSQL(sql)){
				return true;
			}else{
				return false;
			}
		} catch (ClsExceptions e) {
			return false;
		}
		
	}

}