
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
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.beans.FcsPagoColegiadoAdm;
import com.siga.beans.FcsPagosJGAdm;
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
				 "   and C.CEN_CLIENTE_IDINSTITUCION = "+idInstitucion;
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
		Vector result= new Vector();
		String sinActuaciones=//"Guardia sense actuacions";
			UtilidadesString.getMensajeIdioma(idioma,"informes.colegiadosPagos.guardiaSinActuaciones");
		int contador=0;
		Hashtable codigo =new Hashtable();
		double importeAsistenciaAux=0;
		boolean porActuacion = false;
		
		try {
			StringBuffer sql1 = new StringBuffer();
			sql1.append("select to_char(fa.fechainicio, 'DD/MM/YYYY') FECHAINICIO, ");
			sql1.append(" to_char(cab.fecha_fin, 'DD/MM/YYYY') FECHAFIN, ");
			sql1.append(" TU.NOMBRE TURNO, ");
			sql1.append(" tu.abreviatura ABREVIATURA_TURNO, ");
			sql1.append("fa.IDFACTURACION, ");
			sql1.append("fa.IDTURNO, ");
			sql1.append("fa.IDGUARDIA, ");
			sql1.append("fa.IDCALENDARIOGUARDIAS, ");
			sql1.append("f_siga_formatonumero(fa.precioaplicado, 2) IMPORTE_ACTUACION, ");
			sql1.append("fa.IDAPUNTE, ");
			sql1.append("gu.nombre as NOMBRE_GUARDIA ");
			sql1.append("from fcs_fact_apunte fa, SCS_TURNO TU, scs_guardiasturno gu, SCS_CABECERAGUARDIAS cab  ");

			sql1.append(" where FA.IDTURNO = TU.IDTURNO ");
			sql1.append(" and cab.idturno = fa.idturno ");
			sql1.append(" and cab.idinstitucion = fa.idinstitucion ");
			sql1.append(" and cab.idguardia = fa.idguardia ");
			sql1.append(" and cab.idcalendarioguardias = fa.idcalendarioguardias ");
			sql1.append(" and cab.idpersona = fa.idpersona ");
			sql1.append(" and cab.fechainicio = fa.fechainicio ");
			sql1.append("   and FA.IDINSTITUCION = TU.IDINSTITUCION ");
			sql1.append("   and gu.idinstitucion = tu.idinstitucion");
			sql1.append("   and gu.idturno = tu.idturno");
			sql1.append("   and gu.idguardia = fa.idguardia");
			contador++;
			codigo.put(new Integer(contador),idInstitucion);
			sql1.append(" AND FA.IDINSTITUCION = :"+contador);
			contador++;
			codigo.put(new Integer(contador),idPersona);
			sql1.append("   and FA.IDPERSONA = :" +contador);
			contador++;
			codigo.put(new Integer(contador),idFacturacion);	
			sql1.append("   and FA.IDFACTURACION = :"+contador);
			sql1.append("   and fa.precioaplicado >0.0 "); // Eliminamos las asistencias de importe 0
			sql1.append(" order by fa.fechainicio, GU.NOMBRE, TU.NOMBRE, FA.IDAPUNTE");
	 
			RowsContainer rc=new RowsContainer();
			rc.findBind(sql1.toString(),codigo);
			if(rc!=null && rc.size()>0){
				int size=rc.size();
				for(int i=0;i<size;i++){
					Row r1=(Row)rc.get(i);
					Hashtable htAux=r1.getRow();
					String sImporteFacturado=r1.getString("IMPORTE_ACTUACION");
					htAux.put("IMPORTE_ACTUACION",sImporteFacturado+ClsConstants.CODIGO_EURO);
					
					String idTurno=r1.getString("IDTURNO");
					String idGuardia=r1.getString("IDGUARDIA");
					String idCalendarioGuardias=r1.getString("IDCALENDARIOGUARDIAS");
					String fechaDesde=r1.getString("FECHAINICIO");
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
						sql2= "SELECT f_siga_getrecurso(TA.descripcion, " + idioma + " ) TIPOACTUACION, " +
							" AAS.ANIO || '/' || AAS.NUMERO || '-' || AAS.IDACTUACION ACTUACION, " + 
							" AAS.ANIO || '/' || AAS.NUMERO ASISTENCIA, " +
							" PJG.NOMBRE||' '||PJG.APELLIDO1||' '||PJG.APELLIDO2 NOMBRE_ASISTIDO, " + 
							" to_char(AAS.FECHA,'DD/MM/YYYY') FECHA_ACTUACION, " +
							" DECODE(FAAS.PRECIOAPLICADO,0,NULL,f_siga_formatonumero(FAAS.PRECIOAPLICADO,2)) AS PRECIO_ACTUACION, " +
							" f_siga_getrecurso(COS.DESCRIPCION, " + idioma + " ) AS TIPO_DESPLAZAMIENTO, " +
							" f_siga_formatonumero(TACTCOS.IMPORTE, 2) AS IMPORTE_DESPLAZAMIENTO, " +
							" (case when instr(f_siga_getrecurso(COS.DESCRIPCION, 1),' 5 km') > 0 then '5 km' " +
							"   when instr(f_siga_getrecurso(COS.DESCRIPCION, 1),' 25 km') > 0 then '25 km' " +
							"   when instr(f_siga_getrecurso(COS.DESCRIPCION, 1),' 50 km') > 0 then '50 km' " +
							"   else f_siga_getrecurso(COS.DESCRIPCION, " + idioma + " ) " +
							"   end ) ABREVIATURA_DESPLAZAMIENTO, " +
							" NVL(JUZGADOS.NOMBRE, JUZGADOS_ASI.NOMBRE) AS JUZGADO " +
						" FROM FCS_FACT_APUNTE FAP, FCS_FACT_ACTUACIONASISTENCIA FAAS, " +
							" SCS_ACTUACIONASISTENCIA AAS, " +
							" SCS_ASISTENCIA ASI, " +
							" SCS_PERSONAJG PJG, " +
							" SCS_ACTUACIONASISTCOSTEFIJO ACTCOS, " +
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
							" and FAP.IDTURNO = "  +idTurno +
							" and FAP.IDGUARDIA = "  +idGuardia +
							" and FAP.IDCALENDARIOGUARDIAS = " + idCalendarioGuardias +
							" and trunc(FAP.FECHAINICIO) = '" + fechaDesde  +"' "+
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
							" and TACTCOS.IDCOSTEFIJO(+) = ACTCOS.IDCOSTEFIJO  " +          
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
						" ORDER BY AAS.ANIO, AAS.NUMERO, AAS.IDACTUACION, NOMBRE_ASISTIDO";
						
					}else{
							// inc7405 // Si se factura por asistencia solo escribimos la primera actuacion de la asistencia
							// asi que filtramos para que salga solo la primera actuacion
						sql2 = "SELECT '' TIPOACTUACION, " +
							" '' ACTUACION, " +
							" ASI.ANIO || '/' || ASI.NUMERO ASISTENCIA, " +
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
							" and trunc(FAP.FECHAINICIO) = '" + fechaDesde + "' " +
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
						" GROUP BY ASI.ANIO, ASI.NUMERO, " +
							" PJG.NOMBRE, " +
							" PJG.APELLIDO1, " + 
							" PJG.APELLIDO2, " +
							" ASI.FECHAHORA, " +
							" FASI.PRECIOAPLICADO, " +
							" JUZGADOS.NOMBRE " +
						" ORDER BY ASI.ANIO, ASI.NUMERO, PJG.NOMBRE || ' ' || PJG.APELLIDO1 || ' ' || PJG.APELLIDO2" ;
					}

					RowsContainer rc2 = new RowsContainer();
					rc2.find(sql2);
					if(rc2!=null && rc2.size()>0){
						//tratar el primero
						Row r2=(Row)rc2.get(0);
						htAux.putAll(r2.getRow());
						String sImporteDesplazamineto=r2.getString("IMPORTE_DESPLAZAMIENTO");
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
	protected Vector obtenerDatosOficio(String idInstitucion, String idPersona, String idFacturacion) throws ClsExceptions
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
				" FROM SCS_ACTUACIONDESIGNA AD, " +
					" SCS_PROCEDIMIENTOS PRO, " +
					" SCS_DESIGNA DES, " +
					" FCS_FACT_ACTUACIONDESIGNA fact, " +
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
					" and AD.IDINSTITUCION = PRO.IDINSTITUCION " +
					" and AD.IDPROCEDIMIENTO = PRO.IDPROCEDIMIENTO " +
					" and ad.idinstitucion = fact.idinstitucion " +
					" and ad.idpersonacolegiado = fact.idpersona " +
					" and ad.NUMEROASUNTO = fact.NUMEROASUNTO " +
					" and ad.NUMERO = fact.NUMERO " +
					" and ad.ANIO = fact.ANIO " +
					" and ad.IDTURNO = fact.IDTURNO " +
					" and fac.idinstitucion = fact.idinstitucion " +
					" and fac.idfacturacion = fact.idfacturacion " +
					" and fact.idfacturacion = " + idFacturacion +
					" and acreprod.idprocedimiento = ad.idprocedimiento " +
					" and acreprod.idinstitucion = ad.idinstitucion_proc " +
					" and acreprod.idacreditacion = ad.idacreditacion " +
					" and acre.idacreditacion = acreprod.idacreditacion " +
					// Relacioamos la tabla SCS_ACTUACIONDESIGNA AD, CEN_COLEGIADO cole
					" and AD.idinstitucion = cole.idinstitucion " +
					" and AD.idpersonacolegiado = cole.idpersona " +
					" and AD.idinstitucion = " + idInstitucion +
					" and AD.idpersonacolegiado = " + idPersona +
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
					ht.put("IMPORTE_PROCEDIMIENTO", ((String) ht.get("IMPORTE_PROCEDIMIENTO")) + ClsConstants.CODIGO_EURO);
					ht.put("IMPORTE_OFICIO", ((String) ht.get("IMPORTE_OFICIO")) + ClsConstants.CODIGO_EURO);
					result.add(g, ht);
				}
			}

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al generar el informe");
		}

		return result;
	}

}