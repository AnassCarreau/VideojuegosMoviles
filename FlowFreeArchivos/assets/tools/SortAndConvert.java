import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;

// Takes Localizable.strings from OneSkyApp and does two useful things:
//   1. It converts them from UTF-16 to UTF-8
//   2. It sorts them alphabetically according to the English source string 
//
// The result is much easier to diff when checking into git, and easier to diff against
// the output of the genstrings command as new strings are added to the source code

public class SortAndConvert {
    // use a regular expression to extract english from "english" = "localized"
    private static String extractEnglishString(String str) {
        Pattern p = Pattern.compile("\\\"(.+)\\\".+=");
        Matcher m = p.matcher(str);
        if(m.find()) {
            return m.group(1);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
    
    public static void main(String[] args) throws IOException {
        if(args.length < 2) {
            System.out.println("usage:\n  SortAndConvert <inputFilename> <outputFilename>");
            return;
        }
        String inputFilename = args[0];
        String outputFilename = args[1];
        String outputFormat = "UTF-8";

        if(args.length == 3) {
            String arg3 = args[2].toLowerCase();
            if(arg3.equals("utf-16") || arg3.equals("utf16")) {
                outputFormat = "UTF-16";
            }
        }
        
        InputStream fileInputStream = new FileInputStream(inputFilename);
        BufferedReader reader = new BufferedReader(new UnicodeReader(fileInputStream, "UTF-8"));
        Map<String, String> stringTable = new HashMap<String, String>();
        List<String> keys = new ArrayList<String>();
        StringBuffer nextString = new StringBuffer();
        nextString.append("\n");
    
        try {
            while (true) {
                String line = reader.readLine();
                if(line == null) {
                    break;
                }
                nextString.append(line+"\n");
                if(line.startsWith("\"") && line.indexOf("=") > 0) {
                    // found: "english string" = "localized string"
                    // use this as the key for sorting
                    String key = extractEnglishString(line);
                    keys.add(key);
                    stringTable.put(key, nextString.toString());
                    nextString.setLength(0); // clear the buffer
                }
            }
        } finally {
            reader.close();
            fileInputStream.close();
        }
        
        if(keys.size() == 0) {
        	System.out.println("ERROR: no string pairs found!");
        	return;
        }
        
        System.out.print("writing "+keys.size()+" entries to "+outputFilename+"... ");
        Writer out = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream(outputFilename), outputFormat));
        boolean first = true;
        try {
            // we have to sort the list twice because case insensitive compare 
            // does not prefer 'a' before 'A', they are equal 
            Collections.sort(keys, Collections.reverseOrder());
            Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
            for(String item : keys) {
                String itemValue = stringTable.get(item);
                if(first) {
                    // first item no line feed at the top of the file
                    while(itemValue.startsWith("\n")) {
                      itemValue = itemValue.substring(1, itemValue.length());
                    }
                    first = false;
                }
                out.write(itemValue);
            }
            out.write("\n");
        } finally {
            out.close();
        }
        System.out.println("done");
    }
}


// Helper class for detecting Unicode file encoding
//
class UnicodeReader extends Reader {

	private static final int BOM_SIZE = 4;

	private final InputStreamReader reader;

	public UnicodeReader(InputStream in, String defaultEncoding) throws IOException {
		byte bom[] = new byte[BOM_SIZE];
		String encoding;
		int unread;

		PushbackInputStream pushbackStream = new PushbackInputStream(in, BOM_SIZE);
		int n = pushbackStream.read(bom, 0, bom.length);

		if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF)) {
			encoding = "UTF-8";
			unread = n - 3;
		} else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF)) {
			encoding = "UTF-16BE";
			unread = n - 2;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)) {
			encoding = "UTF-16LE";
			unread = n - 2;
		} else if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00) && (bom[2] == (byte) 0xFE) && (bom[3] == (byte) 0xFF)) {
			encoding = "UTF-32BE";
			unread = n - 4;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE) && (bom[2] == (byte) 0x00) && (bom[3] == (byte) 0x00)) {
			encoding = "UTF-32LE";
			unread = n - 4;
		} else {
			encoding = defaultEncoding;
			unread = n;
		}

		if (unread > 0) {
			pushbackStream.unread(bom, (n - unread), unread);
		} else if (unread < -1) {
			pushbackStream.unread(bom, 0, 0);
		}

		if (encoding == null) {
			reader = new InputStreamReader(pushbackStream);
		} else {
			reader = new InputStreamReader(pushbackStream, encoding);
		}
	}

	public String getEncoding() {
		return reader.getEncoding();
	}

	public int read(char[] cbuf, int off, int len) throws IOException {
		return reader.read(cbuf, off, len);
	}

	public void close() throws IOException {
		reader.close();
	}
}
