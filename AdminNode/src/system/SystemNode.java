package system;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import common.User;
import common.Publication;
public class SystemNode
{
	private ConnectionFactory connectionFactory;
	private Destination destination;
	
	// Contains all requests from Users that need admin's approval
	public static ArrayList<TextMessage> msgs=new ArrayList<TextMessage>(); 
	
	public SystemNode() throws NamingException
	{
		this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.destination = InitialContext.doLookup("jms/topic/playTopic");
	}
	
	
	// Verifies if a user is active or not
	public int verifyActiveUSer(String username) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		String jpql = "SELECT s FROM User s";
    	TypedQuery<User> typedQuery = em.createQuery(jpql, User.class);
    	List<User> mylist = typedQuery.getResultList();
    	
    	for(User u: mylist) {
    		if(u.getUsername().equals(username) && u.isActive()) {
    			em.close();
    			emf.close();
    			return 0;
    		}
    		if(u.getUsername().equals(username) && u.isActive()==false) {
    			em.close();
    			emf.close();
    			return 1;
    		}		
    	}
    	em.close();
		emf.close();
    	return 1;
	}
	
	//Activate a user
	public String activateUser(String username) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		String jpql = "SELECT s FROM User s";
    	TypedQuery<User> typedQuery = em.createQuery(jpql, User.class);
    	List<User> mylist = typedQuery.getResultList();
    	
    	for(User u: mylist) {
    		if(u.getUsername().equals(username) && u.isActive()) {
    			em.close();
    			emf.close();
    			return "User already active";
    		}
    		if(u.getUsername().equals(username) && u.isActive()==false) {
    			u.setActive(true);
    			em.getTransaction().commit();
    			em.close();
    			emf.close();
    			return "User activated";
    		}		
    	}
    	em.close();
		emf.close();
    	return "User not found";
	}
	
	//Deactivate a user
	public String deactivateUser(String username) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		String jpql = "SELECT s FROM User s";
    	TypedQuery<User> typedQuery = em.createQuery(jpql, User.class);
    	List<User> mylist = typedQuery.getResultList();
    	
    	for(User u: mylist) {
    		if(u.getUsername().equals(username) && u.isActive()) {
    			u.setActive(false);
    			em.getTransaction().commit();
    			em.close();
    			emf.close();
    			return "User deactivated";
    		}
    		if(u.getUsername().equals(username) && u.isActive()==false) {
    			em.close();
    			emf.close();
    			return "User already deactivated";
    		}		
    	}
    	em.close();
		emf.close();
    	return "User not found";
	}
	
	//List all active users
	public String listUsers() {
		String users="";
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		String jpql = "SELECT s FROM User s";
    	TypedQuery<User> typedQuery = em.createQuery(jpql, User.class);
    	List<User> mylist = typedQuery.getResultList();
		
    	for(User u: mylist) {
    		if(u.isActive()) {
    			users+= u.getUsername() + "\n";
    		}
    	}
    	em.close();
		emf.close();
    	return users;
	}
	
	// FUnction to delete a publication
	public String deletePub(String title) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();

		
		String jpql = "SELECT s FROM Publication s";
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	List<Publication> mylist = typedQuery.getResultList();
    	
    	for(Publication p: mylist) {
    		if(p.getName().equals(title)) {
    			em.remove(p);
    			em.getTransaction().commit();
    			em.close();
    			emf.close();
    			return "The publication was removed successfully\n";
    		}
    			
    		
    	}
    	em.close();
		emf.close();
		return "The publication wasn't removed or didnt exist!\n";
		
		
		
	}
	
	// Add Publication
	public String addPub(String title, String date,String type) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		String jpql = "SELECT s FROM Publication s";
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	List<Publication> mylist = typedQuery.getResultList();
    	
    	for(Publication p: mylist) {
    		if(p.getName().equals(title)) {
    			return "This publication already exists";
    		}
    	}
		Publication pb1 = new Publication(title,type,date);
		em.persist(pb1);
		em.getTransaction().commit();
		em.close();
		emf.close();
		return "Publication added";

	}
	
	// UpdatePublication Title
	public String updateTitle(String title,String newtitle) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		String jpql = "SELECT s FROM Publication s";
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	List<Publication> mylist = typedQuery.getResultList();
    	
    	for(Publication p:mylist) {
    		if(p.getName().equals(title)) {
    			p.setName(newtitle);
    			Publication pb1 = p;
    			em.remove(p);
    			em.persist(pb1);
    			em.getTransaction().commit();
    			em.close();
    			emf.close();
    			return "Publication Updated";
    		}
    	}
    	return "Publication did not exist";
    	
	}
	
	// Update Publication Type
	public String updateType(String title,String newType) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		String jpql = "SELECT s FROM Publication s";
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	List<Publication> mylist = typedQuery.getResultList();
    	
    	for(Publication p:mylist) {
    		if(p.getName().equals(title)) {
    			p.setType(newType);
    			Publication pb1 = p;
    			em.remove(p);
    			em.persist(pb1);
    			em.getTransaction().commit();
    			em.close();
    			emf.close();
    			return "Publication Updated";
    		}
    	}
    	return "Publication did not exist";
	}
	
	// Update Publication PubDate
	public String updateDate(String title, String newdate) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		String jpql = "SELECT s FROM Publication s";
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	List<Publication> mylist = typedQuery.getResultList();
    	
    	for(Publication p:mylist) {
    		if(p.getName().equals(title)) {
    			p.setPubDate(newdate);
    			Publication pb1=p;
    			em.remove(p);
    			em.persist(pb1);
    			em.getTransaction().commit();
    			em.close();
    			emf.close();
    			return "Publication Updated";
    		}
    	}
    	return "Publication did not exist";
	}
	
	// Function returns all info from a certain Publication title
public String titlePubInfo(String title) {
	
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
    	
    	String jpql = "SELECT s FROM Publication s";
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	List<Publication> mylist = typedQuery.getResultList();
    	
    	String kappa = "";
    	
    	for(Publication p: mylist) {
    		if(p.getName().equals(title)) {

        		kappa += "Name: " + p.getName() + " Type: " + p.getType() + " Publication Date: " + p.getPubDate();
    		}
    	}
    	return kappa; 	
	}
	
	// Function goes to the DB and returns all Publication Titles if they exist
	public String getPubTitles() {
		
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		    	
    	String jpql = "SELECT s FROM Publication s";
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	List<Publication> mylist = typedQuery.getResultList();
    	
    	String temp ="";
    	// Gets all info from Publications
    	for(Publication p: mylist) {
    		temp += "Publication\n";
    		temp += "Name: " + p.getName()+"\n";	
    		System.out.println();
    		temp+="\n";
    	}
    	return temp;
     	
    }
    
	// Function that verifies if username and password match one from the DB
	public int verifyLogin(String username, String password) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		
		String jpql = "Select u FROM User u";
    	TypedQuery<User> typedQuery = em.createQuery(jpql, User.class);
    	List<User> listUsers = typedQuery.getResultList();
    	
    	for(User i: listUsers) {
    		if(i.getUsername().equals(username) && i.getPassword().equals(password))
    			return 0; // 0 - Username and Password match the ones found in the DB
    	}
    	
    	em.close();
		emf.close();
    	
    	return 1;  // No matches were found inside the DB
    	
    	
		
		
		
	}
	
	// ADDS A researcher to the DB
	public int addResearcher(String username,String password) {
		
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		
		String jpql = "Select u FROM User u";
    	TypedQuery<User> typedQuery = em.createQuery(jpql, User.class);
    	List<User> listUsers = typedQuery.getResultList();
    	
    	for(User i: listUsers) {
    		if(i.getUsername().equals(username))
    			return 1; // 1 - Username ja existe
    	}
		
		User user = new User(username,password,true);
		
		em.persist(user);
		
		em.getTransaction().commit();
		em.close();
		emf.close();
		
		return 0; // Username doesnt exist and the registration is completed
	}
	
	
	
	private void receive() throws JMSException, FileNotFoundException, NamingException, IOException
	{
		
		// try (JMSContext context = connectionFactory.createContext("calmo", "Calmo@1997");)
		try (JMSContext context = connectionFactory.createContext("calmo", "Calmo@1997");)
		{
			JMSConsumer consumer = context.createConsumer(destination);
			TextMessage msg = (TextMessage) consumer.receive();
			Scanner in = new Scanner(System.in); 
			String temp,opt;
			
			System.out.println("Researcher: "+ msg.getText());
			
			String [] tokens = msg.getText().split(" ");
			String [] request;
			JMSProducer producer = context.createProducer();
			TextMessage reply = context.createTextMessage();
			
			// task comes from the admin and its the same as the one from Researcher
			switch(tokens[0]) {
				// 1 - Register. User needs admins approval 				
				case "1": 				
			        msgs.add(msg);

					break;
					
				// Login doesnt need admin's approval
				case "2":
					if(verifyActiveUSer(tokens[1])==0) {
						if(verifyLogin(tokens[1],tokens[2])== 0)
			        		reply.setText("SUCCESS Researcher " + tokens[1]+ " has logged in !");
						else
			        		reply.setText("ERROR Researcher " + tokens[1]+ " failed to log in !");
					}
					else 
						reply.setText("ERROR Researcher is not active and does not have permission to enter the system");
					
					producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
					
				// Researcher or Admin wants to see all Publication Titles	
				case "11":
				case "3":
					temp = getPubTitles();
	        		reply.setText("Publications Titles: " + temp);
		        	producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
				
				// Search a Publication with a title
				case "12":
				case "4":
					temp = titlePubInfo(tokens[1]);
	        		reply.setText(temp);
		        	producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
					
				// Add a publication requires admin approval	
				case "5":

			        msgs.add(msg);
					break;
				
				//Update publication title requires admin approval
				case "6":
			        msgs.add(msg);
					break;
					
				//Update publication date requires admin approval
				case "20":

			        msgs.add(msg);
					break;
					
				//Update publication type requires admin's approval
				case "21":


			        msgs.add(msg);
					break;
				// Delete a Publication requires admin's approval
				case "7":

			        msgs.add(msg);
					break;
					
				//List all users ADMIN ONLY
				case "8":
					String res=listUsers();
					reply.setText(res);
					producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
				// Deactivate User ADMIN ONLY
				case "10":
					String res1=deactivateUser(tokens[1]);
					reply.setText(res1);
					producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
				// Activate User ADMIN ONLY
				case "13":
					String res11=activateUser(tokens[1]);
					reply.setText(res11);
					producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
				//Pending Tasks ADMIN ONLY	
				case "9":
					String tasks="";
					for(TextMessage i: msgs) {
						tasks+= i.getText() + "\n";
					}
					reply.setText(tasks);
					producer.send(msg.getJMSReplyTo(),reply);
					System.out.println("Sent reply to "+msg.getJMSReplyTo());
					break;
				// Admin's answers to the pending tasks	
				case "y":
				case "n":
					for(int i=0;i<tokens.length;i++) {
						request=msgs.get(i).getText().split(" ");
						
						if(request[0].equals("1") && tokens[i].equals("y")) {
							int res2 = addResearcher(request[1],request[2]);
				        	if(res2 == 0)
				        		reply.setText("SUCCESS Researcher " + request[1]+ " has been registered with success!");
				        	else
				        		reply.setText("The username already exists! Please try again");
						}
						 else if(request[0].equals("1") && tokens[i].equals("y"))
								reply.setText("ERROR Researcher " + request[1]+ " wasnt registered!");
						
						if(request[0].equals("5") && tokens[i].equals("y")) {
							String res3 = addPub(request[1],request[2],request[3]);
				        	reply.setText(res3);
				        	Thread notif = new Thread(new Notif("Publication has been added to the DB"));
				        	notif.start();
				        }				        		        
				        else if(request[0].equals("5") && tokens[i].equals("n"))
							reply.setText("ERROR Admin did not authorize");
						
						if(request[0].equals("6") && tokens[i].equals("y")) {
							String res4 = updateTitle(request[1],request[2]);
				        	reply.setText(res4);
				        	Thread notif = new Thread(new Notif("Publication title has been updated"));
				        	notif.start();
				        	
						}else if(request[0].equals("6") && tokens[i].equals("n"))
							reply.setText("ERROR Admin did not authorize");
						
						if(request[0].equals("7") && tokens[i].equals("y")) {
							String res5 = deletePub(request[1]);
				        	reply.setText(res5);
				        	Thread notif = new Thread(new Notif("Publication  has been deleted"));
				        	notif.start();
				        }
				        		        
				        else if(request[0].equals("7") && tokens[i].equals("n"))
							reply.setText("ERROR Admin did not authorize");	
						
						if(request[0].equals("20") && tokens[i].equals("y")) {
							String res6 = updateDate(request[1],request[2]);
				        	reply.setText(res6);
				        	Thread notif = new Thread(new Notif("Publication date has been updated"));
				        	notif.start();
						}
						else if(request[0].equals("20") && tokens[i].equals("n"))
							reply.setText("ERROR Admin did not authorize");
						
						if(request[0].equals("21")&& tokens[i].equals("y")) {
							String res7 = updateType(request[1],request[2]);
				        	reply.setText(res7);
				        	Thread notif = new Thread(new Notif("Publication type has been updated"));
				        	notif.start();
						}
						else if(request[0].equals("21") && tokens[i].equals("n"))
							reply.setText("ERROR Admin did not authorize");
						
						producer.send(msgs.get(i).getJMSReplyTo(), reply);
						producer.send(msg.getJMSReplyTo(), "Task done");
						System.out.println("Sent reply to " + msgs.get(i).getJMSReplyTo());
					}
					msgs.clear();
			}
			
			
			
		}
		catch (JMSRuntimeException re)
		{
		re.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws NamingException, JMSException, FileNotFoundException, IOException
	{

		SystemNode client = new SystemNode();
		while(true) {
			client.receive();
		}
		
		
	}
}