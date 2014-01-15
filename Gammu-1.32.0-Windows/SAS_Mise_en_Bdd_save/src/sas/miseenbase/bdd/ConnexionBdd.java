package sas.miseenbase.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe contenant les attributs pour la connexion avec la base de données
 * Permet de générer et fermer des connexions
 * @param Aucun argument requis
 * @return Renvoi un objet connexion
 * @author J Desprez
 */
public class ConnexionBdd {
	
	private static Logger logger = Logger.getLogger("SAS");
	/* ------------ variables --------------- */
	
	private static String serverConnexion = "localhost";
	private static String portConnexion = "3306";
	private static String baseConnexion = "SAS";
	private static String userConnexion = "user";
	private static String mdpConnexion = "passsas";
	private Connection connexion = null;
	
	/*--------------------- Méthodes --------------------*/
	/**
	 * Verifie qu'une connexion n'est pas nulle
	 * Retourne true si connexion existe sinon false
	 * @param null
	 * @return boolean active
	 */
	public boolean connexionActive(){
		if(connexion != null)
			{return true;}
		return false;
	}
	
	/**
	 * Ferme une connexion, ou déclare la raison de l'echec
	 * Retourne le succes ou la raison de l'echec
	 * @param null
	 * @return String info
	 */
	public String fermerConnexion(){
		if(connexion != null){
			try {
				connexion.close();
				return "Connexion fermee avec succes";
			} catch (SQLException e) {
				return "La connexion n'a pas pu être fermée";
			}
		}
		else	{return "La connexion n'existe pas";}
	}
	
	/*--------- Constructeur et getters / setters ---------*/
	/**
	 * Crée une connexion Mysql et stocke dans l'objet Connexion_bdd
	 * Retourne un string de succes ou la raison de l'echec
	 * @param null
	 * @return String
	 */
	public ConnexionBdd(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://"+serverConnexion+":"+portConnexion+"/"+baseConnexion;
			this.connexion = DriverManager.getConnection(url, userConnexion, mdpConnexion);
		}
		catch (ClassNotFoundException e)  { logger.log(Level.SEVERE,"Erreur de connexion: pilote",e);}
		catch (SQLException e)            { logger.log(Level.SEVERE,"Erreur de connexion: SQL",e);}
	}

	public Connection getConnexion() {
		return connexion;
	}

}
