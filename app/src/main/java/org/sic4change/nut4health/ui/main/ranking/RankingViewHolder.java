package org.sic4change.nut4health.ui.main.ranking;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Ranking;
import org.sic4change.nut4health.data.entities.User;

import de.hdodenhof.circleimageview.CircleImageView;


public class RankingViewHolder extends RecyclerView.ViewHolder {

private TextView mRankingUsername;
private TextView mRankingPoints;
private TextView mPosition;
private CircleImageView mUsernamePhoto;
private ImageView ivPositionRanking;
private CardView cvRanking;
private Ranking mRanking;
private RankingAdapter.ItemAction itemAction;
private Context context;

    RankingViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        mPosition = itemView.findViewById(R.id.tvPosition);
        mRankingUsername = itemView.findViewById(R.id.tvUsername);
        mRankingPoints = itemView.findViewById(R.id.tvPoints);
        mUsernamePhoto = itemView.findViewById(R.id.ivUserImage);
        cvRanking = itemView.findViewById(R.id.cvRanking);
        ivPositionRanking = itemView.findViewById(R.id.ivPositionRanking);
    }

    public Ranking getRanking() {
        return mRanking;
    }

    void bindTo(User user, int position, Ranking ranking, final RankingAdapter.ItemAction itemAction) {
        mRanking = ranking;
        mPosition.setText(position + ".");
        mRankingUsername.setText(ranking.getUsername());
        mRankingPoints.setText(ranking.getPoints() + " " + context.getResources().getString(R.string.points));
        if ((ranking.getPhoto() != null) && (!ranking.getPhoto().isEmpty())) {
            Glide.with(context)
                    .load(ranking.getPhoto())
                    .into(mUsernamePhoto);
        } else {
            Glide.with(context)
                    .load(context.getResources().getDrawable(R.mipmap.icon))
                    .into(mUsernamePhoto);
        }
        if (position < 4) {
            ivPositionRanking.setVisibility(View.VISIBLE);
            if (position == 1) {
                Glide.with(context)
                        .load(context.getResources().getDrawable(R.mipmap.ic_gold_medal))
                        .into(ivPositionRanking);
            } else if (position == 2) {
                Glide.with(context)
                        .load(context.getResources().getDrawable(R.mipmap.ic_plate_medal))
                        .into(ivPositionRanking);
            } else {
                Glide.with(context)
                        .load(context.getResources().getDrawable(R.mipmap.ic_bronce_medal))
                        .into(ivPositionRanking);
            }
        } else {
            ivPositionRanking.setVisibility(View.GONE);
        }
        if ((user != null) && (user.getUsername() != null) && (!user.getUsername().isEmpty()) && (ranking.getUsername().equals(user.getUsername()))) {
            cvRanking.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            mPosition.setTextColor(context.getResources().getColor(R.color.colorAccent));
            mRankingUsername.setTextColor(context.getResources().getColor(R.color.colorAccent));
            mRankingPoints.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            mPosition.setTextColor(context.getResources().getColor(R.color.common_google_signin_btn_text_light_default));
            mRankingUsername.setTextColor(context.getResources().getColor(R.color.common_google_signin_btn_text_light_default));
            mRankingPoints.setTextColor(context.getResources().getColor(R.color.common_google_signin_btn_text_light_default));
        }
        setClickAction(this.itemAction);
        cvRanking.setOnClickListener(v -> itemAction.onClick(getRanking().getUsername(), position));
    }

    void clear() {
        itemView.invalidate();
        mRankingUsername.invalidate();
        mRankingPoints.invalidate();
        mUsernamePhoto.invalidate();
    }

    void setClickAction(RankingAdapter.ItemAction itemClickAction) {
        itemAction = itemClickAction;
    }
}
