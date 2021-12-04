package application;
	
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import application.User;

public class Main extends Application {
	User user = null;
	String something = "";
	
	private int getNumberOfRows(ResultSet rs) {
		try {
			rs.last();
			int rows = rs.getRow();
			rs.beforeFirst();
			return rows;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
			
	private void login(String username, String password, String table) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "  root", "mysqlroot");
			Statement stmt = con.createStatement();
			
			//username = youngkim, password = password
			ResultSet rs = stmt.executeQuery("select * from " + table + " where username='" + username + "' and password='" + password +"';");
			
			int rows = getNumberOfRows(rs);
			
			if (rows != 1) {
//				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + " " + rs.getString(4));
				user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getInt(10), rs.getString(11), rs.getString(12));
			} else {
				user = null;
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void register(
			String firstName, 
			String lastName, 
			String address, 
			String zip, 
			String state,
			String username,
			String password,
			String email,
			String ssn,
			String securityQuestion,
			String securityAnswer) {
		try {
			//INSERT INTO customer (firstName, lastName, address, zip, state, username, password, email, ssn, securityQuestion, securityAnswer) VALUES ("Young", "Kim", "123 Address St.", 30024, "GA", "youngkim11800", "password", "email@test.com", "1234567890", "First car", "Accord")
// registered
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement();
			String values = "(" + "'" + firstName + "','" + lastName +  "','" + address + "'," + zip + ",'" + state + "','" + username + "','" + password + "','" + email + "'," + ssn + "," + securityQuestion + "','" + securityAnswer + "'" + ")";
			System.out.println("INSERT INTO customer(firstName, lastName, address, zip, state, username, password, email, ssn, securityQuestion, securityAnswer) VALUES" + values);
			ResultSet rs = stmt.executeQuery("INSERT INTO customer(firstName, lastName, address, zip, state, username, password, email, ssn, securityQuestion, securityAnswer) VALUES" + values);
			System.out.println("INSERT INTO customer(firstName, lastName, address, zip, state, username, password, email, ssn, securityQuestion, securityAnswer) VALUES" + values);
			if (rs.next() && rs.getRow() == 1) {
//				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + " " + rs.getString(4));
				user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getInt(10), rs.getString(11), rs.getString(12));
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Welcome");
        
        //simple page with three options: register, login as user, login as admin
        
        //if user presses register
        //javafx code for a lot of rows, username, password, and password again
        //when button is pressed
        //make sure each textfield is filled out, NO NULLs
        //make sure that pw1 and pw2 match or give an error msg
//        register(userTextField.getText(), firstNameTextField.getText(), ...)
        
        GridPane grid = new GridPane();
        renderFirstPage(grid);
        
        //if user presses user
//        userLoginScreen();

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
	public void renderFirstPage(GridPane grid) {
		grid.getChildren().clear();
		
		grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Button registerBtn = new Button("Register");
        Button userLoginBbtn = new Button("Sign in as user");
        Button adminLoginBbtn = new Button("Sign in as admin");
        HBox registerHbBtn = new HBox(10);
        registerHbBtn.setAlignment(Pos.CENTER);
        
        registerHbBtn.getChildren().add(registerBtn);
        registerHbBtn.getChildren().add(userLoginBbtn);
        registerHbBtn.getChildren().add(adminLoginBbtn);
        
        grid.add(registerHbBtn, 0, 1);
        
        registerBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	renderRegisterPage(grid);
            	//TODO
            	System.out.println("TODO");
            }
        });
        
        userLoginBbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	renderUserLoginPage(grid);
            }
        });
        
        adminLoginBbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	renderAdminLoginPage(grid);
            	//TODO
            	System.out.println("TODO");
            }
        });
	}
	
	public void renderRegisterPage(GridPane grid) {
		grid.getChildren().clear();
		
		Text scenetitle = new Text("Register");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        //First Name box
        Label fName = new Label("First Name:");
        grid.add(fName, 0, 1);
        TextField fNameTextField = new TextField();
        grid.add(fNameTextField, 1, 1);
        
        //Last Name box
        Label lName = new Label("Last name:");
        grid.add(lName, 0, 2);
        TextField lNameTextField = new TextField();
        grid.add(lNameTextField, 1, 2);
        
        //Address 
        Label Address = new Label("Address:"); 
        grid.add(Address, 0, 3);
        TextField AddressTextField = new TextField(); 
        grid.add(AddressTextField, 1, 3);
        
        //Zip Code 
        Label Zip = new Label("Zip Code"); 
        grid.add(Zip, 0, 4);
        TextField ZipTextField = new TextField(); 
        grid.add(ZipTextField, 1, 4);
        
        //State 
        Label State = new Label("State:"); 
        grid.add(State, 0, 5); 
        TextField StateTextField = new TextField(); 
        grid.add(StateTextField, 1, 5);

        //Username 
        Label Username = new Label("Username:"); 
        grid.add(Username, 0, 6); 
        TextField UsernameTextField = new TextField(); 
        grid.add(UsernameTextField, 1, 6);
        
        //Password
        Label Password = new Label("Password:"); 
        grid.add(Password, 0, 7); 
        TextField PasswordTextField = new TextField(); 
        grid.add(PasswordTextField, 1, 7);
        
        //Email
        Label Email = new Label("Email:"); 
        grid.add(Email, 0, 8); 
        TextField EmailTextField = new TextField(); 
        grid.add(EmailTextField, 1, 8);
        
        //SSN 
        Label SSN = new Label("SSN:"); 
        grid.add(SSN, 0, 9); 
        TextField SSNTextField = new TextField(); 
        grid.add(SSNTextField, 1, 9);
        
        //Security Q 
        Label SecurityQ = new Label("SecurityQ:"); 
        grid.add(SecurityQ, 0, 10); 
        TextField SecurityQTextField = new TextField(); 
        grid.add(SecurityQTextField, 1, 10);
        
        // Security A 
        Label SecurityA = new Label("SecurityA:"); 
        grid.add(SecurityA, 0, 11); 
        TextField SecurityATextField = new TextField(); 
        grid.add(SecurityATextField, 1, 11);
        
            
        Button btn = new Button("Register");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 12);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent e) {
                //actiontarget.setFill(Color.FIREBRICK);
                
        		//1. validation (no empty fields) young kim
        		Boolean firstNameExists = (fNameTextField.getText() != null && !fNameTextField.getText().isEmpty());
        		Boolean lastNameExists = (lNameTextField.getText() != null && !lNameTextField.getText().isEmpty());
        		Boolean addressExists = (AddressTextField.getText() != null && !AddressTextField.getText().isEmpty()); 
        		Boolean zipExists = (ZipTextField.getText() != null && !ZipTextField.getText().isEmpty()); 
        		Boolean stateExists = (StateTextField.getText() != null && !StateTextField.getText().isEmpty()); 
        		Boolean usernameExists = (UsernameTextField.getText() != null && !UsernameTextField.getText().isEmpty()); 
        		Boolean passwordExists = (PasswordTextField.getText() != null && !PasswordTextField.getText().isEmpty()); 
        		Boolean emailExists = (EmailTextField.getText() != null && !EmailTextField.getText().isEmpty()); 
        		Boolean ssnExists = (SSNTextField.getText() != null && !SSNTextField.getText().isEmpty()); 
        		Boolean securityqExists = (SecurityQTextField.getText() != null && !SecurityQTextField.getText().isEmpty()); 
        		Boolean securityaExists = (SecurityATextField.getText() != null && !SecurityATextField.getText().isEmpty()); 
        		
        		
        		Boolean allFieldsExist = firstNameExists && lastNameExists && addressExists && zipExists && stateExists && usernameExists && passwordExists && emailExists && ssnExists && securityqExists && securityaExists; 
        		//2. make sure there are no duplicates
        		Boolean doesNotExist = hasNoDuplicates(UsernameTextField.getText(), EmailTextField.getText(), "customer");
        		
        		if (allFieldsExist && doesNotExist) {
//        			register(fNameTextField.getText(), lNameTextField.getText());
        		}

        		//2. make sure there are no duplicates
        		//3. insert into table
                // back button on others 
            }
        });
        
        //sign in button, check for dupes
	}
	
	public Boolean hasNoDuplicates(String username, String email, String table) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("select * from " + table + " where username='" + username + "' or email='" + email +"';");
			con.close();
			
			int rows = getNumberOfRows(rs);
			
			if (rows == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public void renderUserLoginPage(GridPane grid) {
		grid.getChildren().clear();
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        Button backBtn = new Button("Back");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        hbBtn.getChildren().add(backBtn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
//                System.out.println("Username is: " + userTextField.getText());
//                System.out.println("Password is: " + pwBox.getText());
                login(userTextField.getText(), pwBox.getText(), "customer");
                System.out.println(user);
                if (user != null) {
                	System.out.println("Successful Login");
                	actiontarget.setText("Successful Login");
                } else {
                	System.out.println("No user with this username/password");
                	actiontarget.setText("No user with this username/password");
                }
            }
        });
	
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
            public void handle(ActionEvent e) {
        		renderFirstPage(grid);
        	}	
        });
   	}
	
		
	public void renderAdminLoginPage(GridPane grid) {
		grid.getChildren().clear();
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        Button backBtn = new Button("Back");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        hbBtn.getChildren().add(backBtn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
//                System.out.println("Username is: " + userTextField.getText());
//                System.out.println("Password is: " + pwBox.getText());
                login(userTextField.getText(), pwBox.getText(), "administrator");
                System.out.println(user);
                if (user != null) {
                	System.out.println("Successful Login");
                	actiontarget.setText("Successful Login");
                } else {
                	System.out.println("No user with this username/password");
                	actiontarget.setText("No user with this username/password");
                }
            }
        });
        
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
            public void handle(ActionEvent e) {
        		renderFirstPage(grid);
        	}
        });
	}
	

	
	// admin and user should be able to log out
	
	
	//young - make initial screen bigger, make sign in database work, flights database should include  
	// make app run, allow admin and user to add/drop flight , admin should be able to add, update or delete a flight, log out button
	
	
	// after initial 
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
