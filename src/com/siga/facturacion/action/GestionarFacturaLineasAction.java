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
				else request.setAttribute("modificar", new Boolean (false));
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
/*
			UtilidadesHash.set(hash, GenParametrosBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(hash, GenParametrosBean.C_MODULO, ClsConstants.MODULO_FACTURACION);
			UtilidadesHash.set(hash, GenParametrosBean.C_PARAMETRO, "MODIFICAR_DESCRIPCION");
			Vector vAux = paramAdm.select(hash);
			if (vAux != null) {
				GenParametrosBean beanParametro = (GenParametrosBean)vAux.get(0);
				if (beanParametro.getValor().equalsIgnoreCase("s"))
					bEditarDescripcion = new Boolean(true);
			}
*/			
			aux = paramAdm.getValor(idInstitucion.toString(),ClsConstants.MODULO_FACTURACION,"MODIFICAR_IMPORTE_UNITARIO",null);
			if (aux != null && aux.equalsIgnoreCase("s")) {
				bEditarPrecio = new Boolean(true);
			}
/*
			UtilidadesHash.set(hash, GenParametrosBean.C_PARAMETRO, "MODIFICAR_IMPORTE_UNITARIO");
			vAux = paramAdm.select(hash);
			if (vAux != null) {
				GenParametrosBean beanParametro = (GenParametrosBean)vAux.get(0);
				if (beanParametro.getValor().equalsIgnoreCase("s"))
					bEditarPrecio = new Boolean(true);
			}
*/			

			aux = paramAdm.getValor(idInstitucion.toString(),ClsConstants.MODULO_FACTURACION,"MODIFICAR_IVA",null);
			if (aux != null && aux.equalsIgnoreCase("s")) {
				bEditarIVA = new Boolean(true);
			}
/*
			UtilidadesHash.set(hash, GenParametrosBean.C_PARAMETRO, "MODIFICAR_IVA");
			vAux = paramAdm.select(hash);
			if (vAux != null) {
				GenParametrosBean beanParametro = (GenParametrosBean)vAux.get(0);
				if (beanParametro.getValor().equalsIgnoreCase("s"))
					bEditarIVA = new Boolean(true);
			}
*/
			request.setAttribute("editarDescripcion", bEditarDescripcion);
			request.setAttribute("editarPrecio", bEditarPrecio);
			request.setAttribute("editarIVA", bEditarIVA);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null); 
		}				
		return modo;
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		UserTransaction t = this.getUserBean(request).getTransaction();
		try{
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;			
			FacLineaFacturaBean beanLinea = (FacLineaFacturaBean) request.getSession().getAttribute("DATABACKUP");
			FacLineaFacturaAdm lineasAdm = new FacLineaFacturaAdm(this.getUserBean(request));
			
			beanLinea.setDescripcion(miForm.getDatosLineaDescripcion());
			beanLinea.setPrecioUnitario(miForm.getDatosLineaPrecio());
			beanLinea.setIva(miForm.getDatosLineaIVA());
			t.begin();
			if(lineasAdm.update(beanLinea)) {
				t.commit();
			}
			else {
				throwExcp("messages.updated.error",new String[] {"modulo.facturacion"},new ClsExceptions("messages.updated.error"),t); 
			}
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, t); 
		}				
		return exitoModal("messages.updated.success", request);
	}
	
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

