package com.siga.gratuita.service.impl;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.mapper.GenParametrosMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.ScsEjgMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.ScsSojMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.ScsUnidadfamiliarejgMapper;
import org.redabogacia.sigaservices.app.autogen.model.GenParametros;
import org.redabogacia.sigaservices.app.autogen.model.GenParametrosKey;
import org.redabogacia.sigaservices.app.autogen.model.ScsEjg;
import org.redabogacia.sigaservices.app.autogen.model.ScsSoj;
import org.redabogacia.sigaservices.app.autogen.model.ScsUnidadfamiliarejg;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.helper.DocuShareHelper;

import com.siga.gratuita.service.ScsEjgService;
import com.xerox.docushare.DSException;

import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.MyBatisBusinessServiceTemplate;

public class ScsEjgServiceImpl extends MyBatisBusinessServiceTemplate implements ScsEjgService{

	public ScsEjgServiceImpl(BusinessManager businessManager) {
		super(businessManager);
		// TODO Auto-generated constructor stub
	}

	public Object executeService(Object... arg0) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
	public void insert(String idInstitucion, String fechaApertura, String idTipoEjgcolegio,String idTurnoTramita,
			String idGuardiaTramita,String idPersonaTramita,String observaciones,String delitos,ScsSoj scsSoj,Integer usuario) throws BusinessException {
		ScsEjg scsEjg = insert(idInstitucion, fechaApertura, idTipoEjgcolegio, idTurnoTramita, idGuardiaTramita, idPersonaTramita,scsSoj.getDescripcionconsulta(),
				scsSoj.getRespuestaletrado(),usuario)	;	
		scsSoj = update(scsSoj,scsEjg,usuario);
		if(scsSoj.getIdpersonajg()!=null){
			insert(scsEjg,scsSoj.getIdpersonajg(),usuario);
		}
		//aNTIGUAMENTE EN EPOCAS DE ATOS EXISTIA UNA FUNCIONALIDAD QUE COPIABA LA DOCUEMNTACION DEL SOJ AL EJG PERO SE DEPRECO. pOR LO TAMTO, SIRVA
		//ESTE COMENTARIO COMO RECUERDO.
		
	}
	
	public ScsSoj update(ScsSoj scsSoj,ScsEjg scsEjg,Integer usuario){
		ScsSojMapper scsSojMapper = getMyBatisSqlSessionManager().getMapper(ScsSojMapper.class);
		scsSoj.setEjganio(scsEjg.getAnio());
		scsSoj.setEjgnumero(scsEjg.getNumero());
		scsSoj.setEjgidtipoejg(scsEjg.getIdtipoejg());
		scsSoj.setUsumodificacion(usuario);
		scsSoj.setFechamodificacion(new Date());
		scsSojMapper.updateByPrimaryKeySelective(scsSoj);
		return scsSoj;
	}
	
	public ScsUnidadfamiliarejg insert(ScsEjg scsEjg,Long idPersona,Integer usuario) throws BusinessException {
		ScsUnidadfamiliarejgMapper scsUnidadfamiliarejgMapper = getMyBatisSqlSessionManager().getMapper(ScsUnidadfamiliarejgMapper.class);
		//insertar unidad familiar
		ScsUnidadfamiliarejg scsUnidadfamiliarejg = new ScsUnidadfamiliarejg();
		scsUnidadfamiliarejg.setIdinstitucion(scsEjg.getIdinstitucion());
		scsUnidadfamiliarejg.setAnio(scsEjg.getAnio());
		scsUnidadfamiliarejg.setNumero(scsEjg.getNumero());
		scsUnidadfamiliarejg.setIdtipoejg(scsEjg.getIdtipoejg());
		scsUnidadfamiliarejg.setIdpersona(idPersona);
		scsUnidadfamiliarejg.setEncalidadde("SOLICITANTE");
		scsUnidadfamiliarejg.setSolicitante(new Short("1"));
		scsUnidadfamiliarejg.setUsumodificacion(usuario);
		scsUnidadfamiliarejg.setFechamodificacion(new Date());
		
		scsUnidadfamiliarejgMapper.insert(scsUnidadfamiliarejg);
		return scsUnidadfamiliarejg;
	}
	public ScsEjg insert(String idInstitucion,String fechaApertura, String idTipoEjgcolegio,String idTurno,
			String idGuardia,String idPersona,String observaciones,String delitos,Integer usuCreacion) throws BusinessException {
		ScsEjgMapper scsEjgMapper = getMyBatisSqlSessionManager().getMapper(ScsEjgMapper.class);

		ScsEjg scsEjg = new ScsEjg();
		
		scsEjg.setIdinstitucion(Short.valueOf(idInstitucion));
		String anio = fechaApertura.substring(6);
		scsEjg.setAnio(Short.valueOf(anio));

		if(idTurno!=null){
			scsEjg.setGuardiaturnoIdturno(Integer.valueOf(idTurno));	
		}
		if(idGuardia!=null){
			scsEjg.setGuardiaturnoIdguardia(Integer.valueOf(idGuardia));	
		}
		if(fechaApertura!=null){
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			try {
				scsEjg.setFechaapertura(formato.parse(fechaApertura));
			} catch (ParseException e) {
				throw new BusinessException("Error en formato fecha");
			}
		}
		if(idPersona!=null){
			scsEjg.setIdpersona(Long.valueOf(idPersona));
		}
		if(idTipoEjgcolegio!=null){
			scsEjg.setIdtipoejgcolegio(Short.valueOf(idTipoEjgcolegio));

		}
		scsEjg.setCalidad("0");
		scsEjg.setCalidadidinstitucion(scsEjg.getIdinstitucion());
		scsEjg.setIdtipoencalidad(Short.valueOf("0"));
	//Se entra cuando creamos desde ejg y soj
		scsEjg.setUsucreacion(usuCreacion);
		scsEjg.setFechacreacion(new Date());

		//Ahiora hay que crear el identificador de Docushare
//		Hay que llamar a un metodo que nos genere el docushare
		scsEjgMapper.insert(scsEjg);
		String collectionTitle = DocuShareHelper.getTitleEJG(anio, scsEjg.getNumejg());
		
		/* Sólo se intentará la Conexion al DocuShare si el parámetro general para la institucion=1*/
		GenParametrosMapper genParametrosMapper = getMyBatisSqlSessionManager().getMapper(GenParametrosMapper.class);
		GenParametrosKey key = new GenParametrosKey();
		key.setIdinstitucion(scsEjg.getIdinstitucion());
		key.setModulo(AppConstants.MODULO.GEN.toString());
		key.setParametro(AppConstants.PARAMETRO.REGTEL.toString());
		GenParametros genParametros = genParametrosMapper.selectByPrimaryKey(key);
		
		String valor = genParametros.getValor();
		if (valor!=null && valor.equals("1")){
			DocuShareHelper docuShareHelper = new DocuShareHelper(scsEjg.getIdinstitucion().intValue());
			key.setParametro(AppConstants.PARAMETRO.DOCUSHARE_HOST.toString());
			genParametros = genParametrosMapper.selectByPrimaryKey(key);

			String host = genParametros.getValor();
			key.setParametro(AppConstants.PARAMETRO.DOCUSHARE_PORT.toString());
			genParametros = genParametrosMapper.selectByPrimaryKey(key);
			String port = genParametros.getValor();
			key.setParametro(AppConstants.PARAMETRO.DOCUSHARE_DOMAIN.toString());
			genParametros = genParametrosMapper.selectByPrimaryKey(key);
			String domain = genParametros.getValor();
			key.setParametro(AppConstants.PARAMETRO.DOCUSHARE_USER.toString());
			genParametros = genParametrosMapper.selectByPrimaryKey(key);
			String user = genParametros.getValor();
			key.setParametro(AppConstants.PARAMETRO.DOCUSHARE_PASSWORD.toString());
			genParametros = genParametrosMapper.selectByPrimaryKey(key);
			String password = genParametros.getValor();
			
			key.setParametro(AppConstants.PARAMETRO.ID_DOCUSHARE.toString());
			genParametros = genParametrosMapper.selectByPrimaryKey(key);
			String idExpedientes = genParametros.getValor();
			
			String identificadorDS;
			try {
				identificadorDS = docuShareHelper.createCollectionEJG(collectionTitle,host,  port, domain,  user,
						 password,idExpedientes);
				scsEjg.setIdentificadords(identificadorDS);
			}catch (DSException e) {
				throw new BusinessException("Error en docushare");				
			}
			scsEjgMapper.updateByPrimaryKeySelective(scsEjg);
		}
		return scsEjg;
		
		
	}
	
	
	

}
