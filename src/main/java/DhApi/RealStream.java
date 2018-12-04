package DhApi;

import com.dh.DpsdkCore.*;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class RealStream
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

    public static void main(String argv[])
    {
        new RealStream().getRealStream("1");
    }
    public String getRealStream(String deviceid)
    {
        Return_Value_Info_t res = new Return_Value_Info_t();
        int nRet = IDpsdkCore.DPSDK_Create(dpsdk_sdk_type_e.DPSDK_CORE_SDK_SERVER,res);
        m_nDLLHandle = res.nReturnValue;
        System.out.print("DPSDK_Create, m_nDLLHandle = ");
        System.out.println(m_nDLLHandle);
        String pRealStreamUrls = "";

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

            nRet = IDpsdkCore.DPSDK_Login(m_nDLLHandle,loginInfo,10000);
            if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS)
            {
                System.out.printf("login success! nRet = %d", nRet);
            }else
            {
                System.out.printf("login failed! nRet = %d", nRet);
            }
            System.out.println();

            //get real stream url
            Get_RealStreamUrl_Info_t pRealStreamUrlInfo = new Get_RealStreamUrl_Info_t();
            for (int i=0; i < 4; i++) {
                String m_strRealCamareIDTemp = deviceid + "$1$0$" + i;
                pRealStreamUrlInfo.szCameraId = m_strRealCamareIDTemp.getBytes();
                pRealStreamUrlInfo.nMediaType = dpsdk_media_type_e.DPSDK_CORE_MEDIATYPE_VIDEO;
                pRealStreamUrlInfo.nStreamType = dpsdk_stream_type_e.DPSDK_CORE_STREAMTYPE_MAIN;
                pRealStreamUrlInfo.nTransType = dpsdk_trans_type_e.DPSDK_CORE_TRANSTYPE_TCP;
                nRet = IDpsdkCore.DPSDK_GetRealStreamUrl(m_nDLLHandle, pRealStreamUrlInfo, 10000);
                if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS)
                {
                    String url = new String(pRealStreamUrlInfo.szUrl).trim();
                    //System.out.println(url);
                    pRealStreamUrls += url.split("\\|")[1] + "|";

                }else
                {
                    System.out.printf("获取URL失败，nRet = %d", nRet);
                    pRealStreamUrls += "获取URL失败，nRet = " + nRet;
                    break;
                }
                System.out.println();

            }

            System.out.println(pRealStreamUrls);
            System.out.println();


        }

        return pRealStreamUrls;
    }
}
