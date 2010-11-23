/*
 * Created on 21-mar-2005
 *
 */
package com.siga.facturacion.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.facturacion.form.GestionarFacturaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 *
 */
public class GestionarFacturaLineasAction extends MasterAction 
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try{
			// Obtengo los diferentes parametros recibidos
			String accion =  (String)request.getParameter("accion");
			String idFactura = (String)request.getParameter("idFactura");
			Integer idInstitucion = new Integer((String)request.getParameter("idInstitucion"));

			String volver = null;
			try {				
				if (request.getParameter("botonVolver")==null)
					volver = "NO";
				else
					volver = (String)request.getParameter("botonVolver");
			} catch (Exception e){
				volver = "NO";
			}
			
			// Datos de lineas
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set(claves, FacLineaFacturaBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, FacLineaFacturaBean.C_IDFACTURA, idFactura);

			FacLineaFacturaAdm lineasAdm = new FacLineaFacturaAdm(this.getUserBean(request));
			Vector vLineas = lineasAdm.select(claves);
			
			FacFacturaAdm facturaAdm = new FacFacturaAdm(this.getUserBean(request));
			Vector vFactura = facturaAdm.select(claves);
			if (vFactura != null) {
				FacFacturaBean beanFactura = (FacFacturaBean)vFactura.get(0);
				if (beanFactura.getNumeroFactura() == null              || 
					beanFactura.getNumeroFactura().equalsIgnoreCase("") || 
					beanFactura.getNumeroFactura().equalsIgnoreCase("0")
				   )  
					request.setAttribute("modificar", new Boolean (true));
				else request.setAttribute("modificar", new Boolean (true));
			}
			else request.setAttribute("modificar", new Boolean (false));
			
			request.getSession().setAttribute("DATABACKUP", vLineas);
			request.setAttribute("modoRegistroBusqueda", accion);
			request.setAttribute("volver", volver);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null); 
		}				
		return "inicio";
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		String modo = "";
		try{
			// Obtengo los diferentes parametros recibidos
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;
			Integer idInstitucion = new Integer((String)miForm.getDatosTablaOcultos(0).get(0));
			
			String idFactura = (String)miForm.getDatosTablaOcultos(0).get(1);
			Long numeroLinea = new Long((String)miForm.getDatosTablaOcultos(0).get(2));
			modo = miForm.getModo().toLowerCase();

			// Recuperamos el bean asociado a la linea
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set(claves, FacLineaFacturaBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, FacLineaFacturaBean.C_IDFACTURA, idFactura);
			UtilidadesHash.set(claves, FacLineaFacturaBean.C_NUMEROLINEA, numeroLinea);
			
     		//Recuperamos el numerofactura, si tiene numero de factura solo se puede cambiar la descripción de la linea, al editar.
			FacFacturaAdm facturaAdm = new FacFacturaAdm(this.getUserBean(request));
			String numerofactura=facturaAdm.getNumerofactura(idInstitucion, idFactura);
			
			// Recuperamos la linea
			FacLineaFacturaAdm lineasAdm = new FacLineaFacturaAdm(this.getUserBean(request));
			Vector vLineas = lineasAdm.selectByPK(claves);
			request.getSession().setAttribute("DATABACKUP", (FacLineaFacturaBean) vLineas.get(0));
			request.setAttribute("modo", modo);

			// Recuperamos los permisos sobre los campos a modificar
			Boolean bEditarIVA = new Boolean(false);
			Boolean bEditarPrecio = new Boolean(false);
			Boolean bEditarDescripcion = new Boolean(false);

		
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			Hashtable hash = new Hashtable();
			String aux = paramAdm.getValor(idInstitucion.toString(),ClsConstants.MODULO_FACTURACION,"MODIFICAR_DESCRIPCION",null);
			if (aux != null && aux.equalsIgnoreCase("s")) {
				bEditarDescripcion = new Boolean(true);
			}

			aux = paramAdm.getValor(idInstitucion.toString(),ClsConstants.MODULO_FACTURACION,"MODIFICAR_IMPORTE_UNITARIO",null);
			if (aux != null && aux.equalsIgnoreCase("s")) {
				bEditarPrecio = new Boolean(true);
			}

			aux = paramAdm.getValor(idInstitucion.toString(),ClsConstants.MODULO_FACTURACION,"MODIFICAR_IVA",null);
			if (aux != null && aux.equalsIgnoreCase("s")) {
				bEditarIVA = new Boolean(true);
			}

			//Si tiene un numero de factura, se modifica solo la descripcio de las lineas, el resto de campos no se pueden modificar.
			if (!numerofactura.equals("")){			
					  aux = paramAdm.getValor(idInstitucion.toString(),ClsConstants.MODULO_FACTURACION,"MODIFICAR_DESCRIPCION",null);
					 if (aux != null && aux.equalsIgnoreCase("s")) {
						 bEditarDescripcion = new Boolean(true);
					 }			
					 bEditarIVA = new Boolean(false);
					 bEditarPrecio = new Boolean(false);
			}
				
			request.setAttribute("editarDescripcion", bEditarDescripcion);
			request.setAttribute("editarPrecio", bEditarPrecio);
			request.setAttribute("editarIVA", bEditarIVA);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null); 
		}				
		return modo;
	}
	
	/**
	 * Ejecuta la modificacion de una linea en BD
	 */
	protected String modificar (ActionMapping mapping,
								MasterForm formulario,
								HttpServletRequest request,
								HttpServletResponse response)
		throws ClsExceptions, SIGAException
	{
		//Variables generales
		UserTransaction tx = null;
		boolean actualizado = false;
		
		try {
			//Controles generales
			UsrBean usr = this.getUserBean(request);
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;			
			FacLineaFacturaAdm lineasAdm = new FacLineaFacturaAdm(usr);
			FacFacturaAdm facturaAdm = new FacFacturaAdm(usr);
			
			tx = usr.getTransaction();
			tx.begin();
			actualizado = false;
			
			//actualizando linea factura
			FacLineaFacturaBean beanLinea = (FacLineaFacturaBean) request.getSession().getAttribute("DATABACKUP");
			beanLinea.setDescripcion(miForm.getDatosLineaDescripcion());
			beanLinea.setPrecioUnitario(miForm.getDatosLineaPrecio());
			beanLinea.setIva(miForm.getDatosLineaIVA());
			if (lineasAdm.update(beanLinea))
				actualizado = true;
			
			//actualizando total factura
			String sql = 
				"update fac_factura " +
				"   set imptotalneto              = pkg_siga_totalesfactura.totalneto(idinstitucion, " +
				"                                                                     idfactura), " +
				"       imptotaliva               = pkg_siga_totalesfactura.totaliva(idinstitucion, " +
				"                                                                    idfactura), " +
				"       imptotal                  = pkg_siga_totalesfactura.total(idinstitucion, " +
				"                                                                 idfactura), " +
				"       imptotalanticipado        = pkg_siga_totalesfactura.totalanticipado(idinstitucion, " +
				"                                                                           idfactura), " +
				"       imptotalpagadoporcaja     = pkg_siga_totalesfactura.totalpagadoporcaja(idinstitucion, " +
				"                                                                              idfactura), " +
				"       imptotalpagadosolocaja    = pkg_siga_totalesfactura.totalpagadosolocaja(idinstitucion, " +
				"                                                                               idfactura), " +
				"       imptotalpagadosolotarjeta = pkg_siga_totalesfactura.totalpagadosolotarjeta(idinstitucion, " +
				"                                                                                  idfactura), " +
				"       imptotalpagadoporbanco    = pkg_siga_totalesfactura.totalpagadoporbanco(idinstitucion, " +
				"                                                                               idfactura), " +
				"       imptotalpagado            = pkg_siga_totalesfactura.totalpagado(idinstitucion, " +
				"                                                                       idfactura), " +
				"       imptotalporpagar          = pkg_siga_totalesfactura.pendienteporpagar(idinstitucion, " +
				"                                                                             idfactura), " +
				"       imptotalcompensado        = pkg_siga_totalesfactura.totalcompensado(idinstitucion, " +
				"                                                                           idfactura), " +
				"       fechamodificacion         = sysdate, " +
				"       usumodificacion           = "+usr.getUserName()+" " +
				" where idinstitucion = "+beanLinea.getIdInstitucion()+" " +
				"   and idfactura = "+beanLinea.getIdFactura()+" ";
			if (facturaAdm.updateSQL(sql))
				actualizado = true;
			
			if (actualizado)
				tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", 
					new String[] {"modulo.facturacion"}, 
					e, tx); 
		}
		finally {
			if (! actualizado)
				throwExcp("messages.updated.error", 
						new String[] {"modulo.facturacion"}, 
						new ClsExceptions("messages.updated.error"), tx); 
		}
		
		return exitoModal("messages.updated.success", request);
	} //modificar()
	
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		String modo = "";
		try{
			// Obtengo los diferentes parametros recibidos
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;
			Integer idInstitucion = new Integer((String)miForm.getDatosTablaOcultos(0).get(0));
			String idFactura = (String)miForm.getDatosTablaOcultos(0).get(1);
			Long numeroLinea = new Long((String)miForm.getDatosTablaOcultos(0).get(2));
			modo = miForm.getModo().toLowerCase();

			// Recuperamos el bean asociado a la linea
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set(claves, FacLineaFacturaBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, FacLineaFacturaBean.C_IDFACTURA, idFactura);
			UtilidadesHash.set(claves, FacLineaFacturaBean.C_NUMEROLINEA, numeroLinea);

			FacLineaFacturaAdm lineasAdm = new FacLineaFacturaAdm(this.getUserBean(request));
			Vector vLineas = lineasAdm.selectByPK(claves);
			request.getSession().setAttribute("DATABACKUP", (FacLineaFacturaBean) vLineas.get(0));
			request.setAttribute("modo", modo);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null); 
		}				
		return modo;
	}
}

