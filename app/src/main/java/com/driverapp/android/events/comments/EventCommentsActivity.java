package com.driverapp.android.events.comments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.driverapp.android.R;
import com.driverapp.android.core.BaseActivity;
import com.driverapp.android.events.comments.EventCommentsAdapter;
import com.driverapp.android.models.EventComment;

import java.util.ArrayList;


public class EventCommentsActivity extends BaseActivity {

    private static final java.lang.String EXTRA_EVENTID = "extra_event_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        final TextView statusView = (TextView) findViewById(R.id.status);
        final ListView list = (ListView) findViewById(R.id.list);
        int eventId = getIntent().getExtras().getInt(EXTRA_EVENTID);
        EventCommentsTask listTask = new EventCommentsTask(eventId) {
            @Override
            protected void onSuccess(ArrayList<EventComment> result) {
                list.setAdapter(new EventCommentsAdapter(getBaseContext(), result));
                if(result.isEmpty()){
                    statusView.setText("Пока никто не комментировал");
                }
            }

            @Override
            protected void onError(Exception exp) {
                statusView.setText("Ошибка загрузки");
            }
        };
        listTask.start();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View sendButton = findViewById(R.id.send);
        final EditText commentBox = (EditText) findViewById(R.id.comment_text);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentBody = commentBox.getText().toString();
                NewCommentTask newCommentTask = new NewCommentTask(commentBody);


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getActivityIntent(Context context, int id) {
        Intent intent = new Intent(context, EventCommentsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_EVENTID, id);
        intent.putExtras(bundle);
        return intent;
    }
}
