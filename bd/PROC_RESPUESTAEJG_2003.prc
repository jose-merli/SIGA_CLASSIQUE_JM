CREATE OR REPLACE PROCEDURE PROC_RESPUESTAEJG_2003(P_IDINSTITUCION      IN CAJG_REMESARESOLUCION.IDINSTITUCION%type,
                                                   P_IDREMESARESOLUCION IN CAJG_REMESARESOLUCION.IDREMESARESOLUCION%TYPE,
                                                   P_DELIMITADOR        IN VARCHAR2,
                                                   P_NOMBREFICHERO      IN VARCHAR2,
                                                   P_USUMODIFICACION    IN ADM_USUARIOS.IDUSUARIO%TYPE)

 IS

  v_cabecera_linea     varchar2(3);
  v_tipointercambio     varchar2(3);
  V_NUMEJG             NUMBER(8);
  V_ANIO               NUMBER(4);
  V_REF_EJG            VARCHAR(15);
  V_NUMEROINTERCAMBIO  NUMBER(8);
  V_NUMINTERCAMBIOEJGREMESA      NUMBER(7);
  V_CAB_INTERCAMBIO_ID PCAJG_ALC_INT_CAB.CAB_INTERCAMBIO_ID%TYPE;

  V_TIPOERRORCODIGO        PCAJG_ALC_TIPOERRORINTERCAMBIO.ERROR_CODIGO%TYPE;
  V_TIPOCAMPOCODIGO        PCAJG_ALC_TIPOCAMPOCARGA.CAMPO_CODIGO%TYPE;
  V_DESCRIPCIONERROR   VARCHAR(100);
  
  V_TIPOERRORDESCRIPCION  PCAJG_ALC_TIPOERRORINTERCAMBIO.ERROR_DESCRIPCION%TYPE;
  V_TIPOERRORSOLUCION  PCAJG_ALC_TIPOERRORINTERCAMBIO.ERROR_SOLUCION%TYPE;
  V_TIPOCAMPODESCRIPCION PCAJG_ALC_TIPOCAMPOCARGA.CAMPO_DESCRIPCION%TYPE;
  V_ERROR varchar2(700);

  err_num     number;
  err_msg     varchar2(255);
  var_num_reg varchar2(1);

  EJG_NOT_FOUND EXCEPTION;
  EJG_TOO_MANY_ROWS EXCEPTION;
  REMESA_NOT_FOUND EXCEPTION;
  REMESA_TOO_MANY_ROWS EXCEPTION;
  INTERCAMBIO_NOT_ALLOWED EXCEPTION;
  EJG_ANIO           SCS_EJG.ANIO%TYPE;
  EJG_NUMERO         SCS_EJG.NUMERO%TYPE;
  EJG_IDTIPOEJG      SCS_EJG.IDTIPOEJG%TYPE;
  REMESA_IDEJGREMESA CAJG_EJGREMESA.IDEJGREMESA%TYPE;
  REMESA_IDREMESA    CAJG_EJGREMESA.IDREMESA%TYPE;

  CURSOR C_DATOS is
    SELECT RF.IDREMESARESOLUCIONFICHERO, RF.LINEA
      FROM CAJG_REMESARESOLUCIONFICHERO RF
     WHERE RF.IDINSTITUCION = P_IDINSTITUCION
       AND RF.IDREMESARESOLUCION = P_IDREMESARESOLUCION
     ORDER BY RF.IDREMESARESOLUCIONFICHERO;

begin

  FOR REG IN C_DATOS LOOP
  
    BEGIN
      v_cabecera_linea := substr(REG.LINEA, 0, 3);
    
      IF v_cabecera_linea = 'CAB' THEN
        --inicializamos todas las variables
        V_NUMEJG            := null;
        V_ANIO              := null;
        V_NUMEROINTERCAMBIO := null;
        V_NUMINTERCAMBIOEJGREMESA       := null;
        EJG_ANIO            := null;
        EJG_NUMERO          := null;
        EJG_IDTIPOEJG       := null;
        V_ERROR  := null;
      
      ELSIF v_cabecera_linea = 'CAO' THEN
        v_tipointercambio := substr(REG.LINEA, 14, 3);
        
        IF v_tipointercambio <> '002' AND v_tipointercambio <> '001' THEN
           RAISE INTERCAMBIO_NOT_ALLOWED;
        END IF;
        
        V_NUMEROINTERCAMBIO := substr(REG.LINEA, 17, 7);
        -- En el envio estamos multiplicando por 10 el numero de intercambio para poder enviar mas de una actualizacion por expediente
        V_NUMINTERCAMBIOEJGREMESA := trunc(V_NUMEROINTERCAMBIO/10);
        V_NUMEROINTERCAMBIO := V_NUMINTERCAMBIOEJGREMESA*10;

      
      ELSIF v_cabecera_linea = 'DER' THEN
        V_TIPOERRORCODIGO := substr(REG.LINEA, 4, 3);
        V_DESCRIPCIONERROR := substr(REG.LINEA, 7, 100);
        V_TIPOCAMPOCODIGO := substr(REG.LINEA, 107, 12);
      
        BEGIN
          --buscamos la remesa el EJG
          SELECT IDEJGREMESA, IDREMESA, ANIO, NUMERO, IDTIPOEJG
            INTO REMESA_IDEJGREMESA,
                 REMESA_IDREMESA,
                 EJG_ANIO,
                 EJG_NUMERO,
                 EJG_IDTIPOEJG
            FROM CAJG_EJGREMESA
           WHERE TO_NUMBER(V_NUMINTERCAMBIOEJGREMESA) = NUMEROINTERCAMBIO
             AND IDINSTITUCION = P_IDINSTITUCION;
        
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            RAISE REMESA_NOT_FOUND;
          WHEN TOO_MANY_ROWS THEN
            RAISE REMESA_TOO_MANY_ROWS;
        END;

            
        
        
        
        IF var_num_reg IS NULL THEN
--          var_num_reg := '1';
          
          --actualizamos la remesa de resultados poniendo la remesa a la que modifica sus respuesta SOLO EN EL PRIMER REGISTRO
           UPDATE CAJG_REMESARESOLUCION R SET R.IDREMESA = REMESA_IDREMESA
                      WHERE R.IDINSTITUCION = P_IDINSTITUCION
                      AND R.IDTIPOREMESA = 3
                      AND R.IDREMESARESOLUCION = P_IDREMESARESOLUCION;
            
          
          
          BEGIN
            SELECT MAX(CAB.CAB_INTERCAMBIO_ID)
              INTO V_CAB_INTERCAMBIO_ID
              FROM PCAJG_ALC_INT_CAB CAB
             WHERE CAB.CAB2_NUMERO_INTERCAMBIO = V_NUMEROINTERCAMBIO;
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              RAISE REMESA_NOT_FOUND;
            
          END;
        
        
        END IF;
      
        BEGIN
          SELECT TE.ERROR_DESCRIPCION,TE.ERROR_SOLUCION
            INTO V_TIPOERRORDESCRIPCION,V_TIPOERRORSOLUCION
            FROM PCAJG_ALC_TIPOERRORINTERCAMBIO TE
           WHERE TE.ERROR_CODIGO = V_TIPOERRORCODIGO;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            V_TIPOERRORDESCRIPCION := V_TIPOERRORCODIGO ||
                                 ' Campo no definido en PCAJG_ALC_TIPOERRORINTERCAMBIO';
          
        END;
      
        IF V_TIPOERRORCODIGO = '002' THEN
          --Exp. duplicado
          --ACTUALIZAMOS LA FECHARESPUESTA Y EL ESTADO DEL HISTORICO DE ENVIO
          UPDATE PCAJG_ALC_INT_CAB
             SET CAB_FECHARESPUESTA     = SYSDATE,
                 CAB_EST_ID             = 1,
                 CAB_ANT_EST_ID         = 4,
                 CAB_ANT_FECHARESPUESTA = SYSDATE
           WHERE CAB2_NUMERO_INTERCAMBIO = V_NUMEROINTERCAMBIO;
        
          UPDATE PCAJG_ALC_INT_DOC
             SET DOC_DOCUMENTACION_ID        = NULL,
                 DOC1_DOCUMENTACION_APORTADA = NULL
           WHERE DOC_DOCUMENTACION_ID = V_CAB_INTERCAMBIO_ID;
          UPDATE PCAJG_ALC_INT_DOM
             SET DOM1_TIPO_DOMICILIO            = NULL,
                 DOM2_PAIS_DOMICILIO            = NULL,
                 DOM3_PROVINCIA_DOMICILIO       = NULL,
                 DOM4_DESCRIP_PROVIN_DOMICI_EXT = NULL,
                 DOM5_MUNICIPIO_DOMICILIO_NORM  = NULL,
                 DOM6_DESC_MUNIC_DOM_SINNORM    = NULL,
                 DOM7_TIPO_NUMERACION           = NULL,
                 DOM8_PISO                      = NULL,
                 DOM9_TIPO_VIA                  = NULL,
                 DOM10_DESCRIPCION_TIPO_VIA     = NULL,
                 DOM11_NUMERO_PORTAL            = NULL,
                 DOM12_DESCRIPCION_VIA_EXTRANJ  = NULL,
                 DOM13_DESCRIPCION_VIA          = NULL,
                 DOM14_DESCRIPCION_VIA_COMPLETA = NULL,
                 DOM15_CODIGO_POSTAL            = NULL
           WHERE DOM_INTERCAMBIO_ID = V_CAB_INTERCAMBIO_ID;
          UPDATE PCAJG_ALC_INT_ECO
             SET ECO1_CONCEPTO_INGRESO_ANUAL    = NULL,
                 ECO2_IMPORTE_INGRESO_ANUAL     = NULL,
                 ECO3_CONCEPTO_INGRESO_MENSUAL  = NULL,
                 ECO4_IMPORTE_INGRESO_MENSUAL   = NULL,
                 ECO5_CONCEPTO_OTROS_INGRESOS   = NULL,
                 ECO6_IMPORTE_OTROS_INGRESOS    = NULL,
                 ECO7_PROPIEDADES_INMUEBLES     = NULL,
                 ECO8_IMPORTE_PROP_INMUEBLES    = NULL,
                 ECO9_OBLIGACIONES_ECONOMICAS   = NULL,
                 ECO10_IMPORTE_OBLIGAC_ECONOMIC = NULL,
                 ECO11_DESCRIP_INF_ECONOMICA    = NULL
           WHERE ECO_INTERCAMBIO_ID = V_CAB_INTERCAMBIO_ID;
          UPDATE PCAJG_ALC_INT_EXP
             SET EXP1_NUM_EXPEDIENTE           = NULL,
                 EXP2_ANIO_EXPEDIENTE          = NULL,
                 EXP3_TIPO_EXPEDIENTE          = NULL,
                 EXP4_TIPO_EXPEDIENTE_EXTRAORD = NULL,
                 EXP5_FECHA_SOLICITUD          = NULL,
                 EXP6_COLEGIO_ABOGADOS         = NULL,
                 EXP7_CALIFICACION             = NULL,
                 EXP8_FECHA_DICTAMEN           = NULL,
                 EXP9_FUNDAMENTO_CALIFICACION  = NULL,
                 EXP10_PRECISA_ABOGADO         = NULL,
                 EXP11_PRECISA_PROCURADOR      = NULL,
                 EXP12_PROCEDENCIA             = NULL,
                 EXP13_OBSERVACIONES           = NULL,
                 EXP14_EEJG                    = NULL
           WHERE EXP_INTERCAMBIO_ID = V_CAB_INTERCAMBIO_ID;
          UPDATE PCAJG_ALC_INT_PRD
             SET PRD1_NCOLEGIADOABOGADO         = NULL,
                 PRD2_FECHA_DESIGNA             = NULL,
                 PRD3_NUMERO_DESIGNA            = NULL,
                 PRD4_ANIO_DESIGNA              = NULL,
                 PRD5_NCOLEGIADO_PROCURADOR     = NULL,
                 PRD6_ANIO_DESIGNA_PROCURADOR   = NULL,
                 PRD7_FECHA_DESIGNA_PROCURADOR  = NULL,
                 PRD8_NUMERO_DESIGNA_PROCURADOR = NULL
           WHERE PRD_INTERCAMBIO_ID = V_CAB_INTERCAMBIO_ID;
          UPDATE PCAJG_ALC_INT_PRJ
             SET PRJ1_ORDEN_JURISDICCIONAL     = NULL,
                 PRJ2_CLASE_ASUNTO_TIPO_ORDEN  = NULL,
                 PRJ3_ORGANO_JUDICIAL          = NULL,
                 PRJ4_TIPO_PROCED_JUDICIAL     = NULL,
                 PRJ5_NUM_PROCEDIMIENTO        = NULL,
                 PRJ6_ANIO_PROCEDIMIENTO       = NULL,
                 PRJ7_NATURALEZA_PROCEDIMIENTO = NULL,
                 PRJ8_ESTADO_PROCEDIMIENTO     = NULL,
                 PRJ9_DESCRIPCION_PROCEDIMIEN  = NULL,
                 PRJ10_ACUSACION_PARTICULAR    = NULL,
                 PRJ11_PETICION_PARTICULAR     = NULL,
                 PRJ12_PARTE_CONTRARIA         = NULL
           WHERE PRJ_INTERCAMBIO_ID = V_CAB_INTERCAMBIO_ID;
          UPDATE PCAJG_ALC_INT_SOL
             SET SOL1_TIPO_IDENTIFICACION      = NULL,
                 SOL2_IDENTIFICACION_SOLICIT   = NULL,
                 SOL3_TIPOPERSONA              = NULL,
                 SOL4_NOMBRE_SOLICITANTE       = NULL,
                 SOL5_APELLIDO1_SOLICITANTE    = NULL,
                 SOL6_APELLIDO2_SOLICITANTE    = NULL,
                 SOL7_SEXO_SOLICITANTE         = NULL,
                 SOL8_NACIONALIDAD             = NULL,
                 SOL9_SITUACION_PERSONAL       = NULL,
                 SOL10_FECHA_NACIMIENTO        = NULL,
                 SOL11_PROFESION               = NULL,
                 SOL12_REGIMEN_ECONOMICO       = NULL,
                 SOL13_NOMBRE_ENTIDAD_JURIDICA = NULL,
                 SOL14_USO_DOMICILIO           = NULL,
                 SOL15_TIPO_TELEFONO           = NULL,
                 SOL16_NUMERO_TELEFONO         = NULL,
                 SOL17_PRESO                   = NULL,
                 SOL18_CENTRO_PENITENCIARIO    = NULL
           WHERE SOL_INTERCAMBIO_ID = V_CAB_INTERCAMBIO_ID;
        
          V_ERROR := V_TIPOERRORDESCRIPCION|| ' ' ||V_TIPOERRORSOLUCION ;
        
        ELSIF V_TIPOERRORCODIGO = '003' THEN
          --Exp. No existente
          --    3 Recibida respuesta incorrecta (Exp. No existente) 
          --ACTUALIZAMOS LA FECHARESPUESTA Y EL ESTADO DEL HISTORICO DE ENVIO
          UPDATE PCAJG_ALC_INT_CAB
             SET CAB_EST_ID             = 3,
                 CAB_FECHARESPUESTA     = SYSDATE,
                 CAB_ANT_EST_ID         = CAB_EST_ID,
                 CAB_ANT_FECHARESPUESTA = CAB_FECHARESPUESTA
           WHERE CAB2_NUMERO_INTERCAMBIO IN
                 (SELECT NUMEROINTERCAMBIO
                    FROM CAJG_EJGREMESA
                   WHERE (IDINSTITUCION, ANIO, NUMERO, IDTIPOEJG) IN
                         (SELECT IDINSTITUCION, ANIO, NUMERO, IDTIPOEJG
                            FROM CAJG_EJGREMESA
                           WHERE IDINSTITUCION = P_IDINSTITUCION
                             AND NUMEROINTERCAMBIO = V_NUMEROINTERCAMBIO));
          V_ERROR := V_TIPOERRORDESCRIPCION|| ' ' ||V_TIPOERRORSOLUCION ;
        ELSE
          --(2 Recibida respuesta incorrecta)
          IF( TRIM(V_TIPOCAMPOCODIGO) IS NOT NULL ) THEN
            BEGIN
              SELECT CC.CAMPO_DESCRIPCION
                INTO V_TIPOCAMPODESCRIPCION
                FROM PCAJG_ALC_TIPOCAMPOCARGA CC
               WHERE CC.CAMPO_CODIGO = V_TIPOCAMPOCODIGO;
            EXCEPTION
              WHEN NO_DATA_FOUND THEN
                V_TIPOCAMPODESCRIPCION := V_TIPOCAMPOCODIGO ||
                                      ' Campo no definido en PCAJG_ALC_TIPOCAMPOCARGA';
            
            END;
            V_ERROR := V_TIPOCAMPODESCRIPCION || ' ' || V_TIPOERRORDESCRIPCION ||' '||V_DESCRIPCIONERROR|| ' ' ||V_TIPOERRORSOLUCION;  
          ELSE
             V_ERROR := V_DESCRIPCIONERROR || ' ' || V_TIPOERRORDESCRIPCION|| ' ' ||V_TIPOERRORSOLUCION;  
          END IF;
          
        
          --ACTUALIZAMOS LA FECHARESPUESTA Y EL ESTADO DEL HISTORICO DE ENVIO
          UPDATE PCAJG_ALC_INT_CAB
             SET CAB_FECHARESPUESTA = SYSDATE, CAB_EST_ID = 2
           WHERE CAB2_NUMERO_INTERCAMBIO = V_NUMEROINTERCAMBIO;
           

           
           UPDATE PCAJG_ALC_INT_CAB
             SET CAB_FECHARESPUESTA = SYSDATE, CAB_EST_ID = 2
           WHERE CAB2_NUMERO_INTERCAMBIO = V_NUMEROINTERCAMBIO;
           
           
        END IF;
      
      
      IF var_num_reg IS NULL THEN
          var_num_reg := '1';
      --mETEMOS EL ESTADO DEVUELTO AL COLEGIO
      
      
          INSERT INTO SCS_ESTADOEJG
            (IDINSTITUCION,
             IDTIPOEJG,
             ANIO,
             NUMERO,
             IDESTADOPOREJG,
             IDESTADOEJG,
             FECHAINICIO,
             FECHAMODIFICACION,
             USUMODIFICACION,
             OBSERVACIONES,
             AUTOMATICO,
             PROPIETARIOCOMISION,
             FECHABAJA)
          VALUES
            (P_IDINSTITUCION,
             EJG_IDTIPOEJG,
             EJG_ANIO,
             EJG_NUMERO,
             (SELECT (MAX(IDESTADOPOREJG) + 1) AS ESTADOPOREJG
                FROM SCS_ESTADOEJG
               WHERE IDINSTITUCION = P_IDINSTITUCION
                 AND ANIO = EJG_ANIO
                 AND NUMERO = EJG_NUMERO
                 AND IDTIPOEJG = EJG_IDTIPOEJG),
             21,
             SYSDATE,
             SYSDATE,
             1,
             V_ERROR,
             1,
             NULL,
             NULL);
        END IF;
        --CAMBIAMOS EL CAMPO RECIBIDA DEL EJG INCLUIDO EN LA REMESA A 2 Recibida respuesta incorrecta
        UPDATE CAJG_EJGREMESA
           SET RECIBIDA = 2
         WHERE IDEJGREMESA = REMESA_IDEJGREMESA;
         
        --BORRAMOS LAS RESPUESTAS QUE TUVIERA
--        DELETE FROM CAJG_RESPUESTA_EJGREMESA CR        WHERE CR.IDEJGREMESA = REMESA_IDEJGREMESA;
        -- ISERTAMOS EL ERROR QUE SE MOSTRARA AL USUARIO
        INSERT INTO CAJG_RESPUESTA_EJGREMESA
          (IDRESPUESTA,
           IDEJGREMESA,
           CODIGO,
           DESCRIPCION,
           ABREVIATURA,
           FECHA,
           FECHAMODIFICACION,
           USUMODIFICACION,
           IDTIPORESPUESTA)
        VALUES
          (SEQ_CAJG_RESPUESTA_EJGREMESA.NEXTVAL,
           REMESA_IDEJGREMESA,
           -1,
           V_ERROR,
           null,
           sysdate,
           sysdate,
           P_USUMODIFICACION,
           2);
      
      END IF;
    
    EXCEPTION
      WHEN EJG_NOT_FOUND THEN
        --si no encontramos el EJG
        UPDATE CAJG_REMESARESOLUCIONFICHERO
           SET IDERRORESREMESARESOL = 1, PARAMETROSERROR = V_REF_EJG
         WHERE IDREMESARESOLUCIONFICHERO = REG.IDREMESARESOLUCIONFICHERO;
      
      WHEN EJG_TOO_MANY_ROWS THEN
        --si encuentro mas de un EJG
        UPDATE CAJG_REMESARESOLUCIONFICHERO
           SET IDERRORESREMESARESOL = 2, PARAMETROSERROR = V_REF_EJG
         WHERE IDREMESARESOLUCIONFICHERO = REG.IDREMESARESOLUCIONFICHERO;
      WHEN REMESA_NOT_FOUND THEN
        --NO ENCEUNTRO EGJREMESA `PARA EL NUEMRO DE INTERCAMBIO Y EL EJG
        UPDATE CAJG_REMESARESOLUCIONFICHERO
           SET IDERRORESREMESARESOL = 16, PARAMETROSERROR = V_REF_EJG
         WHERE IDREMESARESOLUCIONFICHERO = REG.IDREMESARESOLUCIONFICHERO;
      WHEN REMESA_TOO_MANY_ROWS THEN
        --NO ENCEUNTRO EGJREMESA `PARA EL NUEMRO DE INTERCAMBIO Y EL EJG
        UPDATE CAJG_REMESARESOLUCIONFICHERO
           SET IDERRORESREMESARESOL = 17, PARAMETROSERROR = V_REF_EJG
         WHERE IDREMESARESOLUCIONFICHERO = REG.IDREMESARESOLUCIONFICHERO;
      WHEN INTERCAMBIO_NOT_ALLOWED THEN
           UPDATE CAJG_REMESARESOLUCIONFICHERO
           SET IDERRORESREMESARESOL = 18, PARAMETROSERROR = V_REF_EJG
         WHERE IDREMESARESOLUCIONFICHERO = REG.IDREMESARESOLUCIONFICHERO;
      
      
      
      WHEN OTHERS THEN
        --si ocurre cualquier otro error
        err_num := SQLCODE;
        err_msg := SQLERRM;
        UPDATE CAJG_REMESARESOLUCIONFICHERO
           SET IDERRORESREMESARESOL = -2,
               PARAMETROSERROR      = V_REF_EJG || ',' || err_num || ',' ||
                                      err_msg
         WHERE IDREMESARESOLUCIONFICHERO = REG.IDREMESARESOLUCIONFICHERO;
    END;
  
  END LOOP;

end PROC_RESPUESTAEJG_2003;
