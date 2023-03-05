package dc.contactsdemo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FcmService extends FirebaseMessagingService {
    public static final String TAG = "FcmService";

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.updateToken(1, token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.d(TAG, response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.d(TAG, "From: " + message.getFrom());

        // Check if message contains a data payload.
        if (message.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + message.getData());

        }

        // Check if message contains a notification payload.
        if (message.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + message.getNotification().getBody());
        }
    }


}
