/*
 * VERSIONES:
 * 
 * miguel.villegas - 11-03-2005 - Creacion
 *	
 */

/**
 * Clase action para el mantenimiento del desglose de los abonos.<br/>
 * Gestiona la edicion, consulta y borrado de las lineas de los abonos  
 */

package com.siga.facturacion.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.beans.ConsPLFacturacion;
import com.siga.beans.FacAbonoAdm;
import com.siga.beans.FacAbonoBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacLineaAbonoAdm;
import com.siga.beans.FacLineaAbonoBean;
import com.siga.facturacion.form.AbonosLineasForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


public class AbonosLineasAction extends MasterAction {

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
		Vector seleccionados= new Vector();
		Vector escogido= new Vector();
		String contabilizado= ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA;
		ClsExceptions ee=null;

		try {
			String volver = null;
			if (request.getParameter("botonVolver")!=null )
				volver = (String)request.getParameter("botonVolver");
			else
				volver = "NO";
			
			// Obtengo el UserBean y los diferentes parametros recibidos
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String accion = (String)request.getParameter("accion");
			String idAbono = (String)request.getParameter("idAbono");
			String idInstitucionAbono = (String)request.getParameter("idInstitucion");
			
			String idInstitucion=user.getLocation();


			// Obtengo manejadores para accesos a las BBDDs y obtengo el desglose
			FacLineaAbonoAdm abonoPorLineasAdm = new FacLineaAbonoAdm(this.getUserBean(request));
			seleccionados=abonoPorLineasAdm.getDesglose(idAbono,idInstitucionAbono);
			
			// MAV 12/7/05 a instancias JG
			// Obtengo si contabilizada para permitir o no modificaciones en el desglose del abono
			FacAbonoAdm abonoAdm = new FacAbonoAdm(this.getUserBean(request));
			escogido=abonoAdm.getAbono(idInstitucionAbono,idAbono);
			if (!escogido.isEmpty()){
				if (((Row)escogido.firstElement()).getRow().get(FacAbonoBean.C_CONTABILIZADA)!=null){
					contabilizado=((Row)escogido.firstElement()).getRow().get(FacAbonoBean.C_CONTABILIZADA).toString();					
				}
				else{
					ee = new ClsExceptions("No existe informacion relativa a contabilizado para este abono");
				}
			}
			else{
				ee = new ClsExceptions("No existe informacion relativa a contabilizado para este abono");
			}
			
						
			// Paso de parametros empleando request
			//idPagoJG:Esto es para saber si estamos en abonos de SJCS en Censo > Ficha > Turno Oficio > Facturaciones > Pagos
			request.setAttribute("idPagoJG", ((Row)escogido.firstElement()).getRow().get("IDPAGOSJG"));
			request.setAttribute("IDABONO", idAbono);
			request.setAttribute("IDINSTITUCION", idInstitucion);			
			request.setAttribute("ACCION", accion);
			request.setAttribute("container", seleccionados);
			request.setAttribute("contabilizado", contabilizado);
			request.setAttribute("volver", volver);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}				
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="abrirAvanzada";

		// Cuentas de impNeto, impIva e impTotal
		Row row =new Row();
		String resultado=(new Double((new Double(row.getString(FacLineaAbonoBean.C_CANTIDAD))).doubleValue()*(new Double(row.getString(FacLineaAbonoBean.C_CANTIDAD))).doubleValue())).toString();
		String resultado2=(new Double(((new Double(resultado)).doubleValue()*(new Double(row.getString(FacLineaAbonoBean.C_IVA))).doubleValue())/(new Double("100").doubleValue()))).toString();
		String resultado3= new Double(new Double(resultado2).doubleValue() + new Double(resultado).doubleValue()).toString();
		
		return result;
		
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "buscar";
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
		
		String result="";

		try {
			Vector ocultos=new Vector();
			Vector entrada=new Vector();				
			Vector pagos= new Vector();
			Vector info= new Vector();
			Hashtable hashInfo= new Hashtable();
			Hashtable hashPagos= new Hashtable();

			result="editar";
			AbonosLineasForm form = (AbonosLineasForm) formulario;
			FacLineaAbonoAdm admin=new FacLineaAbonoAdm(this.getUserBean(request));
			Object remitente=(Object)"modificar";
			request.setAttribute("modelo",remitente);
		
			// Mostrar valores del formulario en MantenimientoProductos (posible traslado a editar o abrir avanzado)
			ocultos = (Vector)form.getDatosTablaOcultos(0);					
			entrada=admin.getLineaAbono((String)ocultos.get(1),(String)ocultos.get(0),(String)ocultos.get(2));
			
			// Informacion adicional para calculo cantidades pendientes de abono
			FacAbonoAdm abonoAdm = new FacAbonoAdm(this.getUserBean(request));
			info=abonoAdm.getAbono((String)ocultos.get(1),(String)ocultos.get(0));
			hashInfo=((Row)info.firstElement()).getRow();
			if (!((String)hashInfo.get(FacAbonoBean.C_IDFACTURA)).equalsIgnoreCase("")){
				FacFacturaAdm facturaAdm = new FacFacturaAdm(this.getUserBean(request));
				hashPagos=facturaAdm.getFacturaDatosGeneralesTotalFactura(new Integer((String)ocultos.get(1)),(String)hashInfo.get(FacAbonoBean.C_IDFACTURA),ClsConstants.LENGUAJE_ESP);
			}	
			
			// Paso valores originales del registro al session para tratar siempre copn los mismos valores
			// y no los de posibles modificaciones
			request.getSession().setAttribute("DATABACKUP", ((Row)entrada.firstElement()).getRow());
			
			// Paso valores para dar valores iniciales al formulario			
			request.setAttribute("container", entrada);
			
			// Paso parametros de informacion adicional para calculo cantidades pendientes de abono
			request.setAttribute("IDFACTURA", hashInfo.get(FacAbonoBean.C_IDFACTURA));
			request.setAttribute("TOTAL_FACTURA", hashPagos.get("TOTAL_FACTURA"));
			request.setAttribute("TOTAL_ABONO", form.getImporteTotal());
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}			
		return (result);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="";

		try {
			Vector ocultos=new Vector();
			Vector entrada=new Vector();				
			Vector pagos= new Vector();
			Vector info= new Vector();
			Hashtable hashInfo= new Hashtable();
			Hashtable hashPagos= new Hashtable();

			result="editar";
			AbonosLineasForm form = (AbonosLineasForm) formulario;
			FacLineaAbonoAdm admin=new FacLineaAbonoAdm(this.getUserBean(request));
			Object remitente=(Object)"ver";
			request.setAttribute("modelo",remitente);
		
			// Mostrar valores del formulario en MantenimientoProductos (posible traslado a editar o abrir avanzado)
			ocultos = (Vector)form.getDatosTablaOcultos(0);					
			entrada=admin.getLineaAbono((String)ocultos.get(1),(String)ocultos.get(0),(String)ocultos.get(2));
			
			// Informacion adicional para calculo cantidades pendientes de abono
			FacAbonoAdm abonoAdm = new FacAbonoAdm(this.getUserBean(request));
			info=abonoAdm.getAbono((String)ocultos.get(1),(String)ocultos.get(0));
			hashInfo=((Row)info.firstElement()).getRow();
			if (!((String)hashInfo.get(FacAbonoBean.C_IDFACTURA)).equalsIgnoreCase("")){
				FacFacturaAdm facturaAdm = new FacFacturaAdm(this.getUserBean(request));
				hashPagos=facturaAdm.getFacturaDatosGeneralesTotalFactura(new Integer((String)ocultos.get(1)),(String)hashInfo.get(FacAbonoBean.C_IDFACTURA),ClsConstants.LENGUAJE_ESP);
			}	
			
			// Paso valores originales del registro al session para tratar siempre copn los mismos valores
			// y no los de posibles modificaciones
			request.getSession().setAttribute("DATABACKUP", ((Row)entrada.firstElement()).getRow());
			
			// Paso valores para dar valores iniciales al formulario			
			request.setAttribute("container", entrada);
			
			// Paso parametros de informacion adicional para calculo cantidades pendientes de abono
			request.setAttribute("IDFACTURA", hashInfo.get(FacAbonoBean.C_IDFACTURA));
			request.setAttribute("TOTAL_FACTURA", hashPagos.get("TOTAL_FACTURA"));
			request.setAttribute("TOTAL_ABONO", form.getImporteTotal());
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
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
		try{
		
			Hashtable hash=new Hashtable();	
			Vector pagos= new Vector();
			Vector info= new Vector();
			Hashtable hashInfo= new Hashtable();
			Hashtable hashPagos= new Hashtable();
			 			
			// Obtengo los datos del formulario (idPersona e idInstitucion)
			AbonosLineasForm miForm = (AbonosLineasForm)formulario;
			String idAbono=miForm.getIdAbono();
			String idInstitucion=miForm.getIdInstitucion();
			
			// Informacion adicional para calculo cantidades pendientes de abono
			FacAbonoAdm abonoAdm = new FacAbonoAdm(this.getUserBean(request));
			info=abonoAdm.getAbono(miForm.getIdInstitucion(),miForm.getIdAbono());
			hashInfo=((Row)info.firstElement()).getRow();
			if (!((String)hashInfo.get(FacAbonoBean.C_IDFACTURA)).equalsIgnoreCase("")){
				FacFacturaAdm facturaAdm = new FacFacturaAdm(this.getUserBean(request));
				hashPagos=facturaAdm.getFacturaDatosGeneralesTotalFactura(new Integer(miForm.getIdInstitucion()),(String)hashInfo.get(FacAbonoBean.C_IDFACTURA),"ES");
			}

			// Paso de parametros empleando request
			request.setAttribute("IDABONO",idAbono);
			request.setAttribute("IDINSTITUCION", idInstitucion);			
			
			// Paso el origen
			Object remitente=(Object)"insertar";
			request.setAttribute("modelo",remitente);
			
			// Paso parametros de informacion adicional para calculo cantidades pendientes de abono
			request.setAttribute("IDFACTURA", hashInfo.get(FacAbonoBean.C_IDFACTURA));
			request.setAttribute("TOTAL_FACTURA", hashPagos.get("TOTAL_FACTURA"));
			request.setAttribute("TOTAL_ABONO", miForm.getImporteTotal());
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
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
		
		String result="insertar";
		UserTransaction tx = null;
		Hashtable hash = new Hashtable();
		
		try {		 				

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			FacLineaAbonoAdm admin=new FacLineaAbonoAdm(this.getUserBean(request));			
 			
			// Obtengo los datos del formulario
			AbonosLineasForm miForm = (AbonosLineasForm)formulario;
			
			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			hash.put(FacLineaAbonoBean.C_IDINSTITUCION, miForm.getIdInstitucion());									
			hash.put(FacLineaAbonoBean.C_IDABONO, miForm.getIdAbono());
			hash.put(FacLineaAbonoBean.C_NUMEROLINEA, admin.getNuevoID(miForm.getIdInstitucion(),miForm.getIdAbono()));
			hash.put(FacLineaAbonoBean.C_DESCRIPCIONLINEA, miForm.getDescripcion());
			hash.put(FacLineaAbonoBean.C_CANTIDAD, miForm.getCantidad());
			hash.put(FacLineaAbonoBean.C_PRECIOUNITARIO, miForm.getPrecio());									
			hash.put(FacLineaAbonoBean.C_IVA, miForm.getIva());
			
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();	
			
			if (admin.insert(hash)){
				// Actualizo estado del abono
				ConsPLFacturacion plFacturacion=new ConsPLFacturacion();
				plFacturacion.actualizarEstadoAbono(new Integer(miForm.getIdInstitucion()),new Long (miForm.getIdAbono()),this.getUserName(request));
				result=exitoModal("messages.updated.success",request);
				tx.commit();
			}	
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
		}
		return (result);
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
		UserTransaction tx = null;
		Hashtable hash = new Hashtable();
		Hashtable hashOriginal = new Hashtable();
		
		try {		 				

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			FacLineaAbonoAdm admin=new FacLineaAbonoAdm(this.getUserBean(request));			
 			
			// Obtengo los datos del formulario
			AbonosLineasForm miForm = (AbonosLineasForm)formulario;

			// Cargo una hastable con los valores originales del registro sobre el que se realizará la modificacion						
			hashOriginal=(Hashtable)request.getSession().getAttribute("DATABACKUP");
			
			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			hash.put(FacLineaAbonoBean.C_IDINSTITUCION, miForm.getIdInstitucion());									
			hash.put(FacLineaAbonoBean.C_IDABONO, miForm.getIdAbono());
			hash.put(FacLineaAbonoBean.C_NUMEROLINEA, miForm.getNumeroLinea());
			hash.put(FacLineaAbonoBean.C_DESCRIPCIONLINEA, miForm.getDescripcion());
			hash.put(FacLineaAbonoBean.C_CANTIDAD, miForm.getCantidad());
			hash.put(FacLineaAbonoBean.C_PRECIOUNITARIO, miForm.getPrecio());									
			hash.put(FacLineaAbonoBean.C_IVA, miForm.getIva());
			
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();	
			
			if (admin.update(hash,hashOriginal)){
				// Actualizo estado del abono
				ConsPLFacturacion plFacturacion=new ConsPLFacturacion();
				plFacturacion.actualizarEstadoAbono(new Integer(miForm.getIdInstitucion()),new Long(miForm.getIdAbono()),this.getUserName(request));
				result=exitoModal("messages.updated.success",request);
				tx.commit();
			}	
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
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
		
		String result="borrar";
		Vector fila = new Vector();
		boolean correcto=true;		
		Hashtable hash = new Hashtable();
		UserTransaction tx=null;
		String idAbono = "";
		String idInstitucion = "";
		String numeroLinea = "";
		
		try{
			
			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			FacLineaAbonoAdm admin=new FacLineaAbonoAdm(this.getUserBean(request));
			// Comienzo control de transacciones
			tx = usr.getTransaction(); 			
			// Obtengo los datos del formulario		
			AbonosLineasForm miform = (AbonosLineasForm)formulario;		
			fila = miform.getDatosTablaOcultos(0);
			idAbono = (String)fila.get(0);
			idInstitucion = (String)fila.get(1);
			numeroLinea = (String)fila.get(2);
					
			// Cargo la tabla hash con los valores del formulario para eliminar en PYS_PRODUCTOSINSTITUCION y PYS_FORMAPAGOPRODUCTO		
			hash.put(FacLineaAbonoBean.C_IDINSTITUCION,idInstitucion);
			hash.put(FacLineaAbonoBean.C_IDABONO,idAbono);
			hash.put(FacLineaAbonoBean.C_NUMEROLINEA,numeroLinea);
			
			tx.begin();
			
			//Realizo el borrado del abono
			correcto=admin.delete(hash);

			// Actualizo estado del abono
			if (correcto){
				ConsPLFacturacion plFacturacion=new ConsPLFacturacion();
				plFacturacion.actualizarEstadoAbono(new Integer(idInstitucion),new Long(idAbono),this.getUserName(request));
			}
			
			if (correcto){
				tx.commit();
				result=exitoRefresco("messages.deleted.success",request);				
			}	
			else{
				request.setAttribute("descOperation","messages.deleted.error");
				result="refresco";					
			}
		} 
		catch (Exception e) { 
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
		
		return (result);
				
	}

}
