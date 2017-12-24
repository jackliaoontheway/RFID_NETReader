package RFID;

public class rfidlib_LSGate {
	static{
		System.loadLibrary("jni_rfidlib_LSGate");
	}
	public native static int LSG_GetLibVersion(char[] buf ,int nSize /* in character */)  ;
	public native static int LSG_CmdGetReports(long hr ,byte Flag,byte recordsToGet) ;
	public native static int LSG_CmdGetSystemTime(long hr,Integer year,Byte month,Byte day ,Byte hour,Byte minute,Byte second);
	public native static int LSG_CmdSetSystemTime(long hr,int year,byte month,byte day ,byte hour,byte minute,byte second) ;
	public native static int LSG_CmdResetFlowOfPeople(long hr,byte flag) ;
	public native static int LSG_CmdGetCurrentFlowOfPeople(long hr,Integer inFlow,Integer outFlow);
	public native static int LSG_CmdReverseDirection(long hr);
	public native static int LSG_ParseSCEventData(long hReport ,char[] slData,Integer nSize,Byte dir ,byte[] time) ;
}
