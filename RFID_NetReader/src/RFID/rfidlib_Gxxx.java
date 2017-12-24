package RFID;

public class rfidlib_Gxxx {
	static{
		System.loadLibrary("jni_rfidlib_Gxxx");
	}
	public native static int MGXXX_GetLibVersion(char[] buf ,int nSize/* in character */);
	public native static int MGXXX_GetReports(long hr ,byte Flag);
	public native static int MGXXX_RunLoopCollecting(long hr);
	public native static int MGXXX_StopLoopCollecting(long hr);
	public native static int MGXXX_StopLoopCollectingBegin(long hr);
	public native static int MGXXX_StopLoopCollectingEnd(long hr);
	public native static int MGXXX_ParseIOReportData(long hr,long hreport,char[]uid,Integer nSize/*in and out*/,Byte dir/*out*/,byte[]time);
}
