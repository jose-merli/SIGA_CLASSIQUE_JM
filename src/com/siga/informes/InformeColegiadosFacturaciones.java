
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
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.general.SIGAException;

/**
 * Clase para el Mantenimiento de los Informes generados en formato PDF para los Pagos a Colegiados.
 */
public class InformeColegiadosFacturaciones extends MasterReport {
	
	protected String formatoImportes="999,999,999,999,990.00";
	
	public Hashtable getDatosInformeColegiado(UsrBean usr, Hashtable htDatos) throws ClsExceptions, SIGAException{
		String idioma = usr.getLanguage();
		UtilidadesHash.set(htDatos,"FECHA",UtilidadesBDAdm.getFechaEscritaBD(idioma));
		UtilidadesHash.set(htDatos,"FECHALETRA",UtilidadesBDAdm.getFechaEscritaBD(idioma));
		String institucion =usr.getLocation();
		String idFacturacion =(String)htDatos.get("idFacturacion");
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
		htAux=this.obtenerDatosPersonaSociedad(institucion,idPersona,usr, idFacturacion);
		htDatos.putAll(htAux);
				
		Hashtable htFacturacion=null;
		FcsFacturacionJGAdm factAdm = new FcsFacturacionJGAdm(usr);
		htFacturacion = factAdm.obtenerDetalleFacturacion(institucion, idFacturacion, idPersona);
		
		String fechaInicio = (String) htFacturacion.get("FECHADESDE");	
		String fechaFin = (String) htFacturacion.get("FECHAHASTA");

		htFacturacion.put("DIAFECHAINICIO", fechaInicio.substring(0,2));
		htFacturacion.put("MESFECHAINICIO", fechaInicio.substring(3,5));
		htFacturacion.put("ANIOFECHAINICIO", fechaInicio.substring(6,10));
	
		htFacturacion.put("DIAFECHAFIN", fechaFin.substring(0,2));
		htFacturacion.put("MESFECHAFIN", fechaFin.substring(3,5));
		htFacturacion.put("ANIOFECHAFIN", fechaFin.substring(6,10));
		
		
		htDatos.putAll(htFacturacion);
		
		//Datos de las Asistencias
		Vector datosAsistencias=this.obtenerDatosAsistencia(institucion, idPersona, idFacturacion, idioma);
		htDatos.put("VASISTENCIAS", datosAsistencias);
		
		//Datos de los Oficios
		Vector datosOficios=this.obtenerDatosOficio(institucion,idPersona, idFacturacion);
		htDatos.put("VOFICIOS", datosOficios);
		
		
		
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
		
		//Datos del Pago y Totales
		String total2=(htDatos.get("TOTAL_MOVIMIENTOS")!=null)?(String)htDatos.get("TOTAL_MOVIMIENTOS"):"0";
		if (total2.length()>2) total2=total2.substring(0,total2.length()-2);
		if (total2.equals("0")) {
			String delimIni=CTR+"INI_TODO_MOVIMIENTOS"+CTR;
			String delimFin=CTR+"FIN_TODO_MOVIMIENTOS"+CTR;
			String sAux="";
			plantillaFO=UtilidadesString.reemplazaEntreMarcasCon(plantillaFO, delimIni, delimFin,sAux);
		}
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
	protected Hashtable obtenerDatosPersonaSociedad(String idInstitucion, String idPersona, UsrBean user, String idFacturacion) throws ClsExceptions {
		Hashtable result= new Hashtable();
		RowsContainer rc=null;
		
		String idSociedad=null;
		//UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		try {
			 //buscamos el nombre de la persona
			String sql=
				 "select "+UtilidadesMultidioma.getCampoMultidiomaSimple("T.DESCRIPCION",user.getLanguage())+"||' '||P.NOMBRE||' '||P.APELLIDOS1||' '||P.APELLIDOS2 NOMBRE_PERSONA, P.APELLIDOS1||' '||P.APELLIDOS2 APELLIDOS_PERSONA" +
				 "  from CEN_PERSONA P, CEN_CLIENTE C, CEN_TRATAMIENTO T " +
				 " where C.IDPERSONA = P.IDPERSONA  " +
				 "   and C.IDTRATAMIENTO = T.IDTRATAMIENTO " +
				 "   and C.IDPERSONA = " +idPersona+
				 "   and C.IDINSTITUCION ="+idInstitucion;
			 rc = new RowsContainer();
			 rc.find(sql);
			 if(rc!=null && rc.size()>0){
				 Row r=(Row)rc.get(0);
				 result.putAll(r.getRow());
			 }
			 
			 //compruebo si actua en nombre de una sociedad
			 sql=
				 "select C.IDPERSONA, C.SOCIEDAD " +
				 "  from CEN_PERSONA P, CEN_COMPONENTES C " +
				 " where C.IDPERSONA = P.IDPERSONA " +
				 "   and C.CEN_CLIENTE_IDPERSONA = " +idPersona+
				 "   and C.CEN_CLIENTE_IDINSTITUCION = "+idInstitucion+
				 "   and (C.FECHABAJA IS NULL or C.FECHABAJA > SYSDATE)";
			 rc = new RowsContainer(); 
			 rc.find(sql);
			 if(rc!=null && rc.size()>0){
				 Row r=(Row)rc.get(0);
				 String sSociedad= r.getString("SOCIEDAD");
				 if(sSociedad!=null && sSociedad.equals(ClsConstants.DB_TRUE)){
					 idSociedad=r.getString("IDPERSONA");
				 }
			 }

			 
			 if(idSociedad==null){
				 //Si no existe sociedad sacamos los datos relativos a la persona
				 idSociedad=idPersona;
				 
			 }else{
				 //nombre de la sociedad
				 sql=
					 "select P.NOMBRE||' '||P.APELLIDOS1||' '||P.APELLIDOS2 NOMBRE_SOCIEDAD" +
					 "  from CEN_PERSONA P, CEN_CLIENTE C" +
					 " where C.IDPERSONA = P.IDPERSONA  " +
					 "   and C.IDPERSONA = " +idSociedad+
					 "   and C.IDINSTITUCION ="+idInstitucion;
				 rc = new RowsContainer();
				 rc.find(sql);
				 if(rc!=null && rc.size()>0){
					 Row r=(Row)rc.get(0);
					 result.putAll(r.getRow());
				 }
			 }
			
			
			//Direccion de la sociedad o persona
			 String direccion="";
				 direccion=this.getDireccionCartaPago(idSociedad,idInstitucion,"C");// buscamos una direccion preferente correo
				 rc = new RowsContainer();
					rc.find(direccion);
					if(rc!=null && rc.size()>0){
						Row r=(Row)rc.get(0);
						result.putAll(r.getRow());
					}
				 
				 if (rc==null || rc.size()==0 ) {
				 	direccion=this.getDireccionCartaPago(idSociedad,idInstitucion,"2");// si no hay direccion preferente Correo, buscamos cualquier direccion de despacho.
				 	 rc = new RowsContainer();
						rc.find(direccion);
						if(rc!=null && rc.size()>0){
							Row r=(Row)rc.get(0);
							result.putAll(r.getRow());
						}
				 	if (rc==null || rc.size()==0 ) {
				 		direccion=this.getDireccionCartaPago(idSociedad,idInstitucion,"3");// si no hay direccion de despacho, buscamos cualquier direccion de correo.
				 		 rc = new RowsContainer();
							rc.find(direccion);
							if(rc!=null && rc.size()>0){
								Row r=(Row)rc.get(0);
								result.putAll(r.getRow());
							}
				 		if (rc ==null || rc.size()==0 ){
				 			direccion=this.getDireccionCartaPago(idSociedad,idInstitucion,"");// si no hay direccion de correo, buscamos cualquier direccion.
				 			 rc = new RowsContainer();
								rc.find(direccion);
								if(rc!=null && rc.size()>0){
									Row r=(Row)rc.get(0);
									result.putAll(r.getRow());
								}				 		   
				 		}  
				 	}
				 }
				
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		}

		return result;
	}		
	
	public String getDireccionCartaPago (String idSociedad, String idInstitucion, String idDireccion) throws ClsExceptions, SIGAException {
		   Hashtable datos=new Hashtable();
		   String sql="";
		   Row result=new Row();
	      
	            RowsContainer rc = new RowsContainer(); 
	            sql=
			    	"select D.DOMICILIO, D.CODIGOPOSTAL," +
			    	"(select p.nombre from cen_poblaciones p where p.idpoblacion=d.idpoblacion) POBLACION," +
			    	"(select p.nombre from cen_provincias p where p.idprovincia=d.idprovincia) PROVINCIA" +
			    	"  from CEN_DIRECCIONES D, CEN_DIRECCION_TIPODIRECCION DTD" +
			    	" where D.IDDIRECCION = DTD.IDDIRECCION  " +
			    	"   and D.IDINSTITUCION = DTD.IDINSTITUCION   " +
			    	"   and D.IDPERSONA = DTD.IDPERSONA    " +
			    	"   and d.fechabaja is null"+
			    	"   and D.IDPERSONA = " +idSociedad+
	                "   and D.IDINSTITUCION="+idInstitucion;
	                
							try{
							 if (idDireccion!=null){
								if ((new Integer(idDireccion)) != null){ 
									sql+=  " AND DTD." + CenDireccionTipoDireccionBean.C_IDTIPODIRECCION + "=" +idDireccion;
								}	
								else {
									throw new Exception ("");
								}
							 }	
							 	
							}	catch(Exception e){
								sql+=  " AND upper(D.PREFERENTE) like upper('%" +idDireccion+"%') ";
							}
							
	       return sql;                        
	    }
	
		/**
	 * Obtiene el listado de datos del pago de las asistencias 
	 * @param idInstitucion
	 * @param idPersona
	 * @param idioma
	 * @return
	 * @throws SIGAException
	 */	
	protected Vector obtenerDatosAsistencia(String idInstitucion, String idPersona, String idFacturacion, String idioma) throws ClsExceptions {
		Vector result = new Vector();
		String sinActuaciones = UtilidadesString.getMensajeIdioma(idioma,"informes.colegiadosPagos.guardiaSinActuaciones"); //"Guardia sense actuacions";
		boolean porActuacion = false;
		
		try {
			String sql1 = "SELECT TO_CHAR(FA.FECHAINICIO, 'DD/MM/YYYY') AS FECHADESDE, "
							+ " TO_CHAR(FA.FECHAINICIO, 'DD/MM/YYYY') AS FECHAHASTA, "
							+ " TO_CHAR(FA.FECHAINICIO, 'DD/MM/YYYY') AS FECHAINICIO, "
							+ " TO_CHAR(CAB.FECHA_FIN, 'DD/MM/YYYY') AS FECHAFIN, "
							+ " TU.NOMBRE AS TURNO, "
							+ " TU.ABREVIATURA AS ABREVIATURA_TURNO, "
							+ " F_SIGA_FORMATONUMERO(FA.PRECIOAPLICADO, 2) AS IMPORTE_ACTUACION, "
							+ " GU.NOMBRE AS NOMBRE_GUARDIA, "
							+ " FA.IDFACTURACION, "
							+ " FA.IDTURNO, "
							+ " FA.IDGUARDIA, "
							+ " FA.IDCALENDARIOGUARDIAS, "
							+ " FA.IDAPUNTE "
						+ " FROM FCS_FACT_APUNTE FA, "
							+ " SCS_TURNO TU, "
							+ " SCS_GUARDIASTURNO GU, "
							+ " SCS_CABECERAGUARDIAS CAB "
						// Obtengo datos de FCS_FACT_APUNTE (FA)
						+ " WHERE FA.IDINSTITUCION = " + idInstitucion
							+ " AND FA.IDPERSONA = " + idPersona
							+ " AND FA.IDFACTURACION = " + idFacturacion
							// Relacion FCS_FACT_APUNTE (FA) con SCS_TURNO (TU)
							+ " AND TU.IDTURNO = FA.IDTURNO "
							+ " AND TU.IDINSTITUCION = FA.IDINSTITUCION "
							// Relacion FCS_FACT_APUNTE (FA) con SCS_CABECERAGUARDIAS (CAB)
							+ " AND CAB.IDTURNO = FA.IDTURNO "
							+ " AND CAB.IDINSTITUCION = FA.IDINSTITUCION "
							+ " AND CAB.IDGUARDIA = FA.IDGUARDIA "
							+ " AND CAB.IDCALENDARIOGUARDIAS = FA.IDCALENDARIOGUARDIAS "
							+ " AND CAB.IDPERSONA = FA.IDPERSONA "
							+ " AND CAB.FECHAINICIO = FA.FECHAINICIO "		
							// Relacion FCS_FACT_APUNTE (FA) con SCS_GUARDIASTURNO (GU)
							+ " AND GU.IDINSTITUCION = FA.IDINSTITUCION "
							+ " AND GU.IDTURNO = FA.IDTURNO "
							+ " AND GU.IDGUARDIA = FA.IDGUARDIA "
							// Eliminamos las asistencias de importe 0
							+ " AND FA.PRECIOAPLICADO > 0.0 " 
						+ " ORDER BY FA.FECHAINICIO, GU.NOMBRE, TU.NOMBRE, FA.IDAPUNTE";
	 
			RowsContainer rc = new RowsContainer();
			rc.find(sql1);
			if (rc!=null && rc.size()>0) {
				int size=rc.size();
				for(int i=0;i<size;i++){
					Row r1 = (Row)rc.get(i);
					Hashtable htAux = r1.getRow();
					
					String sImporteFacturado = r1.getString("IMPORTE_ACTUACION");
					htAux.put("IMPORTE_ACTUACION", sImporteFacturado + ClsConstants.CODIGO_EURO);
					
					String idTurno = r1.getString("IDTURNO");
					String idGuardia = r1.getString("IDGUARDIA");
					String idCalendarioGuardias = r1.getString("IDCALENDARIOGUARDIAS");
					String fechaDesde = r1.getString("FECHAINICIO");
					String idApunte = r1.getString("IDAPUNTE");
					
					// inc7405: Comprobamos si se paga por actuacion para saber si hay que mostrar las actuaciones o no
					String sqlAct = " SELECT 1 "
										+ " FROM SCS_HITOFACTURABLEGUARDIA "
										+ " WHERE IDINSTITUCION = " + idInstitucion
											+ " AND IDTURNO = " + idTurno
											+ " AND IDGUARDIA = " + idGuardia
											+ " AND IDHITO IN (4, 7, 22, 46)";
					
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
					if(rc2!=null && rc2.size()>0){
						//tratar el primero
						Row r2 = (Row)rc2.get(0);
						htAux.putAll(r2.getRow());
						
						String sImporteDesplazamineto = r2.getString("IMPORTE_DESPLAZAMIENTO");
						htAux.put("IMPORTE_DESPLAZAMIENTO", sImporteDesplazamineto + ClsConstants.CODIGO_EURO);
						
						String sPrecioAplicado = r1.getString("PRECIO_ACTUACION");
						if (sPrecioAplicado!=null && !sPrecioAplicado.equalsIgnoreCase("")) 
							htAux.put("PRECIO_ACTUACION", sPrecioAplicado + ClsConstants.CODIGO_EURO);
						
						result.addElement(htAux);
						
						//tratar el resto
						int size2=rc2.size();
						for(int j=1;j<size2;j++){
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
	protected Vector obtenerDatosOficio(String idInstitucion, String idPersona, String idFacturacion) throws ClsExceptions {
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
				" FROM SCS_ACTUACIONDESIGNA AD, " +
					" SCS_PROCEDIMIENTOS PRO, " +
					" SCS_DESIGNA DES, " +
					" FCS_FACT_ACTUACIONDESIGNA FACT, " +
					" FCS_FACTURACIONJG FAC, " +
					" SCS_ACREDITACIONPROCEDIMIENTO ACREPROD, " +
					" SCS_ACREDITACION ACRE, " +
					" SCS_TURNO TURNO, " +
					" CEN_COLEGIADO COLE, " +
					" SCS_JUZGADO JUZGADOS, " +
					" SCS_JUZGADO JUZGADOSAD " +
				// Obtengo datos de FCS_FACT_ACTUACIONDESIGNA (FACT)
				" WHERE FACT.IDFACTURACION = " + idFacturacion +
					" AND FACT.IDINSTITUCION = " + idInstitucion +
					" AND FACT.IDPERSONA = " + idPersona +
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
					// Relacion SCS_ACTUACIONDESIGNA (AD) con SCS_PROCEDIMIENTOS (PRO)	
					" AND PRO.IDINSTITUCION = AD.IDINSTITUCION " +
					" AND PRO.IDPROCEDIMIENTO = AD.IDPROCEDIMIENTO " +	
					// Relacion SCS_ACTUACIONDESIGNA (AD) con SCS_ACREDITACIONPROCEDIMIENTO (ACREPROD)
					" AND ACREPROD.IDPROCEDIMIENTO = AD.IDPROCEDIMIENTO " +
					" AND ACREPROD.IDINSTITUCION = AD.IDINSTITUCION_PROC " +
					" AND ACREPROD.IDACREDITACION = AD.IDACREDITACION " +
					// Relacion SCS_ACREDITACIONPROCEDIMIENTO (ACREPROD) con SCS_ACREDITACION (ACRE)
					" AND ACRE.IDACREDITACION = ACREPROD.IDACREDITACION " +
					// Relacion SCS_DESIGNA (DES) con SCS_JUZGADO (JUZGADOS)	
					" AND JUZGADOS.IDINSTITUCION(+) = DES.IDINSTITUCION " +   
					" AND JUZGADOS.IDJUZGADO(+) = DES.IDJUZGADO " +	
					// Relacion SCS_ACTUACIONDESIGNA (AD) con SCS_JUZGADO (JUZGADOSAD)
					" AND JUZGADOSAD.IDINSTITUCION(+) = AD.IDINSTITUCION " +   
					" AND JUZGADOSAD.IDJUZGADO(+) = AD.IDJUZGADO " +
					// Relacion SCS_ACTUACIONDESIGNA (AD) con CEN_COLEGIADO (COLE)		
					" AND COLE.IDINSTITUCION = AD.IDINSTITUCION " +
					" AND COLE.IDPERSONA = AD.IDPERSONACOLEGIADO " +
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
					result.addElement(hDatos);
				}
			}

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al generar el informe");
		}

		return result;
	}
}