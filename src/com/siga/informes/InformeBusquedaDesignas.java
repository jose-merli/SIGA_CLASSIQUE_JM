package com.siga.informes;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.ScsActuacionDesignaAdm;
import com.siga.beans.ScsContrariosDesignaAdm;
import com.siga.beans.ScsDefendidosDesignaAdm;
import com.siga.beans.ScsDelitosDesignaAdm;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsJuzgadoAdm;
import com.siga.general.SIGAException;


/**
 * @author david.sanchezp
 * @since 23/05/2006
 */
public class InformeBusquedaDesignas extends MasterReport {
	
	/**
	  * Este método se debe sobreescribir para reemplazar los valores en las plantillas FO
	  * @param request Objeto HTTPRequest
	  * @param plantillaFO Plantilla FO con parametros 
	  * @return Plantilla FO en donde se han reemplazado los parámetros
	  * @throws ClsExceptions
	  */
	protected String reemplazarDatos(HttpServletRequest request, String plantillaFO, Hashtable datosDesigna) throws ClsExceptions, SIGAException{
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String idioma = usr.getLanguage().toUpperCase();
		Integer institucion=Integer.valueOf(usr.getLocation());
		Integer turno=Integer.valueOf((String)datosDesigna.get(ScsDesignaBean.C_IDTURNO));
		Integer numero=Integer.valueOf((String)datosDesigna.get(ScsDesignaBean.C_NUMERO));
		Integer anio=Integer.valueOf((String)datosDesigna.get(ScsDesignaBean.C_ANIO));
		
		//Datos Generales, solo falta el EJG
		ScsDesignaAdm designaAdm=new ScsDesignaAdm(usr);
		String numeroEJG=designaAdm.getNumeroEJGDesigna(institucion,anio,numero,turno);
		if(numeroEJG!=null){
			datosDesigna.put("NUMERO_EJG",numeroEJG);
		}
		
		//Juzgado
		 String idJuzgado=(String)datosDesigna.get(ScsDesignaBean.C_IDJUZGADO);
		 String idInstitJuzgado=(String)datosDesigna.get(ScsDesignaBean.C_IDINSTITUCIONJUZGADO);
		 if (idJuzgado!=null && idJuzgado.length()>0){
			 ScsJuzgadoAdm juzgadoAdm=new ScsJuzgadoAdm(usr);
			 Hashtable ht=juzgadoAdm.obtenerDatosJuzgado(idInstitJuzgado,idJuzgado);
			 if (ht!=null) {
				 datosDesigna.putAll(ht);
			 }
		 }
		
		//Actuaciones 
		ScsActuacionDesignaAdm actuacionesAdm=new ScsActuacionDesignaAdm(usr);
		Vector actuaciones=actuacionesAdm.getListaActuaciones(institucion,anio,numero,turno);		
		plantillaFO = this.reemplazaRegistros(plantillaFO,actuaciones,datosDesigna,"ACTUACIONES");
		
		//Delitos
		ScsDelitosDesignaAdm delitosAdm=new ScsDelitosDesignaAdm(usr);
		Vector delitos=delitosAdm.getDelitosDesigna(institucion,anio,numero,turno,usr.getLanguage());
		plantillaFO = this.reemplazaRegistros(plantillaFO,delitos,datosDesigna,"DELITOS");
		
        //Interesados
		ScsDefendidosDesignaAdm defendidosAdm=new ScsDefendidosDesignaAdm(usr);
		Vector defendidos=defendidosAdm.getListaDefendidos(institucion,anio,numero,turno, idioma);
		plantillaFO = this.reemplazaRegistros(plantillaFO,defendidos,datosDesigna,"INTERESADOS");
		
		//Letrado
		 String idPersonaLetrado=(String)datosDesigna.get("IDLETRADO");
		 if (idPersonaLetrado!=null && idPersonaLetrado.length()>0){
			 CenColegiadoAdm colegiadoAdm=new CenColegiadoAdm(usr);
			 Hashtable ht=colegiadoAdm.obtenerDatosColegiado(institucion.toString(),idPersonaLetrado,idioma);
			 if (ht!=null) {
				 datosDesigna.putAll(ht);
			 }
		 }
		 
		//Contrarios
		ScsContrariosDesignaAdm contrariosAdm=new ScsContrariosDesignaAdm(usr);
		Vector contrarios=contrariosAdm.getListaContrarios(institucion,anio,numero,turno);
		plantillaFO = this.reemplazaRegistros(plantillaFO,contrarios,datosDesigna,"CONTRARIOS");
		 
		 //reemplazar los datos en la plantilla
		 plantillaFO = this.reemplazaVariables(datosDesigna,plantillaFO);
		 
		 // Datos generales EJG 
		 return plantillaFO;
	}
	
}