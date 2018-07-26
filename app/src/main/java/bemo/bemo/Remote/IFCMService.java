package bemo.bemo.Remote;

import bemo.bemo.Model.DataMessage;
import bemo.bemo.Model.FCMResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAm8xw5A8:APA91bHtHEN3wO4if5FhFs57voCJtau_4LaxMlmISjDVkn7_ZwahYcS0rq0yP3sfFhv8GbhcN8nuHbWV0-RacE7tWjphOLBtQklKZvT8JSQLWMve37bHlcRx6S9wCz2FsiAQNOGwvmj0"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body DataMessage body);
}
