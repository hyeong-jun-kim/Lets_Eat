package org.techtown.letseat.photo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.letseat.R;

// 사진 탭에서 사진 클릭 시 나오는 플래그먼트
public class PhotoFragment extends Fragment {
    private ImageView photoView;
    private TextView title;
    private TextView review;
    private TextView sample_text;
    private Button cancelButton;
    private int resdId;
    private String title_text;
    private String review_text;
    PhotoList photoList;
    public View onCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_fragment, container, false);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.photo_fragment, container, false);
        photoView = view.findViewById(R.id.photo_view);
        title = view.findViewById(R.id.photo_title);
        review = view.findViewById(R.id.photo_review);
        photoView.setImageResource(resdId);
        title.setText(title_text);
        review.setText(review_text);
        photoList = PhotoList.photoList;
        PhotoFragment photoFragment = this;
        cancelButton = view.findViewById(R.id.photo_cancel);
        sample_text = view.findViewById(R.id.sample_text);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int resId = bundle.getInt("aP");
            sample_text.setText(resId);
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoList.check = false;
                getActivity().getSupportFragmentManager().beginTransaction().remove(photoFragment).commit();
            }
        });
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void setresId(int resId){
        this.resdId = resId;
    }
    public void setTitle(String text){
        title_text = text;
    }
    public void setReview(String text){
        review_text = text;
    }
}