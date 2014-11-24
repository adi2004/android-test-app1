package sample;

import javax.swing.JOptionPane;

public class ExceptionSnippets extends RuntimeException {
	private static final long serialVersionUID = -6595355192775667394L;

	public ExceptionSnippets(String string) {
		super(string);
	}

	public ExceptionSnippets() {
	}

	public ExceptionSnippets(String string, Throwable cause) {
		super(string, cause);
	}

	public static void main(String... strings) throws InterruptedException {
		int goodExecutions = 0;
		int exceptions = 0;
		for (;;) {
			try {
				(new ExceptionSnippets()).start();
				goodExecutions++;
			} catch (Throwable e) {
				exceptions++;
				System.out.println("I got it!<this is before>");
				e.printStackTrace(System.out);
				System.out.println("I got it!<this is after>");
			}
			System.out.println("Program now ended with (goodExecutions, exceptions) = (" + goodExecutions + "; "
					+ exceptions + ");");
		}
	}

	private void start() throws ExceptionSnippets {
		String input = JOptionPane.showInputDialog("Don't type 'a'!");
		if (input.equals("a")) {
			throw new ExceptionSnippets("Oh, no, you typed 'a'", new ExceptionSnippets("No cause baby"));
		}
	}
}
