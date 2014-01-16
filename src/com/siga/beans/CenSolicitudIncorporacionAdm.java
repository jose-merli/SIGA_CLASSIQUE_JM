/*
 * Created on 20-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author daniel.campos
 */
public class CenSolicitudIncorporacionAdm extends MasterBeanAdministrador {
	
	public CenSolicitudIncorporacionAdm (UsrBean usu) {
		super(CenSolicitudIncorporacionBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenSolicitudIncorporacionBean.C_APELLIDO1,				CenSolicitudIncorporacionBean.C_APELLIDO2,
							CenSolicitudIncorporacionBean.C_CODIGOPOSTAL,			CenSolicitudIncorporacionBean.C_CORREOELECTRONICO,
							CenSolicitudIncorporacionBean.C_DOMICILIO,				CenSolicitudIncorporacionBean.C_FAX1,
							CenSolicitudIncorporacionBean.C_FAX2,					CenSolicitudIncorporacionBean.C_FECHAESTADO,
							CenSolicitudIncorporacionBean.C_FECHAMODIFICACION,
							CenSolicitudIncorporacionBean.C_FECHANACIMIENTO,		CenSolicitudIncorporacionBean.C_FECHASOLICITUD,
							CenSolicitudIncorporacionBean.C_IDESTADO,				CenSolicitudIncorporacionBean.C_IDESTADOCIVIL,
							CenSolicitudIncorporacionBean.C_IDINSTITUCION,			CenSolicitudIncorporacionBean.C_IDPAIS,
							CenSolicitudIncorporacionBean.C_IDPOBLACION,			CenSolicitudIncorporacionBean.C_IDPROVINCIA,		
							CenSolicitudIncorporacionBean.C_POBLACIONEXTRANJERA,		
							CenSolicitudIncorporacionBean.C_IDSOLICITUD,			CenSolicitudIncorporacionBean.C_IDTIPOCOLEGIACION,	
							CenSolicitudIncorporacionBean.C_IDTIPOIDENTIFICACION,	CenSolicitudIncorporacionBean.C_IDTIPOSOLICITUD,	
							CenSolicitudIncorporacionBean.C_IDTRATAMIENTO,			CenSolicitudIncorporacionBean.C_MOVIL,		
							CenSolicitudIncorporacionBean.C_NATURALDE,				CenSolicitudIncorporacionBean.C_NCOLEGIADO,			
							CenSolicitudIncorporacionBean.C_NOMBRE,					CenSolicitudIncorporacionBean.C_NUMEROIDENTIFICADOR,
							CenSolicitudIncorporacionBean.C_OBSERVACIONES,			CenSolicitudIncorporacionBean.C_TELEFONO1,			
							CenSolicitudIncorporacionBean.C_TELEFONO2,				CenSolicitudIncorporacionBean.C_SEXO,
							CenSolicitudIncorporacionBean.C_IDMODALIDADDOCUMENTACION, CenSolicitudIncorporacionBean.C_USUMODIFICACION,
							CenSolicitudIncorporacionBean.C_FECHAESTADOCOLEGIAL,   	CenSolicitudIncorporacionBean.C_ABONOCARGO,
							CenSolicitudIncorporacionBean.C_ABONOSJCS,				CenSolicitudIncorporacionBean.C_CBO_CODIGO,
							CenSolicitudIncorporacionBean.C_CODIGOSUCURSAL,			CenSolicitudIncorporacionBean.C_DIGITOCONTROL,
							CenSolicitudIncorporacionBean.C_NUMEROCUENTA,			CenSolicitudIncorporacionBean.C_TITULAR,
							CenSolicitudIncorporacionBean.C_IDPERSONATEMP,			CenSolicitudIncorporacionBean.C_IDDIRECCIONTEMP,
							CenSolicitudIncorporacionBean.C_RESIDENTE,				CenSolicitudIncorporacionBean.C_IDPERSONA,
							CenSolicitudIncorporacionBean.C_FECHAALTA,				CenSolicitudIncorporacionBean.C_IBAN};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenSolicitudIncorporacionBean.C_IDSOLICITUD};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenSolicitudIncorporacionBean bean = null;
		
		try {
			bean = new CenSolicitudIncorporacionBean();
			
			bean.setApellido1(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_APELLIDO1));
			bean.setApellido2(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_APELLIDO2));
			bean.setCodigoPostal(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_CODIGOPOSTAL));
			bean.setCorreoElectronico(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_CORREOELECTRONICO));
			bean.setDomicilio(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_DOMICILIO));
			bean.setFax1(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_FAX1));
			bean.setFax2(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_FAX2));
			bean.setFechaEstado(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_FECHAESTADO));
			bean.setFechaMod(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_FECHAMODIFICACION));
			bean.setFechaNacimiento(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_FECHANACIMIENTO));
			bean.setFechaSolicitud(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_FECHASOLICITUD));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, CenSolicitudIncorporacionBean.C_IDESTADO));
			bean.setIdEstadoCivil(UtilidadesHash.getInteger(hash, CenSolicitudIncorporacionBean.C_IDESTADOCIVIL));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CenSolicitudIncorporacionBean.C_IDINSTITUCION));
			bean.setIdPais(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_IDPAIS));
			bean.setIdPoblacion(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_IDPOBLACION));
			bean.setPoblacionExtranjera(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_POBLACIONEXTRANJERA));
			bean.setIdProvincia(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_IDPROVINCIA));
			bean.setIdSolicitud(UtilidadesHash.getLong(hash, CenSolicitudIncorporacionBean.C_IDSOLICITUD));
			bean.setIdTipoColegiacion(UtilidadesHash.getInteger(hash, CenSolicitudIncorporacionBean.C_IDTIPOCOLEGIACION));
			bean.setIdTipoIdentificacion(UtilidadesHash.getInteger(hash, CenSolicitudIncorporacionBean.C_IDTIPOIDENTIFICACION));
			bean.setIdTipoSolicitud(UtilidadesHash.getInteger(hash, CenSolicitudIncorporacionBean.C_IDTIPOSOLICITUD));
			bean.setIdTratamiento(UtilidadesHash.getInteger(hash, CenSolicitudIncorporacionBean.C_IDTRATAMIENTO));
			bean.setMovil(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_MOVIL));
			bean.setNaturalDe(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_NATURALDE));
			bean.setNColegiado(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_NCOLEGIADO));
			bean.setNombre(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_NOMBRE));
			bean.setNumeroIdentificador(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_NUMEROIDENTIFICADOR));
			bean.setObservaciones(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_OBSERVACIONES));
			bean.setTelefono1(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_TELEFONO1));
			bean.setTelefono2(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_TELEFONO2));
			bean.setSexo(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_SEXO));
			bean.setIdModalidadDocumentacion(UtilidadesHash.getInteger(hash, CenSolicitudIncorporacionBean.C_IDMODALIDADDOCUMENTACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenSolicitudIncorporacionBean.C_USUMODIFICACION));
			bean.setFechaEstadoColegial(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_FECHAESTADOCOLEGIAL));
			bean.setAbonoCargo(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_ABONOCARGO));
			bean.setAbonoSJCS(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_ABONOSJCS));
			bean.setCbo_Codigo(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_CBO_CODIGO));
			bean.setCodigoSucursal(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_CODIGOSUCURSAL));
			bean.setDigitoControl(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_DIGITOCONTROL));
			bean.setNumeroCuenta(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_NUMEROCUENTA));
			bean.setTitular(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_TITULAR));
			bean.setResidente(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_RESIDENTE).equalsIgnoreCase("1"));
			bean.setIdPersona(UtilidadesHash.getInteger(hash, CenSolicitudIncorporacionBean.C_IDPERSONA));
			bean.setFechaAlta(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_FECHAALTA));
			bean.setIdPersonaTemp(UtilidadesHash.getLong(hash, CenSolicitudIncorporacionBean.C_IDPERSONATEMP));
			bean.setIdDireccionTemp(UtilidadesHash.getLong(hash, CenSolicitudIncorporacionBean.C_IDDIRECCIONTEMP));
			bean.setIban(UtilidadesHash.getString(hash, CenSolicitudIncorporacionBean.C_IBAN));
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
			CenSolicitudIncorporacionBean b = (CenSolicitudIncorporacionBean) bean;
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_APELLIDO1, b.getApellido1());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_APELLIDO2, b.getApellido2());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_CORREOELECTRONICO, b.getCorreoElectronico());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_DOMICILIO, b.getDomicilio());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_FAX1, b.getFax1());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_FAX2, b.getFax2());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_FECHAESTADO, b.getFechaEstado());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_FECHANACIMIENTO, b.getFechaNacimiento());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_FECHASOLICITUD, b.getFechaSolicitud());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDESTADO, String.valueOf(b.getIdEstado()));
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDESTADOCIVIL, String.valueOf(b.getIdEstadoCivil()));
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDPAIS, b.getIdPais());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDPOBLACION, b.getIdPoblacion());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_POBLACIONEXTRANJERA, b.getPoblacionExtranjera());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDPROVINCIA, b.getIdProvincia());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDSOLICITUD, String.valueOf(b.getIdSolicitud()));
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDTIPOCOLEGIACION, String.valueOf(b.getIdTipoColegiacion()));
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDTIPOIDENTIFICACION, String.valueOf(b.getIdTipoIdentificacion()));
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDTIPOSOLICITUD, String.valueOf(b.getIdTipoSolicitud()));
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDTRATAMIENTO, String.valueOf(b.getIdTratamiento()));
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_MOVIL, b.getMovil());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_NATURALDE, b.getNaturalDe());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_NCOLEGIADO, b.getNColegiado());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_NUMEROIDENTIFICADOR, b.getNumeroIdentificador());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_OBSERVACIONES, b.getObservaciones());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_TELEFONO1, b.getTelefono1());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_TELEFONO2, b.getTelefono2());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_SEXO, b.getSexo());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDMODALIDADDOCUMENTACION, b.getIdModalidadDocumentacion());
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_FECHAESTADOCOLEGIAL, String.valueOf(b.getFechaEstadoColegial()));
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_ABONOCARGO, String.valueOf(b.getAbonoCargo()));   
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_ABONOSJCS, String.valueOf(b.getAbonoSJCS()));   
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_CBO_CODIGO, String.valueOf(b.getCbo_Codigo()));   
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_CODIGOSUCURSAL, String.valueOf(b.getCodigoSucursal()));   
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_DIGITOCONTROL, String.valueOf(b.getDigitoControl()));   
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_NUMEROCUENTA, String.valueOf(b.getNumeroCuenta()));   
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_TITULAR, String.valueOf(b.getTitular())); 
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_RESIDENTE, b.getResidente()?"1":"0");
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDPERSONA, String.valueOf(b.getIdPersona()));
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_FECHAALTA, String.valueOf(b.getFechaAlta()));
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDPERSONATEMP, String.valueOf(b.getIdPersonaTemp()));
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IDDIRECCIONTEMP, String.valueOf(b.getIdDireccionTemp()));
			UtilidadesHash.set(htData, CenSolicitudIncorporacionBean.C_IBAN, String.valueOf(b.getIban()));
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
}
