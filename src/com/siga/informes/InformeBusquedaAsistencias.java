package com.siga.informes;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.ScsActuacionAsistenciaAdm;
import com.siga.beans.ScsActuacionAsistenciaBean;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsComisariaAdm;
import com.siga.beans.ScsContrariosAsistenciaAdm;
import com.siga.beans.ScsDelitosAsistenciaAdm;
import com.siga.beans.ScsJuzgadoAdm;
import com.siga.beans.ScsTelefonosPersonaJGAdm;
import com.siga.general.SIGAException;

/**
 * @author david.sanchezp
 * @since 23/05/2006
 */
public class InformeBusquedaAsistencias extends MasterReport {

	
	/**
	  * Este método se debe sobreescribir para reemplazar los valores en las plantillas FO
	  * @param request Objeto HTTPRequest
	  * @param plantillaFO Plantilla FO con parametros 
	  * @return Plantilla FO en donde se han reemplazado los parámetros
	  * @throws ClsExceptions
	  */
	protected String reemplazarDatos(HttpServletRequest request, String plantillaFO, Hashtable datosAsistencia) throws ClsExceptions, SIGAException{
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String idioma = usr.getLanguage().toUpperCase();
		Integer institucion=Integer.valueOf(usr.getLocation());
		Integer anio=Integer.valueOf((String)datosAsistencia.get(ScsAsistenciasBean.C_ANIO));
		Integer numero=Integer.valueOf((String)datosAsistencia.get(ScsAsistenciasBean.C_NUMERO));
		
		//Datos Generales, solo falta el EJG
		ScsAsistenciasAdm asistenciasAdm=new ScsAsistenciasAdm(usr);
		String numeroEJG=asistenciasAdm.getNumeroAnioEJGAsistencia(institucion,anio,numero);
		if(numeroEJG!=null){
			datosAsistencia.put("NUMERO_EJG",numeroEJG);
		}
		
		//Lugar
		ScsActuacionAsistenciaAdm actuacionAdm=new ScsActuacionAsistenciaAdm(usr);
		Hashtable htLugar=actuacionAdm.obtenerIdsLugar(institucion,anio,numero);
		if(htLugar!=null && htLugar.size()>0){
			String idJuzgado=(String)htLugar.get(ScsActuacionAsistenciaBean.C_IDJUZGADO);
			String idInstitJuzgado=(String)htLugar.get(ScsActuacionAsistenciaBean.C_IDINSTITUCIONJUZGADO);
			if (idJuzgado!=null && idJuzgado.length()>0){
				ScsJuzgadoAdm juzgadoAdm=new ScsJuzgadoAdm(usr);
				htLugar=juzgadoAdm.obtenerDatosJuzgado(idInstitJuzgado,idJuzgado);
				if (htLugar!=null) {
					datosAsistencia.putAll(htLugar);
				}
			}else{
				String idComisaria=(String)htLugar.get(ScsActuacionAsistenciaBean.C_IDCOMISARIA);
				String idInstitComisaria=(String)htLugar.get(ScsActuacionAsistenciaBean.C_IDINSTITUCIONCOMISARIA);
				if (idComisaria!=null && idComisaria.length()>0){
					ScsComisariaAdm comisariaAdm=new ScsComisariaAdm(usr);
					htLugar=comisariaAdm.obtenerDatosComisaria(idInstitComisaria,idComisaria);
					if (htLugar!=null) {
						datosAsistencia.putAll(htLugar);
					}
				}
			}
		}
		
		//Delitos
		ScsDelitosAsistenciaAdm delitosAdm=new ScsDelitosAsistenciaAdm(usr);
		Vector delitos=delitosAdm.getDelitosAsitencia(institucion,anio,numero,usr.getLanguage());
		plantillaFO = this.reemplazaRegistros(plantillaFO,delitos,datosAsistencia,"DELITOS");
		
		//Asistido tengo nif ,nombre y direccion
		 String idPersonaAsistido=(String)datosAsistencia.get("IDASISTIDO");
		 if (idPersonaAsistido!=null && idPersonaAsistido.length()>0){
			 ScsTelefonosPersonaJGAdm telefonosPersonaJGAdm = new ScsTelefonosPersonaJGAdm(usr);
			 String telefono=telefonosPersonaJGAdm.getNumeroTelefono(institucion.toString(),idPersonaAsistido);
			 if(telefono!=null){
				 datosAsistencia.put("TELEFONO_ASISTIDO",telefono);
			 }
		 }

		//Letrado
		 String idPersonaLetrado=(String)datosAsistencia.get("IDLETRADO");
		 if (idPersonaLetrado!=null && idPersonaLetrado.length()>0){
			 CenColegiadoAdm colegiadoAdm=new CenColegiadoAdm(usr);
			 Hashtable ht=colegiadoAdm.obtenerDatosColegiado(institucion.toString(),idPersonaLetrado,idioma);
			 if (ht!=null) {
				 datosAsistencia.putAll(ht);
			 }
		 }
		//Contrario
			ScsContrariosAsistenciaAdm contrariosAdm=new ScsContrariosAsistenciaAdm(usr);
			Vector contrarios=contrariosAdm.getListaContrarios(institucion,anio,numero);
			plantillaFO = this.reemplazaRegistros(plantillaFO,contrarios,datosAsistencia,"CONTRARIOS");
		
		
		 //reemplazar los datos en la plantilla
		 plantillaFO = this.reemplazaVariables(datosAsistencia,plantillaFO);
		 
		 // Datos generales EJG 
		 return plantillaFO;
	}	

}