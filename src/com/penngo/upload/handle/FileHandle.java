package com.penngo.upload.handle;

import org.json.simple.JSONObject;

/**
 * 返回文件名给客户端和文件上传完成需要执行的操作
 * 按日期返回文件名
 * 保留和原来一样的文件名
 * 
 */
public interface FileHandle {
	public String createFileName(JSONObject fileInfo);
	public void finishUpload();
}
