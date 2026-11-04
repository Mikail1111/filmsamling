# FURPS – vores filmsamling
| FURPS | Krav                                                                               | Opfyldt? | Hvor |
|---|------------------------------------------------------------------------------------|---|---|
| F – Functionality | Brugeren kan søge efter film ved at skrive en del af titlen.                       | Ja | MovieCollection.searchMovies() |
| U – Usability | Brugeren kan trykke Enter for at beholde en eksisterende værdi ved redigering.     | Ja | UserInterface.editMovie() |
| R – Reliability | Programmet må ikke gå ned hvis brugeren, skriver bogstaver i stedet for et årstal. | Nej – del 6 | Skal implementeres |
| P – Performance | Programmet skal kun gemme filmsamlingen i en fil, når der er ændringer.            | Nej – del 7 | Skal implementeres |
| S – Supportability | Filmens logik skal kunne testes med automatiske teste.                             | Delvist | Movie og MovieCollection, tests mangler |