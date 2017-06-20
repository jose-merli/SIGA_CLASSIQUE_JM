CREATE OR REPLACE Package Pkg_Siga_Retenciones_Sjcs Is

  Procedure Proc_Fcs_Aplicar_Retenc_Judic(p2_Idinstitucion   In Fcs_Pago_Colegiado.Idinstitucion%Type,
                                          p2_Idpago          In Fcs_Pago_Colegiado.Idpagosjg%Type,
                                          p2_Idpersona       In Fcs_Pago_Colegiado.Idperdestino%Type,
                                          p2_Importeneto     In Fcs_Pago_Colegiado.Impret%Type,
                                          p2_Usumodificacion In Fcs_Pago_Colegiado.Usumodificacion%Type,
                                          p2_Idioma          In Adm_Lenguajes.Idlenguaje%Type,
                                          p_Codretorno      Out Varchar2,
                                          p_Datoserror      Out Varchar2);

  Procedure Proc_Aplica_Retenciones_Nolec(p_Importenetorestante In Out Number, p_fechaMes In Date);
  Procedure Proc_Aplica_Retenciones_Lec(p_Importenetorestante In Number, p_fechaMes In Date);

  Procedure Proc_Fcs_Retencion_Persona(p_Idinstitucion    In Number,
                                       p_Idpago           In Number,
                                       p_Idpersona        In Number,
                                       p_Importeretencion Out Varchar2,
                                       p_Codretorno       Out Varchar2,
                                       p_Datoserror       Out Varchar2);

End;
 
/
create or replace package body PKG_SIGA_RETENCIONES_SJCS is

  -- Public variable declarations
  V_DATOSERROR VARCHAR2(4000);
  
  -- Parametros generales de todo el proceso
  p_idinstitucion   Fcs_Pago_Colegiado.Idinstitucion%Type;
  p_idpago          Fcs_Pago_Colegiado.Idpagosjg%Type;
  p_idpersona       Fcs_Pago_Colegiado.Idperdestino%Type;
  p_importeneto     Fcs_Pago_Colegiado.Impret%Type;
  p_usumodificacion Fcs_Pago_Colegiado.Usumodificacion%Type;
  p_idioma          Adm_Lenguajes.Idlenguaje%Type;

  -- Cursor de retenciones variable, segun el tipo pasado como parametro
  CURSOR C_RETENCIONES (p_tiporetencion Varchar2) IS
    Select Idretencion,
           Tiporetencion,
           Iddestinatario,
           Importe,
           Importe + Nvl((Select Sum(c.Importeretenido)
                           From Fcs_Cobros_Retencionjudicial c
                          Where c.Idinstitucion = Ret.Idinstitucion
                            And c.Idpersona = Ret.Idpersona
                            And c.Idretencion = Ret.Idretencion),
                         0) As Importependiente,
           (Select o.Orden
              From Fcs_Destinatarios_Retenciones o
             Where o.Idinstitucion = Ret.Idinstitucion
               And o.Iddestinatario = Ret.Iddestinatario) As Orden
      From Fcs_Retenciones_Judiciales Ret
     Where Idinstitucion = P_Idinstitucion
       And (Idpersona Is Null Or Idpersona = P_Idpersona)
       And Trunc(Fechainicio) <= Trunc(Sysdate)
       And (Fechafin Is Null Or Trunc(Fechafin) >= Trunc(Sysdate))
          
       And p_Tiporetencion Like '%' || Tiporetencion || '%'
          -- las retenciones periodicas no se terminan nunca por importe
       And (Tiporetencion = 'P' And Importe Is Not Null 
           Or
           -- las retenciones fijas se terminan por importe: asi que hay que comprobarlo
           (Tiporetencion = 'F' And Importe Is Not Null And
           Importe + Nvl((Select Sum(c.Importeretenido)
                             From Fcs_Cobros_Retencionjudicial c
                            Where c.Idinstitucion = Ret.Idinstitucion
                              And c.Idpersona = Ret.Idpersona
                              And c.Idretencion = Ret.Idretencion),
                           0) > 0)
           Or
           -- las retenciones LEC se terminan por importe, 
           -- solo si tienen importe: hay que comprobarlo en dicho caso
           (Tiporetencion = 'L' And Importe Is Not Null And
           Importe + Nvl((Select Sum(c.Importeretenido)
                             From Fcs_Cobros_Retencionjudicial c
                            Where c.Idinstitucion = Ret.Idinstitucion
                              And c.Idpersona = Ret.Idpersona
                              And c.Idretencion = Ret.Idretencion),
                           0) > 0)
           Or
           (Tiporetencion = 'L' And Importe Is Null))
    
     Order By Orden, Fechainicio, Fechaalta;

  /*
   * Hace un insert en FCS_COBROS_RETENCIONJUDICIAL en funcion de los parametros generales 
   * establecidos y los que le pasan como parametros.
   * Esto significa que apunta una retencion a la persona y el pago dado, por el importe pasado.
   */
  Procedure Proc_Apunta_Cobro_Retencion(p_Idretencion            In Fcs_Cobros_Retencionjudicial.Idretencion%Type,
                                        p_Importecobrado         In Fcs_Cobros_Retencionjudicial.Importeretenido%Type,
                                        p_Importeaplicaretencion In Fcs_Cobros_Retencionjudicial.Importeaplicaretencion%Type,
                                        p_Mes                    In Fcs_Cobros_Retencionjudicial.Mes%Type,
                                        p_Anio                   In Fcs_Cobros_Retencionjudicial.Anio%Type) Is
    v_Nuevoidcobro Fcs_Cobros_Retencionjudicial.Idcobro%Type;
  Begin
    Select Nvl(Max(Idcobro), 0) + 1
      Into v_Nuevoidcobro
      From Fcs_Cobros_Retencionjudicial
     Where Idpersona = p_Idpersona
       And Idinstitucion = p_Idinstitucion
       And Idretencion = p_Idretencion;
  
    Insert Into Fcs_Cobros_Retencionjudicial
      (Idinstitucion, Idpersona, Idpagosjg, Idcobro, Fecharetencion,
       Fechamodificacion, Usumodificacion,
       Idretencion, Importeretenido, Importeaplicaretencion, Mes, Anio)
    Values
      (p_Idinstitucion, p_Idpersona, p_Idpago, v_Nuevoidcobro, Sysdate,
       Sysdate, p_Usumodificacion,
       p_Idretencion, p_Importecobrado * -1, p_Importeaplicaretencion, p_Mes, p_Anio);
  End Proc_Apunta_Cobro_Retencion;

  /* 
   * Proceso principal de aplicacion de retenciones
   */
  Procedure Proc_Fcs_Aplicar_Retenc_Judic(p2_Idinstitucion   In Fcs_Pago_Colegiado.Idinstitucion%Type,
                                          p2_Idpago          In Fcs_Pago_Colegiado.Idpagosjg%Type,
                                          p2_Idpersona       In Fcs_Pago_Colegiado.Idperdestino%Type,
                                          p2_Importeneto     In Fcs_Pago_Colegiado.Impret%Type,
                                          p2_Usumodificacion In Fcs_Pago_Colegiado.Usumodificacion%Type,
                                          p2_Idioma          In Adm_Lenguajes.Idlenguaje%Type,
                                          p_Codretorno      Out Varchar2,
                                          p_Datoserror      Out Varchar2) Is
  
    v_Importenetorestante Number := 0;
    v_Importeultimomes    Number := 0;
    v_Importemes          Number := 0;
  
    -- Variables para recorrer los meses del pago
    v_Nmeses          Number;
    v_Fechainiciopago Date;
    v_Fechafinpago    Date;
    v_Fechames        Date;
  
  Begin
  
    -- cargando parametros en variables para hacer mas sencillo el trabajo en el paquete
    p_Idinstitucion   := p2_Idinstitucion;
    p_Idpago          := p2_Idpago;
    p_Idpersona       := p2_Idpersona;
    p_Importeneto     := p2_Importeneto;
    p_Usumodificacion := p2_Usumodificacion;
    p_Idioma          := p2_Idioma;
    
    v_Datoserror := 'Calculando meses del periodo de retencion';
    Select To_Date('01/' || To_Char(p.Fechadesde, 'MM/YYYY'), 'DD/MM/YYYY'),
           To_Date('01/' || To_Char(p.Fechahasta, 'MM/YYYY'), 'DD/MM/YYYY')
      Into v_Fechainiciopago, v_Fechafinpago
      From Fcs_Pagosjg p
     Where p.Idinstitucion = p_Idinstitucion
       And p.Idpagosjg = p_Idpago;
  
    v_Datoserror       := 'Entrando en el bucle de meses del periodo de retencion';
    v_Nmeses           := Abs(Months_Between(v_Fechainiciopago, v_Fechafinpago)) + 1;
    v_Importemes       := Round(p_Importeneto / v_Nmeses, 2);
    v_Importeultimomes := p_Importeneto - (v_Importemes * (v_Nmeses - 1));
    v_Fechames         := v_Fechainiciopago;
    Loop
      If v_Fechames = v_Fechafinpago Then
        v_Importenetorestante := v_Importeultimomes;
      Else
        v_Importenetorestante := v_Importemes;
      End If;
      -- Las retenciones No LEC restan importenetorestante antes de aplicar las LEC
      Proc_Aplica_Retenciones_Nolec(v_Importenetorestante, v_Fechames);
      Proc_Aplica_Retenciones_Lec(v_Importenetorestante, v_Fechames);
      
      v_Datoserror := 'Avanzando en el bucle de meses del periodo de retencion';
      v_Fechames   := Add_Months(v_Fechames, 1);
      Exit When(v_Fechames > v_Fechafinpago);
    End Loop;
  
    p_Codretorno := 0;
    p_Datoserror := 'Fin correcto';
  
  Exception
    When Others Then
      p_Codretorno := To_Char(Sqlcode);
      p_Datoserror := Sqlerrm || ' (' || v_Datoserror || ')';
  End Proc_Fcs_Aplicar_Retenc_Judic;

  Procedure Proc_Aplica_Retenciones_Nolec(p_Importenetorestante In Out Number, p_fechaMes In Date) Is
    -- Variables
    v_Importe_Retencion_Aplicada Number;
    v_Importeneto_mes Number;
  
  Begin
  
    -- guardando el importe del mes para calcular en las retenciones porcentuales
    v_Importeneto_mes := p_Importenetorestante;
  
    v_Datoserror := 'Entrando en el cursor de retenciones Fijas y Periodicas';
    For v_Retenciones In c_Retenciones('PF') Loop
    
      If (v_Retenciones.Tiporetencion = 'F') Then
        -- SI ES RETENCION FIJO
      
        -- Si el importe pdte de la retencion es menor o igual que el neto restante ...
        If (v_Retenciones.Importependiente <= p_Importenetorestante) Then
        
          -- entonces se puede cobrar toda la retencion ...
          v_Importe_Retencion_Aplicada := v_Retenciones.Importependiente;
        
          -- y cerramos la retencion con una observacion. R1411_0024 Eliminamos el mensaje
          /**Begin
            Update Fcs_Retenciones_Judiciales
               Set Observaciones = Observaciones || ' ' ||
                                   f_Siga_Getrecurso_Etiqueta('FactSJCS.mantRetencionesJ.plAplicarRetencionesJudiciales.aviso.finRetencion',
                                                              p_Idioma) || '(' ||
                                   To_Char(Sysdate, 'DD-MM-YYYY') || ')'
             Where Idinstitucion = p_Idinstitucion
               And Idretencion = v_Retenciones.Idretencion;
          Exception
            When Others Then
              Null;
          End;**/
        
        Else
          --Si no alcanza el neto para cobrar toda la retencion ...
        
          -- entonces se cobra todo el neto
          v_Importe_Retencion_Aplicada := p_Importenetorestante;
        
        End If;
      
      Elsif (v_Retenciones.Tiporetencion = 'P') Then
        -- SI ES RETENCION PORCENTUAL
      
        -- Calcula el importe a retener  tomando el  importe neto total
        -- (no el restante tras aplicar otras retenciones) como base
        v_Importe_Retencion_Aplicada := round(v_Importeneto_mes * v_Retenciones.Importe / 100, 2);
      
        -- Si el importe es mayor que el restante neto solo se puede retener el importenetorestante
        If (v_Importe_Retencion_Aplicada > p_Importenetorestante) Then
          v_Importe_Retencion_Aplicada := p_Importenetorestante;
        End If;
      
      End If;
    
      v_Datoserror := 'Apuntando el cobro de la retencion';
      Proc_Apunta_Cobro_Retencion(v_Retenciones.Idretencion,
                                  v_Importe_Retencion_Aplicada,
                                  p_Importenetorestante,
                                  To_Char(p_Fechames, 'mm'),
                                  To_Char(p_Fechames, 'yyyy'));
    
      v_Datoserror          := 'Restando el importe a retener: se sale si no se puede aplicar mas';
      p_Importenetorestante := p_Importenetorestante - v_Importe_Retencion_Aplicada;
      Exit When p_Importenetorestante = 0;
    
    End Loop;
  End Proc_Aplica_Retenciones_Nolec;

  Procedure Proc_Aplica_Retenciones_Lec(p_Importenetorestante In Number, p_fechaMes In Date) Is
    -- Importe SMI de cada mes
    v_Importesmi Number;
    -- Retencion finalmente aplicada
    v_Importe_Retencion_Aplicada Number;
    -- Total del Importe retenido y la base sobre la que se aplico, anteriormente al pago actual
    v_Importeanterior_Base     Number;
    v_Importeanterior_Retenido Number;
    -- Total del Importe retenido y la base sobre la que se aplica, incluyendo el pago actual
    v_Importetotal_Base     Number;
    v_Importetotal_Retenido Number;
    -- Importe retenido y la base sobre la que se aplica, solo del pago actual
    --v_importeActual_Base Number; Este importe nunca se usa, pero lo dejamos para que se vea que son pareados
    v_Importeactual_Aretener Number;
    
    v_Importenetorestante Number;
  
  Begin
    v_Importenetorestante := p_Importenetorestante;
	
    v_Datoserror := 'Obteniendo SMI del mes';
    Select To_Number(Valor)
      Into v_Importesmi
      From Fcs_Smi
     Where Anio = To_Char(p_Fechames, 'yyyy');
    
    v_Datoserror := 'Obteniendo total retenido y base de los pagos anteriores';
    Select Nvl(Abs(Sum(c.Importeaplicaretencion)), 0),
           Nvl(Abs(Sum(c.Importeretenido)), 0)
      Into v_Importeanterior_Base, v_Importeanterior_Retenido
      From Fcs_Cobros_Retencionjudicial c, Fcs_Retenciones_Judiciales r
     Where c.Idretencion = r.Idretencion
       And c.Idinstitucion = r.Idinstitucion
       And r.Tiporetencion = 'L'
       And c.Idinstitucion = p_Idinstitucion
       And c.Idpersona = p_Idpersona
          
       And c.Mes = To_Char(p_Fechames, 'mm')
       And c.Anio = To_Char(p_Fechames, 'yyyy');
  
    v_Datoserror             := 'Calculando retencion LEC anterior y actual';
    v_Importetotal_Base      := v_Importeanterior_Base + v_Importenetorestante;
    v_Importetotal_Retenido  := Round(f_Siga_Retencion_Lec(v_Importetotal_Base,
                                                           v_Importesmi,
                                                           p_Idinstitucion),
                                      2);
    v_Importeactual_Aretener := v_Importetotal_Retenido - v_Importeanterior_Retenido;
	
    v_Datoserror := 'Recorriendo las retenciones LEC pendientes';
    For v_Retenciones In c_Retenciones('L') Loop
    
      If (v_Retenciones.Importependiente Is Not Null And v_Retenciones.Importependiente < v_Importeactual_Aretener) Then
        -- Si el importe pendiente de la retencion es menor que el importe LEC que se puede retener, 
        -- entonces solo se retiene el pendiente
        v_Importe_Retencion_Aplicada := v_Retenciones.Importependiente;
        /**** R1411_0024 Eliminamos el mensaje
        Begin
          Update Fcs_Retenciones_Judiciales
             Set Observaciones = Observaciones || ' ' ||
                                 f_Siga_Getrecurso_Etiqueta('FactSJCS.mantRetencionesJ.plAplicarRetencionesJudiciales.aviso.finRetencion',
                                                            p_Idioma) || '(' ||
                                 To_Char(Sysdate, 'DD-MM-YYYY') || ')'
           Where Idinstitucion = p_Idinstitucion
             And Idretencion = v_Retenciones.Idretencion;
        Exception
          When Others Then
            Null;
        End;
        ****/
      Else
        v_Importe_Retencion_Aplicada := v_Importeactual_Aretener;
      End If;
    
      v_Datoserror := 'Apuntando el cobro de la retencion';
      Proc_Apunta_Cobro_Retencion(v_Retenciones.Idretencion,
                                  v_Importe_Retencion_Aplicada,
                                  v_Importenetorestante,
                                  To_Char(p_Fechames, 'mm'),
                                  To_Char(p_Fechames, 'yyyy'));
      
      -- poniendo el importe restante a 0 para que, en caso de varias retenciones, no se duplique el importe base
      v_Importenetorestante := 0;

      v_Datoserror             := 'Restando el importe a retener: se sale si no se puede aplicar mas';
      v_Importeactual_Aretener := v_Importeactual_Aretener - v_Importe_Retencion_Aplicada;
      Exit When(v_Importeactual_Aretener = 0);
    
    End Loop;
  End Proc_Aplica_Retenciones_Lec;
  
  /***********************************************************************************************/
  /* Nombre:   PROC_FCS_RETENCION_PERSONA                                                        */
  /* Descripcion: Calcula las retenciones judiciales aplicadas a una persona para un pago        */
  /*                                                                                             */
  /* Parametros            IN/OUT   Descripcion                                   Tipo de Datos  */
  /* -------------------   ------   -------------------------------------------   -------------  */
  /*                                                                                             */
  /* Version:        1.0                                                                         */
  /* Fecha Creacion: 12/05/2005                                                                  */
  /* Autor:          Raul Gonzalez Gonzalez                                                      */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion           */
  /* ------------------   ---------------------------------   ---------------------------------- */
  /***********************************************************************************************/
  PROCEDURE PROC_FCS_RETENCION_PERSONA(P_IDINSTITUCION IN NUMBER,
                                       P_IDPAGO        IN NUMBER,
                                       P_IDPERSONA     IN NUMBER,

                                       P_IMPORTERETENCION OUT VARCHAR2,
                                       P_CODRETORNO       OUT VARCHAR2,
                                       P_DATOSERROR       OUT VARCHAR2) IS

    -- VARIABLES
    V_AUXIMPORTE NUMBER := 0;

  BEGIN

    P_IMPORTERETENCION := TO_CHAR(0);
    P_CODRETORNO       := TO_CHAR(0);
    P_DATOSERROR       := NULL;

    -- MOVIMIENTOS VARIOS
    SELECT SUM(IMPORTERETENIDO)
      INTO V_AUXIMPORTE
      FROM FCS_COBROS_RETENCIONJUDICIAL
     WHERE IDINSTITUCION = P_IDINSTITUCION
       AND IDPAGOSJG = P_IDPAGO
       AND IDPERSONA = P_IDPERSONA;

    IF (V_AUXIMPORTE IS NOT NULL) THEN
      P_IMPORTERETENCION := TO_CHAR(V_AUXIMPORTE);
    END IF;

  EXCEPTION
    WHEN OTHERS THEN
      P_IMPORTERETENCION := 0;
      P_CODRETORNO       := TO_CHAR(SQLCODE);
      P_DATOSERROR       := SQLERRM;

  END; -- Procedure PROC_FCS_RETENCION_PERSONA

End;
/
