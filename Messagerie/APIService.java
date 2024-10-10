package ca.uqac.projetmobile.Messagerie;

import ca.uqac.projetmobile.Notifications.MyResponse;
import ca.uqac.projetmobile.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAbAhZ-JA:APA91bFN8Ouy9lnYGjWlRpBffz_TGwn0bbdADYvXGLsCE9mZYBV7SJDls1-1giIlPtoqmSusbRCvuuViHUgwgdIbE9lNYZhagMwwPf6F3M12NClSJEfUSjeBu5K-c0j_X296CzMFC8iI"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
