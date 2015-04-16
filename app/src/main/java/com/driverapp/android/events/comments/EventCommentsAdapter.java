package com.driverapp.android.events.comments;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.driverapp.android.R;
import com.driverapp.android.core.BaseViewHolder;
import com.driverapp.android.core.utils.ScreenUtil;
import com.driverapp.android.core.utils.TimeUtils;
import com.driverapp.android.events.LikeToggleResult;
import com.driverapp.android.events.LikeTogglerTask;
import com.driverapp.android.models.Event;
import com.driverapp.android.models.EventComment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class EventCommentsAdapter extends RecyclerView.Adapter {

    private static final int EVENT_VIEW_TYPE = 0;
    private final ArrayList<EventComment> comments;
    private final Context context;
    private Event event;

    public EventCommentsAdapter(Context context, ArrayList<EventComment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getItemCount() {
        return comments.size()+1;
    }

    public EventComment getItem(int position) {
        return comments.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return EVENT_VIEW_TYPE;
        }
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EVENT_VIEW_TYPE) {
            return new EventFullViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_event_content, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position==0){
            if(event!=null) {
                ((EventFullViewHolder) holder).bind(event);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return comments.get(position).id;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);

        ImageView userPhoto = (ImageView) itemView.findViewById(R.id.user_photo);
        TextView dateView = (TextView) itemView.findViewById(R.id.date);
        TextView bodyView = (TextView) itemView.findViewById(R.id.body);

        EventComment comment = getItem(position);

        bodyView.setText(comment.getUserName() + " " + comment.comment);
        dateView.setText(comment.date_create);
        ImageLoader.getInstance().displayImage(comment.user_photo_path, userPhoto);

        return itemView;
    }

    public void setEvent(Event result) {
        this.event = result;
        notifyItemChanged(0);
    }

    private class EventFullViewHolder extends BaseViewHolder {

        private View likeButton;
        private TextView addressView;
        private TextView userNameView;
        private TextView categoryView;
        private TextView bodyView;
        private ImageView imageView;
        private ImageView userPhotoView;
        private TextView dateView;


        public EventFullViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            bodyView = (TextView) itemView.findViewById(R.id.body);
            dateView = (TextView) itemView.findViewById(R.id.date);
            addressView = (TextView) itemView.findViewById(R.id.address);
            userNameView = (TextView) itemView.findViewById(R.id.user_name);
            userPhotoView = (ImageView) itemView.findViewById(R.id.user_photo);
            categoryView = (TextView) itemView.findViewById(R.id.category);
            likeButton =  itemView.findViewById(R.id.like_holder);

        }

        public void bind(final Event event){
            itemView.setVisibility(View.VISIBLE);
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
                }*/

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
            });
        }
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder {
        public CommentViewHolder(View itemView) {
            super(itemView);
        }
    }
}
