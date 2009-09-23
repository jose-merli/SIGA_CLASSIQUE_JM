package com.siga.facturacion.action;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.FacAbonoAdm;
import com.siga.beans.FacAbonoBean;
import com.siga.beans.FacAbonoIncluidoEnDisqueteAdm;
import com.siga.beans.FacBancoInstitucionAdm;
import com.siga.beans.FacBancoInstitucionBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacFacturaIncluidaEnDisqueteAdm;
import com.siga.beans.FacFacturaIncluidaEnDisqueteBean;
import com.siga.beans.FacLineaDevoluDisqBancoAdm;
import com.siga.beans.FacLineaDevoluDisqBancoBean;
import com.siga.beans.FacPagoAbonoEfectivoAdm;
import com.siga.beans.FacPagosPorCajaAdm;
import com.siga.beans.FacPagosPorCajaBean;
import com.siga.beans.FacRegistroFichContaAdm;
import com.siga.beans.FacRegistroFichContaBean;
import com.siga.beans.FcsMovimientosVariosAdm;
import com.siga.beans.FcsMovimientosVariosBean;
import com.siga.beans.FcsPagosJGAdm;
import com.siga.beans.FcsPagosJGBean;
import com.siga.beans.FcsRetencionesJudicialesAdm;
import com.siga.beans.FcsRetencionesJudicialesBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.GenParametrosBean;
import com.siga.beans.PysAnticipoLetradoAdm;
import com.siga.beans.PysAnticipoLetradoBean;
import com.siga.beans.PysProductosInstitucionAdm;
import com.siga.beans.PysProductosInstitucionBean;
import com.siga.beans.PysServiciosInstitucionAdm;
import com.siga.beans.PysServiciosInstitucionBean;
import com.siga.facturacion.form.ContabilidadForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.servlets.SIGASvlProcesoAutomaticoRapido;

/**
 * @author carlos.vidal
 * @since 3/2/2005
 */

public class MantContabilidadAction extends MasterAction 
{

	

	
	
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
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
	public ActionForward executeInternal (ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
			{
		String mapDestino = "exception";
		MasterForm miForm = null;
		
		try 
		{ 
			do 
			{
				miForm = (MasterForm) formulario;
				
				if (miForm != null) 
				{
					String accion = miForm.getModo();
					
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir"))
					{
						mapDestino = abrir(mapping, miForm, request, response);
						break;
					} 
					
					else if (accion.equalsIgnoreCase("download"))
					{
						mapDestino = download(mapping, miForm, request, response);
					} 
					else if (accion.equalsIgnoreCase("ejecutar"))
					{
						mapDestino = ejecutar(mapping, miForm, request, response);
					} 
					
					else 
					{
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);
			
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) { 
				throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) { 
			es.printStackTrace();
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"});
		} 
			}
	
	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException
			{
		return "inicio";
			}
	
	/** 
	 *  Funcion que atiende la accion abrirAvanzada
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		return null;
	}
	
	/** 
	 *  Funcion que atiende la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		ContabilidadForm miForm = (ContabilidadForm) formulario;
		// Se realiza una select con idinstitucion, fechadesde y fechahasta
		String fechaDesde = miForm.getFechaDesde();
		String fechaHasta = miForm.getFechaHasta();
		String idEstado = request.getParameter("buscarIdEstado");
		
		int contador = 0;             
        Hashtable codigos = new Hashtable();
		// Actualizamos la fechaHasta al final del dia
		if (!fechaHasta.equals("")) {
			fechaHasta = fechaHasta+" 23:59:59";
		}
		contador++;
		codigos.put(new Integer(contador),usr.getLocation());
		String where =	" WHERE IDINSTITUCION =:"+contador;
		
		if (idEstado!=null && !idEstado.trim().equals("") && !idEstado.trim().equals("0")) {
		    contador++;
			codigos.put(new Integer(contador),idEstado);
			where +=	" AND ESTADO =:"+contador;
			    
		}
		
		/*if(!fechaDesde.equals("") && !fechaHasta.equals(""))
			where+=" AND FECHADESDE >= TO_DATE('"+GstDate.getApplicationFormatDate(usr.getLanguage(),fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND FECHAHASTA <= TO_DATE('"+GstDate.getApplicationFormatDate(usr.getLanguage(),fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
		else if(!fechaDesde.equals(""))
			where+=" AND FECHADESDE >= TO_DATE('"+GstDate.getApplicationFormatDate(usr.getLanguage(),fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
		else if(!fechaHasta.equals(""))
			where+=" AND FECHAHASTA <= TO_DATE('"+GstDate.getApplicationFormatDate(usr.getLanguage(),fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
		*/
		String fDesdeInc = fechaDesde; 
		   String fHastaInc = fechaHasta;
			if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
				
				if (!fDesdeInc.equals(""))
					fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
				if (!fHastaInc.equals(""))
					fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
				Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("FECHADESDE", fDesdeInc, fHastaInc,contador,codigos);
				
				contador=new Integer(vCondicion.get(0).toString()).intValue();
				where +=" AND " + vCondicion.get(1) ;
			}	
		FacRegistroFichContaAdm adm = new FacRegistroFichContaAdm(this.getUserBean(request));
		try{
			Vector vResultado=(Vector)adm.selectBind(where,codigos);
			request.setAttribute("resultado",vResultado);
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"});
		} 
		return "listado";
	}
	
	
	/** 
	 *  Funcion que atiende la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, 
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response)throws ClsExceptions,SIGAException  {
		return "editar";
	}
	
	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException  {
		return "editar";
	}
	
	/** 
	 *  Funcion que atiende la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {
		return "nuevo";
	}
	
	/** 
	 *  Funcion que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String insertar(	ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {
		
		
	String retorno = null;
	UserTransaction tx = null;
	try {
		
		// Generamos el fichero de contabilidad
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		tx = usr.getTransaction();
		FacRegistroFichContaAdm admContabilidad = new FacRegistroFichContaAdm(usr); 
		ContabilidadForm miForm = (ContabilidadForm) formulario;
		String conf = miForm.getEsConfirmacion();
		boolean existeContabilidad = false;
		if(conf == null || !conf.equalsIgnoreCase("1"))
		{
			existeContabilidad = admContabilidad.existeContabilidad(miForm.getFechaDesde(),miForm.getFechaHasta(),usr.getLocation());
		}
		if(!existeContabilidad)
		{
		    // RGG aquí hay que hacer la llamada al servicio automático
		    tx.begin();
		    // se crea la contablidad en estado programado lista para ejecutarse por el proceso automático.
		    admContabilidad.crearContabilidad(miForm.getFechaDesde(),miForm.getFechaHasta());
		    tx.commit();

		    // Notificación
			SIGASvlProcesoAutomaticoRapido.NotificarAhora(SIGASvlProcesoAutomaticoRapido.procesoRapido);

		    request.setAttribute("mensaje","messages.inserted.contablidad");
			request.setAttribute("modal","1");
			retorno = "exito";
		}
		else
		{
			retorno = "nuevo";
			request.setAttribute("confirmar","ok");
			request.setAttribute("fechaDesde",miForm.getFechaDesde());
			request.setAttribute("fechaHasta",miForm.getFechaHasta());
		}
	}
	catch (Exception e) {
		throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx);
	}
	return retorno;
	} 
	
	/** 
	 *  Funcion que atiende la accion ejecutar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String ejecutar(	ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {
		
		
	String retorno = null;
	UserTransaction tx = null;
	try {
		
		// Generamos el fichero de contabilidad
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		tx = usr.getTransaction();
		FacRegistroFichContaAdm admContabilidad = new FacRegistroFichContaAdm(usr); 
		String idCont = request.getParameter("idContabilidad");
		Hashtable ht = new Hashtable();
		ht.put("IDINSTITUCION",usr.getLocation());
		ht.put("IDCONTABILIDAD",idCont);
		Vector v = admContabilidad.selectByPK(ht);
		FacRegistroFichContaBean bean = null;
		if (v!=null && v.size()>0) {
		    bean = (FacRegistroFichContaBean) v.get(0);
		}

		// RGG aquí hay que hacer la llamada al servicio automático
	    tx.begin();
	    
	    // le cambio el estado a programado
	    bean.setEstado(new Integer(FacRegistroFichContaBean.ESTADO_PROGRAMADO));
	    if (!admContabilidad.updateDirect(bean)) {
	        throw new ClsExceptions("Error al actualizar el estado de la contabildiad. programado. "+admContabilidad.getError());
	    }
	    tx.commit();

	    // Notificación
		SIGASvlProcesoAutomaticoRapido.NotificarAhora(SIGASvlProcesoAutomaticoRapido.procesoRapido);

		request.setAttribute("mensaje","messages.inserted.contablidad");
		request.setAttribute("refresco","1");
		retorno = "exito";
	}
	catch (Exception e) {
		throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx);
	}
	return retorno;
	} 
	
	/** 
	 *  Funcion que atiende la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {
		return "refresh";
	}
	
	/** 
	 *  Funcion que atiende la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException  {
		return "exito";
	}
	
	/** 
	 *  Funcion que atiende la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException  {
		return "listado";
	}
	/**
	 * Se va a realizar la descarga del fichero (DOWNLOAD).
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
	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String ficheroName = "";
		String sRutaFisicaJava = "";
		UsrBean user = null;
		try {
			user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			ContabilidadForm form = (ContabilidadForm)formulario;
			ficheroName = form.getFichero();
			ReadProperties rp = new ReadProperties("SIGA.properties");
			sRutaFisicaJava = rp.returnProperty("contabilidad.directorioFisicoContabilidad");
			File fich = new File(sRutaFisicaJava+File.separator+user.getLocation()+File.separator+ficheroName);
			if(fich==null || !fich.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			request.setAttribute("nombreFichero",fich.getName());
			request.setAttribute("rutaFichero",fich.getPath()); // Ruta del fichero SIN EL NOMBRE DEL FICHERO
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion.previsionesFacturacion"},e,null); 
		}
		return "descargaFichero";
	}
}