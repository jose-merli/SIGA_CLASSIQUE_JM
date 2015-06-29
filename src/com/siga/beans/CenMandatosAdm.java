
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
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT F_SIGA_GETNCOL_NCOM(");
			sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
			sql.append(", ");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_IDPERSONA);
			sql.append(") AS NCOLEGIADO,");			
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_NIFCIF);
			sql.append(",");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_IDPERSONA);
			sql.append(",");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_NOMBRE);
			sql.append(",");
			sql.append(" DECODE(");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_APELLIDOS1);
			sql.append(", '#NA', '', ");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_APELLIDOS1);
			sql.append("|| ' ' || ");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_APELLIDOS2);
			sql.append(") AS APELLIDOS,");
			sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenCuentasBancariasBean.C_IBAN);
			sql.append(",");
			sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenCuentasBancariasBean.C_TITULAR);
			sql.append(",");
			sql.append(" DECODE(");
			sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenMandatosCuentasBancariasBean.C_TIPOMANDATO);
			sql.append(", 0, 'SERVICIOS', 1, 'PRODUCTOS') AS TIPOMANDATO,");
			sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenMandatosCuentasBancariasBean.C_REFMANDATOSEPA);
			sql.append(" AS REFERENCIA,");
			sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenMandatosCuentasBancariasBean.C_FIRMA_FECHA);
			sql.append(" AS FECHAFIRMA,");
			sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenMandatosCuentasBancariasBean.C_FIRMA_LUGAR);
			sql.append(" AS LUGARFIRMA,");
			sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenMandatosCuentasBancariasBean.C_IDCUENTA);
			sql.append(",");
			sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenMandatosCuentasBancariasBean.C_IDMANDATO);			
			sql.append(" FROM ");
			sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(",");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(","); 
			sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);			
			sql.append(" WHERE ");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_IDPERSONA);
			sql.append(" = ");
			sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenMandatosCuentasBancariasBean.C_IDPERSONA);
			sql.append(" AND ");
			sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenCuentasBancariasBean.C_IDINSTITUCION);
			sql.append(" = ");
			sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
			sql.append(" AND ");
			sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenCuentasBancariasBean.C_IDPERSONA);
			sql.append(" = ");
			sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenMandatosCuentasBancariasBean.C_IDPERSONA);
			sql.append(" AND ");
			sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenCuentasBancariasBean.C_IDCUENTA);
			sql.append(" = ");
			sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenMandatosCuentasBancariasBean.C_IDCUENTA);
			sql.append(" AND ");
			sql.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenCuentasBancariasBean.C_FECHABAJA);
			sql.append(" IS NULL");
			sql.append(" AND ");
			sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
			sql.append(" = ");
			sql.append(usrbean.getLocation());
			
			if (formulario.getChkPendientesFirmar()!=null && formulario.getChkPendientesFirmar().equalsIgnoreCase("1")){
				sql.append(" AND ");
				sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CenMandatosCuentasBancariasBean.C_FIRMA_FECHA);
				sql.append(" IS NULL");
			}

			if (formulario.getNombrePersona()!=null && !formulario.getNombrePersona().trim().equals("")) {
				sql.append(" AND UPPER(");
				sql.append(CenPersonaBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CenPersonaBean.C_NOMBRE);
				sql.append(") LIKE '%");
				sql.append(formulario.getNombrePersona().toUpperCase());
				sql.append("%'");;
			}
			
			if (formulario.getApellido1()!=null && !formulario.getApellido1().trim().equals("")) {
				sql.append(" AND UPPER(");
				sql.append(CenPersonaBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CenPersonaBean.C_APELLIDOS1);
				sql.append(") LIKE '%");
				sql.append(formulario.getApellido1().toUpperCase());
				sql.append("%'");
			}
			
			if (formulario.getApellido2()!=null && !formulario.getApellido2().trim().equals("")) {
				sql.append(" AND UPPER(");
				sql.append(CenPersonaBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CenPersonaBean.C_APELLIDOS2);
				sql.append(") LIKE '%");
				sql.append(formulario.getApellido2().toUpperCase());
				sql.append("%'");
			}
			
			if (formulario.getNif()!=null && !formulario.getNif().trim().equals("")) {
				sql.append(" AND UPPER(");
				sql.append(CenPersonaBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CenPersonaBean.C_NIFCIF);
				sql.append(") LIKE '%");
				sql.append(formulario.getNif().toUpperCase());
				sql.append("%'");;
			}
			
			/* Inicio - Control CEN_MANDATOS_CUENTASBANCARIAS.FIRMA_FECHA Inicio .. Fin */
			String dFechaDesde = null, dFechaHasta = null;
			if (formulario.getFechaFirmaInicio()!=null && !formulario.getFechaFirmaInicio().trim().equals(""))
			    dFechaDesde = GstDate.getApplicationFormatDate("",formulario.getFechaFirmaInicio());
			
			if (formulario.getFechaFirmaFin()!=null && !formulario.getFechaFirmaFin().trim().equals(""))
				dFechaHasta = GstDate.getApplicationFormatDate("",formulario.getFechaFirmaFin());
			
			if (dFechaDesde!=null || dFechaHasta!=null){
				sql.append(" AND ");
				sql.append(GstDate.dateBetweenDesdeAndHasta(CenMandatosCuentasBancariasBean.T_NOMBRETABLA + "." + CenMandatosCuentasBancariasBean.C_FIRMA_FECHA, dFechaDesde, dFechaHasta));
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
				sql.append(" AND ");
				sql.append(GstDate.dateBetweenDesdeAndHasta(CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_FECHAMODIFICACION, dFechaDesde, dFechaHasta));
			}			
			/* Fin - Control CEN_CUENTASBANCARIAS.FECHAMODIFICACION Inicio .. Fin */
			
			if (formulario.getTipoMandato()!=null && !formulario.getTipoMandato().trim().equals("")) {
				sql.append(" AND ");
				sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CenMandatosCuentasBancariasBean.C_TIPOMANDATO);
				sql.append(" = '");
				sql.append(formulario.getTipoMandato());
				sql.append("'");
			}
			
			if (formulario.getTipoCliente()!=null && !formulario.getTipoCliente().trim().equals("")) {				
				if (formulario.getTipoCliente().equalsIgnoreCase("C")){				
					sql.append(" AND EXISTS (");
					sql.append(" SELECT 1 ");
					sql.append(" FROM ");
					sql.append(CenColegiadoBean.T_NOMBRETABLA);
					sql.append(" WHERE ");
					sql.append(CenColegiadoBean.T_NOMBRETABLA);
					sql.append(".");
					sql.append(CenColegiadoBean.C_IDPERSONA);
					sql.append(" = ");
					sql.append(CenPersonaBean.T_NOMBRETABLA);
					sql.append(".");
					sql.append(CenPersonaBean.C_IDPERSONA);
					sql.append(" AND ");
					sql.append(CenColegiadoBean.T_NOMBRETABLA);
					sql.append(".");
					sql.append(CenColegiadoBean.C_IDINSTITUCION);
					sql.append(" = ");
					sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
					sql.append(".");
					sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
					
					if (formulario.getNumeroColegiado()!=null && !formulario.getNumeroColegiado().trim().equals("")) {
						sql.append(" AND DECODE(");
						sql.append(CenColegiadoBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenColegiadoBean.C_COMUNITARIO);
						sql.append(", 1, ");
						sql.append(CenColegiadoBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenColegiadoBean.C_NCOMUNITARIO);
						sql.append(", ");
						sql.append(CenColegiadoBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenColegiadoBean.C_NCOLEGIADO);
						sql.append(") = ");
						sql.append(formulario.getNumeroColegiado());
					}
					sql.append(")");
					
					if (formulario.getTipoColegiado()!=null && !formulario.getTipoColegiado().trim().equals("")) {
						sql.append(" AND EXISTS ("); 
						sql.append(" SELECT 1"); 
						sql.append(" FROM ");
						sql.append(CenDatosColegialesEstadoBean.T_NOMBRETABLA);
						sql.append(" WHERE ");
						sql.append(CenDatosColegialesEstadoBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenDatosColegialesEstadoBean.C_IDINSTITUCION);
						sql.append(" = ");
						sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
						sql.append(" AND ");
						sql.append(CenDatosColegialesEstadoBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenDatosColegialesEstadoBean.C_IDPERSONA);
						sql.append(" = ");
						sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenMandatosCuentasBancariasBean.C_IDPERSONA);
						sql.append(" AND ");
						sql.append(CenDatosColegialesEstadoBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenDatosColegialesEstadoBean.C_IDESTADO); 
						
						if (formulario.getTipoColegiado().equals(String.valueOf(ClsConstants.ESTADO_COLEGIAL_ALTA))) {
							sql.append(" IN (");
							sql.append(ClsConstants.ESTADO_COLEGIAL_EJERCIENTE);
							sql.append(", ");
							sql.append(ClsConstants.ESTADO_COLEGIAL_SINEJERCER);
							sql.append(")");
						} else {
							sql.append(" = ");
							sql.append(formulario.getTipoColegiado()); 
						}
						sql.append(" AND ");
						sql.append(CenDatosColegialesEstadoBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenDatosColegialesEstadoBean.C_FECHAESTADO);
						sql.append(" = ("); 
						sql.append(" SELECT MAX(CDCE.");
						sql.append(CenDatosColegialesEstadoBean.C_FECHAESTADO);
						sql.append(")");
						sql.append(" FROM ");
						sql.append(CenDatosColegialesEstadoBean.T_NOMBRETABLA);
						sql.append(" CDCE "); 
						sql.append(" WHERE CDCE.");
						sql.append(CenDatosColegialesEstadoBean.C_IDINSTITUCION);
						sql.append(" = ");
						sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
						sql.append(" AND CDCE.");
						sql.append(CenDatosColegialesEstadoBean.C_IDPERSONA);
						sql.append(" = ");
						sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenMandatosCuentasBancariasBean.C_IDPERSONA);
						sql.append(" AND CDCE.");
						sql.append(CenDatosColegialesEstadoBean.C_FECHAESTADO);
						sql.append(" <= SYSDATE ");
						sql.append(")");
						sql.append(")");
					 }
					 
				} else if (formulario.getTipoCliente().equalsIgnoreCase("N")) {
					sql.append(" AND NOT EXISTS (");
					sql.append(" SELECT 1 ");
					sql.append(" FROM ");
					sql.append(CenColegiadoBean.T_NOMBRETABLA);
					sql.append(" WHERE ");
					sql.append(CenColegiadoBean.T_NOMBRETABLA);
					sql.append(".");
					sql.append(CenColegiadoBean.C_IDPERSONA);
					sql.append(" = ");
					sql.append(CenPersonaBean.T_NOMBRETABLA);
					sql.append(".");
					sql.append(CenPersonaBean.C_IDPERSONA);
					sql.append(" AND ");
					sql.append(CenColegiadoBean.T_NOMBRETABLA);
					sql.append(".");
					sql.append(CenColegiadoBean.C_IDINSTITUCION);
					sql.append(" = ");
					sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
					sql.append(".");
					sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
					sql.append(" ) ");
					
					if (formulario.getTipoNoColegiado()!=null && !formulario.getTipoNoColegiado().trim().equals("")) {
						sql.append(" AND NOT EXISTS (");
						sql.append(" SELECT 1 ");
						sql.append(" FROM ");
						sql.append(CenNoColegiadoBean.T_NOMBRETABLA);
						sql.append(" WHERE ");
						sql.append(CenNoColegiadoBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenNoColegiadoBean.C_IDPERSONA);
						sql.append(" = ");
						sql.append(CenPersonaBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenPersonaBean.C_IDPERSONA);
						sql.append(" AND ");
						sql.append(CenNoColegiadoBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenNoColegiadoBean.C_IDINSTITUCION);
						sql.append(" = ");
						sql.append(CenMandatosCuentasBancariasBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenMandatosCuentasBancariasBean.C_IDINSTITUCION);
						sql.append(" AND ");
						sql.append(CenNoColegiadoBean.T_NOMBRETABLA);
						sql.append(".");
						sql.append(CenNoColegiadoBean.C_TIPO);
						sql.append(" = '");
						sql.append(formulario.getTipoNoColegiado());
						sql.append("'"); 
						sql.append(" ) ");
					}
				}
			}

			sql.append(" ORDER BY APELLIDOS ASC, ");
			sql.append(CenPersonaBean.C_NOMBRE);
			sql.append(" ASC");			
			
			Paginador paginador = new Paginador(sql.toString());				
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