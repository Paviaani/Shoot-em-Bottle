
package Peli1;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.JApplet;
import javax.swing.JPanel;


/**
 *
 * @author Ari Salopää, Jani Joensuu, Henri Tuukkanen
 * @version 0.1a  23.4.2010
 */
public class MainDemo1 extends JApplet implements Runnable {

    Thread saie = null;
    Maalitaulu maali[]; //pullojen tiedot
    Maalitaulu kori[];
    Maalitaulu kori2[];
    Font f1; //Tekstin tulostukseen
    Random rn = new Random();
    private AudioClip laukaus, osu;//Äänet
    private MediaTracker mt;
    private Image pullo, hiiri, tyhja, koppa, tausta;
    private int tahtainX = 16, tahtainY = 16, heittoa=1;
    private int maalitauluja = 27; //pulloja
    private int jaljella = maalitauluja;//laskee koska pullot on loppu
    private int rivinvaihto = maalitauluja / 3; //koska pitää vaihtaa riviä
    private int tila=1;    //tila tai moodi eli 1=valikko, 2=peli, 3=highscore
    private int pisteet = 2000;    //alotus pistemäärä
    



    @Override
    public void init() {


        mt = new MediaTracker(this);
        //ladataan kuvat
        pullo = getImage(getDocumentBase(), "pullo.gif");
        hiiri = getImage(getDocumentBase(), "tahtain.gif");
        tyhja = getImage(getDocumentBase(), "tyhja.gif");
        koppa = getImage(getDocumentBase(),"koppa.gif");
        tausta = getImage(getDocumentBase(),"tausta2.jpg");

        //ladataan ääniä
        laukaus = getAudioClip(getDocumentBase(), "laukaus.wav");
        osu = getAudioClip(getDocumentBase(), "osu.wav");

        //Alusta Fontti
        f1 = new Font("Arial", Font.BOLD, 10);
        f1 = f1.deriveFont(24.0f);

        setSize(500, 500);//alueen koko

        try {
            mt.addImage(pullo, 0);
            mt.addImage(hiiri, 1);
            mt.addImage(tyhja, 2);
            mt.addImage(koppa, 3);
            mt.waitForAll();

        } catch (InterruptedException e) {
            System.out.println("Ei ladattu kuvaa");
        }

        //#### Alusta kohteet
        maali = new Maalitaulu[maalitauluja];
        kori = new Maalitaulu[3];   //korien alustus
        kori2 = new Maalitaulu[2];  //korien alustus

       for (int j = 0; j < maalitauluja; j++) {//Alusta pullot
            if (j < rivinvaihto) {
                maali[j] = new Maalitaulu((5 + 50 * j), (20), pullo, 3 );
            }
            if (j >= rivinvaihto && j < (rivinvaihto*2)){
                maali[j] = new Maalitaulu((5 + (j - rivinvaihto) * 50), (180), pullo, -3);  //2. rivi
            }
            if (j >= (rivinvaihto*2)){
                maali[j] = new Maalitaulu((5 + 50 * (j - rivinvaihto*2)), (340), pullo, 3 ); //3. rivi
            }
            System.out.println(maali[j].palautaSijaintiX());
        }
        
        for (int j = 0; j < 3; j++) {    //tee 3 koria
            kori[j] = new Maalitaulu( j * 140 +20, 200, koppa, 0);
        }

        
        for (int j = 0; j < 2 ; j++) {    //tee 2 koria
            kori2[j] = new Maalitaulu( j * 250 +20, 200, koppa, 0);
        }
        

        //piirto omal luokka
        getContentPane().add(new Piirtopaneeli());
        addMouseListener(new HiirenKuuntelija());

        
    }//end init
    

    @Override
    public void start() {
        // jos säie ei ole ajossa, käynnistetään uusi säie
        if (saie == null) {
            saie = new Thread(this);
            saie.start();
        }
    }

    @SuppressWarnings("static-access")
    public void run() {
        while (true) {
            

            if (tila == 2) {
            //pullo reunan yli niin
            for (int j = 0; j < maalitauluja; j++) {
                if (maali[j].palautaSijaintiX() + maali[j].palautaKuvaW() + 2 >= getSize().width)
                    { /*jos pullo menee reunan yli niin*/
                    maali[j].muutaSijaintiX(3);
                    }
                if (maali[j].palautaSijaintiX() - 2 <= 0)
                    { /*jos pullo menee reunan yli niin*/
                    maali[j].muutaSijaintiX(getSize().width - maali[j].palautaKuvaW() - 2);
                    }/*lisää nopeus pullon X akseliin*/
                    maali[j].muutaSijaintiX(maali[j].palautaSijaintiX() + maali[j].palautaNopeus());   
                }//end for
            }

            if(tila == 2){
            //muuta tähtäimen sijaintia
            tahtainX = rn.nextInt(heittoa) + 16 - (heittoa / 2);
            tahtainY = rn.nextInt(heittoa) + 16 - (heittoa / 2);
            pisteet -= 10;  //pisteet
            }else{
            tahtainX=16;
            tahtainY=16;
            }//end tähtäimen heiluminen

            //kursorin korvaaaminen kuvalla
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Point hotSpot = new Point(tahtainX, tahtainY);//hotspotin teko
            Cursor cursor = toolkit.createCustomCursor(hiiri, hotSpot, "tahtain");//luodaan kursori
            setCursor(cursor);//kursorin käyttö
            repaint();

            try {
                // säie nukkuu 500 millisekuntia
                saie.sleep(50);
            } catch (InterruptedException e) {
            }

            if (jaljella == 0) {
                tila=3;   //vaihdetaan tilaa
            }

        }//end while
    }

    //luokka toteut. hiiren liik. tapah.
    class HiirenKuuntelija implements MouseListener {
        // kaikki MouseListener-luokan metodit tulee toteuttaa, koska ne ovat abstrakteja

        public void mouseExited(MouseEvent tapahtuma) {
        }

        public void mouseEntered(MouseEvent tapahtuma) {
        }

        public void mouseClicked(MouseEvent tapahtuma) {
            if (tila == 1) {    //Valikossa
                for (int h = 0; h < 3; h++) {
                    if (tapahtuma.getX() > kori[h].palautaSijaintiX() /*onko hiiri vas reunan sisäpuolella*/
                            && tapahtuma.getX() < kori[h].palautaSijaintiX() + kori[h].palautaKuvaW()/*oik puolel*/
                            && tapahtuma.getY() > kori[h].palautaSijaintiY() /*ylä reunan alla*/
                            && tapahtuma.getY() < kori[h].palautaSijaintiY() + kori[h].palautaKuvaH()) {/*alan yllä*/

                        if (h == 0) {
                            tila = 2; //aloitan peli
                        }
                        if (h == 1) {
                            tila = 3;//skippaa Highscoreen
                        }
                        if (h == 2) {
                            System.exit(10); //lopeta
                        }
                        osu.play(); //osuma ääni
                    }
                }//end for
            }//end valikko, tila=1

            if (tila == 3) {    //Valikossa
                for (int h = 0; h < 2; h++) {
                    if (tapahtuma.getX() > kori2[h].palautaSijaintiX() /*onko hiiri vas reunan sisäpuolella*/
                            && tapahtuma.getX() < kori2[h].palautaSijaintiX() + kori2[h].palautaKuvaW()/*oik puolel*/
                            && tapahtuma.getY() > kori2[h].palautaSijaintiY() /*ylä reunan alla*/
                            && tapahtuma.getY() < kori2[h].palautaSijaintiY() + kori2[h].palautaKuvaH()) {/*alan yllä*/

                        if (h == 0) {
                            jaljella = maalitauluja;
                            for (int hj = 0; hj < maalitauluja; hj++) {
                                 if (hj < rivinvaihto) {
                                      maali[hj].muutaSijaintiX(5 + 50 * hj);
                                  }
                                  if (hj >= rivinvaihto && hj < (rivinvaihto * 2)) {
                                      maali[hj].muutaSijaintiX(5 + (hj - rivinvaihto) * 50);  //2 rivi
                                  }
                                  if (hj >= (rivinvaihto * 2)) {
                                       maali[hj].muutaSijaintiX(5 + 50 * (hj - rivinvaihto * 2));
                                  }
                                  maali[hj].muutaKuva(pullo);
                            }
                            pisteet=2000;    //alusta pistemäärä
                            tila = 2; //aloitan peli
                        }
                        if (h == 1) {
                            System.exit(10); //lopeta
                        }
                        osu.play(); //osuma ääni
                    }
                }//end for
            }//end valikko, tila=3
          }//end Event


        public void mouseReleased(MouseEvent tapahtuma) {
        }

        // käsitellään hiiren klikkauksesta aiheutuva tapahtuma
        public void mousePressed(MouseEvent tapahtuma) {
            //laukaus ääni
            if (true)
                laukaus.play();
            else laukaus.stop();

            // tulostetaan tilariville tekstiä (kohta missä hiiri on ollut kun klikataan applettia)
            showStatus("Hiirtä klikattu:" + tapahtuma.getX() + "," + tapahtuma.getY());
            if (tila == 2) {    //pelissä
            for (int h = 0; h < maalitauluja; h++) {
                if (tapahtuma.getX() > maali[h].palautaSijaintiX()  /*onko hiiri vas reunan sisäpuolella*/
                        && tapahtuma.getX() < maali[h].palautaSijaintiX() + maali[h].palautaKuvaW()/*oik puolel*/
                        && tapahtuma.getY() > maali[h].palautaSijaintiY()  /*ylä reunan alla*/
                        && tapahtuma.getY() < maali[h].palautaSijaintiY() + maali[h].palautaKuvaH()) {/*alan yllä*/
                        jaljella--;   //vähennä pullo laskurista
                        maali[h].muutaKuva(tyhja); //piilota osuneeseen pulloon

                    if (heittoa < 32) {//heitto ei saa ylittää 32
                        heittoa++;  //Lisää tähtäimen heittoa
                    }

                    osu.play(); //osuma ääni
                    }
            }//end for
            }//end pelissä, tila=2

        }
    }

    //luokka toteutttaa appletin piirtämisen
    class Piirtopaneeli extends JPanel {
        
        @Override
        public void paintComponent(Graphics g) {
                    g.setFont(f1);
                    g.setColor(Color.blue);
                    g.drawImage(tausta, 0, 0, this);
            if (tila == 1) { for (int j = 0; j < 3; j++) {

                    g.drawImage(kori[j].palautaKuva(), kori[j].palautaSijaintiX(), kori[j].palautaSijaintiY(), this);
                    g.drawString("Shoot'em Bottle", 150,20);
                    g.drawString("Aloita", kori[0].palautaSijaintiX()+45, kori[0].palautaSijaintiY()+kori[0].palautaKuvaH()+20);
                    g.drawString("Highscore", kori[1].palautaSijaintiX()+30, kori[1].palautaSijaintiY()+kori[1].palautaKuvaH()+20);
                    g.drawString("Lopeta", kori[2].palautaSijaintiX()+45, kori[2].palautaSijaintiY()+kori[2].palautaKuvaH()+20);
                }   //Valikon piirtäminen
            }//end if tila=1           


            if (tila == 2) {    //pelin piirtäminen
                super.paintComponent(g);
               
                for (int j = 0; j < maalitauluja; j++) {
                    g.drawImage(maali[j].palautaKuva(), maali[j].palautaSijaintiX(), maali[j].palautaSijaintiY(), this);
                }
                g.drawString("Pisteesi: " + pisteet, 10, 20);
            }//end if tila=2 

            if (tila == 3){ for (int j = 0; j < 2; j++) {
                    g.drawImage(kori2[j].palautaKuva(), kori2[j].palautaSijaintiX(), kori2[j].palautaSijaintiY(), this);
                    g.drawString("Uudelleen", kori2[0].palautaSijaintiX()+30, kori2[0].palautaSijaintiY()+kori2[0].palautaKuvaH()+20);
                    g.drawString("Lopeta", kori2[1].palautaSijaintiX()+45, kori2[1].palautaSijaintiY()+kori2[1].palautaKuvaH()+20);
                }   
                g.drawString("Pisteesi: " + pisteet, 40, 100);
            }//end if tila=3


        }//end paincomponent
    }//end Piirtopaneeli

}//end all

