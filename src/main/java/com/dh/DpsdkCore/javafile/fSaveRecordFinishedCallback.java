package com.dh.DpsdkCore.javafile;

/** 录像转码完成回调.
@param   IN									nPDLLHandle				SDK句柄
@param   IN									nPlaybackSeq            录像转码的序列号
@remark									
*/
public interface fSaveRecordFinishedCallback{
	void invoke(int nPDLLHandle, int nPlaybackSeq);
}