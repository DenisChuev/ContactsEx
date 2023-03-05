package dc.contactsdemo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface ApiInterface {
    @GET("get_contacts.php")
    Call<List<Contact>> getContacts();

    @POST("contacts.php")
    Call<List<Contact>> actualContacts(@Query("user_id") int curUserId, @Body List<Contact> contacts);

    @POST("update_token.php")
    Call<String> updateToken(@Query("user_id") int curUserId, @Body String token);
}
