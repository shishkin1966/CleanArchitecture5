package shishkin.cleanarchitecture.mvi.app.screen.fragment.val_curs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;


import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.data.Valute;
import shishkin.cleanarchitecture.mvi.app.screen.adapter.ValCursRecyclerViewAdapter;
import shishkin.cleanarchitecture.mvi.common.recyclerview.SwipeTouchHelper;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ValCursFragment extends AbsContentFragment<ValCursModel> implements ValCursView {

    public static ValCursFragment newInstance() {
        return new ValCursFragment();
    }

    private ValCursRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout bottomBar;
    private ExpandableLayout mExpandableLayout;
    private TextView messageView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_val_curs, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = findView(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.gray_light);
        mSwipeRefreshLayout.setOnRefreshListener(getModel().getPresenter());

        mExpandableLayout = findView(R.id.expandable_layout);
        messageView = findView(R.id.message);

        mAdapter = new ValCursRecyclerViewAdapter(getContext(), getModel().getPresenter());
        mAdapter.setOnItemClickListener((v, position, item) -> {
            getModel().getPresenter().onClickItems(item);
        });

        mRecyclerView = findView(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        final ItemTouchHelper.Callback callback = new SwipeTouchHelper(mAdapter) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Valute item = mAdapter.getItem(position);
                getModel().getPresenter().swipeItem(item);

                super.onSwiped(viewHolder, direction);
            }
        };
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        bottomBar = findView(R.id.bottomBar);

        findView(R.id.delete).setOnClickListener(getModel().getPresenter());
    }

    @Override
    public String getName() {
        return ValCursFragment.class.getName();
    }

    @Override
    public ValCursModel createModel() {
        return new ValCursModel(this);
    }

    @Override
    public void refreshViews(ValCursViewData viewData) {
        if (viewData != null && viewData.getData() != null) {
            mAdapter.setItems(viewData.getData());
        }
    }

    @Override
    public void hideProgressBar() {
        if (validate()) {
            super.hideProgressBar();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public boolean onBackPressed() {
        SLUtil.getActivityUnion().switchToTopFragment();
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mRecyclerView.setAdapter(null);
    }

    @Override
    public void refreshSelected(ValCursViewData viewData) {
        mAdapter.notifyDataSetChanged();
        refreshBottomNavigation(viewData);
    }

    @Override
    public void refreshBottomNavigation(ValCursViewData viewData) {
        if (!viewData.isSelected()) {
            if (bottomBar.getVisibility() == View.VISIBLE) {
                mExpandableLayout.collapse();
                final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        bottomBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                bottomBar.startAnimation(animation);
            }
        } else {
            messageView.setText("Выделено : " + viewData.getSelectedCount() + " / " + mAdapter.getItemCount());
            if (bottomBar.getVisibility() == View.GONE) {
                mExpandableLayout.expand();
                bottomBar.setVisibility(View.VISIBLE);
                final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
                bottomBar.startAnimation(animation);
            }
        }
    }

    @Override
    public void removeItems(List<Valute> items) {
        for (Valute item : items) {
            mAdapter.remove(mAdapter.getItems().indexOf(item));
        }
        messageView.setText("Выделено : " + 0 + " / " + mAdapter.getItemCount());
    }
}
