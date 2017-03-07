/*
 * VERSIONES:
 * 
 * miguel.villegas - 13-01-2005 - Funciones de accesos a BBDDs y relacionadas con los doatos de estado colegiales
 *	
 */
package com.siga.beans;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Vector;

import javax.transaction.UserTransaction;

import org.acabogacia.www.aca2.ws.certrev.CertificateReviewResponse;
import org.acabogacia.www.aca2.ws.certrev.ErrorsType;
import org.acabogacia.www.aca2.ws.certrev.WarningsType;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;
import com.siga.ws.aca.AcaWSClient;

import es.satec.businessManager.BusinessManager;

/**
*
* Clase que gestiona la tabla CenHistorico de la BBDD
* 
*/
// RGG cambio visibilidad public class CenDatosColegialesEstadoAdm extends MasterBeanAdministrador {
public class CenDatosColegialesEstadoAdm extends MasterBeanAdmVisible {

	/**  
	 *  Constructor
	 * @param  usu - Usuario  
	 */	
	public CenDatosColegialesEstadoAdm(UsrBean usu) {
		super(CenDatosColegialesEstadoBean.T_NOMBRETABLA, usu); 
	}

	/**
	 * @param tabla
	 * @param usuario
	 * @param userbean
	 * @param idInsitucionClientes
	 * @param idPersonaCliente
	 */
	public CenDatosColegialesEstadoAdm(Integer usuario, UsrBean usrbean,int idInstitucionCliente, long idPersonaCliente) {
		super( CenDatosColegialesEstadoBean.T_NOMBRETABLA, usuario, usrbean, idInstitucionCliente,idPersonaCliente);
	}	
	
	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenDatosColegialesEstadoBean.C_IDPERSONA,
							CenDatosColegialesEstadoBean.C_IDINSTITUCION,
							CenDatosColegialesEstadoBean.C_FECHAESTADO,
							CenDatosColegialesEstadoBean.C_OBSERVACIONES,
							CenDatosColegialesEstadoBean.C_IDESTADO,
							CenDatosColegialesEstadoBean.C_FECHAMODIFICACION,
							CenDatosColegialesEstadoBean.C_USUMODIFICACION};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {CenDatosColegialesEstadoBean.C_IDPERSONA,
							CenDatosColegialesEstadoBean.C_IDINSTITUCION,
							CenDatosColegialesEstadoBean.C_FECHAESTADO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}	

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenDatosColegialesEstadoBean bean = null;
		
		try {
			bean = new CenDatosColegialesEstadoBean();
			bean.setIdPersona (UtilidadesHash.getLong(hash,CenDatosColegialesEstadoBean.C_IDPERSONA));			
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash,CenDatosColegialesEstadoBean.C_IDINSTITUCION));
			bean.setFechaEstado (UtilidadesHash.getString(hash,CenDatosColegialesEstadoBean.C_FECHAESTADO));
			bean.setObservaciones (UtilidadesHash.getString(hash,CenDatosColegialesEstadoBean.C_OBSERVACIONES));
			bean.setIdEstado (UtilidadesHash.getInteger(hash,CenDatosColegialesEstadoBean.C_IDESTADO));			
			bean.setFechaMod(UtilidadesHash.getString(hash,CenDatosColegialesEstadoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenDatosColegialesEstadoBean.C_USUMODIFICACION));			
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
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenDatosColegialesEstadoBean b = (CenDatosColegialesEstadoBean) bean;
			UtilidadesHash.set(htData,CenDatosColegialesEstadoBean.C_IDPERSONA,b.getIdPersona ());
			UtilidadesHash.set(htData,CenDatosColegialesEstadoBean.C_IDINSTITUCION,b.getIdInstitucion ()); 
			UtilidadesHash.set(htData,CenDatosColegialesEstadoBean.C_FECHAESTADO, b.getFechaEstado());
			UtilidadesHash.set(htData,CenDatosColegialesEstadoBean.C_OBSERVACIONES,b.getObservaciones());
			UtilidadesHash.set(htData,CenDatosColegialesEstadoBean.C_IDESTADO,b.getIdEstado());
			UtilidadesHash.set(htData,CenDatosColegialesEstadoBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(htData,CenDatosColegialesEstadoBean.C_USUMODIFICACION,b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
			
	/** 
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/>
	 * @param  idPers - identificador de la persona 
	 * @param  idInst - identificador de la institucion
	 * @param  idHistor - identificador del historico	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public MasterBean obtenerEntradaEstadoColegial (String idPers, String idInst, String fecha) throws ClsExceptions, SIGAException {
		
		CenDatosColegialesEstadoBean datos=new CenDatosColegialesEstadoBean();
		String sql="";
		
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            sql ="SELECT " +
	            	 CenDatosColegialesEstadoBean.T_NOMBRETABLA + "." + CenDatosColegialesEstadoBean.C_IDPERSONA  + "," +
					 CenDatosColegialesEstadoBean.T_NOMBRETABLA + "." + CenDatosColegialesEstadoBean.C_IDINSTITUCION + "," +
	            	 CenDatosColegialesEstadoBean.T_NOMBRETABLA + "." + CenDatosColegialesEstadoBean.C_FECHAESTADO  + "," +
					 CenDatosColegialesEstadoBean.T_NOMBRETABLA + "." + CenDatosColegialesEstadoBean.C_OBSERVACIONES  + "," +
					 CenDatosColegialesEstadoBean.T_NOMBRETABLA + "." + CenDatosColegialesEstadoBean.C_IDESTADO +
					 " FROM " + CenDatosColegialesEstadoBean.T_NOMBRETABLA + 
					 " WHERE " +
					 CenDatosColegialesEstadoBean.T_NOMBRETABLA +"."+ CenDatosColegialesEstadoBean.C_IDPERSONA + "=" + idPers +
					 " AND " +
					 CenDatosColegialesEstadoBean.T_NOMBRETABLA +"."+ CenDatosColegialesEstadoBean.C_IDINSTITUCION + "=" + idInst +
					 " AND " +
	            	 CenDatosColegialesEstadoBean.T_NOMBRETABLA +"."+ CenDatosColegialesEstadoBean.C_FECHAESTADO +  " = TO_DATE('" + fecha + "', '" + ClsConstants.DATE_FORMAT_SQL + "')";
	            //sql = sql + GstDate.dateBetween0and24h(CenDatosColegialesEstadoBean.C_FECHAESTADO,fecha);	            																				

	            // RGG cambio visibilidad
	            rc = this.find(sql);
	            if (rc!=null) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.setIdPersona(Long.valueOf(fila.getString(CenDatosColegialesEstadoBean.C_IDPERSONA)));
	                  datos.setIdInstitucion(Integer.valueOf(fila.getString(CenDatosColegialesEstadoBean.C_IDINSTITUCION)));
	                  datos.setFechaEstado(fila.getString(CenDatosColegialesEstadoBean.C_FECHAESTADO));
	                  datos.setObservaciones(fila.getString(CenDatosColegialesEstadoBean.C_OBSERVACIONES));
	                  datos.setIdEstado(Integer.valueOf(fila.getString(CenDatosColegialesEstadoBean.C_IDESTADO)));	                  
	               }
	            }
	       }
	      

	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre los datos de estado colegiales.");
	       }
	       return datos;                        
	    }

	/**
	 * Modifica el estado colegial y rellena la tabla de historicos (CEN_HISTORICO)	 
	 * @param  modificacion - datos modificados 
	 * @param  original - datos originales
	 * @param  entHistorico - entrada historico
	 * @return  Boolean - Resultado de la operacion  
	 * @exception  ClsExceptions  En cualquier caso de error 
	 */
	
	public boolean modificacionConHistorico (Hashtable modificacion, Hashtable original, boolean bDesdeCGAE) throws ClsExceptions, SIGAException 
	{
		CenHistoricoAdm adminHist=new CenHistoricoAdm(this.usrbean);			
		boolean resultado=false;
		
		// Cargo una nueva tabla hash para insertar en la tabla de historico
		Hashtable hashHist = new Hashtable();			
		// Cargo los valores obtenidos del formulario referentes al historico			
		hashHist.put(CenHistoricoBean.C_IDPERSONA, modificacion.get(CenDatosColegialesEstadoBean.C_IDPERSONA));
		hashHist.put(CenHistoricoBean.C_IDINSTITUCION, (bDesdeCGAE ? Integer.toString(ClsConstants.INSTITUCION_CGAE) : modificacion.get(CenDatosColegialesEstadoBean.C_IDINSTITUCION)));
		hashHist.put(CenHistoricoBean.C_MOTIVO, modificacion.get(CenHistoricoBean.C_MOTIVO));
		// Especifico el tipo de cambio del historico
		int tipohistorico=0;
		switch (Integer.valueOf(modificacion.get(CenDatosColegialesEstadoBean.C_IDESTADO).toString())) {
		case ClsConstants.ESTADO_COLEGIAL_BAJACOLEGIAL:
			tipohistorico = ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_BAJA_COLEGIAL;
			break;
		case ClsConstants.ESTADO_COLEGIAL_EJERCIENTE:
			tipohistorico = ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_ALTA_EJERCICIO;
			break;
		case ClsConstants.ESTADO_COLEGIAL_SINEJERCER:
			tipohistorico = ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_BAJA_EJERCICIO;
			break;
		case ClsConstants.ESTADO_COLEGIAL_INHABILITACION:
			tipohistorico = ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_INHABILITACION;
			break;
		case ClsConstants.ESTADO_COLEGIAL_SUSPENSION:
			tipohistorico = ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_SUSPENSION;
			break;
		}
		if (tipohistorico > 0) {
			hashHist.put(CenHistoricoBean.C_IDTIPOCAMBIO, new Integer(tipohistorico).toString());
		}
		
		// Asigno el IDHISTORICO			
		hashHist.put(CenHistoricoBean.C_IDHISTORICO, adminHist.getNuevoID(hashHist).toString());			
		
		try {		
			String fechaNewBD = UtilidadesFecha.getFechaApruebaDeFormato((String) modificacion.get(CenDatosColegialesEstadoBean.C_FECHAESTADO));
			modificacion.put(CenDatosColegialesEstadoBean.C_FECHAESTADO, fechaNewBD);
			
			//ELimino campos no usados:
			modificacion.remove("MODO");
			modificacion.remove("NCOLEGIADO");
			
			CenDatosColegialesEstadoBean beanDatos = (CenDatosColegialesEstadoBean) this.hashTableToBean(modificacion);
			beanDatos.setOriginalHash(original);
			
			//1.-Borro el registro 
			this.delete(original);
			
			//2.-Creo el  registro
			if (this.insert(beanDatos)) {
			
				// Obtengo del formulario datos que me interesaran para la insercion
				hashHist.put(CenHistoricoBean.C_FECHAEFECTIVA, fechaNewBD);
				// entHistorico.put(CenHistoricoBean.C_FECHAENTRADA, fechaNewBD);
				
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
				if (admHis.insertCompleto(hashHist, beanDatos, CenHistoricoAdm.ACCION_UPDATE, this.getLenguaje())) {
					resultado=true;
				}	
			}		 
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al actualizar datos en B.D.");
		}
		return resultado;
	}	
	
	/**
	 * Inserta un nuevo estado colegial y rellena la tabla de historicos (CEN_HISTORICO)	 
	 *
	 * @param estadoColegialHashtable
	 * @param bDesdeCGAE
	 * @param idioma
	 * @return
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	public int insertaEstadoColegial(Hashtable<String, String> estadoColegialHashtable, boolean bDesdeCGAE, String idioma) throws SIGAException, ClsExceptions
	{
		boolean hayQueNotificarAca = false;
		int resultado = 0;

		try {
			String idPersona = estadoColegialHashtable.get(CenDatosColegialesEstadoBean.C_IDPERSONA);
			String idInstitucion = estadoColegialHashtable.get(CenDatosColegialesEstadoBean.C_IDINSTITUCION);
			String fechaEstado = estadoColegialHashtable.get(CenDatosColegialesEstadoBean.C_FECHAESTADO);
			String idEstadocolegial = estadoColegialHashtable.get(CenDatosColegialesEstadoBean.C_IDESTADO);
			int idEstadocolegial_int = Integer.parseInt(idEstadocolegial);

			// Compruebo que existan datos colegiales asociados a esos identificadores
			CenColegiadoAdm admCol = new CenColegiadoAdm(this.usrbean);
			if (admCol.getDatosColegiales(new Long(idPersona), new Integer(idInstitucion)) == null) {
				throw new SIGAException("messages.censo.estadosColegiales.errorNoEsColegiado", new String[] { "modulo.censo" });
			}

			// construyendo el registro de auditoria
			CenHistoricoBean beanHis = new CenHistoricoBean();
			switch (idEstadocolegial_int) {
			case ClsConstants.ESTADO_COLEGIAL_EJERCIENTE:
				beanHis.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_ALTA_EJERCICIO));
				hayQueNotificarAca = false;
				break;
			case ClsConstants.ESTADO_COLEGIAL_BAJACOLEGIAL:
				beanHis.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_BAJA_COLEGIAL));
				hayQueNotificarAca = true;
				break;
			case ClsConstants.ESTADO_COLEGIAL_INHABILITACION:
				beanHis.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_INHABILITACION));
				hayQueNotificarAca = true;
				break;
			case ClsConstants.ESTADO_COLEGIAL_SUSPENSION:
				beanHis.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_SUSPENSION));
				hayQueNotificarAca = true;
				break;
			case ClsConstants.ESTADO_COLEGIAL_SINEJERCER:
				beanHis.setIdTipoCambio(new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_ESTADO_BAJA_EJERCICIO));
				hayQueNotificarAca = true;
				break;
			}
			beanHis.setMotivo(estadoColegialHashtable.get(CenDatosColegialesEstadoBean.C_OBSERVACIONES));
			// si el estado se inserta desde el Consejo, el registro de auditoria debe ir en el Consejo
			if (bDesdeCGAE) {
				beanHis.setIdInstitucion(ClsConstants.INSTITUCION_CGAE);
			}

			CenDatosColegialesEstadoBean beanDatos = (CenDatosColegialesEstadoBean) this.hashTableToBean(estadoColegialHashtable);
			if (! this.insert(beanDatos)) {
				throw new ClsExceptions("Error inesperado al insertar el estado colegial");
			}
			
			// insertando en la cola de modificacion de datos para Consejos
			CenDireccionesBean beanDir = new CenDireccionesBean ();
			beanDir.setIdPersona (Long.valueOf(idPersona));
			beanDir.setIdInstitucion (Integer.valueOf(idInstitucion));
			this.insertarModificacionConsejo(beanDir,this.usrbean, ClsConstants.COLA_CAMBIO_LETRADO_MODIFICACION_DIRECCION);
			
			this.revisionesPorCambioEstadoColegial(idInstitucion, idPersona, idEstadocolegial,fechaEstado, this.usrbean); // OJO: se pasa la fecha sin hora
			
			
			// llamando al servicio de notificacion ACA
			if (hayQueNotificarAca){
				String llamadaReport;
				try {
					llamadaReport = llamadaWebServiceAcaRevisionLetrado(Long.valueOf(idPersona), Short.valueOf(idInstitucion));	
				} catch (BusinessException e) {
					resultado = 2;
					llamadaReport = e.getMessage();
				}
				
				if (llamadaReport != null && !llamadaReport.equalsIgnoreCase("")) {
					estadoColegialHashtable.put("RESPUESTA_ACA", llamadaReport);
					beanHis.setObservaciones(llamadaReport);
				}
			}
			
			
			CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
			if (! admHis.insertCompleto(beanHis, beanDatos, CenHistoricoAdm.ACCION_INSERT, idioma)) {
				throw new ClsExceptions("Error inesperado al insertar el registro de auditoria por nuevo estado colegial");
			}
			
			resultado = 1;
			
		} catch (SIGAException ee) {
			resultado = 0;
			throw new SIGAException("messages.censo.estadosColegiales.errorNoEsColegiado", ee, new String[] { "modulo.censo" });
		} catch (ClsExceptions e) {
			resultado = 0;
			throw new ClsExceptions("messages.general.error");
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
		
		return resultado;
	}
	
	public void insertarModificacionConsejo(CenDireccionesBean beanDir, UsrBean usr, int accionCola) throws SIGAException{
		CenColaCambioLetradoAdm colaAdm = new CenColaCambioLetradoAdm (usr);
		if (!colaAdm.insertarCambioEnCola (accionCola, beanDir.getIdInstitucion (), beanDir.getIdPersona (), beanDir.getIdDireccion ()))
			throw new SIGAException (colaAdm.getError ());
	}
	
	/**
	 * Este metodo hace una llamada al web service de revision de estado colegial
	 * @param idTipoIdentificacion
	 * @param numIdentificacion
	 * @param idInstitucion
	 * @return
	 * @throws BusinessException
	 */
	public String llamadaWebServiceAcaRevisionLetrado(Long idPersona, Short idInstitucion) throws BusinessException{
		
		// comprobando si esta activa la llamada GENERAL al servicio Web llamadaWebServiceAcaRevisionLetrado
		GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
		String acaActivo = genParametrosService.getValorParametroWithNull((short)0,PARAMETRO.WS_ACA_ACTIVO,MODULO.CEN);
		if (acaActivo != null && acaActivo.equalsIgnoreCase(AppConstants.DB_TRUE)) {
			// ademas hay que comprobar que este activa la llamada PARA ESTA INSTITUCION
			acaActivo = genParametrosService.getValorParametroWithNull(idInstitucion,PARAMETRO.WS_ACA_ACTIVO,MODULO.CEN);
		}
		if (acaActivo == null || acaActivo.equalsIgnoreCase(AppConstants.DB_FALSE)) {
			// si no esta activa la comunicacion con ACA, no hacemos nada
			return null;
		}
		
		StringBuilder llamadaWSAca = new StringBuilder("");
		boolean isErrorLlamadaAca = false;
		try {
			CenPersonaAdm cenPersonaAdm = new CenPersonaAdm(this.usrbean);
			Hashtable<String, String> hashPersona = new Hashtable<String, String>();
			hashPersona.put(CenPersonaBean.C_IDPERSONA, idPersona.toString());
			Vector personaVector = cenPersonaAdm.selectByPK(hashPersona);
			CenPersonaBean cenPersonaBean = (CenPersonaBean) personaVector.get(0);
			
			short idTipoIdentificacion = (short)cenPersonaBean.getIdTipoIdentificacion().shortValue();
			String numIdentificacion = cenPersonaBean.getNIFCIF();

			ClsLogging.writeFileLog("Inicio llamadaWebServiceAcaRevisionLetrado "+ numIdentificacion+ " "+idTipoIdentificacion + " "+idInstitucion, 3); 
			
			//Informamos al Servicio Web de ACA de que al haber una baja  colegial debe revisar los certificados del letrado.
			AcaWSClient acaWSClient = new AcaWSClient();
			if(AcaWSClient.PersonalIdTypes.getEnum(idTipoIdentificacion)==null)
				throw new BusinessException(UtilidadesString.getMensajeIdioma(usrbean, "messages.bajacolegial.identNoValidaSolicitudRevisionAca"));
			CertificateReviewResponse certificateReviewResponse = acaWSClient.getCertificateReviewResponse(AcaWSClient.PersonalIdTypes.getEnum(idTipoIdentificacion).getIdType(), numIdentificacion,idInstitucion,this.usrbean);
			if(certificateReviewResponse.getSuccess()==null){
				ErrorsType errorsType = certificateReviewResponse.getErrors();
				String messagesErrorNotificaionAca = UtilidadesString.getMensajeIdioma(usrbean, "messages.bajacolegial.errorNotificacionAca");
				llamadaWSAca.append(messagesErrorNotificaionAca);
				llamadaWSAca.append(". ");
				llamadaWSAca.append(errorsType.getError().getText());
				ClsLogging.writeFileLog(messagesErrorNotificaionAca+" "+errorsType.getError().getCode()+" "+errorsType.getError().getText(),3);
				isErrorLlamadaAca = true;
				
				
			}else{
//				SuccessType successType =  certificateReviewResponse.getSuccess();
				llamadaWSAca.append(UtilidadesString.getMensajeIdioma(usrbean, "messages.bajacolegial.successSolicitudRevisionAca"));
				WarningsType warningsType = certificateReviewResponse.getWarnings();
				if(warningsType!=null && warningsType.getWarning()!=null){
					String messagesAvisorNotificaionAca = UtilidadesString.getMensajeIdioma(usrbean, "messages.bajacolegial.avisoSolicitudRevisionAca");
					llamadaWSAca.append(messagesAvisorNotificaionAca);
					llamadaWSAca.append(". ");
					llamadaWSAca.append(warningsType.getWarning().getText());
					ClsLogging.writeFileLog(messagesAvisorNotificaionAca+" "+warningsType.getWarning().getCode()+" "+warningsType.getWarning().getText(),3);
				}
			}
			
			ClsLogging.writeFileLog("Fin llamadaWebServiceAcaRevisionLetrado "+ llamadaWSAca.toString(), 3);
			
		}catch (BusinessException e) {
			ClsLogging.writeFileLog("Fin llamadaWebServiceAcaRevisionLetrado.  Se ha producido un error controlado en la llamada el Servicio web de ACA AcaWSClient.getCertificateReviewResponse "+e.getMessage(),7);
			isErrorLlamadaAca = true;
			llamadaWSAca.append(UtilidadesString.getMensajeIdioma(usrbean, "messages.bajacolegial.errorNotificacionAca"));
			llamadaWSAca.append(". ");
			llamadaWSAca.append(e.getMessage());
			
		} catch (Exception e) {
			
			StringBuilder errorControlado = new StringBuilder(UtilidadesString.getMensajeIdioma(usrbean, "messages.bajacolegial.errorNotificacionAca"));
			errorControlado.append(". ");
			errorControlado.append(UtilidadesString.getMensajeIdioma(usrbean,"messages.general.error.conexion"));
			BusinessException businessException = new BusinessException(errorControlado.toString());
			ClsLogging.writeFileLogError("Fin llamadaWebServiceAcaRevisionLetrado.  Se ha producido un error no controlado en la llamada el Servicio web de ACA AcaWSClient.getCertificateReviewResponse",businessException,7); 
			throw businessException;
		}
		
		if(isErrorLlamadaAca)
			throw new BusinessException(llamadaWSAca.toString());
		
		
		return llamadaWSAca.toString();
		
	}
	
	
	/**
	 * Borra un estado colegial y rellena la tabla de historicos (CEN_HISTORICO)	 
	 * @param  entrada - estado a borrar 
	 * @param  entHistorico - entrada historico
	 * @return  Boolean - Resultado de la operacion  
	 * @exception  ClsExceptions  En cualquier caso de error 
	 */
	
	public boolean borrarConHistorico (Hashtable entrada, boolean bDesdeCGAE) throws ClsExceptions, SIGAException 
	{
		CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
		// generando datos para el historico
		Hashtable hashHist = new Hashtable();
		hashHist.put(CenHistoricoBean.C_IDINSTITUCION, (bDesdeCGAE ? Integer.toString(ClsConstants.INSTITUCION_CGAE) : entrada.get(CenDatosColegialesEstadoBean.C_IDINSTITUCION)));
		hashHist.put(CenHistoricoBean.C_IDPERSONA, entrada.get(CenDatosColegialesEstadoBean.C_IDPERSONA));
		hashHist.put(CenHistoricoBean.C_MOTIVO, ClsConstants.HISTORICO_REGISTRO_ELIMINADO);
		hashHist.put(CenHistoricoBean.C_IDTIPOCAMBIO, new Integer(ClsConstants.TIPO_CAMBIO_HISTORICO_DATOS_COLEGIALES).toString());
		hashHist.put(CenHistoricoBean.C_IDHISTORICO, admHis.getNuevoID(hashHist).toString());
		
		try {
			CenDatosColegialesEstadoBean beanDatos = (CenDatosColegialesEstadoBean) this.selectByPK(entrada).get(0);
			if (this.delete(entrada)) {
				if (admHis.insertCompleto(hashHist, beanDatos, CenHistoricoAdm.ACCION_DELETE, this.usrbean.getLanguage())) {
					return true;
				}	
			}					
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
		return false;
	}	
	
	public Vector getDatosColegialesPersonaInstitucion(String idInstitucion, String idPersona) throws ClsExceptions
	{
		Vector salida = new Vector();
		RowsContainer rows = new RowsContainer();
		Hashtable codigos=new Hashtable();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from " + CenDatosColegialesEstadoBean.T_NOMBRETABLA);
		sql.append(" where " + CenDatosColegialesEstadoBean.C_IDINSTITUCION + " = " + idInstitucion);
		sql.append(" and " + CenDatosColegialesEstadoBean.C_IDPERSONA + " = " + idPersona);	
		sql.append(" ORDER BY fechaestado ");
		try{
			
            RowsContainer rc = this.findBind(sql.toString(),codigos);
			if (rc!=null) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						salida.add(registro);
				}
			}


		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al buscar los certificado de una persona en una institución. ");
		}

		return salida;

	}	
	
	/**
	 * Borra un estado colegial. En este método se hacen las validaciones
	 * que se hacían en el action para así unificar los borrados del estado colegial
	 * desde DatosColegiacionAction y DatosColegialesAction	 
	 * @param  entrada - idinstitucion, idpersona, fechaEstado, usr
	 * @param  entHistorico - entrada historico
	 * @return  Boolean - Resultado de la operacion  
	 * @exception  ClsExceptions  En cualquier caso de error 
	 */
	
	public String eliminarEstadoColegiado(String idinstitucion,String idpersona,String fechaEstado) throws ClsExceptions, SIGAException{
	
		String message="";
		UserTransaction tx = null;
		UsrBean usr = this.usrbean;
		try{
			
			CenClienteAdm admCliente = new CenClienteAdm(usr);
			CenColegiadoAdm admColegiado = new CenColegiadoAdm(usr);
			CenDatosColegialesEstadoAdm admEstados = new CenDatosColegialesEstadoAdm(usr);
			CenHistoricoAdm admHistorico = new CenHistoricoAdm(usr);
			
			boolean bDesdeCGAE = false;
			if (Integer.parseInt(usr.getLocation()) == 2000){
				bDesdeCGAE = true;
			}
			
			// a. comprobando si tiene cosas pendientes de SJCS (Guardias y Designas)
			int pendienteSJCS = admCliente.tieneTrabajosSJCSPendientes(new Long(idpersona), new Integer(idinstitucion),null,null);
			
			if (pendienteSJCS == 3)
				throw new SIGAException(admCliente.getError());
			
			if(bDesdeCGAE){
				if (pendienteSJCS == 1){
					message="error.message.guardiasEstadoColegial";
					return message;
				}else if (pendienteSJCS == 2){
					message="error.message.designasEstadoColegial";
					return message;
				}
			}
			
			// obteniendo datos del estado
			Hashtable hash = new Hashtable();
			hash.put(CenDatosColegialesEstadoBean.C_IDPERSONA, idpersona);
			hash.put(CenDatosColegialesEstadoBean.C_FECHAESTADO, fechaEstado);
			hash.put(CenDatosColegialesEstadoBean.C_IDINSTITUCION, idinstitucion);
			
				
			
			//b. Si el estado que se está borrando no es Ejerciente/No Ejerciente 
			//	 Se comprueba que no existan inscripciones en turnos/guardias que tengan fecha de baja=fecha del estado que se está borrando
			CenDatosColegialesEstadoBean beanDatos = (CenDatosColegialesEstadoBean) this.selectByPK(hash).get(0);
			Integer idEstadoB=beanDatos.getIdEstado();
						
			if(idEstadoB!=ClsConstants.ESTADO_COLEGIAL_EJERCIENTE){
				
				ScsInscripcionTurnoAdm admInscTurno = new ScsInscripcionTurnoAdm(usr);
				ScsInscripcionGuardiaAdm admInscGuardia = new ScsInscripcionGuardiaAdm(usr);
			
				if((admInscTurno.getInscripcionesTurnoBajaAFecha(idinstitucion,idpersona,fechaEstado)>0)||(admInscGuardia.getInscripcionesGuardiaBajaAFecha(idinstitucion,idpersona,fechaEstado)>0))
				{
					message = "messages.censo.estadosColegiales.error.inscripciones.baja.fecha";
					return message;
				}	
			}
			
			// iniciando transaccion
			tx = usr.getTransaction();
			tx.begin();

			// 1 y 2. borrando estado e insertando historico
			if (admEstados.borrarConHistorico(hash, bDesdeCGAE)) {
				// obteniendo ultimo estado colegial
				Vector<Row> vEstados = admColegiado.getEstadosColegiales(new Long(idpersona), new Integer(idinstitucion), usr.getLanguage());
				if (vEstados != null && vEstados.size() > 0) {
					Row ultimoEstado = vEstados.get(0);
					String estado = ultimoEstado.getString(CenDatosColegialesEstadoBean.C_IDESTADO);
					String fechaEstadoU = ultimoEstado.getString(CenDatosColegialesEstadoBean.C_FECHAESTADO);
					
					// ejecutando revisiones
					revisionesPorCambioEstadoColegial(idinstitucion, idpersona, estado, fechaEstadoU, usr);
				}

				// terminando transaccion
				tx.commit();

				// informando de fin correcto y de cosas SJCS pendientes
				if (pendienteSJCS == 1 || pendienteSJCS == 2){
					message = (UtilidadesString.getMensajeIdioma(usr, "messages.deleted.success"));
					message += "\r\n"
							+ UtilidadesString.getMensajeIdioma(usr,
									"messages.censo.estadosColegiales.avisoTareasPendientes");
				}else{
					message="messages.deleted.success";
				}
				
			} else {
				if (tx!=null) {
					tx.rollback();
				}
				throw new SIGAException(admEstados.getError());
			}
			
		
		} catch (Exception e) {
			if (tx!=null) {
				try {
					tx.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
			}
			throw new ClsExceptions(e,"Error al eliminar el estado del colegiado. ");
		}
		
		return message;
	
	}
	
	/**
	 *  Realiza revisiones sobre el colegiado a partir del estado pasado como parametro:
	 *  1. Revisar anticipos
	 *  2. Revisar suscripciones a servicios
	 *  3. Dar de baja en las colas de guardia y turno si el nuevo estado no es ejerciente
	 *  4. Revocar certificados si el nuevo estado es de baja
	 *  
	 *  Hay una transaccion que se realiza por encima [this.borrar(), this.insertar()]
	 *  
	 * @param idinstitucion
	 * @param idpersona
	 * @param estado
	 * @param fechaEstado
	 * @param usr
	 * @throws ClsExceptions
	 * @throws ParseException
	 * @throws SIGAException
	 */
	public void revisionesPorCambioEstadoColegial(String idinstitucion, String idpersona, String estado, String fechaEstado, UsrBean usr) throws ClsExceptions, ParseException, SIGAException {
		// Controles generales
		CenPersonaAdm admPersona = new CenPersonaAdm(usr);
		AdmCertificadosAdm admCertif = new AdmCertificadosAdm(usr);
		String usuario = usr.getUserName();

		// 1. revisando anticipos
		String resultado1[] = EjecucionPLs.ejecutarPL_RevisionAnticiposLetrado(idinstitucion, idpersona, usuario);
		if ((resultado1 == null) || (!resultado1[0].equals("0")))
			throw new ClsExceptions("Error al ejecutar el PL PROC_SIGA_ACT_ANTICIPOSCLIENTE ");

		// 2. revisando suscripciones a servicios
		try {
			fechaEstado=UtilidadesString.formatoFecha(fechaEstado, ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH);
		} catch (Exception e1) {
			// La fecha esta bien formada como dia/mes/ano
		}
		
		String resultado[] = EjecucionPLs.ejecutarPL_RevisionSuscripcionesLetrado(idinstitucion, idpersona, fechaEstado, usuario);
		if ((resultado == null) || (!resultado[0].equals("0")))
			throw new ClsExceptions("Error al ejecutar el PL PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO"+resultado[1]);

		// 3. Dar de baja en las colas de guardia y turno si el nuevo estado es de baja
		if (new Integer(estado).intValue() != ClsConstants.ESTADO_COLEGIAL_EJERCIENTE) {			
						
			/* JPT: Me ha costado llegar hasta aqui.
	 		1. Expedientes > Tipos Expedientes: Crear un tipo de expediente con fase, clasificación, estados y permiso de acceso.
	 		2. Expedientes > Gestionar Expedientes: Introducir colegiado, estado y datos obligatorios.
	 		3. Cada vez que cambias el estado, sale una pantalla con una serie de checks:
	 		- Baja Turno de Oficio: Da de baja al colegiado del turno de oficio.
	 		- Baja Colegial: Cambia el estado del colegiado y da de baja las inscripciones de turno y guardia activas.
	 		- Baja en ejercicio: Cambia el estado del colegiado y da de baja las inscripciones de turno y guardia activas.
	 		- Inhabilitación perpetua: Cambia el estado del colegiado y da de baja las inscripciones de turno y guardia activas.
	 		- Suspensión ejercicio: Cambia el estado del colegiado y da de baja las inscripciones de turno y guardia activas.*/			
			ScsInscripcionTurnoAdm tAdm = new ScsInscripcionTurnoAdm(usr);
			String sMotivo = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.motivo.bajaEstadoColegial");
			tAdm.cancelarInscripcionesTurnosPersona(idpersona, idinstitucion, sMotivo, fechaEstado);
		}

		// 4. revocando certificados
		if (new Integer(estado).intValue() > ClsConstants.ESTADO_COLEGIAL_EJERCIENTE) {
			String nif = admPersona.obtenerNIF(idpersona);
			admCertif.revocarCertificados(new Integer(idinstitucion), nif);
		}
	} // revisionesPorCambioEstadoColegial()
}
