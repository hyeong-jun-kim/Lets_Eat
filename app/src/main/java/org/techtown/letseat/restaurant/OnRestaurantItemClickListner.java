package org.techtown.letseat.restaurant;

import android.view.View;

public interface OnRestaurantItemClickListner {
    public void OnItemClick(rest_recycle_adapter.ViewHolder holder, View view, int position);
}
