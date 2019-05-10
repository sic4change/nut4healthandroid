package org.sic4change.nut4health.ui.main.ranking;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Ranking;
import org.sic4change.nut4health.data.entities.User;

public class RankingAdapter extends PagedListAdapter<Ranking, RankingViewHolder> {

    private ItemAction mItemOnClickAction;

    Context context;
    User user;

    public RankingAdapter(Context context, User user) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public interface ItemAction {
        void onClick(String username, int position);
    }

    public void setItemOnClickAction(ItemAction itemOnClickAction) {
        mItemOnClickAction = itemOnClickAction;
    }

    public Ranking getRankingAtPosition(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_ranking, parent, false);
        return new RankingViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        Ranking ranking = getItem(position);
        if (ranking != null) {
            holder.bindTo(user, ranking, mItemOnClickAction);
        } else {
            holder.clear();
        }
    }

    private static final DiffUtil.ItemCallback<Ranking> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Ranking>() {
                @Override
                public boolean areItemsTheSame(@NonNull Ranking oldItem, @NonNull Ranking newItem) {
                    return oldItem.getUsername().equals(newItem.getUsername());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Ranking oldItem, @NonNull Ranking newItem) {
                    return oldItem == newItem;
                }
            };


}
