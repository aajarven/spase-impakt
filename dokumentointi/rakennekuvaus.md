Pelin ydin on Pelimoottori, joka huolehtii pelin tapahtumien päivittämisestä, siis hahmojen liikkeelle käskemisestä pelin ollessa käynnissä ja törmäystarkastusten kutsumisesta hahmoille, jolloin saadaan tietää, pitääkö joitakin hahmoja poistaa tai peli lopettaa ja antaa HighscoreManagerille uusi pelitulos tallennettavaksi (jos se on riittävän hyvä). Pelin käynnistää Pelirunko, joka luo pelimoottorin ja käyttöliittymän. Ikkuna on käyttöliittymän osa, joka huolehtii hahmojen piirtämisestä käyttäjän nähtäville. Graafisella käyttöliittymällä on myös sen valikoihin liittyviä kuuntelijoita aliluokkinaan. NappaimistoKuuntelija on omana luokkanaan, ja välittää alukselle tiedon siitä, pitääkö sen muuttaa suuntaansa/pysähtyä/ampua.

Kaikki hahmot, joita Ikkunaan piirretään (alus, aseet, vihut), toteuttavat rajapinnan Piirrettava. Nämä käyttöliittymä saa pelimoottorilta. Niitä yhdistää se, että jokaisella on koordinaatit sekä sprite. Aseita on tällä hetkellä toteutettuna vain kaksi: Ammus ja Laser. Pommi-luokka on olemassa ja täysin toimiva, mutta sille ei ole grafiikkaa eikä näppäimistökuuntelija kuuntele siihen liittyvää nappulaa.

Pelimoottori on ulkoistanut levelin vaihtamisen LevelManager-luokalle. Se lukee JsonLukijaa käyttäen leveliin kuuluvat viholliset (myöhemmin myös esimerkiksi tausta-artin, pomovastuksen ja ehkä levelin alussa näytettävän viestin) ja luo uuden Level-olion, johon se antaa viitteen pelimoottorille, jotta jokaisessa pelisyklissä voidaan tarkastaa, pitääkö piirrettäviin lisätä uusia vihollisia.

Vihu on rajapinta, jonka tällä hetkellä toteuttaa vain yksi luokka, Pikkuvihu. Myöhemmin sen toteuttaa myös pomovastus, joita on jokaisessa levelissä yksi. Vihollisen liikuttamisesta näytöllä huolehtii Liikutin-olio, joita on tällä hetkellä vain yhdenlaisia. Nyt toteutettuna on funktioliikutin, joka siirtää vihollista annetun määrän vasemmalle jokaisen pelisyklin aikana, ja laskee tätä x-koordinaattia vastaavan y-koordinaatin annetusta neljännen asteen funktiosta. Myöhemmin liikkumisvaihtoehdoksi tulee ainakin pelaajan aluksen seuraaminen y-suunnassa.