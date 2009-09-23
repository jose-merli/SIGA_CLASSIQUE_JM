//VERSIONES
//raul.ggonzalez 23-03-2005 Creacion
//

package com.siga.facturacionSJCS.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
* Clase action form del caso de uso REGULARIZACION
* @author AtosOrigin 23-03-2005
*/
public class RegularizacionForm extends MasterForm {
	
	// Metodos Set (Formulario (*.jsp))

	public void setIdInstitucion (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"idInstitucion", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setIdFacturacion (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"idFacturacion", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setNombre (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"nombre", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	// Metodos Get 1 por campo Formulario (*.jsp)
	
	public String getIdInstitucion	() 	{ 
		return UtilidadesHash.getString(this.datos, "idInstitucion");		
	}

	
	public String getIdFacturacion	() 	{ 
		return UtilidadesHash.getString(this.datos, "idFacturacion");		
	}

	public String getNombre	() 	{ 
		return UtilidadesHash.getString(this.datos, "nombre");		
	}



	// OTRAS FUNCIONES 

}