package sas.miseenbase.sms;

import java.util.logging.Logger;

/**
 * Classe contenant un sms d�format�
 * Ces sms peuvent �tre mis en base
 * @param args
 * @author J Desprez
 */
public class Sms {
	private static Logger logger = Logger.getLogger("SAS");
	/* ----------------  Variables -------------- */
	// Nombre de champs dans le sms (pour le samu et les autres tables)
	public static final int NBATTRIBUTS = 29;
	public static final int NBATTRIBUTSSAMU = 30;
	// Indice de l'attibut contenant le centre de secours commence � 0
	public static final int EMPLACEMENTCENTRESECOURS = 4;
	// Tableau des attributs deformat�s
	private String attributsSms [] = new String [NBATTRIBUTSSAMU];
	
	/* -------- Constructeurs et getters--------- */
	/**
	 * Construit un SMS vide
	 * @param null
	 */
	public Sms() {
		logger.info("Sms Cr��");
	}
	/**
	 * Construit un SMS � l'aide de ses attributs
	 * @param [] Attributs
	 */
	public Sms(String[] attributs) {
		
		if(attributs.length == NBATTRIBUTSSAMU || attributs.length == NBATTRIBUTS){
			// remplissage des champs du sms
			for (int i=0; i < attributs.length ; i++) 
			{
				this.attributsSms[i] = attributs[i];			
			}
		}
		else{
			logger.warning("Erreur, le tableau transmis n est pas de la bonne taille, taille attendue:"+NBATTRIBUTSSAMU+" constate:"+attributs.length);
		}
	}

	/**
	 * Retourne le tableau d'attributs
	 * @return [] Attributs
	 */
	public String[] getAttibutsSms() {
		return attributsSms;
	}
	
	/**
	 * Setter ecrit les attributs de l'objet sms
	 * @param attibutsSms
	 */
	public void setAttibutsSms(String[] attibutsSms) {
		if(attributsSms.length == NBATTRIBUTSSAMU || attributsSms.length == NBATTRIBUTS){
			// remplissage des champs du sms
			for (int i=0; i < attributsSms.length ; i++) 
			{
				this.attributsSms[i] = attributsSms[i];			
			}
		}
		else{
			logger.warning("Erreur, le tableau transmis n est pas de la bonne taille, taille attendue:"+NBATTRIBUTSSAMU+" constate:"+attributsSms.length);
		}
	}
}
