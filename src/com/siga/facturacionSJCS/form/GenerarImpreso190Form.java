//VERSIONES
//raul.ggonzalez 05-04-2005 Creacion
//

package com.siga.facturacionSJCS.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
* Clase action form del caso de uso GenerarImpreso190
* @author AtosOrigin 05-04-2005
*/
public class GenerarImpreso190Form extends MasterForm {
	
	// Metodos Set (Formulario (*.jsp))

	public void setAnio (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"anio", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setNombreFichero (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"nombreFichero", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}
	public void setNombreFicheroOriginal (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"nombreFicheroOriginal", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setCodigoProvincia (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"codigoProvincia", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setSoporte (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"soporte", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setTelefonoContacto (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"telefonoContacto", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setNombreContacto (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"nombreContacto", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setApellido1Contacto (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"apellido1Contacto", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	public void setApellido2Contacto (String dato) { 
		try {
			UtilidadesHash.set(this.datos,"apellido2Contacto", dato);
		} catch (Exception e) {
			// escribimos la traza de momento
			e.printStackTrace();
		}
	}

	// Metodos Get 1 por campo Formulario (*.jsp)
	
	public String getAnio	() 	{ 
		return UtilidadesHash.getString(this.datos, "anio");		
	}

	
	public String getNombreFichero	() 	{ 
		return UtilidadesHash.getString(this.datos, "nombreFichero");		
	}
	public String getNombreFicheroOriginal	() 	{ 
		return UtilidadesHash.getString(this.datos, "nombreFicheroOriginal");		
	}

	public String getCodigoProvincia() 	{ 
		return UtilidadesHash.getString(this.datos, "codigoProvincia");		
	}

	public String getSoporte() 	{ 
		return UtilidadesHash.getString(this.datos, "soporte");		
	}

	public String getTelefonoContacto	() 	{ 
		return UtilidadesHash.getString(this.datos, "telefonoContacto");		
	}

	public String getNombreContacto	() 	{ 
		return UtilidadesHash.getString(this.datos, "nombreContacto");		
	}

	public String getApellido1Contacto() 	{ 
		return UtilidadesHash.getString(this.datos, "apellido1Contacto");		
	}

	public String getApellido2Contacto() 	{ 
		return UtilidadesHash.getString(this.datos, "apellido2Contacto");		
	}


	// OTRAS FUNCIONES 

}