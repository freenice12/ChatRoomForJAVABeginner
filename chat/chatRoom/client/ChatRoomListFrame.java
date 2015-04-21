package chatRoom.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

public class ChatRoomListFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	protected ClientChat clientChat;
	private String clientId;
//	private DefaultTableModel defaultTableModel;
	private ArrayList<String> list;
	protected JList<String> titleList;
	protected String title;
	protected String password;

	public Logger logger = Logger.getLogger(this.getClass());

	public ChatRoomListFrame(ClientChat clientChat,
			ArrayList<String> list, String clientId) {
		super("Select Room");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.clientChat = clientChat;
		this.clientId = clientId;
		this.list = list;

		JPanel mainPanel = new JPanel();

		JLabel label = new JLabel("Room List");
		JButton createRoomButton = new JButton("Create Room");
		createRoomButton.addActionListener(new CreateRoomButtonActionListener(
				this));
		
		JButton refreshButton = new JButton("Refresh!");
		refreshButton.addActionListener(new RefreshButtonActionListener());

		titleList = new JList<String>();
		
		updateTitleList();
		titleList.addListSelectionListener(new ListHandler());
		titleList.addMouseListener(new ListMouseHandler(this));
		
		JScrollPane scroll = new JScrollPane(titleList);
		
		scroll.setPreferredSize(new Dimension(400,450));
//		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
		mainPanel.add(label, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(scroll, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(createRoomButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(refreshButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		
		this.add(mainPanel);
		this.setPreferredSize(new Dimension(450, 600));
		this.setLocation(100, 100);
		this.setResizable(false);
		this.setVisible(true);
		this.pack();

	}

	private void updateTitleList() {
		updateTitleList(list);
	}
	
	public void updateTitleList(ArrayList<String> titlesList) {
		DefaultListModel<String> defaultListModel = new DefaultListModel<>();
		int index = 0;
		for(String aTitle : titlesList){
			defaultListModel.add(index++, aTitle);
		}
		
		titleList.setModel(defaultListModel);
		
		titleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public class CreateRoomButtonActionListener implements ActionListener {
		ChatRoomListFrame chatRoomListFrame;
		CreateRoomFrame createRoomFrame;

		public CreateRoomButtonActionListener(
				ChatRoomListFrame chatRoomListFrame) {
			this.chatRoomListFrame = chatRoomListFrame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			logger.info(e.getActionCommand() + " button click");
			setVisible(false);
			createRoomFrame = new CreateRoomFrame(chatRoomListFrame);

		}
	}
	public class RefreshButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			logger.info(e.getActionCommand() + " button click");
			clientChat.requestRoomTitles();
		}
	}

	public void createRoom(String pTitle, String pPassword) {
		clientChat.createRoom(pTitle, pPassword);
	}

	public void enterRoom(String pTitle, String pPassword) {
		clientChat.enterRoom(pTitle, pPassword);
	}
	
	public class ListHandler implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent e) {
			title = titleList.getSelectedValue();
		}
		
	}
	
	public class ListMouseHandler implements MouseListener{
		
		ChatRoomListFrame chatRoomListFrame;
		

		public ListMouseHandler(ChatRoomListFrame chatRoomListFrame) {
			super();
			this.chatRoomListFrame = chatRoomListFrame;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2){
				password = JOptionPane.showInputDialog(null, "Enter Password:",
						"Input Password", JOptionPane.DEFAULT_OPTION);
				enterRoom(title, password);
				chatRoomListFrame.setVisible(false);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}
		
	}

}
