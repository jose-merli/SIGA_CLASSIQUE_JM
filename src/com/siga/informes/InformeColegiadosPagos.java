
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
		UtilidadesHash.set(htDatos,"FECHA",UtilidadesBDAdm.getFechaEscritaBD((String)htDatos.get("idiomaExt")));
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
		
		//Datos del Pago y Totales
		htAux=this.obtenerDatosPago(institucion, idPersona, idPagos, usr);
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
	protected Hashtable obtenerDatosPersonaSociedad(String idInstitucion, String idPersona, UsrBean user, String idPagos) throws ClsExceptions {
		Hashtable result= new Hashtable();
		RowsContainer rc=null;
		
		String idSociedad=null;
		//UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		try {
			 //buscamos el nombre de la persona
			String sql=
				 "select "+UtilidadesMultidioma.getCampoMultidiomaSimple("T.DESCRIPCION",user.getLanguage())+"||' '||P.NOMBRE||' '||P.APELLIDOS1||' '||P.APELLIDOS2 NOMBRE_PERSONA" +
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
				
			String sql1 = "select * from fac_abono where idpersona="+idSociedad +"and idinstitucion="+idInstitucion+" and idpagosjg ="+idPagos;
			RowsContainer rc1=new RowsContainer();
			rc1.find(sql1);
			String idCuenta="";
			if(rc1!=null && rc1.size()>0){
				int size=rc1.size();
				for(int i=0;i<size;i++){
					Row r1=(Row)rc1.get(i);
					Hashtable htAux=r1.getRow();
					 idCuenta=r1.getString("IDCUENTA");
				}
			}	
			
			if (!(idCuenta.equals(""))){
				// Datos Bancarios de la sociedad o persona
		    sql=
		    	"SELECT DECODE(CUEN.NUMEROCUENTA,NULL,'',CUEN.CBO_CODIGO||' '||CUEN.CODIGOSUCURSAL||' '||CUEN.DIGITOCONTROL||' '||CUEN.NUMEROCUENTA||' '|| Decode(Substr(Ban.Nombre, 1, 1), '~', '', Ban.Nombre)) CUENTA_CORRIENTE" +
		    	"  FROM CEN_CUENTASBANCARIAS CUEN, CEN_BANCOS BAN" +
		    	" WHERE BAN.CODIGO = CUEN.CBO_CODIGO " +
		    	"   AND CUEN.FECHABAJA IS NULL " +
		    	"   AND CUEN.Idcuenta = "+idCuenta +
		    	"   AND CUEN.IDINSTITUCION = "+idInstitucion+
		    	"   AND CUEN.IDPERSONA = " +idSociedad;
			rc = new RowsContainer();
			rc.find(sql);
			if(rc!=null && rc.size()>0){
				Row r=(Row)rc.get(0);
				result.putAll(r.getRow());
			}
			}else result.put("CUENTA_CORRIENTE","");
			
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
					
					// inc7405 // Comprobamos si se para por actuacion para saber si hay que mostrar las actuaciones o no
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
						sql2=
						"select f_siga_getrecurso(TA.descripcion, "+ idioma +" ) TIPOACTUACION, AAS.ANIO || '/' || AAS.NUMERO || '-' || AAS.IDACTUACION ACTUACION, AAS.ANIO || '/' || AAS.NUMERO ASISTENCIA," +
						"       PJG.NOMBRE||' '||PJG.APELLIDO1||' '||PJG.APELLIDO2 NOMBRE_ASISTIDO, to_char(AAS.FECHA,'DD/MM/YYYY') FECHA_ACTUACION, " +
						"       DECODE(FAAS.PRECIOAPLICADO,0,NULL,FAAS.PRECIOAPLICADO) AS PRECIO_ACTUACION," +
						"		f_siga_getrecurso(COS.DESCRIPCION, "+ idioma +" ) AS TIPO_DESPLAZAMIENTO," +
						"		TACTCOS.IMPORTE AS IMPORTE_DESPLAZAMIENTO, " +
						"		(case when instr(f_siga_getrecurso(COS.DESCRIPCION, 1),' 5 km') > 0 then '5 km' " +
						"		when instr(f_siga_getrecurso(COS.DESCRIPCION, 1),' 25 km') > 0 then '25 km' " +
						"		when instr(f_siga_getrecurso(COS.DESCRIPCION, 1),' 50 km') > 0 then '50 km' " +
						"		else f_siga_getrecurso(COS.DESCRIPCION, "+ idioma+" ) " +
						"       end ) ABREVIATURA_DESPLAZAMIENTO " +
						"  from FCS_FACT_APUNTE FAP, FCS_FACT_ACTUACIONASISTENCIA FAAS," +
						"		SCS_ACTUACIONASISTENCIA AAS," +
						"		SCS_ASISTENCIA ASI," +
						"		SCS_PERSONAJG PJG," +
						"		SCS_ACTUACIONASISTCOSTEFIJO  ACTCOS," +
						"		SCS_TIPOACTUACIONCOSTEFIJO   TACTCOS," +
						"		SCS_COSTEFIJO                COS," +
						"       SCS_TIPOACTUACION            TA "+
						" where FAP.IDINSTITUCION = FAAS.IDINSTITUCION " +
						"   and FAP.IDFACTURACION = FAAS.IDFACTURACION " +
						"   and FAP.IDAPUNTE = FAAS.IDAPUNTE " +
						"   and FAP.IDPERSONA = FAAS.IDPERSONA " +
						"   and FAP.IDINSTITUCION = "+idInstitucion +
						"   and FAP.IDFACTURACION = "+idFacturacion +
						"   and FAP.IDTURNO ="+idTurno +
						"   and FAP.IDGUARDIA = "+idGuardia +
						"   and FAP.IDCALENDARIOGUARDIAS = "+idCalendarioGuardias +
						"   and trunc(FAP.FECHAINICIO)= '"+fechaDesde+"' "+
						"   and FAP.Idpersona = "+idPersona+" "+
						"   and FAP.IdApunte = "+idApunte+" "+
						"   and FAAS.IDINSTITUCION = AAS.IDINSTITUCION (+)" +
						"   and FAAS.NUMERO = AAS.NUMERO (+)" +
						"   and FAAS.ANIO = AAS.ANIO (+)" +
						"   and FAAS.IDACTUACION=AAS.IDACTUACION (+)" +
						"   and FAAS.IDINSTITUCION = ASI.IDINSTITUCION (+)" +
						"   and FAAS.NUMERO = ASI.NUMERO (+)" +
						"   and FAAS.ANIO = ASI.ANIO (+)" +
						"   and ASI.IDPERSONAJG = PJG.IDPERSONA(+)" +
						"   and ASI.IDINSTITUCION = PJG.IDINSTITUCION(+)" +
						// Cruzamos con tablas nuevas para sacar los desplazamientos 
						" 	and ACTCOS.IDINSTITUCION(+) = AAS.IDINSTITUCION " +
						"	and ACTCOS.ANIO(+) = AAS.ANIO" +
						" 	and ACTCOS.NUMERO(+) = AAS.NUMERO" +
						" 	and ACTCOS.IDACTUACION(+) = AAS.IDACTUACION" +
						" 	and TACTCOS.IDINSTITUCION(+) = ACTCOS.IDINSTITUCION " +       
						" 	and TACTCOS.IDTIPOASISTENCIA(+) = ACTCOS.IDTIPOASISTENCIA " + 
						" 	and TACTCOS.IDTIPOACTUACION(+) = ACTCOS.IDTIPOACTUACION " +   
						" 	and TACTCOS.IDCOSTEFIJO(+) = ACTCOS.IDCOSTEFIJO  " +          
						" 	and COS.IDINSTITUCION(+)= ACTCOS.IDINSTITUCION " +            
						" 	and COS.IDCOSTEFIJO(+)=ACTCOS.IDCOSTEFIJO " +      
						 //Relacionamos la tabla  SCS_TIPORACTUACION  TA, SCS_ACTUACIONASISTENCIA AAS
						"   and TA.idinstitucion = AAS.idinstitucion " +
						"   and TA.idtipoasistencia= AAS.idtipoasistencia "+
						"   and TA.idtipoactuacion = AAS.idtipoactuacion " +	
						" order by AAS.ANIO, AAS.NUMERO, AAS.IDACTUACION, NOMBRE_ASISTIDO";
						}else{
							// inc7405 // Si se factura por asistencia solo escribimos la primera actuacion de la asistencia
							// asi que filtramos para que salga solo la primera actuacion
							sql2=
							" select '' TIPOACTUACION, " +
							"       '' ACTUACION, " +
							"       ASI.ANIO || '/' || ASI.NUMERO ASISTENCIA, " +
							"       PJG.NOMBRE || ' ' || PJG.APELLIDO1 || ' ' || PJG.APELLIDO2 NOMBRE_ASISTIDO, " +
							"       to_char(ASI.FECHAHORA, 'DD/MM/YYYY') FECHA_ACTUACION, " +
							"       DECODE(FASI.PRECIOAPLICADO, 0, NULL, FASI.PRECIOAPLICADO) AS PRECIO_ACTUACION, " +
							"       '' AS TIPO_DESPLAZAMIENTO, " +
							"       SUM(TACTCOS.IMPORTE) AS IMPORTE_DESPLAZAMIENTO, " +
							"       '' AS ABREVIATURA_DESPLAZAMIENTO " +
							"  from FCS_FACT_ASISTENCIA FASI, " +
							"       SCS_ASISTENCIA               ASI, " +
							"       FCS_FACT_APUNTE              FAP, " +

							"       SCS_PERSONAJG                PJG, " +
							"       FCS_FACT_ACTUACIONASISTENCIA FAAS, " +
							"       SCS_ACTUACIONASISTENCIA      AAS, " +
							"       SCS_ACTUACIONASISTCOSTEFIJO  ACTCOS, " +
							"       SCS_TIPOACTUACIONCOSTEFIJO   TACTCOS, " +
							"       SCS_COSTEFIJO                COS, " +
							"       SCS_TIPOACTUACION            TA " +

							" where FASI.IDINSTITUCION = ASI.IDINSTITUCION " +
							"   and FASI.ANIO = ASI.ANIO " +
							"   and FASI.NUMERO = ASI.NUMERO " +
							"   and FASI.IDINSTITUCION = FAP.IDINSTITUCION " +
							"   and FASI.IDFACTURACION = FAP.IDFACTURACION " +
							"   and FASI.IDAPUNTE = FAP.IDAPUNTE " +

							"   and FASI.IDINSTITUCION = "+idInstitucion +
							"   and FAP.IDFACTURACION = "+idFacturacion +
							"   and FAP.IDTURNO ="+idTurno +
							"   and FAP.IDGUARDIA = "+idGuardia +
							"   and FAP.IDCALENDARIOGUARDIAS = "+idCalendarioGuardias +
							"   and trunc(FAP.FECHAINICIO)= '"+fechaDesde+"' "+
							"   and FAP.Idpersona = "+idPersona+" "+
							"   and FAP.IdApunte = "+idApunte+" "+
							" " +
							"   and ASI.IDPERSONAJG = PJG.IDPERSONA(+) " +
							"   and ASI.IDINSTITUCION = PJG.IDINSTITUCION(+) " + 
							"   and FASI.IDINSTITUCION = FAAS.IDINSTITUCION(+) " +
							"   and FASI.IDFACTURACION = FAAS.IDFACTURACION(+) " +
							"   and FASI.ANIO = FAAS.ANIO(+) " +
							"   and FASI.NUMERO = FAAS.NUMERO(+) " +
							"   and FAAS.IDINSTITUCION = AAS.IDINSTITUCION(+) " +
							"   and FAAS.NUMERO = AAS.NUMERO(+) " +
							"   and FAAS.ANIO = AAS.ANIO(+) " +
							"   and FAAS.IDACTUACION = AAS.IDACTUACION(+) " +
							"   and AAS.IDINSTITUCION = ACTCOS.IDINSTITUCION(+) " +
							"   and AAS.ANIO = ACTCOS.ANIO(+) " +
							"   and AAS.NUMERO = ACTCOS.NUMERO(+) " +
							"   and AAS.IDACTUACION = ACTCOS.IDACTUACION(+) " +
							"   and ACTCOS.IDINSTITUCION = TACTCOS.IDINSTITUCION(+) " +
							"   and ACTCOS.IDTIPOASISTENCIA = TACTCOS.IDTIPOASISTENCIA(+) " +
							"   and ACTCOS.IDTIPOACTUACION = TACTCOS.IDTIPOACTUACION(+) " +
							"   and ACTCOS.IDCOSTEFIJO = TACTCOS.IDCOSTEFIJO(+) " +
							"   and ACTCOS.IDINSTITUCION = COS.IDINSTITUCION(+) " +
							"   and ACTCOS.IDCOSTEFIJO = COS.IDCOSTEFIJO(+) " +
							"   and AAS.idinstitucion = TA.idinstitucion(+) " +
							"   and AAS.idtipoasistencia = TA.idtipoasistencia(+) " +
							"   and AAS.idtipoactuacion = TA.idtipoactuacion(+) " +
							" group by ASI.ANIO, ASI.NUMERO, " +
							"       PJG.NOMBRE || ' ' || PJG.APELLIDO1 || ' ' || PJG.APELLIDO2, " +
							"       to_char(ASI.FECHAHORA, 'DD/MM/YYYY'), " +
							"       DECODE(FASI.PRECIOAPLICADO, 0, NULL, FASI.PRECIOAPLICADO) " +
							" order by ASI.ANIO, ASI.NUMERO, PJG.NOMBRE || ' ' || PJG.APELLIDO1 || ' ' || PJG.APELLIDO2 " ;
						}

					RowsContainer rc2 = new RowsContainer();
					rc2.find(sql2);
					if(rc2!=null && rc2.size()>0){
						//tratar el primero
						Row r2=(Row)rc2.get(0);
						htAux.putAll(r2.getRow());
						result.addElement(htAux);
						//tratar el resto
						int size2=rc2.size();
						for(int j=1;j<size2;j++){
							r2=(Row)rc2.get(j);
							htAux=r2.getRow();
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
	protected Vector obtenerDatosOficio(String idInstitucion, String idPersona, String idPagos) throws ClsExceptions {
		Vector result= null;
		int contador=0;
		Hashtable codigo=new Hashtable();
		
		try {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" Select  ");
			sql.append(" f_siga_getdatoejgreldesigna(Des.Idinstitucion,Des.idturno,Des.anio,Des.numero,1) as NUMEROEJG,  ");
			sql.append(" f_siga_getdatoejgreldesigna(Des.Idinstitucion,Des.idturno,Des.anio,Des.numero,4) as ANIOEJG,  ");
			sql.append(" f_siga_getdatoejgreldesigna(Des.Idinstitucion,Des.idturno,Des.anio,Des.numero,3) as NUMERO_CAJG,  ");
			sql.append(" f_siga_getdatoejgreldesigna(Des.Idinstitucion,Des.idturno,Des.anio,Des.numero,2) as ANIO_CAJG,  ");
			sql.append("        decode(cole.comunitario,'1',cole.ncomunitario,cole.ncolegiado) as NUMERO_COLEGIADO, AD.FECHA, to_char(AD.FECHA,'DD/MM/YYYY') FECHA_OFICIO,  PRO.NOMBRE PROCEDIMIENTO, ");
			sql.append("        f_siga_formatonumero(COL.IMPOFICIO,2)  IMPORTEPAGADO, ");
			sql.append("        DES.ANIO || '/' || DES.CODIGO  ASIOFI, ");
			sql.append("        f_siga_getdefendidosdesigna(DES.IDINSTITUCION, des.anio, des.idturno, des.numero,0) NOMBRE_SOLICITANTE, ");
			sql.append("        f_siga_formatonumero(fact.precioaplicado,2) IMPORTE_PROCEDIMIENTO, ");
			sql.append("        f_siga_formatonumero(fact.precioaplicado*fact.porcentajefacturado/100,2) IMPORTE_OFICIO, ");
			sql.append("        acreprod.porcentaje as PORCENTAJE_PAGADO,");
			sql.append("        acre.descripcion as ACREDITACION, ");
			sql.append("   turno.nombre AS NOMBRE_TURNO, ");
			sql.append("   turno.abreviatura AS ABREVIATURA_TURNO, ");
			sql.append("        ad.numeroasunto as NUMEROASUNTO ");
			sql.append("   from FCS_PAGO_COLEGIADO   COL, ");
			sql.append("        SCS_ACTUACIONDESIGNA      AD, ");
			sql.append("        SCS_PROCEDIMIENTOS        PRO, ");
			sql.append("        SCS_DESIGNA               DES, ");
			sql.append("        FCS_FACT_ACTUACIONDESIGNA fact, ");
			sql.append("        FCS_PAGOSJG               pag, ");
			sql.append("        FCS_FACTURACIONJG         fac, ");      
	       	sql.append("        SCS_ACREDITACIONPROCEDIMIENTO acreprod, ");
	       	sql.append("        SCS_ACREDITACION acre, ");
	       	sql.append("        SCS_TURNO turno, ");	       	
	       	sql.append("        CEN_COLEGIADO cole ");	      
			sql.append("  where DES.IDINSTITUCION = AD.IDINSTITUCION ");
			sql.append("    AND DES.IDTURNO = AD.IDTURNO ");
			sql.append("    AND DES.ANIO = AD.ANIO ");
			sql.append("    AND DES.NUMERO = AD.NUMERO ");
			sql.append("    AND DES.IDTURNO = turno.idturno ");
			sql.append("    AND DES.Idinstitucion = turno.Idinstitucion ");			
			sql.append("    AND COL.IDINSTITUCION = AD.IDINSTITUCION ");
			sql.append("    and AD.IDINSTITUCION = PRO.IDINSTITUCION ");
			sql.append("    and AD.IDPROCEDIMIENTO = PRO.IDPROCEDIMIENTO ");
			   
			sql.append("    and COL.IDINSTITUCION = "+idInstitucion);
			sql.append("    and COL.IDPERORIGEN = "+idPersona);
			sql.append("    and COL.IDPAGOSJG = "+idPagos);
			  
			sql.append("    and col.idinstitucion = pag.idinstitucion ");
			sql.append("    and col.idpagosjg = pag.idpagosjg ");
			sql.append("    and COL.idinstitucion = fact.idinstitucion ");
			sql.append("    and COL.idperorigen = fact.idpersona ");
			  
			sql.append("    and ad.idinstitucion = fact.idinstitucion ");
			sql.append("    and ad.idpersonacolegiado = fact.idpersona ");
			sql.append("    and ad.NUMEROASUNTO = fact.NUMEROASUNTO ");
			sql.append("    and ad.NUMERO = fact.NUMERO ");
			sql.append("    and ad.ANIO = fact.ANIO ");
			sql.append("    and ad.IDTURNO = fact.IDTURNO ");
			
			sql.append("    and pag.idinstitucion = fact.idinstitucion ");
			sql.append("    and pag.idfacturacion = fact.idfacturacion ");
			  
			sql.append("    and fac.idinstitucion = fact.idinstitucion ");
			sql.append("    and fac.idfacturacion = fact.idfacturacion ");
			
			// Relacionamos las nuevas tablas para sacar la forma de pago
			sql.append(" and acreprod.idprocedimiento = ad.idprocedimiento ");
			sql.append(" and acreprod.idinstitucion = ad.idinstitucion_proc ");
			sql.append(" and acreprod.idacreditacion = ad.idacreditacion ");
			sql.append(" and acre.idacreditacion = acreprod.idacreditacion ");
			
			//Relacioamos la tabla SCS_ACTUACIONDESIGNA AD, CEN_COLEGIADO cole 
			sql.append(" and  AD.idinstitucion= cole.idinstitucion ");	
			sql.append(" and  AD.idpersonacolegiado= cole.idpersona ");			
	          
			sql.append(" order by AD.FECHA, ASIOFI, NUMEROASUNTO, PROCEDIMIENTO");
			
			RowsContainer rc=new RowsContainer();
			rc.find(sql.toString());
			result=new Vector();
			if(rc!=null && rc.size()>0){
				Vector result3=rc.getAll();
				Vector aux=new Vector();
				for (int g=0;result3!=null && g<result3.size();g++){
				    aux.add(g,(Row) result3.get(g));
				}
				for (int g=0;aux!=null && g<aux.size();g++){
				    Hashtable ht = ((Row) aux.get(g)).getRow();
				    ht.put("IMPORTEPAGADO", ((String)ht.get("IMPORTEPAGADO"))+ClsConstants.CODIGO_EURO);
				    ht.put("IMPORTE_OFICIO", ((String)ht.get("IMPORTE_OFICIO"))+ClsConstants.CODIGO_EURO);
				    result.add(g,ht);
				}
			}

		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
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
	protected Hashtable obtenerDatosPago(String idInstitucion, String idPersona, String idPago, UsrBean usr) throws ClsExceptions {
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
			buf0.append("SELECT   abo.numeroabono numeroabono, SUM (caj.importe) compensado_factura,");
			buf0.append(" SUM (efe.importe) pagado_caja");
			buf0.append(" FROM fac_pagoabonoefectivo efe RIGHT JOIN fac_abono abo");
			buf0.append(" ON efe.idinstitucion = abo.idinstitucion");
			buf0.append(" AND efe.idabono = abo.idabono");
			buf0.append(" LEFT JOIN fac_pagosporcaja caj");
			buf0.append(" ON efe.idinstitucion = caj.idinstitucion");
			buf0.append(" AND efe.idabono = caj.idabono");
			buf0.append(" AND efe.idpagoabono = caj.idpagoabono");
			buf0.append(" WHERE abo.idpersona = " + idPersona);
			buf0.append(" and abo.idpagosjg = " +idPago);
			buf0.append(" and abo.idinstitucion = " + idInstitucion);
			buf0.append(" group by abo.numeroabono, abo.idpagosjg");		
			
	/*		buf0.append("select abo.numeroabono as NUMEROABONO, sum(efe.importe) as COMPENSADO_CAJA");
			buf0.append(" from FAC_ABONO ABO, FAC_PAGOABONOEFECTIVO EFE");
			buf0.append(" where abo.idinstitucion = efe.idinstitucion(+)");
			buf0.append(" and abo.idabono = efe.idabono(+)");
			buf0.append(" and abo.idpersona = " + idPersona);
			buf0.append(" and abo.idpagosjg = " +idPago);
			buf0.append(" and abo.idinstitucion = " + idInstitucion);
			buf0.append(" group by abo.numeroabono");*/
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
				result.put("FECHA_PAGO",UtilidadesString.getFechaEscrita(fechaPago,"dd/MM/yyyy",usr.getLanguage()));
			}
		
			//fecha de abono del disco bancario
			
			buf0 = new StringBuffer();
			buf0.append("SELECT TO_CHAR(fd.fecha, 'DD/MM/YYYY') FECHAABONODISCOBANCO FROM FAC_ABONO fa, FCS_PAGOSJG fp,fac_abonoincluidoendisquete fi, fac_disqueteabonos fd ");
			buf0.append(" where fa.idinstitucion =" + idInstitucion);
			buf0.append(" and fa.idpagosjg= " + idPago);
			buf0.append(" and fa.idpersona="+ idPersona);
			buf0.append(" AND fa.idinstitucion = fa.idinstitucion ");
			buf0.append(" AND fa.idpagosjg = fp.idpagosjg ");
			buf0.append(" and fi.idinstitucion= fa.idinstitucion ");
			buf0.append(" and fi.idabono = fa.idabono ");
			buf0.append(" and fi.idinstitucion= fd.idinstitucion ");
			buf0.append(" and fi.iddisqueteabono= fd.iddisqueteabono ");			
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
	
	
}