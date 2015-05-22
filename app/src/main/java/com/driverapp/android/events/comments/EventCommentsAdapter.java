package com.driverapp.android.events.comments;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.driverapp.android.R;
import com.driverapp.android.core.BaseViewHolder;
import com.driverapp.android.core.utils.ImageUtil;
import com.driverapp.android.core.utils.UserUtil;
import com.driverapp.android.events.feed.EventViewHolder;
import com.driverapp.android.models.Event;
import com.driverapp.android.models.EventComment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class EventCommentsAdapter extends RecyclerView.Adapter {

    private static final int EVENT_VIEW_TYPE = 0;
    private static final int NEW_COMMENT_VIEW_TYPE =1;

    private final ArrayList<EventComment> comments;
    private final Context context;
    private Event event;

    public EventCommentsAdapter(Context context, ArrayList<EventComment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getItemCount() {
        if(event==null)
            return 0;
        return comments.size()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return EVENT_VIEW_TYPE;
        } else
        if(position==getItemCount()-1)
            return NEW_COMMENT_VIEW_TYPE;
        return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EVENT_VIEW_TYPE) {
            return new EventFullViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_event_content, parent, false));
        } else if (viewType == NEW_COMMENT_VIEW_TYPE)
            return new NewCommentViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment_add, parent, false));
        return new CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof EventFullViewHolder){
            if(event!=null) {
                ((EventFullViewHolder) holder).bind(event);
            }
        } else if(holder instanceof CommentViewHolder){
            ((CommentViewHolder)holder).bind(comments.get(position-1));
        }
    }

    @Override
    public long getItemId(int position) {
        return comments.get(position).id;
    }




    public void setEvent(Event result) {
        this.event = result;
        notifyItemRangeInserted(0, 2);
    }

    public void setComments(ArrayList<EventComment> comments) {
        this.comments.addAll(comments);
        notifyItemRangeInserted(1, comments.size());
    }

    private class EventFullViewHolder extends EventViewHolder {


        public EventFullViewHolder(View itemView) {
            super(itemView);

        }

        public void bind(final Event event) {
            itemView.setVisibility(View.VISIBLE);
            super.bind(event);
            /*
            bodyView.setText(event.desc);
            dateView.setText(TimeUtils.getTime(event.date_create));
            categoryView.setText(event.category_name);
            addressView.setText(event.city + ", " + event.address);
            userNameView.setText(event.getUserName());
            userPhotoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            ImageLoader.getInstance().displayImage(event.user_avatar_path, userPhotoView);
            if (event.photo_path == null) {
                imageView.setVisibility(View.INVISIBLE);
                final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                        new int[] { android.R.attr.actionBarSize });
                int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
                styledAttributes.recycle();
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.height = mActionBarSize;
                imageView.setLayoutParams(params);


            } else {
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.height = ScreenUtil.getWidth();
                imageView.setLayoutParams(params);
                ImageLoader.getInstance().displayImage(event.photo_path, imageView);
            }
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                /*if(UserUtil.id==0){
                    new AlertDialog.Builder(EventActivity.this)
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
                            .show();
                    return;
                }*//*

                    LikeTogglerTask likeTogglerTask = new LikeTogglerTask(event.id) {
                        @Override
                        protected void onSuccess(LikeToggleResult result) {
                            Toast.makeText(getContext(), result.act, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        protected void onError(Exception exp) {
                            Toast.makeText(getContext(), "Error " + exp.toString(),Toast.LENGTH_SHORT).show();
                        }
                    };
                    likeTogglerTask.start();
                }
            });*/

        }
    }

    private class CommentViewHolder extends BaseViewHolder {
        private final ImageView userPhoto;
        private final TextView bodyView;
        private final TextView dateView;
        private final TextView userName;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userPhoto = (ImageView) findViewById(R.id.userPhoto);
            userName = (TextView) findViewById(R.id.user_name);
            bodyView = (TextView) findViewById(R.id.body);
            dateView = (TextView) findViewById(R.id.date);
        }

        public void bind(EventComment comment) {

            userName.setText(comment.getUserName());
            bodyView.setText(comment.comment);
            dateView.setText("" + comment.date);
            ImageLoader.getInstance().loadImage(comment.user_photo_path, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    userPhoto.setImageBitmap(ImageUtil.circle(loadedImage));
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
    }

    private class NewCommentViewHolder extends BaseViewHolder {
        private final View send;

        public NewCommentViewHolder(View itemView) {
            super(itemView);
            send = findViewById(R.id.send);
            final EditText editText = ((EditText)findViewById(R.id.text));
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    send.setEnabled(s.length() != 0);
                }
            });
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AddCommentTask(event.id, String.valueOf(editText.getText())) {
                        @Override
                        protected void onSuccess(final AddCommentResult result) {
                            comments.add(new EventComment() {{
                                id = result.id;
                                comment = String.valueOf(editText.getText());
                                user_photo_path = UserUtil.getPhoto();
                                user_id = UserUtil.id;
                                user_name = UserUtil.getName();
                                user_surname = UserUtil.getSurname();
                            }});
                            editText.setText("");
                        }

                        @Override
                        protected void onError(Exception exp) {

                        }
                    }.execute();
                }
            });
        }
    }
}
