
package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.helper.DocuShareHelper;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsBeneficiarioSOJAdm;
import com.siga.beans.ScsBeneficiarioSOJBean;
import com.siga.beans.ScsContrariosAsistenciaAdm;
import com.siga.beans.ScsContrariosAsistenciaBean;
import com.siga.beans.ScsContrariosDesignaAdm;
import com.siga.beans.ScsContrariosDesignaBean;
import com.siga.beans.ScsContrariosEJGAdm;
import com.siga.beans.ScsContrariosEJGBean;
import com.siga.beans.ScsDefendidosDesignaAdm;
import com.siga.beans.ScsDefendidosDesignaBean;
import com.siga.beans.ScsDefinirSOJAdm;
import com.siga.beans.ScsDelitosAsistenciaAdm;
import com.siga.beans.ScsDelitosAsistenciaBean;
import com.siga.beans.ScsDelitosDesignaAdm;
import com.siga.beans.ScsDelitosDesignaBean;
import com.siga.beans.ScsDelitosEJGAdm;
import com.siga.beans.ScsDelitosEJGBean;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsDesignasProcuradorAdm;
import com.siga.beans.ScsDesignasProcuradorBean;
import com.siga.beans.ScsDocumentacionEJGAdm;
import com.siga.beans.ScsDocumentacionEJGBean;
import com.siga.beans.ScsDocumentacionSOJAdm;
import com.siga.beans.ScsDocumentacionSOJBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsEJGDESIGNAAdm;
import com.siga.beans.ScsEJGDESIGNABean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsSOJBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsUnidadFamiliarEJGAdm;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirCrearEJGForm;
import com.siga.gratuita.form.DefinirEJGForm;



/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_EJG
*/
public class DefinirCrearEJGAction extends MasterAction 
{
	final String[] clavesBusqueda={ScsEJGBean.C_IDINSTITUCION,ScsEJGBean.C_IDTIPOEJG,ScsEJGBean.C_ANIO
			,ScsEJGBean.C_NUMERO};
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response)throws SIGAException {
		
		String mapDestino = "exception";
		MasterForm miForm = null;
		
		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
					return mapping.findForward(mapDestino);
				}
				
				String accion = miForm.getModo();
				
//				 La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					DefinirEJGForm form = (DefinirEJGForm)miForm;
					form.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					form.reset(mapping,request);
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = abrir(mapping, miForm, request, response);						
				}else if (accion.equalsIgnoreCase("buscarInit")){
					miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = buscarPor(mapping, miForm, request, response); 
				} else {
					return super.executeInternal(mapping,
							      formulario,
							      request, 
							      response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				//mapDestino = "exception";
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }
			    
			    //throw new ClsExceptions("El ActionMapping no puede ser nulo");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
		}
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"}); 
		} 
		return mapping.findForward(mapDestino);
	}
	


	/**
	 * Rellena un hash con los valores recogidos del formulario y los inserta en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected  synchronized String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UserTransaction tx=null;
		
		try {
		
			Hashtable miHash = new Hashtable(); 
			Hashtable hashTemporal = new Hashtable();	
			HttpSession session =request.getSession();

			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			DefinirCrearEJGForm miForm = (DefinirCrearEJGForm) formulario;	
			
			miHash = miForm.getDatos();
			Hashtable miHashSOJ=new Hashtable(); 
			Hashtable miHashSOJDocu=new Hashtable(); 
			Hashtable miHashDocuEJG=new Hashtable();
			Hashtable hashAux=new Hashtable(); 
			
			// 1. Obtenemos el idPersona
			// Si hemos introducido manualmente el numero de colegiado, no sabremos su idPersona lo consultamos de BD
//			if ((miForm.getIdPersona()==null)||(miForm.getIdPersona().equals("")) ||(miForm.getIdPersona().equals("null")) ){
//				try{
//					CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
//					String idPersonaSel = colegiadoAdm.getIdPersona(miForm.getNColegiado(), this.getIDInstitucion(request).toString());
//					miHash.put(ScsDesignasLetradoBean.C_IDPERSONA,idPersonaSel);
//				}
//				catch(Exception e){
//					throwExcp("No existe colegiado con ese NColegiado.",e,null);
//				}
//			}
			ScsEJGAdm ejgAdm =  new ScsEJGAdm(this.getUserBean(request));
			

			tx = usr.getTransaction();		
			tx.begin();
			// Preparamos los datos para insertarlos (formato de fecha adecuado, 
			// y campos que se rellenan automáticamente y que vienen vacíos de la jsp
			miHash = ejgAdm.prepararInsert(miHash);
			
			//OBTENCION DEL NUMEJG DEL EJG
			
			/*String numEJG;
    		GestorContadores cont=new GestorContadores(this.getUserBean(request));
    		Hashtable contadorHash=cont.getContador(this.getIDInstitucion(request),ClsConstants.EJG);
    		numEJG=cont.getNuevoContador(contadorHash);
    		
    		cont.setContador(contadorHash,numEJG);
    		
    		miHash.put(ScsEJGBean.C_NUMEJG, numEJG);*/

						
			ScsSOJBean sojBean=null;
			ScsDefinirSOJAdm sojAdm = new ScsDefinirSOJAdm (this.getUserBean(request));
			
			
			if (miHash.get(ScsEJGBean.C_ANIO) != null && miHash.get(ScsEJGBean.C_NUMEJG) != null) {
				try {
					short idInstitucionShort = getIDInstitucion(request).shortValue();
					DocuShareHelper docuShareHelper = new DocuShareHelper(idInstitucionShort);
					String identificadorDS = docuShareHelper.getIdentificadorDocuShare(miHash.get(ScsEJGBean.C_ANIO).toString(),  miHash.get(ScsEJGBean.C_NUMEJG).toString());
					if (identificadorDS != null) {
						miHash.put(ScsEJGBean.C_IDENTIFICADORDS, identificadorDS);
					}	
				} catch (Exception e) {
					ClsLogging.writeFileLog("No se ha podico crear la carpeta de REGTEl del expediente "+miHash.get(ScsEJGBean.C_ANIO)+"/"+ miHash.get(ScsEJGBean.C_NUMEJG)+":"+e.toString(),10);
				}
				
			}
			

			// Desde SOJ o nuevo EJG
			if (((miForm.getDesignaAnio()    == null || miForm.getDesignaAnio().equals("")) &&
				 (miForm.getAsistenciaAnio() == null || miForm.getAsistenciaAnio().equals("")))) { 

								
/**/      if (miForm.getSOJIdTipoSOJ()!=null && !miForm.getSOJIdTipoSOJ().equalsIgnoreCase("")){//Sólo en el caso de un EJG dado de alta desde un SOJ
			  // Recuperamos los datos introducidos en la pestana Datos Generales del SOJ para insertar aquellos que nos convengan en EJG
	            request.getSession().removeAttribute("DATAPAGINADOR");
				miHashSOJ.put(ScsSOJBean.C_ANIO, miForm.getSOJAnio());
				miHashSOJ.put(ScsSOJBean.C_NUMERO, miForm.getSOJNumero());
				miHashSOJ.put(ScsSOJBean.C_IDTIPOSOJ,miForm.getSOJIdTipoSOJ());
				miHashSOJ.put(ScsSOJBean.C_IDINSTITUCION,miForm.getIdInstitucion());
				Vector vSOJ = sojAdm.selectByPK(miHashSOJ);
				
				if ((vSOJ != null) && (vSOJ.size() == 1)) {
					sojBean = (ScsSOJBean) vSOJ.get(0);
					miHash.put(ScsEJGBean.C_OBSERVACIONES,sojBean.getDescripcionConsulta());
					miHash.put(ScsEJGBean.C_DELITOS,sojBean.getRespuestaLetrado());
					
					
				}
				UtilidadesHash.set(miHashSOJ,ScsSOJBean.C_IDPERSONAJG,sojBean.getIdPersonaJG());
				if (miHash.get("IDPERSONA")==null ||((String)miHash.get("IDPERSONA")).equals("")){
				 hashAux=sojAdm.existeTramitadorSOJ((String)miForm.getIdInstitucion(),(String)miForm.getSOJNumero(),(String)miForm.getSOJAnio(),(String)miForm.getSOJIdTipoSOJ());
				 miHash.put(ScsEJGBean.C_IDPERSONA,hashAux.get("IDPERSONA"));
				}	
			  }
               /**INC_05960_SIGA, se introduce el campo idtipoencalidad a cero ya que cuando se crea por primera
               vez el campo calidad por defecto sera "Demandado"**/
				miHash.put(ScsEJGBean.C_CALIDAD,"0");
				miHash.put(ScsEJGBean.C_IDTIPOENCALIDAD,0);
				miHash.put(ScsEJGBean.C_CALIDADIDINSTITUCION,usr.getLocation());
				
				//Se entra cuando creamos desde ejg y soj
				miHash.put(ScsEJGBean.C_USUCREACION,new Integer (usr.getUserName()));
				miHash.put(ScsEJGBean.C_FECHACREACION,"sysdate");
				
				
				
				// 1. Insertamos el EJG
				if (!ejgAdm.insert(miHash)) {
					throw new ClsExceptions ("Error al crear el EJG desde la designa");
				}
				
/**/	  if (miForm.getSOJIdTipoSOJ()!=null && !miForm.getSOJIdTipoSOJ().equalsIgnoreCase("")){//Sólo en el caso de un EJG dado de alta desde un SOJ
			
				// creamos la relacion entre el SOJ y el EJG EN EL SOJ.
				miHashSOJ.put(ScsSOJBean.C_EJGIDTIPOEJG, (String)miHash.get(ScsEJGBean.C_IDTIPOEJG));
				miHashSOJ.put(ScsSOJBean.C_EJGANIO, (String)miHash.get(ScsEJGBean.C_ANIO));
				miHashSOJ.put(ScsSOJBean.C_EJGNUMERO, (String)miHash.get(ScsEJGBean.C_NUMERO));
				
				if (!sojAdm.updateDirect(miHashSOJ,new String[] {ScsSOJBean.C_IDINSTITUCION,ScsSOJBean.C_IDTIPOSOJ,ScsSOJBean.C_ANIO,ScsSOJBean.C_NUMERO},new String[] {ScsSOJBean.C_EJGIDTIPOEJG,ScsSOJBean.C_EJGANIO,ScsSOJBean.C_EJGNUMERO})) {
					throw new ClsExceptions("Error al actualizar la relacion en tre EJG y SOJ. "+sojAdm.getError());
				}
	
				if(sojBean.getIdPersonaJG()!=null){// Siempre que en el SOJ se haya dado de alta un beneficiario
			  	  copiarBeneficiarioSOJEnEJG(miHashSOJ,miHash,request);
			  	}
//				 Si el check copiar Documentacion está Activo copiamos la documentacion de SOJ a EJG
				boolean checkDocuSOJ=UtilidadesString.stringToBoolean(miForm.getChkDocumentacion());
				if (checkDocuSOJ){
					miHashSOJDocu.put(ScsDocumentacionSOJBean.C_ANIO, miForm.getSOJAnio());
					miHashSOJDocu.put(ScsDocumentacionSOJBean.C_NUMERO, miForm.getSOJNumero());
					miHashSOJDocu.put(ScsDocumentacionSOJBean.C_IDTIPOSOJ,miForm.getSOJIdTipoSOJ());
					miHashSOJDocu.put(ScsDocumentacionSOJBean.C_IDINSTITUCION,miForm.getIdInstitucion());
					ScsDocumentacionSOJAdm sojDocuAdm = new ScsDocumentacionSOJAdm (this.getUserBean(request));
					ScsDocumentacionEJGAdm ejgDocuAdm = new ScsDocumentacionEJGAdm (this.getUserBean(request));
					Vector vSOJDocu = sojDocuAdm.select(miHashSOJDocu);
					ScsDocumentacionSOJBean sojDocuBean=null;
						if (vSOJDocu != null) {
						 for (int i=0; i<vSOJDocu.size();i++){
						 	sojDocuBean = (ScsDocumentacionSOJBean) vSOJDocu.get(i);
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_ANIO,miHash.get(ScsEJGBean.C_ANIO));
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_NUMERO,miHash.get(ScsEJGBean.C_NUMERO));
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_IDTIPOEJG,miHash.get(ScsEJGBean.C_IDTIPOEJG));
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_IDINSTITUCION,miForm.getIdInstitucion());
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_FECHAENTREGA,sojDocuBean.getFechaEntrega());
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_FECHALIMITE,sojDocuBean.getFechaLimite());
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_DOCUMENTACION,sojDocuBean.getDocumentacion());
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_REGENTRADA,sojDocuBean.getRegEntrada());
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_REGSALIDA,sojDocuBean.getRegSalida());
							miHashDocuEJG = ejgDocuAdm.prepararInsert(miHashDocuEJG);
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_FECHAENTREGA,sojDocuBean.getFechaEntrega());
							miHashDocuEJG.put(ScsDocumentacionEJGBean.C_FECHALIMITE,sojDocuBean.getFechaLimite());
							if (!ejgDocuAdm.insert(miHashDocuEJG)) {
								throw new ClsExceptions ("Error al crear registro de documentacion en EJG desde un SOJ");
							}
						 }
						}
			  }
			 } 	
			}
			else {
				
				Integer EJG_anio		  = UtilidadesHash.getInteger(miHash, ScsEJGBean.C_ANIO);
				Integer EJG_idInstitucion = UtilidadesHash.getInteger(miHash, ScsEJGBean.C_IDINSTITUCION);
				Integer EJG_idTipoEJG 	  = UtilidadesHash.getInteger(miHash, ScsEJGBean.C_IDTIPOEJG);
				Integer EJG_numero		  = UtilidadesHash.getInteger(miHash, ScsEJGBean.C_NUMERO);
				

				
				//  Desde Designacion
				if (miForm.getDesignaAnio()!=null && !miForm.getDesignaAnio().equals("")){ 

					// 1. Obtenemos todos los datos de la designa
					ScsDesignaBean designaBean = null;
					Hashtable p = new Hashtable(); 
					UtilidadesHash.set(p, ScsDesignaBean.C_ANIO,    		miForm.getDesignaAnio());
					UtilidadesHash.set(p, ScsDesignaBean.C_IDINSTITUCION, 	this.getIDInstitucion(request));
					UtilidadesHash.set(p, ScsDesignaBean.C_IDTURNO,  		miForm.getDesignaIdTurno());
					UtilidadesHash.set(p, ScsDesignaBean.C_NUMERO,     		miForm.getDesignaNumero());
					ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
					Vector vD = designaAdm.select(p);
					if ((vD != null) && (vD.size() == 1)) {
						designaBean = (ScsDesignaBean) vD.get(0);
					}

					// 2. Insertamos el EJG a partir de la Designa 
					
					// jbd // esto ya no se saca de la designa, hay que ir a la tabla scs_designaprocurador 
					// UtilidadesHash.set(miHash, ScsEJGBean.C_IDPROCURADOR,    			designaBean.getIdProcurador());
					// UtilidadesHash.set(miHash, ScsEJGBean.C_IDINSTITUCIONPROCURADOR,    designaBean.getIdInstitucionProcurador());
					ScsDesignasProcuradorAdm admProcurador = new ScsDesignasProcuradorAdm(this.getUserBean(request));
					Vector vP = admProcurador.select(p); // Aprovechamos la hash anterior que ya tiene los datos de la designa
					ScsDesignasProcuradorBean beanProcurador;
					if ((vP != null) && (vP.size() > 0)) {
						for (int i = 0; i < vP.size(); i++) {
							beanProcurador = (ScsDesignasProcuradorBean) vP.get(i);
							if((beanProcurador.getFechaRenuncia()==null)||(beanProcurador.getFechaRenuncia().equalsIgnoreCase(""))){
								UtilidadesHash.set(miHash, ScsEJGBean.C_IDINSTITUCIONPROCURADOR,    beanProcurador.getIdInstitucionProc());
								UtilidadesHash.set(miHash, ScsEJGBean.C_IDPROCURADOR,    			beanProcurador.getIdProcurador());
								UtilidadesHash.set(miHash, ScsEJGBean.C_NUMERODESIGNAPROC,    		beanProcurador.getNumeroDesignacion());
								UtilidadesHash.set(miHash, ScsEJGBean.C_FECHADESIGPROC,    			beanProcurador.getFechaDesigna());
							}
						}
					}
					UtilidadesHash.set(miHash, ScsEJGBean.C_IDPERSONAJG,     			miForm.getIdPersonaJG());
					
					UtilidadesHash.set(miHash, ScsEJGBean.C_JUZGADO,     				designaBean.getIdJuzgado());
					UtilidadesHash.set(miHash, ScsEJGBean.C_JUZGADOIDINSTITUCION,     	designaBean.getIdInstitucionJuzgado());
					UtilidadesHash.set(miHash, ScsEJGBean.C_NUMEROPROCEDIMIENTO,     	designaBean.getNumProcedimiento());
					if(designaBean.getAnioProcedimiento() != null)
						UtilidadesHash.set(miHash, ScsEJGBean.C_ANIOPROCEDIMIENTO,     		designaBean.getAnioProcedimiento());
					UtilidadesHash.set(miHash, ScsEJGBean.C_NIG,     					designaBean.getNIG());
					UtilidadesHash.set(miHash, ScsEJGBean.C_OBSERVACIONES,     			designaBean.getResumenAsunto());
					UtilidadesHash.set(miHash, ScsEJGBean.C_IDPRETENSION,     			designaBean.getIdPretension());
					
															
					//obtengo la calidad del interesado (Del EJG) mediante la clave de designa y idPersonaJG en defendidos designa
					Hashtable bus = new Hashtable();
					UtilidadesHash.set(bus, ScsDefendidosDesignaBean.C_ANIO,    			designaBean.getAnio());
					UtilidadesHash.set(bus, ScsDefendidosDesignaBean.C_IDTURNO, 			designaBean.getIdTurno());
					UtilidadesHash.set(bus, ScsDefendidosDesignaBean.C_NUMERO,  			designaBean.getNumero());
					UtilidadesHash.set(bus, ScsDefendidosDesignaBean.C_IDINSTITUCION,  			designaBean.getIdInstitucion());
					UtilidadesHash.set(bus, ScsDefendidosDesignaBean.C_IDPERSONA,     			miForm.getIdPersonaJG());
					ScsDefendidosDesignaBean defBean = new ScsDefendidosDesignaBean ();
					ScsDefendidosDesignaAdm defAdm = new ScsDefendidosDesignaAdm (this.getUserBean(request));
					Vector vDe = defAdm.select(bus);
					if ((vDe != null) && (vDe.size() == 1)) {
						defBean = (ScsDefendidosDesignaBean) vDe.get(0);
					}
					  /**INC_05960_SIGA, se introduce el campo idtipoencalidad a cero ya que cuando se crea por primera
                      vez el campo calidad por defecto sera "Demandado"**/
					miHash.put(ScsEJGBean.C_CALIDAD,"0");
				    miHash.put(ScsEJGBean.C_IDTIPOENCALIDAD,0);					
				    miHash.put(ScsEJGBean.C_CALIDADIDINSTITUCION,usr.getLocation());
				    
				    //Se entra cuando creamos desde designacion
				    miHash.put(ScsEJGBean.C_USUCREACION,new Integer (usr.getUserName()));
				    miHash.put(ScsEJGBean.C_FECHACREACION,"sysdate");
					
					if (!ejgAdm.insert(miHash)) {
						throw new ClsExceptions ("Error al crear el EJG desde la designa");
					}
					 //Insertamos tabla relacion EJG con designas
					ScsEJGDESIGNABean ejgDesignabean=new ScsEJGDESIGNABean();
					
					ejgDesignabean.setIdInstitucion(this.getIDInstitucion(request));
					ejgDesignabean.setAnioDesigna(designaBean.getAnio());
					ejgDesignabean.setIdTurno(designaBean.getIdTurno());
					ejgDesignabean.setNumeroDesigna(new Integer(designaBean.getNumero().intValue()));
					ejgDesignabean.setAnioEJG(EJG_anio);
					ejgDesignabean.setIdTipoEJG(EJG_idTipoEJG);
					ejgDesignabean.setNumeroEJG(EJG_numero);
					ScsEJGDESIGNAAdm ejgDesignaAdm = new ScsEJGDESIGNAAdm (this.getUserBean(request));
					if(!ejgDesignaAdm.insert(ejgDesignabean))
						throw new ClsExceptions ("Error al crear el EJG desde la designa");

					
					//como hemos creado un EJG desde una Designación, si esa Designación ha sido creada desde una asistencia
					// creamos automaticamente la relación entre la asistencia y el ejg
					ScsDesignaAdm desigAdm =new ScsDesignaAdm(this.getUserBean(request));
					Hashtable hashDesAsig = desigAdm.procedeDeAsistencia(designaBean.getIdTurno().toString(),designaBean.getNumero().toString(),designaBean.getAnio().toString());
					if (hashDesAsig.get("ASIANIO")!=null && !((String)hashDesAsig.get("ASIANIO")).equals("")){
						//esta relacionado con una asistencia
						// 4. Relacionar designa con la Asistencia siempre que la asistencia no tenga ninguna otra Designacion relacionada
						
						// Obtenemos la asistencia
						Hashtable datos = new Hashtable();
						ScsAsistenciasAdm asistenciaAdm = new ScsAsistenciasAdm (this.getUserBean(request));
						UtilidadesHash.set(datos, ScsAsistenciasBean.C_ANIO,  hashDesAsig.get("ASIANIO").toString());
						UtilidadesHash.set(datos, ScsAsistenciasBean.C_IDINSTITUCION, this.getUserBean(request).getLocation());
						UtilidadesHash.set(datos, ScsAsistenciasBean.C_NUMERO, hashDesAsig.get("ASINUMERO").toString());

						
						ScsAsistenciasBean asistenciaBean = (ScsAsistenciasBean)((Vector)asistenciaAdm.selectByPK(datos)).get(0);
						if (asistenciaBean.getEjgAnio()==null || asistenciaBean.getEjgAnio().equals("")){
							asistenciaBean.setEjgAnio(EJG_anio);
							asistenciaBean.setEjgNumero(new Long (EJG_numero.intValue()));
							asistenciaBean.setEjgIdTipoEjg(EJG_idTipoEJG);
							
							if (!asistenciaAdm.update(asistenciaBean)) {
								throw new ClsExceptions ("Error al crear la relación automática entre Asistencia y el EJG");
							}
						}	
					}
					// 3. Insertamos en ContrariosDesigna los contrario del EJG  
					ScsContrariosDesignaAdm contrariosDesignaAdm = new ScsContrariosDesignaAdm (this.getUserBean(request));
					p.clear(); 
					UtilidadesHash.set(p, ScsContrariosDesignaBean.C_ANIO, 			designaBean.getAnio());
					UtilidadesHash.set(p, ScsContrariosDesignaBean.C_IDINSTITUCION, designaBean.getIdInstitucion());
					UtilidadesHash.set(p, ScsContrariosDesignaBean.C_IDTURNO, 		designaBean.getIdTurno());
					UtilidadesHash.set(p, ScsContrariosDesignaBean.C_NUMERO, 		designaBean.getNumero());
					Vector contrarios = contrariosDesignaAdm.select(p);

					ScsContrariosEJGAdm contrariosEJGAdm = new ScsContrariosEJGAdm (this.getUserBean(request)); 
					for (int i = 0; (contrarios != null) &&  (i < contrarios.size()); i++) {
						ScsContrariosDesignaBean contrariosDesignaBean = (ScsContrariosDesignaBean) contrarios.get(i); 
						ScsContrariosEJGBean contrariosEJGBean = new ScsContrariosEJGBean();
						contrariosEJGBean.setAnio(EJG_anio);
						contrariosEJGBean.setIdInstitucion(EJG_idInstitucion);
						contrariosEJGBean.setIdTipoEJG(EJG_idTipoEJG);
						contrariosEJGBean.setNumero(EJG_numero);
						contrariosEJGBean.setIdPersona(contrariosDesignaBean.getIdPersona());
						contrariosEJGBean.setObservaciones(contrariosDesignaBean.getObservaciones());
						if(contrariosDesignaBean.getIdProcurador()!=null)
							contrariosEJGBean.setIdProcurador(Long.valueOf(contrariosDesignaBean.getIdProcurador()));
						contrariosEJGBean.setIdInstitucionProcurador(contrariosDesignaBean.getIdInstitucionProcurador());
						// TODO // jbd // Con el representante tenemos un problema gordo, en EJG y asistencias es una personaJG y en designas es un colegiado
						// contrariosEJGBean.setIdRepresentanteEjg(contrariosDesignaBean.getIdRepresentanteLegal());
						// contrariosEJGBean.setNombreRepresentanteEjg(contrariosDesignaBean.getNombreRepresentante());
						contrariosEJGBean.setIdAbogadoContrarioEjg(contrariosDesignaBean.getIdAbogadoContrario());
						contrariosEJGBean.setNombreAbogadoContrarioEjg(contrariosDesignaBean.getnombreAbogadoContrario());
						
						if (!contrariosEJGAdm.insert(contrariosEJGBean))
							throw new ClsExceptions ("Error al crear el EJG desde la designa");
					}
					
					// 4. Insertamos en delitosEJG los delitos de la designa 
					ScsDelitosDesignaAdm delitosDesignaAdm = new ScsDelitosDesignaAdm (this.getUserBean(request));
					Hashtable aux = new Hashtable();
					UtilidadesHash.set(aux, ScsDelitosDesignaBean.C_ANIO, 			designaBean.getAnio());
					UtilidadesHash.set(aux, ScsDelitosDesignaBean.C_IDINSTITUCION, 	designaBean.getIdInstitucion());
					UtilidadesHash.set(aux, ScsDelitosDesignaBean.C_IDTURNO, 		designaBean.getIdTurno());
					UtilidadesHash.set(aux, ScsDelitosDesignaBean.C_NUMERO, 		designaBean.getNumero());
					Vector delitos = delitosDesignaAdm.select(aux);
					
					ScsDelitosEJGAdm delitosEJGAdm = new ScsDelitosEJGAdm (this.getUserBean(request));
					for (int i = 0; (delitos != null) &&  (i < delitos.size()); i++) {
						ScsDelitosDesignaBean delitosDesginaBean = (ScsDelitosDesignaBean) delitos.get(i); 
						ScsDelitosEJGBean delitosEJBBean = new ScsDelitosEJGBean();
						delitosEJBBean.setAnio(EJG_anio);
						delitosEJBBean.setIdInstitucion(EJG_idInstitucion);
						delitosEJBBean.setIdTipoEJG(EJG_idTipoEJG);
						delitosEJBBean.setNumero(EJG_numero);
						delitosEJBBean.setIdDelito(delitosDesginaBean.getIdDelito());
						if (!delitosEJGAdm.insert(delitosEJBBean))
							throw new ClsExceptions ("Error al crear el EJG desde la designa");
					}
					//Metemos el check de solicitante='1' para el beneficiario del EJG seleccionado
					if ((miHash.containsKey("IDPERSONAJG")) && (!((String)miHash.get("IDPERSONAJG")).equals(""))){
						ScsUnidadFamiliarEJGAdm admUnidad =  new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
						Hashtable miHashUF=new Hashtable();
						miHashUF.put(ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,EJG_idInstitucion);
						miHashUF.put(ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,EJG_idTipoEJG);
						miHashUF.put(ScsUnidadFamiliarEJGBean.C_ANIO,EJG_anio);
						miHashUF.put(ScsUnidadFamiliarEJGBean.C_NUMERO,EJG_numero);
						miHashUF.put("SOLICITANTE","1");
						miHashUF.put("ENCALIDADDE","SOLICITANTE");
						miHashUF.put(ScsUnidadFamiliarEJGBean.C_IDPERSONA,miHash.get("IDPERSONAJG"));
						
						admUnidad.insert(miHashUF);
					}
				}
				
				// Desde Asistencia
				else {
					// 1. Obtenemos todos los datos de la asistencia
					ScsAsistenciasBean asistenciaBean = null;
/**/				Hashtable hasClavesAsistencia = new Hashtable(); 
					Hashtable p = new Hashtable(); 
					UtilidadesHash.set(p, ScsAsistenciasBean.C_ANIO,    		miForm.getAsistenciaAnio());
					UtilidadesHash.set(p, ScsAsistenciasBean.C_IDINSTITUCION, 	this.getIDInstitucion(request));
					UtilidadesHash.set(p, ScsAsistenciasBean.C_NUMERO,     		miForm.getAsistenciaNumero());
					ScsAsistenciasAdm asisnteciaAdm = new ScsAsistenciasAdm (this.getUserBean(request));
					Vector vA = asisnteciaAdm.select(p);
					if ((vA != null) && (vA.size() == 1)) {
						asistenciaBean = (ScsAsistenciasBean) vA.get(0);
					}
					
					// 2. Insertamos el EJG con los datos de la asistencia 

					UtilidadesHash.set(miHash, ScsEJGBean.C_IDPERSONAJG,     	  asistenciaBean.getIdPersonaJG());
					UtilidadesHash.set(miHash, ScsEJGBean.C_DELITOS,     		  asistenciaBean.getDelitosImputados());
					UtilidadesHash.set(miHash, ScsEJGBean.C_NUMEROPROCEDIMIENTO,  asistenciaBean.getNumeroProcedimiento());
					UtilidadesHash.set(miHash, ScsEJGBean.C_NUMERODILIGENCIA,     asistenciaBean.getNumeroDiligencia());	
					UtilidadesHash.set(miHash, ScsEJGBean.C_COMISARIA,     		  asistenciaBean.getComisaria());
					UtilidadesHash.set(miHash, ScsEJGBean.C_COMISARIAIDINSTITUCION,asistenciaBean.getComisariaIdInstitucion());
					UtilidadesHash.set(miHash, ScsEJGBean.C_JUZGADOIDINSTITUCION, asistenciaBean.getJuzgadoIdInstitucion());
					UtilidadesHash.set(miHash, ScsEJGBean.C_JUZGADO,     		  asistenciaBean.getJuzgado());	
					UtilidadesHash.set(miHash, ScsEJGBean.C_NIG,     		  	  asistenciaBean.getNIG());
					UtilidadesHash.set(miHash, ScsEJGBean.C_IDPRETENSION,	  	  asistenciaBean.getIdPretension());
					
					/**INC_05960_SIGA, se introduce el campo idtipoencalidad a cero ya que cuando se crea por primera
                      vez el campo calidad por defecto sera "Demandado"**/
					miHash.put(ScsEJGBean.C_CALIDAD,"0");
				    miHash.put(ScsEJGBean.C_IDTIPOENCALIDAD,0);
					miHash.put(ScsEJGBean.C_CALIDADIDINSTITUCION,usr.getLocation());
					
					//Se entra cuando creamos desde asistencia y VE
				    miHash.put(ScsEJGBean.C_USUCREACION,new Integer (usr.getUserName()));
				    miHash.put(ScsEJGBean.C_FECHACREACION,"sysdate");
					
					if (!ejgAdm.insert(miHash)) {
						throw new ClsExceptions ("Error al crear el EJG desde la asistencia");
					}
					//MKetemos tambien al solicitante en la unidad familiar
					if ((miHash.containsKey("IDPERSONAJG")) && (!((String)miHash.get("IDPERSONAJG")).equals(""))){
						ScsUnidadFamiliarEJGAdm admUnidad =  new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
						miHash.put("SOLICITANTE","1");
						miHash.put("ENCALIDADDE","SOLICITANTE");
						miHash.put("IDPERSONA",miHash.get("IDPERSONAJG"));
						
						admUnidad.insert(miHash);
					}
					
					

					String[] Claves={ScsAsistenciasBean.C_IDINSTITUCION,ScsAsistenciasBean.C_ANIO,ScsAsistenciasBean.C_NUMERO};
					String[] Campos={ScsAsistenciasBean.C_EJGANIO,ScsAsistenciasBean.C_EJGNUMERO,ScsAsistenciasBean.C_EJGIDTIPOEJG};
/**/				UtilidadesHash.set(hasClavesAsistencia, ScsAsistenciasBean.C_ANIO,  	  asistenciaBean.getAnio());
/**/				UtilidadesHash.set(hasClavesAsistencia, ScsAsistenciasBean.C_NUMERO,	  asistenciaBean.getNumero());
/**/				UtilidadesHash.set(hasClavesAsistencia, ScsAsistenciasBean.C_IDINSTITUCION, 	this.getIDInstitucion(request));
/**/				UtilidadesHash.set(hasClavesAsistencia, ScsAsistenciasBean.C_EJGANIO,  	  		(String)miHash.get(ScsEJGBean.C_ANIO));
/**/				UtilidadesHash.set(hasClavesAsistencia, ScsAsistenciasBean.C_EJGNUMERO,	  		(String)miHash.get(ScsEJGBean.C_NUMERO));
/**/				UtilidadesHash.set(hasClavesAsistencia, ScsAsistenciasBean.C_EJGIDTIPOEJG, 		(String)miHash.get(ScsEJGBean.C_IDTIPOEJG));

/**/				ScsAsistenciasAdm asisAdm =  new ScsAsistenciasAdm(this.getUserBean(request));					
/**/				if (!asisAdm.updateDirect(hasClavesAsistencia,Claves,Campos)) {
/**/					throw new ClsExceptions ("Error al crear el EJG desde la asistencia");
/**/				}

					// Si la asistencia desde la que hemos creado el EJG tiene relación con una Designación, establecemos
                    // automáticamente la relación entre el EJG creado y la Designación

					if (asistenciaBean.getDesignaAnio()!=null && !(asistenciaBean.getDesignaAnio().equals(""))){
						ScsEJGDESIGNABean ejgDesignabean=new ScsEJGDESIGNABean();
						
						ejgDesignabean.setIdInstitucion(this.getIDInstitucion(request));
						ejgDesignabean.setAnioDesigna(asistenciaBean.getDesignaAnio());
						ejgDesignabean.setIdTurno(asistenciaBean.getDesignaTurno());
						ejgDesignabean.setNumeroDesigna(new Integer(asistenciaBean.getDesignaNumero().intValue()));
						ejgDesignabean.setAnioEJG(new Integer((String)miHash.get(ScsEJGBean.C_ANIO)));
						ejgDesignabean.setIdTipoEJG(new Integer((String)miHash.get(ScsEJGBean.C_IDTIPOEJG)));
						ejgDesignabean.setNumeroEJG(new Integer((String)miHash.get(ScsEJGBean.C_NUMERO)));
						
						Hashtable hashEjgDesigna=new Hashtable();
						ScsEJGDESIGNAAdm ejgDesignaAdm = new ScsEJGDESIGNAAdm (this.getUserBean(request));
						
						UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_ANIODESIGNA, 			asistenciaBean.getDesignaAnio());
						UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_NUMERODESIGNA,  new Integer(asistenciaBean.getDesignaNumero().intValue()));
						UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDTURNO, 		asistenciaBean.getDesignaTurno());
						UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDTIPOEJG, 		new Integer((String)miHash.get(ScsEJGBean.C_IDTIPOEJG)));
						UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_NUMEROEJG, 		new Integer((String)miHash.get(ScsEJGBean.C_NUMERO)));
						UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_ANIOEJG, 		new Integer((String)miHash.get(ScsEJGBean.C_ANIO)));
						UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDINSTITUCION, 		this.getIDInstitucion(request));
						Vector existeRelacion = ejgDesignaAdm.select(hashEjgDesigna);
						
						if (existeRelacion.size()==0){//Si no existe la relación, la creamos
							if(!ejgDesignaAdm.insert(ejgDesignabean))
								throw new ClsExceptions ("Error al crear la relacion entre EJG y la designa");
						}
					}
					// 3. Insertamos en ContrariosAsistencia los contrario del EJG  
					ScsContrariosAsistenciaAdm contrariosAsistenciaAdm = new ScsContrariosAsistenciaAdm (this.getUserBean(request));
					p.clear(); 
					UtilidadesHash.set(p, ScsContrariosAsistenciaBean.C_ANIO, 			asistenciaBean.getAnio());
					UtilidadesHash.set(p, ScsContrariosAsistenciaBean.C_IDINSTITUCION,  asistenciaBean.getIdInstitucion());
					UtilidadesHash.set(p, ScsContrariosAsistenciaBean.C_NUMERO, 		asistenciaBean.getNumero());
					Vector contrarios = contrariosAsistenciaAdm.select(p);

					ScsContrariosEJGAdm contrariosEJGAdm = new ScsContrariosEJGAdm (this.getUserBean(request)); 
					for (int i = 0; (contrarios != null) &&  (i < contrarios.size()); i++) {
						ScsContrariosAsistenciaBean contrariosAsistenciaBean = (ScsContrariosAsistenciaBean) contrarios.get(i); 
						ScsContrariosEJGBean contrariosEJGBean = new ScsContrariosEJGBean();
						contrariosEJGBean.setAnio(EJG_anio);
						contrariosEJGBean.setIdInstitucion(EJG_idInstitucion);
						contrariosEJGBean.setIdTipoEJG(EJG_idTipoEJG);
						contrariosEJGBean.setNumero(EJG_numero);
						contrariosEJGBean.setIdPersona(new Integer(contrariosAsistenciaBean.getIdPersona().intValue()));
						contrariosEJGBean.setObservaciones(contrariosAsistenciaBean.getObservaciones());
						if (!contrariosEJGAdm.insert(contrariosEJGBean))
							throw new ClsExceptions ("Error al crear el EJG desde la asistencia");
					}

					// 4. Insertamos en delitosEJG todos los delitos de la asistencia
					ScsDelitosAsistenciaAdm delitosAsistenciaAdm = new ScsDelitosAsistenciaAdm (this.getUserBean(request));
					Hashtable aux = new Hashtable();
					UtilidadesHash.set(aux, ScsDelitosAsistenciaBean.C_ANIO, 			asistenciaBean.getAnio());
					UtilidadesHash.set(aux, ScsDelitosAsistenciaBean.C_IDINSTITUCION, 	asistenciaBean.getIdInstitucion());
					UtilidadesHash.set(aux, ScsDelitosAsistenciaBean.C_NUMERO, 			asistenciaBean.getNumero());
					Vector delitos = delitosAsistenciaAdm.select(aux);
					
					ScsDelitosEJGAdm delitosEJGAdm = new ScsDelitosEJGAdm (this.getUserBean(request));
					for (int i = 0; (delitos != null) &&  (i < delitos.size()); i++) {
						ScsDelitosAsistenciaBean delitosAsistenciaBean = (ScsDelitosAsistenciaBean) delitos.get(i); 
						ScsDelitosEJGBean delitosEJBBean = new ScsDelitosEJGBean();
						delitosEJBBean.setAnio(EJG_anio);
						delitosEJBBean.setIdInstitucion(EJG_idInstitucion);
						delitosEJBBean.setIdTipoEJG(EJG_idTipoEJG);
						delitosEJBBean.setNumero(EJG_numero);
						delitosEJBBean.setIdDelito(delitosAsistenciaBean.getIdDelito());
						if (!delitosEJGAdm.insert(delitosEJBBean))
							throw new ClsExceptions ("Error al crear el EJG desde la asistencia");
					}
				}
			}
			
			if (miHash.get(ScsSOJBean.C_ANIO)==null){//Sólo en el caso de un EJG dado de alta desde un SOJ
				// 5. Si se inserta una idPersonaJG (en el caso de crearse desde una Asistencia o Designa) se inserta también en UnidadFamiliar
				if ((miHash.containsKey("IDPERSONAJG")) && (!((String)miHash.get("IDPERSONAJG")).equals(""))){
					ScsUnidadFamiliarEJGAdm admUnidad =  new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
					miHash.put("SOLICITANTE","1");
					miHash.put("ENCALIDADDE","SOLICITANTE");
					miHash.put("IDPERSONA",miHash.get("IDPERSONAJG"));
					
					admUnidad.insert(miHash);
				}
			}	
			
			// 7. Saltos y compensaciones
			ScsSaltosCompensacionesAdm saltosCompAdm = new ScsSaltosCompensacionesAdm(this.getUserBean(request));
			saltosCompAdm.cumplirCompensacionDesdeEjg(miHash);
			
			String checkSalto = request.getParameter("checkSalto");
			saltosCompAdm.crearSaltoDesdeEjg(miForm, checkSalto);
			
			
			tx.commit();
			

			// Añadimos parametros para las pestanhas
			//request.getSession().setAttribute("idEJG", miHash);
			session.setAttribute("accion","editar");
			//request.getSession().setAttribute("modo", "editar");
			request.setAttribute("NUMERO",miHash.get(ScsEJGBean.C_NUMERO));
			request.setAttribute("TIPO",miHash.get(ScsEJGBean.C_IDTIPOEJG));
			request.setAttribute("INSTITUCION",miHash.get(ScsEJGBean.C_IDINSTITUCION));
			request.setAttribute("ANIO",miHash.get(ScsEJGBean.C_ANIO));
			
			//Comprobamos si tiene permisos de creación de EJG para la redirección
			String accessEnvio="";
			boolean isPermisoEnvio = true;
			try {
				accessEnvio = testAccess(request.getContextPath()+"/JGR_EJG.do",null,request);
				if (!accessEnvio.equals(SIGAConstants.ACCESS_READ) && !accessEnvio.equals(SIGAConstants.ACCESS_FULL)) {
					//miForm.setEnviar(ClsConstants.DB_FALSE);
					isPermisoEnvio = false;
					request.setAttribute("PERMISO","NO");
				}else{
					request.setAttribute("PERMISO","SI");
				}
				//hacemos lo siguiente para setear el permiso de esta accion
				testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
			} catch (ClsExceptions e) {
				throw new SIGAException(e.getMsg());
			}
			
			
			String origen = miForm.getOrigen();
			if(origen!=null && origen.equals("A")&& miForm.getAsistenciaAnio()!=null && !miForm.getAsistenciaAnio().equals("")){
				return super.exitoModal("messages.inserted.success", request);
			}

		} 
		catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		return exitoModal("messages.inserted.success",request);
	} //insertar()

	

	protected String exitoModal(String mensaje, HttpServletRequest request) 
	{
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje",mensaje);
		}
		request.setAttribute("modal","");
		return "exitoEJG"; 
	}
	


	protected void copiarBeneficiarioSOJEnEJG (Hashtable hdatosSOJ,Hashtable hdatosEJG,HttpServletRequest request) throws SIGAException 
	{
		String result = "";
		
		Hashtable hIdPersonaJG=new Hashtable();
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	     

	     	// clave del EJG
			
			boolean nuevaRel = false; 
	     	// Datos Persona
			Hashtable persona= new Hashtable();
			Hashtable beneficiari= new Hashtable();
			ScsPersonaJGAdm perAdm = new ScsPersonaJGAdm(this.getUserBean(request));
			ScsBeneficiarioSOJAdm beneficiarioAdm=new ScsBeneficiarioSOJAdm(this.getUserBean(request));
			
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDINSTITUCION,user.getLocation());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPERSONA,hdatosSOJ.get(ScsSOJBean.C_IDPERSONAJG).toString());
			// Obtenemos toda la informacion del Beneficiario que se encuentra en la tabla scs_personajg
			Vector vPersonaJG = perAdm.selectByPK(persona);
			ScsPersonaJGBean personaJGBean=null;
			if ((vPersonaJG != null) && (vPersonaJG.size() == 1)) {
				personaJGBean = (ScsPersonaJGBean) vPersonaJG.get(0);
			}
			
			Vector vBeneficiarioSOJ = beneficiarioAdm.selectByPK(persona);
			ScsBeneficiarioSOJBean beneficiarioSOJBean=null;
			if ((vBeneficiarioSOJ != null) && (vBeneficiarioSOJ.size() == 1)) {
				beneficiarioSOJBean = (ScsBeneficiarioSOJBean) vBeneficiarioSOJ.get(0);
			}
			
			// OJO, utilizo los set, porque siempre tengo datos, aunque sean blancos, que es lo que me interesa
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_NIF,personaJGBean.getNif().toString());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_NOMBRE,personaJGBean.getNombre());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_APELLIDO1,personaJGBean.getApellido1());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_APELLIDO2,personaJGBean.getApellido2());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_DIRECCION,personaJGBean.getDireccion());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_CODIGOPOSTAL,personaJGBean.getCodigoPostal());						
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_FECHANACIMIENTO,GstDate.getApplicationFormatDate("",personaJGBean.getFechaNacimiento()));			
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPROFESION,personaJGBean.getIdProfesion());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPAIS,personaJGBean.getIdPais());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPROVINCIA,personaJGBean.getIdProvincia());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPOBLACION,personaJGBean.getIdPoblacion());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_ESTADOCIVIL,personaJGBean.getIdEstadoCivil());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_REGIMENCONYUGAL,personaJGBean.getRegimenConyugal());			 
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_TIPOPERSONAJG,personaJGBean.getTipo());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDTIPOIDENTIFICACION,personaJGBean.getTipoIdentificacion());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_ENCALIDADDE,personaJGBean.getEnCalidadDe());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_OBSERVACIONES,personaJGBean.getObservaciones());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDREPRESENTANTEJG,personaJGBean.getIdRepresentanteJG());
			

			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDINSTITUCION,user.getLocation());
			UtilidadesHash.set(persona,ScsPersonaJGBean.C_IDPERSONA,hdatosSOJ.get(ScsSOJBean.C_IDPERSONAJG).toString());
			
			// Obtenemos toda la informacion del Beneficiario que se encuentra en la tabla scs_personajg
			

			ScsUnidadFamiliarEJGAdm unidadFamiliarAdm = new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
			
			// ATENCION!! SE IGNORA LA TABLA SCS_CONTRARIOS_EJG

			// CREAR SCS_UNIDADFAMILAREJG
			Hashtable unidadFamiliarBean = new Hashtable();
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,hdatosEJG.get(ScsEJGBean.C_IDINSTITUCION).toString());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_ANIO,hdatosEJG.get(ScsEJGBean.C_ANIO).toString());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_NUMERO,hdatosEJG.get(ScsEJGBean.C_NUMERO).toString());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,hdatosEJG.get(ScsEJGBean.C_IDTIPOEJG).toString());				
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IDPERSONA,personaJGBean.getIdPersona());
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_OBSERVACIONES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_ENCALIDADDE,UtilidadesHash.getString(hdatosEJG,ScsEJGBean.C_CALIDAD));
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_SOLICITANTE,"1");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_OTROSBIENES,"");
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES,"");						 
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES,"");	
			UtilidadesHash.set(unidadFamiliarBean,ScsUnidadFamiliarEJGBean.C_TIPOGRUPOLAB,beneficiarioSOJBean.getIdTipoGrupoLab());
			
			
	     	
			
			
			
			// RELACIONARLO CON EL EJG (UPDATE NORMAL)
			// Si el concepto es EJG solamente
//			ScsEJGAdm admEJG = new ScsEJGAdm(this.getUserBean(request));
//			String idPersonaAnterior = "";
//			Hashtable ht = new Hashtable();
//			ht.put(ScsEJGBean.C_IDINSTITUCION,hdatosEJG.get(ScsEJGBean.C_IDINSTITUCION).toString());
//			ht.put(ScsEJGBean.C_IDTIPOEJG,hdatosEJG.get(ScsEJGBean.C_IDTIPOEJG).toString());
//			ht.put(ScsEJGBean.C_ANIO,hdatosEJG.get(ScsEJGBean.C_ANIO).toString());
//			ht.put(ScsEJGBean.C_NUMERO,hdatosEJG.get(ScsEJGBean.C_NUMERO).toString());
//			Vector v = admEJG.selectByPK(ht);
//			if (v!=null && v.size()>0) {
//				nuevaRel = true; 
//				ScsEJGBean beanEJG = (ScsEJGBean) v.get(0);
//				if (miform.getConceptoE().equals(PersonaJGAction.EJG)) {
//					// compruebo si ha cambiado el id persona para la relacion
//					if (beanEJG.getIdPersonaJG()!=null && !beanEJG.getIdPersonaJG().equals(new Integer(miform.getIdPersonaJG()))) {
//						// guardo el idpersona anterior para buscar las relaciones y actualizarlas
//						idPersonaAnterior = beanEJG.getIdPersonaJG().toString();
//					} else {
//						// si no el que borrare sera el mismo, el actual
//						idPersonaAnterior = miform.getIdPersonaJG();
//					}
//				} else 
//				if (miform.getConceptoE().equals(PersonaJGAction.EJG_UNIDADFAMILIAR)) {
//					// en este caso lo cojo del databackup de unidad familiar, si esxiste
//					Hashtable oldUF = (Hashtable) dataBackup.get(ScsUnidadFamiliarEJGBean.T_NOMBRETABLA);
//					if (oldUF!=null) {
//						idPersonaAnterior = (String) oldUF.get(ScsUnidadFamiliarEJGBean.C_IDPERSONA);
//					} else {
//						idPersonaAnterior = null;
//					}
//				}
//				// RGG solamente para el interesado
//				if (miform.getConceptoE().equals(PersonaJGAction.EJG)) {
//					// actualizo la personaJG, que sera el interesado en este caso. 
//					beanEJG.setIdPersonaJG(new Integer(miform.getIdPersonaJG()));
//					if (!admEJG.updateDirect(beanEJG)) {
//						throw new ClsExceptions("Error en updateEJG. " + admEJG.getError());
//					}
//				}
//			}
//			
			// INSERTAR O ACTUALIZAR UNIDAD FAMILIAR EJG (RELACIONADO)
			ScsUnidadFamiliarEJGAdm ufAdm = new ScsUnidadFamiliarEJGAdm(this.getUserBean(request));
			// Hay que actualizarla. La borro con la clave adecuada
			// anterior para el caso de que haya cambiado
			
			// jbd // Crisis de bloqueos de SCS_EJG
			/* Quito el borrado de la UF que se hace sin sentido, ya que se acaba de crear el EJG
				Hashtable borrar = new Hashtable();
				borrar.put(ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,hdatosEJG.get(ScsEJGBean.C_IDINSTITUCION).toString());
				borrar.put(ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,hdatosEJG.get(ScsEJGBean.C_IDTIPOEJG).toString());
				borrar.put(ScsUnidadFamiliarEJGBean.C_ANIO,hdatosEJG.get(ScsEJGBean.C_ANIO).toString());
				borrar.put(ScsUnidadFamiliarEJGBean.C_NUMERO,hdatosEJG.get(ScsEJGBean.C_NUMERO).toString());
				borrar.put(ScsUnidadFamiliarEJGBean.C_IDPERSONA,personaJGBean.getIdPersona());
				ufAdm.delete(borrar);
			*/		
			
			// Insert unidad familiar con los nuevos valores
			if (!ufAdm.insert(unidadFamiliarBean)) {
				throw new ClsExceptions("Error en insert unidad familiar. " + ufAdm.getError());
			}
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		
	}
	
	
}