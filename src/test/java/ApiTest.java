import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URI;

public class ApiTest {

    String api;

    @BeforeClass
    public void BeforeClass(){
        RestAssured.baseURI = "https://dev.urbox.dev/";
        RestAssured.basePath = "urcard/v1/detail?card_number=";
        System.out.println("Before class");
    }

    @Test
    public void test1() {
        String card_number = "9996036467863613";
        api = RestAssured.baseURI + RestAssured.basePath + card_number;
        System.out.println(api);
        Response response = RestAssured.get(api);

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "API returned an error");

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        JSONObject jsonObject = new JSONObject(responseBody);
        String sub_po_code = jsonObject.getString("sub_po_code");
        System.out.println(sub_po_code);
    }
    @Test(dataProvider =  "numberCard")
    public void test2(String numberCard){
        api = RestAssured.baseURI + RestAssured.basePath + numberCard;
        System.out.println(api);
        Response response = RestAssured.get(api);

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 400, "API returned an error");

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);

        JSONObject error =jsonObject.getJSONObject("error");
        System.out.println(error);

        String detail = error.getString("detail");
        System.out.println(detail);

        Assert.assertEquals(detail, "Mã thẻ không hợp lệ.", "API returned message error" );
    }

    @AfterClass
    public void afterClass(){
        System.out.println("after class");
    }

    @DataProvider(name = "numberCard")
    public Object[][] data(){
        Object[][] objects = new Object[][]{
                {"9996036467863663"},
                {"0996036467863613"},
                {"999603646786"}
        };
        return  objects;
    }

}
