package com.siga.gratuita.form;

/**
 * @author cristina.santos
 */
import com.siga.general.MasterForm;

public class ColaGuardiasForm extends MasterForm {

	//Campos para la tabla de turnos
	String idPersona = "";

	public void setDefGuardia		(String  guardia)		{ if(guardia==null)   datos.remove("DEFGUARDIA"); else datos.put("DEFGUARDIA",guardia);		}
	public void setIdGuardia		(String  guardia)		{ if(guardia==null)   datos.remove("IDGUARDIA");  else datos.put("IDGUARDIA",guardia);		}
	public void setNColegiado		(String  nColegiado)	{ if(nColegiado==null)datos.remove("NCOLEGIADO"); else datos.put("NCOLEGIADO",nColegiado);	}
	public void setNombre			(String  nombre)	 	{ if(nombre==null)	  datos.remove("NOMBRE");     else datos.put("NOMBRE",nombre); 			}
	public void setApellido1		(String  apellido)	 	{ if(apellido==null)  datos.remove("APELLIDO1");  else datos.put("APELLIDO1",apellido); 	}
	public void setApellido2		(String  apellido)	 	{ if(apellido==null)  datos.remove("APELLIDO2");  else datos.put("APELLIDO2",apellido); 	}

	public String 	  getDefGuardia() 						{ return (String)datos.get("DEFGUARDIA"); 	}
	public String 	  getIdGuardia() 						{ return (String)datos.get("IDGUARDIA"); 	}
	public String 	  getNColegiado() 						{ return (String)datos.get("NCOLEGIADO"); 	}
	public String 	  getNombre()     						{ return (String)datos.get("NOMBRE"); 		}
	public String 	  getApellido1()    					{ return (String)datos.get("APELLIDO1"); 	}
	public String 	  getApellido2()     					{ return (String)datos.get("APELLIDO2"); 	}
	
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
}