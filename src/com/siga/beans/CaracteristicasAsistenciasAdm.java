package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

public class CaracteristicasAsistenciasAdm extends MasterBeanAdministrador
{

	public CaracteristicasAsistenciasAdm(UsrBean usuario)
	{
	    super(CaracteristicasAsistenciasBean.T_NOMBRETABLA, usuario);
	}
	
	
	protected String[] getCamposBean() {
		String[] campos = { CaracteristicasAsistenciasBean.C_IDINSTITUCION, CaracteristicasAsistenciasBean.C_ANIO,
				CaracteristicasAsistenciasBean.C_FECHAMODIFICACION, 	CaracteristicasAsistenciasBean.C_NUMERO,
				CaracteristicasAsistenciasBean.C_IDINSTITUCION_JUZGADO,	CaracteristicasAsistenciasBean.C_IDORIGENCONTACTO,
				CaracteristicasAsistenciasBean.C_DESCRIPCIONCONTACTO,CaracteristicasAsistenciasBean.C_DESCRIPCIONJUZGADO,
				CaracteristicasAsistenciasBean.C_OTRODESCRIPCIONORIGENCONTACTO, CaracteristicasAsistenciasBean.C_USUMODIFICACION,
				CaracteristicasAsistenciasBean.C_JUDICIAL,CaracteristicasAsistenciasBean.C_DESCRIPCIONPRETENSION,
				CaracteristicasAsistenciasBean.C_VIOLENCIADOMESTICA,CaracteristicasAsistenciasBean.C_VIOLENCIAGENERO,
				CaracteristicasAsistenciasBean.C_CONTRALIBERTADSEXUAL,
				CaracteristicasAsistenciasBean.C_CIVIL,CaracteristicasAsistenciasBean.C_PENAL,
				CaracteristicasAsistenciasBean.C_INTERPOSICIONDENUNCIA,CaracteristicasAsistenciasBean.C_SOLICITUDMEDIDASCAUTELARES,
				CaracteristicasAsistenciasBean.C_ASISTENCIADECLARACION,CaracteristicasAsistenciasBean.C_MEDIDASPROVISIONALES,
				CaracteristicasAsistenciasBean.C_ORDENPROTECCION,CaracteristicasAsistenciasBean.C_OTRAS,
				CaracteristicasAsistenciasBean.C_OTRASDESCRIPCION,CaracteristicasAsistenciasBean.C_ASESORAMIENTO,
				CaracteristicasAsistenciasBean.C_DERIVARACTUACIONESJUDICIALES,
				CaracteristicasAsistenciasBean.C_INTERPOSICIONMINISTFISCAL,CaracteristicasAsistenciasBean.C_INTERVENCIONMEDICOFORENSE,
				CaracteristicasAsistenciasBean.C_DERECHOSJUSTICIAGRATUITA,CaracteristicasAsistenciasBean.C_OBLIGADADESALOJODOMICILIO,
				CaracteristicasAsistenciasBean.C_ENTREVISTALETRADODEMANDANTE,CaracteristicasAsistenciasBean.C_LETRADODESIGNADOCONTIACTUJUDI,
				CaracteristicasAsistenciasBean.C_CIVILESPENALES,CaracteristicasAsistenciasBean.C_VICTIMALETRADOANTERIORIDAD,
				CaracteristicasAsistenciasBean.C_IDPERSONA,CaracteristicasAsistenciasBean.C_NUMEROPROCEDIMIENTO,
				CaracteristicasAsistenciasBean.C_IDJUZGADO,CaracteristicasAsistenciasBean.C_NIG,
				CaracteristicasAsistenciasBean.C_IDPRETENSION};
		return campos;
	}

	
	protected String[] getClavesBean() {
		String[] campos = {	CaracteristicasAsistenciasBean.C_IDINSTITUCION, CaracteristicasAsistenciasBean.C_ANIO,CaracteristicasAsistenciasBean.C_NUMERO};
		return campos;
	}

	
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CaracteristicasAsistenciasBean bean = null;
		try {
			bean = new CaracteristicasAsistenciasBean();
			
			bean.setAnio(UtilidadesHash.getInteger(hash, CaracteristicasAsistenciasBean.C_ANIO));
			bean.setAsistenciaDeclaracion(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_ASISTENCIADECLARACION));
			bean.setAsesoramiento(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_ASESORAMIENTO));
			bean.setCivil(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_CIVIL));
			bean.setCivilesPenales(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_CIVILESPENALES));
			bean.setContraLibertadSexual(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_CONTRALIBERTADSEXUAL));			
			bean.setDescripcionContacto(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_DESCRIPCIONCONTACTO));
			bean.setDescripcionJuzgado(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_DESCRIPCIONJUZGADO));
			bean.setDescripcionPretension(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_DESCRIPCIONPRETENSION));			
			bean.setDerechosJusticiaGratuita(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_DERECHOSJUSTICIAGRATUITA));
			bean.setDerivaActuacionesJudiciales(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_DERIVARACTUACIONESJUDICIALES));
			bean.setEntrevistaLetradoDemandante(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_ENTREVISTALETRADODEMANDANTE));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CaracteristicasAsistenciasBean.C_IDINSTITUCION));
			bean.setIdInstitucionJuzgado(UtilidadesHash.getInteger(hash, CaracteristicasAsistenciasBean.C_IDINSTITUCION_JUZGADO));
			bean.setIdJuzgado(UtilidadesHash.getLong(hash, CaracteristicasAsistenciasBean.C_IDJUZGADO));
			bean.setIdOrigenContacto(UtilidadesHash.getInteger(hash, CaracteristicasAsistenciasBean.C_IDORIGENCONTACTO));
			bean.setIdPersona(UtilidadesHash.getLong(hash, CaracteristicasAsistenciasBean.C_IDPERSONA));
			bean.setIdPretension(UtilidadesHash.getInteger(hash, CaracteristicasAsistenciasBean.C_IDPRETENSION));
			bean.setInterposicionDenuncia(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_INTERPOSICIONDENUNCIA));
			bean.setInterposicionMinistFiscal(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_INTERPOSICIONMINISTFISCAL));
			bean.setIntervencionMedicoForense(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_INTERVENCIONMEDICOFORENSE));
			bean.setJudicial(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_JUDICIAL));
			bean.setLetradoDesignadoContiActuJudi(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_LETRADODESIGNADOCONTIACTUJUDI));
			bean.setViolenciaDomestica(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_VIOLENCIADOMESTICA));
			bean.setViolenciaGenero(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_VIOLENCIAGENERO));
			bean.setMedidasProvisionales(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_MEDIDASPROVISIONALES));
			bean.setNig(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_NIG));
			bean.setNumero(UtilidadesHash.getDouble(hash, CaracteristicasAsistenciasBean.C_NUMERO));
			bean.setNumeroProcedimiento(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_NUMEROPROCEDIMIENTO));
			bean.setObligadaDesalojoDomicilio(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_OBLIGADADESALOJODOMICILIO));
			bean.setOrdenProteccion(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_ORDENPROTECCION));
			bean.setOtras(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_OTRAS));
			bean.setOtrasDescripcion(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_OTRASDESCRIPCION));
			bean.setOtroDescripcionOrigenContacto(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_OTRODESCRIPCIONORIGENCONTACTO));
			bean.setPenal(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_PENAL));
			bean.setSolicitudMedidasCautelares(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_SOLICITUDMEDIDASCAUTELARES));
			bean.setVictimaLetradoAnterioridad(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_VICTIMALETRADOANTERIORIDAD));
			
			bean.setFechaMod(UtilidadesHash.getString(hash, CaracteristicasAsistenciasBean.C_FECHAMODIFICACION));						
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CaracteristicasAsistenciasBean.C_USUMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}
	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try
		{
			hash = new Hashtable();
			CaracteristicasAsistenciasBean b = (CaracteristicasAsistenciasBean) bean;
			
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_ANIO,	b.getAnio());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_ASISTENCIADECLARACION, b.getAsistenciaDeclaracion());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_ASESORAMIENTO, b.getAsesoramiento());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_CIVIL, b.getCivil());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_CIVILESPENALES, b.getCivilesPenales());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_CONTRALIBERTADSEXUAL, b.getContraLibertadSexual());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_JUDICIAL, b.getJudicial());			
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_DESCRIPCIONCONTACTO, b.getDescripcionContacto());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_DESCRIPCIONJUZGADO, b.getDescripcionJuzgado());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_DESCRIPCIONPRETENSION, b.getDescripcionPretension());			
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_DERECHOSJUSTICIAGRATUITA, b.getDerechosJusticiaGratuita());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_DERIVARACTUACIONESJUDICIALES, b.getDerivaActuacionesJudiciales());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_ENTREVISTALETRADODEMANDANTE, b.getEntrevistaLetradoDemandante());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_IDINSTITUCION_JUZGADO, b.getIdInstitucionJuzgado());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_IDJUZGADO, b.getIdJuzgado());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_IDORIGENCONTACTO, b.getIdOrigenContacto());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_IDPRETENSION, b.getIdPretension());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_INTERPOSICIONDENUNCIA, b.getInterposicionDenuncia());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_INTERPOSICIONMINISTFISCAL, b.getInterposicionMinistFiscal());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_INTERVENCIONMEDICOFORENSE, b.getIntervencionMedicoForense());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_LETRADODESIGNADOCONTIACTUJUDI, b.getLetradoDesignadoContiActuJudi());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_VIOLENCIADOMESTICA,b.getViolenciaDomestica());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_VIOLENCIAGENERO,b.getViolenciaGenero());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_MEDIDASPROVISIONALES, b.getMedidasProvisionales());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_NIG, b.getNig());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_NUMERO, b.getNumero());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_NUMEROPROCEDIMIENTO, b.getNumeroProcedimiento());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_OBLIGADADESALOJODOMICILIO, b.getObligadaDesalojoDomicilio());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_ORDENPROTECCION, b.getOrdenProteccion());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_OTRAS, b.getOtras());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_OTRASDESCRIPCION, b.getOtrasDescripcion());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_OTRODESCRIPCIONORIGENCONTACTO, b.getOtroDescripcionOrigenContacto());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_PENAL, b.getPenal());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_SOLICITUDMEDIDASCAUTELARES, b.getSolicitudMedidasCautelares());
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_VICTIMALETRADOANTERIORIDAD, b.getVictimaLetradoAnterioridad());
															
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_FECHAMODIFICACION, b.getFechaMod());			
			UtilidadesHash.set(hash, CaracteristicasAsistenciasBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}		
}

