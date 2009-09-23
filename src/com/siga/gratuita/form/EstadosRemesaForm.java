// VERSIONES
//David Sanchez Pina
// 01/02/2007

package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CajgRemesaBean;
import com.siga.beans.CajgRemesaEstadosBean;
import com.siga.beans.CajgTipoEstadoRemesaBean;

import com.siga.general.MasterForm;

/**
 * Clase action form del caso de uso GRUPOS CLIENTES CLIENTES
 * @author AtosOrigin 01/02/2007
 */
 public class EstadosRemesaForm extends MasterForm {
 	
	public void setIdInstitucion (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"IDINSTITUCION", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setIdRemesa (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CajgRemesaBean.C_IDREMESA, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public void setFechaEstado (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CajgRemesaEstadosBean.C_FECHAREMESA, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public void setIdEstado (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CajgRemesaEstadosBean.C_IDESTADO, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public void setEstado (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,CajgTipoEstadoRemesaBean.C_DESCRIPCION, dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public String getFechaEstado	() 	{ 
 		return UtilidadesHash.getString(this.datos, CajgRemesaEstadosBean.C_FECHAREMESA);		
 	}
 	public String getIdEstado	() 	{ 
 		return UtilidadesHash.getString(this.datos, CajgRemesaEstadosBean.C_IDESTADO);		
 	}
 	public String getEstado	() 	{ 
 		return UtilidadesHash.getString(this.datos, CajgTipoEstadoRemesaBean.C_DESCRIPCION);		
 	}

 	
 	public String getIdInstitucion	() 	{ 
 		return UtilidadesHash.getString(this.datos, "IDINSTITUCION");		
 	}

 	public String getIdRemesa	() 	{ 
 		return UtilidadesHash.getString(this.datos, CajgRemesaBean.C_IDREMESA);		
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