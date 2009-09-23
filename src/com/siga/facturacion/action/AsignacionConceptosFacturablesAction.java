/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 */

/**
 * <p>Clase que gestiona las series de facturación que se utilizan como base para
 * posteriores programaciones de facturación y definición de previsiones de facturación.</p>
 */

package com.siga.facturacion.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmContadorAdm;
import com.siga.beans.CenGruposClienteBean;
import com.siga.beans.CenGruposCriteriosBean;
import com.siga.beans.FacClienIncluidoEnSerieFacturAdm;
import com.siga.beans.FacFacturacionProgramadaAdm;
import com.siga.beans.FacFacturacionProgramadaBean;
import com.siga.beans.FacGrupCritIncluidosEnSerieAdm;
import com.siga.beans.FacGrupCritIncluidosEnSerieBean;
import com.siga.beans.FacPrevisionFacturacionAdm;
import com.siga.beans.FacPrevisionFacturacionBean;
import com.siga.beans.FacSerieFacturacionAdm;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.beans.FacTipoCliIncluidoEnSerieFacAdm;
import com.siga.beans.FacTipoCliIncluidoEnSerieFacBean;
import com.siga.beans.FacTiposProduIncluEnFactuAdm;
import com.siga.beans.FacTiposProduIncluEnFactuBean;
import com.siga.beans.FacTiposServInclsEnFactAdm;
import com.siga.beans.FacTiposServInclsEnFactBean;
import com.siga.beans.PysTipoServiciosBean;
import com.siga.beans.PysTiposProductosBean;
import com.siga.facturacion.form.AsignacionConceptosFacturablesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class AsignacionConceptosFacturablesAction extends MasterAction
{	
	/**
	 * Es el metodo inicial que se ejecuta al entrar a la pantalla de busqueda.
	 * Limpia la sesion de los datos del formulario.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try 
		{ 
		    // Borramos las tablas hash: DATOSFORMULARIO, DATABACKUP
			request.getSession().removeAttribute("DATOSFORMULARIO");
			request.getSession().removeAttribute("DATABACKUP");
		
			//Borramos los atributos pasados por sesión
			request.getSession().removeAttribute("idSerieFacturacion");
			request.getSession().removeAttribute("editable");
			request.getSession().removeAttribute("accion");
		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		}
		
		return "inicio";
	}
	
	  /**
	   * Ejecuta un sentencia SELECT en la Base de Datos para las series 
	   * de facturación asociadas a los campos de la búsqueda.
	   * 
	   * @param  mapping - Mapeo de los struts
	   * @param  formulario -  Action Form asociado a este Action
	   * @param  request - objeto llamada HTTP 
	   * @param  response - objeto respuesta HTTP
	   * 
	   * @return String que indicará la siguiente acción a llevar a cabo.
	   * 
	   * @exception  ClsExceptions  En cualquier caso de error
	   */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
			
		try 
		{
			// Borramos las tablas hash: DATOSFORMULARIO, DATABACKUP
			request.getSession().removeAttribute("DATOSFORMULARIO");
				
			AsignacionConceptosFacturablesForm formFact = (AsignacionConceptosFacturablesForm) formulario;
			FacSerieFacturacionAdm admFac = new FacSerieFacturacionAdm(getUserBean(request));
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
			
			String nombreAbreviado = formFact.getNombreAbreviado();
			String descripcion = formFact.getDescripcion();
			String tipoProducto = formFact.getTipoProducto();
			String tipoServicio = formFact.getTipoServicio();
			String grupoClienteFijo = formFact.getGrupoClienteFijo();
			String grupoClientesDinamico = formFact.getGrupoClientesDinamico();
			
			String where = 	" where ";
			
			where += FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDINSTITUCION+"="+idInstitucion +
					 " AND (" + FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_TIPOSERIE+"='G' OR "+FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_TIPOSERIE+" IS NULL) ";
			
			if (nombreAbreviado!=null && !nombreAbreviado.trim().equals("")) {
				where += " and "+ComodinBusquedas.prepararSentenciaCompleta(nombreAbreviado.trim(),FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_NOMBREABREVIADO);
			}
			
			if (descripcion!=null && !descripcion.trim().equals("")) {
				where += " and "+ComodinBusquedas.prepararSentenciaCompleta(descripcion.trim(),FacSerieFacturacionBean.T_NOMBRETABLA+"."+FacSerieFacturacionBean.C_DESCRIPCION);
			}
			
			if (tipoProducto!=null && !tipoProducto.trim().equals("")) {
				where += " and "+FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDINSTITUCION+"="+FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+"."+FacTiposProduIncluEnFactuBean.C_IDINSTITUCION+
					 	" and "+FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDSERIEFACTURACION+"="+FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+"."+FacTiposProduIncluEnFactuBean.C_IDSERIEFACTURACION+
						" and "+PysTiposProductosBean.T_NOMBRETABLA+"."+ PysTiposProductosBean.C_IDTIPOPRODUCTO+"="+FacTiposProduIncluEnFactuBean.T_NOMBRETABLA+"."+FacTiposProduIncluEnFactuBean.C_IDTIPOPRODUCTO+
						" and "+ComodinBusquedas.prepararSentenciaCompleta(tipoProducto.trim(),UtilidadesMultidioma.getCampoMultidiomaSimple(PysTiposProductosBean.T_NOMBRETABLA+"."+ PysTiposProductosBean.C_DESCRIPCION,user.getLanguage()) );
			}
			
			if (tipoServicio!=null && !tipoServicio.trim().equals("")) {
				where += " and "+FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDINSTITUCION+"="+FacTiposServInclsEnFactBean.T_NOMBRETABLA+"."+FacTiposServInclsEnFactBean.C_IDINSTITUCION+
					 	" and "+FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDSERIEFACTURACION+"="+FacTiposServInclsEnFactBean.T_NOMBRETABLA+"."+FacTiposServInclsEnFactBean.C_IDSERIEFACTURACION+
						" and "+PysTipoServiciosBean.T_NOMBRETABLA+"."+ PysTipoServiciosBean.C_IDTIPOSERVICIOS+"="+FacTiposServInclsEnFactBean.T_NOMBRETABLA+"."+FacTiposServInclsEnFactBean.C_IDTIPOSERVICIOS+
						" and "+ComodinBusquedas.prepararSentenciaCompleta(tipoServicio.trim(),UtilidadesMultidioma.getCampoMultidiomaSimple(PysTipoServiciosBean.T_NOMBRETABLA+"."+ PysTipoServiciosBean.C_DESCRIPCION,user.getLanguage()));
			}
			
			if (grupoClienteFijo!=null && !grupoClienteFijo.trim().equals("")) {
				where += " and "+FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDINSTITUCION+"="+FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+"."+FacTipoCliIncluidoEnSerieFacBean.C_IDINSTITUCION+
					 	" and "+FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDSERIEFACTURACION+"="+FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+"."+FacTipoCliIncluidoEnSerieFacBean.C_IDSERIEFACTURACION+
						" and "+CenGruposClienteBean.T_NOMBRETABLA+"."+ CenGruposClienteBean.C_IDGRUPO+"="+FacTipoCliIncluidoEnSerieFacBean.T_NOMBRETABLA+"."+FacTipoCliIncluidoEnSerieFacBean.C_IDGRUPO+
						" and "+ComodinBusquedas.prepararSentenciaCompleta(grupoClienteFijo.trim(),CenGruposClienteBean.T_NOMBRETABLA+"."+ CenGruposClienteBean.C_NOMBRE);
			}
			
			if (grupoClientesDinamico!=null && !grupoClientesDinamico.trim().equals("")) {
				where += " and "+FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDINSTITUCION+"="+FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+"."+FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION+
					 	" and "+FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDSERIEFACTURACION+"="+FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+"."+FacGrupCritIncluidosEnSerieBean.C_IDSERIEFACTURACION+
						" and "+CenGruposCriteriosBean.T_NOMBRETABLA+"."+ CenGruposCriteriosBean.C_IDINSTITUCION+"="+FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+"."+FacGrupCritIncluidosEnSerieBean.C_IDINSTITUCION+
						" and "+CenGruposCriteriosBean.T_NOMBRETABLA+"."+ CenGruposCriteriosBean.C_IDGRUPOSCRITERIOS+"="+FacGrupCritIncluidosEnSerieBean.T_NOMBRETABLA+"."+FacGrupCritIncluidosEnSerieBean.C_IDGRUPOSCRITERIOS+
						" and "+ComodinBusquedas.prepararSentenciaCompleta(grupoClientesDinamico.trim(),CenGruposCriteriosBean.T_NOMBRETABLA+"."+ CenGruposCriteriosBean.C_NOMBRE);
			}
			
			Vector datosTab = admFac.selectTabla(tipoProducto, tipoServicio, grupoClienteFijo, grupoClientesDinamico, where);
			request.setAttribute("datosTab", datosTab);
			
			// Guardamos en una tabla hash los datos de la busqueda
			// para cuando volvamos de las pestanhas, tener los datos 
			// actualizados.
			Hashtable hashFormulario = new Hashtable();
			hashFormulario.put("NOMBREABREVIADO",nombreAbreviado);
			hashFormulario.put("DESCRIPCION",descripcion);
			hashFormulario.put("TIPOPRODUCTO",tipoProducto);
			hashFormulario.put("TIPOSERVICIO",tipoServicio);
			hashFormulario.put("GRUPOCLIENTEFIJO",grupoClienteFijo);
			hashFormulario.put("GRUPOCLIENTESDINAMICO",grupoClientesDinamico);
			hashFormulario.put("INICIARBUSQUEDA","SI");
			request.getSession().setAttribute("DATOSFORMULARIO",hashFormulario);
		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		}
		  
		return "success";
	}

	/**
	 * Se va a mostrar la pantalla de mantenimiento en modo modificación.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 * 
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try 
		{
			String accion = "editar";
			request.setAttribute("accion", accion);
		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		}
			
		return mostrarRegistro(mapping, formulario, request, response, true);	
	}

	
	/** 
	 *  Funcion que atiende la accion abrirConParametros
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * 
	 * @return  String  Destino del action  
	 * 
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirConParametros (ActionMapping mapping, 		
										  	 MasterForm formulario, 
											 HttpServletRequest request, 
											 HttpServletResponse response) throws SIGAException {

		try	{
			String accion = "editar";
			request.setAttribute("accion", accion);

			AsignacionConceptosFacturablesForm miForm = (AsignacionConceptosFacturablesForm)formulario;

			String idInstitucion = miForm.getIdInstitucion().toString();
			String idSerieFacturacion = miForm.getIdSerieFacturacion().toString();
		
		
			FacSerieFacturacionAdm admSerieFac = new FacSerieFacturacionAdm(this.getUserBean(request));
					
			
			String sWhere = " where ";
			sWhere += FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDSERIEFACTURACION+"="+idSerieFacturacion+
				  	" and "+
					FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDINSTITUCION+"="+idInstitucion;
		
			Vector datosSerie = admSerieFac.select(sWhere);
			if (!datosSerie.isEmpty()) {
				FacSerieFacturacionBean beanSerie = (FacSerieFacturacionBean)datosSerie.get(0);				
		
				// Almacenamos en sesion el registro de la serie de facturación
				Hashtable backupSerFac = new Hashtable();
				backupSerFac.put("IDINSTITUCION",idInstitucion);
				backupSerFac.put("IDSERIEFACTURACION",idSerieFacturacion);
				backupSerFac.put("IDPLANTILLA", beanSerie.getIdPlantilla());
				backupSerFac.put("DESCRIPCION", beanSerie.getDescripcion());
				backupSerFac.put("NOMBREABREVIADO", beanSerie.getNombreAbreviado());
				request.getSession().setAttribute("DATABACKUP",backupSerFac);
			}
			
			request.setAttribute("datosSerie", datosSerie);
			request.setAttribute("editable", "1");
			request.setAttribute("idSerieFacturacion", idSerieFacturacion);
		} catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		}
		return "pestanasFacturacion";
	}	
	
	/**
	 * Se va a mostrar la pantalla de mantenimiento en modo consulta.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 * 
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try 
		{
			String accion = "ver";
			request.setAttribute("accion", accion);
		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		}
		
		return mostrarRegistro(mapping, formulario, request, response, false);
	}

	  /**
	   * Se va a mostrar la pantalla de mantenimiento en modo inserción.
	   *   
	   * @param  mapping - Mapeo de los struts
	   * @param  formulario -  Action Form asociado a este Action
	   * @param  request - objeto llamada HTTP 
	   * @param  response - objeto respuesta HTTP
	   * 
	   * @return  String que indicará la siguiente acción a llevar a cabo
	   *   
	   * @exception  ClsExceptions  En cualquier caso de error
	   */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try 
		{
			request.setAttribute("editable", "1");
			String accion = "nuevo";
			request.setAttribute("accion", accion);
		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		}
				
		return "pestanasFacturacion";
	}


	/**
	 * Rellena un hash con los valores recogidos del formulario y los borra de la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 * 
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try
		{
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			String idInstitucion = user.getLocation();
		
			AsignacionConceptosFacturablesForm form = (AsignacionConceptosFacturablesForm)formulario;
			Vector vOcultos = form.getDatosTablaOcultos(0);	
			String idSerieFacturacion = (String)vOcultos.elementAt(0);
			
			FacPrevisionFacturacionAdm admPrev =  new FacPrevisionFacturacionAdm(this.getUserBean(request));
			String where = " Where ";
			where += FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+ FacPrevisionFacturacionBean.C_IDINSTITUCION+"="+idInstitucion+
				 	 " and "+
					 FacPrevisionFacturacionBean.T_NOMBRETABLA+"."+ FacPrevisionFacturacionBean.C_IDSERIEFACTURACION+"="+idSerieFacturacion;
			Vector datosPrev = admPrev.select(where);
			
			FacFacturacionProgramadaAdm admProg =  new FacFacturacionProgramadaAdm(this.getUserBean(request));
			String where1 = " Where ";
			where1 += FacFacturacionProgramadaBean.T_NOMBRETABLA+"."+ FacFacturacionProgramadaBean.C_IDINSTITUCION+"="+idInstitucion+
				 	  " and "+
				 	  FacFacturacionProgramadaBean.T_NOMBRETABLA+"."+ FacFacturacionProgramadaBean.C_IDSERIEFACTURACION+"="+idSerieFacturacion;
			Vector datosProg = admProg.select(where1);
			
			if ((datosPrev==null || datosPrev.size()==0) && (datosProg==null || datosProg.size()==0))
			{
				UserTransaction tx = null;
		
				Hashtable miHash = new Hashtable();
				try {				
					FacSerieFacturacionAdm admSerieFac =  new FacSerieFacturacionAdm(this.getUserBean(request));
					AdmContadorAdm admContador=  new AdmContadorAdm(this.getUserBean(request));
					FacTiposProduIncluEnFactuAdm admTProd =  new FacTiposProduIncluEnFactuAdm(this.getUserBean(request));
					FacTiposServInclsEnFactAdm admTServ =  new FacTiposServInclsEnFactAdm(this.getUserBean(request));
					FacClienIncluidoEnSerieFacturAdm admDInd =  new FacClienIncluidoEnSerieFacturAdm(this.getUserBean(request));
					FacTipoCliIncluidoEnSerieFacAdm admGFij =  new FacTipoCliIncluidoEnSerieFacAdm(this.getUserBean(request));
					FacGrupCritIncluidosEnSerieAdm admGDin =  new FacGrupCritIncluidosEnSerieAdm(this.getUserBean(request));
					
					miHash.put(FacSerieFacturacionBean.C_IDINSTITUCION,idInstitucion);
					miHash.put(FacSerieFacturacionBean.C_IDSERIEFACTURACION,idSerieFacturacion);
					
					Vector vSerie = admSerieFac.selectByPK(miHash);
					String idContador = null;
					if (vSerie!=null && vSerie.size()>0) {
						FacSerieFacturacionBean b = (FacSerieFacturacionBean) vSerie.get(0);
						idContador = b.getIdContador();
					}
					
					
					tx = user.getTransaction();
					tx.begin();
			
					String claves[] = new String[2];
					claves[0] = FacSerieFacturacionBean.C_IDINSTITUCION;
					claves[1] = FacSerieFacturacionBean.C_IDSERIEFACTURACION;
					admTProd.deleteDirect(miHash, claves);
					admTServ.deleteDirect(miHash, claves);
					admDInd.deleteDirect(miHash, claves);
					admGFij.deleteDirect(miHash, claves);
					admGDin.deleteDirect(miHash, claves);
					


					if (admSerieFac.delete(miHash))
					{

						// RGG 10/09/2007 BORRO EL CONTADOR SI NO ES GENERAL
						// Borramos el contador que estaba relacionado si no es general ni tiene otras relaciones
						admContador.borrarContadorLibre(idInstitucion, idContador);
						

						request.setAttribute("mensaje","messages.deleted.success");
						tx.commit();
						
						
					}
					else
					{
						request.setAttribute("mensaje","error.messages.deleted");
						tx.rollback();
					}
				} catch (ClsExceptions e){
					try { 
						if (tx!=null) tx.rollback(); 
					} 
					catch (Exception exx) {
						com.atos.utils.ClsLogging.writeFileLogError("ClsException: Error al ejecutar 'rollback' en 'borrar' de la clase AsignacionConceptosFacturablesAction", exx,3);
					}
					com.atos.utils.ClsLogging.writeFileLogError("ClsException: Error al ejecutar 'borrar' de la clase AsignacionConceptosFacturablesAction", e,3);
					throw e;
				} catch (Exception e) {
					try { 
						if (tx!=null) tx.rollback(); 
					} 
					catch (Exception exx) {
						com.atos.utils.ClsLogging.writeFileLogError("ClsException: Error al ejecutar 'rollback' en 'borrar' de la clase AsignacionConceptosFacturablesAction", exx,3);
					}
					com.atos.utils.ClsLogging.writeFileLogError("ClsException: Error al ejecutar 'borrar' de la clase AsignacionConceptosFacturablesAction", e,3);
					throw new ClsExceptions(e,"EXCEPCION EN BORRAR");
				}
			}
			else
			{
				if (datosPrev!=null && datosPrev.size()!=0)
					request.setAttribute("mensaje","facturacion.resultadoSeriesFacturacion.literal.mensajeExistenPrevisiones");
				else
					if (datosProg!=null && datosProg.size()!=0)
						request.setAttribute("mensaje","facturacion.resultadoSeriesFacturacion.literal.mensajeExistenProgramadas");
			}
		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		}
		
		// Refrescamos la tabla
		return "exito";
	}

	/**
	 * Se va a mostrar la pantalla de búsqueda.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 * 
	 * @exception  ClsExceptions  En cualquier caso de error 
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		// Volvemos a la pantalla de búsqueda sin actualizar los datos
		// AsignacionConceptosFacturablesForm form = (AsignacionConceptosFacturablesForm)formulario;
		// form.reset(mapping, request);
		
		// Volvemos a la pantalla de búsqueda actualizando los datos
		return "inicio";
	}
	
	/**
	 * Se va a mostrar la pantalla de mantenimiento con los datos recogidos 
	 * en la búsqueda.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * @param boolean bEditable: true o false.
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 * 
	 * @exception  ClsExceptions  En cualquier caso de error 
	 */
	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws SIGAException
	{
		try
		{
			AsignacionConceptosFacturablesForm form = (AsignacionConceptosFacturablesForm)formulario;
		
			FacSerieFacturacionAdm admSerieFac = new FacSerieFacturacionAdm(this.getUserBean(request));
					
			Vector vOcultos = form.getDatosTablaOcultos(0);
			
			String idSerieFacturacion = (String)vOcultos.elementAt(0);
			String idInstitucion = ((UsrBean) request.getSession().getAttribute("USRBEAN")).getLocation();
			
			String sWhere = " where ";
			sWhere += FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDSERIEFACTURACION+"="+idSerieFacturacion+
				  	" and "+
					FacSerieFacturacionBean.T_NOMBRETABLA+"."+ FacSerieFacturacionBean.C_IDINSTITUCION+"="+idInstitucion;
		
			Vector datosSerie = admSerieFac.select(sWhere);
		
			// Almacenamos en sesion el registro de la serie de facturación
			Hashtable backupSerFac = new Hashtable();
			backupSerFac.put("IDINSTITUCION",idInstitucion);
			backupSerFac.put("IDSERIEFACTURACION",idSerieFacturacion);
			backupSerFac.put("IDPLANTILLA",vOcultos.get(2));
			backupSerFac.put("DESCRIPCION",UtilidadesString.andToComa((String)vOcultos.get(4)));
			backupSerFac.put("NOMBREABREVIADO",UtilidadesString.andToComa((String)vOcultos.get(5)));
			request.getSession().setAttribute("DATABACKUP",backupSerFac);
			
			request.setAttribute("datosSerie", datosSerie);
			request.setAttribute("editable", bEditable ? "1" : "0");
			request.setAttribute("idSerieFacturacion", idSerieFacturacion);
		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.facturacion.asignacionConceptos"},e,null); 
		}
			
		return "pestanasFacturacion";
	}
}