create or replace package PKG_SERVICIOS_AUTOMATICOS is

  /****************************************************************************************************************/
  /* Nombre: PROCESO_BAJA_SERVICIO
    Descripcion:
        1. Las solicitudes de servicios pasan al estado denegado.
        2. Las peticiones de baja de servicios se procesan
        3. Las peticiones que no tengan algo pendiente se procesan
        4. Las suscripciones pasan a tener la baja logica

    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER(4)
    - P_IDTIPOSERVICIOS - IN - Identificador del tipo de servicio - NUMBER(4)
    - P_IDSERVICIO - IN - Identificador del servicio - NUMBER(10)
    - P_IDSERVICIOSINSTITUCION - IN - Identificador del servicio institucion - NUMBER(10)
    - P_FECHAPROCESO - IN - Fecha en la que se va a realizar la baja del servicio - VARCHAR2(10)
    - P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER(5)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Version: 1.0 - 31/01/2006 - Pilar Duran
    Version: 2.0 - 14/09/2015 - Jorge Paez
        - Version SIGA_122         
  /****************************************************************************************************************/
    PROCEDURE PROCESO_BAJA_SERVICIO(
        P_IDINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDINSTITUCION%TYPE,
        P_IDTIPOSERVICIOS IN PYS_SERVICIOSSOLICITADOS.IDTIPOSERVICIOS%TYPE,
        P_IDSERVICIO IN PYS_SERVICIOSSOLICITADOS.IDSERVICIO%TYPE,
        P_IDSERVICIOSINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDSERVICIOSINSTITUCION%TYPE,
        P_FECHAPROCESO IN VARCHAR2,
        P_USUMODIFICACION IN PYS_SERVICIOSSOLICITADOS.USUMODIFICACION%TYPE,
        P_CODRETORNO OUT  VARCHAR2,
        P_DATOSERROR OUT  VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre: PROCESO_SUSCRIPCION_AUTO
    Descripcion: Proceso que se encarga de gestionar la suscripcion automatica a un servicio

    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER(4)
    - P_IDTIPOSERVICIOS - IN - Identificador del tipo de servicio - NUMBER(4)
    - P_IDSERVICIO - IN - Identificador del servicio - NUMBER(10)
    - P_IDSERVICIOSINSTITUCION - IN - Identificador del servicio institucion - NUMBER(10)
    - P_FECHAPROCESO - IN - Fecha en la que se va a realizar la suscripcion automatica - DATE
    - P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER(5)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Version: 1.0 - 31/01/2006 - Pilar Duran
    Version: 2.0 - 14/09/2015  - Jorge Paez
        - Version SIGA_122         
  /****************************************************************************************************************/    
  PROCEDURE PROCESO_SUSCRIPCION_AUTO(
        P_IDINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDINSTITUCION%TYPE,
        P_IDTIPOSERVICIOS IN PYS_SERVICIOSSOLICITADOS.IDTIPOSERVICIOS%TYPE,
        P_IDSERVICIO IN PYS_SERVICIOSSOLICITADOS.IDSERVICIO%TYPE,
        P_IDSERVICIOSINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDSERVICIOSINSTITUCION%TYPE,
        P_FECHAPROCESO IN PYS_SERVICIOSSOLICITADOS.FECHAMODIFICACION%TYPE,
        P_USUMODIFICACION IN PYS_SERVICIOSSOLICITADOS.USUMODIFICACION%TYPE,
        P_CODRETORNO OUT  VARCHAR2,
        P_DATOSERROR OUT  VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre: PROCESO_REVISION_LETRADO
    Descripcion: Dado un cliente y fecha, revisa las suscripciones automaticas y da de alta o baja segun corresponda

    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER(4)
    - P_IDPERSONA - IN - Identificador de la persona suscrita al servicio - NUMBER(10)
    - P_FECHAPROCESO - IN - Fecha para la revision de las suscripciones - DATE
    - P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER(5)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Version: 1.0 - 02/01/2006 - Pilar Duran
    Version: 2.0 - 08/09/2014 - Adrian Ayala
        - Para los servicios automaticos dados de baja se estaban creando peticiones de compra vacias
        - Revision general del metodo     
    Version: 3.0 - 14/09/2015 - Jorge Paez
        - Version SIGA_122         
  /****************************************************************************************************************/   
    Procedure PROCESO_REVISION_LETRADO(
        P_IDINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDINSTITUCION%TYPE,
        P_IDPERSONA IN PYS_SERVICIOSSOLICITADOS.IDPERSONA%TYPE,
        P_FECHAPROCESO IN CEN_DATOSCOLEGIALESESTADO.FECHAESTADO%TYPE,
        P_USUMODIFICACION IN PYS_SERVICIOSSOLICITADOS.USUMODIFICACION%TYPE,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2);

    
  /****************************************************************************************************************/
  /* Nombre: PROCESO_ACT_CUENTA_BANCO_PEND
    Descripcion: Este proceso se encarga de actualizar las cosas pendientes asociadas a la cuenta de baja de una persona

    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER(4)
    - P_IDPERSONA - IN - Identificador de la persona suscrita al servicio - NUMBER(10)
    - P_IDCUENTA - IN - Identificador de la cuenta bancaria dada de baja - DATE
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Version: 1.0 - 10/03/2014 - Maria Jimenez     
    Version: 2.0 - 08/08/2014 - Jorge Paez
        - Version SIGA_122         
  /****************************************************************************************************************/        
    PROCEDURE PROCESO_ACT_CUENTA_BANCO_PEND (
        p_Idinstitucion IN PYS_SERVICIOSSOLICITADOS.IDINSTITUCION%TYPE,
        p_Idpersona IN PYS_SERVICIOSSOLICITADOS.IDPERSONA%TYPE,
        p_Idcuenta IN PYS_SERVICIOSSOLICITADOS.IDCUENTA%TYPE,
        p_Usumodificacion IN PYS_SERVICIOSSOLICITADOS.USUMODIFICACION%TYPE,
        p_Codretorno OUT VARCHAR2,
        p_Datoserror OUT VARCHAR2);

  /****************************************************************************************************************/
  /* Nombre: PROCESO_ELIMINAR_SUSCRIPCION
    Descripcion: Eliminar fisicamente las suscripciones y solicitudes de compra a un servicio que no se encuentren en ningun ciclo de facturacion, ya sea GENERADO, PENDIENTE CONFIRMAR o CONFIRMADO.

    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER(4)
    - P_IDTIPOSERVICIOS - IN - Identificador del tipo de servicio - NUMBER(4)
    - P_IDSERVICIO - IN - Identificador del servicio - NUMBER(10)
    - P_IDSERVICIOSINSTITUCION - IN - Identificador del servicio institucion - NUMBER(10)
    - P_FECHAPROCESO - IN - Fecha en la que se va a realizar la baja del servicio - VARCHAR2(10)
    - P_ALTA - IN - Indica los tipos de solicitudes a borrar - VARCHAR2(1)
    - P_FECHAALTA - IN - Fecha de alta en formato DD/MM/YYYY - VARCHAR2(10)
    - P_INCLUIRMANUALES - IN - Incluir servicios manuales - VARCHAR2(1)
    - P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER(5)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el mensaje de error Oracle correspondiente.
    - P_BORRADOSUSCRIPCIONES - OUT - Devuelve el numero de suscripciones borradas - NUMBER  

    Version: 1.0 - 06/02/2007 - Pilar Duran
    Version: 2.0 - 14/09/2015 - Jorge Paez
        - Version SIGA_122         
  /****************************************************************************************************************/  
    PROCEDURE PROCESO_ELIMINAR_SUSCRIPCION(
        P_IDINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDINSTITUCION%TYPE,
        P_IDTIPOSERVICIOS IN PYS_SERVICIOSSOLICITADOS.IDTIPOSERVICIOS%TYPE,
        P_IDSERVICIO IN PYS_SERVICIOSSOLICITADOS.IDSERVICIO%TYPE,
        P_IDSERVICIOSINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDSERVICIOSINSTITUCION%TYPE,
        P_ALTA IN VARCHAR2,
        P_FECHAALTA IN VARCHAR2,
        P_INCLUIRMANUALES IN VARCHAR2,
        P_USUMODIFICACION IN PYS_SERVICIOSSOLICITADOS.USUMODIFICACION%TYPE,        
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2,
        P_BORRADOSUSCRIPCIONES OUT VARCHAR2);

    /****************************************************************************************************************/
  /* Nombre: PROCESO_REVISION_AUTO
    Descripcion: Proceso que se encarga de dar de alta o de baja en todos los servicios a todos los clientes de un colegio dependiendo de si cumple o no los criterios de cada servicio

    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER(4)
    - P_FECHAEFECTIVA - IN - Fecha en la que se va a realizar la baja del servicio - VARCHAR2(10)
    - P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER(5)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el mensaje de error Oracle correspondiente.  

    Version: 1.0 - 14/01/2009 - Pilar Duran
    Version: 2.0 - 04/11/2014 - Adrian Ayala
        - Reformateado hasta la ejecucion del primer cursor dinamico
        - Arreglo de R1410_0070: se estaban insertando suscripciones duplicadas cuando se daba de baja a futuro.
        - La razon era que solo se comprobaba que existiera suscripcion sin fecha de baja. Lo que faltaba era comprobar tambien que existiera suscripcion con fecha de baja pero en el futuro.
    Version: 2.0 - 14/09/2015 - Jorge Paez
        - Version SIGA_122         
  /****************************************************************************************************************/  
    PROCEDURE PROCESO_REVISION_AUTO (
        P_IDINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDINSTITUCION%TYPE,
        P_FECHAEFECTIVA IN PYS_SERVICIOSSOLICITADOS.FECHAMODIFICACION%TYPE,
        P_USUMODIFICACION IN PYS_SERVICIOSSOLICITADOS.USUMODIFICACION%TYPE,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2);

END PKG_SERVICIOS_AUTOMATICOS;
/
CREATE OR REPLACE PACKAGE BODY PKG_SERVICIOS_AUTOMATICOS IS

  /****************************************************************************************************************/
  /* Nombre: PROCESO_BAJA_SERVICIO
    Descripcion:
        1. Las solicitudes de servicios pasan al estado denegado.
        2. Las peticiones de baja de servicios se procesan
        3. Las peticiones que no tengan algo pendiente se procesan
        4. Las suscripciones pasan a tener la baja logica

    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER(4)
    - P_IDTIPOSERVICIOS - IN - Identificador del tipo de servicio - NUMBER(4)
    - P_IDSERVICIO - IN - Identificador del servicio - NUMBER(10)
    - P_IDSERVICIOSINSTITUCION - IN - Identificador del servicio institucion - NUMBER(10)
    - P_FECHAPROCESO - IN - Fecha en la que se va a realizar la baja del servicio - VARCHAR2(10)
    - P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER(5)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Version: 1.0 - 31/01/2006 - Pilar Duran
    Version: 2.0 - 14/09/2015 - Jorge Paez
        - Version SIGA_122         
  /****************************************************************************************************************/
    PROCEDURE PROCESO_BAJA_SERVICIO(
        P_IDINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDINSTITUCION%TYPE,
        P_IDTIPOSERVICIOS IN PYS_SERVICIOSSOLICITADOS.IDTIPOSERVICIOS%TYPE,
        P_IDSERVICIO IN PYS_SERVICIOSSOLICITADOS.IDSERVICIO%TYPE,
        P_IDSERVICIOSINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDSERVICIOSINSTITUCION%TYPE,
        P_FECHAPROCESO IN VARCHAR2,
        P_USUMODIFICACION IN PYS_SERVICIOSSOLICITADOS.USUMODIFICACION%TYPE,
        P_CODRETORNO OUT  VARCHAR2,
        P_DATOSERROR OUT  VARCHAR2) IS

        V_CONTADOR NUMBER;
        V_FECHAEFECTIVA DATE;
        V_PROCEDIMIENTO CONSTANT VARCHAR2(40) := 'PROCESO_BAJA_SERVICIO';
        V_TEXTOERROR VARCHAR2(4000) := NULL;

        CURSOR C_BAJA (VC_IDINSTITUCION NUMBER, VC_IDTIPOSERVICIOS NUMBER, VC_IDSERVICIO NUMBER, VC_IDSERVICIOSINSTITUCION NUMBER) IS
            SELECT PP.IDPETICION,
                PP.TIPOPETICION
            FROM PYS_SERVICIOSSOLICITADOS SS, 
                PYS_PETICIONCOMPRASUSCRIPCION PP
            WHERE SS.IDPETICION = PP.IDPETICION
               AND SS.IDINSTITUCION = PP.IDINSTITUCION
               AND SS.IDINSTITUCION = VC_IDINSTITUCION
               AND SS.IDTIPOSERVICIOS = VC_IDTIPOSERVICIOS
               AND SS.IDSERVICIO = VC_IDSERVICIO
               AND SS.IDSERVICIOSINSTITUCION = VC_IDSERVICIOSINSTITUCION;

    BEGIN   
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 1 - Lanza el cursor';
        FOR DatosBaja IN C_BAJA(P_IDINSTITUCION, P_IDTIPOSERVICIOS, P_IDSERVICIO, P_IDSERVICIOSINSTITUCION) LOOP

            -- Actualizamos las solicitudes a estado = 'B'
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 2 - Denegar la solicitud la peticion ' || DatosBaja.IDPETICION;
            UPDATE PYS_SERVICIOSSOLICITADOS  
            SET ACEPTADO = 'B', -- Denegada
                USUMODIFICACION = P_USUMODIFICACION,
                FECHAMODIFICACION = SYSDATE
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                AND IDSERVICIO = P_IDSERVICIO
                AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                AND IDPETICION = DatosBaja.IDPETICION;

            -- Si la peticion es de baja se pone como procesada.
            IF DatosBaja.TIPOPETICION = 'B' THEN
                V_CONTADOR := 0;

            ELSE
                -- Si no es de baja hay que comprobar si todos los servicios solicitados que la componen estan aceptados para poner el estado como procesada.
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 3 - Consulta datos pendientes de la solicitud de la peticion ' || DatosBaja.IDPETICION;
                SELECT COUNT(*)
                    INTO V_CONTADOR
                FROM PYS_PETICIONCOMPRASUSCRIPCION PP
                WHERE PP.IDINSTITUCION = P_IDINSTITUCION
                    AND PP.IDPETICION = DatosBaja.IDPETICION                
                    AND EXISTS (
                        SELECT 1
                        FROM PYS_SERVICIOSSOLICITADOS SS
                        WHERE SS.IDINSTITUCION = PP.IDINSTITUCION
                            AND SS.IDPETICION = PP.IDPETICION
                            AND SS.ACEPTADO = 'P' -- Pendiente
                        UNION 
                        SELECT 1
                        FROM PYS_PRODUCTOSSOLICITADOS PS
                        WHERE PS.IDINSTITUCION = PP.IDINSTITUCION
                            AND PS.IDPETICION = PP.IDPETICION
                            AND PS.ACEPTADO = 'P' -- Pendiente                            
                    );                        
            END IF;
            
            IF (V_CONTADOR = 0) THEN
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 4 - Procesar la peticion ' || DatosBaja.IDPETICION;
                UPDATE PYS_PETICIONCOMPRASUSCRIPCION 
                SET IDESTADOPETICION = 20, -- PROCESADA
                    USUMODIFICACION = P_USUMODIFICACION,
                    FECHAMODIFICACION = SYSDATE
                WHERE IDINSTITUCION = P_IDINSTITUCION
                    AND IDPETICION = DatosBaja.IDPETICION;                            
            END IF;              
        END LOOP;
                
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 5 - Convierte la fecha efectiva ' || P_FECHAPROCESO;
        V_FECHAEFECTIVA := TO_DATE(P_FECHAPROCESO, 'DD/MM/YYYY');

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 6 - Actualiza las fechas de baja logica del servicio';
        UPDATE PYS_SUSCRIPCION 
        SET FECHABAJA = CASE WHEN(V_FECHAEFECTIVA < FECHASUSCRIPCION) THEN FECHASUSCRIPCION ELSE V_FECHAEFECTIVA END,
            FECHABAJAFACTURACION = CASE WHEN(V_FECHAEFECTIVA < FECHASUSCRIPCION) THEN FECHASUSCRIPCION ELSE V_FECHAEFECTIVA END,
            USUMODIFICACION = P_USUMODIFICACION,
            FECHAMODIFICACION = SYSDATE
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
            AND IDSERVICIO = P_IDSERVICIO
            AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
            AND FECHABAJA IS NULL;

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 7 - Finaliza el proceso correctamente';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_TEXTOERROR;
        
        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM || ' - ' || V_TEXTOERROR;
    END PROCESO_BAJA_SERVICIO;
       
  /****************************************************************************************************************/
  /* Nombre: PROCESO_BAJA_SERVICIO_PERSONA
    Descripcion:
        1. Las solicitudes de servicios pasan al estado denegado.
        2. Las peticiones de baja de servicios se procesan
        3. Las peticiones que no tengan algo pendiente se procesan
        4. Las suscripciones pasan a tener la baja logica

    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER(4)
    - P_IDTIPOSERVICIOS - IN - Identificador del tipo de servicio - NUMBER(4)
    - P_IDSERVICIO - IN - Identificador del servicio - NUMBER(10)
    - P_IDSERVICIOSINSTITUCION - IN - Identificador del servicio institucion - NUMBER(10)
    - P_IDPERSONA - IN - Identificador de la persona suscrita al servicio - NUMBER(10)
    - P_FECHAPROCESO - IN - Fecha en la que se va a realizar la baja del servicio - DATE
    - P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER(5)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Version: 1.0 - 15/03/2007 - Pilar Duran
    Version: 2.0 - 14/09/2015 - Jorge Paez
        - Version SIGA_122         
  /****************************************************************************************************************/    
    PROCEDURE BAJA_SERVICIO_PERSONA(
        P_IDINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDINSTITUCION%TYPE,
        P_IDTIPOSERVICIOS IN PYS_SERVICIOSSOLICITADOS.IDTIPOSERVICIOS%TYPE,
        P_IDSERVICIO IN PYS_SERVICIOSSOLICITADOS.IDSERVICIO%TYPE,
        P_IDSERVICIOSINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDSERVICIOSINSTITUCION%TYPE,
        P_IDPERSONA IN PYS_SERVICIOSSOLICITADOS.IDPERSONA%TYPE,
        P_FECHAPROCESO IN DATE,
        P_USUMODIFICACION IN PYS_SERVICIOSSOLICITADOS.USUMODIFICACION%TYPE,
        P_CODRETORNO OUT  VARCHAR2,
        P_DATOSERROR OUT  VARCHAR2) IS

        V_CONTADOR NUMBER;
        V_PROCEDIMIENTO CONSTANT VARCHAR2(40) := 'BAJA_SERVICIO_PERSONA';
        V_TEXTOERROR VARCHAR2(4000) := NULL;
        P_FECHAPROCESO_FINAL DATE := P_FECHAPROCESO - 1; -- Se le resta un dia porque cuando se da de baja algun servicio automaticamente esta de alta hasta el dia anterior

        CURSOR C_BAJA  (VC_IDINSTITUCION NUMBER, VC_IDTIPOSERVICIOS NUMBER, VC_IDSERVICIO NUMBER, VC_IDSERVICIOSINSTITUCION NUMBER, VC_IDPERSONA NUMBER) IS
            SELECT PP.IDPETICION,
                PP.TIPOPETICION
            FROM PYS_SUSCRIPCION SS, 
                PYS_PETICIONCOMPRASUSCRIPCION PP
            WHERE SS.IDINSTITUCION = VC_IDINSTITUCION
               AND SS.IDTIPOSERVICIOS = VC_IDTIPOSERVICIOS
               AND SS.IDSERVICIO = VC_IDSERVICIO
               AND SS.IDSERVICIOSINSTITUCION = VC_IDSERVICIOSINSTITUCION
               AND SS.FECHABAJA IS NULL
               AND SS.IDPERSONA = VC_IDPERSONA
               AND SS.IDINSTITUCION = PP.IDINSTITUCION
               AND SS.IDPETICION = PP.IDPETICION
               AND SS.IDPERSONA = PP.IDPERSONA;

    BEGIN
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 1 - Lanza el cursor';
        FOR DatosBaja IN C_BAJA(P_IDINSTITUCION, P_IDTIPOSERVICIOS, P_IDSERVICIO, P_IDSERVICIOSINSTITUCION, P_IDPERSONA) LOOP

            -- Actualizamos las solicitudes a estado = 'B'
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 2 - Denegar la solicitud la peticion ' || DatosBaja.IDPETICION;
            UPDATE PYS_SERVICIOSSOLICITADOS  
            SET ACEPTADO = 'B', -- Denegada
                USUMODIFICACION = P_USUMODIFICACION,
                FECHAMODIFICACION = SYSDATE
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                AND IDSERVICIO = P_IDSERVICIO
                AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                AND IDPETICION = DatosBaja.IDPETICION;

            -- Si la peticion es de baja se pone como procesada.
            IF DatosBaja.TIPOPETICION = 'B' THEN
                V_CONTADOR := 0;

            ELSE
                -- Si no es de baja hay que comprobar si todos los servicios solicitados que la componen estan aceptados para poner el estado como procesada.
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 3 - Consulta datos pendientes de la solicitud de la peticion ' || DatosBaja.IDPETICION;                                   
                SELECT COUNT(*)
                    INTO V_CONTADOR
                FROM PYS_PETICIONCOMPRASUSCRIPCION PP
                WHERE PP.IDINSTITUCION = P_IDINSTITUCION
                    AND PP.IDPETICION = DatosBaja.IDPETICION                    
                    AND EXISTS (
                        SELECT 1
                        FROM PYS_SERVICIOSSOLICITADOS SS
                        WHERE SS.IDINSTITUCION = PP.IDINSTITUCION
                            AND SS.IDPETICION = PP.IDPETICION
                            AND SS.ACEPTADO = 'P' -- Pendiente
                        UNION 
                        SELECT 1
                        FROM PYS_PRODUCTOSSOLICITADOS PS
                        WHERE PS.IDINSTITUCION = PP.IDINSTITUCION
                            AND PS.IDPETICION = PP.IDPETICION
                            AND PS.ACEPTADO = 'P' -- Pendiente
                    );                     
            END IF;
            
            IF (V_CONTADOR = 0) THEN                        
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 4 - Procesar la peticion ' || DatosBaja.IDPETICION;                    
                UPDATE PYS_PETICIONCOMPRASUSCRIPCION 
                SET IDESTADOPETICION = 20, -- PROCESADA
                    USUMODIFICACION = P_USUMODIFICACION,
                    FECHAMODIFICACION = SYSDATE
                WHERE IDINSTITUCION = P_IDINSTITUCION
                    AND IDPETICION = DatosBaja.IDPETICION;
            END IF;             
        END LOOP;

        -- Borrado logico de las suscripciones
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 5 - Actualiza las fechas de baja logica del servicio';   
        UPDATE PYS_SUSCRIPCION 
        SET FECHABAJA = CASE WHEN(P_FECHAPROCESO_FINAL < FECHASUSCRIPCION) THEN FECHASUSCRIPCION ELSE P_FECHAPROCESO_FINAL END ,
            FECHABAJAFACTURACION = CASE WHEN(P_FECHAPROCESO_FINAL < FECHASUSCRIPCION) THEN FECHASUSCRIPCION ELSE P_FECHAPROCESO_FINAL END,
            USUMODIFICACION = P_USUMODIFICACION,
            FECHAMODIFICACION = SYSDATE
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
            AND IDSERVICIO = P_IDSERVICIO
            AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
            AND IDPERSONA = P_IDPERSONA
            AND FECHABAJA IS NULL;   

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 6 - Finaliza el proceso correctamente';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_TEXTOERROR;
        
        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM || ' - ' || V_TEXTOERROR;
  END BAJA_SERVICIO_PERSONA;
  
  /****************************************************************************************************************/
  /* Nombre: ALTA_SERVICIO_PERSONA
    Descripcion: Inscribe a una persona a un servicio automaticamente

    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER(4)
    - P_IDTIPOSERVICIOS - IN - Identificador del tipo de servicio - NUMBER(4)
    - P_IDSERVICIO - IN - Identificador del servicio - NUMBER(10)
    - P_IDSERVICIOSINSTITUCION - IN - Identificador del servicio institucion - NUMBER(10)
    - P_IDPERSONA - IN - Identificador de la persona suscrita al servicio - NUMBER(10)
    - P_FECHAPROCESO - IN - Fecha en la que se va a realizar el alta de la suscripcion - DATE   
    - P_DESCRIPCION - IN - Descripcion del servicio - VARCHAR2(100)
    - P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER(5)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Version: 1.0 - 14/09/2015 - Jorge Paez
        - Version SIGA_122         
  /****************************************************************************************************************/      
    PROCEDURE ALTA_SERVICIO_PERSONA(
        P_IDINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDINSTITUCION%TYPE,
        P_IDTIPOSERVICIOS IN PYS_SERVICIOSSOLICITADOS.IDTIPOSERVICIOS%TYPE,
        P_IDSERVICIO IN PYS_SERVICIOSSOLICITADOS.IDSERVICIO%TYPE,
        P_IDSERVICIOSINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDSERVICIOSINSTITUCION%TYPE,
        P_IDPERSONA IN PYS_SERVICIOSSOLICITADOS.IDPERSONA%TYPE,
        P_FECHAPROCESO IN DATE,
        P_DESCRIPCION IN PYS_SERVICIOSINSTITUCION.DESCRIPCION%TYPE,
        P_USUMODIFICACION IN PYS_SERVICIOSSOLICITADOS.USUMODIFICACION%TYPE,
        P_CODRETORNO OUT  VARCHAR2,
        P_DATOSERROR OUT  VARCHAR2) IS
                
        V_FORMAPAGO PYS_FORMAPAGOSERVICIOS.IDFORMAPAGO%TYPE;
        V_FORMAPAGO20 PYS_FORMAPAGOSERVICIOS.IDFORMAPAGO%TYPE; -- DOMICILIACIONBANCARIA
        V_FORMAPAGONO20 PYS_FORMAPAGOSERVICIOS.IDFORMAPAGO%TYPE; -- NO DOMICILIACIONBANCARIA
        V_IDPETICION PYS_SERVICIOSSOLICITADOS.IDPETICION%TYPE;
        V_IDSUSCRIPCION PYS_SUSCRIPCION.IDSUSCRIPCION%TYPE;
        V_IDCUENTA CEN_CUENTASBANCARIAS.IDCUENTA%TYPE;
        V_CONTADOR NUMBER;
        V_PROCEDIMIENTO CONSTANT VARCHAR2(40) := 'ALTA_SERVICIO_PERSONA';
        V_TEXTOERROR VARCHAR2(4000) := NULL;
        
    BEGIN                                                      
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 1 - Consulto si el servicio esta activo a dia de hoy';
        SELECT COUNT(*)
            INTO V_CONTADOR
        FROM PYS_SUSCRIPCION
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
            AND IDSERVICIO = P_IDSERVICIO
            AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
            AND IDPERSONA = P_IDPERSONA            
            --AND TRUNC(FECHASUSCRIPCION) <= TRUNC(SYSDATE) -- JPT: Se comento esta linea para no dar de alta suscripciones cuando estaba suscrito en el futuro
            AND (FECHABAJA IS NULL OR (TRUNC(FECHABAJA) >= TRUNC(SYSDATE) AND TRUNC(FECHABAJA) >= TRUNC(FECHASUSCRIPCION))); -- Busca suscripciones sin baja logica o con baja logica mayor o igual que hoy
                        
        -- Compruebo que no tiene suscripciones activas hoy, y por tanto hay que suscribirlo    
        IF (V_CONTADOR = 0) THEN
                    
            BEGIN
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 2 - Consulto si el servicio acepta la forma de pago por domiciliacion bancaria';
                SELECT IDFORMAPAGO 
                    INTO V_FORMAPAGO20
                FROM PYS_FORMAPAGOSERVICIOS
                WHERE IDINSTITUCION = P_IDINSTITUCION
                    AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                    AND IDSERVICIO = P_IDSERVICIO
                    AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                    AND IDFORMAPAGO = 20 -- DOMICILIACIONBANCARIA
                    AND ROWNUM = 1;
                                
                EXCEPTION
                    WHEN NO_DATA_FOUND THEN
                        V_FORMAPAGO20 := NULL;
            END;
                            
            BEGIN
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 3 - Consulto una forma de pago del servicio que no sea por domiciliacion bancaria';
                SELECT IDFORMAPAGO 
                    INTO V_FORMAPAGONO20
                FROM (
                    SELECT IDFORMAPAGO                                         
                    FROM PYS_FORMAPAGOSERVICIOS
                    WHERE IDINSTITUCION = P_IDINSTITUCION
                        AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                        AND IDSERVICIO = P_IDSERVICIO
                        AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                        AND IDFORMAPAGO <> 20 -- NO DOMICILIACIONBANCARIA
                   ORDER BY DECODE(IDFORMAPAGO, 30, 1, IDFORMAPAGO) -- METALICOOEFECTIVO EN PRIMER LUGAR
               )
                WHERE ROWNUM = 1;
                                                  
                EXCEPTION
                    WHEN NO_DATA_FOUND THEN
                        V_FORMAPAGONO20 := NULL;
            END;        
                            
            IF (V_FORMAPAGO20 IS NULL AND V_FORMAPAGONO20 IS NULL) THEN
                -- JPT: Se pone Metalico para el caso de un servicio sin definir las formas de pago
                -- JPT: Hay que suscribirle como sea y por tanto se pone el pago en metalico
                V_FORMAPAGONO20 := 30; -- METALICOOEFECTIVO  
            END IF;     
            
            -- JPT: Consulto si tiene una cuenta bancaria de cargo activa, cuando el servicio tiene la forma de pago por domiciliacion bancaria            
            IF (V_FORMAPAGO20 IS NOT NULL) THEN
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 4 - Consulto si la persona tiene una cuenta de cargo activa';
                SELECT MIN(IDCUENTA) 
                        INTO V_IDCUENTA
                FROM (                        
                    Select IDCUENTA                         
                    FROM CEN_CUENTASBANCARIAS
                    WHERE IDINSTITUCION = P_IDINSTITUCION
                        AND IDPERSONA = P_IDPERSONA
                        AND FECHABAJA IS NULL
                        AND ABONOCARGO IN ('C', 'T') -- C=CuentaCargo; T=CuentaAbonoCargo
                    ORDER BY FECHAMODIFICACION DESC
                )
                WHERE ROWNUM = 1;       
                                                        
            ELSE                                    
                V_IDCUENTA := NULL;
            END IF;      
                    
            -- Controlo que si el servicio solo acepta DOMICILIACIONBANCARIA, debe terner cuenta bancaria de cargos activa
            IF (V_FORMAPAGO20 IS NOT NULL AND V_FORMAPAGONO20 IS NULL AND V_IDCUENTA IS NULL) THEN
                   -- JPT: Se pone Metalico para el caso de un servicio solo por domiciliacion de cuenta bancaria y sin cuenta bancaria activa de la persona
                   -- JPT: Hay que suscribirle como sea y por tanto se pone el pago en metalico
                   V_FORMAPAGO20 := NULL;
                   V_FORMAPAGONO20 := 30; -- METALICOOEFECTIVO  
            END IF;                     
                            
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 5 - Obtiene un nuevo identificador de peticion de la institucion';
            SELECT NVL(MAX(IDPETICION), 0) + 1
                INTO V_IDPETICION
            FROM PYS_PETICIONCOMPRASUSCRIPCION
            WHERE IDINSTITUCION = P_IDINSTITUCION;           
                            
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 6 - Inserta la peticion de la suscripcion';
            INSERT INTO PYS_PETICIONCOMPRASUSCRIPCION(
                IDINSTITUCION,
                IDPETICION,
                TIPOPETICION,
                IDPERSONA,
                FECHA,
                IDESTADOPETICION,
                NUM_OPERACION,
                FECHAMODIFICACION,
                USUMODIFICACION
            ) VALUES (
                P_IDINSTITUCION,
                V_IDPETICION,
                'A', -- ALTA
                P_IDPERSONA,
                SYSDATE,
                20,  -- PROCESADA,
                '1' || LPAD(P_IDINSTITUCION, 4, 0) || LPAD(P_IDPERSONA, 10, 0) || F_TO_MILISEGUNDOS(SYSDATE),
                SYSDATE,
                P_USUMODIFICACION);
                        
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 7 - Calcula la forma de pago';            
            -- Compruebo si acepta la forma de pago por domiciliacion bancaria y tiene una cuenta bancaria activa
            IF (V_FORMAPAGO20 IS NOT NULL AND V_IDCUENTA IS NOT NULL) THEN
                V_FORMAPAGO := V_FORMAPAGO20; -- DOMICILIACIONBANCARIA    
                                        
            ELSIF (V_FORMAPAGONO20 IS NULL) THEN
                V_FORMAPAGO := 30; -- METALICOOEFECTIVO
                                        
            ELSE                                        
                V_FORMAPAGO := V_FORMAPAGONO20;
            END IF;                                                                    
                                        
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 8 - Inserta la solicitud del servicio';
            INSERT INTO PYS_SERVICIOSSOLICITADOS(
                IDINSTITUCION,
                IDTIPOSERVICIOS,
                IDSERVICIO,
                IDSERVICIOSINSTITUCION,
                IDPETICION,
                IDFORMAPAGO,
                IDPERSONA,
                CANTIDAD,
                ACEPTADO,
                IDCUENTA,
                FECHAMODIFICACION,
                USUMODIFICACION
            ) VALUES (
                P_IDINSTITUCION,
                P_IDTIPOSERVICIOS,
                P_IDSERVICIO,
                P_IDSERVICIOSINSTITUCION,
                V_IDPETICION,
                V_FORMAPAGO, 
                P_IDPERSONA,
                1,
                'A', -- Aceptado
                V_IDCUENTA,
                SYSDATE,
                P_USUMODIFICACION);    
                                                                            
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 9 - Obtiene un nuevo identificador de suscripcion';
            SELECT NVL(MAX(IDSUSCRIPCION), 0) + 1
                INTO V_IDSUSCRIPCION
            FROM PYS_SUSCRIPCION
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                AND IDSERVICIO = P_IDSERVICIO
                AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION;
                                        
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 10 - Inserta la suscripcion al servicio';
            INSERT INTO PYS_SUSCRIPCION (
                IDINSTITUCION,
                IDTIPOSERVICIOS,
                IDSERVICIO,
                IDSERVICIOSINSTITUCION,
                IDSUSCRIPCION,
                IDPERSONA,
                IDPETICION,
                FECHASUSCRIPCION,
                CANTIDAD,
                IDFORMAPAGO,
                DESCRIPCION,
                IDCUENTA,
                FECHAMODIFICACION,
                USUMODIFICACION                    
            ) VALUES (
                P_IDINSTITUCION,
                P_IDTIPOSERVICIOS,
                P_IDSERVICIO,
                P_IDSERVICIOSINSTITUCION,
                V_IDSUSCRIPCION,
                P_IDPERSONA,
                V_IDPETICION,
                P_FECHAPROCESO,
                1,
                V_FORMAPAGO,
                P_DESCRIPCION,
                V_IDCUENTA,
                SYSDATE,
                P_USUMODIFICACION);         
        END IF;                        
            
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 11 - Finaliza el proceso correctamente';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_TEXTOERROR;                       
            
        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM || ' - ' || V_TEXTOERROR;
    END ALTA_SERVICIO_PERSONA;  

  /****************************************************************************************************************/
  /* Nombre: PROCESO_SUSCRIPCION_AUTO
    Descripcion: Proceso que se encarga de gestionar la suscripcion automatica a un servicio

    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER(4)
    - P_IDTIPOSERVICIOS - IN - Identificador del tipo de servicio - NUMBER(4)
    - P_IDSERVICIO - IN - Identificador del servicio - NUMBER(10)
    - P_IDSERVICIOSINSTITUCION - IN - Identificador del servicio institucion - NUMBER(10)
    - P_FECHAPROCESO - IN - Fecha en la que se va a realizar la suscripcion automatica - DATE
    - P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER(5)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Version: 1.0 - 31/01/2006 - Pilar Duran
    Version: 2.0 - 14/09/2015 - Jorge Paez
        - Version SIGA_122         
  /****************************************************************************************************************/    
  PROCEDURE PROCESO_SUSCRIPCION_AUTO(
        P_IDINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDINSTITUCION%TYPE,
        P_IDTIPOSERVICIOS IN PYS_SERVICIOSSOLICITADOS.IDTIPOSERVICIOS%TYPE,
        P_IDSERVICIO IN PYS_SERVICIOSSOLICITADOS.IDSERVICIO%TYPE,
        P_IDSERVICIOSINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDSERVICIOSINSTITUCION%TYPE,
        P_FECHAPROCESO IN PYS_SERVICIOSSOLICITADOS.FECHAMODIFICACION%TYPE,
        P_USUMODIFICACION IN PYS_SERVICIOSSOLICITADOS.USUMODIFICACION%TYPE,
        P_CODRETORNO OUT  VARCHAR2,
        P_DATOSERROR OUT  VARCHAR2) IS
        
        v_criterios PYS_SERVICIOSINSTITUCION.CRITERIOS%TYPE;
        v_criteriosAux PYS_SERVICIOSINSTITUCION.CRITERIOS%TYPE;
        posIdpersona NUMBER;
        cadena1 PYS_SERVICIOSINSTITUCION.CRITERIOS%TYPE;
        cadena2 PYS_SERVICIOSINSTITUCION.CRITERIOS%TYPE;
        cadenaFinal PYS_SERVICIOSINSTITUCION.CRITERIOS%TYPE;
        longitud1 NUMBER;
        longitud2 NUMBER;
        posComa NUMBER;
        V_CURSOR INTEGER; /* Cursor donde se ejecuta la sentencia SQL */
        V_DUMMY INTEGER; /* Donde se guarda el valor devuelto de la consulta */
        V_IDINSTITUCION CEN_CLIENTE.IDINSTITUCION%TYPE; /* Identificador de la institucion */
        V_IDPERSONA CEN_CLIENTE.IDPERSONA%TYPE; /* Persona a la que se va a facturar */
        V_DESCRIPCION PYS_SERVICIOSINSTITUCION.DESCRIPCION%TYPE;
        V_PROCEDIMIENTO CONSTANT VARCHAR2(40) := 'PROCESO_SUSCRIPCION_AUTO';
        V_TEXTOERROR VARCHAR2(4000) := NULL;
        E_ERRORCONTROLADO EXCEPTION;
        E_ERRORPROCEDIMIENTO EXCEPTION;
   
    BEGIN
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 1 - Obtiene los criterios del servicio';
        SELECT CRITERIOS, DESCRIPCION
            INTO V_CRITERIOS, V_DESCRIPCION
        FROM PYS_SERVICIOSINSTITUCION
        WHERE IDINSTITUCION = P_IDINSTITUCION
            AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
            AND IDSERVICIO = P_IDSERVICIO
            AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION;
            
        IF (v_criterios IS NULL) THEN
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 2 - No tiene criterios el servicio (IDINSTITUCION=' || P_IDINSTITUCION || '; IDTIPOSERVICIOS=' || P_IDTIPOSERVICIOS || '; IDSERVICIO=' || P_IDSERVICIO || '; IDSERVICIOSINSTITUCION=' || P_IDSERVICIOSINSTITUCION || ')';
            RAISE E_ERRORCONTROLADO;
        END IF;            
            
        -- v_criterios almacena el contenido del campo criterios recuperado, en el sustituimos @fecha@ por SYSDATE:
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 3 - Transforma el campo fecha por SYSDATE';
        v_criterios := REPLACE(UPPER(v_criterios), '@FECHA@', 'SYSDATE');

        -- reemplazar @idpersona@ por el literal que se obtiene encontrando la primera ocurrencia de idpersona dentro de v_criterios. Esta ocurrencia puede tener delante el nombre de una tabla.
        -- Habria que coger el nombre de la tabla.idpersona y utilizar ese literal para sustituir.
        
        -- PASO 1: Obtenemos en que posicion se encuentra la primera ocurrencia de idpersona hasta el FROM
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 4 - Obtiene la posicion del primer FROM';
        v_criteriosAux := SUBSTR(v_criterios, 1, INSTR(UPPER(v_criterios), 'FROM', 1, 1));
        
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 5 - Obtiene la posicion del campo IDPERSONA';
        posIdpersona := INSTR(UPPER(v_criteriosAux), 'IDPERSONA', 1, 1);

        IF (posIdpersona=0) THEN
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 6 - No se encuentra la cadena idpersona en el criterio del servicio (IDINSTITUCION=' || P_IDINSTITUCION || '; IDTIPOSERVICIOS=' || P_IDTIPOSERVICIOS || '; IDSERVICIO=' || P_IDSERVICIO || '; IDSERVICIOSINSTITUCION=' || P_IDSERVICIOSINSTITUCION || ')';
            RAISE E_ERRORCONTROLADO;
        END IF;            

        -- PASO 2: Nos quedamos con la cadena desde el principio hasta posIdPersona + longitud('IDPERSONA')
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 7 - Obtiene la subcadena hasta el campo IDPERSONA';
        cadena1 := SUBSTR(v_criteriosAux, 1, posIdpersona + LENGTH('IDPERSONA'));

        -- PASO 3: Calculamos la longitud de la cadena resultante
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 8 - Obtiene la dimension de la subcadena';
        longitud1 := LENGTH(cadena1);

        -- PASO 4: Obtenemos la posicion de la primera coma de la cadena resultante
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 9 - Obtiene la primera coma de la subcadena';
        posComa := INSTR(cadena1, ',', -1, 1);

        IF (posComa=0) THEN

            -- Si ocurre esto quiere decir que la cadena idpersona es el primer campo de las SELECT por ello la cadena buscada seria
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 10 - Obtiene el primer campo de la subcadena';
            cadenaFinal := TRIM(SUBSTR(cadena1, LENGTH('SELECT') + 1, longitud1 - LENGTH('SELECT')));

        ELSE
            -- PASO 5: Nos quedamos con la cadena desde el principio hasta la posicion posComa
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 11 - Obtiene el contenido de la subcadena hasta la primera coma';
            cadena2 := SUBSTR(cadena1, 1, posComa);

            -- PASO 6: Calculamos la longitud de esta cadena
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 12 - Calcula la dimension del contenido de la subcadena hasta la primera coma';
            longitud2 := LENGTH(cadena2);

            -- PASO 7: Obtenemos la cadena que nos interesa partiendo de cadena1 desde posComa+1 hasta posIdpersona-posComa
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 13 - Obtiene el contenido de la subcadena desde la primera coma';
            cadenaFinal := TRIM(SUBSTR(cadena1, longitud2 + 1, longitud1 - longitud2));
       END IF;

        -- Hacemos una comprobacion final de que efectivamente tenemos la cadena buscada 'idpersona'
        IF INSTR(UPPER(cadenaFinal), 'IDPERSONA') = 0 THEN
            V_TEXTOERROR :=  V_PROCEDIMIENTO || ': 14 - No se encuentra la cadena idpersona en el criterio del servicio (IDINSTITUCION=' || P_IDINSTITUCION || '; IDTIPOSERVICIOS=' || P_IDTIPOSERVICIOS || '; IDSERVICIO=' || P_IDSERVICIO || '; IDSERVICIOSINSTITUCION=' || P_IDSERVICIOSINSTITUCION || ')';
            RAISE E_ERRORCONTROLADO;
        END IF;
          
        -- PASO 8: reemplazamos @idpersona@ por cadenaFinal obteniendo asi la query definitiva que ejecutaremos posteriormente.
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 15 - Transforma el campo idpersona por la subcadena calculada';
        v_criterios := REPLACE(UPPER(v_criterios), '@IDPERSONA@', cadenaFinal);

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 16 - Apertura del cursor para procesamiento';
        V_CURSOR := DBMS_SQL.OPEN_CURSOR;

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 17 - Analisis de la consulta (IDINSTITUCION=' || P_IDINSTITUCION || '; IDTIPOSERVICIOS=' || P_IDTIPOSERVICIOS || '; IDSERVICIO=' || P_IDSERVICIO || '; IDSERVICIOSINSTITUCION=' || P_IDSERVICIOSINSTITUCION || ')';
        DBMS_SQL.PARSE(V_CURSOR, v_criterios, DBMS_SQL.V7);

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 18 - Introduce las variables de salida de la consulta';
        DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, V_IDINSTITUCION);
        DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 2, V_IDPERSONA);

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 19 - Lanza la consulta dinamica';
        V_DUMMY := DBMS_SQL.EXECUTE(V_CURSOR);  

        LOOP
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 20 - Sale del bucle cuando ya no encuentre mas datos';
            EXIT WHEN DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0;

            V_TEXTOERROR := V_PROCEDIMIENTO || ': 21 - Obtiene la institucion y la persona de la consulta dinamica';
            DBMS_SQL.COLUMN_VALUE(V_CURSOR, 1, V_IDINSTITUCION);
            DBMS_SQL.COLUMN_VALUE(V_CURSOR, 2, V_IDPERSONA);
            
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 22 - Llamada al procedimiento ALTA_SERVICIO_PERSONA';
            ALTA_SERVICIO_PERSONA(
                V_IDINSTITUCION,
                P_IDTIPOSERVICIOS,
                P_IDSERVICIO,
                P_IDSERVICIOSINSTITUCION,
                V_IDPERSONA,
                P_FECHAPROCESO,
                V_DESCRIPCION,
                P_USUMODIFICACION,
                P_CODRETORNO,
                P_DATOSERROR);
            IF (P_CODRETORNO <> '0') THEN
                P_DATOSERROR := V_TEXTOERROR || ' - ' || P_DATOSERROR;
                RAISE E_ERRORPROCEDIMIENTO;
            END IF;
        END LOOP;

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 23 - Cierra el cursor';
        DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
        
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 24 - Finaliza el proceso correctamente';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_TEXTOERROR;
  
        EXCEPTION
            WHEN E_ERRORPROCEDIMIENTO THEN
                DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
        
            WHEN E_ERRORCONTROLADO THEN
                P_CODRETORNO := '-1';
                P_DATOSERROR := V_TEXTOERROR;

            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM || ' - ' || V_TEXTOERROR;
                DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
    END PROCESO_SUSCRIPCION_AUTO;
  
  /****************************************************************************************************************/
  /* Nombre: PROCESO_REVISION_LETRADO
    Descripcion: Dado un cliente y fecha, revisa las suscripciones automaticas y da de alta o baja segun corresponda

    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER(4)
    - P_IDPERSONA - IN - Identificador de la persona suscrita al servicio - NUMBER(10)
    - P_FECHAPROCESO - IN - Fecha para la revision de las suscripciones - DATE
    - P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER(5)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Version: 1.0 - 02/01/2006 - Pilar Duran
    Version: 2.0 - 08/09/2014 - Adrian Ayala
        - Para los servicios automaticos dados de baja se estaban creando peticiones de compra vacias
        - Revision general del metodo     
    Version: 3.0 - 14/09/2015 - Jorge Paez
        - Version SIGA_122         
  /****************************************************************************************************************/      
    Procedure PROCESO_REVISION_LETRADO(
        P_IDINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDINSTITUCION%TYPE,
        P_IDPERSONA IN PYS_SERVICIOSSOLICITADOS.IDPERSONA%TYPE,
        P_FECHAPROCESO IN CEN_DATOSCOLEGIALESESTADO.FECHAESTADO%TYPE,
        P_USUMODIFICACION IN PYS_SERVICIOSSOLICITADOS.USUMODIFICACION%TYPE,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

        CURSOR C_CRITERIOS (VC_IDINSTITUCION NUMBER) IS
            SELECT IDTIPOSERVICIOS,
                IDSERVICIO,
                IDSERVICIOSINSTITUCION,
                CRITERIOS,
                FECHABAJA,
                DESCRIPCION
            FROM PYS_SERVICIOSINSTITUCION
            WHERE IDINSTITUCION = VC_IDINSTITUCION
                AND AUTOMATICO = '1'
                AND FECHABAJA IS NULL;

        V_FECHAPROCESO DATE;
        V_CRITERIOS PYS_SERVICIOSINSTITUCION.CRITERIOS%TYPE;
        V_CURSOR INTEGER; -- Cursor donde se ejecuta la sentencia SQL
        V_DUMMY INTEGER; -- Donde se guarda el valor devuelto de la consulta
        V_IDINSTITUCION CEN_CLIENTE.IDINSTITUCION%TYPE;
        V_IDPERSONA CEN_CLIENTE.IDPERSONA%TYPE;        
        V_PROCEDIMIENTO CONSTANT VARCHAR2(40) := 'PROCESO_REVISION_LETRADO';
        V_TEXTOERROR VARCHAR2(4000) := NULL;
        E_ERRORCONTROLADO EXCEPTION;
        E_ERRORPROCEDIMIENTO EXCEPTION;

    BEGIN
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 1 - NVL de fecha estado';
        V_FECHAPROCESO := NVL(P_FECHAPROCESO, SYSDATE);

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 2 - Lanza el cursor';
        FOR DatosCriterio In C_CRITERIOS(P_IDINSTITUCION) LOOP
            V_CRITERIOS := DatosCriterio.CRITERIOS;      

            -- Un servicio automatico ha de tener criterios
            IF (V_CRITERIOS) IS NULL THEN
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 3 - No tiene criterios el servicio automático (IDINSTITUCION=' || P_IDINSTITUCION || '; IDTIPOSERVICIOS=' || DatosCriterio.IDTIPOSERVICIOS || '; IDSERVICIO=' || DatosCriterio.IDSERVICIO || '; IDSERVICIOSINSTITUCION=' || DatosCriterio.IDSERVICIOSINSTITUCION || ')';
                Raise E_ERRORCONTROLADO;
            END IF;

            V_TEXTOERROR := V_PROCEDIMIENTO || ': 4 - Transforma el campo fecha por la fecha de la revision';
            V_CRITERIOS  := REPLACE(UPPER(V_CRITERIOS), '@FECHA@', 'TO_DATE(''' || V_FECHAPROCESO || ''')');

            V_TEXTOERROR := V_PROCEDIMIENTO || ': 5 - Transforma el campo idpersona por la persona de la revision';
            V_CRITERIOS  := REPLACE(UPPER(V_CRITERIOS), '@IDPERSONA@', P_IDPERSONA);

            V_TEXTOERROR := V_PROCEDIMIENTO || ': 6 - Apertura del cursor';
            V_CURSOR := DBMS_SQL.OPEN_CURSOR;

            V_TEXTOERROR := V_PROCEDIMIENTO || ': 7 - Analisis de la consulta (IDINSTITUCION=' || P_IDINSTITUCION || '; IDTIPOSERVICIOS=' || DatosCriterio.IDTIPOSERVICIOS || '; IDSERVICIO=' || DatosCriterio.IDSERVICIO || '; IDSERVICIOSINSTITUCION=' || DatosCriterio.IDSERVICIOSINSTITUCION || ')';
            DBMS_SQL.PARSE(V_CURSOR, V_CRITERIOS, DBMS_SQL.V7);

            V_TEXTOERROR := V_PROCEDIMIENTO || ': 8 - Introduce las variables de salida de la consulta';
            DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, V_IDINSTITUCION);
            DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 2, V_IDPERSONA);

            V_TEXTOERROR := V_PROCEDIMIENTO || ': 9 - Lanza la consulta dinamica';
            v_Dummy := DBMS_SQL.EXECUTE(V_CURSOR);
            -- La consulta obtenida en V_CURSOR nos devuelve un registro si la persona cumple con las condiciones para suscribirse al servicio automatico y no devuelve registros si no lo cumple
            
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 10 - Controla si no cumple el criterio (deja de estar suscrito), o bien tiene baja logica';
            IF (DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0 OR TRUNC(DatosCriterio.FECHABAJA) < TRUNC(V_FECHAPROCESO)) THEN
                
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 11 - Llamada al procedimiento BAJA_SERVICIO_PERSONA';
                BAJA_SERVICIO_PERSONA(
                    P_IDINSTITUCION,
                    DatosCriterio.IDTIPOSERVICIOS,
                    DatosCriterio.IDSERVICIO,
                    DatosCriterio.IDSERVICIOSINSTITUCION,
                    P_IDPERSONA,
                    V_FECHAPROCESO,
                    P_USUMODIFICACION,
                    P_CODRETORNO,
                    P_DATOSERROR);
                IF (P_CODRETORNO <> '0') THEN
                    P_DATOSERROR := V_TEXTOERROR || ' - ' || P_DATOSERROR;  
                    RAISE E_ERRORPROCEDIMIENTO;
                END IF;
               
            ELSE     
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 12 - Llamada al procedimiento ALTA_SERVICIO_PERSONA';
                ALTA_SERVICIO_PERSONA(
                    P_IDINSTITUCION,
                    DatosCriterio.IDTIPOSERVICIOS,
                    DatosCriterio.IDSERVICIO,
                    DatosCriterio.IDSERVICIOSINSTITUCION,
                    P_IDPERSONA,
                    V_FECHAPROCESO,
                    DatosCriterio.DESCRIPCION,
                    P_USUMODIFICACION,
                    P_CODRETORNO,
                    P_DATOSERROR);
                IF (P_CODRETORNO <> '0') THEN
                    P_DATOSERROR := V_TEXTOERROR || ' - ' || P_DATOSERROR;
                    RAISE E_ERRORPROCEDIMIENTO;
                END IF;      
            END IF;                                         
                     
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 13 - Cierra el cursor';
            DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
        END LOOP;
        
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 14 - Finaliza el proceso correctamente';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_TEXTOERROR;        

        EXCEPTION
            WHEN E_ERRORPROCEDIMIENTO THEN
                DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
        
            WHEN E_ERRORCONTROLADO THEN
                P_CODRETORNO := '-1';
                P_DATOSERROR := V_TEXTOERROR;

            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM || ' - ' || V_TEXTOERROR;
                DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
    End PROCESO_REVISION_LETRADO;
    
  /****************************************************************************************************************/
  /* Nombre: PROCESO_ACT_CUENTA_BANCO_PEND
    Descripcion: Este proceso se encarga de actualizar las cosas pendientes asociadas a la cuenta de baja de una persona

    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER(4)
    - P_IDPERSONA - IN - Identificador de la persona suscrita al servicio - NUMBER(10)
    - P_IDCUENTA - IN - Identificador de la cuenta bancaria dada de baja - DATE
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Version: 1.0 - 10/03/2014 - Maria Jimenez     
    Version: 2.0 - 08/08/2014 - Jorge Paez
        - Version SIGA_122         
  /****************************************************************************************************************/         
    PROCEDURE PROCESO_ACT_CUENTA_BANCO_PEND (
        P_IDINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDINSTITUCION%TYPE,
        P_IDPERSONA IN PYS_SERVICIOSSOLICITADOS.IDPERSONA%TYPE,
        P_IDCUENTA IN PYS_SERVICIOSSOLICITADOS.IDCUENTA%TYPE,
        P_USUMODIFICACION IN PYS_SERVICIOSSOLICITADOS.USUMODIFICACION%TYPE,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS

        v_numCuentas NUMBER;
        v_idCuentaFinal CEN_CUENTASBANCARIAS.IDCUENTA%TYPE;
        V_PROCEDIMIENTO CONSTANT VARCHAR2(40) := 'PROCESO_ACT_CUENTA_BANCO_PEND';
        V_TEXTOERROR VARCHAR2(4000) := NULL;

    BEGIN
        -- Paso 1: Compruebo el numero de cuentas de cargo activas de la persona
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 1 - Compruebo el numero de cuentas de cargo activas de la persona';
        SELECT COUNT(*)
            INTO v_numCuentas
        FROM CEN_CUENTASBANCARIAS CUENTASBANCARIAS
        WHERE CUENTASBANCARIAS.IDINSTITUCION = p_Idinstitucion
            AND CUENTASBANCARIAS.IDPERSONA = p_Idpersona
            AND CUENTASBANCARIAS.ABONOCARGO IN ('C', 'T')
            AND CUENTASBANCARIAS.FECHABAJA IS NULL;

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 2 - Obtengo el identificador de la cuenta final';
        IF (v_numCuentas = 0) THEN -- No existe otra cuenta de baja
            v_idCuentaFinal := NULL;

        ELSE
            SELECT CUENTAS_BANCARIAS_ORDENADAS.IDCUENTA
                INTO v_idCuentaFinal
            FROM (
                    SELECT CUENTASBANCARIAS.IDCUENTA, CUENTASBANCARIAS.FECHAMODIFICACION
                    FROM CEN_CUENTASBANCARIAS CUENTASBANCARIAS
                    WHERE CUENTASBANCARIAS.IDINSTITUCION = p_Idinstitucion
                        AND CUENTASBANCARIAS.IDPERSONA = p_Idpersona
                        AND CUENTASBANCARIAS.ABONOCARGO IN ('C', 'T')
                        AND CUENTASBANCARIAS.FECHABAJA IS NULL
                    ORDER BY CUENTASBANCARIAS.FECHAMODIFICACION DESC
                ) CUENTAS_BANCARIAS_ORDENADAS
            WHERE ROWNUM = 1;
        END IF;

         V_TEXTOERROR := V_PROCEDIMIENTO || ': 3 - Actualiza los servicios';
        UPDATE PYS_SUSCRIPCION SUSCRIPCION
        SET SUSCRIPCION.IDCUENTA = v_idCuentaFinal,
            SUSCRIPCION.IDFORMAPAGO = DECODE(v_idCuentaFinal, NULL, 30, 20),
            SUSCRIPCION.USUMODIFICACION = p_Usumodificacion,
            SUSCRIPCION.FECHAMODIFICACION = SYSDATE
        WHERE SUSCRIPCION.IDINSTITUCION = p_Idinstitucion
            AND SUSCRIPCION.IDPERSONA = p_Idpersona
            AND SUSCRIPCION.IDCUENTA = p_Idcuenta
            AND EXISTS (
                SELECT 1
                FROM PYS_SERVICIOSSOLICITADOS SERVICIOSSOLICITADOS,
                    PYS_PETICIONCOMPRASUSCRIPCION PETICIONCOMPRASUSCRIPCION
                WHERE SERVICIOSSOLICITADOS.IDINSTITUCION = SUSCRIPCION.IDINSTITUCION
                    AND SERVICIOSSOLICITADOS.IDTIPOSERVICIOS = SUSCRIPCION.IDTIPOSERVICIOS
                    AND SERVICIOSSOLICITADOS.IDSERVICIO = SUSCRIPCION.IDSERVICIO
                    AND SERVICIOSSOLICITADOS.IDSERVICIOSINSTITUCION = SUSCRIPCION.IDSERVICIOSINSTITUCION
                    AND SERVICIOSSOLICITADOS.IDPETICION = SUSCRIPCION.IDPETICION
                    AND PETICIONCOMPRASUSCRIPCION.IDINSTITUCION = SERVICIOSSOLICITADOS.IDINSTITUCION
                    AND PETICIONCOMPRASUSCRIPCION.IDPETICION = SERVICIOSSOLICITADOS.IDPETICION
                    AND PETICIONCOMPRASUSCRIPCION.TIPOPETICION = 'A'
                    AND SERVICIOSSOLICITADOS.ACEPTADO IN ('A', 'P', 'B') -- Aceptado, Pendiente o Baja
            );
            /* JPT: Las suscripciones hay que actualizarlas siempre, ya que aunque tengan factura pagada, hay que cambiarlas para la factura posterior
            AND (
                NOT EXISTS (
                    SELECT 1
                    FROM FAC_FACTURACIONSUSCRIPCION FACTURACIONSUSCRIPCION
                    WHERE FACTURACIONSUSCRIPCION.IDINSTITUCION = SUSCRIPCION.IDINSTITUCION
                        AND FACTURACIONSUSCRIPCION.IDTIPOSERVICIOS = SUSCRIPCION.IDTIPOSERVICIOS
                        AND FACTURACIONSUSCRIPCION.IDSERVICIO = SUSCRIPCION.IDSERVICIO
                        AND FACTURACIONSUSCRIPCION.IDSERVICIOSINSTITUCION = SUSCRIPCION.IDSERVICIOSINSTITUCION
                        AND FACTURACIONSUSCRIPCION.IDSUSCRIPCION = SUSCRIPCION.IDSUSCRIPCION
                ) OR EXISTS (
                    SELECT 1
                    FROM FAC_FACTURACIONSUSCRIPCION FACTURACIONSUSCRIPCION,
                        FAC_FACTURA FACTURA
                    WHERE FACTURACIONSUSCRIPCION.IDINSTITUCION = SUSCRIPCION.IDINSTITUCION
                        AND FACTURACIONSUSCRIPCION.IDTIPOSERVICIOS = SUSCRIPCION.IDTIPOSERVICIOS
                        AND FACTURACIONSUSCRIPCION.IDSERVICIO = SUSCRIPCION.IDSERVICIO
                        AND FACTURACIONSUSCRIPCION.IDSERVICIOSINSTITUCION = SUSCRIPCION.IDSERVICIOSINSTITUCION
                        AND FACTURACIONSUSCRIPCION.IDSUSCRIPCION = SUSCRIPCION.IDSUSCRIPCION
                        AND FACTURA.IDINSTITUCION = FACTURACIONSUSCRIPCION.IDINSTITUCION
                        AND FACTURA.IDFACTURA = FACTURACIONSUSCRIPCION.IDFACTURA -- JPT: Si la factura tiene comisiones es que ya se ha facturado
                        AND FACTURA.ESTADO IN (5, 7)
                )
            );*/

        /* No vamos a modificar el servicio solicitado, para mantener sus datos iniciales, solo PYS_SUSCRIPCION
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 4 - Actualiza los servicios solicitados';
        UPDATE PYS_SERVICIOSSOLICITADOS SERVICIOSSOLICITADOS
        SET SERVICIOSSOLICITADOS.IDCUENTA = v_idCuentaFinal,
            SERVICIOSSOLICITADOS.IDFORMAPAGO = DECODE(v_idCuentaFinal, NULL, 30, 20),
            SERVICIOSSOLICITADOS.USUMODIFICACION = p_Usumodificacion,
            SERVICIOSSOLICITADOS.FECHAMODIFICACION = SYSDATE
        WHERE SERVICIOSSOLICITADOS.IDINSTITUCION = p_Idinstitucion
            AND SERVICIOSSOLICITADOS.IDPERSONA = p_Idpersona
            AND SERVICIOSSOLICITADOS.IDCUENTA = p_Idcuenta
            AND SERVICIOSSOLICITADOS.ACEPTADO IN ('A', 'P', 'B') -- Aceptado, Pendiente o Baja
            AND EXISTS (
                SELECT 1
                FROM PYS_PETICIONCOMPRASUSCRIPCION PETICIONCOMPRASUSCRIPCION
                WHERE PETICIONCOMPRASUSCRIPCION.IDINSTITUCION = SERVICIOSSOLICITADOS.IDINSTITUCION
                    AND PETICIONCOMPRASUSCRIPCION.IDPETICION = SERVICIOSSOLICITADOS.IDPETICION
                    AND PETICIONCOMPRASUSCRIPCION.TIPOPETICION = 'A'
            )
            AND (
                NOT EXISTS (
                    SELECT 1
                    FROM FAC_FACTURACIONSUSCRIPCION FACTURACIONSUSCRIPCION,
                        PYS_SUSCRIPCION SUSCRIPCION
                    WHERE SUSCRIPCION.IDINSTITUCION = SERVICIOSSOLICITADOS.IDINSTITUCION
                        AND SUSCRIPCION.IDTIPOSERVICIOS = SERVICIOSSOLICITADOS.IDTIPOSERVICIOS
                        AND SUSCRIPCION.IDSERVICIO = SERVICIOSSOLICITADOS.IDSERVICIO
                        AND SUSCRIPCION.IDSERVICIOSINSTITUCION = SERVICIOSSOLICITADOS.IDSERVICIOSINSTITUCION
                        AND SUSCRIPCION.IDPETICION = SERVICIOSSOLICITADOS.IDPETICION
                        AND FACTURACIONSUSCRIPCION.IDINSTITUCION = SUSCRIPCION.IDINSTITUCION
                        AND FACTURACIONSUSCRIPCION.IDTIPOSERVICIOS = SUSCRIPCION.IDTIPOSERVICIOS
                        AND FACTURACIONSUSCRIPCION.IDSERVICIO = SUSCRIPCION.IDSERVICIO
                        AND FACTURACIONSUSCRIPCION.IDSERVICIOSINSTITUCION = SUSCRIPCION.IDSERVICIOSINSTITUCION
                        AND FACTURACIONSUSCRIPCION.IDSUSCRIPCION = SUSCRIPCION.IDSUSCRIPCION
                ) OR EXISTS (
                    SELECT 1
                    FROM FAC_FACTURACIONSUSCRIPCION FACTURACIONSUSCRIPCION,
                        FAC_FACTURA FACTURA,
                        PYS_SUSCRIPCION SUSCRIPCION
                    WHERE SUSCRIPCION.IDINSTITUCION = SERVICIOSSOLICITADOS.IDINSTITUCION
                        AND SUSCRIPCION.IDTIPOSERVICIOS = SERVICIOSSOLICITADOS.IDTIPOSERVICIOS
                        AND SUSCRIPCION.IDSERVICIO = SERVICIOSSOLICITADOS.IDSERVICIO
                        AND SUSCRIPCION.IDSERVICIOSINSTITUCION = SERVICIOSSOLICITADOS.IDSERVICIOSINSTITUCION
                        AND SUSCRIPCION.IDPETICION = SERVICIOSSOLICITADOS.IDPETICION
                        AND FACTURACIONSUSCRIPCION.IDINSTITUCION = SUSCRIPCION.IDINSTITUCION
                        AND FACTURACIONSUSCRIPCION.IDTIPOSERVICIOS = SUSCRIPCION.IDTIPOSERVICIOS
                        AND FACTURACIONSUSCRIPCION.IDSERVICIO = SUSCRIPCION.IDSERVICIO
                        AND FACTURACIONSUSCRIPCION.IDSERVICIOSINSTITUCION = SUSCRIPCION.IDSERVICIOSINSTITUCION
                        AND FACTURACIONSUSCRIPCION.IDSUSCRIPCION = SUSCRIPCION.IDSUSCRIPCION
                        AND FACTURA.IDINSTITUCION = FACTURACIONSUSCRIPCION.IDINSTITUCION
                        AND FACTURA.IDFACTURA =  FACTURACIONSUSCRIPCION.IDFACTURA -- JPT: Si la factura tiene comisiones es que ya se ha facturado
                        AND FACTURA.ESTADO IN (5, 7)
                )
            );*/

            V_TEXTOERROR := V_PROCEDIMIENTO || ': 5 - Actualiza la compra de los productos';
            UPDATE PYS_COMPRA COMPRA
            SET COMPRA.IDCUENTA = v_idCuentaFinal,
                COMPRA.IDFORMAPAGO = DECODE(COMPRA.IDCUENTADEUDOR, NULL, DECODE(v_idCuentaFinal, NULL, 30, 20), 20),
                COMPRA.USUMODIFICACION = p_Usumodificacion,
                COMPRA.FECHAMODIFICACION = SYSDATE
            WHERE COMPRA.IDINSTITUCION = p_Idinstitucion
                AND COMPRA.IDPERSONA = p_Idpersona
                AND COMPRA.IDCUENTA = p_Idcuenta
                AND (
                    COMPRA.IDFACTURA IS NULL
                    OR EXISTS (
                             SELECT 1
                            FROM FAC_FACTURA FACTURA
                            WHERE FACTURA.IDINSTITUCION = COMPRA.IDINSTITUCION
                                AND FACTURA.IDFACTURA = COMPRA.IDFACTURA -- JPT: Si la factura tiene comisiones es que ya se ha facturado
                                AND FACTURA.ESTADO IN (5, 7)
                    )
                )
                AND EXISTS (
                    SELECT 1
                    FROM PYS_PRODUCTOSSOLICITADOS PRODUCTOSSOLICITADOS,
                        PYS_PETICIONCOMPRASUSCRIPCION PETICIONCOMPRASUSCRIPCION
                    WHERE PRODUCTOSSOLICITADOS.IDINSTITUCION = COMPRA.IDINSTITUCION
                        AND PRODUCTOSSOLICITADOS.IDTIPOPRODUCTO = COMPRA.IDTIPOPRODUCTO
                        AND PRODUCTOSSOLICITADOS.IDPRODUCTO = COMPRA.IDPRODUCTO
                        AND PRODUCTOSSOLICITADOS.IDPRODUCTOINSTITUCION = COMPRA.IDPRODUCTOINSTITUCION
                        AND PRODUCTOSSOLICITADOS.IDPETICION = COMPRA.IDPETICION
                        AND PETICIONCOMPRASUSCRIPCION.IDINSTITUCION = PRODUCTOSSOLICITADOS.IDINSTITUCION
                        AND PETICIONCOMPRASUSCRIPCION.IDPETICION = PRODUCTOSSOLICITADOS.IDPETICION
                        AND PETICIONCOMPRASUSCRIPCION.TIPOPETICION = 'A'
                );

       V_TEXTOERROR := V_PROCEDIMIENTO || ': 6 - Actualiza la compra de los productos de la persona deudora';
        UPDATE PYS_COMPRA COMPRA
        SET COMPRA.IDCUENTADEUDOR = v_idCuentaFinal,
            COMPRA.IDFORMAPAGO = DECODE(v_idCuentaFinal, NULL, DECODE(COMPRA.IDCUENTA, NULL, 30, 20), 20),
            COMPRA.USUMODIFICACION = p_Usumodificacion,
            COMPRA.FECHAMODIFICACION = SYSDATE
        WHERE COMPRA.IDINSTITUCION = p_Idinstitucion
            AND COMPRA.IDPERSONADEUDOR = p_Idpersona
            AND COMPRA.IDCUENTADEUDOR = p_Idcuenta
            AND (
                COMPRA.IDFACTURA IS NULL
                OR EXISTS (
                         SELECT 1
                        FROM FAC_FACTURA FACTURA
                        WHERE FACTURA.IDINSTITUCION = COMPRA.IDINSTITUCION
                            AND FACTURA.IDFACTURA = COMPRA.IDFACTURA -- JPT: Si la factura tiene comisiones es que ya se ha facturado
                            AND FACTURA.ESTADO IN (5, 7)
                )
            )
            AND EXISTS (
                SELECT 1
                FROM PYS_PRODUCTOSSOLICITADOS PRODUCTOSSOLICITADOS,
                    PYS_PETICIONCOMPRASUSCRIPCION PETICIONCOMPRASUSCRIPCION
                WHERE PRODUCTOSSOLICITADOS.IDINSTITUCION = COMPRA.IDINSTITUCION
                    AND PRODUCTOSSOLICITADOS.IDTIPOPRODUCTO = COMPRA.IDTIPOPRODUCTO
                    AND PRODUCTOSSOLICITADOS.IDPRODUCTO = COMPRA.IDPRODUCTO
                    AND PRODUCTOSSOLICITADOS.IDPRODUCTOINSTITUCION = COMPRA.IDPRODUCTOINSTITUCION
                    AND PRODUCTOSSOLICITADOS.IDPETICION = COMPRA.IDPETICION
                    AND PETICIONCOMPRASUSCRIPCION.IDINSTITUCION = PRODUCTOSSOLICITADOS.IDINSTITUCION
                    AND PETICIONCOMPRASUSCRIPCION.IDPETICION = PRODUCTOSSOLICITADOS.IDPETICION
                    AND PETICIONCOMPRASUSCRIPCION.TIPOPETICION = 'A'
            );

        /* No vamos a modificar el producto solicitado, para mantener sus datos iniciales, solo PYS_COMPRA
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 7 - Actualiza los productos solicitados';
        UPDATE PYS_PRODUCTOSSOLICITADOS PRODUCTOSSOLICITADOS
        SET PRODUCTOSSOLICITADOS.IDFORMAPAGO = DECODE(v_idCuentaFinal, NULL, 30, 20),
            PRODUCTOSSOLICITADOS.IDCUENTA = v_idCuentaFinal,
            PRODUCTOSSOLICITADOS.USUMODIFICACION = p_Usumodificacion,
            PRODUCTOSSOLICITADOS.FECHAMODIFICACION = SYSDATE
        WHERE PRODUCTOSSOLICITADOS.IDINSTITUCION = p_Idinstitucion
            AND PRODUCTOSSOLICITADOS.IDPERSONA = p_Idpersona
            AND PRODUCTOSSOLICITADOS.IDCUENTA = p_Idcuenta
            AND EXISTS (
                SELECT 1
                FROM PYS_PETICIONCOMPRASUSCRIPCION PETICIONCOMPRASUSCRIPCION,
                    PYS_COMPRA COMPRA
                WHERE PETICIONCOMPRASUSCRIPCION.IDINSTITUCION = PRODUCTOSSOLICITADOS.IDINSTITUCION
                    AND PETICIONCOMPRASUSCRIPCION.IDPETICION = PRODUCTOSSOLICITADOS.IDPETICION
                    AND PETICIONCOMPRASUSCRIPCION.TIPOPETICION = 'A'
                    AND COMPRA.IDINSTITUCION = PRODUCTOSSOLICITADOS.IDINSTITUCION
                    AND COMPRA.IDTIPOPRODUCTO = PRODUCTOSSOLICITADOS.IDTIPOPRODUCTO
                    AND COMPRA.IDPRODUCTO = PRODUCTOSSOLICITADOS.IDPRODUCTO
                    AND COMPRA.IDPRODUCTOINSTITUCION = PRODUCTOSSOLICITADOS.IDPRODUCTOINSTITUCION
                    AND COMPRA.IDPETICION = PRODUCTOSSOLICITADOS.IDPETICION
                    AND (
                        COMPRA.IDFACTURA IS NULL
                        OR EXISTS (
                                 SELECT 1
                                FROM FAC_FACTURA FACTURA
                                WHERE FACTURA.IDINSTITUCION = COMPRA.IDINSTITUCION
                                    AND FACTURA.IDFACTURA = COMPRA.IDFACTURA -- JPT: Si la factura tiene comisiones es que ya se ha facturado
                                    AND FACTURA.ESTADO IN (5, 7)
                        )
                    )
            );*/

        /* Notas JPT - Estados Factura:
            - 1: Pagado => No se modifica
            - 2: Pendiente de cobro (caja) => como no tiene cuenta no se modifica
            - 4: Devuelta => Como tiene que renegociar no se modifica
            - 5: Pendiente banco => Se debe modificar
            - 7: En revision => Se debe modificar
            - 8: Anulada => No se modifica
        */
        -- Actualizo las facturas de los productos y servicios, de la persona y cuenta de baja
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 8 - Actualiza las facturas';
        UPDATE FAC_FACTURA FACTURA
        SET FACTURA.IDCUENTA = v_idCuentaFinal,
            FACTURA.IDFORMAPAGO = DECODE(FACTURA.IDCUENTADEUDOR, NULL, DECODE(v_idCuentaFinal, NULL, 30, 20), 20),
            FACTURA.ESTADO = DECODE(FACTURA.IDCUENTADEUDOR, NULL, DECODE(v_idCuentaFinal, NULL, 2, FACTURA.ESTADO), FACTURA.ESTADO),
            FACTURA.USUMODIFICACION = p_Usumodificacion,
            FACTURA.FECHAMODIFICACION = SYSDATE
        WHERE FACTURA.IDINSTITUCION = p_Idinstitucion
            AND FACTURA.IDPERSONA = p_Idpersona
            AND FACTURA.IDCUENTA = p_Idcuenta
            AND FACTURA.ESTADO IN (5, 7);

        -- Actualizo las facturas de los productos y servicios, de la persona deudora y cuenta de baja
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 9 - Actualiza las facturas de la persona dedudora';
        UPDATE FAC_FACTURA FACTURA
        SET FACTURA.IDCUENTADEUDOR = v_idCuentaFinal,
            FACTURA.IDFORMAPAGO = DECODE(v_idCuentaFinal, NULL, DECODE(FACTURA.IDCUENTA, NULL, 30, 20), 20),
            FACTURA.ESTADO = DECODE(v_idCuentaFinal, NULL, DECODE(FACTURA.IDCUENTA, NULL, 2, FACTURA.ESTADO), FACTURA.ESTADO),
            FACTURA.USUMODIFICACION = p_Usumodificacion,
            FACTURA.FECHAMODIFICACION = SYSDATE
        WHERE FACTURA.IDINSTITUCION = p_Idinstitucion
            AND FACTURA.IDPERSONADEUDOR = p_Idpersona
            AND FACTURA.IDCUENTADEUDOR = p_Idcuenta
            AND FACTURA.ESTADO IN (5, 7);

        /*** ABONOS ***/
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 10 - Compruebo el numero de cuentas de abono activas de la persona';
        SELECT COUNT(*)
            INTO v_numCuentas
        FROM CEN_CUENTASBANCARIAS CUENTASBANCARIAS
        WHERE CUENTASBANCARIAS.IDINSTITUCION = p_Idinstitucion
            AND CUENTASBANCARIAS.IDPERSONA = p_Idpersona
            AND CUENTASBANCARIAS.ABONOCARGO IN ('A', 'T')
            AND CUENTASBANCARIAS.FECHABAJA IS NULL
            AND CUENTASBANCARIAS.ABONOSJCS = 0;

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 11 - Obtengo el identificador de la cuenta final de abono';
        IF (v_numCuentas = 0) THEN -- No existe otra cuenta de baja
            v_idCuentaFinal := NULL;

        ELSE
            SELECT CUENTAS_BANCARIAS_ORDENADAS.IDCUENTA
                INTO v_idCuentaFinal
            FROM (
                    SELECT CUENTASBANCARIAS.IDCUENTA, CUENTASBANCARIAS.FECHAMODIFICACION
                    FROM CEN_CUENTASBANCARIAS CUENTASBANCARIAS
                    WHERE CUENTASBANCARIAS.IDINSTITUCION = p_Idinstitucion
                        AND CUENTASBANCARIAS.IDPERSONA = p_Idpersona
                        AND CUENTASBANCARIAS.ABONOCARGO IN ('A', 'T')
                        AND CUENTASBANCARIAS.FECHABAJA IS NULL
                        AND CUENTASBANCARIAS.ABONOSJCS = 0
                    ORDER BY CUENTASBANCARIAS.FECHAMODIFICACION DESC
                ) CUENTAS_BANCARIAS_ORDENADAS
            WHERE ROWNUM = 1;
        END IF;

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 12 - Actualiza los abonos de la persona y cuenta de baja';
        UPDATE FAC_ABONO ABONO
        SET ABONO.IDCUENTA = v_idCuentaFinal,
            ABONO.ESTADO = DECODE(ABONO.IDCUENTADEUDOR, NULL, DECODE(v_idCuentaFinal, NULL, 6, 5), 5),
            ABONO.USUMODIFICACION = p_Usumodificacion,
            ABONO.FECHAMODIFICACION = SYSDATE
        WHERE ABONO.IDINSTITUCION = p_Idinstitucion
            AND ABONO.IDPERSONA = p_Idpersona
            AND ABONO.IDCUENTA = p_Idcuenta
            AND ABONO.ESTADO = 5
            AND ABONO.IDPAGOSJG IS NULL;

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 13 - Actualiza los abonos de la persona y cuenta de baja';
        UPDATE FAC_ABONO ABONO
        SET ABONO.IDCUENTADEUDOR = v_idCuentaFinal,
            ABONO.ESTADO = DECODE(v_idCuentaFinal, NULL, DECODE(ABONO.IDCUENTA, NULL, 6, 5), 5),
            ABONO.USUMODIFICACION = p_Usumodificacion,
            ABONO.FECHAMODIFICACION = SYSDATE
        WHERE ABONO.IDINSTITUCION = p_Idinstitucion
            AND ABONO.IDPERSONADEUDOR = p_Idpersona
            AND ABONO.IDCUENTADEUDOR = p_Idcuenta
            AND ABONO.ESTADO = 5
            AND ABONO.IDPAGOSJG IS NULL;

         /*** ABONOS SJCS ***/
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 14 - Compruebo el numero de cuentas de abono SJCS activas de la persona';
        SELECT COUNT(*)
            INTO v_numCuentas
        FROM CEN_CUENTASBANCARIAS CUENTASBANCARIAS
        WHERE CUENTASBANCARIAS.IDINSTITUCION = p_Idinstitucion
            AND CUENTASBANCARIAS.IDPERSONA = p_Idpersona
            AND CUENTASBANCARIAS.ABONOCARGO IN ('A', 'T')
            AND CUENTASBANCARIAS.FECHABAJA IS NULL
            AND CUENTASBANCARIAS.ABONOSJCS = 1;

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 15 - Obtengo el identificador de la cuenta final de abono SJCS';
        IF (v_numCuentas = 0) THEN -- No existe otra cuenta de baja
            v_idCuentaFinal := NULL;

        ELSE
            SELECT CUENTAS_BANCARIAS_ORDENADAS.IDCUENTA
                INTO v_idCuentaFinal
            FROM (
                    SELECT CUENTASBANCARIAS.IDCUENTA, CUENTASBANCARIAS.FECHAMODIFICACION
                    FROM CEN_CUENTASBANCARIAS CUENTASBANCARIAS
                    WHERE CUENTASBANCARIAS.IDINSTITUCION = p_Idinstitucion
                        AND CUENTASBANCARIAS.IDPERSONA = p_Idpersona
                        AND CUENTASBANCARIAS.ABONOCARGO IN ('A', 'T')
                        AND CUENTASBANCARIAS.FECHABAJA IS NULL
                        AND CUENTASBANCARIAS.ABONOSJCS = 1
                    ORDER BY CUENTASBANCARIAS.FECHAMODIFICACION DESC
                ) CUENTAS_BANCARIAS_ORDENADAS
            WHERE ROWNUM = 1;
        END IF;

       V_TEXTOERROR := V_PROCEDIMIENTO || ': 16 - Actualiza los abonos de la persona y cuenta de baja';
        UPDATE FAC_ABONO ABONO
        SET ABONO.IDCUENTA = v_idCuentaFinal,
            ABONO.ESTADO = DECODE(ABONO.IDCUENTADEUDOR, NULL, DECODE(v_idCuentaFinal, NULL, 6, 5), 5),
            ABONO.USUMODIFICACION = p_Usumodificacion,
            ABONO.FECHAMODIFICACION = SYSDATE
        WHERE ABONO.IDINSTITUCION = p_Idinstitucion
            AND ABONO.IDPERSONA = p_Idpersona
            AND ABONO.IDCUENTA = p_Idcuenta
            AND ABONO.ESTADO = 5
            AND ABONO.IDPAGOSJG IS NOT NULL;


       V_TEXTOERROR := V_PROCEDIMIENTO || ': 17 - Actualiza los abonos de la persona y cuenta de baja';
        UPDATE FAC_ABONO ABONO
        SET ABONO.IDCUENTADEUDOR = v_idCuentaFinal,
            ABONO.ESTADO = DECODE(v_idCuentaFinal, NULL, DECODE(ABONO.IDCUENTA, NULL, 6, 5), 5),
            ABONO.USUMODIFICACION = p_Usumodificacion,
            ABONO.FECHAMODIFICACION = SYSDATE
        WHERE ABONO.IDINSTITUCION = p_Idinstitucion
            AND ABONO.IDPERSONADEUDOR = p_Idpersona
            AND ABONO.IDCUENTADEUDOR = p_Idcuenta
            AND ABONO.ESTADO = 5
            AND ABONO.IDPAGOSJG IS NOT NULL;

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 18 - Finaliza el proceso correctamente';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_TEXTOERROR;   

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM || ' - ' || V_TEXTOERROR;
    END PROCESO_ACT_CUENTA_BANCO_PEND;
  
  /****************************************************************************************************************/
  /* Nombre: PROCESO_ELIMINAR_SUSCRIPCION
    Descripcion: Eliminar fisicamente las suscripciones y solicitudes de compra a un servicio que no se encuentren en ningun ciclo de facturacion, ya sea GENERADO, PENDIENTE CONFIRMAR o CONFIRMADO.

    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER(4)
    - P_IDTIPOSERVICIOS - IN - Identificador del tipo de servicio - NUMBER(4)
    - P_IDSERVICIO - IN - Identificador del servicio - NUMBER(10)
    - P_IDSERVICIOSINSTITUCION - IN - Identificador del servicio institucion - NUMBER(10)
    - P_FECHAPROCESO - IN - Fecha en la que se va a realizar la baja del servicio - VARCHAR2(10)
    - P_ALTA - IN - Indica los tipos de solicitudes a borrar - VARCHAR2(1)
    - P_FECHAALTA - IN - Fecha de alta en formato DD/MM/YYYY - VARCHAR2(10)
    - P_INCLUIRMANUALES - IN - Incluir servicios manuales - VARCHAR2(1)
    - P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER(5)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el mensaje de error Oracle correspondiente.
    - P_BORRADOSUSCRIPCIONES - OUT - Devuelve el numero de suscripciones borradas - NUMBER  

    Version: 1.0 - 06/02/2007 - Pilar Duran
    Version: 2.0 - 14/09/2015 - Jorge Paez
        - Version SIGA_122         
  /****************************************************************************************************************/  
    PROCEDURE PROCESO_ELIMINAR_SUSCRIPCION(
        P_IDINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDINSTITUCION%TYPE,
        P_IDTIPOSERVICIOS IN PYS_SERVICIOSSOLICITADOS.IDTIPOSERVICIOS%TYPE,
        P_IDSERVICIO IN PYS_SERVICIOSSOLICITADOS.IDSERVICIO%TYPE,
        P_IDSERVICIOSINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDSERVICIOSINSTITUCION%TYPE,
        P_ALTA IN VARCHAR2,
        P_FECHAALTA IN VARCHAR2,
        P_INCLUIRMANUALES IN VARCHAR2,
        P_USUMODIFICACION IN PYS_SERVICIOSSOLICITADOS.USUMODIFICACION%TYPE,        
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2,
        P_BORRADOSUSCRIPCIONES OUT VARCHAR2) IS
      
        V_CONTADORBORRADOSOLICITUD NUMBER := 0;
        V_IDPETICIONBAJA NUMBER;
        V_CONTADOR NUMBER;
        V_PROCEDIMIENTO CONSTANT VARCHAR2(40) := 'PROCESO_ELIMINAR_SUSCRIPCION';
        V_TEXTOERROR VARCHAR2(4000) := NULL;
                        
        CURSOR C_CRITERIOS  (VC_IDINSTITUCION NUMBER, VC_IDTIPOSERVICIOS NUMBER, VC_IDSERVICIO NUMBER, VC_IDSERVICIOSINSTITUCION NUMBER, VC_FECHAALTA VARCHAR2) IS
            SELECT DISTINCT SS.IDPETICION,
                SS.IDPERSONA
            FROM PYS_SERVICIOSSOLICITADOS SS, 
                PYS_PETICIONCOMPRASUSCRIPCION PP
            WHERE SS.IDPETICION = PP.IDPETICION
                AND SS.IDINSTITUCION = PP.IDINSTITUCION
                AND SS.IDINSTITUCION = VC_IDINSTITUCION
                AND SS.IDTIPOSERVICIOS = VC_IDTIPOSERVICIOS
                AND SS.IDSERVICIO = VC_IDSERVICIO
                AND SS.IDSERVICIOSINSTITUCION = VC_IDSERVICIOSINSTITUCION
                AND PP.TIPOPETICION = 'A' -- Peticion de alta
                AND (VC_FECHAALTA IS NULL OR TRUNC(PP.FECHA) = TO_DATE(VC_FECHAALTA, 'DD/MM/YYYY')); -- No tiene o es la fecha de alta

        CURSOR C_BAJA (VC_IDINSTITUCION NUMBER, VC_IDTIPOSERVICIOS NUMBER, VC_IDSERVICIO NUMBER, VC_IDSERVICIOSINSTITUCION NUMBER) IS
            SELECT DISTINCT SS.IDPETICION,
                SS.IDPERSONA
            FROM PYS_SERVICIOSSOLICITADOS SS, 
                PYS_PETICIONCOMPRASUSCRIPCION PP
            WHERE SS.IDPETICION = PP.IDPETICION
                AND  SS.IDINSTITUCION = PP.IDINSTITUCION
                AND  SS.IDINSTITUCION = VC_IDINSTITUCION
                AND  SS.IDTIPOSERVICIOS = VC_IDTIPOSERVICIOS
                AND  SS.IDSERVICIO = VC_IDSERVICIO
                AND  SS.IDSERVICIOSINSTITUCION = VC_IDSERVICIOSINSTITUCION
                AND  PP.TIPOPETICION = 'A' -- Peticion de alta
                AND  SS.ACEPTADO = 'B'; -- Denegada

        CURSOR C_PETICIONBAJA (VC_IDINSTITUCION NUMBER, VC_IDTIPOSERVICIOS NUMBER, VC_IDSERVICIO NUMBER, VC_IDSERVICIOSINSTITUCION NUMBER) IS
            SELECT DISTINCT PPB.IDPETICION
        FROM PYS_SERVICIOSSOLICITADOS SS, -- SS Alta
            PYS_PETICIONCOMPRASUSCRIPCION PP, -- PCS Alta
            PYS_PETICIONCOMPRASUSCRIPCION PPB, -- PCS Baja
            PYS_SERVICIOSSOLICITADOS SSB -- SS Baja            
        WHERE SS.IDPETICION = PP.IDPETICION
            AND SS.IDINSTITUCION = PP.IDINSTITUCION
            AND SS.IDINSTITUCION = VC_IDINSTITUCION
            AND SS.IDTIPOSERVICIOS = VC_IDTIPOSERVICIOS
            AND SS.IDSERVICIO = VC_IDSERVICIO
            AND SS.IDSERVICIOSINSTITUCION = VC_IDSERVICIOSINSTITUCION
            AND PP.TIPOPETICION = 'A' -- Peticion del alta
            AND PP.IDINSTITUCION = PPB.IDINSTITUCION
            AND PP.IDPETICION = PPB.IDPETICIONALTA -- Indico la peticion que tiene como alta la peticion de baja 
            AND SSB.IDINSTITUCION = SS.IDINSTITUCION
            AND SSB.IDTIPOSERVICIOS = SS.IDTIPOSERVICIOS
            AND SSB.IDSERVICIO = SS.IDSERVICIO
            AND SSB.IDSERVICIOSINSTITUCION = SS.IDSERVICIOSINSTITUCION            
            AND SSB.IDINSTITUCION = PPB.IDINSTITUCION
            AND SSB.IDPETICION = PPB.IDPETICION;

    BEGIN
        IF (P_ALTA = '0') THEN --El usuario ha solicitado el borrado de las solicitudes de Alta y Baja
    
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 1 - Obtiene las peticiones de alta de la fecha de alta ' || P_FECHAALTA;
            FOR DatosCriterios IN C_CRITERIOS (P_IDINSTITUCION, P_IDTIPOSERVICIOS, P_IDSERVICIO, P_IDSERVICIOSINSTITUCION, P_FECHAALTA) LOOP                                
                
                /* PROCESO JPT:
                    1. Elimino PYS_SUSCRIPCION
                    2. Elimino PYS_SERVICIOSSOLICITADOS de la peticion de baja
                    3. Elimino PYS_PETICIONCOMPRASUSCRIPCION de la peticion de baja
                    4. Elimino PYS_SERVICIOSSOLICITADOS de la peticion de alta
                    5. Elimino PYS_PETICIONCOMPRASUSCRIPCION de la peticion de alta si no tiene otros servicios o productos
                    6. Actualizo PYS_PETICIONCOMPRASUSCRIPCION de la peticion de alta si tiene todos los servicios y productos aceptados*/                  
                    
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 2 - Consulto si la petición está facturada ' || DatosCriterios.IDPETICION;
                SELECT COUNT(*) 
                    INTO V_CONTADOR
                FROM PYS_SUSCRIPCION SUS, 
                    FAC_FACTURACIONSUSCRIPCION FAC
                WHERE SUS.IDINSTITUCION = FAC.IDINSTITUCION
                    AND SUS.IDTIPOSERVICIOS = FAC.IDTIPOSERVICIOS
                    AND SUS.IDSERVICIO = FAC.IDSERVICIO
                    AND SUS.IDSERVICIOSINSTITUCION = FAC.IDSERVICIOSINSTITUCION
                    AND SUS.IDSUSCRIPCION = FAC.IDSUSCRIPCION
                    AND SUS.IDINSTITUCION = P_IDINSTITUCION
                    AND SUS.IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                    AND SUS.IDSERVICIO = P_IDSERVICIO
                    AND SUS.IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                    AND SUS.IDPETICION = DatosCriterios.IDPETICION
                    AND SUS.IDPERSONA = DatosCriterios.IDPERSONA;
                        
                IF (V_CONTADOR = 0) THEN
                    V_CONTADORBORRADOSOLICITUD := V_CONTADORBORRADOSOLICITUD + 1;
                    V_TEXTOERROR := V_PROCEDIMIENTO || ': 3 - Elimino la suscripción de la petición ' || DatosCriterios.IDPETICION;
                    DELETE PYS_SUSCRIPCION
                    WHERE IDINSTITUCION = P_IDINSTITUCION
                        AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                        AND IDSERVICIO = P_IDSERVICIO
                        AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                        AND IDPETICION = DatosCriterios.IDPETICION
                        AND IDPERSONA = DatosCriterios.IDPERSONA;
                            
                    V_TEXTOERROR := V_PROCEDIMIENTO || ': 4 - Consulto si tiene petición de baja ' || DatosCriterios.IDPETICION;
                    SELECT MIN(PP.IDPETICION) 
                        INTO V_IDPETICIONBAJA
                    FROM PYS_PETICIONCOMPRASUSCRIPCION PP, 
                        PYS_SERVICIOSSOLICITADOS SS
                    WHERE SS.IDINSTITUCION = PP.IDINSTITUCION
                        AND SS.IDPETICION = PP.IDPETICION
                        AND PP.IDINSTITUCION = P_IDINSTITUCION
                        AND PP.IDPETICIONALTA = DatosCriterios.IDPETICION -- Indico la peticion que tiene como alta la peticion de baja 
                        AND SS.IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                        AND SS.IDSERVICIO = P_IDSERVICIO
                        AND SS.IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION;
                            
                    IF (V_IDPETICIONBAJA IS NOT NULL) THEN -- Si existe peticion de baja la borramos
                        V_TEXTOERROR := V_PROCEDIMIENTO || ': 5 - Elimino la solicitud de la petición de baja ' || V_IDPETICIONBAJA;
                        DELETE PYS_SERVICIOSSOLICITADOS
                        WHERE IDINSTITUCION = P_IDINSTITUCION
                            AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                            AND IDSERVICIO = P_IDSERVICIO
                            AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                            AND IDPETICION = V_IDPETICIONBAJA;

                            V_TEXTOERROR := V_PROCEDIMIENTO || ': 6 - Elimino la petición de baja ' || V_IDPETICIONBAJA;
                            DELETE PYS_PETICIONCOMPRASUSCRIPCION 
                            WHERE IDINSTITUCION = P_IDINSTITUCION
                                AND IDPETICION = V_IDPETICIONBAJA;
                    END IF;                            

                    V_TEXTOERROR := V_PROCEDIMIENTO || ': 7 - Elimino la petición de alta ' || DatosCriterios.IDPETICION;
                    DELETE PYS_SERVICIOSSOLICITADOS
                    WHERE IDINSTITUCION = P_IDINSTITUCION
                        AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                        AND IDSERVICIO = P_IDSERVICIO
                        AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                        AND IDPETICION = DatosCriterios.IDPETICION;
                    
                    -- Cambiamos el estado de la peticion de alta (de PENDIENTE a PROCESADA si todos los productos/servicios restantes estan aceptados o denegados) o eliminarla si no quedan productos/servicios solicitados.                    
                    V_TEXTOERROR := V_PROCEDIMIENTO || ': 8 - Consulto si a la petición de alta le queda alguna solicitud de productos o servicios ' || DatosCriterios.IDPETICION;
                    SELECT COUNT(*)
                        INTO V_CONTADOR
                    FROM PYS_PETICIONCOMPRASUSCRIPCION PP
                    WHERE PP.IDINSTITUCION = P_IDINSTITUCION
                        AND PP.IDPETICION = DatosCriterios.IDPETICION
                        AND EXISTS (
                            SELECT 1
                            FROM PYS_SERVICIOSSOLICITADOS SS
                            WHERE SS.IDINSTITUCION = PP.IDINSTITUCION
                                AND SS.IDPETICION = PP.IDPETICION
                            UNION 
                            SELECT 1
                            FROM PYS_PRODUCTOSSOLICITADOS PS
                            WHERE PS.IDINSTITUCION = PP.IDINSTITUCION
                                AND PS.IDPETICION = PP.IDPETICION                
                        );     
                        
                    IF (V_CONTADOR = 0) THEN   
                        V_TEXTOERROR := V_PROCEDIMIENTO || ': 9 - Elimino la petición de alta ' || DatosCriterios.IDPETICION;
                        DELETE PYS_PETICIONCOMPRASUSCRIPCION
                        WHERE IDINSTITUCION = P_IDINSTITUCION
                            AND IDPETICION = DatosCriterios.IDPETICION;
                        
                    ELSE
                        V_TEXTOERROR := V_PROCEDIMIENTO || ': 10 - Consulto si la petición de alta tiene algo pendiente ' || DatosCriterios.IDPETICION;
                        SELECT COUNT(*)
                            INTO V_CONTADOR
                        FROM PYS_PETICIONCOMPRASUSCRIPCION PP
                        WHERE PP.IDINSTITUCION = P_IDINSTITUCION
                            AND PP.IDPETICION = DatosCriterios.IDPETICION
                            AND EXISTS (
                                SELECT 1
                                FROM PYS_SERVICIOSSOLICITADOS SS
                                WHERE SS.IDINSTITUCION = PP.IDINSTITUCION
                                    AND SS.IDPETICION = PP.IDPETICION
                                    AND SS.ACEPTADO = 'P' -- Pendiente
                                UNION 
                                SELECT 1
                                FROM PYS_PRODUCTOSSOLICITADOS PS
                                WHERE PS.IDINSTITUCION = PP.IDINSTITUCION
                                    AND PS.IDPETICION = PP.IDPETICION
                                    AND PS.ACEPTADO = 'P' -- Pendiente
                            );                         
                                
                        IF (V_CONTADOR = 0) THEN                            
                            V_TEXTOERROR := V_PROCEDIMIENTO || ': 11 - Actualiza la petición de alta ' || DatosCriterios.IDPETICION;
                            UPDATE PYS_PETICIONCOMPRASUSCRIPCION
                            SET IDESTADOPETICION = 20, -- PROCESADA
                                USUMODIFICACION = P_USUMODIFICACION,
                                FECHAMODIFICACION = SYSDATE
                            WHERE IDINSTITUCION = P_IDINSTITUCION
                                AND IDPETICION = DatosCriterios.IDPETICION;
                        END IF;                                                                    
                    END IF;                                                                                    
                END IF;
            END LOOP;

        ELSIF (P_INCLUIRMANUALES=0) THEN -- Se borra la baja, pero no el alta
        
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 12 - Obtiene las peticiones de alta denegadas';
            FOR DatosBaja IN C_BAJA(P_IDINSTITUCION, P_IDTIPOSERVICIOS, P_IDSERVICIO, P_IDSERVICIOSINSTITUCION) LOOP                

                V_TEXTOERROR := V_PROCEDIMIENTO || ': 13 - Consulto si la petición de baja tiene peticiones de alta ' || DatosBaja.IDPETICION;
                SELECT COUNT(*)
                    INTO V_CONTADOR
                FROM PYS_PETICIONCOMPRASUSCRIPCION PP,
                    PYS_SERVICIOSSOLICITADOS SS
                WHERE SS.IDINSTITUCION = PP.IDINSTITUCION
                    AND SS.IDPETICION = PP.IDPETICION
                    AND PP.IDINSTITUCION = P_IDINSTITUCION
                    AND PP.IDPETICIONALTA = DatosBaja.IDPETICION -- Indico la peticion que tiene como alta la peticion de baja 
                    AND SS.IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                    AND SS.IDSERVICIO = P_IDSERVICIO
                    AND SS.IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION;
                        
                IF (V_CONTADOR = 0) THEN
                                        
                    V_TEXTOERROR := V_PROCEDIMIENTO || ': 14 - Consulto si la petición de alta está facturada ' || DatosBaja.IDPETICION;               
                    SELECT COUNT(*)
                        INTO V_CONTADOR
                    FROM PYS_SUSCRIPCION SUS, 
                        FAC_FACTURACIONSUSCRIPCION FAC
                    WHERE SUS.IDINSTITUCION = FAC.IDINSTITUCION
                        AND SUS.IDTIPOSERVICIOS = FAC.IDTIPOSERVICIOS
                        AND SUS.IDSERVICIO = FAC.IDSERVICIO
                        AND SUS.IDSERVICIOSINSTITUCION = FAC.IDSERVICIOSINSTITUCION
                        AND SUS.IDSUSCRIPCION = FAC.IDSUSCRIPCION
                        AND SUS.IDINSTITUCION = P_IDINSTITUCION
                        AND SUS.IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                        AND SUS.IDSERVICIO = P_IDSERVICIO
                        AND SUS.IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                        AND SUS.IDPETICION = DatosBaja.IDPETICION
                        AND SUS.IDPERSONA = DatosBaja.IDPERSONA;
                                        
                    IF (V_CONTADOR = 0) THEN                        
                        V_CONTADORBORRADOSOLICITUD := V_CONTADORBORRADOSOLICITUD + 1;

                        V_TEXTOERROR := V_PROCEDIMIENTO || ': 15 - Elimina las fechas de baja del servicio de la petición ' || DatosBaja.IDPETICION;      
                        UPDATE PYS_SUSCRIPCION 
                        SET FECHABAJA = NULL,
                            FECHABAJAFACTURACION = NULL,
                            USUMODIFICACION = P_USUMODIFICACION,
                            FECHAMODIFICACION = SYSDATE
                        WHERE IDINSTITUCION = P_IDINSTITUCION
                            AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                            AND IDSERVICIO = P_IDSERVICIO
                            AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                            AND IDPETICION = DatosBaja.IDPETICION
                            AND IDPERSONA = DatosBaja.IDPERSONA;

                        V_TEXTOERROR := V_PROCEDIMIENTO || ': 16 - Acepta las solicitudes del servicio de la petición ' || DatosBaja.IDPETICION;
                        UPDATE PYS_SERVICIOSSOLICITADOS 
                        SET ACEPTADO = 'A', -- Aceptado
                            USUMODIFICACION = P_USUMODIFICACION,
                            FECHAMODIFICACION = SYSDATE
                        WHERE IDINSTITUCION = P_IDINSTITUCION
                            AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                            AND IDSERVICIO = P_IDSERVICIO
                            AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                            AND IDPETICION = DatosBaja.IDPETICION;
                    END IF;                            
                END IF;
            END LOOP;

        ELSE 
            -- Eliminamos las Bajas incluyendo las manuales
            -- Eliminamos primero las solicitudes de Alta con servicio como BAJA.

            V_TEXTOERROR := V_PROCEDIMIENTO || ': 17 - Obtiene las peticiones de alta denegadas';
            FOR DatosBaja IN C_BAJA(P_IDINSTITUCION, P_IDTIPOSERVICIOS, P_IDSERVICIO, P_IDSERVICIOSINSTITUCION) LOOP
                
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 18 - Consulto si la petición de alta está facturada ' || DatosBaja.IDPETICION;
                SELECT COUNT(*)
                    INTO V_CONTADOR
                FROM PYS_SUSCRIPCION SUS, 
                    FAC_FACTURACIONSUSCRIPCION FAC
                WHERE SUS.IDINSTITUCION = FAC.IDINSTITUCION
                    AND SUS.IDTIPOSERVICIOS = FAC.IDTIPOSERVICIOS
                    AND SUS.IDSERVICIO = FAC.IDSERVICIO
                    AND SUS.IDSERVICIOSINSTITUCION = FAC.IDSERVICIOSINSTITUCION
                    AND SUS.IDSUSCRIPCION = FAC.IDSUSCRIPCION
                    AND SUS.IDINSTITUCION = P_IDINSTITUCION
                    AND SUS.IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                    AND SUS.IDSERVICIO = P_IDSERVICIO
                    AND SUS.IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                    AND SUS.IDPETICION = DatosBaja.IDPETICION
                    AND SUS.IDPERSONA = DatosBaja.IDPERSONA;
                    
                IF (V_CONTADOR = 0) THEN                        
                    V_CONTADORBORRADOSOLICITUD := V_CONTADORBORRADOSOLICITUD + 1;

                    V_TEXTOERROR := V_PROCEDIMIENTO || ': 19 - Elimina las fechas de baja del servicio de la petición ' || DatosBaja.IDPETICION;      
                    UPDATE PYS_SUSCRIPCION 
                    SET FECHABAJA = NULL,
                        FECHABAJAFACTURACION = NULL,
                        USUMODIFICACION = P_USUMODIFICACION,
                        FECHAMODIFICACION = SYSDATE
                    WHERE IDINSTITUCION = P_IDINSTITUCION
                        AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                        AND IDSERVICIO = P_IDSERVICIO
                        AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                        AND IDPETICION = DatosBaja.IDPETICION
                        AND IDPERSONA = DatosBaja.IDPERSONA;

                    V_TEXTOERROR := V_PROCEDIMIENTO || ': 20 - Acepta las solicitudes del servicio de la petición ' || DatosBaja.IDPETICION;
                    UPDATE PYS_SERVICIOSSOLICITADOS 
                    SET ACEPTADO = 'A', -- Aceptado
                        USUMODIFICACION = P_USUMODIFICACION,
                        FECHAMODIFICACION = SYSDATE
                    WHERE IDINSTITUCION = P_IDINSTITUCION
                        AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                        AND IDSERVICIO = P_IDSERVICIO
                        AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                        AND IDPETICION = DatosBaja.IDPETICION;
                END IF;                      
            END LOOP;

            V_TEXTOERROR := V_PROCEDIMIENTO || ': 21 - Obtiene las peticiones de baja del servicio';
            FOR DatosPeticionBaja IN C_PETICIONBAJA(P_IDINSTITUCION, P_IDTIPOSERVICIOS, P_IDSERVICIO, P_IDSERVICIOSINSTITUCION) LOOP
                V_CONTADORBORRADOSOLICITUD := V_CONTADORBORRADOSOLICITUD + 1;         
                    
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 22 - Elimino la solicitud de la petición de baja ' || DatosPeticionBaja.IDPETICION;
                DELETE PYS_SERVICIOSSOLICITADOS
                WHERE IDINSTITUCION = P_IDINSTITUCION
                    AND IDTIPOSERVICIOS = P_IDTIPOSERVICIOS
                    AND IDSERVICIO = P_IDSERVICIO
                    AND IDSERVICIOSINSTITUCION = P_IDSERVICIOSINSTITUCION
                    AND IDPETICION = DatosPeticionBaja.IDPETICION;

                V_TEXTOERROR := V_PROCEDIMIENTO || ': 23 - Elimino la petición de baja ' || DatosPeticionBaja.IDPETICION;
                DELETE PYS_PETICIONCOMPRASUSCRIPCION
                WHERE IDINSTITUCION = P_IDINSTITUCION
                    AND IDPETICION = DatosPeticionBaja.IDPETICION;
            END LOOP;
        END IF;
        
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 24 - Finaliza el proceso correctamente';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_TEXTOERROR;  
        P_BORRADOSUSCRIPCIONES := TO_CHAR(V_CONTADORBORRADOSOLICITUD);
        
        EXCEPTION                
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM || ' - ' || V_TEXTOERROR;
                P_BORRADOSUSCRIPCIONES := '0';
    END PROCESO_ELIMINAR_SUSCRIPCION;
  
    /****************************************************************************************************************/
  /* Nombre: PROCESO_REVISION_AUTO
    Descripcion: Proceso que se encarga de dar de alta o de baja en todos los servicios a todos los clientes de un colegio dependiendo de si cumple o no los criterios de cada servicio

    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER(4)
    - P_FECHAEFECTIVA - IN - Fecha en la que se va a realizar la baja del servicio - VARCHAR2(10)
    - P_USUMODIFICACION - IN - Usuario que realiza la modificacion - NUMBER(5)
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - NUMBER(10)
        En caso de error devuelve el mensaje de error Oracle correspondiente.  

    Version: 1.0 - 14/01/2009 - Pilar Duran
    Version: 2.0 - 04/11/2014 - Adrian Ayala
        - Reformateado hasta la ejecucion del primer cursor dinamico
        - Arreglo de R1410_0070: se estaban insertando suscripciones duplicadas cuando se daba de baja a futuro.
        - La razon era que solo se comprobaba que existiera suscripcion sin fecha de baja. Lo que faltaba era comprobar tambien que existiera suscripcion con fecha de baja pero en el futuro.
    Version: 2.0 - 14/09/2015 - Jorge Paez
        - Version SIGA_122         
  /****************************************************************************************************************/  
    PROCEDURE PROCESO_REVISION_AUTO (
        P_IDINSTITUCION IN PYS_SERVICIOSSOLICITADOS.IDINSTITUCION%TYPE,
        P_FECHAEFECTIVA IN PYS_SERVICIOSSOLICITADOS.FECHAMODIFICACION%TYPE,
        P_USUMODIFICACION IN PYS_SERVICIOSSOLICITADOS.USUMODIFICACION%TYPE,
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS
        
        V_FECHAEFECTIVA DATE;
        V_TEXTOFECHA VARCHAR2(40);
        V_CRITERIOS PYS_SERVICIOSINSTITUCION.CRITERIOS%TYPE;
        V_CRITERIOSORIGINAL PYS_SERVICIOSINSTITUCION.CRITERIOS%TYPE;
        V_POSCOMA NUMBER;
        V_PRIMERAAPARICIONIDPERSONA NUMBER;
        V_TABLAPRINCIPAL VARCHAR2(4000) := '';        
        V_CURSOR INTEGER;
        V_DUMMY INTEGER;
        V_IDINSTITUCION CEN_CLIENTE.IDINSTITUCION%TYPE;
        V_IDPERSONA CEN_CLIENTE.IDPERSONA%TYPE;        
        V_PROCEDIMIENTO CONSTANT VARCHAR2(40) := 'PROCESO_REVISION_AUTO';
        V_TEXTOERROR VARCHAR2(4000) := NULL;
        E_ERRORCONTROLADO EXCEPTION;
        E_ERRORPROCEDIMIENTO EXCEPTION;
    
        CURSOR C_SERVICIOSAUTOMATICOS (VC_IDINSTITUCION NUMBER) IS
            SELECT *
            FROM PYS_SERVICIOSINSTITUCION
            WHERE IDINSTITUCION = VC_IDINSTITUCION
                AND AUTOMATICO = '1'
                AND FECHABAJA IS NULL;

    BEGIN
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 1 - NVL de fecha efectiva';
        V_FECHAEFECTIVA := NVL(P_FECHAEFECTIVA, SYSDATE);
        V_TEXTOFECHA := ' TO_DATE(''' || TO_CHAR(V_FECHAEFECTIVA, 'DD/MM/YYYY') || ''',''DD/MM/YYYY'') ';   

        V_TEXTOERROR := V_PROCEDIMIENTO || ': 2 - Obtiene los servicios automáticos activos';
        FOR DatosServicioAuto IN C_SERVICIOSAUTOMATICOS(P_IDINSTITUCION) LOOP
        
            V_CRITERIOSORIGINAL := UPPER(DatosServicioAuto.CRITERIOS);
            IF (V_CRITERIOSORIGINAL IS NULL) THEN
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 3 - No tiene criterios el servicio (IDINSTITUCION=' || P_IDINSTITUCION || '; IDTIPOSERVICIOS=' || DatosServicioAuto.IDTIPOSERVICIOS || '; IDSERVICIO=' || DatosServicioAuto.IDSERVICIO || '; IDSERVICIOSINSTITUCION=' || DatosServicioAuto.IDSERVICIOSINSTITUCION || ')';
                RAISE E_ERRORCONTROLADO;
            END IF;

            V_TEXTOERROR := V_PROCEDIMIENTO || ': 4 - Transforma el campo fecha por la fecha efectiva ' || v_Fechaefectiva;
            V_CRITERIOS := REPLACE(V_CRITERIOSORIGINAL, '@FECHA@', V_TEXTOFECHA);

            V_TEXTOERROR := V_PROCEDIMIENTO || ': 5 - Obtiene la posición del campo idpersona en el criterio';
            V_PRIMERAAPARICIONIDPERSONA := INSTR(V_CRITERIOS, 'IDPERSONA', 1);
      
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 6 - Obtiene la posición de la primera coma en el criterio';
            V_POSCOMA := INSTR(SUBSTR(V_CRITERIOS, 1, V_PRIMERAAPARICIONIDPERSONA), ',', -1);
      
            IF (V_POSCOMA = 0) THEN
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 7 - Obtiene la posición del primer espacio en el criterio';
                V_POSCOMA := INSTR(SUBSTR(V_CRITERIOS, 1, V_PRIMERAAPARICIONIDPERSONA), ' ', -1);
            END IF;
      
            V_TEXTOERROR := V_PROCEDIMIENTO || ': 8 - Obtiene la tabla principal';
            V_TABLAPRINCIPAL := TRIM(SUBSTR(V_CRITERIOS, V_POSCOMA + 1, V_PRIMERAAPARICIONIDPERSONA - V_POSCOMA - 1));

            IF (V_TABLAPRINCIPAL IS NOT NULL) THEN
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 9 - Transforma idpersona por el campo de la tabla';
                V_CRITERIOS := REPLACE(V_CRITERIOS, '@IDPERSONA@', V_TABLAPRINCIPAL || 'IDPERSONA');

                V_TEXTOERROR := V_PROCEDIMIENTO || ': 10 - Incluye en el criterio la condición que el servicio no puede estar de baja';
                V_CRITERIOS := V_CRITERIOS || 
                    ' AND NOT EXISTS ( ' ||
                        ' SELECT 1 ' ||
                        ' FROM PYS_SUSCRIPCION ' ||
                        ' WHERE PYS_SUSCRIPCION.IDINSTITUCION = ' || P_IDINSTITUCION ||
                            ' AND PYS_SUSCRIPCION.IDTIPOSERVICIOS = ' || DatosServicioAuto.IDTIPOSERVICIOS ||
                            ' AND PYS_SUSCRIPCION.IDSERVICIO = ' || DatosServicioAuto.IDSERVICIO ||
                            ' AND PYS_SUSCRIPCION.IDSERVICIOSINSTITUCION = ' || DatosServicioAuto.IDSERVICIOSINSTITUCION ||
                            ' AND TRUNC(PYS_SUSCRIPCION.FECHASUSCRIPCION) <= ' || V_TEXTOFECHA ||
                            ' AND (PYS_SUSCRIPCION.FECHABAJA IS NULL' ||
                            ' OR (TRUNC(PYS_SUSCRIPCION.FECHABAJA) >= ' || V_TEXTOFECHA ||
                            ' AND TRUNC(PYS_SUSCRIPCION.FECHABAJA) >= TRUNC(PYS_SUSCRIPCION.FECHASUSCRIPCION)))' || -- Busca suscripciones sin baja logica o con baja logica mayor o igual que hoy                                                                                    
                            ' AND PYS_SUSCRIPCION.IDINSTITUCION = ' || V_TABLAPRINCIPAL || 'IDINSTITUCION ' ||
                            ' AND PYS_SUSCRIPCION.IDPERSONA = ' || V_TABLAPRINCIPAL || 'IDPERSONA)';
                            
                            

                V_TEXTOERROR := V_PROCEDIMIENTO || ': 11 - Apertura del cursor para procesamiento';
                V_CURSOR := DBMS_SQL.OPEN_CURSOR;
                
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 12 - Analisis de la consulta (IDINSTITUCION=' || P_IDINSTITUCION || '; IDTIPOSERVICIOS=' || DatosServicioAuto.IDTIPOSERVICIOS || '; IDSERVICIO=' || DatosServicioAuto.IDSERVICIO || '; IDSERVICIOSINSTITUCION=' || DatosServicioAuto.IDSERVICIOSINSTITUCION || ')';
                DBMS_SQL.PARSE(V_CURSOR, V_CRITERIOS, DBMS_SQL.V7);
          
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 13 - Inclusión de variables de salida de la consulta';
                DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, V_IDINSTITUCION);
                DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 2, V_IDPERSONA);
                
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 14 - Ejecución de la consulta';
                V_DUMMY := DBMS_SQL.EXECUTE(V_CURSOR);                        

                -- 1. Damos de alta en el servicio automatico a las personas que no estaban suscritas y cumplen los criterios del servicio
                LOOP
                    EXIT WHEN DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0;
                    
                    V_TEXTOERROR := V_PROCEDIMIENTO || ': 15 - Recupera los valores de la consulta';
                    DBMS_SQL.COLUMN_VALUE(V_CURSOR, 1, V_IDINSTITUCION);
                    DBMS_SQL.COLUMN_VALUE(V_CURSOR, 2, V_IDPERSONA);
                    
                    V_TEXTOERROR := V_PROCEDIMIENTO || ': 16 - Llamada al procedimiento ALTA_SERVICIO_PERSONA';
                    ALTA_SERVICIO_PERSONA(
                        V_IDINSTITUCION,
                        DatosServicioAuto.IDTIPOSERVICIOS,
                        DatosServicioAuto.IDSERVICIO,
                        DatosServicioAuto.IDSERVICIOSINSTITUCION,
                        V_IDPERSONA,
                        V_FECHAEFECTIVA,
                        DatosServicioAuto.DESCRIPCION,
                        P_USUMODIFICACION,
                        P_CODRETORNO,
                        P_DATOSERROR);
                    IF (P_CODRETORNO <> '0') THEN
                        P_DATOSERROR := V_TEXTOERROR || ' - ' || P_DATOSERROR;
                        RAISE E_ERRORPROCEDIMIENTO;
                    END IF;                
                END LOOP;

                V_TEXTOERROR := V_PROCEDIMIENTO || ': 17 - Cierra el cursor';
                DBMS_SQL.CLOSE_CURSOR(V_CURSOR);

                -- 2. Damos de baja en el servicio automatico a las personas suscritas a ese servicio y que ya no cumplen las condiciones
                -- Pero la comprobacion de las condiciones solo se hace a fecha solicitada en caso de que sea posterior a la fecha de suscripcion original
                -- En otro caso, se comprueban las condiciones a futuro, a fecha de suscripcion que se comprueba

                V_TEXTOERROR := V_PROCEDIMIENTO || ': 18 - Transforma la fecha por la fecha efectiva';
                V_CRITERIOS := REPLACE(V_CRITERIOSORIGINAL, '@FECHA@',
                    'CASE WHEN TRUNC(PYS_SUSCRIPCION.FECHASUSCRIPCION) > ' || V_TEXTOFECHA || ' THEN PYS_SUSCRIPCION.FECHASUSCRIPCION ELSE ' || V_TEXTOFECHA || ' END');
                
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 19 - Transforma el idpersona por el campo correspondiente';    
                V_CRITERIOS := REPLACE(V_CRITERIOS, '@IDPERSONA@', 'PYS_SUSCRIPCION.IDPERSONA');
                
                V_TEXTOERROR := V_PROCEDIMIENTO || ': 20 - Incluye en la consulta la relación con la tabla PYS_SUSCRIPCION';
                V_CRITERIOS := 'SELECT DISTINCT IDINSTITUCION, IDPERSONA ' ||
                    ' FROM PYS_SUSCRIPCION ' ||
                    ' WHERE IDINSTITUCION = ' || P_IDINSTITUCION ||
                    ' AND IDTIPOSERVICIOS = ' || DatosServicioAuto.IDTIPOSERVICIOS ||
                    ' AND IDSERVICIO = ' || DatosServicioAuto.IDSERVICIO ||
                    ' AND IDSERVICIOSINSTITUCION = ' || DatosServicioAuto.IDSERVICIOSINSTITUCION ||
                    ' AND TRUNC(FECHASUSCRIPCION) <= ' || V_TEXTOFECHA ||                                          
                    ' AND (FECHABAJA IS NULL' ||
                    ' OR (TRUNC(FECHABAJA) >= ' || V_TEXTOFECHA ||
                    ' AND TRUNC(FECHABAJA) >= TRUNC(FECHASUSCRIPCION)))' || -- Busca suscripciones sin baja logica o con baja logica mayor o igual que hoy                                                    
                    ' AND NOT EXISTS ( ' || V_CRITERIOS || ')';                    

                V_TEXTOERROR := V_PROCEDIMIENTO || ': 21 - Apertura del cursor para procesamiento';
                V_CURSOR := DBMS_SQL.OPEN_CURSOR;

                V_TEXTOERROR := V_PROCEDIMIENTO || ': 22 - Analisis de la consulta (IDINSTITUCION=' || P_IDINSTITUCION || '; IDTIPOSERVICIOS=' || DatosServicioAuto.IDTIPOSERVICIOS || '; IDSERVICIO=' || DatosServicioAuto.IDSERVICIO || '; IDSERVICIOSINSTITUCION=' || DatosServicioAuto.IDSERVICIOSINSTITUCION || ')';
                DBMS_SQL.PARSE(V_CURSOR, V_CRITERIOS, DBMS_SQL.V7);

                V_TEXTOERROR := V_PROCEDIMIENTO || ': 23 - Inclusión de variables de salida de la consulta';
                DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 1, V_IDINSTITUCION);
                DBMS_SQL.DEFINE_COLUMN(V_CURSOR, 2, V_IDPERSONA);

                V_TEXTOERROR := V_PROCEDIMIENTO || ': 24 - Ejecución de la consulta';
                V_DUMMY := DBMS_SQL.EXECUTE(V_CURSOR);

                LOOP
                    EXIT WHEN DBMS_SQL.FETCH_ROWS(V_CURSOR) = 0;
                    
                    V_TEXTOERROR := V_PROCEDIMIENTO || ': 25 - Recupera los valores de la consulta';
                    DBMS_SQL.COLUMN_VALUE(V_CURSOR, 1, V_IDINSTITUCION);
                    DBMS_SQL.COLUMN_VALUE(V_CURSOR, 2, V_IDPERSONA);
                    
                    V_TEXTOERROR := V_PROCEDIMIENTO || ': 26 - Llamada al procedimiento BAJA_SERVICIO_PERSONA';
                    BAJA_SERVICIO_PERSONA(
                        V_IDINSTITUCION,
                        DatosServicioAuto.IDTIPOSERVICIOS,
                        DatosServicioAuto.IDSERVICIO,
                        DatosServicioAuto.IDSERVICIOSINSTITUCION,
                        V_IDPERSONA,
                        V_FECHAEFECTIVA,
                        P_USUMODIFICACION,
                        P_CODRETORNO,
                        P_DATOSERROR);
                    IF (P_CODRETORNO <> '0') THEN
                        P_DATOSERROR := V_TEXTOERROR || ' - ' || P_DATOSERROR;
                        RAISE E_ERRORPROCEDIMIENTO;
                    END IF;                
                END LOOP;

                V_TEXTOERROR := V_PROCEDIMIENTO || ': 27 - Cierra el cursor';
                DBMS_SQL.CLOSE_CURSOR(V_CURSOR);
            END IF;                
        END LOOP;
        
        V_TEXTOERROR := V_PROCEDIMIENTO || ': 28 - Finaliza el proceso correctamente';
        P_CODRETORNO := '0';
        P_DATOSERROR := V_TEXTOERROR;   
        
        EXCEPTION
            WHEN E_ERRORPROCEDIMIENTO THEN
                DBMS_SQL.CLOSE_CURSOR(V_CURSOR);        
        
            WHEN E_ERRORCONTROLADO THEN
                P_CODRETORNO := '-1';
                P_DATOSERROR := V_TEXTOERROR;        
                
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := SQLERRM || ' - ' || V_TEXTOERROR;
                DBMS_SQL.CLOSE_CURSOR(V_CURSOR);               
    END PROCESO_REVISION_AUTO;
END PKG_SERVICIOS_AUTOMATICOS;
/
