// VERSIONES
//David Sanchez Pina
// 01/02/2007

package com.siga.censo.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenPersonaBean;
import com.siga.general.MasterForm;

/**
 * Clase action form del caso de uso GRUPOS CLIENTES CLIENTES
 * @author AtosOrigin 01/02/2007
 */
 public class ActividadProfesionalForm extends MasterForm {
 	
	public void setIdInstitucion (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"IDINSTITUCION", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setIdPersona (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CenPersonaBean.C_IDPERSONA, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	
 	public String getIdInstitucion	() 	{ 
 		return UtilidadesHash.getString(this.datos, "IDINSTITUCION");		
 	}

 	public String getIdPersona	() 	{ 
 		return UtilidadesHash.getString(this.datos, CenPersonaBean.C_IDPERSONA);		
 	}

 	public String getIdActividadProfesional() 	{ 
		return UtilidadesHash.getString(this.datos, "ACTIVIDADPROFESIONAL");		
	}
	public void setIdActividadProfesional(String dato) { 
		UtilidadesHash.set(this.datos,"ACTIVIDADPROFESIONAL", dato);
	}
	public String getIdIdActividadProfesional() 	{ 
		return UtilidadesHash.getString(this.datos, "IDINSTITUCIONACTIVIDADPROFESIONAL");		
	}
	public void setIdIdActividadProfesional(String dato) { 
		UtilidadesHash.set(this.datos,"IDINSTITUCIONACTIVIDADPROFESIONAL", dato);
	}
	public String getModoAnterior() 	{ 
		return UtilidadesHash.getString(this.datos, "MODOANTERIOR");		
	}
	public void setModoAnterior(String dato) { 
		UtilidadesHash.set(this.datos,"MODOANTERIOR", dato);
	}
	public String getGrupos() 	{ 
		return UtilidadesHash.getString(this.datos, "GRUPOS");		
	}
	public void setGrupos(String dato) { 
		UtilidadesHash.set(this.datos,"GRUPOS", dato);
	}
}