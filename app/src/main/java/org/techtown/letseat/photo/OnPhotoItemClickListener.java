package org.techtown.letseat.photo;

import android.view.View;

public interface OnPhotoItemClickListener{
    public void onItemClick(PhotoRecyclerAdapter.ItemViewHolder holder, View view, int position);
}
