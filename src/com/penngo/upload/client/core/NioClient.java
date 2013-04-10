package com.penngo.upload.client.core;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.penngo.upload.util.Adler32Util;

public class NioClient {
	private final static int MAX_SIZE = 1024;
	private ByteBuffer sendBuffer = ByteBuffer.allocate(MAX_SIZE);
	/* 接受数据缓冲区 */
	private ByteBuffer revBuffer = ByteBuffer.allocate(MAX_SIZE);
	
	private InetSocketAddress SERVER;
	private Selector selector;
	private SocketChannel client;
	public NioClient(String ip, int port){
		try{
			SERVER = new InetSocketAddress(ip, port);
//			init();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void connect(){
		try {
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			selector = Selector.open();
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
			socketChannel.connect(SERVER);
			while (true) {
				selector.select();
				Set<SelectionKey> keySet = selector.selectedKeys();
				for (final SelectionKey key : keySet) {
					if(key.isConnectable()){
						client = (SocketChannel)key.channel();
						client.finishConnect();
						client.register(selector, SelectionKey.OP_WRITE);

					}
					else if(key.isWritable()){
						//sendFile(client);
					}
					else if(key.isReadable()){
						client = (SocketChannel) key.channel();
						revBuffer.clear();
						int count = client.read(revBuffer);
						StringBuffer sff = new StringBuffer();
						while(count > 0){
							sff.append(String.valueOf(revBuffer.array()));
							if(revBuffer.hasRemaining() == false){
								revBuffer.flip();
								revBuffer.clear();
							}
							count = client.read(revBuffer);
						}
						if(sff.length() > 0){
							handleMsg(sff.toString());
						}
					}
				}
				keySet.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void handleMsg(String msg){
		System.out.println("handleMsg===============" + msg);
		JSONObject jsonobj = (JSONObject)JSONValue.parse(msg);
		
	}
	
	public void sendFileInfo(File file) throws IOException{
		String fileName = file.getName();
		long fileLength = file.length();
		long adler32 = Adler32Util.doChecksum(file);
		JSONObject json = new JSONObject();
		json.put("fileName", fileName);
		json.put("fileLength", fileLength);
		json.put("adler32", adler32);
		this.sendMsg(json.toJSONString());
	}
	
	public void sendMsg(String msg) throws IOException{
		sendBuffer.clear();
		sendBuffer.put(msg.getBytes());
		client.write(sendBuffer);
	}
}
