CREATE OR REPLACE PACKAGE PKG_SIGA_ACCIONES_GUARDIAS IS

    /****************************************************************************************************
    Nombre: PROC_ACCIONES_GUARDIAS
    Descripcion: Procedimiento que comprueba las acciones que puede hacer en una guardia (Sustituir, Anular, Borrar, Permutar)
    1. Si NO esta facturado, entonces se podra sustituir y anular
    2. Si NO esta facturado, el dia de guardia es posterior al actual, existe la guardia y no tiene asistencias, entonces se podra borrar
    3. Si NO esta facturado, el dia de guardia es posterior al actual, existe la guardia y no tiene una permuta pendiente, entonces se podra permutar

    Parametros (IN/OUT - Descripcion -Tipo de Datos - Valores)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_IDPERSONA - IN - Identificador de la persona - NUMBER
    - P_FECHA - IN - Fecha de la guardia - DATE
    - P_SUSTITUIR - OUT - Indica si es sustituible la guardia - VARCHAR2 - 'N': no sustituible; 'S': sustituible
    - P_ANULAR - OUT - Indica si es anulable la guardia - VARCHAR2 - 'N': no anulable; 'S': anulable
    - P_BORRAR - OUT - Indica si es borrable la guardia - VARCHAR2 - 'N': no borrable; 'S': borrable
    - P_PERMUTAR - OUT - Indica si es permutable la guardia - VARCHAR2 - 'N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
    - P_ASISTENCIA - OUT - Indica si tiene asistencia asociada - VARCHAR2 - 'N': sin Asistencia; 'S': con asistencia
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/03/2017 - Jorge Paez Trivino - Version inicial
  ****************************************************************************************************/ 
    PROCEDURE PROC_ACCIONES_GUARDIAS(
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_IDPERSONA IN NUMBER,
        P_FECHA IN DATE,
        P_SUSTITUIR OUT VARCHAR2, -- 'N': no sustituible; 'S': sustituible
        P_ANULAR OUT VARCHAR2, -- 'N': no anulable; 'S': anulable
        P_BORRAR OUT VARCHAR2, -- 'N': no borrable; 'S': borrable
        P_PERMUTAR OUT VARCHAR2, -- 'N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
        P_ASISTENCIA OUT VARCHAR2, -- 'N': sin Asistencia; 'S': con asistencia
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2);
        
    /****************************************************************************************************
    Nombre: FUNC_ACCIONES_GUARDIAS
    Descripcion: Funcion que comprueba las acciones que puede hacer en una guardia (Sustituir, Anular, Borrar, Permutar)
    1. Si NO esta facturado, entonces se podra sustituir y anular
    2. Si NO esta facturado, el dia de guardia es posterior al actual, existe la guardia y no tiene asistencias, entonces se podra borrar
    3. Si NO esta facturado, el dia de guardia es posterior al actual, existe la guardia y no tiene una permuta pendiente, entonces se podra permutar

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_IDPERSONA - IN - Identificador de la persona - NUMBER
    - P_FECHA - IN - Fecha de la guardia - DATE
    - RETORNA SUSTITUIR(1) || ANULAR(1) || BORRAR(1) || PERMUTAR(1) || ASISTENCIA(1)
    -- SUSTITUIR VARCHAR2(1); -- 'N': no sustituible; 'S': sustituible
    -- ANULAR VARCHAR2(1); -- 'N': no anulable; 'S': anulable
    -- BORRAR VARCHAR2(1); -- 'N': no borrable; 'S': borrable
    -- PERMUTAR VARCHAR2(1); -- N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
    -- ASISTENCIA VARCHAR2(1); -- 'N': sin Asistencia; 'S': con asistencia        

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/03/2017 - Jorge Paez Trivino - Version inicial
  ****************************************************************************************************/     
    FUNCTION FUNC_ACCIONES_GUARDIAS(
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_IDPERSONA IN NUMBER,
        P_FECHA IN DATE) RETURN VARCHAR2;        

END PKG_SIGA_ACCIONES_GUARDIAS;
/
CREATE OR REPLACE PACKAGE BODY PKG_SIGA_ACCIONES_GUARDIAS IS

    /****************************************************************************************************
    Nombre: PROC_ACCIONES_GUARDIAS
    Descripcion: Procedimiento que comprueba las acciones que puede hacer en una guardia (Sustituir, Anular, Borrar, Permutar)
    1. Si NO esta facturado, entonces se podra sustituir y anular
    2. Si NO esta facturado, el dia de guardia es posterior al actual, existe la guardia y no tiene asistencias, entonces se podra borrar
    3. Si NO esta facturado, el dia de guardia es posterior al actual, existe la guardia y no tiene una permuta pendiente, entonces se podra permutar

    Parametros (IN/OUT - Descripcion -Tipo de Datos - Valores)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_IDPERSONA - IN - Identificador de la persona - NUMBER
    - P_FECHA - IN - Fecha de la guardia - DATE
    - P_SUSTITUIR - OUT - Indica si es sustituible la guardia - VARCHAR2 - 'N': no sustituible; 'S': sustituible
    - P_ANULAR - OUT - Indica si es anulable la guardia - VARCHAR2 - 'N': no anulable; 'S': anulable
    - P_BORRAR - OUT - Indica si es borrable la guardia - VARCHAR2 - 'N': no borrable; 'S': borrable
    - P_PERMUTAR - OUT - Indica si es permutable la guardia - VARCHAR2 - 'N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
    - P_ASISTENCIA - OUT - Indica si tiene asistencia asociada - VARCHAR2 - 'N': sin Asistencia; 'S': con asistencia
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/03/2017 - Jorge Paez Trivino - Version inicial
  ****************************************************************************************************/    
    PROCEDURE PROC_ACCIONES_GUARDIAS(
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_IDPERSONA IN NUMBER,
        P_FECHA IN DATE,
        P_SUSTITUIR OUT VARCHAR2, -- 'N': no sustituible; 'S': sustituible
        P_ANULAR OUT VARCHAR2, -- 'N': no anulable; 'S': anulable
        P_BORRAR OUT VARCHAR2, -- 'N': no borrable; 'S': borrable
        P_PERMUTAR OUT VARCHAR2, -- 'N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
        P_ASISTENCIA OUT VARCHAR2, -- 'N': sin Asistencia; 'S': con asistencia
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS      
        
        V_FACTURADO NUMBER; -- 0:NoFacturado; 1:Facturado
        V_EXISTE_GUARDIA NUMBER; -- 0:NoExisteGuardia; 1:ExisteGuardia
        V_PERMUTACION NUMBER; -- 0:PermutaPendienteSolicitante; 1:Permutada; 2:PermutaPendienteConfirmador 
          
    BEGIN
        P_SUSTITUIR := 'N'; -- No sustituible
        P_ANULAR := 'N'; -- No anulable
        P_BORRAR := 'N'; -- No borrable
        P_PERMUTAR := 'N'; -- No permutable
        P_ASISTENCIA := 'N'; -- Sin Asistencia
        
        -- Paso 1. Busca si esta facturado
        BEGIN             
            SELECT 1 INTO V_FACTURADO
            FROM FCS_FACT_APUNTE
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND IDTURNO = P_IDTURNO
                AND IDGUARDIA = P_IDGUARDIA
                AND IDPERSONA = P_IDPERSONA
                AND TRUNC(FECHAINICIO) = TRUNC(P_FECHA)
                AND ROWNUM = 1; -- Con una ya esta facturada 
        
            EXCEPTION WHEN NO_DATA_FOUND THEN
                V_FACTURADO := 0; -- No esta facturado
        END;
                
        -- Compruebo que NO esta facturado
        IF (V_FACTURADO = 0) THEN -- 0:NoFacturado; 1:Facturado
        
            -- Si NO esta facturado, entonces se podra sustituir y anular
            P_SUSTITUIR := 'S';
            P_ANULAR := 'S';
            
            -- Paso 2. Busco si tiene asistencias asociadas
            BEGIN
                SELECT 'S' INTO P_ASISTENCIA
                FROM SCS_ASISTENCIA
                WHERE IDINSTITUCION = P_IDINSTITUCION
                    AND IDTURNO = P_IDTURNO
                    AND IDGUARDIA = P_IDGUARDIA
                    AND IDPERSONACOLEGIADO = P_IDPERSONA
                    AND TRUNC(FECHAHORA) = TRUNC(P_FECHA)
                    AND ROWNUM = 1; -- Con una ya tiene asistencias
                    
                EXCEPTION WHEN NO_DATA_FOUND THEN
                    P_ASISTENCIA := 'N'; -- No tiene asistencias asociadas
            END;

            -- Paso 3. Compruebo que el dia de la guardia es posterior al dia actual 
            IF TRUNC(SYSDATE) < TRUNC(P_FECHA) THEN
            
                -- Paso 4. Compruebo que existe la guardia
                BEGIN
                    SELECT 1 INTO V_EXISTE_GUARDIA
                    FROM SCS_CABECERAGUARDIAS
                    WHERE IDINSTITUCION = P_IDINSTITUCION
                        AND IDTURNO = P_IDTURNO
                        AND IDGUARDIA = P_IDGUARDIA
                        AND IDPERSONA = P_IDPERSONA
                        AND TRUNC(FECHAINICIO) = TRUNC(P_FECHA)
                        AND ROWNUM = 1; -- Con una ya existe la guardia
        
                    EXCEPTION WHEN NO_DATA_FOUND THEN
                        V_EXISTE_GUARDIA := 0; -- No existe la guardia
                END;
                
                -- Compruebo que existe la guardia
                IF (V_EXISTE_GUARDIA = 1) THEN -- 0:NoExisteGuardia; 1:ExisteGuardia
                
                    /* Se puede borrar si se cumple lo siguiente:
                        - NO esta facturado
                        - El dia de guardia es posterior al actual
                        - Existe la guardia
                        - Sin asistencias asociadas*/
                    IF (P_ASISTENCIA = 'N') THEN -- 'N': sin Asistencia; 'S': con asistencia
                        P_BORRAR := 'S';
                    END IF;
                    
                    -- Paso 5. Compruebo si es permutable 
                    BEGIN
                        SELECT PERMUTACION INTO V_PERMUTACION -- 0:PermutaPendienteSolicitante; 1:Permutada; 2:PermutaPendienteConfirmador 
                        FROM (             
                            SELECT FECHAMODIFICACION, NVL2(FECHACONFIRMACION, 1, DECODE(IDPERSONA_CONFIRMADOR, P_IDPERSONA, 2, 0)) AS PERMUTACION
                            FROM SCS_PERMUTAGUARDIAS
                            WHERE IDINSTITUCION = P_IDINSTITUCION
                                AND (
                                    (
                                        IDTURNO_CONFIRMADOR = P_IDTURNO 
                                        AND IDGUARDIA_CONFIRMADOR = P_IDGUARDIA
                                        AND IDPERSONA_CONFIRMADOR = P_IDPERSONA
                                        AND TRUNC(FECHAINICIO_CONFIRMADOR) = TRUNC(P_FECHA)
                                    ) OR (
                                        IDTURNO_SOLICITANTE = P_IDTURNO 
                                        AND IDGUARDIA_SOLICITANTE = P_IDGUARDIA
                                        AND IDPERSONA_SOLICITANTE = P_IDPERSONA
                                        AND TRUNC(FECHAINICIO_SOLICITANTE) = TRUNC(P_FECHA)
                                    )
                                )
                            ORDER BY FECHAMODIFICACION DESC                        
                        ) TABLA_PERMUTAGUARDIAS
                        WHERE ROWNUM = 1;                        
                  
                        EXCEPTION WHEN NO_DATA_FOUND THEN
                                V_PERMUTACION := 1; -- No tiene permutas
                    END;
                    
                    -- Compruebo que NO tiene permutas pendientes
                    IF (V_PERMUTACION = 1) THEN
                    
                        /* Se puede permutar si se cumple lo siguiente:
                            - NO esta facturado
                            - El dia de guardia es posterior al actual
                            - Existe la guardia
                            - Permutada o sin permutar*/
                        P_PERMUTAR := 'S';
                           
                    ELSIF (V_PERMUTACION = 2) THEN -- Pendiente del confirmador
                        P_PERMUTAR := 'P';
                    END IF; -- Sin permutas Pendientes
                END IF; -- Existe la guardia
            END IF; -- Fecha posterior a la actual
        END IF; -- Sin Facturar

        P_DATOSERROR := 'PROC_ACCIONES_GUARDIAS: Finalizado correctamente';
        P_CODRETORNO := '0';

        EXCEPTION
            WHEN OTHERS THEN
                P_CODRETORNO := TO_CHAR(SQLCODE);
                P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;        
    END PROC_ACCIONES_GUARDIAS;
    
    /****************************************************************************************************
    Nombre: FUNC_ACCIONES_GUARDIAS
    Descripcion: Funcion que comprueba las acciones que puede hacer en una guardia (Sustituir, Anular, Borrar, Permutar)
    1. Si NO esta facturado, entonces se podra sustituir y anular
    2. Si NO esta facturado, el dia de guardia es posterior al actual, existe la guardia y no tiene asistencias, entonces se podra borrar
    3. Si NO esta facturado, el dia de guardia es posterior al actual, existe la guardia y no tiene una permuta pendiente, entonces se podra permutar

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_IDPERSONA - IN - Identificador de la persona - NUMBER
    - P_FECHA - IN - Fecha de la guardia - DATE
    - RETORNA SUSTITUIR(1) || ANULAR(1) || BORRAR(1) || PERMUTAR(1) || ASISTENCIA(1)
    -- SUSTITUIR VARCHAR2(1); -- 'N': no sustituible; 'S': sustituible
    -- ANULAR VARCHAR2(1); -- 'N': no anulable; 'S': anulable
    -- BORRAR VARCHAR2(1); -- 'N': no borrable; 'S': borrable
    -- PERMUTAR VARCHAR2(1); -- N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
    -- ASISTENCIA VARCHAR2(1); -- 'N': sin Asistencia; 'S': con asistencia        

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/03/2017 - Jorge Paez Trivino - Version inicial
  ****************************************************************************************************/        
    FUNCTION FUNC_ACCIONES_GUARDIAS(
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_IDPERSONA IN NUMBER,
        P_FECHA IN DATE) RETURN VARCHAR2 IS
        
        V_SUSTITUIR VARCHAR2(1); -- 'N': no sustituible; 'S': sustituible
        V_ANULAR VARCHAR2(1); -- 'N': no anulable; 'S': anulable
        V_BORRAR VARCHAR2(1); -- 'N': no borrable; 'S': borrable
        V_PERMUTAR VARCHAR2(1); -- N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
        V_ASISTENCIA VARCHAR2(1); -- 'N': sin Asistencia; 'S': con asistencia
        V_CODRETORNO VARCHAR2(10);
        V_DATOSERROR VARCHAR2(400);
        
    BEGIN   
    
        PROC_ACCIONES_GUARDIAS(
            P_IDINSTITUCION,
            P_IDTURNO,
            P_IDGUARDIA,
            P_IDPERSONA,
            P_FECHA,
            V_SUSTITUIR, -- 'N': no sustituible; 'S': sustituible
            V_ANULAR, -- 'N': no anulable; 'S': anulable
            V_BORRAR, -- 'N': no borrable; 'S': borrable
            V_PERMUTAR, -- N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
            V_ASISTENCIA, -- 'N': sin Asistencia; 'S': con asistencia
            V_CODRETORNO,
            V_DATOSERROR);
            
            -- Si hay error indico que no puedo hacer nada
            IF (V_CODRETORNO <> '0') THEN
                V_SUSTITUIR := 'N';
                V_ANULAR := 'N';
                V_BORRAR := 'N';
                V_PERMUTAR := 'N';
                V_ASISTENCIA := 'N';                
            END IF;      
            
        RETURN V_SUSTITUIR || V_ANULAR || V_BORRAR || V_PERMUTAR || V_ASISTENCIA;
    END FUNC_ACCIONES_GUARDIAS;

END PKG_SIGA_ACCIONES_GUARDIAS;
/
