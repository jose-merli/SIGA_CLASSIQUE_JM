package com.siga.comun.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesListas;
import com.siga.Utilidades.UtilidadesString;
import com.siga.comun.form.BaseForm;

public class SessionForms {


	private static String DISPLAYTAGDATA = "d-.*-.*";
	private static String IGUAL = "=";
	private static String ET = "&";
	private static String FORMS = "FORMS_BACKUP";
	private static String KEY_SEPARATOR = "#";
	private static List<String> ACCIONES_INICIO_LIST = UtilidadesListas.add(new ArrayList<String>(), "inicio",null);
	private static List<String> EXCLUDED_FIELDS = UtilidadesListas.add(new ArrayList<String>(), "id","modo","accion","deleteForm","backupForm",null);


	public static void check (HttpServletRequest request, BaseForm form) throws ClsExceptions{

		String action = request.getServletPath();
		String accion = form.getAccion();
		if(accion==null || "".equals(accion))
			accion = form.getModo(); 
		//		Boolean deleteForm = UtilidadesString.stringToBoolean(form.getDeleteForm());
		Boolean backupForm = UtilidadesString.stringToBoolean(form.getBackupForm());
		HttpSession session = request.getSession();

		//Se guardan los datos si se ha especificado guardar la operacion 
		//o si se esta ordenando o paginando un displayTag
		if (backupForm || checkDisplaytag(request)){
			backupForm(session, action, accion, request.getParameterMap());
		}
		//Si es una accion de inicio se eliminan los formularios asociados a su action
		if (isInit(accion)){
			deleteForm(session, action);
		}
	}


	/**
	 * Comprueba si hay parametros de ordenacion o paginacion en la request
	 * @param request
	 * @return <code>true</code> si hay parametros de ordenacion o paginacion en la request
	 * <code>false</code> en caso contrario.
	 */
	private static boolean checkDisplaytag(HttpServletRequest request) {
		Iterator iter = request.getParameterMap().entrySet().iterator();
		for (;iter.hasNext();){
			Entry entry = (Entry) iter.next();
			if (entry.getKey().toString().matches(DISPLAYTAGDATA))
				return true;
		}
		return false;
	}


	/**
	 * Elimina la informacion de los formularios con clave <code>getKey(action,accion)</code>
	 * @param session
	 * @param action
	 * @param accion
	 */
	@SuppressWarnings("unchecked")
	private static void deleteForm(HttpSession session, String action, String accion) {
		Map<String,String> forms = (Map<String,String>) session.getAttribute(FORMS);
		if (forms!=null){
			forms.remove(getKey(action,accion));
		}
	}

	/**
	 * Elimina la informacion de los formularios almacenados en session si su <code>key</code> 
	 * contiene el nombre del Action <code>action</code>
	 * @param session
	 * @param action
	 */
	@SuppressWarnings("unchecked")
	private static void deleteForm(HttpSession session, String action) {
		Map<String,String> forms = (Map<String, String>) session.getAttribute(FORMS);
		if (forms!=null){
			for (Iterator iter = forms.entrySet().iterator(); iter.hasNext();){
				Entry entry = (Entry) iter.next();
				String key = (String) entry.getKey();
				if (key.matches(action+".*")){
					iter.remove();
				}
			}
		}
	}
	private static void setForms(HttpSession session, Map<String,String> forms) {
		session.setAttribute(FORMS,forms);
	}

	@SuppressWarnings("unchecked")
	private static Map<String,String> getForms(HttpSession session) {
		Map<String,String> forms = (Map<String,String>) session.getAttribute(FORMS);
		if (forms == null){
			forms = new HashMap<String,String>();
		}
		return forms;
	}

	public static String getForm(HttpServletRequest request,String action, String accion){
		if (existsForm(request.getSession(), action, accion))
			return getForms(request.getSession()).get(getKey(action, accion));
		return "";
	}

	@SuppressWarnings("unchecked")
	private static void backupForm(HttpSession session, String action, String accion, Map parameterMap) {

		StringBuffer backup = new StringBuffer();
		Iterator iter = parameterMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry n = (Entry)iter.next();
			String key = n.getKey().toString();
			String values[] = (String[]) n.getValue();
			String value = values[0].toString();
			if (!EXCLUDED_FIELDS.contains(key) && value != null){
				backup.append(key);
				backup.append(IGUAL);
				backup.append(value);
				backup.append(ET);
			}
		}
		Map<String,String> forms  = getForms(session);
		forms.put(getKey(action,accion),backup.toString());
		setForms(session, forms);
	}

	private static boolean isInit(String accion) {
		return ACCIONES_INICIO_LIST.contains(accion);
	}

	@SuppressWarnings("unchecked")
	private static boolean existsForm(HttpSession session, String action, String accion) {
		Map<String,String> forms = (Map<String,String>) session.getAttribute(FORMS);
		return forms.containsKey(getKey(action,accion));
	}

	private static String getKey(String action, String accion) {
		return action + KEY_SEPARATOR + accion;
	}


}
