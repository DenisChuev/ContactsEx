package dc.contactsdemo;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseIntentService extends IntentService {

    private Context context = ExerciseIntentService.this;
    private static final String ACTION_WRITE_EXERCISE = "test";

    private NotificationManager notificationManager;
    private static final int NOTIFY_ID = 101;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    public ExerciseIntentService() {
        super("ExerciseIntentService");
    }

    public static void startActionWriteExercise(Context context) {
        Intent intent = new Intent(context, ExerciseIntentService.class);
        intent.setAction(ACTION_WRITE_EXERCISE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WRITE_EXERCISE.equals(action)) {
                handleActionWriteExercise();
            }
        }
    }

    private void handleActionWriteExercise() {

        MeAsyncTask meAsyncTask;

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        List<Contact> colc = new ArrayList<>();
        List<Contact> colc_full = new ArrayList<>();
        List<Contact> colc_bd = new ArrayList<>();

        Contact contact;
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                contact = new Contact();
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                contact.setId(id);


                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contact.setName(name);
                //   contact.setTag(true);

                @SuppressLint("Range") String has_phone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (Integer.parseInt(has_phone) > 0) {
                    // extract phone number
                    Cursor pCur;
                    pCur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        @SuppressLint("Range") String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact.setPhone(phone);
                    }
                    pCur.close();
                }


                if (name != null && !name.contains("@")) {
                    colc.add(contact);

                }

            }

        }

        Collections.sort(colc, Comparator.comparing(Contact::getName));
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getContacts().enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                Log.d("Test", "add server contacts");
                colc_bd.addAll(response.body());

                for (int i = 0; i < colc_bd.size(); i++) {
                    for (int j = 0; j < colc.size(); j++) {

                        String nomer1 = PhoneNumberUtils.normalizeNumber(colc.get(j).getPhone());
                        if (nomer1.startsWith("8")) {
                            nomer1 = nomer1.substring(1);
                        } else if (nomer1.startsWith("+")) {
                            nomer1 = nomer1.substring(2);
                        }

                        String nomer2 = PhoneNumberUtils.normalizeNumber(colc_bd.get(i).getPhone());
                        if (nomer2.startsWith("8")) {
                            nomer2 = nomer2.substring(1);
                        } else if (nomer2.startsWith("+")) {
                            nomer2 = nomer2.substring(2);
                        }

                        if (nomer1.contains(nomer2)) {
                            colc.get(j).setTag(true);
                            colc.get(j).setId(colc_bd.get(i).getId());
                            colc.get(j).setPhoto(colc_bd.get(i).getPhoto());
                            colc.get(j).setBirthday(colc_bd.get(i).getBirthday());
                            colc_full.add(colc.get(j));
                            break;
                        }

                    }
                }
                colc.removeAll(colc_full);
                colc_full.addAll(colc);

                // Плучаю дату сегодня
                String today = DateFormat.format("dd.MM.yy", System.currentTimeMillis()).toString().substring(0, 5);
                List<Contact> contactBirthday = new ArrayList<>();

                for (int i = 0; i < colc_full.size(); i++) {
                    if (colc_full.get(i).getTag()) {
                        String birth = colc_full.get(i).getBirthday().substring(0, 5);
                        if (birth.contentEquals(today)) {
                            contactBirthday.add(colc_full.get(i));
                        }
                    }
                }


                if (contactBirthday.size() > 0) {
                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                    .setAutoCancel(false)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setWhen(System.currentTimeMillis())
                                    .setContentTitle("У Вашего знакомого День Рождения!")
                                    .setContentText("Выберите подарок для " + contactBirthday.get(0).getName() + "!")
                                    .setPriority(PRIORITY_HIGH);

                    createChannelIfNeeded(notificationManager);
                    notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
                }
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.d("Test", "error", t);

            }
        });

    }

    public static void createChannelIfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }
}