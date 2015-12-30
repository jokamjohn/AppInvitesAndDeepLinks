package johnkagga.me.appinvites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

public class MainActivity extends AppCompatActivity {

    private static final CharSequence INVITATION_TITLE = "call your friends";
    private static final CharSequence INVITATION_MSG = "Invite your friends to enjoy the goodness";
    private static final CharSequence INVITATION_CALL_TO_ACTION = "Share";
    private static final int REQUEST_INVITE = 500;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Button appButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appButton = (Button) findViewById(R.id.invite);
        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new AppInviteInvitation.IntentBuilder(INVITATION_TITLE)
                        .setMessage(INVITATION_MSG)
                        .setCallToActionText(INVITATION_CALL_TO_ACTION)
                        .setDeepLink(Uri.parse("johnkagga://app.invite/50"))
                        .build();
                startActivityForResult(intent, REQUEST_INVITE);
            }
        });

        //Receiving and parsing the custom Uri
        GoogleApiClient apiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.v(LOG_TAG,"Failed connection: " + connectionResult);
                        showMessage("Connection Failed");
                    }
                })
                .build();
        //Parsing the invite
        AppInvite.AppInviteApi.getInvitation(apiClient,this,true)
                .setResultCallback(new ResultCallback<AppInviteInvitationResult>() {
                    @Override
                    public void onResult(@NonNull AppInviteInvitationResult appInviteInvitationResult) {
                        //true above automatically calls the AppInvited activity
                    }
                });

    }

    /**
     * Steps to follow
     * 1. Add the PlayServices SDK
     * 2. Find the directions: https://developers.google.com/app-invites/android/guides/app?configured=true
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                appButton.setVisibility(View.GONE);

                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                StringBuilder sb = new StringBuilder();
                sb.append("Sent ").append(Integer.toString(ids.length)).append(" invitations: ");
                for (String id : ids) sb.append("[").append(id).append("]");
                Log.d(getString(R.string.app_name), sb.toString());

            } else {
                // Sending failed or it was canceled using the back button
                showMessage("Sorry, I wasn't able to send the invites");
            }
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
                .show();
    }

}
