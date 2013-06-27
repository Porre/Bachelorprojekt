import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;


public class FactorGUI {

	private JFrame frmFaktoriseringV;
	private JTextField txtFactor;
	private Worker worker;
	private String method;
	private BigInteger trialIterations;
	private int pollardsMax;
	private int lenstrasMax;
	private int curves;
	private BigInteger n;
	private long start, end;
	
	private JTextArea txtOutput;
	private JButton btnFactor;
	private JTextField txtTrialIterations;
	private JTextField txtPollardsMax;
	private JTextField txtLenstraMax;
	private JTextField txtCurves;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		 try {
	            // Set System L&F
	        UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	    	// do nothing
	    }
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FactorGUI window = new FactorGUI();
					window.frmFaktoriseringV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FactorGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFaktoriseringV = new JFrame();
		frmFaktoriseringV.setResizable(false);
		frmFaktoriseringV.setTitle("Prime Factorization by Jakob Ørhøj - orhoj.com");
		frmFaktoriseringV.setBounds(100, 100, 600, 380);
		frmFaktoriseringV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFaktoriseringV.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmFaktoriseringV.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Factorization", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblIndtastHeltalDer = new JLabel("Input integer to be factored");
		lblIndtastHeltalDer.setBounds(10, 11, 399, 14);
		panel.add(lblIndtastHeltalDer);
		
		txtFactor = new JTextField();
		txtFactor.setBounds(10, 28, 569, 20);
		panel.add(txtFactor);
		txtFactor.setColumns(10);
		
		txtOutput = new JTextArea();
		txtOutput.setEditable(false);
		txtOutput.setFont(new Font("Monospaced", Font.PLAIN, 11));
		txtOutput.setBounds(10, 93, 569, 220);
		panel.add(txtOutput);
		txtOutput.setLineWrap(true);
		
		btnFactor = new JButton("Find factors");
		btnFactor.setBounds(10, 59, 154, 23);
		panel.add(btnFactor);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				worker.cancel(true);
				btnFactor.setEnabled(true);
			}
		});
		btnCancel.setBounds(174, 59, 89, 23);
		panel.add(btnCancel);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Options", null, panel_1, null);
			panel_1.setLayout(null);
		
			
			JLabel lblNewLabel = new JLabel("Method of factorization:");
			lblNewLabel.setBounds(10, 11, 235, 14);
			panel_1.add(lblNewLabel);
			
			final JComboBox comboMethod = new JComboBox();
			comboMethod.setBounds(10, 36, 235, 20);
			panel_1.add(comboMethod);
			comboMethod.setModel(new DefaultComboBoxModel(new String[] {"Trial division", "Pollards p-1", "Lenstras ECM", "Combined method"}));
			comboMethod.setSelectedIndex(3);
			
			JLabel lblTrialDivisionParameters = new JLabel("Trial division: max iterations");
			lblTrialDivisionParameters.setBounds(10, 67, 235, 14);
			panel_1.add(lblTrialDivisionParameters);
			
			txtTrialIterations = new JTextField();
			txtTrialIterations.setText("100000");
			txtTrialIterations.setBounds(10, 84, 235, 20);
			panel_1.add(txtTrialIterations);
			txtTrialIterations.setColumns(10);
			
			JLabel lblPollardsPK = new JLabel("Pollards p-1: K parameter");
			lblPollardsPK.setBounds(10, 115, 235, 14);
			panel_1.add(lblPollardsPK);
			
			txtPollardsMax = new JTextField();
			txtPollardsMax.setText("100");
			txtPollardsMax.setBounds(10, 132, 235, 20);
			panel_1.add(txtPollardsMax);
			txtPollardsMax.setColumns(10);
			
			JLabel lblLenstrasEcmK = new JLabel("Lenstras ECM: K parameter");
			lblLenstrasEcmK.setBounds(10, 163, 235, 14);
			panel_1.add(lblLenstrasEcmK);
			
			txtLenstraMax = new JTextField();
			txtLenstraMax.setText("100");
			txtLenstraMax.setBounds(10, 180, 235, 20);
			panel_1.add(txtLenstraMax);
			txtLenstraMax.setColumns(10);
			
			JLabel lblLenstrasEcmNumber = new JLabel("Lenstras ECM: number of curves to test");
			lblLenstrasEcmNumber.setBounds(10, 211, 235, 14);
			panel_1.add(lblLenstrasEcmNumber);
			
			txtCurves = new JTextField();
			txtCurves.setText("20");
			txtCurves.setBounds(10, 228, 235, 20);
			panel_1.add(txtCurves);
			txtCurves.setColumns(10);
			
			JLabel lblAbout = new JLabel("<html><b>About</b><br>\r\nThis program was created as a supplement to my bachelor's thesis: \"Elliptic Curves and Prime Factorization\". It demonstrates the algorithms presented in the thesis with very direct implementations.\r\n<br>\r\n<br>\r\nCombined method: combines trial division, Pollards p-1 algorithm and Lenstras ECM to factor an integer. The options on the left also affects the values used in this algorithm.\r\n<br>\r\n<br>\r\nmax iterations: we test factors one by one until this value is reached.\r\n<br><br>\r\nK parameter: in both Pollard's p-1 algorithm and Lenstra's ECM we use k=lcm[1,2,...,K] and this can be adjusted here.\r\n<br><br>\r\nnumber of curves: for every value of K (and thereby k) we test a set amount of curves to find a factor, this value can be adjusted here.\r\n</html>");
			lblAbout.setVerticalAlignment(SwingConstants.TOP);
			lblAbout.setBounds(255, 11, 324, 302);
			panel_1.add(lblAbout);
			
		btnFactor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				txtOutput.setText("");
				
				try {
					n = new BigInteger(txtFactor.getText());
					if (n.compareTo(BigInteger.valueOf(2)) < 0) {
						throw new Exception();
					}
				} catch (Exception e) {
					txtOutput.setText("Input should be a positive integer greater than or equal to 2");
					return;
				}
				
				method = (String) comboMethod.getSelectedItem();
				trialIterations = new BigInteger(txtTrialIterations.getText());
				pollardsMax = Integer.parseInt(txtPollardsMax.getText());
				lenstrasMax = Integer.parseInt(txtLenstraMax.getText());
				curves = Integer.parseInt(txtCurves.getText());
				
				if (method.equals(null)) {
					txtOutput.setText("Please select a factoring method");
					return;
				}
				
				worker = new Worker();
			    worker.execute();
			    
			    btnFactor.setEnabled(false);
			}
		});
	}
	
	class Worker extends SwingWorker<ArrayList<BigInteger>, Integer> {
	 
	    @Override
	    protected ArrayList<BigInteger> doInBackground() throws Exception {
			start = System.currentTimeMillis();
	    	Factor f = new Factor(trialIterations, pollardsMax, lenstrasMax, curves);
	    	
	    	if (method.equals("Trial division")) {
	    		f.trialFactor(n);
	    	} else if (method.equals("Pollards p-1")) {
	    		f.pollardFactor(n);
	    	} else if (method.equals("Lenstras ECM")) {
	    		f.lenstraFactor(n);
	    	} else {
	    		f.findFactor(n);
	    	}
	    
	        end = System.currentTimeMillis();
	        return f.getFactors();
	    }

	    @Override
	    protected void done() {
	    	ArrayList<BigInteger> result;
	    	BigInteger product = BigInteger.ONE;
	        try {
	          result = worker.get();
	          if (!result.equals(null)) {
	        	  for (int i = 0; i < result.size(); i++) {
	        		  product = product.multiply(result.get(i));
	        		  
	        		  if (i == result.size() - 1) {
	        			  txtOutput.append(result.get(i) + "");
	        		  } else {
	        			  txtOutput.append(result.get(i) + " x ");
	        		  }
	        	  }
	        	  
	        	  if (n.equals(product)) {
	        		  txtOutput.append("\nFactors found in: " + (end - start) / 1000 + " seconds, using " + method + " to factor.");
	        	  } else {
	        		  txtOutput.append("\nMaybe found some factors in: " + (end - start) / 1000 + " seconds.");
	        		  txtOutput.append("\nTry a greater value of K to find more factors.");
	        	  }
	          } else {
	        	  txtOutput.append("No factors found. Try again.");
	          }
	        } catch (InterruptedException | ExecutionException ex) {
	        	// Maybe tell people something went wrong.
	        } catch (Exception e) {
	        	// It's okay...
	        }
	        btnFactor.setEnabled(true);
	   }
	}
}