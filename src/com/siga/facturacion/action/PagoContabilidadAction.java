package com.siga.facturacion.action;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.FacPagosPorCajaAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.facturacion.form.GestionarFacturaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.PagoTarjeta;
import com.siga.general.SIGAException;

/**
 * @author carlos.vidal
 * @since 3/2/2005
 */

public class PagoContabilidadAction extends MasterAction {

	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
					return mapping.findForward(mapDestino);
				}
				
				String accion = miForm.getModo();
				
				// La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);	
					
				}else if (accion.equalsIgnoreCase("resultTPV")){
					mapDestino = resultTPV(mapping, miForm, request, response);
				} 
				else {
					return super.executeInternal(mapping, formulario, request, response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 				
			{ 				
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }			    
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}			
			
		 }catch (SIGAException es) { 
		    throw es; 
		 }catch (Exception e) { 
		    throw new SIGAException("messages.general.error",e,new String[] {"modulo.productos"}); 
		 } 
		 
		   return mapping.findForward(mapDestino);   	
	}
	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'abrir'
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
		try
		{
			String importe = "";
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;
			// Se obtiene el idinstitucion y el idFactura
			Integer idInstitucion 	= miForm.getIdInstitucion();
			String  idFactura 		= miForm.getIdFactura();
			String  numeroFactura	= miForm.getNumeroFactura();
			// Obtenemos el siguiente idcontabilidad
			FacPagosPorCajaAdm facPagosPorCajaAdm = new FacPagosPorCajaAdm(this.getUserBean(request));
			Integer idPagoPorCaja	= facPagosPorCajaAdm.getNuevoID(idInstitucion,idFactura);
			// Obtenemos el importe pendiente de cobrar.
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idInstitucion);
		    codigos.put(new Integer(2),idFactura);
		    String sql = "SELECT IMPTOTALPORPAGAR AS IMPORTE FROM FAC_FACTURA WHERE IDINSTITUCION=:1 AND IDFACTURA=:2";
		    //String sql = "SELECT PKG_SIGA_TOTALESFACTURA.PENDIENTEPORPAGAR("+idInstitucion+","+idFactura+") AS IMPORTE from DUAL";
			Vector vResultado=(Vector)facPagosPorCajaAdm.ejecutaSelectBind(sql,codigos);
			if(vResultado != null && vResultado.size()>0)
				importe = (String)((Hashtable)vResultado.get(0)).get("IMPORTE");
			request.setAttribute("idInstitucion",idInstitucion);
			request.setAttribute("idFactura"	,idFactura);
			request.setAttribute("idPagoPorCaja",idPagoPorCaja);
			request.setAttribute("numeroFactura",numeroFactura);
			// Calculamos el importe pendiente por cobrar.
			// Redondeamos el importe a 2 decimales
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setDecimalSeparator('.');
			DecimalFormat dfEuro = new DecimalFormat("###0.00",dfs);
			String imp = dfEuro.format(Double.valueOf(importe));
			request.setAttribute("importe"		,imp);
			return "abrir";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"});
		} 
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		return "abrirAvanzada";
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
		return "ver";
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
		// Obtenemos los datos del pago y hacemos la llamada al jsp productos/pagoTPV.jsp
		// Datos a recuperar: Tarjeta, Caducidad, importe, idpagoporcaja, idfactura, idinstitucion
		try
		{
			UsrBean usr 				= (UsrBean)request.getSession().getAttribute("USRBEAN");
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;
			String tarjeta 				= miForm.getTarjeta();
			String caducidad 			= miForm.getCaducidad();
			Double importe 				= miForm.getDatosPagosCajaImportePendiente();
			String idPagoPorCaja		= miForm.getIdPagoPorCaja();
			String idFactura 			= miForm.getIdFactura();
			Integer idInstitucion 		= miForm.getIdInstitucion();
			String fecha 				= miForm.getFecha();
			String numeroFactura		= miForm.getNumeroFactura();
			//Obtenemos la descripcion.
			String where = null;
			/*
			GenRecursosAdm genRecursos = new GenRecursosAdm(Integer.valueOf(usr.getUserName()));
			where = " WHERE IDRECURSO = 'facturacion.pagoContabilidad.literal.pagoPorTarjeta' AND IDLENGUAJE = '1'"; // Ya veremos si se quiere en multidioma
			Vector vRecurso = (Vector)genRecursos.select(where);
 			String descripcion = ((GenRecursosBean) vRecurso.get(0)).getDescripcion();*/
 			
			String descripcion = UtilidadesString.getMensajeIdioma(usr,"facturacion.pagoContabilidad.literal.pagoPorTarjeta");
 			
			// Cargamos en el request los valores
			// Generamos la operacion: 2+idinstitucion+idfactura+idpagoporcaja+time
			String idInst = String.valueOf(idInstitucion);
			String idFactAux = idFactura;
			String idPagoAux = idPagoPorCaja;
			while(idInst.length()<4) idInst = "0"+idInst;
			while(idFactAux.length()<10) idFactAux = "0"+idFactAux;
			while(idPagoAux.length()<3) idPagoAux = "0"+idPagoAux;
			String operacion = "2"+idInstitucion+idFactAux+idPagoAux+new Date().getTime();
		    request.setAttribute("OPERACION",operacion);
		    // Modif. Carlos
			// Redondeamos el importe a 2 decimales
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setDecimalSeparator('.');
			DecimalFormat dfEuro = new DecimalFormat("###0.00",dfs);
			String impString = dfEuro.format(importe);
		    //String impString = String.valueOf(importe);
			// Fin Modif. Carlos
		    impString = impString.substring(0,impString.indexOf("."))+impString.substring(impString.indexOf(".")+1);		    
			request.setAttribute("IMPORTE",impString); //parte entera+decimal 
		    request.setAttribute("PAN",tarjeta);
		    request.setAttribute("CADUCIDAD",caducidad); //FORMATO AÑOMES    
		    request.setAttribute("EXPONENTE","2"); //2   FIJO
		    request.setAttribute("MONEDA",ClsConstants.EURO); //978 FIJO
		    request.setAttribute("DESCRIPCION",descripcion+numeroFactura); //Texto que poneis vosotros y saldra en la pagina de CECA con los datos de la compra 
			/* Obtenemos los siguientes datos 
			ClsConstants.PASSWORD_FIRMA 
			ClsConstants.MERCHANTID
			ClsConstants.ACQUIREBIN
			ClsConstants.TERMINALID
			ClsConstants.URL_TPV
			*/       
            //Consulto los datos del comercio en base de datos y los almaceno en el request:
			where = "";
			GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
			String urlTPV = parametrosAdm.getValor(idInstitucion.toString(),"PYS",ClsConstants.URL_TPV,null);
			String merchantId = parametrosAdm.getValor(idInstitucion.toString(),"PYS",ClsConstants.MERCHANTID,null);
			String acquireBin = parametrosAdm.getValor(idInstitucion.toString(),"PYS",ClsConstants.ACQUIREBIN,null);
			String terminalId = parametrosAdm.getValor(idInstitucion.toString(),"PYS",ClsConstants.TERMINALID,null);
			String passwordFirma = parametrosAdm.getValor(idInstitucion.toString(),"PYS",ClsConstants.PASSWORD_FIRMA,null);

/*			
            where  = "WHERE "+GenParametrosBean.C_IDINSTITUCION+"="+idInstitucion;
            where += " AND "+GenParametrosBean.C_PARAMETRO+"='"+ClsConstants.URL_TPV+"'";
            String urlTPV = ((GenParametrosBean)(parametrosAdm.select(where).get(0))).getValor();
            request.setAttribute(ClsConstants.URL_TPV,urlTPV);
            
            where  = "WHERE "+GenParametrosBean.C_IDINSTITUCION+"="+idInstitucion;
            where += " AND "+GenParametrosBean.C_PARAMETRO+"='"+ClsConstants.MERCHANTID+"'";
            String merchantId = ((GenParametrosBean)(parametrosAdm.select(where).get(0))).getValor();
            request.setAttribute(ClsConstants.MERCHANTID,merchantId);

            where  = "WHERE "+GenParametrosBean.C_IDINSTITUCION+"="+idInstitucion;
		    where += " AND "+GenParametrosBean.C_PARAMETRO+"='"+ClsConstants.ACQUIREBIN+"'";
		    String acquireBin = ((GenParametrosBean)(parametrosAdm.select(where).get(0))).getValor();
		    request.setAttribute(ClsConstants.ACQUIREBIN,acquireBin);

		    where  = "WHERE "+GenParametrosBean.C_IDINSTITUCION+"="+idInstitucion;
		    where += " AND "+GenParametrosBean.C_PARAMETRO+"='"+ClsConstants.TERMINALID+"'";
		    String terminalId = ((GenParametrosBean)(parametrosAdm.select(where).get(0))).getValor();
		    request.setAttribute(ClsConstants.TERMINALID,terminalId);

		    where  = "WHERE "+GenParametrosBean.C_IDINSTITUCION+"="+idInstitucion;
		    where += " AND "+GenParametrosBean.C_PARAMETRO+"='"+ClsConstants.PASSWORD_FIRMA+"'";
		    String passwordFirma = ((GenParametrosBean)(parametrosAdm.select(where).get(0))).getValor();
		    request.setAttribute(ClsConstants.PASSWORD_FIRMA,passwordFirma);
*/

			// Modificado MAV 7/7/05 Pasar parametros que se habían comentado erroneamente antes
			request.setAttribute(ClsConstants.URL_TPV,urlTPV);
            request.setAttribute(ClsConstants.MERCHANTID,merchantId);
		    request.setAttribute(ClsConstants.ACQUIREBIN,acquireBin);
		    request.setAttribute(ClsConstants.TERMINALID,terminalId);
		    request.setAttribute(ClsConstants.PASSWORD_FIRMA,passwordFirma);
			// Fin modificacion
			
		    //Calculo la ruta del dominio del servidor:
		    StringBuffer ruta = request.getRequestURL();
		    String sRuta = ruta.substring(0,ruta.lastIndexOf(("/"))+1); 
		    request.setAttribute("URLOK",sRuta+"FAC_PagosFacturaPorTarjeta.do?modo=resultTPV&resultado=OK");
		    request.setAttribute("URLKO",sRuta+"FAC_PagosFacturaPorTarjeta.do?modo=resultTPV&resultado=ERROR");               String urlOK = (String)request.getAttribute("URLOK"); //Action con un modo y un parametro resultado=OK por ejemplo
		    // Guardamos en el contexto los datos del pago
		    PagoTarjeta pagoTarjeta = new PagoTarjeta();
		    pagoTarjeta.setIdInstitucion(String.valueOf(idInstitucion));
		    pagoTarjeta.setIdFactura(idFactura);
		    pagoTarjeta.setIdPagoPorCaja(idPagoPorCaja);
		    pagoTarjeta.setImporte(String.valueOf(importe));
		    pagoTarjeta.setFecha(fecha);
		    pagoTarjeta.setNumeroFactura(numeroFactura);
		    pagoTarjeta.setOperacion(operacion);
		    pagoTarjeta.setUsrBean(usr);
		    pagoTarjeta.setTarjeta(tarjeta);
		    pagoTarjeta.setCaducidad(caducidad);
		    pagoTarjeta.setDescripcion(descripcion+numeroFactura);
            this.servlet.getServletContext().setAttribute(operacion,pagoTarjeta);
            // Guardo en la session la operacion
            request.getSession().setAttribute("pagoTarjeta",pagoTarjeta);
			return "nuevo";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"});
		} 
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
		return "insertar";
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
		return "modificar";
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
			
        return "borrar";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException  {
	    
        return "buscarPor";
	}
	
	/** 
	 * Atiende la accion resultTPV. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action o error en caso de no completar con exito.
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String resultTPV(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String modo = "comprobantePago";
		UsrBean user = null;		
		String operacion=null, operacionOK=null;
		String error = "NO";
		
		try {
			user=(UsrBean)request.getSession().getAttribute("USRBEAN");							
			//Chequeo si operacionOK vale OK o ERROR:
			operacionOK = request.getParameter("resultado");
			//Obtenemos el pagoTarjeta
			PagoTarjeta pagoTarjeta = new PagoTarjeta();
			pagoTarjeta = (PagoTarjeta)request.getSession().getAttribute("pagoTarjeta");				
			if (operacionOK==null || operacionOK.equals("ERROR")) error = "SI";
			//Chequeo si existe el numero de operacion
			else if (pagoTarjeta==null) 				error = "SI";
			//Metemos los datos en el request.
			request.setAttribute("TARJETA",pagoTarjeta.getTarjeta());
			request.setAttribute("CADUCIDAD",pagoTarjeta.getCaducidad());
			request.setAttribute("IMPORTE",pagoTarjeta.getImporte());
			request.setAttribute("FECHA",pagoTarjeta.getFecha());
			request.setAttribute("NUMEROFACTURA",pagoTarjeta.getNumeroFactura());
			request.setAttribute("ERROR",error);
			request.setAttribute("OPERACION",pagoTarjeta.getOperacion());
			//Eliminamos de sesion la operacion
			request.getSession().removeAttribute("operacion");
			//Mete
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		} 	
		return modo;	
	}
	
	
}