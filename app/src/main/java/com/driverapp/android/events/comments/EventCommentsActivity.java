package com.driverapp.android.events.comments;

import com.driverapp.android.core.BaseActivity;


public class EventCommentsActivity extends BaseActivity {

    /*private static final java.lang.String EXTRA_EVENTID = "extra_event_id";

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
        View authView = findViewById(R.id.auth);


        if(UserUtil.id ==0){
            *//*new AlertDialog.Builder(EventCommentsActivity.this)
                    .setTitle("Сначала нужно зайти")
                    .setMessage("Пока не сделано")
                    .setPositiveButton("Зайти", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("Ладно", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();*//*
            sendButton.setVisibility(View.GONE);
            commentBox.setVisibility(View.GONE);
            authView.setVisibility(View.GONE);
            return;
        }


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
    }*/
}
