
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
			// Nos aseguramos de que el numero de cuenta este completo
			if(cuenta.length()>=23){
				// Como viene concatenada la cuenta recuperamos solo el numero de cuenta.
				String numero = cuenta.substring(13, 23);
				// Le ocultamos parte con asteriscos
				numero = UtilidadesString.mostrarNumeroCuentaConAsteriscos(numero);
				// Volvemos a unir la cuenta
				cuenta = cuenta.substring(0, 13) + numero + cuenta.substring(23);
				htAux.put("CUENTA_CORRIENTE", cuenta);
				// 
				htAux.put("NUMERO_CUENTA_CORRIENTE", cuenta.substring(0, 23));
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
			    sql = "SELECT DECODE(CUEN.NUMEROCUENTA,NULL,'',CUEN.CBO_CODIGO||' '||CUEN.CODIGOSUCURSAL||' '||CUEN.DIGITOCONTROL||' '||CUEN.NUMEROCUENTA||' '|| Decode(Substr(Ban.Nombre, 1, 1), '~', '', Ban.Nombre)) CUENTA_CORRIENTE " +
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
		String sinActuaciones=//"Guardia sense actuacions";
			UtilidadesString.getMensajeIdioma(idioma,"informes.colegiadosPagos.guardiaSinActuaciones");
		int contador=0;
		Hashtable codigo =new Hashtable();
		double importeAsistenciaAux=0;
		boolean porActuacion = false;
		
		try {
			StringBuffer sql1 = new StringBuffer();
			sql1.append("select to_char(fa.fechainicio, 'DD/MM/YYYY') FECHADESDE, ");
			sql1.append(" to_char(fa.fechainicio, 'DD/MM/YYYY') FECHAHASTA, ");
			sql1.append(" (select to_char(max(cab.fecha_fin), 'DD/MM/YYYY') ");
			sql1.append(" from SCS_CABECERAGUARDIAS cab ");
			sql1.append(" where cab.idturno = gu.idturno ");
			sql1.append(" and cab.idinstitucion = gu.idinstitucion ");
			sql1.append(" and cab.idguardia = gu.idguardia ");
			sql1.append(" and cab.idcalendarioguardias = fa.idcalendarioguardias ");
			sql1.append(" and cab.idpersona = col.idperorigen ");
			sql1.append(" group by cab.idinstitucion, cab.idguardia, cab.idcalendarioguardias, cab.idpersona) FECHAFIN, ");
			sql1.append("TU.NOMBRE TURNO, ");
			sql1.append(" tu.abreviatura ABREVIATURA_TURNO, ");
			sql1.append("f_siga_formatonumero(col.impasistencia, 2) IMPORTEPAGADO, ");
			sql1.append("pag.IDFACTURACION, ");
			sql1.append("fa.IDTURNO, ");
			sql1.append("fa.IDGUARDIA, ");
			sql1.append("fa.IDCALENDARIOGUARDIAS, ");
			sql1.append("f_siga_formatonumero(fa.precioaplicado, 2) IMPORTE_ACTUACION, ");
			sql1.append("fa.IDAPUNTE, ");
			sql1.append("gu.nombre as NOMBRE_GUARDIA ");
			sql1.append("from FCS_PAGO_COLEGIADO col, fcs_pagosjg pag, fcs_fact_apunte fa, SCS_TURNO TU, scs_guardiasturno gu ");
			contador++;
			codigo.put(new Integer(contador),idInstitucion);
			sql1.append(" where COL.IDINSTITUCION = :"+contador);
			contador++;
			codigo.put(new Integer(contador),idPersona);
			sql1.append("   and COL.IDPERORIGEN = :" +contador);
			contador++;
			codigo.put(new Integer(contador),idPagos);	
			sql1.append("   and COL.IDPAGOSJG = :" +contador);
			sql1.append("   and COL.IDINSTITUCION = PAG.IDINSTITUCION ");
			sql1.append("   and COL.IDPAGOSJG = PAG.IDPAGOSJG ");
			sql1.append("   and PAG.IDFACTURACION = FA.IDFACTURACION ");
			sql1.append("   and PAG.IDINSTITUCION= FA.IDINSTITUCION ");
			sql1.append("   and COL.IDPERORIGEN = FA.IDPERSONA ");
			sql1.append("   and FA.IDTURNO = TU.IDTURNO ");
			sql1.append("   and FA.IDINSTITUCION = TU.IDINSTITUCION ");
			sql1.append("   and gu.idinstitucion = tu.idinstitucion");
			sql1.append("   and gu.idturno = tu.idturno");
			sql1.append("   and gu.idguardia = fa.idguardia");
			sql1.append("   and fa.precioaplicado >0.0 "); // Eliminamos las asistencias de importe 0
			sql1.append(" order by fa.fechainicio, GU.NOMBRE, TU.NOMBRE, FA.IDAPUNTE");
	 
			RowsContainer rc=new RowsContainer();
			rc.findBind(sql1.toString(),codigo);
			if(rc!=null && rc.size()>0){
				int size=rc.size();
				for(int i=0;i<size;i++){
					Row r1=(Row)rc.get(i);
					Hashtable htAux=r1.getRow();
					String sImportePagado=r1.getString("IMPORTEPAGADO");
					htAux.put("IMPORTEPAGADO",sImportePagado+ClsConstants.CODIGO_EURO);
					String sImporteFacturado=r1.getString("IMPORTE_ACTUACION");
					htAux.put("IMPORTE_ACTUACION",sImporteFacturado+ClsConstants.CODIGO_EURO);
					
					String idTurno=r1.getString("IDTURNO");
					String idGuardia=r1.getString("IDGUARDIA");
					String idCalendarioGuardias=r1.getString("IDCALENDARIOGUARDIAS");
					String idFacturacion=r1.getString("IDFACTURACION");
					String fechaDesde=r1.getString("FECHADESDE");
					String idApunte=r1.getString("IDAPUNTE");
					
					// inc7405 // Comprobamos si se paga por actuacion para saber si hay que mostrar las actuaciones o no
					StringBuffer sqlAct = new StringBuffer();
					sqlAct.append(" select 1 from scs_hitofacturableguardia where ");
					sqlAct.append(" idinstitucion= "+idInstitucion);
					sqlAct.append(" and idturno= "+idTurno);
					sqlAct.append(" and idguardia= "+idGuardia);
					sqlAct.append(" and idhito in (4,7,22,46)");
					RowsContainer rows=new RowsContainer();
					rows.find(sqlAct.toString());
					if (rows!=null && rows.size()>0)
						porActuacion=true;
					else
						porActuacion=false;
					String sql2="";
					if(porActuacion){
						sql2="SELECT f_siga_getrecurso(TA.descripcion, " + idioma + " ) TIPOACTUACION, " +
							" AAS.ANIO || '/' || AAS.NUMERO || '-' || AAS.IDACTUACION ACTUACION, " +
							" AAS.ANIO || '/' || AAS.NUMERO ASISTENCIA," +
							" DECODE((ASI.EJGANIO || '/' || ASI.EJGNUMERO), '/', '', ASI.EJGANIO || '/' || ASI.EJGNUMERO) NUMEJGASISTENCIA, " +
							" PJG.NOMBRE||' '||PJG.APELLIDO1||' '||PJG.APELLIDO2 NOMBRE_ASISTIDO, " +
							" to_char(AAS.FECHA,'DD/MM/YYYY') FECHA_ACTUACION, " +
							" DECODE(FAAS.PRECIOAPLICADO,0,NULL,f_siga_formatonumero(FAAS.PRECIOAPLICADO,2)) AS PRECIO_ACTUACION, " +
							" f_siga_getrecurso(COS.DESCRIPCION, " + idioma + " ) AS TIPO_DESPLAZAMIENTO, " +
							" f_siga_formatonumero(TACTCOS.IMPORTE, 2) AS IMPORTE_DESPLAZAMIENTO, " +
							" (case when instr(f_siga_getrecurso(COS.DESCRIPCION, 1),' 5 km') > 0 then '5 km' " +
								" when instr(f_siga_getrecurso(COS.DESCRIPCION, 1),' 25 km') > 0 then '25 km' " +
								" when instr(f_siga_getrecurso(COS.DESCRIPCION, 1),' 50 km') > 0 then '50 km' " +
								" else f_siga_getrecurso(COS.DESCRIPCION, "+ idioma+" ) " +
								" end ) ABREVIATURA_DESPLAZAMIENTO, " +
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
						" WHERE FAP.IDINSTITUCION = FAAS.IDINSTITUCION " +
							" and FAP.IDFACTURACION = FAAS.IDFACTURACION " +
							" and FAP.IDAPUNTE = FAAS.IDAPUNTE " +
							" and FAP.IDPERSONA = FAAS.IDPERSONA " +
							" and FAP.IDINSTITUCION = " + idInstitucion +
							" and FAP.IDFACTURACION = " + idFacturacion +
							" and FAP.IDTURNO = " + idTurno +
							" and FAP.IDGUARDIA = " + idGuardia +
							" and FAP.IDCALENDARIOGUARDIAS = " + idCalendarioGuardias +
							" and trunc(FAP.FECHAINICIO) = '" + fechaDesde + "' " +
							" and FAP.Idpersona = " + idPersona +
							" and FAP.IdApunte = " + idApunte +
							" and FAAS.IDINSTITUCION = AAS.IDINSTITUCION (+) " +
							" and FAAS.NUMERO = AAS.NUMERO (+) " +
							" and FAAS.ANIO = AAS.ANIO (+) " +
							" and FAAS.IDACTUACION = AAS.IDACTUACION (+) " +
							" and FAAS.IDINSTITUCION = ASI.IDINSTITUCION (+) " +
							" and FAAS.NUMERO = ASI.NUMERO (+) " +
							" and FAAS.ANIO = ASI.ANIO (+) " +
							" and ASI.IDPERSONAJG = PJG.IDPERSONA(+) " +
							" and ASI.IDINSTITUCION = PJG.IDINSTITUCION(+) " +
							// Cruzamos con tablas nuevas para sacar los desplazamientos 
							" and ACTCOS.IDINSTITUCION(+) = AAS.IDINSTITUCION " +
							" and ACTCOS.ANIO(+) = AAS.ANIO " +
							" and ACTCOS.NUMERO(+) = AAS.NUMERO " +
							" and ACTCOS.IDACTUACION(+) = AAS.IDACTUACION " +
							" and TACTCOS.IDINSTITUCION(+) = ACTCOS.IDINSTITUCION " +       
							" and TACTCOS.IDTIPOASISTENCIA(+) = ACTCOS.IDTIPOASISTENCIA " + 
							" and TACTCOS.IDTIPOACTUACION(+) = ACTCOS.IDTIPOACTUACION " +   
							" and TACTCOS.IDCOSTEFIJO(+) = ACTCOS.IDCOSTEFIJO " +          
							" and COS.IDINSTITUCION(+) = ACTCOS.IDINSTITUCION " +            
							" and COS.IDCOSTEFIJO(+) = ACTCOS.IDCOSTEFIJO " +      
							 //Relacionamos la tabla  SCS_TIPORACTUACION  TA, SCS_ACTUACIONASISTENCIA AAS
							" and TA.idinstitucion = AAS.idinstitucion " +
							" and TA.idtipoasistencia = AAS.idtipoasistencia " +
							" and TA.idtipoactuacion = AAS.idtipoactuacion " +	
							" AND AAS.IDINSTITUCION = JUZGADOS.IDINSTITUCION(+) " + 
							" AND AAS.IDJUZGADO = JUZGADOS.IDJUZGADO(+) " + 
							" AND ASI.IDINSTITUCION = JUZGADOS_ASI.IDINSTITUCION(+) " +   
							" AND ASI.JUZGADO = JUZGADOS_ASI.IDJUZGADO(+) " +
						" ORDER BY AAS.ANIO, AAS.NUMERO, AAS.IDACTUACION, ASI.EJGANIO, ASI.EJGNUMERO, NOMBRE_ASISTIDO";
						
					}else{
						// inc7405 // Si se factura por asistencia solo escribimos la primera actuacion de la asistencia
						// asi que filtramos para que salga solo la primera actuacion
						sql2= " SELECT '' TIPOACTUACION, " +
							" '' ACTUACION, " +
							" ASI.ANIO || '/' || ASI.NUMERO ASISTENCIA, " +
							" DECODE((ASI.EJGANIO || '/' || ASI.EJGNUMERO), '/', '', ASI.EJGANIO || '/' || ASI.EJGNUMERO) NUMEJGASISTENCIA, " +
							" PJG.NOMBRE || ' ' || PJG.APELLIDO1 || ' ' || PJG.APELLIDO2 NOMBRE_ASISTIDO, " +
							" to_char(ASI.FECHAHORA, 'DD/MM/YYYY') FECHA_ACTUACION, " +
							" DECODE(FASI.PRECIOAPLICADO, 0, NULL, f_siga_formatonumero(FASI.PRECIOAPLICADO,2)) AS PRECIO_ACTUACION, " +
							" '' AS TIPO_DESPLAZAMIENTO, " +
							" f_siga_formatonumero(SUM(TACTCOS.IMPORTE), 2) AS IMPORTE_DESPLAZAMIENTO, " +
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
						" WHERE FASI.IDINSTITUCION = ASI.IDINSTITUCION " +
							" and FASI.ANIO = ASI.ANIO " +
							" and FASI.NUMERO = ASI.NUMERO " +
							" and FASI.IDINSTITUCION = FAP.IDINSTITUCION " +
							" and FASI.IDFACTURACION = FAP.IDFACTURACION " +
							" and FASI.IDAPUNTE = FAP.IDAPUNTE " +
							" and FASI.IDINSTITUCION = " + idInstitucion +
							" and FAP.IDFACTURACION = " + idFacturacion +
							" and FAP.IDTURNO = " + idTurno +
							" and FAP.IDGUARDIA = " + idGuardia +
							" and FAP.IDCALENDARIOGUARDIAS = " + idCalendarioGuardias +
							" and trunc(FAP.FECHAINICIO) = '" + fechaDesde + "' "+
							" and FAP.Idpersona = " + idPersona +
							" and FAP.IdApunte = " + idApunte +
							" and ASI.IDPERSONAJG = PJG.IDPERSONA(+) " +
							" and ASI.IDINSTITUCION = PJG.IDINSTITUCION(+) " + 
							" and FASI.IDINSTITUCION = FAAS.IDINSTITUCION(+) " +
							" and FASI.IDFACTURACION = FAAS.IDFACTURACION(+) " +
							" and FASI.ANIO = FAAS.ANIO(+) " +
							" and FASI.NUMERO = FAAS.NUMERO(+) " +
							" and FAAS.IDINSTITUCION = AAS.IDINSTITUCION(+) " +
							" and FAAS.NUMERO = AAS.NUMERO(+) " +
							" and FAAS.ANIO = AAS.ANIO(+) " +
							" and FAAS.IDACTUACION = AAS.IDACTUACION(+) " +
							" and AAS.IDINSTITUCION = ACTCOS.IDINSTITUCION(+) " +
							" and AAS.ANIO = ACTCOS.ANIO(+) " +
							" and AAS.NUMERO = ACTCOS.NUMERO(+) " +
							" and AAS.IDACTUACION = ACTCOS.IDACTUACION(+) " +
							" and ACTCOS.IDINSTITUCION = TACTCOS.IDINSTITUCION(+) " +
							" and ACTCOS.IDTIPOASISTENCIA = TACTCOS.IDTIPOASISTENCIA(+) " +
							" and ACTCOS.IDTIPOACTUACION = TACTCOS.IDTIPOACTUACION(+) " +
							" and ACTCOS.IDCOSTEFIJO = TACTCOS.IDCOSTEFIJO(+) " +
							" and ACTCOS.IDINSTITUCION = COS.IDINSTITUCION(+) " +
							" and ACTCOS.IDCOSTEFIJO = COS.IDCOSTEFIJO(+) " +
							" and AAS.idinstitucion = TA.idinstitucion(+) " +
							" and AAS.idtipoasistencia = TA.idtipoasistencia(+) " +
							" and AAS.idtipoactuacion = TA.idtipoactuacion(+) " +
							" AND ASI.IDINSTITUCION = JUZGADOS.IDINSTITUCION(+) " +   
							" AND ASI.JUZGADO = JUZGADOS.IDJUZGADO(+) " +
						" GROUP BY ASI.ANIO, " +
							" ASI.NUMERO, " +
							" ASI.EJGANIO, " +
							" ASI.EJGNUMERO," +
							" PJG.NOMBRE, " +
							" PJG.APELLIDO1, " +
							" PJG.APELLIDO2, " +
							" ASI.FECHAHORA, " +
							" FASI.PRECIOAPLICADO, " +
							" JUZGADOS.NOMBRE " +
						" ORDER BY ASI.ANIO, ASI.NUMERO, PJG.NOMBRE || ' ' || PJG.APELLIDO1 || ' ' || PJG.APELLIDO2 " ;
					}

					RowsContainer rc2 = new RowsContainer();
					rc2.find(sql2);
					if(rc2!=null && rc2.size()>0){
						//tratar el primero
						Row r2=(Row)rc2.get(0);						
						htAux.putAll(r2.getRow());
						String sImporteDesplazamineto=r1.getString("IMPORTE_DESPLAZAMIENTO");
						htAux.put("IMPORTE_DESPLAZAMIENTO",sImporteDesplazamineto+ClsConstants.CODIGO_EURO);
						String sPrecioAplicado=r1.getString("PRECIO_ACTUACION");
						if (sPrecioAplicado!=null && !sPrecioAplicado.equalsIgnoreCase("")) 
							htAux.put("PRECIO_ACTUACION",sPrecioAplicado+ClsConstants.CODIGO_EURO);
						result.addElement(htAux);
						
						//tratar el resto
						int size2=rc2.size();
						for(int j=1;j<size2;j++){
							r2=(Row)rc2.get(j);
							htAux=r2.getRow();
							sImporteDesplazamineto=r2.getString("IMPORTE_DESPLAZAMIENTO");
							htAux.put("IMPORTE_DESPLAZAMIENTO",sImporteDesplazamineto+ClsConstants.CODIGO_EURO);
							sPrecioAplicado=r1.getString("PRECIO_ACTUACION");
							if (sPrecioAplicado!=null && !sPrecioAplicado.equalsIgnoreCase("")) 
								htAux.put("PRECIO_ACTUACION",sPrecioAplicado+ClsConstants.CODIGO_EURO);
							result.addElement(htAux);
						}
					}else{
						htAux.put("NOMBRE_ASISTIDO",sinActuaciones);
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
	protected Vector obtenerDatosOficio(String idInstitucion, String idPersona, String idPagos) throws ClsExceptions
	{
		Vector result = null;
		int contador = 0;
		Hashtable codigo = new Hashtable();

		try {
			String sql = " SELECT " +
				" f_siga_getdatoejgreldesigna(Des.Idinstitucion,Des.idturno,Des.anio,Des.numero,1) as NUMEROEJG, " +
				" f_siga_getdatoejgreldesigna(Des.Idinstitucion,Des.idturno,Des.anio,Des.numero,4) as ANIOEJG, " +
				" f_siga_getdatoejgreldesigna(Des.Idinstitucion,Des.idturno,Des.anio,Des.numero,3) as NUMERO_CAJG, " +
				" f_siga_getdatoejgreldesigna(Des.Idinstitucion,Des.idturno,Des.anio,Des.numero,2) as ANIO_CAJG, " +
				" decode(cole.comunitario,'1',cole.ncomunitario,cole.ncolegiado) as NUMERO_COLEGIADO, " +
				" AD.FECHA, " +
				" to_char(AD.FECHA,'DD/MM/YYYY') FECHA_OFICIO, " +
				" to_char(DES.fechaentrada,'DD/MM/YYYY') FECHA_DESIGNA, " +
				" PRO.NOMBRE PROCEDIMIENTO, " +
				" f_siga_formatonumero(COL.IMPOFICIO,2)  IMPORTEPAGADO, " +
				" DES.ANIO || '/' || DES.CODIGO  ASIOFI, " +
				" f_siga_getdefendidosdesigna(DES.IDINSTITUCION, des.anio, des.idturno, des.numero,0) NOMBRE_SOLICITANTE, " +
				" f_siga_formatonumero(fact.precioaplicado,2) IMPORTE_PROCEDIMIENTO, " +
				" f_siga_formatonumero(fact.precioaplicado*fact.porcentajefacturado/100,2) IMPORTE_OFICIO, " +
				" acreprod.porcentaje as PORCENTAJE_PAGADO, " +
				" acre.descripcion as ACREDITACION, " +
				" turno.nombre AS NOMBRE_TURNO, " +
				" turno.abreviatura AS ABREVIATURA_TURNO, " +
				" ad.numeroasunto as NUMEROASUNTO, " +
				" DES.NIG, " +			
				" NVL(JUZGADOSAD.NOMBRE, JUZGADOS.NOMBRE) AS JUZGADO " +
			" FROM FCS_PAGO_COLEGIADO COL, " +
				" SCS_ACTUACIONDESIGNA AD, " +
				" SCS_PROCEDIMIENTOS PRO, " +
				" SCS_DESIGNA DES, " +
				" FCS_FACT_ACTUACIONDESIGNA fact, " +
				" FCS_PAGOSJG pag, " +
				" FCS_FACTURACIONJG fac, " +
				" SCS_ACREDITACIONPROCEDIMIENTO acreprod, " +
				" SCS_ACREDITACION acre, " +
				" SCS_TURNO turno, " +
				" CEN_COLEGIADO cole, " +	
				" SCS_JUZGADO JUZGADOS, " +
				" SCS_JUZGADO JUZGADOSAD " +
			" WHERE DES.IDINSTITUCION = AD.IDINSTITUCION " +
				" AND DES.IDTURNO = AD.IDTURNO " +
				" AND DES.ANIO = AD.ANIO " +
				" AND DES.NUMERO = AD.NUMERO " +
				" AND DES.IDTURNO = turno.idturno " +
				" AND DES.Idinstitucion = turno.Idinstitucion " +
				" AND COL.IDINSTITUCION = AD.IDINSTITUCION " +
				" and AD.IDINSTITUCION = PRO.IDINSTITUCION " +
				" and AD.IDPROCEDIMIENTO = PRO.IDPROCEDIMIENTO " +
				" and COL.IDINSTITUCION = " + idInstitucion +
				" and COL.IDPERORIGEN = " + idPersona +
				" and COL.IDPAGOSJG = " + idPagos +
				" and col.idinstitucion = pag.idinstitucion " +
				" and col.idpagosjg = pag.idpagosjg " +
				" and COL.idinstitucion = fact.idinstitucion " +
				" and COL.idperorigen = fact.idpersona " +
				" and ad.idinstitucion = fact.idinstitucion " +
				" and ad.idpersonacolegiado = fact.idpersona " +
				" and ad.NUMEROASUNTO = fact.NUMEROASUNTO " +
				" and ad.NUMERO = fact.NUMERO " +
				" and ad.ANIO = fact.ANIO " +
				" and ad.IDTURNO = fact.IDTURNO " +
				" and pag.idinstitucion = fact.idinstitucion " +
				" and pag.idfacturacion = fact.idfacturacion " +
				" and fac.idinstitucion = fact.idinstitucion " +
				" and fac.idfacturacion = fact.idfacturacion " +
				// Relacionamos las nuevas tablas para sacar la forma de pago
				" and acreprod.idprocedimiento = ad.idprocedimiento " +
				" and acreprod.idinstitucion = ad.idinstitucion_proc " +
				" and acreprod.idacreditacion = ad.idacreditacion " +
				" and acre.idacreditacion = acreprod.idacreditacion " +
				// Relacioamos la tabla SCS_ACTUACIONDESIGNA AD, CEN_COLEGIADO cole
				" and AD.idinstitucion = cole.idinstitucion " +
				" and AD.idpersonacolegiado = cole.idpersona " +
				" AND DES.IDINSTITUCION = JUZGADOS.IDINSTITUCION(+) " +   
				" AND DES.IDJUZGADO = JUZGADOS.IDJUZGADO(+) " +
				" AND AD.IDINSTITUCION = JUZGADOSAD.IDINSTITUCION(+) " +   
				" AND AD.IDJUZGADO = JUZGADOSAD.IDJUZGADO(+) " +
			" ORDER BY AD.FECHA, ASIOFI, NUMEROASUNTO, PROCEDIMIENTO";

			RowsContainer rc = new RowsContainer();
			rc.find(sql);
			result = new Vector();
			if (rc != null && rc.size() > 0) {
				Vector result3 = rc.getAll();
				Vector aux = new Vector();
				for (int g = 0; result3 != null && g < result3.size(); g++) {
					aux.add(g, (Row) result3.get(g));
				}
				for (int g = 0; aux != null && g < aux.size(); g++) {
					Hashtable ht = ((Row) aux.get(g)).getRow();
					ht.put("IMPORTEPAGADO", ((String) ht.get("IMPORTEPAGADO")) + ClsConstants.CODIGO_EURO);
					ht.put("IMPORTE_OFICIO", ((String) ht.get("IMPORTE_OFICIO")) + ClsConstants.CODIGO_EURO);
					ht.put("IMPORTE_PROCEDIMIENTO", ((String) ht.get("IMPORTE_PROCEDIMIENTO")) + ClsConstants.CODIGO_EURO);
					result.add(g, ht);
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

	