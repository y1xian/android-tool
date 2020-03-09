package com.yyxnb.widget.fragments;


import android.arch.paging.PagedListAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.adapter.MultiItemTypePagedAdapter;
import com.yyxnb.arch.annotations.BindFragment;
import com.yyxnb.widget.adapter.MainListAdapter;
import com.yyxnb.widget.bean.MainBean;
import com.yyxnb.widget.fragments.adapter.AdapterListFragment;
import com.yyxnb.widget.fragments.dialog.DialogFragment;
import com.yyxnb.widget.fragments.http.NetWorkListFragment;
import com.yyxnb.widget.fragments.lazy.FragmentListFragment;
import com.yyxnb.widget.vm.MainViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
@BindFragment
public class HomeFragment extends AbsListFragment<MainBean, MainViewModel> {

    private MainListAdapter adapter = new MainListAdapter();

//    public HomeFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
//    }


    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mRefreshLayout.setEnableRefresh(false).setEnableLoadMore(false);
        adapter.setOnItemClickListener(new MultiItemTypePagedAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
                super.onItemClick(view, holder, position);
//                ToastUtils.INSTANCE.normal("" + adapter.getData().get(position).url);
//                Navigation.findNavController(getView()).navigate(adapter.getData().get(position).id);
                setMenu(position);
            }
        });

    }

    private void setMenu(int position) {
        switch (position) {
            case 0:
                startFragment(TitleFragment.newInstance());
                break;
            case 1:
                startFragment(NetWorkListFragment.newInstance());
                break;
            case 2:
                startFragment(FragmentListFragment.newInstance());
                break;
            case 3:
                startFragment(AdapterListFragment.newInstance());
                break;
            case 4:
                startFragment(new BehaviorFragment());
                break;
            case 5:
                startFragment(TagFragment.newInstance());
                break;
            case 6:
                startFragment(DialogFragment.newInstance());
                break;
            default:
                break;
        }
    }

    @Override
    public PagedListAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

}
