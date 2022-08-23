package testingapi;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.apache.commons.validator.routines.EmailValidator;
import org.testng.Assert;
import payloadPackage.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class MainClass {
    @Test
    public void test(){

        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/";
        EmailValidator validator = EmailValidator.getInstance();
        int userid =0 ;

        String response = given().contentType("application/json").body(PayloadClass.users()).
                when().get("/users").
                then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js = new JsonPath(response);
        String usernames = js.getString("username");
        String uNames[]= usernames.split(", ");
        System.out.println("Search for the user with username “Delphine”");
        for (int x = 0; x <=uNames.length-1; x++)
        {
            if (uNames[x].equalsIgnoreCase("Delphine")) {
                System.out.println("Username is : " + uNames[x]);
                userid = x+1;
            }
        }

        System.out.println("Use the details fetched to make a search for the posts written by the user.");
        String response1 = given().contentType("application/json").body(PayloadClass.posts()).
                when().get("/posts").
                then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath jsp = new JsonPath(response1);
        String userids = jsp.getString("userId");
        String id[]= userids.split(", ");
        String body = jsp.getString("body");
        String bodycomment[]= body.split(",");
        for (int y = 1; y <=id.length-2; y++)
        {
            //System.out.println(Integer.parseInt(id[y]));
            if (Integer.parseInt(id[y])== userid) {
                System.out.println("posts: " + bodycomment[y]);
            }
        }
        System.out.println("For each post, fetch the comments and validate if the emails in the\n" +
                "comment section are in the proper format.");
        String userTitle = jsp.getString("title");
        String uTitle[]= userTitle.split(", ");
        int titleNum=uTitle.length;
        for(int z=0;z<=titleNum-1;z++)
        {
            String response2 = given().queryParam("postId",z+1).
                    when().get("/comments").
                    then().log().all().assertThat().statusCode(200).extract().response().asString();
            JsonPath jsComments = new JsonPath(response2);
            String jsonEmail = jsComments.getString("email");
            String strEmail=jsonEmail.substring(1,jsonEmail.length()-1);
            String arrEmail[]= strEmail.split(", ");
            System.out.println("Validate if the emails in the comment section are in the proper format." );
            for (int a=0;a<=arrEmail.length-1;a++)
            {
                boolean validateEmail = validator.isValid(arrEmail[a]);
                Assert.assertTrue(validateEmail);
            }
        }



    }
        }




