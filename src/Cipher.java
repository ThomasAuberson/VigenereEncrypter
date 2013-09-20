import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Cipher {

	// FIELDS
	private int[] key; // Key is stored as an array of alphabetic translations 
		// e.g letter d (index 3 in alphabet) represents a translation of +3 to a character's alphabetic index
		// to encrypt it.

	public void setKey(String key) { // Recieve key as a string and convert it to array of translations
		key.toLowerCase();
		char[] temp = key.toCharArray();
		this.key = new int[key.length()];
		for (int i = 0; i < temp.length; i++) {
			this.key[i] = temp[i] - 'a';
		}
	}

	public void encrypt(BufferedReader scan, BufferedWriter prin) throws IOException {
		// Encrypt file via Vigenere Cipher and key. Performed by translating each letter up by index of key character
		// e.g key letter is c ( index = 2). Letter j would be translated up by 2 to l
		if (key == null)
			return;

		char[] buffer; // Character buffer. Each line of text will be stored in here while it is encrypted
		String s;
		int keyIndex = 0;
		while ((s = scan.readLine()) != null) {
			buffer = s.toCharArray(); // Read line from input file into buffer
			for (int i = 0; i < buffer.length; i++) {
				char c = buffer[i]; // Read character from buffer to encrypt it
				if (buffer[i] >= 'A' && buffer[i] <= 'Z') { // If character is a capitalized letter translate it appropriately
					c = (char) (buffer[i] + key[keyIndex]);
					if (c > 'Z') { // If character is translated out of upper case letter range cycle it back to start of alphabet
						c = (char) (buffer[i] + key[keyIndex] - 26);
					}
					keyIndex++; // Increment index of key -- > This is polyalphabetic nature of Vigenere Cipher at work
				} else if (buffer[i] >= 'a' && buffer[i] <= 'z') { // If character is a lower case letter translate it appropriately
					c = (char) (buffer[i] + key[keyIndex]);
					if (c > 'z') { // If character is translated out of lower case letter range cycle it back to start of alphabet
						c = (char) (buffer[i] + key[keyIndex] - 26);
					}
					keyIndex++; // Increment index of key -- > This is polyalphabetic nature of Vigenere Cipher at work
				}
				
				// If character is not a letter ignore it and just put it straight back into buffer
				buffer[i] = c;

				if (keyIndex >= key.length) { // Key repeats over and over until encryption complete
					keyIndex = 0;
				}
			}
			prin.write(buffer); // Write newly encrypted contents of buffer to output file
			prin.newLine();
		}
	}

	public void decrypt(BufferedReader scan, BufferedWriter prin) throws IOException { 
		// Decryption is identical but opposite to encryption. Letters are translated down by key instead of up
		// e.g key letter is c ( index = 2). Letter l would be translated down by 2 to j
		if (key == null)
			return;

		char[] buffer;
		String s;
		int keyIndex = 0;
		while ((s = scan.readLine()) != null) {
			buffer = s.toCharArray();
			for (int i = 0; i < buffer.length; i++) {

				char c = buffer[i];
				if (buffer[i] >= 'A' && buffer[i] <= 'Z') {
					c = (char) (buffer[i] - key[keyIndex]);
					if (c < 'A') {
						c = (char) (buffer[i] - key[keyIndex] + 26);
					}
					keyIndex++;
				} else if (buffer[i] >= 'a' && buffer[i] <= 'z') {
					c = (char) (buffer[i] - key[keyIndex]);
					if (c < 'a') {
						c = (char) (buffer[i] - key[keyIndex] + 26);
					}
					keyIndex++;
				}

				buffer[i] = c;
				
				if (keyIndex >= key.length) {
					keyIndex = 0;
				}
			}
			prin.write(buffer);
			prin.newLine();
		}
	}

}
