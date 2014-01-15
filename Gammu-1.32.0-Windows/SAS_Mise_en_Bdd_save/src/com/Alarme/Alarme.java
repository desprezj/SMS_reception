package com.Alarme;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.DllHandle.AlarmDLL;
import com.sun.jna.Native;
/**
 * Classe alarmes
 */
public class Alarme {

	private AlarmDLL alarme;
	
	/**
	 * Constructeur de la classe alarme : instanciation de la DLL
	 * @throws IOException 
	 */
	public Alarme() 
	{
		String path ="./confAlarme.txt";
		String port = "";
		String dllPath = "" ;
		BufferedReader br;

		try {	
			br = new BufferedReader(new FileReader(path));	
			port = br.readLine();
			dllPath = br.readLine();
		} catch (FileNotFoundException e) {
			System.out.println("Fichier de conf non trouvé");
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		//Chemin d'accès aux DLL
		System.setProperty("jna.library.path", dllPath);
		//Choix de la DLL 32 ou 64 bits
		if(System.getProperty("sun.arch.data.model").equals("64")) {
			alarme = (AlarmDLL) Native.loadLibrary("AlarmeSAS64", AlarmDLL.class);
		} else {
			alarme = (AlarmDLL) Native.loadLibrary("AlarmeSAS32", AlarmDLL.class);		
		}
		//Ouverture du port
		alarme.ComOpenPort(Integer.valueOf(port));
	}
	
	/**
	 * Allumage d'une alarme
	 * @return Nombre d'alarmes allumées
	 */
	public void alarmOn()
	{
		alarme.AlarmOn();
	}

	/**
	 * Extinction d'une alarme
	 * @return Nombre d'alarmes allumées
	 */
	public void alarmOff()
	{
		alarme.AlarmOff();
	}
	
	/**
	 * Fermeture de la DLL
	 */
	public void closeAlarm() {
		alarme.ComClosePort();
	}
	
}
