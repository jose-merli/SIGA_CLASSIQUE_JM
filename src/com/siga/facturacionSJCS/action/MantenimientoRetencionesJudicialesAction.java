
// julio.vicente Creacion: 31-03-2005 

package com.siga.facturacionSJCS.action;

import java.util.Hashtable;

import javax.servlet.http.*;
import javax.transaction.*;

import org.apache.struts.action.*;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.*;
import com.siga.facturacionSJCS.form.*;
import com.siga.general.*;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;

import java.util.Vector;

/**
* Clase action form del caso de uso MANTENIMIENTO RETENCIONES JUDICIALES
* @author julio.vicente 31-03-2005
*/
public class MantenimientoRetencionesJudicialesAction extends MasterAction {

	public ActionForward executeInternal (ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;
		
		try { 
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();		
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = abrir(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("abrirListado")){
						// abrirAvanzadaConParametros
						mapDestino = abrirListado(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarListado")){
						// verFichaColegial
						mapDestino = buscarListado(mapping, miForm, request, response);
					} else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);
			
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacionSJCS"});
		}
	}


		
	/**
	 * Metodo que implementa el modo abrir. Con este metodo entramos a la primera pantalla del caso de uso.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		// Si vengo desde la ficha colegial
		if (mapping.getParameter() != null && mapping.getParameter().toUpperCase().contains(ClsConstants.PARAM_ESFICHACOLEGIAL.toUpperCase())) {
			return this.buscarPor(mapping, formulario, request,response);
		}

		return "inicio";
	}

	/**
	 * Metodo que implementa el modo editar. Pasa los parametros necesarios a la pagina de las pestanhas: <br>
	 * idInstitucion, idPagosJG y la accion.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		FcsRetencionesJudicialesAdm admBean =  new FcsRetencionesJudicialesAdm(this.getUserBean(request));
		MantenimientoRetencionesJudicialesForm miForm = (MantenimientoRetencionesJudicialesForm) formulario;
	
		
		Hashtable miHash = new Hashtable();
		Vector resultado = new Vector();
		String consulta = "";
		Hashtable codigos = new Hashtable();
		
		try {				
			
			miHash.put(FcsRetencionesJudicialesBean.C_IDRETENCION,ocultos.get(0));			
			miHash.put(FcsRetencionesJudicialesBean.C_IDINSTITUCION,usr.getLocation());
			
			// Hacemos la join de las tablas que necesitamos
			/*consulta = "select ret.*, col." + CenPersonaBean.C_NOMBRE + " || ' '  || col." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || col." + CenPersonaBean.C_APELLIDOS2 + " AS NOMBRE, decode(colegiado." + CenColegiadoBean.C_COMUNITARIO+",1,colegiado." + CenColegiadoBean.C_NCOMUNITARIO + ", colegiado." + CenColegiadoBean.C_NCOLEGIADO +") AS NCOLEGIADO "+ 
					   " from " + FcsRetencionesJudicialesBean.T_NOMBRETABLA + " ret, " + CenPersonaBean.T_NOMBRETABLA + " col, " + CenColegiadoBean.T_NOMBRETABLA + " colegiado where ret." + FcsRetencionesJudicialesBean.C_IDPERSONA + " = colegiado." + 
					   CenColegiadoBean.C_IDPERSONA + " and ret." + FcsRetencionesJudicialesBean.C_IDINSTITUCION + " = colegiado." + CenColegiadoBean.C_IDINSTITUCION + " and ret." + FcsRetencionesJudicialesBean.C_IDPERSONA + " = col." + CenPersonaBean.C_IDPERSONA;*/
			codigos.put(new Integer (1),ocultos.get(0));
			codigos.put(new Integer (2),usr.getLocation());
			consulta = "select ret.*, (select decode(colegiado.COMUNITARIO,  1, colegiado.NCOMUNITARIO, colegiado.NCOLEGIADO)"+
			         " from  CEN_COLEGIADO     colegiado   where  ret.IDPERSONA = colegiado.IDPERSONA  and ret.IDINSTITUCION = colegiado.IDINSTITUCION) AS NCOLEGIADO,"+
                     "  (select   col.NOMBRE || ' ' || col.APELLIDOS1 || ' ' || col.APELLIDOS2 from     CEN_PERSONA col"+
                     "   where col.idpersona=ret.idpersona ) AS NOMBRE"+ 
					 " from " + FcsRetencionesJudicialesBean.T_NOMBRETABLA + " ret"+ 
			         " where ret." + FcsRetencionesJudicialesBean.C_IDRETENCION + " = :1  and ret." + FcsRetencionesJudicialesBean.C_IDINSTITUCION + " = :2";		
			
			
			
			resultado = admBean.selectGenericoBind(consulta,codigos);			
						
		} catch (Exception e) {
			   throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		
		request.setAttribute("DATABACKUP", (Hashtable)resultado.get(0));
		request.setAttribute("APLICADARETENCION", ocultos.get(1).toString());
		request.setAttribute("accion","modificar");
		
		return "operar";
	}


	/**
	 * Metodo que implementa el modo ver. Pasa los parametros necesarios a la pagina de las pestanhas: <br>
	 * idInstitucion, idPagosJG y la accion.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		FcsRetencionesJudicialesAdm admBean =  new FcsRetencionesJudicialesAdm(this.getUserBean(request));
		MantenimientoRetencionesJudicialesForm miForm = (MantenimientoRetencionesJudicialesForm) formulario; 
		
		Hashtable miHash = new Hashtable();
		Vector resultado = new Vector();
		String consulta = "";
		Hashtable codigos = new Hashtable();
		
		try {				
			miHash.put(FcsRetencionesJudicialesBean.C_IDRETENCION,ocultos.get(0));			
			miHash.put(FcsRetencionesJudicialesBean.C_IDINSTITUCION,usr.getLocation());
			
			// Hacemos la join de las tablas que necesitamos
			/*consulta = "select ret.*, col." + CenPersonaBean.C_NOMBRE + " || ' '  || col." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || col." + CenPersonaBean.C_APELLIDOS2 + " AS NOMBRE, decode(colegiado." + CenColegiadoBean.C_COMUNITARIO+",1,colegiado." + CenColegiadoBean.C_NCOMUNITARIO + ", colegiado." + CenColegiadoBean.C_NCOLEGIADO +") AS NCOLEGIADO "+ 
					   " from " + FcsRetencionesJudicialesBean.T_NOMBRETABLA + " ret, " + CenPersonaBean.T_NOMBRETABLA + " col, " + CenColegiadoBean.T_NOMBRETABLA + " colegiado where ret." + FcsRetencionesJudicialesBean.C_IDPERSONA + " = colegiado." + 
					   CenColegiadoBean.C_IDPERSONA + " and ret." + FcsRetencionesJudicialesBean.C_IDINSTITUCION + " = colegiado." + CenColegiadoBean.C_IDINSTITUCION + " and ret." + FcsRetencionesJudicialesBean.C_IDPERSONA + " = col." + CenPersonaBean.C_IDPERSONA;*/
			codigos.put(new Integer (1),ocultos.get(0));
			codigos.put(new Integer (2),usr.getLocation());
			consulta = "select ret.*, (select decode(colegiado.COMUNITARIO,  1, colegiado.NCOMUNITARIO, colegiado.NCOLEGIADO)"+
			         " from  CEN_COLEGIADO     colegiado   where  ret.IDPERSONA = colegiado.IDPERSONA  and ret.IDINSTITUCION = colegiado.IDINSTITUCION) AS NCOLEGIADO,"+
                     "  (select   col.NOMBRE || ' ' || col.APELLIDOS1 || ' ' || col.APELLIDOS2 from     CEN_PERSONA col"+
                     "   where col.idpersona=ret.idpersona ) AS NOMBRE"+ 
					 " from " + FcsRetencionesJudicialesBean.T_NOMBRETABLA + " ret"+ 
			         " where ret." + FcsRetencionesJudicialesBean.C_IDRETENCION + " = :1  and ret." + FcsRetencionesJudicialesBean.C_IDINSTITUCION + " = :2";		
			
			
			
			resultado = admBean.selectGenericoBind(consulta,codigos);			
			
			
		} catch (Exception e) {
			   throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		
		request.setAttribute("DATABACKUP", (Hashtable)resultado.get(0));
		request.setAttribute("accion","ver");
		
		return "operar";
	}


	/**
	 * Metodo que implementa el modo insertar.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		MantenimientoRetencionesJudicialesForm miForm = (MantenimientoRetencionesJudicialesForm) formulario;		
		FcsRetencionesJudicialesAdm admBean =  new FcsRetencionesJudicialesAdm(this.getUserBean(request));
		Hashtable miHash = new Hashtable();
		String forward="";
		
		try {
			miHash = admBean.prepararInsert(miForm.getDatos());
			tx=usr.getTransaction();
			tx.begin();
			// RGG 23/06/2008
			miHash.put(FcsRetencionesJudicialesBean.C_CONTABILIZADO,"0");
			boolean checkEsDeTurno  = UtilidadesString.stringToBoolean(miForm.getCheckEsDeTurno());
			if (checkEsDeTurno){
				UtilidadesHash.set(miHash,FcsRetencionesJudicialesBean.C_ESDETURNO,ClsConstants.DB_TRUE);
			}else{
				UtilidadesHash.set(miHash,FcsRetencionesJudicialesBean.C_ESDETURNO,ClsConstants.DB_FALSE);
			}
			if (admBean.insert(miHash))
	        {
				tx.commit();
				forward = "exitoModal";	            
	        }	        
	        else forward ="exito";            
	        
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		if (forward.equalsIgnoreCase("exitoModal")) return exitoModal("messages.inserted.success",request);
		else return exito("messages.inserted.error",request);
	}

	/**
	 * Metodo que implementa el modo nuevo. Pasa los parametros necesarios a la pagina de las pestanhas: <br>
	 * idInstitucion, idPagosJG y la accion. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		request.setAttribute("accion","insertar");	
		
	     return "operar";
	}

	/**
	 * Método que implementa el modo buscarPor. Realiza la busqueda de los pagos teniendo en cuenta la fecha <br>
	 * última del estado del pago.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "";
		Vector resultado = new Vector();
		try {
		 	// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
			// casting del formulario
			MantenimientoRetencionesJudicialesForm miFormulario = (MantenimientoRetencionesJudicialesForm)formulario;
			FcsRetencionesJudicialesAdm admRetenciones = new FcsRetencionesJudicialesAdm(this.getUserBean(request));
			
			Hashtable miHash = miFormulario.getDatos();
			

			// Primera parte de la consulta (las tablas que necesitamos)
			String consulta = " SELECT RETENCIONES." + FcsRetencionesJudicialesBean.C_IDINSTITUCION + ", RETENCIONES."+ FcsRetencionesJudicialesBean.C_IDPERSONA + ", RETENCIONES."+ FcsRetencionesJudicialesBean.C_IDRETENCION +
							  ", (SELECT DECODE(COL." + CenColegiadoBean.C_COMUNITARIO +",1,COL." + CenColegiadoBean.C_NCOMUNITARIO +",COL." + CenColegiadoBean.C_NCOLEGIADO +") FROM " + CenColegiadoBean.T_NOMBRETABLA + " COL WHERE COL." + CenColegiadoBean.C_IDPERSONA + " = RETENCIONES." + FcsRetencionesJudicialesBean.C_IDPERSONA + " AND" + 
							  " COL." + CenColegiadoBean.C_IDINSTITUCION + " = RETENCIONES." + FcsRetencionesJudicialesBean.C_IDINSTITUCION + ") AS " + CenColegiadoBean.C_NCOLEGIADO +
							  ", DECODE(RETENCIONES."+ FcsRetencionesJudicialesBean.C_TIPORETENCION + ", '" + ClsConstants.TIPO_RETENCION_PORCENTAJE + "',f_siga_getrecurso_etiqueta('FactSJCS.mantRetencionesJ.literal.porcentual',"+user.getLanguage()+"),'" + ClsConstants.TIPO_RETENCION_IMPORTEFIJO + "',f_siga_getrecurso_etiqueta('FactSJCS.mantRetencionesJ.literal.importeFijo',"+user.getLanguage()+"),'" + ClsConstants.TIPO_RETENCION_LEC + "',f_siga_getrecurso_etiqueta('FactSJCS.mantRetencionesJ.literal.tramosLEC',"+user.getLanguage()+"))" +
							  " AS " + FcsRetencionesJudicialesBean.C_TIPORETENCION + ", (SELECT (PER." + CenPersonaBean.C_NOMBRE + " || ' ' || PER." + CenPersonaBean.C_APELLIDOS1  + " || ' ' || PER." + CenPersonaBean.C_APELLIDOS2 + ") FROM " + 
							  CenPersonaBean.T_NOMBRETABLA + " PER" + " WHERE PER." + CenPersonaBean.C_IDPERSONA +  " = RETENCIONES." + CenPersonaBean.C_IDPERSONA + ") AS " + CenPersonaBean.C_NOMBRE + ", (SELECT " + FcsDestinatariosRetencionesBean.C_NOMBRE + 
							  " FROM " + FcsDestinatariosRetencionesBean.T_NOMBRETABLA + " DES" + " WHERE DES." + FcsDestinatariosRetencionesBean.C_IDINSTITUCION + " = RETENCIONES." + FcsRetencionesJudicialesBean.C_IDINSTITUCION +
							  " AND DES." + FcsDestinatariosRetencionesBean.C_IDDESTINATARIO + " = RETENCIONES." + FcsRetencionesJudicialesBean.C_IDDESTINATARIO + ") AS NOMBREDESTINATARIO" + ", RETENCIONES." + 
							  FcsRetencionesJudicialesBean.C_FECHAINICIO + ", RETENCIONES." + FcsRetencionesJudicialesBean.C_FECHAFIN + ", RETENCIONES."+FcsRetencionesJudicialesBean.C_IMPORTE+", F_SIGA_APLICADARETENCION(retenciones."+FcsRetencionesJudicialesBean.C_IDINSTITUCION+",retenciones.idretencion) RETENCIONAPLICADA FROM " + FcsRetencionesJudicialesBean.T_NOMBRETABLA + " RETENCIONES, " + FcsDestinatariosRetencionesBean.T_NOMBRETABLA + " o";
			
			// Segunda parte de la consulta (con los criterios de búsqueda seleccionados)
			consulta += " WHERE RETENCIONES." + FcsRetencionesJudicialesBean.C_IDINSTITUCION + " = " + user.getLocation();
			consulta += " AND RETENCIONES." + FcsRetencionesJudicialesBean.C_IDINSTITUCION + " = O." + FcsDestinatariosRetencionesBean.C_IDINSTITUCION;
			consulta += " AND RETENCIONES." + FcsRetencionesJudicialesBean.C_IDDESTINATARIO + " = O." + FcsDestinatariosRetencionesBean.C_IDDESTINATARIO;
			boolean checkEsDeTurno  = UtilidadesString.stringToBoolean(miFormulario.getCheckEsDeTurno());
			if (checkEsDeTurno){
				
				consulta+=" AND RETENCIONES."+FcsRetencionesJudicialesBean.C_ESDETURNO+"="+ClsConstants.DB_TRUE;
			}
				if ((miHash.containsKey(CenColegiadoBean.C_NCOLEGIADO)) && (!miHash.get(CenColegiadoBean.C_NCOLEGIADO).toString().equals("")))
					
					
					consulta += " AND (SELECT LTRIM(decode(COL.comunitario, 1, COL.nCOmunitario, COL.NCOLEGIADO), '0') FROM " + CenColegiadoBean.T_NOMBRETABLA + " COL WHERE COL." + CenColegiadoBean.C_IDPERSONA + " = RETENCIONES." + FcsRetencionesJudicialesBean.C_IDPERSONA + " AND" + 
					  			" COL." + CenColegiadoBean.C_IDINSTITUCION + " = RETENCIONES." + FcsRetencionesJudicialesBean.C_IDINSTITUCION + ") = LTRIM('" +  miHash.get(CenColegiadoBean.C_NCOLEGIADO)+"','0')";	
			
			
			if ((miHash.containsKey(FcsRetencionesJudicialesBean.C_TIPORETENCION)) && (!miHash.get(FcsRetencionesJudicialesBean.C_TIPORETENCION).toString().equals(""))) 
				consulta += " AND RETENCIONES." + FcsRetencionesJudicialesBean.C_TIPORETENCION + " = '" + miHash.get(FcsRetencionesJudicialesBean.C_TIPORETENCION) + "'";
			if ((miHash.containsKey(FcsRetencionesJudicialesBean.C_IDDESTINATARIO)) && (!miHash.get(FcsRetencionesJudicialesBean.C_IDDESTINATARIO).toString().equals(""))) 
				consulta += " AND RETENCIONES." + FcsRetencionesJudicialesBean.C_IDDESTINATARIO + " = " + miHash.get(FcsRetencionesJudicialesBean.C_IDDESTINATARIO);

			
			// Si vengo desde la ficha colegial
			if (mapping.getParameter() != null && mapping.getParameter().toUpperCase().contains(ClsConstants.PARAM_ESFICHACOLEGIAL.toUpperCase())) {
				String idPersona = (String) request.getSession().getAttribute("idPersonaPestanha");
				consulta += " AND nvl(RETENCIONES."+FcsRetencionesJudicialesBean.C_IDPERSONA+", "+idPersona+") = "+idPersona+" ";
			}
			
			String fDesde = UtilidadesHash.getString(miHash,"FECHANOTIFICACIONDESDE"); 
			String fHasta = UtilidadesHash.getString(miHash,"FECHANOTIFICACIONHASTA");
			if ((fDesde != null && !fDesde.trim().equals("")) || (fHasta != null && !fHasta.trim().equals(""))) {
				if (fDesde!=null && !fDesde.equals(""))
					fDesde = GstDate.getApplicationFormatDate("", fDesde); 
				if (fHasta!=null && !fHasta.equals(""))
					fHasta = GstDate.getApplicationFormatDate("", fHasta);
				consulta += " AND " + GstDate.dateBetweenDesdeAndHasta(FcsRetencionesJudicialesBean.C_FECHAINICIO, fDesde, fHasta);
			}
			boolean checkHistorico  = UtilidadesString.stringToBoolean(UtilidadesHash.getString(miHash,"CHECKHISTORICO"));
			if (!checkHistorico){
				consulta += " AND (" + FcsRetencionesJudicialesBean.C_FECHAFIN+" is null "+
				            " or trunc("+ FcsRetencionesJudicialesBean.C_FECHAFIN+")>=trunc(sysdate))" +
				            " and (( " + FcsRetencionesJudicialesBean.C_IMPORTE + 
				            " + NVL ((SELECT SUM (c." + FcsCobrosRetencionJudicialBean.C_IMPORTERETENIDO+")" +
				            " from " + FcsCobrosRetencionJudicialBean.T_NOMBRETABLA + " c" + 
				            " where c." + FcsCobrosRetencionJudicialBean.C_IDINSTITUCION + " = retenciones." + FcsRetencionesJudicialesBean.C_IDINSTITUCION +
				            " and c." + FcsCobrosRetencionJudicialBean.C_IDPERSONA + " = retenciones." + FcsRetencionesJudicialesBean.C_IDPERSONA +
				            " and c." + FcsCobrosRetencionJudicialBean.C_IDRETENCION + " = retenciones." + FcsRetencionesJudicialesBean.C_IDRETENCION + "), 0)" + 
				            " ) > 0" + 
				            " or " + FcsRetencionesJudicialesBean.C_TIPORETENCION + " = 'P'" + 
				            " or (" + FcsRetencionesJudicialesBean.C_TIPORETENCION + " = 'L'" + 
				            " and " + FcsRetencionesJudicialesBean.C_IMPORTE + " is null))";
				
			}
			
			// Y por último se anhaden los criterios de ordenación
			consulta += " ORDER BY o." + FcsDestinatariosRetencionesBean.C_ORDEN + ", " + FcsRetencionesJudicialesBean.C_FECHAINICIO + ", " + FcsRetencionesJudicialesBean.C_FECHAALTA;
			
			resultado = admRetenciones.selectGenerico(consulta);	
			
			request.setAttribute("resultado",resultado);
			String accion = (String)request.getSession().getAttribute("accion");
			if (accion == null)
				accion = "";
			request.setAttribute("accion", accion);
			
			destino="resultado";
			
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
		 return destino;
	}

	
	/** 
	 * Método que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		FcsRetencionesJudicialesAdm admBean =  new FcsRetencionesJudicialesAdm(this.getUserBean(request));
		MantenimientoRetencionesJudicialesForm miForm = (MantenimientoRetencionesJudicialesForm) formulario; 
		
		Hashtable miHash = new Hashtable();
		Hashtable hashBkp = new Hashtable();
		tx=usr.getTransaction();
		String retencionAplicada="";
		
		try {				
			
			
			miHash.put(FcsRetencionesJudicialesBean.C_IDRETENCION,ocultos.get(0));	
			retencionAplicada=ocultos.get(1).toString();
			miHash.put(FcsRetencionesJudicialesBean.C_IDINSTITUCION,usr.getLocation());		
			Vector resultado= admBean.selectPorClave(miHash);
			hashBkp = ((FcsRetencionesJudicialesBean)resultado.get(0)).getOriginalHash();	
			
			tx.begin();
			if (retencionAplicada.equals("0")){
			admBean.delete(miHash);		    
			}else{
				miHash.put(FcsRetencionesJudicialesBean.C_FECHAFIN,"SYSDATE");
				admBean.update(miHash,hashBkp);
					
				 
				
			}
			
			tx.commit();
		} catch (Exception e) {
			
			throwExcp("messages.deleted.error",new String[] {"modulo.facturacionSJCS"},e,tx);
			
		}
		
		
		return exitoRefresco("messages.deleted.success",request);
	}
	
	/**
	 * Rellena un hash con los valores recogidos del formulario y los modifica en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
				
		MantenimientoRetencionesJudicialesForm miForm = (MantenimientoRetencionesJudicialesForm) formulario;		
		FcsRetencionesJudicialesAdm admBean =  new FcsRetencionesJudicialesAdm(this.getUserBean(request));
		String forward = "";
		Hashtable miHash = new Hashtable();			
		
		try {	
			
			miHash = miForm.getDatos();			
			miHash.put(FcsRetencionesJudicialesBean.C_FECHAINICIO,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaInicio()));
			if (miHash.get(FcsRetencionesJudicialesBean.C_FECHAFIN)!=null && !miHash.get(FcsRetencionesJudicialesBean.C_FECHAFIN).equals("") ){
				miHash.put(FcsRetencionesJudicialesBean.C_FECHAFIN,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaFin()));
			}
			
			boolean checkEsDeTurno  = UtilidadesString.stringToBoolean(miForm.getCheckEsDeTurno());
			if (checkEsDeTurno){
				UtilidadesHash.set(miHash, FcsRetencionesJudicialesBean.C_ESDETURNO, ClsConstants.DB_TRUE);
			}else{
				UtilidadesHash.set(miHash, FcsRetencionesJudicialesBean.C_ESDETURNO, ClsConstants.DB_FALSE);
			}
			
			Hashtable hashBkp = new Hashtable();
			Vector resultado= admBean.selectPorClave(miHash);
			hashBkp = ((FcsRetencionesJudicialesBean)resultado.get(0)).getOriginalHash();	
			
			tx=usr.getTransaction();
			tx.begin();
			if (admBean.update(miHash, hashBkp)) {
				tx.commit();
				forward = "exitoModal";
			}			
			else forward = "exito";
			
		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		if (forward.equalsIgnoreCase("exitoModal")) 
			return exitoModal("messages.updated.success",request);
		else 
			return exito("messages.updated.error",request);
	}

	/**
	 * Método que implementa el modo buscarListado. Realiza la busqueda de las retenciones judiciales
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarListado(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "";
		Vector resultado = new Vector();
		try {
		 	// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
			// casting del formulario
			MantenimientoRetencionesJudicialesForm miFormulario = (MantenimientoRetencionesJudicialesForm)formulario;
			FcsRetencionesJudicialesAdm admRetenciones = new FcsRetencionesJudicialesAdm(this.getUserBean(request));
				
			Hashtable miHash = miFormulario.getDatos();

			resultado = admRetenciones.listadoRetenciones(miHash);	
			
			request.setAttribute("resultado",resultado);
			
			destino="resultadoListado";
			
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
		 return destino;
	}

	/**
	 * Metodo que implementa el modo abrirListado. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirListado (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		return "inicioListado";
	}


	
}
