create or replace view v_siga_cat_certificacion as
Select To_Char(Fac.FechaDesde, 'Q"T"YYYY') Trimestre,
       Trim(To_Char(Fac.IdInstitucion, '0000')) CodigoICA,
       Sum(nvl(Mov.Importe,0)) ImportDevolucions,
       'ICT_' || To_Char(Fac.FechaDesde, 'Q"T"YYYY') || '-' || Trim(To_Char(Fac.IdInstitucion, '0000')||'.pdf') nomPdf,
       Null CodigoIncidencia,
       LTrim(To_Char(Sum(Ofi.Importe + Asi.Importe), '9999990D00')) ImporteTotal,
       Sum(Ofi.Cantidad + Asi.Cantidad) CantidadTotal,
       LTrim(To_Char(Sum(Ofi.Importe), '9999990D00')) ImporteOficio,
       Sum(Ofi.Cantidad) CantidadOficio,
       LTrim(To_Char(Sum(Asi.ImpPenal), '9999990D00')) ImporteAsiPenal,
       Sum(Asi.CanPenal) CantidadAsiPenal,
       LTrim(To_Char(Sum(Asi.ImpVido), '9999990D00')) ImporteAsiVido,
       Sum(Asi.CanVido) CantidadAsiVido,
       LTrim(To_Char(Sum(Asi.Importe), '9999990D00')) ImporteAsistencia,
       Sum(Asi.Cantidad) CantidadAsistencia,
       Sum(Asi.CanPenalInactiva) CantidadPenalInactiva,
       Sum(Asi.CanPenalSimple) CantidadPenalSimple,
       Sum(Asi.CanPenaldoble) CantidadPenalDoble,
       Sum(Asi.CanPenalDispensa) CantidadPenalDispensa,
       Sum(Asi.CanPenalComplemento) CantidadPenalComplemento,
       Sum(Asi.CanPenalDiaDespues) CantidadPenalDiaDespues,
       Sum(Asi.CanVidoInactiva) CantidadVidoInactiva,
       Sum(Asi.CanVidoSimple) CantidadVidoSimple,
       Sum(Asi.CanVidoDoble) CantidadVidoDoble,
       Sum(Asi.CanVidoDispensa) CantidadVidoDispensa,
       Sum(Asi.CanVidoComplemento) CantidadVidoComplement,
       Sum(Asi.CanVidoDiaDespues) CantidadVidoDiaDespues,
       Fac.IdInstitucion
  From Fcs_FacturacionJG Fac
       Inner Join (Select IdInstitucion,
                          IdFacturacion,
                          Count(*) Cantidad,
                          Sum(ImporteFacturado) Importe
                     From Fcs_Fact_ActuacionDesigna
                    Group By IdInstitucion,
                             IdFacturacion) Ofi
          On Ofi.IdInstitucion = Fac.IdInstitucion
         And Ofi.IdFacturacion = Fac.IdFacturacion
       Inner Join (Select Apu.IdInstitucion,
                          Apu.IdFacturacion,
                          Sum(Nvl(Act.Cantidad, 1)) Cantidad,
                          Sum(Apu.PrecioAplicado) Importe,
                          Sum(Decode(Gua.IdTipoGuardia, 1, 0, Nvl(Act.Cantidad, 1))) CanPenal,
                          Sum(Decode(Gua.IdTipoGuardia, 1, 0, Apu.PrecioAplicado)) ImpPenal,
                          Sum(Decode(Gua.IdTipoGuardia, 1, Nvl(Act.Cantidad, 1), 0)) CanVido,
                          Sum(Decode(Gua.IdTipoGuardia, 1, Apu.PrecioAplicado, 0)) ImpVido,
                          Sum(Case When Gua.IdTipoGuardia <> 1 And Apu.IdHito = 44 And Act.Cantidad Is Null
                                   Then 1
                                   Else 0
                               End) CanPenalInactiva,
                          Sum(Case When Gua.IdTipoGuardia <> 1 And ((Apu.IdHito = 44 And Act.Cantidad < 6) Or Apu.IdHito = 49)
                                   Then 1
                                   Else 0
                               End) CanPenalSimple,
                          Sum(Case When Gua.IdTipoGuardia <> 1 And ((Apu.IdHito = 7 And Apu.PrecioAplicado <> Act.Importe) Or (Apu.IdHito = 44 And Act.Cantidad > 5))
                                   Then 1
                                   Else 0
                               End) CanPenalDoble,
                          0 CanPenalDispensa,
                          Sum(Case When Gua.IdTipoGuardia <> 1 And Upper(Gua.Nombre) Like '%COMPL%' And ((Apu.IdHito = 7 And Apu.PrecioAplicado = Act.Importe) Or Apu.IdHito = 9)
                                   Then Act.Cantidad
                                   Else 0
                               End) CanPenalComplemento,
                          Sum(Case When Gua.IdTipoGuardia <> 1 And Apu.IdHito = 9
                                   Then Act.Cantidad
                                   Else 0
                               End) CanPenalDiaDespues,
                          Sum(Case When Gua.IdTipoGuardia = 1 And ((Apu.IdHito = 44 And Act.Cantidad Is Null) Or Apu.Idhito = 54)
                                   Then 1
                                   Else 0
                               End) CanVidoInactiva,
                          Sum(Case When Gua.IdTipoGuardia = 1 And ((Apu.IdHito In(44, 49) And Act.Cantidad < 4) Or Apu.IdHito = 38)
                                   Then 1
                                   Else 0
                               End) CanVidoSimple,
                          Sum(Case When Gua.IdTipoGuardia = 1 And ((Apu.IdHito = 7 And Apu.PrecioAplicado <> Act.Importe) Or (Apu.IdHito In(44, 49) And Act.Cantidad > 3))
                                   Then 1
                                   Else 0
                               End) CanVidoDoble,
                          0 CanVidoDispensa,
                          Sum(Case When Gua.IdTipoGuardia = 1 And Upper(Gua.Nombre) Like '%COMPL%' And ((Apu.IdHito = 7 And Apu.PrecioAplicado = Act.Importe) Or Apu.IdHito = 9)
                                   Then Act.Cantidad
                                   Else 0
                               End) CanVidoComplemento,
                          Sum(Case When Gua.IdTipoGuardia = 1 And Apu.IdHito = 9
                                   Then Act.Cantidad
                                   Else 0
                               End) CanVidoDiaDespues
                     From Fcs_Fact_Apunte Apu
                          Inner Join Scs_GuardiasTurno Gua
                             On Gua.IdInstitucion = Apu.IdInstitucion
                            And Gua.IdTurno       = Apu.IdTurno
                            and Gua.IdGuardia     = Apu.IdGuardia
                           Left Outer Join (Select IdInstitucion,
                                                   IdFacturacion,
                                                   IdApunte,
                                                   Count(*) Cantidad,
                                                   Sum(PrecioAplicado) Importe
                                              From Fcs_Fact_ActuacionAsistencia
                                             Group By IdInstitucion,
                                                      IdFacturacion,
                                                      IdApunte) Act
                             On Act.IdInstitucion = Apu.IdInstitucion
                            And Act.IdFacturacion = Apu.IdFacturacion
                            And Act.IdApunte      = Apu.IdApunte
                    Where Apu.PrecioAplicado > 0
                    Group By Apu.IdInstitucion,
                             Apu.IdFacturacion) Asi
          On Asi.IdInstitucion = Fac.IdInstitucion
         And Asi.IdFacturacion = Fac.IdFacturacion
         Left Outer Join (Select IdInstitucion,
                                 To_Char(FechaModificacion, 'MMYYYY') Mes,
                                 Sum(ImporteAplicado) Importe
                            From Fcs_Aplica_MovimientosVarios
                           Group By IdInstitucion,
                                    To_Char(FechaModificacion, 'MMYYYY')) Mov
           On Mov.IdInstitucion = Fac.IdInstitucion
          And Mov.Mes           = To_Char(Fac.FechaDesde, 'MMYYYY')
         Left Outer Join (Select Cab.IdInstitucion,
                                 To_Char(Cab.FechaInicio, 'MMYYYY') Mes,
                                 Sum(Decode(Gua.IdTipoGuardia, 1, 0, 1)) CantidadPenal,
                                 Sum(Decode(Gua.IdTipoGuardia, 1, 1, 0)) CantidadVido
                            From Scs_CabeceraGuardias Cab
                                 Inner Join Scs_GuardiasTurno Gua
                                    On Gua.IdInstitucion = Cab.IdInstitucion
                                   And Gua.Idturno       = Cab.Idturno
                                   And Gua.IdGuardia     = Cab.IdGuardia
                           Where Cab.FechaValidacion Is Null
                           Group By Cab.IdInstitucion,
                                 To_Char(Cab.FechaInicio, 'MMYYYY')) Dis
           On Dis.IdInstitucion = Fac.IdInstitucion
          And Dis.Mes           = To_Char(Fac.FechaDesde, 'MMYYYY')
 Where (Fac.IdInstitucion,
        Fac.IdFacturacion) In(Select IdInstitucion,
                                     IdFacturacion
                                From Fcs_Fact_GrupoFact_Hito
                               Where IdGrupoFacturacion = 1)
 Group By Fac.IdInstitucion,
          To_Char(Fac.FechaDesde, 'Q"T"YYYY')

