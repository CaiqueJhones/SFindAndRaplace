package cj.solstice.swing.sfind;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

/**
 * <p>Caixa de diálogo para pesquisar e substituir textos em componentes 
 * que extendem a classe {@link javax.swing.text.JTextComponent}. </p>
 * <strong>Exemplo de uso</strong></br>
 * <pre>
 * public class TestSfind extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					TestSfind frame = new TestSfind();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public TestSfind() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		final JTextArea textArea = new JTextArea("jhones\jhones\njhones\njhones");
		scrollPane.setViewportView(textArea);
		
		final SFind find = new SFind(textArea);
		
		JButton btnFind = new JButton("Find");
		btnFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				find.setVisible(true);
			}
		});
		contentPane.add(btnFind, BorderLayout.SOUTH);
	}

 }
 * </pre>
 * @author Caique Jhones
 * @version 1.0
 */
public class SFind extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JTextField textPesquisa;
	private JTextField textSubstituir;
	
	private JTextComponent textComponent;
	private JRadioButton radioButtonAll;
	private JRadioButton rdbtnFrente;
	private JRadioButton rdbtnAtras;
	private JLabel labelInfo;
	private JCheckBox caseSensitive;
	
	private int offset;
	private String text;
	private JButton btnSubstituirTudo;
	private JButton btnSubstituir;

	public SFind(JTextComponent textComponent) {
		
		this.textComponent = textComponent;
		
		setTitle("Pesquisar/Substituir");
		setResizable(false);
		setBounds(100, 100, 300, 350);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		
		offset = 0;
		
		createGui();
	}

	private void createGui() {
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		setContentPane(contentPanel);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		ActionEvents action = new ActionEvents();
		
		JPanel panelButton = new JPanel();
		contentPanel.add(panelButton, BorderLayout.SOUTH);
		panelButton.setLayout(new GridLayout(0, 2, 5, 2));
		
		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.setActionCommand("find");
		btnPesquisar.addActionListener(action);
		panelButton.add(btnPesquisar);
				
		btnSubstituir = new JButton("Substituir");
		btnSubstituir.setActionCommand("replace");
		btnSubstituir.addActionListener(action);
		btnSubstituir.setEnabled(false);
		panelButton.add(btnSubstituir);
		
		JButton btnPesquisarSubstituir = new JButton("Pesquisar/Substituir");
		btnPesquisarSubstituir.setActionCommand("find/replace");
		btnPesquisarSubstituir.addActionListener(action);
		panelButton.add(btnPesquisarSubstituir);
		
		btnSubstituirTudo = new JButton("Substituir Tudo");
		btnSubstituirTudo.setActionCommand("replaceAll");
		btnSubstituirTudo.addActionListener(action);
		btnSubstituirTudo.setEnabled(false);
		panelButton.add(btnSubstituirTudo);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panelButton.add(horizontalStrut);
		
		JButton btnOk = new JButton("Fechar");
		btnOk.setActionCommand("close");
		btnOk.addActionListener(action);
		panelButton.add(btnOk);
		getRootPane().setDefaultButton(btnOk);
		
		labelInfo = new JLabel("");
		panelButton.add(labelInfo);
		
		JPanel content = new JPanel();
		contentPanel.add(content, BorderLayout.CENTER);
		content.setLayout(new BorderLayout(0, 0));
		
		JPanel panelPesquisa = new JPanel();
		content.add(panelPesquisa, BorderLayout.NORTH);
		panelPesquisa.setLayout(new GridLayout(2, 0, -100, 5));
		
		JLabel labelPesquisa = new JLabel("Pesquisar:");
		panelPesquisa.add(labelPesquisa);
		
		textPesquisa = new JTextField();
		textPesquisa.setBorder(new SRoundedBorder());
		panelPesquisa.add(textPesquisa);
		textPesquisa.setColumns(10);
		
		JLabel lblSubstituir = new JLabel("Substituir com:");
		panelPesquisa.add(lblSubstituir);
		
		textSubstituir = new JTextField();
		textSubstituir.setBorder(new SRoundedBorder());
		panelPesquisa.add(textSubstituir);
		textSubstituir.setColumns(10);
		
		JPanel panel = new JPanel();
		content.add(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panelDirection = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panelDirection.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panelDirection.setBorder(new TitledBorder(null, "Dire\u00E7\u00E3o", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panelDirection);
		
		rdbtnFrente = new JRadioButton("Para frente");
		rdbtnFrente.setSelected(true);
		rdbtnFrente.addActionListener(action);
		panelDirection.add(rdbtnFrente);
		
		rdbtnAtras = new JRadioButton("Para trás");
		rdbtnAtras.addActionListener(action);
		panelDirection.add(rdbtnAtras);
		
		JPanel panelScope = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panelScope.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		panelScope.setBorder(new TitledBorder(null, "Scopo", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panelScope);
		
		radioButtonAll = new JRadioButton("Tudo");
		radioButtonAll.setSelected(true);
		radioButtonAll.addActionListener(action);
		panelScope.add(radioButtonAll);
			
		JPanel panelOptions = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panelOptions.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		panelOptions.setBorder(new TitledBorder(null, "Op\u00E7\u00F5es", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		content.add(panelOptions, BorderLayout.SOUTH);
		
		caseSensitive = new JCheckBox("Diferenciar maiúsculas e minúsculas");
		caseSensitive.setSelected(true);
		panelOptions.add(caseSensitive);
	}
	
	private void find() {
		iniText();
		
		String pesquisa = textPesquisa.getText();
		if(pesquisa.isEmpty())
			return;
		
		Pattern pattern = null;	
		boolean caseSensitive = this.caseSensitive.isSelected();
		if(caseSensitive) {
			pattern = Pattern.compile(pesquisa);
		}else {
			pattern = Pattern.compile(pesquisa, Pattern.CASE_INSENSITIVE);
		}
		
		Matcher matcher = pattern.matcher(text);
		
		if(!matcher.find()) {
			labelInfo.setText("Nenhum resultado!");
			return;
		}
		matcher = matcher.reset();
		
		offset = textComponent.getCaretPosition();
		
		if(rdbtnFrente.isSelected()) {
			while(matcher.find()) {
				int start = matcher.start();
				int end = matcher.end();
				if(end > offset) {
					fillTextComponent(start, end);
					break;
				}
			}
				
		}else {
			int end = 0;
			int start = 0;
			int pre = 0;
			int pro = 0;
			while (matcher.find()){
				pro = end;
				pre = start;
				start = matcher.start();
				end = matcher.end();
				if(end >= offset){
					if(pro != 0)
						fillTextComponent(pre, pro);
					return;
				}
			}
			if(end != 0)
				fillTextComponent(start, end);
		}
		
	}

	private void fillTextComponent(int start, int end) {
		textComponent.setSelectionStart(start);
		textComponent.setSelectionEnd(end);
		textComponent.requestFocus();
		labelInfo.setText("");
		btnSubstituir.setEnabled(true);
		btnSubstituirTudo.setEnabled(true);
	}
	
	private void replace() {
		int start = textComponent.getSelectionStart();
		int end = textComponent.getSelectionEnd();
		String news = textSubstituir.getText();			
		String text = textComponent.getText();
		String old = text.substring(start, end);
		if(old.isEmpty() || news.isEmpty())
			return;
		String antes = textComponent.getText().substring(0, start);
		String depois = textComponent.getText().substring(end);
		String r = old.replace(old, news);
		textComponent.setText(antes+r+depois);	
		textComponent.setCaretPosition(antes.length()+r.length());
		textComponent.requestFocus();
		btnSubstituir.setEnabled(false);
		btnSubstituirTudo.setEnabled(false);
	}
	
	private void replaceAll() {
		int start = textComponent.getSelectionStart();
		int end = textComponent.getSelectionEnd();
		String news = textSubstituir.getText();
		String old = textComponent.getText().substring(start, end);
		if(old.isEmpty() || news.isEmpty())
			return;
		String text = textComponent.getText().replace(old, news);
		textComponent.setText(text);
		textComponent.requestFocus();
		btnSubstituir.setEnabled(false);
		btnSubstituirTudo.setEnabled(false);
	}

	private void iniText() {
		text = "";
		if(radioButtonAll.isSelected()){
			text = textComponent.getText();
		}else {
			text = textComponent.getSelectedText();
		}
	}
	
	
	@Override
	public void setVisible(boolean b) {
		labelInfo.setText("");
		btnSubstituir.setEnabled(false);
		btnSubstituirTudo.setEnabled(false);
		super.setVisible(b);
	}


	private class ActionEvents implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();
			if(action.equals("close")) {
				dispose();
			}
			
			else if(action.equals("find")) {
				find();
			}
			
			else if(action.equals("replace")) {
				replace();
			}
			
			else if(action.equals("find/replace")) {
				find();
				replace();
			}
			
			else if(action.equals("replaceAll")) {
				replaceAll();
			}
			
			else if(e.getSource() == radioButtonAll) {
				radioButtonAll.setSelected(true);
			}
			
			else if(e.getSource() == rdbtnAtras) {
				rdbtnAtras.setSelected(true);
				rdbtnFrente.setSelected(false);
			}
						
			else if(e.getSource() == rdbtnFrente) {
				rdbtnAtras.setSelected(false);
				rdbtnFrente.setSelected(true);
			}
			
		}
		
	}

}
