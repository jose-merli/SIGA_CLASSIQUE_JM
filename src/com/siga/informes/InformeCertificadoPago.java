
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
			htDatos.put("FECHAACTUAL", UtilidadesBDAdm.getFechaEscritaBD(idioma));

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
				"select F_SIGA_FORMATONUMERO(nvl(F.IMPORTETOTAL,0),2) IMPORTEFACTURADO," +
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
			StringBuffer sql = new StringBuffer();
			sql.append("select count(1) NUMEROTURNOS, ");
			sql.append(" to_char(nvl(sum(AD.IMPOFICIO + (AD.IMPOFICIO * AD.IMPIRPF / 100)), 0), '999,999.00') IMPORTETURNOS ");
			sql.append(" from FCS_PAGO_COLEGIADO AD ");
			sql.append(" where AD.IDINSTITUCION ="+idInstitucion);
			sql.append(" and AD.IDPAGOSJG = " +idPago);
			
			rc = new RowsContainer();
			rc.find(sql.toString());
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
			StringBuffer sql56 = new StringBuffer();
				sql56.append("select count(1) NUMERO ");
				sql56.append(" FROM FCS_PAGO_COLEGIADO   col, ");
				sql56.append(" FCS_PAGOSJG               pag, ");
				sql56.append(" FCS_FACTURACIONJG         fac, ");
				sql56.append(" FCS_FACT_APUNTE           apu ");
				sql56.append(" WHERE col.IDPAGOSJG = "+idPago);
				sql56.append(" and col.idinstitucion = "+idInstitucion);
				sql56.append(" and col.idinstitucion = pag.idinstitucion ");
				sql56.append(" and col.idpagosjg = pag.idpagosjg ");
				sql56.append(" and pag.idinstitucion = apu.idinstitucion ");
				sql56.append(" and pag.idfacturacion = apu.idfacturacion ");
				sql56.append(" and fac.idinstitucion = apu.idinstitucion ");
				sql56.append(" and fac.idfacturacion = apu.idfacturacion ");
				sql56.append(" and col.idperorigen = apu.idpersona ");
			//5 Numero de guardias inferiores o iguales a X actuaciones
			rc = new RowsContainer();
			rc.find(sql56.toString() + " and apu.IDHITO = 1");//GAc
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.put("NUMEROGUARDIASINF",r.getString("NUMERO"));
			}
			
			//6 Numero de guardias superiores a X actuaciones
			rc = new RowsContainer();
			rc.find(sql56.toString() + " and apu.IDHITO = 2");//GDAc
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.put("NUMEROGUARDIASSUP",r.getString("NUMERO"));
			}
			
			
			StringBuffer sql7 = new StringBuffer();
			sql7.append("select count(1) NUMERO ");
			sql7.append("from FCS_FACT_ASISTENCIA A, FCS_PAGO_COLEGIADO  col, FCS_PAGOSJG  pag,  ");
			sql7.append(" FCS_FACTURACIONJG  fac, FCS_FACT_APUNTE apu ");
			sql7.append(" where col.IDINSTITUCION = apu.IDINSTITUCION ");
			sql7.append(" and col.IDINSTITUCION = A.IDINSTITUCION ");
			sql7.append(" and col.IDINSTITUCION = " + idInstitucion);
			sql7.append(" and apu.IDHITO = 44 "); //GAc
			sql7.append(" and col.IDPAGOSJG = " + idPago );
			sql7.append(" and col.idinstitucion = pag.idinstitucion ");
			sql7.append(" and col.idpagosjg = pag.idpagosjg ");
			sql7.append(" and pag.idinstitucion = apu.idinstitucion ");
			sql7.append(" and pag.idfacturacion = apu.idfacturacion ");
			sql7.append(" and fac.idinstitucion = apu.idinstitucion ");
			sql7.append(" and fac.idfacturacion = apu.idfacturacion ");
			sql7.append(" and col.idperorigen = apu.idpersona ");
			sql7.append(" and A.idinstitucion = apu.idinstitucion ");
			sql7.append(" and A.idfacturacion = apu.idfacturacion ");
			sql7.append(" and A.idapunte = apu.Idapunte ");
			
			 //7 Numero de asistencias en guardias inferiores o iguales a y actuaciones
			rc = new RowsContainer();
			rc.find(sql7.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.put("NUMEROASISTENCIASINF",r.getString("NUMERO"));
			}
			
			StringBuffer sql9 = new StringBuffer();
			sql9.append("select count(*) NUMERO from (" );
			sql9.append(" select distinct FA.IDINSTITUCION, FA.ANIO, FA.NUMERO ");
			sql9.append(" from FCS_PAGO_COLEGIADO col, ");
			sql9.append(" FCS_PAGOSJG        pag, ");
			sql9.append(" FCS_FACTURACIONJG  fac, ");
			sql9.append(" FCS_FACT_APUNTE    apu, ");
			sql9.append(" FCS_FACT_ASISTENCIA          FA, ");
			sql9.append(" FCS_FACT_ACTUACIONASISTENCIA FAA ");
			sql9.append(" where col.IDINSTITUCION = FA.IDINSTITUCION ");
			sql9.append(" and col.IDINSTITUCION = FAA.IDINSTITUCION ");
			sql9.append(" and col.IDINSTITUCION = "+idInstitucion);
			sql9.append(" and FAA.IDHITO in (6, 9, 24, 25, 26, 31, 32, 43) ");
			sql9.append(" and col.IDPAGOSJG = "+idPago);
			sql9.append(" and col.idinstitucion = pag.idinstitucion ");
			sql9.append(" and col.idpagosjg = pag.idpagosjg ");
			sql9.append(" and pag.idinstitucion = apu.idinstitucion ");
			sql9.append(" and pag.idfacturacion = apu.idfacturacion ");
			sql9.append(" and fac.idinstitucion = apu.idinstitucion ");
			sql9.append(" and fac.idfacturacion = apu.idfacturacion ");
			sql9.append(" and col.idperorigen = apu.idpersona ");
			sql9.append(" and FA.idfacturacion = apu.idfacturacion ");
			sql9.append(" and FA.idapunte = apu.Idapunte ");
			sql9.append(" and FAA.IDAPUNTE = apu.IDAPUNTE ");
			sql9.append(" and FAA.IDFACTURACION = apu.IDFACTURACION )");		
			
			//9 Numero de asistencias con posterioridad al dia de guardia
			rc = new RowsContainer();
			rc.find(sql9.toString());
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
			StringBuffer sql = new StringBuffer();
			sql.append(" select F_SIGA_FORMATONUMERO(nvl(sum(FA.PRECIOAPLICADO),0),2) IMPORTEASISTENCIAS ");
			sql.append(" FROM FCS_PAGO_COLEGIADO   col, ");
			sql.append(" FCS_PAGOSJG               pag, ");
			sql.append(" FCS_FACTURACIONJG         fac, ");
			sql.append(" FCS_FACT_ASISTENCIA       fa ");
			sql.append(" WHERE col.IDPAGOSJG = "+idPago);
			sql.append(" and col.idinstitucion = "+idInstitucion);
			sql.append(" and col.idinstitucion = pag.idinstitucion ");
			sql.append(" and col.idpagosjg = pag.idpagosjg ");
			sql.append(" and pag.idinstitucion = fa.idinstitucion ");
			sql.append(" and pag.idfacturacion = fa.idfacturacion ");
			sql.append(" and fac.idinstitucion = fa.idinstitucion ");
			sql.append(" and fac.idfacturacion = fa.idfacturacion ");
			sql.append(" and col.idperorigen = fa.idpersona ");

			rc = new RowsContainer();
			rc.find(sql.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
			}
			
			// Importe a repartir (C) IMPORTESJCS
			sql = new StringBuffer();
			sql.append(" select F_SIGA_FORMATONUMERO(nvl(importerepartir,0),2) IMPORTESJCS ");
			sql.append(" from fcs_pagosjg ");
			sql.append(" where idinstitucion="+idInstitucion );
			sql.append(" and idpagosjg="+idPago );
			rc = new RowsContainer();
			rc.find(sql.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
			}

			// Movimientos varios (D) IMPORTEMOVIMIENTOS
			sql = new StringBuffer();
			sql.append("select F_SIGA_FORMATONUMERO(nvl(sum(impmovvar),0),2) IMPORTEMOVIMIENTOS");
			sql.append("  from fcs_pago_colegiado");
			sql.append(" where idinstitucion="+idInstitucion);
			sql.append("   and idpagosjg="+idPago);
			rc = new RowsContainer();
			rc.find(sql.toString());
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