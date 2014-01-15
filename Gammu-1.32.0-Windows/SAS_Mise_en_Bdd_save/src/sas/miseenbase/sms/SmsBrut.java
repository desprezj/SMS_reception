package sas.miseenbase.sms;

import java.util.logging.Logger;

/**
 * Classe récupérant un sms brut depuis le fichier texte
 * Ces sms ne peuvent pas mis en base (non formatés)
 * @param args
 * @author J Desprez
 */
public class SmsBrut {
	
	private static Logger logger = Logger.getLogger("SAS");
	/* constantes (pour la qualité) */
	static final int N0 = 0;static final int N1 = 1;static final int N2 = 2;static final int N3 = 3;static final int N4 = 4;static final int N5 = 5;static final int N6 = 6;static final int N7 = 7;static final int N8 = 8;static final int N9 = 9;
	static final int N10 = 10;static final int N11 = 11;static final int N12 = 12;static final int N13 = 13;static final int N14 = 14;static final int N15 = 15;static final int N16 = 16;static final int N17 = 17;
	static final int N18 = 18;static final int N19 = 19;static final int N20 = 20;static final int N21 = 21;static final int N22 = 22;static final int N23 = 23;static final int N24 = 24;static final int N25 = 25;
	static final int N26 = 26;static final int N27 = 27;static final int N28 = 28;static final int N29 = 29; static final String S0 = "0";
	static final int NBATTRIBUTS_TEMP = 25;
	static final int NBATTRIBUTS_TEMP_SAMU = 26;
	/* ------------ variables --------------- */
	// contient la chaine de caractères dans le sms
	private String texteNonFormate = ""; 
	private String separateurFormatage = ";";
	
	/*-- Constructeur et getters / setters --*/
	
	public SmsBrut()   {}

	/* --------------- Methodes --------------- */
	/**
	 * Transforme un Sms_brut en un Sms valide déformaté
	 * @param Sms_brut
	 * @return Sms déformaté
	 */
	public Sms deformaterLeSms(String smscNumber, String dateReception,String numEnvoyeur){
		if(this.texteNonFormate.isEmpty()){
			logger.warning("Echec du deformatage: texte vide!");
			return null;
		}
		else{
			/* Déformatage du contenu du sms */
			// Valeur à régler déjà dans Sms
			String temp [] = this.texteNonFormate.split(separateurFormatage);
			if(temp.length == NBATTRIBUTS_TEMP)
			{
				logger.info("Parsage pomp");
				//Création du tableau contnenant les attributs triés
				String attributs[] = new String [Sms.NBATTRIBUTS];
				//Déclarations pour la qualité
				//Num Envoyeur
				attributs[N0] = numEnvoyeur;   
				//Smsc number
				attributs[N1] = smscNumber;   
				//Date envoi
				attributs[N2] = temp[N2];
				//Date reception
				attributs[N3] = dateReception; 
				//Centre sec
				attributs[N4] = temp[N0];
				//Type secours
				attributs[N5] = temp[N1];
				//Nom
				attributs[N6] = temp[N3];
				//prenom
				attributs[N7] = temp[N4];
				//Num rue
				attributs[N8] = temp[N8];
				//Bis ter
				attributs[N9] = temp[N9];
				//Type de rue
				attributs[N10] = temp[N10];
				//Nom rue
				attributs[N11] = temp[N11];
				//Ville
				attributs[N12] = temp[N12];
				//Code postal
				attributs[N13] = temp[N13];
				//Batiment
				attributs[N14] = temp[N14];
				//Etage
				attributs[N15] = temp[N15];
				//Code
				attributs[N16] = temp[N16];
				//Comm
				attributs[N17] = temp[N17];
				//Com final
				attributs[N18] = temp[N18];
				//traitement 0 ou 1 traité ou non
				attributs[N19] = S0;
				//Date de naissance
				attributs[N20] = temp[N5];	  
				//Civilité
				attributs[N21] = temp[N6];	  
				//Antécedents
				attributs[N22] = temp[N7];	  
				//Nom contact
				attributs[N23] = temp[N19];	  
				//Num Contact dom
				attributs[N24] = temp[N20];	  
				//Num Contact port
				attributs[N25] = temp[N21];	  
				//Marque tel
				attributs[N26] = temp[N22];	  
				//lat
				attributs[N27] = temp[N23];	  
				//long
				attributs[N28] = temp[N24];
				/* On crée le sms valide */
				return new Sms(attributs);
			}
			if(temp.length == NBATTRIBUTS_TEMP_SAMU)
			{
				//Création du tableau contnenant les attributs triés
				String attributs[] = new String [Sms.NBATTRIBUTSSAMU];
				//Déclarations pour la qualité
				//Num Envoyeur
				attributs[N0] = numEnvoyeur;   
				//Smsc number
				attributs[N1] = smscNumber;   
				//Date envoi
				attributs[N2] = temp[N3];
				//Date reception
				attributs[N3] = dateReception; 
				//Centre sec
				attributs[N4] = temp[N0];
				//Temoin
				attributs[N5] = temp[N1];
				//Interv-vitale
				attributs[N6] = temp[N2];
				//Nom
				attributs[N7] = temp[N4];
				//prenom
				attributs[N8] = temp[N5];
				//Num rue
				attributs[N9] = temp[N9];
				//Bis ter
				attributs[N10] = temp[N10];
				//Type de rue
				attributs[N11] = temp[N11];
				//Nom rue
				attributs[N12] = temp[N12];
				//Ville
				attributs[N13] = temp[N13];
				//Code postal
				attributs[N14] = temp[N14];
				//Batiment
				attributs[N15] = temp[N15];
				//Etage
				attributs[N16] = temp[N16];
				//Code
				attributs[N17] = temp[N17];
				//Comm
				attributs[N18] = temp[N18];
				//Com final
				attributs[N19] = temp[N19];
				//traitement 0 ou 1 traité ou non
				attributs[N20] = S0;
				//Date de naissance
				attributs[N21] = temp[N6];	  
				//Civilité
				attributs[N22] = temp[N7];	  
				//Antécedents
				attributs[N23] = temp[N8];	  
				//Nom contact
				attributs[N24] = temp[N20];	  
				//Num Contact dom
				attributs[N25] = temp[N21];	  
				//Num Contact port
				attributs[N26] = temp[N22];	  
				//Marque tel
				attributs[N27] = temp[N23];	  
				//lat
				attributs[N28] = temp[N24];	  
				//long
				attributs[N29] = temp[N25];
				/* On crée le sms valide */
				return new Sms(attributs);
			}
			else{
				logger.info("Nombre d'attributs mauvais:" +temp.length+ "constatés");
				return null;
			}
		}
	}
	
	/**
	 * Rempli le texte brut d'un SMS, revoie true si l'allocation a fonctionné
	 * @param texteBrut
	 * @return true false
	 */
	public boolean remplirSms(String texteBrut){
		if(texteBrut != null){
			this.texteNonFormate = texteBrut;
			return true;
		}
		else {return false;}
	}
}