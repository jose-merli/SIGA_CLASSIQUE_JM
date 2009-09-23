//Clase: DefinirExpedientesSOJAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 14/02/2005

package com.siga.informes;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.ScsDelitosEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsTelefonosPersonaJGAdm;
import com.siga.beans.ScsUnidadFamiliarEJGAdm;
import com.siga.general.SIGAException;


/**
 * @author david.sanchezp
 * @since 23/05/2006
 */
public class InformeDefinirEJG extends MasterReport {	
	
	/**
	  * Este método se debe sobreescribir para reemplazar los valores en las plantillas FO
	  * @param request Objeto HTTPRequest
	  * @param plantillaFO Plantilla FO con parametros 
	  * @return Plantilla FO en donde se han reemplazado los parámetros
	  * @throws ClsExceptions
	  */
	protected String reemplazarDatos(HttpServletRequest request, String plantillaFO, Hashtable datosEJG) throws ClsExceptions, SIGAException{
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String idioma = usr.getLanguage().toUpperCase();
		String institucion=(String)datosEJG.get(ScsEJGBean.C_IDINSTITUCION);
		String tipoEJG=(String)datosEJG.get(ScsEJGBean.C_IDTIPOEJG);
		String numero=(String)datosEJG.get(ScsEJGBean.C_NUMERO);
		String anio=(String)datosEJG.get(ScsEJGBean.C_ANIO);
		
		//delitos
		ScsDelitosEJGAdm delitosAdm=new ScsDelitosEJGAdm(usr);
		Vector delitos=delitosAdm.getDelitosEJG(Integer.valueOf(institucion),Integer.valueOf(anio),Integer.valueOf(numero),Integer.valueOf(tipoEJG), usr.getLanguage());
		plantillaFO = this.reemplazaRegistros(plantillaFO,delitos,datosEJG,"DELITOS");
		
		//datos unidad familiar
		ScsUnidadFamiliarEJGAdm ufAdm =  new ScsUnidadFamiliarEJGAdm(usr);	
		Vector vUF=ufAdm.getListadoCartaEJG(institucion,tipoEJG,anio,numero);
		plantillaFO = this.reemplazaRegistros(plantillaFO,vUF,datosEJG,"UNIDAD_FAMILIAR");
		 
		Vector vTUF = ufAdm.getTotalesCartaEJG(institucion,tipoEJG,anio,numero);
		 if (vTUF!=null && vTUF.size()==1) {
			 datosEJG.putAll((Hashtable)vTUF.get(0));
		}
		 
		 //datos interesado (el nombre y la direccion ya vienen de los datos EJG)
		 String idPersonaInteresado=(String)datosEJG.get("IDINTERESADO");
		 if (idPersonaInteresado!=null && idPersonaInteresado.length()>0){
			 ScsTelefonosPersonaJGAdm telefonosPersonaJGAdm = new ScsTelefonosPersonaJGAdm(usr);
			 String telefono=telefonosPersonaJGAdm.getNumeroTelefono(institucion,idPersonaInteresado);
			 if(telefono!=null){
				 datosEJG.put("TELEFONO_INTERESADO",telefono);
			 }
		 }
		 
		 //datos letrado
		 String idPersonaLetrado=(String)datosEJG.get("IDLETRADO");
		 if (idPersonaLetrado!=null && idPersonaLetrado.length()>0){
			 CenColegiadoAdm colegiadoAdm=new CenColegiadoAdm(usr);
			 Hashtable ht=colegiadoAdm.obtenerDatosColegiado(institucion,idPersonaLetrado,idioma);
			 if (ht!=null) {
				 datosEJG.putAll(ht);
			 }
		 }
		 
		 //reemplazar los datos en la plantilla
		 plantillaFO = this.reemplazaVariables(datosEJG,plantillaFO);
		 
		 // Datos generales EJG 
		 return plantillaFO;
	}
	  
}