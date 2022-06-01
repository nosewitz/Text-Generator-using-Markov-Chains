import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class MarkovGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea inputField;
	private JTextField kField;
	private JTextField tField;
	private JTextArea genField;
	private JTextField optionalField;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MarkovGUI frame = new MarkovGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MarkovGUI() {
		setTitle("Text Generator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 925, 635);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Label for input box
		JLabel textLabel = new JLabel("Input text:");
		textLabel.setBounds(10, 10, 160, 13);
		contentPane.add(textLabel);
		
		
		// Takes in input text
		inputField = new JTextArea();
		inputField.setWrapStyleWord(true);
		inputField.setLineWrap(true);
		inputField.setBounds(10, 33, 834, 128);
		contentPane.add(inputField);
		inputField.setColumns(10);
		

	    JScrollPane inpScroll = new JScrollPane ( inputField );
	    inpScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	    inpScroll.setBounds(10, 30, 834, 200);

	    //Add Textarea in to middle panel
	    contentPane.add ( inpScroll );

		
		
		// Button action
		JButton runMarkov = new JButton("Generate");
		runMarkov.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String str = inputField.getText();

				int k =  Integer.parseInt(kField.getText());
				int T = Integer.parseInt(tField.getText());
				
				
				MarkovModel  mk = new MarkovModel(str, k);
				
				String generated;
				if (optionalField.getText().equals("")) {
					generated = mk.gen(str.substring(0, k), T);
				}
				
				else {
					generated = mk.gen( optionalField.getText(), T);
				}
				
				
				
				genField.setText(generated);
				
			}
		});
	
		
		runMarkov.setBounds(655, 268, 160, 30);
		contentPane.add(runMarkov);
		// takes in K
		kField = new JTextField();
		kField.setBounds(10, 274, 96, 19);
		contentPane.add(kField);
		kField.setColumns(10);
		// takes in T
		tField = new JTextField();
		tField.setBounds(176, 274, 125, 19);
		contentPane.add(tField);
		tField.setColumns(10);
		
		// Simple descriptor labels
		JLabel kgramLabel = new JLabel("Size of k-grams:");
		kgramLabel.setToolTipText("Size on which to split your original text, the higher K is, the closer it is to the original.");
		kgramLabel.setBounds(10, 251, 96, 13);
		contentPane.add(kgramLabel);
		
		JLabel sizeLabel = new JLabel("Size of generated text:");
		sizeLabel.setToolTipText("How big should the generated text be, integer.");
		sizeLabel.setBounds(176, 251, 160, 13);
		contentPane.add(sizeLabel);
		
		
		// Label for genField
		JLabel genLabel = new JLabel("Generated text:");
		genLabel.setBounds(10, 303, 96, 13);
		contentPane.add(genLabel);
		
		// GEnerated text field
		genField = new JTextArea(16,48);
		genField.setWrapStyleWord(true);
		genField.setEditable(false);
		genField.setLineWrap(true);
		genField.setBounds(10, 254, 834, 151);
		contentPane.add(genField);
		genField.setColumns(10);
		

//	    JTextArea display = new JTextArea ( 16, 58 );
//	    display.setEditable ( false ); // set textArea non-editable
	    JScrollPane genScroll = new JScrollPane ( genField );
	    genScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	    genScroll.setBounds(10, 320, 834, 250);

	    //Add Textarea in to middle panel
	    contentPane.add ( genScroll );
	    
	    optionalField = new JTextField();
	    optionalField.setColumns(10);
	    optionalField.setBounds(366, 274, 125, 19);
	    contentPane.add(optionalField);
	    
	    JLabel lblStartingKgram = new JLabel("Starting k-gram:");
	    lblStartingKgram.setToolTipText("Type a substring of your text of length K");
	    lblStartingKgram.setBounds(366, 251, 160, 13);
	    contentPane.add(lblStartingKgram);

	    // My code
	    

	}
}
