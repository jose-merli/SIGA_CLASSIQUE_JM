
/*
 * Created on May 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.informes;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.FcsEstadosPagosAdm;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.beans.FcsFacturacionJGBean;
import com.siga.beans.FcsPagosEstadosPagosAdm;
import com.siga.beans.FcsPagosJGAdm;
import com.siga.beans.FcsPagosJGBean;
import com.siga.informes.MasterReport;

/**
 * Clase para el Mantenimiento de los Informes generados en formato PDF para los Ppagos, Facturaciones.
 * @author david.sanchezp
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InformeFichaPago extends MasterReport {
	
	
	/**
	 * Metodo que implementa el modo generarPago
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public String generarFichaPago (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws ClsExceptions {
		
		String salida = null;
		String generacionPdfOK = "ERROR";
	
		try {
			if (this.generarInforme(request,ClsConstants.PLANTILLA_FO_FICHAPAGO)){
				generacionPdfOK = "OK";			
			}				
			request.setAttribute("generacionOK",generacionPdfOK);			
			salida = "descarga";
		}catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		}
		return salida;	
	}
	
		
	protected String reemplazarDatos(HttpServletRequest request, String plantillaFO) throws ClsExceptions{
		Hashtable hashDatos = null;
		FcsPagosJGBean beanPago = null;
		FcsFacturacionJGBean beanFacturacion = null;
		
		try {		
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			FcsPagosJGAdm admPago = new FcsPagosJGAdm(usr);
			FcsEstadosPagosAdm admNombreEstadosPago = new FcsEstadosPagosAdm(usr);
			FcsPagosEstadosPagosAdm admEstadoPago = new FcsPagosEstadosPagosAdm(usr); 
			FcsFacturacionJGAdm admFacturacion = new FcsFacturacionJGAdm(usr);
			//Obtenemos el formulario y sus datos:
			//miform = (MantenimientoInformesForm)formulario;
			String idPago = request.getParameter("idPago");//miform.getIdPago();
			String idInstitucion = usr.getLocation();
			String idEstado = admEstadoPago.getIdEstadoPago(idInstitucion, idPago);			

			//Obtengo el bean de la facturacion:
			String where = " WHERE "+FcsPagosJGBean.C_IDINSTITUCION+"="+usr.getLocation()+
			" AND "+FcsPagosJGBean.C_IDPAGOSJG+"="+idPago;
			beanPago = (FcsPagosJGBean)admPago.select(where).get(0);
			
			where = " WHERE "+FcsFacturacionJGBean.C_IDINSTITUCION+"="+usr.getLocation()+
			" AND "+FcsFacturacionJGBean.C_IDFACTURACION+"="+beanPago.getIdFacturacion();
			beanFacturacion = (FcsFacturacionJGBean)admFacturacion.select(where).get(0);
			
			// Relleno la hash con los datos que me faltan:
			hashDatos = new Hashtable();
			//hashDatos.put("IDIOMA",idioma);				
			//hashDatos.put("IDINSTITUCION",beanPago.getIdInstitucion().toString());
			hashDatos.put("IDPAGO",beanPago.getIdPagosJG().toString());
			hashDatos.put("IDFACTURACION",beanPago.getIdFacturacion().toString());
			hashDatos.put("DESCRIPCION_PAGO",beanPago.getNombre());
			hashDatos.put("FACTURACION_PAGO",beanFacturacion.getNombre());
			hashDatos.put("PORCENTAJETURNOS_PAGO",beanPago.getPorcentajeOficio().toString());
			hashDatos.put("PORCENTAJEGUARDIAS_PAGO",beanPago.getPorcentajeGuardias().toString());
			hashDatos.put("PORCENTAJESOJ_PAGO",beanPago.getPorcentajeSOJ().toString());
			hashDatos.put("PORCENTAJEEJG_PAGO",beanPago.getPorcentajeEJG().toString());
			hashDatos.put("ESTADO_PAGO",admNombreEstadosPago.getNombreEstadoPago(idEstado));//Estado del Pago				
			hashDatos.put("FECHAEJECUCION_PAGO",GstDate.getFormatedDateShort(usr.getLanguage(),admEstadoPago.getFechaEstadoPago(idInstitucion,idPago,idEstado)));//Fecha del último Estado del Pago				
		} catch (Exception e) {
			hashDatos = null;
		}
		return this.reemplazaVariables(hashDatos,plantillaFO);	
	}
	
	
	
}