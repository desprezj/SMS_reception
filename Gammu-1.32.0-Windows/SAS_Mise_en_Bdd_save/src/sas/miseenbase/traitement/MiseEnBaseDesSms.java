package sas.miseenbase.traitement;

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.Alarme.Alarme;

import sas.miseenbase.alarmesonor.WavePlayer;
import sas.miseenbase.bdd.InsertionBDD;
import sas.miseenbase.sms.Sms;
import sas.miseenbase.sms.SmsBrut;

/**
 * Classe effectuant les appels à gammu et aux mises en base
 * Tourne en continu
 * @param args
 * @author julien desprez
 */
public final class MiseEnBaseDesSms {
	static final int TAILLEINFOSGAMMU = 3;
	static final int DEBUT_SMSC_REMOTE = 24;
	static final int FIN_SMSC_REMOTE = 36;
	static final int DEBUT_SENT = 23;
	static final int LARGEUR_FENETRE = 400;
	static final int HAUTEUR_FENETRE = 140;
    static WavePlayer wp;
    static ImageIcon icon = new ImageIcon("C:/SAS/Gammu-1.32.0-Windows/SASv1.gif");
    static JLabel lab = new JLabel(icon);
    //Déclaration fenêtre d'alarme
    static JFrame fenetre;
    //Déclaration de l'objet alarme
    static private Alarme alarme;
    
	private static Logger logger = Logger.getLogger("SAS");
	/**
	 * main
	 * @param args
	 * @author T LAVAUD P2013
	 */
	public static void main(String[] args) {
		
		BufferedReader lecture = null ;
		String ligneLue = "";
	    String infosSmsBrut[] = new String [TAILLEINFOSGAMMU];
	    //Chemin jusqu'au batch lançant Gammu
		String path = "C:/SAS/Gammu-1.32.0-Windows/gammu/bin/extraction_sms.bat";
		// Chemin jusqu'au fichier texte contenant les sms
		String pathSms = "C:/SAS/Gammu-1.32.0-Windows/gammu/bin/sms.txt";
		// Chemin jusqu'au fichier sonore
		String pathSonSms = "C:/SAS/Gammu-1.32.0-Windows/NouveauSms.WAV";
		//Initialisation Wave
		wp = new WavePlayer(pathSonSms);
		// Création d'un objet sms brut
		SmsBrut smsGammu = new SmsBrut();
		// Création d'un objet sms déformaté
		Sms smsDeformate;
		// Création d'un module d'insertion en base cntenant la connexion et les requêtes préparées
		InsertionBDD moduleInsertion = new InsertionBDD();
		//Création de l'objet alarme
		alarme = new Alarme();
		
		/* Début de la déclaration de la fenetre */
		fenetre = new JFrame(); 
	    //Définit un titre pour notre fenêtre
	    fenetre.setTitle("SAS - Secours Alertés Par SMS (Mise en base des sms)");
	    //Définit sa taille : 400 pixels de large et 100 pixels de haut
	    fenetre.setSize(LARGEUR_FENETRE, HAUTEUR_FENETRE);
	    //Nous demandons maintenant à notre objet de se positionner au centre
	    fenetre.setLocationRelativeTo(null);
	    //Termine le processus lorsqu'on clique sur la croix rouge
	    fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //Et enfin, la rendre visible        
	    fenetre.setVisible(true);
	    fenetre.setContentPane(creerTexteJframe());
	    /* Fin de la déclaration de la fenetre */
		/* --------------- Debut de la boucle ---------------- */
	    
		do
		{
			// Application du script Gammu pour mise des SMS dans un fichier
			try{
				logger.info("Lancement de l'extraction des sms...");
			    Process p = Runtime.getRuntime().exec("cmd /c "+path);
			    // on attend la fin de l'execution du script pour continuer
			    p.waitFor();
			    logger.info("... terminé.");
			    // Gestion des erreurs
			}catch( IOException ex )          {logger.log(Level.SEVERE,"Exception In/out",ex);
			}catch( InterruptedException ex ) {logger.log(Level.SEVERE,"Script interrompu par un élément extérieur",ex);}

			// -------------------- Ouverture et parcours du fichier contenant les sms
			try
			{
				if(new File(path).exists()){
					lecture = new BufferedReader(new FileReader(pathSms)) ;
					ligneLue = lecture.readLine();
					while(ligneLue != null)
					{
						if(ligneLue.startsWith("SMSC"))   {infosSmsBrut[0] = ligneLue.substring(DEBUT_SMSC_REMOTE,FIN_SMSC_REMOTE);}
						if(ligneLue.startsWith("Sent"))   {infosSmsBrut[1] = ligneLue.substring(DEBUT_SENT);}
						if(ligneLue.startsWith("Remote")) {infosSmsBrut[2] = ligneLue.substring(DEBUT_SMSC_REMOTE,FIN_SMSC_REMOTE);}
							
						if(ligneLue.equals("Status               : UnRead"))
						{
							ligneLue = lecture.readLine();
							if(ligneLue.startsWith("User"))  {lecture.readLine();}
							
							ligneLue = lecture.readLine();
							if(ligneLue != null){
								logger.info("!!! NOUVEAU SMS ARRIVE !!!");
								if(!wp.isAlive())   {
									wp = new WavePlayer(pathSonSms);
									wp.start();
									alarme.alarmOn();
									setAlarmeVisible();
								}
								smsGammu.remplirSms(ligneLue);
								//Deformatage du SMS
								smsDeformate = smsGammu.deformaterLeSms(infosSmsBrut[0],infosSmsBrut[1],infosSmsBrut[2]);
								// On verifie que le sms n'est pas problématique
								if(smsDeformate != null){
									//on insère le sms
									logger.info("Insertion en base");
									moduleInsertion.insererSMS(smsDeformate);
								}
							}
						}
						ligneLue = lecture.readLine();
					}
					// fermetutre du fichier
					lecture.close();
				}
			}//try
			catch (Exception a)           {logger.log(Level.SEVERE,"Exception In/out",a);}
			// Fermeture du reader

			
		}while(true); 
		// Fin de la boucle
		/* ---------- Fin de la boucle principale ---------- */
	}
	
	private MiseEnBaseDesSms(){}
	
	/**
	 * montre l'alarme visuelle
	 */
	private static void setAlarmeVisible(){
		lab.setVisible(true);
		fenetre.setSize(650, 440);
        fenetre.validate();
	}
	
	/**
	 * cache l'alarme visuelle
	 */
	private static void setAlarmeInvisible(){
		lab.setVisible(false);
		fenetre.setSize(400, 140);
        fenetre.validate();
	}
	
	/**
	 * Création du contenu de la Jframe pour sigbifier à l'utilisateur que l'application tourne
	 * Permet aussi de couper le processus
	 * @return le label contenant le texte de la fenêtre
	 */
	private static JPanel creerTexteJframe(){
		//Déclaration du panel
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		//Déclaration du premier Label (le retour à la ligne n'est aps possible en Jlabel)
		JLabel label = new JLabel("Le module de mise en base SAS est en cours.");
		//Mise dans le panel
		panel.add(label);
		//Déclaration du second Label
		label = new JLabel("Pour arrêter le module, cliquer sur la croix supérieure droite.");
		//Mise dans le panel
		panel.add(label);
		//On retourne le panel pour la JFrame
		JButton jButton_stop = new javax.swing.JButton();
        jButton_stop.setText("Stopper l'alarme");
        jButton_stop.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                //On coupe le thread d'alarme sonore musicale
            	wp.cancel();
            	//On coupe l'alarme
                alarme.alarmOff();
                //On cache le Gif pour epilectique
                setAlarmeInvisible();
            }
        });
        panel.add(jButton_stop);
        panel.add(lab);
        lab.setVisible(false);
        panel.validate();
		return panel;
	}	
}

