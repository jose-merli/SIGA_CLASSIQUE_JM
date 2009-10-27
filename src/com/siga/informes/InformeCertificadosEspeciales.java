package com.siga.informes;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;

import com.siga.Utilidades.UtilidadesHash;


import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CerPlantillasAdm;
import com.siga.certificados.Certificado;
import com.siga.general.SIGAException;

/**
 * Realiza un informe PDF mediante FOP de las facturas emitidas según plantilla introducida
 */

public class InformeCertificadosEspeciales extends MasterReport 
{
	private String idioma = "";
	
	
	public InformeCertificadosEspeciales(UsrBean usuario) {
		super(usuario);
		idioma = usuario.getLanguage();
	}

	protected String reemplazarDatos(HttpServletRequest request, String plantillaFO, Hashtable datosBase) throws ClsExceptions, SIGAException 
	{
		String institucion = this.getUsuario().getLocation();
		Hashtable hDatosFijos = new Hashtable ();
		
		
		// Colocamos los registros
		Vector vDatos =getDatos(datosBase);
		CenClienteAdm admcli=new CenClienteAdm(this.getUsuario());
		
		/********NUEVAS ETIQUETAS COMUNES ***********************/
		hDatosFijos=admcli.getEtiquetasComunesCertificados(datosBase,institucion);
		
		Vector vDatosFormato=new Vector();
		String vIteracion="";
		String tipoCert=UtilidadesHash.getString(datosBase,"TIPOCERTIFICADO");
		String[] datosPasantia;
		String letradoPasantia="";
		String dniPasantia="";
		if (vDatos != null){
			CerPlantillasAdm admPlantilla = new CerPlantillasAdm(this.getUsuario());
			
			for (int i=0; i<vDatos.size();i++){
			 Hashtable haux=(Hashtable) vDatos.get(i);	
			 String vDescripcionPasantia=UtilidadesHash.getString(haux,"DESCRIPCIONPASANTIA");
			 if (vDescripcionPasantia!=null && !vDescripcionPasantia.equals("")){
			 	
			 	datosPasantia =vDescripcionPasantia.split("&");
			 	if (datosPasantia.length>1){
			 	if (datosPasantia[0]!=null && datosPasantia[0].length()>0){	
			 	 letradoPasantia=datosPasantia[0];
			 	}
 			 	if (datosPasantia[1]!=null && datosPasantia[1].length()>0){
			 	  dniPasantia=datosPasantia[1];
			 	}
			 	UtilidadesHash.set(haux,"LETRADOPASANTIAS",letradoPasantia);
			 	UtilidadesHash.set(haux,"DNIPASANTIAS",dniPasantia);
			 	}
			 	
			 }
			 Hashtable htDatos = admPlantilla.darFormatoCampos(String.valueOf(institucion), UtilidadesHash.getString(haux,"IDTIPOPRO"),  UtilidadesHash.getString(haux,"IDPRO"),  UtilidadesHash.getString(haux,"IDPROINSTI"), UtilidadesHash.getString(datosBase,"IDPLANTILLA"),idioma, haux);
			 
		      if (tipoCert!=null && (tipoCert.equals(Certificado.CERT_TIPO_TURNO)||tipoCert.equals(Certificado.CERT_TIPO_PASANTIA)||tipoCert.equals(Certificado.CERT_TIPO_DESPACHO1)||tipoCert.equals(Certificado.CERT_TIPO_DESPACHO2))) {
			   UtilidadesHash.set(htDatos,"NEXO_AL",UtilidadesHash.getString(haux,"NEXO_AL"));
		      } 
			 
		      if (htDatos.get("FECHAFINPASANTIAS")==null || htDatos.get("FECHAFINPASANTIAS").equals("")){
		      	String vMensaje=UtilidadesString.getMensajeIdioma(this.getUsuario().getLanguage(),"messages.certificado.texto.fechaFin");
			       UtilidadesHash.set(htDatos,"FECHAFINPASANTIAS",vMensaje);
			  }
		     vDatosFormato.add(htDatos);
			}
			if (tipoCert!=null && tipoCert.equals(Certificado.CERT_TIPO_CURSO)) {
				vIteracion = "SEMINARIOS_CURSOS";
	        }
			else if (tipoCert!=null && tipoCert.equals(Certificado.CERT_TIPO_TURNO)) {
	        	vIteracion="TURNOS";	
	        }
			else if (tipoCert!=null && tipoCert.equals(Certificado.CERT_TIPO_PASANTIA)) {
	        	vIteracion = "PASANTIAS";
	        }
			else if (tipoCert!=null && (tipoCert.equals(Certificado.CERT_TIPO_DESPACHO1)||tipoCert.equals(Certificado.CERT_TIPO_DESPACHO2))) {
	        	vIteracion = "HISTORICO_DESPACHOS";
	        }
			else if (tipoCert!=null && (tipoCert.equals(Certificado.CERT_TIPO_BANCO))) {
	        	vIteracion = "BANCOS";
	        }			
			else if (tipoCert!=null && (tipoCert.equals(Certificado.CERT_TIPO_DIRECCION))) {
	        	vIteracion = "HISTORICO_DIRECCIONES";
	        }

			plantillaFO = this.reemplazaRegistros(plantillaFO, vDatosFormato, null,vIteracion);
		}	

		// Colocamos datos fijos (cabeceras, etc)
		CenInstitucionAdm admInstitucion= new CenInstitucionAdm (this.getUsuario());
		CenPersonaBean beanPer = null;
		Hashtable registro=new Hashtable();
		if (vDatosFormato!=null && vDatosFormato.size()>0) {
			registro = (Hashtable) vDatosFormato.get(0);
			
			
		}
		String nombre = UtilidadesHash.getString(registro,"NOMBRE");
		
		String apellido1 =UtilidadesHash.getString(registro,"APELLIDO1");
		String apellido2 = UtilidadesHash.getString(registro,"APELLIDO2");
		String SITUACIONRESIDENTE = UtilidadesHash.getString(registro,"RESIDENTE");
		String nif = UtilidadesHash.getString(registro,"CIFNIF");
		String fechaEmisionCert = UtilidadesHash.getString(registro,"FECHAEMISIONCERTIFICADO");
		String fechaIncorporacionCert = UtilidadesHash.getString(registro,"FECHAINCORPORACION");
		String valor1 = UtilidadesHash.getString(registro,"VALOR1");
		String valor2 = UtilidadesHash.getString(registro,"VALOR2");
		String valor3 = UtilidadesHash.getString(registro,"VALOR3");
		String valor4 = UtilidadesHash.getString(registro,"VALOR4");
		String valor5 = UtilidadesHash.getString(registro,"VALOR5");
		String valor6 = UtilidadesHash.getString(registro,"VALOR6");
		
		
		
		
		UtilidadesHash.set(hDatosFijos, "CIFNIF", nif);
		UtilidadesHash.set(hDatosFijos, "NOMBRE", nombre);
		UtilidadesHash.set(hDatosFijos, "APELLIDO1", apellido1);
		UtilidadesHash.set(hDatosFijos, "APELLIDO2", apellido2);
		UtilidadesHash.set(hDatosFijos, "FECHAEMISIONCERTIFICADO", fechaEmisionCert);
		UtilidadesHash.set(hDatosFijos, "RESIDENTE", SITUACIONRESIDENTE);
		UtilidadesHash.set(hDatosFijos, "FECHAINCORPORACION", fechaIncorporacionCert);
		
		UtilidadesHash.set(hDatosFijos, "VALOR1", valor1);
		UtilidadesHash.set(hDatosFijos, "VALOR2", valor2);
		UtilidadesHash.set(hDatosFijos, "VALOR3", valor3);
		UtilidadesHash.set(hDatosFijos, "VALOR4", valor4);
		UtilidadesHash.set(hDatosFijos, "VALOR5", valor5);
		UtilidadesHash.set(hDatosFijos, "VALOR6", valor6);
		
		UtilidadesHash.set(hDatosFijos, "FECHAACTUAL", UtilidadesHash.getString(registro,"FECHAACTUAL"));
		UtilidadesHash.set(hDatosFijos, "TRATAMIENTO", UtilidadesHash.getString(registro,"TRATAMIENTO"));
		UtilidadesHash.set(hDatosFijos, "INSTITUCIONORIGEN", UtilidadesHash.getString(registro,"INSTITUCIONORIGEN"));
		UtilidadesHash.set(hDatosFijos, "INSTITUCIONDESTINO", UtilidadesHash.getString(registro,"INSTITUCIONDESTINO"));
		UtilidadesHash.set(hDatosFijos, "FECHAPRESENTACION", UtilidadesHash.getString(registro,"FECHAPRESENTACION"));
		UtilidadesHash.set(hDatosFijos, "NCOLEGIADO", UtilidadesHash.getString(registro,"NCOLEGIADO"));
		UtilidadesHash.set(hDatosFijos, "TIPOIDENTIFICACION", UtilidadesHash.getString(registro,"TIPOIDENTIFICACION"));
		UtilidadesHash.set(hDatosFijos, "NATURALDE", UtilidadesHash.getString(registro,"NATURALDE"));
		UtilidadesHash.set(hDatosFijos, "ESTADO_COLEGIAL", UtilidadesHash.getString(registro,"ESTADO_COLEGIAL"));
		
		UtilidadesHash.set(hDatosFijos, "DIRECCION_DESPACHO", UtilidadesHash.getString(registro,"DIRECCION_DESPACHO"));
		UtilidadesHash.set(hDatosFijos, "DOMICILIO_DESPACHO", UtilidadesHash.getString(registro,"DOMICILIO_DESPACHO"));
		UtilidadesHash.set(hDatosFijos, "CODIGOPOSTAL_DESPACHO", UtilidadesHash.getString(registro,"CODIGOPOSTAL_DESPACHO"));
		UtilidadesHash.set(hDatosFijos, "POBLACION_DESPACHO", UtilidadesHash.getString(registro,"POBLACION_DESPACHO"));
		UtilidadesHash.set(hDatosFijos, "PROVINCIA_DESPACHO", UtilidadesHash.getString(registro,"PROVINCIA_DESPACHO"));
		UtilidadesHash.set(hDatosFijos, "PAIS_DESPACHO", UtilidadesHash.getString(registro,"PAIS_DESPACHO"));
		UtilidadesHash.set(hDatosFijos, "TELEFONO_DESPACHO", UtilidadesHash.getString(registro,"TELEFONO_DESPACHO"));
		
		
		

		plantillaFO = this.reemplazaVariables(hDatosFijos, plantillaFO);
		return plantillaFO;
	}

	/**
	 * Metodo que implementa la generación de la factura en PDF
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @param  idFactura - 
	 * @return  File - fichero PDF generado  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public File generarListadoCertificados (HttpServletRequest request, Hashtable datos, String pdfRuta, String pdfNombre, String plantillaRuta, String plantillaNombre, String dirTemporal ) throws ClsExceptions,SIGAException 
	{
		String resultado="exito";
		File fPdf = null;
			
		try {
			String contenidoPlantilla = this.obtenerContenidoPlantilla(plantillaRuta,plantillaNombre);
			fPdf = this.generarInforme(request, datos, dirTemporal, contenidoPlantilla, pdfRuta, pdfNombre.substring(0,pdfNombre.indexOf(".pdf")));
		}
		catch (SIGAException se) {
			throw se;
		}
		catch (ClsExceptions ex) {
			throw ex;
		}
		catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe: "+e.getLocalizedMessage());
		} 
		finally {

		}
        return fPdf;
	}
	
	private Vector getDatos(Hashtable ht)
	{
		Vector v=null; 
	    try
	    {   
	        Hashtable htDatos = new Hashtable();
	        String idInstitucion=UtilidadesHash.getString(ht,"@idinstitucion@"); 
	        String idPersona=UtilidadesHash.getString(ht,"@idPersona@");
	        String idSolicitud=UtilidadesHash.getString(ht,"@idsolicitud@");
	        String idIdioma=UtilidadesHash.getString(ht,"@idioma@");
	        
	       /* ReadProperties rp = new ReadProperties("SIGA.properties");
	        
	        String sSQL = rp.returnProperty("certificados.consultacliente");*/
	        String sSQL ="";
	       
	        String tipoCert=UtilidadesHash.getString(ht,"TIPOCERTIFICADO");
	        
	        if (tipoCert!=null && tipoCert.equals(Certificado.CERT_TIPO_CURSO)) {
	        	sSQL = getSqlCurso(tipoCert);
	        }
	        else if (tipoCert!=null && tipoCert.equals(Certificado.CERT_TIPO_TURNO)) {
	        	sSQL=getSqlTurno(tipoCert);	
	        }
	        else if (tipoCert!=null && tipoCert.equals(Certificado.CERT_TIPO_PASANTIA)) {
	        	sSQL = getSqlPasantia(tipoCert);
	        }
	        else if (tipoCert!=null && (tipoCert.equals(Certificado.CERT_TIPO_DESPACHO1)||tipoCert.equals(Certificado.CERT_TIPO_DESPACHO2))) {
	        	sSQL = getSqlDespacho(tipoCert);
	        }
	        else if (tipoCert!=null && tipoCert.equals(Certificado.CERT_TIPO_BANCO)) {
	        	sSQL = getSqlBanco(tipoCert);
	        }
	        else if (tipoCert!=null && tipoCert.equals(Certificado.CERT_TIPO_DIRECCION)) {
	        	sSQL = getSqlDirecciones(tipoCert);
	        }
	        
	        Enumeration enumHash = ht.keys();
	        
	        while (enumHash.hasMoreElements())
	        {
	            String sClave = (String)enumHash.nextElement();
	            String sValor = (String)ht.get(sClave);
	            
	            //sSQL = sSQL.replaceFirst(sClave, sValor);
	            sSQL = sSQL.replaceAll("(?i)"+sClave, sValor);
	        }
	        
	        sSQL = sSQL.replaceAll("(?i)" + "@IDIOMA@", idioma);
//	        sSQL = sSQL.replaceAll("@idioma@", this.usrbean.getLanguage());
	        
	        RowsContainer rc =new RowsContainer();
	        rc.find(sSQL);

	        if (rc==null || rc.size()==0) 
	        {
	            throw new SIGAException("messages.certificados.error.noexistendatos", null, null);
	        }
            
	        if (rc!=null) {
 				v = new Vector();
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow(); 
					if (registro != null) 
						v.add(registro);
				}
			}
	    }catch(Exception e){}	
	    return v;
	}
	
	
	public String getSqlTurno(String tipoCert){
		String sSQL =getSqlCamposGeneral(tipoCert)+",  (select scs_turno.nombre "+
									        " from scs_turno "+
									        " where scs_turno.idinstitucion=it.idinstitucion "+
									        " and scs_turno.idturno=it.idturno) as nombre_turno, "+
									        " it.fechavalidacion as fechaalta_turno, "+
									        " it.fechabaja as fechabaja_turno, "+
									        " DECODE(it.fechabaja,NULL,'',F_SIGA_GETRECURSO_ETIQUETA('messages.certificado.texto.nexoAL',@idioma@)) as NEXO_AL "+
	                 getSqlFromGeneral()+",  scs_inscripcionturno      it "+
					 getSqlWhereGeneral()+" and it.idinstitucion(+)=c.idinstitucion "+
					   " and it.idpersona(+)=c.idpersona  "+
					   " and it.fechavalidacion(+) is not null "+
					   " order by it.fechavalidacion asc";
		
		return sSQL;
	}
	
	public String getSqlCurso(String tipoCert){
		String sSQL = getSqlCamposGeneral (tipoCert)+" , f_siga_getrecurso(cv.descripcion, @idioma@) as NOMBRECURSO, "+
                      " cv.fechainicio as FECHAINICIOCURSO,"+ "cv.fechafin as FECHAFINCURSO" +
					  getSqlFromGeneral()+" , cen_datoscv  cv,"+
                                     " cen_tiposcv  tipocv"+
                      getSqlWhereGeneral()+" and cv.idpersona(+)=c.idpersona"+
                      " and cv.idinstitucion(+)=c.idinstitucion"+
                      " and cv.idtipocv=tipocv.idtipocv(+)"+
                      " and tipocv.idtipocv=1    "+
                      " order by cv.fechainicio asc";
		return sSQL;

	}
	public String getSqlPasantia(String tipoCert){
		String sSQL =getSqlCamposGeneral(tipoCert)+", f_siga_getrecurso(cv.descripcion, @idioma@) as DESCRIPCIONPASANTIA, "+
                      " cv.fechainicio as FECHAINICIOPASANTIAS, "+
                      " cv.fechafin as FECHAFINPASANTIAS, "+
                      " DECODE(cv.fechafin,NULL,'',F_SIGA_GETRECURSO_ETIQUETA('messages.certificado.texto.nexoAL',@idioma@)) as NEXO_AL "+
                      getSqlFromGeneral()+", cen_datoscv               cv,"+
                                     " cen_tiposcv               tipocv"+
                      getSqlWhereGeneral()+ " and cv.idinstitucion(+)=c.idinstitucion"+
					  " and cv.idpersona(+)=c.idpersona "+
                      " and cv.idtipocv=tipocv.idtipocv(+) "+
                      " and tipocv.idtipocv=3    "+
                      " order by cv.fechainicio asc";
		return sSQL;

	}
	
	public String getSqlDespacho(String tipoCert){
		String sSQL = getSqlCamposGeneral(tipoCert)+", ( d.domicilio||' '||d.codigopostal||' '||"+
	                  "   decode(d.idpais,191,((select po.nombre"+
	                  "                         from cen_poblaciones po"+
	                  "                         where po.idpoblacion=d.idpoblacion)"+
	                  "          ),d.poblacionextranjera)) as DIRECCION_DESPACHO,"+
	                  " d.domicilio   AS DOMICILIO_DESPACHO,"+
	                  " d.codigopostal AS CODIGOPOSTAL_DESPACHO,"+
	                  " decode(d.idpais,191,((select po.nombre"+
	                  "                       from cen_poblaciones po"+
	                  "                       where po.idpoblacion=d.idpoblacion)"+
	                  "                      ),d.poblacionextranjera)  AS POBLACION_DESPACHO,"+                           
	                  " (select f_siga_getrecurso(c.nombre,@idioma@)"+
	                  "  from cen_provincias c"+
	                  "   where c.idprovincia=d.idprovincia ) AS PROVINCIA_DESPACHO,"+
	                  " (select f_siga_getrecurso(c.nombre,@idioma@)"+
	                  " from cen_pais c"+
	                  " where c.idpais=d.idpais  ) AS PAIS_DESPACHO,"+
	                  " s.comentario AS OBSERVACIONES,"+
	                  " d.telefono1 AS TELEFONO_DESPACHO,"+
	                  " decode ((select 1 "+
	                  "          from dual "+
	                  "          where nvl(d.fechacarga,D.FECHAMODIFICACION)<D.FECHAMODIFICACION),1,d.fechacarga,D.FECHAMODIFICACION) AS FECHAALTA_DESPACHO,"+
				      " D.FECHABAJA AS FECHABAJA_DESPACHO,"+
	                  " DECODE(D.FECHABAJA,NULL,'',F_SIGA_GETRECURSO_ETIQUETA('messages.certificado.texto.nexoAL',1)) as NEXO_AL"+ 
	                  getSqlFromGeneral()+" , cen_direcciones           d,"+
				      " cen_direccion_tipodireccion tipodir"+
					  getSqlWhereGeneral()+" and tipodir.idinstitucion(+)=d.idinstitucion"+
					  "  and tipodir.idpersona(+)=d.idpersona"+
					  "  and tipodir.iddireccion(+)=d.iddireccion"+    
					  "  and d.idpersona(+)=c.idpersona"+
					  "  and d.idinstitucion(+)=c.idinstitucion"+
					  "  and tipodir.idtipodireccion=2"+
					  "  ORDER BY FECHAALTA_DESPACHO desc ";
		return sSQL;

	}
	
	
	public String getSqlBanco(String tipoCert){
		String sSQL =getSqlCamposGeneral(tipoCert)+
		" ,decode(cb.ABONOCARGO,'T',F_SIGA_GETRECURSO_ETIQUETA('censo.tipoCuenta.abonoCargo',1),'A',F_SIGA_GETRECURSO_ETIQUETA('censo.tipoCuenta.abono',1),'C',F_SIGA_GETRECURSO_ETIQUETA('censo.tipoCuenta.cargo',1)) as ABONOCARGO_BANCO, " +
		" cb.CBO_CODIGO || '-' || " +
		" cb.CODIGOSUCURSAL || '-' || " +
		" cb.DIGITOCONTROL || '-' || " +
		" cb.NUMEROCUENTA as CUENTA_BANCO, " +
		" cb.TITULAR as TITULAR_BANCO, " +
		" cb.FECHABAJA as FECHABAJA_BANCO, " +
		" cb.CUENTACONTABLE as CUENTACONTABLE_BANCO, " +
		" (select nombre from cen_bancos where codigo = cb.CBO_CODIGO) as NOMBRE_BANCO, " +
		" decode(cb.ABONOSJCS,'0',F_SIGA_GETRECURSO_ETIQUETA('general.no',1),F_SIGA_GETRECURSO_ETIQUETA('general.yes',1)) as SJCS_BANCO, " +
		" decode(cb.IDPERSONA,'2040000024',F_SIGA_GETRECURSO_ETIQUETA('general.no',1),F_SIGA_GETRECURSO_ETIQUETA('general.yes',1)) as SOCIEDAD_BANCO  " +       
		getSqlFromGeneral()+",  cen_cuentasbancarias cb "+
		getSqlWhereGeneral()+
		" and cb.idinstitucion(+)=c.idinstitucion "+
		" and cb.idpersona(+)=c.idpersona  "+
		" and (cb.IDPERSONA = c.idpersona  "+
		" AND cb.IDINSTITUCION = c.idinstitucion "+
		"  OR (cb.IDINSTITUCION,cb.IDPERSONA,cb.IDCUENTA) in "+ 
		" 		(SELECT CEN_CUENTASBANCARIAS.IDINSTITUCION,CEN_CUENTASBANCARIAS.IDPERSONA,CEN_CUENTASBANCARIAS.IDCUENTA "+
		" 	       FROM CEN_COMPONENTES "+
		" 	       JOIN CEN_CUENTASBANCARIAS ON CEN_COMPONENTES.IDINSTITUCION = CEN_CUENTASBANCARIAS.IDINSTITUCION "+
		" 	                                AND CEN_COMPONENTES.IDPERSONA = CEN_CUENTASBANCARIAS.IDPERSONA "+
		" 	      WHERE CEN_COMPONENTES.CEN_CLIENTE_IDPERSONA = c.idpersona  "+
		" 	        AND CEN_COMPONENTES.CEN_CLIENTE_IDINSTITUCION = c.idinstitucion) )"+
		" ORDER BY CB.TITULAR ";
		
		return sSQL;
	}

	
	public String getSqlDirecciones(String tipoCert){
		String sSQL = getSqlCamposGeneral(tipoCert)+", "+
	                  " f_siga_gettiposdireccion(d.IDINSTITUCION,d.IDPERSONA,d.IDDIRECCION, 1) AS TIPO_DIRECCION, " +
	                  " d.domicilio   AS DOMICILIO_DIRECCION,"+
	                  " d.codigopostal AS CODIGOPOSTAL_DIRECCION,"+
	                  " decode(d.idpais,191,((select po.nombre"+
	                  "                       from cen_poblaciones po"+
	                  "                       where po.idpoblacion=d.idpoblacion)"+
	                  "                      ),d.poblacionextranjera)  AS POBLACION_DIRECCION,"+                           
	                  " (select f_siga_getrecurso(c.nombre,@idioma@)"+
	                  "  from cen_provincias c"+
	                  "   where c.idprovincia=d.idprovincia ) AS PROVINCIA_DIRECCION,"+
	                  " (select f_siga_getrecurso(c.nombre,@idioma@)"+
	                  " from cen_pais c"+
	                  " where c.idpais=d.idpais  ) AS PAIS_DIRECCION,"+
	                  " s.comentario AS OBSERVACIONES,"+
	                  " d.telefono1 AS TELEFONO1_DIRECCION,"+
	                  " d.fax1 AS FAX1_DIRECCION,"+
	                  " d.movil AS MOVIL_DIRECCION,"+
	                  " d.correoelectronico AS EMAIL_DIRECCION,"+
	                  " d.preferente AS PREFERENTE_DIRECCION,"+
	                  " decode ((select 1 "+
	                  "          from dual "+
	                  "          where nvl(d.fechacarga,D.FECHAMODIFICACION)<D.FECHAMODIFICACION),1,d.fechacarga,D.FECHAMODIFICACION) AS FECHAALTA_DIRECCION,"+
				      " D.FECHABAJA AS FECHABAJA_DIRECCION,"+
	                  " DECODE(D.FECHABAJA,NULL,'',F_SIGA_GETRECURSO_ETIQUETA('messages.certificado.texto.nexoAL',1)) as NEXO_AL"+ 
	                  getSqlFromGeneral()+" , cen_direcciones           d "+
					  getSqlWhereGeneral()+"  and d.idpersona(+)=c.idpersona"+
					  "  and d.idinstitucion(+)=c.idinstitucion"+
					  "  ORDER BY FECHAALTA_DIRECCION desc ";
		return sSQL;

	}

	
	public String getSqlCamposGeneral(String tipoCert){
		String sql="";
		 sql=" SELECT SYSDATE AS FECHAACTUAL, "+
			    " f_siga_getrecurso(t.DESCRIPCION, @idioma@) as TRATAMIENTO, "+
				  " p.nifcif AS CIFNIF, "+
				  " p.nombre AS NOMBRE,"+
				  "	p.apellidos1 AS APELLIDO1,"+
				  "	p.apellidos2 AS APELLIDO2,"+
				  "	s.FECHAEMISIONCERTIFICADO,"+
				  " f_siga_getrecurso_etiqueta(decode(l.SITUACIONRESIDENTE,1,'censo.consultaDatosColegiales.literal.residente','censo.consultaDatosColegiales.literal.noResidente'),@IDIOMA@) as RESIDENTE,"+
				  " (SELECT nombre"+
				  " FROM CEN_INSTITUCION"+
			    " WHERE IDINSTITUCION = s.idinstitucionorigen) as INSTITUCIONORIGEN,"+
			    " (SELECT nombre"+
			    " FROM CEN_INSTITUCION"+
			    " WHERE IDINSTITUCION = s.idinstituciondestino) as INSTITUCIONDESTINO,"+
				  "	S.PREFIJO_CER,"+
				  " S.CONTADOR_CER,"+
				  " S.SUFIJO_CER,"+
			    " s.ppn_idtipoproducto as idtipopro, "+
			    " s.ppn_idproducto as idpro, "+
			    " s.ppn_idproductoinstitucion as idproinsti, "+
				  " l.fechapresentacion as FECHAPRESENTACION,"+
				  " l.fechaincorporacion as FECHAINCORPORACION,"+
				  " decode(l.comunitario, 1, l.ncomunitario, l.ncolegiado) AS NCOLEGIADO,"+
				  " (SELECT f_siga_getrecurso(DESCRIPCION, @idioma@) as DESCRIPCION"+
			    "  FROM CEN_TIPOIDENTIFICACION"+
			    "  WHERE IDTIPOIDENTIFICACION = p.Idtipoidentificacion) AS TIPOIDENTIFICACION,"+
			    " P.NATURALDE AS NATURALDE, ";
			if (!tipoCert.equals(Certificado.CERT_TIPO_DESPACHO1)&&!tipoCert.equals(Certificado.CERT_TIPO_DESPACHO2)){	
			  sql+=  "  F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " +
                " 					@idpersona@, " +
                "                   @idioma@, " +
                "                   null) AS DIRECCION_DESPACHO, " +
		       "F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " +
		       "                            @idpersona@, " +
		       "                            @idioma@, " +
		       "                            1) AS DOMICILIO_DESPACHO, " +
		       "F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " +
		       "                            @idpersona@, " +
		       "                            @idioma@, " +
		       "                            2) AS CODIGOPOSTAL_DESPACHO, " +
		       "F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " +
		       "                            @idpersona@, " +
		       "                            @idioma@, " +
		       "                            3) AS POBLACION_DESPACHO, " +
		       "F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " +
		       "                            @idpersona@, " +
		       "                            @idioma@, " +
		       "                            4) AS PROVINCIA_DESPACHO, " +
		       "F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " +
		       "                            @idpersona@, " +
		       "                            @idioma@, " +
		       "                            5) AS PAIS_DESPACHO, " +
		       "s.comentario AS OBSERVACIONES, " +
		       "F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " +
		       "                            @idpersona@, " +
		       "                            @idioma@, " +
		       "                            6) AS TELEFONO_DESPACHO, " ;
			} 
			sql+=	" (select f_siga_getrecurso(es.descripcion, @idioma@) "+
				" from cen_estadocolegial es  " +
				" where es.idestado=f_siga_gettipocliente(@idpersona@,@idinstitucion@,sysdate)) AS ESTADO_COLEGIAL";
			return sql;
	}
	
	public String getSqlFromGeneral(){
		return " FROM cen_cliente               c,"+
		        " cen_persona               p,"+
			      " cen_tratamiento           t,"+
			      " cer_solicitudcertificados s,"+
			      " cen_colegiado             l ";
				  
	}
	
	public String getSqlWhereGeneral(){
		return " WHERE c.idpersona = @idPersona@"+
		        " AND c.idinstitucion = @idinstitucion@"+
		        " AND s.idsolicitud = @idsolicitud@"+
				  "  AND c.idpersona = s.idpersona_des"+
				  "  AND c.idpersona = p.idpersona"+
				  "  AND c.idtratamiento = t.idtratamiento"+
				  "  AND l.idpersona(+) = c.idpersona"+
				  "  AND l.idinstitucion(+) = c.idinstitucion"+
				  "  AND (c.idinstitucion = s.idinstitucionorigen or"+
				  "      c.idinstitucion = s.idinstitucion)";
                  
	}
	
}