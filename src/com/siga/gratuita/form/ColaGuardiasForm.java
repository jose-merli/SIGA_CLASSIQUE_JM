package com.siga.gratuita.form;

/**
 * @author cristina.santos
 */
import com.siga.general.MasterForm;

public class ColaGuardiasForm extends MasterForm {

	//Campos para la tabla de turnos
	String idPersona = "";
	private String fechaConsulta;
	public String getFechaConsulta() {
		return fechaConsulta;
	}
	public void setFechaConsulta(String fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}
	public void setDefGuardia		(String  guardia)		{ if(guardia==null)   datos.remove("DEFGUARDIA"); else datos.put("DEFGUARDIA",guardia);		}
	public void setIdGuardia		(String  guardia)		{ if(guardia==null)   datos.remove("IDGUARDIA");  else datos.put("IDGUARDIA",guardia);		}
	public void setNColegiado		(String  nColegiado)	{ if(nColegiado==null)datos.remove("NCOLEGIADO"); else datos.put("NCOLEGIADO",nColegiado);	}
	public void setNombre			(String  nombre)	 	{ if(nombre==null)	  datos.remove("NOMBRE");     else datos.put("NOMBRE",nombre); 			}
	public void setApellido1		(String  apellido)	 	{ if(apellido==null)  datos.remove("APELLIDO1");  else datos.put("APELLIDO1",apellido); 	}
	public void setApellido2		(String  apellido)	 	{ if(apellido==null)  datos.remove("APELLIDO2");  else datos.put("APELLIDO2",apellido); 	}
	public void setDatosModificados	(String  datosMod)	 	{ if(datosMod==null)  datos.remove("DATOSMODIFICADOS");  else datos.put("DATOSMODIFICADOS",datosMod); 	}
	public void setIdInstitucion	(String  dato)	 		{ if(dato==null)  datos.remove("IDINSTITUCION");  else datos.put("IDINSTITUCION",dato); 	}
	public void setIdTurno			(String  dato)	 		{ if(dato==null)  datos.remove("IDTURNO");  else datos.put("IDTURNO",dato); 	}
	public void setIdGrupoGuardiaColegiado (String  idGuardia){ 
		if(idGuardia==null) 
			datos.remove("IDGRUPOGUARDIACOLEGIADO");  
		else 
			datos.put("IDGRUPOGUARDIACOLEGIADO",idGuardia); 	
	}

	public String 	  getDefGuardia() 						{ return (String)datos.get("DEFGUARDIA"); 	}
	public String 	  getIdGuardia() 						{ return (String)datos.get("IDGUARDIA"); 	}
	public String 	  getNColegiado() 						{ return (String)datos.get("NCOLEGIADO"); 	}
	public String 	  getNombre()     						{ return (String)datos.get("NOMBRE"); 		}
	public String 	  getApellido1()    					{ return (String)datos.get("APELLIDO1"); 	}
	public String 	  getApellido2()     					{ return (String)datos.get("APELLIDO2"); 	}
	public String 	  getIdGrupoGuardiaColegiado()     		{ return (String)datos.get("IDGRUPOGUARDIACOLEGIADO"); 	}
	public String 	  getDatosModificados()     			{ return (String)datos.get("DATOSMODIFICADOS"); 	}
	public String 	  getIdTurno()     						{ return (String)datos.get("IDTURNO"); 	}
	public String 	  getIdInstitucion()     				{ return (String)datos.get("IDINSTITUCION"); 	}
	
	
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
}