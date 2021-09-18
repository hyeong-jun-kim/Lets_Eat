package org.techtown.letseat.restaurant.list;

import android.view.View;

public interface OnRestaurantItemClickListner {
    public void OnItemClick(RestListAdapter.ViewHolder holder, View view, int position);
}
