package johnkagga.me.appinvites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInviteReferral;

public class AppInvited extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_invited);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //check whether the intent contains deep link data
        Intent intent = getIntent();
        if (AppInviteReferral.hasReferral(intent))
        {
            String deepLink = AppInviteReferral.getDeepLink(intent);
            Log.d(getString(R.string.app_name),
                    "Found Referral: " + AppInviteReferral.getInvitationId(intent) + ":" + deepLink);

            String[] array = deepLink.split("/");

            if (array.length > 0) {
                TextView tv = (TextView) findViewById(R.id.discount);
                tv.setText(String.format(tv.getText().toString(), array[array.length-1]));
            }
        }
    }
}

