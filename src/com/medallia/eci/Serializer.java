package com.medallia.eci;

	import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.*;
import javax.xml.bind.DatatypeConverter;

	public class Serializer {    
	  public static void main(String[] args) throws Exception {
		  System.out.println("hello");
	    File file = new File("lib/en-token/token.model");
	    byte[] bytes = loadFile(file, new ByteArrayOutputStream()).toByteArray();
	    String encoded = DatatypeConverter.printBase64Binary(bytes);
	    System.out.println(encoded);
	    byte[] decoded = DatatypeConverter.parseBase64Binary(encoded);
	    // check
	    for (int i = 0; i < bytes.length; i++) {
	      assert bytes[i] == decoded[i];
	    }
	  }

	  private static <T extends OutputStream> T loadFile(File file, T out)
	                                                       throws IOException {
		  System.out.println("hello");

	    FileChannel in = new FileInputStream(file).getChannel();
	    try {
	      assert in.size() == in.transferTo(0, in.size(), Channels.newChannel(out));
	      return out;
	    } finally {
	      in.close();
	    }
	  }
	}


