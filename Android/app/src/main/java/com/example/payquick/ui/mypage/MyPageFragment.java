package com.example.payquick.ui.mypage;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payquick.BarcodeActivity;
import com.example.payquick.LoginActivity;
import com.example.payquick.MainActivity;
import com.example.payquick.MyAccountActivity;
import com.example.payquick.MyMembershipActivity;
import com.example.payquick.MyOrdersActivity;
import com.example.payquick.PaymentCartActivity;
import com.example.payquick.R;
import com.example.payquick.ShoplistActivity;
import com.example.payquick.data.LoginResponse;
import com.example.payquick.data.MyPageData;
import com.example.payquick.data.ProductData;
import com.example.payquick.ui.product.ProductListAdapter;

import java.util.Arrays;
import java.util.List;

public class MyPageFragment extends Fragment implements MyPageAdapter.OnListItemSelectedInterface{
    private MyPageViewModel myPageViewModel;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyPageAdapter m_adapter;
    MyOrdersFragment moFragment;
    MyAccountFragment maFragment;
    TextView nameTextView;

    Context context;
    SharedPreferences storeToken;
    String nameToken;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        /*
        myPageViewModel =
                ViewModelProviders.of(this).get(MyPageViewModel.class);

        View root = inflater.inflate(R.layout.fragment_mypage, container, false);


        final TextView textView = root.findViewById(R.id.text_mypage);
        myPageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
        View root = inflater.inflate(R.layout.fragment_mypage, container, false);
        //ImageView profileImageView = (ImageView) root.findViewById(R.id.img_profile);

        context = getActivity();
        storeToken = context.getSharedPreferences("store", Context.MODE_PRIVATE);

        // 프로필 이름 표시
        nameToken = storeToken.getString("name", null);
        if (nameToken != null) {
            nameTextView = (TextView) root.findViewById(R.id.name_profile);
            nameTextView.setText(nameToken + " 님");
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = root.findViewById(R.id.myPageRecyclerView) ;
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        m_adapter = new MyPageAdapter(this);
        recyclerView.setAdapter(m_adapter);
        getData();
        return root;
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList(" Membership", " Orders", " Account", " Log Out");

        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            MyPageData data = new MyPageData();
            data.setTitle(listTitle.get(i));
            m_adapter.addItem(data);
        }
        m_adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(View v, int position) {
        MyPageAdapter.ItemViewHolder viewHolder = (MyPageAdapter.ItemViewHolder) recyclerView.findViewHolderForAdapterPosition(position);

        if (position == 0) {
            Intent intent = new Intent(getActivity(), MyMembershipActivity.class);
            startActivity(intent);
        } else if(position == 1) {
            /*
            // manager 와 transaction 초기화
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            moFragment = new MyOrdersFragment();
            // 전달받은 fragment 를 replace
            transaction.replace(R.id.nav_host_fragment, moFragment);
            // 해당 transaction 을 Back Stack 에 저장
            transaction.addToBackStack(null);
            // transaction 마무리
            transaction.commit();
             */
            Intent intent = new Intent(getActivity(), MyOrdersActivity.class);
            startActivity(intent);

        } else if(position == 2) {
            Intent intent = new Intent(getActivity(), MyAccountActivity.class);
            startActivity(intent);
        } else if (position == 3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("로그아웃");
            builder.setMessage("로그아웃하시겠습니까?");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Context context = getActivity();

                    //storeToken = context.getSharedPreferences("store", Context.MODE_PRIVATE);
                    //SharedPreferences storeToken = context.getSharedPreferences("store", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = storeToken.edit();
                    //editor.clear()는 storeToken에 들어있는 모든 정보를 기기에서 지웁니다.
                    editor.clear();
                    editor.commit();

                    //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    //fragmentManager.beginTransaction().remove(MyPageFragment.this).commit();
                    //fragmentManager.popBackStack();

                    getActivity().finish();
                    //System.exit(0);
                    //android.os.Process.killProcess(android.os.Process.myPid());

                    // 이전 액티비티 모두 종료 참고 : https://minyoungjung.github.io/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C/2017/07/12/android-activity-close/
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    /*
                    Intent i = new Intent(context, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                     */

                    Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("취소",  null);
            builder.create().show();
        }
    }
}
