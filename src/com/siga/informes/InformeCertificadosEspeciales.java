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
import com.siga.beans.CerSolicitudCertificadosAdm;
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


	public InformeCertificadosEspeciales() {

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
		      if (tipoCert.equals(Certificado.CERT_TIPO_PASANTIA)){
		    	  UtilidadesHash.set(htDatos,"DESCRIPCIONPASANTIA",UtilidadesHash.getString(haux,"DESCRIPCIONPASANTIA"));
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
				else if (tipoCert!=null && (tipoCert.equals(Certificado.CERT_TIPO_COMPONENTES))) {
	        	vIteracion = "COMPONENTES";
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
		String o_a = UtilidadesHash.getString(registro,"O_A");
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
		UtilidadesHash.set(hDatosFijos, "O_A", o_a);
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
		UtilidadesHash.set(hDatosFijos, "FECHAALTACLIENTE", UtilidadesHash.getString(registro,"FECHAALTACLIENTE"));
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
		UtilidadesHash.set(hDatosFijos, "MOVIL_DESPACHO", UtilidadesHash.getString(registro,"MOVIL_DESPACHO"));
		UtilidadesHash.set(hDatosFijos, "FAX_DESPACHO", UtilidadesHash.getString(registro,"FAX_DESPACHO"));
		UtilidadesHash.set(hDatosFijos, "NUMEROREGISTRO", UtilidadesHash.getString(registro,"NUMEROREGISTRO"));
		UtilidadesHash.set(hDatosFijos, "TIPO", UtilidadesHash.getString(registro,"TIPO"));
		UtilidadesHash.set(hDatosFijos, "FECHACONSTITUCION", UtilidadesHash.getString(registro,"FECHACONSTITUCION"));
		UtilidadesHash.set(hDatosFijos, "FECHAFIN_SOCIEDAD", UtilidadesHash.getString(registro,"FECHAFIN_SOCIEDAD"));
		UtilidadesHash.set(hDatosFijos, "RESENA_SOCIEDAD", UtilidadesHash.getString(registro,"RESENA_SOCIEDAD"));
		UtilidadesHash.set(hDatosFijos, "OBJETOSOCIAL", UtilidadesHash.getString(registro,"OBJETOSOCIAL"));
		UtilidadesHash.set(hDatosFijos, "NOMBRENOTARIO", UtilidadesHash.getString(registro,"NOMBRENOTARIO"));
		UtilidadesHash.set(hDatosFijos, "APELLIDOS1_NOTARIO", UtilidadesHash.getString(registro,"APELLIDOS1_NOTARIO"));
		UtilidadesHash.set(hDatosFijos, "APELLIDOS2_NOTARIO", UtilidadesHash.getString(registro,"APELLIDOS2_NOTARIO"));
		UtilidadesHash.set(hDatosFijos, "NIFCIF_NOTARIO", UtilidadesHash.getString(registro,"NIFCIF_NOTARIO"));
		UtilidadesHash.set(hDatosFijos, "TIDENTIFICACION_NOTARIO", UtilidadesHash.getString(registro,"TIDENTIFICACION_NOTARIO"));		
		
		
		

		plantillaFO = this.reemplazaVariables(hDatosFijos, plantillaFO);
		//aalg: inc_10344_siga. Quitar la cadena BRdummyBR que se añadió para quitar los \n de texto
		plantillaFO = UtilidadesString.replaceAllIgnoreCase(plantillaFO, "BRdummyBR", "<fo:block  space-before=\"0.2cm\" />");
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
		Vector v = null;
		try {
			String sSQL = "";

			String tipoCert = UtilidadesHash.getString(ht, "TIPOCERTIFICADO");

			if (tipoCert != null && tipoCert.equals(Certificado.CERT_TIPO_CURSO)) {
				sSQL = getSqlCurso(tipoCert);
			} else if (tipoCert != null && tipoCert.equals(Certificado.CERT_TIPO_TURNO)) {
				sSQL = getSqlTurno(tipoCert);
			} else if (tipoCert != null && tipoCert.equals(Certificado.CERT_TIPO_PASANTIA)) {
				sSQL = getSqlPasantia(tipoCert);
			} else if (tipoCert != null	&& (tipoCert.equals(Certificado.CERT_TIPO_DESPACHO1) || 
											tipoCert.equals(Certificado.CERT_TIPO_DESPACHO2))) {
				sSQL = getSqlDespacho(tipoCert);
			} else if (tipoCert != null && tipoCert.equals(Certificado.CERT_TIPO_BANCO)) {
				sSQL = getSqlBanco(tipoCert);
			} else if (tipoCert != null && tipoCert.equals(Certificado.CERT_TIPO_DIRECCION)) {
				sSQL = getSqlDirecciones(tipoCert);
			} else if (tipoCert != null && tipoCert.equals(Certificado.CERT_TIPO_COMPONENTES)) {
				sSQL = getSqlComponentes(tipoCert);
			}

			Enumeration enumHash = ht.keys();

			while (enumHash.hasMoreElements()) {
				String sClave = (String) enumHash.nextElement();
				String sValor = (String) ht.get(sClave);

				sSQL = sSQL.replaceAll("(?i)" + sClave, sValor);
			}

			sSQL = sSQL.replaceAll("(?i)" + "@IDIOMA@", idioma);

			RowsContainer rc = new RowsContainer();
			rc.find(sSQL);

			if (rc == null || rc.size() == 0) {
				throw new SIGAException("messages.certificados.error.noexistendatos", null, null);
			}

			if (rc != null) {
				v = new Vector();
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow();
					if (registro != null)
						v.add(registro);
				}
			}
		} catch (Exception e) {
		}
		return v;
	}	
	
	public String getSqlTurno(String tipoCert){
		String sSQL =getSqlCamposGeneral(tipoCert,"")+",  (select scs_turno.nombre "+
									        " from scs_turno "+
									        " where scs_turno.idinstitucion=it.idinstitucion "+
									        " and scs_turno.idturno=it.idturno) as nombre_turno, "+
									        " it.fechavalidacion as fechaalta_turno, "+
									        " it.fechabaja as fechabaja_turno, "+
									        " DECODE(it.fechabaja,NULL,'',F_SIGA_GETRECURSO_ETIQUETA('messages.certificado.texto.nexoAL',@idioma@)) as NEXO_AL "+
	                 getSqlFromGeneral()+",  scs_inscripcionturno      it "+
					 getSqlWhereGeneral()+" and it.idinstitucion(+)=c.idinstitucion "+
					   " and it.idpersona(+)=c.idpersona  "+
					   " and it.fechavalidacion(+) <=SYSDATE " +
					   
					   " order by it.fechavalidacion asc";
		
		return sSQL;
	}
	
	public String getSqlCurso(String tipoCert){
		String sSQL = getSqlCamposGeneral (tipoCert,"")+" , f_siga_getrecurso(cv.descripcion, @idioma@) as NOMBRECURSO, "+
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
		String sSQL =getSqlCamposGeneral(tipoCert,"")+", f_siga_getrecurso(cv.descripcion, @idioma@) as DESCRIPCIONPASANTIA, "+
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
		String sSQL = getSqlCamposGeneral(tipoCert,"")+", ( d.domicilio||' '||d.codigopostal||' '||"+
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
	                  " d.movil AS MOVIL_DESPACHO,"+
	                  " d.fax1 AS FAX_DESPACHO,"+
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
		String sSQL =getSqlCamposGeneral(tipoCert,"")+
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
		" decode(cb.IDPERSONA,@idpersona@,F_SIGA_GETRECURSO_ETIQUETA('general.no',1),F_SIGA_GETRECURSO_ETIQUETA('general.yes',1)) as SOCIEDAD_BANCO  " +       
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
		"           AND CEN_COMPONENTES.FECHABAJA IS NULL "+
		" 	        AND CEN_COMPONENTES.CEN_CLIENTE_IDINSTITUCION = c.idinstitucion) )"+
		" ORDER BY CB.TITULAR ";
		
		return sSQL;
	}

	
	public String getSqlComponentes(String tipoCert){
		String sSQL =getSqlCamposGeneral(tipoCert,"")+
		",  cp.NUMCOLEGIADO as NUMCOLEG_COMPONENTE, " +      
		" 	N.NIFCIF as NIFCIF_COMPONENTE, "+
        "   N.NOMBRE AS NOMBRE_COMPONENTE, "+
        "   N.APELLIDOS1 AS APELLIDO1_COMPONENTE, "+
        "   N.APELLIDOS2 AS APELLIDO2_COMPONENTE "+  
        getSqlFromGeneral()+",  CEN_COMPONENTES cp,CEN_PERSONA N  "+
        getSqlWhereGeneral()+     
        " and cp.CEN_CLIENTE_IDPERSONA = N.IDPERSONA  "+
        " and cp.IDPERSONA =@idpersona@ "+
		" and cp.IDINSTITUCION = @idinstitucion@ "+
		" and (cp.FECHABAJA IS NULL OR cp.FECHABAJA > SYSDATE)"+
		" AND no.idinstitucion=cp.IDINSTITUCION "+
        " AND no.idpersona=cp.IDPERSONA "+
		" ORDER BY p.NIFCIF ";
		return sSQL;
		
	}
	
	public String getSqlDirecciones(String tipoCert){
		String sSQL = getSqlCamposGeneral(tipoCert,"")+", "+
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

	public String getSqlCamposGeneral(Integer idInstitucion)throws SIGAException{
		CerSolicitudCertificadosAdm cerSolAdmin = new CerSolicitudCertificadosAdm(this.getUsuario());
		String contador = "";
		try
		{
			contador =(String)cerSolAdmin.getContador(idInstitucion.toString());
		}catch(ClsExceptions e)
		{
			throw new SIGAException("Error al recoger el contador de SSPP.",e);
		}
		return getSqlCamposGeneral(null,contador) + getSqlFromGeneral() + getSqlWhereGeneral();
	}
	public String getSqlCamposGeneral(String tipoCert,String contador){
		StringBuffer sql= new StringBuffer();
		sql.append(" SELECT SYSDATE AS FECHAACTUAL, ");
		sql.append(" c.fechaalta AS FECHAALTACLIENTE, ");
		sql.append(" p.fechanacimiento FECHACONSTITUCION, ");  
		sql.append(" no.OBJETOSOCIAL as OBJETOSOCIAL, ");
		sql.append(" no.RESENA as RESENA_SOCIEDAD, ");
		sql.append(" no.fechafin as FECHAFIN_SOCIEDAD, ");		
		sql.append(" nvl(no.prefijo_numsspp,no.prefijo_numreg)|| nvl(LPAD(NO.contador_numsspp,");
		sql.append("'"+contador+"'");
		sql.append(",'0'),LPAD(NO.contador_numreg,");
		sql.append("'"+contador+"'");
		sql.append(",'0')) || nvl(no.SUFIJO_NUMSSPP,no.SUFIJO_NUMREG) as NUMEROREGISTRO, ");
		sql.append(" (select pe.nombre from cen_persona pe where pe.idpersona= no.idpersonanotario) AS NOMBRENOTARIO, ");
		sql.append(" (select pe.apellidos1 from cen_persona pe where pe.idpersona= no.idpersonanotario) AS APELLIDOS1_NOTARIO, ");
		sql.append(" (select pe.apellidos2 from cen_persona pe where pe.idpersona= no.idpersonanotario) AS APELLIDOS2_NOTARIO, ");
		sql.append(" (select pe.nifcif from cen_persona pe where pe.idpersona= no.idpersonanotario) AS NIFCIF_NOTARIO, ");
		sql.append(" (select f_siga_getrecurso(CT.DESCRIPCION, @idioma@) from cen_persona pe, CEN_TIPOIDENTIFICACION CT where pe.idpersona = no.idpersonanotario and CT.IDTIPOIDENTIFICACION = pe.Idtipoidentificacion ) AS TIDENTIFICACION_NOTARIO, ");		
		sql.append(" f_siga_getrecurso(t.DESCRIPCION, @idioma@) as TRATAMIENTO, ");
		sql.append(" p.nifcif AS CIFNIF, ");
		sql.append(" p.nombre AS NOMBRE,");
		sql.append("	p.apellidos1 AS APELLIDO1,");
		sql.append("	p.apellidos2 AS APELLIDO2,");
		sql.append("	decode(p.sexo,'H','o','a') as O_A,");
		sql.append("	decode(p.sexo,'H','o','a') as O_A2,");		
		sql.append("	s.FECHAEMISIONCERTIFICADO,");
		sql.append(" f_siga_getrecurso_etiqueta(decode(l.SITUACIONRESIDENTE,1,'censo.consultaDatosColegiales.literal.residente','censo.consultaDatosColegiales.literal.noResidente'),@IDIOMA@) as RESIDENTE,");
		sql.append(" (SELECT nombre");
		sql.append(" FROM CEN_INSTITUCION");
		sql.append(" WHERE IDINSTITUCION = s.idinstitucionorigen) as INSTITUCIONORIGEN,");
		sql.append(" (SELECT nombre");
		sql.append(" FROM CEN_INSTITUCION");
		sql.append(" WHERE IDINSTITUCION = s.idinstituciondestino) as INSTITUCIONDESTINO,");
		sql.append(" (SELECT nombre");
		sql.append(" FROM CEN_INSTITUCION");
		sql.append(" WHERE IDINSTITUCION = s.idinstitucioncolegiacion) as INSTITUCIONCOLEGIACION,");		
		sql.append("	S.PREFIJO_CER,");
		sql.append(" S.CONTADOR_CER,");
		sql.append(" S.SUFIJO_CER,");
		sql.append(" s.ppn_idtipoproducto as idtipopro, ");
		sql.append(" s.ppn_idproducto as idpro, ");
		sql.append(" s.ppn_idproductoinstitucion as idproinsti, ");
		sql.append(" l.fechapresentacion as FECHAPRESENTACION,");
		sql.append(" l.fechaincorporacion as FECHAINCORPORACION,");
		sql.append(" decode(l.comunitario, 1, l.ncomunitario, l.ncolegiado) AS NCOLEGIADO,");
		sql.append(" (SELECT f_siga_getrecurso(DESCRIPCION, @idioma@) as DESCRIPCION");
		sql.append("  FROM CEN_TIPOIDENTIFICACION");
		sql.append("  WHERE IDTIPOIDENTIFICACION = p.Idtipoidentificacion) AS TIPOIDENTIFICACION,");
		sql.append(" P.NATURALDE AS NATURALDE, ");
			if (!Certificado.CERT_TIPO_DESPACHO1.equals(tipoCert) && 
				!Certificado.CERT_TIPO_DESPACHO2.equals(tipoCert)){	
				sql.append("  F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " );
				sql.append(" 					@idpersona@, " );
				sql.append("                   @idioma@, " );
				sql.append("                   null) AS DIRECCION_DESPACHO, " );
				sql.append("F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " );
				sql.append("                            @idpersona@, " );
				sql.append("                            @idioma@, " );
				sql.append("                            1) AS DOMICILIO_DESPACHO, " );
				sql.append("F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " );
				sql.append("                            @idpersona@, " );
				sql.append("                            @idioma@, " );
				sql.append("                            2) AS CODIGOPOSTAL_DESPACHO, " );
				sql.append("F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " );
				sql.append("                            @idpersona@, " );
				sql.append("                            @idioma@, " );
				sql.append("                            3) AS POBLACION_DESPACHO, " );
				sql.append("F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " );
				sql.append("                            @idpersona@, " );
				sql.append("                            @idioma@, " );
				sql.append("                            4) AS PROVINCIA_DESPACHO, " );
				sql.append("F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " );
				sql.append("                            @idpersona@, " );
				sql.append("                            @idioma@, " );
				sql.append("                            5) AS PAIS_DESPACHO, " );
				sql.append("s.comentario AS OBSERVACIONES, " );
				sql.append("F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " );
				sql.append("                            @idpersona@, " );
				sql.append("                            @idioma@, " );
				sql.append("                            6) AS TELEFONO_DESPACHO, " );
				sql.append("F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " );
				sql.append("                            @idpersona@, " );
				sql.append("                            @idioma@, " );
				sql.append("                            7) AS MOVIL_DESPACHO, " );
				sql.append("F_SIGA_GETDIRECCIONDESPACHO(@idinstitucion@, " );
				sql.append("                            @idpersona@, " );
				sql.append("                            @idioma@, " );
				sql.append("                            8) AS F_DESPACHO, ");
			} 
			sql.append(" (select f_siga_getrecurso(es.descripcion, @idioma@) ");
			sql.append(" from cen_estadocolegial es  " );
			sql.append(" where es.idestado=f_siga_gettipocliente(@idpersona@,@idinstitucion@,sysdate)) AS ESTADO_COLEGIAL");
			return sql.toString();
	}
	
	public String getSqlFromGeneral(){
		return " FROM cen_cliente               c,"+
		        " cen_persona               p,"+
			      " cen_tratamiento           t,"+
			      " cer_solicitudcertificados s,"+
			      " cen_colegiado             l, "+
		          " cen_nocolegiado          no ";
				  
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
				  "      c.idinstitucion = s.idinstitucion)"+
		 		  "  AND no.idinstitucion(+)=c.idinstitucion "+
				  "  AND no.idpersona(+)=c.idpersona ";
                  
	}
	
}