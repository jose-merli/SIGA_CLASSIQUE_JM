package com.siga.informes;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.ScsActuacionAsistenciaAdm;
import com.siga.beans.ScsActuacionAsistenciaBean;
import com.siga.beans.ScsComisariaAdm;
import com.siga.beans.ScsDelitosAsistenciaAdm;
import com.siga.beans.ScsDocumentacionEJGAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsJuzgadoAdm;
import com.siga.beans.ScsUnidadFamiliarEJGAdm;
import com.siga.general.SIGAException;

/**
 * @author david.sanchezp
 * @since 23/05/2006
 */
public class InformeDefinirMantenimientoEJG extends MasterReport {	
	
	protected String reemplazarDatos(HttpServletRequest request, String plantilla, Hashtable datosBase) throws ClsExceptions, SIGAException{
		Hashtable htDatos=(Hashtable)datosBase.clone();
		
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN"); 
		//Integer usuario=this.getUserName(request);
		String institucion =usr.getLocation();
		String idioma=usr.getLanguage().toUpperCase();
		
		String anio  =(String)datosBase.get(ScsEJGBean.C_ANIO);
		String numero=(String)datosBase.get(ScsEJGBean.C_NUMERO);
		String tipo  =(String)datosBase.get(ScsEJGBean.C_IDTIPOEJG);
		String sqlEJG=
			"select " +
			"idpersona idletrado," +
			"idpersonajg idinteresado," +
			"idinstitucion_proc," +
			"idprocurador," +
			"observaciones," +
			"(select a.anio from scs_asistencia a where a.idinstitucion=idinstitucion and a.ejgidtipoejg=idtipoejg and a.ejganio=anio and a.ejgnumero=numero) as asistencia_anio," +
			"(select a.numero from scs_asistencia a where a.idinstitucion=idinstitucion and a.ejgidtipoejg=idtipoejg and a.ejganio=anio and a.ejgnumero=numero) as asistencia_numero," +
			"decode(calidad, 'D', 'Demandante', 'O', 'Demandado', '') calidad" +
			" from scs_ejg " +
			"where idinstitucion=" +institucion+
			"  and anio="+anio+
			"  and numero="+numero+
			"  and idtipoejg="+tipo;
		
		ScsEJGAdm adm = new ScsEJGAdm(usr);
		RowsContainer rc= adm.find(sqlEJG);
		if(rc!=null && rc.size()>0){
			Hashtable ht= ((Row)rc.get(0)).getRow();
			htDatos.putAll(ht);
		}
		
		String interesado=(String)htDatos.get("IDINTERESADO");
		if(interesado!= null && interesado.length()>0){
			String gananciales= UtilidadesString.getMensajeIdioma(idioma,"gratuita.personaJG.regimen.literal.gananciales");
			String separacionBienes = UtilidadesString.getMensajeIdioma(idioma,"gratuita.personaJG.regimen.literal.separacion");
			
			String sqlDatosInteresado=
				"select " +
				"pjg.apellido1||' '||pjg.apellido2||', '||pjg.nombre nombre," +
				"pjg.nif," +
				"(select " + UtilidadesMultidioma.getCampoMultidioma("p.descripcion",usr.getLanguage()) + " from scs_profesion p where p.idprofesion=pjg.idprofesion) profesion," +
				"(select " + UtilidadesMultidioma.getCampoMultidioma("e.descripcion", usr.getLanguage()) + " from cen_estadocivil e where e.idestadocivil=pjg.idestadocivil) estado_civil," +
				"to_char(pjg.fechanacimiento,'DD/MM/YYYY') fecha_nacimiento," +
				"decode(pjg.regimen_conyugal,'G','"+gananciales+"','"+separacionBienes+"') regimen_conyugal," +
				"pjg.direccion," +
				"pjg.codigopostal," +
				"(select tp1.numerotelefono from Scs_TelefonosPersona tp1" +
				" where tp1.idinstitucion = pjg.idinstitucion and tp1.idpersona = pjg.idpersona" +
				"   and tp1.idtelefono =" +
				"       (select min(tp2.idtelefono) from Scs_TelefonosPersona tp2" +
				"        where tp2.idinstitucion = tp1.idinstitucion and tp2.idpersona = tp1.idpersona)" +
				") telefono," +
				"(select p.nombre from cen_poblaciones p where p.idpoblacion= pjg.idpoblacion) poblacion," +
				"(select p.nombre from cen_provincias p where p.idprovincia= pjg.idprovincia) provincia," +
				"(select " + UtilidadesMultidioma.getCampoMultidioma("p.nombre", usr.getLanguage()) +" from cen_pais p where p.idpais= pjg.idpais) pais " +
				"from scs_personajg pjg " +
				"where pjg.idpersona=" +interesado+
				"  and pjg.idinstitucion=" +institucion;
			rc= adm.find(sqlDatosInteresado);
			if(rc!=null && rc.size()>0){
				Hashtable ht= ((Row)rc.get(0)).getRow();
				htDatos.putAll(ht);
			}
			
			// Información para obtener los miembros de la unidad familiar
			Vector familiares=adm.getMiembrosUnidadFamiliar(institucion,tipo,anio,numero);
			plantilla = this.reemplazaRegistros(plantilla,familiares,datosBase,"UNIDAD_FAMILIAR");
			
			// Datos economicos de la unidad familiar
			//Vector datosEconomicos=adm.getDatosEconomicosUnidadFamiliar(institucion,tipo,anio,numero);
			//plantilla=this.reemplazaRegistros(plantilla,datosEconomicos,datosBase,"INGRESOS_ANUALES");
			//datos unidad familiar
			ScsUnidadFamiliarEJGAdm ufAdm =  new ScsUnidadFamiliarEJGAdm(usr);	
			Vector vUF=ufAdm.getListadoCartaEJG(institucion,tipo,anio,numero);
			plantilla = this.reemplazaRegistros(plantilla,vUF,datosBase,"INGRESOS_ANUALES");
			plantilla = this.reemplazaRegistros(plantilla,vUF,datosBase,"BIENES_MUEBLES");
			plantilla = this.reemplazaRegistros(plantilla,vUF,datosBase,"BIENES_INMUEBLES");
			plantilla = this.reemplazaRegistros(plantilla,vUF,datosBase,"OTROS_BIENES");
		}
		
		//datos letrado
		String idPersonaLetrado=(String)htDatos.get("IDLETRADO");
		if (idPersonaLetrado!=null && idPersonaLetrado.length()>0){
			CenColegiadoAdm colegiadoAdm=new CenColegiadoAdm(usr);
			Hashtable ht=colegiadoAdm.obtenerDatosColegiado(institucion,idPersonaLetrado,idioma);
			if (ht!=null) {
				htDatos.putAll(ht);
			}
		}
		//datos procurador
		String idInstitProcurador=(String)htDatos.get(ScsEJGBean.C_IDINSTITUCIONPROCURADOR);
		String idProcurador=(String)htDatos.get(ScsEJGBean.C_IDPROCURADOR);
		if (idProcurador!=null && idProcurador.length()>0){
			String sqlProc=
				"select p.ncolegiado ncolegiado_proc," +
				"p.apellidos1||' '||p.apellidos2||', '||p.nombre nombre_proc," +
				"p.domicilio direccion_proc,p.codigopostal cp_proc," +
				"(select pb.nombre from cen_poblaciones pb where pb.idpoblacion=p.idpoblacion) poblacion_proc," +
				"(select pr.nombre from cen_provincias pr where pr.idprovincia=p.idprovincia) provincia_proc," +
				"p.telefono1 tf1_proc,p.telefono2 tf2_proc " +
				"from scs_procurador p " +
				"where p.idinstitucion=" +idInstitProcurador+
				" and p.idprocurador="+idProcurador;
			rc= adm.find(sqlProc);
			if(rc!=null && rc.size()>0){
				Hashtable ht= ((Row)rc.get(0)).getRow();
				htDatos.putAll(ht);
			}
			
			
			
			//datos asistencia
			String asistencia_anio=(String)htDatos.get("asistencia_anio");
			String asistencia_numero=(String)htDatos.get("asistencia_numero");
			try {
				Integer intInstitucion= Integer.valueOf(institucion); 
				Integer intAsistAnio= Integer.valueOf(asistencia_anio); 
				Integer intAsistNumero=Integer.valueOf(asistencia_numero); 
				//nombre del letrado y fecha 
				String sqlAsistencia=
					"select " +
					"(select p.apellidos1 || ' ' || p.apellidos2 || ', ' || p.nombre" +
					"   from cen_persona p  " +
					"  where p.idpersona = a.idpersonacolegiado) asistencia_letrado," +
					"to_char(a.fechahora, 'DD/MM/YYYY') asistencia_fecha  " +
					"from scs_asistencia a " +
					"where idinstitucion = " +intInstitucion+
					"  and anio = " +intAsistAnio+
					"  and numero = "+intAsistNumero;
				rc= adm.find(sqlAsistencia);
				if(rc!=null && rc.size()>0){
					Hashtable ht= ((Row)rc.get(0)).getRow();
					htDatos.putAll(ht);
				}
				
				//Lugar
				ScsActuacionAsistenciaAdm actuacionAdm=new ScsActuacionAsistenciaAdm(usr);
				Hashtable htLugar=actuacionAdm.obtenerIdsLugar(intInstitucion,intAsistAnio,intAsistNumero);
				if(htLugar!=null && htLugar.size()>0){
					String idJuzgado=(String)htLugar.get(ScsActuacionAsistenciaBean.C_IDJUZGADO);
					String idInstitJuzgado=(String)htLugar.get(ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO);
					if (idJuzgado!=null && idJuzgado.length()>0){
						ScsJuzgadoAdm juzgadoAdm=new ScsJuzgadoAdm(usr);
						htLugar=juzgadoAdm.obtenerDatosJuzgado(idInstitJuzgado,idJuzgado);
						if (htLugar!=null) {
							if(htLugar.containsKey("NOMBRE"))htDatos.put("NOMBRE_LUGAR",htLugar.get("NOMBRE"));
							if(htLugar.containsKey("DIRECCION_JUZGADO"))htDatos.put("DIRECCION_LUGAR",htLugar.get("DIRECCION_JUZGADO"));
							if(htLugar.containsKey("PARTIDO_JUDICIAL"))htDatos.put("PARTIDO_JUDICIAL",htLugar.get("PARTIDO_JUDICIAL"));
						}
					}else{
						String idComisaria=(String)htLugar.get(ScsActuacionAsistenciaBean.C_IDCOMISARIA);
						String idInstitComisaria=(String)htLugar.get(ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA);
						if (idComisaria!=null && idComisaria.length()>0){
							ScsComisariaAdm comisariaAdm=new ScsComisariaAdm(usr);
							htLugar=comisariaAdm.obtenerDatosComisaria(idInstitComisaria,idComisaria);
							if (htLugar!=null) {
								if(htLugar.containsKey("NOMBRE"))htDatos.put("NOMBRE_LUGAR",htLugar.get("NOMBRE"));
								if(htLugar.containsKey("DIRECCION_COMISARIA"))htDatos.put("DIRECCION_LUGAR",htLugar.get("DIRECCION_COMISARIA"));
								if(htLugar.containsKey("PARTIDO_JUDICIAL"))htDatos.put("PARTIDO_JUDICIAL",htLugar.get("PARTIDO_JUDICIAL"));
							}
						}
					}
				}
				
				//Delitos
				ScsDelitosAsistenciaAdm delitosAdm=new ScsDelitosAsistenciaAdm(usr);
				Vector delitos=delitosAdm.getDelitosAsitencia(intInstitucion,intAsistAnio,intAsistNumero,usr.getLanguage());
				plantilla = this.reemplazaRegistros(plantilla,delitos,htDatos,"DELITOS");
			} catch (NullPointerException e1) {
			} catch (NumberFormatException e2) {
				//si ocurre una excepcion de estos tipos es porque no existe asistencia
			}
			
			//datos colegio
			CenInstitucionAdm institAdm= new CenInstitucionAdm(usr);
			String nombreInstit=institAdm.getNombreInstitucion(institucion);
			if(nombreInstit!=null)htDatos.put("NOMBRE_COLEGIO",nombreInstit);
			//documentos que faltan
			ScsDocumentacionEJGAdm docsAdm=new ScsDocumentacionEJGAdm(usr);
			Vector docs=docsAdm.buscarDocumentacionPendiente(institucion, tipo, anio, numero);
			plantilla = this.reemplazaRegistros(plantilla,docs,htDatos,"DOCUMENTOS");
			
			//&#x25A0; &#x274F; son los simbolos de cuadradito y cuadradito relleno, 
			//equivalentes a una checkbox, para la fuente "ZapfDingbats"
			if(docs!=null && docs.size()>0){
				htDatos.put("MARCA1","\u274F");
				htDatos.put("MARCA2","\u25A0");
			}else{
				htDatos.put("MARCA1","\u25A0");
				htDatos.put("MARCA2","\u274F");
			}
			
			
			//fecha actual
			UtilidadesHash.set(htDatos,"SYSDATE",UtilidadesBDAdm.getFechaBD(""));
			
			
		}
		
		plantilla = this.reemplazaVariables(htDatos, plantilla);
		return plantilla;
	}
}