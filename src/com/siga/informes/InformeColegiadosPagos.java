
/*
 * Created on May 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.informes;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.FcsMovimientosVariosAdm;
import com.siga.beans.FcsMovimientosVariosBean;
import com.siga.beans.FcsPagoColegiadoAdm;
import com.siga.beans.FcsPagosJGAdm;
import com.siga.general.SIGAException;

/**
 * Clase para el Mantenimiento de los Informes generados en formato PDF para los Pagos a Colegiados.
 */
public class InformeColegiadosPagos extends MasterReport {
	
	protected String formatoImportes="999,999,999,999,990.00";
	
	public Hashtable getDatosInformeColegiado(UsrBean usr, Hashtable htDatos) throws ClsExceptions, SIGAException{
		String idioma = usr.getLanguage().toUpperCase();
		String institucion =usr.getLocation();
		String idPagos =(String)htDatos.get("idPago");
		Hashtable htAux=null;
		//firmas
		htDatos.put("SECRETARIO","XXXXXXX XXXXXXXXXXXXXXX XXXXXXXXXX");
		htDatos.put("FIRMA","xxxxxxxxx xxxxxxxxx xxxxxxxx");
		htDatos.put("NDUPLICADO","XXXXXXXX");
		
		//datos colegio
		CenInstitucionAdm institAdm= new CenInstitucionAdm(usr);
		String nombreInstit=institAdm.getNombreInstitucion(institucion);
		if(nombreInstit!=null)htDatos.put("NOMBRE_INSTITUCION",nombreInstit);
		
		//datos cabecera
		String idPersona=(String) htDatos.get("idPersona");
		
		//Datos Persona
		CenPersonaAdm perAdm = new CenPersonaAdm(usr);
		CenPersonaBean beanPersona = perAdm.getPersonaColegiado(new Long(idPersona), new Integer(institucion));
		htDatos.put("NCOLEGIADO", beanPersona.getColegiado().getNColegiado());
		htDatos.put("NIF", beanPersona.getNIFCIF());
		htDatos.put("NOMBRE", beanPersona.getNombre());
		htDatos.put("APELLIDOS1", beanPersona.getApellido1());
		htDatos.put("APELLIDOS2", beanPersona.getApellido2());
		
		//Datos Cabecera
		htAux=this.obtenerDatosPersonaSociedad(institucion,idPersona,usr, idPagos);
		

		String cuenta=(String)htAux.get("CUENTA_CORRIENTE");
		if (cuenta==null || cuenta.equals("")) {
			String delimIni=CTR+"INI_TODO_CUENTA"+CTR;
			String delimFin=CTR+"FIN_TODO_CUENTA"+CTR;
			String sAux="";
			//plantillaFO=UtilidadesString.reemplazaEntreMarcasCon(plantillaFO, delimIni, delimFin,sAux);
		}else{
			// JBD 03/02/2009 Si hemos obtenido una cuenta debemos ocultar el numero con asteriscos (INC-5635)
			// Nos aseguramos de que el IBAN este completo
			if(cuenta.length()>=15){
				//YA NO VIENE CONCATENADA, YA QUE NO SABEMOS EL TAMAÑO EXACTO DEL IBAN. CONCATENAMOS EN EL PUT			

				// Ocultamos con asteriscos el numero de cuenta
				String numero = UtilidadesString.mostrarIBANConAsteriscos(cuenta);
				
				//NUMERO UNICO DE CUENTA CORRIENTE SIN BANCO
				htAux.put("NUMERO_CUENTA_CORRIENTE", numero);
				
				// Volvemos a unir el IBAN CON BANCO
				String banco = (String)htAux.get("BANCO_CUENTA");
				htAux.put("CUENTA_CORRIENTE", numero+ " " +banco);
				
			}
		}
		htDatos.putAll(htAux);
		
		//Datos de las Asistencias
		Vector datosAsistencias=this.obtenerDatosAsistencia(institucion, idPersona, idPagos, idioma);
		htDatos.put("VASISTENCIAS", datosAsistencias);
		
		//plantillaFO = this.reemplazaRegistros(plantillaFO,datosAsistencias,htDatos,"ASISTENCIA");
		
		//Datos de los Oficios
		Vector datosOficios=this.obtenerDatosOficio(institucion,idPersona, idPagos);
		htDatos.put("VOFICIOS", datosOficios);
		
		//Datos de los Movimientos Varios
		FcsMovimientosVariosAdm movVariosAdm = new FcsMovimientosVariosAdm(usr);
		Vector datosMovimientosVarios = movVariosAdm.getMovimientos(institucion, idPagos, idPersona);		
		
		// JPT - Tratamiento para poner el euro en las cantidades
		Vector vMovimientosVarios = new Vector();
		for(int iMV=0; iMV<datosMovimientosVarios.size(); iMV++){
			Hashtable hMovimiento = (Hashtable) datosMovimientosVarios.get(iMV);
			
			String sCantidad = (String) hMovimiento.get(FcsMovimientosVariosBean.C_CANTIDAD);
			if (sCantidad!=null && !sCantidad.equalsIgnoreCase(""))
				hMovimiento.put(FcsMovimientosVariosBean.C_CANTIDAD, sCantidad.replace(".", ",") + ClsConstants.CODIGO_EURO);
			
			vMovimientosVarios.addElement(hMovimiento);
		}
		htDatos.put("VMOVVARIOS", vMovimientosVarios);
		
		//plantillaFO = this.reemplazaRegistros(plantillaFO,datosOficios,htDatos,"OFICIOS");
		
		String idPerDestino = (String)htDatos.get("IDPERDESTINO");
		
		//Datos del Pago y Totales
		htAux=this.obtenerDatosPago(institucion, idPersona, idPagos, usr,idPerDestino);
		
		//Fecha actual con letra para mostrar en las cartas
		UtilidadesHash.set(htDatos,"FECHA",UtilidadesBDAdm.getFechaEscritaBD(idioma));
		
		htDatos.putAll(htAux);
		
		return htDatos;
	}
	
	
	protected String reemplazarDatos(UsrBean usr, String plantillaFO, Hashtable htDatos) throws ClsExceptions, SIGAException{
		
		String cuenta=(String)htDatos.get("CUENTA_CORRIENTE");
		if (cuenta==null || cuenta.equals("")) {
			String delimIni=CTR+"INI_TODO_CUENTA"+CTR;
			String delimFin=CTR+"FIN_TODO_CUENTA"+CTR;
			String sAux="";
			plantillaFO=UtilidadesString.reemplazaEntreMarcasCon(plantillaFO, delimIni, delimFin,sAux);
		}

	
		//Datos de las Asistencias
		Vector datosAsistencias = (Vector)htDatos.get("VASISTENCIAS");
		plantillaFO = this.reemplazaRegistros(plantillaFO,datosAsistencias,htDatos,"ASISTENCIA");
		
		//Datos de los Oficios
		Vector datosOficios = (Vector)htDatos.get("VOFICIOS");
		plantillaFO = this.reemplazaRegistros(plantillaFO,datosOficios,htDatos,"OFICIOS");
		
		//Datos de los Movimientos Varios
		Vector datosMovimientosVarios = (Vector)htDatos.get("VMOVVARIOS");
		plantillaFO = this.reemplazaRegistros(plantillaFO, datosMovimientosVarios, htDatos, "MOV_VARIOS");
				
		// Jorge PT: no tiene sentido este codigo
		//Datos del Pago y Totales
		String total2=(htDatos.get("TOTAL_MOVIMIENTOS")!=null)?(String)htDatos.get("TOTAL_MOVIMIENTOS"):"0";
		if (total2.length()>2) 
			total2=total2.substring(0,total2.length()-2);
		if (total2.equals("0")) {
			String delimIni=CTR+"INI_TODO_MOVIMIENTOS"+CTR;
			String delimFin=CTR+"FIN_TODO_MOVIMIENTOS"+CTR;
			String sAux="";
			plantillaFO=UtilidadesString.reemplazaEntreMarcasCon(plantillaFO, delimIni, delimFin,sAux);
		}
		
		// Jorge PT: no tiene sentido este codigo
		total2=(htDatos.get("TOTAL_RETENCIONES")!=null)?(String)htDatos.get("TOTAL_RETENCIONES"):"0";
		if (total2.length()>2) total2=total2.substring(0,total2.length()-2);
		if (total2.equals("0")) {
			String delimIni=CTR+"INI_TODO_RETENCIONES"+CTR;
			String delimFin=CTR+"FIN_TODO_RETENCIONES"+CTR;
			String sAux="";
			plantillaFO=UtilidadesString.reemplazaEntreMarcasCon(plantillaFO, delimIni, delimFin,sAux);
		}
		
		return this.reemplazaVariables(htDatos,plantillaFO);
	} //reemplazarDatos()
	
	/**
	 * Obtienes nombre y direccion del letrado o Sociedad
	 * @param idInstitucion Identificador de la institucion
	 * @param idPersona Identificador del letrado o Sociedad
	 * @return
	 * @throws SIGAException
	 */	
	protected Hashtable obtenerDatosPersonaSociedad(String idInstitucion, String idPersona, UsrBean user, String idPagos) throws ClsExceptions {
		Hashtable result = new Hashtable();
		RowsContainer rc = null;		
		
		try {
			 //buscamos el nombre de la persona
			String sql = "SELECT " + UtilidadesMultidioma.getCampoMultidiomaSimple("T.DESCRIPCION",user.getLanguage()) + " || ' ' || P.NOMBRE || ' ' || P.APELLIDOS1 || ' ' || P.APELLIDOS2 AS NOMBRE_PERSONA, " +
					" P.APELLIDOS1 || ' ' || P.APELLIDOS2 AS APELLIDOS_PERSONA, " +
					" P.NOMBRE || NVL2(P.APELLIDOS1, ' ' || P.APELLIDOS1, '') || NVL2(P.APELLIDOS2, ' ' || P.APELLIDOS2, '') AS NOMBRE_DESTINO, " +
					" P.NIFCIF AS NIFCIF_DESTINO " +
				" FROM CEN_PERSONA P, " +
					" CEN_CLIENTE C, " +
					" CEN_TRATAMIENTO T " +
				" WHERE C.IDPERSONA = P.IDPERSONA " +
					" AND C.IDTRATAMIENTO = T.IDTRATAMIENTO " +
					" AND C.IDPERSONA = " + idPersona+
					" AND C.IDINSTITUCION = " + idInstitucion;
			
			 rc = new RowsContainer();
			 rc.find(sql);
			 if(rc!=null && rc.size()>0) {
				 Row r=(Row)rc.get(0);
				 result.putAll(r.getRow());
			 }
				 			
			sql = "SELECT NVL(IDPERDESTINO, '') AS IDPERDESTINO " +
				" FROM FCS_PAGO_COLEGIADO " +
				" WHERE IDPERORIGEN = " + idPersona +
					" AND IDINSTITUCION = " + idInstitucion +
					" AND IDPAGOSJG = " + idPagos;	
			
			rc = new RowsContainer(); 
			rc.find(sql);
			String idPerDestino="";
			if(rc!=null && rc.size()>0){
				Row r = (Row)rc.get(0);											
				result.putAll(r.getRow());
				idPerDestino=r.getString("IDPERDESTINO");
			}	 				 			
			
			// Controlo si no tiene destino, o si el destinatario es el mismo que el origen
			if(idPerDestino==null || idPerDestino.equals("") || idPerDestino.equals(idPersona)){
				 //Si no existe sociedad sacamos los datos relativos a la persona
				 idPerDestino=idPersona;
				 
			} else {
				 //nombre de la sociedad
				 sql = "SELECT PER.NOMBRE " +
						 " || NVL2 (PER.APELLIDOS1, DECODE(PER.APELLIDOS1,'#NA', '', ' ' || PER.APELLIDOS1), '') " + 
						 " || NVL2(PER.APELLIDOS2, ' ' || PER.APELLIDOS2, '') AS NOMBRE_SOCIEDAD, " +
						 " PER.NIFCIF AS CIF_SOCIEDAD " +
					" FROM CEN_PERSONA PER, CEN_CLIENTE CLI " +
					" WHERE CLI.IDPERSONA = PER.IDPERSONA " +
						" AND CLI.IDPERSONA = " + idPerDestino +
						" AND CLI.IDINSTITUCION = " + idInstitucion;
				 
				 rc = new RowsContainer();
				 rc.find(sql);
				 if(rc!=null && rc.size()>0){
					 Row r = (Row)rc.get(0);
					 result.putAll(r.getRow());
					 result.put("NOMBRE_DESTINO", r.getString("NOMBRE_SOCIEDAD"));
					 result.put("NIFCIF_DESTINO", r.getString("CIF_SOCIEDAD"));
				 }
				 
				 // OBTENGO LA DIRECCION DE LA SOCIEDAD
				 this.obtenerDireccion(idPerDestino, idInstitucion, "_SOCIEDAD", result);
			}
			 
			// OBTENGO LA DIRECCION DESTINO
			this.obtenerDireccion(idPerDestino, idInstitucion, "", result);
			 
			// OBTENGO LA DIRECCION DE LA PERSONA
			this.obtenerDireccion(idPersona, idInstitucion, "_PERSONA", result);			
					
			sql = "SELECT * " +
				" FROM FAC_ABONO " +
				" WHERE IDPERSONA = " + idPerDestino +
					" AND IDINSTITUCION = " + idInstitucion +
					" AND IDPAGOSJG = " + idPagos + 
					" AND IDPERORIGEN = " + idPersona;
			
			rc=new RowsContainer();
			rc.find(sql);
			String idCuenta="";
			if(rc!=null && rc.size()>0){
				Row r = (Row)rc.get(0);											
				result.putAll(r.getRow());
				idCuenta=r.getString("IDCUENTA");
			}	
			
			if (!(idCuenta==null || idCuenta.equals(""))){
				// Datos Bancarios de la sociedad o persona
			    sql = "SELECT DECODE(CUEN.IBAN, NULL, '', CUEN.IBAN) CUENTA_CORRIENTE,    "+   
			    	"		  DECODE(Substr(Ban.Nombre, 1, 1), '~', '', Ban.Nombre) BANCO_CUENTA "+
			    	" FROM CEN_CUENTASBANCARIAS CUEN, " +
			    		" CEN_BANCOS BAN " +
			    	" WHERE BAN.CODIGO = CUEN.CBO_CODIGO " +
			    		" AND CUEN.FECHABAJA IS NULL " +
			    		" AND CUEN.Idcuenta = " + idCuenta +
			    		" AND CUEN.IDINSTITUCION = " + idInstitucion +
			    		" AND CUEN.IDPERSONA = " + idPerDestino;
			    
				rc = new RowsContainer();
				rc.find(sql);
				if(rc!=null && rc.size()>0){
					Row r=(Row)rc.get(0);
					result.putAll(r.getRow());
				}
				
			} else { 
				result.put("CUENTA_CORRIENTE","");
			}
			
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		}

		return result;
	}
	
	
	/**
	 * Obtiene el listado de datos del pago de las asistencias 
	 * @param idInstitucion
	 * @param idPersona
	 * @param idioma
	 * @return
	 * @throws SIGAException
	 */	
	protected Vector obtenerDatosAsistencia(String idInstitucion, String idPersona, String idPagos, String idioma) throws ClsExceptions {
		Vector result= new Vector();
		String sinActuaciones = UtilidadesString.getMensajeIdioma(idioma,"informes.colegiadosPagos.guardiaSinActuaciones"); //"Guardia sense actuacions";
		boolean porActuacion = false;
		
		try {
			String sql1 = " SELECT TO_CHAR(FA.FECHAINICIO, 'DD/MM/YYYY') AS FECHADESDE, "					
					+ " TO_CHAR(FA.FECHAINICIO, 'DD/MM/YYYY') AS FECHAHASTA, "
					+ " TO_CHAR(FA.FECHAINICIO, 'DD/MM/YYYY') AS FECHAINICIO, "
					+ " ( "
						+ " SELECT TO_CHAR(MAX(CAB.FECHA_FIN), 'DD/MM/YYYY') "
						+ " FROM SCS_CABECERAGUARDIAS CAB "
						+ " WHERE CAB.IDTURNO = GU.IDTURNO "
							+ " AND CAB.IDINSTITUCION = GU.IDINSTITUCION "
							+ " AND CAB.IDGUARDIA = GU.IDGUARDIA "
							+ " AND CAB.IDCALENDARIOGUARDIAS = FA.IDCALENDARIOGUARDIAS "
							+ " AND CAB.IDPERSONA = COL.IDPERORIGEN "
						+ " GROUP BY CAB.IDINSTITUCION, CAB.IDGUARDIA, CAB.IDCALENDARIOGUARDIAS, CAB.IDPERSONA "
					+ " ) AS FECHAFIN, "
					+ " TU.NOMBRE AS TURNO, "
					+ " TU.ABREVIATURA AS ABREVIATURA_TURNO, "
					+ " F_SIGA_FORMATONUMERO(COL.IMPASISTENCIA, 2) AS IMPORTEPAGADO, "					
					+ " F_SIGA_FORMATONUMERO(fa.precioaplicado, 2) AS IMPORTE_ACTUACION, "					
					+ " GU.NOMBRE AS NOMBRE_GUARDIA, "
					+ " PAG.IDFACTURACION, "
					+ " FA.IDTURNO, "
					+ " FA.IDGUARDIA, "
					+ " FA.IDCALENDARIOGUARDIAS, "
					+ " FA.IDAPUNTE "
				+ " FROM FCS_PAGO_COLEGIADO COL, "
					+ " FCS_PAGOSJG PAG, "
					+ " FCS_FACT_APUNTE FA, "
					+ " SCS_TURNO TU, " 
					+ " SCS_GUARDIASTURNO GU "
				// Obtengo datos de FCS_PAGO_COLEGIADO (COL)
				+ " WHERE COL.IDINSTITUCION = " + idInstitucion
					+ " AND COL.IDPERORIGEN = " +idPersona
					+ " AND COL.IDPAGOSJG = " +idPagos
					// Relacion FCS_PAGO_COLEGIADO (COL) con FCS_PAGOSJG (PAG)
					+ " AND PAG.IDINSTITUCION = COL.IDINSTITUCION "
					+ " AND PAG.IDPAGOSJG = COL.IDPAGOSJG "
					// Relacion FCS_PAGOSJG (PAG) + FCS_PAGO_COLEGIADO (COL) con FCS_FACT_APUNTE (FA)
					+ " AND FA.IDFACTURACION = PAG.IDFACTURACION "
					+ " AND FA.IDINSTITUCION = PAG.IDINSTITUCION "
					+ " AND FA.IDPERSONA = COL.IDPERORIGEN "
					// Relacion FCS_FACT_APUNTE (FA) con SCS_TURNO (TU)
					+ " AND TU.IDTURNO = FA.IDTURNO "
					+ " AND TU.IDINSTITUCION = FA.IDINSTITUCION "
					// Relacion SCS_TURNO (TU) con SCS_GUARDIASTURNO (GU)
					+ " AND GU.IDINSTITUCION = TU.IDINSTITUCION "
					+ " AND GU.IDTURNO = TU.IDTURNO "
					+ " AND GU.IDGUARDIA = FA.IDGUARDIA "
					// Eliminamos las asistencias de importe 0
					+ " AND FA.PRECIOAPLICADO > 0.0 " 
				+ " ORDER BY FA.FECHAINICIO, GU.NOMBRE, TU.NOMBRE, FA.IDAPUNTE";
	 
			RowsContainer rc=new RowsContainer();
			rc.find(sql1);
			if (rc!=null && rc.size()>0) {
				int size=rc.size();
				for(int i=0;i<size;i++){
					Row r1=(Row)rc.get(i);
					Hashtable htAux=r1.getRow();
					
					String sImportePagado = r1.getString("IMPORTEPAGADO");
					htAux.put("IMPORTEPAGADO", sImportePagado + ClsConstants.CODIGO_EURO);
					
					String sImporteFacturado = r1.getString("IMPORTE_ACTUACION");
					htAux.put("IMPORTE_ACTUACION", sImporteFacturado + ClsConstants.CODIGO_EURO);
					
					String idTurno = r1.getString("IDTURNO");
					String idGuardia = r1.getString("IDGUARDIA");
					String idCalendarioGuardias = r1.getString("IDCALENDARIOGUARDIAS");
					String idFacturacion = r1.getString("IDFACTURACION");
					String fechaDesde = r1.getString("FECHADESDE");
					String idApunte = r1.getString("IDAPUNTE");
					
					// inc7405: Comprobamos si se paga por actuacion para saber si hay que mostrar las actuaciones o no
					String sqlAct = " SELECT 1 "
								+ " FROM SCS_HITOFACTURABLEGUARDIA "
								+ " WHERE IDINSTITUCION = " + idInstitucion
									+ " AND IDTURNO = " + idTurno
									+ " AND IDGUARDIA = " + idGuardia
									+ " AND IDHITO IN (4, 7, 22, 46) ";
					
					RowsContainer rows=new RowsContainer();
					rows.find(sqlAct);
					if (rows!=null && rows.size()>0)
						porActuacion=true;
					else
						porActuacion=false;
					
					String sql2="";
					if (porActuacion) {
						sql2="SELECT F_SIGA_GETRECURSO(TA.DESCRIPCION, " + idioma + " ) AS TIPOACTUACION, " +
								" AAS.ANIO || '/' || AAS.NUMERO || '-' || AAS.IDACTUACION AS ACTUACION, " +
								" AAS.ANIO || '/' || AAS.NUMERO AS ASISTENCIA," +
								" DECODE((ASI.EJGANIO || '/' || ASI.EJGNUMERO), '/', '', ASI.EJGANIO || '/' || ASI.EJGNUMERO) AS NUMEJGASISTENCIA, " +
								" PJG.NOMBRE || ' ' || PJG.APELLIDO1 || ' ' || PJG.APELLIDO2 AS NOMBRE_ASISTIDO, " +							
								" PJG.NOMBRE AS NOM_ASISTIDO, " +
								" SUBSTR(TRIM(PJG.NOMBRE),1,1) AS ININOM_ASISTIDO, " +
								" PJG.APELLIDO1 AS APE1_ASISTIDO, " +
								" SUBSTR(TRIM(PJG.APELLIDO1),1,1) AS INIAPE1_ASISTIDO, " +
								" PJG.APELLIDO2 AS APE2_ASISTIDO, " +
								" SUBSTR(TRIM(PJG.APELLIDO2),1,1) AS INIAPE2_ASISTIDO, " +
								" TO_CHAR(AAS.FECHA,'DD/MM/YYYY') AS FECHA_ACTUACION, " +
								" DECODE(FAAS.PRECIOAPLICADO, 0, NULL, F_SIGA_FORMATONUMERO(FAAS.PRECIOAPLICADO,2)) AS PRECIO_ACTUACION, " +
								" F_SIGA_GETRECURSO(COS.DESCRIPCION, " + idioma + " ) AS TIPO_DESPLAZAMIENTO, " +
								" F_SIGA_FORMATONUMERO(TACTCOS.IMPORTE, 2) AS IMPORTE_DESPLAZAMIENTO, " +
								" (CASE WHEN INSTR(F_SIGA_GETRECURSO(COS.DESCRIPCION, 1),' 5 km') > 0 THEN '5 km' " +
									" WHEN INSTR(F_SIGA_GETRECURSO(COS.DESCRIPCION, 1),' 25 km') > 0 THEN '25 km' " +
									" WHEN INSTR(F_SIGA_GETRECURSO(COS.DESCRIPCION, 1),' 50 km') > 0 THEN '50 km' " +
									" ELSE F_SIGA_GETRECURSO(COS.DESCRIPCION, " + idioma + " ) " +
									" END) AS ABREVIATURA_DESPLAZAMIENTO, " +
								" NVL(JUZGADOS.NOMBRE, JUZGADOS_ASI.NOMBRE) AS JUZGADO " +
							" FROM FCS_FACT_APUNTE FAP, " +
								" FCS_FACT_ACTUACIONASISTENCIA FAAS, " +
								" SCS_ACTUACIONASISTENCIA AAS, " +
								" SCS_ASISTENCIA ASI, " +
								" SCS_PERSONAJG PJG, " +
								" SCS_ACTUACIONASISTCOSTEFIJO  ACTCOS, " +
								" SCS_TIPOACTUACIONCOSTEFIJO TACTCOS, " +
								" SCS_COSTEFIJO COS, " +
								" SCS_TIPOACTUACION TA, " +
								" SCS_JUZGADO JUZGADOS, " +
								" SCS_JUZGADO JUZGADOS_ASI " +
							// Obtengo datos de FCS_FACT_APUNTE (FAP)
							" WHERE FAP.IDINSTITUCION = " + idInstitucion +
								" AND FAP.IDFACTURACION = " + idFacturacion +
								" AND FAP.IDTURNO = " + idTurno +
								" AND FAP.IDGUARDIA = " + idGuardia +
								" AND FAP.IDCALENDARIOGUARDIAS = " + idCalendarioGuardias +
								" AND TRUNC(FAP.FECHAINICIO) = '" + fechaDesde + "' " +
								" AND FAP.Idpersona = " + idPersona +
								" AND FAP.IdApunte = " + idApunte +
								// Relacion FCS_FACT_APUNTE (FAP) con FCS_FACT_ACTUACIONASISTENCIA (FAAS)
								" AND FAAS.IDINSTITUCION = FAP.IDINSTITUCION " +
								" AND FAAS.IDFACTURACION = FAP.IDFACTURACION " +
								" AND FAAS.IDAPUNTE = FAP.IDAPUNTE " +
								" AND FAAS.IDPERSONA = FAP.IDPERSONA " +
								// Relacion FCS_FACT_ACTUACIONASISTENCIA (FAAS) con SCS_ACTUACIONASISTENCIA (AAS)
								" AND AAS.IDINSTITUCION(+) = FAAS.IDINSTITUCION " +
								" AND AAS.NUMERO(+) = FAAS.NUMERO " +
								" AND AAS.ANIO(+) = FAAS.ANIO " +
								" AND AAS.IDACTUACION(+) = FAAS.IDACTUACION " +
								// Relacion FCS_FACT_ACTUACIONASISTENCIA (FAAS) con SCS_ASISTENCIA (ASI)
								" AND ASI.IDINSTITUCION(+) = FAAS.IDINSTITUCION " +
								" AND ASI.NUMERO(+) = FAAS.NUMERO " +
								" AND ASI.ANIO(+) = FAAS.ANIO " +
								// Relacion SCS_ASISTENCIA (ASI) con SCS_PERSONAJG (PJG)
								" AND PJG.IDPERSONA(+) = ASI.IDPERSONAJG " +
								" AND PJG.IDINSTITUCION(+) = ASI.IDINSTITUCION " +
								// Relacion SCS_ACTUACIONASISTENCIA (AAS) con SCS_ACTUACIONASISTCOSTEFIJO (ACTCOS): para sacar los desplazamientos 
								" AND ACTCOS.IDINSTITUCION(+) = AAS.IDINSTITUCION " +
								" AND ACTCOS.ANIO(+) = AAS.ANIO " +
								" AND ACTCOS.NUMERO(+) = AAS.NUMERO " +
								" AND ACTCOS.IDACTUACION(+) = AAS.IDACTUACION " +
								// Relacion SCS_ACTUACIONASISTCOSTEFIJO (ACTCOS) con SCS_TIPOACTUACIONCOSTEFIJO (TACTCOS)
								" AND TACTCOS.IDINSTITUCION(+) = ACTCOS.IDINSTITUCION " +       
								" AND TACTCOS.IDTIPOASISTENCIA(+) = ACTCOS.IDTIPOASISTENCIA " + 
								" AND TACTCOS.IDTIPOACTUACION(+) = ACTCOS.IDTIPOACTUACION " +   
								" AND TACTCOS.IDCOSTEFIJO(+) = ACTCOS.IDCOSTEFIJO " +
								// Relacion SCS_ACTUACIONASISTCOSTEFIJO (ACTCOS) con SCS_COSTEFIJO (COS)
								" AND COS.IDINSTITUCION(+) = ACTCOS.IDINSTITUCION " +            
								" AND COS.IDCOSTEFIJO(+) = ACTCOS.IDCOSTEFIJO " +      
								// Relacion SCS_ACTUACIONASISTENCIA (AAS) con SCS_TIPOACTUACION (TA)
								" AND TA.idinstitucion = AAS.idinstitucion " +
								" AND TA.idtipoasistencia = AAS.idtipoasistencia " +
								" AND TA.idtipoactuacion = AAS.idtipoactuacion " +	
								// Relacion SCS_ACTUACIONASISTENCIA (AAS) con SCS_JUZGADO (JUZGADOS) 
								" AND JUZGADOS.IDINSTITUCION(+) = AAS.IDINSTITUCION " + 
								" AND JUZGADOS.IDJUZGADO(+) = AAS.IDJUZGADO " + 
								// Relacion SCS_ASISTENCIA (ASI) con SCS_JUZGADO (JUZGADOS_ASI) 
								" AND JUZGADOS_ASI.IDINSTITUCION(+) = ASI.IDINSTITUCION " +   
								" AND JUZGADOS_ASI.IDJUZGADO(+) = ASI.JUZGADO " +
							" ORDER BY AAS.ANIO, AAS.NUMERO, AAS.IDACTUACION, ASI.EJGANIO, ASI.EJGNUMERO, NOMBRE_ASISTIDO";
						
					} else {
						// inc7405 // Si se factura por asistencia solo escribimos la primera actuacion de la asistencia
						// asi que filtramos para que salga solo la primera actuacion
						sql2= " SELECT '' AS TIPOACTUACION, " +
								" '' AS ACTUACION, " +
								" ASI.ANIO || '/' || ASI.NUMERO AS ASISTENCIA, " +
								" DECODE((ASI.EJGANIO || '/' || ASI.EJGNUMERO), '/', '', ASI.EJGANIO || '/' || ASI.EJGNUMERO) AS NUMEJGASISTENCIA, " +
								" PJG.NOMBRE || ' ' || PJG.APELLIDO1 || ' ' || PJG.APELLIDO2 AS NOMBRE_ASISTIDO, " +
								" PJG.NOMBRE AS NOM_ASISTIDO, " +
								" SUBSTR(TRIM(PJG.NOMBRE),1,1) AS ININOM_ASISTIDO, " +
								" PJG.APELLIDO1 AS APE1_ASISTIDO, " +
								" SUBSTR(TRIM(PJG.APELLIDO1),1,1) AS INIAPE1_ASISTIDO, " +
								" PJG.APELLIDO2 AS APE2_ASISTIDO, " +
								" SUBSTR(TRIM(PJG.APELLIDO2),1,1) AS INIAPE2_ASISTIDO, " +
								" TO_CHAR(ASI.FECHAHORA, 'DD/MM/YYYY') AS FECHA_ACTUACION, " +
								" DECODE(FASI.PRECIOAPLICADO, 0, NULL, F_SIGA_FORMATONUMERO(FASI.PRECIOAPLICADO,2)) AS PRECIO_ACTUACION, " +
								" '' AS TIPO_DESPLAZAMIENTO, " +
								" F_SIGA_FORMATONUMERO(SUM(TACTCOS.IMPORTE), 2) AS IMPORTE_DESPLAZAMIENTO, " +
								" '' AS ABREVIATURA_DESPLAZAMIENTO, " +
								" JUZGADOS.NOMBRE AS JUZGADO " +
							" FROM FCS_FACT_ASISTENCIA FASI, " +
								" SCS_ASISTENCIA ASI, " +
								" FCS_FACT_APUNTE FAP, " +
								" SCS_PERSONAJG PJG, " +
								" FCS_FACT_ACTUACIONASISTENCIA FAAS, " +
								" SCS_ACTUACIONASISTENCIA AAS, " +
								" SCS_ACTUACIONASISTCOSTEFIJO ACTCOS, " +
								" SCS_TIPOACTUACIONCOSTEFIJO TACTCOS, " +
								" SCS_COSTEFIJO COS, " +
								" SCS_TIPOACTUACION TA, " +
								" SCS_JUZGADO JUZGADOS " +
							// Obtengo datos de FCS_FACT_APUNTE (FAP)
							" WHERE FAP.IDINSTITUCION = " + idInstitucion +
								" AND FAP.IDFACTURACION = " + idFacturacion +
								" AND FAP.IDTURNO = " + idTurno +
								" AND FAP.IDGUARDIA = " + idGuardia +
								" AND FAP.IDCALENDARIOGUARDIAS = " + idCalendarioGuardias +
								" AND TRUNC(FAP.FECHAINICIO) = '" + fechaDesde + "' "+
								" AND FAP.IDPERSONA = " + idPersona +
								" AND FAP.IDAPUNTE = " + idApunte +
								// Relacion FCS_FACT_APUNTE (FAP) con FCS_FACT_ASISTENCIA (FASI)
								" AND FASI.IDINSTITUCION = FAP.IDINSTITUCION " +
								" AND FASI.IDFACTURACION = FAP.IDFACTURACION " +
								" AND FASI.IDAPUNTE = FAP.IDAPUNTE " +
								// Relacion FCS_FACT_ASISTENCIA (FASI) con SCS_ASISTENCIA (ASI)
								" AND ASI.IDINSTITUCION = FASI.IDINSTITUCION " +
								" AND ASI.ANIO = FASI.ANIO " +
								" AND ASI.NUMERO = FASI.NUMERO " +
								// Relacion SCS_ASISTENCIA (ASI) con SCS_PERSONAJG (PJG)
								" AND PJG.IDPERSONA(+) = ASI.IDPERSONAJG " +
								" AND PJG.IDINSTITUCION(+) = ASI.IDINSTITUCION " +
								// Relacion SCS_ASISTENCIA (FASI) con SCS_PERSONAJG (FAAS)
								" AND FAAS.IDINSTITUCION(+) = FASI.IDINSTITUCION " +
								" AND FAAS.IDFACTURACION(+) = FASI.IDFACTURACION " +
								" AND FAAS.ANIO(+) = FASI.ANIO " +
								" AND FAAS.NUMERO(+) = FASI.NUMERO " +
								// Relacion SCS_PERSONAJG (FAAS) con SCS_ACTUACIONASISTENCIA (AAS)
								" AND AAS.IDINSTITUCION(+) = FAAS.IDINSTITUCION " +
								" AND AAS.NUMERO(+) = FAAS.NUMERO " +
								" AND AAS.ANIO(+) = FAAS.ANIO " +
								" AND AAS.IDACTUACION(+) = FAAS.IDACTUACION " +
								// Relacion SCS_ACTUACIONASISTENCIA (AAS) con SCS_ACTUACIONASISTCOSTEFIJO (ACTCOS): para sacar los desplazamientos 
								" AND ACTCOS.IDINSTITUCION(+) = AAS.IDINSTITUCION " +
								" AND ACTCOS.ANIO(+) = AAS.ANIO " +
								" AND ACTCOS.NUMERO(+) = AAS.NUMERO  " +
								" AND ACTCOS.IDACTUACION(+) = AAS.IDACTUACION " +
								// Relacion SCS_ACTUACIONASISTCOSTEFIJO (ACTCOS) con SCS_TIPOACTUACIONCOSTEFIJO (TACTCOS)
								" AND TACTCOS.IDINSTITUCION(+) = ACTCOS.IDINSTITUCION " +
								" AND TACTCOS.IDTIPOASISTENCIA(+) = ACTCOS.IDTIPOASISTENCIA " +
								" AND TACTCOS.IDTIPOACTUACION(+) = ACTCOS.IDTIPOACTUACION " +
								" AND TACTCOS.IDCOSTEFIJO(+) = ACTCOS.IDCOSTEFIJO " +
								// Relacion SCS_ACTUACIONASISTCOSTEFIJO (ACTCOS) con SCS_COSTEFIJO (COS)
								" AND COS.IDINSTITUCION(+) = ACTCOS.IDINSTITUCION " +
								" AND COS.IDCOSTEFIJO(+) = ACTCOS.IDCOSTEFIJO " +
								// Relacion SCS_ACTUACIONASISTENCIA (AAS) con SCS_TIPOACTUACION (TA)
								" AND TA.IDINSTITUCION(+) = AAS.IDINSTITUCION " +
								" AND TA.IDTIPOASISTENCIA(+) = AAS.IDTIPOASISTENCIA " +
								" AND TA.IDTIPOACTUACION(+) = AAS.IDTIPOACTUACION " +
								// Relacion SCS_ASISTENCIA (ASI) con SCS_JUZGADO (JUZGADOS) 
								" AND JUZGADOS.IDINSTITUCION(+) = ASI.IDINSTITUCION " +   
								" AND JUZGADOS.IDJUZGADO(+) = ASI.JUZGADO " +
							" GROUP BY ASI.ANIO, ASI.NUMERO, ASI.EJGANIO, ASI.EJGNUMERO, PJG.NOMBRE, PJG.APELLIDO1, PJG.APELLIDO2, ASI.FECHAHORA, FASI.PRECIOAPLICADO, JUZGADOS.NOMBRE " +
							" ORDER BY ASI.ANIO, ASI.NUMERO, PJG.NOMBRE || ' ' || PJG.APELLIDO1 || ' ' || PJG.APELLIDO2 " ;
					}

					RowsContainer rc2 = new RowsContainer();
					rc2.find(sql2);
					if (rc2!=null && rc2.size()>0) {
						//tratar el primero
						Row r2 = (Row)rc2.get(0);						
						htAux.putAll(r2.getRow());
						
						String sImporteDesplazamineto = r1.getString("IMPORTE_DESPLAZAMIENTO");
						htAux.put("IMPORTE_DESPLAZAMIENTO", sImporteDesplazamineto + ClsConstants.CODIGO_EURO);
						
						String sPrecioAplicado = r1.getString("PRECIO_ACTUACION");
						if (sPrecioAplicado!=null && !sPrecioAplicado.equalsIgnoreCase("")) 
							htAux.put("PRECIO_ACTUACION",sPrecioAplicado+ClsConstants.CODIGO_EURO);
						
						result.addElement(htAux);
						
						//tratar el resto
						int size2=rc2.size();
						for (int j=1; j<size2; j++) {
							r2=(Row)rc2.get(j);
							htAux=r2.getRow();
							
							sImporteDesplazamineto = r2.getString("IMPORTE_DESPLAZAMIENTO");
							htAux.put("IMPORTE_DESPLAZAMIENTO", sImporteDesplazamineto + ClsConstants.CODIGO_EURO);
							
							sPrecioAplicado = r1.getString("PRECIO_ACTUACION");
							if (sPrecioAplicado!=null && !sPrecioAplicado.equalsIgnoreCase("")) 
								htAux.put("PRECIO_ACTUACION", sPrecioAplicado + ClsConstants.CODIGO_EURO);
							
							result.addElement(htAux);
						}
					} else {
						htAux.put("NOMBRE_ASISTIDO", sinActuaciones);
						result.addElement(htAux);
					}
				}
			}
			
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		}
		
		return result;
	}
	
	/**
	 * Obtiene el listado de datos del pago de los oficios 
	 * @param idInstitucion
	 * @param idPersona
	 * @param idPagos
	 * @return
	 * @throws SIGAException
	 */
	protected Vector obtenerDatosOficio(String idInstitucion, String idPersona, String idPagos) throws ClsExceptions {
		Vector result = null;

		try {
			String sql = " SELECT " +
				/*" F_SIGA_GETDATOEJGRELDESIGNA(DES.IDINSTITUCION, DES.IDTURNO, DES.ANIO, DES.NUMERO, 1) AS NUMEROEJG, " +
				" F_SIGA_GETDATOEJGRELDESIGNA(DES.IDINSTITUCION, DES.IDTURNO, DES.ANIO, DES.NUMERO, 2) AS ANIO_CAJG, " +
				" F_SIGA_GETDATOEJGRELDESIGNA(DES.IDINSTITUCION, DES.IDTURNO, DES.ANIO, DES.NUMERO, 3) AS NUMERO_CAJG, " +
				" F_SIGA_GETDATOEJGRELDESIGNA(DES.IDINSTITUCION, DES.IDTURNO, DES.ANIO, DES.NUMERO, 4) AS ANIOEJG, " +*/								
				" F_SIGA_GETDATOEJGRELDESIGNA(DES.IDINSTITUCION, DES.IDTURNO, DES.ANIO, DES.NUMERO, 5) AS VALORES_EJG, " +
				" DECODE(COLE.COMUNITARIO, '1', COLE.NCOMUNITARIO, COLE.NCOLEGIADO) AS NUMERO_COLEGIADO, " +
				" AD.FECHA, " +
				" TO_CHAR(AD.FECHA, 'DD/MM/YYYY') AS FECHA_OFICIO, " +
				" TO_CHAR(DES.FECHAENTRADA, 'DD/MM/YYYY') AS FECHA_DESIGNA, " +
				" PRO.NOMBRE AS PROCEDIMIENTO, " +
				" F_SIGA_FORMATONUMERO(COL.IMPOFICIO, 2)  AS IMPORTEPAGADO, " +
				" DES.ANIO || '/' || DES.CODIGO AS ASIOFI, " +
				/*" F_SIGA_GETDEFENDIDOSDESIGNA(DES.IDINSTITUCION, DES.ANIO, DES.IDTURNO, DES.NUMERO, 0) AS NOMBRE_SOLICITANTE, " + // Lista de solicitantes
				" F_SIGA_GETDEFENDIDOSDESIGNA(DES.IDINSTITUCION, DES.ANIO, DES.IDTURNO, DES.NUMERO, 4) AS NOM_SOLICITANTE, " + // Lista de nombres de solicitantes
				" F_SIGA_GETDEFENDIDOSDESIGNA(DES.IDINSTITUCION, DES.ANIO, DES.IDTURNO, DES.NUMERO, 5) AS ININOM_SOLICITANTE, " + // Lista de iniciales de nombres del solicitante
				" F_SIGA_GETDEFENDIDOSDESIGNA(DES.IDINSTITUCION, DES.ANIO, DES.IDTURNO, DES.NUMERO, 6) AS APE1_SOLICITANTE, " + // Lista de primeros apellidos de solicitantes
				" F_SIGA_GETDEFENDIDOSDESIGNA(DES.IDINSTITUCION, DES.ANIO, DES.IDTURNO, DES.NUMERO, 7) AS INIAPE1_SOLICITANTE, " + // Lista de iniciales de primeros apellidos de solicitantes
				" F_SIGA_GETDEFENDIDOSDESIGNA(DES.IDINSTITUCION, DES.ANIO, DES.IDTURNO, DES.NUMERO, 8) AS APE2_SOLICITANTE, " + // Lista de segundos apellidos de solicitantes
				" F_SIGA_GETDEFENDIDOSDESIGNA(DES.IDINSTITUCION, DES.ANIO, DES.IDTURNO, DES.NUMERO, 9) AS INIAPE2_SOLICITANTE, " + // Lista de iniciales de segundos apellidos de solicitantes	*/
				" F_SIGA_GETDEFENDIDOSDESIGNA(DES.IDINSTITUCION, DES.ANIO, DES.IDTURNO, DES.NUMERO, 10) AS VALORES_SOLICITANTE, " + // Lista de valores con los solicitantes
				" F_SIGA_FORMATONUMERO(FACT.PRECIOAPLICADO, 2) AS IMPORTE_PROCEDIMIENTO, " +
				" F_SIGA_FORMATONUMERO(FACT.PRECIOAPLICADO * FACT.PORCENTAJEFACTURADO / 100, 2) AS IMPORTE_OFICIO, " +
				" ACREPROD.PORCENTAJE AS PORCENTAJE_PAGADO, " +
				" ACRE.DESCRIPCION AS ACREDITACION, " +
				" TURNO.NOMBRE AS NOMBRE_TURNO, " +
				" TURNO.ABREVIATURA AS ABREVIATURA_TURNO, " +
				" AD.NUMEROASUNTO AS NUMEROASUNTO, " +
				" DES.NIG, " +			
				" NVL(JUZGADOSAD.NOMBRE, JUZGADOS.NOMBRE) AS JUZGADO " +			
			" FROM FCS_PAGO_COLEGIADO COL, " +
				" SCS_ACTUACIONDESIGNA AD, " +
				" SCS_PROCEDIMIENTOS PRO, " +
				" SCS_DESIGNA DES, " +
				" FCS_FACT_ACTUACIONDESIGNA FACT, " +
				" FCS_PAGOSJG PAG, " +
				" FCS_FACTURACIONJG FAC, " +
				" SCS_ACREDITACIONPROCEDIMIENTO ACREPROD, " +
				" SCS_ACREDITACION ACRE, " +
				" SCS_TURNO TURNO, " +
				" CEN_COLEGIADO COLE, " +	
				" SCS_JUZGADO JUZGADOS, " +
				" SCS_JUZGADO JUZGADOSAD " +
			// Obtengo datos de FCS_PAGO_COLEGIADO (COL)
			" WHERE COL.IDINSTITUCION = " + idInstitucion +
				" AND COL.IDPERORIGEN = " + idPersona +
				" AND COL.IDPAGOSJG = " + idPagos +
				// Relacion FCS_PAGO_COLEGIADO (COL) con FCS_PAGOSJG (PAG)
				" AND PAG.IDINSTITUCION = COL.IDINSTITUCION " +
				" AND PAG.IDPAGOSJG = COL.IDPAGOSJG " +
				// Relacion FCS_PAGO_COLEGIADO (COL) + FCS_PAGOSJG (PAG) con FCS_FACT_ACTUACIONDESIGNA (FACT) --- INCOMPLETA
				" AND FACT.IDINSTITUCION = COL.IDINSTITUCION " +
				" AND FACT.IDPERSONA = COL.IDPERORIGEN " +				
				" AND FACT.IDFACTURACION = PAG.IDFACTURACION " +
				// Relacion FCS_FACT_ACTUACIONDESIGNA (FACT) con FCS_FACTURACIONJG (FAC)
				" AND FAC.IDINSTITUCION = FACT.IDINSTITUCION " +
				" AND FAC.IDFACTURACION = FACT.IDFACTURACION " +				
				// Relacion FCS_FACT_ACTUACIONDESIGNA (FACT) con SCS_ACTUACIONDESIGNA (AD) 
				" AND AD.IDINSTITUCION = FACT.IDINSTITUCION " +
				" AND AD.IDPERSONACOLEGIADO = FACT.IDPERSONA " +
				" AND AD.NUMEROASUNTO = FACT.NUMEROASUNTO " +
				" AND AD.NUMERO = FACT.NUMERO " +
				" AND AD.ANIO = FACT.ANIO " +
				" AND AD.IDTURNO = FACT.IDTURNO " +
				// Relacion SCS_ACTUACIONDESIGNA (AD) con SCS_DESIGNA (DES)
				" AND DES.IDINSTITUCION = AD.IDINSTITUCION " +
				" AND DES.IDTURNO = AD.IDTURNO " +
				" AND DES.ANIO = AD.ANIO " +
				" AND DES.NUMERO = AD.NUMERO " +
				// Relacion SCS_DESIGNA (DES) con SCS_TURNO (TURNO)			
				" AND TURNO.IDTURNO = DES.IDTURNO " +
				" AND TURNO.IDINSTITUCION = DES.IDINSTITUCION " +
				// Relacion SCS_DESIGNA (DES) con SCS_JUZGADO (JUZGADOS)	
				" AND JUZGADOS.IDINSTITUCION(+) = DES.IDINSTITUCION " +   
				" AND JUZGADOS.IDJUZGADO(+) = DES.IDJUZGADO " +				
				// Relacion SCS_ACTUACIONDESIGNA (AD) con CEN_COLEGIADO (COLE)		
				" AND COLE.IDINSTITUCION = AD.IDINSTITUCION " +
				" AND COLE.IDPERSONA = AD.IDPERSONACOLEGIADO " +
				// Relacion SCS_ACTUACIONDESIGNA (AD) con SCS_PROCEDIMIENTOS (PRO)	
				" AND PRO.IDINSTITUCION = AD.IDINSTITUCION " +
				" AND PRO.IDPROCEDIMIENTO = AD.IDPROCEDIMIENTO " +								
				// Relacion SCS_ACTUACIONDESIGNA (AD) con SCS_JUZGADO (JUZGADOSAD)
				" AND JUZGADOSAD.IDINSTITUCION(+) = AD.IDINSTITUCION " +   
				" AND JUZGADOSAD.IDJUZGADO(+) = AD.IDJUZGADO " +
				// Relacion SCS_ACTUACIONDESIGNA (AD) con SCS_ACREDITACIONPROCEDIMIENTO (ACREPROD)
				" AND ACREPROD.IDPROCEDIMIENTO = AD.IDPROCEDIMIENTO " +
				" AND ACREPROD.IDINSTITUCION = AD.IDINSTITUCION_PROC " +
				" AND ACREPROD.IDACREDITACION = AD.IDACREDITACION " +
				// Relacion SCS_ACREDITACIONPROCEDIMIENTO (ACREPROD) con SCS_ACREDITACION (ACRE)
				" AND ACRE.IDACREDITACION = ACREPROD.IDACREDITACION " +
				// JPT: CREO QUE ESTA RELACION ES TRIVIAL " AND FACT.IDINSTITUCION = PAG.IDINSTITUCION " +
				// JPT: CREO QUE ESTA RELACION ES TRIVIAL " AND COL.IDINSTITUCION = AD.IDINSTITUCION " +
			" ORDER BY AD.FECHA, ASIOFI, NUMEROASUNTO, PROCEDIMIENTO";
						
			/**
			 * DATOSOFICIO.VALORES_SOLICITANTE contiene una lista de solicitante con todos los valores
			 * %%NOMBRE_SOLICITANTE%% #1 %%NOM_SOLICITANTE%% #2 %%ININOM_SOLICITANTE%% #3 %%APE1_SOLICITANTE%% #4 %%INIAPE1_SOLICITANTE%% #5 %%APE2_SOLICITANTE%% #6 %%INIAPE2_SOLICITANTE%%  
			 * Adrián Ayala Gómez, Jorge Páez Triviño #1 Adrián, Jorge #2 A, J #3 Ayala, Páez #4 A, P #5 Gómez, Triviño #6 G, T
			 */
			sql = "SELECT SUBSTR(DATOSOFICIO.VALORES_SOLICITANTE, 1, INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#1') - 1) AS NOMBRE_SOLICITANTE, " +
					" SUBSTR(DATOSOFICIO.VALORES_SOLICITANTE, INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#1') + 2, INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#2') - INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#1') - 2) AS NOM_SOLICITANTE, " +
					" SUBSTR(DATOSOFICIO.VALORES_SOLICITANTE, INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#2') + 2, INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#3') - INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#2') - 2) AS ININOM_SOLICITANTE, " +
					" SUBSTR(DATOSOFICIO.VALORES_SOLICITANTE, INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#3') + 2, INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#4') - INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#3') - 2) AS APE1_SOLICITANTE, " +
					" SUBSTR(DATOSOFICIO.VALORES_SOLICITANTE, INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#4') + 2, INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#5') - INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#4') - 2) AS INIAPE1_SOLICITANTE, " +
					" SUBSTR(DATOSOFICIO.VALORES_SOLICITANTE, INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#5') + 2, INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#6') - INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#5') - 2) AS APE2_SOLICITANTE, " +
					" SUBSTR(DATOSOFICIO.VALORES_SOLICITANTE, INSTR(DATOSOFICIO.VALORES_SOLICITANTE,'#6') + 2) AS INIAPE2_SOLICITANTE, " +
					" SUBSTR(DATOSOFICIO.VALORES_EJG, 1, INSTR(DATOSOFICIO.VALORES_EJG,'#1') - 1) AS NUMEROEJG, " +
					" SUBSTR(DATOSOFICIO.VALORES_EJG, INSTR(DATOSOFICIO.VALORES_EJG,'#1') + 2, INSTR(DATOSOFICIO.VALORES_EJG,'#2') - INSTR(DATOSOFICIO.VALORES_EJG,'#1') - 2) AS ANIO_CAJG, " +
					" SUBSTR(DATOSOFICIO.VALORES_EJG, INSTR(DATOSOFICIO.VALORES_EJG,'#2') + 2, INSTR(DATOSOFICIO.VALORES_EJG,'#3') - INSTR(DATOSOFICIO.VALORES_EJG,'#2') - 2) AS NUMERO_CAJG, " +
					" SUBSTR(DATOSOFICIO.VALORES_EJG, INSTR(DATOSOFICIO.VALORES_EJG,'#3') + 2) AS ANIOEJG, " +
					" DATOSOFICIO.* " +
					" FROM (" + sql + ") DATOSOFICIO";		
			
			RowsContainer rc = new RowsContainer();
			rc.find(sql);
			result = new Vector();
			
			if (rc!=null && rc.size()>0) {
				for(int i=0; i<rc.size(); i++){
					Row rDatos= (Row)rc.get(i);
					Hashtable hDatos = rDatos.getRow();
			
					hDatos.put("IMPORTE_PROCEDIMIENTO", ((String) hDatos.get("IMPORTE_PROCEDIMIENTO")) + ClsConstants.CODIGO_EURO);
					hDatos.put("IMPORTE_OFICIO", ((String) hDatos.get("IMPORTE_OFICIO")) + ClsConstants.CODIGO_EURO);
					hDatos.put("IMPORTEPAGADO", ((String) hDatos.get("IMPORTEPAGADO")) + ClsConstants.CODIGO_EURO);
					
					result.addElement(hDatos);
				}
			}			

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al generar el informe");
		}

		return result;
	}		
	
	/**
	 * Calcula la cantidad equivalente al cien por cien, dada una cantidad y el porcentaje al que corresponde
	 * @param porcentaje
	 * @param cantidad
	 * @return
	 */
	protected double obtenerCienPorCien(String porcentaje, double cantidad) {
		double result= 0;
		try {
			int iPorcentaje=Integer.parseInt(porcentaje);
			if(iPorcentaje!=0 && cantidad!=0){
				result=(cantidad*100)/iPorcentaje;
			}
		} catch (Exception e) {
			//me lo trago, devuelvo 0
		}

		return result;
	}
	
	/**
	 * Obtiene el importe total bruto, el IRPF y el importe total neto 
	 * @param totalGeneral
	 * @return
	 * @throws SIGAException
	 */
	protected Hashtable obtenerDatosPago(String idInstitucion, String idPersona, String idPago, UsrBean usr,String IdPerDestino) throws ClsExceptions {
		RowsContainer rc=null;
		Hashtable result= new Hashtable();
		double dTotalAsistencia=0;
		double dTotalFactAsistencia=0;
		double dTotalOficio=0;
		double dTotalFactOficio=0;
		double dCompensadoCaja=0;
		double sPagadoCaja=0;
		double sPagoRectificado=0;
		double sTotalFactura=0;
		double sPagadoBanco;
		String pcAsistencia=null;
		String pcOficio=null;
		String fechaPago=null;
		String idFactura=null;
		String numFactura;
		String conceptoFactura;
		String sDescripcion="";
		double sTotalLiquidacion = 0;
		String sFormaPago;
		int IRPF = 0;
		
		try {
			// Porcentajes DEL PAGO
			StringBuffer buf0 = new StringBuffer();
			buf0.append("select PA.IMPORTEPAGADO, PA.NOMBRE NOMBRE_PAGO, ");
			buf0.append("       DECODE(FA.IMPORTEOFICIO,0,'0',F_SIGA_FORMATONUMERO(PA.IMPORTEOFICIO * 100 / FA.IMPORTEOFICIO, 2)) PORCENTAJE_TURNOS, ");
			buf0.append("       DECODE(FA.IMPORTEGUARDIA,0,'0',F_SIGA_FORMATONUMERO(PA.IMPORTEGUARDIA * 100 / FA.IMPORTEGUARDIA, 2)) PORCENTAJE_ASISTENCIA");
			buf0.append("  from FCS_PAGOSJG PA, FCS_FACTURACIONJG FA");
			buf0.append(" where pa.idinstitucion = "+idInstitucion);
			buf0.append("   and pa.idpagosjg = " +idPago);
			buf0.append("   and pa.idinstitucion = fa.idinstitucion");
			buf0.append("   and pa.idfacturacion = fa.idfacturacion");

			rc = new RowsContainer();
			rc.find(buf0.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result=r.getRow();
				pcAsistencia=(String)r.getString("PORCENTAJE_ASISTENCIA");
				pcOficio=(String)r.getString("PORCENTAJE_TURNOS");
			}
			
			//Obtiene el importe del compensado o pagado por caja o rectificativo
			buf0 = new StringBuffer();
			buf0.append(" SELECT abo.numeroabono numeroabono, ");
			buf0.append(" 	SUM (caj.importe) compensado_factura, ");			
			buf0.append(" 	SUM (efe.importe) pagado_caja ");
			buf0.append(" FROM fac_pagoabonoefectivo efe ");
			buf0.append("	RIGHT JOIN fac_abono abo ");			
			buf0.append(" 		ON efe.idinstitucion = abo.idinstitucion ");
			buf0.append(" 		AND efe.idabono = abo.idabono ");
			buf0.append(" 	LEFT JOIN fac_pagosporcaja caj ");
			buf0.append(" 		ON efe.idinstitucion = caj.idinstitucion ");
			buf0.append(" 		AND efe.idabono = caj.idabono ");
			buf0.append(" 		AND efe.idpagoabono = caj.idpagoabono ");
			buf0.append(" WHERE abo.idpersona = " + IdPerDestino);
			buf0.append(" 	AND abo.idpagosjg = " + idPago);
			buf0.append(" 	AND abo.idinstitucion = " + idInstitucion);
			buf0.append(" 	AND abo.idperorigen = " + idPersona);
			buf0.append(" GROUP BY abo.numeroabono, abo.idpagosjg");		
			
			rc = new RowsContainer();
			rc.find(buf0.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
				if(!r.getString("COMPENSADO_FACTURA").equalsIgnoreCase("")){
					dCompensadoCaja= Double.parseDouble(r.getString("COMPENSADO_FACTURA"));
				}else {    
					dCompensadoCaja = 0;
				}					
			}
			
			result.put("COMPENSADO_FACTURA",UtilidadesString.formatoImporte(dCompensadoCaja)+ClsConstants.CODIGO_EURO);

			result.put("COMPENSADO_CAJA",UtilidadesString.formatoImporte(dCompensadoCaja)+ClsConstants.CODIGO_EURO);
			
			//Se reutiliza la query del detalle de pagos para recuperar importes
			FcsPagosJGAdm pagosAdm = new FcsPagosJGAdm(usr);
			String sql = pagosAdm.getQueryDetallePagoColegiado(idInstitucion, idPago, idPersona, false, usr.getLanguage());
			
			rc = new RowsContainer();
			rc.find(sql);
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
				
				String sTotalGeneral=r.getString("TOTALIMPORTESJCS");
				result.put("IMPORTETOTAL",UtilidadesNumero.formatoCartaPago(sTotalGeneral));
				result.put("TOTAL_GENERAL",UtilidadesNumero.formatoCartaPago(sTotalGeneral)+ClsConstants.CODIGO_EURO);
					
				String sTotalIRPF=r.getString("TOTALIMPORTEIRPF");
				result.put("TOTAL_IRPF",UtilidadesNumero.formatoCartaPago(sTotalIRPF)+ClsConstants.CODIGO_EURO);
				
				String sTotalMovimientos=r.getString("IMPORTETOTALMOVIMIENTOS");
				result.put("TOTAL_MOVIMIENTOS",UtilidadesNumero.formatoCartaPago(sTotalMovimientos)+ClsConstants.CODIGO_EURO);
				
				String sTotalRetenciones=r.getString("IMPORTETOTALRETENCIONES");
				result.put("TOTAL_RETENCIONES",UtilidadesNumero.formatoCartaPago(sTotalRetenciones)+ClsConstants.CODIGO_EURO);
				
				// Añadimos el total bruto
				Double dTotalBruto=	Double.parseDouble(sTotalGeneral) + Double.parseDouble(sTotalMovimientos);
				result.put("TOTAL_BRUTO",UtilidadesNumero.formatoCartaPago(dTotalBruto.toString())+ClsConstants.CODIGO_EURO);
				
				// Añadimos el total neto
				Double dTotalNeto=	dTotalBruto + Double.parseDouble(sTotalIRPF);
				result.put("TOTAL_NETO",UtilidadesNumero.formatoCartaPago(dTotalNeto.toString())+ClsConstants.CODIGO_EURO);
				
				Double dTotalLiquidacion =  Double.parseDouble(sTotalGeneral) + 
											Double.parseDouble(sTotalRetenciones) + 
											Double.parseDouble(sTotalIRPF) + 
											Double.parseDouble(sTotalMovimientos)-
											dCompensadoCaja; // Restamos lo compensado
				result.put("TOTAL_LIQUIDACION",UtilidadesNumero.formatoCartaPago(dTotalLiquidacion.toString())+ClsConstants.CODIGO_EURO);
				
				sTotalLiquidacion = dTotalLiquidacion.doubleValue();
			}
			
			//Obtiene lo que se ha pagado por banco
			if (String.valueOf(sTotalLiquidacion).equals(null) || sTotalLiquidacion == 0 || String.valueOf(sTotalLiquidacion).equals("")) {
				
				buf0 = new StringBuffer();
				buf0.append("SELECT   descripcion DESCRIP");
				buf0.append(" from GEN_RECURSOS");
				buf0.append(" where idrecurso = 'censo.tipoAbono.caja'");
				buf0.append(" and idlenguaje = " +usr.getLanguage());
	
				rc = new RowsContainer();
				rc.find(buf0.toString());
				if(rc!=null && rc.size()>0){
					Row r=(Row)rc.get(0);
					result.putAll(r.getRow());
					result.put("FORMA_PAGO",r.getString("DESCRIP"));
				}
				
			} else {
				
				buf0 = new StringBuffer();
				buf0.append("SELECT   descripcion DESCRIP");
				buf0.append(" from GEN_RECURSOS");
				buf0.append(" where idrecurso = 'censo.tipoAbono.banco'");
				buf0.append(" and idlenguaje = " +usr.getLanguage());
	
				rc = new RowsContainer();
				rc.find(buf0.toString());
				if(rc!=null && rc.size()>0){
					Row r=(Row)rc.get(0);
					result.putAll(r.getRow());
					result.put("FORMA_PAGO",r.getString("DESCRIP"));
				}
			}						
			
			//Obtiene el IRPF,los totales y facturados de oficios
			buf0 = new StringBuffer();
			buf0.append("SELECT NVL(col.impoficio, 0) TOTAL_OFICIO, ");
			buf0.append("NVL(SUM(act.PRECIOAPLICADO*act.porcentajefacturado/100), 0) TOTAL_FACTURADO_OFICIO ");
			buf0.append("FROM FCS_PAGO_COLEGIADO col, FCS_PAGOSJG pag, FCS_FACTURACIONJG fac, FCS_FACT_ACTUACIONDESIGNA act ");
			buf0.append("WHERE col.IDPAGOSJG = "+idPago);
			buf0.append(" and col.idinstitucion = "+idInstitucion);
			buf0.append(" and col.idperorigen = "+idPersona);
			buf0.append(" and col.idinstitucion = pag.idinstitucion ");
			buf0.append(" and col.idpagosjg = pag.idpagosjg ");
			buf0.append(" and pag.idinstitucion = act.idinstitucion ");
			buf0.append(" and pag.idfacturacion = act.idfacturacion ");
			buf0.append(" and fac.idinstitucion = act.idinstitucion ");
			buf0.append(" and fac.idfacturacion = act.idfacturacion ");
			buf0.append(" and col.idperorigen = act.idpersona ");
			buf0.append(" group by col.impoficio ");

			rc = new RowsContainer();
			rc.find(buf0.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
								
				dTotalOficio = Double.parseDouble(r.getString("TOTAL_OFICIO"));
				dTotalFactOficio = Double.parseDouble(r.getString("TOTAL_FACTURADO_OFICIO"));
				result.put("TOTAL_OFICIOS",UtilidadesString.formatoImporte(dTotalOficio)+ClsConstants.CODIGO_EURO);
				result.put("CPC_OFICIOS", UtilidadesString.formatoImporte(dTotalFactOficio)+ClsConstants.CODIGO_EURO);
			}
			
			//Obtiene el IRPF,los totales y facturados de asistencias
			buf0 = new StringBuffer();
			buf0.append("SELECT NVL(col.impasistencia, 0) TOTAL_ASISTENCIA, ");
			buf0.append("NVL(SUM(apu.PRECIOAPLICADO + apu.preciocostesfijos), 0) TOTAL_FACTURADO ");
			buf0.append("FROM FCS_PAGO_COLEGIADO col, FCS_PAGOSJG pag, FCS_FACTURACIONJG fac, FCS_FACT_APUNTE apu ");
			buf0.append("WHERE col.IDPAGOSJG = "+idPago);
			buf0.append(" and col.idinstitucion = "+idInstitucion);
			buf0.append(" and col.idperorigen = "+idPersona);
			buf0.append(" and col.idinstitucion = pag.idinstitucion ");
			buf0.append(" and col.idpagosjg = pag.idpagosjg ");
			buf0.append(" and pag.idinstitucion = apu.idinstitucion ");
			buf0.append(" and pag.idfacturacion = apu.idfacturacion ");
			buf0.append(" and fac.idinstitucion = apu.idinstitucion ");
			buf0.append(" and fac.idfacturacion = apu.idfacturacion ");
			buf0.append(" and col.idperorigen = apu.idpersona ");
			buf0.append(" group by col.impasistencia ");

			rc = new RowsContainer();
			rc.find(buf0.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
				
				dTotalAsistencia = Double.parseDouble(r.getString("TOTAL_ASISTENCIA"));
				result.put("TOTAL_ASISTENCIA",UtilidadesString.formatoImporte(dTotalAsistencia)+ClsConstants.CODIGO_EURO);
				dTotalFactAsistencia = Double.parseDouble(r.getString("TOTAL_FACTURADO"));
				result.put("CPC_ASISTENCIA", UtilidadesString.formatoImporte(dTotalFactAsistencia)+ClsConstants.CODIGO_EURO);
			}
			
			//obtiene el irpf del pago
			FcsPagoColegiadoAdm pagoAdm = new FcsPagoColegiadoAdm(usr);
			String irpf = pagoAdm.getIrpf(idInstitucion, idPago, idPersona);		
			result.put("IRPF",irpf);
			
			// jbd 18/2/2010 inc-6868 Sacamos la fecha del pago para que la usen en vez de sysdate
			buf0 = new StringBuffer();
			buf0.append("SELECT TO_CHAR(PEP.FECHAESTADO, 'DD/MM/YYYY') FECHAESTADO FROM FCS_PAGOS_ESTADOSPAGOS PEP WHERE PEP.IDESTADOPAGOSJG = 30 ");
			buf0.append(" AND PEP.IDINSTITUCION = " + idInstitucion);
			buf0.append(" AND IDPAGOSJG= " + idPago);
			rc = new RowsContainer();
			rc.find(buf0.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
				
				fechaPago = r.getString("FECHAESTADO");
				result.put("FECHA_PAGO",UtilidadesString.getFechaEscrita(fechaPago,"dd/MM/yyyy",usr.getLanguageExt()));
			}
		
			//fecha de abono del disco bancario
			
			buf0 = new StringBuffer();
			buf0.append(" SELECT TO_CHAR(fd.fecha, 'DD/MM/YYYY') FECHAABONODISCOBANCO ");
			buf0.append(" FROM FAC_ABONO fa, ");
			buf0.append(" 	FCS_PAGOSJG fp, ");
			buf0.append(" 	fac_abonoincluidoendisquete fi, ");
			buf0.append(" 	fac_disqueteabonos fd ");
			buf0.append(" WHERE fa.idinstitucion = " + idInstitucion);
			buf0.append(" 	AND fa.idpagosjg = " + idPago);
			buf0.append(" 	AND fa.idpersona = " + IdPerDestino);
			buf0.append(" 	AND fa.idpersona = " + idPersona);
			buf0.append(" 	AND fa.idinstitucion = fa.idinstitucion ");
			buf0.append(" 	AND fa.idpagosjg = fp.idpagosjg ");
			buf0.append(" 	AND fi.idinstitucion = fa.idinstitucion ");
			buf0.append(" 	AND fi.idabono = fa.idabono ");
			buf0.append(" 	AND fi.idinstitucion = fd.idinstitucion ");
			buf0.append(" 	AND fi.iddisqueteabono = fd.iddisqueteabono ");			
			rc = new RowsContainer();
			rc.find(buf0.toString());
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
				result.put("FECHAABONODISCOBANCO",r.getString("FECHAABONODISCOBANCO"));
			}			
		}catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		}		
		return result;
	}
	
	public String getDireccionCartaPago (String idSociedad, String idInstitucion, String idDireccion, String tituloEtiqueta) throws ClsExceptions, SIGAException {
	   String sql = "SELECT D.DOMICILIO AS DOMICILIO" + tituloEtiqueta + ", " +
	    		" D.CODIGOPOSTAL AS CODIGOPOSTAL" + tituloEtiqueta + ", " +
	    		"(select p.nombre from cen_poblaciones p where p.idpoblacion=d.idpoblacion) AS POBLACION" + tituloEtiqueta + ", " +
	    		"(select p.nombre from cen_provincias p where p.idprovincia=d.idprovincia) AS PROVINCIA" + tituloEtiqueta +
	    	" FROM CEN_DIRECCIONES D, CEN_DIRECCION_TIPODIRECCION DTD " +
	    	" WHERE D.IDDIRECCION = DTD.IDDIRECCION " +
	    		" AND D.IDINSTITUCION = DTD.IDINSTITUCION " +
	    		" AND D.IDPERSONA = DTD.IDPERSONA " +
	    		" AND d.fechabaja is null " +
	    		" AND D.IDPERSONA = " + idSociedad +
	    		" AND D.IDINSTITUCION = " + idInstitucion;
            
		if (idDireccion != null){
			if (isNumeric(idDireccion)) { 
				sql += " AND DTD." + CenDireccionTipoDireccionBean.C_IDTIPODIRECCION + " = " +idDireccion;				
			} else {
				sql += " AND UPPER(D.PREFERENTE) LIKE UPPER('%" + idDireccion + "%') ";
			}
		}			 	

		return sql;                        
    }
	
	public void obtenerDireccion (String idPersona, String idInstitucion, String tituloEtiqueta, Hashtable resultado) throws ClsExceptions, SIGAException {		
		if (tituloEtiqueta == null) {
			tituloEtiqueta = "";
		}
		
		String direccion=this.getDireccionCartaPago(idPersona, idInstitucion, "C", tituloEtiqueta);// buscamos una direccion preferente correo
		RowsContainer rc = new RowsContainer();
		rc.find(direccion);
		if(rc!=null && rc.size()>0){
			Row r=(Row)rc.get(0);
			resultado.putAll(r.getRow());
		}
		 
		if (rc==null || rc.size()==0 ) {
			direccion=this.getDireccionCartaPago(idPersona, idInstitucion, "2", tituloEtiqueta);// si no hay direccion preferente Correo, buscamos cualquier direccion de despacho.
		 	rc = new RowsContainer();
			rc.find(direccion);
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				resultado.putAll(r.getRow());
				
			} else {
		 		direccion=this.getDireccionCartaPago(idPersona, idInstitucion, "3", tituloEtiqueta);// si no hay direccion de despacho, buscamos cualquier direccion de correo.
		 		rc = new RowsContainer();
				rc.find(direccion);
				if(rc!=null && rc.size()>0){
					Row r=(Row)rc.get(0);
					resultado.putAll(r.getRow());
					
				} else {
		 			direccion=this.getDireccionCartaPago(idPersona, idInstitucion, "", tituloEtiqueta);// si no hay direccion de correo, buscamos cualquier direccion.
		 			rc = new RowsContainer();
					rc.find(direccion);
					if(rc!=null && rc.size()>0){
						Row r=(Row)rc.get(0);
						resultado.putAll(r.getRow());
					}				 		   
		 		}  
		 	}
		 }
	}
	
	private static boolean isNumeric(String cadena){	
		try {		
			Integer.parseInt(cadena);		
			return true;	
		} catch (NumberFormatException nfe){		
			return false;	
		}
	}
}

	