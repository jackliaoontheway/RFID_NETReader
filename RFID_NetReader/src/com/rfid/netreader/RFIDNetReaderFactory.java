package com.rfid.netreader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import RFID.rfid_def;
import RFID.rfidlib_LSGate;
import RFID.rfidlib_reader;

public class RFIDNetReaderFactory {

	private static RFIDNetReaderFactory RFID_INSTANCE = null;

	private RFIDNetReaderFactory() {

	}

	public static RFIDNetReaderFactory getInstance() {
		if (RFID_INSTANCE == null) {
			RFID_INSTANCE = new RFIDNetReaderFactory();
		}
		return RFID_INSTANCE;
	}

	private long m_handle = 0;// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�

	private void loadDriver() {
		// 锟斤拷锟斤拷锟借备锟斤拷锟斤拷
//		String baseDirPath = System.getProperty("user.dir");
		rfidlib_reader.RDR_LoadReaderDrivers("C:\\rfiddriver\\Drivers");
	}

	private void openDev(String Ip) {
		String connStr = String.format("RDType=LSG405;CommType=NET;RemoteIP=%s;RemotePort=6012;LocalIP=", Ip);
		Long hrOut = new Long(0);
		int nret = rfidlib_reader.RDR_Open(connStr, hrOut);
		if (nret == 0) {
			m_handle = hrOut;
		} else {
			System.out.println("Open device failed!");
		}
	}

	private void closeDev() {
		int nret = rfidlib_reader.RDR_Close(m_handle);
		if (nret == 0) {
			m_handle = 0;
		} else {
			System.out.println("Close device failed!");
		}
	}

	public Set<String> readAllRFID(String Ip,Date startTime) {

		loadDriver();

		openDev(Ip);

		Set<String> rfidList = readRFID(startTime);

		closeDev();

		return rfidList;
	}

	public static void main(String[] args) {
		RFIDNetReaderFactory frm = new RFIDNetReaderFactory();

		String ip = "192.168.5.7";

		Set<String> rfidList = frm.readAllRFID(ip,new Date());
		System.out.println(rfidList.size());
		System.out.println(rfidList);
	}

	private Set<String> readRFID(Date startTime) {
		Set<String> rfidList = new TreeSet<>();

		int nret = 0;
		byte flag = 0;
		rfidlib_reader.RDR_ResetCommuImmeTimeout(m_handle);

		boolean deadWhile = true;
		while (deadWhile) {
			nret = rfidlib_LSGate.LSG_CmdGetReports(m_handle, flag, (byte) 10);
			if (nret != 0) {
				System.out.println(String.format("err:%d", nret));
				break;
			} else {
				flag = 1;
				int mCnt = rfidlib_reader.RDR_GetTagDataReportCount(m_handle);
				if (mCnt > 0) {
					int hReport = rfidlib_reader.RDR_GetTagDataReport(m_handle, rfid_def.RFID_SEEK_FIRST);
					while (hReport != 0) {
						char[] slData = new char[64];
						Integer nSize = new Integer(64);
						Byte dir = new Byte((byte) 0);
						byte[] m_Time = new byte[6];
						nret = rfidlib_LSGate.LSG_ParseSCEventData(hReport, slData, nSize, dir, m_Time);
						if (nret != 0) {
							flag = 0;
							break;
						}
						
						
						String strDate = String.format("%d-%d-%d %d:%d:%d", m_Time[0],m_Time[1],m_Time[2],m_Time[3],
								m_Time[4],m_Time[5]);
						SimpleDateFormat dateFormat=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
						try {
							Date rfidDate = dateFormat.parse(strDate);
							if(startTime == null || rfidDate.after(startTime)) {
								rfidList.add(String.valueOf(slData).trim());
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
						hReport = rfidlib_reader.RDR_GetTagDataReport(m_handle, rfid_def.RFID_SEEK_NEXT);
					}
				}
				else {
					break;
				}
			}
		}

		rfidlib_reader.RDR_ResetCommuImmeTimeout(m_handle);
		return rfidList;
	}
}
