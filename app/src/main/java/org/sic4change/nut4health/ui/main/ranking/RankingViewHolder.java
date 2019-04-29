package org.sic4change.nut4health.ui.main.ranking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Ranking;

import de.hdodenhof.circleimageview.CircleImageView;


public class RankingViewHolder extends RecyclerView.ViewHolder {

private TextView mRankingUsername;
private TextView mRankingPoints;
private TextView mPosition;
private CircleImageView mUsernamePhoto;
private ImageView ivPositionRanking;
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
        ivPositionRanking = itemView.findViewById(R.id.ivPositionRanking);
    }

    public Ranking getRanking() {
        return mRanking;
    }

    void bindTo(int position, Ranking ranking, final RankingAdapter.ItemAction itemAction) {
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
        if (position < 3) {
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
        //mRankingUsername.setOnClickListener(v -> itemAction.onClick(getmRanking().getId()));
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
