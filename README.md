# Simple-Database-Manager-unfinished-
Simple database manager in which user can manage databases and tables through simple GUI


------
Ukratko o aplikaciji:

Poanta aplikacije je da pruža korisniku mogućnost da kroz grafičko sučelje jednostavno kreira bazu podataka i tablice

///Bitna bilješka: aplikacija je nedovršena, i nekoliko funkcija fali. Poanta aplikacije mi je bila da se osobno upoznam sa JDBC i SceneBuilderom. Aplikacija je pravljena iz obrazovnih razloga///

------
Aplikacija se sastoji od nekoliko prozora od kojih svi imaju svoju funkciju, počevši od login prozora. Korisnik unosi svoje SQL podatke i server adresu i ukoliko su podatci ispravni vidit će dostupne baze podataka.

![1](https://user-images.githubusercontent.com/89515896/151012345-2b10bcbe-449f-4794-ab98-c849f5e5213e.png)

U prvom prozoru nakon prijave imamo izbor bazi podataka na koje korisnik može klinuti. Također imamo opcije za brisanje (Delete mode tipka). Korisnik također iz ovog prozora može i napraviti novu bazu pomoću prostora za unošenje teksta pri vrhu aplikacije.

Pri dnu prozora imamo opcije za promjenu layouta, tj broj prikazanih redova i stupaca. I ispod toga na samom dnu prozora korisnik ima mogućnost ručno upisati SQL querry za obavljanje radnji.

![2](https://user-images.githubusercontent.com/89515896/151012368-c662610c-16fb-435c-bbf0-006610d5244a.png)

Primjer delete funkcije, i promjene layouta (5 stupaca, 2 retka). Ukoliko korisnik sada stisne na tipku otvoriti će se novi prozor u kojem mora potvrditi brisanje.

![3](https://user-images.githubusercontent.com/89515896/151012487-730df8e5-5d40-435f-9f6d-56bcde0f4ffc.png)


Nakon što izaberemo bazu korisnik može odabrati tablicu koju želi otvoriti ili obrisati, osim toga korisnik ima opciju napraviti novu tablicu.

Pri dnu prozora imamo opcije za promjenu layouta, tj broj prikazanih redova i stupaca. I ispod toga na samom dnu prozora korisnik ima mogućnost ručno upisati SQL querry za obavljanje radnji.

![4](https://user-images.githubusercontent.com/89515896/151012563-0d1269df-f2c1-4e06-888c-a7b35e761688.png)

Prozor u kojem korisnik može kreirati novu tablicu na jednostavan način.

U primjeru smo nazvali tablicu "Test_tablica" i pritiskom na tipku "Add a column" 3 puta dobijemo 3 nova redka od kojih svaki predstavlja jedan stupac u tablici.

![5](https://user-images.githubusercontent.com/89515896/151012614-ed32f5c4-a474-4ae1-908e-541ae24ce2af.png)

Kada otvorimo tablicu, možemo dodavati podatke na 2 načina:

1) Pritiskom na "Add a new row" gumb pojavi se novi redak i korisnik moze upisivati podatke za svaki redak Aplikacija prepoznaje ako je stupac AUTO INCREMENT tako da korisnik ne mora upisivati vrijednost. Aplikacija također prepoznaje ako je stupac NOT NULL i stupac je obojan crveno. Korisnik mora upisat vrijednost.
2) Pri dnu imamo SQL querry gdje možemo putem tekstualnog oblika ubacit vrijednosti.

![6](https://user-images.githubusercontent.com/89515896/151012706-01f965b7-615c-4664-a8d0-292a330065ca.png)

Nakon što smo stisnuli "Insert" gumb, unešeni podatci se ubacuju u tablicu.

Drugi način za uređivanje podataka je putem tekstualnog prozora na donjem dijelu aplikacije.

![7](https://user-images.githubusercontent.com/89515896/151012821-1aad9d6f-feb6-4e59-b2de-7ec41728eace.png)

[NEDOVRŠENO, ne radi kako spada]

Ukoliko korisnik klikne na gumb "Edit table" dok se nalazi u table view prozoru, korisnik može jednostavno mijenjati vrijednosti u bilo kojem stupcu ili retku, te spremiti tablicu

![8](https://user-images.githubusercontent.com/89515896/151012895-ca808f2d-656e-4c89-8b96-59746254552f.png)

