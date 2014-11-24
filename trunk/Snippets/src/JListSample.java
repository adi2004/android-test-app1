import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JListSample {
	final static ImageIcon longIcon = new ImageIcon("gui\\Back24.gif");
	final static ImageIcon shortIcon = new ImageIcon("gui\\Forward24.gif");

	int size = 10;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new JListSample().createUI();
			}
		});

	}

	void createUI() {
		JFrame frame = new JFrame();
		String values[] = { "a", "b", "c", "c", "c" };//, "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c" };
		final DefaultListModel<String> listModel = new DefaultListModel<String>();
		listModel.addElement("One");
		listModel.addElement("2");
		listModel.addElement("Three");

		JButton button = new JButton("Inc size");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Size: " + size);
				listModel.setElementAt("Two", 1);
			}
		});

		JList<String> list = new JList<String>();
		// list.setListData(bigData);
		list.setModel(listModel);
		// bigData.addListDataListener(list.getListSelectionListeners());
		// list.get
		// bigData.addListDataListener(list);
		// new DefaultListModel();
		list.setCellRenderer(new MyCellRenderer());
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				System.out.println("Event: " + e);
			}
		});

		JScrollPane scrollPane = new JScrollPane(list);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(scrollPane);
		frame.add(button);
		frame.setLayout(new FlowLayout());
		frame.pack();
		frame.setVisible(true);
	}

	//Display an icon and a string for each object in the list.

	class MyCellRenderer extends JLabel implements ListCellRenderer<Object> {
		// This is the only method defined by ListCellRenderer.
		// We just reconfigure the JLabel each time we're called.

		public Component getListCellRendererComponent(JList<?> list, // the list
				Object value, // value to display
				int index, // cell index
				boolean isSelected, // is the cell selected
				boolean cellHasFocus) // does the cell have focus
		{
			String s = value.toString();
			setText(s);
			setIcon((s.length() > 10) ? longIcon : shortIcon);
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return this;
		}
	}
}