/*
 * Created on 07-abr-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.facturacionSJCS.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.beans.FcsHitoGeneralAdm;
import com.siga.beans.FcsHitoGeneralBean;
import com.siga.beans.FcsPagosJGAdm;
import com.siga.beans.FcsPagosJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * @author S230298
 *
 */
public class DatosImporteTotalesPagoAction extends MasterAction{
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		try {
			// Recogemos los parámetros de las pestanhas 
			String idPago = (String)request.getParameter("idPagosJG");
			String idInstitucion = (String)request.getParameter("idInstitucion");

			// Recuperamos el idFacturacion asociado
			String idFacturacion = "";
			FcsPagosJGAdm pagosJGAdm = new FcsPagosJGAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, FcsPagosJGBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, FcsPagosJGBean.C_IDPAGOSJG, idPago);
			Vector v = pagosJGAdm.selectByPK(claves);
			if ((v != null) && (v.size() == 1)) {
				idFacturacion = "" + ((FcsPagosJGBean)v.get(0)).getIdFacturacion();
			}
			
			FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm (this.getUserBean(request));
			
			// Datos Facturacion
			Hashtable imporFacturacion   = facturacionAdm.getImportesTotalesFacturacion     (idInstitucion, idFacturacion);

			// Datos Pagos Realizados
			Hashtable imporPagoRealizado = facturacionAdm.getImportesTotalesPagosRealizados (idInstitucion, idFacturacion);

			// Datos Pago actual
			Hashtable imporPagoActual    = facturacionAdm.getImportesTotalesPagoActual      (idInstitucion, idPago);

			FcsHitoGeneralAdm hitoAdm = new FcsHitoGeneralAdm (this.getUserBean(request));
			Vector vHitos = hitoAdm.select("");
			Hashtable hitos = new Hashtable ();
			UtilidadesHash.set(hitos, "" + ((FcsHitoGeneralBean)vHitos.get(0)).getIdHitoGeneral(), ((FcsHitoGeneralBean)vHitos.get(0)).getDescripcion()); 
			UtilidadesHash.set(hitos, "" + ((FcsHitoGeneralBean)vHitos.get(1)).getIdHitoGeneral(), ((FcsHitoGeneralBean)vHitos.get(1)).getDescripcion()); 
			UtilidadesHash.set(hitos, "" + ((FcsHitoGeneralBean)vHitos.get(2)).getIdHitoGeneral(), ((FcsHitoGeneralBean)vHitos.get(2)).getDescripcion()); 
			UtilidadesHash.set(hitos, "" + ((FcsHitoGeneralBean)vHitos.get(3)).getIdHitoGeneral(), ((FcsHitoGeneralBean)vHitos.get(3)).getDescripcion()); 
			
			String nombreInstitucion = "";
			try{
				//Consultamos el nombre de la institucion
				CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
				nombreInstitucion = (String)institucionAdm.getNombreInstitucion(""+this.getIDInstitucion(request));
			}
			catch(ClsExceptions e){
				ClsLogging.writeFileLogError("Error: No se ha podido recuperar el nombre del Colegio", e,3);
			}

			request.setAttribute("nombreInstitucion",  nombreInstitucion);
			request.setAttribute("datosFacturacion",   imporFacturacion);
			request.setAttribute("datosPagoRealizado", imporPagoRealizado);
			request.setAttribute("datosPagoActual",    imporPagoActual);
			request.setAttribute("datosConcepto", 	   hitos);
		}
		catch (Exception e) {
		}

		return "inicio";
	}
}
