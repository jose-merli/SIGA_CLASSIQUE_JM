/*
 * Created on Jan 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.form;

import com.siga.general.MasterForm;

/**
 * Formulario con los datos de los denunciantes de un expediente
 */
public class ExpDenuncianteForm extends MasterForm {
	
	private String modo="";
    private String modal="";
	private String nombre="";
	private String primerApellido="";
	private String segundoApellido="";
	private String direccion="";
	private String poblacion="";
	private String provincia="";
	private String pais="";
	private String cpostal="";
	private String telefono="";
	private String nif="";
	private String poblacionExt="";
	private String numColegiado = "";
	private String idPersona = "", 
			idDireccion = "" , 
			idInstitucion = "", 
			idInstitucion_TipoExpediente = "", 
			idTipoExpediente = "", 
			numExpediente = "", 
			anioExpediente = "";
	

	public String getCpostal() {
		return cpostal;
	}
	public void setCpostal(String cpostal) {
		this.cpostal = cpostal;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}
	 public void setPoblacionExt(String poblacion) {
        this.poblacionExt = poblacion;
    }
    public String getPoblacionExt() {
        return poblacionExt;
    }
	public String getPrimerApellido() {
		return primerApellido;
	}
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getSegundoApellido() {
		return segundoApellido;
	}
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
    /**
     * @return Returns the idDireccion.
     */
    public String getIdDireccion() {
        return idDireccion;
    }
    /**
     * @param idDireccion The idDireccion to set.
     */
    public void setIdDireccion(String idDireccion) {
        this.idDireccion = idDireccion;
    }
    /**
     * @return Returns the idPersona.
     */
    public String getIdPersona() {
        return idPersona;
    }
    /**
     * @param idPersona The idPersona to set.
     */
    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
    }
    /**
     * @return Returns the anioExpediente.
     */
    public String getAnioExpediente() {
        return anioExpediente;
    }
    /**
     * @param anioExpediente The anioExpediente to set.
     */
    public void setAnioExpediente(String anioExpediente) {
        this.anioExpediente = anioExpediente;
    }
    /**
     * @return Returns the idInstitucion.
     */
    public String getIdInstitucion() {
        return idInstitucion;
    }
    /**
     * @param idInstitucion The idInstitucion to set.
     */
    public void setIdInstitucion(String idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    /**
     * @return Returns the idInstitucion_TipoExpediente.
     */
    public String getIdInstitucion_TipoExpediente() {
        return idInstitucion_TipoExpediente;
    }
    /**
     * @param idInstitucion_TipoExpediente The idInstitucion_TipoExpediente to set.
     */
    public void setIdInstitucion_TipoExpediente(
            String idInstitucion_TipoExpediente) {
        this.idInstitucion_TipoExpediente = idInstitucion_TipoExpediente;
    }
    /**
     * @return Returns the idTipoExpediente.
     */
    public String getIdTipoExpediente() {
        return idTipoExpediente;
    }
    /**
     * @param idTipoExpediente The idTipoExpediente to set.
     */
    public void setIdTipoExpediente(String idTipoExpediente) {
        this.idTipoExpediente = idTipoExpediente;
    }
    /**
     * @return Returns the numExpediente.
     */
    public String getNumExpediente() {
        return numExpediente;
    }
    /**
     * @param numExpediente The numExpediente to set.
     */
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }
    
	private String tituloVentana = "";
	
    /**
     * @return Returns the tituloVentana.
     */
    public String getTituloVentana() {
        return tituloVentana;
    }
    /**
     * @param tituloVentana The tituloVentana to set.
     */
    public void setTituloVentana(String tituloVentana) {
        this.tituloVentana = tituloVentana;
    }
	public String getNumColegiado() {
		return numColegiado;
	}
	public void setNumColegiado(String numColegiado) {
		this.numColegiado = numColegiado;
	}
}
