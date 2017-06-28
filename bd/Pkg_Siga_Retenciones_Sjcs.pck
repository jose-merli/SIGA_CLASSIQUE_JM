CREATE OR REPLACE Package Pkg_Siga_Retenciones_Sjcs Is

  Procedure Proc_Fcs_Aplicar_Retenc_Judic(p2_Idinstitucion   In Fcs_Pago_Colegiado.Idinstitucion%Type,
                                          p2_Idpago          In Fcs_Pago_Colegiado.Idpagosjg%Type,
                                          p2_Idpersona       In Fcs_Pago_Colegiado.Idperdestino%Type,
                                          p2_Importeneto     In Fcs_Pago_Colegiado.Impret%Type,
                                          p2_Usumodificacion In Fcs_Pago_Colegiado.Usumodificacion%Type,
                                          p2_Idioma          In Adm_Lenguajes.Idlenguaje%Type,
                                          p_Codretorno      Out Varchar2,
                                          p_Datoserror      Out Varchar2);

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
           Nvl(Importe, 0) As Importe,
           Nvl(Importe, 0) + Nvl((Select Sum(c.Importeretenido)
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
                              And c.Idretencion = Ret.Idretencion),
                           0) > 0)
           Or
           -- las retenciones LEC se terminan por importe, 
           -- solo si tienen importe: hay que comprobarlo en dicho caso
           (Tiporetencion = 'L' And Importe Is Not Null And
           Importe + Nvl((Select Sum(c.Importeretenido)
                             From Fcs_Cobros_Retencionjudicial c
                            Where c.Idinstitucion = Ret.Idinstitucion
                              And c.Idretencion = Ret.Idretencion),
                           0) > 0)
           Or
           (Tiporetencion = 'L' And Importe Is Null))
    
     Order By Orden, Fechainicio, Fechaalta;

  --
  -- Proc_Apunta_Cobro_Retencion
  -- 
  -- Hace un insert en FCS_COBROS_RETENCIONJUDICIAL en funcion de los parametros generales 
  -- establecidos y los que le pasan como parametros.
  -- Esto significa que apunta una retencion a la persona y el pago dado, por el importe pasado.
  --
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

  --
  -- Proc_Aplicar_Retenc_Judic_Mes
  --
  -- Proceso principal de aplicacion de retenciones
  --
  Procedure Proc_Aplicar_Retenc_Judic_Mes(p_Importe_Netobase In Number, p_Fechames In Date) Is
  
    v_Importe_Netorestante       Number; --El neto base que se va reduciendo segun se aplican retenciones
    v_Importe_Aintentarretener   Number; --Contiene el calculo de lo que se intenta retener. Luego se mira si esto se pasa de lo posible
    v_Importe_Retencion_Aplicada Number; --Lo retenido ya de verdad. Al final se restara al restante por si hay mas retenciones y queda algo de neto restante.
    v_Importe_Base_Usado         Number; --El importe sobre el que se aplica la retencion: en LEC es todo el neto, en las otras es lo que va quedando
  
    -- Variables solo para LEC
    v_Importesmi               Number; --El SMI correspondiente a este mes (anyo)
    v_Importeanterior_Base     Number; --La base del mes de anteriores retenciones
    v_Importetotal_Base        Number; --La base del mes actual con la base de anteriores retenciones
    v_Importeanterior_Retenido Number; --Retenciones aplicadas anteriormente: se va incrementando si se aplican varias retenciones en este mes
    v_Importe_Maximoaretener   Number; --En esta variable se calculara el importe maximo que se podra retener por tramos LEC
    b_Retencion_Lec_Yaaplicada Boolean; --Indica si se ha aplicado en este mismo bucle otra retencion LEC. Esto se hace para que no se duplique en ambas retenciones el importe base. Como no podemos de antemano saber qué retenciones se aplicarán, lo más sencillo es poner en una todo el base y en las siguientes poner 0. Así los totales cuadran, que es lo importante.
  
  Begin
  
    -- Calculos previos para retenciones LEC
    Begin
      v_Datoserror := 'Obteniendo SMI del mes';
      Select To_Number(Valor) Into v_Importesmi From Fcs_Smi Where Anio = To_Char(p_Fechames, 'yyyy');
    
      v_Datoserror := 'Obteniendo total retenido de los pagos anteriores';
      Select Nvl(Abs(Sum(c.Importeretenido)), 0)
        Into v_Importeanterior_Retenido
        From Fcs_Cobros_Retencionjudicial c, Fcs_Retenciones_Judiciales r
       Where c.Idretencion = r.Idretencion
         And c.Idinstitucion = r.Idinstitucion
            --And r.Tiporetencion = 'L' Ahora tenemos en cuenta cualquier retencion aplicada
         And c.Idinstitucion = p_Idinstitucion
         And c.Idpersona = p_Idpersona
            
         And c.Mes = To_Char(p_Fechames, 'mm')
         And c.Anio = To_Char(p_Fechames, 'yyyy');
    
      v_Datoserror := 'Obteniendo base de los pagos anteriores';
      -- No podemos obtener el importe base en retenciones no LEC porque se repite en todos los meses. Hay que obtener el importe base directamente del pago
      Select Nvl(Abs(Sum(Round((Pag.Impoficio + Pag.Impasistencia + Pag.Impsoj + Pag.Impejg + Pag.Impmovvar +
                               Pag.Impirpf) / (Select Count(Distinct c.Mes || '/' || c.Anio)
                                                 From Fcs_Cobros_Retencionjudicial c
                                                Where Pag.Idinstitucion = c.Idinstitucion
                                                  And Pag.Idpagosjg = c.Idpagosjg
                                                  And Pag.Idperorigen = c.Idpersona),
                               2))),
                 0)
        Into v_Importeanterior_Base
        From Fcs_Pago_Colegiado Pag
       Where Exists (Select 1
                From Fcs_Cobros_Retencionjudicial c
               Where Pag.Idinstitucion = c.Idinstitucion
                 And Pag.Idpagosjg = c.Idpagosjg
                 And Pag.Idperorigen = c.Idpersona
                 And c.Mes = To_Char(p_Fechames, 'mm')
                 And c.Anio = To_Char(p_Fechames, 'yyyy'))
            
         And Pag.Idinstitucion = p_Idinstitucion
         And Pag.Idperorigen = p_Idpersona;
    
      b_Retencion_Lec_Yaaplicada := False;
    End;
  
    v_Importetotal_Base        := v_Importeanterior_Base + p_Importe_Netobase;
    v_Importeanterior_Retenido := v_Importeanterior_Retenido; --se va aumentando en cada iteracion, pero solo se utiliza en LEC
    v_Importe_Netorestante     := p_Importe_Netobase;
  
    For v_Retenciones In c_Retenciones('PFL') Loop
    
      If v_Retenciones.Tiporetencion = 'F' Then
        v_Importe_Aintentarretener   := v_Retenciones.Importependiente;
        v_Importe_Retencion_Aplicada := Least(v_Importe_Aintentarretener, v_Importe_Netorestante); -- no se puede retener mas de lo que hay
        v_Importe_Base_Usado         := v_Importe_Netorestante;
      
      Elsif v_Retenciones.Tiporetencion = 'P' Then
        v_Importe_Aintentarretener   := Round(p_Importe_Netobase * v_Retenciones.Importe / 100, 2);
        v_Importe_Retencion_Aplicada := Least(v_Importe_Aintentarretener, v_Importe_Netorestante); -- no se puede retener mas de lo que hay
        v_Importe_Base_Usado         := p_Importe_Netobase;
      
      Elsif v_Retenciones.Tiporetencion = 'L' Then
        v_Importe_Aintentarretener := v_Retenciones.Importependiente;
        v_Importe_Maximoaretener   := Round(f_Siga_Retencion_Lec(v_Importetotal_Base, v_Importesmi, p_Idinstitucion), 2) -
                                      v_Importeanterior_Retenido;
        If v_Importe_Maximoaretener <= 0 Then
          -- ya se ha retenido el maximo
          v_Importe_Retencion_Aplicada := 0;
        Else
          v_Importe_Retencion_Aplicada := Least(v_Importe_Maximoaretener, v_Importe_Netorestante); -- no se puede retener mas de lo que hay ni pasarse del maximo por LEC
          If v_Retenciones.importe > 0 Then --Si la retencion LEC tiene importe (que es lo normal), entonces tampoco se puede retener mas de lo pendiente en la retencion
            v_Importe_Retencion_Aplicada := Least(v_Importe_Aintentarretener, v_Importe_Retencion_Aplicada);
          End If;
        End If;
      
        If b_Retencion_Lec_Yaaplicada Then
          v_Importe_Base_Usado := 0;
        Else
          b_Retencion_Lec_Yaaplicada := True;
          v_Importe_Base_Usado       := v_Importetotal_Base;
        End If;
      End If;
    
      v_Datoserror := 'Apuntando el cobro de la retencion';
      Proc_Apunta_Cobro_Retencion(v_Retenciones.Idretencion,
                                  v_Importe_Retencion_Aplicada,
                                  v_Importe_Base_Usado,
                                  To_Char(p_Fechames, 'mm'),
                                  To_Char(p_Fechames, 'yyyy'));
    
      v_Datoserror               := 'Acumulando retenido para otra retencion posterior';
      v_Importeanterior_Retenido := v_Importeanterior_Retenido + v_Importe_Retencion_Aplicada;
    
      v_Datoserror           := 'Restando el importe a retener: se sale si no se puede aplicar mas';
      v_Importe_Netorestante := v_Importe_Netorestante - v_Importe_Retencion_Aplicada;
      Exit When v_Importe_Netorestante = 0;
    
    End Loop;
  
  End Proc_Aplicar_Retenc_Judic_Mes;
  
  Procedure Proc_Fcs_Aplicar_Retenc_Judic(p2_Idinstitucion   In Fcs_Pago_Colegiado.Idinstitucion%Type,
                                          p2_Idpago          In Fcs_Pago_Colegiado.Idpagosjg%Type,
                                          p2_Idpersona       In Fcs_Pago_Colegiado.Idperdestino%Type,
                                          p2_Importeneto     In Fcs_Pago_Colegiado.Impret%Type,
                                          p2_Usumodificacion In Fcs_Pago_Colegiado.Usumodificacion%Type,
                                          p2_Idioma          In Adm_Lenguajes.Idlenguaje%Type,
                                          p_Codretorno       Out Varchar2,
                                          p_Datoserror       Out Varchar2) Is
  
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
      Proc_Aplicar_Retenc_Judic_Mes(v_Importenetorestante, v_Fechames);
      
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

End;
/
