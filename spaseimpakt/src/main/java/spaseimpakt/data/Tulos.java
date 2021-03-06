/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaseimpakt.data;

import java.io.Serializable;

/**
 * Pelaajan pistetilanne pelin päättyessä. Tallentaa pelaajan nimen ja hänen
 * saamansa pisteet.
 *
 * @author Anni Järvenpää
 */
public class Tulos implements Comparable<Tulos>, Serializable {

    private final int pisteet;
    private final String nimi;

    /**
     *
     * @param nimi pelaajan nimi
     * @param pisteet pelaajan saamat pisteet
     */
    public Tulos(String nimi, int pisteet) {
        this.nimi = nimi;
        this.pisteet = pisteet;
    }

    /**
     *
     * @return pelaajan pistemäärä
     */
    public int getPisteet() {
        return pisteet;
    }

    /**
     *
     * @return pelaajan nimi
     */
    public String getNimi() {
        return nimi;
    }

    @Override
    public int compareTo(Tulos t) {
        if (this.pisteet > t.getPisteet()) {
            return -1;
        } else if (this.pisteet < t.getPisteet()) {
            return 1;
        } else {
            return this.nimi.compareTo(t.getNimi());
        }
    }
}
