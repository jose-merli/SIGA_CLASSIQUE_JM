create or replace view v_siga_cat_anexo as

Select Trimestre,
       CodigoICA,
       OrigenDatos,
       ImporteDevoluciones,
       CodigoIncidencia,
       Modulo,
       ImporteModulo,
       Importe,
       Cantidad,
       IDINSTITUCION
      
  From (
Select To_Char(Fac.FechaDesde, 'Q"T"YYYY') Trimestre,
       Trim(To_Char(Fac.IdInstitucion, '0000')) CodigoICA,
       'ICA' OrigenDatos,
       '0.00' ImporteDevoluciones,
       '' CodigoIncidencia,
       Decode(Act.IdAcreditacion, 5, '22', Pro.CodigoExt) Modulo,
       Decode(Act.IdAcreditacion, 5, 0.75, Fad.PrecioAplicado) ImporteModulo,
       To_Char(Sum(Fad.ImporteFacturado), '9999990D00') Importe,
       Count(*) Cantidad
       ,Fac.IdInstitucion
       
  From Fcs_FacturacionJG Fac
       Inner Join Fcs_Fact_ActuacionDesigna Fad
          On Fad.IdInstitucion = Fac.IdInstitucion
         And Fad.IdFacturacion = Fac.IdFacturacion
             Inner Join Scs_ActuacionDesigna Act
                On Act.IdInstitucion = Fad.IdInstitucion
               And Act.IdTurno       = Fad.IdTurno
               And Act.Anio          = Fad.Anio
               And Act.Numero        = Fad.Numero
               And Act.NumeroAsunto  = Fad.NumeroAsunto
                   Inner Join Scs_Procedimientos Pro
                      On Pro.IdInstitucion   = Act.IdInstitucion
                     And Pro.IdProcedimiento = Act.IdProcedimiento
 Where (Fac.IdInstitucion,
        Fac.IdFacturacion) In(Select IdInstitucion,
                                     IdFacturacion
                                From Fcs_Fact_GrupoFact_Hito
                               Where IdGrupoFacturacion = 1)
 Group By To_Char(Fac.FechaDesde, 'Q"T"YYYY'),
          Fac.IdInstitucion,
          Decode(Act.IdAcreditacion, 5, '22', Pro.CodigoExt),
          Decode(Act.IdAcreditacion, 5, 0.75, Fad.PrecioAplicado)

Union All

Select To_Char(Fac.FechaDesde, 'Q"T"YYYY') Trimestre,
       Trim(To_Char(Fac.IdInstitucion, '0000')) CodigoICA,
       'ICA' OrigenDatos,
       '0.00' ImporteDevoluciones,
       '' CodigoIncidencia,
       Case When Gua.IdtipoGuardia = 1
            Then Case Apu.IdHito
                      When  7 Then Case When Apu.PrecioAplicado = Act.ImpoAct
                                        Then '40a'
                                        Else '40c'
                                    End
                      When  9 Then '40a'
                      When 38 Then '40b'
                      When 44 Then Case When Act.CantAct Is Null
                                        Then '40d'
                                        Else Case When Act.CantAct < 4
                                                  Then '40b'
                                                  Else '40c'
                                              End
                                    End
                      When 49 Then Case When Act.CantAct < 4
                                        Then '40b'
                                        Else '40c'
                                    End
                      When 54 Then '40d'
                  End
            Else Case Apu.IdHito
                      When 7 Then Case When Apu.PrecioAplicado = Act.ImpoAct
                                       Then Case When Upper(Gua.Nombre) Like '%COMPL%'
                                                 Then '30c'
                                                 Else '30a'
                                             End
                                       Else Case When Upper(Gua.Nombre) Like '%COMPL%'
                                                 Then '32ca'
                                                 Else '32a'
                                             End
                                   End
                      When 9 Then Case When Upper(Gua.Nombre) Like '%COMPL%'
                                       Then '30cb'
                                       Else '30b'
                                   End
                      When 29 Then '30a'
                      When 44 Then Case When Nvl(CantAct, 0) < 6
                                        Then '31'
                                        Else '32'
                                    End
                      When 49 Then '31a'
                  End
        End Modulo,
       Decode(Apu.PrecioAplicado, Act.ImpoAct, Act.ImpoAct / Act.CantAct, PrecioAplicado) ImporteModulo,
       To_Char(Sum(Apu.PrecioAplicado), '9999990D00') Importe,
       Sum(Decode(Apu.PrecioAplicado, Act.ImpoAct, Act.CantAct, 1)) Cantidad
       ,Fac.IdInstitucion
  From Fcs_FacturacionJG Fac
       Inner Join Fcs_Fact_Apunte Apu
          On Apu.IdInstitucion = Fac.IdInstitucion
         And Apu.IdFacturacion = Fac.IdFacturacion
             Inner Join Scs_GuardiasTurno Gua
                On Gua.IdInstitucion = Apu.IdInstitucion
               And Gua.IdTurno       = Apu.IdTurno
               And Gua.IdGuardia     = Apu.IdGuardia
              Left Outer Join (Select IdInstitucion,
                                      IdFacturacion,
                                      IdApunte,
                                      Count(*) CantAct,
                                      Sum(PrecioAplicado) ImpoAct
                                 From Fcs_Fact_ActuacionAsistencia
                                Group By IdInstitucion,
                                      IdFacturacion,
                                      IdApunte) Act
                On Act.IdInstitucion = Apu.IdInstitucion
               And Act.IdFacturacion = Apu.IdFacturacion
               and Act.IdApunte      = Apu.IdApunte
 Where (Fac.IdInstitucion,
        Fac.IdFacturacion) In(Select IdInstitucion,
                                     IdFacturacion
                                From Fcs_Fact_GrupoFact_Hito
                               Where IdGrupoFacturacion = 1)
   And Apu.PrecioAplicado > 0
 Group By To_Char(Fac.FechaDesde, 'Q"T"YYYY'),
          Fac.IdInstitucion,
          Case When Gua.IdtipoGuardia = 1
               Then Case Apu.IdHito
                         When  7 Then Case When Apu.PrecioAplicado = Act.ImpoAct
                                           Then '40a'
                                           Else '40c'
                                       End
                         When  9 Then '40a'
                         When 38 Then '40b'
                         When 44 Then Case When Act.CantAct Is Null
                                           Then '40d'
                                           Else Case When Act.CantAct < 4
                                                     Then '40b'
                                                     Else '40c'
                                                 End
                                       End
                         When 49 Then Case When Act.CantAct < 4
                                           Then '40b'
                                           Else '40c'
                                       End
                         When 54 Then '40d'
                     End
               Else Case Apu.IdHito
                         When  7 Then Case When Apu.PrecioAplicado = Act.ImpoAct
                                           Then Case When Upper(Gua.Nombre) Like '%COMPL%'
                                                     Then '30c'
                                                     Else '30a'
                                                 End
                                           Else Case When Upper(Gua.Nombre) Like '%COMPL%'
                                                     Then '32ca'
                                                     Else '32a'
                                                 End
                                       End
                         When  9 Then Case When Upper(Gua.Nombre) Like '%COMPL%'
                                           Then '30cb'
                                           Else '30b'
                                       End
                         When 29 Then '30a'
                         When 44 Then Case When Nvl(CantAct, 0) < 6
                                           Then '31'
                                           Else '32'
                                       End
                         When 49 Then '31a'
                     End
           End,
           Decode(Apu.PrecioAplicado, Act.ImpoAct, Act.ImpoAct / Act.CantAct, PrecioAplicado)
)

