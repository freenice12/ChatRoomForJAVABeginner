package chatRoom.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ChatRoomFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	protected ClientChat clientChat;
	private String[] clientArray;
	protected JTextArea showChatTextArea;
	private JTextArea showClientIdTextArea;
	protected JTextField messageField;
	protected DefaultListModel<String> clientIdListModel;
	protected JList<String> clientIdList;

	public ChatRoomFrame(ClientChat clientChat, String[] clientArr) {
		super("Chat Room");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new ChatWindowListener());
		this.clientChat = clientChat;
		this.clientArray = clientArr;
		buildUp();
	}

	public class ChatWindowListener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
		}

		@Override
		public void windowClosing(WindowEvent e) {
			quitProcess();
		}

		@Override
		public void windowClosed(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}
	}

	public class QuitActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			quitProcess();
		}

	}

	public void quitProcess() {
		clientChat.quitRoom(showChatTextArea.getText());
		System.exit(0);
	}

	public void buildUp() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		JLabel label = new JLabel("Title : "+clientChat.getChatRoomTitle()+" ("+clientChat.getChatRoomPassword()+")",
				SwingConstants.CENTER);

		JLabel contentsLabel = new JLabel("");
		JLabel clientListLabel = new JLabel("");
		
		
		showChatTextArea = new JTextArea(25, 50);
		showChatTextArea.setEditable(false);
		showChatTextArea.setLineWrap(true);

		showClientIdTextArea = new JTextArea(25, 15);
		showClientIdTextArea.setEditable(false);

		JScrollPane chatAreaScrollPane = new JScrollPane();
		chatAreaScrollPane.setViewportView(showChatTextArea);

		clientIdListModel = new DefaultListModel<>();
		clientIdList = new JList<>();
		clientIdList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		clientIdList.addMouseListener(new ClientIdListListener());
		// clientIdList.addListSelectionListener(new ClientIdListListener());
		updateClientList(clientArray);

		JScrollPane clientIdAreaScrollPane = new JScrollPane();
		clientIdAreaScrollPane.setPreferredSize(new Dimension(150, 453));
		clientIdAreaScrollPane.setViewportView(clientIdList);

		messageField = new JTextField(30);
		messageField.requestFocusInWindow();
		messageField.addKeyListener(new messageFieldKeyListener());

		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendActionListener());

		JButton quitButton = new JButton("Quit");
//		quitButton.setPreferredSize(new Dimension(100, 15));
		quitButton.addActionListener(new QuitActionListener());

		setShortCuts();
		mainPanel.add(label, new GridBagConstraints(1, 0, 3, 2, 0.0, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(quitButton, new GridBagConstraints(5, 0, 1, 2, 0.0, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5), 0, 0));
		
		mainPanel.add(contentsLabel, new GridBagConstraints(1, 0, 1, 1, 0.6, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(clientListLabel, new GridBagConstraints(1, 3, 1, 1, 0.4, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		mainPanel.add(chatAreaScrollPane, new GridBagConstraints(0, 3, 4, 8, 0.0, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(clientIdAreaScrollPane, new GridBagConstraints(4, 3, 2, 8, 0.0, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		mainPanel.add(messageField, new GridBagConstraints(0, 11, 4, 2, 0.0, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(sendButton, new GridBagConstraints(5, 11, 1, 2, 0.0, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
//		GridBagLayout gbl = new GridBagLayout();
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.CENTER;
//        setLayout(gbl);
//        addGrid(gbl, gbc, label, 1, 0, 6, 2, 1, 0);
//        addGrid(gbl, gbc, contentsLabel, 0, 2, 4, 1, 0, 0);
//        addGrid(gbl, gbc, chatAreaScrollPane, 0, 3, 4, 8, 0, 0);
//        addGrid(gbl, gbc, quitButton, 7, 0, 2, 2, 0, 0);
//        addGrid(gbl, gbc, clientListLabel, 6, 2, 2, 1, 0, 0);
//        addGrid(gbl, gbc, clientIdAreaScrollPane, 6, 3, 2, 8, 0, 0);
//        addGrid(gbl, gbc, messageField, 0, 10, 4, 2, 0, 0);
//        addGrid(gbl, gbc, sendButton, 5, 10, 2, 2, 0, 0);
        
//        addGrid(gbl, gbc, label, 1, 0, 3, 2, 0, 0);
//        addGrid(gbl, gbc, quitButton, 5, 0, 1, 2, 0, 0);
//        
//        addGrid(gbl, gbc, contentsLabel, 0, 2, 4, 1, 0, 0);
//        addGrid(gbl, gbc, clientListLabel, 4, 2, 2, 1, 0, 0);
//        
//        addGrid(gbl, gbc, chatAreaScrollPane, 0, 3, 4, 8, 0, 0);
//        addGrid(gbl, gbc, clientIdAreaScrollPane, 4, 3, 2, 8, 0, 0);
//        
//        addGrid(gbl, gbc, messageField, 0, 11, 4, 2, 0, 0);
//        addGrid(gbl, gbc, sendButton, 5, 11, 1, 2, 0, 0);
         
//		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
//		mainPanel.add(chatAreaScrollPane);
//		mainPanel.add(clientIdAreaScrollPane);
//		mainPanel.add(messageField);
//		mainPanel.add(sendButton);
//		mainPanel.add(quitButton);
//		add(BorderLayout.NORTH, label);
//		add(BorderLayout.CENTER, mainPanel);
		add(mainPanel);
        pack();
		setResizable(false);
		setBounds(100, 100, 800, 570);
		setVisible(true);

	}
	
//	private void addGrid(GridBagLayout gbl, GridBagConstraints gbc, Component c,  
//            int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty) {
//      gbc.gridx = gridx;
//      gbc.gridy = gridy;
//      gbc.gridwidth = gridwidth;
//      gbc.gridheight = gridheight;
//      gbc.weightx = weightx;
//      gbc.weighty = weighty;
//      gbl.setConstraints(c, gbc);
//      add(c);
//	}

	private void setShortCuts() {
		KeyStroke exitKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_X,
				InputEvent.ALT_MASK, false);
		ActionListener exitKeyActionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				quitProcess();
			}
		};

		getRootPane().registerKeyboardAction(exitKeyActionListener,
				exitKeyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	public void updateClientList(String[] clientIdArray) {
		boolean print = true;
		if(clientIdListModel.getSize() > clientIdArray.length){
			for(int i=0; clientIdListModel.getSize()>i; i++){
				System.out.println("out : "+clientIdListModel.get(i));
				for(String newId : clientIdArray){
					if(clientIdListModel.get(i).equals(newId)){
						print = false;
						break;
					}
				}
				if(print){
					showChatTextArea.append("\t\t["+clientIdListModel.get(i)+"]퇴장\n");
				}
				print = true;
			}
		}
		clientIdListModel = new DefaultListModel<String>();
		for (String clientId : clientIdArray) {
			clientIdListModel.addElement(clientId);
		}
		clientIdList.setModel(clientIdListModel);
		
		if(clientIdList.getSize().height < clientIdArray.length){
			showChatTextArea.append("\t\t["+clientIdArray[clientIdArray.length-1]+"]입장\n");
		}
	}

	public class SendActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			getMessageToSend();
		}
	}

	protected void getMessageToSend() {
		String message = messageField.getText();
		if (!message.equals("")) {
			messageField.setText("");
			messageField.requestFocusInWindow();
			clientChat.sendMessage(message);
		}
	}

	public class messageFieldKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 10) {
				getMessageToSend();
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

	}

	// public class ClientIdListListener implements ListSelectionListener{
	//
	// @Override
	// public void valueChanged(ListSelectionEvent e) {
	// if(e.getValueIsAdjusting()){
	// messageField.setText(messageField.getText()+clientIdList.getSelectedValue());
	// }
	// }
	//
	// }

	public class ClientIdListListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			String id = clientIdList.getSelectedValue();
			String message = messageField.getText();
			if (e.getClickCount() == 2) {
				messageField.setText(message + id);
				messageField.requestFocusInWindow();
			}
		}
	}

	public void updateChatTextArea(String log) {
		showChatTextArea.append(log.substring(0, log.length())
				+ "\n ------------------------이전 대화 내용 \n");
	}

	public void updateChatTextArea(String transmitterId, String message) {
		showChatTextArea.append(transmitterId + " : " + message + "\n");
	}

	public void updateChatTextAreaToShowWhisper(String transmitterId,
			String whisperMessage) {
		showChatTextArea.append("* From : " + transmitterId + " : "
				+ whisperMessage + "*\n");
	}

	public void updateChatTextAreaToReflect(String transmitterId,
			String whisperMessage) {
		showChatTextArea.append("* To : " + transmitterId + " : "
				+ whisperMessage + "*\n");

	}
}
