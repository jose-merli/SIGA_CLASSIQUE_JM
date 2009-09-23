
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
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.beans.GenParametrosAdm;

/**
 * Clase para el Mantenimiento de los Informes generados en formato PDF para los Ppagos, Facturaciones.
 * @author david.sanchezp
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InformeCertificadoPago extends MasterReport {
	
	protected String formatoImportes="999,999,999,999,990.00";
	
	/**
	 * Metodo que implementa el modo generarPago
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public String generarCertificadoPago (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws ClsExceptions{
		
		String salida = null;
		String generacionPdfOK = "ERROR";
	
		try{
			if (this.generarInforme(request,ClsConstants.PLANTILLA_FO_CERTIFICADOPAGO)){
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
		Hashtable htDatos = new Hashtable();
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String institucion =usr.getLocation();
		String idioma=request.getParameter("idioma").toUpperCase();
		request.getParameterNames();
		String idPago =request.getParameter("idPago");
		Hashtable htAux=null;

		try {
			//FIRMA (1) ?? y FECHAACTUAL (13)
			htDatos.put("FIRMA","XXXXXX XXXXXX XXXXXX");
			htDatos.put("FECHAACTUAL","Leida a "+UtilidadesBDAdm.getFechaEscritaBD(idioma));

			//FECHADESDE y FECHAHASTA(2), IMPORTEFACTURADO (14)
			htAux=this.obtenerDatosFacturacion(institucion,idPago);
			htDatos.putAll(htAux);

			// NUMEROTURNOS (3), IMPORTETURNOS (4)
			htAux=this.obtenerDatosTurnos(institucion,idPago);
			htDatos.putAll(htAux);

			//NUM_ACTUACIONES (5+), NUM_ASISTENCIAS (6+)
			GenParametrosAdm paramAdm= new GenParametrosAdm(usr);
			htDatos.put("NUM_ACTUACIONES",paramAdm.getValor(institucion,"FCS","NUM_ACTUACIONES",""));
			htDatos.put("NUM_ASISTENCIAS",paramAdm.getValor(institucion,"FCS","NUM_ASISTENCIAS",""));
			
			//NUMEROGUARDIASINF (5), NUMEROGUARDIASSUP (6), NUMEROASISTENCIASINF (7), NUMEROASISTENCIASPOST (9)
			htAux=this.obtenerDatosGuardias(institucion,idPago);
			htDatos.putAll(htAux);
			
			//IMPORTEASISTENCIAS, IMPORTESJCS (11), IMPORTEMOVIMIENTOS (12) 
			htAux=this.obtenerDatosImportes(institucion,idPago);
			htDatos.putAll(htAux);
		} catch (ClsExceptions e) {
			htDatos = null;
		}
		return this.reemplazaVariables(htDatos,plantillaFO);	
	}
	
	/**
	 * Obtiene rango de fechas e importe facturado
	 * @param idInstitucion Identificador de la institucion
	 * @param idPago Identificador del pago
	 * @return
	 * @throws ClsExceptions
	 */	
	protected Hashtable obtenerDatosFacturacion(String idInstitucion, String idPago) throws ClsExceptions{
		Hashtable result= new Hashtable();
		RowsContainer rc=null;
		
		try {
			String sql=
				"select to_char(nvl(F.IMPORTETOTAL,0),'"+formatoImportes+"') IMPORTEFACTURADO," +
				"       to_char(F.FECHADESDE,'DD/MM/YYYY') FECHADESDE," +
				"       to_char(F.FECHAHASTA,'DD/MM/YYYY') FECHAHASTA," +
				" 		F.NOMBRE AS DESCRIPCION_FACT" +
				"  from FCS_FACTURACIONJG F, FCS_PAGOSJG P" +
				" where F.IDFACTURACION=P.IDFACTURACION" +
				"   and F.IDINSTITUCION=P.IDINSTITUCION" +
				"   and P.IDINSTITUCION ="+idInstitucion +
				"   and P.IDPAGOSJG = " +idPago;
			rc = new RowsContainer();
			rc.find(sql);
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
				if (result.get("IMPORTEFACTURADO")!=null && ((String)result.get("IMPORTEFACTURADO")).equals(""))
					result.put("IMPORTEFACTURADO","0.00");
			}
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		}
		
		return result;
	}
	
	/**
	 * Obtiene numero de turnos e importe
	 * @param idInstitucion Identificador de la institucion
	 * @param idPago Identificador del pago
	 * @return
	 * @throws ClsExceptions
	 */	
	protected Hashtable obtenerDatosTurnos(String idInstitucion, String idPago) throws ClsExceptions{
		Hashtable result= new Hashtable();
		RowsContainer rc=null;
		
		try {
			String sql=
				"select count(1) NUMEROTURNOS, " +
				"       to_char(nvl(sum(AD.IMPORTEPAGADO+AD.IMPORTEIRPF),0),'"+formatoImportes+"') IMPORTETURNOS " +
				"  from FCS_PAGO_ACTUACIONDESIGNA AD" +
				" where AD.IDINSTITUCION ="+idInstitucion+
				"   and AD.IDPAGOSJG = " +idPago;
			rc = new RowsContainer();
			rc.find(sql);
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
			}
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		}
		
		return result;
	}
	
	/**
	 * Obtiene numero de guardias inferiores o iguales a X actuaciones, 
	 *         numero de guardias superiores a           X actuaciones,
	 *         numero de asistencias en guardias inferiores o iguales a y actuaciones
	 * Y       numero de asistencias con posterioridad al dia de guardia
	 * @param idInstitucion Identificador de la institucion
	 * @param idPago Identificador del pago
	 * @return
	 * @throws ClsExceptions
	 */	
	protected Hashtable obtenerDatosGuardias(String idInstitucion, String idPago) throws ClsExceptions{
		Hashtable result= new Hashtable();
		RowsContainer rc=null;
		
		try {
			String sql56=
				"select count(*) NUMERO" +
				"  from FCS_PAGO_APUNTE PA, FCS_FACT_APUNTE FA" +
				" where PA.IDINSTITUCION = FA.IDINSTITUCION" +
				"   and PA.IDINSTITUCION ="+idInstitucion+
				"   and PA.IDFACTURACION = FA.IDFACTURACION" +
				"   and PA.IDAPUNTE = FA.IDAPUNTE" +
				"   and PA.IDPAGOSJG = " +idPago;
			//5 Numero de guardias inferiores o iguales a X actuaciones
			rc = new RowsContainer();
			rc.find(sql56+" and FA.IDHITO = 1");//GAc
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.put("NUMEROGUARDIASINF",r.getString("NUMERO"));
			}
			
			//6 Numero de guardias superiores a X actuaciones
			rc = new RowsContainer();
			rc.find(sql56+" and FA.IDHITO = 2");//GDAc
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.put("NUMEROGUARDIASSUP",r.getString("NUMERO"));
			}
			
			
			String sql7=
				"select count(*) NUMERO" +
				"  from FCS_PAGO_APUNTE PA, FCS_FACT_APUNTE FA, FCS_FACT_ASISTENCIA A" +
				" where PA.IDINSTITUCION = FA.IDINSTITUCION" +
				"   and PA.IDINSTITUCION = A.IDINSTITUCION" +
				"   and PA.IDINSTITUCION ="+idInstitucion+
				"   and PA.IDFACTURACION = FA.IDFACTURACION" +
				"   and PA.IDFACTURACION = A.IDFACTURACION" +
				"   and PA.IDAPUNTE = FA.IDAPUNTE" +
				"   and A.IDAPUNTE = FA.IDAPUNTE" +
				"   and FA.IDHITO = 44"+ //GAc
				"   and PA.IDPAGOSJG = " +idPago;
			
			 //7 Numero de asistencias en guardias inferiores o iguales a y actuaciones
			rc = new RowsContainer();
			rc.find(sql7);
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.put("NUMEROASISTENCIASINF",r.getString("NUMERO"));
			}
			
			String sql9=
				"select count(*) NUMERO from (" +
				"  select distinct FA.IDINSTITUCION, FA.ANIO, FA.NUMERO" +
				"    from FCS_PAGO_APUNTE PA, FCS_FACT_ASISTENCIA FA, FCS_FACT_ACTUACIONASISTENCIA FAA" +
				"   where PA.IDINSTITUCION = FA.IDINSTITUCION" +
				"     and PA.IDINSTITUCION = FAA.IDINSTITUCION" +
				"     and PA.IDINSTITUCION ="+idInstitucion+
				"     and PA.IDFACTURACION = FA.IDFACTURACION" +
				"     and PA.IDFACTURACION = FAA.IDFACTURACION" +
				"     and PA.IDAPUNTE = FA.IDAPUNTE" +
				"     and PA.IDAPUNTE = FAA.IDAPUNTE" +
				"     and FAA.IDHITO in (6, 9, 24, 25, 26, 31, 32, 43)" +
				"     and PA.IDPAGOSJG = " +idPago+
				")";
			
			//9 Numero de asistencias con posterioridad al dia de guardia
			rc = new RowsContainer();
			rc.find(sql9);
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.put("NUMEROASISTENCIASPOST",r.getString("NUMERO"));
			}

		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		}
		
		return result;
	}
	
	/**
	 * Obtiene numero de turnos e importe
	 * @param idInstitucion Identificador de la institucion
	 * @param idPago Identificador del pago
	 * @return
	 * @throws ClsExceptions
	 */	
	protected Hashtable obtenerDatosImportes(String idInstitucion, String idPago) throws ClsExceptions{
		Hashtable result= new Hashtable();
		RowsContainer rc=null;
		
		try {
			//IMPORTEASISTENCIAS (10)
			String sql=
				"select to_char(nvl(sum(FA.PRECIOAPLICADO),0),'"+formatoImportes+"') IMPORTEASISTENCIAS" +
				"  from FCS_PAGO_APUNTE FP, FCS_FACT_ASISTENCIA FA" +
				" where FP.IDINSTITUCION = FA.IDINSTITUCION" +
				"   and FP.IDFACTURACION = FA.IDFACTURACION" +
				"	and FP.IDAPUNTE = FA.IDAPUNTE" +
				"	and FP.IDINSTITUCION ="+idInstitucion+
				"	and FP.IDPAGOSJG = " +idPago;
			rc = new RowsContainer();
			rc.find(sql);
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
			}
			
			// Importe a repartir (C) IMPORTESJCS
			String sql1=
				"select to_char(nvl(importerepartir,0),'"+formatoImportes+"') IMPORTESJCS " +
				"  from fcs_pagosjg " +
				" where idinstitucion="+idInstitucion+
				"   and idpagosjg="+idPago;
			rc = new RowsContainer();
			rc.find(sql1);
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
			}

			// Movimientos varios (D) IMPORTEMOVIMIENTOS
			String sql2=
						"select to_char(nvl(sum(cantidad),0),'"+formatoImportes+"') IMPORTEMOVIMIENTOS" +
						"  from fcs_movimientosvarios" +
						" where idinstitucion="+idInstitucion+
						"   and idpagosjg="+idPago;
			rc = new RowsContainer();
			rc.find(sql2);
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
			}
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		}
		
		return result;
	}
	
	
}