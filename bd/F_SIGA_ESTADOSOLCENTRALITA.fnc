CREATE OR REPLACE FUNCTION F_SIGA_ESTADOSOLCENTRALITA(P_IDINSTITUCION IN SCS_SOLICITUD_ACEPTADA.IDINSTITUCION%TYPE,
                                                            P_IDSOLICITUD   IN SCS_SOLICITUD_ACEPTADA.IDSOLICITUD%TYPE,
                                                            P_DESCRIPCIONCONFIRMADA IN GEN_RECURSOS.DESCRIPCION%TYPE)
  RETURN VARCHAR2 IS

  /****************************************************************************************************************/
  /* NOMBRE:        F_SIGA_ESTADOSOLICITUDCENTRALITA                                                                         */
  /* DESCRIPCION:   FUNCION QUE NOS SACA LA DESCRIPCION DEL ESTADO DE UNA SOLICITUD CONFIRMADA DE CENTRALITA. CONCATENARA LAS ASISTENCIAS CREADAS DESDE ESTA SOLICITUD        */
  /****************************************************************************************************************/

  /* DECLARACION DE VARIABLES */

  V_SALIDA VARCHAR2(250);

  CURSOR C_ASISTENCIAS IS
    SELECT (ANIO || '/' || NUMERO) ASISTENCIA
      FROM SCS_ASISTENCIA
     WHERE IDINSTITUCION = P_IDINSTITUCION
       AND IDSOLICITUDCENTRALITA = P_IDSOLICITUD
     ORDER BY ANIO DESC,NUMERO DESC;

BEGIN

  FOR I IN C_ASISTENCIAS LOOP
    V_SALIDA := V_SALIDA || I.ASISTENCIA || ', ';
  END LOOP;
  V_SALIDA := P_DESCRIPCIONCONFIRMADA||' ('||V_SALIDA;
  V_SALIDA := RTRIM(TRIM(V_SALIDA), ',');
  V_SALIDA := V_SALIDA||')';

  RETURN(V_SALIDA);
END F_SIGA_ESTADOSOLCENTRALITA;


 
/
