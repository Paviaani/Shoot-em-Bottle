package Peli1;

import java.awt.Image;

/**
 *  Tällä luokalla säädetään, muokataan ja tarkastellaan
 *  maalitaulujen ominaisuuksia
 *
 * @author Ari Salopää, Jani Joensuu, Henri Tuukkanen
 * @version 0.1a 23.4.2010
 */

public class Maalitaulu {
    /**
     * Parametrit
     */
    private int sijaintiX,sijaintiY;
    private Image kuva;
    private int kuvaW, kuvaH;
    private int nopeus;

    /**
     * Parametrien sijoitus
     * @param x objektin sijainti x-suunnassa
     * @param y objektin sijainti y-suunnassa
     * @param kuva2 kuva kopioidaan kuva2:een
     * @param nopeus2 kuvan nopeus kopioidaan nopeus2:een       
     */
    public Maalitaulu(int x, int y, Image kuva2, int nopeus2){
        this.sijaintiX=x;
        this.sijaintiY=y;
        this.kuva=kuva2;
        kuvaW = kuva2.getWidth(null);//kuvan leveys
        kuvaH = kuva2.getHeight(null); //kuvan korkeus
        this.nopeus = nopeus2;
    }

    /**
     * Palauttaa objektin sijainnin X-suunnassa
     * @return sijainti x-suunnassa
     */
    public int palautaSijaintiX(){
        return this.sijaintiX;
    }

    /**
     * Palauttaa objektin sijainnin Y-suunnassa
     * @return sijainti y-suunnassa
     */
    public int palautaSijaintiY(){
        return this.sijaintiY;
    }

    /**
     * Palauttaa kuvan
     * @return kuva
     */
    public Image palautaKuva(){
        return this.kuva;
    }

    /**
     * Palauttaa kuvan leveyden
     * @return kuvan leveys
     */
    public int palautaKuvaW(){
        return this.kuvaW;
    }

    /**
     * Palauttaa kuvan korkeuden
     * @return kuvan korkeus
     */
    public int palautaKuvaH(){
        return this.kuvaH;
    }

    /**
     * Palauttaa objektin nopeuden
     * @return objektin nopeus
     */
    public int palautaNopeus(){
        return this.nopeus;
    }

    /**
     * Muuttaa sijaintia x-suunnassa
     * @param uusX uusi x-suuntainen sijainti
     */
    public void muutaSijaintiX(int uusX){
        this.sijaintiX=uusX;
    }

    /**
     * Muuttaa sijaintia x-suunnassa
     * @param uusY uusi y-suuntainen sijainti
     */
    public void muutaSijaintiY(int uusY){
        this.sijaintiY=uusY;
    }

    /**
     * Muuttaa kuvan toiseksi kuvaksi
     * @param kuva2 uusi kuva
     */
    public void muutaKuva(Image kuva2){
        this.kuva=kuva2;
        kuvaW = kuva.getWidth(null);//kuvan leveys
        kuvaH = kuva.getHeight(null); //kuvan korkeus

    }

    /**
     * Muutta objektin nopeutta
     * @param uusNopeus uusi nopeus
     */
    public void muutaNopeus(int uusNopeus){
        this.nopeus = uusNopeus;
    }

}
