package sas.miseenbase.bdd;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import sas.miseenbase.sms.Sms;

/**
 * Classe pour l'insertion en base des SMS
 * Contient les prepared statetments pour toutes les bases
 * @param Aucun argument requis
 * @return Renvoi un objet connexion
 * @author J Desprez
 */
public class InsertionBDD {
	
	private static Logger logger = Logger.getLogger("SAS");
	/* ------- variables -------- */
	private PreparedStatement requetePrepareePol;
	private PreparedStatement requetePrepareePom;
	private PreparedStatement requetePrepareeSam;
	private PreparedStatement requetePrepareeGen;
	private ConnexionBdd connexion = new ConnexionBdd();
	//détermine le nombre d'attribut à mettre dans la requete
	private int nbAttributs = 0;
	//Indices type de secours
	static final int INDICE_GENDARMERIE = 0;
	static final int INDICE_POLICE = 1;
	static final int INDICE_POMPIER = 2;
	static final int INDICE_SAMU = 3;
	
	/* ----- Constructeurs ------ */
	/**
	 * Crée un module d'insertion avec les requetes préparées pour toutes les tables
	 * @param connexion_fournie
	 */
	public InsertionBDD() {
		String corpsRequete = "(id, Numero_envoyeur, SMSC_number, Date_envoi, Date_reception, Centre_secours, Type_secours, Nom, Prenom, Num_rue, Bis_ter, Type_rue, Nom_rue, Ville, Code_postal, Batiment, Etage, Code, Comm, Comm_final, traitement, Date_naissance, Civilite, Antecedents, Nom_contact, num_contact_dom, num_contact_port, Marque_tel, lat, lon) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String corpsRequeteSamu = "(id, Numero_envoyeur, SMSC_number, Date_envoi, Date_reception, Centre_secours, Temoin, Interv_vitale, Nom, Prenom, Num_rue, Bis_ter, Type_rue, Nom_rue, Ville, Code_postal, Batiment, Etage, Code, Comm, Comm_final, traitement, Date_naissance, Civilite, Antecedents, Nom_contact, num_contact_dom, num_contact_port, Marque_tel, lat, lon) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String psPom="INSERT INTO POMPIERS "+corpsRequete;
		String psPol="INSERT INTO POLICE "+corpsRequete;
		String psSam="INSERT INTO SAMU "+corpsRequeteSamu;
		String psGen="INSERT INTO GENDARMERIE "+corpsRequete;
		
		// preparation du prepared statement
		try {
			this.requetePrepareePol = connexion.getConnexion().prepareStatement(psPol);
			this.requetePrepareePom = connexion.getConnexion().prepareStatement(psPom);
			this.requetePrepareeSam = connexion.getConnexion().prepareStatement(psSam);
			this.requetePrepareeGen = connexion.getConnexion().prepareStatement(psGen);
		} catch (SQLException e) {
			logger.log(Level.SEVERE,"SQL erreur durant la construction de la requete preparee.",e);
		}
	}
	
	/* --------- Methodes ------------- */
	/**
	 * Insère un sms dans la table correcte, identifiée depuis le contenu des champs
	 * @param Sms smsAinserer
	 * @return true false
	 */
	public boolean insererSMS (Sms smsAinserer){
		//récupération des attributs
		String attributsAInserer [] = smsAinserer.getAttibutsSms(); 
		/* Note à ce point, le java 7 fait des switch avec des String, pour un soucis d'interopérabilité, nous ne le ferons pas */
		try {
			PreparedStatement requetePreparee;
			// Specification de la table correcte pour insertion du contenu
			System.out.println(attributsAInserer[Sms.EMPLACEMENTCENTRESECOURS]);
			switch( (Integer.parseInt(attributsAInserer[Sms.EMPLACEMENTCENTRESECOURS]))){
				// Choix du prepared statement à utiliser et choix du nombre d'arguments à mettre en base (!= suivant la table)
				case INDICE_GENDARMERIE: requetePreparee = requetePrepareeGen;
										 nbAttributs = Sms.NBATTRIBUTS;
										 break;
						
				case INDICE_POLICE: requetePreparee = requetePrepareePol;
									nbAttributs = Sms.NBATTRIBUTS;
									break;
						
				case INDICE_POMPIER: requetePreparee = requetePrepareePom;
									 nbAttributs = Sms.NBATTRIBUTS;
									 break;
						
				case INDICE_SAMU: requetePreparee = requetePrepareeSam;
								  nbAttributs = Sms.NBATTRIBUTSSAMU;
								  break;
				// erreur, on abandonne
				default:return false; 
			}
			// Preparation de tous les champs de la requête
			for(int i = 1; i < nbAttributs+1 ; i++)
			{
				requetePreparee.setString(i,attributsAInserer[i-1]);
			}
			// on execute l'insertion
			requetePreparee.executeUpdate(); 
			logger.info("Requête executée avec succes.");
		} catch (Exception e) {
			// erreur à l'insertion
			logger.log(Level.SEVERE,"SQL erreur durant la l'execution de la requete preparee.",e);
			return false; 
		}
		return true;
	}
}