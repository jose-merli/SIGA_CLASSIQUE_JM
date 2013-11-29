
package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.censo.form.SolicitudIncorporacionForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ActuacionAsistenciaForm;
/**
 * 
 * @author jorgeta
 *
 */
public class CenSolicitudMutualidadAdm extends MasterBeanAdministrador {
	
	public CenSolicitudMutualidadAdm (UsrBean usu) {
		super(CenSolicitudMutualidadBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenSolicitudMutualidadBean.C_IDSOLICITUD,CenSolicitudMutualidadBean.C_IDSOLICITUDINCORPORACION,CenSolicitudMutualidadBean.C_FECHASOLICITUD,
				CenSolicitudMutualidadBean.C_IDTRATAMIENTO,CenSolicitudMutualidadBean.C_NOMBRE,CenSolicitudMutualidadBean.C_APELLIDO1,
				CenSolicitudMutualidadBean.C_APELLIDO2,CenSolicitudMutualidadBean.C_NUMEROIDENTIFICADOR,CenSolicitudMutualidadBean.C_DOMICILIO,
				CenSolicitudMutualidadBean.C_CODIGOPOSTAL,CenSolicitudMutualidadBean.C_TELEFONO1,CenSolicitudMutualidadBean.C_CORREOELECTRONICO,
				CenSolicitudMutualidadBean.C_IDINSTITUCION,CenSolicitudMutualidadBean.C_IDESTADO,CenSolicitudMutualidadBean.C_IDTIPOSOLICITUD,
				CenSolicitudMutualidadBean.C_FECHAMODIFICACION,CenSolicitudMutualidadBean.C_USUMODIFICACION,CenSolicitudMutualidadBean.C_FECHANACIMIENTO,
				CenSolicitudMutualidadBean.C_IDTIPOIDENTIFICACION,CenSolicitudMutualidadBean.C_IDPROVINCIA,CenSolicitudMutualidadBean.C_IDPOBLACION,
				CenSolicitudMutualidadBean.C_FECHAESTADO,CenSolicitudMutualidadBean.C_TELEFONO2,CenSolicitudMutualidadBean.C_MOVIL,
				CenSolicitudMutualidadBean.C_FAX1,CenSolicitudMutualidadBean.C_FAX2,CenSolicitudMutualidadBean.C_IDESTADOCIVIL,
				CenSolicitudMutualidadBean.C_IDPAIS,CenSolicitudMutualidadBean.C_NATURALDE,CenSolicitudMutualidadBean.C_IDSEXO,
				CenSolicitudMutualidadBean.C_POBLACIONEXTRANJERA,CenSolicitudMutualidadBean.C_TITULAR,CenSolicitudMutualidadBean.C_CODIGOSUCURSAL,
				CenSolicitudMutualidadBean.C_CBO_CODIGO,CenSolicitudMutualidadBean.C_DIGITOCONTROL,CenSolicitudMutualidadBean.C_NUMEROCUENTA,
				CenSolicitudMutualidadBean.C_IBAN,CenSolicitudMutualidadBean.C_IDPERIODICIDADPAGO,CenSolicitudMutualidadBean.C_IDCOBERTURA,
				CenSolicitudMutualidadBean.C_IDBENEFICIARIO,CenSolicitudMutualidadBean.C_OTROSBENEFICIARIOS,CenSolicitudMutualidadBean.C_IDASISTENCIASANITARIA,
				CenSolicitudMutualidadBean.C_FECHANACIMIENTOCONYUGE,CenSolicitudMutualidadBean.C_NUMEROHIJOS,CenSolicitudMutualidadBean.C_EDADHIJO1,
				CenSolicitudMutualidadBean.C_EDADHIJO2,CenSolicitudMutualidadBean.C_EDADHIJO3,CenSolicitudMutualidadBean.C_EDADHIJO4,
				CenSolicitudMutualidadBean.C_EDADHIJO5,CenSolicitudMutualidadBean.C_EDADHIJO6,CenSolicitudMutualidadBean.C_EDADHIJO7,
				CenSolicitudMutualidadBean.C_EDADHIJO8,CenSolicitudMutualidadBean.C_EDADHIJO9,CenSolicitudMutualidadBean.C_EDADHIJO10,
				
				CenSolicitudMutualidadBean.C_SWIFT,CenSolicitudMutualidadBean.C_PERIODICIDADPAGO,CenSolicitudMutualidadBean.C_COBERTURA,
				CenSolicitudMutualidadBean.C_BENEFICIARIO,CenSolicitudMutualidadBean.C_ASISTENCIASANITARIA,CenSolicitudMutualidadBean.C_CUOTACOBERTURA,
				CenSolicitudMutualidadBean.C_CAPITALCOBERTURA,CenSolicitudMutualidadBean.C_IDSOLICITUDACEPTADA,
				CenSolicitudMutualidadBean.C_PAIS,CenSolicitudMutualidadBean.C_PROVINCIA,CenSolicitudMutualidadBean.C_POBLACION,
				CenSolicitudMutualidadBean.C_ESTADO,CenSolicitudMutualidadBean.C_ESTADOMUTUALISTA
		};
		
		
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenSolicitudMutualidadBean.C_IDSOLICITUD};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenSolicitudMutualidadBean bean = null;
		
		try {
			bean = new CenSolicitudMutualidadBean();
			
			bean.setApellido1(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_APELLIDO1));
			bean.setApellido2(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_APELLIDO2));
			bean.setCodigoPostal(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_CODIGOPOSTAL));
			bean.setCorreoElectronico(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_CORREOELECTRONICO));
			bean.setDomicilio(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_DOMICILIO));
			bean.setFax1(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_FAX1));
			bean.setFax2(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_FAX2));
			bean.setFechaEstado(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_FECHAESTADO));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_FECHAMODIFICACION));
			bean.setFechaNacimiento(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_FECHANACIMIENTO));
			bean.setFechaSolicitud(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_FECHASOLICITUD));
			bean.setIdEstado(UtilidadesHash.getInteger(hash,CenSolicitudMutualidadBean.C_IDESTADO));
			bean.setIdEstadoCivil(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_IDESTADOCIVIL));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenSolicitudMutualidadBean.C_IDINSTITUCION));
			bean.setIdPais(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_IDPAIS));
			bean.setIdPoblacion(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_IDPOBLACION));
			bean.setPoblacionExtranjera(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_POBLACIONEXTRANJERA));
			bean.setIdProvincia(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_IDPROVINCIA));
			bean.setIdSolicitud(UtilidadesHash.getLong(hash,CenSolicitudMutualidadBean.C_IDSOLICITUD));
			bean.setIdSolicitudIncorporacion(UtilidadesHash.getLong(hash,CenSolicitudMutualidadBean.C_IDSOLICITUDINCORPORACION));
			bean.setIdTipoIdentificacion(UtilidadesHash.getInteger(hash,CenSolicitudMutualidadBean.C_IDTIPOIDENTIFICACION));
			bean.setIdTipoSolicitud(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_IDTIPOSOLICITUD));
			bean.setIdTratamiento(UtilidadesHash.getInteger(hash,CenSolicitudMutualidadBean.C_IDTRATAMIENTO));
			bean.setMovil(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_MOVIL));
			bean.setNaturalDe(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_NATURALDE));
			bean.setNombre(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_NOMBRE));
			bean.setNumeroIdentificacion(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_NUMEROIDENTIFICADOR));
			bean.setTelef1(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_TELEFONO1));
			bean.setTelef2(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_TELEFONO2));
			bean.setIdSexo(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_IDSEXO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenSolicitudMutualidadBean.C_USUMODIFICACION));
			bean.setCboCodigo(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_CBO_CODIGO));
			bean.setCodigoSucursal(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_CODIGOSUCURSAL));
			bean.setDigitoControl(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_DIGITOCONTROL));
			bean.setNumeroCuenta(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_NUMEROCUENTA));
			bean.setTitular(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_TITULAR));
			bean.setIban(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_IBAN));
			bean.setIdPeriodicidadPago(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_IDPERIODICIDADPAGO));
			bean.setIdCobertura(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_IDCOBERTURA));
			bean.setIdBeneficiario(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_IDBENEFICIARIO));
			bean.setOtrosBeneficiarios(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_OTROSBENEFICIARIOS));
			bean.setIdAsistenciaSanitaria(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_IDASISTENCIASANITARIA));
			bean.setFechaNacimientoConyuge(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_FECHANACIMIENTOCONYUGE));
			bean.setNumeroHijos(UtilidadesHash.getInteger(hash,CenSolicitudMutualidadBean.C_NUMEROHIJOS));
			bean.setEdadHijo1(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_EDADHIJO1));
			bean.setEdadHijo2(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_EDADHIJO2));
			bean.setEdadHijo3(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_EDADHIJO3));
			bean.setEdadHijo4(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_EDADHIJO4));
			bean.setEdadHijo5(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_EDADHIJO5));
			bean.setEdadHijo6(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_EDADHIJO6));
			bean.setEdadHijo7(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_EDADHIJO7));
			bean.setEdadHijo8(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_EDADHIJO8));
			bean.setEdadHijo9(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_EDADHIJO9));
			bean.setEdadHijo10(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_EDADHIJO10));
			
			bean.setSwift(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_SWIFT));
			bean.setPeriodicidadPago(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_PERIODICIDADPAGO));
			bean.setCobertura(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_COBERTURA));
			bean.setBeneficiario(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_BENEFICIARIO));
			bean.setAsistenciaSanitaria(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_ASISTENCIASANITARIA));
			bean.setCuotaCobertura(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_CUOTACOBERTURA));
			bean.setCapitalCobertura(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_CAPITALCOBERTURA));
			bean.setIdSolicitudAceptada(UtilidadesHash.getLong(hash,CenSolicitudMutualidadBean.C_IDSOLICITUDACEPTADA));
			
			bean.setPais(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_PAIS));
			bean.setProvincia(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_PROVINCIA));
			bean.setPoblacion(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_POBLACION));
			
			bean.setEstado(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_ESTADO));
			bean.setEstadoMutualista(UtilidadesHash.getString(hash,CenSolicitudMutualidadBean.C_ESTADOMUTUALISTA));
			
			
			
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
			CenSolicitudMutualidadBean b = (CenSolicitudMutualidadBean) bean;
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_APELLIDO1, b.getApellido1());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_APELLIDO2, b.getApellido2());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_CODIGOPOSTAL, b.getCodigoPostal());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_CORREOELECTRONICO, b.getCorreoElectronico());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_DOMICILIO, b.getDomicilio());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_FAX1, b.getFax1());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_FAX2, b.getFax2());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_FECHAESTADO, b.getFechaEstado());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_FECHANACIMIENTO, b.getFechaNacimiento());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_FECHASOLICITUD, b.getFechaSolicitud());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDESTADO, b.getIdEstado());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDESTADOCIVIL, b.getIdEstadoCivil());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDPAIS, b.getIdPais());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDPOBLACION, b.getIdPoblacion());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_POBLACIONEXTRANJERA, b.getPoblacionExtranjera());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDPROVINCIA, b.getIdProvincia());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDSOLICITUD,b.getIdSolicitud());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDSOLICITUDINCORPORACION,b.getIdSolicitudIncorporacion());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDTIPOIDENTIFICACION, b.getIdTipoIdentificacion());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDTIPOSOLICITUD, b.getIdTipoSolicitud());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDTRATAMIENTO, b.getIdTratamiento());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_MOVIL, b.getMovil());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_NATURALDE, b.getNaturalDe());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_NUMEROIDENTIFICADOR, b.getNumeroIdentificacion());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_TELEFONO1, b.getTelef1());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_TELEFONO2, b.getTelef2());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDSEXO, b.getIdSexo());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IBAN, b.getIban());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_CBO_CODIGO, b.getCboCodigo());   
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_CODIGOSUCURSAL, b.getCodigoSucursal());   
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_DIGITOCONTROL, b.getDigitoControl());   
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_NUMEROCUENTA, b.getNumeroCuenta());   
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_TITULAR, b.getTitular());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDPERIODICIDADPAGO, b.getIdPeriodicidadPago());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDCOBERTURA, b.getIdCobertura());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDBENEFICIARIO, b.getIdBeneficiario());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_OTROSBENEFICIARIOS, b.getOtrosBeneficiarios());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDASISTENCIASANITARIA, b.getIdAsistenciaSanitaria());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_FECHANACIMIENTOCONYUGE, b.getFechaNacimientoConyuge());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_NUMEROHIJOS, b.getNumeroHijos());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_EDADHIJO1, b.getEdadHijo1());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_EDADHIJO2, b.getEdadHijo2());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_EDADHIJO3, b.getEdadHijo3());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_EDADHIJO4, b.getEdadHijo4());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_EDADHIJO5, b.getEdadHijo5());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_EDADHIJO6, b.getEdadHijo6());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_EDADHIJO7, b.getEdadHijo7());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_EDADHIJO8, b.getEdadHijo8());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_EDADHIJO9, b.getEdadHijo9());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_EDADHIJO10, b.getEdadHijo10());
			
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_SWIFT, b.getSwift());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_PERIODICIDADPAGO, b.getPeriodicidadPago());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_COBERTURA, b.getCobertura());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_BENEFICIARIO, b.getBeneficiario());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_ASISTENCIASANITARIA, b.getAsistenciaSanitaria());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_CUOTACOBERTURA, b.getCuotaCobertura());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_CAPITALCOBERTURA, b.getCapitalCobertura());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_IDSOLICITUDACEPTADA, b.getIdSolicitudAceptada());
			
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_PAIS, b.getPais());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_PROVINCIA, b.getProvincia());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_POBLACION, b.getPoblacion());
			
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_ESTADO, b.getEstado());
			UtilidadesHash.set(htData,CenSolicitudMutualidadBean.C_ESTADOMUTUALISTA, b.getEstadoMutualista());

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
	public Long getNewIdSolicitud() throws ClsExceptions{
        Long idSolicitud = getSecuenciaNextVal(CenSolicitudMutualidadBean.SEQ_CEN_SOLICITUDMUTUALIDAD);
        return idSolicitud;
    }
	public  List<CenSolicitudMutualidadBean> getSolicitudesMutualidad(CenSolicitudIncorporacionBean solicitudIncorporacionBean) throws ClsExceptions{
		StringBuffer sql =  new StringBuffer();
		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		sql.append("SELECT SOL.IDSOLICITUD,SOL.IDESTADO,SOL.ESTADO,SOL.IDTIPOSOLICITUD,SOL.IDSOLICITUDACEPTADA,SOL.ESTADOMUTUALISTA ");
		sql.append("FROM CEN_SOLICITUDMUTUALIDAD SOL ");
		sql.append("WHERE SOL.IDSOLICITUDINCORPORACION = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),solicitudIncorporacionBean.getIdSolicitud());
		sql.append(" OR SOL.IDSOLICITUDINCORPORACION = (SELECT IDSOLICITUD FROM CEN_SOLICITUDINCORPORACION WHERE IDPERSONA=:");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),solicitudIncorporacionBean.getIdSolicitud());
		sql.append(" AND IDINSTITUCION=:");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),solicitudIncorporacionBean.getIdInstitucion());
		sql.append(") ORDER by SOL.FECHASOLICITUD ASC");

		List<CenSolicitudMutualidadBean> solicitudMutualidadBeans = null;
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	solicitudMutualidadBeans = new ArrayList<CenSolicitudMutualidadBean>();
            	CenSolicitudMutualidadBean solicitudMutualidadBean = null;
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		solicitudMutualidadBean =  (CenSolicitudMutualidadBean)this.hashTableToBean(htFila);
            		solicitudMutualidadBeans.add(solicitudMutualidadBean);
            	}
            }else{
            	solicitudMutualidadBeans = new ArrayList<CenSolicitudMutualidadBean>();
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		
	
	
		return solicitudMutualidadBeans;
	
	}
	public  CenSolicitudMutualidadBean getSolicitudMutualidad(String idSolicitud) throws ClsExceptions{
		
		
		
		Hashtable pkHashtable = new Hashtable();
		pkHashtable.put(CenSolicitudIncorporacionBean.C_IDSOLICITUD, idSolicitud);
		Vector<CenSolicitudMutualidadBean> solicitudMutualidadBeans =selectByPK(pkHashtable);
		CenSolicitudMutualidadBean solicitudMutualidadBean= (CenSolicitudMutualidadBean)solicitudMutualidadBeans.get(0);
		return solicitudMutualidadBean;
		
		
	
	}
	
	public  CenSolicitudMutualidadBean getSolicitudMutualidad(String idPersona,String idTipoSolicitudMutualidad) throws ClsExceptions, SIGAException{
		StringBuffer sql =  new StringBuffer();
		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		sql.append("SELECT SOL.* ");
		sql.append(" FROM CEN_SOLICITUDMUTUALIDAD SOL ");
		sql.append(" WHERE ( SOL.IDSOLICITUDINCORPORACION = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idPersona);
		sql.append(" OR SOL.IDSOLICITUDINCORPORACION = (SELECT IDSOLICITUD FROM CEN_SOLICITUDINCORPORACION WHERE IDPERSONA=:");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idPersona);
		sql.append(")) AND SOL.IDTIPOSOLICITUD = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),idTipoSolicitudMutualidad);
		sql.append(" ORDER BY IDSOLICITUD DESC");
		CenSolicitudMutualidadBean solicitudMutualidadBean = null;
		try {
			RowsContainer rc = new RowsContainer(); 
            if (rc.findBind(sql.toString(),htCodigos)) {
				if(rc.size()>0) {
					Row fila = (Row) rc.get(0);
            		Hashtable<String, Object> htFila=fila.getRow();
            		solicitudMutualidadBean =  (CenSolicitudMutualidadBean)this.hashTableToBean(htFila);
				}
            }
       }catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		
	
	
		return solicitudMutualidadBean;
	
	}
	public void actualizaSolicitudAceptada(CenSolicitudMutualidadBean solicitudMutualidadBean) throws ClsExceptions{
		String[] campos = {CenSolicitudMutualidadBean.C_IDSOLICITUDACEPTADA,CenSolicitudMutualidadBean.C_IDESTADO,CenSolicitudMutualidadBean.C_ESTADO,CenSolicitudMutualidadBean.C_FECHAMODIFICACION,CenSolicitudMutualidadBean.C_USUMODIFICACION};
		this.updateDirect(solicitudMutualidadBean,this.getClavesBean(),campos);
	}
	public void actualizaEstadoMutualista(CenSolicitudMutualidadBean solicitudMutualidadBean) throws ClsExceptions{
		String[] campos = {CenSolicitudMutualidadBean.C_ESTADOMUTUALISTA,CenSolicitudMutualidadBean.C_FECHAMODIFICACION,CenSolicitudMutualidadBean.C_USUMODIFICACION};
		this.updateDirect(solicitudMutualidadBean,this.getClavesBean(),campos);
	}
}
