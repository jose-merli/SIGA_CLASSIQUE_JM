
package com.siga.beans;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.GstStringTokenizer;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.paginadores.Paginador;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.censo.action.Direccion;
import com.siga.censo.form.MantenimientoMandatosForm;
import com.siga.general.CenVisibilidad;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;
import com.siga.informes.InformeCertificadosEspeciales;


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

		Integer idTipoCVSubtipo1 = null;
		Integer idInstitucionSubtipo1=null;
		Integer idTipoCVSubtipo2 = null;
		Integer idInstitucionSubtipo2=null;
		Hashtable codigosBind = new Hashtable();
		int contador=0;
	  	
	  	// Acceso a BBDD
		try { 
			
			StringBuilder sql = new StringBuilder();
			
			
			sql.append("SELECT f_siga_getncol_ncom("+usrbean.getLocation()+", PER.IDPERSONA) NCOLEGIADO,");
			sql.append(" PER.NIFCIF NIFCIF,");
			sql.append(" PER.IDPERSONA IDPERSONA,");
			sql.append(" PER.NOMBRE NOMBRE,");
			sql.append(" PER.APELLIDOS1 || ' ' || PER.APELLIDOS2 APELLIDOS,");
			sql.append(" CUENTA.IBAN IBAN,");
			sql.append(" CUENTA.TITULAR TITULAR,");
			sql.append(" DECODE(MAN.TIPOMANDATO, 0, 'SERVICIOS', 1, 'PRODUCTOS') TIPOMANDATO,");
			sql.append(" MAN.REFMANDATOSEPA REFERENCIA,");
			sql.append(" MAN.FIRMA_FECHA FECHAFIRMA,");
			sql.append(" MAN.FIRMA_LUGAR LUGARFIRMA,");
			sql.append(" MAN.IDCUENTA IDCUENTA, ");
			sql.append(" MAN.IDMANDATO IDMANDATO ");

			sql.append(" FROM (select MANDATO.*, tipomandato as tipo from CEN_MANDATOS_CUENTASBANCARIAS MANDATO) MAN, CEN_PERSONA PER, CEN_CUENTASBANCARIAS CUENTA ");

			sql.append(" WHERE PER.IDPERSONA = MAN.IDPERSONA ");
			sql.append(" AND CUENTA.IDINSTITUCION = MAN.IDINSTITUCION ");
			sql.append(" AND CUENTA.IDPERSONA = MAN.IDPERSONA ");
			sql.append(" AND CUENTA.IDCUENTA = MAN.IDCUENTA ");
			sql.append(" AND CUENTA.FECHABAJA IS NULL ");
			sql.append(" AND MAN.IDINSTITUCION = "+usrbean.getLocation());
			if(formulario.getChkPendientesFirmar()!=null && formulario.getChkPendientesFirmar().equalsIgnoreCase("1")){
				sql.append(" AND MAN.FIRMA_FECHA IS NULL ");
			}
			// Aplicamos los filtros de busqueda
			if (formulario.getNumeroColegiado()!=null && !formulario.getNumeroColegiado().trim().equals("")) {
				sql.append(" AND f_siga_getncol_ncom("+usrbean.getLocation()+", PER.IDPERSONA)=");
				sql.append(formulario.getNumeroColegiado());
			}
			if (formulario.getNombrePersona()!=null && !formulario.getNombrePersona().trim().equals("")) {
				sql.append(" AND UPPER(PER.NOMBRE) LIKE '%"+formulario.getNombrePersona().toUpperCase()+"%' ");
			}
			if (formulario.getApellido1()!=null && !formulario.getApellido1().trim().equals("")) {
				sql.append(" AND UPPER(PER.APELLIDOS1) LIKE '%"+formulario.getApellido1().toUpperCase()+"%' ");
			}
			if (formulario.getApellido2()!=null && !formulario.getApellido2().trim().equals("")) {
				sql.append(" AND UPPER(PER.APELLIDOS2) LIKE '%"+formulario.getApellido2().toUpperCase()+"%' ");
			}
			if (!formulario.getNif().trim().equals("")) {
				sql.append(" AND UPPER(PER.NIFCIF) LIKE '%"+formulario.getNif().toUpperCase()+"%' ");
			}
			
			if (formulario.getFechaFirmaInicio()!=null && !formulario.getFechaFirmaInicio().trim().equals("")) {
				sql.append(" AND MAN.FIRMA_FECHA >=TO_DATE('"+formulario.getFechaFirmaInicio()+"','DD/MM/YYYY') ");
			}
			if (formulario.getFechaFirmaFin()!=null && !formulario.getFechaFirmaFin().trim().equals("")) {
				sql.append(" AND MAN.FIRMA_FECHA <=TO_DATE('"+formulario.getFechaFirmaFin()+"','DD/MM/YYYY') ");
			}
			if (formulario.getFechaModInicio()!=null && !formulario.getFechaModInicio().trim().equals("")) {
				sql.append(" AND CUENTA.FECHAMODIFICACION >=TO_DATE('"+formulario.getFechaModInicio()+"','DD/MM/YYYY') ");
			}
			if (formulario.getFechaModFin()!=null && !formulario.getFechaModFin().trim().equals("")) {
				sql.append(" AND CUENTA.FECHAMODIFICACION <=TO_DATE('"+formulario.getFechaModFin()+"','DD/MM/YYYY') ");
			}
			if (formulario.getTipoMandato()!=null && !formulario.getTipoMandato().trim().equals("")) {
				sql.append(" AND MAN.TIPOMANDATO='"+formulario.getTipoMandato()+"'");
			}

			sql.append(" Order by APELLIDOS asc, nombre asc ");			
			
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
			sql+= ", firma_lugar='"+lugar+"'";
			sql+= ", mcb.fechamodificacion=sysdate";
			sql+= ", mcb.usumodificacion="+usrbean.getUserName();
			sql+=" where mcb.refmandatosepa='"+ref+"'";
			sql+=" and mcb.idInstitucion="+usrbean.getLocation();
			sql+=" and mcb.firma_fecha is null";
		try {
			this.updateSQL(sql);
		} catch (ClsExceptions e) {
			e.printStackTrace();
		}
		return true;
		
	}

}