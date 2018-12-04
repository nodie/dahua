package com.dh.DpsdkCore.javafile;

/** Json传输协议回调
@param szJson	Json字符串
*/
public interface fDPSDKGeneralJsonTransportCallback {
	void invoke(int nPDLLHandle, byte[] szJson);
}
