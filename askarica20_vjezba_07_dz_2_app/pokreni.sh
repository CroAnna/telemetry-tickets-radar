#!/bin/bash

java -cp target/askarica20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.CentralniSustav NWTiS_DZ1_CS.txt &
sleep 5
java -cp target/askarica20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.PosluziteljKazni NWTiS_DZ1_PK.txt &
sleep 2
java -cp target/askarica20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara NWTiS_DZ1_R1.txt &
sleep 2
java -cp target/askarica20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara NWTiS_DZ1_R2.txt &
sleep 2
java -cp target/askarica20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara NWTiS_DZ1_R3.txt &
sleep 2
tail -f /dev/null
