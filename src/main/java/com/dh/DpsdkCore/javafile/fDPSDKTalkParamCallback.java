package com.dh.DpsdkCore.javafile;

/** 语音对讲信息报回调函数定义
@param	  IN	                                nPDLLHandle		    SDK句柄
@param	  IN									nTalkType			对讲类型 	dpsdk_talk_type_e
@param	  IN	                                nAudioType			音频类型 	dpsdk_audio_type_e
@param	  IN	                                nAudioBit			位数		dpsdk_talk_bits_e
@param	  IN	                                nSampleRate			采样率	Talk_Sample_Rate_e
@param	  IN	                                nTransMode			传输类型	dpsdk_trans_type_e
*/

public interface fDPSDKTalkParamCallback 
{
	void invoke(int nPDLLHandle,
                int nTalkType,
                int nAudioType,
                int nAudioBit,
                int nSampleRate,
                int nTransMode
    );
}
