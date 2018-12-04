package DhApi;

import com.dh.DpsdkCore.*;

import java.io.IOException;

public class TalkStream
{
	public IDpsdkCore dpsdk = new IDpsdkCore();
	public int 	    m_nDLLHandle    = -1;
	public String   m_strAlarmCamareID = "1000000";
	public String   m_strRealCamareID = "1000000$1$0$0";
	public String   m_strPTZCamareID = "1000000$1$0$0";
	public String   m_strNetAlarmHostCamareID = "1000000$1$0$0";
	public String   m_strNetAlarmHostDeviceID = "1000000";

	public String 	m_strIp 		= "101.89.177.190";
	public int    	m_nPort 		= 9000;
	public String 	m_strUser 		= "admin";
	public String 	m_strPassword 	= "1234qwer";

	public static String StrCarNum;
	public static boolean bDownloadFinish = false;

	Return_Value_Info_t m_nPlaybackSeq = new Return_Value_Info_t();
	Return_Value_Info_t nRealSeq = new Return_Value_Info_t();
	Record_Info_t records;

	public fDPSDKDevStatusCallback fDeviceStatus = new fDPSDKDevStatusCallback() {
		@Override
		public void invoke(int nPDLLHandle, byte[] szDeviceId, int nStatus) {
			String status = "离线";
			if(nStatus == 1)
			{
				status = "在线";
				Device_Info_Ex_t deviceInfo = new Device_Info_Ex_t();
				int nRet = IDpsdkCore.DPSDK_GetDeviceInfoExById(m_nDLLHandle, szDeviceId,deviceInfo);
				if(deviceInfo.nDevType == dpsdk_dev_type_e.DEV_TYPE_NVR || deviceInfo.nDevType == dpsdk_dev_type_e.DEV_TYPE_EVS || deviceInfo.nDevType == dpsdk_dev_type_e.DEV_TYPE_SMART_NVR || deviceInfo.nDevType == dpsdk_dev_type_e.DEV_MATRIX_NVR6000)
				{
					nRet = IDpsdkCore.DPSDK_QueryNVRChnlStatus(m_nDLLHandle, szDeviceId , 10*1000);

					if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS)
					{
						System.out.printf("查询NVR通道状态成功，nRet = %d", nRet);
					}else
					{
						System.out.printf("查询NVR通道状态失败，nRet = %d", nRet);
					}
					System.out.println();
				}
			}
			System.out.printf("Device Status Report!, szDeviceId = %s, nStatus = %s", new String(szDeviceId),status);
			System.out.println();
		}
	};

	public fDPSDKNVRChnlStatusCallback fNVRChnlStatus = new fDPSDKNVRChnlStatusCallback() {
		@Override
		public void invoke(int nPDLLHandle, byte[] szCameraId, int nStatus) {
			String status = "离线";
			if(nStatus == 1)
			{
				status = "在线";
			}
			System.out.printf("NVR Channel Status Report!, szCameraId = %s, nStatus = %s", new String(szCameraId),status);
			System.out.println();
		}
	};

	public fDPSDKGeneralJsonTransportCallback fGeneralJson = new fDPSDKGeneralJsonTransportCallback() {
		@Override
		public void invoke(int nPDLLHandle, byte[] szJson) {
			System.out.printf("General Json Return, ReturnJson = %s", new String(szJson));
			System.out.println();
		}
	};

	public fMediaDataCallback fm = new fMediaDataCallback() {
				@Override
				public void invoke(int nPDLLHandle, int nSeq, int nMediaType,
									byte[] szNodeId, int nParamVal, byte[] szData, int nDataLen) {
					//System.out.print("fMediaDataCallback, nDataLen = ");
					System.out.printf("DPSDKMediaDataCallback nSeq = %d, nMediaType = %d, nDataLen = %d", nSeq, nMediaType, nDataLen);
					System.out.println(nDataLen);
				}
			};
	public fDPSDKNetAlarmHostStatusCallback fNetAlarmHostStatus = new fDPSDKNetAlarmHostStatusCallback() {
				@Override
				public void invoke(int nPDLLHandle, byte[] szDeviceId, int nRType, int nOperType, int nStatus) {
					String DeviceId = new String(szDeviceId);
					String strReportType = "";
					String strOperationType = "";
					String strStatus = "";
					switch(nRType)
					{
						case 1:
							strReportType = "留守布防";
							break;
						case 2:
							strReportType = "防区旁路";
							break;
						default:
							strReportType = "未知";
							break;
					}
					switch(nOperType)
					{
						case 1:
							strOperationType = "设备 布/撤防";
							break;
						case 2:
							strOperationType = "通道 布/撤防";
							break;
						case 3:
							strOperationType = "报警输出通道操作";
							break;
						default:
							strOperationType = "未知";
							break;
					}
					switch(nStatus)
					{
						case 1:
							strStatus = "布防/旁路";
							break;
						case 2:
							strStatus = "撤防/取消旁路";
							break;
						default:
							strStatus = "未知";
							break;
					}
					System.out.printf("NetAlarmHostStatusCallback, szDeviceId[%s]nRType[%s]nOperType[%s]nStatus[%s]",DeviceId, strReportType, strOperationType, strStatus);
					System.out.println();
				}
			};

	public fDPSDKAlarmCallback fAlarm = new fDPSDKAlarmCallback() {
				@Override
				public void invoke( int nPDLLHandle, byte[] szAlarmId, int nDeviceType, byte[] szCameraId,
						byte[] szDeviceName, byte[] szChannelName, byte[] szCoding, byte[] szMessage, int nAlarmType,
						int nEventType, int nLevel, long nTime, byte[] pAlarmData, int nAlarmDataLen, byte[] pPicData, int nPicDataLen){
						System.out.printf("fDPSDKAlarmCallback, nAlarmType[%d]szCameraId[%s]szDeviceName[%s]szChannelName[%s]szCoding[%s]szMessage[%s]nTime[%d]pAlarmData[%s]nAlarmDataLen[%d]pPicData[%s]nPicDataLen[%d]",nAlarmType,new String(szCameraId),
						new String(szDeviceName),new String(szChannelName),new String(szCoding),new String(szMessage),nTime,new String(pAlarmData),nAlarmDataLen,new String(pPicData),nPicDataLen);
					System.out.println();
				}
			};

	//区间测速回调
	public fDPSDKGetAreaSpeedDetectCallback fGetAreaSpeedDetectCallback = new fDPSDKGetAreaSpeedDetectCallback() {
		@Override
		public void invoke(int nPDLLHandle, Area_Detect_Info_t areaSpeedDetectInfo) {
			try {
				StrCarNum = new String(areaSpeedDetectInfo.szCarNum, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.printf("AreaSpeedDetectInfo Report, szCarNum=%s, szPicName0=%s", StrCarNum.trim(), new String(areaSpeedDetectInfo.szPicName0).trim());
			System.out.println();
		}
	};

	//转码下载录像完成回调
	public fSaveRecordFinishedCallback fSaveRecordFinished = new fSaveRecordFinishedCallback() {
		@Override
		public void invoke(int nPDLLHandle, int nPlaybackSeq) {
			System.out.printf("录像下载完成，nPlaybackSeq = %d", nPlaybackSeq);
			System.out.println();
			bDownloadFinish = true;
		}
	};

	//语音对讲回调
	public fDPSDKTalkParamCallback fDPSDKTalkParam = new fDPSDKTalkParamCallback() {
		@Override
		public void invoke(int nPDLLHandle, int nTalkType, int nAudioType, int nAudioBit, int nSampleRate, int nTransMode) {

			System.out.printf("fDPSDKTalkParam Report");
			System.out.println();
		}
	};

	public static void main(String argv[])
	{
		new TalkStream();
	}
	public TalkStream()
	{
		Return_Value_Info_t res = new Return_Value_Info_t();
		int nRet = IDpsdkCore.DPSDK_Create(dpsdk_sdk_type_e.DPSDK_CORE_SDK_SERVER,res);
		m_nDLLHandle = res.nReturnValue;
		System.out.print("DPSDK_Create, m_nDLLHandle = ");
		System.out.println(m_nDLLHandle);
		if(m_nDLLHandle > 0)
		{
			//设置DPSDK日志路径
			//String fileName = "/root/DPSDK_log";
			//nRet = dpsdk.DPSDK_SetLog(m_nDLLHandle, fileName.getBytes());
			//if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS)
			//{
			//	System.out.printf("DPSDK_SetLog success! nRet = %d,fileNamepath = %s", nRet, fileName);
			//}else
			//{
			//	System.out.printf("DPSDK_SetLog failed! nRet = %d", nRet);
			//}
			//System.out.println();



			//login
			Login_Info_t loginInfo = new Login_Info_t();
			loginInfo.szIp = m_strIp.getBytes();
			loginInfo.nPort = m_nPort;
			loginInfo.szUsername = m_strUser.getBytes();
			loginInfo.szPassword = m_strPassword.getBytes();
			loginInfo.nProtocol = dpsdk_protocol_version_e.DPSDK_PROTOCOL_VERSION_II;
			loginInfo.iType = 1;


			nRet = IDpsdkCore.DPSDK_SetDPSDKTalkParamCallback(m_nDLLHandle, fDPSDKTalkParam);
			if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS)
			{
				System.out.printf("talk param success! nRet = %d", nRet);
			}else
			{
				System.out.printf("talk param failed! nRet = %d", nRet);
			}
			System.out.println();

			nRet = IDpsdkCore.DPSDK_Login(m_nDLLHandle,loginInfo,10000);
			if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS)
			{
				System.out.printf("login success! nRet = %d", nRet);
			}else
			{
				System.out.printf("login failed! nRet = %d", nRet);
			}
			System.out.println();

			//设置设备状态上报监听函数
			nRet = IDpsdkCore.DPSDK_SetDPSDKDeviceStatusCallback(m_nDLLHandle, fDeviceStatus);

			//设置NVR通道状态上报监听函数
			nRet =IDpsdkCore.DPSDK_SetDPSDKNVRChnlStatusCallback(m_nDLLHandle, fNVRChnlStatus);

			//设置通用JSON回调
			nRet = IDpsdkCore.DPSDK_SetGeneralJsonTransportCallback(m_nDLLHandle, fGeneralJson);

			//Set Net Alarm Host Status Callback
			nRet = IDpsdkCore.DPSDK_SetNetAlarmHostStatusCallback(m_nDLLHandle, fNetAlarmHostStatus);

			nRet = IDpsdkCore.DPSDK_SetDPSDKGetAreaSpeedDetectCallback(m_nDLLHandle, fGetAreaSpeedDetectCallback);

			nRet = IDpsdkCore.DPSDK_SetDPSDKSaveRecordFinishedCallback(m_nDLLHandle, fSaveRecordFinished);

			if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS)
			{
				System.out.printf("DPSDK_SetNetAlarmHostStatusCallback success! nRet = %d", nRet);
			}else
			{
				System.out.printf("DPSDK_SetNetAlarmHostStatusCallback failed! nRet = %d", nRet);
			}
			System.out.println();

			//load group info
			Return_Value_Info_t nLen = new Return_Value_Info_t();
			nRet = IDpsdkCore.DPSDK_LoadDGroupInfo(m_nDLLHandle, nLen, 10000);

			if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS)
			{
				System.out.printf("load all Group info success! nRet = %d, strLength = %d", nRet,nLen.nReturnValue);
			}else
			{
				System.out.printf("load all Group info failed! nRet = %d", nRet);
			}
			System.out.println();

			byte[] szGroupBuf = new byte[nLen.nReturnValue];
			nRet = IDpsdkCore.DPSDK_GetDGroupStr(m_nDLLHandle, szGroupBuf, nLen.nReturnValue, 10000);

			if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS)
			{
				//System.out.printf("获取所有组织树串成功，nRet = %d, szGroupBuf = [%s]", nRet, new String(szGroupBuf));

			}else
			{
				System.out.printf("获取所有组织树串失败，nRet = %d", nRet);
			}
			System.out.println();



			Return_Value_Info_t nTalkSeq = new Return_Value_Info_t();
			Get_TalkStream_Info_t getInfo = new Get_TalkStream_Info_t();
			getInfo.szCameraId = "1000000".getBytes();
			getInfo.nAudioType = dpsdk_audio_type_e.Talk_Coding_AAC;
			getInfo.nTalkType = dpsdk_talk_type_e.Talk_Type_Device;
			getInfo.nBitsType = dpsdk_talk_bits_e.Talk_Audio_Bits_8;
			getInfo.nSampleType = Talk_Sample_Rate_e.Talk_Audio_Sam_8K;
			getInfo.nTransType = dpsdk_trans_type_e.DPSDK_CORE_TRANSTYPE_TCP;
			nRet = IDpsdkCore.DPSDK_GetTalkStream(m_nDLLHandle, nTalkSeq, getInfo, fm, 100000);
			if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS)
			{
				System.out.printf("获取对讲机成功，nRet = %d", nRet);

			}else
			{
				System.out.printf("获取对讲机失败，nRet = %d", nRet);
			}
			System.out.println();

			// while(true) {
			// 	//延时15秒，等待服务模块建立链接
			// 	try{
			// 		Thread.sleep(15000);
			// 	}catch(InterruptedException e){
			// 	e.printStackTrace();
			// 	}
			// }

		}
	}
}
