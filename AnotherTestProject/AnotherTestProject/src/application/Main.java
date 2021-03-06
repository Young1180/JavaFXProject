package application;
	
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.ComboBox;

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
			// login function where it pulls strings from database and matches username and password depending on the account type
	private void login(String username, String password, String table) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// matching username to username and password to password to log in
			ResultSet rs = stmt.executeQuery("select * from " + table + " where username='" + username + "' and password='" + password +"';");
			System.out.println("select * from " + table + " where username='" + username + "' and password='" + password +"';");
			int rows = getNumberOfRows(rs);
			System.out.println(rows);
			if (rs.next() && rows == 1) {
				user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getInt(10), rs.getString(11), rs.getString(12));
			} else {
				user = null;
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	// allows user to register by inputting needed values, if successful create account
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
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String values = "(" + "'" + firstName + "','" + lastName +  "','" + address + "'," + zip + ",'" + state + "','" + username + "','" + password + "','" + email + "'," + ssn + ",'" + securityQuestion + "','" + securityAnswer + "'" + ")";
			stmt.executeUpdate("INSERT INTO customer(firstName, lastName, address, zip, state, username, password, email, ssn, securityQuestion, securityAnswer) VALUES" + values + ";", Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			int rows = getNumberOfRows(rs);
			if (rs.next() & rows == 1) {  // Parse the zip code and social security since they are hard digits (string into integer)
				user = new User(rs.getInt(1), firstName, lastName, address, Integer.parseInt(zip), state, username, password, email, Integer.parseInt(ssn), securityQuestion, securityAnswer);
				System.out.println("Successfully made user");
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	//initial start screen boot 
	@Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Welcome");
        
        GridPane grid = new GridPane();
        renderFirstPage(grid);

        Scene scene = new Scene(grid, 1000, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	//initial start screen but with options like registration, user login, and administer login 
	public void renderFirstPage(GridPane grid) {
		grid.getChildren().clear();
		
		grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
// buttons, hBox is button grouping not always needed. 
        Button registerBtn = new Button("Register");
        Button userLoginBbtn = new Button("Sign in as user");
        Button adminLoginBbtn = new Button("Sign in as admin");
        HBox registerHbBtn = new HBox(10);
        registerHbBtn.setAlignment(Pos.CENTER);
// getChildren is a method to get children components like boxes and button (like a list of components currently in the container        
        registerHbBtn.getChildren().add(registerBtn);
        registerHbBtn.getChildren().add(userLoginBbtn);
        registerHbBtn.getChildren().add(adminLoginBbtn);
        
        grid.add(registerHbBtn, 0, 1);
// each button has its own event that sends the user to a different page <ActionEvent>        
        registerBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	renderRegisterPage(grid);
            	System.out.println("Registering...");
            }
        });
        
        userLoginBbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	renderLoginPage(grid, "customer");
            	System.out.println("User signing in...");
            }
        });
        
        adminLoginBbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	renderLoginPage(grid, "administrator");
            	System.out.println("Admin signing in...");	
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
        TextField fNameTextField = new TextField("");
        grid.add(fNameTextField, 1, 1);
        
        //Last Name box
        Label lName = new Label("Last name:");
        grid.add(lName, 0, 2);
        TextField lNameTextField = new TextField("");
        grid.add(lNameTextField, 1, 2);
        
        //Address 
        Label Address = new Label("Address:"); 
        grid.add(Address, 0, 3);
        TextField AddressTextField = new TextField(""); 
        grid.add(AddressTextField, 1, 3);
        
        //Zip Code 
        Label Zip = new Label("Zip Code"); 
        grid.add(Zip, 0, 4);
        TextField ZipTextField = new TextField(""); 
        grid.add(ZipTextField, 1, 4);
        
        //State 
        Label State = new Label("State:"); 
        grid.add(State, 0, 5); 
        TextField StateTextField = new TextField(""); 
        grid.add(StateTextField, 1, 5);

        //Username 
        Label Username = new Label("Username:"); 
        grid.add(Username, 0, 6); 
        TextField UsernameTextField = new TextField(); 
        grid.add(UsernameTextField, 1, 6);
        
        //Password
        Label Password = new Label("Password:"); 
        grid.add(Password, 0, 7); 
        TextField PasswordTextField = new TextField(""); 
        grid.add(PasswordTextField, 1, 7);
        
        //Email
        Label Email = new Label("Email:"); 
        grid.add(Email, 0, 8); 
        TextField EmailTextField = new TextField(); 
        grid.add(EmailTextField, 1, 8);
        
        //SSN 
        Label SSN = new Label("SSN:"); 
        grid.add(SSN, 0, 9); 
        TextField SSNTextField = new TextField(""); 
        grid.add(SSNTextField, 1, 9);
        
        //Security Q 
        Label SecurityQ = new Label("Security Question:"); 
        grid.add(SecurityQ, 0, 10); 
// this string is a ComboBox which is a combination of a text field and a drop down menu so user has to choose since they should'nt make their own questions, redundancy       
        ComboBox<String> SecurityQTextField = new ComboBox<String>();
        SecurityQTextField.getItems().add("First Car");
        SecurityQTextField.getItems().add("Highschool Name");
        SecurityQTextField.getItems().add("Favorite Color");

        grid.add(SecurityQTextField, 1, 10);
        
        // Security A 
        Label SecurityA = new Label("Security Answer:"); 
        grid.add(SecurityA, 0, 11); 
        TextField SecurityATextField = new TextField(""); 
        grid.add(SecurityATextField, 1, 11);
        
            
        Button btn = new Button("Register");
        Button backBtn = new Button("Back");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(backBtn);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 12);
        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 13);
// this section is where boolean statements make sure there are no empty fields, duplicates,          
        btn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent e) {
                
        		//1. validation (no empty fields) young
        		Boolean firstNameExists = (fNameTextField.getText() != null && !fNameTextField.getText().isEmpty());
        		Boolean lastNameExists = (lNameTextField.getText() != null && !lNameTextField.getText().isEmpty());
        		Boolean addressExists = (AddressTextField.getText() != null && !AddressTextField.getText().isEmpty()); 
        		Boolean zipExists = (ZipTextField.getText() != null && !ZipTextField.getText().isEmpty()); 
        		Boolean stateExists = (StateTextField.getText() != null && !StateTextField.getText().isEmpty()); 
        		Boolean usernameExists = (UsernameTextField.getText() != null && !UsernameTextField.getText().isEmpty()); 
        		Boolean passwordExists = (PasswordTextField.getText() != null && !PasswordTextField.getText().isEmpty()); 
        		Boolean emailExists = (EmailTextField.getText() != null && !EmailTextField.getText().isEmpty()); 
        		Boolean ssnExists = (SSNTextField.getText() != null && !SSNTextField.getText().isEmpty()); 
        		Boolean securityqExists = (SecurityQTextField.getValue() != null); 
        		Boolean securityaExists = (SecurityATextField.getText() != null && !SecurityATextField.getText().isEmpty()); 
        		
        		// allFields exists can be seen as a encapsulation since youre hiding the necessary fields as allFieldsExist when compared to the boolean
        		Boolean allFieldsExist = firstNameExists && lastNameExists && addressExists && zipExists && stateExists && usernameExists && passwordExists && emailExists && ssnExists && securityqExists && securityaExists; 
// make sure there are no duplicates  with username and email with hasNoDuplicates application since they should always be unique PK wise
        		Boolean doesNotExist = hasNoDuplicates(UsernameTextField.getText(), EmailTextField.getText(), "customer");
        		
        		if (allFieldsExist && doesNotExist) {
        			register(fNameTextField.getText(), lNameTextField.getText(), AddressTextField.getText(), ZipTextField.getText(), StateTextField.getText(), UsernameTextField.getText(), PasswordTextField.getText(), EmailTextField.getText(), SSNTextField.getText(), SecurityQTextField.getValue().toString(), SecurityATextField.getText());
        			
        			if (user != null)  {
        				renderLoggedInView(grid, "customer");
        			} else {
        				System.out.println("Something went wrong");
        				actiontarget.setFill(Color.FIREBRICK);
        				actiontarget.setText("Something went wrong, validation cleared but user not created");
        			}
        			
        		} else if (!allFieldsExist) {                // allFieldsExist will put out error message saying empty fields 
        			System.out.println("Fields are missing");
        			actiontarget.setFill(Color.FIREBRICK);
    				actiontarget.setText("Fields are missing");
        		} else if (!doesNotExist){                   // doesNotExist will put out error message saying unique field already exists
        			System.out.println("This username/password already exists");
        			actiontarget.setFill(Color.FIREBRICK);
    				actiontarget.setText("User already exists");
        		} else {                                     // validation error where the database can't keep up with the problem or input something foreign
        			System.out.println("Something went wrong");
        			actiontarget.setText("Something went wrong, unclear validation errors");
        		}


            }
        });
// this is the base logic for "logout button" with user = null        
		backBtn.setOnAction(new EventHandler<ActionEvent>() {
		        	
        	@Override
            public void handle(ActionEvent e) {
        		renderFirstPage(grid);
        	}	
        });
        
        
	}
	
	public Boolean hasNoDuplicates(String username, String email, String table) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			ResultSet rs = stmt.executeQuery("select * from " + table + " where username='" + username + "' or email='" + email +"';");
			int rows = getNumberOfRows(rs);
			con.close();
//if fields are not taken return true if taken return false			
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
	// login page 
	public void renderLoginPage(GridPane grid, String table) {
		grid.getChildren().clear();
        Text scenetitle = new Text("Login (" + table + ")");
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
        Button forgotBtn = new Button("Forgot Password?");

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(forgotBtn);
        hbBtn.getChildren().add(btn);
        hbBtn.getChildren().add(backBtn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {
// This ActionEvent is when you press the login button certain results will take place like successful login
            @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
                login(userTextField.getText(), pwBox.getText(), table);
                System.out.println(user);
                if (user != null) {
                	System.out.println("Successful Login");
                	actiontarget.setText("Successful Login");
                	renderLoggedInView(grid, table);
                } else {
                	System.out.println("No " + table + " with this username/password");
                	actiontarget.setText("No " + table + " with this username/password");
                }
            }
        });
	
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
            public void handle(ActionEvent e) {
        		renderFirstPage(grid);
        	}	
        });
 // very important button!       
        forgotBtn.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
            public void handle(ActionEvent e) {
        		renderForgotPasswordPage(grid, table);
        	}	
        });
   	
	}
// when button is pressed, the first requirement is the username, when with the correct username it will give the security question and when you answer successfully you get password. 	
	public void renderForgotPasswordPage(GridPane grid, String table) {
		grid.getChildren().clear();
        Text scenetitle = new Text("Forgot Password? (" + table + ")");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);
// get password button is unable to do the ActionEvent since incorrect username has been inputted. 
        Button btn = new Button("Get Security Question");
        Button getPasswordBtn = new Button("Get Password");
        getPasswordBtn.setDisable(true);  // EXPLAIN HERE
        Button backBtn = new Button("Back");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(backBtn);
        hbBtn.getChildren().add(getPasswordBtn);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 8);
        
        Label secuityQuestionLabel = new Label("Security Question: ");
        grid.add(secuityQuestionLabel, 0, 3);

        final Text securityQuestion = new Text();
        grid.add(securityQuestion, 1, 3);
    	
    	Label securityAnswerLabel = new Label("Security Answer: ");
        grid.add(securityAnswerLabel, 0, 4);
        
        TextField securityAnswer = new TextField();
        grid.add(securityAnswer, 1, 4);
        
        Label passwordLabel = new Label("Password: ");
        grid.add(passwordLabel, 0, 5);

        final Text password = new Text();
        grid.add(password, 1, 5);
        
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
            public void handle(ActionEvent e) {
        		renderFirstPage(grid);
        	}	
        });
        
  
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                String sq = getSecurityQuestion(userTextField.getText(), table);
                
                securityQuestion.setText(sq);
                securityAnswer.setText("");
                password.setText("");
                
                if (sq != "This username does not exist") {
                	getPasswordBtn.setDisable(false); //  get password button is disabled due to the requirement 
                }
                
            }
        });
        
        getPasswordBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                String pw = checkAnswer(userTextField.getText(), securityAnswer.getText(), table);
                
                password.setText(pw);
                
            }
        });
	}
// get security question answer
	public String checkAnswer(String username, String answer, String table) {
		try {
			//Biolerplate connection to database; (can be used without change so its static)
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			//Get current admin/customer user
			ResultSet rs = stmt.executeQuery("select * from " + table + " where username='" + username +"';");
			int rows = getNumberOfRows(rs);
			
			//basecase string (returning a value without making any recursive calls (continuous)
			String password = "Wrong answer";
			
			
			if (rs.next() && rows == 1) {
				//Database answer
				String databaseAnswer = rs.getString(12);
				
				//if inputted answer equals database answer, update string to password
				if (databaseAnswer.trim().toLowerCase().equals(answer.trim().toLowerCase())) {
					password = rs.getString(8); //<- user/admin password
				}
			}
			con.close();
			return password;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
// when trying to retrieve password, you need to input username, and then the security question answer, if these conditions are not met, no security question / password
	public String getSecurityQuestion(String username, String table) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			ResultSet rs = stmt.executeQuery("select * from " + table + " where username='" + username +"';");
			int rows = getNumberOfRows(rs);
			String sq = "This username does not exist";
			if (rs.next() && rows == 1) {
				sq = rs.getString(11);
			}
			con.close();
			return sq;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
// logged in view for admin/user	Polymorphism? superclass is account holder, subclasses user and admin.
	public void renderLoggedInView(GridPane grid, String table) {
		grid.getChildren().clear();
		grid.setAlignment(Pos.TOP_CENTER);
        Text scenetitle = new Text("Flights");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
// this is charts where it shows all flights and booked flights       
        ObservableList<Flight> flights = getFlights();
        ObservableList<Flight> bookedFlights = getBookedFlights(flights, table);
		ObservableList<String> startCities = getAllStartCities(flights);
		ObservableList<String> lastCities = getAllLastCities(flights);
		ObservableList<String> DepartureTimes = getAllDepartureTimes(flights);
		ObservableList<String> ArrivalTime = getAllArrivalTime(flights); 
		ObservableList<String> AllDates = getAllDates(flights);
		
		Label startCityLabel = new Label("Start Cities:");
        Label endCityLabel = new Label("End Cities:");
        Label departureLabel = new Label("Departure Times:");
        Label arrivalLabel = new Label("Arrival Time:");
        Label dateLabel = new Label("Dates:");
//drop down menu for the filters       
		ComboBox<String> startCityDropdown = new ComboBox<String>();
        startCityDropdown.setItems(startCities);
        
        ComboBox<String> lastCityDropdown = new ComboBox<String>();
        lastCityDropdown.setItems(lastCities);  
        
        ComboBox<String> departureTimeDropdown = new ComboBox<String>();
        departureTimeDropdown.setItems(DepartureTimes);
        
        ComboBox<String> arrivalTimeDropdown = new ComboBox<String>();
        arrivalTimeDropdown.setItems(ArrivalTime);
       
        ComboBox<String> allDateDropdown = new ComboBox<String>();
        allDateDropdown.setItems(AllDates);
        
        Button resetFilters = new Button("Reset Filter");
        Button filterFlights = new Button("Filter");
// inheritance        
        HBox filterLabels = new HBox(10);
        filterLabels.setAlignment(Pos.TOP_LEFT);
        filterLabels.getChildren().add(startCityLabel);
        filterLabels.getChildren().add(endCityLabel);
        filterLabels.getChildren().add(departureLabel);
        filterLabels.getChildren().add(arrivalLabel);
        filterLabels.getChildren().add(dateLabel);
        grid.add(filterLabels, 0, 1);
        
        HBox filterDropdowns = new HBox(10);
        filterDropdowns.setAlignment(Pos.TOP_LEFT);
        filterDropdowns.getChildren().add(startCityDropdown);
        filterDropdowns.getChildren().add(lastCityDropdown);
        filterDropdowns.getChildren().add(departureTimeDropdown);
        filterDropdowns.getChildren().add(arrivalTimeDropdown);
        filterDropdowns.getChildren().add(allDateDropdown);
        grid.add(filterDropdowns, 0, 2);
        
        HBox filterButtons = new HBox(10);
        filterDropdowns.setAlignment(Pos.TOP_LEFT);
        filterButtons.getChildren().add(resetFilters);
        filterButtons.getChildren().add(filterFlights);
        grid.add(filterButtons, 1, 2);
        
        final Label flightLabel = new Label("All Flights");
        
        TableView<Flight> flightTable = new TableView<Flight>();
        flightTable.setEditable(false);
        TableColumn<Flight, String> startCityCol = new TableColumn<Flight, String>("Start City");
        startCityCol.setCellValueFactory(new PropertyValueFactory<Flight, String>("startCity"));
        
        TableColumn<Flight, String> endCityCol = new TableColumn<Flight, String>("End City");
        endCityCol.setCellValueFactory(new PropertyValueFactory<Flight, String>("endCity"));
        
        TableColumn<Flight, String> departureTimeCol = new TableColumn<Flight, String>("Departure Time");
        departureTimeCol.setCellValueFactory(new PropertyValueFactory<Flight, String>("departuretime"));
        
        TableColumn<Flight, String> arrivalTimeCol = new TableColumn<Flight, String>("Arrival Time");
        arrivalTimeCol.setCellValueFactory(new PropertyValueFactory<Flight, String>("arrivalTime"));
        
        TableColumn<Flight, String> dateCol = new TableColumn<Flight, String>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<Flight, String>("date"));
        
        if (flights.size() > 0) {
        	flightTable.setItems(flights);
        }
        
        flightTable.getColumns().addAll(startCityCol, endCityCol, departureTimeCol, arrivalTimeCol, dateCol);
        
        final VBox vbox = new VBox();
        vbox.setPadding(new Insets(25, 0, 15, 0));
        vbox.getChildren().addAll(flightLabel, flightTable);
        grid.add(vbox, 0, 3, 10, 10);
        
        Button bookBtn = new Button("Book");
        grid.add(bookBtn, 0, 13);
        
        //My Flights chart after adding a flight from the flight database
        
        final Label bookedFlightsLabel = new Label("My Flights");
        
        TableView<Flight> bookedFlightsTable = new TableView<Flight>();
        bookedFlightsTable.setEditable(false);
        
        TableColumn<Flight, String> startCityCol2 = new TableColumn<Flight, String>("Start City");
        startCityCol2.setCellValueFactory(new PropertyValueFactory<Flight, String>("startCity"));
        
        TableColumn<Flight, String> endCityCol2 = new TableColumn<Flight, String>("End City");
        endCityCol2.setCellValueFactory(new PropertyValueFactory<Flight, String>("endCity"));
        
        TableColumn<Flight, String> departureTimeCol2 = new TableColumn<Flight, String>("Departure Time");
        departureTimeCol2.setCellValueFactory(new PropertyValueFactory<Flight, String>("departuretime"));
        
        TableColumn<Flight, String> arrivalTimeCol2 = new TableColumn<Flight, String>("Arrival Time");
        arrivalTimeCol2.setCellValueFactory(new PropertyValueFactory<Flight, String>("arrivalTime"));
        
        TableColumn<Flight, String> dateCol2 = new TableColumn<Flight, String>("Date");
        dateCol2.setCellValueFactory(new PropertyValueFactory<Flight, String>("date"));
        
        if (bookedFlights.size() > 0) {
        	bookedFlightsTable.setItems(bookedFlights);
        }
        
        bookedFlightsTable.getColumns().addAll(startCityCol2, endCityCol2, departureTimeCol2, arrivalTimeCol2, dateCol2);
        
        final VBox vbox2 = new VBox();
        vbox2.setPadding(new Insets(15, 0, 25, 0));
        vbox2.getChildren().addAll(bookedFlightsLabel, bookedFlightsTable);
        
        grid.add(vbox2, 0, 17, 10, 10);
        
        Button deleteBtn = new Button("Cancel Flight");
        grid.add(deleteBtn, 0, 27);
        Button logoutBtn = new Button("Log Out");
        grid.add(logoutBtn, 9, 27);
        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 14);
   //booking button where it takes flights and puts it in the booking table making it your flight
        bookBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	if (flightTable.getSelectionModel().getSelectedItem() != null) {
                    Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();
                    System.out.println("Flight ID: " + selectedFlight.id);
                    
                    //Booking logic
                    //1. not already booked
                    Boolean notBooked = checkNotBooked(selectedFlight, bookedFlights);
                    
                    //2. full seats
                    Boolean availableSeats = checkAvailableSeats(selectedFlight);
                    
                    //3. time conflict
                    Boolean timeConflict = checkTimeConflict(selectedFlight, bookedFlights);
                    
                    if (notBooked && availableSeats) {
                    	Boolean addedFlight = bookFlight(selectedFlight, table);
                    	// when there is a time conflict input error message
                    	if (timeConflict) {
                    		actiontarget.setFill(Color.GOLDENROD);
            				actiontarget.setText("WARNING: This flight overlaps with another");
                    		System.out.println("WARNING: This flight overlaps with another");
                    	}
                    	// if there is no time conflict book flight
                        if (addedFlight) {
                        	actiontarget.setFill(Color.DARKSEAGREEN);
            				actiontarget.setText("Success!");
                        	bookedFlights.add(selectedFlight);
                        	bookedFlightsTable.setItems(bookedFlights);
                        }
                    } else if (!notBooked){
                    	System.out.println("Already booked");
                    	actiontarget.setFill(Color.FIREBRICK);
        				actiontarget.setText("This flight is already booked");
                    } else if (!availableSeats) {
                    	System.out.println("Seats filled");
                    	actiontarget.setFill(Color.FIREBRICK);
        				actiontarget.setText("This flight is fully booked");
                    } else {
                    	System.out.println("Something went wrong");
                    	actiontarget.setFill(Color.FIREBRICK);
        				actiontarget.setText("Something went wrong");
                    }
                } else {
                	actiontarget.setFill(Color.FIREBRICK);
    				actiontarget.setText("No flights selected!");
                }
            }
        });
// deleting the flights through the table which also deletes the database entry     
        deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	if (bookedFlightsTable.getSelectionModel().getSelectedItem() != null) {
                    Flight selectedFlight = bookedFlightsTable.getSelectionModel().getSelectedItem();
                    
                    Boolean flightRemoved = removeFlight(selectedFlight, table);
                    if (flightRemoved) {
                    	bookedFlights.removeIf(f -> f.id == selectedFlight.id);
                    	bookedFlightsTable.setItems(bookedFlights);
                    	actiontarget.setFill(Color.DARKSLATEBLUE);
        				actiontarget.setText("Successfuly deleted flight!");
                    }
                }
            }
        });

        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
            public void handle(ActionEvent e) {
        		user = null;
        		renderFirstPage(grid);
        	}	
        }); 
// admin privileges where you can edit, delete, and create a flight from the front end to the database        
        if (table == "administrator") {
        	HBox hbBtn = new HBox(5);
            hbBtn.setAlignment(Pos.BASELINE_RIGHT);
            
        	Button createFlightBtn = new Button("Create Flight");
            
            Button editFlightBtn = new Button("Edit Flight");
            
            Button deleteFlightBtn = new Button("Delete Flight");
            
            hbBtn.getChildren().add(createFlightBtn);
            hbBtn.getChildren().add(editFlightBtn);
            hbBtn.getChildren().add(deleteFlightBtn);
            
            grid.add(hbBtn, 2, 13);
// all referencing from the flights database table            
            createFlightBtn.setOnAction(new EventHandler<ActionEvent>() {
            	@Override
                public void handle(ActionEvent e) {
            		renderCreateorEditView(grid, "create", null);
            	}
            });
            
            editFlightBtn.setOnAction(new EventHandler<ActionEvent>() {
            	@Override
                public void handle(ActionEvent e) {
            		if (flightTable.getSelectionModel().getSelectedItem() != null) {
                        Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();
                        renderCreateorEditView(grid, "edit", selectedFlight);
            		} else {
            			actiontarget.setFill(Color.FIREBRICK);
        				actiontarget.setText("Flight not selected");
            		}
            	}
            });
            
            deleteFlightBtn.setOnAction(new EventHandler<ActionEvent>() {
            	@Override
                public void handle(ActionEvent e) {
            		if (flightTable.getSelectionModel().getSelectedItem() != null) {
                        Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();
                        Boolean flightRemoved = adminDeleteFlight(selectedFlight);
                        if (flightRemoved) {
                        	flights.removeIf(f -> f.id == selectedFlight.id);
                        	flightTable.setItems(flights);
                        	
                        	bookedFlights.removeIf(f -> f.id == selectedFlight.id);
                        	bookedFlightsTable.setItems(bookedFlights);
                        }
            		} else {
            			actiontarget.setFill(Color.FIREBRICK);
        				actiontarget.setText("Flight not selected");
            		}
            	}
            });
        }
//IMPORTANT! filters from start/end cities, start/end times, dates       
        resetFilters.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent e) {
        		startCityDropdown.valueProperty().set(null);
        		lastCityDropdown.valueProperty().set(null);
        		departureTimeDropdown.valueProperty().set(null);
        		arrivalTimeDropdown.valueProperty().set(null);
        		allDateDropdown.valueProperty().set(null);
        		
        		flightTable.setItems(flights);
        	}	
        });
        
        filterFlights.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent e) {
        		ObservableList<Flight> filteredFlights = FXCollections.observableArrayList();
        		
        		//Deep copy list to remove values that don't fit the filter
        		for (Flight flight: flights) {
        			filteredFlights.add(flight);
        		}
        		
        		if (startCityDropdown.getValue() != null && !startCityDropdown.getValue().isEmpty()) {
        			filteredFlights.removeIf(f -> !f.startCity.get().equals(startCityDropdown.getValue()));
        		}
        		
        		if (lastCityDropdown.getValue() != null && !lastCityDropdown.getValue().isEmpty()) {
        			filteredFlights.removeIf(f -> !f.endCity.get().equals(lastCityDropdown.getValue()));
        		}
        		
        		if (departureTimeDropdown.getValue() != null && !departureTimeDropdown.getValue().isEmpty()) {
        			filteredFlights.removeIf(f -> !f.departuretime.get().equals(departureTimeDropdown.getValue()));
        		}

        		if (arrivalTimeDropdown.getValue() != null && !arrivalTimeDropdown.getValue().isEmpty()) {
        			filteredFlights.removeIf(f -> !f.arrivalTime.get().equals(arrivalTimeDropdown.getValue()));
        		}
        		
        		if (allDateDropdown.getValue() != null && !allDateDropdown.getValue().isEmpty()) {
        			filteredFlights.removeIf(f -> !f.date.get().equals(allDateDropdown.getValue()));
        		}
        		
        		flightTable.setItems(filteredFlights);
        	}	
        });
	}
// when filtering by start cities only bring up the same start city	
	public ObservableList<String> getAllStartCities(ObservableList<Flight> flights) {
		ObservableList<String> startCities = FXCollections.observableArrayList();
		
		for (int i = 0; i < flights.size(); i++) {
			Flight currentFlight = flights.get(i);
			
			if (!startCities.contains(currentFlight.startCity.get())) {
				startCities.add(currentFlight.startCity.get());
			}
		}
		
		return startCities;
	}
// when filtering by start cities only bring up the same end city		
	public ObservableList<String> getAllLastCities(ObservableList<Flight> flights) {
		ObservableList<String> lastCities = FXCollections.observableArrayList();
		
		for (int i = 0; i < flights.size(); i++) {
			Flight currentFlight = flights.get(i);
			
			if (!lastCities.contains(currentFlight.endCity.get())) {
				lastCities.add(currentFlight.endCity.get());
			}
		}
		
		return lastCities;
	}
// same logic as above	
	public ObservableList<String> getAllDepartureTimes(ObservableList<Flight> flights) {
		ObservableList<String> DepartureTimes = FXCollections.observableArrayList();
		
		for (int i = 0; i < flights.size(); i++) {
			Flight currentFlight = flights.get(i);
			
			if (!DepartureTimes.contains(currentFlight.departuretime.get())) {
				DepartureTimes.add(currentFlight.departuretime.get());
			}
		}
		
		return DepartureTimes;
	}
	
	public ObservableList<String> getAllArrivalTime(ObservableList<Flight> flights) {
		ObservableList<String> ArrivalTime = FXCollections.observableArrayList();
		
		for (int i = 0; i < flights.size(); i++) {
			Flight currentFlight = flights.get(i);
			
			if (!ArrivalTime.contains(currentFlight.arrivalTime.get())) {
				ArrivalTime.add(currentFlight.arrivalTime.get());
			}
		}
		
		return ArrivalTime;
	}

	public ObservableList<String> getAllDates(ObservableList<Flight> flights) {
		ObservableList<String> AllDates = FXCollections.observableArrayList();
		
		for (int i = 0; i < flights.size(); i++) {
			Flight currentFlight = flights.get(i);
			
			if (!AllDates.contains(currentFlight.date.get())) {
				AllDates.add(currentFlight.date.get());
			}
		}
		
		return AllDates;
	}
// this is referencing the flights database on the all flights chart when searching for flights
	public ObservableList<Flight> getFlights() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			ResultSet rs = stmt.executeQuery("select * from flights;");
			
			ObservableList<Flight> flights = FXCollections.observableArrayList();
			
			while(rs.next()) {
				Flight flight = new Flight(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				flights.add(flight);
			}
			
			con.close();
			return flights;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
// this is referencing the my flights table that pulls from the database and the all flights table (FK)	
	public ObservableList<Flight> getBookedFlights(ObservableList<Flight> flights, String table) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String FKname = "";
			if (table.equals("customer")) {
				FKname = "idCustomer";
			} else {
				FKname = "idAdmin";
			}
			ResultSet rs = stmt.executeQuery("select idFlight from booking where " + FKname + "=" + user.id);
			
			ObservableList<Integer> bookedFlightIDs = FXCollections.observableArrayList();
			ObservableList<Flight> bookedFlights = FXCollections.observableArrayList();
			
			while(rs.next()) {
				bookedFlightIDs.add(rs.getInt(1));
			}
			
			for (Flight flight: flights) {
				if (bookedFlightIDs.contains(flight.id)) {
					bookedFlights.add(flight);
				}
			}
			
			con.close();
			return bookedFlights;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
// this is the action of being able to book the actual flight	
	public Boolean bookFlight(Flight selectedFlight, String table) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Boolean success = false;
			
			String FKname = "";
			if (table.equals("customer")) {
				FKname = "idCustomer";
			} else {
				FKname = "idAdmin";
			}
			String values = "(" + user.id + "," + selectedFlight.id + ")";
			stmt.executeUpdate("INSERT INTO booking( " + FKname + ", idFlight) VALUES" + values + ";", Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			int rows = getNumberOfRows(rs);
			if (rs.next() & rows == 1) {
				System.out.println("Successfully booked flight");
				success = true;
			}
			con.close();
			return success;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	// this is the action of being able to remove NOT DELETE the actual flight		
	public Boolean removeFlight(Flight selectedFlight, String table) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Boolean success = false;
			
			String FKname = "";
			if (table.equals("customer")) {
				FKname = "idCustomer";
			} else {
				FKname = "idAdmin";
			}
//			System.out.println("DELETE FROM booking WHERE idCustomer=" + user.id + " and idFlight=" + selectedFlight.id + ";");
			int result = stmt.executeUpdate("DELETE FROM booking WHERE " + FKname + "=" + user.id + " AND idFlight=" + selectedFlight.id + ";");
			if (result == 1) {
				System.out.println("Successfully canceled flight");
				success = true;
			}
			con.close();
			return success;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
// when you try to book a flight but you didnt select anything	
	public Boolean checkNotBooked(Flight selectedFlight, ObservableList<Flight> bookedFlights) {
		ObservableList<Integer> bookedFlightIDs = FXCollections.observableArrayList();
		for (Flight bookedFlight: bookedFlights) {
			bookedFlightIDs.add(bookedFlight.id);
		}
		
		return !bookedFlightIDs.contains(selectedFlight.id);
	}
// referencing seat number with flight id you can check the total space and the amount of space taken	
	public Boolean checkAvailableSeats(Flight selectedFlight) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Boolean seatsAvailable = false;
			
// trying into the booking table to see who booked what and how many booked it			
			ResultSet rs = stmt.executeQuery("select idBooking from booking where idFlight = " + selectedFlight.id + ";");
			int rows = getNumberOfRows(rs);
			int totalSeats = Integer.parseInt(selectedFlight.totalSeats);
			System.out.println("Current Seats: " + rows);
			System.out.println("Total Seats: " + totalSeats);
			if (rows < totalSeats) {
				System.out.println("Seats available");
				seatsAvailable =  true;
			}
			con.close();
			return seatsAvailable;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
// time conflict, the time and date are originally strings on the database but after converting them via parse statements they are integers that will send messages when overlapped	
	public Boolean checkTimeConflict(Flight selectedFlight, ObservableList<Flight> bookedFlights) {
		try {
			ObservableList<FlightTimes> bookedFlightTimes = FXCollections.observableArrayList();
			
			Comparator<FlightTimes> comparator = Comparator.comparingLong(FlightTimes::getStartTime); 
			
			for (Flight bookedFlight: bookedFlights) {
				Long startTime = new SimpleDateFormat("yyyyMMdd HHmm").parse(bookedFlight.date.get() + " " + bookedFlight.departuretime.get()).getTime();
			    Long endTime = new SimpleDateFormat("yyyyMMdd HHmm").parse(bookedFlight.date.get() + " " + bookedFlight.arrivalTime.get()).getTime();
				FlightTimes flightTimes = new FlightTimes(startTime, endTime);
				bookedFlightTimes.add(flightTimes);	
			}
			
			Long selectedFlightStartTime = new SimpleDateFormat("yyyyMMdd HHmm").parse(selectedFlight.date.get() + " " + selectedFlight.departuretime.get()).getTime();
			Long selectedFlightStartEndTime = new SimpleDateFormat("yyyyMMdd HHmm").parse(selectedFlight.date.get() + " " + selectedFlight.departuretime.get()).getTime();
			
			FlightTimes selectedFlightTimes = new FlightTimes(selectedFlightStartTime, selectedFlightStartEndTime);
			bookedFlightTimes.add(selectedFlightTimes);
			
			bookedFlightTimes.sort(comparator);
			
			for (int i = 0; i < bookedFlightTimes.size() - 1; ++i) {
				System.out.println("Flight " + i + " Start: " + bookedFlightTimes.get(i).startTime + " End: " + bookedFlightTimes.get(i).endTime);
	            if (conflictsWith(bookedFlightTimes.get(i), bookedFlightTimes.get(i + 1))) {
	            	System.out.println("Conflict");
	                return true;
	            }
	        }
			
			return false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public static class FlightTimes {
		Long startTime;
		Long endTime;

		public FlightTimes(Long startTime, Long endTime) {
			this.startTime = startTime;
			this.endTime = endTime;
		}
		
		public Long getStartTime() {
			return startTime;
		}
		
		public Long getEndTime() {
			return endTime;
		}
	}
	
	public boolean conflictsWith(FlightTimes flight1, FlightTimes flight2) {
	    if (flight1.endTime >= flight2.startTime) {
	        return true;
	    }

	    if (flight2.endTime >= flight1.startTime) {
	        return true;
	    }

	    return false;
	}
// this is admin being able to delete flights through front end and also being deleted through the back end	
	public boolean adminDeleteFlight(Flight selectedFlight) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Boolean success = false;
			
			int result = stmt.executeUpdate("DELETE FROM flights WHERE idFlights=" + selectedFlight.id + ";");
			if (result == 1) {
				System.out.println("Successfully canceled flight");
				success = true;
			}
			con.close();
			return success;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
// admin page separate from users, additional 3 buttons edit, create, and delete	
	public void renderCreateorEditView(GridPane grid, String mode, Flight selectedFlight) {
		grid.getChildren().clear();
		grid.setAlignment(Pos.CENTER);
		
		String modeFormatted = "Create";
		if (mode.equals("edit")) {
			modeFormatted = "Edit";
		}
        Text scenetitle = new Text(modeFormatted + " Flight");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
        
        //Start City
        Label startCityLabel = new Label("Start City:");
        grid.add(startCityLabel, 0, 1);
        String startCity = null;
        if (mode.equals("edit")) {
        	startCity = selectedFlight.startCity.get();
		}
        TextField startCityTextField = new TextField(startCity);
        grid.add(startCityTextField, 1, 1);
        
     	//End City
        Label endCityLabel = new Label("End City:");
        grid.add(endCityLabel, 0, 2);
        String endCity = null;
        if (mode.equals("edit")) {
        	endCity = selectedFlight.endCity.get();
		}
        TextField endCityTextField = new TextField(endCity);
        grid.add(endCityTextField, 1, 2);
        
        //Departure Time
        Label departureTimeLabel = new Label("Departure Time:");
        grid.add(departureTimeLabel, 0, 3);
        String departureTime = null;
        if (mode.equals("edit")) {
        	departureTime = selectedFlight.departuretime.get();
		}
        TextField departureTimeTextField = new TextField(departureTime);
        grid.add(departureTimeTextField, 1, 3);
        
        //Arrival Time
        Label arrivalTimeLabel = new Label("Arrival Time:");
        grid.add(arrivalTimeLabel, 0, 4);
        String arrivalTime = null;
        if (mode.equals("edit")) {
        	arrivalTime = selectedFlight.arrivalTime.get();
		}
        TextField arrivalTimeTextField = new TextField(arrivalTime);
        grid.add(arrivalTimeTextField, 1, 4);
        
        //Total Seats
        Label totalSeatsLabel = new Label("Total Seats:");
        grid.add(totalSeatsLabel, 0, 5);
        String totalSeats = null;
        if (mode.equals("edit")) {
        	totalSeats = selectedFlight.totalSeats;
		}
        TextField totalSeatsTextField = new TextField(totalSeats);
        grid.add(totalSeatsTextField, 1, 5);
        
        
        //Date
        Label dateLabel = new Label("Date:");
        grid.add(dateLabel, 0, 6);
        String date = null;
        if (mode.equals("edit")) {
        	date = selectedFlight.date.get();
		}
        TextField dateTextField = new TextField(date);
        grid.add(dateTextField, 1, 6);
        
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
         
        Button backBtn = new Button("Cancel");
        hbBtn.getChildren().add(backBtn);
        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 7);
        
        if (mode.equals("edit")) {
        	Button editBtn = new Button("Edit");
        	hbBtn.getChildren().add(editBtn);
 // by setting boolean statements when we edit we can make sure there is nothing wrong (missing fields)           
        	editBtn.setOnAction(new EventHandler<ActionEvent>() {
            	@Override
                public void handle(ActionEvent e) {
            		Boolean startCityTextFieldExists = (startCityTextField.getText() != null && !startCityTextField.getText().isEmpty());
            		Boolean endCityTextFieldExists = (endCityTextField.getText() != null && !endCityTextField.getText().isEmpty());
            		Boolean departureTimeTextFieldExists = (departureTimeTextField.getText() != null && !departureTimeTextField.getText().isEmpty()); 
            		Boolean arrivalTimeTextFieldExists = (arrivalTimeTextField.getText() != null && !arrivalTimeTextField.getText().isEmpty()); 
            		Boolean totalSeatsTextFieldExists = (totalSeatsTextField.getText() != null && !totalSeatsTextField.getText().isEmpty()); 
            		Boolean dateTextFieldExists = (dateTextField.getText() != null && !dateTextField.getText().isEmpty());
            		
            		Boolean allFieldsFilled = 
            				startCityTextFieldExists && 
            				endCityTextFieldExists && 
            				departureTimeTextFieldExists && 
            				arrivalTimeTextFieldExists && 
            				totalSeatsTextFieldExists &&
            				dateTextFieldExists;
            		
            		if (allFieldsFilled) {
            			Boolean success = editFlight(
            					selectedFlight.id,
	                    		startCityTextField.getText(), 
	                    		endCityTextField.getText(), 
	                    		departureTimeTextField.getText(), 
	                    		arrivalTimeTextField.getText(), 
	                    		totalSeatsTextField.getText(), 
	                    		dateTextField.getText()
            			);
            			// once succeeded bring them back to book a flight page
            			if (success) {
                        	renderLoggedInView(grid, "administrator");
                        } else {
                        	System.out.println("Something went wrong");
                        	//Add new text thing like action field
                        }
            		} else {
            			actiontarget.setFill(Color.FIREBRICK);
        				actiontarget.setText("Fields are missing");
            		}
            	}
            });
		} else {
			Button createBtn = new Button("Create");
            hbBtn.getChildren().add(createBtn);
 // when creating flights, error messages will show when something went wrong           
            createBtn.setOnAction(new EventHandler<ActionEvent>() {
            	@Override
                public void handle(ActionEvent e) {
            		Boolean startCityTextFieldExists = (startCityTextField.getText() != null && !startCityTextField.getText().isEmpty());
            		Boolean endCityTextFieldExists = (endCityTextField.getText() != null && !endCityTextField.getText().isEmpty());
            		Boolean departureTimeTextFieldExists = (departureTimeTextField.getText() != null && !departureTimeTextField.getText().isEmpty()); 
            		Boolean arrivalTimeTextFieldExists = (arrivalTimeTextField.getText() != null && !arrivalTimeTextField.getText().isEmpty()); 
            		Boolean totalSeatsTextFieldExists = (totalSeatsTextField.getText() != null && !totalSeatsTextField.getText().isEmpty()); 
            		Boolean dateTextFieldExists = (dateTextField.getText() != null && !dateTextField.getText().isEmpty());
            		
            		Boolean allFieldsFilled = 
            				startCityTextFieldExists && 
            				endCityTextFieldExists && 
            				departureTimeTextFieldExists && 
            				arrivalTimeTextFieldExists && 
            				totalSeatsTextFieldExists &&
            				dateTextFieldExists;
					            		
					if (allFieldsFilled) {
	            		Boolean success = createFlight(
	                    		startCityTextField.getText(), 
	                    		endCityTextField.getText(), 
	                    		departureTimeTextField.getText(), 
	                    		arrivalTimeTextField.getText(), 
	                    		totalSeatsTextField.getText(), 
	                    		dateTextField.getText()
	                    );
	
	                    if (success) {
	                    	renderLoggedInView(grid, "administrator");
	                    } else {
	                    	System.out.println("Something went wrong");
	                    	//Add new text thingy like action field
	                    }
					} else {
            			actiontarget.setFill(Color.FIREBRICK);
        				actiontarget.setText("Fields are missing");
            		}
            	}
            });
		}
        
        grid.add(hbBtn, 1, 7);
        
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent e) {
        		renderLoggedInView(grid, "administrator");
        	}
        });
	}
	//editing flights by referencing the database so when you input the necessary fields, flight is edited
	public Boolean editFlight(int id, String startCity, String endCity, String departureTime, String arrivalTime, String totalSeats, String date) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Boolean success = false;
			
			String values = 
					"startCity=" + "'" + startCity + "'" + "," +
					"lastCity=" + "'" + endCity + "'" + "," +
					"departureTime=" + "'" + departureTime + "'" + "," +
					"arrivalTime=" + "'" + arrivalTime + "'" + "," +
					"totalSeats=" + "'" + totalSeats + "'" + "," +
					"date=" + "'" + date + "'";
					
			int result = stmt.executeUpdate("UPDATE flights SET " + values + " WHERE idFlights=" + id);
			System.out.println("UPDATE flights SET " + values + " WHERE idFlights=" + id);
			
			if (result == 1) {
				System.out.println("Successfully edited flight");
				success = true;
			}
			con.close();
			return success;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	//editing flights by referencing the database so when you input the necessary fields, flight is created	
	public Boolean createFlight(String startCity, String endCity, String departureTime, String arrivalTime, String totalSeats, String date) { 
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "mysqlroot");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// method matches parameters against each other, making sure nothing is blank and it can create/edit flight 
			Boolean success = false;
			String values = "(" + "'" + startCity + "','" + endCity +  "','" + departureTime + "','" + arrivalTime + "','" + totalSeats + "','" + date + "')";
			System.out.println("INSERT INTO flights(startCity, lastCity, departureTime, arrivalTime, totalSeats, date) VALUES" + values + ";");
			stmt.executeUpdate("INSERT INTO flights(startCity, lastCity, departureTime, arrivalTime, totalSeats, date) VALUES" + values + ";", Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			int rows = getNumberOfRows(rs);
			if (rs.next() & rows == 1) {
				System.out.println("Successfully made flight");
				success = true;
			}
			con.close();
			return success;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
