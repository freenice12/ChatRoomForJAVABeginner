package chatRoom.client;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

public class CreateRoomFrame extends JDialog {
	private static final long serialVersionUID = 1L;
	protected JTextField titleField;
	protected JTextField passwordField;
	protected ChatRoomListFrame chatRoomListFrame;
	
	private GridBagLayout gridBagLayout;
	private GridBagConstraints gridBagConstraints;
	

	public Logger logger = Logger.getLogger(this.getClass());

	public CreateRoomFrame(ChatRoomListFrame chatRoomListFrame) {
		super(chatRoomListFrame, "Create Room!", true);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.chatRoomListFrame = chatRoomListFrame;
		
		JLabel topLabel = new JLabel("Create Room!", SwingConstants.CENTER);
		
		JLabel titleLabel = new JLabel("  Title :");
		titleField = new JTextField(20);

		JLabel passwordLabel = new JLabel("  Password :");
		passwordField = new JTextField(17);

		JButton createButton = new JButton("Create!");
		createButton.addActionListener(new CreateButtonListener());
		
		gridBagLayout = new GridBagLayout();
		gridBagConstraints = new GridBagConstraints();
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.BOTH;
		
//		gridBagConstraints.ipady = 10;
		
		gridBagConstraints.insets = new Insets(10, 10, 10, 10);
		
		
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		
		setLayout(gridBagLayout);
		
		setGridBag(topLabel, 0, 0, 2, 1);
		
		setGridBag(titleLabel, 0, 1, 1, 1);
		setGridBag(titleField, 1, 1, 1, 1);
		
		setGridBag(passwordLabel, 0, 2, 1, 1);
		setGridBag(passwordField, 1, 2, 1, 1);
		
		setGridBag(createButton, 0, 3, 2, 1);

		pack();
		this.setResizable(false);
		this.setBounds(100, 100, 350, 250);
		this.setVisible(true);
	}

	private void setGridBag(Component component, int gridx, int gridy, int gridWidth, int gridHeight) {
		gridBagConstraints.gridx = gridx;
		gridBagConstraints.gridy = gridy;
		gridBagConstraints.gridwidth = gridWidth;
		gridBagConstraints.gridheight = gridHeight;
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		
		gridBagLayout.setConstraints(component, gridBagConstraints);

		add(component);
	}

	class CreateButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			logger.info(e.getActionCommand() + " button click");

			chatRoomListFrame.createRoom(titleField.getText(),
					passwordField.getText());
			setVisible(false);

		}
	}
}
