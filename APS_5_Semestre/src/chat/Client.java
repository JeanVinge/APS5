package chat;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

import javax.swing.JScrollPane;


public class Client extends ChatAdapter implements Serializable{
	
	private static final long serialVersionUID = 9159486593659897373L;
	
	private String chatServer;

	public Client(String host, Usuario user) {
		super("ClienteSide", user,"messages");
		
		chatServer = host; 

		enterField.setEditable(false);
		quandoPressionarEnter();
		
		add(enterField, BorderLayout.NORTH);

		add(new JScrollPane(displayArea), BorderLayout.CENTER);

		setSize(300, 150);
		setVisible(true); 
	}

	private void quandoPressionarEnter() {
		enterField.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					String mensagem = event.getActionCommand();
					sendData(mensagem);
					enterField.setText("");
					user.getMensagens().add(new MensagemTO(mensagem, new Date()));
				}
			} 
		);
	} 

	public void conectarBatePapo() {
		try 
		{
			connectToServer(); 
			getStreams(); 
			verificaConexao(); 
		} 
		catch (EOFException eofException) {
			enviarMensagem(bundle.getMessage("sessaoTerminada"));
		}
		catch (IOException ioException) {
			enviarMensagem(bundle.getMessage("falhaConexao"));
		} finally {
			closeConnection(); 
		} 
	} 

	private void connectToServer() throws IOException {
		enviarMensagem(bundle.getMessage("estabelecendoConexao"));
		connection = new Socket(InetAddress.getByName(chatServer), PORT_CONNECTION);
		enviarMensagem(bundle.getMessage("conectadoA") + connection.getInetAddress().getHostName());
	} 

	private void getStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush(); 

		input = new ObjectInputStream(connection.getInputStream());

		enviarMensagem(bundle.getMessage("conexaoValidada"));
	}

} 
