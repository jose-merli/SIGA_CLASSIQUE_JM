/*
 * VERSIONES:
 * 
 * miguel.villegas - 07-03-2005 - Creacion
 *	
 */

/**
 * Clase action para el mantenimiento de abonos.<br/>
 * Gestiona la edicion, borrado, consulta y mantenimiento de los abonos.  
 */

package com.siga.facturacion.action;


import javax.servlet.http.*;
import javax.transaction.*;


import org.apache.struts.action.*;

import com.atos.utils.*;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.facturacion.form.GenerarAbonosForm;
import java.util.*;


public class GenerarAbonosAction extends MasterAction {

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
		
		String result="abrir";
		
		try{
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			
			// para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("CenBusquedaClientesTipo","GAA"); // busqueda normal
			
			// miro a ver si tengo que ejecutar 
			//la busqueda una vez presentada la pagina
			String buscar = request.getParameter("buscar");
			if (buscar==null){
				GenerarAbonosForm miformSession = (GenerarAbonosForm)request.getSession().getAttribute("GenerarAbonosForm");
				if (miformSession!=null) {
					miformSession.reset(mapping,request);
					miformSession.setBusquedaIdAbono("");
					miformSession.setBusquedaIdFactura("");
					miformSession.setContabilizadoBusqueda("");
					miformSession.setFechaAbonoDesde("");
					miformSession.setFechaAbonoHasta("");
					miformSession.setFechaFacturaDesde("");
					miformSession.setFechaFacturaHasta("");
					miformSession.setFormaPagoBusqueda("");
					miformSession.setIdPersonaBusqueda("");
					miformSession.setPagadoBusqueda("");
					miformSession.setTipoAbonoBusqueda("");
				}
				GenerarAbonosForm miform = (GenerarAbonosForm)formulario;
				miform.reset(mapping,request);
				miformSession.setBusquedaIdAbono("");
				miformSession.setBusquedaIdFactura("");
				miformSession.setContabilizadoBusqueda("");
				miformSession.setFechaAbonoDesde("");
				miformSession.setFechaAbonoHasta("");
				miformSession.setFechaFacturaDesde("");
				miformSession.setFechaFacturaHasta("");
				miformSession.setFormaPagoBusqueda("");
				miformSession.setIdPersonaBusqueda("");
				miformSession.setPagadoBusqueda("");
				miformSession.setTipoAbonoBusqueda("");
			}
			request.setAttribute("buscar",buscar);
			
			
			// Paso de parametros empleando request
			request.setAttribute("IDINSTITUCION", idInstitucion);				
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		} 

			
	return result;
	}

	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "abrirAvanzada";
	}

	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return null;
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
		
		String result = "";
		String modo = "";
		Hashtable datosAbonos = new Hashtable();
		
		try {
     	
			GenerarAbonosForm miform = (GenerarAbonosForm)formulario;
	
			// Obtengo el formulario
			Vector fila = miform.getDatosTablaOcultos(0);
			String idAbono = (String)fila.get(0);
			String idInstitucion = (String)fila.get(1);

			modo = "editar";
			datosAbonos.put("accion",modo);
			datosAbonos.put("idAbono",idAbono);
			datosAbonos.put("idInstitucion",idInstitucion);
			datosAbonos.put("botonVolver", "SI");
	
			request.setAttribute("datosAbonos", datosAbonos);		
	
			result="administrar";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null);
		}
	
		return result;
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result = "";
		String modo = "";
		Hashtable datosAbonos = new Hashtable();
		
		try {
     	
			GenerarAbonosForm miform = (GenerarAbonosForm)formulario;
	
			// Obtengo el formulario
			Vector fila = miform.getDatosTablaOcultos(0);
			String idAbono = (String)fila.get(0);
			String idInstitucion = (String)fila.get(1);

			modo = "consulta";
			datosAbonos.put("accion",modo);
			datosAbonos.put("idAbono",idAbono);
			datosAbonos.put("idInstitucion",idInstitucion);
			datosAbonos.put("botonVolver", "SI");
			
			request.setAttribute("datosAbonos", datosAbonos);					
			
			result="administrar";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null);
		}
	
		return result;
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
		try{						
			Object remitente=(Object)"insertar";
			request.setAttribute("modelo",remitente);									
		} 
		catch (Exception e) { 
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
		String result="";
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
		String result="error";
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
		
		String result="editar";
		Vector fila = new Vector();
		boolean correcto=true;		
		Hashtable hash = new Hashtable();
		UserTransaction tx=null;
		String idAbono = "";
		String idInstitucion = "";
		Vector entradas = new Vector();
		Hashtable lineaAbono = new Hashtable();
		// Obtengo usuario y creo manejadores para acceder a las BBDD
		UsrBean usr = this.getUserBean(request);
		FacAbonoAdm admAbono=new FacAbonoAdm(usr);
		FacLineaAbonoAdm admLineaAbono=new FacLineaAbonoAdm(usr);
		FacPagosPorCajaAdm admPagosPorCaja = new FacPagosPorCajaAdm(usr);
		FacPagoAbonoEfectivoAdm admPagoAbonoefectivo = new FacPagoAbonoEfectivoAdm(usr);
		FacFacturaAdm facturaAdm = new FacFacturaAdm(usr);
		// Comienzo control de transacciones
		tx = usr.getTransaction(); 			
		// Obtengo los datos del formulario		
		GenerarAbonosForm miform = (GenerarAbonosForm)formulario;		
		fila = miform.getDatosTablaOcultos(0);
		idAbono = (String)fila.get(0);
		idInstitucion = (String)fila.get(1);
		try{
			
			
			tx.begin();
			admAbono.lockTable();
			facturaAdm.lockTable();
			admPagosPorCaja.lockTable();
			admPagoAbonoefectivo.lockTable();			
			admLineaAbono.lockTable();
			
			Hashtable htRegistro = new Hashtable();
			htRegistro.put(FacLineaAbonoBean.C_IDINSTITUCION, idInstitucion);
			htRegistro.put(FacLineaAbonoBean.C_IDABONO, idAbono);
			
			// busco el abono
			FacAbonoBean abonoBean = null;
			FacAbonoAdm abonoAdm = new FacAbonoAdm(this.getUserBean(request));
		    Vector vA = abonoAdm.selectByPK(htRegistro);
		    if (vA!=null && vA.size()>0) {
		        abonoBean = (FacAbonoBean) vA.get(0);
		    } else {
		        throw new ClsExceptions("No se encuentra el abono a eliminar: "+abonoAdm.getError());
		    }
		    
		    //BNS Comprobamos si es un abono SJCS para no permitirlo (SJCS tendrá su propia gestión de abonos)
		    if (abonoBean.getIdPagosJG() != null && !abonoBean.getIdPagosJG().equals("")){
		    	throw new ClsExceptions("No se puede eliminar un abono SJCS, el mantenimiento de estos abonos se realizará desde su propia pantalla");
		    }
		    
			String clavesLineas[]= new String[]{FacLineaAbonoBean.C_IDINSTITUCION,FacLineaAbonoBean.C_IDABONO};
			correcto = admLineaAbono.deleteDirect(htRegistro, clavesLineas);
			if(!correcto) {
				throw new ClsExceptions("Error al eliminar las lineas del abono: "+admLineaAbono.getError());
			}

			htRegistro.put(FacPagosPorCajaBean.C_TIPOAPUNTE, FacPagosPorCajaBean.tipoApunteCompensado);

			boolean bAbonoConFacturaAsociada = false;
			//BNS: Como puede ser un abono SJCS controlamos si tiene factura asociada o no
			if (abonoBean.getIdFactura() != null && abonoBean.getIdFactura() != ""){
				bAbonoConFacturaAsociada = true;
				//BNS: Como puede haber pagos parciales de un abono SJCS incluimos el id de factura en la query
				htRegistro.put(FacPagosPorCajaBean.C_IDFACTURA, abonoBean.getIdFactura());								
		    }
			
			Vector vP = admPagosPorCaja.select(htRegistro);
			double importeCompensado = 0;
			int numeroPagos = 0;
		    if (vP!=null && vP.size()>0) {		    	
		        for (int g=0;g<vP.size();g++) {
		            FacPagosPorCajaBean pagosBean = (FacPagosPorCajaBean) vP.get(0);
		            numeroPagos++;
		            importeCompensado+=pagosBean.getImporte().doubleValue();
		        }
		    } else {
		        // no hay pagos que compensar, no hacemos nada.
		    }
			
			if (numeroPagos>0) {
			    // elimino los pagos por caja
			    String clavesPagoPorCaja[]= new String[]{FacPagosPorCajaBean.C_IDINSTITUCION,FacPagosPorCajaBean.C_IDABONO,FacPagosPorCajaBean.C_TIPOAPUNTE};
				correcto = admPagosPorCaja.deleteDirect(htRegistro, clavesPagoPorCaja);
			
				if(correcto && bAbonoConFacturaAsociada) {
				    // ahora tenemos que actualizar los importes y estado de la factura.
				    FacFacturaBean facturaBean = null;					
				    Hashtable ht = new Hashtable();
				    ht.put(FacFacturaBean.C_IDINSTITUCION,idInstitucion);
				    ht.put(FacFacturaBean.C_IDFACTURA,abonoBean.getIdFactura());
				    Vector v = facturaAdm.selectByPK(ht);
				    if (v!=null && v.size()>0) {
				        facturaBean = (FacFacturaBean) v.get(0);
				        
				        // AQUI VAMOS A MODIFICAR LOS VALORES DE IMPORTES
				        facturaBean.setImpTotalCompensado(new Double(facturaBean.getImpTotalCompensado().doubleValue()-importeCompensado));
				        facturaBean.setImpTotalPagado(new Double(facturaBean.getImpTotalPagado().doubleValue()-importeCompensado));
				        facturaBean.setImpTotalPorPagar(new Double(facturaBean.getImpTotalPorPagar().doubleValue()+importeCompensado));
				        
				        if (facturaAdm.update(facturaBean)) {
					        // AQUI VAMOS A MODIFICAR EL VALOR DE ESTADO
							facturaAdm.actualizarEstadoFactura(facturaBean, this.getUserName(request));
				        } else {
				            throw new ClsExceptions("Error al actualizar los importes de la factura: "+facturaAdm.getError());
				        }
						
				    } else {
				        throw new ClsExceptions("No se ha encontrado la factura buscada: "+idInstitucion+ " "+abonoBean.getIdFactura());
				    }
				} else if (!correcto){
					throw new ClsExceptions("Error al eliminar las compensaciones del abono (Pagos por caja de la factura): "+admPagosPorCaja.getError());
				}
			}
			
			
			htRegistro.remove(FacPagosPorCajaBean.C_TIPOAPUNTE);
			String clavesPagoAbonoEfectivo[]= new String[]{FacPagoAbonoEfectivoBean.C_IDINSTITUCION,FacPagoAbonoEfectivoBean.C_IDABONO};
			correcto = admPagoAbonoefectivo.deleteDirect(htRegistro, clavesPagoAbonoEfectivo);
			if(!correcto) {
				throw new ClsExceptions("Error al eliminar las compensaciones del abono (Pagos por caja del abono): "+admPagoAbonoefectivo.getError());
			}
				
			
			String clavesAbono[]= new String[]{FacAbonoBean.C_IDINSTITUCION,FacAbonoBean.C_IDABONO};
			correcto = admAbono.deleteDirect(htRegistro, clavesAbono);
			if(!correcto) {
				throw new ClsExceptions("Error al eliminar el abono: "+admAbono.getError());
			}
			
			
			tx.commit();
			result=exitoRefresco("messages.deleted.success",request);				
		} 
		catch (Exception e) {
			request.setAttribute("descOperation","messages.deleted.error");
			result="refresco";
			throwExcp("messages.deleted.error",new String[] {"modulo.productos"},e,tx); 
		}				
		
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

		String result="listar";
		Vector vector=new Vector();
		Hashtable criterios = new Hashtable();
		
		try {
			// Obtengo el UserBean y el identificador de la institucion
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String idInstitucion=user.getLocation();
			
			GenerarAbonosForm form = (GenerarAbonosForm) formulario;
			FacAbonoAdm admin=new FacAbonoAdm(this.getUserBean(request));
			
			// Relleno la hash con los criterios de busqueda
			criterios.put(FacAbonoBean.C_IDINSTITUCION,idInstitucion);
			criterios.put(FacAbonoBean.C_IDABONO,form.getBusquedaIdAbono());
			criterios.put(FacAbonoBean.C_NUMEROABONO,form.getNumeroAbono());
			criterios.put("ABONOFECHAINICIO",form.getFechaAbonoDesde());
			criterios.put("ABONOFECHAFIN",form.getFechaAbonoHasta());
			criterios.put(FacAbonoBean.C_IDPERSONA,form.getIdPersonaBusqueda());
			criterios.put(FacAbonoBean.C_IDFACTURA,form.getBusquedaIdFactura());
			criterios.put("FACTURAFECHAINICIO",form.getFechaFacturaDesde());
			criterios.put("FACTURAFECHAFIN",form.getFechaFacturaHasta());
			criterios.put("FORMAPAGO",form.getFormaPagoBusqueda());
			criterios.put("PAGADO",form.getPagadoBusqueda());
			criterios.put(FacAbonoBean.C_CONTABILIZADA,form.getContabilizadoBusqueda());
			criterios.put(FacAbonoBean.C_IDPAGOSJG,form.getTipoAbonoBusqueda());
			
			 HashMap databackup=new HashMap();
				
			 	if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
			 		databackup = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");
			 		PaginadorCaseSensitive paginador = (PaginadorCaseSensitive)databackup.get("paginador");
				     Vector datos=new Vector();
				
				
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
				
				 
				
			 if (paginador!=null){	
				if (pagina!=null){
					datos = paginador.obtenerPagina(Integer.parseInt(pagina));
				}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
					datos = paginador.obtenerPagina((paginador.getPaginaActual()));
				}
			 }	
			 
				
			 datos  = actualizarAbonos(admin,new Integer(idInstitucion),datos);
				
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);
				
					
				
				
		  }else{	
				
		  	    databackup=new HashMap();
				
				//obtengo datos de la consulta 			
		  	PaginadorCaseSensitive abonos = null;
			Vector datos = null;
		
		// Realizo la busqueda
			abonos=admin.getAbonos(criterios);
		databackup.put("paginador",abonos);
		if (abonos!=null){ 
		   datos = abonos.obtenerPagina(1);
		   datos  = actualizarAbonos(admin,new Integer(idInstitucion),datos);
		   databackup.put("datos",datos);
		   request.getSession().setAttribute("DATAPAGINADOR",databackup);
		} 
		  }
	//	request.setAttribute("container", vector);
		
		/*// Obtengo el maximo identificador 
		maxId=admin.getMaxID(idInstitucion).toString();
		request.setAttribute("maximo", maxId);*/
		
	} 
	catch (Exception e) { 
		throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
	}		

	return (result);
		
		
	}
	private Vector actualizarAbonos(FacAbonoAdm admAbono ,Integer idInstitucion,Vector datos) throws SIGAException,ClsExceptions{
		
		

		for (int i=0;i<datos.size();i++) 
		{
			Row fila = (Row)datos.get(i);
			Hashtable registro = (Hashtable) fila.getRow();
			String idAbono = UtilidadesHash.getString(registro, FacAbonoBean.C_IDABONO);
			//Miramos si existe algn pago del abono(caja o banco, NO compensacion)
			boolean isAbonoConPago = admAbono.isAbonoConPago(idAbono, idInstitucion);
			//Miramos si esta contabilizado la factura o el abono
			String contabilizado = UtilidadesHash.getString(registro, FacAbonoBean.C_CONTABILIZADA);
			String facturaContabilizado = UtilidadesHash.getString(registro, "FACTURACONTABILIZADA");
			boolean isContabilizado =  (contabilizado!=null && contabilizado.equals(ClsConstants.FACTURA_ABONO_CONTABILIZADA))
				|| (facturaContabilizado!=null && facturaContabilizado.equals(ClsConstants.FACTURA_ABONO_CONTABILIZADA));
			
			
			
			//Miramos si esta pagado
			boolean isPermitidoBorrar = !isAbonoConPago && !isContabilizado;
			registro.put("ESPERMITIDOBORRAR", new Boolean(isPermitidoBorrar));
			

		}
		
		return datos;
		
		
	}
	
	
}

