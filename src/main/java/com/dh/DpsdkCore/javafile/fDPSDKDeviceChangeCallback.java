package com.dh.DpsdkCore.javafile;

/** DPSDK 设备修改回调.
@param   IN									nPDLLHandle				SDK句柄
@param   IN									nChangeType				参考dpsdk_change_type_e
@param   IN									szDeviceId              设备Id
@param   IN									szDepCode               组织Code
@param   IN									szNewOrgCode            新组织Code   
@remark									
*/
public interface fDPSDKDeviceChangeCallback {
	void invoke(int nPDLLHandle, int nChangeType, byte[] szDeviceId, byte[] szDepCode, byte[] szNewOrgCode);
}