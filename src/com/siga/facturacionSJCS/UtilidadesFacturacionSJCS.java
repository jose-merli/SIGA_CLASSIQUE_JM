/*
 * Created on 10-mar-2006
 */
package com.siga.facturacionSJCS;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ListOfFiles;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFicheros;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmLenguajesBean;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.beans.FcsFacturacionJGBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 *
 */
public class UtilidadesFacturacionSJCS 
{

	/**
	 * Funcion que devuelve el nombre de los ficheros de exportacion
	 * @param idInstitucion
	 * @param idFacturacion
	 * @param usuario
	 * @return
	 * @throws SIGAException
	 */
	static public Hashtable getNombreFicherosFacturacion (Integer idInstitucion, Integer idFacturacion, UsrBean usuario) throws SIGAException {
		return UtilidadesFacturacionSJCS.getNombreFicherosFac(idInstitucion, idFacturacion, null, null, usuario);
	}

	/**
	 * Funcion que devuelve el nombre de los ficheros de exportacion
	 * @param idInstitucion
	 * @param idFacturacion
	 * @param idPago
	 * @param idPersona
	 * @param usuario
	 * @return
	 * @throws SIGAException
	 */
	static public Hashtable getNombreFicherosPago (Integer idInstitucion, Integer idFacturacion, Integer idPago, Long idPersona, UsrBean usuario) throws SIGAException {
		return UtilidadesFacturacionSJCS.getNombreFicherosFac(idInstitucion, idFacturacion, idPago, idPersona, usuario);
	}


	
	/**
 	 * Funcion que devuelve el nombre de los ficheros de exportacion
	 * @param idInstitucion
	 * @param idFacturacion
	 * @param idPago
	 * @param idPersona
	 * @param usuario
	 * @return
	 * @throws SIGAException
	 */
	static private Hashtable getNombreFicherosFac (Integer idInstitucion, Integer idFacturacion, Integer idPago, Long idPersona, UsrBean usuario) throws SIGAException  
	{
		try {
			Hashtable nombreFicheros = new Hashtable();
			Hashtable datos = new Hashtable();
			UtilidadesHash.set(datos, FcsFacturacionJGBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(datos, FcsFacturacionJGBean.C_IDFACTURACION, idFacturacion);
			FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm (usuario);
			Vector v = facturacionAdm.selectByPK(datos);
			if ((v == null) || (v.size()!= 1)) 
				return nombreFicheros;
			
			FcsFacturacionJGBean facturacionBean = (FcsFacturacionJGBean) v.get(0);
			
			boolean bPrevision      = (facturacionBean.getPrevision().equalsIgnoreCase(ClsConstants.DB_TRUE)?true:false);
			boolean bRegularizacion = (facturacionBean.getRegularizacion().equalsIgnoreCase(ClsConstants.DB_TRUE)?true:false);
			
			String sNombreFichero = "";
			if (bPrevision && bRegularizacion) 
				return nombreFicheros;

			if (idPago != null) {
				sNombreFichero = "PAGOS_";				// Pagos
			}
			else {
				if (!bPrevision && !bRegularizacion)  	// Facturacion
					sNombreFichero = "FACTURACION_";
				
				if (bPrevision)							// Prevision 
					sNombreFichero = "PREVISION_";
			
				if (bRegularizacion) 					// Regularizacion
					sNombreFichero = "REGULARIZACION_";
			}
			
			String extensionFichero = "_" + idInstitucion + "_" + idFacturacion;
			if (idPago != null) {
				extensionFichero += "_" + idPago;
			}
			if (idPersona != null) {
				extensionFichero += "_" + idPersona;
			}
			extensionFichero += ".XLS";
			
			UtilidadesHash.set(nombreFicheros, "" + ClsConstants.HITO_GENERAL_TURNO,   sNombreFichero + "TURNOSOFICIO" + extensionFichero);
			UtilidadesHash.set(nombreFicheros, "" + ClsConstants.HITO_GENERAL_GUARDIA, sNombreFichero + "GUARDIAS"     + extensionFichero);
			UtilidadesHash.set(nombreFicheros, "" + ClsConstants.HITO_GENERAL_EJG,     sNombreFichero + "EJG"          + extensionFichero);
			UtilidadesHash.set(nombreFicheros, "" + ClsConstants.HITO_GENERAL_SOJ,     sNombreFichero + "SOJ"          + extensionFichero);
			return nombreFicheros;
		}
		catch (Exception e) {
			throw new SIGAException (e.getMessage());
		}
	}


	/**
	 * Funcion que elimina los ficheros de una facturacion o pago
	 * @param idInstitucion
	 * @param nombreFicheros
	 * @param usuario
	 * @throws SIGAException
	 */
	static public void borrarFicheros (Integer idInstitucion, Hashtable nombreFicheros, UsrBean usuario) throws SIGAException
	{
		try {
			GenParametrosAdm paramAdm = new GenParametrosAdm(usuario);
			String path = paramAdm.getValor("" + idInstitucion, "FCS", "PATH_PREVISIONES", null);
			
			File fichero = null;
//			Hashtable nombreFicheros = UtilidadesFacturacionSJCS.getNombreFicherosFacturacion(idInstitucion, idFacturacion, usuario);			
			if (nombreFicheros == null) 
				return;
			
			fichero = new File(path + File.separator + UtilidadesHash.getString(nombreFicheros, "" + ClsConstants.HITO_GENERAL_TURNO));
			if (fichero.exists())	
				fichero.delete();
			
			fichero = new File(path + File.separator + UtilidadesHash.getString(nombreFicheros, "" + ClsConstants.HITO_GENERAL_GUARDIA));
			if (fichero.exists())	
				fichero.delete();
			
			fichero = new File(path + File.separator + UtilidadesHash.getString(nombreFicheros, "" + ClsConstants.HITO_GENERAL_EJG));
			if (fichero.exists())	
				fichero.delete();
			
			fichero = new File(path + File.separator + UtilidadesHash.getString(nombreFicheros, "" + ClsConstants.HITO_GENERAL_SOJ));
			if (fichero.exists())	
				fichero.delete();
		}
		catch (Exception e) {
			throw new SIGAException (e.getMessage());
		}
	}
	
	/**
	 * Funcion que devuelve la cabecera de Excel, dependiendo del tipo
	 * 
	 * @param concepto: turnos oficio, guardias, soj, ejg
	 * @param tipoCabecera: pago, facturacion
	 * @param usuario
	 * @return
	 */
	static public String getCabecerasFicheros (int concepto,
											   String tipoCabecera,
											   UsrBean usuario) 
	{
		String cabecera = "";
		
		try
		{
			if (concepto == ClsConstants.HITO_GENERAL_TURNO)
			{
				cabecera =
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numColLetrado") 			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.letrado") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.turno") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.fecha") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.fechaActuacion")			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.fechaJustificacion") 		+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.nDesigna") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.nAct") 						+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.codigoProc") 				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.procedimientoActuacion")	+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.pretension")	 			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importeProcedimiento") 		+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.tipoAcreditacion") 			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importe") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.separadorOtrosCampos")		+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.nProcedimiento") 			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numDocAsistido")			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.Litigante") 				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.juzgado") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.localidad") 				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.turnoAsistencia")			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.guardiaAsistencia")			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.nAsist")	 				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.nExpEJG") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.nExpCAJG") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.totalLetrado") 				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.totalAcumulado");
				
				// Si es un pago
				if (tipoCabecera.equalsIgnoreCase(ClsConstants.PAGOS_SJCS))
					cabecera +=
					"#" +
					UtilidadesFacturacionSJCS.construirCabeceraPago(usuario);
			}
			else if (concepto == ClsConstants.HITO_GENERAL_GUARDIA && !tipoCabecera.equalsIgnoreCase("3001"))
			{
				cabecera =
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numColLetrado") 			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.letrado") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.turno") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.guardia") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.tipoApunte")				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.fechainicio") 				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.fechafin") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.fechaActuacion")			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.fechaJustificacion")		+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.nAsist")					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.nAct")						+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.motivo") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numActuacionesFacturadas") 	+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numActuacFGFacturadas") 	+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numAsistenciasFacturadas") 	+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numActuacionesTotales") 	+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numActuacFGTotales")	 	+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numAsistenciasTotales") 	+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importe") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.costesFijos") 				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.total")             		+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.separadorOtrosCampos")		+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numDocAsistido")			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.asistido")					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.delitos") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.lugar") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.localidad") 				+ "#" +
					
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.totalLetrado");
				
				// Si es un pago
				if (tipoCabecera.equalsIgnoreCase(ClsConstants.PAGOS_SJCS))
					cabecera +=
					"#" +
					UtilidadesFacturacionSJCS.construirCabeceraPagoGuardias(usuario);
			}
			else if (concepto == ClsConstants.HITO_GENERAL_GUARDIA && tipoCabecera.equalsIgnoreCase("3001"))
			{
				cabecera =
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numColLetrado") 			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.letrado") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.turno") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.guardia") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.tipoApunte")				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.fechainicio") 				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.fechafin") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.fechaActuacion")			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.fechaJustificacion")		+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.nAsist")					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.nAct")						+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numActuacionesFacturadas") 	+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numActuacFGFacturadas") 	+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numActuacionesTotales") 	+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numActuacFGTotales")	 	+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importeGuardia")		 	+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.costesFijos")				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.totalGuardia")				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.separadorOtrosCampos")		+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.numDocAsistido")			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.asistido")					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.delitos") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.lugar") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.localidad");
			}
			else if (concepto == ClsConstants.HITO_GENERAL_SOJ)
			{
				cabecera =
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.tramitador") 				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.turno") 	    			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.guardia") 	    			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.nExpEJG") 	    			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.fecha") 	    			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.interesado")    			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importe") 	    			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.total")         			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.totalAcumulado");
				
				// Si es un pago
				if (tipoCabecera.equalsIgnoreCase(ClsConstants.PAGOS_SJCS))
					cabecera +=
					"#" +
					UtilidadesFacturacionSJCS.construirCabeceraPago(usuario);
			}
			else if (concepto == ClsConstants.HITO_GENERAL_EJG)
			{
				cabecera =
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.tramitador") 				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.turno") 	    			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.guardia") 	    			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.nExpEJG") 	    			+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.fecha") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.ncajg") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.anioCajg") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.interesado")				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importe") 					+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.total")     				+ "#" +
					UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.totalAcumulado");
				
				if (tipoCabecera.equalsIgnoreCase(ClsConstants.PAGOS_SJCS))
					cabecera +=
					"#" +
					UtilidadesFacturacionSJCS.construirCabeceraPago(usuario);
			}
		}
		catch (Exception e) {
			cabecera = "";
		}
		return cabecera;
	} //getCabecerasFicheros()
	private static String construirCabeceraPago (UsrBean usuario)
	{
		String cabecera =
			UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importePagosRealizados") 			+ "#" +
			UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importeIRPFPagosRealizados") 		+ "#" +
			UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importePagoActual") 				+ "#" +
			UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importeIRPFPagoActual") 			+ "#" +
			UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importeTotalPagosRealizados") 		+ "#" +
			UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importeTotalIRPFPagosRealizados") 	+ "#" +
			UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importeTotalPagoActual") 			+ "#" +
			UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importeTotalIRPFPagoActual");
		return cabecera;
	} //construirCabeceraPago()
	private static String construirCabeceraPagoGuardias (UsrBean usuario)
	{
		String cabecera =
			UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importePagoActual")				+ "#" +
			UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importeIRPFPagosRealizados") 	+ "#" +
			UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.porcentajeIRPFPagosRealizados")	+ "#" +
			UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.importeTotalPagado")			+ "#" +
			UtilidadesString.getMensajeIdioma(usuario, "fcs.ficheroFacturacion.cabecera.totalPagadoLetrado");
		return cabecera;
	} //construirCabeceraPagoGuardias()
	
	static public void exportarDatosFacturacion (Integer idInstitucion,
												 Integer idFacturacion,
												 UsrBean usuario)
			throws SIGAException
	{
		UtilidadesFacturacionSJCS.exportarDatosFac(idInstitucion, idFacturacion, null, null, usuario);
	}
	static public void exportarDatosPagos (Integer idInstitucion,
										   Integer idFacturacion,
										   Integer idPago,
										   Long idPersona,
										   UsrBean usuario)
			throws SIGAException
	{
		UtilidadesFacturacionSJCS.exportarDatosFac(idInstitucion, idFacturacion, idPago, idPersona, usuario);
	}

	
	static private void exportarDatosFac (Integer idInstitucion, Integer idFacturacion, Integer idPago, Long idPersona, UsrBean usuario) throws SIGAException
	{
		try {
			
			String tipoP=ClsConstants.FACTURACION_SJCS;
			 
			GenParametrosAdm paramAdm = new GenParametrosAdm(usuario);
			String pathFicheros = paramAdm.getValor("" + idInstitucion, "FCS", "PATH_PREVISIONES_BD", null);
			
			Hashtable nombreFicheros = null;
			if (idPago == null) { 
				nombreFicheros = UtilidadesFacturacionSJCS.getNombreFicherosFacturacion(idInstitucion, idFacturacion, usuario);
			} else {
				nombreFicheros = UtilidadesFacturacionSJCS.getNombreFicherosPago(idInstitucion, idFacturacion, idPago, idPersona, usuario);
				tipoP=ClsConstants.PAGOS_SJCS;
			}
			
			//El lenguaje de los informes es el de la institucion a la que pertenecen las
			//facturaciones, no tiene que ver con el usuario que la genera. Ademas si es un
			//proceso automatica el userBean Automatico tiene por defecto el idioma
			//español que seria un error para lo colegios con otro idioma.
			//Por lo tanto accedemos al idioma de cada institucion
			AdmLenguajesAdm admLen = new AdmLenguajesAdm(usuario);
			AdmLenguajesBean beanLenguajeIntitucion = admLen.getLenguajeInstitucion(idInstitucion.toString());

			// TURNOS DE OFICIO
			Object[] param_in_turno = new Object[8];
			param_in_turno[0] = idInstitucion.toString(); 					// IDINSTITUCION
			param_in_turno[1] = idFacturacion.toString(); 					// IDFACTURACION
			param_in_turno[2] = (idPago    == null?"":idPago.toString()); 	// IDPAGO
			param_in_turno[3] = (idPersona == null?"":idPersona.toString()); 	// IDPERSONA
			param_in_turno[4] = pathFicheros;									// PATH_FICHEROS 
			param_in_turno[5] = UtilidadesHash.getString(nombreFicheros, "" + ClsConstants.HITO_GENERAL_TURNO);
			param_in_turno[6] = UtilidadesFacturacionSJCS.getCabecerasFicheros(ClsConstants.HITO_GENERAL_TURNO,tipoP, usuario);
			param_in_turno[7] = beanLenguajeIntitucion.getIdLenguaje();				// IDOMA 
			String resultado[] = new String[2];
	    	resultado = /*EjecucionPLs.ejecutarPLExportarTurnosOficio()*/ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_EXPORTAR_TURNOS_OFI (?,?,?,?,?,?,?,?,?,?)}", 2, param_in_turno);
	    	if (!resultado[0].equalsIgnoreCase("0")) {
	    		ClsLogging.writeFileLog("Error en PL = "+(String)resultado[1],3);
	    	}


			Object[] param_in = new Object[7];	    	
			param_in[0] = idInstitucion.toString(); 					// IDINSTITUCION
			param_in[1] = idFacturacion.toString(); 					// IDFACTURACION
			param_in[2] = (idPago    == null?"":idPago.toString()); 	// IDPAGO
			param_in[3] = (idPersona == null?"":idPersona.toString()); 	// IDPERSONA
			param_in[4] = pathFicheros;									// PATH_FICHEROS 
			
	    	// SOJ
			param_in[5] = UtilidadesHash.getString(nombreFicheros, "" + ClsConstants.HITO_GENERAL_SOJ);
			param_in[6] = UtilidadesFacturacionSJCS.getCabecerasFicheros(ClsConstants.HITO_GENERAL_SOJ, tipoP, usuario);
	    	resultado = new String[2];
	    	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_EXPORTAR_SOJ (?,?,?,?,?,?,?,?,?)}", 2, param_in);
	    	if (!resultado[0].equalsIgnoreCase("0")) {
	    		ClsLogging.writeFileLog("Error en PL = "+(String)resultado[1],3);
	    	}
	    	
	    	// EJG
			param_in[5] = UtilidadesHash.getString(nombreFicheros, "" + ClsConstants.HITO_GENERAL_EJG);
			param_in[6] = UtilidadesFacturacionSJCS.getCabecerasFicheros(ClsConstants.HITO_GENERAL_EJG, tipoP, usuario);
	    	resultado = new String[2];
	    	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_EXPORTAR_EJG (?,?,?,?,?,?,?,?,?)}", 2, param_in);
	    	if (!resultado[0].equalsIgnoreCase("0")) {
	    		ClsLogging.writeFileLog("Error en PL = "+(String)resultado[1],3);
	    	}

	    	// GUARDIAS Solo se pasa el idioma cuando exportamos guardias
	    	Object[] param_in1 = new Object[8];
			param_in1[0] = idInstitucion.toString(); 					// IDINSTITUCION
			param_in1[1] = idFacturacion.toString(); 					// IDFACTURACION
			param_in1[2] = (idPago    == null?"":idPago.toString()); 	// IDPAGO
			param_in1[3] = (idPersona == null?"":idPersona.toString()); 	// IDPERSONA
			param_in1[4] = pathFicheros;									// PATH_FICHEROS 
			param_in1[7] = beanLenguajeIntitucion.getIdLenguaje();				// IDOMA 
			param_in1[5] = UtilidadesHash.getString(nombreFicheros, "" + ClsConstants.HITO_GENERAL_GUARDIA);
			param_in1[6] = UtilidadesFacturacionSJCS.getCabecerasFicheros(ClsConstants.HITO_GENERAL_GUARDIA, 
					(idInstitucion.intValue() == 2012 ||
					idInstitucion.intValue() == 2026 ||
					idInstitucion.intValue() == 2028 ||
					idInstitucion.intValue() == 2030 ||
					idInstitucion.intValue() == 2041 ||
					idInstitucion.intValue() == 2047 ||
					idInstitucion.intValue() == 2048 ||
					idInstitucion.intValue() == 2057 ||
					idInstitucion.intValue() == 2059 ||
					idInstitucion.intValue() == 2061 ||
					idInstitucion.intValue() == 2071 ||
					idInstitucion.intValue() == 2072 ||
					idInstitucion.intValue() == 2075 ||
					idInstitucion.intValue() == 2079 ? "3001":tipoP),usuario);
	    	resultado = new String[2];
	    	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.proc_fcs_exportar_guardias (?,?,?,?,?,?,?,?,?,?)}", 2, param_in1);
	    	if (!resultado[0].equalsIgnoreCase("0")) {
	    		ClsLogging.writeFileLog("Error en PL = "+(String)resultado[1],3);
	    	}
		}
		catch (Exception e) {
    		throw new SIGAException ("Error al exportar datos: " + e.getMessage());			
		}
	}
	
	/**
	 * Genera los ficheros excel de facturaciones para las facturaciones entre <code>idFacturacionIni</code> e 
	 * <code>idFacturacionFin</code>. Genera un único fichero para cada tipo (EJG, SOJ, Guardias y Turnos) resultado
	 * de la union de los ficheros ya generados para cada facturacion.
	 * 
	 * @param idInstitucion
	 * @param idFacturacionIni Facturación inicial
	 * @param idFacturacionFin Facturación Final
	 * @param usuario
	 * @throws SIGAException
	 */
	public static void generarFicherosFacturacionMultiple(Integer idInstitucion,
														  Integer idFacturacionIni,
														  Integer idFacturacionFin,
														  UsrBean usuario)
			throws SIGAException
	{
		try {
			GenParametrosAdm paramAdm = new GenParametrosAdm(usuario);
			String pathFicheros = paramAdm.getValor("" + idInstitucion, "FCS",
					"PATH_PREVISIONES_BD", null);

			String nombreFichConRuta = pathFicheros + File.separator
					+ "FACTURACION_";
			String nombreFich = "FACTURACION_";
			String extensionFichero = "_" + idInstitucion + "_"
					+ idFacturacionIni + "-" + idFacturacionFin;
			extensionFichero += ".XLS";

			String sFacturaciones = EjecucionPLs
					.ejecutarFuncFacturacionesIntervalo(idInstitucion
							.toString(), idFacturacionIni.toString(),
							idFacturacionFin.toString());
			String[] lFacturaciones = sFacturaciones.replaceAll(" ", "").split(
					",");
			int nFact = lFacturaciones.length;
			ArrayList<String> lFacturacionesTurno = new ArrayList<String>();
			ArrayList<String> lFacturacionesEJG = new ArrayList<String>();
			ArrayList<String> lFacturacionesSOJ = new ArrayList<String>();
			ArrayList<String> lFacturacionesGuardias = new ArrayList<String>();

			// Solo se pasan a la funcion de concatenacion de ficheros, aquellos
			// que existen
			for (int i = 0; i < nFact; i++) {
				String ext = "_" + idInstitucion + "_" + lFacturaciones[i]
						+ ".XLS";
				String aux = nombreFichConRuta + "TURNOSOFICIO" + ext;
				if (new File(aux).exists()) {
					lFacturacionesTurno.add(aux);
				}
				aux = nombreFichConRuta + "EJG" + ext;
				if (new File(aux).exists()) {
					lFacturacionesEJG.add(aux);
				}
				aux = nombreFichConRuta + "SOJ" + ext;
				if (new File(aux).exists()) {
					lFacturacionesSOJ.add(aux);
				}
				aux = nombreFichConRuta + "GUARDIAS" + ext;
				if (new File(aux).exists()) {
					lFacturacionesGuardias.add(aux);
				}
			}

			ListOfFiles lFicheros = null;

			// TURNOS DE OFICIO
			if (!lFacturacionesTurno.isEmpty()) {
				String sFicheroTurnos = nombreFichConRuta + "TURNOSOFICIO"
						+ extensionFichero;
				lFicheros = new ListOfFiles((String[]) lFacturacionesTurno
						.toArray(new String[lFacturacionesTurno.size()]));
				UtilidadesFicheros
						.concatenarFicheros(lFicheros, sFicheroTurnos);
			}

			// EJG
			if (!lFacturacionesEJG.isEmpty()) {
				String sFicheroEJG = nombreFichConRuta + "EJG"
						+ extensionFichero;
				lFicheros = new ListOfFiles((String[]) lFacturacionesEJG
						.toArray(new String[lFacturacionesEJG.size()]));
				UtilidadesFicheros.concatenarFicheros(lFicheros, sFicheroEJG);
			}

			// SOJ
			if (!lFacturacionesSOJ.isEmpty()) {
				String sFicheroSOJ = nombreFichConRuta + "SOJ"
						+ extensionFichero;
				lFicheros = new ListOfFiles((String[]) lFacturacionesSOJ
						.toArray(new String[lFacturacionesSOJ.size()]));
				UtilidadesFicheros.concatenarFicheros(lFicheros, sFicheroSOJ);
			}
			//El lenguaje de los informes es el de la institucion a la que pertenecen las
			//facturaciones, no tiene que ver con el usuario que la genera. Ademas si es un
			//proceso automatica el userBean Automatico tiene por defecto el idioma
			//español que seria un error para lo colegios con otro idioma.
			//Por lo tanto accedemos al idioma de cada institucion
			AdmLenguajesAdm admLen = new AdmLenguajesAdm(usuario);
			AdmLenguajesBean beanLenguajeIntitucion = admLen.getLenguajeInstitucion(idInstitucion.toString());
			
			// GUARDIAS
			if (idInstitucion.intValue() == 2012 ||
				idInstitucion.intValue() == 2026 ||
				idInstitucion.intValue() == 2028 ||
				idInstitucion.intValue() == 2030 ||
				idInstitucion.intValue() == 2041 ||
				idInstitucion.intValue() == 2047 ||
				idInstitucion.intValue() == 2048 ||
				idInstitucion.intValue() == 2057 ||
				idInstitucion.intValue() == 2059 ||
				idInstitucion.intValue() == 2061 ||
				idInstitucion.intValue() == 2071 ||
				idInstitucion.intValue() == 2072 ||
				idInstitucion.intValue() == 2075 ||
				idInstitucion.intValue() == 2079) {
				// GUARDIAS Solo se pasa el idioma cuando exportamos
				// guardias
				Object[] param_in1 = new Object[7];
				param_in1[0] = idInstitucion.toString(); // IDINSTITUCION
				param_in1[1] = idFacturacionIni.toString(); // IDFACTURACION
				param_in1[2] = (idFacturacionFin == null ? ""
						: idFacturacionFin.toString()); // IDPAGO
				param_in1[3] = pathFicheros; // PATH_FICHEROS
				param_in1[4] = nombreFich + "GUARDIAS" + extensionFichero;
				param_in1[5] = UtilidadesFacturacionSJCS
						.getCabecerasFicheros(
								ClsConstants.HITO_GENERAL_GUARDIA,
								"3001", usuario);
				param_in1[6] = beanLenguajeIntitucion.getIdLenguaje(); // IDOMA
				String[] resultado = new String[2];
				resultado = ClsMngBBDD
						.callPLProcedure(
								"{call PKG_SIGA_FACTURACION_SJCS.proc_exportar_guardias_3001 (?,?,?,?,?,?,?,?,?)}",
								2, param_in1);
				if (!resultado[0].equalsIgnoreCase("0")) {
					ClsLogging.writeFileLog("Error en PL = "
							+ (String) resultado[1], 3);
				}
			}
			else if (!lFacturacionesGuardias.isEmpty()) {
				String sFicheroGuardias = nombreFichConRuta + "GUARDIAS"
						+ extensionFichero;
				lFicheros = new ListOfFiles(
						(String[]) lFacturacionesGuardias
								.toArray(new String[lFacturacionesGuardias
										.size()]));
				UtilidadesFicheros.concatenarFicheros(lFicheros,
						sFicheroGuardias);
			}
		} catch (Exception e) {
			throw new SIGAException("Error al exportar datos: "
					+ e.getMessage());
		}
	} // generarFicherosFacturacionMultiple()

}
