package com.aktivo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.ConnectionUtil;
import com.aktivo.Utils.Methods;
import com.aktivo.response.CommentDataPost;
import com.aktivo.response.CommentResponseMain;
import com.aktivo.response.Comments;
import com.aktivo.response.UserDetailTable;
import com.aktivo.webservices.WebApiClient;
import com.commonmodule.mi.utils.Validation;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SocialCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialCommentFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String POST_TYPE_KEY = "post_type_key";
    private static final String POSTION_KEY = "postin_key";
    private  int postion_value;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String post_type_value;

    private OnFragmentInteractionListener mListener;
    ListView lvCommentsList;
    SocialCommentListAdapter socialCommentListAdapter;
    EditText edtComment;
    ImageView iv_back;
    SimpleDraweeView sv_uesr_pic;
    TextView tv_post, tv_like;
    UserDetailTable userDetailTable;
    public String user_id,socialid;
    ArrayList<Comments> listcomment;
    public SocialCommentFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //* @param param1 Parameter 1.
     *
     * @return A new instance of fragment SocialCommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SocialCommentFragment newInstance(ArrayList<Comments> listcomment, String postid,String post_type,int postion) {
        SocialCommentFragment fragment = new SocialCommentFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, listcomment);
        args.putString(ARG_PARAM2, postid);
        args.putString(POST_TYPE_KEY, post_type);
        args.putInt(POSTION_KEY, postion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //    return inflater.inflate(R.layout.fragment_social_comment, container, false);
        View view = inflater.inflate(R.layout.fragment_social_comment, container, false);
        lvCommentsList = (ListView) view.findViewById(R.id.lvCommentsList);
        edtComment = (EditText) view.findViewById(R.id.etComment);
        tv_post = (TextView) view.findViewById(R.id.tvcommentPost);
        sv_uesr_pic = (SimpleDraweeView) view.findViewById(R.id.user_pic);
        tv_like = view.findViewById(R.id.tvTotalLike);
        iv_back = view.findViewById(R.id.iv_back);

        edtComment.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_post.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_like.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));

        listcomment=new ArrayList<>();
        socialCommentListAdapter=new SocialCommentListAdapter(mActivity,listcomment,"");
        lvCommentsList.setAdapter(socialCommentListAdapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBuandleValue();
        setHeader();
    }

    private void setHeader() {
        mActivity.setToolbarBottomVisibility(false);
    }

    private void getBuandleValue() {

        final Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(POST_TYPE_KEY)) {
                post_type_value=arguments.getString(POST_TYPE_KEY);
            }
            if (arguments.containsKey(POSTION_KEY)) {
                postion_value=arguments.getInt(POSTION_KEY);
            }
            if (arguments.containsKey(ARG_PARAM1)) {
              listcomment = arguments.getParcelableArrayList(ARG_PARAM1);
                Log.e("get list comment", listcomment + "");
                socialid = arguments.getString(ARG_PARAM2);
                socialCommentListAdapter.setDataUpdate(listcomment);

                if (listcomment.size()>1){
                    tv_like.setText(listcomment.size() +" "+"users have commented on this");
                }else {
                    tv_like.setText(listcomment.size() +" "+"user have commented on this");
                }
               // tv_like.setText(listcomment.size() +" "+"users have commented on this");


            }
        }

        userDetailTable = UserDetailTable.getUserDetail();
        if (userDetailTable != null) {
            if (Validation.isRequiredField(userDetailTable.get_id())) {
                user_id = userDetailTable.get_id();
            }
            if (Validation.isRequiredField(userDetailTable.getPhoto())) {
                sv_uesr_pic.setImageURI(userDetailTable.getPhoto());
            }
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.onBackPressed();
            }
        });
        tv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtComment.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Please enter comment",Toast.LENGTH_LONG).show();
                }else {
                    if(ConnectionUtil.isInternetAvailable(mActivity)){
                        callPostCommnetApi(user_id,socialid,edtComment.getText().toString());

                    }
                }

            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class SocialCommentListAdapter extends BaseAdapter {

        Context context;
        ArrayList<Comments> listcomment;
        String socialid;

        LayoutInflater inflater = null;
        Typeface tfSfUiDisReg, tfSfUiDisSemi, tfSegeoReg, tfSegeoSemi, tfSfTxtReg, tfSfTxtSemi;


        public SocialCommentListAdapter(Context mainActivity, ArrayList<Comments> listcomment, String socialid) {
            this.context = mainActivity;
            this.listcomment = listcomment;
            this.inflater = LayoutInflater.from(context);
            this.socialid = socialid;


            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setDataUpdate(ArrayList<Comments> listcomment){
            this.listcomment=listcomment;
            notifyDataSetChanged();

        }


        @Override
        public int getCount() {
            return listcomment.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class Holder {
            TextView tvName, tvComment, tvDate;
            SimpleDraweeView socialprofile_image;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = new Holder();
            View rowview = null;
            try {
                rowview = inflater.inflate(R.layout.layout_list_commentlist_items, null);

                holder.tvName = (TextView) rowview.findViewById(R.id.tvName);
                holder.tvComment = (TextView) rowview.findViewById(R.id.tvComment);
                holder.tvDate = (TextView) rowview.findViewById(R.id.tvDate);

                holder.tvName.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                holder.tvComment.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
                holder.tvDate.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
                holder.socialprofile_image = (SimpleDraweeView) rowview.findViewById(R.id.socialprofile_image);
                holder.tvName.setText(listcomment.get(position).getName());
                holder.tvComment.setText(listcomment.get(position).getComment());
                holder.tvDate.setText(listcomment.get(position).getCreated_at());
                holder.socialprofile_image.setImageURI(listcomment.get(position).getPhoto());


            } catch (Exception e) {
                //Help.systemLog(context,"Exception","DriverListAdapter"+e.getMessage());
            }
            return rowview;
        }
    }

    // call API for comment

    public void callPostCommnetApi(final String userid, final String socialid, final String comment) {
        Methods.isProgressShow(mActivity);
        WebApiClient.getInstance(mActivity).getWebApi().callPostCommentApi(userid, socialid, "Comment", comment).enqueue(new Callback<CommentResponseMain>() {
            @Override
            public void onResponse(Call<CommentResponseMain> call, Response<CommentResponseMain> response) {
                if (response != null) {
                    if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {

                        edtComment.setText("");
                        if(listcomment!=null && !listcomment.isEmpty()){
                            listcomment.clear();
                        }
                        List<CommentDataPost> data = response.body().getData();

                        for (int i = 0; i <data.size(); i++) {
                            List<Comments> comments = data.get(i).getComments();
                            for (int j = 0; j < comments.size(); j++) {
                                listcomment.add(comments.get(j));
                            }
                        }

                        if (listcomment.size()>1){
                            tv_like.setText(listcomment.size() +" "+"users have commented on this");
                        }else {
                            tv_like.setText(listcomment.size() +" "+"user have commented on this");
                        }

                        Intent intent=new Intent(CommonKeys.COMMENT_UPDATE_COUNT_BR);
                        intent.putExtra(CommonKeys.POSTTYPE,post_type_value);
                        intent.putExtra(CommonKeys.SOCIAL_POSTION,postion_value);
                        intent.putExtra(CommonKeys.TOTAL_COUNT,String.valueOf(""+listcomment.size()));

                        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                        socialCommentListAdapter.setDataUpdate(listcomment);

                    }
                }
                Methods.isProgressHide();
            }

            @Override
            public void onFailure(Call<CommentResponseMain> call, Throwable throwable) {
                Methods.isProgressHide();
                common_methods.setExceptionMessage(throwable, mActivity);
            }
        });
    }

}
