package com.penngo.upload.util;

import java.util.zip.CheckedInputStream;
import java.util.zip.Adler32;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Adler32Util {

	public static long doChecksum(File file) {
		try {
			CheckedInputStream cis = null;
			try {
				cis = new CheckedInputStream(new FileInputStream(file),
						new Adler32());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			byte[] buf = new byte[128];
			while (cis.read(buf) >= 0) {
			}
			long checksum = cis.getChecksum().getValue();
			return checksum;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}