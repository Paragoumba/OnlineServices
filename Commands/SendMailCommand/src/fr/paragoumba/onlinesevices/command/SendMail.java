package fr.paragoumba.onlinesevices.command;

import fr.paragoumba.onlineservices.api.Command;
import fr.paragoumba.onlineservices.api.Status;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMail implements Command {

    @Override
    public int execute(String[] strings) {

        sendMail(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5], strings[6]);
        return Status.OK;
    
    }
    
    private static void sendMail(String host, String from, String to, String username, String password, String subject, String text){

        try {

            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props);
            session.setDebug(true);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(text);

            Transport tr = session.getTransport("smtp");
            tr.connect(host, username, password);
            message.saveChanges();

            System.out.println("Sending message");

            tr.sendMessage(message, message.getAllRecipients());
            tr.close();

            System.out.println("Message sent to " + to + " by " + from);

        } catch (Exception e){

            e.printStackTrace();

        }
    }
}
