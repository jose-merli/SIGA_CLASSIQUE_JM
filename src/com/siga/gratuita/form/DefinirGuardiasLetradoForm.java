package com.siga.gratuita.form;

import com.siga.general.MasterForm;

/**
 * @author ruben.fernandez
 */



public class DefinirGuardiasLetradoForm extends MasterForm {
	
		//Metodos set de los campos del formulario

	public void setGuardia (String valor){ 
		this.datos.put("GUARDIA", valor);
	}
	public void setObligatoriedad (String valor){ 
		this.datos.put("OBLIGATORIEDAD", valor);
	}
	public void setTipoDia (String valor){ 
		this.datos.put("TIPODIA", valor);
	}
	public void setDuracion (String valor){ 
		this.datos.put("DURACION", valor);
	}
	public void setLetradosGuardia (String valor){ 
		this.datos.put("LETRADOSGUARDIA", valor);
	}
	public void setNumeroSustitutos (String valor){ 
		this.datos.put("NUMEROSUSTITUTOS", valor);
	}
	public void setDiasPagados (String valor){ 
		this.datos.put("DIASPAGADOS", valor);
	}
	public void setValidarJustificacion (String valor){ 
		this.datos.put("VALIDARJUSTIFICACIONES", valor);
	}
	public void setLetradosApuntados (String valor){ 
		this.datos.put("LETRADOSAPUNTADOS", valor);
	}
	public void setFechaInscripcion (String valor){ 
		this.datos.put("FECHAINSCRIPCION", valor);
	}
	public void setFechaBaja (String valor){ 
		this.datos.put("FECHABAJA", valor);
	}
		//Metodos get

	public String getGuardia (String valor){ 
		return (String)this.datos.get("GUARDIA");
	}
	public String getObligatoriedad (String valor){ 
		return (String)this.datos.get("OBLIGATORIEDAD");
	}
	public String getTipoDia (String valor){ 
		return (String)this.datos.get("TIPODIA");
	}
	public String getDuracion (String valor){ 
		return (String)this.datos.get("DURACION");
	}
	public String getLetradosGuardia (String valor){ 
		return (String)this.datos.get("LETRADOSGUARDIA");
	}
	public String getNumeroSustitutos (String valor){ 
		return (String)this.datos.get("NUMEROSUSTITUTOS");
	}
	public String getDiasPagados (String valor){ 
		return (String)this.datos.get("DIASPAGADOS");
	}
	public String getValidarJustificacion (String valor){ 
		return (String)this.datos.get("VALIDARJUSTIFICACIONES");
	}
	public String getLetradosApuntados (String valor){ 
		return (String)this.datos.get("LETRADOSAPUNTADOS");
	}
	public String getFechaInscripcion (String valor){ 
		return (String)this.datos.get("FECHAINSCRIPCION");
	}
	public String getFechaBaja (String valor){ 
		return (String)this.datos.get("FECHABAJA");
	}
	
}