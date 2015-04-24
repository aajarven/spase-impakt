/*
 * Copyright Anni Järvenpää 2015
 */
package spaseimpakt.logiikka;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import spaseimpakt.data.Alus;
import spaseimpakt.data.Ammus;
import spaseimpakt.data.Ase;
import spaseimpakt.data.Level;
import spaseimpakt.data.Piirrettava;
import spaseimpakt.data.Vihu;
import spaseimpakt.kayttoliittyma.GraafinenKayttoliittyma;

/**
 * Hoitaa pelin pyörittämisen ja ohjaa muiden luokkien toimintaa
 *
 * @author Anni
 */
public class Pelimoottori extends Thread {

    /**
     * True jos peli on käynnissä, muuten false
     */
    private boolean kaynnissa;
    /**
     * True jos kaikki levelit on selvitetty, muuten false
     */
    private boolean peliVoitettu;

    /**
     * Viive kahden ruudunpäivityksen välillä
     */
    private final int STEP = 1000 / 75;

    private Alus alus;
    private GraafinenKayttoliittyma kayttoliittyma;
    
    /**
     * Pelaajan ampumat aseet, jotka tällä hetkellä ovat ruudulla näkyvissä
     */
    private CopyOnWriteArrayList<Ase> aseet;
    
    /**
     * Kaikki pelihahmot (esim viholliset, alus), jotka näytölle pitää pirtää
     */
    private CopyOnWriteArraySet<Piirrettava> piirrettavat;
    
    /**
     * Näytöllä tällä hetkellä olevat viholliset
     */
    private CopyOnWriteArrayList<Vihu> viholliset;
    
    /**
     * LevelManager, joka ohjaa levelien vaihtamista
     */
    private LevelManager lvlManager;
    
    private Level lvl;
    
    /**
     * Polut leveltiedostoihin
     */
    private final String[] levelTiedostot = new String[]{"resources/levelit/ekalevel.json"}; 
    
    /**
     * 
     */
    private long levelinAlkuAika;
    
    /**
     * Konstruktori
     *
     * @param kayttoliittyma käyttöliittymä, joka näyttää pelin tapahtumat
     * @param maailma maailma, jonka tilannetta päivitetään
     */
    public Pelimoottori(GraafinenKayttoliittyma kayttoliittyma) {
        this.kayttoliittyma = kayttoliittyma;
        kaynnissa = true;
        alus = new Alus(0, Pelirunko.KORKEUS / 2, Pelirunko.LEVEYS, Pelirunko.KORKEUS, this);
        aseet = new CopyOnWriteArrayList<>();
        piirrettavat = new CopyOnWriteArraySet<>();
        viholliset = new CopyOnWriteArrayList<>();
        lvlManager=new LevelManager(levelTiedostot, this);
        lvl=lvlManager.lueSeuraavaLevel();
        levelinAlkuAika=System.currentTimeMillis();
        peliVoitettu=false;
        piirrettavat.add(alus);
    }

    /**
     * Metodi, jota kutsutaan, kun pelaaja ampuu uuden aseen
     *
     * @param ase ammuttu ase
     */
    public void lisaaAse(Ase ase) {
        aseet.add(ase);
        if (ase instanceof Ammus) { // tai myöhemmin laser, pommia ei piirretä
            piirrettavat.add((Ammus) ase);
        }
    }

    /**
     * Poistaa olemassaolevan aseen kun se ei enää ole toiminnassa
     *
     * @param ase ase, joka poistetaan
     */
    public void poistaAse(Ase ase) {
        aseet.remove(ase);
        piirrettavat.remove(ase);
    }

    /**
     * Palauttaa listan kaikista tällä hetkellä aktiivisista aseista
     *
     * @return Lista, joka sisältää kaikki aseet.
     */
    public CopyOnWriteArrayList<Ase> getAseet() {
        return aseet;
    }

    public CopyOnWriteArrayList<Vihu> getVihut() {
        return viholliset;
    }

    /**
     * Lisää peliin uuden vihollisen
     *
     * @param vihu lisättävä vihollinen
     */
    public void lisaaVihu(Vihu vihu) {
        viholliset.add(vihu);
        piirrettavat.add((Piirrettava) vihu);
    }

    /**
     * Poistaa pelistä vihollisen
     *
     * @param vihu poistettava vihollinen
     */
    public void poistaVihu(Vihu vihu) {
        viholliset.remove(vihu);
        piirrettavat.remove(vihu);
    }

    /**
     * Palauttaa pelaajan ohjaaman aluksen
     *
     * @return pelaajan lentämä alus
     */
    public Alus getAlus() {
        return alus;
    }

    /**
     * Varsinainen pelitapahtuma. Liikuttaa alusta, siirtää maailmaa ja
     * päivittää käyttöliittymän, minkä jälkeen tutkii, tuleeko pelin suoritusta
     * vielä jatkaa ja toimii asianmukaisesti.
     */
    @Override
    public void run() {

        //TODO alkuun varmaan dialogi, jossa kysytään pelaajan nimeä
        do {        
            alus.paivita();

            for (Ase ase : aseet) {
                ase.liiku();
            }

            for (Vihu vihu : viholliset) {
                vihu.liiku();
            }

            kayttoliittyma.piirra();

            paivitaLevel();
            odota();
        } while (kaynnissa);
    }

    private void paivitaLevel() {
        if(lvl==null){
            lvl=lvlManager.lueSeuraavaLevel();
            if(lvl==null){
                peliVoitettu=false;
                lopeta();
            }
            levelinAlkuAika=System.currentTimeMillis();
        }
        
        Long aikaLevelinAlustaLong = System.currentTimeMillis()-levelinAlkuAika;
        int aikaLevelinAlusta = aikaLevelinAlustaLong.intValue();
        System.out.println(aikaLevelinAlusta);
        while(lvl.onkoSeuraavanVihollisenAika(aikaLevelinAlusta)){
            lisaaVihu(lvl.seuraavaVihollinen());
        }
    }

    /**
     * Pysäyttää pelin suorituksen muuttujan step osoittamaksi ajaksi.
     */
    public void odota() {
        try {
            Thread.sleep(STEP);
        } catch (InterruptedException e) {
            System.out.println("sleeping interrupted");
            e.printStackTrace();
//            System.exit(0);
        }
    }

    /**
     * Lopettaa pelitilanteen päivittämisen.
     */
    public void lopeta() {
        kaynnissa = false;
    }

    public CopyOnWriteArraySet<Piirrettava> getPiirrettavat() {
        return piirrettavat;
    }

}
