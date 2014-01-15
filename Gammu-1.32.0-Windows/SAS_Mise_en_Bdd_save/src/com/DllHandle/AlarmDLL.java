package com.DllHandle;
import com.sun.jna.Library;

public interface AlarmDLL extends Library {
	
	void ComClosePort();
	void AlarmOn();
	void AlarmOff();
	int ComOpenPort(int port);
}
