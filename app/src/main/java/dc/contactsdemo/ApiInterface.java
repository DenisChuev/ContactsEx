package dc.contactsdemo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface ApiInterface {
    @GET("laffy/db_select_contact.php")
    Call<List<Contact>> getContacts();
}
