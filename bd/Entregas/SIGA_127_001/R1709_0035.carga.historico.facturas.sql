spool 10.carga.historico.facturas.log
prompt 10.carga.historico.facturas.log
select to_char(sysdate, 'hh24:mi:ss') as "Inicio" from dual;
prompt .


set serveroutput on
Declare
  Cursor c_institucion Is
    Select idinstitucion, Count(1) total From Fac_Factura fac
     Group By fac.Idinstitucion Order By Count(1) Desc;
  Cursor c_facturas(p_idinstitucion Cen_Institucion.Idinstitucion%Type) Is 
    Select idinstitucion, idfactura, idpersona From Fac_Factura Where idinstitucion = p_idinstitucion;
  Cursor c_historico (p_idinstitucion Fac_Factura.Idinstitucion%Type, p_idfactura Fac_Factura.Idfactura%Type, p_idpersona Fac_Factura.Idpersona%Type) Is
Select * From (
Select 0 As Idtabla,1 As idtipoaccion,
       Case when (Select Count(1)
               From Fac_Disquetecargos Dis
              Where Dis.Idinstitucion = Factura.Idinstitucion
                And Dis.Idseriefacturacion = Factura.Idseriefacturacion
                And Dis.Idprogramacion = Factura.Idprogramacion) > 0 Then 20 Else 30 End As Idformapago,
       Factura.Idpersonadeudor,Factura.Idcuentadeudor,7 As Idestado,
       f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.accion.emisionFactura', 1) || ' (' ||
       Factura.Numerofactura || ') ' As Tabla,
       (Select f_Siga_Getrecurso_Etiqueta(Fac_Estadofactura.Descripcion, 1) From Fac_Estadofactura Where Idestado = 7) As Estado,
       Factura.Fechaemision As Fecha,
       Factura.Fechamodificacion As Fecha_Orden,
       Nvl(Factura.Imptotal - Facturaanterior.Imptotalpagado, Factura.Imptotal) As Importe,
       Factura.Idfactura As Idfactura,Factura.idinstitucion,Factura.Idpersona,Factura.Idcuenta,
       0 As Idpago,
       '' As Devuelta
       , Factura.Imptotal,Factura.Imptotaliva,Factura.Imptotalneto,0 As Imptotalanticipado,0 As Imptotalpagadoporcaja,0 As Imptotalpagadosolocaja,0 As Imptotalpagadosolotarjeta,0 As Imptotalpagadoporbanco,0 As Imptotalpagado,Factura.Imptotal As Imptotalporpagar,0 As Imptotalcompensado
       , Null As idpagoporcaja, Null As iddisquetecargos, Null As idfacturaincluidaendisquete, Null As iddisquetedevoluciones, Null As idrecibo, Null As idrenegociacion, Null As idabono, Factura.comisionidfactura
  From Fac_Factura Factura, Fac_Factura Facturaanterior
 Where Factura.Idinstitucion = p_idinstitucion
   And Factura.Idfactura In (p_idfactura)
   And Facturaanterior.Idinstitucion(+) = Factura.Idinstitucion
   And Facturaanterior.Idfactura(+) = Factura.Comisionidfactura
Union
Select 1 As Idtabla,2 As idtipoaccion,
       Case when (Select Count(1)
               From Fac_Disquetecargos Dis
              Where Dis.Idinstitucion = Factura.Idinstitucion
                And Dis.Idseriefacturacion = Factura.Idseriefacturacion
                And Dis.Idprogramacion = Factura.Idprogramacion) > 0 Then 20 Else 30 End As Idformapago,
       Factura.Idpersonadeudor,Factura.Idcuentadeudor,9 As Idestado,
       f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.accion.confirmacionFactura', 1) As Tabla,
       Decode(Nvl(Factura.Imptotal - Facturaanterior.Imptotalpagado, Factura.Imptotal),
              0,
              (Select f_Siga_Getrecurso_Etiqueta(Fac_Estadofactura.Descripcion, 1)
                 From Fac_Estadofactura
                Where Idestado = 1),
              f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.estado.pendienteCobro', 1)) As Estado,
       Decode(Factura.Comisionidfactura,
              Null,
              Fac_Facturacionprogramada.Fechaconfirmacion,
              Factura.Fechaemision) As Fecha,
       Factura.Fechamodificacion As Fecha_Orden,
       Nvl(Factura.Imptotal - Facturaanterior.Imptotalpagado, Factura.Imptotal) As Importe,
       Factura.Idfactura As Idfactura,Factura.idinstitucion,Factura.Idpersona,Factura.Idcuenta,
       0 As Idpago,
       '' As Devuelta
       , Factura.Imptotal,Factura.Imptotaliva,Factura.Imptotalneto,0 As Imptotalanticipado,0 As Imptotalpagadoporcaja,0 As Imptotalpagadosolocaja,0 As Imptotalpagadosolotarjeta,0 As Imptotalpagadoporbanco,0 As Imptotalpagado,Factura.Imptotal As Imptotalporpagar,0 As Imptotalcompensado
       , Null As idpagoporcaja, Null As iddisquetecargos, Null As idfacturaincluidaendisquete, Null As iddisquetedevoluciones, Null As idrecibo, Null As idrenegociacion, Null As idabono, Factura.comisionidfactura
  From Fac_Factura Factura, Fac_Factura Facturaanterior, Fac_Facturacionprogramada
 Where Factura.Idinstitucion = Fac_Facturacionprogramada.Idinstitucion
   And Factura.Idseriefacturacion = Fac_Facturacionprogramada.Idseriefacturacion
   And Factura.Idprogramacion = Fac_Facturacionprogramada.Idprogramacion
   And Factura.Idinstitucion = p_idinstitucion
   And Factura.Idfactura In (p_idfactura)
   And Facturaanterior.Idinstitucion(+) = Factura.Idinstitucion
   And Facturaanterior.Idfactura(+) = Factura.Comisionidfactura
   And Factura.Numerofactura Is Not Null
Union
Select 2 As Idtabla,3 As idtipoaccion,Factura.Idformapago,Factura.Idpersonadeudor,Factura.Idcuentadeudor,Factura.Estado As Idestado,
       f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.accion.aplicarAnticipo', 1) As Tabla,
       Case
         When (Factura.Imptotal > Factura.Imptotalanticipado) Then
          f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.estado.pendienteCobro', 1)
         Else
          (Select f_Siga_Getrecurso_Etiqueta(Fac_Estadofactura.Descripcion, 1) From Fac_Estadofactura Where Idestado = 1)
       End As Estado,
       Factura.Fechaemision As Fecha,
       Factura.Fechamodificacion As Fecha_Orden,
       Factura.Imptotalanticipado As Importe,
       Factura.Idfactura As Idfactura,Factura.idinstitucion,Factura.Idpersona,Factura.Idcuenta,
       0 As Idpago,
       '' As Devuelta
       , Factura.Imptotal,Factura.Imptotaliva,Factura.Imptotalneto,Factura.Imptotalanticipado,0 As Imptotalpagadoporcaja,0 As Imptotalpagadosolocaja,0 As Imptotalpagadosolotarjeta,0 As Imptotalpagadoporbanco,Factura.Imptotalanticipado As Imptotalpagado,Factura.Imptotal-Factura.Imptotalanticipado As Imptotalporpagar,0 As Imptotalcompensado
       , Null As idpagoporcaja, Null As iddisquetecargos, Null As idfacturaincluidaendisquete, Null As iddisquetedevoluciones, Null As idrecibo, Null As idrenegociacion, Null As idabono, Null As comisionidfactura
  From Fac_Factura Factura
 Where Factura.Idinstitucion = p_idinstitucion
   And Factura.Idfactura In (p_idfactura)
   And Factura.Imptotalanticipado > 0
Union
Select 4 As Idtabla,4 As idtipoaccion,30 As Idformapago,Factura.Idpersonadeudor,Factura.Idcuentadeudor,(Case When Fac_Pagosporcaja.Importe = Factura.Imptotal Then 1 Else 2 End) As Idestado,
       f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.accion.pagosCaja', 1) As Tabla,
       Case
         When ((Factura.Imptotal - Factura.Imptotalanticipado) >
              (Select Sum(Pagocaja2.Importe)
                  From Fac_Pagosporcaja Pagocaja2
                 Where Pagocaja2.Idinstitucion = p_idinstitucion
                   And Pagocaja2.Idfactura = Fac_Pagosporcaja.Idfactura
                   And Pagocaja2.Idpagoporcaja <= Fac_Pagosporcaja.Idpagoporcaja)) Then
          (Select f_Siga_Getrecurso_Etiqueta(Fac_Estadofactura.Descripcion, 1) From Fac_Estadofactura Where Idestado = 2)
         Else
          (Select f_Siga_Getrecurso_Etiqueta(Fac_Estadofactura.Descripcion, 1) From Fac_Estadofactura Where Idestado = 1)
       End As Estado,
       Fac_Pagosporcaja.Fecha As Fecha,
       Fac_Pagosporcaja.Fechamodificacion As Fecha_Orden,
       Fac_Pagosporcaja.Importe As Importe,
       Fac_Pagosporcaja.Idfactura As Idfactura,Fac_Pagosporcaja.idinstitucion,Factura.Idpersona,Null As Idcuenta,
       Fac_Pagosporcaja.Idpagoporcaja As Idpago,
       '' As Devuelta
       , Factura.Imptotal,Factura.Imptotaliva,Factura.Imptotalneto,Factura.Imptotalanticipado,(Case When Factura.Imptotalpagadosolocaja>0 Then Factura.Imptotalpagadosolocaja When Fac_Pagosporcaja.Importe>0 Then Fac_Pagosporcaja.Importe Else 0.1 End) As Imptotalpagadoporcaja,(Case When Factura.Imptotalpagadosolocaja>0 Then Factura.Imptotalpagadosolocaja When Fac_Pagosporcaja.Importe>0 Then Fac_Pagosporcaja.Importe Else 0.1 End) As Imptotalpagadosolocaja,Factura.Imptotalpagadosolotarjeta,Factura.Imptotalpagadoporbanco,(Case When Factura.Imptotalpagado>0 Then Factura.Imptotalpagado When Fac_Pagosporcaja.Importe>0 Then Fac_Pagosporcaja.Importe Else 0.1 End) As Imptotalpagado,Factura.Imptotalporpagar,Factura.Imptotalcompensado
       , Fac_Pagosporcaja.idpagoporcaja, Null As iddisquetecargos, Null As idfacturaincluidaendisquete, Null As iddisquetedevoluciones, Null As idrecibo, Null As idrenegociacion, Null As idabono, Null As comisionidfactura
  From Fac_Pagosporcaja, Fac_Factura Factura
 Where Fac_Pagosporcaja.Idinstitucion = Factura.Idinstitucion
   And Fac_Pagosporcaja.Idfactura = Factura.Idfactura
   And Fac_Pagosporcaja.Idinstitucion = p_idinstitucion
   And Fac_Pagosporcaja.Idfactura In (p_idfactura)
   And Fac_Pagosporcaja.Idabono Is Null
Union
Select 4 As Idtabla,5 As idtipoaccion,20 As Idformapago,Factura.Idpersonadeudor,Factura.Idcuentadeudor,1 As Idestado,
       f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.accion.pagoBanco', 1) As Tabla,
       (Select f_Siga_Getrecurso_Etiqueta(Fac_Estadofactura.Descripcion, 1) From Fac_Estadofactura Where Idestado = 1) As Estado,
       Cargos.Fechacreacion As Fecha,
       Cargos.Fechacreacion As Fecha_Orden,
       Incluidadisquete.Importe As Importe,
       Incluidadisquete.Idfactura As Idfactura,Incluidadisquete.idinstitucion,Incluidadisquete.Idpersona,Incluidadisquete.Idcuenta,
       0 As Idpago,
       Incluidadisquete.Devuelta As Devuelta
       , Factura.Imptotal,Factura.Imptotaliva,Factura.Imptotalneto,Factura.Imptotalanticipado,Factura.Imptotalpagadoporcaja,Factura.Imptotalpagadosolocaja,Factura.Imptotalpagadosolotarjeta,(Case When Incluidadisquete.Importe = 0 Then 0.1 Else Incluidadisquete.Importe End) As Imptotalpagadoporbanco,(Case When Factura.Imptotalpagado>0 Then Factura.Imptotalpagado When Incluidadisquete.Importe>0 Then Incluidadisquete.Importe Else 0.1 End) As Imptotalpagado,Factura.Imptotalporpagar,Factura.Imptotalcompensado
       , Null As idpagoporcaja, Incluidadisquete.iddisquetecargos, Incluidadisquete.idfacturaincluidaendisquete, Null As iddisquetedevoluciones, Incluidadisquete.idrecibo, Null As idrenegociacion, Null As idabono, Null As comisionidfactura
  From Fac_Facturaincluidaendisquete Incluidadisquete, Fac_Disquetecargos Cargos, Fac_Factura Factura
 Where Incluidadisquete.Idinstitucion = Cargos.Idinstitucion
   And Incluidadisquete.Iddisquetecargos = Cargos.Iddisquetecargos
   And Incluidadisquete.Idinstitucion = Factura.Idinstitucion
   And Incluidadisquete.Idfactura = Factura.Idfactura
   And Incluidadisquete.Idinstitucion = p_idinstitucion
   And Incluidadisquete.Idfactura In (p_idfactura)
Union
Select 4 As Idtabla,6 As idtipoaccion,20 As Idformapago,Factura.Idpersonadeudor,Factura.Idcuentadeudor,4 As Idestado,
       f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.accion.devolucion', 1) || ' (' ||
       Lineadevolucion.Descripcionmotivos || ')' As Tabla,
       (Select f_Siga_Getrecurso_Etiqueta(Fac_Estadofactura.Descripcion, 1) From Fac_Estadofactura Where Idestado = 4) As Estado,
       Devoluciones.Fechageneracion As Fecha,
       Devoluciones.Fechamodificacion As Fecha_Orden,
       Incluidadisquete.Importe As Importe,
       Incluidadisquete.Idfactura As Idfactura,Incluidadisquete.idinstitucion,Incluidadisquete.Idpersona,Incluidadisquete.Idcuenta,
       0 As Idpago,
       Incluidadisquete.Devuelta As Devuelta
       , Factura.Imptotal,Factura.Imptotaliva,Factura.Imptotalneto,Factura.Imptotalanticipado,Factura.Imptotalpagadoporcaja,Factura.Imptotalpagadosolocaja,Factura.Imptotalpagadosolotarjeta,Factura.Imptotalpagadoporbanco,Factura.Imptotalpagado,Incluidadisquete.Importe As Imptotalporpagar,Factura.Imptotalcompensado
       , Null As idpagoporcaja, Incluidadisquete.iddisquetecargos, Incluidadisquete.idfacturaincluidaendisquete, Lineadevolucion.iddisquetedevoluciones, Lineadevolucion.idrecibo, Null As idrenegociacion, Null As idabono, Null As comisionidfactura
  From Fac_Facturaincluidaendisquete Incluidadisquete,
       Fac_Lineadevoludisqbanco      Lineadevolucion,
       Fac_Disquetedevoluciones      Devoluciones,
       Fac_Factura                   Factura
 Where Incluidadisquete.Idinstitucion = Lineadevolucion.Idinstitucion
   And Incluidadisquete.Iddisquetecargos = Lineadevolucion.Iddisquetecargos
   And Incluidadisquete.Idfacturaincluidaendisquete = Lineadevolucion.Idfacturaincluidaendisquete
   And Lineadevolucion.Idinstitucion = Devoluciones.Idinstitucion
   And Lineadevolucion.Iddisquetedevoluciones = Devoluciones.Iddisquetedevoluciones
   And Incluidadisquete.Idinstitucion = Factura.Idinstitucion
   And Incluidadisquete.Idfactura = Factura.Idfactura
   And Incluidadisquete.Idinstitucion = p_idinstitucion
   And Incluidadisquete.Idfactura In (p_idfactura)
Union
Select 4 As Idtabla,7 As idtipoaccion,Factura.Idformapago,Factura.Idpersonadeudor,Factura.Idcuentadeudor,Factura.Estado As Idestado,
       f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.accion.renegociacion', 1) || ' ' ||
       Renegociacion.Comentario As Tabla,
       Case
         When (Renegociacion.Idcuenta Is Null) Then
          (Select f_Siga_Getrecurso_Etiqueta(Fac_Estadofactura.Descripcion, 1) From Fac_Estadofactura Where Idestado = 2)
         Else
          (Select f_Siga_Getrecurso_Etiqueta(Fac_Estadofactura.Descripcion, 1) From Fac_Estadofactura Where Idestado = 5)
       End As Estado,
       Renegociacion.Fecharenegociacion As Fecha,
       Renegociacion.Fechamodificacion As Fecha_Orden,
       Renegociacion.Importe As Importe,
       Renegociacion.Idfactura As Idfactura,Renegociacion.idinstitucion,Renegociacion.Idpersona,Renegociacion.Idcuenta,
       0 As Idpago,
       '' As Devuelta
       , Factura.Imptotal,Factura.Imptotaliva,Factura.Imptotalneto,Factura.Imptotalanticipado,Factura.Imptotalpagadoporcaja,Factura.Imptotalpagadosolocaja,Factura.Imptotalpagadosolotarjeta,Factura.Imptotalpagadoporbanco,Factura.Imptotalpagado,Factura.Imptotalporpagar,Factura.Imptotalcompensado
       , Null As idpagoporcaja, Null As iddisquetecargos, Null As idfacturaincluidaendisquete, Null As iddisquetedevoluciones, Null As idrecibo, Renegociacion.idrenegociacion, Null As idabono, Null As comisionidfactura
  From Fac_Factura Factura, Fac_Renegociacion Renegociacion
 Where Renegociacion.Idinstitucion = Factura.Idinstitucion
   And Renegociacion.Idfactura = Factura.Idfactura
   And Renegociacion.Idinstitucion = p_idinstitucion
   And Renegociacion.Idfactura In (p_idfactura)
Union
Select 5 As Idtabla,8 As idtipoaccion,Factura.Idformapago,Factura.Idpersonadeudor,Factura.Idcuentadeudor,8 As Idestado,
       f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.accion.anulacion', 1) As Tabla,
       (Select f_Siga_Getrecurso_Etiqueta(Fac_Estadofactura.Descripcion, 1) From Fac_Estadofactura Where Idestado = 8) As Estado,
       Abono.Fecha As Fecha,
       Abono.Fechamodificacion As Fecha_Orden,
       Abono.Imptotal As Importe,
       Factura.Idfactura As Idfactura,Factura.idinstitucion,Factura.Idpersona,Factura.Idcuenta,
       0 As Idpago,
       '' As Devuelta
       , Factura.Imptotal,Factura.Imptotaliva,Factura.Imptotalneto,Factura.Imptotalanticipado,Factura.Imptotalpagadoporcaja,Factura.Imptotalpagadosolocaja,Factura.Imptotalpagadosolotarjeta,Factura.Imptotalpagadoporbanco,Factura.Imptotalpagado,0 As Imptotalporpagar,Factura.Imptotalcompensado
       , Null As idpagoporcaja, Null As iddisquetecargos, Null As idfacturaincluidaendisquete, Null As iddisquetedevoluciones, Null As idrecibo, Null As idrenegociacion, Abono.idabono, Null As comisionidfactura
  From Fac_Factura Factura, Fac_Abono Abono
 Where Factura.Idinstitucion = Abono.Idinstitucion
   And Factura.Idfactura = Abono.Idfactura
   And Factura.Idinstitucion = p_idinstitucion
   And Factura.Idfactura In (p_idfactura)
Union
Select 5 As Idtabla,9 As idtipoaccion,Factura.Idformapago,Factura.Idpersonadeudor,Factura.Idcuentadeudor,8 As Idestado,
       f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.accion.anulacion', 1) || ' (' ||
       f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.accion.anulacionComision', 1) || ')' As Tabla,
       (Select f_Siga_Getrecurso_Etiqueta(Fac_Estadofactura.Descripcion, 1) From Fac_Estadofactura Where Idestado = 8) As Estado,
       Facturaposterior.Fechaemision As Fecha,
       Factura.Fechamodificacion As Fecha_Orden,
       Factura.Imptotalporpagar As Importe,
       Factura.Idfactura As Idfactura,Factura.idinstitucion,Factura.Idpersona,Factura.Idcuenta,
       0 As Idpago,
       '' As Devuelta
       , Factura.Imptotal,Factura.Imptotaliva,Factura.Imptotalneto,Factura.Imptotalanticipado,Factura.Imptotalpagadoporcaja,Factura.Imptotalpagadosolocaja,Factura.Imptotalpagadosolotarjeta,Factura.Imptotalpagadoporbanco,Factura.Imptotalpagado,Factura.Imptotalporpagar,Factura.Imptotalcompensado
       , Null As idpagoporcaja, Null As iddisquetecargos, Null As idfacturaincluidaendisquete, Null As iddisquetedevoluciones, Null As idrecibo, Null As idrenegociacion, Null As idabono, Facturaposterior.comisionidfactura
  From Fac_Factura Factura, Fac_Factura Facturaposterior
 Where Factura.Idinstitucion = p_idinstitucion
   And Factura.Idfactura In (p_idfactura)
   And Facturaposterior.Idinstitucion = Factura.Idinstitucion
   And Facturaposterior.Comisionidfactura = Factura.Idfactura
   And Factura.Estado = 8
Union
Select 4 As Idtabla,10 As idtipoaccion,30 As Idformapago,Factura.Idpersonadeudor,Factura.Idcuentadeudor,Factura.Estado As Idestado,
       f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.accion.compensacion', 1) As Tabla,
       f_Siga_Getrecurso_Etiqueta('facturacion.pagosFactura.estado.compensacion', 1) As Estado,
       Pc.Fecha As Fecha,
       Pc.Fechamodificacion As Fecha_Orden,
       Pc.Importe As Importe,
       Pc.Idfactura As Idfactura,Pc.idinstitucion,Ab.Idpersona,Null As Idcuenta,
       Pc.Idpagoporcaja As Idpago,
       '' As Devuelta
       , Factura.Imptotal,Factura.Imptotaliva,Factura.Imptotalneto,Factura.Imptotalanticipado,Factura.Imptotalpagadoporcaja,Factura.Imptotalpagadosolocaja,Factura.Imptotalpagadosolotarjeta,Factura.Imptotalpagadoporbanco,Factura.Imptotalpagado,Factura.Imptotalporpagar,Factura.Imptotalcompensado
       , Pc.idpagoporcaja, Null As iddisquetecargos, Null As idfacturaincluidaendisquete, Null As iddisquetedevoluciones, Null As idrecibo, Null As idrenegociacion, Ab.idabono, Null As comisionidfactura
  From Fac_Abono Ab, Fac_Pagosporcaja Pc, Fac_Factura factura, Fac_Pagoabonoefectivo Aef
 Where Pc.Idinstitucion = Aef.Idinstitucion
   And Pc.Idinstitucion = Ab.Idinstitucion
   And pc.Idinstitucion = factura.Idinstitucion
   And pc.Idfactura = factura.Idfactura
   And Pc.Idinstitucion = p_idinstitucion
   And Pc.Idfactura In (p_idfactura)
   And Pc.Idabono = Aef.Idabono
   And Pc.Idabono = Ab.Idabono
   And Pc.Idpagoabono = Aef.Idpagoabono
   And (Ab.Idfactura <> Pc.Idfactura Or Ab.Idfactura Is Null)
   And Ab.Idpagosjg Is Not Null)

 Order By To_Number(Idfactura) Asc,
          Idtabla Asc,
          To_Char(Fecha, 'YYYYMMDD') Asc,
          Fecha_Orden Asc,
          Idpago Asc,
          Devuelta Asc;
  hora                  Varchar2(10);
  n_registros_historico Number;
  v_idhistorico         Number;
Begin
  Select to_char(Sysdate, 'hh:mi:ss') Into hora From dual;
  Dbms_Output.put_line(hora || ': INICIO');
  
  For r_institucion In c_institucion Loop
    Delete From Fac_Historicofactura his Where his.Idinstitucion = r_institucion.idinstitucion;
    n_registros_historico := 0;
    For r_factura In c_facturas(r_institucion.idinstitucion) Loop
      v_idhistorico := 0;
      For r_historico In c_historico(r_factura.idinstitucion, r_factura.idfactura, r_factura.idpersona) Loop
        v_idhistorico := v_idhistorico + 1;
        Insert Into fac_historicofactura
          (idinstitucion, idfactura, idhistorico, fechamodificacion, usumodificacion, idtipoaccion, idformapago, idpersona, idcuenta, idpersonadeudor, idcuentadeudor, imptotalanticipado, imptotalpagadoporcaja, imptotalpagadosolocaja, imptotalpagadosolotarjeta, imptotalpagadoporbanco, imptotalpagado, imptotalporpagar, imptotalcompensado, estado, idpagoporcaja, iddisquetecargos, idfacturaincluidaendisquete, iddisquetedevoluciones, idrecibo, idrenegociacion, idabono, comisionidfactura)
        Values
          (r_historico.idinstitucion, r_historico.idfactura, v_idhistorico, r_historico.Fecha_Orden, -1, r_historico.idtipoaccion, r_historico.idformapago, r_historico.idpersona, r_historico.idcuenta, r_historico.idpersonadeudor, r_historico.idcuentadeudor, r_historico.imptotalanticipado, r_historico.imptotalpagadoporcaja, r_historico.imptotalpagadosolocaja, r_historico.imptotalpagadosolotarjeta, r_historico.imptotalpagadoporbanco, r_historico.imptotalpagado, r_historico.imptotalporpagar, r_historico.imptotalcompensado, r_historico.idestado, r_historico.idpagoporcaja, r_historico.iddisquetecargos, r_historico.idfacturaincluidaendisquete, r_historico.iddisquetedevoluciones, r_historico.idrecibo, r_historico.idrenegociacion, r_historico.idabono, r_historico.comisionidfactura);
        n_registros_historico := n_registros_historico + sql%rowcount;
      End Loop;
    End Loop;
    
    Select to_char(Sysdate, 'hh:mi:ss') Into hora From dual;
    Dbms_Output.put_line(hora || ': FIN ' || r_institucion.idinstitucion || ' con ' || r_institucion.total || ' facturas. Insertados ' || n_registros_historico || ' registros de historico');
    Commit;
  End Loop;
  Select to_char(Sysdate, 'hh:mi:ss') Into hora From dual;
  Dbms_Output.put_line(hora || ': FIN');
End;
/
set serveroutput off


prompt .
select to_char(sysdate, 'hh24:mi:ss') as "Fin" from dual;
spool off
