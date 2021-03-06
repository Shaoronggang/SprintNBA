package com.yuyh.sprintnba.ui.adapter;

import android.content.Context;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;
import com.yuyh.sprintnba.R;
import com.yuyh.sprintnba.http.bean.forum.ForumsData;
import com.yuyh.sprintnba.support.OnListItemClickListener;
import com.yuyh.sprintnba.utils.FrescoUtils;
import com.yuyh.sprintnba.utils.ItemAnimHelper;

import java.util.List;

/**
 * @author yuyh.
 * @date 16/6/25.
 */
public class ForumListAdapter extends EasyRVAdapter<ForumsData.Forum> {

    private OnListItemClickListener listener;
    private ItemAnimHelper helper = new ItemAnimHelper();

    public ForumListAdapter(List<ForumsData.Forum> mList, Context context, int... layoutIds) {
        super(context, mList, layoutIds);
    }

    @Override
    protected void onBindData(EasyRVHolder viewHolder, final int position, final ForumsData.Forum item) {
        if (!item.fid.equals("0")) {
            viewHolder.setText(R.id.tvTeamFullName, item.name);
            SimpleDraweeView ivTeamLogo = viewHolder.getView(R.id.ivTeamLogo);
            ivTeamLogo.setController(FrescoUtils.getController(item.logo, ivTeamLogo));
        } else {
            viewHolder.setText(R.id.tvForumName, item.name);
        }
        viewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && !item.fid.equals("0"))
                    listener.onItemClick(v, position, item);
            }
        });
        helper.showItemAnim(viewHolder.getItemView(), position);
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getLayoutIndex(int position, ForumsData.Forum item) {
        if (item.fid.equals("0"))
            return 1;
        else
            return 0;
    }
}
