package com.siga.gratuita.form;


import java.util.Hashtable;
import com.siga.general.MasterForm;


/**
 * Formulario para la administración de los Hitos Facturables de las Guardias
 * 
 * @since 04-01-2005
 * @author ruben.fernandez
 * @version adrian.ayala - 02-07-2008: Limpieza general 
 */
public class DefinirHitosFacturablesGuardiasForm extends MasterForm
{
	//////////////////// ATRIBUTOS ////////////////////
	protected Hashtable mapHitoPrecio = null;
	protected String radioA="0";
	protected boolean checkB1=false;
	protected boolean checkB2=false;
	protected boolean chAsist=false;
	protected boolean chGuardias=false;
	protected boolean chNoGuardias=false;
	protected boolean chMinAsist=false;
	protected boolean chActuacion=false;
	protected boolean chMinAct=false;
	protected boolean chActAsist=false;
	protected boolean chGDoblada=false;
	protected boolean chFGuardia=false;
	protected boolean chActFG=false;
	protected boolean checkC=false;
	protected boolean chPagaGuardiaLunes=false;
	protected boolean chPagaGuardiaMartes=false;
	protected boolean chPagaGuardiaMiercoles=false;
	protected boolean chPagaGuardiaJueves=false;
	protected boolean chPagaGuardiaViernes=false;
	protected boolean chPagaGuardiaSabado=false;
	protected boolean chPagaGuardiaDomingo=false;
	protected boolean chNoPagaGuardiaLunes=false;
	protected boolean chNoPagaGuardiaMartes=false;
	protected boolean chNoPagaGuardiaMiercoles=false;
	protected boolean chNoPagaGuardiaJueves=false;
	protected boolean chNoPagaGuardiaViernes=false;
	protected boolean chNoPagaGuardiaSabado=false;
	protected boolean chNoPagaGuardiaDomingo=false;
	protected boolean chPagaGuardiaPorDia=false;
	protected boolean chNoPagaGuardiaPorDia=false;
	
	
	
	//////////////////// GETTERS ////////////////////
	public Object getHitoPrecio	(int key) {
		if (this.mapHitoPrecio == null)
			return "";
		else {
			String precio = (String) this.mapHitoPrecio.get (Integer.toString (key));
			if (precio == null)
				return new String ("0");
			else if (precio.equals (""))
				return new String ("0");
			else
				return precio;
		}
	}
	public Hashtable getHitoPrecioHt			() {return this.mapHitoPrecio;}
	public String getRadioA						() {return radioA;}
	public boolean getCheckB1					() {return checkB1;}
	public boolean getCheckB2					() {return checkB2;}
	public boolean getChAsist					() {return chAsist;}
	public boolean getChGuardias				() {return chGuardias;}
	public boolean getChNoGuardias				() {return chNoGuardias;}
	public boolean getChMinAsist				() {return chMinAsist;}
	public boolean getChActuacion				() {return chActuacion;}
	public boolean getChMinActuacion			() {return chMinAct;}
	public boolean getChActAsist				() {return chActAsist;}
	public boolean getChGDoblada				() {return chGDoblada;}
	public boolean getChFGuardia				() {return chFGuardia;}
	public boolean getChActFG					() {return chActFG;}
	public boolean getCheckC					() {return checkC;}
	public boolean getChPagaGuardiaLunes		() {return chPagaGuardiaLunes;}
	public boolean getChPagaGuardiaMartes		() {return chPagaGuardiaMartes;}
	public boolean getChPagaGuardiaMiercoles	() {return chPagaGuardiaMiercoles;}
	public boolean getChPagaGuardiaJueves		() {return chPagaGuardiaJueves;}
	public boolean getChPagaGuardiaViernes		() {return chPagaGuardiaViernes;}
	public boolean getChPagaGuardiaSabado		() {return chPagaGuardiaSabado;}
	public boolean getChPagaGuardiaDomingo		() {return chPagaGuardiaDomingo;}
	public boolean getChNoPagaGuardiaLunes		() {return chNoPagaGuardiaLunes;}
	public boolean getChNoPagaGuardiaMartes		() {return chNoPagaGuardiaMartes;}
	public boolean getChNoPagaGuardiaMiercoles	() {return chNoPagaGuardiaMiercoles;}
	public boolean getChNoPagaGuardiaJueves		() {return chNoPagaGuardiaJueves;}
	public boolean getChNoPagaGuardiaViernes	() {return chNoPagaGuardiaViernes;}
	public boolean getChNoPagaGuardiaSabado		() {return chNoPagaGuardiaSabado;}
	public boolean getChNoPagaGuardiaDomingo	() {return chNoPagaGuardiaDomingo;}
	public boolean getChPagaGuardiaPorDia		() {return chPagaGuardiaPorDia;}
	public boolean getChNoPagaGuardiaPorDia		() {return chNoPagaGuardiaPorDia;}
	
	public String getDescripcion		() {return (String) this.datos.get("IDHITO");}
		//corresponde con el idHito del Hito seleccionado en el combo
	public String getFechaModificacion	() {return (String) this.datos.get("FECHAMODIFICACION");}
	public String getModal				() {return (String) this.datos.get("MODAL");}
	public String getPrecio				() {return (String) this.datos.get("PRECIOHITO");}
	public String getPuntos				() {return (String) this.datos.get("PUNTOS");}
	public String getUsuModificacion	() {return (String) this.datos.get("USUMODIFICACION");}
	
	
	
	//////////////////// SETTERS ////////////////////
	public void setHitoPrecio (int key, Object value) {
		if (this.mapHitoPrecio==null)
			this.mapHitoPrecio = new Hashtable ();
		this.mapHitoPrecio.put(Integer.toString(key), value);
	}
	public void setHitoPrecioHt				(Hashtable ht) {this.mapHitoPrecio=ht;}
	public void setRadioA					(String valor) {radioA=valor;}
	public void setCheckB1					(boolean valor) {checkB1=valor;}
	public void setCheckB2					(boolean valor) {checkB2=valor;}
	public void setChAsist					(boolean valor) {chAsist=valor;}
	public void setChGuardias				(boolean valor) {chGuardias=valor;}
	public void setChNoGuardias				(boolean valor) {chNoGuardias=valor;}
	public void setChMinAsist				(boolean valor) {chMinAsist=valor;}
	public void setChActuacion				(boolean valor) {chActuacion=valor;}
	public void setChMinActuacion			(boolean valor) {chMinAct=valor;}
	public void setChActAsist				(boolean valor) {chActAsist=valor;}
	public void setChGDoblada				(boolean valor) {chGDoblada=valor;}
	public void setChFGuardia				(boolean valor) {chFGuardia=valor;}
	public void setChActFG					(boolean valor) {chActFG=valor;}
	public void setCheckC					(boolean valor) {checkC=valor;}
	public void setChPagaGuardiaLunes		(boolean valor) {chPagaGuardiaLunes=valor;}
	public void setChPagaGuardiaMartes		(boolean valor) {chPagaGuardiaMartes=valor;}
	public void setChPagaGuardiaMiercoles	(boolean valor) {chPagaGuardiaMiercoles=valor;}
	public void setChPagaGuardiaJueves		(boolean valor) {chPagaGuardiaJueves=valor;}
	public void setChPagaGuardiaViernes		(boolean valor) {chPagaGuardiaViernes=valor;}
	public void setChPagaGuardiaSabado		(boolean valor) {chPagaGuardiaSabado=valor;}
	public void setChPagaGuardiaDomingo		(boolean valor) {chPagaGuardiaDomingo=valor;}
	public void setChNoPagaGuardiaLunes		(boolean valor) {chNoPagaGuardiaLunes=valor;}
	public void setChNoPagaGuardiaMartes	(boolean valor) {chNoPagaGuardiaMartes=valor;}
	public void setChNoPagaGuardiaMiercoles	(boolean valor) {chNoPagaGuardiaMiercoles=valor;}
	public void setChNoPagaGuardiaJueves	(boolean valor) {chNoPagaGuardiaJueves=valor;}
	public void setChNoPagaGuardiaViernes	(boolean valor) {chNoPagaGuardiaViernes=valor;}
	public void setChNoPagaGuardiaSabado	(boolean valor) {chNoPagaGuardiaSabado=valor;}
	public void setChNoPagaGuardiaDomingo	(boolean valor) {chNoPagaGuardiaDomingo=valor;}
	public void setChPagaGuardiaPorDia		(boolean valor) {chPagaGuardiaPorDia=valor;}
	public void setChNoPagaGuardiaPorDia	(boolean valor) {chNoPagaGuardiaPorDia=valor;}
	
	public void setDescripcion			(String descripcion) {this.datos.put ("IDHITO", descripcion);}
	public void setFechaModificacion	(String fechaModificacion) {this.datos.put ("FECHAMODIFICACION", fechaModificacion);}
	public void setModal				(String modal) {this.datos.put ("MODAL", modal);}
	public void setPrecio				(String precio) {this.datos.put ("PRECIOHITO", precio);}
	public void setPuntos				(String puntos) {this.datos.put ("PUNTOS", puntos);}
	public void setUsuModificacion		(String usuModificacion) {this.datos.put ("USUMODIFICACION", usuModificacion);}
}