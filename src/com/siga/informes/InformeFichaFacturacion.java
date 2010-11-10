
/*
 * Created on May 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.informes;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.FcsFactEstadosFacturacionAdm;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.informes.MasterReport;

/**
 * Clase para el Mantenimiento de los Informes generados en formato PDF para los Ppagos, Facturaciones.
 * @author david.sanchezp
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InformeFichaFacturacion extends MasterReport {
	
	/**
	 * Metodo que implementa el modo generarFacturacion
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public String generarFichaFacturacion (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response)	
	throws ClsExceptions{
			
			String salida = null;
			String generacionPdfOK = "ERROR";
		
			try {			
				if (this.generarInforme(request,ClsConstants.PLANTILLA_FO_FICHAFACTURACION)){
					generacionPdfOK = "OK";			
				}				
				request.setAttribute("generacionOK",generacionPdfOK);			
				salida = "descarga";
			} catch (Exception e) {
				throw new ClsExceptions(e,"Error al generar el informe");
			}
			return salida;	
		}
		
	/**
	 * Metodo que implementa el modo generarInformeFacturacion: implementacion de la descarga del informe solicitado por Baleares
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public String generarInformeFacturacion (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response)	
	throws ClsExceptions{
			
			String salida = null;
			String generacionPdfOK = "ERROR";
		
			try {			
				if (this.generarInforme(request,ClsConstants.PLANTILLA_FO_FICHAFACTURACION)){
					generacionPdfOK = "OK";			
				}				
				request.setAttribute("generacionOK",generacionPdfOK);			
				salida = "descarga";
			} catch (Exception e) {
				throw new ClsExceptions(e,"Error al generar el informe");
			}
			return salida;	
		}
				
	protected String reemplazarDatos(HttpServletRequest request, String plantillaFO) throws ClsExceptions{
		Hashtable hashDatos = null;
		try {
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			FcsFacturacionJGAdm admFacturacion = new FcsFacturacionJGAdm(usr);		
			FcsFactEstadosFacturacionAdm admEstadoFact = new FcsFactEstadosFacturacionAdm(usr);
			
			//Obtenemos el formulario y sus datos:
			//miform = (MantenimientoInformesForm)formulario;
			String idFacturacion = request.getParameter("idFacturacion");//miform.getIdFacturacion();
			String idInstitucion = usr.getLocation();
			String idEstado =admEstadoFact.getIdEstadoFacturacion(idInstitucion, idFacturacion);
			String idioma = request.getParameter("idioma").toUpperCase();//miform.getIdioma().toUpperCase();
			
			String sql=
				"select " +
				"f.nombre DESCRIPCION_FACTURACION," +
				UtilidadesMultidioma.getCampoMultidiomaSimple("e.descripcion",usr.getLanguage())+" ESTADO_FACTURACION,"+
				" to_char(fe.FECHAESTADO,'DD/MM/YYYY')  FECHAEJECUCION_FACTURACION,"+
//				"(SELECT to_char(max(FECHAESTADO),'DD/MM/YYYY')"+
//				"   FROM FCS_FACT_ESTADOSFACTURACION"+
//				"  WHERE IDESTADOFACTURACION = 20"+
//				"    AND IDFACTURACION = "+idFacturacion +
//				"    and Idinstitucion=" +idInstitucion+") FECHAEJECUCION_FACTURACION,"+
				"to_char(f.fechadesde,'DD/MM/YYYY') FECHADESDE_FACTURACION,"+
				"to_char(f.fechahasta,'DD/MM/YYYY') FECHAHASTA_FACTURACION,"+
				"decode(f.regularizacion, 1, 'SI', 'NO') REGULARIZACION_FACTURACION,"+
				"f2.nombre DESC_FACTURACION_REGULARIZADA "+
				"  from " +
				"fcs_facturacionjg f,"+
				"fcs_fact_estadosfacturacion fe,"+
				"fcs_estadosfacturacion e,"+
				"fcs_facturacionjg f2 "+
				"where fe.Idinstitucion=f.Idinstitucion" +
				"  and fe.Idfacturacion=f.Idfacturacion" +				
				" And fe.Idordenestado = "+
                "       (Select Max(Est2.Idordenestado) "+
                "          From Fcs_Fact_Estadosfacturacion Est2 "+
                "         Where Est2.Idinstitucion = fe.Idinstitucion "+
                "           And Est2.Idfacturacion = fe.Idfacturacion) "+							
				"  and fe.idestadofacturacion = e.idestadofacturacion"+
				"  and f.Idinstitucion=f2.Idinstitucion(+)"+
				"  and f.idfacturacion_regulariza = f2.idfacturacion(+)"+
				"  and f.Idinstitucion=" +idInstitucion+
				"  and f.idfacturacion = "+idFacturacion+
				"  and e.idestadofacturacion = "+idEstado;
			//Obtengo el bean de la facturacion:
			Vector v = admFacturacion.selectGenerico(sql);
			if(v!=null && v.size()>0){
				hashDatos=(Hashtable)v.get(0);
				
				//Cargar recurso 
				String estFact=(String)hashDatos.get("ESTADO_FACTURACION");
				if(estFact!=null && estFact.length()>0){
					hashDatos.put("ESTADO_FACTURACION",UtilidadesString.getMensajeIdioma(idioma,estFact));
				}
				
				//Cargar recurso 
				String regul=(String)hashDatos.get("REGULARIZACION_FACTURACION");
				if(regul!=null && regul.equals("SI")){
					hashDatos.put("TEXTO_FACTURACION_REGULARIZADA",UtilidadesString.getMensajeIdioma(idioma,"informes.fichaFacturacion.facturacionRegularizada")+":");
				}
				
				//validada=SI <= Estado==Lista para Consejo
				String validada = (Integer.parseInt(idEstado)==ClsConstants.ESTADO_FACTURACION_LISTA_CONSEJO?"SI":"NO");
				hashDatos.put("VALIDADA_FACTURACION",validada);
				
			}	
		} catch (Exception e) {
			hashDatos = null;
		}
		return this.reemplazaVariables(hashDatos,plantillaFO);	
	}
	
	
	
}