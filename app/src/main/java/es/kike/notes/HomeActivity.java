package es.kike.notes;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.login.EvernoteLoginFragment;

public class HomeActivity extends AppCompatActivity implements EvernoteLoginFragment.ResultCallback{

    /* Evernote info */
    private static final String CONSUMER_KEY = "heisba";
    private static final String CONSUMER_SECRET = "22418c4a1a998242";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
    private EvernoteSession mEvernoteSession = null;

    private Button loginButton = null;// (Button) findViewById(R.id.loginButton);
    private View loadingWheel = null;// findViewById(R.id.loadingWheel);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loginButton = (Button) findViewById(R.id.loginButton);
        loadingWheel = findViewById(R.id.loadingWheel);

        loadingWheel.setVisibility(View.GONE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setVisibility(View.GONE);
                loadingWheel.setVisibility(View.VISIBLE);
                attemptLogin();
            }
        });

        /* Start Evernote session */
        mEvernoteSession = new EvernoteSession.Builder(this)
                .setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(true)
                .build(CONSUMER_KEY, CONSUMER_SECRET)
                .asSingleton();
    }

    private void attemptLogin() {
        if(!mEvernoteSession.isLoggedIn()) {
            mEvernoteSession.authenticate(this);
        } else {
            goToNotesView();
        }
    }

    private void goToNotesView() {
        // Change to NotesListView
        Intent notes = new Intent(mEvernoteSession.getApplicationContext(), NotesListActivity.class);
        startActivityForResult(notes, 0);
    }

    @Override
    public void onLoginFinished(boolean successful)
    {
        if (successful) {
            goToNotesView();
        } else {
            // Do not change view and show a message
            Toast.makeText(getApplicationContext(), "Could not login. Try again.", Toast.LENGTH_LONG).show();
            loadingWheel.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }
}
