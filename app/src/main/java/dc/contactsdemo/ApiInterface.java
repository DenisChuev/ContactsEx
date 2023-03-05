package dc.contactsdemo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface ApiInterface {
    @GET("get_contacts.php")
    Call<List<Contact>> getContacts();

    @POST("contacts.php")
    Call<List<Contact>> actualContacts(@Body List<Contact> contacts);
}
