CREATE OR REPLACE TRIGGER FAC_HISTORICOFACTURA_BF
  BEFORE Insert ON USCGAE_DESA.FAC_HISTORICOFACTURA
  REFERENCING NEW AS NEW
  For Each Row
Declare
  e_Error Exception;
  c_Accion_Emision           Constant Number := 1;
  c_Accion_Confirmacion      Constant Number := 2;
  c_Accion_Anticipo          Constant Number := 3;
  c_Accion_Pagocaja          Constant Number := 4;
  c_Accion_Pagobanco         Constant Number := 5;
  c_Accion_Devolucion        Constant Number := 6;
  c_Accion_Renegociacion     Constant Number := 7;
  c_Accion_Anulacion         Constant Number := 8;
  c_Accion_Anulacioncomision Constant Number := 9;
  c_Accion_Compensacion      Constant Number := 10;

  c_Estado_Pagado          Constant Number := 1;
  c_Estado_Pendientecobro  Constant Number := 2;
  c_Estado_Devuelta        Constant Number := 4;
  c_Estado_Pendientebanco  Constant Number := 5;
  c_Estado_Anulada         Constant Number := 8;
  c_Estado_Enrevision      Constant Number := 7;
  c_Estado_Pendientecobro2 Constant Number := 9;
Begin

  Case :New.Idtipoaccion
    When c_Accion_Emision Then
      If (:New.Estado != c_Estado_Enrevision) Then
        Raise_Application_Error(-20001, 'El estado indicado no es correcto para una emisión');
      End If;
    When c_Accion_Confirmacion Then
      If (:New.Estado Not In
         (c_Estado_Pendientecobro2, c_Estado_Pagado, c_Estado_Pendientebanco, c_Estado_Pendientecobro)) Then
        Raise_Application_Error(-20002, 'El estado indicado no es correcto para una confirmación');
      End If;
    When c_Accion_Anticipo Then
      If (:New.Imptotalanticipado <= 0) Then
        Raise_Application_Error(-20003, 'El importe total anticipado debe ser mayor que cero');
      End If;
    When c_Accion_Pagocaja Then
      If (:New.Idformapago != 30) Then
        Raise_Application_Error(-20004, 'La forma de pago debería ser por caja');
      Elsif (:New.Idcuenta Is Not Null Or :New.Idcuentadeudor Is Not Null) Then
        Raise_Application_Error(-20004, 'En un pago por caja no debe añadirse cuenta');
      Elsif (:New.Imptotalpagadoporcaja <= 0 Or :New.Imptotalpagadosolocaja <= 0 Or :New.Imptotalpagado <= 0) Then
        Raise_Application_Error(-20004, 'Debe indicarse importe de pago por caja');
      Elsif (:New.Estado Not In (c_Estado_Pagado, c_Estado_Enrevision, c_Estado_Pendientecobro)) Then
        Raise_Application_Error(-20004, 'El estado indicado no es correcto para un pago por caja');
      Elsif (:New.Idpagoporcaja Is Null) Then
        Raise_Application_Error(-20004, 'Debe indicarse un pago por caja relacionado');
      End If;
    When c_Accion_Pagobanco Then
      If (:New.Idformapago != 20) Then
        Raise_Application_Error(-20005, 'La forma de pago debería ser por banco');
      Elsif (:New.Idcuenta Is Null And :New.Idcuentadeudor Is Null) Then
        Raise_Application_Error(-20005, 'En un pago por banco debe añadirse cuenta');
      Elsif (:New.Idcuentadeudor Is Not Null And :New.Idpersonadeudor Is Null) Then
        Raise_Application_Error(-20005, 'En un pago por banco con deudor debe añadirse persona y cuenta');
      Elsif (:New.Imptotalpagadoporbanco <= 0 Or :New.Imptotalpagado <= 0) Then
        Raise_Application_Error(-20005, 'Debe indicarse importe de pago por banco');
      Elsif (:New.Estado Not In (c_Estado_Pagado, c_Estado_Pendientebanco)) Then
        Raise_Application_Error(-20005, 'El estado indicado no es correcto para un pago por banco');
      Elsif (:New.Iddisquetecargos Is Null Or :New.Idfacturaincluidaendisquete Is Null) Then
        Raise_Application_Error(-20005, 'Debe indicarse un fichero bancario relacionado');
      End If;
    When c_Accion_Devolucion Then
      If (:New.Imptotalporpagar <= 0) Then
        Raise_Application_Error(-20006, 'Debe indicarse importe de devolución');
      Elsif (:New.Estado != c_Estado_Devuelta) Then
        Raise_Application_Error(-20006, 'El estado indicado no es correcto para una devolución');
      Elsif (:New.Iddisquetedevoluciones Is Null Or :New.Idrecibo Is Null) Then
        Raise_Application_Error(-20006, 'Debe indicarse un fichero de devolución relacionado');
      End If;
    When c_Accion_Renegociacion Then
      If (:New.Idrenegociacion Is Null) Then
        Raise_Application_Error(-20007, 'Debe indicarse una renegociación relacionada');
      End If;
    When c_Accion_Anulacion Then
      If (:New.Imptotalporpagar <> 0) Then
        Raise_Application_Error(-20008, 'En la anulación no puede quedar importe por pagar');
      Elsif (:New.Estado != c_Estado_Anulada) Then
        Raise_Application_Error(-20008, 'El estado indicado no es correcto para una anulacion');
      Elsif (:New.Idabono Is Null) Then
        Raise_Application_Error(-20008, 'Debe indicarse un abono relacionado');
      End If;
    When c_Accion_Anulacioncomision Then
      --El control de que el imptotalporpagar = 0 no se pone debido a que cuando se llega a este estado no se pone a cero el importetotalporpagar, es un fallo que se debe de solucionar para poder incluirlo en el if
      If (:New.Estado != c_Estado_Anulada) Then
        Raise_Application_Error(-20009, 'El estado indicado no es correcto para una anulación con comisión');
      Elsif (:New.Comisionidfactura Is Null) Then
        Raise_Application_Error(-20009, 'Debe indicarse una factura con comision relacionada');
      End If;
    When c_Accion_Compensacion Then
      If (:New.Imptotalcompensado <= 0) Then
        Raise_Application_Error(-20010, 'Debe indicarse importe de compensación');
      Elsif (:New.Idpagoporcaja Is Null Or :New.Idabono Is Null) Then
        Raise_Application_Error(-20010, 'Debe indicarse un pago por caja relacionado');
      End If;
  End Case;
End Fac_Historicofactura_Bf;
/