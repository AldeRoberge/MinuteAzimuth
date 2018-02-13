import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DegreeMinuteSeconde {

	private JFrame frmMinuteAzimuth;
	private JTextArea inputTextArea;
	private JTextArea outputTextArea;
	private JPanel panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {


		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DegreeMinuteSeconde window = new DegreeMinuteSeconde();
					window.frmMinuteAzimuth.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DegreeMinuteSeconde() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMinuteAzimuth = new JFrame();
		frmMinuteAzimuth.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmMinuteAzimuth.setIconImage(Toolkit.getDefaultToolkit().getImage(
				DegreeMinuteSeconde.class.getResource("/com/sun/java/swing/plaf/windows/icons/Computer.gif")));
		frmMinuteAzimuth.setTitle("Minute, Azimuth!");
		frmMinuteAzimuth.setBounds(100, 100, 516, 300);
		frmMinuteAzimuth.getContentPane().setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(200);
		frmMinuteAzimuth.getContentPane().add(splitPane, BorderLayout.CENTER);

		JScrollPane inputScrollPane = new JScrollPane();
		splitPane.setLeftComponent(inputScrollPane);

		inputTextArea = new JTextArea();

		inputTextArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateOutput();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateOutput();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateOutput();
			}
		});

		inputScrollPane.setViewportView(inputTextArea);

		JScrollPane outputScrollPane = new JScrollPane();
		splitPane.setRightComponent(outputScrollPane);

		outputTextArea = new JTextArea();
		outputScrollPane.setViewportView(outputTextArea);

		panel = new JPanel();
		frmMinuteAzimuth.getContentPane().add(panel, BorderLayout.SOUTH);
	}

	private void updateOutput() {

		outputTextArea.setText("");

		List<String> arrList = JTextAreaToArray.getTextAreaAsArrayList(inputTextArea);

		String error = "";

		for (String s : arrList) {

			int degree = 0, minutes = 0, seconds = 0;

			String output = "";

			if (s.contains(",")) {
				String[] lineSplit = s.split(",");

				if (lineSplit.length == 1) {
					degree = parse(lineSplit[0]);
					minutes = 0;
					seconds = 0;
				} else if (lineSplit.length == 2) {
					degree = parse(lineSplit[0]);
					minutes = parse(lineSplit[1]);
					seconds = 0;
				} else if (lineSplit.length == 3) {
					degree = parse(lineSplit[0]);
					minutes = parse(lineSplit[1]);
					seconds = parse(lineSplit[2]);
				} else {
					error += "Error : Line " + arrList.indexOf(s) + " '" + s + "' has an invalid number of arguments : "
							+ lineSplit.length;
					System.err.println(error);
				}

				if (degree == -1 || minutes == -1 || seconds == -1) {
					error += "Invalid";
					degree = 0;
				}

			} else if (s.contains("°")) {
				// -40°30'

				try {

					if (s.contains("°")) {
						degree = parse(s.substring(0, s.indexOf("°")));

						if (s.contains("'")) {
							minutes = parse(s.substring(s.indexOf("°") + 1, s.indexOf("'")));

							if (s.contains("\"")) {
								seconds = parse(s.substring(s.indexOf("'") + 1, s.indexOf("\"")));
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					error += "Error! Could not parse this string.";
				}

			} else {
				error += "Invalid string format. Valid formats are '48,18,34' and '—48°18'34\"";
			}

			if (!error.equals("")) {
				output = "*" + error + "*";
			} else {
				output = convertToRealAzimuth(degree, minutes, seconds);
			}

			outputTextArea.append(output + "\n");

		}

	}

	private int parse(String string) {

		try {
			if (string.equals("")) {
				return 0;
			} else {
				string = string.replaceAll(" ", "");
				string = string.replaceAll("[^\\d.]", "");
				return Integer.parseInt(string);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	private String convertToRealAzimuth(int degree, int minutes, int seconds) {

		System.out.println("Input : degree " + degree + " minutes : " + minutes + " seconds : " + seconds);

		DecimalFormat df = new DecimalFormat(".####");

		String f = df.format(((float) minutes / 60 + seconds / 3600));

		return degree + f;

	}

}
