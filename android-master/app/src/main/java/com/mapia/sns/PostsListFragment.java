package com.mapia.sns;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.mapia.R;
import com.mapia.sns.asne.core.SocialNetwork;
import com.mapia.sns.asne.core.listener.OnRequestGetPostsCompleteListener;
import com.mapia.sns.model.SocialPost;

import java.util.ArrayList;

/**
 * Created by daehyun on 15. 6. 12..
 */
public class PostsListFragment extends Fragment implements OnRequestGetPostsCompleteListener {

    private static final String NETWORK_ID = "NETWORK_ID";
    private ListView listView;

    public static PostsListFragment newInstannce(int id) {
        PostsListFragment fragment = new PostsListFragment();
        Bundle args = new Bundle();
        args.putInt(NETWORK_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public PostsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        int networkId = getArguments().containsKey(NETWORK_ID) ? getArguments().getInt(NETWORK_ID) : 0;

        View rootView = inflater.inflate(R.layout.posts_list_fragment, container, false);

        listView = (ListView) rootView.findViewById(R.id.list);

        SocialNetwork socialNetwork = SNSFragment.mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.setOnRequestGetPostsCompleteListener(this);
        socialNetwork.requestGetPosts();
        SNSActivity.showProgress("Loading posts");

        return rootView;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void OnGetPostsIdComplete(int id, String[] friendsID) {
//        ((SNSActivity)getActivity()).getSupportActionBar().setTitle(friendsID.length + " Friends");
    }

    @Override
    public void OnGetPostsComplete(int networkID, ArrayList<SocialPost> socialPosts) {
        SNSActivity.hideProgress();
        PostsListAdapter adapter = new PostsListAdapter(getActivity(), socialPosts, networkID);
        listView.setAdapter(adapter);
    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        SNSActivity.hideProgress();
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }
}
