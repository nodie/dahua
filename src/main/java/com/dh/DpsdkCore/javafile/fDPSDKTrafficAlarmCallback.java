package com.dh.DpsdkCore.javafile;

/** 车辆违章上报回调函数定义
    @param IN                                    nPDLLHandle              SDK句柄
    @param IN                                    pUserParam               用户指针参数,由对应的DPSDK_SetxxxxCallBack传入
	@remark	参数定义请参考Traffic_Alarm_Info_t
*/
public interface fDPSDKTrafficAlarmCallback{
	void invoke(int nPDLLHandle, Traffic_Alarm_Info_t trafficAlarmInfo);
}