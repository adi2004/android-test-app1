package flashcards;

import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.print.attribute.standard.Media;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class UIFlashCards {

	public static void main(String... args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new UIFlashCards().createUI();
			}
		});
	}

	protected void createUI() {
		JFrame frame = new JFrame();
		JTextArea textArea = new JTextArea();
		textArea.setText(readFromFile());
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setSize(new Dimension(800, 600));
		textArea.setFont(new Font("Serif", Font.PLAIN, 20));
		textArea.setEditable(false);
		textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//		textArea.setF

		frame.add(textArea);

		frame.setTitle("Text");
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private String readFromFile() {
		StringBuffer string = new StringBuffer();
		try {
			File f = new File("text\\kaffee.txt");
			BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));
			String line;
			while ((line = fin.readLine()) != null) {
				string.append(line + "\n");
			}
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return string.toString();
	}

}
