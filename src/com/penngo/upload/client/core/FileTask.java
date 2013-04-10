package com.penngo.upload.client.core;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * 文件上传任务
 *
 */
public class FileTask {
	private final static int MAX_SIZE = 1024;
	private ByteBuffer sendBuffer = ByteBuffer.allocate(MAX_SIZE);
	/* 接受数据缓冲区 */
	private ByteBuffer revBuffer = ByteBuffer.allocate(MAX_SIZE);
	
	public FileTask(File uploadFile){
		
	}
}
