package dc.contactsdemo;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addContacts(View view) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        apiInterface.actualContacts(getContacts()).enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                Log.d(TAG, response.body().toString());
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {

            }
        });
//        Log.d(TAG, getContacts().toString());
    }

    @SuppressLint("Range")
    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();

        Contact contact;
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                contact = new Contact();
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                contact.setId(id);
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contact.setName(name);

                String has_phone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (Integer.parseInt(has_phone) > 0) {
                    Cursor pCur;
                    pCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String normPhone = PhoneNumberUtils.normalizeNumber(phone);
                        if (normPhone.startsWith("+")) {
                            normPhone = normPhone.substring(1);
                        } else {
                            normPhone = "7" + normPhone.substring(1);
                        }
                        if (normPhone.length() != 11) continue;
                        contact.setPhone(normPhone);
                    }
                    pCur.close();
                }


                if (name != null && !name.contains("@") && contact.phone != null) {
                    contacts.add(contact);
                }
            }
        }
        cursor.close();
        return contacts;
    }
}