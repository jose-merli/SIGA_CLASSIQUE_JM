/*
 * Created on Mar 15, 2005
 * @author juan.grau
 *
 */
package com.siga.envios.form;

import java.util.StringTokenizer;

import com.siga.beans.EnvEnviosAdm;
import com.siga.general.MasterForm;

/**
 * Formulario para la definición de envios.
 */
public class DefinirEnviosForm extends MasterForm {
	
	

/*	
    //private String modo="";
    private String subModo="";
    //private String modal="";
    private String idEnvio;
	private String nombre="";
	private String tipoFecha = EnvEnviosAdm.FECHA_CREACION;
	private String fechaDesde;
	private String fechaHasta;
	private String idEstado;
	private String idTipoEnvio;
	private String comboTipoEnvio;
	private String comboPlantillaEnvio;
	private String idPlantillaEnvios;
	private String idPlantillaGeneracion;
	
	//para envioModal
	private String fechaProgramada;
	private String idPersona;
	private String idSolicitud;
	private String descEnvio;
	private String editarEnvio="";
	
	//para proceso de Correo Ordinario
	private String idImpresora;
*/	
	
	
	private String tipoFecha = EnvEnviosAdm.FECHA_CREACION;

	public String getComboTipoEnvio() {
        return (String) this.datos.get("comboTipoEnvio");
    }
    public void setComboTipoEnvio(String comboTipoEnvio) {
    	this.datos.put("comboTipoEnvio",comboTipoEnvio);
    	//this.comboTipoEnvio = comboTipoEnvio;
        if (comboTipoEnvio!=null && !comboTipoEnvio.equals("")){
	    	StringTokenizer st = new StringTokenizer(comboTipoEnvio,",");
	    	st.nextToken();//idinstitucion
	    	this.setIdTipoEnvio(st.nextToken());        	
	    }else{        	
	    	this.setIdTipoEnvio("");  
	    }
    }
    public String getIdEstado() {
        return (String) this.datos.get("idEstado");
    }
    public void setIdEstado(String idEstado) {
        try {
        	this.datos.put("idEstado",idEstado);
        	//this.idEstado = idEstado;
        } catch (Exception e) {
    		
    	}
    }
    public String getIdTipoEnvio() {
        return (String) this.datos.get("idTipoEnvio");
    }
    public void setIdTipoEnvio(String idTipoEnvio) {
    	try {
    		this.datos.put("idTipoEnvio",idTipoEnvio);
        	//this.idTipoEnvio = idTipoEnvio;
	    } catch (Exception e) {
			
		}

    }

    public String getNombre() {
        return (String) this.datos.get("nombre");
    }
    public void setNombre(String nombre) {
    	try {
    		this.datos.put("nombre",nombre);
        	//this.nombre = nombre;
	    } catch (Exception e) {
			
		}
        
    }
    public String getFechaDesde() {
        return (String) this.datos.get("fechaDesde");
    }
    public void setFechaDesde(String fechaDesde) {
    	try {
    		this.datos.put("fechaDesde",fechaDesde);
        	//this.fechaDesde = fechaDesde;
	    } catch (Exception e) {
			
		}
    }
    public String getFechaHasta() {
        return (String) this.datos.get("fechaHasta");
    }
    public void setFechaHasta(String fechaHasta) {
    	try {
    		this.datos.put("fechaHasta",fechaHasta);
        	//this.fechaHasta = fechaHasta;
	    } catch (Exception e) {
			
		}
    }
    public String getIdEnvio() {
        return (String) this.datos.get("idEnvio");
    }
    public void setIdEnvio(String idEnvio) {
    	try {
    		this.datos.put("idEnvio",idEnvio);
        	//this.idEnvio = idEnvio;
	    } catch (Exception e) {
				
		}
    }
   
    public String getIdEnvioBuscar() {
        return (String) this.datos.get("idEnvioBuscar");
    }
    public void setIdEnvioBuscar(String idEnvio) {
    	try {
    		this.datos.put("idEnvioBuscar",idEnvio);
        	//this.idEnvio = idEnvio;
	    } catch (Exception e) {
				
		}
    }
   
    public String getTipoFecha() {
        return tipoFecha;
    }
    public void setTipoFecha(String tipoFecha) {
    	try {
    		this.tipoFecha = tipoFecha;
	    } catch (Exception e) {
			
		}
    }
    public String getIdPlantillaEnvios() {
        return (String) this.datos.get("idPlantillaEnvios");
    }
    public void setIdPlantillaEnvios(String idPlantillaEnvios) {
    	try {
    		this.datos.put("idPlantillaEnvios",idPlantillaEnvios);
        	//this.idPlantillaEnvios = idPlantillaEnvios;
	    } catch (Exception e) {
			
		}
    }
    public String getFechaProgramada() {
        return (String) this.datos.get("fechaProgramada");
    }
    public void setFechaProgramada(String fechaProgramada) {
    	try {
    		this.datos.put("fechaProgramada",fechaProgramada);
        	//this.fechaProgramada = fechaProgramada;
	    } catch (Exception e) {
				
		}
    }
    public String getIdPersona() {
        return (String) this.datos.get("idPersona");
    }
    public void setIdPersona(String idPersona) {
    	try {
    		this.datos.put("idPersona",idPersona);
        	//this.idPersona = idPersona;
	    } catch (Exception e) {
			
		}
    }
    public String getIdSolicitud() {
        return (String) this.datos.get("idSolicitud");
    }
    public void setIdSolicitud(String idSolicitud) {
    	try {
    		this.datos.put("idSolicitud",idSolicitud);
        	//this.idSolicitud = idSolicitud;
	    } catch (Exception e) {
			
		}
    }
    public String getComboPlantillaEnvio() {
        return (String) this.datos.get("comboPlantillaEnvio");
    }
    public void setComboPlantillaEnvio(String comboPlantillaEnvio) {
    	this.datos.put("comboPlantillaEnvio",comboPlantillaEnvio);
    	//this.comboPlantillaEnvio = comboPlantillaEnvio;
        if (comboPlantillaEnvio!=null && !comboPlantillaEnvio.equals("")){
	    	StringTokenizer st = new StringTokenizer(comboPlantillaEnvio,",");
	    	this.setIdPlantillaEnvios(st.nextToken());        	
	    }else{        	
	    	this.setIdPlantillaEnvios("");  
	    }
    }
    public String getIdPlantillaGeneracion() {
        return (String) this.datos.get("idPlantillaGeneracion");
    }
    public void setIdPlantillaGeneracion(String idPlantillaGeneracion) {
    	try {
    		this.datos.put("idPlantillaGeneracion",idPlantillaGeneracion);
        	//this.idPlantillaGeneracion = idPlantillaGeneracion;
	    } catch (Exception e) {
				
		}
    }
    public String getDescEnvio() {
        return (String) this.datos.get("descEnvio");
    }
    public void setDescEnvio(String descEnvio) {
    	try {
    		this.datos.put("descEnvio",descEnvio);
        	//this.descEnvio = descEnvio;
	    } catch (Exception e) {
			
		}
    }
    public String getSubModo() {
        return (String) this.datos.get("subModo");
    }
    public void setSubModo(String subModo) {
    	try {
    		this.datos.put("subModo",subModo);
        	//this.subModo = subModo;
	    } catch (Exception e) {
			
		}
    }
    public String getEditarEnvio() {
        return (String) this.datos.get("editarEnvio");
    }
    public void setEditarEnvio(String editarEnvio) {
    	try {
    		this.datos.put("editarEnvio",editarEnvio);
        	//this.editarEnvio = editarEnvio;
	    } catch (Exception e) {
			
		}
    }
    public String getIdImpresora() {
        return (String) this.datos.get("idImpresora");
    }
    public void setIdImpresora(String idImpresora) {
    	try {
    		this.datos.put("idImpresora",idImpresora);
        	//this.idImpresora = idImpresora;
	    } catch (Exception e) {
			
		}
    }
    public String getIdFactura() {
        return (String) this.datos.get("idFactura");
    }
    public void setIdFactura(String idFactura) {
    	try {
    		this.datos.put("idFactura",idFactura);
	    } catch (Exception e) {
			
		}
    }
    public String getIdsParaEnviar() {
        return (String) this.datos.get("idsParaEnviar");
    }
    public void setIdsParaEnviar(String idsParaEnviar) {
    	try {
    		this.datos.put("idsParaEnviar",idsParaEnviar);
	    } catch (Exception e) {
			
		}
    }
    public String getColegio() {
        return (String) this.datos.get("colegio");
    }
    public void setColegio(String colegio) {
    	try {
    		this.datos.put("colegio",colegio);
	    } catch (Exception e) {
			
		}
    }
    public String getDatosEnvios() {
		return (String) this.datos.get("datosEnvios");
	}
	public void setDatosEnvios(String datosEnvios) {
		this.datos.put("datosEnvios",datosEnvios);
	}/*
	public String getDatosMorosos() {
		return (String) this.datos.get("datosMorosos");
	}
	public void setDatosMorosos(String datosMorosos) {
		this.datos.put("datosMorosos",datosMorosos);
	}*/
	
	public String getDescargar() {
		return (String) this.datos.get("descargar");
	}
	public void setDescargar(String descargar) {
		this.datos.put("descargar",descargar);
	}
	public String getClavesIteracion() {
		return (String) this.datos.get("clavesIteracion");
	}
	public void setClavesIteracion(String clavesIteracion) {
		this.datos.put("clavesIteracion",clavesIteracion);
	}
	
}
