/*
 * VERSIONES:
 * 
 * daniel.casla	- 4-11-2004 - Inicio
 * miguel.villegas - 25-11-2004 - Continuacion
 *	
 */

/**
 * Clase action para el mantenimiento de productos.<br/>
 * Gestiona la edicion, borrado, consulta y mantenimiento de los productos
 * @version modiifcado por david.sanchezp para incluir el concepto.  
 */

package com.siga.productos.action;


import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmContadorAdm;
import com.siga.beans.AdmContadorBean;
import com.siga.beans.CerSolicitudCertificadosBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysFormaPagoAdm;
import com.siga.beans.PysFormaPagoProductoAdm;
import com.siga.beans.PysFormaPagoProductoBean;
import com.siga.beans.PysProductosInstitucionAdm;
import com.siga.beans.PysProductosInstitucionBean;
import com.siga.beans.PysProductosSolicitadosAdm;
import com.siga.beans.PysProductosSolicitadosBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.productos.form.MantenimientoProductosForm;


public class MantenimientoProductosAction extends MasterAction {

	/** 
	 *  Funcion que atiende la accion abrir.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String result="abrir";
		
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			
			// Paso de parametros empleando request
			request.setAttribute("IDINSTITUCION", idInstitucion);				
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		} 

			
	return result;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		return "abrirAvanzada";
	}

	/** 
	 *  Funcion que implementa la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="editar";
		Vector ocultos=new Vector();
		Vector infoProd=new Vector();
		Vector pagoInt=new Vector();
		Vector pagoComun=new Vector();
		Vector pagoSec=new Vector();		
		
		try {
			
			MantenimientoProductosForm form = (MantenimientoProductosForm) formulario;
			PysProductosInstitucionAdm admin=new PysProductosInstitucionAdm(this.getUserBean(request));
			Object remitente=(Object)"modificar";
			request.setAttribute("modelo",remitente);
			
			// Mostrar valores del formulario en MantenimientoProductos (posible traslado a editar o abrir avanzado)
			ocultos = (Vector)form.getDatosTablaOcultos(0);					
															
			infoProd=admin.obtenerInfoProducto((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3));
			pagoInt=admin.obtenerFormasPago((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3),ClsConstants.TIPO_PAGO_INTERNET);
			pagoSec=admin.obtenerFormasPago((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3),ClsConstants.TIPO_PAGO_SECRETARIA);			
			pagoComun=admin.obtenerFormasPago((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3),ClsConstants.TIPO_PAGO_INTERNET_SECRETARIA);
			
			String tieneComision = admin.comprobarTieneComision((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3));
			
			// Paso valores originales del registro al session para tratar siempre con los mismos valores
			// y no los de posibles modificaciones
			request.getSession().setAttribute("DATABACKUP", infoProd);
			
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			String delimitador = paramAdm.getValor((String)ocultos.get(0),"PYS","SEPARADOR_FICHEROCOMPRAS","");
			request.setAttribute("DELIMITADOR", delimitador);
			
			// Paso valores para dar valores iniciales al formulario			
			request.setAttribute("container", infoProd);
			request.setAttribute("container_I", pagoInt);			
			request.setAttribute("container_S", pagoSec);
			request.setAttribute("container_A", pagoComun);			
			request.setAttribute("tieneComision", tieneComision);
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		}
		
		return (result);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		String result="ver";
		Vector ocultos=new Vector();
		Vector infoProd=new Vector();
		Vector pagoInt=new Vector();
		Vector pagoComun=new Vector();
		Vector pagoSec=new Vector();		
		
		try {
			
			MantenimientoProductosForm form = (MantenimientoProductosForm) formulario;
			PysProductosInstitucionAdm admin=new PysProductosInstitucionAdm(this.getUserBean(request));
			Object remitente=(Object)"consulta";
			request.setAttribute("modelo",remitente);
			
			// Mostrar valores del formulario en MantenimientoProductos (posible traslado a editar o abrir avanzado)
			ocultos = (Vector)form.getDatosTablaOcultos(0);					
															
			infoProd=admin.obtenerInfoProducto((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3));
			pagoInt=admin.obtenerFormasPago((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3),ClsConstants.TIPO_PAGO_INTERNET);
			pagoSec=admin.obtenerFormasPago((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3),ClsConstants.TIPO_PAGO_SECRETARIA);			
			pagoComun=admin.obtenerFormasPago((String)ocultos.get(0),(String)ocultos.get(1),(String)ocultos.get(2),(String)ocultos.get(3),ClsConstants.TIPO_PAGO_INTERNET_SECRETARIA);								
			
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			String delimitador = paramAdm.getValor((String)ocultos.get(0),"PYS","SEPARADOR_FICHEROCOMPRAS","");
			request.setAttribute("DELIMITADOR", delimitador);			
			// Paso valores para dar valores iniciales al formulario			
			request.setAttribute("container", infoProd);
			request.setAttribute("container_I", pagoInt);			
			request.setAttribute("container_S", pagoSec);
			request.setAttribute("container_A", pagoComun);					
						
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		}		
		
		return (result);
	}

	/** 
	 *  Funcion que implementa la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String result="nuevo";
		try {					
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion = usuario.getLocation();
			
			PysProductosInstitucionAdm admin = new PysProductosInstitucionAdm(usuario);
			String tieneComision = admin.comprobarTieneComision(idInstitucion, null, null, null);
			request.setAttribute("tieneComision", tieneComision);
			
			request.setAttribute("modelo", "insertar");
			
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		}
		
		return result;
	}

	/** 
	 *  Funcion que implementa la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub		
		
		String result="";
		String producto;
		String pago;		
		String tipoProducto;
		String[] pagosInternet;
		String[] pagosSecretaria;		
		ArrayList pagosCom = new ArrayList();		
		Hashtable pagosHash;
		UserTransaction tx = null;
		boolean correcto=true;

		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			PysProductosInstitucionAdm adminPI=new PysProductosInstitucionAdm(this.getUserBean(request));
			PysFormaPagoProductoAdm adminFPP=new PysFormaPagoProductoAdm(this.getUserBean(request));
			PysFormaPagoAdm adminFP = new PysFormaPagoAdm(this.getUserBean(request));
			// Comienzo control de transacciones
			tx = usr.getTransaction(); 
			// Obtengo los datos del formulario
			MantenimientoProductosForm miForm = (MantenimientoProductosForm)formulario;
			producto=miForm.getProducto();
			tipoProducto=miForm.getTipoProducto();
			pagosInternet=miForm.getFormaPagoInternet();
			pagosSecretaria=miForm.getFormaPagoSecretaria();
			pagosCom=pagosComunes(pagosInternet,pagosSecretaria);
			// Cargo la tabla hash con los valores del formulario para insertar en PYS_PRODUCTOSINSTITUCION
			Hashtable hash = formulario.getDatos();
			// Anhado valores que faltan
			hash.put("IDINSTITUCION",usr.getLocation());
			hash.put("IDTIPOPRODUCTO",tipoProducto);			
			
			// Obtengo el IDPRODUCTOINSTITUCION y los campos SOLICITARBAJA y SOLICITARALTA si no fueron seleccionados
			
//			 Si se trata de TIPO_CERTIFICADO_COMISIONBANCARIA prevalece sobre los demás tipos de certificados
			if(UtilidadesHash.getString(hash, "TIPOCERTIFICADOCOMISION")!=null && UtilidadesHash.getString(hash, "TIPOCERTIFICADOCOMISION").equalsIgnoreCase("1")){
				UtilidadesHash.set(hash, PysProductosInstitucionBean.C_TIPOCERTIFICADO, PysProductosInstitucionAdm.TIPO_CERTIFICADO_COMISIONBANCARIA);
			}
			
			
			adminPI.prepararInsert(hash);

			// Comienzo la transaccion
			tx.begin();		

			
			
			// RGG 05/03/2007 INC_3011 Insercion Contador certificados en caso de certificado.
			if (!miForm.getTipoCertificado().trim().equals("")){	
				// es de tipo certificado
				AdmContadorAdm admContador = new AdmContadorAdm(this.getUserBean(request));
				AdmContadorBean b = new AdmContadorBean();

				b.setIdCampoContador(CerSolicitudCertificadosBean.C_CONTADOR_CER);
				b.setIdCampoPrefijo(CerSolicitudCertificadosBean.C_PREFIJO_CER);
				b.setIdCampoSufijo(CerSolicitudCertificadosBean.C_SUFIJO_CER);
				b.setIdContador("PYS_"+hash.get("IDTIPOPRODUCTO")+"_"+hash.get("IDPRODUCTO")+"_"+hash.get("IDPRODUCTOINSTITUCION"));
				b.setIdinstitucion(new Integer((String)hash.get("IDINSTITUCION")));
				b.setNombre((String)hash.get("DESCRIPCION"));
				b.setDescripcion((String)hash.get("DESCRIPCION"));
				b.setModificableContador("0");// NO MODIFICABLE	
				b.setModoContador(new Integer(0)); // MODO REGISTRO	
				b.setContador(new Long(0));	
				b.setPrefijo("");	
				b.setGeneral("0");	
				String anioActual=UtilidadesString.formatoFecha(new Date(),"yyyy");
				b.setSufijo("/"+anioActual);
				b.setLongitudContador(new Integer(5));
				b.setFechaReconfiguracion(null);
				b.setReconfiguracionContador("0");
				b.setReconfiguracionPrefijo(null);
				b.setReconfiguracionSufijo(null);
				b.setIdModulo(new Integer("9"));//Productos y Servicios
				b.setIdTabla(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				b.setFechaMod("SYSDATE");
				b.setUsuMod(new Integer(usr.getUserName()));
				b.setFechacreacion("SYSDATE");
				b.setUsucreacion(new Integer(usr.getUserName()));
				
				admContador.insert(b);					
			
				// ACTUALIZO EL VALOR EN LA SOLICITUD (FK A CONTADORES)
				hash.put(PysProductosInstitucionBean.C_IDCONTADOR,"PYS_"+hash.get("IDTIPOPRODUCTO")+"_"+hash.get("IDPRODUCTO")+"_"+hash.get("IDPRODUCTOINSTITUCION"));	
				
			}

			
			// Inserto en PYS_PRODUCTOSINSTITUCION
			correcto=adminPI.insert(hash); 			
			
			// Creacion de las hash para insertar las diferentes formas de pago de Internet	y Secretaria
			Hashtable hashAux = new Hashtable();
			Vector vectorAux = new Vector();
			
			// Insercion Forma Pago Internet y Secretaria
			if (correcto){
				if (!pagosCom.isEmpty()){			
					int i=0;
					while(i<pagosCom.size()){
						if (((String)pagosCom.get(i)).compareToIgnoreCase("-1")!=0){
							hashAux.put("IDINSTITUCION",hash.get("IDINSTITUCION"));																					
							hashAux.put("IDTIPOPRODUCTO",hash.get("IDTIPOPRODUCTO"));
							hashAux.put("IDPRODUCTO",hash.get("IDPRODUCTO"));				
							hashAux.put("IDPRODUCTOINSTITUCION",hash.get("IDPRODUCTOINSTITUCION"));				
							hashAux.put("IDFORMAPAGO",(String)pagosCom.get(i));
							hashAux.put("INTERNET",ClsConstants.TIPO_PAGO_INTERNET_SECRETARIA);
							correcto=adminFPP.insert(hashAux);					
						}	
						i++;				
					}
				}	
			}	
				
			// Insercion Forma Pago Internet
			if (correcto){			
				if (pagosInternet!=null){			
					int i=0;
					while(i<pagosInternet.length){
						if ((pagosInternet[i].compareToIgnoreCase("-1")!=0)&&(pagosInternet[i].compareToIgnoreCase("")!=0)&&(!estaPago(pagosInternet[i],pagosCom))){
							hashAux.put("IDINSTITUCION",hash.get("IDINSTITUCION"));				
							hashAux.put("IDTIPOPRODUCTO",hash.get("IDTIPOPRODUCTO"));
							hashAux.put("IDPRODUCTO",hash.get("IDPRODUCTO"));				
							hashAux.put("IDPRODUCTOINSTITUCION",hash.get("IDPRODUCTOINSTITUCION"));				
							hashAux.put("IDFORMAPAGO",pagosInternet[i]);
							hashAux.put("INTERNET",ClsConstants.TIPO_PAGO_INTERNET);
							correcto=adminFPP.insert(hashAux);					
						}	
						i++;				
					}
				}
			}	
			
			// Insercion Forma Pago Secretaria
			if (correcto){			
				if (pagosSecretaria!=null){			
					int j=0;			
					while(j<pagosSecretaria.length){
						if ((pagosSecretaria[j].compareToIgnoreCase("-1")!=0)&&(pagosSecretaria[j].compareToIgnoreCase("")!=0)&&(!estaPago(pagosSecretaria[j],pagosCom))){
							hashAux.put("IDINSTITUCION",hash.get("IDINSTITUCION"));				
							hashAux.put("IDTIPOPRODUCTO",hash.get("IDTIPOPRODUCTO"));
							hashAux.put("IDPRODUCTO",hash.get("IDPRODUCTO"));				
							hashAux.put("IDPRODUCTOINSTITUCION",hash.get("IDPRODUCTOINSTITUCION"));				
							hashAux.put("IDFORMAPAGO",pagosSecretaria[j]);
							hashAux.put("INTERNET",ClsConstants.TIPO_PAGO_SECRETARIA);							
							adminFPP.insert(hashAux);					
						}	
						j++;				
					}						
				}
			}
			
			if (correcto){
				tx.commit();				
				result=exitoModal("messages.updated.success",request);			
			}			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,tx); 
		}		

		return result;

	}

	/** 
	 *  Funcion que implementa la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String result="error";
		String producto;
		String pago;		
		String paramConsulta;		
		String tipoProducto;
		String idProdInst;
		String[] pagosInternet;
		String[] pagosSecretaria;
		ArrayList pagosCom = new ArrayList();		
		Hashtable pagosHash;
		Hashtable hashOriginal = new Hashtable();
		Enumeration filaOriginal; 		
		Row registroOriginal;
		UserTransaction tx=null;

		// Creacion de las hash para insertar las diferentes formas de pago de Internet	y Secretaria
		Hashtable hashAux = new Hashtable();
		Vector vectorAux = new Vector();
		Vector vectorFP = new Vector();
		Vector camposOcultos = new Vector();
		boolean correcto=true;
		
		
		String[] claves = { AdmContadorBean.C_IDINSTITUCION,
				AdmContadorBean.C_IDCONTADOR };
		
		String[] campos = { AdmContadorBean.C_CONTADOR,
				AdmContadorBean.C_DESCRIPCION,
				AdmContadorBean.C_FECHARECONFIGURACION,
				AdmContadorBean.C_IDCAMPOCONTADOR,
                AdmContadorBean.C_IDCAMPOPREFIJO,
                AdmContadorBean.C_IDCAMPOSUFIJO,
				AdmContadorBean.C_IDCONTADOR,
				AdmContadorBean.C_IDINSTITUCION,
				AdmContadorBean.C_IDTABLA,
				AdmContadorBean.C_LONGITUDCONTADOR,
				AdmContadorBean.C_MODIFICABLECONTADOR,
				AdmContadorBean.C_MODO,
				AdmContadorBean.C_NOMBRE,
				AdmContadorBean.C_PREFIJO,
				AdmContadorBean.C_RECONFIGURACIONCONTADOR,
				AdmContadorBean.C_RECONFIGURACIONPREFIJO,
				AdmContadorBean.C_RECONFIGURACIONSUFIJO,
				AdmContadorBean.C_SUFIJO, 	
				AdmContadorBean.C_GENERAL,
                AdmContadorBean.C_IDMODULO,
                AdmContadorBean.C_FECHAMODIFICACION,
				AdmContadorBean.C_USUMODIFICACION};

		
		try {		
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			PysProductosInstitucionAdm adminPI=new PysProductosInstitucionAdm(this.getUserBean(request));
			PysFormaPagoProductoAdm adminFPP=new PysFormaPagoProductoAdm(this.getUserBean(request));
			PysProductosSolicitadosAdm adminPPS=new PysProductosSolicitadosAdm(this.getUserBean(request));
			
			PysFormaPagoAdm adminFP = new PysFormaPagoAdm(this.getUserBean(request));
			// Comienzo control de transacciones
			tx = usr.getTransaction(); 			
			// Obtengo los datos del formulario
			MantenimientoProductosForm miForm = (MantenimientoProductosForm)formulario;
			producto=miForm.getProducto();
			tipoProducto=miForm.getTipoProducto();
			
			
			// Obtengo las diferentes formas de pago "modificadas"
			pagosInternet=miForm.getFormaPagoInternet();
			pagosSecretaria=miForm.getFormaPagoSecretaria();
			pagosCom=pagosComunes(pagosInternet,pagosSecretaria);			
			idProdInst=miForm.getIdProdInst();	
			
			// Cargo la tabla hash con los valores del formulario para insertar en PYS_PRODUCTOSINSTITUCION
			Hashtable hash = formulario.getDatos();			
			
			
			String bajaLogica = (String) request.getParameter("bajaLogica"); 
			if (bajaLogica!=null) {
				hash.put("FECHABAJA","SYSDATE");
			} else {
				hash.put("FECHABAJA","");
				//hash.remove("FECHABAJA");
			}
				
			// Anhado valores que faltan
			hash.put("IDINSTITUCION",usr.getLocation());
			hash.put("IDTIPOPRODUCTO",tipoProducto);			
			hash.put("IDPRODUCTO",producto);
			boolean checkNoFacturable  = UtilidadesString.stringToBoolean(miForm.getNoFacturable());
			if (checkNoFacturable){
			  hash.put("NOFACTURABLE","1");
			}
			
			
			
			// Si se trata de TIPO_CERTIFICADO_COMISIONBANCARIA prevalece sobre los demás tipos de certificados
			if(UtilidadesHash.getString(hash, "TIPOCERTIFICADOCOMISION")!=null && UtilidadesHash.getString(hash, "TIPOCERTIFICADOCOMISION").equalsIgnoreCase("1")){
				UtilidadesHash.set(hash, PysProductosInstitucionBean.C_TIPOCERTIFICADO, PysProductosInstitucionAdm.TIPO_CERTIFICADO_COMISIONBANCARIA);
			}
			
			
			
			// Obtengo el IDPRODUCTOINSTITUCION y los campos SOLICITARBAJA y SOLICITARALTA si no fueron seleccionados
			adminPI.prepararInsert(hash);
			// Doy el valor correcto a IDPRODUCTOINSTITUCION
			hash.put("IDPRODUCTOINSTITUCION",idProdInst);

			// Obtengo formas de pago existentes, construyo sentencia where para busqueda y realizo la busqueda
			paramConsulta = " WHERE " +
			  	PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDINSTITUCION + "=" + hash.get("IDINSTITUCION") +
				" AND " +
				PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDTIPOPRODUCTO + "=" + hash.get("IDTIPOPRODUCTO") +
				" AND " +
				PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDPRODUCTO + "=" + hash.get("IDPRODUCTO") +
				" AND " +
				PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDPRODUCTOINSTITUCION + "=" + hash.get("IDPRODUCTOINSTITUCION");
			
			vectorAux=adminFPP.select(paramConsulta);
			
			// Cargo una hastable con los valores originales del registro sobre el que se realizará la modificacion						
			filaOriginal=((Vector)request.getSession().getAttribute("DATABACKUP")).elements();
			while(filaOriginal.hasMoreElements()){
              	registroOriginal = (Row) filaOriginal.nextElement(); 
				hashOriginal.put("IDINSTITUCION",registroOriginal.getString(PysProductosInstitucionBean.C_IDINSTITUCION));				              									
				hashOriginal.put("IDTIPOPRODUCTO",registroOriginal.getString(PysProductosInstitucionBean.C_IDTIPOPRODUCTO));
				hashOriginal.put("IDPRODUCTO",registroOriginal.getString(PysProductosInstitucionBean.C_IDPRODUCTO));				              									
				hashOriginal.put("IDPRODUCTOINSTITUCION",registroOriginal.getString(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION));
				hashOriginal.put("DESCRIPCION",registroOriginal.getString(PysProductosInstitucionBean.C_DESCRIPCION));				              									
				hashOriginal.put("CUENTACONTABLE",registroOriginal.getString(PysProductosInstitucionBean.C_CUENTACONTABLE));
				hashOriginal.put("VALOR",registroOriginal.getString(PysProductosInstitucionBean.C_VALOR));				              									
				hashOriginal.put("PORCENTAJEIVA",registroOriginal.getString(PysProductosInstitucionBean.C_PORCENTAJEIVA));								
				hashOriginal.put("MOMENTOCARGO",registroOriginal.getString(PysProductosInstitucionBean.C_MOMENTOCARGO));
				hashOriginal.put("SOLICITARBAJA",registroOriginal.getString(PysProductosInstitucionBean.C_SOLICITARBAJA));				              									
				hashOriginal.put("SOLICITARALTA",registroOriginal.getString(PysProductosInstitucionBean.C_SOLICITARALTA));												
				hashOriginal.put("IDCONTADOR",registroOriginal.getString(PysProductosInstitucionBean.C_IDCONTADOR));												
				hashOriginal.put("NOFACTURABLE",registroOriginal.getString(PysProductosInstitucionBean.C_NOFACTURABLE));
			}
			
			// Comienzo la transaccion
			tx.begin();	

			
			// RGG 05/03/2007 INC_3011 Modificacion Contador certificados en caso de certificado.
			if (!miForm.getTipoCertificado().trim().equals("")){	
				// es de tipo certificado
				Hashtable contador = new Hashtable();
				contador.put(AdmContadorBean.C_IDINSTITUCION,usr.getLocation());
				contador.put(AdmContadorBean.C_IDCONTADOR,(String)hashOriginal.get("IDCONTADOR"));
				AdmContadorAdm admContador = new AdmContadorAdm(this.getUserBean(request));
				Vector v = admContador.selectByPK(contador);
				if (v!=null && v.size()>0) {
					AdmContadorBean b = (AdmContadorBean) v.get(0);
					b.setDescripcion((String)hash.get("DESCRIPCION"));
					b.setNombre((String)hash.get("DESCRIPCION"));
					b.setFechaMod("sysdate");			
					b.setUsuMod(new Integer(usr.getUserName()));
					admContador.updateDirect(b,claves,campos);									
				} else {
					// No existía el certificado. Se crea
					AdmContadorBean b = new AdmContadorBean();

					b.setIdCampoContador(CerSolicitudCertificadosBean.C_CONTADOR_CER);
					b.setIdCampoPrefijo(CerSolicitudCertificadosBean.C_PREFIJO_CER);
					b.setIdCampoSufijo(CerSolicitudCertificadosBean.C_SUFIJO_CER);
					b.setIdContador("PYS_"+hash.get("IDTIPOPRODUCTO")+"_"+hash.get("IDPRODUCTO")+"_"+hash.get("IDPRODUCTOINSTITUCION"));
					b.setIdinstitucion(new Integer((String)hash.get("IDINSTITUCION")));
					b.setNombre((String)hash.get("DESCRIPCION"));
					b.setDescripcion((String)hash.get("DESCRIPCION"));
					b.setModificableContador("0");// NO MODIFICABLE	
					b.setModoContador(new Integer(0)); // MODO REGISTRO	
					b.setContador(new Long(0));	
					b.setPrefijo("");	
					b.setGeneral("0");	
					String anioActual=UtilidadesString.formatoFecha(new Date(),"yyyy");
					b.setSufijo("/"+anioActual);
					b.setLongitudContador(new Integer(5));
					b.setFechaReconfiguracion(null);
					b.setReconfiguracionContador("0");
					b.setReconfiguracionPrefijo(null);
					b.setReconfiguracionSufijo(null);
					b.setIdModulo(new Integer("9"));//Productos y Servicios
					b.setIdTabla(CerSolicitudCertificadosBean.T_NOMBRETABLA);
					b.setFechaMod("SYSDATE");
					b.setUsuMod(new Integer(usr.getUserName()));
					b.setFechacreacion("SYSDATE");
					b.setUsucreacion(new Integer(usr.getUserName()));
					admContador.insert(b);					
				
					// ACTUALIZO EL VALOR EN LA SOLICITUD (FK A CONTADORES)
					hash.put(PysProductosInstitucionBean.C_IDCONTADOR,"PYS_"+hash.get("IDTIPOPRODUCTO")+"_"+hash.get("IDPRODUCTO")+"_"+hash.get("IDPRODUCTOINSTITUCION"));	
					
				}
				
			}
			
			// Actualizo PYS_PRODUCTOSINSTITUCION (necesario previa insercion en PYS_FORMAPAGOPRODUCTO)
			correcto=adminPI.update(hash,hashOriginal);
			
			if (correcto){
				// paso la consulta realizada a un objeto enumeracion para recorrerlo facilmente
		    	Enumeration enumer = vectorAux.elements();
		    	// Creo un vector con las formas de pago (IDFORMAPAGO) existentes para esas caracteristicas
				while (enumer.hasMoreElements())
				{
					PysFormaPagoProductoBean fila = (PysFormaPagoProductoBean) enumer.nextElement();
		           	vectorFP.add(fila.getIdFormaPago().toString());
				}
	
				// Borro todas las formas de pago existentes
				Enumeration idFormasPago = vectorFP.elements();
				while (idFormasPago.hasMoreElements())
				{
					String nuevoPago = (String) idFormasPago.nextElement();
					hashAux.put("IDINSTITUCION",hash.get("IDINSTITUCION"));				
					hashAux.put("IDTIPOPRODUCTO",hash.get("IDTIPOPRODUCTO"));
					hashAux.put("IDPRODUCTO",hash.get("IDPRODUCTO"));				
					hashAux.put("IDPRODUCTOINSTITUCION",hash.get("IDPRODUCTOINSTITUCION"));				
					hashAux.put("IDFORMAPAGO",nuevoPago);
					correcto=adminFPP.delete(hashAux);
				}	
				if (!checkNoFacturable){
					// Insercion Forma Pago Internet y Secretaria
					if (!pagosCom.isEmpty()){			
						int i=0;
						while(i<pagosCom.size()){
							if (((String)pagosCom.get(i)).compareToIgnoreCase("-1")!=0){
								hashAux.put("IDINSTITUCION",hash.get("IDINSTITUCION"));				
								hashAux.put("IDTIPOPRODUCTO",hash.get("IDTIPOPRODUCTO"));
								hashAux.put("IDPRODUCTO",hash.get("IDPRODUCTO"));				
								hashAux.put("IDPRODUCTOINSTITUCION",hash.get("IDPRODUCTOINSTITUCION"));				
								hashAux.put("IDFORMAPAGO",(String)pagosCom.get(i));
								hashAux.put("INTERNET",ClsConstants.TIPO_PAGO_INTERNET_SECRETARIA);
								correcto=adminFPP.insert(hashAux);					
							}	
							i++;				
						}
					}	
						
					// Insercion Forma Pago Internet
					if (pagosInternet!=null){			
						int i=0;
						while(i<pagosInternet.length){
							if ((pagosInternet[i].compareToIgnoreCase("-1")!=0)&&(pagosInternet[i].compareToIgnoreCase("")!=0)&&(!estaPago(pagosInternet[i],pagosCom))){
								hashAux.put("IDINSTITUCION",hash.get("IDINSTITUCION"));				
								hashAux.put("IDTIPOPRODUCTO",hash.get("IDTIPOPRODUCTO"));
								hashAux.put("IDPRODUCTO",hash.get("IDPRODUCTO"));				
								hashAux.put("IDPRODUCTOINSTITUCION",hash.get("IDPRODUCTOINSTITUCION"));				
								hashAux.put("IDFORMAPAGO",pagosInternet[i]);
								hashAux.put("INTERNET",ClsConstants.TIPO_PAGO_INTERNET);
								correcto=adminFPP.insert(hashAux);					
							}	
							i++;				
						}
					}	
					
					// Insercion Forma Pago Secretaria
					if (pagosSecretaria!=null){			
						int j=0;			
						while(j<pagosSecretaria.length){
							if ((pagosSecretaria[j].compareToIgnoreCase("-1")!=0)&&(pagosSecretaria[j].compareToIgnoreCase("")!=0)&&(!estaPago(pagosSecretaria[j],pagosCom))){
								hashAux.put("IDINSTITUCION",hash.get("IDINSTITUCION"));				
								hashAux.put("IDTIPOPRODUCTO",hash.get("IDTIPOPRODUCTO"));
								hashAux.put("IDPRODUCTO",hash.get("IDPRODUCTO"));				
								hashAux.put("IDPRODUCTOINSTITUCION",hash.get("IDPRODUCTOINSTITUCION"));				
								hashAux.put("IDFORMAPAGO",pagosSecretaria[j]);
								hashAux.put("INTERNET",ClsConstants.TIPO_PAGO_SECRETARIA);							
								correcto=adminFPP.insert(hashAux);					
							}	
							j++;				
						}						
					}
				}
			}
			
			String where =" WHERE "+PysProductosSolicitadosBean.C_IDINSTITUCION+"="+hash.get("IDINSTITUCION")+
			              " and  "+PysProductosSolicitadosBean.C_IDPRODUCTO+"="+hash.get("IDPRODUCTO")+
						  " and  "+PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION+"="+hash.get("IDPRODUCTOINSTITUCION")+
						  " and  "+PysProductosSolicitadosBean.C_IDTIPOPRODUCTO+"="+hash.get("IDTIPOPRODUCTO");
		     Vector vPPS=adminPPS.select(where);
			String consulta="";
			RowsContainer rc = new RowsContainer();
			
			if (hashOriginal.get("NOFACTURABLE").equals(ClsConstants.DB_FALSE)&& checkNoFacturable){ //Si el producto era Facturable y cambia a No Facturable
				
				if (vPPS!=null && vPPS.size()>=0){
						for (int i=0;i<vPPS.size();i++) {
							PysProductosSolicitadosBean registro = (PysProductosSolicitadosBean) vPPS.get(i);
							consulta="select F_SIGA_ESTADOCOMPRA("+registro.getIdInstitucion()+","+ registro.getIdPeticion()+","+registro.getIdProducto()+
							         ","+registro.getIdTipoProducto()+", "+registro.getIdProductoInstitucion()+") ESTADOPAGO from dual";
							Vector vestadoPago=adminPPS.selectGenerico(consulta);
							String estado =	(String)((Hashtable)vestadoPago.get(0)).get("ESTADOPAGO");
							 if ((registro.getAceptado().equals(ClsConstants.PRODUCTO_ACEPTADO) || registro.getAceptado().equals(ClsConstants.PRODUCTO_PENDIENTE)) && !(estado.equals("estados.compra.cobrado")) && !(estado.equals("estados.compra.pendienteFactura")) && !(estado.equals("estados.compra.facturado"))) {
						 	
						 	registro.setNoFacturable(ClsConstants.DB_TRUE);
						    adminPPS.updateDirect(registro);
						} 
					}
				}	
			}else{
				if (hashOriginal.get("NOFACTURABLE").equals(ClsConstants.DB_TRUE)&& !checkNoFacturable){// Si el producto era NO Facturable y se cambia a facturable
					if (vPPS!=null && vPPS.size()>=0){
						for (int i=0;i<vPPS.size();i++) {
						 PysProductosSolicitadosBean registro=(PysProductosSolicitadosBean)vPPS.get(i);
						 consulta="select F_SIGA_ESTADOCOMPRA("+registro.getIdInstitucion()+","+ registro.getIdPeticion()+","+registro.getIdProducto()+
				         ","+registro.getIdTipoProducto()+", "+registro.getIdProductoInstitucion()+") ESTADOPAGO from dual";
						 Vector vestadoPago=adminPPS.selectGenerico(consulta);
						 
						String estado =	(String)((Hashtable)vestadoPago.get(0)).get("ESTADOPAGO");
						 if ((registro.getAceptado().equals(ClsConstants.PRODUCTO_ACEPTADO) || registro.getAceptado().equals(ClsConstants.PRODUCTO_PENDIENTE)) && !(estado.equals("estados.compra.cobrado")) && !(estado.equals("estados.compra.pendienteFactura")) && !(estado.equals("estados.compra.facturado"))) {
						 	     registro.setNoFacturable(ClsConstants.DB_FALSE);
								 adminPPS.updateDirect(registro);
						 }
						} 
					}
				}
			}	
			
			if (correcto){
				tx.commit();				
				result=exitoModal("messages.updated.success",request);			
			}			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,tx); 
		}		
					
		return (result);		
	}


	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		
		String result="error";
		int numElementos;		
		boolean correcto;		
		Hashtable hash = new Hashtable();		
		Vector camposVistos = new Vector();
		Vector camposOcultos = new Vector();
		UserTransaction tx=null;
		
		try{
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			PysProductosInstitucionAdm admin=new PysProductosInstitucionAdm(this.getUserBean(request));
			PysFormaPagoProductoAdm adminFPP=new PysFormaPagoProductoAdm(this.getUserBean(request));
			// Comienzo control de transacciones
			tx = usr.getTransaction(); 			
			// Obtengo los datos del formulario		
			MantenimientoProductosForm miForm = (MantenimientoProductosForm)formulario;		
			camposVistos = miForm.getDatosTabla();
			numElementos = camposVistos.size();
			camposVistos = miForm.getDatosTablaVisibles(0);
			camposOcultos = miForm.getDatosTablaOcultos(0);
		
			tx.begin();


			// Cargo la tabla hash con los valores del formulario para eliminar en PYS_PRODUCTOSINSTITUCION y PYS_FORMAPAGOPRODUCTO		
			hash.put("IDINSTITUCION",camposOcultos.get(0));
			hash.put("IDTIPOPRODUCTO",camposOcultos.get(1));
			hash.put("IDPRODUCTO",camposOcultos.get(2));
			hash.put("IDPRODUCTOINSTITUCION",camposOcultos.get(3));		

			// RGG 05/03/2007 INC_3011 Borrar Contador certificados en caso de certificado.
			// es de tipo certificado
			Vector vv = admin.selectByPK(hash);
			Hashtable contador = new Hashtable();
			if (vv!=null && vv.size()>0) {
				PysProductosInstitucionBean bb = (PysProductosInstitucionBean) vv.get(0);
				contador.put(AdmContadorBean.C_IDINSTITUCION,usr.getLocation());
				contador.put(AdmContadorBean.C_IDCONTADOR,bb.getIdContador());
			}
			
			// Procedo a borrar en BBDDs PYS_FORMAPAGOPRODUCTO -> ya no es necesario -> Borrado en cascada
			// correcto=adminFPP.delete(hash); if...				
			// Procedo a borrar en BBDDs PYS_PRODUCTOSINSTITUCION				
			correcto=admin.delete(hash);
			if (correcto){

				AdmContadorAdm admContador = new AdmContadorAdm(this.getUserBean(request));
				Vector v = admContador.selectByPK(contador);
				if (v!=null && v.size()>0) {
					AdmContadorBean b = (AdmContadorBean) v.get(0);
					admContador.delete(b);
				}

				tx.commit();
				result=exitoRefresco("messages.deleted.success",request);				
			    //request.setAttribute("descOperation","messages.deleted.success");					
				//result="refresco";
			}	
			else{
				request.setAttribute("descOperation","messages.deleted.error");
				result="refresco";					
			}
		}
//		catch(ClsExceptions ex){
//			try {
//				tx.rollback();
//			} 
//			catch (Exception e) {}
//			result=exito("messages.deleted.error",request);
//		} 
		catch (Exception e) { 
			throwExcp("messages.pys.mantenimientoProductos.errorBorrado",new String[] {"modulo.productos"},e,tx); 
		}				
		
        request.setAttribute("hiddenFrame", "1");        
        
		return (result);
	}
					
	/** 
	 *  Funcion que implementa la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub

		String result="listar";
		Vector v=new Vector();		
		
		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			MantenimientoProductosForm f = (MantenimientoProductosForm) formulario;
			PysProductosInstitucionAdm admin=new PysProductosInstitucionAdm(this.getUserBean(request));
			v=admin.obtenerProductosInstitucion(f.getBusquedaTipo(), 
												f.getBusquedaCategoria(),
												f.getBusquedaProducto(),
												usr.getLocation(),
												f.getBusquedaPago(),
												f.getBusquedaEstado());
			request.setAttribute("container", v);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		}		

		return (result);
		
		
	}

	/** 
	 *  Funcion que prepara el formato fecha
	 * @param  fecha - Fecha en formato original
	 * @return  String - Formato actualizado fecha   
	 */	
	protected String prepararFecha(String fecha){
		
		String sInicial=fecha;
		String sFinal="";		
		
		for (int i=0;i<sInicial.length();i++) {
			if (sInicial.charAt(i) == '-') sFinal += '/';
			else sFinal += sInicial.charAt(i);
		}
		sFinal += " 00:00:00";		
		return sFinal;
	}
	
	/** 
	 *  Funcion que recoge formas de pago comunes.
	 * @param  pagosA - Lista de pagos A
	 * @param  pagosB - Lista de pagos B
	 * @return  ArrayList - Lista de pagos comunes  
	 */	
	protected ArrayList pagosComunes(String[] pagosA, String[] pagosB){
		
		int i,j,k;
		ArrayList comunes=new ArrayList();
		
		if (pagosA!=null) {
			for (i=0;i<pagosA.length;i++) {
				for (j=0;j<pagosB.length;j++) {
				 if (pagosA[i]!=null && !pagosA[i].equals("")){	
					if (pagosA[i].compareToIgnoreCase(pagosB[j])==0){
					 comunes.add(pagosA[i]);
					}
				 }	
				}
			}	
		}
		return comunes;
	}
	
	protected boolean estaPago(String pago, ArrayList listaPagos){
				
		return listaPagos.contains(pago);
	}
	
}
