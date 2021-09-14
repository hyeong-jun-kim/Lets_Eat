package org.techtown.letseat.photo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.letseat.R;
import org.techtown.letseat.menu.MenuData;

// 사진 탭에서 사진 클릭 시 나오는 플래그먼트
public class PhotoFragment extends Fragment {
    private ImageView photoView;
    private TextView title;
    private TextView review;
    public static Fragment newInstance(MenuData data){
        Fragment f = new PhotoFragment();
        Bundle b = new Bundle();
        b.putParcelable(MenuData.class.getName(), data);
        f.setArguments(b);
        return f;
    }
    public View onCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_fragment, container, false);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_fragment, container, false);
        photoView = view.findViewById(R.id.photo_view);
        title = view.findViewById(R.id.photo_title);
        review = view.findViewById(R.id.photo_review);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void setPhotoView(int resId){
        photoView.setImageResource(resId);
    }
    public void setTitle(String text){
        title.setText(text);
    }
    public void setReview(String text){
        title.setText(text);
    }
}