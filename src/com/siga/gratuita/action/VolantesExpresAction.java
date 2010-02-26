package com.siga.gratuita.action;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsActuacionAsistCosteFijoAdm;
import com.siga.beans.ScsActuacionAsistenciaAdm;
import com.siga.beans.ScsActuacionAsistenciaBean;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsDelitosAsistenciaAdm;
import com.siga.beans.ScsDelitosAsistenciaBean;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.beans.ScsGuardiasColegiadoBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.VolantesExpresForm;
import com.siga.gratuita.util.calendarioSJCS.CalendarioSJCS;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;


public class VolantesExpresAction extends MasterAction 
{
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String mapDestino = "exception";
		MasterForm miForm = null;
			
		try { 
			do {
				miForm = (MasterForm) formulario;
				
				if (miForm != null) {
					String accion = miForm.getModo();
				
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = abrir (mapping, miForm, request, response);
						break; 
					} 
					else if (accion.equalsIgnoreCase("obtenerDatosCabecera")){  
						mapDestino = obtenerDatosCabecera(miForm, request);
					}else if (accion.equalsIgnoreCase("limpiarPersona")){  
						mapDestino = limpiarPersona(mapping,miForm,request,response);
					}  
					else if (accion.equalsIgnoreCase("insertarFila")){  
						mapDestino = insertarFila(miForm, request);
					} 
					else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);
			
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}
	
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
	    ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp3 = new ReadProperties("SIGA.properties");
		String tipoAsistenciaDefecto = rp3.returnProperty("codigo.general.scs_tipoasistencia.volanteExpres");
		request.setAttribute("tipoAsistenciaDefecto", tipoAsistenciaDefecto);
		UsrBean usrBean = this.getUserBean(request);
		GenParametrosAdm paramAdm = new GenParametrosAdm (usrBean);
		String delitos_VE = paramAdm.getValor (usrBean.getLocation (), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_DELITOS_VE, "");
		Boolean isDelitosVE = new Boolean((delitos_VE!=null && delitos_VE.equalsIgnoreCase(ClsConstants.DB_TRUE)));
		request.setAttribute("isDelitosVE", isDelitosVE);
		VolantesExpresForm f  = (VolantesExpresForm) formulario;
		request.setAttribute("fechaJustificacion", f.getFechaJustificacion());
		
		formulario.reset(mapping, request);
		return "inicio";
	}
	protected String abrir (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		request.getSession().removeAttribute("DATABACKUP");
		UsrBean usrBean = this.getUserBean(request);
		GenParametrosAdm paramAdm = new GenParametrosAdm (usrBean);
		String delitos_VE = paramAdm.getValor (usrBean.getLocation (), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_DELITOS_VE, "");
		Boolean isDelitosVE = new Boolean((delitos_VE!=null && delitos_VE.equalsIgnoreCase(ClsConstants.DB_TRUE)));
		request.setAttribute("isDelitosVE", isDelitosVE);
		VolantesExpresForm f  = (VolantesExpresForm) formulario;
		request.setAttribute("fechaJustificacion", f.getFechaJustificacion());
		
		return "inicio";
			}
	
	protected String limpiarPersona(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		//formulario.reset(mapping, request);	
	    ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp3 = new ReadProperties("SIGA.properties");
		VolantesExpresForm f  = (VolantesExpresForm) formulario;
        String tipoAsistenciaDefecto = rp3.returnProperty("codigo.general.scs_tipoasistencia.volanteExpres");
        UsrBean usrBean = this.getUserBean(request);
		GenParametrosAdm paramAdm = new GenParametrosAdm (usrBean);
		String delitos_VE = paramAdm.getValor (usrBean.getLocation (), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_DELITOS_VE, "");
		Boolean isDelitosVE = new Boolean((delitos_VE!=null && delitos_VE.equalsIgnoreCase(ClsConstants.DB_TRUE)));
		request.setAttribute("isDelitosVE", isDelitosVE);
		
		request.setAttribute("fechaJustificacion", f.getFechaJustificacion());
        
        String letrado        = f.getLetrado();
		String diaGuardia     = f.getGuardiaDia();
		Integer idInstitucion = this.getIDInstitucion(request);
		String centroJuzgado = f.getLugar();
		String idGuardiaFORM = f.getGuardia();
		String idTurno = f.getTurno();
		String tipoAsistencia = f.getTipoAsistencia();
		String tipoAsistenciaColegio = f.getTipoAsistenciaColegio();
		String getSinAvisos = f.getSinAvisos();
		String getDesdeNuevo = f.getDesdeNuevo();
		String getSustitutoDe = f.getSustitutoDe();
		String getGuardiaDelSustituto = f.getGuardiaDelSustituto();
		
		request.setAttribute("diaGuardia", diaGuardia);
		request.setAttribute("centroJuzgado", f.getLugar());
		request.setAttribute("idGuardia", f.getGuardia());
		request.setAttribute("idTurno", f.getTurno());
		request.setAttribute("tipoAsistencia", f.getTipoAsistencia());
		request.setAttribute("tipoAsistenciaColegio", f.getTipoAsistenciaColegio());
		request.setAttribute("sinAvisos", f.getSinAvisos());
		request.setAttribute("desdeNuevo", f.getDesdeNuevo());
		request.setAttribute("asistencias", new Vector());
        
        
        request.setAttribute("tipoAsistenciaDefecto", tipoAsistenciaDefecto);
        
		
        
		return "inicio";
	}
	private void borrar (String anio, String numero, String idInstitucion, UsrBean usr) throws ClsExceptions 
	{
		Hashtable claves = new Hashtable ();
		UtilidadesHash.set (claves, ScsAsistenciasBean.C_ANIO, anio);
		UtilidadesHash.set (claves, ScsAsistenciasBean.C_NUMERO, numero);
		UtilidadesHash.set (claves, ScsAsistenciasBean.C_IDINSTITUCION, idInstitucion);

		String campos [] = {ScsAsistenciasBean.C_ANIO, ScsAsistenciasBean.C_NUMERO, ScsAsistenciasBean.C_IDINSTITUCION};
		
		ScsActuacionAsistCosteFijoAdm costeActuacionAdm = new ScsActuacionAsistCosteFijoAdm (usr);
		costeActuacionAdm.deleteDirect(claves, campos);
		
		ScsActuacionAsistenciaAdm actuacionAdm = new ScsActuacionAsistenciaAdm (usr);
		actuacionAdm.deleteDirect(claves, campos);
		ScsDelitosAsistenciaAdm delitosAsistenciaAdm = new ScsDelitosAsistenciaAdm (usr);
		delitosAsistenciaAdm.borrarDelitosAsitencia(new Integer(idInstitucion),new Integer(anio),new Integer(numero));
		
		
		ScsAsistenciasAdm asistenciaAdm = new ScsAsistenciasAdm (usr);
		asistenciaAdm.delete(claves);
	}
	
	/**
	 * Recupera los datos de la asistencia e inserta al letrado si no esta
	 */
	private Hashtable getDatosAsistencia (int i, VolantesExpresForm f, UsrBean usr)
			throws SIGAException 
	{
		Hashtable salida = null;
		
		try { 
			String idInstitucion = usr.getLocation();
			Vector vCampos = (Vector)f.getDatosTablaOcultos(i);
			if (vCampos != null) {
				
				salida = new Hashtable();
				
				String accion = (String)vCampos.get(0);
				String claveAnio = (String)vCampos.get(1);
				String claveNumero = (String)vCampos.get(2);
				String claveIdInstitucion = (String)vCampos.get(3);

				UtilidadesHash.set (salida, "accion", accion);
				UtilidadesHash.set (salida, "claveAnio", claveAnio);
				UtilidadesHash.set (salida, "claveNumero", claveNumero);
				UtilidadesHash.set (salida, "claveIdInstitucion", claveIdInstitucion);

				if (accion.equalsIgnoreCase("--borrar--")) {
					return salida;
				}
				
				String hora = (String)vCampos.get(4);
				String minuto = (String)vCampos.get(5);
				String comisaria_juzgado = (String)vCampos.get(6);
				String comisaria_juzgado_institucion = (String)vCampos.get(7);
				String dni = UtilidadesString.replaceAllIgnoreCase((String)vCampos.get(8), "~", ",");
				String nombre = (String)vCampos.get(9);
				nombre = UtilidadesString.replaceAllIgnoreCase(nombre, "~", ",");
				String apellido1 = UtilidadesString.replaceAllIgnoreCase((String)vCampos.get(10), "~", ",");
				String apellido2 = UtilidadesString.replaceAllIgnoreCase((String)vCampos.get(11), "~", ",");
				String idPersonaAsistido = (String)vCampos.get(12);
				String diligencia = UtilidadesString.replaceAllIgnoreCase((String)vCampos.get(13), "~", ",");
				String observaciones = UtilidadesString.replaceAllIgnoreCase((String)vCampos.get(14), "~", ",");
				String numeroEjg = (String)vCampos.get(15);
				String anioEjg = (String)vCampos.get(16);
				String idTipoEjg = (String)vCampos.get(17);
				String idDelito = (String)vCampos.get(18);
				
				// Estos campos pueden ser vacios o nulos
				if (this.esCampoVacio(hora)) hora = new String ("00");
				if (this.esCampoVacio(minuto)) minuto = new String ("00");
				if (this.esCampoVacio(dni)) dni = new String ("");
				if (this.esCampoVacio(apellido1)) apellido1 = new String ("");
				if (this.esCampoVacio(apellido2)) apellido2 = new String ("");
				if (this.esCampoVacio(idPersonaAsistido)) idPersonaAsistido = null;
				if (this.esCampoVacio(diligencia)) diligencia = new String ("");
				if (this.esCampoVacio(observaciones)) observaciones = new String ("");
				if (this.esCampoVacio(numeroEjg)) numeroEjg = new String ("");
				if (this.esCampoVacio(anioEjg)) anioEjg = new String ("");
				if (this.esCampoVacio(idTipoEjg)) idTipoEjg = new String ("");
				
				
				
				String anio = f.getGuardiaDia().split("/")[2];
				String fechaAsistencia = GstDate.getApplicationFormatDate("", f.getGuardiaDia());
				
				fechaAsistencia = fechaAsistencia.substring(0,fechaAsistencia.indexOf(" ")) + " " + hora + ":" + minuto + ":00";
				
				// Si no hay persona la insertamos
				if (idPersonaAsistido == null || idPersonaAsistido.equals("") || idPersonaAsistido.equalsIgnoreCase("nuevo") || accion.equalsIgnoreCase("--modificar--")) { 
					ScsPersonaJGAdm personaAdm = new ScsPersonaJGAdm (usr);
					ScsPersonaJGBean p = new ScsPersonaJGBean();
					p.setApellido1(apellido1);
					p.setApellido2(apellido2);
					p.setIdInstitucion(new Integer(idInstitucion));
					p.setNif(dni);
					p.setNombre(nombre);
					p.setTipo(ClsConstants.TIPO_PERSONA_FISICA); 						// persona fisica
					p.setTipoIdentificacion(""+ClsConstants.TIPO_IDENTIFICACION_OTRO);	// otro
					personaAdm.prepararInsert(p);
					personaAdm.insert(p);
					idPersonaAsistido = "" + p.getIdPersona();
				}

				UtilidadesHash.set (salida, "anio", anio);
				UtilidadesHash.set (salida, "fechaAsistencia", fechaAsistencia);
				UtilidadesHash.set (salida, "idPersonaAsistido", idPersonaAsistido);
				UtilidadesHash.set (salida, "comisaria_juzgado", comisaria_juzgado);
				UtilidadesHash.set (salida, "comisaria_juzgado_institucion", comisaria_juzgado_institucion);
				UtilidadesHash.set (salida, "diligencia", diligencia);
				UtilidadesHash.set (salida, "observaciones", observaciones);
				UtilidadesHash.set (salida, "numeroEjg", numeroEjg);
				UtilidadesHash.set (salida, "anioEjg", anioEjg);
				UtilidadesHash.set (salida, "idTipoEjg", idTipoEjg);
				UtilidadesHash.set (salida, "idDelito", idDelito.trim());
			}
		}
		catch (Exception e) {
			throw new SIGAException (e.getMessage(),e);
		}
		return salida;
	} //getDatosAsistencia ()
	
	protected String insertar(ActionMapping mapping, MasterForm formulario,	HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		UserTransaction tx = null;

		try {

			VolantesExpresForm f  = (VolantesExpresForm) formulario;
			String idTurno           = f.getTurno().split(",")[1];
			String idGuardia         = f.getGuardia().split(",")[0];
			String idInstitucion     = "" + this.getIDInstitucion(request);
			String idPersonaSaliente = f.getSustitutoDe(); 
			String idPersonaEntrante = f.getLetrado(); 
			String fechaJustificacion = GstDate.getApplicationFormatDate("", f.getFechaJustificacion());
			
			tx = this.getUserBean(request).getTransaction();		
			tx.begin(); // Abro aqui la transaccion porque se insertan personas
			
			// Si es una sustitucion aplico la sustitucion del letrado y todo lo que ello conlleva
			if (f.getSustitutoDe() != null && !f.getSustitutoDe().equals("")) {
				try {
					String m = aplicarSustitucion(idInstitucion, idTurno, idGuardia, idPersonaSaliente, idPersonaEntrante, f.getGuardiaDia(), this.getUserBean(request));
					if (m != null) {
						throw new SIGAException (m);
		 			}
				} catch (Exception e) {
					request.setAttribute("sustituto", f.getSustitutoDe());
					throw e;
				}
				
				
			}else{
				Hashtable h = new Hashtable();
				UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDPERSONA, idPersonaEntrante);
				UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_FECHAFIN, GstDate.getApplicationFormatDate("", f.getGuardiaDia()));
				UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDINSTITUCION, idInstitucion);
				
				
				if (f.getTurno() != null && !f.getTurno().equals("")) {
					UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDTURNO, f.getTurno().split(",")[1]);
				}
				if (f.getGuardia() != null && !f.getGuardia().equals("")) {
					UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDGUARDIA, f.getGuardia().split(",")[0]);
				}
				ScsGuardiasColegiadoAdm adm = new ScsGuardiasColegiadoAdm (this.getUserBean(request));
				// Vector vGuardias = adm.select(h);
				Vector vGuardias = adm.selectParaVolantes(h);
				//Si no tiene guardia ese dia insertaremos en el calendario.
				//sin oexiste calendario saltara la excepcion y no insertara
				if (vGuardias == null ||vGuardias.size()<1 ){
					insertarGuardiaManual(idInstitucion, idTurno, idGuardia, idPersonaEntrante,  f.getGuardiaDia(), this.getUserBean(request));
					
					
				}
					
					// No tiene guardias ese dia
					
				
				
				
			}
			

			ScsAsistenciasAdm asistenciaAdm = new ScsAsistenciasAdm (this.getUserBean(request));
			ScsActuacionAsistenciaAdm actAdm = new ScsActuacionAsistenciaAdm (this.getUserBean(request)); 
			
			
			Vector vAsistencias = f.getDatosTabla();
			for (int i = 0; i < vAsistencias.size(); i++ ) {
				
				// Recuperamos los datos de la asistencia e inserta al letrado si no esta
				Hashtable h = this.getDatosAsistencia(i, f, this.getUserBean(request));
				if (h == null) continue;
				
				String accion = UtilidadesHash.getString(h, "accion");
				String claveAnio = UtilidadesHash.getString(h, "claveAnio");
				String claveNumero = UtilidadesHash.getString(h, "claveNumero");
				String claveIdInstitucion = UtilidadesHash.getString(h, "claveIdInstitucion");
				
				if (accion == null) continue;

				if (accion.equalsIgnoreCase("--borrar--")) {
					this.borrar (claveAnio, claveNumero, claveIdInstitucion, this.getUserBean(request));
					continue;
				}

				String anio = UtilidadesHash.getString(h, "anio");
				String fechaAsistencia = UtilidadesHash.getString(h, "fechaAsistencia");
				String idPersonaAsistido = UtilidadesHash.getString(h, "idPersonaAsistido");
				String comisaria_juzgado = UtilidadesHash.getString(h, "comisaria_juzgado");
				String comisaria_juzgado_institucion = UtilidadesHash.getString(h, "comisaria_juzgado_institucion");
				String diligencia = UtilidadesHash.getString(h, "diligencia");
				String observaciones = UtilidadesHash.getString(h, "observaciones");
				String numeroEjg = UtilidadesHash.getString(h, "numeroEjg");
				String anioEjg = UtilidadesHash.getString(h, "anioEjg");
				String idTipoEjg = UtilidadesHash.getString(h, "idTipoEjg");
				String idDelito = UtilidadesHash.getString(h, "idDelito");
				
				ScsAsistenciasBean asistencia = new ScsAsistenciasBean ();
				
				asistencia.setAnio(new Integer(anio));
				asistencia.setFechaEstadoAsistencia(fechaAsistencia);
				asistencia.setFechaGuardia(GstDate.getApplicationFormatDate("", f.getGuardiaDia()));
				
				
				asistencia.setFechaHora(fechaAsistencia);
				asistencia.setIdEstadoAsistencia(new Integer(1));
				asistencia.setIdguardia(new Integer(idGuardia));
				asistencia.setIdInstitucion(new Integer(idInstitucion));
				
				
				
				asistencia.setIdPersonaColegiado(new Integer(f.getLetrado()));
				asistencia.setIdPersonaJG(new Integer(idPersonaAsistido));
				asistencia.setIdTipoAsistencia(new Integer(f.getTipoAsistencia()));
				asistencia.setIdTipoAsistenciaColegio(new Integer(f.getTipoAsistenciaColegio()));
				asistencia.setIdturno(new Integer(idTurno));
				asistencia.setNumeroDiligencia(diligencia);
				asistencia.setObservaciones(observaciones);
				if(!anioEjg.equals("")){
					asistencia.setEjgAnio(new Integer(anioEjg));
					asistencia.setEjgIdTipoEjg(new Integer(idTipoEjg));
					asistencia.setEjgNumero(new Long(numeroEjg));
				}
				
				if (f.getLugar().equalsIgnoreCase("juzgado")) {
					asistencia.setJuzgado(new Long(comisaria_juzgado));
					asistencia.setJuzgadoIdInstitucion(new Integer(comisaria_juzgado_institucion));
				}
				else {
					asistencia.setComisaria(new Long(comisaria_juzgado));
					asistencia.setComisariaIdInstitucion(new Integer(comisaria_juzgado_institucion));
				}


// IMPORTANTE:
//				asistencia.setIdCalendarioGuardias()
// Estudiar:				
// 				asistencia.setFacturado()
//				asistencia.setIdFacturacion()
//				asistencia.setNumeroProcedimiento()
//				asistencia.setPagado()
// No importantes:				
//				asistencia.setContrarios()
//				asistencia.setDatosDefensaJuridica()
//				asistencia.setDelitosImputados()
//				asistencia.setFechaAnulacion()
//				asistencia.setFechaCierre()
//				asistencia.setIdPersonaRepresentante()
//				asistencia.setIncidencias()
//				asistencia.setMotivosAnulacion()

				if (accion.equalsIgnoreCase("--insertar--")){
					asistencia.setNumero(new Integer(asistenciaAdm.getNumeroAsistencia(idInstitucion, Integer.parseInt(anio))));
					asistenciaAdm.insert(asistencia);
				}
				else {
					asistencia.setAnio(new Integer(claveAnio));
					asistencia.setIdInstitucion(new Integer(claveIdInstitucion));
					asistencia.setNumero(new Integer(claveNumero));
					asistenciaAdm.updateDirect(asistencia);
				}
				ScsDelitosAsistenciaBean delitoAsistencia = null;
				if(idDelito!=null && !idDelito.equals("")){
					delitoAsistencia = new ScsDelitosAsistenciaBean();
					delitoAsistencia.setIdDelito(new Integer(idDelito));
					delitoAsistencia.setIdInstitucion(asistencia.getIdInstitucion());
					delitoAsistencia.setNumero(asistencia.getNumero());
					delitoAsistencia.setAnio(asistencia.getAnio());
				}
				ScsDelitosAsistenciaAdm delAsisAdm =  null;
				if (accion.equalsIgnoreCase("--insertar--")){
					//Insertamos el delito
					if(delitoAsistencia!=null){
						delAsisAdm =  new ScsDelitosAsistenciaAdm(this.getUserBean(request));
						delAsisAdm.insert(delitoAsistencia);
					}
					
				}else{
					delAsisAdm =  new ScsDelitosAsistenciaAdm(this.getUserBean(request));
					//Borramos los delitos que tuviera
					delAsisAdm.borrarDelitosAsitencia(asistencia.getIdInstitucion(), asistencia.getAnio(), asistencia.getNumero());
					//si tiene delito lo insertamos
					if(delitoAsistencia!=null){
						delAsisAdm =  new ScsDelitosAsistenciaAdm(this.getUserBean(request));
						delAsisAdm.insert(delitoAsistencia);
					}
					
					
				}
				
				ScsActuacionAsistenciaBean act = new ScsActuacionAsistenciaBean ();
				act.setAcuerdoExtrajudicial(new Integer(0));
				act.setAnio(asistencia.getAnio());
				act.setDescripcionBreve("("+UtilidadesString.getMensajeIdioma(this.getUserBean(request), "menu.justiciaGratuita.volantesExpres")+")");
				act.setDiaDespues("N");
				act.setFecha(fechaAsistencia);
				act.setFechaJustificacion(fechaJustificacion);
				act.setIdActuacion(new Long(1));
				act.setIdInstitucion(asistencia.getIdInstitucion());
				act.setIdTipoAsistencia(asistencia.getIdTipoAsistencia());
				act.setNumero(new Long(""+asistencia.getNumero()));
				act.setNumeroAsunto(asistencia.getNumeroDiligencia());
				act.setObservaciones(asistencia.getObservaciones());
				act.setValidada("1"); // validada si 

				if (f.getLugar().equalsIgnoreCase("juzgado")) {
					act.setIdJuzgado(new Integer(""+asistencia.getJuzgado()));
					act.setIdInstitucionJuzgado(new Long(""+asistencia.getJuzgadoIdInstitucion()));
					act.setIdTipoActuacion(new Integer(2));
				} 
				else {
					act.setIdComisaria(new Integer(""+asistencia.getComisaria()));
					act.setIdInstitucionComisaria(new Long(""+asistencia.getComisariaIdInstitucion()));
					act.setIdTipoActuacion(new Integer(1));
				}

//				 Estudiar:				

//				act.setAnulacion();

//				act.setFacturado();
//				act.setIdFacturacion();
//				act.setIdInstitucionPrision();
//				act.setIdPrision();
//				act.setLugar();
//				act.setObservacionesJustificacion();
//				act.setPagado();

				f.setSustitutoDe("");
				request.setAttribute("sustituto", f.getSustitutoDe());
				if (accion.equalsIgnoreCase("--insertar--")){
					actAdm.insert(act);
				}
				else {
					actAdm.updateDirect(act);
				}
			}
			tx.commit();
			request.setAttribute("fechaJustificacion", f.getFechaJustificacion());
		}
		catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		return exitoRefresco("messages.updated.success", request);
	}

	protected String insertarFila (ActionForm formulario, HttpServletRequest request) 
	{
		request.setAttribute("modoAnterior", "insertarFila");
		return this.obtenerDatosCabecera(formulario, request);
	}

	protected String obtenerDatosCabecera (ActionForm formulario, HttpServletRequest request) 
	{
		try {
		    ReadProperties rp3= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp3 = new ReadProperties("SIGA.properties");
			String tipoAsistenciaDefecto = rp3.returnProperty("codigo.general.scs_tipoasistencia.volanteExpres");
			request.setAttribute("tipoAsistenciaDefecto", tipoAsistenciaDefecto);
			UsrBean usrBean = this.getUserBean(request);
			GenParametrosAdm paramAdm = new GenParametrosAdm (usrBean);
			String delitos_VE = paramAdm.getValor (usrBean.getLocation (), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_DELITOS_VE, "");
			Boolean isDelitosVE = new Boolean((delitos_VE!=null && delitos_VE.equalsIgnoreCase(ClsConstants.DB_TRUE)));
			request.setAttribute("isDelitosVE", isDelitosVE);
					
			
			
			
			VolantesExpresForm f  = (VolantesExpresForm) formulario;
			request.setAttribute("fechaJustificacion", f.getFechaJustificacion());
			String letrado        = f.getLetrado();
			String diaGuardia     = f.getGuardiaDia();
			Integer idInstitucion = this.getIDInstitucion(request);
			String centroJuzgado = f.getLugar();
			String idGuardiaFORM = f.getGuardia();
			String idTurno = f.getTurno();
			String tipoAsistencia = f.getTipoAsistencia();
			String tipoAsistenciaColegio = f.getTipoAsistenciaColegio();
			String getSinAvisos = f.getSinAvisos();
			String getDesdeNuevo = f.getDesdeNuevo();
			String getSustitutoDe = f.getSustitutoDe();
			String getGuardiaDelSustituto = f.getGuardiaDelSustituto();
			
			if(f.getGuardia()!=null && !f.getGuardia().equals("")){
				if((letrado == null || letrado.equals("")) && diaGuardia != null && !diaGuardia.equals("")){
					request.setAttribute("diaGuardia", diaGuardia);
					request.setAttribute("centroJuzgado", f.getLugar());
					request.setAttribute("idGuardia", f.getGuardia());
					request.setAttribute("idTurno", f.getTurno());
					request.setAttribute("tipoAsistencia", f.getTipoAsistencia());
					request.setAttribute("tipoAsistenciaColegio", f.getTipoAsistenciaColegio());
					request.setAttribute("sinAvisos", f.getSinAvisos());
					request.setAttribute("desdeNuevo", f.getDesdeNuevo());
					request.setAttribute("asistencias", new Vector());
					
				}else if ( diaGuardia != null && !diaGuardia.equals("")) {
					
					CenPersonaAdm pAdm = new CenPersonaAdm(this.getUserBean(request)); 
					Hashtable p = pAdm.getPersonYnColegiado (letrado, idInstitucion);
					if (p != null) {
						request.setAttribute("ncolegiado",    p.get("NCOLEGIADO"));
						request.setAttribute("idpersona",     p.get("IDPERSONA"));
						request.setAttribute("nombreletrado", p.get("NOMBRE") + " " + p.get("APELLIDOS1") + " " + p.get("APELLIDOS2"));
					}
	
					request.setAttribute("diaGuardia", diaGuardia);
					request.setAttribute("centroJuzgado", f.getLugar());
					request.setAttribute("idGuardia", f.getGuardia());
					request.setAttribute("idTurno", f.getTurno());
					request.setAttribute("tipoAsistencia", f.getTipoAsistencia());
					request.setAttribute("tipoAsistenciaColegio", f.getTipoAsistenciaColegio());
					request.setAttribute("sinAvisos", f.getSinAvisos());
					request.setAttribute("desdeNuevo", f.getDesdeNuevo());
					request.setAttribute("sustituto", f.getSustitutoDe());
					request.setAttribute("guardiaDelSustituto", f.getGuardiaDelSustituto());
	
					diaGuardia = GstDate.getApplicationFormatDate("", diaGuardia);
					
					Hashtable h = new Hashtable();
					UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDPERSONA, letrado);
					UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_FECHAFIN, diaGuardia);
					UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDINSTITUCION, idInstitucion);
					
					
					if (f.getTurno() != null && !f.getTurno().equals("")) {
						UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDTURNO, f.getTurno().split(",")[1]);
					}
					if (f.getGuardia() != null && !f.getGuardia().equals("")) {
						UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDGUARDIA, f.getGuardia().split(",")[0]);
					}
					// Si es sustituto buscamos por su guardia
					if (f.getGuardiaDelSustituto() != null && !f.getGuardiaDelSustituto().equals("")) {
						UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDGUARDIA, f.getGuardiaDelSustituto());
					}
					
					ScsGuardiasColegiadoAdm adm = new ScsGuardiasColegiadoAdm (this.getUserBean(request));
					// Vector vGuardias = adm.select(h);
					Vector vGuardias = adm.selectParaVolantes(h);
					
					// Puede haber varias guardias, cojo los datos de la primera. Se avisara en jsp que hay varias
					
					if (vGuardias != null) {
						
						// No tiene guardias ese dia
						if (vGuardias.size() <= 0) {
							request.setAttribute("aviso", UtilidadesString.getMensajeIdioma(this.getUserBean(request), "gratuita.volantesExpres.mensaje.letradoSinGuardia"));
	
							// Buscamos los datos asociados a la guardia aunque no tenga para ese dia
							ScsAsistenciasAdm admAsistencias = new ScsAsistenciasAdm (this.getUserBean(request));
							Vector vAsistencias = admAsistencias.getAsistenciasVolantesExpres(idInstitucion, letrado, diaGuardia, new Integer(f.getTurno().split(",")[1]), 	new Integer(f.getGuardia().split(",")[0]),!f.getLugar().equals("centro"),f.getTipoAsistencia(),f.getTipoAsistenciaColegio());
							if (vAsistencias != null && vAsistencias.size() < 1) {
								return "inicio";
	//							request.setAttribute("centroJuzgado", "centro");
	//							request.setAttribute("idGuardia", "");
	//							request.setAttribute("idTurno", "");
	//							request.setAttribute("tipoAsistencia", "");
	//							request.setAttribute("tipoAsistenciaColegio", "");
							}
							else {
								request.setAttribute("asistencias", vAsistencias);
							}
							request.setAttribute("sustituto", "");
							request.setAttribute("guardiaDelSustituto", "");
							return "inicio";
						}
	
						// Tiene mas de una guardia, pero en la ventana aparecera seleccionado la primera guardia que encuentre. 
						if (vGuardias.size() > 1) {
							request.setAttribute("aviso", UtilidadesString.getMensajeIdioma(this.getUserBean(request), "gratuita.volantesExpres.mensaje.letradoVariasGuardias"));
						}
						
						// Tiene solo una guardia
						// Buscamos los datos asociados a la guardia
						ScsGuardiasColegiadoBean b = (ScsGuardiasColegiadoBean) vGuardias.get(0);
						
						// Verficamos si es una guardia de sustitucion
						Integer idGuardia = this.getGuardiaPadreDeSusticion (b, this.getUserBean(request));
						if (idGuardia == null)idGuardia = b.getIdGuardia();
						else {
							// es guardia de sustitucion
							if (f.getSustitutoDe() == null || f.getSustitutoDe().equals("") && vGuardias.size() == 1) {
								
								request.setAttribute("guardiaDelSustituto", ""+b.getIdGuardia());
								request.setAttribute("aviso", UtilidadesString.getMensajeIdioma(this.getUserBean(request), "gratuita.volantesExpres.mensaje.esLetradoSustituto"));
							}
						}
						
						ScsAsistenciasAdm admAsistencias = new ScsAsistenciasAdm (this.getUserBean(request));
						
						Vector vAsistencias = admAsistencias.getAsistenciasVolantesExpres(
								idInstitucion, letrado, diaGuardia, b.getIdTurno(), idGuardia,!f.getLugar().equals("centro"),f.getTipoAsistencia(),f.getTipoAsistenciaColegio());
						//Vamos a recorrer los registros para buscar los ejg asociados
						request.setAttribute("asistencias", vAsistencias);
						request.setAttribute("idGuardia", ""+idGuardia);
						request.setAttribute("idGuardiaSelec", ""+idGuardia);
						request.setAttribute("idTurnoSelec", b.getIdTurno().toString());
	
	//					request.setAttribute("idCalendarioGuardia", ""+b.getIdCalendarioGuardias());
	//					request.setAttribute("fechaInicio", b.getFechaInicio());
					}
				}
				
			}else{
			
			
				if((letrado == null || letrado.equals("")) && diaGuardia != null && !diaGuardia.equals("")){
					request.setAttribute("diaGuardia", diaGuardia);
					request.setAttribute("centroJuzgado", f.getLugar());
					request.setAttribute("idGuardia", f.getGuardia());
					request.setAttribute("idTurno", f.getTurno());
					request.setAttribute("tipoAsistencia", f.getTipoAsistencia());
					request.setAttribute("tipoAsistenciaColegio", f.getTipoAsistenciaColegio());
					request.setAttribute("sinAvisos", f.getSinAvisos());
					request.setAttribute("desdeNuevo", f.getDesdeNuevo());
					request.setAttribute("asistencias", new Vector());
					
				}else if ( diaGuardia != null && !diaGuardia.equals("")) {
					
					CenPersonaAdm pAdm = new CenPersonaAdm(this.getUserBean(request)); 
					Hashtable p = pAdm.getPersonYnColegiado (letrado, idInstitucion);
					if (p != null) {
						request.setAttribute("ncolegiado",    p.get("NCOLEGIADO"));
						request.setAttribute("idpersona",     p.get("IDPERSONA"));
						request.setAttribute("nombreletrado", p.get("NOMBRE") + " " + p.get("APELLIDOS1") + " " + p.get("APELLIDOS2"));
					}
	
					request.setAttribute("diaGuardia", diaGuardia);
					request.setAttribute("centroJuzgado", f.getLugar());
					request.setAttribute("idGuardia", f.getGuardia());
					request.setAttribute("idTurno", f.getTurno());
					request.setAttribute("tipoAsistencia", f.getTipoAsistencia());
					request.setAttribute("tipoAsistenciaColegio", f.getTipoAsistenciaColegio());
					request.setAttribute("sinAvisos", f.getSinAvisos());
					request.setAttribute("desdeNuevo", f.getDesdeNuevo());
					request.setAttribute("sustituto", f.getSustitutoDe());
					request.setAttribute("guardiaDelSustituto", f.getGuardiaDelSustituto());
					request.setAttribute("asistencias", new Vector());
					
					diaGuardia = GstDate.getApplicationFormatDate("", diaGuardia);
					Hashtable h = new Hashtable();
					UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDPERSONA, letrado);
					UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_FECHAFIN, diaGuardia);
					UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDINSTITUCION, idInstitucion);
					
					// Si es sustituto buscamos por su guardia
					if (f.getGuardiaDelSustituto() != null && !f.getGuardiaDelSustituto().equals("")) {
						UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDGUARDIA, f.getGuardiaDelSustituto());
					}
					
					ScsGuardiasColegiadoAdm adm = new ScsGuardiasColegiadoAdm (this.getUserBean(request));
					// Vector vGuardias = adm.select(h);
					Vector vGuardias = adm.selectParaVolantes(h);
					
					// Puede haber varias guardias, cojo los datos de la primera. Se avisara en jsp que hay varias
					
					if (vGuardias != null) {
						// No tiene guardias ese dia
						if (vGuardias.size() <= 0) {
							request.setAttribute("aviso", UtilidadesString.getMensajeIdioma(this.getUserBean(request), "gratuita.volantesExpres.mensaje.letradoSinGuardia"));
														
						}else if (vGuardias.size() > 1) {
							request.setAttribute("aviso", UtilidadesString.getMensajeIdioma(this.getUserBean(request), "gratuita.volantesExpres.mensaje.letradoVariasGuardias"));
						
						}
						
					}else{
						request.setAttribute("aviso", UtilidadesString.getMensajeIdioma(this.getUserBean(request), "gratuita.volantesExpres.mensaje.letradoSinGuardia"));
						
					}
					
					
					return "inicio";
					
				}
			}
		}
		catch (Exception e) {
			System.out.println("ERROR AQUI"+e.toString());
		}
		
		return "inicio";
	}
	
	private boolean esCampoVacio (String s) 
	{
		final String vacio = "--vacio--";
		if (s == null) return true;
		if (s.equals("")) return true;
		if (s.equalsIgnoreCase(vacio)) return true;
		return false;
	}


	private Integer getGuardiaPadreDeSusticion (ScsGuardiasColegiadoBean b, UsrBean usr) 
	{
		try {
			Hashtable h = new Hashtable ();
			UtilidadesHash.set(h, ScsGuardiasTurnoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(h, ScsGuardiasTurnoBean.C_IDTURNO, b.getIdTurno());
			UtilidadesHash.set(h, ScsGuardiasTurnoBean.C_IDGUARDIA, b.getIdGuardia());
			ScsGuardiasTurnoAdm adm = new ScsGuardiasTurnoAdm (usr);
			Vector v = adm.selectByPK(h);
			ScsGuardiasTurnoBean bb = (ScsGuardiasTurnoBean)v.get(0);
			return bb.getIdGuardiaSustitucion();
		}
		catch (Exception e) {
			return null;

		}
	}

	//--------------------------------------------------------------------------------------------
	// Saliente indica al letrado que va a ser sustituido y entrante el que va a sustituir al saliente
	//-----------------------------------------------------------------------------------------------
	
	private String aplicarSustitucion(String idInstitucion, String idTurno, 
			String idGuardia, String idPersonaSaliente, String idPersonaEntrante, 
			String fechaFin, UsrBean usr) throws Exception
	{
		try {

			fechaFin = GstDate.getApplicationFormatDate("", fechaFin);

			// Obtenemos el idCalendario
			Hashtable h = new Hashtable();
			UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDPERSONA, idPersonaSaliente);
			UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_FECHAFIN, fechaFin);
			UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDTURNO, idTurno);
			UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDGUARDIA, idGuardia);
			UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDINSTITUCION, idInstitucion);
			ScsGuardiasColegiadoAdm admAux = new ScsGuardiasColegiadoAdm (usr);
			Vector vGuardias = admAux.select(h);
			if (vGuardias != null && vGuardias.size() != 1) {
	            throw new ClsExceptions("La guardia seleccionada tiene varios calendarios asociados");
			}
			ScsGuardiasColegiadoBean b = (ScsGuardiasColegiadoBean) vGuardias.get(0);
			String idCalendarioGuardias = ""+b.getIdCalendarioGuardias();
			String fechaInicio = GstDate.getFormatedDateLong("", b.getFechaInicio()); 
			String salto = null; 			// No creamos salto
			String compensacion = null; 	// No creamos compensacion

			ScsGuardiasColegiadoAdm guardiasColegiadoAdm = new ScsGuardiasColegiadoAdm(usr);
			
			String mensaje = guardiasColegiadoAdm.validacionesSustitucionGuardia(usr,idInstitucion, idTurno, idGuardia, idCalendarioGuardias, fechaInicio,fechaFin,idPersonaEntrante, idPersonaSaliente);
			
			if(!mensaje.equalsIgnoreCase("OK")) {
				return mensaje;
			}
			else {
				guardiasColegiadoAdm.validarColegiadoEntrante(usr,idInstitucion, idTurno, idGuardia, idCalendarioGuardias, fechaInicio,fechaFin,idPersonaEntrante);
				guardiasColegiadoAdm.sustitucionLetradoGuardiaPuntual(usr, null, idInstitucion, idTurno,idGuardia,idCalendarioGuardias,idPersonaSaliente,fechaInicio,fechaFin,idPersonaEntrante, salto, compensacion,"si", null);
			}
		}
		catch (Exception e) {
			throw e; 
		}
		return null;
	}
	private void insertarGuardiaManual(String idInstitucion, String idTurno, 
		String idGuardia, String idPersonaEntrante, 
		String fechaFin, UsrBean usr)throws Exception{
		String idCalendarioGuardias = null;//miForm.getIdCalendarioGuardias();
		String idPersona =idPersonaEntrante;// miForm.getIdPersona();
		
		//Periodo:
		int indicePeriodo = 0;// Integer.parseInt(miForm.getIndicePeriodo());
		
		Hashtable h = new Hashtable();
		
		//Hacemos un recorrido por todas los calendarios que tenemos. 
		UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_FECHAFIN, GstDate.getApplicationFormatDate("", fechaFin));
		UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDTURNO, idTurno);
		UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDGUARDIA, idGuardia);
		UtilidadesHash.set (h, ScsGuardiasColegiadoBean.C_IDINSTITUCION, idInstitucion);
		ScsGuardiasColegiadoAdm admAux = new ScsGuardiasColegiadoAdm (usr);
		
		Vector vGuardias = admAux.select(h);
		
		if(vGuardias!=null &&vGuardias.size()>0){
			
			ScsGuardiasColegiadoBean b = (ScsGuardiasColegiadoBean) vGuardias.get(0);
			idCalendarioGuardias = ""+b.getIdCalendarioGuardias();
			//Calculo los periodos de guardias:
			CalendarioSJCS calendarioSJCS = new CalendarioSJCS
				(new Integer(idInstitucion), new Integer(idTurno),
				new Integer(idGuardia), new Integer(idCalendarioGuardias),
				usr);
			calendarioSJCS.calcularMatrizPeriodosDiasGuardia();
			
			//Nota: El array arrayPeriodosSJCS es un array periodos y cada periodo es un array de dias
			ArrayList arrayPeriodosSJCS = calendarioSJCS.getArrayPeriodosDiasGuardiaSJCS();
			
			//Obtenemos los dias a Agrupar
			List lDiasASeparar = calendarioSJCS.getDiasASeparar(new Integer(idInstitucion), new Integer(idTurno), new Integer(idGuardia) , usr);
			
			//Selecciono el periodo de la lista de periodos:
			String fechaInicioCalendario ="";
			String fechaFinCalendario ="";
			boolean findIt = false;
			for (int i = 0; i < arrayPeriodosSJCS.size(); i++) {
				ArrayList auxArrayPeriodoSeleccionado = (ArrayList)arrayPeriodosSJCS.get(i);
				for (int j = 0; j < auxArrayPeriodoSeleccionado.size(); j++) {
					String fecha = (String)auxArrayPeriodoSeleccionado.get(j);
					if(fecha.equals(fechaFin)){
						fechaInicioCalendario = (String)auxArrayPeriodoSeleccionado.get(0);
						fechaFinCalendario = (String)auxArrayPeriodoSeleccionado.get(auxArrayPeriodoSeleccionado.size()-1);
						findIt = true;
						break;
						
					}
					
				}
				if(findIt){
					indicePeriodo = i;
					break;
				}
			}
			ArrayList arrayPeriodoSeleccionado = (ArrayList)arrayPeriodosSJCS.get(indicePeriodo);
			
			//Creo el Letrado:
			LetradoGuardia letrado = new LetradoGuardia
				(new Long(idPersona), new Integer(idInstitucion),
				new Integer(idTurno), new Integer(idGuardia));			
			
			//VALIDACIONES:
			//Relleno una hash con los datos necesarios para validar:
			Hashtable miHash = new Hashtable ();
			miHash.put("IDPERSONA",idPersona);
			miHash.put("IDINSTITUCION",idInstitucion);
			miHash.put("IDCALENDARIOGUARDIAS",idCalendarioGuardias);
			miHash.put("IDTURNO",idTurno);
			miHash.put("IDGUARDIA",idGuardia);
			miHash.put("FECHAINICIO",fechaInicioCalendario); //Del periodo
			miHash.put("FECHAFIN",fechaFinCalendario); //Del periodo
			
			//	METER VALIDACIONES TODAVIA NO DEFINIDAS
	
				//INSERT (INICIO TRANSACCION)
			
			try {
				//Almaceno en BBDD la cabecera y las guardias colegiado para este letrado:
				calendarioSJCS.almacenarAsignacionGuardiaLetrado(letrado,arrayPeriodoSeleccionado,lDiasASeparar);
			} catch (Exception e) {
				throw e;
			}
	        
		}else{
			throw new SIGAException("gratuita.volantesExpres.mensaje.diaSinCalendarioGuardias");
			
		}

		
		
	}

}