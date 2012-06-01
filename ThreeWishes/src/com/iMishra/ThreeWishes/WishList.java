package com.iMishra.ThreeWishes;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;

/**
 * Servlet implementation class WishList
 */
public class WishList extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private Calendar calendar = new GregorianCalendar();
	
	protected static final String DOCTYPE =
	    "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">";
	
	protected String wish1;
	protected String wish2;
	protected String wish3;
	protected String userName;
	protected String date;
	protected String month;
	protected String year;
	protected String week;
	protected String fullDate;
	protected String[] getListArray = new String[3];
	protected Map<String,String[]> wishMap = new HashMap<String,String[]>();

	public void readDataBase(String userName, String wish1, String wish2, String wish3, ArrayList<String> dateArray) throws Exception {
		try {
			
			String date = dateArray.get(0);
			String month = dateArray.get(1);
			String day = dateArray.get(2);
			String year = dateArray.get(3);
			String week = dateArray.get(4);
			
			preparedStatement = connect
					.prepareStatement("insert into  WISHLIST.WISHES values (default, ?, ?, ?, ?, ?, ?, ?)");

			// Parameters start with 1
			preparedStatement.setString(1, userName);			
			preparedStatement.setString(2, wish1);
			preparedStatement.setString(3, date);
			preparedStatement.setString(4, month);
			preparedStatement.setString(5, day);
			preparedStatement.setString(6, year);
			preparedStatement.setString(7, week);
			preparedStatement.executeUpdate();

			preparedStatement.setString(1, userName);			
			preparedStatement.setString(2, wish2);
			preparedStatement.setString(3, date);
			preparedStatement.setString(4, month);
			preparedStatement.setString(5, day);
			preparedStatement.setString(6, year);
			preparedStatement.setString(7, week);
			preparedStatement.executeUpdate();
			
			preparedStatement.setString(1, userName);			
			preparedStatement.setString(2, wish3);
			preparedStatement.setString(3, date);
			preparedStatement.setString(4, month);
			preparedStatement.setString(5, day);
			preparedStatement.setString(6, year);
			preparedStatement.setString(7, week);
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			throw e;
		}

	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			int id = resultSet.getInt(1);
			String userName = resultSet.getString("username");
			String wish = resultSet.getString("allwishes");
			String date = resultSet.getString("date");
			String month = resultSet.getString("month");
			String day = resultSet.getString("day");
			String year = resultSet.getString("year");
			String week = resultSet.getString("week");
			System.out.println("ID: " + id + ", User: " + userName + ", Wish: " + wish + 
					", Date: " + date + ", Month: " + month + ", Day: " + day + ", Year: " + year +
					", Week: " + week);
			
		}
	}

	// You need to close the resultSet
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WishList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    userName = request.getRemoteUser();
	    wish1 = request.getParameter("1");
	    wish2 = request.getParameter("2");
	    wish3 = request.getParameter("3");
	    getListArray[0] = wish1;
	    getListArray[1] = wish2;
	    getListArray[2] = wish3;
	    
	    year = "" + calendar.get(Calendar.YEAR);
	    month = "" + (calendar.get(Calendar.MONTH)+1);
	    date = "" + calendar.get(Calendar.DATE);
	    week = "" + calendar.get(Calendar.WEEK_OF_MONTH);
	    fullDate = month + "-" + date + "-" + year;
	    
	    ArrayList<String> dateArray = new ArrayList<String>();
	    dateArray.add(fullDate);
	    dateArray.add(month);
	    dateArray.add(date);
	    dateArray.add(year);
	    dateArray.add(week);
		
		System.out.println("My Date === (mm-dd-yyyy) === " + calendar.get(Calendar.MONTH)+1 + "-" + 
					calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.YEAR));
	    
	    try {
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/wishlist?"
							+ "user=wishlistuser&password=wishlistuser");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			readDataBase(userName, wish1, wish2, wish3, dateArray);
			
			//Printing wishes:
			resultSet = statement.executeQuery("select * from WISHLIST.WISHES");
			writeResultSet(resultSet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
	    
	    out.println
	      (headWithTitle("My Wish List") +
	       "<body>\n" + 
	       topHead() + 
	       "<form name=\"input\"> \n" +
	       	"<div id=\"maincontent\"> \n" +
	       		tabs(wish1, wish2, wish3) +
	       		"<div class=\"views\" style=\"top: 160px; height:600px;\"> \n" +
	       			firstWish(getGoogleProducts(wish1), getTweets(wish1)) + 
	       			secondWish(getGoogleProducts(wish2), getTweets(wish2)) +
	       			thirdWish(getGoogleProducts(wish3), getTweets(wish3)) +
	       		"</div> \n" +
	       	"</div> \n" +
	       "</form> \n" +
	       "</body></html>");
	
	}
	
	public String getGoogleProducts(String value) {
		  String googleWish = "";
		  String results = "";
		  String line = null;
		  
		  value = value.replace(" ", "+");
			try {
	          URL google = new URL(
	          		"https://www.googleapis.com/shopping/search/v1/public/products?country=US&maxResults=5&q="+value+
	          		"&startIndex=1&pp=1&key=AIzaSyA1nwGPXd9gBqUJOMkgum4FBVd8i_71g4E");
	          URLConnection tc = google.openConnection();
	          BufferedReader in = new BufferedReader(new InputStreamReader(
	                  tc.getInputStream()));
	          
	          while ((line = in.readLine()) != null) {
	          	results = results + line;
	          	//System.out.println(line);
	          }
	  
	          in.close();
	      } catch (MalformedURLException e) {
	          e.printStackTrace();
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	      
	      InputStream input = null;
	      
	      try {
	    	  input = new ByteArrayInputStream(results.getBytes("UTF-8"));
	      } catch (UnsupportedEncodingException e) {
	    	  e.printStackTrace();
	      }
	      String jsonTxt = null;
			
	      try {
				jsonTxt = IOUtils.toString(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      
			JSONObject jObject = null;
			try {
				jObject = (JSONObject)JSONSerializer.toJSON(jsonTxt);
			} catch (Exception e) {
				e.printStackTrace();
			}
	      
	      JSONArray items = jObject.getJSONArray("items");
	  	
	      googleWish = googleWish + "Google Product Results" + "\n" + "</br>";
	      googleWish = googleWish + "----------------------" + "\n" + "</br>"; 
	      
	      String link = "";
	      String linkURL = "";
	      
	      for(int i = 0; i < items.size(); i++) {
	    	JSONArray imageArray = items.getJSONObject(i).getJSONObject("product").getJSONArray("images");  
	    	String[] imageURLArray = new String[imageArray.size()];
	    	int sizeArray = 0;
	    	if(imageArray.size() > 5) {
	    		sizeArray = 5;
	    	} else {
	    		sizeArray = imageArray.size();
	    	}
	    	
  	      	String imageURLPublish = "";

	    	for(int j = 0; j < sizeArray; j++) {
	    		imageURLArray[j] = imageArray.getJSONObject(j).getString("link");
	    		imageURLPublish = imageURLPublish + " " + "<a href=\"" + imageURLArray[j] + "\" target=\"_blank\">"+ "<img src=\"" + 
	    			imageURLArray[j] + "\" height=200px border=2px></img></a>";
	    	}
	    	
	      	link = items.getJSONObject(i).getJSONObject("product").getString("link");
	      	linkURL = "Link: <a href=\"" + link + "\" target=\"_blank\">" + "Click here" + "</a><br/>\n";
	      	
	      	googleWish = googleWish + "<p>" + imageURLPublish + "</p>";
	      	googleWish = googleWish + "Store "+ (i+1) + ": " + items.getJSONObject(i).getJSONObject("product").getJSONObject("author").getString("name") + "\n" + "</br>";
	      	googleWish = googleWish + "Product Name: " + items.getJSONObject(i).getJSONObject("product").getString("title") + "\n" + "</br>";
	      	googleWish = googleWish + "Brand: " + items.getJSONObject(i).getJSONObject("product").getString("brand") + "\n" + "</br>";
	      	googleWish = googleWish + linkURL;
	      	googleWish = googleWish + "Price: $" + items.getJSONObject(i).getJSONObject("product").getJSONArray("inventories").getJSONObject(0).getString("price") + "\n" + "</br>";
	      	googleWish = googleWish + "" + "</br><br/>" + "\n" + "\n";
	      }
		  return googleWish;
	  }
	  
	  public String getTweets(String value) {
		  String tweets = "";
		  String results = "";
		  String line = null;
		  value = value.replace(" ", "+");

			try {
	          URL google = new URL(
	          		"http://search.twitter.com/search.json?q="+value+"&rpp=5&result_type=mixed&lang=en");
	          URLConnection tc = google.openConnection();
	          BufferedReader in = new BufferedReader(new InputStreamReader(
	                  tc.getInputStream()));
	          
	          while ((line = in.readLine()) != null) {
	          	results = results + line;
	          	//System.out.println(results);
	          }
	  
	          in.close();
	      } catch (MalformedURLException e) {
	          e.printStackTrace();
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	      
	      InputStream input = null;
	      
	      try {
	    	  input = new ByteArrayInputStream(results.getBytes("UTF-8"));
	      } catch (UnsupportedEncodingException e) {
	    	  e.printStackTrace();
	      }
	      String jsonTxt = null;
			
	      try {
				jsonTxt = IOUtils.toString(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      
			JSONObject jObject = null;
	  		try {
	  			jObject = (JSONObject)JSONSerializer.toJSON(jsonTxt);
	  		} catch (Exception e) {
	  			e.printStackTrace();
	  		}
	        
	        JSONArray resultTweets = jObject.getJSONArray("results");
	        System.out.println("Total Results = " + resultTweets.size() + "\n");
	        
	        tweets = tweets + "Recent Tweets" + "\n" + "</br>";
	        tweets = tweets + "-------------" + "\n"; 
	        String profileImage = "";
	        String profileImageURL = "";
	        
	        for(int i = 0; i < resultTweets.size(); i++) {
	        	profileImage = resultTweets.getJSONObject(i).getString("profile_image_url");
	        	profileImageURL = "<br/><img src=\"" + profileImage + "\" border=1px></img>";
	        	tweets = tweets + "<br/>";
	        	tweets = tweets + profileImageURL + " @" + resultTweets.getJSONObject(i).getString("from_user") + " tweets,"+ "\n" + "</br>";
	        	tweets = tweets + resultTweets.getJSONObject(i).getString("text") + "\n" + "</br>";
	        	tweets = tweets + "Date: " + resultTweets.getJSONObject(i).getString("created_at") + "\n" + "</br>";
	        	tweets = tweets + "<br/>" + "\n" + "\n";
	        }
		  return tweets;
	  }
	  
	  
	  public void giveOutput(String output) {
		  
	  }
	  
	  public static String headWithTitle(String title) {
		    return(DOCTYPE + "\n" +
		    		"<HTML>\n" +
		    		"<HEAD><TITLE>" + title + "</TITLE>" +
		    		"<link rel=\"stylesheet\" type=\"text/css\" href=\"./css/buttons.css\" />\n" +
		    	    "<link rel=\"stylesheet\" type=\"text/css\" href=\"./css/frameworknew.css\" />\n" + 
		    	    "<link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>\n" +
		    	    "<script src=\'./javascript/layout.js\' type=\'text/javascript\'></script> \n" +
		    		"</HEAD>\n");
		}
	  
	  public String topHead() {
		  String topHead = "";
		  topHead = topHead + "<div id=\"framecontentTop\"> \n" +
			"<div class=\"innertubeTop\" style=\"top: 0px\"> \n" + "My Wish List" + "\n"
			+ "</div></div> \n";
		  return topHead;
	  }
	  
	  public String tabs(String value1, String value2, String value3) {
		  String leftBody="";
		  leftBody = leftBody + "<div class=\"tabs\" style=\"height: 44px\"> \n" +
			"<div id=\'default\' class=\'tab deselected\' onclick=\'select(this,\"A\")\'>" + value1 + "</div> \n" +
			"<div class=\'tab deselected\' onclick=\'select(this,\"B\")\'>" + value2 + "</div> \n" +
			"<div class=\'tab deselected\' onclick=\'select(this,\"C\")\'>" + value3 + "</div> \n" +
			"</div> \n";
		  return leftBody;
	  }
	  
	  public String firstWish(String google, String tweets) {
		  String bodyContent = "";
		  bodyContent = bodyContent + "<div id=\"A\" class=\"view\">" +
		  				"<div class=\"left\"> \n" +
		  				google + "\n" + "<br/>" + "<br/>" + tweets +
		  				"</div></div>\n";
		  return bodyContent;
	  }
	  
	  public String secondWish(String google, String tweets) {
		  String bodyContent = "";
		  bodyContent = bodyContent + "<div id=\"B\" class=\"view\">" +
		  				"<div class=\"left\">\n" +
		  				google + "\n" + "<br/>" + "<br/>" + tweets +
		  				"</div></div>\n";
		  return bodyContent;
	  }
	  
	  public String thirdWish(String google, String tweets) {
		  String bodyContent = "";
		  bodyContent = bodyContent + "<div id=\"C\" class=\"view\">" +
		  				"<div class=\"left\">\n" +
		  				google + "\n" + "<br/>" + "<br/>" + tweets +
		  				"</div></div> \n";
		  return bodyContent;
	  }
		  
	  
	  public String[] getList() {
		  return getListArray;
	  }
	  
	  public void setList(int key, String value) {
		  getListArray[key] = value;
	  }

}
